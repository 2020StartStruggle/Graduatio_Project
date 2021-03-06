package com.ancestor.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ancestor.modules.system.domain.Dict;
import com.ancestor.modules.system.service.dto.DictDetailDTO;
import com.ancestor.modules.system.service.dto.DictQueryCriteria;
import com.ancestor.utils.FileUtil;
import com.ancestor.utils.PageUtil;
import com.ancestor.utils.QueryHelp;
import com.ancestor.utils.ValidationUtil;
import com.ancestor.modules.system.repository.DictRepository;
import com.ancestor.modules.system.service.DictService;
import com.ancestor.modules.system.service.dto.DictDTO;
import com.ancestor.modules.system.service.mapper.DictMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@Service
@CacheConfig(cacheNames = "dict")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl implements DictService {

    private final DictRepository dictRepository;

    private final DictMapper dictMapper;

    public DictServiceImpl(DictRepository dictRepository, DictMapper dictMapper) {
        this.dictRepository = dictRepository;
        this.dictMapper = dictMapper;
    }

    @Override
    @Cacheable
    public Map<String, Object> queryAll(DictQueryCriteria dict, Pageable pageable){
        Page<Dict> page = dictRepository.findAll((root, query, cb) -> QueryHelp.getPredicate(root, dict, cb), pageable);
        return PageUtil.toPage(page.map(dictMapper::toDto));
    }

    @Override
    public List<DictDTO> queryAll(DictQueryCriteria dict) {
        List<Dict> list = dictRepository.findAll((root, query, cb) -> QueryHelp.getPredicate(root, dict, cb));
        return dictMapper.toDto(list);
    }

    @Override
    @Cacheable(key = "#p0")
    public DictDTO findById(Long id) {
        Dict dict = dictRepository.findById(id).orElseGet(Dict::new);
        ValidationUtil.isNull(dict.getId(),"Dict","id",id);
        return dictMapper.toDto(dict);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public DictDTO create(Dict resources) {
        return dictMapper.toDto(dictRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Dict resources) {
        Dict dict = dictRepository.findById(resources.getId()).orElseGet(Dict::new);
        ValidationUtil.isNull( dict.getId(),"Dict","id",resources.getId());
        resources.setId(dict.getId());
        dictRepository.save(resources);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        dictRepository.deleteById(id);
    }

    @Override
    public void download(List<DictDTO> dictDTOS, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DictDTO dictDTO : dictDTOS) {
            if(CollectionUtil.isNotEmpty(dictDTO.getDictDetails())){
                for (DictDetailDTO dictDetail : dictDTO.getDictDetails()) {
                    Map<String,Object> map = new LinkedHashMap<>();
                    map.put("????????????", dictDTO.getName());
                    map.put("????????????", dictDTO.getRemark());
                    map.put("????????????", dictDetail.getLabel());
                    map.put("?????????", dictDetail.getValue());
                    map.put("????????????", dictDetail.getCreateTime());
                    list.add(map);
                }
            } else {
                Map<String,Object> map = new LinkedHashMap<>();
                map.put("????????????", dictDTO.getName());
                map.put("????????????", dictDTO.getRemark());
                map.put("????????????", null);
                map.put("?????????", null);
                map.put("????????????", dictDTO.getCreateTime());
                list.add(map);
            }
        }
        FileUtil.downloadExcel(list, response);
    }
}
