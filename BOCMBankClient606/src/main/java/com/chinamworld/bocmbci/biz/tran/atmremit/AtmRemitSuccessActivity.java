package com.chinamworld.bocmbci.biz.tran.atmremit;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * Atm取款成功
 * 
 * @author wangmengmeng
 * 
 */
public class AtmRemitSuccessActivity extends TranBaseActivity {
	/** atm取款确认信息页面 */
	private View view;
	/** 汇出账户 */
	private TextView confirmAcount;
	/** 手机号码 */
	private TextView confirmPhone;
	/** 姓名 */
	private TextView confirmName;
	/** 汇款金额 */
	private TextView confirmAmount;
	/**有效期至 LineatLayout*/
	private LinearLayout ll_dueDate;
	/**有效期至*/
	private TextView tv_dueDate;
	/** 附言 */
	private TextView confirmFuyan;
	/** 取款编号 */
	private TextView tv_remitNo;
	/** 下方按钮 */
	private Button btnConfirm;
	private String acountNumber;
	private String phoneNumber;
	private String remitName;
	private String remitAmount;
	private String remark;
	private String remitNo;
	private String dueDate;
	private String  status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_atm_title));
		view = addView(R.layout.tran_atm_success);
		toprightBtn();
		back.setVisibility(View.GONE);
		getIntentData();
		init();
		 
	}

	/**
	 * @Title: getIntentData
	 * @Description: 获取intent带来的数据
	 * @param
	 * @return void
	 */
	private void getIntentData() {
		Intent intent = this.getIntent();
		acountNumber = intent.getStringExtra(Comm.ACCOUNTNUMBER);
		phoneNumber = intent.getStringExtra(DrawMoney.PAYEE_MOBILE);
		remitName = intent.getStringExtra(DrawMoney.PAYEE_NAME);
		remitAmount = intent.getStringExtra(DrawMoney.REMIT_AMOUNT);
		remark = intent.getStringExtra(DrawMoney.REMARK);
		remitNo = intent.getStringExtra(Tran.TRAN_ATM_SUB_REMITNO_RES);
		dueDate = intent.getStringExtra(DrawMoney.DUE_DATE);
		status = intent.getStringExtra(DrawMoney.STATUS);
//		tv_dueDate = intent.getStringExtra(name)
	}

	private void init() {
		confirmAcount = (TextView) view
				.findViewById(R.id.remitout_confirm_account);
		confirmPhone = (TextView) view.findViewById(R.id.remit_confirm_phone);
		confirmName = (TextView) view.findViewById(R.id.remit_confirm_name);
		confirmAmount = (TextView) view.findViewById(R.id.remit_confirm_amount);
		confirmFuyan = (TextView) view.findViewById(R.id.remit_confirm_fuyan);

		btnConfirm = (Button) view.findViewById(R.id.remit_confirm_next_btn);
		tv_remitNo = (TextView) view.findViewById(R.id.remitout_remitNo);
		tv_dueDate = (TextView) view.findViewById(R.id.tv_dueDate);
		
		tv_dueDate.setText(DateUtils.formatTime(dueDate));
//		if("OU".equals(status) || "CR".equals(status) || "L3".equals(status)){
//			tv_dueDate.setText( DateUtils.formatStr(dueDate));
//			view.findViewById(R.id.ll_dueDate).setVisibility(View.VISIBLE);
//		}else{
//			view.findViewById(R.id.ll_dueDate).setVisibility(View.GONE);
//		}
		
		
		confirmAcount.setText(StringUtil.getForSixForString(String
				.valueOf(acountNumber)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				confirmAcount);
		confirmPhone.setText(phoneNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				confirmPhone);
		confirmName.setText(remitName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				confirmName);
		confirmAmount.setText(StringUtil.parseStringPattern(remitAmount, 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				confirmAmount);
		confirmFuyan.setText(StringUtil.isNullChange(remark));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				confirmFuyan);
		tv_remitNo.setText(remitNo);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_remitNo);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_dueDate);
		
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent(AtmRemitSuccessActivity.this,
						AtmThirdMenu.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}
