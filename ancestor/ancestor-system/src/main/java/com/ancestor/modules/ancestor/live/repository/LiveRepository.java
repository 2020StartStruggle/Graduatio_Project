package com.ancestor.modules.ancestor.live.repository;

import com.ancestor.modules.ancestor.live.domain.Live;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author zhanghouying
* @date 2019-11-18
*/
public interface LiveRepository extends JpaRepository<Live, Integer>, JpaSpecificationExecutor<Live> {
}