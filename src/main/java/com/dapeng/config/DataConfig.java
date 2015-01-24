package com.dapeng.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.dapeng.repository")
@PropertySources({@PropertySource("classpath:properties/dapeng-local.properties")})
public class DataConfig {

	private static final Logger logger = LoggerFactory.getLogger(DataConfig.class);

	@Autowired
	private Environment env;

	@Bean
	public DruidDataSource druidDataSource(){
		DruidDataSource druidDataSource = new DruidDataSource();

		druidDataSource.setUrl(env.getProperty("jdbc.url"));
		druidDataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		druidDataSource.setUsername(env.getProperty("jdbc.username"));
		druidDataSource.setPassword(env.getProperty("jdbc.password"));

//		try {
//			druidDataSource.setFilters("config");
//		} catch(SQLException e){
//			logger.info(e.getMessage(), e);
//		}

		Properties connectionProperties = new Properties();
		connectionProperties.put("config.decrypt", true);
//		druidDataSource.setConnectProperties(connectionProperties);

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

//		Properties jpaProperties = new Properties();
//		jpaProperties.put("hibernate.show_sql", true);
//		jpaProperties.put("hibernate.dbm2ddl.auto", "update");
//		entityManagerFactory.setJpaProperties(jpaProperties);
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
























