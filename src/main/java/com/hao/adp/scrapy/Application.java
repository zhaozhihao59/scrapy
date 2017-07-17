package com.hao.adp.scrapy;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.annotation.Resource;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import redis.clients.jedis.JedisPoolConfig;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@Configuration
@EnableAspectJAutoProxy
@EnableAsync
@EnableSwagger2
@PropertySource(encoding = "utf-8" ,value ={"classpath:application.properties","classpath:redis.properties"})
@EnableCaching
public class Application 
{	
	@Resource
	private Environment env;
	
	@Bean
	public Executor myExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
//		env.getProperty("poolSize",Integer.class)
//		env.getProperty("maxPoolSize",Integer.class)
		executor.setMaxPoolSize(1);
		executor.initialize();
		return executor;
	}
	
	@Bean
	public Docket addUserDocket(){
		Docket docket = new Docket(DocumentationType.SWAGGER_2);
		docket.apiInfo(apiInfo());
		return docket;
	}
	
	public ApiInfo apiInfo(){
		return  new ApiInfoBuilder().description("scrapy api").licenseUrl("http://www.bilibili.com/").title("zhaozhihao scrapy").build();
	}
	
	@Bean
	public JedisPoolConfig poolConfig(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(env.getProperty("redis.maxActive", Integer.class));
		poolConfig.setMaxTotal(env.getProperty("redis.maxTotal", Integer.class));
		poolConfig.setMinIdle(env.getProperty("redis.minActive", Integer.class));
		poolConfig.setMaxWaitMillis(env.getProperty("redis.maxWait", Long.class));
		return poolConfig;
	}
	@Bean
	public ResourcePropertySource resourcePropertySource() throws IOException{
		ResourcePropertySource resouce = new ResourcePropertySource("redis.properties","classpath:redis.properties");
		return resouce;
	}
	@Bean
	public RedisClusterConfiguration redisClusterConfiguration(ResourcePropertySource resourcePropertySource){
		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(resourcePropertySource);
		return redisClusterConfiguration;
	}
	
	
	@Bean
	public JedisConnectionFactory redisConnectionFactory(JedisPoolConfig poolConfig,RedisClusterConfiguration redisClusterConfiguration){
		JedisConnectionFactory factory = new JedisConnectionFactory(redisClusterConfiguration,poolConfig);
		factory.setTimeout(env.getProperty("redis.timeout", Integer.class));
		return factory;
	}
	@Bean
	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory redisConnectionFactory){
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
		
	}
	
	@Bean
	public RedisCacheManager cacheManager(RedisTemplate<String, Object> redisTemplate){
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		return cacheManager;
	}
	
    public static void main( String[] args )
    {
        SpringApplication.run(Application.class,args);
    }
}
