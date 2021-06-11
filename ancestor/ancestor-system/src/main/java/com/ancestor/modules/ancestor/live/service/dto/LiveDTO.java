package com.ancestor.modules.ancestor.live.service.dto;

import lombok.Data;
import java.io.Serializable;


/**
* @author zhanghouying
* @date 2019-11-18
*/
@Data
public class LiveDTO implements Serializable {

    // 编号
    private Integer id;

    // 地名
    private String name;
}