package com.bilibili.adp.scrapy.reduce.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.bilibili.adp.scrapy.base.aop.DBUseTimeServiceImpl;
import com.bilibili.adp.scrapy.base.aop.ReduceMember;
import com.bilibili.adp.scrapy.base.util.HttpUtil;
import com.bilibili.adp.scrapy.base.util.Utils;
import com.bilibili.adp.scrapy.reduce.entity.Concern;
import com.bilibili.adp.scrapy.reduce.entity.Follow;
import com.bilibili.adp.scrapy.reduce.entity.Member;
import com.bilibili.adp.scrapy.reduce.service.ICommonService;
import com.bilibili.adp.scrapy.reduce.service.IConcernService;
import com.bilibili.adp.scrapy.reduce.service.IFollowService;
import com.bilibili.adp.scrapy.reduce.service.IMemberService;
@Service("commonServiceImpl")
@Transactional(value = "transactionManager")
public class CommonServiceImpl implements ICommonService{
	@Resource
	private IMemberService memberService;
	@Resource
	private IFollowService followService;
	@Resource
	private IConcernService  concernService;
	@Resource
	private DBUseTimeServiceImpl dbUseTimeServiceImpl;
	@Resource
	private TaskCommonServiceImpl taskCommonServiceImpl;
	private Log logger = LogFactory.getLog(getClass());
	public static List<Header> headerList = new ArrayList<>();
	static{
		headerList.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36"));
		headerList.add(new BasicHeader("Connection", "keep-alive"));
		headerList.add(new BasicHeader("authorization", "Bearer Mi4wQUFEQUdHTWZBQUFBTUFMOEYtOTFDeGNBQUFCaEFsVk5kWFZyV1FCZlcwaG9nelhIU01mbVZYM0U0T2MzMm5vQ3FR|1497622645|455e71adc1071b6f4f2d58bbef6381cfb9b7fbab"));
	}
	@Override
	public void reduceZhiHuUser(String urlToken) throws Exception {
	 	Member curMember = addMember(taskCommonServiceImpl.getJsonObject(urlToken));
	 	taskCommonServiceImpl.reduceFollow(curMember.getId(), urlToken, curMember.getFollows());
	 	taskCommonServiceImpl.reduceConcern(curMember.getId(), urlToken, curMember.getConcerns());
	 	
	}
	@Override
	@ReduceMember(cacheName = "reduceMemberVal",key = "#urlToken")
	public Member reduceMember(String urlToken) throws Exception{
		long start = System.currentTimeMillis();
		JSONObject tempJson = taskCommonServiceImpl.getJsonObject(urlToken);
		long end = System.currentTimeMillis();
		dbUseTimeServiceImpl.submit("getMemberNet", (end - start));
		Member tempMember = addMember(tempJson);
		tempMember.setMemberData(null);
		tempMember.setDescribe(null);
		tempMember.setIntroduce(null);
		tempMember.setUserName(null);
		tempMember.setPicUrl(null);
		return tempMember;
	}
	
	public void reduceMembers(List<JSONObject> userList,Boolean flag) throws Exception{
		for (JSONObject jsonObject : userList) {
			logger.info("start reduce " + jsonObject + " cur flag" + flag);
			if(flag){
				taskCommonServiceImpl.reduceFollow(jsonObject.getLong("id"),jsonObject.getString("urlToken"),jsonObject.getInteger("count"));
			}else{
				taskCommonServiceImpl.reduceConcern(jsonObject.getLong("id"),jsonObject.getString("urlToken"),jsonObject.getInteger("count"));
			}
		}
	}
	
	
	
	public Member addMember(JSONObject jsonObj){
		Member item = new Member();
	 	item.setIntroduce(jsonObj.getString("headline"));
	 	item.setPicUrl(jsonObj.getString("avatar_url"));
	 	item.setSex(jsonObj.getInteger("gender"));
	 	item.setUserName(jsonObj.getString("name"));
	 	item.setDescribe(jsonObj.getString("description"));
	 	item.setUserId(jsonObj.getString("memberId"));
	 	item.setFollows(jsonObj.getInteger("follower_count"));
	 	item.setConcerns(jsonObj.getInteger("following_count"));
	 	Utils.removeKey(jsonObj, "headline","avatar_url","gender","name","description","memberId","follower_count","following_count");
	 	item.setMemberData(jsonObj.toJSONString());
	 	Member temp = memberService.add(item);
	 	item.setId(temp.getId());
	 	return item;
	}
	
	
	public static void main(String[] args) {
		String url = "https://www.zhihu.com/people/mo-lu-6-36";
		String urlName = url.substring(url.lastIndexOf("/") + 1,url.length());
		System.out.println(urlName);
	}

}
