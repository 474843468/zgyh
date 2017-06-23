package com.chinamworld.bocmbci.biz.crcd.mycrcd;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.MyCrcdSetupSmsConfirmActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.MyCrcdSetupTransMoneyConfirmActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class MyMasterAndSupplSetActivity extends CrcdBaseActivity{
	private static final String TAG = "MyMasterAndSupplSetActivity";
	private View view = null;
	/** 信用卡账号 */
	private String accountNumber = null;
	/** 附属卡卡号 */
	private String subaccountNumber = null;
	/** 账号id */
	private String accountId = null;
	/** 主卡卡号 */
	TextView mastercrcdNum;
	/** 附属卡卡号 */
	TextView subcrcdNum;
	
	/** 币种 */
	TextView tv_cardNumber;
	
	/** 币种下拉框 */
	Spinner sp_cardNumber;
	/** 币种代码 */
	public static String currencyCode;
	/** 币种代码名称 */
	public static String strCurrencyCode;
	/** 交易限额 */
	EditText finc_fincName;
	Button sureButton;
	/** 币种代码名称 */
	List<String> cardList = new ArrayList<String>();
	/** 币种代码 */
	List<String> currencyList = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	
	/** 编码： 1=交易限额 ,2=交易短信, */
	 int setMode=1;

	/** 编码： 0=发送主卡 ,1=发送附卡, 2=发送主卡&附卡 */
	 public static int sendMessMode;
	public static String strSendMessMode;
	
	RadioGroup rg_select;

	RadioButton rb_zhu, rb_supply, rb_zhuandsupply;
	private String accountType = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_fushu_set));
		if (view == null) {
			view = addView(R.layout.crcd_supplyment_setup_view);
		}
		accountNumber = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNUMBER_RES);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		subaccountNumber= getIntent().getStringExtra(Crcd.CRCD_SUPPLYCARD_RES);
		accountType= getIntent().getStringExtra(Crcd.CRCD_ACCOUNTTYPE_RES);
//		btn_right.setVisibility(View.GONE);
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
		if(ZHONGYIN.equals(accountType)){
			
			view.findViewById(R.id.rbtn1).setVisibility(View.GONE);
			RadioButton rbtn2=(RadioButton)view.findViewById(R.id.rbtn2);
			rbtn2.setChecked(true);
			findViewById(R.id.lin1).setVisibility(View.GONE);
			findViewById(R.id.lin2).setVisibility(View.VISIBLE);
			setMode=2;
		}
		mastercrcdNum = (TextView) view.findViewById(R.id.mastercrcdNum);
		mastercrcdNum.setText(StringUtil.getForSixForString(accountNumber));
		subcrcdNum = (TextView) view.findViewById(R.id.subcrcdNum);
		subcrcdNum.setText(StringUtil.getForSixForString(subaccountNumber));
		tv_cardNumber = (TextView) view.findViewById(R.id.tv_cardNumber);

		sp_cardNumber = (Spinner) view.findViewById(R.id.sp_cardNumber);

		finc_fincName= (EditText) view.findViewById(R.id.finc_fincName);
		
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

		tv_cardNumber.setText(strCurrencyCode);

		sureButton = (Button) view.findViewById(R.id.sureButton);
		sureButton.setOnClickListener(sureClickListener);
		
		rg_select = (RadioGroup) view.findViewById(R.id.rg_select);
		rb_zhu = (RadioButton) view.findViewById(R.id.rb_zhu);
		rb_supply = (RadioButton) view.findViewById(R.id.rb_supply);
		rb_zhuandsupply = (RadioButton) view.findViewById(R.id.rb_zhuandsupply);
		rb_zhu.setChecked(true);
		sendMessMode = 0;
		strSendMessMode = getString(R.string.mycrcd_send_zhucard);
		// 0=发送主卡 1=发送附卡 2=发送主卡&附卡
		rg_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_zhu:
					sendMessMode = 0;
					strSendMessMode = getString(R.string.mycrcd_send_zhucard);
					break;
				case R.id.rb_supply:
					sendMessMode = 1;
					strSendMessMode = getString(R.string.mycrcd_send_supplymentcard);
					break;
				case R.id.rb_zhuandsupply:
					sendMessMode = 2;
					strSendMessMode = getString(R.string.mycrcd_send_zhuandsupplycard);
					break;
				}
			}
		});

	}

	

	OnClickListener sureClickListener=new OnClickListener() {

			public void onClick(View v) {// transactionAmount
				
				if(setMode==1){
					// 验证
					if (LocalData.codeNoNumber.contains(currencyCode)) {
						RegexpBean reb1 = new RegexpBean(MyMasterAndSupplSetActivity.this
								.getString(R.string.mycrcd_supplymentcard_jiaoyixiane), finc_fincName.getText().toString(),
								"spetialAmount");
						ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
						lists.add(reb1);
						if (RegexpUtils.regexpDate(lists)) {
							requestCommConversationId();
							BaseHttpEngine.showProgressDialog();
						}
					} else {
						RegexpBean reb1 = new RegexpBean(MyMasterAndSupplSetActivity.this
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
				if(setMode==2){
					Intent it = new Intent(MyMasterAndSupplSetActivity.this, MyCrcdSetupSmsConfirmActivity.class);
					it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
					it.putExtra(Crcd.CRCD_SUPPLYCARD_RES, subaccountNumber);
					it.putExtra(Crcd.CRCD_CURRENCYCODE, currencyCode);
					it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
					// startActivity(it);
					startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);	
				}
				

			}
		
		
	};
	
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

	public static String amount;

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
	public static Map<String, Object> returnMap;

	public void psnCrcdAppertainTranSetConfirmCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		returnMap = (Map<String, Object>) body.getResult();

		Intent it = new Intent(MyMasterAndSupplSetActivity.this, MyCrcdSetupTransMoneyConfirmActivity.class);
		it.putExtra(ConstantGloble.CRCD_CODE, currencyCode);
		it.putExtra(Crcd.CRCD_CURRENCYCODE, strCurrencyCode);
		it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, accountNumber);
		it.putExtra(Crcd.CRCD_SUPPLYCARD_RES, subaccountNumber);
		it.putExtra(Crcd.CRCD_AMOUNT, amount);
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
	
	
	
	/** 设置类型*/
	/** 交易限额 */
	public void cardset_rbtn1Onclick(View v) {
		findViewById(R.id.lin1).setVisibility(View.VISIBLE);
		findViewById(R.id.lin2).setVisibility(View.GONE);
		setMode=1;
	

	}
	/** 交易短信 */
	public void cardset_rbtn2Onclick(View v) {
		findViewById(R.id.lin1).setVisibility(View.GONE);
		findViewById(R.id.lin2).setVisibility(View.VISIBLE);
		setMode=2;

	}
}
