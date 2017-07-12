package com.bilibili.adp.scrapy;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.bilibili.adp.scrapy.base.util.RedisCacheUtil;
import com.bilibili.adp.scrapy.reduce.service.IConcernService;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class AppTest {

	private static Log logger = LogFactory.getLog(AppTest.class);
	@Resource
	private RedisCacheUtil redisCacheUtil;
	@Resource
	private IConcernService concernService;

	@org.junit.Test
	public void testRedis() {
		String cacheName = "reduceFollow";
		String cacheName1 = "asdiofjaio";
		String key = "key";
		Integer val = 0;
		redisCacheUtil.putCacheValue(cacheName, key, val);
		redisCacheUtil.putCacheValue(cacheName, key + "1", val);
		redisCacheUtil.putCacheValue(cacheName1, key, val);
		redisCacheUtil.cleanCache(cacheName, key);
		Object val1 = redisCacheUtil.getCacheValue(cacheName1, key);

		System.out.println(val1);
	}

	@org.junit.Test
	public void testTrascation() {
		concernService.testTranscation();
	}

	public static void main(String[] args) throws InterruptedException {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(8);
		executor.setCorePoolSize(1);
		executor.initialize();

		for (int i = 0; i < 5000; i++) {
			executor.execute(new Runnable() {

				@Override
				public void run() {
					logger.info("test");
				}
			});
		}
		TimeUnit.SECONDS.sleep(2);
	}

}
