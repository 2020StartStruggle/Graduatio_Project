package com.ancestor.modules.ancestor.person.rest;

import com.ancestor.aop.log.Log;
import com.ancestor.modules.ancestor.person.domain.Person;
import com.ancestor.modules.ancestor.person.service.PersonService;
import com.ancestor.modules.ancestor.person.service.dto.PersonQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author zhanghouying
* @date 2019-11-18
*/
@Api(tags = "Person管理")
@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('person:list')")
    public void download(HttpServletResponse response, PersonQueryCriteria criteria) throws IOException {
        personService.download(personService.queryAll(criteria), response);
    }

	@ApiOperation("返回全部的人")
	@GetMapping(value = "/tree")
	@PreAuthorize("@el.check('person:list')")
	public ResponseEntity getPersonListTree(){
		return new ResponseEntity<>(personService.getPersonTree(personService.findByParentId(-1)),HttpStatus.OK);
	}

	@ApiOperation("返回全部的人用于显示格式")
	@GetMapping(value = "/display")
	@PreAuthorize("@el.check('person:list')")
	public ResponseEntity getDisplay(){
		List<Map<String, Object>> resultList = personService.getDisplayMap(personService.queryAll(new PersonQueryCriteria()),-1);
		if (resultList.size() == 1) {
			return new ResponseEntity<>(resultList.get(0), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}


	@GetMapping(value = "/{id}")
	@Log("查询单个Person")
	@ApiOperation("查询单个Person")
	@PreAuthorize("@el.check('person:list')")
	public ResponseEntity getPerson(@PathVariable Integer id){
		return new ResponseEntity<>(personService.findById(id),HttpStatus.OK);
	}

	@GetMapping
    @Log("查询Person")
    @ApiOperation("查询Person")
    @PreAuthorize("@el.check('person:list')")
    public ResponseEntity getPersons(PersonQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(personService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Person")
    @ApiOperation("新增Person")
    @PreAuthorize("@el.check('person:add')")
    public ResponseEntity create(@Validated @RequestBody Person resources){
        return new ResponseEntity<>(personService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Person")
    @ApiOperation("修改Person")
    @PreAuthorize("@el.check('person:edit')")
    public ResponseEntity update(@Validated @RequestBody Person resources){
        personService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除Person")
    @ApiOperation("删除Person")
    @PreAuthorize("@el.check('person:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        personService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
