package com.ancestor.modules.ancestor.person.repository;

import com.ancestor.modules.ancestor.person.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
* @author zhanghouying
* @date 2019-11-18
*/
public interface PersonRepository extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person> {

	/**
	 * 根据父编号查找儿子节点
	 * @param parentId
	 * @return
	 */
	public List<Person> findByParentId(int parentId);

}
