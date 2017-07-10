package com.bilibili.adp.scrapy;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.bilibili.adp.scrapy.base.util.RedisCacheUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class AppTest{
   @Resource
   private RedisCacheUtil redisCacheUtil;
   
   @org.junit.Test
   public void testRedis(){
	   String cacheName = "reduceFollow";
	   String cacheName1 = "asdiofjaio";
	   String key = "key";
	   Integer val = 0; 
	   redisCacheUtil.putCacheValue(cacheName, key, val);
	   redisCacheUtil.putCacheValue(cacheName, key + "1", val);
	   redisCacheUtil.putCacheValue(cacheName1, key, val);
	   redisCacheUtil.cleanCache(cacheName,key);
	   Object val1 = redisCacheUtil.getCacheValue(cacheName1, key);
	   
	   System.out.println(val1);
   }
	
	
}
