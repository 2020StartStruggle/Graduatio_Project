package com.ancestor.modules.ancestor.person.service.mapper;

import com.ancestor.base.BaseMapper;
import com.ancestor.modules.ancestor.person.domain.Person;
import com.ancestor.modules.ancestor.person.service.dto.PersonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author zhanghouying
* @date 2019-11-18
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonMapper extends BaseMapper<PersonDTO, Person> {

}
