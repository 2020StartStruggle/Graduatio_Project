package com.ancestor.modules.ancestor.person.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
* @author zhanghouying
* @date 2019-11-18
*/
@Entity
@Data
@Table(name="ancestor_person")
public class Person implements Serializable {

	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

	/**
	 * 姓名
	 */
	@Column(name = "name")
    private String name;

	/**
	 * 妻子
	 */
	@Column(name = "wife")
    private String wife;

	/**
	 * 父编号
	 */
	@Column(name = "parent_id")
    private Integer parentId;

	/**
	 * 辈分
	 */
	@Column(name = "level_id")
    private Integer levelId;

	/**
	 * 居住地编号
	 */
	@Column(name = "live_id")
    private Integer liveId;

	/**
	 * 确定
	 */
	@Column(name = "is_sure")
    private String isSure;

	/**
	 * 备注
	 */
	@Column(name = "remark")
    private String remark;

	/**
	 * 生于
	 */
	@Column(name = "born")
    private String born;

	/**
	 * 卒于
	 */
	@Column(name = "dead")
    private String dead;

	@Transient
	private List<Person> children;

    public void copy(Person source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
