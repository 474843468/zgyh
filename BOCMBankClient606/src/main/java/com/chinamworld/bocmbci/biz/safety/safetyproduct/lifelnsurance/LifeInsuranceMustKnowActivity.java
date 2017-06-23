package com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class LifeInsuranceMustKnowActivity extends LifeInsuranceBaseActivity {

	private static final String TAG = "LifeInsuranceMustKnow";
	/** 是否同意协议 */
	private CheckBox cbIsCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = addView(R.layout.safety_life_product_mustknow);
		setTitle(R.string.safety_cusomerknow_title);
		findView();
		viewSet();
	}

	@Override
	protected void findView() {
		cbIsCheck = (CheckBox) mMainView.findViewById(R.id.cb_isCheck);
	}

	@Override
	protected void viewSet() {
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
		((TextView) mMainView.findViewById(R.id.tv_company)).setText((String) userInput.get(Safety.INSURANCE_COMANY));
		((TextView) mMainView.findViewById(R.id.tv_subCompany)).setText((String) userInput.get(Safety.SUBINSUNAME));
		((TextView) mMainView.findViewById(R.id.tv_productName)).setText((String) userInput.get(Safety.RISKNAME));
		((TextView) mMainView.findViewById(R.id.tv_productCode)).setText((String) userInput.get(Safety.INSUCODE));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_company));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_productName));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_productCode));
		mMainView.findViewById(R.id.btnNext).setOnClickListener(nextListener);
//		mMainView.findViewById(R.id.btnQuit).setOnClickListener(quitListener);
		mMainView.findViewById(R.id.tv_isCheckTip).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (cbIsCheck.isChecked()) {
					cbIsCheck.setChecked(false);
				} else {
					cbIsCheck.setChecked(true);
				}
			}
		});

//		mMainView.findViewById(R.id.btnNext).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_gray_long_normal));
		mMainView.findViewById(R.id.btnNext).setOnClickListener(null);
		cbIsCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
//					mMainView.findViewById(R.id.btnNext).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_red_big_long));
					mMainView.findViewById(R.id.btnNext).setOnClickListener(nextListener);
				} else {
//					mMainView.findViewById(R.id.btnNext).setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_gray_long_normal));
					mMainView.findViewById(R.id.btnNext).setOnClickListener(null);
				}
			}
		});
		
		mMainView.findViewById(R.id.tv_tiaoKuan).setOnClickListener(toWebListener);
	}

	/** 处理栏位控制信息 */
	private void analysisControlInfo(String controlInfo) {
		String[] strLetterList = controlInfo.split(";");
		List<String[]> a = new ArrayList<String[]>();
		List<String[]> b = new ArrayList<String[]>();
		List<String[]> c = new ArrayList<String[]>();
		List<String[]> d = new ArrayList<String[]>();
		List<String[]> e = new ArrayList<String[]>();

		for (int i = 0; i < strLetterList.length; i++) {
			String[] strLetter = strLetterList[i].split(",");
			if (strLetter[0].substring(0, 1).equals("A")) {
				a.add(strLetter);
			} else if (strLetter[0].substring(0, 1).equals("B")) {
				b.add(strLetter);
			} else if (strLetter[0].substring(0, 1).equals("C")) {
				c.add(strLetter);
			} else if (strLetter[0].substring(0, 1).equals("D")) {
				d.add(strLetter);
			} else if (strLetter[0].substring(0, 1).equals("E")) {
				e.add(strLetter);
			}
		}

		SafetyDataCenter.getInstance().setControlInfoA(a);
		SafetyDataCenter.getInstance().setControlInfoB(b);
		SafetyDataCenter.getInstance().setControlInfoC(c);
		SafetyDataCenter.getInstance().setControlInfoD(d);
		SafetyDataCenter.getInstance().setControlInfoE(e);
	}
	
	/** 产品条款链接至web事件 */
	private OnClickListener toWebListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String url = "http://srh.bankofchina.com/search/finprod/getSecuritiesTrader.jsp?keyword=";
			Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
			String params = userInput.get(Safety.INSURANCE_ID) + "_" + userInput.get(Safety.INSUCODE);
//			String params = "0024_6205";// 测试用
			Uri uri = Uri.parse(url + params);  
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
            startActivity(intent);  
		}
	};

	/** 下一步事件 */
	private OnClickListener nextListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!cbIsCheck.isChecked()) {
				// 复选框未勾选时按钮事件为空，这里不用处理
			}
			BaseHttpEngine.showProgressDialog();
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapUserInput();
			params.put(Safety.INSURANCE_ID, userInput.get(Safety.INSURANCE_ID));
			params.put(Safety.RISKCODE, SafetyDataCenter.getInstance().getListLifeInsuranceProductQuery().get((Integer) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(SELECTPOSITION)).get(Safety.RISKCODE));
			getHttpTools().requestHttp(Safety.PSNINSURANCEFIELDCONTROLINFOQUERY, "requestPsnInsuranceFieldControlInfoQueryCallBack", params);
		}
	};

	/** 退出事件 */
//	private OnClickListener quitListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			finish();
//		}
//	};

	/** 产品栏位控制信息查询回调 */
	public void requestPsnInsuranceFieldControlInfoQueryCallBack(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		analysisControlInfo((String) resultMap.get(Safety.CONTROLINFO));
		// 栏位控制信息解析完成，删除返回数据中该字段
		resultMap.remove(Safety.CONTROLINFO);
		SafetyDataCenter.getInstance().setMapInsuranceFieldControlInfoQuery(resultMap);
		// 查询客户信息
		httpTools.requestHttp(Safety.METHOD_PSNSVRPSNINFOQUERY, "requestPsnSVRPsnInfoQueryCallBack", null, false);
	}
	
	/** 查询客户资料回调 */
	public void requestPsnSVRPsnInfoQueryCallBack(Object resultObj) {
		Map<String, Object> userInfo = HttpTools.getResponseResult(resultObj);
		SafetyDataCenter.getInstance().setMapSVRPsnInfoQuery(userInfo);
		
		requestBankAcctList(SafetyDataCenter.lifeAccountTypeList);
	}
	
	/** 银行卡列表返回  */
	@Override
	public void bankAccListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		List<Map<String, Object>> cardList = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(cardList)) {
			BaseDroidApp.getInstanse().showMessageDialog("没有可用的缴费账户", null);
			return;
		}
		if (cardList.size() != 1) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Acc.ACC_ACCOUNTTYPE, "请选择");
			cardList.add(0, map);
		}

		// 临时存储账户列表
		SafetyDataCenter.getInstance().setAcctList(cardList);
		try {
			SafetyUtils.initCityData(this, true);
		} catch (Exception e) {
			LogGloble.e(TAG, e.toString());
		}
		Intent intent = new Intent(LifeInsuranceMustKnowActivity.this, LifeInsuranceInfoInputActivity.class);
		startActivityForResult(intent, 4);
	}
}
