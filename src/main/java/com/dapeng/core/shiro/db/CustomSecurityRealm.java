package com.dapeng.core.shiro.db;

import com.dapeng.core.shiro.authc.EnhanceUser;
import com.dapeng.core.shiro.encoding.CustomSaltCredentialsMatcher;
import com.dapeng.dao.UserAccountDAO;
import com.dapeng.dao.UserInfoDAO;
import com.dapeng.dao.UserPermissionDAO;
import com.dapeng.domain.UserAccount;
import com.dapeng.domain.UserPermission;
import com.google.common.collect.Lists;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class CustomSecurityRealm extends AuthorizingRealm {

	private static Logger logger = LoggerFactory.getLogger(CustomSecurityRealm.class);

	@Autowired
	private UserInfoDAO userInfoDAO;

	@Autowired
	private UserAccountDAO userAccountDAO;

	@Autowired
	private UserPermissionDAO userPermissionDAO;

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.info("authorization: 授权回调函数 " + principals.getRealmNames());

		EnhanceUser enhanceUser = (EnhanceUser) principals.fromRealm(getName()).iterator().next();

		UserAccount userAccount = userAccountDAO.findByUsername(enhanceUser.getUsername());

		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

		for (UserAccount.Role role : UserAccount.Role.values()) {
			if (UserAccount.isInGroup(userAccount.getUserRole(), role.getId())) {
				simpleAuthorizationInfo.addRole(role.toString());
			}
		}

		List<UserPermission> userPermissionList = userPermissionDAO.findAllByUserId(userAccount.getId());
		List<String> permissionStrList = Lists.newArrayList();
		for (UserPermission userPermission : userPermissionList) {
			permissionStrList.add(userPermission.getPermission());
		}

		simpleAuthorizationInfo.addStringPermissions(permissionStrList);

		return simpleAuthorizationInfo;
	}

	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		logger.info("authentication: 认证回调函数");

		UsernamePasswordToken upat = (UsernamePasswordToken) token;

		UserAccount userAccount = userAccountDAO.findByUsername(upat.getUsername());
		if(userAccount == null){
			throw new AuthenticationException("__Invalid username/password combination!");
		}
		return new SimpleAuthenticationInfo(new EnhanceUser(userAccount.getId(), userAccount.getSlug(), userAccount.getUsername()),
				userAccount.getPassword(), getName());
	}

	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		credentialsMatcher = new CustomSaltCredentialsMatcher();
		super.setCredentialsMatcher(credentialsMatcher);
	}
}
