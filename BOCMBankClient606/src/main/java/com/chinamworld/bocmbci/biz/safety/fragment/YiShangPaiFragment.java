package com.chinamworld.bocmbci.biz.safety.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.biz.safety.carsafety.CarMoreInfoInputTempActivity;
import com.chinamworld.bocmbci.biz.safety.carsafety.CarSafetyBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class YiShangPaiFragment extends CarSafetyBaseFragment {
	private static String TAG = "YiShangPaiFragment";

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 车牌号——地区 */
	private Spinner spLicenseNoZone;
	/** 按首字母排序后的简称列表 */
	private List<String> listLicenseNoZoneSimple;
	/** 车牌号——字母数字部分 */
	private EditText etLicenseNo;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

//	public YiShangPaiFragment(CarSafetyBaseActivity activity) {
//		this.activity = activity;
//	}

	@Override
	public void onAttach(Activity activity) {
		this.activity = (CarSafetyBaseActivity)activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 这里给默认新单，如果是续保，会在initCarSafetyContinue()方法内修改
		continueFlag = "false";
		mMainView = inflater.inflate(R.layout.safety_carsafety_fragment_yishangpai, null);
		findView(mMainView);
		viewSet();
		pageSet();
		return mMainView;
	}
	
	/** 控件赋值 */
	private void findView(View mMainView) {
		spZone = (Spinner) mMainView.findViewById(R.id.sp_zone);
		spCity = (Spinner) mMainView.findViewById(R.id.sp_city);
		spLicenseNoZone = (Spinner) mMainView.findViewById(R.id.sp_licenseNo_zone);
		etLicenseNo = (EditText) mMainView.findViewById(R.id.et_licenseNo);
		etCarOwnerName = (EditText) mMainView.findViewById(R.id.et_carOwnerName);
		spCarOwnerIdType = (Spinner) mMainView.findViewById(R.id.sp_carOwnerIdType);
		etCarOwnerId = (EditText) mMainView.findViewById(R.id.et_carOwnerId);
	}

	/** 控件设置 */
	private void viewSet() {
		spZone.setOnItemSelectedListener(zoneSelectListener);

		// 为车牌号字母数字部分添加输入控制
		etLicenseNo.setFilters(new InputFilter[] { new InputFilter.AllCaps(), // 小写字母转大写字母
				new InputFilter.LengthFilter(6)// 输入长度控制最大6位
		});
		SafetyUtils.initSpinnerView(activity, spCarOwnerIdType, SafetyDataCenter.credTypeList);
		spCarOwnerIdType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if ((spCarOwnerIdType.getSelectedItemPosition() == 0) || (spCarOwnerIdType.getSelectedItemPosition() == 1) || (spCarOwnerIdType.getSelectedItemPosition() == 3)) {
					etCarOwnerId.setFilters(new InputFilter[] { new InputFilter.LengthFilter(18) });
				} else {
					etCarOwnerId.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		spLicenseNoZone.setOnItemSelectedListener(selectListener);
	}

	/** 页面显示设置 */
	private void pageSet() {
		if (SafetyDataCenter.getInstance().isHoldToThere) {
			// 如果是续保，数据反显
			initCarSafetyContinue();
		} else if (tempState) {
			initCarSafetyTempContinue();
		} else {
			// 这句代码为车牌号前的简称赋值，因此会第一次触发该spinner的选择事件，默认选择第一个
			SafetyUtils.initSpinnerView(activity, spLicenseNoZone, listLicenseNoZoneSimple);
		}
	}
	
	public void setSimZone(List<String> list) {
		this.listLicenseNoZoneSimple = list;
	}

	/** 初始化车险续保 */
	private void initCarSafetyContinue() {
		continueFlag = "true";
		Map<String, Object> holdProDetil = SafetyDataCenter.getInstance().getHoldProDetail();

		setHaveDataItemShow(R.id.tv_carOwnerName, etCarOwnerName, (String) holdProDetil.get(Safety.CAROWNERNAME));
		setHaveDataItemShow(R.id.tv_carOwnerIdType_data, spCarOwnerIdType, SafetyDataCenter.credType.get(holdProDetil.get(Safety.CAROWNERIDTYPE)));
		setHaveDataItemShow(R.id.tv_carOwnerId_data, etCarOwnerId, (String) holdProDetil.get(Safety.CAROWNERIDNO));

		// 需要设置车牌号前的简称来触发省份和城市下拉框的事件，获取地区码和城市码
		if (((String) holdProDetil.get(Safety.LICENSENO)).length() != 7) {
			SafetyUtils.initSpinnerView(activity, spLicenseNoZone, listLicenseNoZoneSimple);
		} else {
			String str = ((String) holdProDetil.get(Safety.LICENSENO)).substring(0, 1);
			String selectCityCode = SafetyDataCenter.mapCNS_CityCode.get(str);
			// 这句代码调用了与车牌号前省份简称相同的逻辑，因此也会触发省份下拉框初始化时第一次选择事件
			selectSim(selectCityCode);
			setHaveDataItemShow(R.id.tv_licenseNo, mMainView.findViewById(R.id.ll_licenseNo_data), (String) holdProDetil.get(Safety.LICENSENO));
		}
	}

	/** 初始化车险暂存保单继续投保 */
	private void initCarSafetyTempContinue() {
		tempInfo = SafetyDataCenter.getInstance().getMapAutoInsurPolicyDetailQry();
		initViewData(tempInfo);
		// 把暂存单名称放到参数列表里
		SafetyDataCenter.getInstance().getMapSaveParams().put(Safety.POLICYNAME, tempInfo.get(Safety.POLICYNAME));
	}

	/**
	 * 设置当该字段有数据时用TextView显示，且添加浮动框
	 * 
	 * @param id
	 *            需要显示的TextView的id
	 * @param v
	 *            需要隐藏的控件
	 * @param data
	 *            要为TextView赋的值
	 */
	private void setHaveDataItemShow(int id, View v, String data) {
		if (!StringUtil.isNullOrEmpty(data)) {
			mMainView.findViewById(id).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(id)).setText(data);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(activity, (TextView) mMainView.findViewById(id));
			v.setVisibility(View.GONE);
		}
	}

	@Override
	public void initViewData(Map<String, Object> tempInfo) {
		super.initViewData(tempInfo);
		setHaveDataItemShow(R.id.tv_licenseNo, mMainView.findViewById(R.id.ll_licenseNo_data), (String) tempInfo.get(Safety.LICENSENO));
		selectSim((String) tempInfo.get(Safety.CITYCODE));
	};

	@SuppressWarnings("unchecked")
	public void query() {
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		if (cityCode.equals("110000") && !logInfo.get(Comm.IDENTITYTYPE).equals("1")) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("根据保险监管要求，车险投保人的证件类型必须为身份证。请到我行任一网点变更您的证件信息后再进行投保。");
			return;
		}
		if (continueFlag.equals("false") && !submitRegexp(true)) {
			return;
		}
		saveUserInput();
		putSaveParams();

		Map<String, Object> params = new HashMap<String, Object>();
		if (tempState && tempInfoIsChange()) {
			// 如果是暂存保单跳转过来，并且暂存信息没有被更改
			Intent intent = new Intent(activity, CarMoreInfoInputTempActivity.class);
			startActivityForResult(intent, 4);
			return;
		}
		if (continueFlag.equals("true")) {
			params.put(Safety.LICENSENO, ((TextView) mMainView.findViewById(R.id.tv_licenseNo)).getText().toString());
			params.put(Safety.CAROWNERNAME, ((TextView) mMainView.findViewById(R.id.tv_carOwnerName)).getText().toString());
		} else {
			if (tempState) {
				params.put(Safety.LICENSENO, ((TextView) mMainView.findViewById(R.id.tv_licenseNo)).getText().toString());
			} else {
				params.put(Safety.LICENSENO, spLicenseNoZone.getSelectedItem().toString().trim() + etLicenseNo.getText().toString().trim().toUpperCase());
			}
			params.put(Safety.CAROWNERNAME, etCarOwnerName.getText().toString().trim());
		}
		params.put(Safety.ZONENO, zoneNo);
		BaseHttpEngine.showProgressDialog();
		activity.getHttpTools().requestHttp(Safety.METHOD_PSNAUTOINSURANCEQUERYAUTODETAIL, "requestPsnAutoInsuranceQueryAutoDetailCallBack", params, true);
		activity.getHttpTools().registErrorCode(Safety.METHOD_PSNAUTOINSURANCEQUERYAUTODETAIL,"IBAS.2001000901");
	}

	@Override
	public void putSaveParams() {
		super.putSaveParams();
		Map<String, Object> params = SafetyDataCenter.getInstance().getMapSaveParams();
		if (mMainView.findViewById(R.id.ll_licenseNo_data).getVisibility() == View.VISIBLE) {
			params.put(Safety.LICENSENO, spLicenseNoZone.getSelectedItem().toString().trim() + etLicenseNo.getText().toString().trim().toUpperCase());
		} else {
			params.put(Safety.LICENSENO, ((TextView) mMainView.findViewById(R.id.tv_licenseNo)).getText().toString());
		}
		LogGloble.i(TAG, "车辆基本信息页-暂存保单参数列表:\n" + params.toString());
	}

	/**
	 * 判断暂存单内容与控件内容是否一致
	 * 
	 * @return 一致-true 不一致-false
	 */
	private boolean tempInfoIsChange() {
		if (tempInfo.get(Safety.CITYCODE).equals(cityCode) && tempInfo.get(Safety.CAROWNERNAME).equals(etCarOwnerName.getText().toString()) && tempInfo.get(Safety.CAROWNERIDNO_TEMP).equals(etCarOwnerId.getText().toString()) && tempInfo.get(Safety.CAROWNERIDTYPE_TEMP).equals(SafetyDataCenter.rqcredType.get(spCarOwnerIdType.getSelectedItemPosition()))) {
			return true;
		}
		return false;
	}

	@Override
	public void saveUserInput() {
		super.saveUserInput();
		Map<String, Object> userInput = SafetyDataCenter.getInstance().getMapCarSafetyUserInput();
		if (mMainView.findViewById(R.id.ll_licenseNo_data).getVisibility() == View.VISIBLE)
			userInput.put(Safety.LICENSENO, spLicenseNoZone.getSelectedItem().toString().trim() + etLicenseNo.getText().toString().trim().toUpperCase());
		else
			userInput.put(Safety.LICENSENO, ((TextView) mMainView.findViewById(R.id.tv_licenseNo)).getText().toString());

		userInput.put(Safety.NEWCARFLAG, "0");
	};

	/** 提交数据校验 */
	private boolean submitRegexp(boolean required) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		// 车牌号
		if (!tempState) {
			// 暂存保单点继续投保进来，车牌号是不可修改的，不用校验，校验在当初暂存的时候做
			if (onlyRegular(required, etLicenseNo.getText().toString())) {
				RegexpBean licenseNo = new RegexpBean(SafetyConstant.LICENSENO, etLicenseNo.getText().toString(), SafetyConstant.LICENSE_NO);
				lists.add(licenseNo);
			}
		}
		// 车主姓名
		if (onlyRegular(required, etCarOwnerName.getText().toString())) {
			RegexpBean carOwnerName = new RegexpBean(SafetyConstant.CAROWNERNAME, etCarOwnerName.getText().toString(), SafetyConstant.CAROWNER_NAME);
			lists.add(carOwnerName);
		}
		// 车主证件号码
		String AppIdType = spCarOwnerIdType.getSelectedItem().toString();
		if (AppIdType.equals(SafetyDataCenter.credTypeList.get(0)) || AppIdType.equals(SafetyDataCenter.credTypeList.get(1)) || AppIdType.equals(SafetyDataCenter.credTypeList.get(3))) {
			if (onlyRegular(required, etCarOwnerId.getText().toString().trim())) {
				RegexpBean carOwnerIdType = new RegexpBean(SafetyConstant.IDENTITYNUM_CAROWNER, etCarOwnerId.getText().toString().trim(), SafetyConstant.CAROWNER_ID);
				lists.add(carOwnerIdType);
			}
		} else {
			if (onlyRegular(required, etCarOwnerId.getText().toString().trim())) {
				RegexpBean carOwnerIdType = new RegexpBean(SafetyConstant.IDENTITYNUM_CAROWNER, etCarOwnerId.getText().toString().trim(), SafetyConstant.CARSAFETY_OTHERIDTYPE);
				lists.add(carOwnerIdType);
			}
		}
		if (RegexpUtils.regexpDate(lists)) {
			return true;
		}
		return false;
	}

	/** 只作正则校验 */
	private boolean onlyRegular(Boolean required, String content) {
		if ((!required && !StringUtil.isNull(content)) || required) {
			return true;
		}
		return false;
	}

	/** 车牌号前地区简称下拉框选择事件 */
	OnItemSelectedListener selectListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			String selectSim = spLicenseNoZone.getSelectedItem().toString();
			String selectCityCode = SafetyDataCenter.mapCNS_CityCode.get(selectSim);
			selectSim(selectCityCode);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
}
