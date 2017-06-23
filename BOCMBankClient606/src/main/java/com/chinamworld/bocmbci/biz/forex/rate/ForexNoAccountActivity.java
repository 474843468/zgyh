package com.chinamworld.bocmbci.biz.forex.rate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 关联新账户    此页面不在使用，在账户设定页面判断加载
 * 
 * @author 宁焰红
 * 
 */
public class ForexNoAccountActivity extends BaseActivity {
	private static final String TAG = "ForexNoAccountActivity";
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示

	/**
	 * ForexNoAccountActivity的主布局
	 */
	private View rateInfoView = null;
	/**
	 * quickTrade：快速交易
	 */
	private Button quickTrade = null;
	/**
	 * 关联新账户
	 */
	private Button accButton = null;
	/** 获取到的conversationId 保存 */
	private String commConversationId;
	/** 存储所有符合条件的账户 */
	List<Map<String, String>> allAccList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		LogGloble.d(TAG, "onCreate");
		// 初始化弹窗按钮
		initPulldownBtn();
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNull(commConversationId)) {
			return;
		}
		init();
		initOnClick();
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		LogGloble.d(TAG, "init");
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		View footView = findViewById(R.id.rl_menu);
		footView.setVisibility(View.GONE);
		Button leftButton = (Button) findViewById(R.id.btn_show);
		leftButton.setVisibility(View.GONE);
		rateInfoView = LayoutInflater.from(ForexNoAccountActivity.this).inflate(R.layout.forex_rate_no_acc, null);
		tabcontent.addView(rateInfoView);
		/** 为界面标题赋值 */
		setTitle(getResources().getString(R.string.forex_rate_setAcc));
		quickTrade = (Button) findViewById(R.id.ib_top_right_btn);
		quickTrade.setVisibility(View.VISIBLE);
		quickTrade.setText(getResources().getString(R.string.forex_rate_close));
		accButton = (Button) findViewById(R.id.forex_acc_addnewacc);
		accButton.setVisibility(View.GONE);//屏蔽自助关联
		allAccList = new ArrayList<Map<String, String>>();
	}

	private void initOnClick() {
		// 关闭按钮
		quickTrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 返回到外汇行情页面
				Intent intent = new Intent(ForexNoAccountActivity.this, ForexRateInfoOutlayActivity.class);
				startActivity(intent);
			}
		});
		// 关联新账户
		accButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(ForexNoAccountActivity.this, AccInputRelevanceAccountActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);	
				
				BusinessModelControl.gotoAccRelevanceAccount(ForexNoAccountActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			// 查询外汇交易账户
			requestPsnForexActAvai();
			break;
		case RESULT_CANCELED:
			// 开通失败的响应
			break;
		}
	}

	/**
	 * 外汇交易账户列表-01
	 */
	private void requestPsnForexActAvai() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Forex.FOREX_PSNFOREXACTAVAI_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnForexActAvaiCallback");
	}

	/**
	 * 外汇交易账户列表----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPsnForexActAvaiCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultList) || resultList == null||resultList.size() <= 0) {
//			Intent intent = new Intent(ForexNoAccountActivity.this, AccInputRelevanceAccountActivity.class);
//			startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
//			if(BusinessModelControl.gotoAccRelevanceAccount(ForexNoAccountActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null))
			//屏蔽自助关联
			finish();
			return;
		} else {
			Intent intent = new Intent(ForexNoAccountActivity.this, ForexAccSettingActivity.class);
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_ACTAVI_RESULT_KEY, resultList);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
}
