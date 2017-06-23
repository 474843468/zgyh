package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/***
 * 汽车交强险界面
 * 
 * @author Zhi
 */
public class CarJQXActivity extends CarSafetyBaseActivity {
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 主显示布局 */
	private View mMainView;
	/** 下一步按钮*/
	private Button btnNext;
	/** 下一步按钮*/
	private Button btnNextBig;
	
	private int selectPosition;
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.safety_msgfill_title);
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_carsafety_carjqx, null);
		findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
		initView();
		addView(mMainView);
	}
	
	/** 初始化界面组件*/
	private void initView() {
		setStep2();
		btnNext = (Button) mMainView.findViewById(R.id.btnNext);
		btnNextBig = (Button) mMainView.findViewById(R.id.btnNext_big);
		selectPosition = getIntent().getIntExtra("selectPosition", 0);
		Map<String, Object> carType = SafetyDataCenter.getInstance().getListAutoInsuranceQueryAutoType().get(selectPosition);
		((TextView) mMainView.findViewById(R.id.tv_vehicleBrand)).setText((String) carType.get(Safety.VEHICLEBRAND));
		((TextView) mMainView.findViewById(R.id.tv_vehicleModel)).setText((String) carType.get(Safety.VEHICLEMODEL));
		((TextView) mMainView.findViewById(R.id.tv_modelYear)).setText((String) carType.get(Safety.MODELYEAR));
		((TextView) mMainView.findViewById(R.id.tv_seatNum)).setText((String) carType.get(Safety.SEATNUM));
		((TextView) mMainView.findViewById(R.id.tv_newCarPrice)).setText(StringUtil.parseStringPattern((String) carType.get(Safety.NEWCARPRICE), 2));

		Map<String, Object> map = SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCompulsory();
		if (StringUtil.isNullOrEmpty(map.get(Safety.BIZINSBEGINDATE))) {
			mMainView.findViewById(R.id.tv_isBiz).setVisibility(View.VISIBLE);
		}
		((TextView) mMainView.findViewById(R.id.tv_InsBeginDate)).setText((String) map.get(Safety.JQXINSBEGINDATE));
		((TextView) mMainView.findViewById(R.id.tv_InsEndDate)).setText((String) map.get(Safety.JQXINSENDDATE));
		((TextView) mMainView.findViewById(R.id.tv_Amount)).setText(StringUtil.parseStringPattern((String) map.get(Safety.JQXAMOUNT), 2));
		((TextView) mMainView.findViewById(R.id.tv_Premium)).setText(StringUtil.parseStringPattern((String) map.get(Safety.JQXPREMIUM), 2));
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_Amount_key));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_vehicleModel));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_InsBeginDate));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_InsEndDate));
		
		btnNext.setOnClickListener(listener);
		btnNextBig.setOnClickListener(listener);
		if (!SafetyDataCenter.getInstance().isHoldToThere) {
			mMainView.findViewById(R.id.btnSave).setOnClickListener(saveClickListener);
		} else {
			mMainView.findViewById(R.id.btnSave).setVisibility(View.GONE);
			btnNext.setVisibility(View.GONE);
			btnNextBig.setVisibility(View.VISIBLE);
		}
	}
	
	private void setStep2() {
		mMainView.findViewById(R.id.layout_step1).setBackgroundResource(R.drawable.safety_step2);
		mMainView.findViewById(R.id.layout_step2).setBackgroundResource(R.drawable.safety_step1);
		mMainView.findViewById(R.id.layout_step3).setBackgroundResource(R.drawable.safety_step4);
		((TextView) mMainView.findViewById(R.id.index1)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.index2)).setTextColor(getResources().getColor(R.color.red));
		((TextView) mMainView.findViewById(R.id.index3)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.text1)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.text2)).setTextColor(getResources().getColor(R.color.red));
		((TextView) mMainView.findViewById(R.id.text3)).setTextColor(getResources().getColor(R.color.gray));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 4) {
			setResult(4);
			finish();
		}
	}
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			BaseHttpEngine.showProgressDialog();
			Map<String, Object> params = new HashMap<String, Object>();
			
			if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCompulsory().get(Safety.BIZINSBEGINDATE))) {
				// 如果交强险查询接口返回的商业险起期为空，说明这辆车不支持投商业险，直接调用保费试算接口，跳转时跳转到保费及车船税界面
				List<Map<String, Object>> insurForCalcuList = new ArrayList<Map<String,Object>>();
				params.put(Safety.INSURFORCALCULIST, insurForCalcuList);
				params.put(Safety.ZONENO, SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.ZONENO));
				httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCECALCULATION, "requestPsnAutoInsuranceCalculationCallBack", params, true);
			} else {
				// 否则调用查询商业险套餐接口 
				params.put(Safety.CITYCODE, SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.CITYCODE));
				params.put(Safety.ZONENO, SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.ZONENO));
				httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCEQUERYCOMMERCIAL, "requestPsnAutoInsuranceQueryCommercialCallBack", params, true);
			}
		}
	};

	/** 暂存保单按钮点击事件 */
	OnClickListener saveClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showSaveDialog();
		}
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 商业险套餐查询返回 */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsuranceQueryCommercialCallBack(Object resultObj) {
		// 商业险页面会根据calFlag来决定是否请求报价接口，本页面通信框暂时不取消，防止出现重复弹框
		Map<String, Object> resultMap = this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		SafetyDataCenter.getInstance().setMapAutoInsuranceQueryCommercial(resultMap);
		Intent intent = new Intent(this, CarBIZActivity.class);
		startActivityForResult(intent, 4);
	}
	
	/** 保费试算返回 */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsuranceCalculationCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		SafetyDataCenter.getInstance().setMapAutoInsuranceCalculation(resultMap);
		Intent intent = new Intent(this, CalculationDetilActivity.class);
		startActivityForResult(intent, 4);
	}
}
