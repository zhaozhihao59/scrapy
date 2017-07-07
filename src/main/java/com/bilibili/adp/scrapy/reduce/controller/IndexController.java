package com.bilibili.adp.scrapy.reduce.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bilibili.adp.scrapy.base.controller.BaseController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "")
public class IndexController extends BaseController{

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
}
