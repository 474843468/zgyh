package com.chinamworld.bocmbci.biz.dept.ydzc;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 定期约定转存--详情页面----未建立约定 */
public class DeptYdzcQueryDetailActivity extends DeptBaseActivity {
	private static final String TAG = "DeptYdzcQueryActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View detailView = null;
	private int position = -1;
	private List<Map<String, String>> accdetailList = null;
	private TextView volumAndText = null;
	private TextView typeText = null;
	private TextView codeAndCashText = null;
	private TextView moneyText = null;
	private TextView interestStartsDateText = null;
	private TextView interestEndDatevolumAndText = null;
	private TextView interestRateText = null;
	private TextView appointStatusText = null;
	private Button queryButton = null;
	/** 1-已建立约定,2-未建立约定 */
	private int appointStatusTag = 0;
	private String accountId = null;
	private String accountNumber = null;
	private String volumeNumber = null;
	private String cdNumber = null;
	/** 1-转存约定查询，2-撤销约定转存 */
	private int gotoTag = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.dept_dqyzc_title));
		ibRight.setVisibility(View.VISIBLE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		detailView = LayoutInflater.from(this).inflate(R.layout.dept_dqydzc_query_detail, null);
		tabcontent.addView(detailView);
		position = getIntent().getIntExtra(ConstantGloble.POSITION, -1);
		accdetailList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.DEPT_ACCDETAILLIST);
		accountId = getIntent().getStringExtra(Comm.ACCOUNT_ID);
		accountNumber = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);
		if (position < 0 || StringUtil.isNullOrEmpty(accdetailList)) {
			return;
		}
		init();
		initOnclick();
	}

	private void init() {
		volumAndText = (TextView) findViewById(R.id.dept_dqyzc_cdandvolum);
		typeText = (TextView) findViewById(R.id.dept_dqyzc_detail_type);
		codeAndCashText = (TextView) findViewById(R.id.dept_dqyzc_codeandcash);
		moneyText = (TextView) findViewById(R.id.dept_dqyzc_money);
		interestStartsDateText = (TextView) findViewById(R.id.dept_dqyzc_detail_interestStartsDate);
		interestEndDatevolumAndText = (TextView) findViewById(R.id.dept_dqyzc_detail_interestEndDate);
		interestRateText = (TextView) findViewById(R.id.dept_dqyzc_detail_interestRate);
		appointStatusText = (TextView) findViewById(R.id.dept_dqyzc_status);
		queryButton = (Button) findViewById(R.id.dept_dqyzc_detail_query);
		Map<String, String> map = accdetailList.get(position);
		String type = map.get(Dept.DEPT_TYPE_RES);
		volumeNumber = map.get(Dept.DEPT_VOLUMENUMBER_RES);
		cdNumber = map.get(Dept.DEPT_CDNUMBER_RES);
		String currencyCode = map.get(Dept.DEPT_CURRENCYCODE_RES);
		String cashRemit = map.get(Dept.DEPT_CASHREMIT_RES);
		String availableBalance = map.get(Dept.DEPT_BOOKBALANCE_RES);
		String interestStartsDate = map.get(Dept.DEPT_INTERESTSTARTSDATE_RES);
		String interestEndDate = map.get(Dept.DEPT_INTERESTENDDATE_RES);
		String interestRate = map.get(Dept.DEPT_INTERESTRATE_RES);
		String appointStatus = map.get(Dept.DEPT_APPOINTSTATUS_RES);
		// if (ConstantGloble.COMBINE_FLAG_N.equals(appointStatus)) {
		// // N-未建立约定
		// appointStatusTag = 2;
		// cancelButton.setVisibility(View.GONE);
		// queryButton.setText(getResources().getString(R.string.dept_dqyzc_detail_ok));
		// BottomButtonUtils.setSingleLineStyleRed(queryButton);
		// } else if (ConstantGloble.COMBINE_FLAG_Y.equals(appointStatus)) {
		// // Y-已建立约定
		// appointStatusTag = 1;
		// }

		String types = null;
		if (StringUtil.isNull(type)) {
			types = "-";
		} else {
			if (LocalData.fixAccTypeMap.containsKey(type)) {
				types = LocalData.fixAccTypeMap.get(type);
			} else {
				types = "-";
			}
		}
		String code = null;
		if (StringUtil.isNull(currencyCode)) {
			code = "-";
		} else {
			if (LocalData.Currency.containsKey(currencyCode)) {
				code = LocalData.Currency.get(currencyCode);
			} else {
				code = "-";
			}
		}
		String cash = null;
		if (StringUtil.isNull(cashRemit)) {
			cash = "-";
		} else {
			if (LocalData.CurrencyCashremit.containsKey(cashRemit)) {
				cash = LocalData.CurrencyCashremit.get(cashRemit);
			} else {
				cash = "-";
			}
		}
		String money = null;
		if (StringUtil.isNull(availableBalance)) {
			money = "-";
		} else {
			money = StringUtil.parseStringCodePattern(currencyCode, availableBalance, 2);
		}
		String convertTypes = null;
		if (StringUtil.isNull(appointStatus)) {
			convertTypes = "-";
		} else {
			if (LocalData.appointStatusremitautoFlagMap.containsKey(appointStatus)) {
				convertTypes = LocalData.appointStatusremitautoFlagMap.get(appointStatus);
			} else {
				convertTypes = "-";
			}

		}
		volumAndText.setText(volumeNumber + "/" + cdNumber);
		typeText.setText(types);
		if (LocalData.rmbCodeList.contains(currencyCode)) {
			codeAndCashText.setText(code);
		} else {
			codeAndCashText.setText(code + "/" + cash);
		}
		moneyText.setText(money);
		interestStartsDateText.setText(interestStartsDate);
		interestEndDatevolumAndText.setText(interestEndDate);
		interestRateText.setText(interestRate);
		appointStatusText.setText(convertTypes);
	}

	private void initOnclick() {
		queryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 未建立约定--定期约定转存
				// 查询转账账户
				BaseHttpEngine.showProgressDialog();
				String[] s = { ConstantGloble.ACC_TYPE_BRO, ConstantGloble.ACC_TYPE_ORD, ConstantGloble.ACC_TYPE_RAN };
				requestPsnCommonQueryAllChinaBankAccount(s);

			}
		});
