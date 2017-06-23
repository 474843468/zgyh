package com.chinamworld.bocmbci.biz.safety.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.biz.safety.carsafety.CarSafetyBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

public class CarSafetyBaseFragment extends Fragment {

	/** 此Fragment依附的Activity */
	public CarSafetyBaseActivity activity;
	/** 主显示视图 */
	public View mMainView;	
	/** 新单/续保标识 true-续保 false-新单 */
	public String continueFlag;
	/** 行驶城市——地区下拉框，已上牌时根据选择车牌号前的地区下拉框联动，不允许更改，未上牌可以更改 */
	public Spinner spZone;
	/** 行驶城市——城市下拉框 */
	public Spinner spCity;
	/** 选择的城市代码 */
	public String cityCode;
	/** 选中的地区码 */
	public String zoneNo;
	/** 城市列表-code */
	public List<String> listCityCode = new ArrayList<String>();
	/** 行驶城市列表 */
	public List<String> listCity;
	/** 车主姓名 */
	public EditText etCarOwnerName;
	/** 车主证件类型 */
	public Spinner spCarOwnerIdType;
	/** 车主证件号码 */
	public EditText etCarOwnerId;
	/** 如果是暂存保单点继续投保跳转过来，暂存保单的数据放在这里 */
	public Map<String, Object> tempInfo;
	/** 本片段当前状态是否是暂存单继续投保状态，该标识为了防止继续投保进来基类无法区分已上牌和未上牌导致进行错误的数据恢复而写 */
	public boolean tempState = false;
	
	/**
	 * 为输入域控件填充数据
	 * 
	 * @param tempInfo
	 *            填充数据的数据源
	 */
	public void initViewData(Map<String, Object> tempInfo) {
		etCarOwnerName.setText((String) tempInfo.get(Safety.CAROWNERNAME));
		etCarOwnerId.setText((String) tempInfo.get(Safety.CAROWNERIDNO_TEMP));
		spCarOwnerIdType.setSelection(SafetyDataCenter.rqcredType.indexOf(tempInfo.get(Safety.CAROWNERIDTYPE_TEMP)));
	}
	
	/** 选择车牌号前简称下拉框的选择事件，该方法也可以在不选择下拉框的情况下触发带出省份和地区 */
	public void selectSim(String selectCityCode) {
		String str = selectCityCode.substring(0, 2) + "0000";
		List<String> selectZone = new ArrayList<String>();
		selectZone.add(SafetyDataCenter.mapCityCode_CN.get(str));
		SafetyUtils.initSpinnerView(activity, spZone, selectZone);
		if (!spZone.isEnabled()) {
			SafetyUtils.setSpinnerBackground(spZone, false);
		}
	}

	/** 用于保存用户输入信息供以后界面使用，在点查询按钮时调用 */
	public void saveUserInput() {
		// 第一次实例化为DataCenter赋值
		Map<String, Object> map = new HashMap<String, Object>();
		SafetyDataCenter.getInstance().setMapCarSafetyUserInput(map);
		map.put(Safety.CONTINUEFLAG, continueFlag);
		map.put(Safety.CITYCODE, cityCode);
		map.put(Safety.ZONENO, zoneNo);
		if (cityCode.equals("120000")) {
			// 如果是天津，需要车船税缴费地区
			map.put(Safety.TRUNLNADDRESS, SafetyDataCenter.carOnZoneCode.get(spCity.getSelectedItemPosition()));
		}
		if (continueFlag.equals("true")) {
			map.put(Safety.CAROWNERNAME, SafetyDataCenter.getInstance().getHoldProDetail().get(Safety.CAROWNERNAME));
			map.put(Safety.CAROWNERIDENTIFYTYPE, SafetyDataCenter.getInstance().getHoldProDetail().get(Safety.CAROWNERIDTYPE));
			map.put(Safety.CAROWNERIDENTIFYNO, SafetyDataCenter.getInstance().getHoldProDetail().get(Safety.CAROWNERIDNO));
		} else {
			map.put(Safety.CAROWNERNAME, etCarOwnerName.getText().toString().trim());
			map.put(Safety.CAROWNERIDENTIFYTYPE, SafetyDataCenter.rqcredType.get(spCarOwnerIdType.getSelectedItemPosition()));
			map.put(Safety.CAROWNERIDENTIFYNO, etCarOwnerId.getText().toString().trim());
		}
		map.put(Safety.CONTINUEFLAG, continueFlag);
	}

