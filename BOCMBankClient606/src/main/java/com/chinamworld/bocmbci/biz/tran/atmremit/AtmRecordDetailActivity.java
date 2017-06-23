package com.chinamworld.bocmbci.biz.tran.atmremit;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * Atm取款查询详情页面
 * 
 * @author wangmengmeng
 * 
 */
public class AtmRecordDetailActivity extends TranBaseActivity {

	private View view;
	/** 汇款编号 */
	private TextView tv_atm_remitNo;
	/** 汇出账号 */
	private TextView tv_remitNumber;
	// /** 汇款人名称 */
	// private TextView tv_atm_remitName;
	/** 收款人姓名 */
	private TextView tv_payeeName;
	/** 收款人手机号码 */
	private TextView tv_payeeMobile;
	/** 汇款金额 */
	private TextView tv_money;
	/** 附言 */
	private TextView tv_furInfo;
	/** 状态 */
	private TextView tv_state;
	/** 有效期 */
	private TextView tv_dueDate;
	/** 取款日期 */
	private TextView payment_Date;
	// private String paymentDate;
	/** 详情 */
	private Map<String, Object> detailMap;
	private String dueDateStr;
	private Button btn_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.trans_atm_two_menu));
		view = addView(R.layout.tran_atm_records_detail_activity);
		setLeftSelectedPosition("tranManager_5");
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		toprightBtn();
		detailMap = TranDataCenter.getInstance().getMobileTransDetailMap();
		init();
	}

	public void init() {
		tv_dueDate = (TextView) view.findViewById(R.id.dueDate);
		tv_atm_remitNo = (TextView) view.findViewById(R.id.tv_atm_remitNo);
		tv_atm_remitNo.setText((String) detailMap.get(Tran.TRAN_ATM_DETAIL_REMITNUMBER_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_atm_remitNo);
		tv_remitNumber = (TextView) view.findViewById(R.id.tv_remitNumber);
		String remitNumber = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_CARDNO_RES);
		String fromActNumber = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_FROMACTNUMBER_RES);
		tv_remitNumber.setText(StringUtil.isNull(remitNumber) ? StringUtil.getForSixForString(fromActNumber) : StringUtil
				.getForSixForString(remitNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_remitNumber);
		// tv_atm_remitName = (TextView)
		// view.findViewById(R.id.tv_atm_remitName);
		// tv_atm_remitName.setText((String) detailMap
		// .get(Tran.TRAN_ATM_DETAIL_FROMNAME_RES));
		/** 收款人姓名 */
		tv_payeeName = (TextView) view.findViewById(R.id.tv_payeeName);
		tv_payeeName.setText((String) detailMap.get(Tran.TRAN_ATM_DETAIL_TONAME_RES));
		/** 收款人手机号码 */
		tv_payeeMobile = (TextView) view.findViewById(R.id.tv_payeeMobile);
		tv_payeeMobile.setText((String) detailMap.get(Tran.TRAN_ATM_DETAIL_TOMOBILE_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_payeeMobile);
		/** 转账金额 */
		tv_money = (TextView) view.findViewById(R.id.tv_money);
		String amount = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_PAYMENTAMOUNT_RES);
		tv_money.setText(StringUtil.parseStringPattern(amount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_money);
		/** 备注 */
		tv_furInfo = (TextView) view.findViewById(R.id.tv_furInfo);
		String furInfo = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_COMMENT_RES);
		tv_furInfo.setText(StringUtil.isNull(furInfo) ? ConstantGloble.BOCINVT_DATE_ADD : furInfo);
		/** 状态 */
		tv_state = (TextView) view.findViewById(R.id.tv_state);
		String status = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_STATUS_RES);
		tv_state.setText((String) AtmStatusMap.get(status));
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		// 取款日期
		payment_Date = (TextView) view.findViewById(R.id.get_remit_date);

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_payeeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_furInfo);

		if ("OK".equals(status)) {
			payment_Date.setText((String) detailMap.get(Tran.TRAN_ATM_QUERY_PAYMENTDATE_RES));
			view.findViewById(R.id.get_date_layout).setVisibility(View.VISIBLE);
		} else {
			view.findViewById(R.id.get_date_layout).setVisibility(View.GONE);
		}

		// dueDateStr= "-";
		// 设置有效期 (YYYY/MM/DD。当交易状态为OK或CL时，不会返回到期日期字段)
		if ("OU".equals(status) || "CR".equals(status) || "L3".equals(status)) {
			dueDateStr = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_DUEDATE);
			dueDateStr = DateUtils.formatTime(dueDateStr);
			tv_dueDate.setText(dueDateStr);
			view.findViewById(R.id.date_layout).setVisibility(View.VISIBLE);
		} else {
			view.findViewById(R.id.date_layout).setVisibility(View.GONE);
		}

		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_dueDate);

		// TODO 隐藏撤销按钮
		if (!StringUtil.isNull(status)) {
			if (status.equals(CANCANCELSTATUS) || status.equals(CANCANCELSTATUS1) || status.equals(CANCANCELSTATUS2)
					|| status.equals(CANCANCELSTATUS3)) {
				btn_cancel.setVisibility(View.VISIBLE);
			}
		}
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AtmRecordDetailActivity.this, AtmRecordCancelConfirmActivity.class);
				intent.putExtra(Tran.TRAN_ATM_DETAIL_DUEDATE, dueDateStr);
				// intent.putExtra(Tran.TRAN_ATM_QUERY_PAYMENTDATE_RES,
				// paymentDate);
				startActivityForResult(intent, 2);
				overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}
}
