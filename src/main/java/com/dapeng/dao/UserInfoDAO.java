package com.dapeng.dao;

import com.dapeng.domain.UserInfo;

public interface UserInfoDAO extends HibernateDAO<UserInfo, Long> {

	UserInfo findByUserId(Long userId);
}
