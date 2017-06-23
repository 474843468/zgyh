package com.chinamworld.bocmbci.biz.drawmoney.remitout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.drawmoney.DrawBaseActivity;
import com.chinamworld.bocmbci.biz.drawmoney.DrawMoneyData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: RemitInputInfoActivity
 * @Description: 汇款填写信息页面 (流程为
 *               请求conversationId——请求安全因子组合——跳出安全选择框——请求“汇往取款人预交易”—
 *               —请求随机数——跳转下一页面)
 * @author JiangWei
 * @date 2013-7-16 上午11:44:36
 */
public class RemitInputInfoActivity extends DrawBaseActivity {

	private static final String TAG = RemitInputInfoActivity.class.getSimpleName();
	/** 从通讯录导入联系人标识 */
	private static final int GET_CONTACT = 101;

	/** 汇出账户 */
	private TextView remitAcount;
	/** 收款人手机号 */
	private EditText editPhone;
	/** 收款人姓名 */
	private EditText editName;
	/** 币种 */
	private TextView textMoneyType;
	/** 汇款金额 */
	private EditText editMoneyAcount;
	/** 附言 */
	private EditText editFuYan;
	/** 从通讯录导入按钮 */
	private Button chooseContact;
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
	/** 安全因子数组 */
	private List<Map<String, Object>> combineList;
	/** 会话id */
	private String conversationId;
	/** 随机数 */
	private String randomNumber;
	/** 是否需要otp */
	private boolean isNeedOtp;
	/** 是否需要smc */
	private boolean isNeedSmc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(R.layout.drawmoney_remit_input_info, null);
		tabcontent.addView(view);
		setTitle(R.string.remitout_title);

		acountId = getIntent().getStringExtra(Comm.ACCOUNT_ID);
		acountNumber = getIntent().getStringExtra(Comm.ACCOUNTNUMBER);

