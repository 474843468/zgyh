package com.chinamworld.bocmbci.biz.gatherinitiative.creatgather;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.GatherInitiative;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherBaseActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherInitiativeData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: CreatGatherInputInfoActivity
 * @Description: 发起新存款
 * @author JiangWei
 * @date 2013-8-21上午11:38:09
 */
public class CreatGatherInputInfoActivity2 extends GatherBaseActivity {
	/** 付款人类型选择 */
	private RadioGroup mRadioGroup;
	/** 付款人姓名 */
	private EditText payerNameEdit;
	/** 付款人手机号 */
	private EditText payerPhoneEdit;
	/** 付款人客户号 */
	private EditText lineBankNumberEdit;
	/** 付款人客户号布局 */
	private LinearLayout layoutLineBankNumber;
	/** 下一步按钮 */
	private Button nextBtn;
	/** 付款人类型 1：WEB渠道 2：手机渠道 */
	private String payerChannelStr = "1";
	/** 付款人姓名str */
	private String payerNameStr;
	/** 付款人手机号str */
	private String payerPhoneStr;
	/** 付款人客户号str */
	private String lineBankNumberStr = "";
	/** 是否需要选择账户 */
	private boolean isNeedChooseAccount = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.creat_new_gather);
		View view = LayoutInflater.from(this).inflate(R.layout.gather_creat_input_info, null);
		tabcontent.addView(view);

		isNeedChooseAccount = this.getIntent().getBooleanExtra(IS_NEED_CHOOSE_ACCOUNT, true);
		init();
	}

	/**
	 * @Title: init
	 * @Description: 初始化页面和数据
	 * @param
	 * @return void
	 * @throws
	 */
	private void init() {
		mRadioGroup = (RadioGroup) this.findViewById(R.id.payer_type_radiogroup);
		payerNameEdit = (EditText) this.findViewById(R.id.edit_payer_name);
		EditTextUtils.setLengthMatcher(this, payerNameEdit, 60);
		payerPhoneEdit = (EditText) this.findViewById(R.id.edit_payer_phone);
		lineBankNumberEdit = (EditText) this.findViewById(R.id.edit_payer_customer_number);
		nextBtn = (Button) this.findViewById(R.id.creat_gather_input_next_btn);

		layoutLineBankNumber = (LinearLayout) this.findViewById(R.id.layout_line_bank_number);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int id) {
				// TODO Auto-generated method stub
				switch (id) {
				case R.id.rb_user_from_line_bank:
					layoutLineBankNumber.setVisibility(View.VISIBLE);
					payerChannelStr = "1";
					break;

				case R.id.rb_user_from_phone_bank:
					layoutLineBankNumber.setVisibility(View.GONE);
					payerChannelStr = "2";
					break;
				}
			}
		});

		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				excuseNext();
			}
		});
	}

	/**
	 * @Title: excuseNext
	 * @Description: 执行下一步操作
	 * @param
	 * @return void
	 * @throws
	 */
	private void excuseNext() {
		payerNameStr = payerNameEdit.getText().toString().trim();
		payerPhoneStr = payerPhoneEdit.getText().toString().trim();
		if (layoutLineBankNumber.getVisibility() == View.VISIBLE) {
			lineBankNumberStr = lineBankNumberEdit.getText().toString().trim();
		}

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean regName = new RegexpBean(this.getString(R.string.payer_name), payerNameStr, "payeeName_notEmpty");
		lists.add(regName);
		RegexpBean regBankNum = new RegexpBean(this.getString(R.string.payer_phone), payerPhoneStr, "longMobile");
		lists.add(regBankNum);
		if (layoutLineBankNumber.getVisibility() == View.VISIBLE) {

		}

		if (RegexpUtils.regexpDate(lists)) {
			if (isNeedChooseAccount) {
				BaseHttpEngine.showProgressDialog();
				requestGatherAccountList();
			} else {
				toNextActivity();
			}
		}
	}

	/**
	 * @Title: toNextActivity
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void toNextActivity() {
		if ("1".equals(payerChannelStr)) {
			// WEB渠道.客户号为空
			if (TextUtils.isEmpty(lineBankNumberStr)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getText(R.string.please_input_customer_number).toString());
				return;
			}
		}

		Intent intent = new Intent(this, GatherInputPreInfoActivity.class);
		intent.putExtra(GatherInitiative.PAYER_NAME, payerNameStr);
		intent.putExtra(GatherInitiative.PAYER_MOBILE, payerPhoneStr);
		intent.putExtra(GatherInitiative.PAYER_CHANNEL, payerChannelStr);
		intent.putExtra(GatherBaseActivity.IS_NEED_CHOOSE_ACCOUNT, isNeedChooseAccount);
		// if("1".equals(payerChannelStr)){
		intent.putExtra(GatherInitiative.PAYER_CUST_ID, lineBankNumberStr);
		// }
		startActivityForResult(intent, 101);
	}

	@Override
	public void communicationCallBack(int flag) {
		// TODO Auto-generated method stub
		super.communicationCallBack(flag);
		if (flag == QUERY_GATHER_ACCOUNT_CALLBACK) {
			// 没有符合类型的收款账户
			if (StringUtil.isNullOrEmpty(GatherInitiativeData.getInstance()
					.getQueryAcountCallBackList())) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getText(R.string.no_suitable_customer_account)
								.toString());
				return;
			}
			toNextActivity();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 101 && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}

}
