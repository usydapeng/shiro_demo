package com.dapeng.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.google.common.collect.Maps;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Map;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

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

		//字符编码过滤器
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("GBK");
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

		super.onStartup(servletContext);
	}

}
