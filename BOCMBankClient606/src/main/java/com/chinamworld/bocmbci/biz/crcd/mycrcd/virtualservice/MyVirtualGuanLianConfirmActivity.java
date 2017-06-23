package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 虚拟卡关联网银确认
 * 
 * @author huangyuchao
 * 
 */
public class MyVirtualGuanLianConfirmActivity extends CrcdAccBaseActivity {

	private View view = null;
	Button lastButton, sureButton;

	private static final int REQUEST_RANDOM = 1;
	private static final int REQUEST_RESULT = 2;

	private static int request_num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setTitle(this.getString(R.string.mycrcd_virtual_guanlian_title));
		if (view == null) {
			view = addView(R.layout.crcd_virtualcard_guanlian_confirm);
		}
		request_num = REQUEST_RANDOM;
		init();
		BaseHttpEngine.showProgressDialog();
		requestForRandomNumber();
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "queryRandomNumberCallBack");
	}

	/** 加密控件里的随机数 */
	protected String randomNumber;

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		randomNumber = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNumber)) {
			return;
		}
		// 加密控件设置随机数
		sipBox.setRandomKey_S(randomNumber);
		// init();
	}

	TextView mycrcd_account, mycrcd_shiti_num, mycrcd_xuni_num;
	EditText mycrcd_dianhua_pass;

	LinearLayout ll_sip;
	/** 加密控件 */
	private SipBox sipBox = null;

	/** 输入框密码 */
	private String password = "";
	/** 加密之后加密随机数 */
	private String password_RC = "";

	private String bankphonepasswordWrong;

	/** 初始化界面 */
	private void init() {

		bankphonepasswordWrong = this.getResources().getString(R.string.bankphone_password_wrong);

		mycrcd_account = (TextView) findViewById(R.id.mycrcd_account);
		mycrcd_shiti_num = (TextView) findViewById(R.id.mycrcd_shiti_num);
		mycrcd_xuni_num = (TextView) findViewById(R.id.mycrcd_xuni_num);

		mycrcd_account.setText(MyVirtualBCListActivity.accountId);
		mycrcd_shiti_num.setText(StringUtil.getForSixForString(MyVirtualBCListActivity.accountNumber));
		mycrcd_xuni_num.setText(StringUtil.getForSixForString(VirtualBCListActivity.virtualCardNo));
		// mycrcd_dianhua_pass = (EditText)
		// findViewById(R.id.mycrcd_dianhua_pass);

		ll_sip = (LinearLayout) findViewById(R.id.ll_activecode_sip);
		// ll_sip.setGravity(Gravity.CENTER_VERTICAL);
		sipBox = (SipBox) view.findViewById(R.id.sipbox_active);
		// add by 2016年9月12日 luqp 修改
		sipBox.setTextSize(Integer.valueOf(getResources().getString(R.string.sipboxtextsize)));
		SipBoxUtils.initSipBoxWithTwoType(sipBox, ConstantGloble.MIN_LENGTH,
				ConstantGloble.MIN_LENGTH, SipBoxUtils.KEYBOARDTYPE_NUMBOER, randomNumber, this);
//		sipBox.setCipherType(SystemConfig.CIPHERTYPE);
//		sipBox.setTextColor(getResources().getColor(android.R.color.black));
//		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		// sipBox.setGravity(Gravity.CENTER_VERTICAL);
//		sipBox.setId(10002);
//		sipBox.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//		sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBox.setPasswordRegularExpression(CheckRegExp.OTP);
//		sipBox.setSipDelegator(this);
//		sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
		// ll_sip.addView(sipBox);

		lastButton = (Button) findViewById(R.id.lastButton);
		sureButton = (Button) findViewById(R.id.sureButton);

		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				request_num = REQUEST_RESULT;
				try {
					// 验证
					RegexpBean reb1 = new RegexpBean(getString(R.string.acc_phonepwd_regex), sipBox.getText()
							.toString(), ConstantGloble.SIPBOXPSW);
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb1);
					if (RegexpUtils.regexpDate(lists)) {
						password = sipBox.getValue().getEncryptPassword();
						password_RC = sipBox.getValue().getEncryptRandomNum();
						pSNGetTokenId();
					}
				} catch (Exception e) {
					LogGloble.exceptionPrint(e);
					BaseDroidApp.getInstanse().createDialog("", e.getMessage());
					// BaseDroidApp.getInstanse().createDialog(null,
					// bankphonepasswordWrong);
				}
			}
		});
	}

	@Override
	public void aquirePSNGetTokenIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.aquirePSNGetTokenIdCallBack(resultObj);
		// 虚拟卡关联网银
		psnCrcdVirtualCardRelevanceResult();
	}

	public void psnCrcdVirtualCardRelevanceResult() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDVIRTUALCARDRELEVABCERESULT);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, MyVirtualBCListActivity.accountId);
		map.put(Crcd.CRCD_ACCNUM_RES, MyVirtualBCListActivity.accountNumber);
		map.put(Crcd.CRCD_VIRTUALCARDNO, VirtualBCListActivity.virtualCardNo);
		map.put(Crcd.CRCD_PHONEBANKPASSWORD, password);
		map.put(Crcd.CRCD_PHONEBANKPASSWORD_RC, password_RC);
		map.put(Crcd.CRCD_TOKEN, tokenId);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdVirtualCardRelevanceResultCallBack");
	}

	static Map<String, Object> returnList;

	public void psnCrcdVirtualCardRelevanceResultCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		returnList = (Map<String, Object>) body.getResult();

		Intent it = new Intent(MyVirtualGuanLianConfirmActivity.this, MyVirtualGuanLianSuccessActivity.class);
		startActivity(it);
	}

}
