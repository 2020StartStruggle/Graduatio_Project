package com.ancestor.modules.ancestor.person.service.dto;

import lombok.Data;
import java.io.Serializable;


/**
* @author zhanghouying
* @date 2019-11-18
*/
@Data
public class PersonDTO implements Serializable {

    // 编号
    private Integer id;

    // 姓名
    private String name;

    // 妻子
    private String wife;

    // 父编号
    private Integer parentId;

    // 辈分
    private Integer levelId;

    // 居住地编号
    private Integer liveId;

    // 确定
    private String isSure;

    // 备注
    private String remark;

    // 生于
    private String born;

    // 卒于
    private String dead;
}
