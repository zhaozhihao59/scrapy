package com.bilibili.adp.scrapy.reduce;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.bilibili.adp.scrapy.reduce.service.ICommonService;
import com.bilibili.adp.scrapy.reduce.service.impl.TaskCommonServiceImpl;

@Component
public class Task {
	@Resource
	private ICommonService commonService;
	@Resource
	private TaskCommonServiceImpl taskCommonServiceImpl;
	private static Log logger = LogFactory.getLog(Task.class);

	// @Scheduled(cron = "* * * * * ?")
	public void start() throws Exception {
		logger.info("start ....................................");
		// commonService.reduceZhiHuUser(urlToken);
		taskCommonServiceImpl.test("myExecetion");
		logger.info("end.....................................");
		TimeUnit.HOURS.sleep(4);
	}

}
