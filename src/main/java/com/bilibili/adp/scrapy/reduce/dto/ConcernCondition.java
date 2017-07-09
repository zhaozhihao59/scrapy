package com.bilibili.adp.scrapy.reduce.dto;

import java.io.Serializable;

/**
 * 你关注的人
 * @creator 赵志豪
 * @create-time 2017-07-08 22:05:54
 * @email 490875647@qq.com
 * @version 1.0
 */
public class ConcernCondition implements Serializable {
	private static final long serialVersionUID = 7904053207325003853L;

	/** 用户名 */
	private String userName;
	/** 简单的自我介绍 */
	private String introduce;
	/** 照片URL */
	private String picUrl;

	/** 用户名 */
	public String getuserName(){
		return this.userName;
	}

	/** 用户名 */
	public void setuserName(String userName){
		this.userName = userName;
	}

	/** 简单的自我介绍 */
	public String getintroduce(){
		return this.introduce;
	}

	/** 简单的自我介绍 */
	public void setintroduce(String introduce){
		this.introduce = introduce;
	}

	/** 照片URL */
	public String getpicUrl(){
		return this.picUrl;
	}

	/** 照片URL */
	public void setpicUrl(String picUrl){
		this.picUrl = picUrl;
	}

}