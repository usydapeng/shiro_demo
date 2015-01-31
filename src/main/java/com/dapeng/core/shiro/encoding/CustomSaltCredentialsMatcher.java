package com.dapeng.core.shiro.encoding;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

public class CustomSaltCredentialsMatcher extends MessageDigestPasswordEncoder implements CredentialsMatcher {

	public CustomSaltCredentialsMatcher() {
		super("MD5");
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		Object tokenCredentials = getCredentials(token);
		Object accountCredentials = getCredentials(info);
		Object salt = getCredentialsSalt(token);
		return equals(tokenCredentials, accountCredentials, salt);
	}

	protected String getCredentials(AuthenticationToken token) {
		if(token instanceof UsernamePasswordToken){
			UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
			return new String(usernamePasswordToken.getPassword());
		}
		return null;
	}

	protected Object getCredentials(AuthenticationInfo info) {
		return info.getCredentials();
	}

	protected Object getCredentialsSalt(AuthenticationToken token) {
		if(token instanceof UsernamePasswordToken){
			UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
			return usernamePasswordToken.getUsername();
		}
		return null;
	}

	protected boolean equals(Object tokenCredentials, Object accountCredentials, Object salt) {
		if(tokenCredentials != null && accountCredentials != null && salt != null){
			return isPasswordValid((String)accountCredentials, (String)tokenCredentials, salt);
		}
		return true;
	}

}
