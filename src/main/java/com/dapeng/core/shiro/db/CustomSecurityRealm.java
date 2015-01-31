package com.dapeng.core.shiro.db;

import com.dapeng.core.shiro.authc.EnhanceUser;
import com.dapeng.core.shiro.encoding.CustomSaltCredentialsMatcher;
import com.dapeng.domain.UserAccount;
import com.dapeng.service.SimpleUserInfo;
import com.dapeng.service.UserService;
import com.dapeng.service.exception.UserAccountException;
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

@Component
public class CustomSecurityRealm extends AuthorizingRealm {

	private static Logger logger = LoggerFactory.getLogger(CustomSecurityRealm.class);

	@Autowired
	private UserService userService;

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.info("authorization: 授权回调函数 " + principals.getRealmNames());

		EnhanceUser enhanceUser = (EnhanceUser) principals.fromRealm(getName()).iterator().next();
		SimpleAuthorizationInfo simpleAuthorizationInfo = null;

		try {
			SimpleUserInfo simpleUserInfo = userService.getByUsername(enhanceUser.getUsername());

			simpleAuthorizationInfo = new SimpleAuthorizationInfo();

			for(UserAccount.Role role : UserAccount.Role.values()){
				if(UserAccount.isInGroup(simpleUserInfo.getUserRole(), role.getId())){
					simpleAuthorizationInfo.addRole(role.toString());
				}
			}

			simpleAuthorizationInfo.addStringPermissions(simpleUserInfo.getUserPermissionList());
		} catch (UserAccountException e) {
			logger.info(e.getMessage(), e);
		}

		return simpleAuthorizationInfo;
	}

	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		logger.info("authentication: 认证回调函数");

		UsernamePasswordToken upat = (UsernamePasswordToken) token;

		try {
			SimpleUserInfo simpleUserInfo = userService.getByUsername(upat.getUsername());
			return new SimpleAuthenticationInfo(new EnhanceUser(simpleUserInfo.getUserId(), simpleUserInfo.getSlug(), simpleUserInfo.getUsername()),
							simpleUserInfo.getPassword(), getName());
		} catch(UserAccountException e){
			throw new AuthenticationException(e.getMessage() + "__Invalid username/password combination!");
		}
	}

	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		credentialsMatcher = new CustomSaltCredentialsMatcher();
		super.setCredentialsMatcher(credentialsMatcher);
	}
}
