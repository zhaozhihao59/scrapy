package com.bilibili.adp.scrapy.reduce.entity;

import  java.io.Serializable;

import java.util.Date;
/**
 * 用户
 * @creator 赵志豪
 * @create-time 2017-07-08 17:06:47
 * @email 490875647@qq.com
 * @version 1.0
 */
public class Member implements Serializable {
	private static final long serialVersionUID = 1L;

	/**  */
	private Long id;
	/** 用户唯一标志 */
	private String userId;
	/** 用户名 */
	private String userName;
	/** 性别：0-男 1-女 */
	private Integer sex;
	/** 简单的自我介绍 */
	private String introduce;
	/** 简单描述 */
	private String describe;
	private String picUrl;
	private String memberData;
	
	private Integer follows;
	private Integer concerns;
	
	/** 创建时间 */
	private Date ctime;
	/** 修改时间 */
	private Date utime;
	/** 0-未删除 1-已删除 */
	private Integer isDelete;

	/**  */
	public Long getId(){
		return this.id;
	}

	/**  */
	public void setId(Long id){
		this.id = id;
	}

	/** 用户唯一标志 */
	public String getUserId(){
		return this.userId;
	}

	/** 用户唯一标志 */
	public void setUserId(String userId){
		this.userId = userId;
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

	/** 简单描述 */
	public String getDescribe(){
		return this.describe;
	}

	/** 简单描述 */
	public void setDescribe(String describe){
		this.describe = describe;
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

	public String getMemberData() {
		return memberData;
	}

	public void setMemberData(String memberData) {
		this.memberData = memberData;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public Integer getFollows() {
		return follows;
	}

	public void setFollows(Integer follows) {
		this.follows = follows;
	}

	public Integer getConcerns() {
		return concerns;
	}

	public void setConcerns(Integer concerns) {
		this.concerns = concerns;
	}


}