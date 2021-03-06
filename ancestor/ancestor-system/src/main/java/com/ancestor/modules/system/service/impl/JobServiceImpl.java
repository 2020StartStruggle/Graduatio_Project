package com.ancestor.modules.system.service.impl;

import com.ancestor.modules.system.domain.Job;
import com.ancestor.modules.system.repository.DeptRepository;
import com.ancestor.modules.system.service.dto.JobQueryCriteria;
import com.ancestor.utils.FileUtil;
import com.ancestor.utils.PageUtil;
import com.ancestor.utils.QueryHelp;
import com.ancestor.utils.ValidationUtil;
import com.ancestor.modules.system.repository.JobRepository;
import com.ancestor.modules.system.service.JobService;
import com.ancestor.modules.system.service.dto.JobDTO;
import com.ancestor.modules.system.service.mapper.JobMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
@Service
@CacheConfig(cacheNames = "job")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    private final JobMapper jobMapper;

    private final DeptRepository deptRepository;

    public JobServiceImpl(JobRepository jobRepository, JobMapper jobMapper, DeptRepository deptRepository) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.deptRepository = deptRepository;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(JobQueryCriteria criteria, Pageable pageable) {
        Page<Job> page = jobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        List<JobDTO> jobs = new ArrayList<>();
        for (Job job : page.getContent()) {
            jobs.add(jobMapper.toDto(job,deptRepository.findNameById(job.getDept().getPid())));
        }
        return PageUtil.toPage(jobs,page.getTotalElements());
    }

    @Override
    @Cacheable
    public List<JobDTO> queryAll(JobQueryCriteria criteria) {
        List<Job> list = jobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        return jobMapper.toDto(list);
    }

    @Override
    @Cacheable(key = "#p0")
    public JobDTO findById(Long id) {
        Job job = jobRepository.findById(id).orElseGet(Job::new);
        ValidationUtil.isNull(job.getId(),"Job","id",id);
        return jobMapper.toDto(job);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public JobDTO create(Job resources) {
        return jobMapper.toDto(jobRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Job resources) {
        Job job = jobRepository.findById(resources.getId()).orElseGet(Job::new);
        ValidationUtil.isNull( job.getId(),"Job","id",resources.getId());
        resources.setId(job.getId());
        jobRepository.save(resources);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        jobRepository.deleteById(id);
    }

    @Override
    public void download(List<JobDTO> jobDTOs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (JobDTO jobDTO : jobDTOs) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("????????????", jobDTO.getName());
            map.put("????????????", jobDTO.getDept().getName());
            map.put("????????????", jobDTO.getEnabled() ? "??????" : "??????");
            map.put("????????????", jobDTO.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
