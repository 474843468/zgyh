package com.chinamworld.bocmbci.biz.peopleservice.utils;

/** 带参数的 回调接口  现用于标签查找时，条件判断回调 */
public interface IFunction{
	
   <T> boolean func(T t);
	
}
