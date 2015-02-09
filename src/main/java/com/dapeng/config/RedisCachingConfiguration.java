package com.dapeng.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
@EnableCaching
public class RedisCachingConfiguration {

	@Value("${redis.server.url}")
	private String redisHostName;

	@Value("${redis.server.port}")
	private int redisPort;

	@Bean
	public RedisCacheManager cacheManager(){
		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());

		List<String> cacheNames = Lists.newArrayList();
		cacheNames.add("product");
		cacheNames.add("default");
		redisCacheManager.setCacheNames(cacheNames);

		return redisCacheManager;
	}

	@Bean
	public RedisCacheManager redisCacheManager(){
		return new RedisCacheManager(redisTemplate());
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate(){
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory(){
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setHostName(redisHostName);
		jedisConnectionFactory.setPort(redisPort);
		jedisConnectionFactory.setUsePool(true);
		return jedisConnectionFactory;
	}

}
