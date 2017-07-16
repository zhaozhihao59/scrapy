package com.hao.adp.scrapy.reduce.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hao.adp.scrapy.base.aop.MyReduce;
import com.hao.adp.scrapy.base.page.PageResult;
import com.hao.adp.scrapy.base.util.RedisCacheUtil;
import com.hao.adp.scrapy.reduce.dao.IMemberDao;
import com.hao.adp.scrapy.reduce.dto.MemberCondition;
import com.hao.adp.scrapy.reduce.entity.Member;
import com.hao.adp.scrapy.reduce.service.IMemberService;
/**
 * 用户
 * @creator 赵志豪
 * @create-time 2017-07-08 17:06:47
 * @email 490875647@qq.com
 * @version 1.0
 */
@Service("memberServiceImpl")
@Transactional(value = "transactionManager")
public class MemberServiceImpl implements IMemberService{

	@Resource(name = "memberDaoImpl")
	private IMemberDao memberDao;
	@Resource
	private RedisCacheUtil redisCacheUtil;

	/**
	 * 查询所有用户
	 */
	public List<Member> listMemberAll(){
		return memberDao.listMemberAll();
	}

	/**
	 * 查询总数
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	public int getMemberByPageCount(MemberCondition condition){
		return memberDao.getMemberByPageCount(condition);
	}

	/**
	 * 分页查询
	 *@param pageResult 分页对象
	 *@param condition 查询条件类
	 */
	public void listMemberByPage(PageResult<Member> pageResult,MemberCondition condition){
		int rows = memberDao.getMemberByPageCount(condition);
		pageResult.setRows(rows);
		RowBounds rowBounds = new RowBounds(pageResult.getCurrentPageIndex(),pageResult.getPageSize());
		List<Member> list = memberDao.listMemberByPage(rowBounds,condition);
		pageResult.setResult(list);
	}

	/**
	 * 根据ID查询
	 *@param id 主键
	 *@return 用户
	 */
	public Member getMemberById(Long id){
		return memberDao.getMemberById(id);
	}

	/**
	 * 新增
	 *@param item 用户
	 */
	@MyReduce(cacheName = "addMember" , key = "#item.userId")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Member add(Member item){
		Member val = memberDao.getMemberByUserId(item.getUserId());
		if(val != null){
			return val;
		}
		
		memberDao.add(item);
		return item;
	}

	/**
	 * 批量新增
	 *@param List 
	 */
	public void batchInsert(List<Member> arrayList){

		memberDao.batchInsert(arrayList);
	}

	/**
	 * 修改
	 *@param item 用户
	 */
	public Member update(Member item){
		memberDao.update(item);
		return item;
	}

	/**
	 * 删除
	 *@param item 用户
	 */
	public void delByIds(String[] ids){
		memberDao.delByIds(ids);
	}

	@Override
	public void reload() {
		MemberCondition condition = new MemberCondition();
		int count = getMemberByPageCount(condition);
		PageResult<Member> pageResult = new PageResult<>();
		
		pageResult.setRows(count);
		pageResult.setPageSize(500);
		int page = pageResult.getAllPages();
		for (int i = 1; i <= page; i++) {
			pageResult.setCurrentPage(i);
			RowBounds rowBounds = new RowBounds(pageResult.getCurrentPageIndex(),pageResult.getPageSize());
			List<Member> list = memberDao.listMemberByPage(rowBounds,condition);
			for (Member item : list) {
				redisCacheUtil.putCacheValue("reduceMemberVal", "reduceMemberVal" + item.getUserId(), item);
			}
		}
		
		
	}

}