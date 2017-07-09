package com.bilibili.adp.scrapy.reduce;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bilibili.adp.scrapy.reduce.service.ICommonService;
import com.bilibili.adp.scrapy.reduce.service.impl.TaskCommonServiceImpl;

@Component
public class Task {
	@Resource
	private ICommonService commonService;
	@Resource
	private TaskCommonServiceImpl taskCommonServiceImpl;
	private Log logger = LogFactory.getLog(getClass());
	@Scheduled(cron = "* * * * * ?")
	public void start() throws Exception{
		logger.info("start ....................................");
		commonService.reduceZhiHuUser();
		
		TimeUnit.HOURS.sleep(8);;
	}
	
	
}
