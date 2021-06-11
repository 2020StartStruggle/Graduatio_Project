package com.ancestor.modules.ancestor.live.service.impl;

import com.ancestor.modules.ancestor.live.domain.Live;
import com.ancestor.utils.ValidationUtil;
import com.ancestor.utils.FileUtil;
import com.ancestor.modules.ancestor.live.repository.LiveRepository;
import com.ancestor.modules.ancestor.live.service.LiveService;
import com.ancestor.modules.ancestor.live.service.dto.LiveDTO;
import com.ancestor.modules.ancestor.live.service.dto.LiveQueryCriteria;
import com.ancestor.modules.ancestor.live.service.mapper.LiveMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ancestor.utils.PageUtil;
import com.ancestor.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author zhanghouying
* @date 2019-11-18
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LiveServiceImpl implements LiveService {

    private final LiveRepository liveRepository;

    private final LiveMapper liveMapper;

    public LiveServiceImpl(LiveRepository liveRepository, LiveMapper liveMapper) {
        this.liveRepository = liveRepository;
        this.liveMapper = liveMapper;
    }

    @Override
    public Map<String,Object> queryAll(LiveQueryCriteria criteria, Pageable pageable){
        Page<Live> page = liveRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(liveMapper::toDto));
    }

    @Override
    public List<LiveDTO> queryAll(LiveQueryCriteria criteria){
        return liveMapper.toDto(liveRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public LiveDTO findById(Integer id) {
        Live live = liveRepository.findById(id).orElseGet(Live::new);
        ValidationUtil.isNull(live.getId(),"Live","id",id);
        return liveMapper.toDto(live);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LiveDTO create(Live resources) {
        return liveMapper.toDto(liveRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Live resources) {
        Live live = liveRepository.findById(resources.getId()).orElseGet(Live::new);
        ValidationUtil.isNull( live.getId(),"Live","id",resources.getId());
        live.copy(resources);
        liveRepository.save(live);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        liveRepository.deleteById(id);
    }


    @Override
    public void download(List<LiveDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LiveDTO live : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("地名", live.getName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
