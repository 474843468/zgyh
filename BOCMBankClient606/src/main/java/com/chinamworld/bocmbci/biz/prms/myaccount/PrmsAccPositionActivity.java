package com.chinamworld.bocmbci.biz.prms.myaccount;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.biz.prms.adapter.PrmsAccBalanceAdapter401;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 我的账户贵金属      持仓状况
 * 
 * @author fsm
 * 
 */
public class PrmsAccPositionActivity extends PrmsBaseActivity implements OnClickListener {
	/**
	 * 我的账户贵金属
	 */
	private static final String TAG = "PrmsAccPositionActivity";
	/**
	 * 显示贵金属账户的View
	 */
	private TextView accNum;
	/**
	 * 贵金属交易账户余额 弹窗时显示的listview 数据
	 */
	private List<Map<String, Object>> dataList = null;
	/**
	 * 账户号码
	 */
	private String accNumStr;
	private String accTypeStr, nickName;
	private PrmsAccBalanceAdapter401 adapter;
	/**
	 * 贵金属账户余额显示
	 */
	private ListView gridView;
	/**
	 * 重置的dialog
	 */
	private CustomDialog accBalanceDialog = null;
	/**
	 * 重设贵金属账户按钮监听事件
	 */
	private OnClickListener resetOnClickListener;

