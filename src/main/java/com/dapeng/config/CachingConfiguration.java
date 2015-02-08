package com.dapeng.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
@EnableCaching
@PropertySources({@PropertySource("classpath:properties/dapeng-local.properties")})
public class CachingConfiguration {

	@Autowired
	private Environment env;

	@Bean
	public SimpleCacheManager cacheManager(){
		SimpleCacheManager cacheManager = new SimpleCacheManager();

		List<Cache> cacheList = Lists.newArrayList();
		cacheList.add(new ConcurrentMapCache("product"));
		cacheList.add(new ConcurrentMapCache("default"));

		cacheManager.setCaches(cacheList);

		return cacheManager;
	}

}
