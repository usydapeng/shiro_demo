package com.dapeng.service;

import com.dapeng.service.exception.UserAccountException;

public interface UserService {

	SimpleUserInfo getAuthorizationByUsername(String username) throws UserAccountException;

	void init();
}
