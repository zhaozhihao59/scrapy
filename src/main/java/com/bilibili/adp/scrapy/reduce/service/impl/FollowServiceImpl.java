package com.bilibili.adp.scrapy.reduce.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bilibili.adp.scrapy.base.aop.MyReduce;
import com.bilibili.adp.scrapy.base.page.PageResult;
import com.bilibili.adp.scrapy.reduce.dao.IFollowDao;
import com.bilibili.adp.scrapy.reduce.dto.FollowCondition;
import com.bilibili.adp.scrapy.reduce.entity.Follow;
import com.bilibili.adp.scrapy.reduce.service.IFollowService;
/**
 * 关注你的人与你关注的人
 * @creator 赵志豪
 * @create-time 2017-07-08 17:06:47
 * @email 490875647@qq.com
 * @version 1.0
 */
@Service("followServiceImpl")
@Transactional(value = "transactionManager")
public class FollowServiceImpl implements IFollowService{

	@Resource(name = "followDaoImpl")
	private IFollowDao followDao;



	/**
	 * 查询所有关注你的人与你关注的人
	 */
	public List<Follow> listFollowAll(){
		return followDao.listFollowAll();
	}

	/**
	 * 查询总数
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	public int getFollowByPageCount(FollowCondition condition){
		return followDao.getFollowByPageCount(condition);
	}

	/**
	 * 分页查询
	 *@param pageResult 分页对象
	 *@param condition 查询条件类
	 */
	public void listFollowByPage(PageResult<Follow> pageResult,FollowCondition condition){
		int rows = followDao.getFollowByPageCount(condition);
		pageResult.setRows(rows);
		RowBounds rowBounds = new RowBounds(pageResult.getCurrentPageIndex(),pageResult.getPageSize());
		List<Follow> list = followDao.listFollowByPage(rowBounds,condition);
		pageResult.setResult(list);
	}

	/**
	 * 根据ID查询
	 *@param id 主键
	 *@return 关注你的人与你关注的人
	 */
	public Follow getFollowById(Long id){
		return followDao.getFollowById(id);
	}

	/**
	 * 新增
	 *@param item 关注你的人与你关注的人
	 */
	@MyReduce(cacheName = "addFollow" ,key = "#item.menberId + #item.followId")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public  Follow add(Follow item){
		Follow val = followDao.getFollowByItem(item);
		if(val != null){
			return val;
		}
		followDao.add(item);
		return item;
	}

	/**
	 * 批量新增
	 *@param List 
	 */
	public void batchInsert(List<Follow> arrayList){

		followDao.batchInsert(arrayList);
	}

	/**
	 * 修改
	 *@param item 关注你的人与你关注的人
	 */
	public Follow update(Follow item){
		followDao.update(item);
		return item;
	}

	/**
	 * 删除
	 *@param item 关注你的人与你关注的人
	 */
	public void delByIds(String[] ids){
		followDao.delByIds(ids);
	}

}