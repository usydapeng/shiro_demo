package com.dapeng.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import java.util.EnumSet;
import java.util.Map;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	private static Logger logger = LoggerFactory.getLogger(WebAppInitializer.class);

	private WebApplicationContext rootContext;

	private WebApplicationContext dispatcherContext;

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{AppConfig.class, SecurityConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{WebConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		super.onStartup(servletContext);

		//字符编码过滤器
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		servletContext.addFilter("characterEncodingFilter", characterEncodingFilter)
					.addMappingForUrlPatterns(null, false, "/*");

		//Http请求方式过滤器
		servletContext.addFilter("hiddenHttpMethodFilter", new HiddenHttpMethodFilter())
					.addMappingForUrlPatterns(null, false, "/*");

		//DruidWebStatFilter过滤器
		WebStatFilter druidWebStatFilter = new WebStatFilter();
		druidWebStatFilter.setProfileEnable(true);
		druidWebStatFilter.setSessionStatEnable(true);
		FilterRegistration.Dynamic druidWebStatFilterDynamic = servletContext.addFilter("druidWebStatFilter", druidWebStatFilter);
		Map<String, String> druidWebStatFilterInitParameters = Maps.newHashMap();
		druidWebStatFilterInitParameters.put("exclusions", "*.less,*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		druidWebStatFilterInitParameters.put("profileEnable", "true");
		druidWebStatFilterInitParameters.put("principalCookieName", "da_uid");
		druidWebStatFilterDynamic.setInitParameters(druidWebStatFilterInitParameters);
		druidWebStatFilterDynamic.addMappingForUrlPatterns(null, false, "/*");

		//Druid监控Servlet
		StatViewServlet druidStatViewServlet = new StatViewServlet();
		ServletRegistration.Dynamic druidStatViewServletDynamic = servletContext.addServlet("druidStatViewServlet", druidStatViewServlet);
		Map<String, String> druidStatViewServletInitParameters = Maps.newHashMap();
		druidStatViewServletInitParameters.put("loginUsername", "dapeng");
		druidStatViewServletInitParameters.put("loginPassword", "dapeng");
		druidStatViewServletDynamic.setInitParameters(druidStatViewServletInitParameters);
		druidStatViewServletDynamic.addMapping("/druid/*");

		//配置Shiro权限控制，
		servletContext.addFilter("shiroFilter", new DelegatingFilterProxy("shiroFilterFactoryBean", rootContext))
				.addMappingForUrlPatterns(EnumSet.of(DispatcherType.ASYNC, DispatcherType.ERROR,
						DispatcherType.INCLUDE, DispatcherType.FORWARD, DispatcherType.REQUEST), false, "/*");
	}

	@Override
	protected WebApplicationContext createRootApplicationContext() {
		logger.info("============= create rootContext =============");
		this.rootContext = super.createRootApplicationContext();
		return this.rootContext;
	}

	@Override
	protected WebApplicationContext createServletApplicationContext() {
		logger.info("============= create dispatcherContext =============");
		this.dispatcherContext = super.createServletApplicationContext();
		return this.dispatcherContext;
	}
}
