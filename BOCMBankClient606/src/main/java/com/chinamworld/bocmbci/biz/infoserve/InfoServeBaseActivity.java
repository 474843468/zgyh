package com.chinamworld.bocmbci.biz.infoserve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;

/**
 * 消息服务的基类
 * 
 * @author xby
 * 
 */
public class InfoServeBaseActivity extends BaseActivity {

	/** 主视图布局 */
	protected LinearLayout tabcontent;// 主Activity显示
	/** 左侧返回按钮 */
	protected Button back;
	/** 右侧按钮点击事件 */
	private OnClickListener rightBtnClick;
	/** 右上角按钮 */
	protected Button btn_right;
	/** 设置按钮 */
	protected Button ib_setting_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();
		Button btn_show = (Button) findViewById(R.id.btn_show);
		btn_show.setVisibility(View.GONE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		btn_right = (Button) findViewById(R.id.ib_top_right_btn);
		back = (Button) findViewById(R.id.ib_back);
		ib_setting_btn = (Button) findViewById(R.id.ib_setting_btn);
		//wuhan 更多模块(设置按钮)
		hineTitlebarLoginButton();
	

	}

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
	public View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.addView(view);
		return view;
	}

	/**
	 * 设置标题栏
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(title);
	}

	/**
	 * @Title: requestForCardList
	 * @Description: 请求中行账户列表
	 * @param
	 * @return void
	 */
	public void requestForCardList() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> accountList = new ArrayList<String>();
		accountList.add(ConstantGloble.ACC_TYPE_BRO);// 借记卡
		accountList.add(ConstantGloble.ACC_TYPE_ORD);// 普通活期
		accountList.add(ConstantGloble.ACC_TYPE_RAN);// 活期一本通
		paramsmap.put(Comm.ACCOUNT_TYPE, accountList);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestForCardListCallBack");
	}

	/**
	 * @Title: requestForCardListCallBack
	 * @Description: 请求中行账户列表返回
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestForCardListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> accountList = (List<Map<String, Object>>) biiResponseBody.getResult();
		InfoServeDataCenter.getInstance().setAccountList(accountList);
	}

	/**
	 * @Title: requestPsnNonFixedProductRemind
	 * @Description:大额到账提醒提醒签约/修改/删除
	 * @param paramsmap
	 * @return void
	 */
	public void requestPsnNonFixedProductRemind(final OnClickListener listener, Map<String, Object> paramsmap) {
		BiiHttpEngine.showProgressDialog(listener);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Push.PSN_NON_FIXED_PRODUCT_REMIND);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnNonFixedProductRemindCallback");
	}

	/**
	 * @Title: requestPsnNonFixedProductRemindCallback
	 * @Description: 大额到账提醒提醒签约/修改/删除的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requestPsnNonFixedProductRemindCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
	}

	/**
	 * @Title: requestPsnXpadDueDateRemindQuery
	 * @Description:理财产品到期提醒签约情况查询
	 * @param
	 * @return void
	 */
	public void requestPsnXpadDueDateRemindQuery() {
		BiiHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Push.PSN_XPAD_DUE_DATE_REMIND_QUERY);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Push.REMAIND_DUERY_TYPE, "4");
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnXpadDueDateRemindQueryCallback");
	}

	/**
	 * @Title: requestPsnXpadDueDateRemindQueryCallback
	 * @Description: 理财产品到期提醒签约情况查询的回调
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadDueDateRemindQueryCallback(Object resultObj) {

	}
	
	/**
	 * wuhan 更多模块
	 */
	@Override
	protected void onResume() {
		super.onResume();
		hineTitlebarLoginButton();
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}

}
