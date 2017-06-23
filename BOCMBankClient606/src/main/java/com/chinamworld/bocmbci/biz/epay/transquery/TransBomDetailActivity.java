package com.chinamworld.bocmbci.biz.epay.transquery;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TQConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 支付交易查询-电子支付详情页面
 * 
 * @author Administrator
 * 
 */
public class TransBomDetailActivity extends EPayBaseActivity {

	private String tag = "TransBomDetailActivity";

	private View tranDetail;

	private TextView tv_trans_date;
	private TextView tv_order_number;
	private TextView tv_trans_number;
	private TextView tv_business_name;
	private TextView tv_account_type;
	private TextView tv_account_num;
	private TextView tv_currency;
	private TextView tv_trans_amount;
	private TextView tv_order_desc;
	private TextView tv_trans_status;
	private TextView tv_client;
	private TextView tv_plan_number;
	private TextView tv_plan_fee;
	private TextView tv_plan_first_amount;
	private TextView tv_plan_each_amount;
	/** 备忘信息 */
	private TextView tv_remark_info;

	private Button bt_ensure;

	private Context tqTransContext;

	private String transDate;
	private String orderNumber;
	private String businessName;
	/** 账户类型 */
	private String accountType;
	private String accountNum;
	private String currency;
	private String transAmount;
	private String orderDesc;
	private String transStatus;
	private String planNumber;
	private String planFee;
	private String planFirstAmount;
	private String planEachAmount;
	private String remarkInfo;
	private String orderSeq;
	private String channl;

	private PubHttpObserver httpObserver;