	/** 准备暂存单保存/更新接口在此页面需要的参数 */
	@SuppressWarnings("unchecked")
	public void putSaveParams() {
		Map<String, Object> params = SafetyDataCenter.getInstance().getMapSaveParams();
		if (tempState || SafetyDataCenter.getInstance().isSaved) {
			params.put(Safety.TYPEFLAG, "1");
		} else {
			params.put(Safety.TYPEFLAG, "0");
		}
		if (tempState) {
			params.put(Safety.POLICYID, SafetyDataCenter.getInstance().getMapAutoInsurPolicyDetailQry().get(Safety.POLICYID));
		} else {
			params.put(Safety.POLICYID, SafetyDataCenter.getInstance().getPolicyId());
		}
		params.put(Safety.INSURCOMPANY, "中银保险有限公司");
			
		params.put(Safety.CITYCODE, cityCode);
		if (cityCode.equals("120000")) {
			// 如果是天津，地区码上送车船税缴费地区
			params.put(Safety.TRUNLNADDRESS, SafetyDataCenter.carOnZoneCode.get(spCity.getSelectedItemPosition()));
		}
		params.put(Safety.AREACODE, zoneNo);
		// 车主姓名
		if (etCarOwnerName.getVisibility() == View.VISIBLE) {
			params.put(Safety.CAROWNERNAME, etCarOwnerName.getText().toString().trim());
		} else {
			params.put(Safety.CAROWNERNAME, ((TextView) mMainView.findViewById(R.id.tv_carOwnerName)).getText().toString().trim());
		}
		// 车主证件类型
		if (spCarOwnerIdType.getVisibility() == View.VISIBLE) {
			params.put(Safety.CAROWNERIDTYPE_D, SafetyDataCenter.rqcredType.get(spCarOwnerIdType.getSelectedItemPosition()));
		} else {
			params.put(Safety.CAROWNERIDTYPE_D, SafetyDataCenter.rqcredType.get(SafetyDataCenter.credTypeList.indexOf(
					((TextView) mMainView.findViewById(R.id.tv_carOwnerIdType_data)).getText().toString())));
		}
		// 车主证件号码
		if (etCarOwnerId.getVisibility() == View.VISIBLE) {
			params.put(Safety.CAROWNERIDNO_D, etCarOwnerId.getText().toString().trim());
		} else {
			params.put(Safety.CAROWNERIDNO_D, ((TextView) mMainView.findViewById(R.id.tv_carOwnerId_data)).getText().toString().trim());
		}
			
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		params.put(Safety.APPL_IDTYPE, SafetyDataCenter.IDENTITYTYPE_credType.get(logInfo.get(Comm.IDENTITYTYPE)));
		params.put(Safety.APPL_IDNO, logInfo.get(Comm.IDENTITYNUMBER));
		params.put(Safety.APPLPHONENO, logInfo.get(Safety.MOBILE));
	}
	
	/** 省份变化时会调用这个方法，子类需要根据省份变化而改变时覆盖此方法 */
	public void zoneChange() {}
	
	/** 省份下拉框监听事件 */
	OnItemSelectedListener zoneSelectListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (StringUtil.isNullOrEmpty(listCity)) {
				listCity = new ArrayList<String>();	
			} else {
				listCity.clear();
			}
			
			String selectZone = spZone.getSelectedItem().toString();
			String selectCityCode = SafetyDataCenter.mapCN_CityCode.get(selectZone);
			
