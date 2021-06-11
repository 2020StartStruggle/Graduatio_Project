package com.ancestor.modules.ancestor.person.service;

import com.ancestor.modules.ancestor.person.domain.Person;
import com.ancestor.modules.ancestor.person.service.dto.PersonDTO;
import com.ancestor.modules.ancestor.person.service.dto.PersonQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author zhanghouying
* @date 2019-11-18
*/
public interface PersonService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(PersonQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<PersonDTO>
    */
    List<PersonDTO> queryAll(PersonQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return PersonDTO
     */
    PersonDTO findById(Integer id);

	/**
	 * 根据父编号查找儿子
	 * @param parentId
	 * @return
	 */
	public List<Person> findByParentId(Integer parentId);

	public List<Map<String,Object>> getPersonTree(List<Person> personList);

    PersonDTO create(Person resources);

    void update(Person resources);

    void delete(Integer id);

    void download(List<PersonDTO> all, HttpServletResponse response) throws IOException;

	List<Map<String,Object>> getDisplayMap(List<PersonDTO> personList,int pId);
}
