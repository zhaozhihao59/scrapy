package com.hao.adp.scrapy.reduce;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hao.adp.scrapy.reduce.dao.IConcernDao;
import com.hao.adp.scrapy.reduce.dto.ConcernCondition;
import com.hao.adp.scrapy.reduce.entity.Concern;
import com.hao.adp.scrapy.reduce.entity.Member;
import com.hao.adp.scrapy.reduce.service.ICommonService;
import com.hao.adp.scrapy.reduce.service.IConcernService;
import com.hao.adp.scrapy.reduce.service.impl.TaskCommonServiceImpl;

@Component
public class Task {
	@Resource
	private ICommonService commonService;
	@Resource
	private TaskCommonServiceImpl taskCommonServiceImpl;
	@Resource
	private IConcernDao concernDao;
	private Log logger = LogFactory.getLog(getClass());
	@Scheduled(cron = "* * * * * ?")
	public void start() throws Exception{
		ConcernCondition condition = new ConcernCondition();
		List<Concern> list = concernDao.listConcernByPage(condition);
		System.out.println(list.size());
	}
	public static void main(String[] args) throws IOException {
		Member item = new Member();
		item.setConcerns(10);
		item.setDescribe("aaasdsdsdsdsdsdsdaaaaaaaaaaaaaaaaaaaaaaaaaa");
		FileOutputStream fos = new FileOutputStream("member.txt");
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(item);
		os.flush();
		os.close();
		fos.close();
	}
	
}