		init();
	}

	/** 通讯录导入 */
	private OnClickListener openContractsListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO打开系统联系人界面
			// Intent intent = new Intent(Intent.ACTION_PICK);
			// intent.setType(ContactsContract.Contacts.CONTENT_TYPE);//
			// startActivityForResult(intent, GET_CONTACT);
			Intent it = new Intent();
			it.setAction(Intent.ACTION_GET_CONTENT);
			it.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
			startActivityForResult(it, GET_CONTACT);
		}
	};

	/**
	 * @Title: init
	 * @Description: 初始化控件和数据
	 * @param
	 * @return void
	 */
	private void init() {
		remitAcount = (TextView) this.findViewById(R.id.remitout_account_tv);
		editPhone = (EditText) this.findViewById(R.id.edit_get_remit_phone);
		editName = (EditText) this.findViewById(R.id.edit_get_remit_name);
		EditTextUtils.setLengthMatcher(this, editName, 56);
		textMoneyType = (TextView) this.findViewById(R.id.remit_cashremit_tv);
		editMoneyAcount = (EditText) this.findViewById(R.id.edit_remit_money_amout);
		editFuYan = (EditText) this.findViewById(R.id.edit_message_et);
		EditTextUtils.setLengthMatcher(this, editFuYan, 50);
		nextBtn = (Button) this.findViewById(R.id.remit_input_next_btn);
		chooseContact = (Button) this.findViewById(R.id.btn_add_contract);

		remitAcount.setText(StringUtil.getForSixForString(String.valueOf(acountNumber)));
		chooseContact.setOnClickListener(openContractsListener);
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
		phoneNumber = editPhone.getText().toString().trim();
		remitName = editName.getText().toString().trim();
		remitAmount = editMoneyAcount.getText().toString().trim();
		remark = editFuYan.getText().toString().trim();

		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean regBankNum = new RegexpBean(this.getString(R.string.payeeMobile_str), phoneNumber, "longMobile");
		lists.add(regBankNum);
		RegexpBean regName = new RegexpBean(this.getString(R.string.get_remit_name_no_label), remitName,
				"payeeNameDrawM");
		lists.add(regName);
		RegexpBean regAmount = new RegexpBean(this.getString(R.string.remit_money_amout_no_label), remitAmount,
				"remitAmount");
		lists.add(regAmount);
		RegexpBean regRemark = new RegexpBean(this.getString(R.string.fuYan_str), remitAmount, "memo");
		lists.add(regRemark);
		if (RegexpUtils.regexpDate(lists)) {
			// 请求conversationId
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	}

	/**
	 * @Title: requestPsnMobileRemitPre
	 * @Description: 发送“汇往取款人预交易”接口的请求
	 * @param
	 * @return void
	 */
	private void requestPsnMobileRemitPre() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(DrawMoney.PSN_MOBILE_REMIT_PRE);
		biiRequestBody.setConversationId(conversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Comm.ACCOUNT_ID, acountId);
		map.put(DrawMoney.PAYEE_MOBILE, phoneNumber);
		map.put(DrawMoney.PAYEE_NAME, remitName);
		map.put(DrawMoney.REMIT_CURRENCY_CODE, "001");
		map.put(DrawMoney.REMIT_AMOUNT, remitAmount);
		map.put(DrawMoney.REMARK, remark);
		map.put(DrawMoney.COMBIN_ID, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnMobileRemitPreCallback");
	}

	/**
	 * @Title: requestPsnMobileRemitPreCallback
	 * @Description: “汇往取款人预交易”接口的请求的回调
	 * @param @param resultObj
	 * @return void
	 */
	public static Map<String, Object> result = null;
	public void requestPsnMobileRemitPreCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		result = (Map<String, Object>) biiResponseBody.getResult();
//		combineList = (List<Map<String, Object>>) result.get(DrawMoney.FACTORLIST);
//		if (StringUtil.isNullOrEmpty(combineList)) {
//			BaseHttpEngine.dissMissProgressDialog();
//			return;
//		}
		// 获取factorList 判断交易需要的安全因子
//		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
//		for (int i = 0; i < combineList.size(); i++) {
//			Map<String, Object> defaultCombine = (Map<String, Object>) combineList.get(i).get("field");
//			listMap.add(defaultCombine);
//		}
//		if (combineList.size() == 1) {
//			String str = (String) listMap.get(0).get(Login.NAME);
//			if (str.equals(DrawMoney.SMC)) {
//				isNeedSmc = true;
//				isNeedOtp = false;
//			} else {
//				isNeedSmc = false;
//				isNeedOtp = true;
//			}
//		} else {
//			isNeedSmc = true;
//			isNeedOtp = true;
//		}
		// 请求随机数
		requestForRandomNumber();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		// 请求安全因子
		requestGetSecurityFactor("PB154");
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 请求预交易
				BaseHttpEngine.showProgressDialog();
				requestPsnMobileRemitPre();
			}
		});
	}

	/**
	 * @Title: requestForRandomNumber
	 * @Description: 请求随机数
	 * @param
	 * @return void
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId(conversationId);
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	/**
	 * @Title: queryRandomNumberCallBack
	 * @Description: 请求随机数的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		DrawMoneyData.getInstance().setRandomNumber(randomNumber);

		Intent intent = new Intent(this, RemitConfirmActivity.class);
		intent.putExtra(Comm.ACCOUNT_ID, acountId);
		intent.putExtra(Comm.ACCOUNTNUMBER, acountNumber);
		intent.putExtra(DrawMoney.PAYEE_MOBILE, phoneNumber);
		intent.putExtra(DrawMoney.PAYEE_NAME, remitName);
		intent.putExtra(DrawMoney.REMIT_CURRENCY_CODE, "001");
		intent.putExtra(DrawMoney.REMIT_AMOUNT, remitAmount);
		intent.putExtra(DrawMoney.REMARK, remark);
//		intent.putExtra(DrawMoney.ISNEEDSMC, isNeedSmc);
//		intent.putExtra(DrawMoney.ISNEEDOTP, isNeedOtp);
		startActivityForResult(intent, 1001);
	}

	// 获取联系人电话
	private String getContactPhone(Cursor cursor) {

		int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		String phoneResult = "";
		if (phoneNum > 0) {
			// 获得联系人的ID号
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId = cursor.getString(idColumn);
			// 获得联系人的电话号码的cursor;
			Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
			if (phones.moveToFirst()) {
				// 遍历所有的电话号码
				for (; !phones.isAfterLast(); phones.moveToNext()) {
					int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
					int phone_type = phones.getInt(typeindex);
					String phoneNumber = phones.getString(index);
					switch (phone_type) {
					case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
					case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
						phoneResult = phoneNumber;
						break;
					}
				}
				if (!phones.isClosed()) {
					phones.close();
				}
			}
		}
		return phoneResult;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case (GET_CONTACT):
			if (resultCode == RESULT_OK) {
				String payeeName = null;
				String phone = null;
				Uri contactData = data.getData();
				if (contactData != null) {
					Cursor c = managedQuery(contactData, null, null, null, null);
					if (c != null && c.moveToFirst()) {
						try {
							payeeName = c.getString(c.getColumnIndexOrThrow(People.NAME));
						} catch (Exception e) {
							LogGloble.e(TAG, e.getMessage(), e);
						}
						if (payeeName == null) {
							try {
								payeeName = c.getString(c.getColumnIndexOrThrow(People.DISPLAY_NAME));
							} catch (Exception e) {
								LogGloble.e(TAG, e.getMessage(), e);
							}
						}
						if (phone == null) {
							try {
								// 小米
								phone = c.getString(c.getColumnIndexOrThrow("data1"));
							} catch (Exception e) {
								LogGloble.e(TAG, e.getMessage(), e);
							}
						}
						if (phone == null) {
							try {
								phone = c.getString(c.getColumnIndexOrThrow(People.NUMBER));
							} catch (Exception e) {
								LogGloble.e(TAG, e.getMessage(), e);
							}
						}

					}
				}
				if (payeeName == null || phone == null) {
					payeeName = "";
					phone = "";
				} else {
					phone = phone.replace(" ", "");
					phone = phone.replace("+86", "");
				}
				editPhone.setText(phone);
				editName.setText(payeeName);
			}
			break;
		case (1001):
			if (resultCode == RESULT_OK) {
				setResult(RESULT_OK);
				finish();
			}
			break;
		}
	}

}
