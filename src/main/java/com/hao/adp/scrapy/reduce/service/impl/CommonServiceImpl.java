package com.hao.adp.scrapy.reduce.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
import com.hao.adp.scrapy.base.aop.DBUseTimeServiceImpl;
import com.hao.adp.scrapy.base.aop.ReduceMember;
import com.hao.adp.scrapy.base.util.HttpUtil;
import com.hao.adp.scrapy.base.util.Utils;
import com.hao.adp.scrapy.reduce.entity.Concern;
import com.hao.adp.scrapy.reduce.entity.Follow;
import com.hao.adp.scrapy.reduce.entity.Member;
import com.hao.adp.scrapy.reduce.service.ICommonService;
import com.hao.adp.scrapy.reduce.service.IConcernService;
import com.hao.adp.scrapy.reduce.service.IFollowService;
import com.hao.adp.scrapy.reduce.service.IMemberService;
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
	public static List<Header> tempHeaderList = new ArrayList<>();
	public static AtomicInteger num = new AtomicInteger(1);
	public static Map<Integer, List<Header>> userHeaderMap = new ConcurrentHashMap<>();
	static{
		tempHeaderList.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36"));
		tempHeaderList.add(new BasicHeader("Connection", "keep-alive"));
		List<Header> tempList = new ArrayList<>();
		tempList.addAll(tempHeaderList);
		tempList.add(new BasicHeader("authorization", "oauth c3cef7c66a1843f8b3a9e6a1e3160e20"));
		userHeaderMap.put(0,tempList);
	}
	
	public static List<Header> getHeader(){
		 return userHeaderMap.get(num.getAndIncrement()%userHeaderMap.size());
	}
	@Override
	public void reduceZhiHuUser(String urlToken) throws Exception {
	 	Member curMember = addMember(taskCommonServiceImpl.getJsonObject(urlToken));
	 	taskCommonServiceImpl.reduceConcern(curMember.getId(), urlToken, curMember.getConcerns());
//	 	taskCommonServiceImpl.reduceFollow(curMember.getId(), urlToken, curMember.getFollows());
	}
	@Override
	@ReduceMember(cacheName = "reduceMemberVal",key = "#urlToken")
	public Member reduceMember(String urlToken) throws Exception{
		long start = System.currentTimeMillis();
		JSONObject tempJson = taskCommonServiceImpl.getJsonObject(urlToken);
		long end = System.currentTimeMillis();
		if(tempJson == null){
			return null;
		}
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
		if(jsonObj == null){
			return null;
		}
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
