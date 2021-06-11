package com.ancestor.modules.ancestor.person.service.dto;

import lombok.Data;
import com.ancestor.annotation.Query;

/**
* @author zhanghouying
* @date 2019-11-18
*/
@Data
public class PersonQueryCriteria{

	/**
	 * 模糊
	 */
	@Query(type = Query.Type.INNER_LIKE)
    private String name;
}
