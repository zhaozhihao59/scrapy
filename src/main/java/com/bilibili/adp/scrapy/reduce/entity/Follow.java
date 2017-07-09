package com.bilibili.adp.scrapy.reduce.entity;

import  java.io.Serializable;

import java.util.Date;
/**
 * 关注你的人与你关注的人
 * @creator 赵志豪
 * @create-time 2017-07-08 17:06:47
 * @email 490875647@qq.com
 * @version 1.0
 */
public class Follow implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Long id;
	/** 用户ID */
	private Long menberId;
	/** 跟随者ID */
	private Long followId;
	/** 用户名 */
	private String userName;
	/** 性别：0-男 1-女 */
	private Integer sex;
	/** 简单的自我介绍 */
	private String introduce;
	/** 创建时间 */
	private Date ctime;
	/** 修改时间 */
	private Date utime;
	/** 0-未删除 1-已删除 */
	private Integer isDelete;
	private String picUrl;

	/**  */
	public Long getId(){
		return this.id;
	}

	/**  */
	public void setId(Long id){
		this.id = id;
	}

	/** 用户ID */
	public Long getMenberId(){
		return this.menberId;
	}

	/** 用户ID */
	public void setMenberId(Long menberId){
		this.menberId = menberId;
	}

	/** 跟随者ID */
	public Long getFollowId(){
		return this.followId;
	}

	/** 跟随者ID */
	public void setFollowId(Long followId){
		this.followId = followId;
	}

	/** 用户名 */
	public String getUserName(){
		return this.userName;
	}

	/** 用户名 */
	public void setUserName(String userName){
		this.userName = userName;
	}

	/** 性别：0-男 1-女 */
	public Integer getSex(){
		return this.sex;
	}

	/** 性别：0-男 1-女 */
	public void setSex(Integer sex){
		this.sex = sex;
	}

	/** 简单的自我介绍 */
	public String getIntroduce(){
		return this.introduce;
	}

	/** 简单的自我介绍 */
	public void setIntroduce(String introduce){
		this.introduce = introduce;
	}

	/** 创建时间 */
	public Date getCtime(){
		return this.ctime;
	}

	/** 创建时间 */
	public void setCtime(Date ctime){
		this.ctime = ctime;
	}

	/** 修改时间 */
	public Date getUtime(){
		return this.utime;
	}

	/** 修改时间 */
	public void setUtime(Date utime){
		this.utime = utime;
	}

	/** 0-未删除 1-已删除 */
	public Integer getIsDelete(){
		return this.isDelete;
	}

	/** 0-未删除 1-已删除 */
	public void setIsDelete(Integer isDelete){
		this.isDelete = isDelete;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

}