package com.hao.adp.scrapy.reduce.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.hao.adp.scrapy.reduce.dto.ConcernCondition;
import com.hao.adp.scrapy.reduce.entity.Concern;
/**
 * 你关注的人
 * @creator 赵志豪
 * @create-time 2017-07-08 22:05:54
 * @email 490875647@qq.com
 * @version 1.0
 */
@Repository("concernDaoImpl")
public interface IConcernDao{

	/**
	 * 查询所有你关注的人
	 */
	List<Concern> listConcernAll();

	/**
	 * 查询总数
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	int getConcernByPageCount(@Param("condition") ConcernCondition condition);
	/**
	 * 分页查询
	 *@param bounds RowBounds对象
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	List<Concern> listConcernByPage(@Param("condition") ConcernCondition condition);
	/**
	 * 分页查询
	 *@param bounds RowBounds对象
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	List<Concern> listConcernByPage(RowBounds bounds,@Param("condition") ConcernCondition condition);

	/**
	 * 根据ID查询
	 *@param id 主键
	 *@return 你关注的人
	 */
	Concern getConcernById(@Param("id") Long id);

	/**
	 * 新增
	 *@param item 你关注的人
	 */
	void add(Concern item);

	/**
	 * 批量新增
	 *@param List 
	 */
	void batchInsert(List<Concern> arrayList);

	/**
	 * 修改
	 *@param item 你关注的人
	 */
	void update(Concern item);

	/**
	 * 删除
	 *@param item 你关注的人
	 */
	void delByIds(String[] ids);

	Concern getConcernByItem(@Param("item")Concern item);

}
