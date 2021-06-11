package com.ancestor.modules.ancestor.live.service.mapper;

import com.ancestor.base.BaseMapper;
import com.ancestor.modules.ancestor.live.domain.Live;
import com.ancestor.modules.ancestor.live.service.dto.LiveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author zhanghouying
* @date 2019-11-18
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LiveMapper extends BaseMapper<LiveDTO, Live> {

}
