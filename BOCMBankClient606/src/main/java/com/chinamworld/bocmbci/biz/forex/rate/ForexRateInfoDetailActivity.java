package com.chinamworld.bocmbci.biz.forex.rate;

import android.os.Bundle;

import com.chinamworld.bocmbci.biz.forex.ForexBaseActivity;
import com.chinamworld.bocmbci.log.LogGloble;


/**
 * 外汇行情--详情页面 走势图
 * 
 * @author 宁焰红
 * 
 */
public class ForexRateInfoDetailActivity extends ForexBaseActivity {
	private static final String TAG = "ForexRateInfoDetailActivity";
	/**
	 * 区别是我的外汇还是成交查询发出的请求 1-我的外汇 2-成交查询
	 */
	private int customerOrQuery = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
	}

}
