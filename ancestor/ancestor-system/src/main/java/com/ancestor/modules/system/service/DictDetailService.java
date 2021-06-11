package com.ancestor.modules.system.service;

import com.ancestor.modules.system.domain.DictDetail;
import com.ancestor.modules.system.domain.DictDetail;
import com.ancestor.modules.system.service.dto.DictDetailDTO;
import com.ancestor.modules.system.service.dto.DictDetailQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
public interface DictDetailService {

    DictDetailDTO findById(Long id);

    DictDetailDTO create(DictDetail resources);

    void update(DictDetail resources);

    void delete(Long id);

    Map queryAll(DictDetailQueryCriteria criteria, Pageable pageable);
}
