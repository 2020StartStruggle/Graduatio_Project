package com.ancestor.modules.ancestor.live.rest;

import com.ancestor.aop.log.Log;
import com.ancestor.modules.ancestor.live.domain.Live;
import com.ancestor.modules.ancestor.live.service.LiveService;
import com.ancestor.modules.ancestor.live.service.dto.LiveQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author zhanghouying
* @date 2019-11-18
*/
@Api(tags = "Live管理")
@RestController
@RequestMapping("/api/live")
public class LiveController {

    private final LiveService liveService;

    public LiveController(LiveService liveService) {
        this.liveService = liveService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('live:list')")
    public void download(HttpServletResponse response, LiveQueryCriteria criteria) throws IOException {
        liveService.download(liveService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Live")
    @ApiOperation("查询Live")
    @PreAuthorize("@el.check('live:list')")
    public ResponseEntity getLives(LiveQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(liveService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Live")
    @ApiOperation("新增Live")
    @PreAuthorize("@el.check('live:add')")
    public ResponseEntity create(@Validated @RequestBody Live resources){
        return new ResponseEntity<>(liveService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Live")
    @ApiOperation("修改Live")
    @PreAuthorize("@el.check('live:edit')")
    public ResponseEntity update(@Validated @RequestBody Live resources){
        liveService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除Live")
    @ApiOperation("删除Live")
    @PreAuthorize("@el.check('live:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        liveService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
