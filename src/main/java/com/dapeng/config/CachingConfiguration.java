package com.dapeng.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisShardInfo;

@Configuration
@EnableCaching
@PropertySources({@PropertySource("classpath:properties/dapeng-local.properties")})
public class CachingConfiguration {

	@Autowired
	private Environment env;

	@Bean
	public RedisCacheManager redisCacheManager(){
		return new RedisCacheManager(redisTemplate());
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(){
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(jedisConnectionFactory());
		return stringRedisTemplate;
	}

	@Bean
	public RedisTemplate redisTemplate(){
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory(){
		return new JedisConnectionFactory(jedisShardInfo());
	}

	@Bean
	public JedisShardInfo jedisShardInfo(){
		return new JedisShardInfo(env.getProperty("redis.server.url"), Integer.valueOf(env.getProperty("redis.server.port")));
	}
}
