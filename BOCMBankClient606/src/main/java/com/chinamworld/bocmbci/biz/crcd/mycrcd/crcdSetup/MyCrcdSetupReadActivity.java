package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 信用卡还款方式阅读声明
 * 
 * @author huangyuchao
 * 
 */
public class MyCrcdSetupReadActivity extends CrcdBaseActivity {

	private View view;
	/** lastButton--不接受，sureButton----接受 */
	Button lastButton, sureButton;
	/** 人民币账户----464格式化 */
	static ArrayList<String> benNumberArray = new ArrayList<String>();
	/** 外币账户----464格式化 */
	static ArrayList<String> waiNumberArray = new ArrayList<String>();
	/** 人民币账户 */
	static ArrayList<String> benIdArray = new ArrayList<String>();
	/** 外币账户 */
	static ArrayList<String> waiIdArray = new ArrayList<String>();
	static String currency1 = "";
	static String currency2 = "";
	private String codeCode1 = null;
	private String codeCode2 = null;
	Map<String, Object> currencyMap1;
	Map<String, Object> currencyMap2;
	private String accountId = null;
	private String accountNumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_money_setup_title));
		view = addView(R.layout.crcd_payment_setup_read);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		init();
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getResources().getString(R.string.mycrcd_read_shengming),
						this.getResources().getString(R.string.mycrcd_setup_style),
						this.getResources().getString(R.string.mycrcd_setup_info) });
		StepTitleUtils.getInstance().setTitleStep(1);

		lastButton = (Button) findViewById(R.id.lastButton);
		sureButton = (Button) findViewById(R.id.sureButton);

		lastButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 查询信用卡币种---当前账户是否双币种
				psnCrcdCurrencyQuery();
			}
		});

	}

	public void psnCrcdCurrencyQuery() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDCURRENCYQUERY);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdCurrencyQueryCallBack");
	}

	public void psnCrcdCurrencyQueryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		currencyMap1 = (Map<String, Object>) resultMap.get(Crcd.CRCD_CURRENCY1);
		currencyMap2 = (Map<String, Object>) resultMap.get(Crcd.CRCD_CURRENCY2);

		if (!StringUtil.isNullOrEmpty(currencyMap1)) {
			currency1 = String.valueOf(currencyMap1.get(Crcd.CRCD_CODE));
			codeCode1 = (String) currencyMap1.get(Crcd.CRCD_CODE);
		}else{
			currency1=null;
			codeCode1=null;
		}
		if (!StringUtil.isNullOrEmpty(currencyMap2)) {
			currency2 = String.valueOf(currencyMap2.get(Crcd.CRCD_CODE));
			codeCode2 = (String) currencyMap2.get(Crcd.CRCD_CODE);
		}else{
			currency2=null;
			codeCode2=null;
		}
		// 获取账户列表
		requestCrcdList();
	}

	public void requestCrcdList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.QRY_CRCD_LIST_API);
		Map<String, Object> params = new HashMap<String, Object>();
		String[] s = { COMMONHUOQI, GREATWALL, GREATWALL_CREDIT, HUOQIBENTONG };
		params.put(Crcd.CRCD_ACCOUNTTYPE_REQ, s);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestCrcdListCallBack");
	}

	private void clearDate() {
		if (waiNumberArray != null && !waiNumberArray.isEmpty()) {
			waiNumberArray.clear();
		}
		if (waiIdArray != null && !waiIdArray.isEmpty()) {
			waiIdArray.clear();
		}
		if (benNumberArray != null && !benNumberArray.isEmpty()) {
			benNumberArray.clear();
		}
		if (benIdArray != null && !benIdArray.isEmpty()) {
			benIdArray.clear();
		}
	}

	/**
	 * 请求账户列表回调
	 */
	public void requestCrcdListCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		List<Map<String, Object>> returnList = (List<Map<String, Object>>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList) || returnList.size() == 0) {
			BaseHttpEngine.dissMissProgressDialog();
		}
		clearDate();
		/** 人民币列表---人民币还款账户 */
		for (int i = 0; i < returnList.size(); i++) {
			if (COMMONHUOQI.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| GREATWALL.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| GREATWALL_CREDIT.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| HUOQIBENTONG.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))) {
				Map<String, Object> hm = (Map<String, Object>) returnList.get(i);
				String accountNum = String.valueOf(hm.get(Crcd.CRCD_ACCOUNTNUMBER_RES));
				String accountId = String.valueOf(hm.get(Crcd.CRCD_ACCOUNTID_RES));
				// 不能包含当前设定的卡

				if (!accountNumber.equals(accountNum)) {
					benNumberArray.add(StringUtil.getForSixForString(accountNum));
					benIdArray.add(accountId);
				}
			}

		}
		/** 外币列表--外币还款账户 */
		for (int i = 0; i < returnList.size(); i++) {
			if (COMMONHUOQI.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| GREATWALL_CREDIT.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
					|| HUOQIBENTONG.equals(returnList.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))) {
				Map<String, Object> hm = (Map<String, Object>) returnList.get(i);
				String accountNum = String.valueOf(hm.get(Crcd.CRCD_ACCOUNTNUMBER_RES));
				String accountId = String.valueOf(hm.get(Crcd.CRCD_ACCOUNTID_RES));

				// 不能包含当前设定的卡
				if (!accountNumber.equals(accountNum)) {
					waiNumberArray.add(StringUtil.getForSixForString(accountNum));
					waiIdArray.add(accountId);
				}
			}
		}
		if (benNumberArray.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.crcd_no_renlistaccount));
			return;
		} else {
			isNeedWaiBiArray();
		}

	}

	/** 根据币种判断账户列表 */
	private void isNeedWaiBiArray() {
		if (!StringUtil.isNullOrEmpty(codeCode1) && StringUtil.isNullOrEmpty(codeCode2)) {
			if (!StringUtil.isNull(codeCode1) && LocalData.rmbCodeList.contains(codeCode1)) {
				// 人民币存在---人民币账户
				gotoActivity();
			} else if (!StringUtil.isNull(codeCode1) && !LocalData.rmbCodeList.contains(codeCode1)) {
				// 单外币
				// 单外币---人民币账户、外币账户
				if (waiNumberArray.size() > 0) {
					gotoActivity();
				} else {
					BaseHttpEngine.dissMissProgressDialog();
					BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.crcd_no_renlistaccount_two));
					return;
				}
			}
		} else if (!StringUtil.isNullOrEmpty(codeCode1) && !StringUtil.isNullOrEmpty(codeCode2)) {
			// 双币种存在
			if (waiNumberArray.size() > 0) {
				gotoActivity();
			} else {
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.crcd_no_renlistaccount_two));
				return;

			}
		}
	}

	/** 跳转到下一页面---信用卡还款方式 */
	private void gotoActivity() {
		BaseHttpEngine.dissMissProgressDialog();
		Intent it = new Intent(MyCrcdSetupReadActivity.this, CrcdPaymentwaySetup.class);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_CURRENCY1, codeCode1);
		it.putExtra(Crcd.CRCD_CURRENCY2, codeCode2);
		// startActivity(it);
		startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}
}
