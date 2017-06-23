package com.chinamworld.bocmbci.biz.gatherinitiative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.gatherinitiative.commenpayer.CommenPayerActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.gatherquery.GatherQueryActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.payquery.PayQueryActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * @ClassName: GatherBaseActivity
 * @Description: 主动收款模块的基类
 * @author JiangWei
 * @date 2013-8-19下午2:11:40
 */
public class GatherBaseActivity extends BaseActivity {
	/** 查询 收款账户 */
	protected static final int QUERY_GATHER_ACCOUNT_CALLBACK = 101;
	/** 查询付款账户列表 */
	protected static final int QUERY_PAY_ACCOUNT_CALLBACK = 102;
	/** 收款指令查询 */
	private static final int GATHER_INSTRUCT_QUERY = 0;
	/** 付款指令查询 */
	private static final int PAY_INSTRUCT_QUERY = 1;
	/** 常用付款人 */
	private static final int COMMON_PAY_PERSON = 2;
	/** 当前选中list中的postion */
	protected static final String CURRENT_POSITION = "currentPosition";
	/** 当前系统时间 */
	protected static final String CURRENT_DATETIME = "currentDateTime";
	/** 是否需要选择账户的 标识*/
	public static final String IS_NEED_CHOOSE_ACCOUNT = "isNeedChooseAccount";
	/** 返回按钮 */
	protected Button ibBack;
	/** 主视图布局 */
	protected LinearLayout tabcontent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		// 加载上边下拉菜单
		initPulldownBtn();
		// 加载底部菜单栏
		initFootMenu();
		// 加载左边菜单栏
		initLeftSideList(this, LocalData.GatherInitiativeLeftList);
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
		String menuId = menuItem.MenuID;
		
		if(menuId.equals("GatherInitiative_1")){//收款指令查询
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof GatherQueryActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						GatherQueryActivity.class);
				context.startActivity(intent);
			}
		}
		else if(menuId.equals("GatherInitiative_2")){//付款指令查询
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof PayQueryActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						PayQueryActivity.class);
				context.startActivity(intent);
			}
		}
		else if(menuId.equals("GatherInitiative_3")){//常用付款人
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof CommenPayerActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						CommenPayerActivity.class);
				context.startActivity(intent);
			}
		}
		return true;
		
//		switch (clickIndex) {
//		//收款指令查询
//		case GATHER_INSTRUCT_QUERY:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof GatherQueryActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//						GatherQueryActivity.class);
//				startActivity(intent);
//			}
//			break;
//		//付款指令查询
//		case PAY_INSTRUCT_QUERY:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof PayQueryActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//						PayQueryActivity.class);
//				startActivity(intent);
//			}
//			break;
//		//常用付款人
//		case COMMON_PAY_PERSON:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof CommenPayerActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//						CommenPayerActivity.class);
//				startActivity(intent);
//			}
//			break;
//
//		default:
//			break;
//		}
	}
	
	/**
	 * @Title: requestGatherAccountList
	 * @Description: 查询 收款账户
	 * @param 
	 * @return void
	 * @throws
	 */
	public void requestGatherAccountList() {
		// 展示通讯框
		
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> accountList = new ArrayList<String>();
		accountList.add(ConstantGloble.ACC_TYPE_BRO);// 借记卡
		accountList.add(ConstantGloble.ACC_TYPE_GRE);// 长城信用卡
		paramsmap.put(Comm.ACCOUNT_TYPE, accountList);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestGatherAccountListCallBack");
	}
	
	/**
	 * @Title: requestTranoutAccountListCallBack
	 * @Description: 查询 “收款账户”的回调
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void requestGatherAccountListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		List<Map<String, Object>> accountOutList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		GatherInitiativeData.getInstance().setQueryAcountCallBackList(accountOutList);
		communicationCallBack(QUERY_GATHER_ACCOUNT_CALLBACK);
	}
	
	
	/**
	 * @Title: requestPayAccountList
	 * @Description: 请求付款账户列表
	 * @param 
	 * @return void
	 * @throws
	 */
	public void requestPayAccountList() {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> accountList = new ArrayList<String>();
		accountList.add(ConstantGloble.ACC_TYPE_BRO);// 借记卡
		accountList.add(ConstantGloble.ACC_TYPE_GRE);// 长城信用卡
		accountList.add(ConstantGloble.ZHONGYIN);// 中银信用卡 103 P601 增加
		paramsmap.put(Comm.ACCOUNT_TYPE, accountList);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestPayAccountListCallBack");
	}
	
	/**
	 * @Title: requestPayAccountListCallBack
	 * @Description: 请求付款账户列表的回调
	 * @param @param resultObj
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void requestPayAccountListCallBack(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> accountInList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		GatherInitiativeData.getInstance().setPayAcountCallBackList(accountInList);
		communicationCallBack(QUERY_PAY_ACCOUNT_CALLBACK);
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