	private int flag;
	private View noBalanceView;
	private final static int CREATE = 1;
	private final static int RESUME = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		flag = CREATE;
		setTitle(getResources().getString(R.string.prms_title_acc));
		if (getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST, false)) {
			BaseHttpEngine.showProgressDialogCanGoBack();
			checkRequestPsnInvestmentManageIsOpen();
		} else {
			// 查询账户余额
			BaseHttpEngine.showProgressDialogCanGoBack();
			queryPrmsAcc();
		}
		setLeftSelectedPosition("prmsManage_2");
	}

	public void checkRequestPsnInvestmentManageIsOpen() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "checkRequestPsnInvestmentManageIsOpenCallback");
	}

	public void checkRequestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isOpenOr = (String) biiResponseBody.getResult();
		if (StringUtil.parseStrToBoolean(isOpenOr)) {
			prmsControl.ifInvestMent = true;
		} else {
			prmsControl.ifInvestMent = false;
		}
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
			prmsControl.accMessage = (Map<String, String>) biiResponseBodys.get(0).getResult();
			prmsControl.accId = String.valueOf(prmsControl.accMessage.get(Prms.QUERY_PRMSACC_ACCOUNTID));
			prmsControl.accNum = String.valueOf(prmsControl.accMessage.get(Prms.QUERY_PRMSACC_ACCOUNT));
			prmsControl.ifhavPrmsAcc = true;
		}
		if (!prmsControl.ifhavPrmsAcc || !prmsControl.ifInvestMent) {
			BaseHttpEngine.dissMissProgressDialog();
			getPopup();
		} else {
			BaseHttpEngine.showProgressDialog();
			init();
			queryPrmsAccBalance();
		}
	}

	@Override
	public void queryPrmsAccBalanceCallBack(Object resultObj) {
		super.queryPrmsAccBalanceCallBack(resultObj);
		dataList = getAccData(prmsControl.accBalanceList, 2);
		BaseHttpEngine.dissMissProgressDialog();
		if (dataList == null || dataList.size() == 0) {
			noBalanceView.setVisibility(View.VISIBLE);
			((TextView)noBalanceView.findViewById(R.id.no_banlance_alert))
			.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
		} else {
			noBalanceView.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
			adapter = new PrmsAccBalanceAdapter401(this, dataList);
			gridView.setAdapter(adapter);
		}
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("prmsManage_2");
//	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	void init() {
		tabcontent.setPadding(0, 0, 0, 0);
		String title = getResources().getString(R.string.prms_title_acc);
		setTitle(title);
		View v = mainInflater.inflate(R.layout.prms_acc, null);
		tabcontent.addView(v);
		accNum = (TextView) findViewById(R.id.prms_acc_number);
		accNum.setOnClickListener(this);
		gridView = (ListView) findViewById(R.id.gridView1);
		noBalanceView = (View) findViewById(R.id.prms_noavaliblebalance_ll);
		String temp = getResources().getString(R.string.prms_str_acc);
		accNumStr = prmsControl.accNum;
		accTypeStr = prmsControl.accMessage.get(Prms.PRMS_ACCOUNTTYPE);
		nickName = prmsControl.accMessage.get(Prms.QUERY_PRMSACC_ACCOUNTNICKNAME);
		if (dataList == null || dataList.size() == 0) {
			noBalanceView.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
		} else {
			noBalanceView.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
			adapter = new PrmsAccBalanceAdapter401(this, dataList);
			gridView.setAdapter(adapter);
		}

		if (accNumStr != null) {
			accNum.setText(temp + StringUtil.getForSixForString(accNumStr));
		}
		// 点击重设贵金属账户 跳转到页面
		resetOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				PrmsAccPositionActivity.this.accBalanceDialog.dismiss();
				BaseHttpEngine.showProgressDialog();
				queryPrmsAccs();
			}
		};
		right.setVisibility(View.GONE);
	}

	@Override
	public void queryPrmsAccsCallBack(Object resultObj) {
		super.queryPrmsAccsCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.prms_noprmsAcc_errors));
			return;
		} else {
			prmsControl.prmsAccList = (List<Map<String, String>>) (biiResponseBody.getResult());
			startActivityForResult(new Intent(this, PrmsAccSettingActivity.class),
					ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE);
			BaseHttpEngine.dissMissProgressDialog();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE:// 设置贵金属账户
			switch (resultCode) {
			case RESULT_OK:
				flag = CREATE;
				prmsControl.ifhavPrmsAcc = true;
				BaseDroidApp.getInstanse().dismissMessageDialog();
				BaseHttpEngine.showProgressDialogCanGoBack();
				noBalanceView.setVisibility(View.VISIBLE);
				gridView.setVisibility(View.GONE);
				dataList = null;
				queryPrmsAcc();
				break;
			default:
				prmsControl.ifhavPrmsAcc = false;
				getPopup();
				break;
			}
			break;
		case ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC1_CODE:// 重设设置贵金属账户
			switch (resultCode) {
			case RESULT_OK:
				flag = RESUME;
				prmsControl.ifhavPrmsAcc = true;
				BaseDroidApp.getInstanse().dismissMessageDialog();
				BaseHttpEngine.showProgressDialogCanGoBack();
				noBalanceView.setVisibility(View.VISIBLE);
				gridView.setVisibility(View.GONE);
				dataList = null;
				queryPrmsAcc();
				break;
			default:
				break;
			}
			break;
		case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
			LogGloble.d(TAG, resultCode + "++++++++++++++++++++=");
			switch (resultCode) {
			case RESULT_OK:
				prmsControl.ifInvestMent = true;
				if (prmsControl.ifhavPrmsAcc) {
					flag = CREATE;
					BaseDroidApp.getInstanse().dismissMessageDialog();
					BaseHttpEngine.showProgressDialogCanGoBack();
					queryPrmsAcc();
				} else {
					getPopup();
				}
				break;
			default:
				prmsControl.ifInvestMent = false;
				getPopup();
				break;
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 弹出页面
	 * 
	 * @Author xyl
	 * @param dataList
	 *            listview中的数据
	 * @param onclicklistener
	 *            重设 贵金属账户onclick事件
	 */
	public void createAccBalanceDialog() {
		Intent intent = new Intent();
		intent.setClass(this, PrmsAccDetailDialogActivity.class);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC1_CODE);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.prms_acc_number:
			createAccBalanceDialog();
			break;
		case R.id.ib_top_right_btn:
			// BaseDroidApp.getInstanse().showSelectBuyOrSaleDialog(getString(R.string.prms_buy),getString(R.string.prms_sale),rightBtnOnClickListenerForTrade);
			break;

		default:
			break;
		}

	}
}
