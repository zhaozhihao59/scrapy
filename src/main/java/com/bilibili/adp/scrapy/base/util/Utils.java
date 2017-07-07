package com.bilibili.adp.scrapy.base.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 工具集合包
 * @author admin 赵志豪
 *
 */
public class Utils {
	
	private static Log logger = LogFactory.getLog(Utils.class);
	public static String hostUrl = "";
	
	public static String table_info  = "/admin/tableInfo.htm";
	
	public static String databases_info = "/admin/databasesInfo.htm";
	
	public static String api_info = "/admin/sqlData.htm";
	
	public static void init(){
		table_info = hostUrl + table_info;
		databases_info = hostUrl + databases_info;
		api_info = hostUrl + api_info;
	}
	/**
	 * 深度复制
	 * @param src
	 * @return
	 */
	public Object deepCopy(Object src){
		Object dest = null;
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();   
	        ObjectOutputStream out = new ObjectOutputStream(byteOut);
			
	        out.writeObject(src);   
	        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());   
	        ObjectInputStream in =new ObjectInputStream(byteIn);   
	        dest = in.readObject();   
			} catch (Exception e) {
				e.printStackTrace();
			}   
        return dest;    
	}
	
	public static String getJsonStr(HttpServletRequest request) throws IOException{
		ServletInputStream im = request.getInputStream();
		if(im != null){
			String reulst = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(im));
			StringBuffer sb = new StringBuffer();
			while((reulst = br.readLine()) != null){
				sb.append(reulst);
			}
			return sb.toString();
		}
		return null;
	}
	public static void main(String[] args) {
	}
	
}
