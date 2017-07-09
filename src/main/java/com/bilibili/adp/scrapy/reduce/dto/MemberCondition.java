package com.bilibili.adp.scrapy.reduce.dto;

import java.io.Serializable;

/**
 * 用户
 * @creator 赵志豪
 * @create-time 2017-07-08 17:06:47
 * @email 490875647@qq.com
 * @version 1.0
 */
public class MemberCondition implements Serializable {
	private static final long serialVersionUID = 7904053207325003853L;

	/** 用户唯一标志 */
	private String userId;
	/** 用户名 */
	private String userName;
	/** 简单的自我介绍 */
	private String introduce;
	/** 简单描述 */
	private String describe;

	/** 用户唯一标志 */
	public String getuserId(){
		return this.userId;
	}

	/** 用户唯一标志 */
	public void setuserId(String userId){
		this.userId = userId;
	}

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

	/** 简单描述 */
	public String getdescribe(){
		return this.describe;
	}

	/** 简单描述 */
	public void setdescribe(String describe){
		this.describe = describe;
	}

}