//		// 撤销约定转存
//		cancelButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				gotoTag = 2;
//				BaseHttpEngine.showProgressDialog();
//				requsetPsnTimeDepositAppointQuery(accountId, volumeNumber, cdNumber);
//			}
//		});
	}

	/** 查询转账账户列表---回调 */
	@Override
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(Object resultObj) {
		super.requestPsnCommonQueryAllChinaBankAccountCallBack(resultObj);
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = response.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		List<Map<String, String>> resultList = (List<Map<String, String>>) body.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		Intent intent = new Intent(DeptYdzcQueryDetailActivity.this, DeptYdzcSetingInputActivity.class);
		intent.putExtra(ConstantGloble.POSITION, position);
		intent.putExtra(Comm.ACCOUNT_ID, accountId);
		intent.putExtra(Comm.ACCOUNTNUMBER, accountNumber);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.DEPT_RESULTLIST, resultList);
		startActivity(intent);
		BaseHttpEngine.dissMissProgressDialog();
	}

	/** 定期约定转存查询-----回调 */
//	@Override
//	public void requsetPsnTimeDepositAppointQueryCallback(Object resultObj) {
//		super.requsetPsnTimeDepositAppointQueryCallback(resultObj);
//		BiiResponse response = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = response.getResponse();
//		BiiResponseBody body = biiResponseBodys.get(0);
//		Map<String, String> resultMap = (Map<String, String>) body.getResult();
//		if (StringUtil.isNullOrEmpty(resultMap)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
//			return;
//		}
//		Intent intent = new Intent();
//		if (gotoTag == 1) {
//			// 查询
//			intent.setClass(DeptYdzcQueryDetailActivity.this, DeptYdzcAppointQueryActivity.class);
//		} else if (gotoTag == 2) {
//			// 撤销
//			intent.setClass(DeptYdzcQueryDetailActivity.this, DeptYdzcAppointDeleteConfiemActivity.class);
//			intent.putExtra(Comm.ACCOUNT_ID, accountId);
//		}
//		intent.putExtra(ConstantGloble.POSITION, position);
//		intent.putExtra(Comm.ACCOUNTNUMBER, accountNumber);
//		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.DEPT_RESULTMAP, resultMap);
//		startActivity(intent);
//		BaseHttpEngine.dissMissProgressDialog();
//	}
}
