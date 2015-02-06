package com.dapeng.service;

import com.dapeng.dao.SlugInfoDAO;
import com.dapeng.dao.UserAccountDAO;
import com.dapeng.dao.UserInfoDAO;
import com.dapeng.dao.UserPermissionDAO;
import com.dapeng.domain.UserAccount;
import com.dapeng.domain.UserInfo;
import com.dapeng.domain.UserPermission;
import com.dapeng.service.exception.UserAccountException;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserInfoDAO userInfoDAO;

	@Autowired
	private UserAccountDAO userAccountDAO;

	@Autowired
	private UserPermissionDAO userPermissionDAO;

	@Autowired
	private SlugInfoDAO slugInfoDAO;

	@Override
	public SimpleUserInfo getByUsername(String username) throws UserAccountException {
		UserAccount userAccount = userAccountDAO.findByUsername(username);
		if(userAccount == null){
			throw new UserAccountException("this account doesn't exist");
		}
		UserInfo userInfo = userInfoDAO.findByUserId(userAccount.getId());

		SimpleUserInfo simpleUserInfo = new SimpleUserInfo();

		simpleUserInfo.setSlug(userAccount.getSlug());
		simpleUserInfo.setUserId(userAccount.getId());
		simpleUserInfo.setSchool(userInfo.getSchool());
		simpleUserInfo.setNickname(userInfo.getNickname());
		simpleUserInfo.setPassword(userAccount.getPassword());
		simpleUserInfo.setUsername(userAccount.getUsername());
		simpleUserInfo.setUserRole(userAccount.getUserRole());

		List<UserPermission> userPermissionList = userPermissionDAO.findAllByUserId(userAccount.getId());
		List<String> permissionStrList = Lists.newArrayList();
		for(UserPermission userPermission : userPermissionList){
			permissionStrList.add(userPermission.getPermission());
		}
		simpleUserInfo.setUserPermissionList(permissionStrList);

		return simpleUserInfo;
	}

	@Override
	public void init() {
		UserAccount userAccount = new UserAccount();
		userAccount.setUsername("admin");
		userAccount.setPassword(new Md5PasswordEncoder().encodePassword("admin123", "admin"));
		userAccount.setUserRole(UserAccount.Role.ROLE_USER.getId() + UserAccount.Role.ROLE_EDITOR.getId());
		userAccountDAO.save(userAccount);

		userAccount.setSlug(slugInfoDAO.findById(userAccount.getId()).getSlug());

		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(userAccount.getId());
		userInfo.setNickname("张三三");
		userInfo.setSchool("北京第三完小");
		userInfo.setGender(UserInfo.Gender.MALE.getId());
		userInfoDAO.save(userInfo);

		UserPermission userPermission = new UserPermission();
		userPermission.setUserId(userAccount.getId());
		userPermission.setCreateTime(new Date());
		userPermission.setPermission("hellolabs:helltest");
		userPermissionDAO.save(userPermission);
	}
}
