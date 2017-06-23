package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 明细查询详情页面 */
public class RemitQueryMealDetailDetailActivity extends TranBaseActivity {
	private static final String TAG = "RemitQueryMealDetailDetailActivity";
	private TextView signTypeText = null;
	private TextView autoFlagText = null;
	private TextView reSignTypeText = null;
	private TextView mobileText = null;
	private TextView startDateText = null;
	private TextView endDateText = null;
	private TextView channelNameText = null;
	private List<Map<String, String>> resultList = null;
	private int position = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getString(R.string.trans_remit_menu_four));
		View view = mInflater.inflate(R.layout.tran_remit_query_detail_detail, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		toprightBtn();
		resultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(Tran.TRAN_RESULTLIST_RES);
		if (StringUtil.isNullOrEmpty(resultList)) {
			return;
		}
		position = getIntent().getIntExtra(ConstantGloble.FINC_SELECTEDPOSITION, -1);
		if (position < 0) {
			return;
		}
		LogGloble.d(TAG, "position======+" + position);
		init();
		setValue();
	}

	private void init() {
		signTypeText = (TextView) findViewById(R.id.tran_remit_n_query_signType);
		autoFlagText = (TextView) findViewById(R.id.tran_remit_n_query_autoFlag);
		reSignTypeText = (TextView) findViewById(R.id.tran_remit_n_query_reSignType);
		mobileText = (TextView) findViewById(R.id.tran_remit_n_query_mobile);
		startDateText = (TextView) findViewById(R.id.tran_remit_n_query_startDate);
		endDateText = (TextView) findViewById(R.id.tran_remit_n_query_endDate);
		channelNameText = (TextView) findViewById(R.id.tran_remit_n_query_channelName);
	}

	private void setValue() {
		Map<String, String> map = resultList.get(position);
		// 交易类型
		String tranType = map.get(Tran.TRAN_TRANTYPE_RES);
		String tranTime = map.get(Tran.TRAN_TRANTIME_RES);
		// 交易状态
		String tranStatus = map.get(Tran.TRAN_TRANSTATUS_RES);
		String tranId = map.get(Tran.TRAN_TRANID_RES);
		String tranDate = map.get(Tran.TRAN_TRANDATE_RES);
		String accNo = map.get(Tran.TRAN_ACCNO_RES);
		// 业务类型
		String bussinessType = map.get(Tran.TRAN_BUSSINESSTYPE_RES);
		// 交易金额
		String tranAmt = map.get(Tran.TRAN_TRANAMT_RES);
		// 交易类型
		String type = null;
		if (StringUtil.isNull(tranType) || !LocalData.remitTranTypeMap.containsKey(tranType)) {
			type = "-";
		} else {
			type = LocalData.remitTranTypeMap.get(tranType);
		}
		// 交易状态
		String status = null;
		if (StringUtil.isNull(tranStatus) || !LocalData.remitTranStatusMap.containsKey(tranStatus)) {
			status = "-";
		} else {
			status = LocalData.remitTranStatusMap.get(tranStatus);
		}
		// 交易类型
		String business = null;
		if (StringUtil.isNull(bussinessType) || !LocalData.remitBussinessTypeMap.containsKey(bussinessType)) {
			business = "-";
		} else {
			business = LocalData.remitBussinessTypeMap.get(bussinessType);
		}
		signTypeText.setText(type);
		autoFlagText.setText(tranDate + " " + tranTime);
		reSignTypeText.setText(status);
		mobileText.setText(tranId);
		String account = null;
		if (StringUtil.isNull(accNo)) {
			account = "-";
		} else {
			account = StringUtil.getForSixForString(accNo);
		}
		startDateText.setText(account);
		endDateText.setText(business);
		String amount = null;
		if (StringUtil.isNull(tranAmt)) {
			amount = "-";
		} else {
			amount = StringUtil.parseStringPattern(tranAmt, 2);
		}
		channelNameText.setText(amount);
	}
}
