package com.bilibili.adp.scrapy.reduce.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.bilibili.adp.scrapy.reduce.entity.Follow;
import com.bilibili.adp.scrapy.reduce.dto.FollowCondition;
/**
 * 关注你的人与你关注的人
 * @creator 赵志豪
 * @create-time 2017-07-08 17:06:47
 * @email 490875647@qq.com
 * @version 1.0
 */
@Repository("followDaoImpl")
public interface IFollowDao{

	/**
	 * 查询所有关注你的人与你关注的人
	 */
	List<Follow> listFollowAll();

	/**
	 * 查询总数
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	int getFollowByPageCount(@Param("condition") FollowCondition condition);

	/**
	 * 分页查询
	 *@param bounds RowBounds对象
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	List<Follow> listFollowByPage(RowBounds bounds,@Param("condition") FollowCondition condition);

	/**
	 * 根据ID查询
	 *@param id 主键
	 *@return 关注你的人与你关注的人
	 */
	Follow getFollowById(@Param("id") Long id);

	/**
	 * 新增
	 *@param item 关注你的人与你关注的人
	 */
	void add(Follow item);

	/**
	 * 批量新增
	 *@param List 
	 */
	void batchInsert(List<Follow> arrayList);

	/**
	 * 修改
	 *@param item 关注你的人与你关注的人
	 */
	void update(Follow item);

	/**
	 * 删除
	 *@param item 关注你的人与你关注的人
	 */
	void delByIds(String[] ids);

	Follow getFollowByItem(@Param("item")Follow item);

}
