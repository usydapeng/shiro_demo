package com.dapeng.config;

import com.dapeng.web.handler.BasicMappingExceptionResolver;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableAsync
@ComponentScan(basePackages = "com.dapeng", useDefaultFilters = false,
		includeFilters = {@ComponentScan.Filter(Controller.class), @ComponentScan.Filter(ControllerAdvice.class)})
public class WebConfig extends WebMvcConfigurerAdapter {

	/**
	 * 配置静态资源的访问路径
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);
		registry.addResourceHandler("/images/**").addResourceLocations("/static/public/images/").setCachePeriod(0);
		registry.addResourceHandler("/styles/**").addResourceLocations("/static/public/styles").setCachePeriod(0);
		registry.addResourceHandler("/javascript/**").addResourceLocations("/static/public/javascript").setCachePeriod(0);
		registry.addResourceHandler("/flash/**").addResourceLocations("/static/public/flash").setCachePeriod(0);
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		Map<String, MediaType> mediaTypeMap = Maps.newHashMap();
		mediaTypeMap.put("atom", MediaType.APPLICATION_ATOM_XML);
		mediaTypeMap.put("html", MediaType.TEXT_HTML);
		mediaTypeMap.put("json", MediaType.APPLICATION_JSON);

		configurer.favorParameter(false).favorPathExtension(false)
				.ignoreAcceptHeader(false).useJaf(false)
				.defaultContentType(MediaType.TEXT_HTML).mediaTypes(mediaTypeMap);
	}

	@Bean
	public ServletContextTemplateResolver templateResolver(){
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setSuffix(".html");
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCacheable(false);
		templateResolver.setOrder(1);
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(){
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver thymeleafViewResolver(){
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setTemplateEngine(templateEngine());
		thymeleafViewResolver.setOrder(1);
//		thymeleafViewResolver.setViewNames(new String[]{"*.html,*.xhtml"});
		thymeleafViewResolver.setCache(false);
		thymeleafViewResolver.setCharacterEncoding("UTF-8");

		Map<String, String> staticVariables = Maps.newHashMap();
		staticVariables.put("footer", "DAPENG Co.,Ltd");
		thymeleafViewResolver.setStaticVariables(staticVariables);

		return thymeleafViewResolver;
	}

	@Bean
	public ContentNegotiatingViewResolver contentNegotiatingViewResolver(){
		ContentNegotiatingViewResolver contentNegotiatingViewResolver = new ContentNegotiatingViewResolver();

		List<ViewResolver> viewResolverList = Lists.newArrayList();
		viewResolverList.add(thymeleafViewResolver());

		List<View> defaultViews = Lists.newArrayList();

		contentNegotiatingViewResolver.setOrder(1);
		contentNegotiatingViewResolver.setViewResolvers(viewResolverList);
		contentNegotiatingViewResolver.setDefaultViews(defaultViews);

		return contentNegotiatingViewResolver;
	}

	@Bean
	public BasicMappingExceptionResolver exceptionResolver(){
		BasicMappingExceptionResolver exceptionResolver = new BasicMappingExceptionResolver();

		Properties exceptionMappings = new Properties();
		exceptionMappings.put("java.sql.SQLException", 404);
		exceptionMappings.put("java.lang.RuntimeException", 404);
		exceptionMappings.put("java.lang.NullPointerException", 404);
		exceptionResolver.setExceptionMappings(exceptionMappings);

		Properties statusCodes = new Properties();
		statusCodes.put("404", "404");
		statusCodes.put("500", "404");
		exceptionResolver.setStatusCodes(statusCodes);

		exceptionResolver.setDefaultErrorView("404");
		exceptionResolver.setDefaultStatusCode(404);

		return exceptionResolver;
	}

	@Bean
	public ResourceBundleMessageSource messageSource(){
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setBasename("Messages");
		messageSource.setCacheSeconds(10);
		return messageSource;
	}

	@Bean
	public FormattingConversionServiceFactoryBean conversionService(){
		FormattingConversionServiceFactoryBean conversionService = new FormattingConversionServiceFactoryBean();

		Set<?> converters = Sets.newHashSet();
		conversionService.setConverters(converters);

		Set<?> formatters = Sets.newHashSet();
		conversionService.setFormatters(formatters);

		return conversionService;
	}

}
































