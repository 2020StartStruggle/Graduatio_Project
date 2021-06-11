package com.ancestor.modules.system.service.mapper;

import com.ancestor.base.BaseMapper;
import com.ancestor.modules.system.domain.Dict;
import com.ancestor.modules.system.service.dto.DictDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictMapper extends BaseMapper<DictDTO, Dict> {

}
