package com.dapeng.dao;

import com.dapeng.domain.UserPermission;

import java.util.List;

public interface UserPermissionDAO extends HibernateDAO<UserPermission, Long> {

	List<UserPermission> findAllByUserId(Long userId);
}
