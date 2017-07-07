package com.bilibili.adp.scrapy.base.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;


public class HttpUtil {

	private static Log logger = LogFactory.getLog(HttpUtil.class);

	/**
	 * Get方式获取数据
	 * @param url
	 * @return
	 */
	public static String loadContentByGetMethod(String url,Header header) throws Exception{
		String result = null;
		
		/* 1 生成 HttpClinet 对象并设置参数 */
		HttpClient httpClient = HttpClients.createDefault(); 
		
		// 设置 Http 连接超时为10秒
//		httpClient.
//		.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		/* 2 生成 GetMethod 对象并设置参数 */
		HttpGet getMethod = new HttpGet(url);
		try {
			getMethod.addHeader(header);
			// 设置 get 请求超时为 10 秒
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).setConnectionRequestTimeout(10000).build();
			getMethod.setConfig(requestConfig);
//			getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
			// 设置请求重试处理，用的是默认的重试处理：请求三次
//			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(3,true));
			/* 3 执行 HTTP GET 请求 */
			 HttpResponse statusResponse = httpClient.execute(getMethod);
			/* 4 判断访问的状态码 */
			if (statusResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				logger.debug("GET请求失败: " + statusResponse.getStatusLine());
			}


			// 读取 HTTP 响应内容，这里简单打印网页内容
			// 读取为字节数组
			result = EntityUtils.toString(statusResponse.getEntity());
		} catch (Exception e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			logger.error("读取GET接口数据时发生异常："+e.getMessage(),e);
			throw new Exception(e);
		} finally {
			/* 6 .释放连接 */
			getMethod.releaseConnection();
		}
		logger.debug("读取数据为："+result);
		
		return result;
	}
	
	/**
	 * 发送JSON数据，POST方式
	 * @param data
	 */
	public static String postJSONData(String url,JSONObject data) throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		if(data != null){
			String inputParam = data.toJSONString();
			logger.debug("POST接口发送内容："+inputParam);
			httpPost.setEntity(new StringEntity(inputParam, "UTF-8"));	
		}
		httpPost.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = null;
		
		String result = null;
		try{
			response = httpclient.execute(httpPost);
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.debug("发送POST接口数据返回内容："+result);
		}catch(Exception ex){
			logger.debug("调用POST接口数据时发生异常："+ex.getMessage(),ex);
			throw new Exception(ex.getMessage());
		}finally{
			if(null != response){
				try{
					response.close();
				}catch(Exception e){}
			}
		}
		
		return result;
		
	}
	
	/**
	 * 发送POST请求参数
	 * @param url
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public static String postData(String url,Map<String,String> paramMap) throws Exception{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		try {
			// 构造请求
			HttpPost httpPost = new HttpPost(url);
			// 封装参数
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			if(null != paramMap){
				for(Entry<String,String> item : paramMap.entrySet()){
					nvps.add(new BasicNameValuePair(item.getKey(),item.getValue()));
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
			// 发起请求
			response = httpClient.execute(httpPost);
			// 获取响应数据
			result = EntityUtils.toString(response.getEntity(),Charset.forName("UTF-8"));
//			logger.debug("接收到的POST响应内容：" + result);
		} catch (Exception ex) {
			String msg = "POST请求发送失败:" + ex.getMessage();
			logger.error(msg, ex);
		} finally {
			try {
				response.close();
				httpClient.close();
			} catch (Exception e) {}
		}
		
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		
	}
}
