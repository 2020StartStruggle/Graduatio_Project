package com.ancestor.service.mapper;

import com.ancestor.domain.LocalStorage;
import com.ancestor.service.dto.LocalStorageDTO;
import com.ancestor.base.BaseMapper;
import com.ancestor.domain.LocalStorage;
import com.ancestor.service.dto.LocalStorageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author Zheng Jie
* @date 2019-09-05
*/
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocalStorageMapper extends BaseMapper<LocalStorageDTO, LocalStorage> {

}
