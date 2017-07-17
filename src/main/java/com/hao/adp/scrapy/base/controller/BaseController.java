package com.hao.adp.scrapy.base.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class BaseController {

	protected Log logger = LogFactory.getLog(getClass());

	public static final String STATUS_PARAMETER_NAME = "status";// 操作状态参数名称
	public static final String MESSAGE_PARAMETER_NAME = "message";// 操作消息参数名称
	public static final String TOKEN_PARAMETER_NAME = "token";	//操作token参数名称
	
	// 操作状态（警告、错误、成功）
	public enum Status {
		warn, error, success
	}
	
	/**
	 * 返回json数据
	 * @param status
	 * @param msg
	 * @return
	 */
	protected ModelAndView ajaxJSON(Status status){
		ModelAndView view = new ModelAndView("jsonView");
		Map<String,Object> data = view.getModel();
		data.put(STATUS_PARAMETER_NAME, status);
		
		return view;
	}
	/**
	 * 返回json数据
	 * @param status
	 * @param msg
	 * @return
	 */
	protected ModelAndView ajaxJSON(Status status,JSONObject root){
		ModelAndView view = new ModelAndView("jsonView");
		Map<String,Object> data = view.getModel();
		data.put(STATUS_PARAMETER_NAME, status);
		for (Object item : root.keySet()) {
			data.put((String)item, root.get(item));
		}
		return view;
	}
	/**
	 * 返回json数据
	 * @param status
	 * @param msg
	 * @return
	 */
	protected ModelAndView ajaxJSON(Status status,String msg){
		ModelAndView view = new ModelAndView("jsonView");
		Map<String,Object> data = view.getModel();
		data.put(STATUS_PARAMETER_NAME, status);
		data.put(MESSAGE_PARAMETER_NAME, msg);
		
		return view;
	}
	/**
	 * 返回json数据
	 * @param status
	 * @param msg
	 * @return
	 */
	protected ModelAndView ajaxJSON(Status status,JSONArray jsonArray){
		ModelAndView view = new ModelAndView("jsonView");
		Map<String,Object> data = view.getModel();
		data.put(STATUS_PARAMETER_NAME, status);
		data.put("data", jsonArray);
		return view;
	}
	/**
	 * 返回json数据
	 * @param status
	 * @param msg
	 * @return
	 */
	protected ModelAndView ajaxJSON(JSONArray jsonArray){
		ModelAndView view = new ModelAndView("jsonView");
		Map<String,Object> data = view.getModel();
		data.put("data", jsonArray);
		return view;
	}
	
	/**
	 * 返回json数据
	 * @param json
	 * @return
	 */
	protected ModelAndView ajaxJSON(JSONObject json){
		ModelAndView view = new ModelAndView("jsonView");
		Map<String,Object> data = view.getModel();
		data.put(STATUS_PARAMETER_NAME, Status.success);
		data.put("data", json);
		return view;
	}
	
	/**
	 * 返回json数据
	 * @param status
	 * @param msg
	 * @param needToken
	 * @return
	 */
	protected ModelAndView ajaxJSON(Status status,String msg,boolean needToken,HttpServletRequest request){
		ModelAndView view = new ModelAndView("jsonView");
		Map<String,Object> data = view.getModel();
		data.put(STATUS_PARAMETER_NAME, status);
		data.put(MESSAGE_PARAMETER_NAME, msg);
		
		if(needToken){
			//String tokenVal = UUIDUtil.uuid();
			//request.getSession().setAttribute(SessionConstant.TOKEN,tokenVal);
			//data.put(TOKEN_PARAMETER_NAME, tokenVal);
		}
		
		return view;
	}
	
	protected ModelAndView ajaxJSON(Status status,String msg,Map<String,String> resultData){
		ModelAndView view = new ModelAndView("jsonView");
		Map<String,Object> data = view.getModel();
		data.put(STATUS_PARAMETER_NAME, status);
		data.put(MESSAGE_PARAMETER_NAME, msg);
		
		if(null != resultData){
			for(Map.Entry<String, String> entry : resultData.entrySet()){
				data.put(entry.getKey(), entry.getValue());
			}
			
			resultData.clear();
			resultData = null;
		}
		
		
		return view;
	}
	
	protected ModelAndView ajaxHTML(Status status,String msg,Map<String,String> resultData){
		ModelAndView view = new ModelAndView("htmlView");
		Map<String,Object> data = view.getModel();
		data.put(STATUS_PARAMETER_NAME, status);
		data.put(MESSAGE_PARAMETER_NAME, msg);
		
		if(null != resultData){
			for(Map.Entry<String, String> entry : resultData.entrySet()){
				data.put(entry.getKey(), entry.getValue());
			}
			
			resultData.clear();
			resultData = null;
		}
		
		
		
		return view;
	}
	
	/**
	 * 输入分页数据
	 * @param root
	 * @param response
	 */
	protected void ajaxPageResult(JSONObject root,HttpServletResponse response) {
		try{
			response.setContentType("text/json;charset=UTF-8");
			response.setDateHeader("Expires", 1L);
			response.addHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
			root.put(STATUS_PARAMETER_NAME, Status.success);
			response.getWriter().print(JSON.toJSONString(root));
		}catch(Exception ex){
			logger.error(ex.getMessage(),ex);
		}
	}

	
	/**
	 * 获取网站地址
	 * @param request
	 * @return
	 */
	protected String getBaseUrl(HttpServletRequest request){
		String path = request.getContextPath();
		StringBuffer basePath = new StringBuffer();
		basePath.append(request.getScheme());
		basePath.append("://");
		basePath.append(request.getServerName());
		int port = request.getServerPort();
		if(port != 80){
			basePath.append(":").append(port);
		}
		basePath.append(path).append("/");
		return basePath.toString();
	}
		
	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 */
	protected String getIpAddr(HttpServletRequest request){
		String ip = request.getHeader("X-Real-IP");
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		} else {
			return ip;
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		} else {
			int index = ip.indexOf(',');
			if (index != -1) {
				ip = ip.substring(0, index);
			}
		}
		return ip;
	}
	
	
	/**
	 * 存入cookie
	 * @param cookieName
	 * @param cookieValue
	 * @param request
	 * @param response
	 */
	protected void putValueToCookie(String cookieName,String cookieValue,Integer expiry,HttpServletResponse response){
		//存入cookie中
		Cookie cookie = new Cookie(cookieName,cookieValue);
		cookie.setPath("/");
		if(null != expiry){
			cookie.setMaxAge(expiry);
		}
		response.addCookie(cookie);
	}

	/**
	 * 获取存在cookie中的值
	 * @param request
	 * @return
	 */
	protected String getValueFromCookie(String cookieKeyName,HttpServletRequest request){
		String cookieValue = null;
		Cookie cookies[] = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (cookieKeyName.equals(cookie.getName())) {
					cookieValue = cookie.getValue();
					break;
				}
			}
		}
		return cookieValue;
	}
	
	/**
	 * 从cookie中删除当前会员
	 * @param request
	 * @param response
	 * @return 存在cookie中的key值
	 */
	protected String removeValueFromCookie(String removeCookieName,HttpServletRequest request,HttpServletResponse response){
		String cookieValue = null;
		Cookie cookies[] = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (removeCookieName.equals(cookie.getName())) {
					cookieValue = cookie.getValue();
					Cookie deleteCookie = new Cookie(cookie.getName(),null);
					deleteCookie.setPath("/");
					deleteCookie.setMaxAge(0);	//立即删除
					response.addCookie(deleteCookie);
					
					break;
				}
			}
		}
		return cookieValue;
	}
	
}
