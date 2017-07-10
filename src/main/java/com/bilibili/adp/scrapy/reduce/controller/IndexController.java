package com.bilibili.adp.scrapy.reduce.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bilibili.adp.scrapy.base.controller.BaseController;
import com.bilibili.adp.scrapy.reduce.Task;
import com.bilibili.adp.scrapy.reduce.service.ICommonService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "")
public class IndexController extends BaseController{
	@Resource
	private Task task;
	@Resource
	private ICommonService commonService;
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
	
	@ApiOperation("存入url")
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
}
