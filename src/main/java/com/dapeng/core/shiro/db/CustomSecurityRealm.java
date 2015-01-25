package com.dapeng.core.shiro.db;

import com.dapeng.service.SimpleUserInfo;
import com.dapeng.service.UserService;
import com.dapeng.service.exception.UserAccountException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomSecurityRealm extends AuthorizingRealm {

	private static Logger logger = LoggerFactory.getLogger(CustomSecurityRealm.class);

	@Autowired
	private UserService userService;

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("authorization: " + principals.getRealmNames());
		return null;
	}

	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upat = (UsernamePasswordToken) token;

		try {
			SimpleUserInfo simpleUserInfo = userService.getByUsername(upat.getUsername());
			SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(simpleUserInfo, simpleUserInfo, simpleUserInfo.getPassword());
			System.out.println(upat.getUsername() + "_" + upat.getPassword() + simpleUserInfo);
			return simpleAuthenticationInfo;
		} catch(UserAccountException e){
			logger.info(e.getMessage(), e);
			throw new AuthenticationException(e.getMessage() + "__Invalid username/password combination!");
		}
	}
}
