package com.bilibili.adp.scrapy.reduce.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bilibili.adp.scrapy.base.aop.MyReduce;
import com.bilibili.adp.scrapy.base.aop.ReduceFollow;
import com.bilibili.adp.scrapy.base.util.HttpUtil;
import com.bilibili.adp.scrapy.reduce.entity.Concern;
import com.bilibili.adp.scrapy.reduce.entity.Follow;
import com.bilibili.adp.scrapy.reduce.entity.Member;
import com.bilibili.adp.scrapy.reduce.service.ICommonService;
import com.bilibili.adp.scrapy.reduce.service.IConcernService;
import com.bilibili.adp.scrapy.reduce.service.IFollowService;
import com.bilibili.adp.scrapy.reduce.service.IMemberService;

@Service
@Transactional(value = "transactionManager")
public class TaskCommonServiceImpl{

	private Log logger = LogFactory.getLog(getClass());
	@Resource
	private IMemberService memberService;
	@Resource
	private IFollowService followService;
	@Resource
	private IConcernService  concernService;
	@Resource(name = "commonServiceImpl")
	private ICommonService commonServiceImpl;
	
	private Follow addFollow(Long sId,Member member){
		Follow item = new Follow();
		item.setFollowId(member.getId());
		item.setMenberId(sId);
		item.setIntroduce(member.getIntroduce());
		item.setUserName(member.getUserName());
		item.setPicUrl(member.getPicUrl());
		item.setSex(member.getSex());
		followService.add(item);
		return item;
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Concern addConcern(Long sId,Member member){
		Concern item = new Concern();
		item.setFollowId(member.getId());
		item.setMenberId(sId);
		item.setIntroduce(member.getIntroduce());
		item.setUserName(member.getUserName());
		item.setPicUrl(member.getPicUrl());
		item.setSex(member.getSex());
		concernService.add(item);
		return item;
	}
	
	public JSONObject getJsonObject(String urlToken) throws Exception{
		String detailUrl = "https://www.zhihu.com/api/v4/members/" + urlToken + "?";
	 	String params = "include=locations%2Cemployments%2Cgender%2Ceducations%2Cbusiness%2Cvoteup_count%2Cthanked_Count%2Cfollower_count%2Cfollowing_count%2Ccover_url%2Cfollowing_topic_count%2Cfollowing_question_count%2Cfollowing_favlists_count%2Cfollowing_columns_count%2Cavatar_hue%2Canswer_count%2Carticles_count%2Cpins_count%2Cquestion_count%2Ccolumns_count%2Ccommercial_question_count%2Cfavorite_count%2Cfavorited_count%2Clogs_count%2Cmarked_answers_count%2Cmarked_answers_text%2Cmessage_thread_token%2Caccount_status%2Cis_active%2Cis_bind_phone%2Cis_force_renamed%2Cis_bind_sina%2Cis_privacy_protected%2Csina_weibo_url%2Csina_weibo_name%2Cshow_sina_weibo%2Cis_blocking%2Cis_blocked%2Cis_following%2Cis_followed%2Cmutual_followees_count%2Cvote_to_count%2Cvote_from_count%2Cthank_to_count%2Cthank_from_count%2Cthanked_count%2Cdescription%2Chosted_live_count%2Cparticipated_live_count%2Callow_message%2Cindustry_category%2Corg_name%2Corg_homepage%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics";
	 	String url = detailUrl + params;
	 	String jsonStr = HttpUtil.loadContentByGetMethod(url, CommonServiceImpl.headerList);
	 	JSONObject jsonObj = JSON.parseObject(jsonStr);
	 	String memberId = DigestUtils.md5Hex(urlToken);
	 	jsonObj.put("memberId", memberId);
		return jsonObj;
	}
	@Async
	@ReduceFollow(cacheName = "reduceConcern" , key = "#sId+#urlToken")
	public void reduceConcern(Long sId,String urlToken,Integer count) throws Exception{
		String detailUrl = "https://www.zhihu.com/api/v4/members/" + urlToken + "/followees?";
		String params = "include=data%5B*%5D.answer_count%2Carticles_count%2Cgender%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics";
		int size = 500;
		int pages = count / size + (count %size == 0?0:1);
		List<JSONObject> list = new ArrayList<>();
		for (int i = 0; i < pages; i++) {
			int offset = i * size;
			String url = detailUrl + params + "&offset="+offset+"&limit="+size;
			String resultStr = HttpUtil.loadContentByGetMethod(url, CommonServiceImpl.headerList);
			JSONObject pageObj = JSON.parseObject(resultStr);
			JSONArray jsonArr = pageObj.getJSONArray("data");
			for (Object object : jsonArr) {
				JSONObject jsonObj = (JSONObject) object;
				String tempToken = jsonObj.getString("url_token");
				JSONObject tempJsonObj = getJsonObject(tempToken);
				Member tempMember = commonServiceImpl.addMember(tempJsonObj);
				addConcern(sId, tempMember);
				JSONObject obj = new JSONObject();
				obj.put("urlToken", tempToken);
				obj.put("count", tempMember.getConcerns());
				obj.put("id", tempMember.getId());
				list.add(obj);
			}
		}
		commonServiceImpl.reduceMembers(list,false);
	}
	@Async
	@ReduceFollow(cacheName = "reduceFollow" ,key = "#sId + #urlToken")
	public void reduceFollow(Long sId,String urlToken,Integer count) throws Exception{
		String detailUrl = "https://www.zhihu.com/api/v4/members/" + urlToken + "/followers?";
		String params = "include=data%5B*%5D.answer_count%2Carticles_count%2Cgender%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics";
		int size = 500;
		int pages = count / size + (count %size == 0?0:1);
		List<JSONObject> list = new ArrayList<>();
		for (int i = 0; i < pages; i++) {
			int offset = i * size;
			String url = detailUrl + params + "&offset="+offset+"&limit="+size;
			String resultStr = HttpUtil.loadContentByGetMethod(url, CommonServiceImpl.headerList);
			JSONObject pageObj = JSON.parseObject(resultStr);
			JSONArray jsonArr = pageObj.getJSONArray("data");
			for (Object object : jsonArr) {
				JSONObject jsonObj = (JSONObject) object;
				String tempToken = jsonObj.getString("url_token");
				JSONObject tempJsonObj = getJsonObject(tempToken);
				Member tempMember = commonServiceImpl.addMember(tempJsonObj);
				addFollow(sId, tempMember);
				JSONObject obj = new JSONObject();
				obj.put("urlToken", tempToken);
				obj.put("count", tempMember.getFollows());
				obj.put("id", tempMember.getId());
				list.add(obj);
			}
		}
		commonServiceImpl.reduceMembers(list,true);
	}
//	@Async
	public void test() throws InterruptedException {
		logger.info("aaaaa");
		TimeUnit.SECONDS.sleep(5);
	}
	
	
}
