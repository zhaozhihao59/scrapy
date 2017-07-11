package com.bilibili.adp.scrapy.reduce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bilibili.adp.scrapy.base.aop.DBUseTimeServiceImpl;
import com.bilibili.adp.scrapy.base.controller.BaseController;
import com.bilibili.adp.scrapy.reduce.Task;
import com.bilibili.adp.scrapy.reduce.service.ICommonService;
import com.bilibili.adp.scrapy.reduce.service.IConcernService;
import com.bilibili.adp.scrapy.reduce.service.IMemberService;
import com.bilibili.adp.scrapy.reduce.service.impl.CommonServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "")
public class IndexController extends BaseController{
	@Resource
	private Task task;
	@Resource
	private ICommonService commonService;
	@Resource
	private IMemberService memberService;
	@Resource
	private IConcernService concernService;
	@Resource
	private DBUseTimeServiceImpl dbUseTimeServiceImpl;
	@ApiOperation("存入url")
	@RequestMapping(value = "/putUrl")
	@ResponseBody
	public Map<String, Object> putUrl(
			@RequestParam(name = "url",required = true) @ApiParam("请求的url") String url,
			@RequestParam(name = "cookie" ,required = false) @ApiParam("请求的cookie") String cookie
			){
		Map<String, Object> map = new HashMap<>();
		map.put("status", Status.success);
		
		return map;
	}
	
	@ApiOperation("开始")
	@RequestMapping(value = "/start")
	@ResponseBody
	public Map<String, Object> start(
			@RequestParam(name = "url_token",required = true) @ApiParam("请求的url") String url_token
			) throws Exception{
		
		Map<String, Object> map = new HashMap<>();
		map.put("status", Status.success);
		commonService.reduceZhiHuUser(url_token);
		return map;
	}
	
	@ApiOperation("reload 用户数据")
	@RequestMapping(value = "/reload")
	@ResponseBody
	public Map<String, Object> reload() throws Exception{
		memberService.reload();
		Map<String, Object> map = new HashMap<>();
		map.put("status", Status.success);
		return map;
	}
	@RequestMapping(value = "/hello")
	@ResponseBody
	public String hello(){
		
		return "hello";
	}
	@ApiOperation("reload 用户数据")
	@RequestMapping(value = "/putAuth")
	@ResponseBody
	public Map<String, Object> putAuth(
			@RequestParam(name = "auth",required = true) @ApiParam("请求的url") String auth
			) throws Exception{
		CommonServiceImpl.headerList.remove(1);
		CommonServiceImpl.headerList.add(new BasicHeader("authorization", auth));
		Map<String, Object> map = new HashMap<>();
		map.put("status", Status.success);
		return map;
	}
}
