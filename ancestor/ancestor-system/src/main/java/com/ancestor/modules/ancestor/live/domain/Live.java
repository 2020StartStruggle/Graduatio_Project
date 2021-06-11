package com.ancestor.modules.ancestor.live.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author zhanghouying
* @date 2019-11-18
*/
@Entity
@Data
@Table(name="ancestor_live")
public class Live implements Serializable {

    // 编号
    @Id
    @Column(name = "id")
    private Integer id;

    // 地名
    @Column(name = "name")
    private String name;

    public void copy(Live source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}