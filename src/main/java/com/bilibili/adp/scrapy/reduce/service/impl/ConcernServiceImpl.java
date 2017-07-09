package com.bilibili.adp.scrapy.reduce.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bilibili.adp.scrapy.base.aop.MyReduce;
import com.bilibili.adp.scrapy.base.page.PageResult;
import com.bilibili.adp.scrapy.reduce.dao.IConcernDao;
import com.bilibili.adp.scrapy.reduce.dto.ConcernCondition;
import com.bilibili.adp.scrapy.reduce.entity.Concern;
import com.bilibili.adp.scrapy.reduce.service.IConcernService;

/**
 * 你关注的人
 * @creator 赵志豪
 * @create-time 2017-07-08 22:05:54
 * @email 490875647@qq.com
 * @version 1.0
 */
@Service("concernServiceImpl")
@Transactional(value = "transactionManager")
public class ConcernServiceImpl implements IConcernService{

	@Resource(name = "concernDaoImpl")
	private IConcernDao concernDao;



	/**
	 * 查询所有你关注的人
	 */
	public List<Concern> listConcernAll(){
		return concernDao.listConcernAll();
	}

	/**
	 * 查询总数
	 *@param condition 查询条件类
	 *@return 总条数
	 */
	public int getConcernByPageCount(ConcernCondition condition){
		return concernDao.getConcernByPageCount(condition);
	}

	/**
	 * 分页查询
	 *@param pageResult 分页对象
	 *@param condition 查询条件类
	 */
	public void listConcernByPage(PageResult<Concern> pageResult,ConcernCondition condition){
		int rows = concernDao.getConcernByPageCount(condition);
		pageResult.setRows(rows);
		RowBounds rowBounds = new RowBounds(pageResult.getCurrentPageIndex(),pageResult.getPageSize());
		List<Concern> list = concernDao.listConcernByPage(rowBounds,condition);
		pageResult.setResult(list);
	}

	/**
	 * 根据ID查询
	 *@param id 主键
	 *@return 你关注的人
	 */
	public Concern getConcernById(Long id){
		return concernDao.getConcernById(id);
	}

	/**
	 * 新增
	 *@param item 你关注的人
	 */
	@MyReduce(cacheName = "addConcern" ,key = "#item.menberId + #item.followId")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public  Concern add(Concern item){
		Concern val = concernDao.getConcernByItem(item);
		if(val != null){
			return val;
		}
		concernDao.add(item);
		return item;
	}

	/**
	 * 批量新增
	 *@param List 
	 */
	public void batchInsert(List<Concern> arrayList){

		concernDao.batchInsert(arrayList);
	}

	/**
	 * 修改
	 *@param item 你关注的人
	 */
	public Concern update(Concern item){
		concernDao.update(item);
		return item;
	}

	/**
	 * 删除
	 *@param item 你关注的人
	 */
	public void delByIds(String[] ids){
		concernDao.delByIds(ids);
	}
	
	

}