package com.ancestor.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.ancestor.domain.Log;
import com.ancestor.repository.LogRepository;
import com.ancestor.service.LogService;
import com.ancestor.service.dto.LogQueryCriteria;
import com.ancestor.domain.Log;
import com.ancestor.repository.LogRepository;
import com.ancestor.service.LogService;
import com.ancestor.service.dto.LogQueryCriteria;
import com.ancestor.service.mapper.LogErrorMapper;
import com.ancestor.service.mapper.LogSmallMapper;
import com.ancestor.utils.FileUtil;
import com.ancestor.utils.PageUtil;
import com.ancestor.utils.QueryHelp;
import com.ancestor.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    private final LogErrorMapper logErrorMapper;

    private final LogSmallMapper logSmallMapper;

    public LogServiceImpl(LogRepository logRepository, LogErrorMapper logErrorMapper, LogSmallMapper logSmallMapper) {
        this.logRepository = logRepository;
        this.logErrorMapper = logErrorMapper;
        this.logSmallMapper = logSmallMapper;
    }

    @Override
    public Object queryAll(LogQueryCriteria criteria, Pageable pageable){
        Page<Log> page = logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)),pageable);
        if ("ERROR".equals(criteria.getLogType())) {
            return PageUtil.toPage(page.map(logErrorMapper::toDto));
        }
        return page;
    }

    @Override
    public List<Log> queryAll(LogQueryCriteria criteria) {
        return logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)));
    }

    @Override
    public Object queryAllByUser(LogQueryCriteria criteria, Pageable pageable) {
        Page<Log> page = logRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)),pageable);
        return PageUtil.toPage(page.map(logSmallMapper::toDto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, Log log){

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.ancestor.aop.log.Log aopLog = method.getAnnotation(com.ancestor.aop.log.Log.class);

        // ????????????
        String methodName = joinPoint.getTarget().getClass().getName()+"."+signature.getName()+"()";

        StringBuilder params = new StringBuilder("{");
        //?????????
        Object[] argValues = joinPoint.getArgs();
        //????????????
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        if(argValues != null){
            for (int i = 0; i < argValues.length; i++) {
                params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
            }
        }
        // ??????
        if (log != null) {
            log.setDescription(aopLog.value());
        }
        assert log != null;
        log.setRequestIp(ip);

        String LOGINPATH = "login";
        if(LOGINPATH.equals(signature.getName())){
            try {
                assert argValues != null;
                username = new JSONObject(argValues[0]).get("username").toString();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.setAddress(StringUtils.getCityInfo(log.getRequestIp()));
        log.setMethod(methodName);
        log.setUsername(username);
        log.setParams(params.toString() + " }");
        log.setBrowser(browser);
        logRepository.save(log);
    }

    @Override
    public Object findByErrDetail(Long id) {
        byte[] details = logRepository.findExceptionById(id).getExceptionDetail();
        return Dict.create().set("exception",new String(ObjectUtil.isNotNull(details) ? details : "".getBytes()));
    }

    @Override
    public void download(List<Log> logs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Log log : logs) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("?????????", log.getUsername());
            map.put("IP", log.getRequestIp());
            map.put("IP??????", log.getAddress());
            map.put("??????", log.getDescription());
            map.put("?????????", log.getBrowser());
            map.put("????????????/??????", log.getTime());
            map.put("????????????", new String(ObjectUtil.isNotNull(log.getExceptionDetail()) ? log.getExceptionDetail() : "".getBytes()));
            map.put("????????????", log.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