			if (selectCityCode.equals("310000") || selectCityCode.equals("500000") || selectCityCode.equals("110000")) {
				// 北京重庆上海三个直辖市直接为城市下拉框赋值
				listCity.add(SafetyDataCenter.mapCityCode_CN.get(selectCityCode));
				SafetyUtils.initSpinnerView(activity, spCity, listCity);
				spCity.setEnabled(false);
			} else if (selectCityCode.equals("120000")) {
				// 如果是天津，二级选天津的区
				spCity.setEnabled(true);
				SafetyUtils.initSpinnerView(activity, spCity, SafetyDataCenter.carOnZoneCN);
				if (SafetyDataCenter.getInstance().isHoldToThere) {
					spCity.setSelection(SafetyDataCenter.carOnZoneCode.indexOf(SafetyDataCenter.getInstance().getHoldProDetail().get(Safety.TRUNLNADDRESS)));
				} else if (tempState) {
					// 如果是暂存保单继续投保并且是天津，二级设置天津的区
					spCity.setSelection(SafetyDataCenter.carOnZoneCode.indexOf(tempInfo.get(Safety.TRUNLNADDRESS)));
				}
			} else {
				spCity.setEnabled(true);
				// 截取到citycode前两位字符串
				String str = selectCityCode.substring(0, 2);
				String endstr = selectCityCode.substring(2, 6);
				// 用截取到的城市代码前两位遍历城市代码列表，匹配出该省份下的城市
				if (!StringUtil.isNullOrEmpty(listCityCode)) {
					listCityCode.clear();
				}
				for (int i = Integer.valueOf(SafetyDataCenter.mapCityCodeIndex.get(str)); i < SafetyDataCenter.listCityCode.size(); i++) {
					// 循环起始下标从字典中取
					if (str.equals(SafetyDataCenter.listCityCode.get(i).substring(0, 2))) {
						if (!endstr.equals(SafetyDataCenter.listCityCode.get(i).substring(2, 6))) {
							// 匹配规则：前两位相同且末四位不同，可以把已经选择的省份去掉
							listCity.add(SafetyDataCenter.mapCityCode_CN.get(SafetyDataCenter.listCityCode.get(i)));
							listCityCode.add(SafetyDataCenter.listCityCode.get(i));
							continue;
						} else {
							continue;
						}
					}
					// 因为cityCode列表相同前缀的cityCode是连着的，能走到这里说明已经把连着的代码取到了，不需要再走循环
					break;
				}
				SafetyUtils.initSpinnerView(activity, spCity, listCity);
				
				if (tempState) {
					// 如果是暂存保单继续投保并且是非直辖市，用暂存保单中的cityCode为城市下拉框赋值
					spCity.setSelection(listCity.indexOf(SafetyDataCenter.mapCityCode_CN.get(tempInfo.get(Safety.CITYCODE))));
				} else if (SafetyDataCenter.getInstance().isHoldToThere) {
					// 如果是 续保且非直辖市，用持有保单详情中的cityCode设置城市下拉框
					String holdCityCode = (String) SafetyDataCenter.getInstance().getHoldProDetail().get(Safety.CITYCODE);
					if (!StringUtil.isNull(holdCityCode)) {
						spCity.setSelection(listCity.indexOf(SafetyDataCenter.mapCityCode_CN.get(holdCityCode)));
					}
				}
			}
			if (spZone.getAdapter().getCount() == 1) {
				SafetyUtils.setSpinnerBackground(spZone, false);
			}
			if (!spCity.isEnabled()) {
				SafetyUtils.setSpinnerBackground(spCity, false);
			}
			spCity.setOnItemSelectedListener(citySelectListener);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	};
	
	/** 城市下拉框选择事件 */
	OnItemSelectedListener citySelectListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (spZone.getSelectedItem().toString().trim().equals(SafetyDataCenter.mapCityCode_CN.get("120000"))) {
				// 天津的城市码从省份下拉框取
				cityCode = "120000";
			} else {
				// 其他从城市下拉框取
				cityCode = SafetyDataCenter.mapCN_CityCode.get(spCity.getSelectedItem().toString().trim());
			}
			zoneNo = SafetyDataCenter.mapCityCode_ZoneNo.get(cityCode);

			zoneChange();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	};
}
