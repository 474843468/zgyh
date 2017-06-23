package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MySupplymentDetailNewActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 设置交易限额
 * 
 * @author huangyuchao
 * 
 */
public class MyCrcdSetupTransMoneyActivity extends CrcdBaseActivity {

	private View view;

	TextView finc_accNumber, finc_accId, tv_cardNumber, finc_fincName;
	Button sureButton;
	/** 币种下拉框 */
	Spinner sp_cardNumber;
	/** 币种代码 */
	static String currencyCode;
	/** 币种代码名称 */
	static String strCurrencyCode;

	ArrayAdapter<String> adapter;
	/** 币种代码名称 */
	List<String> cardList = new ArrayList<String>();
	/** 币种代码 */
	List<String> currencyList = new ArrayList<String>();
	private String accountNumber = null;
	private String accountId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_setup_jiaoe_money));
		view = addView(R.layout.crcd_setup_trans_money);
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
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
				new String[] { this.getResources().getString(R.string.mycrcd_setup_jiaoe_money),
						this.getResources().getString(R.string.mycrcd_setup_info),
						this.getResources().getString(R.string.mycrcd_service_setup_success) });
		StepTitleUtils.getInstance().setTitleStep(1);

		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_accId = (TextView) view.findViewById(R.id.finc_accId);
		tv_cardNumber = (TextView) view.findViewById(R.id.tv_cardNumber);
		finc_fincName = (EditText) view.findViewById(R.id.finc_fincName);

		sp_cardNumber = (Spinner) view.findViewById(R.id.sp_cardNumber);

		cardList = MySupplymentDetailNewActivity.cardList;
		currencyList = MySupplymentDetailNewActivity.currencyList;

		adapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, cardList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.notifyDataSetChanged();
		sp_cardNumber.setAdapter(adapter);
		sp_cardNumber.setSelection(0);
		if (currencyList != null && currencyList.size() > 0) {
			currencyCode = currencyList.get(0);
		}
		if (cardList != null && cardList.size() > 0) {
			strCurrencyCode = cardList.get(0);
		}

		sp_cardNumber.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (currencyList != null && currencyList.size() > 0) {
					currencyCode = currencyList.get(position);
				}
				if (cardList != null && cardList.size() > 0) {
					strCurrencyCode = cardList.get(position);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		finc_accNumber.setText(StringUtil.getForSixForString(accountNumber));

		finc_accId.setText(StringUtil.getForSixForString(MySupplymentDetailActivity.supplyCardNumber));
		tv_cardNumber.setText(strCurrencyCode);

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// transactionAmount
				// 验证
				if (LocalData.codeNoNumber.contains(currencyCode)) {
					RegexpBean reb1 = new RegexpBean(MyCrcdSetupTransMoneyActivity.this
							.getString(R.string.mycrcd_supplymentcard_jiaoyixiane), finc_fincName.getText().toString(),
							"spetialAmount");
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb1);
					if (RegexpUtils.regexpDate(lists)) {
						requestCommConversationId();
						BaseHttpEngine.showProgressDialog();
					}
				} else {
					RegexpBean reb1 = new RegexpBean(MyCrcdSetupTransMoneyActivity.this
							.getString(R.string.mycrcd_supplymentcard_jiaoyixiane), finc_fincName.getText().toString(),
							"amount");
					ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
					lists.add(reb1);
					if (RegexpUtils.regexpDate(lists)) {
						requestCommConversationId();
						BaseHttpEngine.showProgressDialog();
					}
				}

			}
		});

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestCommConversationIdCallBack(resultObj);

		// 请求安全因子组合id
		requestGetSecurityFactor(psnSetupsecurityId);
	}

	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestGetSecurityFactorCallBack(resultObj);

		BaseDroidApp.getInstanse().showSeurityChooseDialog(new OnClickListener() {
			@Override
			public void onClick(View v) {
				psnCrcdAppertainTranSetConfirm();
			}
		});
	}

	static String amount;

	public void psnCrcdAppertainTranSetConfirm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDAPPERTAINNTRANSETCONFIRM);
		Map<String, String> map = new HashMap<String, String>();
		// map.put(Crcd.CRCD_APPLICATIONID_RES,MySupplymentDetailActivity.applicationId);
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		map.put(Crcd.CRCD_CURRENCYCODE, currencyCode);
		amount = finc_fincName.getText().toString();
		map.put(Crcd.CRCD_AMOUNT, amount);
		map.put(Crcd.CRCD_COMBINID, BaseDroidApp.getInstanse().getSecurityChoosed());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdAppertainTranSetConfirmCallBack");
	}

	/** 设置交易限额预处理接口返回的数据 */
	static Map<String, Object> returnMap;

	public void psnCrcdAppertainTranSetConfirmCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnMap = (Map<String, Object>) body.getResult();

		Intent it = new Intent(MyCrcdSetupTransMoneyActivity.this, MyCrcdSetupTransMoneyConfirmActivity.class);
		it.putExtra(ConstantGloble.CRCD_CODE, currencyCode);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
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
