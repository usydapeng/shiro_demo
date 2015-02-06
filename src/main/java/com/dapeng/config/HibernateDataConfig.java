package com.dapeng.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySources({@PropertySource("classpath:properties/dapeng-local.properties")})
public class HibernateDataConfig {

	private static Logger logger = LoggerFactory.getLogger(HibernateDataConfig.class);

	@Autowired
	private Environment env;

	@Bean(initMethod = "init", destroyMethod = "close")
	public DruidDataSource druidDataSource(){
		DruidDataSource druidDataSource = new DruidDataSource();

		druidDataSource.setUrl(env.getProperty("jdbc.url"));
		druidDataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		druidDataSource.setUsername(env.getProperty("jdbc.username"));
		druidDataSource.setPassword(env.getProperty("jdbc.password"));

		try {
			druidDataSource.setFilters("config");
		} catch(SQLException e){
			logger.info(e.getMessage(), e);
		}

		druidDataSource.setConnectionProperties("config.decrypt=true");

		druidDataSource.setInitialSize(1);
		druidDataSource.setMinIdle(10);
		druidDataSource.setMaxActive(200);

		druidDataSource.setMaxWait(60000);
		druidDataSource.setTimeBetweenEvictionRunsMillis(60000);

		return druidDataSource;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {

		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
		localSessionFactoryBean.setDataSource(druidDataSource());

		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		hibernateProperties.put("hibernate.show_sql", "true");
		hibernateProperties.put("hibernate.dbm2ddl.auto", "update");
		//配置hibernate二级缓存
		hibernateProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
		hibernateProperties.put("hibernate.cache.use_second_level_cache", "true");
		hibernateProperties.put("hibernate.cache.use_query_cache", "true");
		hibernateProperties.put("hibernate.format_sql", "false");
		hibernateProperties.put("hibernate.use_sql_comments", "true");
		localSessionFactoryBean.setHibernateProperties(hibernateProperties);

		localSessionFactoryBean.setPackagesToScan("com.dapeng.domain");

		return localSessionFactoryBean;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory){
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory);
		return transactionManager;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(){
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(druidDataSource());
		return jdbcTemplate;
	}
}
