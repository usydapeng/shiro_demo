package com.dapeng.config;

import com.dapeng.core.cache.SSDBCacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class SSDBCachingConfiguration {

	@Bean
	public SSDBCacheManager ssdbCacheManager(){
		return new SSDBCacheManager();
	}
}
