package com.bilibili.adp.scrapy.reduce.service;

import java.util.List;

import com.bilibili.adp.scrapy.base.page.PageResult;
import com.bilibili.adp.scrapy.reduce.dto.MemberCondition;
import com.bilibili.adp.scrapy.reduce.entity.Member;
/**
 * 用户
 * @creator 赵志豪
 * @create-time 2017-07-08 17:06:47
 * @email 490875647@qq.com
 * @version 1.0
 */
public interface IMemberService{

	/**
	 * 查询所有用户
	 */
	List<Member> listMemberAll();

	/**
	 * 查询总数
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	int getMemberByPageCount(MemberCondition condition);

	/**
	 * 分页查询
	 *@param pageResult 分页对象
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	void listMemberByPage(PageResult<Member> pageResult,MemberCondition condition);

	/**
	 * 根据ID查询
	 *@param id 主键
	 *@return 用户
	 */
	Member getMemberById(Long id);

	/**
	 * 新增
	 *@param item 用户
	 */
	Member add(Member item);

	/**
	 * 批量新增
	 *@param List 
	 */
	void batchInsert(List<Member> arrayList);

	/**
	 * 修改
	 *@param item 用户
	 */
	Member update(Member item);

	/**
	 * 删除
	 *@param item 用户
	 */
	void delByIds(String[] ids);

	void reload();

}