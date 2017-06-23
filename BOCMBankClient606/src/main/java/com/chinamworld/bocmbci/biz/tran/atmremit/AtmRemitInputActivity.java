package com.chinamworld.bocmbci.biz.tran.atmremit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.MyJSON;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Atm取款输入信息页面
 * 
 * @author wangmengmeng
 * 
 */
public class AtmRemitInputActivity extends TranBaseActivity {
	/** atm取款输入信息页面 */
	private View view;
	// /** 从通讯录导入联系人标识 */
	// private static final int GET_CONTACT = 101;
	/** 汇出账户 */
	private TextView remitAcount;
	/** 收款人手机号 */
	private TextView editPhone;
	/** 收款人姓名 */
	private TextView editName;
	/** 币种 */
	private TextView textMoneyType;
	/** 汇款金额 */
	private EditText editMoneyAcount;
	/** 附言 */
	private EditText editFuYan;
	/** 从通讯录导入按钮 */
	// private Button chooseContact;
	/** 账户标识str */
	private String acountId;
	/** 账户号码str */
	private String acountNumber;
	/** 手机号码 str */
	private String phoneNumber;
	/** 收款人str */
	private String remitName;
	/** 汇款金额str */
	private String remitAmount;
	/** 附言str */
	private String remark;
	/** 下一步按钮 */
	private Button nextBtn;
	private Map<String, Object> atmChooseMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_atm_title));
		toprightBtn();
		view = addView(R.layout.tran_atm_input);
		init();
	}

	//
	// /** 通讯录导入 */
	// private OnClickListener openContractsListener = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO打开系统联系人界面
	// Intent intent = new Intent(Intent.ACTION_PICK);
	// intent.setType(ContactsContract.Contacts.CONTENT_TYPE);//
	// startActivityForResult(intent, GET_CONTACT);
	// }
	// };

	/**
	 * @Title: init
	 * @Description: 初始化控件和数据
	 * @param
	 * @return void
	 */
	private void init() {
		atmChooseMap = TranDataCenter.getInstance().getAtmChooseMap();
		remitAcount = (TextView) this.findViewById(R.id.remitout_account_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, remitAcount);
		acountNumber = (String) atmChooseMap.get(Acc.ACC_ACCOUNTNUMBER_RES);
		acountId = (String) atmChooseMap.get(Acc.ACC_ACCOUNTID_RES);
		editPhone = (TextView) this.findViewById(R.id.edit_get_remit_phone);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, editPhone);
		editName = (TextView) this.findViewById(R.id.edit_get_remit_name);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, editName);
		// EditTextUtils.setLengthMatcher(this, editName, 60);
		textMoneyType = (TextView) this.findViewById(R.id.remit_cashremit_tv);
		editMoneyAcount = (EditText) this.findViewById(R.id.edit_remit_money_amout);
		editFuYan = (EditText) this.findViewById(R.id.edit_message_et);
		EditTextUtils.setLengthMatcher(this, editFuYan, 50);
		nextBtn = (Button) this.findViewById(R.id.remit_input_next_btn);
		// chooseContact = (Button) this.findViewById(R.id.btn_add_contract);
		Map<String, Object> loginMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		remitName = (String) loginMap.get(Login.CUSTOMER_NAME);
		editName.setText(remitName);
		phoneNumber = (String) loginMap.get(Login.REGISTER_MOBILE);
		editPhone.setText(phoneNumber);
		remitAcount.setText(StringUtil.getForSixForString(acountNumber));
		// chooseContact.setOnClickListener(openContractsListener);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextExcuse();
			}
		});
	}

	/**
	 * @Title: nextExcuse
	 * @Description: 执行下一步
	 * @param
	 * @return void
	 */
	private void nextExcuse() {
		// phoneNumber = editPhone.getText().toString().trim();
		// remitName = editName.getText().toString().trim();
		remitAmount = editMoneyAcount.getText().toString().trim();
		remark = editFuYan.getText().toString().trim();

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// RegexpBean regBankNum = new RegexpBean(
		// this.getString(R.string.regexp_phoneNum_tran), phoneNumber,
		// "longMobile");
		// lists.add(regBankNum);
		// RegexpBean regName = new RegexpBean(
		// this.getString(R.string.get_remit_name_no_label), remitName,
		// "payeeNameDrawM");
		// lists.add(regName);
		
		if (StringUtil.isNull(remitAmount)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("请输入取款金额");
			return;
		}
		RegexpBean regAmount = new RegexpBean(this.getString(R.string.trans_atm_money_nolabel), remitAmount,
				"atmremitAmounts");
		lists.add(regAmount);
		if (RegexpUtils.regexpDate(lists)) {
			// 请求conversationId
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestGetSecurityFactor(ATMSERVICEID);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				requestPsnPasswordRemitPaymentPre();
			}
		});
	}

	/**
	 * 请求汇款预交易
	 */
	public void requestPsnPasswordRemitPaymentPre() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRAN_PSNPASSWORDREMITPAYMENTPRE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.TRAN_ATM_COMBINID_REQ, BaseDroidApp.getInstanse().getSecurityChoosed());
		map.put(Tran.TRAN_ATM_FROMACCOUNTID_REQ, (String) atmChooseMap.get(Acc.ACC_ACCOUNTID_RES));
		map.put(Tran.TRAN_ATM_PAYEENAME_REQ, remitName);
		map.put(Tran.TRAN_ATM_PAYEEMOBILE_REQ, phoneNumber);
		map.put(Tran.TRAN_ATM_AMOUNT_REQ, StringUtil.parseStringPattern(remitAmount, 2));
		map.put(Tran.TRAN_ATM_CURRENCYCODE_REQ, ConstantGloble.BOCINVT_CURRENCY_RMB);
		map.put(Tran.TRAN_ATM_TYPE_REQ, ConstantGloble.ATM_TYPE);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnPasswordRemitPaymentPreCallBack");
	}

	/**
	 * 请求汇款预交易回调
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestPsnPasswordRemitPaymentPreCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> atmpremap = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(atmpremap)) {
			return;
		}
		// TODO
		TranDataCenter.getInstance().setAtmpremap(atmpremap);
		Intent intent = new Intent(this, AtmRemitConfirmActivity.class);
		intent.putExtra(Comm.ACCOUNT_ID, acountId);
		intent.putExtra(Comm.ACCOUNTNUMBER, acountNumber);
		intent.putExtra(DrawMoney.PAYEE_MOBILE, phoneNumber);
		intent.putExtra(DrawMoney.PAYEE_NAME, remitName);
		intent.putExtra(DrawMoney.REMIT_CURRENCY_CODE, ConstantGloble.BOCINVT_CURRENCY_RMB);
		intent.putExtra(DrawMoney.REMIT_AMOUNT, remitAmount);
		intent.putExtra(DrawMoney.REMARK, remark);
		startActivity(intent);
	}

	// // 获取联系人电话
	// private String getContactPhone(Cursor cursor) {
	//
	// int phoneColumn = cursor
	// .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
	// int phoneNum = cursor.getInt(phoneColumn);
	// String phoneResult = "";
	// if (phoneNum > 0) {
	// // 获得联系人的ID号
	// int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
	// String contactId = cursor.getString(idColumn);
	// // 获得联系人的电话号码的cursor;
	// Cursor phones = getContentResolver().query(
	// ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	// null,
	// ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
	// + contactId, null, null);
	// if (phones.moveToFirst()) {
	// // 遍历所有的电话号码
	// for (; !phones.isAfterLast(); phones.moveToNext()) {
	// int index = phones
	// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
	// int typeindex = phones
	// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
	// int phone_type = phones.getInt(typeindex);
	// String phoneNumber = phones.getString(index);
	// switch (phone_type) {
	// case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
	// case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
	// phoneResult = phoneNumber;
	// break;
	// }
	// }
	// if (!phones.isClosed()) {
	// phones.close();
	// }
	// }
	// }
	// return phoneResult;
	// }
	//
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// switch (requestCode) {
	// case (GET_CONTACT):
	// if (resultCode == RESULT_OK) {
	//
	// Uri contactData = data.getData();
	// Cursor c = managedQuery(contactData, null, null, null, null);
	// c.moveToFirst();
	//
	// String payeeName = c
	// .getString(c
	// .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	// String phone = this.getContactPhone(c);
	// editPhone.setText(phone);
	// editName.setText(payeeName);
	// }
	// break;
	// default:
	// break;
	// }
	// }
}
