package com.dapeng.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.google.common.collect.Maps;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Map;

public class WebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		// create the 'root' Spring application context
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(DataConfig.class);
//		rootContext.register(DataConfig.class, SecurityConfig.class);

		// manage the lifecycle of root application context
		servletContext.addListener(new ContextLoaderListener(rootContext));


		// dispatcher Servlet配置
		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
		dispatcherContext.setServletContext(servletContext);
		dispatcherContext.setParent(rootContext);
		dispatcherContext.register(WebConfig.class);

		ServletRegistration.Dynamic dispatcherServletDynamic = servletContext.addServlet("dispatcherServlet", new DispatcherServlet(dispatcherContext));
		dispatcherServletDynamic.setLoadOnStartup(1);
		dispatcherServletDynamic.addMapping("/");

		//字符编码过滤器
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("GBK");
		characterEncodingFilter.setForceEncoding(true);
		servletContext.addFilter("characterEncodingFilter", characterEncodingFilter)
					.addMappingForUrlPatterns(null, false, "/*");

		//Http请求方式过滤器
		servletContext.addFilter("hiddenHttpMethodFilter", new HiddenHttpMethodFilter())
					.addMappingForUrlPatterns(null, false, "/*");



		//shiro拦截器配置
//		DelegatingFilterProxy shiroFilter = new DelegatingFilterProxy("shiroFilterBean", dispatcherContext);
//		shiroFilter.setTargetFilterLifecycle(true);
//		FilterRegistration.Dynamic shiroFilterDynamic = servletContext.addFilter("shiroFilter", shiroFilter);
//		shiroFilterDynamic.setAsyncSupported(true);
//		shiroFilterDynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.ASYNC,
//						DispatcherType.ERROR, DispatcherType.INCLUDE,
//						DispatcherType.FORWARD, DispatcherType.REQUEST), false, "/*");

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

	}

}
