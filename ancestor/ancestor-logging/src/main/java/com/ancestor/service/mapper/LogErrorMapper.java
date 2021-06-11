package com.ancestor.service.mapper;

import com.ancestor.domain.Log;
import com.ancestor.service.dto.LogErrorDTO;
import com.ancestor.base.BaseMapper;
import com.ancestor.domain.Log;
import com.ancestor.service.dto.LogErrorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Zheng Jie
 * @date 2019-5-22
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogErrorMapper extends BaseMapper<LogErrorDTO, Log> {

}
