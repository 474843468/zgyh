package com.chinamworld.bocmbci.biz.blpt.sign;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.blpt.BillPaymentBaseActivity;
import com.chinamworld.bocmbci.biz.blpt.BlptUtil;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 已签约缴费信息详情
 * 
 * @author panwe
 * 
 */
public class BillHadAppliedInfoActivity extends BillPaymentBaseActivity
		implements OnClickListener {

	/** 主布局 */
	private View viewContent;
	/** 缴费title */
	private TextView tvTitleInfo;
	/** 缴费日期 */
	private TextView tvPayDate;
	/** 缴费地区 */
	private TextView tvPayAdress;
	/** 缴费项目 */
	private TextView tvPayType;
	/** 缴费卡号 */
	private TextView tvPayNumber;
	/** 手机号 */
	private TextView tvPayPhone;
	/** 撤销按钮 */
	private Button btnCance;
	/** 缴费项目 */
	private Button btnPay;
	private int tag;
	/** 手机号 **/
	private TextView tvPayPhoneTitle;
	private String mobile;
	/** 账号信息 ***/
	private String accNum;
	private String accId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 添加布局
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_hadapplied_info, null);
		BlptUtil.getInstance().addActivity(this);
		addView(viewContent);
		// 右上角按钮赋值
		setText(this.getString(R.string.go_main));
		setRightBtnClick(rightBtnBackmainClick);
		// 请求系统时间
		BiiHttpEngine.showProgressDialog();
		requestSystemDateTime();
		init();
		getData();
	}

	private void init() {
		tvTitleInfo = (TextView) viewContent.findViewById(R.id.tv_bill_title);

		tvPayDate = (TextView) viewContent.findViewById(R.id.tv_pay_date);
		tvPayAdress = (TextView) viewContent.findViewById(R.id.tv_pay_adress);
		tvPayType = (TextView) viewContent.findViewById(R.id.tv_pay_type);
		tvPayNumber = (TextView) viewContent.findViewById(R.id.tv_pay_number);
		tvPayPhoneTitle = (TextView) viewContent.findViewById(R.id.ph_title);
		tvPayPhone = (TextView) viewContent.findViewById(R.id.tv_pay_phone);

		btnCance = (Button) viewContent.findViewById(R.id.btn_cance);
		btnPay = (Button) viewContent.findViewById(R.id.btn_pay);
		btnPay.setOnClickListener(this);
		btnCance.setOnClickListener(this);
	}

	private void getData() {
		Bundle b = getIntent().getBundleExtra(Blpt.KEY_BUNDLE);
		tag = b.getInt(Blpt.KEY_TAG, 0);
		String title = b.getString(Blpt.KEY_PAYEENAME);
		String city = b.getString(Blpt.KEY_CITY);
		accNum = b.getString(Comm.ACCOUNTNUMBER);
		accId = b.getString(Comm.ACCOUNT_ID);
		mobile = b.getString("MOBILE");
		String mobileTitle = b.getString("PHONETITLE");

		// 为界面标题赋值
		setTitle(title);
		tvPayPhoneTitle.setText(mobileTitle + "：");
		tvPayPhoneTitle.setEllipsize(TruncateAt.MIDDLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvPayPhoneTitle);
		tvTitleInfo.setText(this.getString(R.string.blpt_billinfo_title)
				+ title + "服务资料");
		tvPayAdress.setText(city);
		tvPayType.setText(title);
		tvPayNumber.setText(StringUtil.getForSixForString(accNum));
		tvPayPhone.setText(mobile);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvPayPhone);
	}

	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(dateTime)) {
			return;
		}
		tvPayDate.setText(QueryDateUtils.getcurrentDate(dateTime));
	}

	@Override
	public void onClick(View v) {
		Intent it;
		it = new Intent();
		Bundle b = new Bundle();
		b.putString(Comm.ACCOUNTNUMBER, accNum);
		b.putString(Comm.ACCOUNT_ID, accId);
		b.putString("MOBLIE", mobile);
		b.putInt(Blpt.KEY_TAG, tag);
		it.putExtra(Blpt.KEY_BUNDLE, b);
		switch (v.getId()) {
		case R.id.btn_pay:
			it.setClass(this, BillPaymentSignMsgAddActivity.class);
			break;
		case R.id.btn_cance:
			it.setClass(this, BillRevocationSignMsgaddActivity.class);
			break;
		}
		startActivity(it);
	}

}
