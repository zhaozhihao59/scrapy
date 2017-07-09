package com.bilibili.adp.scrapy.base.conf;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.alibaba.druid.pool.DruidDataSource;

@EnableAutoConfiguration
@Configuration
@ComponentScan("com.revanow")
@PropertySource(value = {"classpath:appconfig.properties","classpath:redis.properties"},ignoreResourceNotFound = true)
@EnableTransactionManagement
public class JavaConf {
	
	@Autowired
	private Environment  env;
	@Value("${jdbc.url}")
	private String url;
	
	@Bean(name = "dataSource",initMethod = "init" , destroyMethod = "close")
	public DruidDataSource dataSource() throws SQLException{
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.user"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		dataSource.setInitialSize(env.getProperty("pool.initialSize",int.class));
		dataSource.setMinIdle(env.getProperty("pool.minIdle",int.class));
		dataSource.setMaxActive(env.getProperty("pool.maxActive",int.class));
		dataSource.setMaxWait(60000l);
		dataSource.setTimeBetweenEvictionRunsMillis(60000l);
		dataSource.setMinEvictableIdleTimeMillis(300000);
		dataSource.setValidationQuery("SELECT 'x'");
		dataSource.setTestOnBorrow(false);
		dataSource.setTestOnReturn(false);
		dataSource.setTestWhileIdle(true);
		dataSource.setPoolPreparedStatements(true);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
		dataSource.setFilters("stat");
		return dataSource;
	}
	
	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:sqlmap-config.xml"));
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:com/bilibili/**/mapper/*.xml"));
		return sqlSessionFactoryBean;
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource){
		return new DataSourceTransactionManager(dataSource);
	}
	
	
	/** ================配置自定义视图 ========================== **/
	@Bean
	public InternalResourceViewResolver viewResolver(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/pages");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	@Bean
	public CommonsMultipartResolver commonsMultipartResolver(){
		return new CommonsMultipartResolver();
	} 
	
	@Bean
	public BeanNameViewResolver beanNameViewResolver(){
		BeanNameViewResolver resolver = new BeanNameViewResolver();
		resolver.setOrder(1);
		return resolver;
	}
	
//	@Bean
//	public CustomJsonView jsonView(){
//		return new CustomJsonView();
//	}
	
	
}
