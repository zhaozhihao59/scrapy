package com.hao.adp.scrapy.base.page;

public abstract class Dialect {

	public static enum Type{
		MYSQL
	}
	
	public abstract String getLimitString(String sql, int skipResults, int maxResults);
	
}
