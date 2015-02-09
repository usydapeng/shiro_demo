package com.dapeng.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = "com.dapeng", excludeFilters = {@ComponentScan.Filter(Configuration.class),
		@ComponentScan.Filter(Controller.class),
		@ComponentScan.Filter(ControllerAdvice.class)})
@Import({DataConfig.class, RedisCachingConfiguration.class})
@PropertySources({@PropertySource("classpath:properties/dapeng-local-mac.properties")})
public class AppConfig {

	@Bean
	public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
		return new PropertySourcesPlaceholderConfigurer();
	}
}