	private String transactionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		tqTransContext = TransContext.getTQTransContext();
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_TQ);
		tranDetail = LayoutInflater.from(this).inflate(R.layout.epay_tq_bom_detail, null);

		super.setType(1);
		super.setShowBackBtn(false);
		super.setTitleName(PubConstants.TITLE_TRANS_QUERY);
		super.setContentView(tranDetail);
		super.onCreate(savedInstanceState);

		// 初始化当前页面内容
		initCurPage();
	}

	private void initCurPage() {
		tv_trans_date = (TextView) tranDetail.findViewById(R.id.tv_trans_date);
		tv_order_number = (TextView) tranDetail.findViewById(R.id.tv_order_number);
		tv_trans_number = (TextView) tranDetail.findViewById(R.id.tv_trans_number);
		tv_business_name = (TextView) tranDetail.findViewById(R.id.tv_business_name);
		tv_account_type = (TextView) tranDetail.findViewById(R.id.tv_account_type);
		tv_account_num = (TextView) tranDetail.findViewById(R.id.tv_account_num);
		tv_currency = (TextView) tranDetail.findViewById(R.id.tv_currency);
		tv_trans_amount = (TextView) tranDetail.findViewById(R.id.tv_trans_amount);
		tv_account_num = (TextView) tranDetail.findViewById(R.id.tv_account_num);
		tv_order_desc = (TextView) tranDetail.findViewById(R.id.tv_order_desc);
		tv_trans_status = (TextView) tranDetail.findViewById(R.id.tv_trans_status);
		tv_client = (TextView) tranDetail.findViewById(R.id.tv_client);
		tv_plan_number = (TextView) tranDetail.findViewById(R.id.tv_plan_number);
		tv_plan_fee = (TextView) tranDetail.findViewById(R.id.tv_plan_fee);
		tv_plan_first_amount = (TextView) tranDetail.findViewById(R.id.tv_plan_first_amount);
		tv_plan_each_amount = (TextView) tranDetail.findViewById(R.id.tv_plan_each_amount);
		tv_remark_info = (TextView) tranDetail.findViewById(R.id.tv_remark_info);
		bt_ensure = (Button) tranDetail.findViewById(R.id.bt_ensure);
		bt_ensure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getTransData();
	}

	/**
	 * 设置页面显示内容
	 */
	private void initDisplay() {
		BiiHttpEngine.dissMissProgressDialog();

		tv_trans_date.setText(transDate);
		tv_order_number.setText(orderNumber);
		tv_business_name.setText(businessName);
		tv_account_type.setText(LocalData.AccountType.get(accountType));
		tv_trans_number.setText(orderSeq);
		tv_account_num.setText(StringUtil.getForSixForString(accountNum));
		tv_currency.setText(LocalData.Currency.get(currency));
		tv_trans_amount.setText(StringUtil.parseStringPattern(transAmount, 2));
		tv_order_desc.setText(orderDesc);
		tv_trans_status.setText(EpayUtil.getString(TQConstants.BOM_ORDER_STATUS.get(transStatus), ""));
		tv_client.setText(TreatyConstants.TREATY_CHANNL.get(channl));
		tv_plan_number.setText(planNumber);
		tv_plan_fee.setText(StringUtil.parseStringPattern(planFee, 2));
		tv_plan_first_amount.setText(StringUtil.parseStringPattern(planFirstAmount, 2));
		tv_plan_each_amount.setText(StringUtil.parseStringPattern(planEachAmount, 2));
		tv_remark_info.setText(remarkInfo);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_client);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_trans_date);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_order_number);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_trans_number);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_business_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_account_type);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_account_num);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_currency);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_trans_amount);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_order_desc);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_trans_status);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_plan_number);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_plan_fee);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_plan_first_amount);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_plan_each_amount);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_remark_info);

		TextView tv_plan_fee_lable = (TextView) findViewById(R.id.tv_plan_fee_lable);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_plan_fee_lable);
	}

	/**
	 * 获取交易数据
	 */
	private void getTransData() {
		Map<Object, Object> selectedResult = tqTransContext.getMap(TQConstants.PUB_SELECTED_RESULT);
		transactionId = EpayUtil.getString(
				selectedResult.get(TQConstants.METHOD_QUERY_BOM_TRANS_RECORD_FEILD_TRANSACTION_ID), "");
		LogGloble.d(tag, "TransactionId : " + transactionId);
		planNumber = EpayUtil.getString(
				selectedResult.get(TQConstants.METHOD_QUERY_BOM_TRANS_RECORD_FEILD_PLAN_NUMBER), "");
		planFee = EpayUtil.getString(selectedResult.get(TQConstants.METHOD_QUERY_BOM_TRANS_RECORD_FEILD_PLAN_FREE), "");
		planFirstAmount = EpayUtil.getString(
				selectedResult.get(TQConstants.METHOD_QUERY_BOM_TRANS_RECORD_FEILD_PLAN_FIRST_AMOUNT), "");
		planEachAmount = EpayUtil.getString(
				selectedResult.get(TQConstants.METHOD_QUERY_BOM_TRANS_RECORD_FEILD_PLAN_EACH_AMOUNT), "");
		BiiHttpEngine.showProgressDialogCanGoBack();
		httpObserver.req_queryBOMTransDetail(transactionId, "queryBOMTransDetailCallback");
	}

	/**
	 * 查询电子支付交易详情回调方法
	 * 
	 * @param resultObj
	 */
	public void queryBOMTransDetailCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		if (result != null) {
			Map<Object, Object> resultMap = EpayUtil.getMap(result);
			this.transDate = EpayUtil.getString(
					resultMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_EPAY_TIME), "");
			this.orderNumber = EpayUtil.getString(
					resultMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_RECORD_FEILD_ORDER_NO), "");
			this.orderSeq = EpayUtil.getString(
					resultMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_ORDER_SEQ), "");
			this.businessName = EpayUtil.getString(
					resultMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_MERCHANT_NAME), "");
			this.accountType = EpayUtil.getString(
					resultMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_ACCOUNT_TYPE), "");
			this.accountNum = EpayUtil.getString(
					resultMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_ACCOUNT_NUMBER), "");
			this.currency = EpayUtil.getString(
					resultMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_CURRENCY_CODE), "");
			this.transAmount = EpayUtil.getString(
					resultMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_AMOUNT), "");
			this.orderDesc = EpayUtil.getString(
					resultMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_ORDER_NOTE), "");
			this.transStatus = EpayUtil.getString(
					resultMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_STATUS), "");
			this.channl = EpayUtil.getString(
					resultMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_CHANNEL), "");
			this.remarkInfo = EpayUtil.getString(resultMap.get(TQConstants.METHOD_QUERY_BOM_TRANS_DETAIL_FEILD_REMARK),
					"");

			initDisplay();
		}
	}
}
