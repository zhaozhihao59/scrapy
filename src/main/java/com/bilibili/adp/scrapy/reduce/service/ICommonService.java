package com.bilibili.adp.scrapy.reduce.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.bilibili.adp.scrapy.reduce.entity.Member;

public interface ICommonService {
	
	void reduceZhiHuUser() throws Exception;

	Member addMember(JSONObject tempJsonObj);

	void reduceMembers(List<JSONObject> list, Boolean b) throws Exception;
}
