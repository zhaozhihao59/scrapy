package com.bilibili.adp.scrapy.reduce.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.bilibili.adp.scrapy.reduce.entity.Member;
import com.bilibili.adp.scrapy.reduce.dto.MemberCondition;
/**
 * 用户
 * @creator 赵志豪
 * @create-time 2017-07-08 17:06:47
 * @email 490875647@qq.com
 * @version 1.0
 */
@Repository("memberDaoImpl")
public interface IMemberDao{

	/**
	 * 查询所有用户
	 */
	List<Member> listMemberAll();

	/**
	 * 查询总数
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	int getMemberByPageCount(@Param("condition") MemberCondition condition);

	/**
	 * 分页查询
	 *@param bounds RowBounds对象
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	List<Member> listMemberByPage(RowBounds bounds,@Param("condition") MemberCondition condition);

	/**
	 * 根据ID查询
	 *@param id 主键
	 *@return 用户
	 */
	Member getMemberById(@Param("id") Long id);

	/**
	 * 新增
	 *@param item 用户
	 */
	void add(Member item);

	/**
	 * 批量新增
	 *@param List 
	 */
	void batchInsert(List<Member> arrayList);

	/**
	 * 修改
	 *@param item 用户
	 */
	void update(Member item);

	/**
	 * 删除
	 *@param item 用户
	 */
	void delByIds(String[] ids);

	Member getMemberByUserId(@Param("userId")String userId);

}
