package com.dapeng.service;

import com.dapeng.repository.UserAccountRepository;
import com.dapeng.repository.UserInfoRepository;
import com.dapeng.repository.UserPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private UserPermissionRepository userPermissionRepository;
}
