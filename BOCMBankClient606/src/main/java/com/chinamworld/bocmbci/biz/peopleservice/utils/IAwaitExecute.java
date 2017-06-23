package com.chinamworld.bocmbci.biz.peopleservice.utils;

import java.util.Map;

import android.view.View;


/** 等待执行标签 */
public interface IAwaitExecute {
	public boolean awaitExecute(Map<String, String> dbMap, View view,IExecuteFuction executeCallBack);
}
