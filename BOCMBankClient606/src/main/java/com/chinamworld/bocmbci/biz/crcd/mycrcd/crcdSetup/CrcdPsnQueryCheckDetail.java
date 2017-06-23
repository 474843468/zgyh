package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 信用卡对账单服务详情-----开通、关闭、编辑页面
 * 
 * @author huangyuchao
 * 
 */
public class CrcdPsnQueryCheckDetail extends CrcdBaseActivity implements OnClickListener {
	private static final String TAG = "CrcdPsnQueryCheckDetail";
	private View view;
	Button btn_crcd_paper_edit, btn_crcd_email_edit, btn_crcd_phone_edit;

	TextView titleText;
	/** 纸质账单地址 */
	TextView tv_paper_commmon;
	/** 邮件地址 */
	TextView tv_email_common;
	/** 手机号 */
	TextView tv_phone_common;

	// ImageView iv_paper_right, iv_paper_close, iv_email_right, iv_email_close,
	// iv_phone_right, iv_phone_close;
	/** 纸质对账单编辑按钮 */
	private ImageView img_crcd_paper_edit;
	/** 电子邮件对账单编辑按钮 */
	private ImageView img_crcd_email_edit;
	/** 手机对账单编辑按钮 */
	private ImageView img_crcd_phone_edit;

	String open, close;

	protected static String email;
	/** 纸质账单地址 */
	protected static String paperAddress;
	protected static String mobile;
	/** 纸质按钮区域 */
	LinearLayout ll_paper;
	/** 电子邮件按钮区域 */
	LinearLayout ll_email;
	/** 手机按钮区域 */
	LinearLayout ll_phone;

	TextView tv_paper_open, tv_paper_close, tv_email_open, tv_email_close, tv_phone_open, tv_phone_close;
	/** 纸质开通标志 */
	String paperBillFlag;
	/** 电子邮件开通标志 */
	String emailBillFlag;
	/** 手机开通标志 */
	String pushBillFlag;

