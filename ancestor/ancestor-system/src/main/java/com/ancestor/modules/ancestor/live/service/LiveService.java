package com.ancestor.modules.ancestor.live.service;

import com.ancestor.modules.ancestor.live.domain.Live;
import com.ancestor.modules.ancestor.live.service.dto.LiveDTO;
import com.ancestor.modules.ancestor.live.service.dto.LiveQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author zhanghouying
* @date 2019-11-18
*/
public interface LiveService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(LiveQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<LiveDTO>
    */
    List<LiveDTO> queryAll(LiveQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return LiveDTO
     */
    LiveDTO findById(Integer id);

    LiveDTO create(Live resources);

    void update(Live resources);

    void delete(Integer id);

    void download(List<LiveDTO> all, HttpServletResponse response) throws IOException;
}