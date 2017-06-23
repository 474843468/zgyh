package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.adapter.CarTypeListAdapter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 车型选择界面
 * 
 * @author Zhi
 */
public class CarTypeChooseActivity extends CarSafetyBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 下一步按钮 */
	private Button btnNext;
	/** 下一步按钮 */
	private Button btnNextBig;
	/** 车型列表 */
	private ListView lvCarType;
	/** 车型列表适配器 */
	private CarTypeListAdapter mAdapter;
	/** 主显示布局 */
	private View mMainView;
	/** 列表选中项下标 */
	private int selectPosition = -1;
	/** 用户输入的数据 */
	Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.safety_msgfill_title);
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_carsafety_choose_cartype, null);
		findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
		initView();
		addView(mMainView);
	}
	
	private void initView() {
		((TextView) mMainView.findViewById(R.id.tv_safetytype)).setText("车险");
		((TextView) mMainView.findViewById(R.id.tv_company)).setText("中银保险有限公司");
		lvCarType = (ListView) mMainView.findViewById(R.id.lv_cartype);
		btnNext = (Button) mMainView.findViewById(R.id.btnNext);
		btnNextBig = (Button) mMainView.findViewById(R.id.btnNext_big);
		
		List<Map<String, Object>> carTypeList = SafetyDataCenter.getInstance().getListAutoInsuranceQueryAutoType();
		if (carTypeList.size() == 1) {
			mAdapter = new CarTypeListAdapter(this, carTypeList, 0);
			selectPosition = 0;
		} else {
			mAdapter = new CarTypeListAdapter(this, carTypeList, -1);
			selectPosition = -1;
		}
		lvCarType.setAdapter(mAdapter);
		lvCarType.setOnItemClickListener(selectListener);
		btnNext.setOnClickListener(clickListener);
		btnNextBig.setOnClickListener(clickListener);
		if (!SafetyDataCenter.getInstance().isHoldToThere) {
			mMainView.findViewById(R.id.btnSave).setOnClickListener(saveClickListener);
		} else {
			mMainView.findViewById(R.id.btnSave).setVisibility(View.GONE);
			btnNext.setVisibility(View.GONE);
			btnNextBig.setVisibility(View.VISIBLE);
		}
	}
	
	/** 请求交强险查询接口 */
	private void requestInsuranceQuery() {
		// 保存用户选择的车辆信息 
		SafetyDataCenter.getInstance().getMapCarSafetyUserInput().put("UserChooseVehicle", SafetyDataCenter.getInstance().getListAutoInsuranceQueryAutoType().get(selectPosition));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Safety.CITYCODE, userInput.get(Safety.CITYCODE));
		params.put(Safety.VEHICLEINDEX, SafetyDataCenter.getInstance().getListAutoInsuranceQueryAutoType().get(selectPosition).get(Safety.VEHICLEINDEX));
		if (!StringUtil.isNullOrEmpty(userInput.get(Safety.LICENSENO))) {
			params.put(Safety.LICENSENO, userInput.get(Safety.LICENSENO));
		}
		params.put(Safety.CAROWNERNAME, userInput.get(Safety.CAROWNERNAME));
		params.put(Safety.FUELTYPE, userInput.get(Safety.FUELTYPE));
		if (userInput.get(Safety.CITYCODE).equals("120000")) {
			params.put(Safety.TRUNLNADDRESS, userInput.get(Safety.TRUNLNADDRESS));
		}
		params.put(Safety.ZONENO, userInput.get(Safety.ZONENO));
		httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCEQUERYCOMPULSORY, "requestPsnAutoInsuranceQueryCompulsoryCallBack", params, true);
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
	
	OnItemClickListener selectListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectPosition = position;
			mAdapter.setSelectedPosition(position);
		}
	};
	
	OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (selectPosition < 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("请选择您的车型");
				return;
			}
			BaseHttpEngine.showProgressDialog();
			userInput.put("selectPosition", selectPosition);
			if (userInput.get(Safety.ZONENO).equals("0010") && userInput.get(Safety.NEWCARFLAG).equals("1")) {
				// 北京地区新车需要先调用新车报备接口
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Safety.VEHICLEINDEX, SafetyDataCenter.getInstance().getListAutoInsuranceQueryAutoType().get(selectPosition).get(Safety.VEHICLEINDEX));
				params.put(Safety.CAROWNERNAME, userInput.get(Safety.CAROWNERNAME));
				params.put(Safety.CAROWNERIDENTIFYTYPE, userInput.get(Safety.CAROWNERIDENTIFYTYPE));
				params.put(Safety.CAROWNERLDENTIFYNO, userInput.get(Safety.CAROWNERLDENTIFYNO));
				params.put(Safety.ENGINENO, userInput.get(Safety.ENGINENO));
				params.put(Safety.FRAMENO, userInput.get(Safety.FRAMENO));
				params.put(Safety.FUELTYPE, userInput.get(Safety.FUELTYPE));
				params.put(Safety.INVOICENO, userInput.get(Safety.INVOICENO));
				params.put(Safety.INVOICEDATE, userInput.get(Safety.ENROLLDATE));
				params.put(Safety.ZONENO, userInput.get(Safety.ZONENO));
				httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCENEWAUTORECORD, "requestPsnAutoInsuranceNewAutoRecordCallBack", params, true);
			} else {
				requestInsuranceQuery();
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
	
	/** 交强险查询（投保查询）回调方法 */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsuranceQueryCompulsoryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) { return; }
		
		Map<String, Object> resultMap = this.getHttpTools().getResponseResult(resultObj);
		SafetyDataCenter.getInstance().setMapAutoInsuranceQueryCompulsory(resultMap);
		
		Intent intent = new Intent(this, CarJQXActivity.class).putExtra("selectPosition", selectPosition);
		startActivityForResult(intent, 4);
	}
	
	/** 新车报备回调方法 */
	public void requestPsnAutoInsuranceNewAutoRecordCallBack(Object resultObj) {
		requestInsuranceQuery();
	}
}