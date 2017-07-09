package com.bilibili.adp.scrapy.reduce.service;

import java.util.List;

import com.bilibili.adp.scrapy.base.page.PageResult;
import com.bilibili.adp.scrapy.reduce.dto.ConcernCondition;
import com.bilibili.adp.scrapy.reduce.entity.Concern;
/**
 * 你关注的人
 * @creator 赵志豪
 * @create-time 2017-07-08 22:05:54
 * @email 490875647@qq.com
 * @version 1.0
 */
public interface IConcernService{

	/**
	 * 查询所有你关注的人
	 */
	List<Concern> listConcernAll();

	/**
	 * 查询总数
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	int getConcernByPageCount(ConcernCondition condition);

	/**
	 * 分页查询
	 *@param pageResult 分页对象
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	void listConcernByPage(PageResult<Concern> pageResult,ConcernCondition condition);

	/**
	 * 根据ID查询
	 *@param id 主键
	 *@return 你关注的人
	 */
	Concern getConcernById(Long id);

	/**
	 * 新增
	 *@param item 你关注的人
	 */
	Concern add(Concern item);

	/**
	 * 批量新增
	 *@param List 
	 */
	void batchInsert(List<Concern> arrayList);

	/**
	 * 修改
	 *@param item 你关注的人
	 */
	Concern update(Concern item);

	/**
	 * 删除
	 *@param item 你关注的人
	 */
	void delByIds(String[] ids);

}