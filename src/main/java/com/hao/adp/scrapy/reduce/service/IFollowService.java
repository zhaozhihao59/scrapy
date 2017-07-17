package com.hao.adp.scrapy.reduce.service;

import java.util.List;

import com.hao.adp.scrapy.base.page.PageResult;
import com.hao.adp.scrapy.reduce.dto.FollowCondition;
import com.hao.adp.scrapy.reduce.entity.Follow;
/**
 * 关注你的人与你关注的人
 * @creator 赵志豪
 * @create-time 2017-07-08 17:06:47
 * @email 490875647@qq.com
 * @version 1.0
 */
public interface IFollowService{

	/**
	 * 查询所有关注你的人与你关注的人
	 */
	List<Follow> listFollowAll();

	/**
	 * 查询总数
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	int getFollowByPageCount(FollowCondition condition);

	/**
	 * 分页查询
	 *@param pageResult 分页对象
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	void listFollowByPage(PageResult<Follow> pageResult,FollowCondition condition);

	/**
	 * 根据ID查询
	 *@param id 主键
	 *@return 关注你的人与你关注的人
	 */
	Follow getFollowById(Long id);

	/**
	 * 新增
	 *@param item 关注你的人与你关注的人
	 */
	Follow add(Follow item);

	/**
	 * 批量新增
	 *@param List 
	 */
	void batchInsert(List<Follow> arrayList);

	/**
	 * 修改
	 *@param item 关注你的人与你关注的人
	 */
	Follow update(Follow item);

	/**
	 * 删除
	 *@param item 关注你的人与你关注的人
	 */
	void delByIds(String[] ids);

}