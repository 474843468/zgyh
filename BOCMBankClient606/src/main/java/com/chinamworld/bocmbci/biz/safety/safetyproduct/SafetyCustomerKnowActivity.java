package com.chinamworld.bocmbci.biz.safety.safetyproduct;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 用户须知
 * 
 * @author panwe
 * 
 */
public class SafetyCustomerKnowActivity extends SafetyBaseActivity implements
		OnClickListener {
	private boolean isShowguid = true;
	private View mMainView;
	/** 条款  */
	private RadioButton mButton;
	/** 房屋类型  */
	private Spinner mSpinner;
	/** 产品名称 */
	private String productName;
	/** 产品须知 */
	private String productNotice;
	/** 产品类型  */
	private String productRisktype;
	/** 保险条款 */
	private String exceptionProfe;
	/** 公司名  */
	private String companyName;
	/** 公司id */
	private String insurId;
	/** insurcode*/
	private String insurCode;
	/** 标识  */
	private boolean fromProductList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = View.inflate(this, R.layout.safety_customer_know, null);
		setTitle(this.getString(R.string.safety_cusomerknow_title));
		addView(mMainView);
		getDateForIntent();
		initView();
	}

	private void getDateForIntent() {
		fromProductList = getIntent().getBooleanExtra(SafetyConstant.PRODUCTORSAVE, false);
		companyName = getIntent().getStringExtra(Safety.INSURANCE_COMANY);
		insurId = getIntent().getStringExtra(Safety.INSURANCE_ID);
		productName = getIntent().getStringExtra(Safety.RISKNAME);
		productNotice = getIntent().getStringExtra(Safety.NOTICE);
		productRisktype = getIntent().getStringExtra(Safety.RISKTYPE);
		exceptionProfe = getIntent().getStringExtra(Safety.EXCEPTIONPROFE);
		insurCode = getIntent().getStringExtra(Safety.INSUCODE);
	}

	private void initView() {
		mButton = (RadioButton)mMainView.findViewById(R.id.profes_btn1);
		mSpinner = (Spinner)mMainView.findViewById(R.id.spinner_type);
		mMainView.findViewById(R.id.btn_profes).setOnClickListener(this);
		mMainView.findViewById(R.id.btnunaccept).setOnClickListener(this);
		mMainView.findViewById(R.id.btnaccept).setOnClickListener(this);
		((TextView) mMainView.findViewById(R.id.companyname)).setText(companyName);
		((TextView) mMainView.findViewById(R.id.productname)).setText(productName);
		((TextView) mMainView.findViewById(R.id.agreement)).setText(productNotice);
		if(productRisktype.equals(Safety.RISKTYPE_YIWAI) && checkInsurCode()){
			mMainView.findViewById(R.id.layout3).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.layout_profes).setVisibility(View.VISIBLE);
		}
		if (SafetyUtils.isHouseType(productRisktype)) {
			mMainView.findViewById(R.id.layout3).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.layout_type).setVisibility(View.VISIBLE);
			SafetyUtils.initSpinnerView(this, mSpinner, SafetyDataCenter.houseTypeList);
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,(TextView) mMainView.findViewById(R.id.companyname));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,(TextView) mMainView.findViewById(R.id.productname));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnunaccept:// 不接受
			finish();
			break;
		
		case R.id.btnaccept:// 接受
			if (SafetyUtils.isHouseType(productRisktype) && mSpinner.getSelectedItemPosition() == 3) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						this.getString(R.string.safety_customs_tip2)); return;
			}
			if (mButton.isChecked()) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						this.getString(R.string.safety_customs_tip)); return;
			}
			BaseHttpEngine.showProgressDialog();
			requestSystemDateTime();
			break;
			
		case R.id.btn_profes://条款详情
			showDialog();
			break;
		}
	}

	/** 条款详情 */
	private void showDialog() {
		View contentView = View.inflate(this, R.layout.safety_exceptionprofes, null);
		((TextView) contentView.findViewById(R.id.exceptionprofes)).setText(exceptionProfe);
		((ImageView) contentView.findViewById(R.id.top_right_close)).
		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
			}
		});
		BaseDroidApp.getInstanse().showAccountMessageDialog(contentView);
	}
	
	/** 显示条款条件   */
	private boolean checkInsurCode(){
		for (int i = 0; i < SafetyDataCenter.insurCode.size(); i++) {
			if (!StringUtil.isNull(insurCode) && insurCode.equals(SafetyDataCenter.insurCode.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && isShowguid) {
			isShowguid = false;
			showGuide();
		}
	}
	
	/** 显示引导  */
	private void showGuide() {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.toast_one, null);
		((TextView) layout.findViewById(R.id.textview)).setText("请上下滑动屏幕阅读全部内容↑↓");
		Toast toast = new Toast(this);
		toast.setView(layout);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(3000);
		toast.show();
	}
	
	/** 请求系统时间返回 */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		SafetyDataCenter.getInstance().setSysTime(dateTime);
		requestBankAcctList(SafetyDataCenter.accountTypeList);
	}
	
	/** 银行卡列表返回  */
	@Override
	public void bankAccListCallBack(Object resultObj) {
		super.bankAccListCallBack(resultObj);
		SafetyUtils.initCityData(this,true);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(new Intent(this, SafetyProductBuyMsgFillActivity.class)
		.putExtra(Safety.HOUSETYPE, mSpinner.getSelectedItemPosition())
		.putExtra(SafetyConstant.PRODUCTORSAVE, fromProductList)
		.putExtra(Safety.INSURANCE_COMANY, companyName)
		.putExtra(Safety.RISKTYPE, productRisktype)
		.putExtra(Safety.INSURANCE_ID, insurId)
		.putExtra(Safety.RISKNAME, productName)
		.putExtra(Safety.INSUCODE, insurCode));
	}
}
