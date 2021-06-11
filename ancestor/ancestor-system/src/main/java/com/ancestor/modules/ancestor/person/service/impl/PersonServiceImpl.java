package com.ancestor.modules.ancestor.person.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ancestor.modules.ancestor.live.domain.Live;
import com.ancestor.modules.ancestor.live.repository.LiveRepository;
import com.ancestor.modules.ancestor.person.domain.Person;
import com.ancestor.modules.ancestor.person.repository.PersonRepository;
import com.ancestor.modules.ancestor.person.service.PersonService;
import com.ancestor.modules.ancestor.person.service.dto.PersonDTO;
import com.ancestor.modules.ancestor.person.service.dto.PersonQueryCriteria;
import com.ancestor.modules.ancestor.person.service.mapper.PersonMapper;
import com.ancestor.utils.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
* @author zhanghouying
* @date 2019-11-18
*/
@Service
@CacheConfig(cacheNames = "person")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    private final LiveRepository liveRepository;

    public PersonServiceImpl(PersonRepository personRepository, LiveRepository liveRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.liveRepository = liveRepository;
    }

    @Override
    public Map<String,Object> queryAll(PersonQueryCriteria criteria, Pageable pageable){
        Page<Person> page = personRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(personMapper::toDto));
    }

    @Override
    public List<PersonDTO> queryAll(PersonQueryCriteria criteria){
        return personMapper.toDto(personRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder), Sort.by(Sort.Direction.ASC,"id")));
    }

    @Override
    public PersonDTO findById(Integer id) {
        Person person = personRepository.findById(id).get();
        ValidationUtil.isNull(person.getId(),"Person","id",id);
        return personMapper.toDto(person);
    }


	@Override
	public List<Person> findByParentId(Integer parentId) {
		return personRepository.findByParentId(parentId);
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonDTO create(Person resources) {
        return personMapper.toDto(personRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Person resources) {
        Person person = personRepository.findById(resources.getId()).orElseGet(Person::new);
        ValidationUtil.isNull( person.getId(),"Person","id",resources.getId());
        person.copy(resources);
        personRepository.save(person);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        personRepository.deleteById(id);
    }


    @Override
    public void download(List<PersonDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PersonDTO person : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("姓名", person.getName());
            map.put("妻子", person.getWife());
            map.put("父编号", person.getParentId());
            map.put("辈分", person.getLevelId());
            map.put("居住地编号", person.getLiveId());
            map.put("确定", person.getIsSure());
            map.put("备注", person.getRemark());
            map.put("生于", person.getBorn());
            map.put("卒于", person.getDead());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

	@Override
	public List<Map<String,Object>> getDisplayMap(List<PersonDTO> personList,int pId) {
		List<Map<String,Object>> list = new LinkedList<>();
		personList.forEach(person -> {
			if (person != null && person.getParentId() == pId) {
				Map<String,Object> map = new HashMap<>();
				map.put("name", person.getName());
				if (StrUtil.isNotEmpty(person.getWife())) {
					map.put("mate", ImmutableMap.of("name",person.getWife()));
				}
				List<Map<String, Object>> sonListMap = getDisplayMap(personList,person.getId());
				if (sonListMap.size() > 0) {
					map.put("children", sonListMap);
				}
				list.add(map);
			}
		});
		Collections.reverse(list);
		return list;
	}

	@Override
	public List<Map<String,Object>> getPersonTree(List<Person> personList) {
		List<Map<String,Object>> list = new LinkedList<>();
		personList.forEach(person -> {
					if (person!=null){
						List<Person> sonList = personRepository.findByParentId(person.getId());
						Map<String,Object> map = new HashMap<>();
						map.put("id",person.getId());
						map.put("name", person.getName());
						map.put("parentId", person.getParentId());
						map.put("liveId", person.getLiveId());
						map.put("levelId", person.getLevelId());
						map.put("label",renderLabel( person));
						if (sonList != null && sonList.size() != 0) {
							map.put("children", getPersonTree(sonList));
							map.put("icon", "el-icon-user-solid");
						} else {
							map.put("icon","el-icon-user");
						}
						list.add(map);
					}
				}
		);
		Collections.reverse(list);
		return list;
	}

	private String renderLabel(Person person) {
		StringBuilder sb = new StringBuilder();
		Live live = liveRepository.findById(person.getLiveId()).get();
		sb.append(person.getName()).append(" ").append(person.getWife()).append(" ").append(live.getName()).append(" ").append(renderLevel(person.getLevelId()));
		return sb.toString();
	}
	private String renderLevel(int level) {
		String[] names = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十","二十一"};
		StringBuilder sb = new StringBuilder("第");
		sb.append(names[level-1]);
		sb.append("世");
		return sb.toString();
	}
}
