package com.dapeng.config;

import com.dapeng.core.shiro.db.ShiroDBRealm;
import com.google.common.collect.Maps;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Map;

@Configuration
public class SecurityConfig {

	@Bean
	public DefaultWebSecurityManager securityManager(){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(new ShiroDBRealm());
		return securityManager;
	}

	@Bean(name = "shiroFilterBean")
	public ShiroFilterFactoryBean shiroFilterBean(){
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

		shiroFilterFactoryBean.setSecurityManager(securityManager());
		shiroFilterFactoryBean.setLoginUrl("/login");
		shiroFilterFactoryBean.setSuccessUrl("/home");
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");

		Map<String, String> chainDefinitionMap = Maps.newLinkedHashMap();
		chainDefinitionMap.put("/logout", "logout");
//		chainDefinitionMap.put("/admin/**", "authc, roles[USER_ADMIN]");
//		chainDefinitionMap.put("/style/**", "anno");
//		chainDefinitionMap.put("/image/**", "anno");
//		chainDefinitionMap.put("/javascript/**", "anno");
//		chainDefinitionMap.put("/flash/**", "anno");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinitionMap);

		return shiroFilterFactoryBean;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
		LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
		return lifecycleBeanPostProcessor;
	}

	@Bean
	public MethodInvokingFactoryBean methodInvokingFactoryBean(){
		MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
		methodInvokingFactoryBean.setArguments(new Object[]{securityManager()});
		methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
		return methodInvokingFactoryBean;
	}

	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		return defaultAdvisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}
}
