package com.dapeng.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.dapeng.repository")
public class DataConfig {

	private static final Logger logger = LoggerFactory.getLogger(DataConfig.class);

	@Value("${jdbc.url}")
	private String jdbcUrl;

	@Value("${jdbc.driverClassName}")
	private String jdbcDriverClassName;

	@Value("${jdbc.username}")
	private String jdbcUsername;

	@Value("${jdbc.password}")
	private String jdbcPassword;

	@Bean(initMethod = "init", destroyMethod = "close")
	public DruidDataSource druidDataSource(){
		DruidDataSource druidDataSource = new DruidDataSource();

		druidDataSource.setUrl(jdbcUrl);
		druidDataSource.setDriverClassName(jdbcDriverClassName);
		druidDataSource.setUsername(jdbcUsername);
		druidDataSource.setPassword(jdbcPassword);

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
	public EntityManagerFactory entityManagerFactory(){
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(druidDataSource());
		entityManagerFactory.setJpaVendorAdapter(hibernateJpaVendorAdapter());
		entityManagerFactory.setPackagesToScan("com.dapeng.domain");
		entityManagerFactory.setPersistenceProvider(persistenceProvider());

		Properties jpaProperties = new Properties();
//		jpaProperties.put("hibernate.show_sql", true);//已经在 hibernateJpaVendorAdapter() 做过配置
//		jpaProperties.put("hibernate.dbm2ddl.auto", "update");
		//配置hibernate二级缓存
		jpaProperties.put("current_session_context_class", "thread");
		jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
		jpaProperties.put("hibernate.cache.use_second_level_cache", "true");
		jpaProperties.put("hibernate.cache.use_query_cache", "true");
		jpaProperties.put("hibernate.format_sql", "false");
		jpaProperties.put("hibernate.use_sql_comments", "false");
		entityManagerFactory.setJpaProperties(jpaProperties);

		entityManagerFactory.afterPropertiesSet();
		return entityManagerFactory.getObject();
	}

	@Bean
	public HibernateJpaVendorAdapter hibernateJpaVendorAdapter(){
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
		hibernateJpaVendorAdapter.setGenerateDdl(true);
		hibernateJpaVendorAdapter.setShowSql(true);
		hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
		return hibernateJpaVendorAdapter;
	}

	@Bean
	public JpaTransactionManager jpaTransactionManager(){
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory());
		return jpaTransactionManager;
	}

	@Bean
	public PersistenceProvider persistenceProvider(){
		PersistenceProvider persistenceProvider = new HibernatePersistenceProvider();
		return persistenceProvider;
	}
}
























