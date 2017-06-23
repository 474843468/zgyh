package com.chinamworld.bocmbci.biz.drawmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivitySwitcher;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.biz.drawmoney.drawfromagencey.DrawInputInfoAcitivity;
import com.chinamworld.bocmbci.biz.drawmoney.drawquery.DrawQueryAcitivity;
import com.chinamworld.bocmbci.biz.drawmoney.remitout.RemitCardListActivity;
import com.chinamworld.bocmbci.biz.drawmoney.remitquery.RemitQueryChooseCardActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DrawBaseActivity
 * @Description: 取款模块的基类
 * @author JiangWei
 * @date 2013-7-15 下午2:27:28
 */
public class DrawBaseActivity extends BaseActivity {
	
	public static String IsSigned = "isSigned";
	
	/** 汇往收款人 */
	private static final int REMIT_OUT = 0;
	/** 汇出查询 */
	private static final int REMIT_QUERY = 1;
	/** 代理点取款 */
	private static final int DRAW_FROM_AGENCEY = 2;
	/** 取款查询 */
	private static final int DRAW_QUERY = 3;
	/** 返回按钮 */
	protected Button ibBack;
	/** 主视图布局 */
	protected LinearLayout tabcontent;
	/** 获取所有中国银行账户返回标识 */
	protected static final int GET_ACCOUNT_IN_CALLBACK = 51;
	/** 获取是否签约代理点 */
	protected static final int GET_IS_SIGNED_CALLBACK = 52;
	/** 当前选中list中的postion */
	protected static final String CURRENT_POSITION = "currentPosition";
	/** 当前系统时间 */
	protected static final String CURRENT_DATETIME = "currentDateTime";
	/** 省份id*/
	protected static final String PROVINCE_ID = "provinceId";
	/** 省份名称 */
	protected static final String PROVINCE = "province";
//	/** 是否签约代理点 */
//	protected boolean isSigned;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		curActivity = this;
		setContentView(R.layout.biz_activity_layout);
		// 加载上边下拉菜单
		initPulldownBtn();
		// 加载底部菜单栏
		initFootMenu();
		// 加载左边菜单栏
		if(ActivitySwitcher.isSigned){
			initLeftSideList(this, LocalData.DrawMoneyLeftList);
		}else{
			initLeftSideList(this, LocalData.DrawMoneyLeftListNoSigned);
		}
		
		// 主布局
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		// 左上角的返回按钮
		ibBack = (Button) this.findViewById(R.id.ib_back);
		ibBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		curActivity = context;
		String menuId = menuItem.MenuID;
		if(menuId.equals("DrawMoney_1") || menuId.equals("DrawMoneyLeftListNoSigned_1")){//汇往取款人
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof RemitCardListActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						RemitCardListActivity.class);
				context.startActivity(intent);
			}
		}
		else if(menuId.equals("DrawMoney_2") || menuId.equals("DrawMoneyLeftListNoSigned_2")){//汇出查询
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof RemitQueryChooseCardActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						RemitQueryChooseCardActivity.class);
				context.startActivity(intent);
			}
		}
		else if(menuId.equals("DrawMoney_3")){//代理点取款
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof DrawInputInfoAcitivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						DrawInputInfoAcitivity.class);
				context.startActivity(intent);
			}
		}
		else if(menuId.equals("DrawMoney_4")){//取款查询
			requestSystemDateTimeForDrawQuery();
		}
		return true;
		
//		switch (clickIndex) {
//		//汇往取款人
//		case REMIT_OUT:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof RemitCardListActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//						RemitCardListActivity.class);
//				startActivity(intent);
//			}
//			break;
//		//汇出查询
//		case REMIT_QUERY:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof RemitQueryChooseCardActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//						RemitQueryChooseCardActivity.class);
//				startActivity(intent);
//			}
//			break;
//		//代理点取款
//		case DRAW_FROM_AGENCEY:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof DrawInputInfoAcitivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//						DrawInputInfoAcitivity.class);
//				startActivity(intent);
//			}
//			break;
//		//取款查询
//		case DRAW_QUERY:
//			requestSystemDateTimeForDrawQuery();
//			break;
//
//		default:
//			break;
//		}
	}
	
	private void requestSystemDateTimeForDrawQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_SYSTEM_TIME);
		HttpManager.requestBii(biiRequestBody, this,
				"requestSystemDateTimeForDrawQueryCallBack");
	}
	
	public void requestSystemDateTimeForDrawQueryCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof DrawQueryAcitivity)) {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent();
			intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
					DrawQueryAcitivity.class);
			intent.putExtra(CURRENT_DATETIME, dateTime);
			curActivity.startActivity(intent);
		}
	}
	
	/**
	 * @Title: requestPsnMobileIsSignedAgent
	 * @Description: 请求“代理点签约判断”接口
	 * @param
	 * @return void
	 */
	public void requestDrawPsnMobileIsSignedAgent() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(DrawMoney.PSN_MOBILE_IS_SIGNED_AGENT);
		Map<String, Object> map = new HashMap<String, Object>();
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestDrawPsnMobileIsSignedAgentCallback");
	}
	
	/**
	 * @Title: requestPsnMobileIsSignedAgentCallback
	 * @Description: 请求“代理点签约判断”接口的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requestDrawPsnMobileIsSignedAgentCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String flag = (String) result.get(DrawMoney.FLAG);
		if (flag.equals("true")) {
			ActivitySwitcher.isSigned = true;
		} else {
			ActivitySwitcher.isSigned = false;
		}
		communicationCallBack(GET_IS_SIGNED_CALLBACK);
	}
	
	/**
	 * @Title: requestForCardList
	 * @Description: 请求中行手机取款相关账户列表
	 * @param  
	 * @return void
	 */
	public void requestForCardList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> accountList = new ArrayList<String>();
		accountList.add(ConstantGloble.ACC_TYPE_BRO);// 借记卡
		accountList.add(ConstantGloble.ACC_TYPE_ORD);// 普通活期
		accountList.add(ConstantGloble.ACC_TYPE_RAN);// 活期一本通 
		paramsmap.put(Comm.ACCOUNT_TYPE, accountList);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCardListCallBack");
	}
	
	/**
	 * @Title: requestForCardListCallBack
	 * @Description: 请求中行手机取款相关账户列表返回
	 * @param @param resultObj 
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestForCardListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> tranoutList = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		DrawMoneyData.getInstance().setAccountList(tranoutList);
		communicationCallBack(GET_ACCOUNT_IN_CALLBACK);
	}
	
	/**
	 * @Title: communicationCallBack
	 * @Description: 通讯回调 需要子类进行覆盖
	 * @param @param flag 
	 * @return void
	 */
	public void communicationCallBack(int flag) {
		//子类进行覆盖
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}

}
