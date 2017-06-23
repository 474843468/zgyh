package com.chinamworld.bocmbci.biz.tran.atmremit;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * Atm取款查询撤销成功页面
 * 
 * @author wangmengmeng
 * 
 */
public class AtmRecordCancelSuccessActivity extends TranBaseActivity {

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
	/** 有效时间至 */
	private TextView tv_dueDate;
	/** 状态 */
	private LinearLayout ll_state;
	private TextView tv_state;
	// 有效期
	private String dueDateStr;
	/** 详情 */
	private Map<String, Object> detailMap;
	private Button btn_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.trans_atm_cancel_title));
		view = addView(R.layout.tran_atm_records_confirm_activity);
		setLeftSelectedPosition("tranManager_5");
		back.setVisibility(View.GONE);
		goneLeftView();
		mTopRightBtn.setText(this.getString(R.string.go_main));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ActivityTaskManager.getInstance().removeAllActivity();
				overridePendingTransition(R.anim.no_animation, R.anim.slide_down_out);
			}
		});
		detailMap = TranDataCenter.getInstance().getMobileTransDetailMap();
		dueDateStr = getIntent().getStringExtra(Tran.TRAN_ATM_DETAIL_DUEDATE);
		init();
	}

	public void init() {
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(this.getString(R.string.trans_atm_cancel_title_suc));
		tv_atm_remitNo = (TextView) view.findViewById(R.id.tv_atm_remitNo);
		tv_atm_remitNo.setText((String) detailMap.get(Tran.TRAN_ATM_DETAIL_REMITNUMBER_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_atm_remitNo);
		String remitNumber = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_CARDNO_RES);
		String fromActNumber = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_FROMACTNUMBER_RES);
		tv_remitNumber = (TextView) view.findViewById(R.id.tv_remitNumber);
		tv_remitNumber.setText(StringUtil.isNull(remitNumber) ? StringUtil.getForSixForString(fromActNumber) : StringUtil
				.getForSixForString(remitNumber));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_remitNumber);
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
		ll_state = (LinearLayout) view.findViewById(R.id.ll_state);
		tv_state = (TextView) view.findViewById(R.id.tv_state);
		String status = (String) detailMap.get(Tran.TRAN_ATM_DETAIL_STATUS_RES);
		tv_state.setText((String) AtmStatusMap.get(status));
		// ll_state.setVisibility(View.GONE);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_cancel.setText(this.getString(R.string.finish));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_payeeName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_furInfo);
		tv_dueDate = (TextView) view.findViewById(R.id.tv_dueDate);
		if (TextUtils.isEmpty(dueDateStr)) {
			view.findViewById(R.id.ll_dueDate).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.ll_dueDate).setVisibility(View.VISIBLE);
			tv_dueDate.setText(dueDateStr);
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_dueDate);

		btn_cancel.setVisibility(View.VISIBLE);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setResult(RESULT_OK);
				finish();
				overridePendingTransition(R.anim.no_animation, R.anim.slide_down_out);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}
