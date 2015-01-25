package com.dapeng.repository;

import com.dapeng.domain.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

	UserPermission findOneByUserId(Long userId);
}