	int white;
	int gray;
	/** 0=纸质对账单,1=电子对账单,2=推入式对账单 */
	protected int billSetupId;
	protected static String strbillSetupId;
	/** open--开通 edit--编辑 */
	public String isOpenOrEdit;
	private String accountId = null;
	private String accountNumber = null;
	private String codeCode = null;
	private int tag = -1;
	/** 重置按钮 */
	private TextView resetButton = null;
	/** 重置区域 */
	private View resetView = null;
	private View paperTextView = null;
	private View emailTextView = null;
	private View phoneTextView = null;
	private View tellResetView = null;
	private TextView tellTextView = null;
	/** 1--邮件对账单，2---手机对账单 */
	private int resetTag = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_bill_zhangdan_service));
		view = addView(R.layout.crcd_psn_checkdetail);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		codeCode = getIntent().getStringExtra(Crcd.CRCD_CURRENCYCODE);
		tag = getIntent().getIntExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, -1);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		init();
	}

	/** 初始化界面 */
	private void init() {

		ll_paper = (LinearLayout) view.findViewById(R.id.ll_paper);
		ll_email = (LinearLayout) view.findViewById(R.id.ll_email);
		ll_phone = (LinearLayout) view.findViewById(R.id.ll_phone);
		paperTextView = findViewById(R.id.tv_paper_commmon_view);
		emailTextView = findViewById(R.id.tv_email_common_view);
		phoneTextView = findViewById(R.id.tv_phone_common_view);

		ll_paper.setOnClickListener(this);
		ll_email.setOnClickListener(this);
		ll_phone.setOnClickListener(this);

		tv_paper_open = (TextView) view.findViewById(R.id.tv_paper_open);
		tv_paper_close = (TextView) view.findViewById(R.id.tv_paper_close);
		tv_email_open = (TextView) view.findViewById(R.id.tv_email_open);
		tv_email_close = (TextView) view.findViewById(R.id.tv_email_close);
		tv_phone_open = (TextView) view.findViewById(R.id.tv_phone_open);
		tv_phone_close = (TextView) view.findViewById(R.id.tv_phone_close);
		titleText = (TextView) view.findViewById(R.id.titleText);
		tv_paper_commmon = (TextView) view.findViewById(R.id.tv_paper_commmon);
		tv_email_common = (TextView) view.findViewById(R.id.tv_email_common);
		tv_phone_common = (TextView) view.findViewById(R.id.tv_phone_common);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_paper_commmon);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_email_common);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_phone_common);
		img_crcd_paper_edit = (ImageView) view.findViewById(R.id.img_crcd_paper_edit);
		img_crcd_email_edit = (ImageView) view.findViewById(R.id.img_crcd_email_edit);
		img_crcd_phone_edit = (ImageView) view.findViewById(R.id.img_crcd_phone_edit);
		resetButton = (TextView) view.findViewById(R.id.reset_text);
		resetView = view.findViewById(R.id.reset_view);
		tellResetView = view.findViewById(R.id.tell_view);
		tellTextView = (TextView) view.findViewById(R.id.tell_reset);
		img_crcd_paper_edit.setOnClickListener(this);
		img_crcd_email_edit.setOnClickListener(this);
		img_crcd_phone_edit.setOnClickListener(this);
		resetButton.setOnClickListener(this);
		tellTextView.setOnClickListener(this);
		if (tag == 2) {
			// 我的信用卡详情
			Map<String, String> map = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.ISFOREX_RESULT_KEY);
			mobile = map.get(Crcd.CRCD_MOBILE);
			email = map.get(Crcd.CRCD_EMAIL);
			paperAddress = map.get(Crcd.CRCD_PAPERADDRESS);
			/** 1:开通,0:未开通 */
			paperBillFlag = map.get(Crcd.CRCD_PAPERBILLFLAG);
			emailBillFlag = map.get(Crcd.CRCD_EMAILBILLFLAG);
			pushBillFlag = map.get(Crcd.CRCD_PUSHBILLFLAG);
		} else {
			// 对账单服务选卡页面
			Map<String, Object> map = CrcdPsnQueryCheckList.returnMap;
			mobile = String.valueOf(map.get(Crcd.CRCD_MOBILE));
			email = String.valueOf(map.get(Crcd.CRCD_EMAIL));
			paperAddress = String.valueOf(map.get(Crcd.CRCD_PAPERADDRESS));
			/** 1:开通,0:未开通 */
			paperBillFlag = String.valueOf(map.get(Crcd.CRCD_PAPERBILLFLAG));
			emailBillFlag = String.valueOf(map.get(Crcd.CRCD_EMAILBILLFLAG));
			pushBillFlag = String.valueOf(map.get(Crcd.CRCD_PUSHBILLFLAG));
		}

		titleText.setText(this.getString(R.string.mycrcd_credit_card) + StringUtil.getForSixForString(accountNumber));
		// 为控件赋值
		if (StringUtil.isNull(paperAddress)) {
			tv_paper_commmon.setText("-");
		} else {
			tv_paper_commmon.setText(paperAddress);
		}
		if (StringUtil.isNull(email)) {
			tv_email_common.setText("-");
		} else {
			tv_email_common.setText(email);
		}
		if (StringUtil.isNull(mobile)) {
			tv_phone_common.setText("-");
		} else {
			tv_phone_common.setText(mobile);
		}

		open = this.getString(R.string.mycrcd_open);
		close = this.getString(R.string.mycrcd_close);

		white = getResources().getColor(R.color.white);
		gray = getResources().getColor(R.color.gray);
		// 编辑按钮显示 或隐藏
		if (ConstantGloble.CRCD_BillFlag_OPEN.equals(paperBillFlag)) {
			ll_paper.setBackgroundResource(R.drawable.btn_check_close);
			tv_paper_open.setTextColor(white);
			tv_paper_close.setTextColor(gray);
			paperTextView.setVisibility(View.VISIBLE);
			img_crcd_paper_edit.setVisibility(View.VISIBLE);
		} else if (ConstantGloble.CRCD_BillFlag_CLOSE.equals(paperBillFlag)) {// 0
			// 未开通
			ll_paper.setBackgroundResource(R.drawable.btn_check_kaitong);
			tv_paper_open.setTextColor(gray);
			tv_paper_close.setTextColor(white);
			img_crcd_paper_edit.setVisibility(View.INVISIBLE);
			tv_paper_commmon.setVisibility(View.INVISIBLE);
			paperTextView.setVisibility(View.INVISIBLE);
		}

		if (ConstantGloble.CRCD_BillFlag_OPEN.equals(emailBillFlag)) {
			ll_email.setBackgroundResource(R.drawable.btn_check_close);
			tv_email_open.setTextColor(white);
			tv_email_close.setTextColor(gray);
			img_crcd_email_edit.setVisibility(View.VISIBLE);
			resetView.setVisibility(View.VISIBLE);
			emailTextView.setVisibility(View.VISIBLE);
		} else if (ConstantGloble.CRCD_BillFlag_CLOSE.equals(emailBillFlag)) {
			ll_email.setBackgroundResource(R.drawable.btn_check_kaitong);
			tv_email_open.setTextColor(gray);
			tv_email_close.setTextColor(white);
			img_crcd_email_edit.setVisibility(View.INVISIBLE);
			tv_email_common.setVisibility(View.INVISIBLE);
			resetView.setVisibility(View.INVISIBLE);
			emailTextView.setVisibility(View.INVISIBLE);
		}

		if (ConstantGloble.CRCD_BillFlag_OPEN.equals(pushBillFlag)) {
			ll_phone.setBackgroundResource(R.drawable.btn_check_close);
			tv_phone_open.setTextColor(white);
			tv_phone_close.setTextColor(gray);
			img_crcd_phone_edit.setVisibility(View.VISIBLE);
			phoneTextView.setVisibility(View.VISIBLE);
			tellResetView.setVisibility(View.VISIBLE);
		} else if (ConstantGloble.CRCD_BillFlag_CLOSE.equals(pushBillFlag)) {
			ll_phone.setBackgroundResource(R.drawable.btn_check_kaitong);
			tv_phone_open.setTextColor(gray);
			tv_phone_close.setTextColor(white);
			img_crcd_phone_edit.setVisibility(View.INVISIBLE);
			tv_phone_common.setVisibility(View.INVISIBLE);
			phoneTextView.setVisibility(View.INVISIBLE);
			tellResetView.setVisibility(View.INVISIBLE);
		}

	}

	// 切换纸质状态
	public void changePaperFlag() {
		if (ConstantGloble.CRCD_BillFlag_OPEN.equals(paperBillFlag)) {
			setBackgroundClick(ll_paper, true, tv_paper_open, tv_paper_close);
			billClose();
		} else if (ConstantGloble.CRCD_BillFlag_CLOSE.equals(paperBillFlag)) {
			setBackgroundClick(ll_paper, false, tv_paper_open, tv_paper_close);
			billOpen();
		}

	}

	// 切换邮件状态
	public void changeEmailFlag() {
		if (ConstantGloble.CRCD_BillFlag_OPEN.equals(emailBillFlag)) {
			setBackgroundClick(ll_email, true, tv_email_open, tv_email_close);
			billClose();
		} else if (ConstantGloble.CRCD_BillFlag_CLOSE.equals(emailBillFlag)) {
			setBackgroundClick(ll_email, false, tv_email_open, tv_email_close);
			billOpen();
		}
	}

	// 切换手机状态
	public void changePhoneFlag() {
		if (ConstantGloble.CRCD_BillFlag_OPEN.equals(pushBillFlag)) {
			setBackgroundClick(ll_phone, true, tv_phone_open, tv_phone_close);
			billClose();
		} else if (ConstantGloble.CRCD_BillFlag_CLOSE.equals(pushBillFlag)) {
			setBackgroundClick(ll_phone, false, tv_phone_open, tv_phone_close);
			billOpen();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_crcd_paper_edit:// 纸质对账单编辑按钮----开通状态
			billSetupId = 0;
			strbillSetupId = this.getString(R.string.mycrcd_paper_billdan);
			// 对账单修改
			billEdit();
			break;
		case R.id.img_crcd_email_edit:// 电子邮件对账单编辑按钮----开通状态
			billSetupId = 1;
			strbillSetupId = this.getString(R.string.mycrcd_email_billdan);
			// 对账单修改
			billEdit();
			break;
		case R.id.img_crcd_phone_edit:// 手机对账单编辑按钮----开通状态
			billSetupId = 2;
			strbillSetupId = this.getString(R.string.mycrcd_phone_billdan);
			// 对账单修改
			billEdit();
			break;
		case R.id.ll_paper:// 纸质对账单----开通关闭区域
			billSetupId = 0;
			strbillSetupId = this.getString(R.string.mycrcd_paper_billdan);
			changePaperFlag();
			break;
		case R.id.ll_email:// 电子邮件对账单----开通关闭区域
			billSetupId = 1;
			strbillSetupId = this.getString(R.string.mycrcd_email_billdan);
			changeEmailFlag();
			break;
		case R.id.ll_phone:// 手机对账单----开通关闭区域
			billSetupId = 2;
			strbillSetupId = this.getString(R.string.mycrcd_phone_billdan);
			changePhoneFlag();
			break;
		case R.id.reset_text:// 电子邮件对账单----重置按钮
			resetTag = 1;
			BaseHttpEngine.showProgressDialog();
			requestSystemDateTime();
			break;
		case R.id.tell_reset:// 手机号对账单----重置按钮
			resetTag = 2;
			BaseHttpEngine.showProgressDialog();
			requestSystemDateTime();
			break;
		}

	}

	/** 设置按钮背景 */
	private void setBackgroundClick(View view, boolean flag, TextView text1, TextView text2) {
		if (flag) {
			// 开通
			view.setBackgroundResource(R.drawable.btn_check_kaitong);
			text1.setTextColor(gray);
			text2.setTextColor(white);
		} else {
			view.setBackgroundResource(R.drawable.btn_check_close);
			text1.setTextColor(white);
			text2.setTextColor(gray);
		}
	}

	/** 点击开通按钮 */
	public void billOpen() {
		isOpenOrEdit = "open";
		Intent it = new Intent(CrcdPsnQueryCheckDetail.this, CrcdPsnCheckOpenActivity.class);
		it.putExtra(ConstantGloble.CRCD_ISOPENOREDIT, isOpenOrEdit);
		it.putExtra(ConstantGloble.CRCD_BILLSETUPID, billSetupId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		// startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	/** 点击编辑图片按钮 */
	public void billEdit() {
		isOpenOrEdit = "edit";
		Intent it = new Intent(CrcdPsnQueryCheckDetail.this, CrcdPsnCheckOpenActivity.class);
		it.putExtra(ConstantGloble.CRCD_ISOPENOREDIT, isOpenOrEdit);
		it.putExtra(ConstantGloble.CRCD_BILLSETUPID, billSetupId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		// startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	/** 点击关闭按钮 */
	public void billClose() {
		Intent it = new Intent(CrcdPsnQueryCheckDetail.this, CrcdPsnCheckCloseActivity.class);
		it.putExtra(ConstantGloble.CRCD_ISOPENOREDIT, isOpenOrEdit);
		it.putExtra(ConstantGloble.CRCD_BILLSETUPID, billSetupId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_PAPERADDRESS, paperAddress);
		it.putExtra(Crcd.CRCD_MOBILE, mobile);
		it.putExtra(Crcd.CRCD_EMAIL, email);
		// startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		String date = dateTime;
		if (StringUtil.isNull(date)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		if (resetTag == 1) {
			resetEmail();
		} else if (resetTag == 2) {
			resetPhone();
		}

	}

	/** 重置电子邮件对账单 */
	private void resetEmail() {
		Intent it = new Intent(CrcdPsnQueryCheckDetail.this, CrcdPsnEmailResetSubmitActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_EMAIL, email);
		it.putExtra(Comm.DATETME, dateTime);
		it.putExtra(ConstantGloble.FOREX_TAG, resetTag);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		BaseHttpEngine.dissMissProgressDialog();
	}

	/** 重置手机号对账单 */
	private void resetPhone() {
		Intent it = new Intent(CrcdPsnQueryCheckDetail.this, CrcdPsnEmailResetSubmitActivity.class);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_MOBILE, mobile);
		it.putExtra(Comm.DATETME, dateTime);
		it.putExtra(ConstantGloble.FOREX_TAG, resetTag);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		BaseHttpEngine.dissMissProgressDialog();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:
			setResult(RESULT_CANCELED);
			if (ConstantGloble.CRCD_BillFlag_OPEN.equals(paperBillFlag)) {
				setBackgroundClick(ll_paper, false, tv_paper_open, tv_paper_close);
			} else if (ConstantGloble.CRCD_BillFlag_CLOSE.equals(paperBillFlag)) {
				setBackgroundClick(ll_paper, true, tv_paper_open, tv_paper_close);
			}
			if (ConstantGloble.CRCD_BillFlag_OPEN.equals(emailBillFlag)) {
				setBackgroundClick(ll_email, false, tv_email_open, tv_email_close);
			} else if (ConstantGloble.CRCD_BillFlag_CLOSE.equals(emailBillFlag)) {
				setBackgroundClick(ll_email, true, tv_email_open, tv_email_close);
			}
			if (ConstantGloble.CRCD_BillFlag_OPEN.equals(pushBillFlag)) {
				setBackgroundClick(ll_phone, false, tv_phone_open, tv_phone_close);
			} else if (ConstantGloble.CRCD_BillFlag_CLOSE.equals(pushBillFlag)) {
				setBackgroundClick(ll_phone, true, tv_phone_open, tv_phone_close);
			}
			break;
		default:
			break;
		}
	}
}
