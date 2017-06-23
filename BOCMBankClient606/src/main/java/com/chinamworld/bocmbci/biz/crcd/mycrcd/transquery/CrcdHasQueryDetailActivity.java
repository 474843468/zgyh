package com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery;

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
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.CrcdAccountDividedActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账单分期----选择币种页面
 * 
 * @author huangyuchao
 * 
 */
public class CrcdHasQueryDetailActivity extends CrcdBaseActivity {

	private View view;
	/** 下一步 */
	private Button nextBtn;
	/** 账号 */
	private TextView tv_cardNumber;
	/** 币种 */
	private Spinner crcd_guashitype;
	String[] sArray = new String[] { strCurrency };

	String strGuashiType;
	int guaShiType;
	/** 账单分期回调----返回结果 */
	Map<String, Object> returnMap;
	/** 账单分期---上限 */
	public static String upMoney;
	/** 账单分期---下限 */
	public static String lowMoney;
	/** 账单分期标志 */
	public String fromHasQuery = "fromHasQuery";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_account_divide));
		if (view == null) {
			view = addView(R.layout.crcd_has_querydetail);
		}

		init();

	}

	public void init() {

		tv_cardNumber = (TextView) findViewById(R.id.tv_cardNumber);
		tv_cardNumber.setText(StringUtil.getForSixForString(CrcdHasQueryListActivity.accountNumber));

		crcd_guashitype = (Spinner) findViewById(R.id.crcd_guashitype);

		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, sArray);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		crcd_guashitype.setAdapter(typeAdapter);
		crcd_guashitype.setSelection(0);

		crcd_guashitype.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				guaShiType = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		nextBtn = (Button) findViewById(R.id.nextButton);
		nextBtn.setOnClickListener(nextClick);
	}

	OnClickListener nextClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			strGuashiType = crcd_guashitype.getSelectedItem().toString();
			requestCommConversationId();
			BaseHttpEngine.showProgressDialog();
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestCommConversationIdCallBack(resultObj);

		// 办理账单分期输入
		psnCrcdDividedPayBillSetInput();
	}

	/** 办理账单分期输入 */
	private void psnCrcdDividedPayBillSetInput() {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDCIVIDEDPAYBILLSETINPUT_API);
		biiRequestBody.setConversationId(String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID)));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, CrcdHasQueryListActivity.accountId);
		map.put(Crcd.CRCD_CURRENCYCODE, currency);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdDividedPayBillSetInputCallBack");
	}

	/** 办理账单分期输入 */
	public void PsnCrcdDividedPayBillSetInputCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		returnMap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(returnMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		upMoney = String.valueOf(returnMap.get(Crcd.CRCD_UPINSTMTAMOUNT));
		lowMoney = String.valueOf(returnMap.get(Crcd.CRCD_LOWINSTMTAMOUNT));
		if (StringUtil.isNull(upMoney) || StringUtil.isNull(lowMoney)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		BaseHttpEngine.dissMissProgressDialog();
		// 账单分期
		Intent it = new Intent(CrcdHasQueryDetailActivity.this, CrcdAccountDividedActivity.class);
		it.putExtra("currenncy", currency);
		it.putExtra("fromHasQuery", fromHasQuery);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, CrcdHasQueryListActivity.accountNumber);
		startActivity(it);
	}

}
