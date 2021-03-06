package com.ancestor.modules.system.repository;

import com.ancestor.modules.system.domain.UserAvatar;
import com.ancestor.modules.system.domain.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
public interface UserAvatarRepository extends JpaRepository<UserAvatar, Long>, JpaSpecificationExecutor<UserAvatar> {

}
