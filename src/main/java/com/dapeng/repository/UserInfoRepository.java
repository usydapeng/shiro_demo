package com.dapeng.repository;

import com.dapeng.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

	UserInfo findOneByUserId(Long id);
}
