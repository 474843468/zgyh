package com.chinamworld.bocmbci.biz.prms.myaccount;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 我的账户贵金属三级菜单页
 * 
 * @author fsm
 * 
 */
public class PrmsAccMeneActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsAccMeneActivity";
	/**
	 * 资金账户信息,持仓状况
	 */
	private View prmsAccCurrencyInfo, prmsAccPositionInfo;

	private int flag;
	private static final int ACCCURRENCYINFO = 1;
	private static final int ACCPOSITIONINFO = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		prmsControl.cleanAll();
		setLeftSelectedPosition("prmsManage_2");
	}

	private void init() {
		initView();
		initOnclickListenner();
	}

	/**
	 * 初始化界面
	 * 
	 * @Author xyl
	 */
	private void initView() {
		View childView = mainInflater.inflate(R.layout.prms_acc_menu, null);
		tabcontent.addView(childView);
		prmsAccCurrencyInfo = findViewById(R.id.prms_acc_currency_info_ll);
		prmsAccPositionInfo = findViewById(R.id.prms_acc_position_info_ll);
		right.setVisibility(View.GONE);
		// 设置标题
		setTitle(R.string.prms_title_acc);
	}

	/**
	 * 添加监听
	 */
	private void initOnclickListenner() {
		prmsAccCurrencyInfo.setOnClickListener(this);
		prmsAccPositionInfo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.prms_acc_currency_info_ll:// 资金账户信息
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(PrmsAccMeneActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(PrmsAccMeneActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			flag = ACCCURRENCYINFO;
			BaseHttpEngine.showProgressDialog();
			checkRequestPsnInvestmentManageIsOpen();
//			startActivity(new Intent(this, PrmsAccActivity401.class));
			break;
		case R.id.prms_acc_position_info_ll:// 持仓状况
			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(PrmsAccMeneActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(PrmsAccMeneActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}
			flag = ACCPOSITIONINFO;
			BaseHttpEngine.showProgressDialog();
			checkRequestPsnInvestmentManageIsOpen();
//			startActivity(new Intent(this, PrmsAccPositionActivity.class));
			break;
		default:
			break;
		}
	}
	
	@Override
	public void checkRequestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		super.checkRequestPsnInvestmentManageIsOpenCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isOpenOr = (String) biiResponseBody.getResult();
		prmsControl.ifInvestMent = StringUtil.parseStrToBoolean(isOpenOr);
		queryPrmsAcc();
	}
	
	@Override
	public void queryPrmsAccCallBack(Object resultObj) {
		super.queryPrmsAccCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getResult() == null) {
			prmsControl.ifhavPrmsAcc = false;
			prmsControl.accMessage = null;
			prmsControl.accId = null;
		} else {
			prmsControl.accMessage = (Map<String, String>) biiResponseBodys
					.get(0).getResult();
			prmsControl.accNum = String.valueOf(prmsControl.accMessage
					.get(Prms.QUERY_PRMSACC_ACCOUNT));
			prmsControl.accId = String.valueOf(prmsControl.accMessage
					.get(Prms.QUERY_PRMSACC_ACCOUNTID));
			prmsControl.ifhavPrmsAcc = true;
		}
		if (!prmsControl.ifhavPrmsAcc || !prmsControl.ifInvestMent) {
			BaseHttpEngine.dissMissProgressDialog();
			getPopup();
		} else {
			switch (flag) {
			case ACCCURRENCYINFO:
				BaseHttpEngine.dissMissProgressDialog();
				startActivity(new Intent(this, PrmsAccActivity401.class));
				break;
			case ACCPOSITIONINFO:
				BaseHttpEngine.dissMissProgressDialog();
				startActivity(new Intent(this, PrmsAccPositionActivity.class));
				break;
			default:
				break;
			}
		}
	}

}
