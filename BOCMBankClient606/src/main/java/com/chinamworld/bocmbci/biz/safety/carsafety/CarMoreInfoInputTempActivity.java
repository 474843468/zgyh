package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.Map;

import android.os.Bundle;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.utils.StringUtil;

public class CarMoreInfoInputTempActivity extends CarMoreInfoInputBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		System.out.println("CarMoreInfoInputTempActivity");
	}
	
	@Override
	public void initView() {
		super.initView();
		btnSave.setOnClickListener(saveClickListener);
		btnCommitQuery.setOnClickListener(listener);
		initViewData();
	}
	
	@Override
	public void initViewData() {
		// 取出车辆补充信息查询返回的数据
		Map<String, Object> map = SafetyDataCenter.getInstance().getMapAutoInsuranceQueryAutoDetail();
		// 取出暂存保单数据
		Map<String, Object> tempMap = SafetyDataCenter.getInstance().getMapAutoInsurPolicyDetailQry();
		if (!StringUtil.isNullOrEmpty(map)) {
			
			setHaveDataItemShow(R.id.tv_brandName, mMainView.findViewById(R.id.ll_brandName_input), (String) map.get(Safety.BRANDNAME));
			setHaveDataItemShow(R.id.tv_engineNo, mMainView.findViewById(R.id.ll_engineNo_input), (String) map.get(Safety.ENGINENO));
			setHaveDataItemShow(R.id.tv_frameNo, mMainView.findViewById(R.id.ll_frameNo_input), (String) map.get(Safety.FRAMENO));
			etBrandName.setVisibility(View.GONE);
			etEngineNo.setVisibility(View.GONE);
			etFrameNo.setVisibility(View.GONE);
			if (!SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.ZONENO).equals("0010")) {
				if (!StringUtil.isNullOrEmpty(map.get(Safety.ENROLLDATE))) {
					setHaveDataItemShow(R.id.tv_enrollDate_show, mMainView.findViewById(R.id.ll_enrollDate_input), (String) map.get(Safety.ENROLLDATE));
					tvEnrollDate.setVisibility(View.GONE);
				} else {
					tvEnrollDate.setText(SafetyDataCenter.getInstance().getSysTime());
				}
			}
			if (!StringUtil.isNullOrEmpty(map.get(Safety.FUELTYPE))) {
				setHaveDataItemShow(R.id.tv_fuelType, spFuelType, SafetyDataCenter.fuelTypeCN.get(SafetyDataCenter.fuelTypeCode.indexOf(map.get(Safety.FUELTYPE))));
			} else {
				spFuelType.setSelection(0);
			}
		} else if (!StringUtil.isNullOrEmpty(tempMap)) {
			
			String areaCode = (String) tempMap.get(Safety.AREACODE);
			String isEdit = (String) tempMap.get(Safety.ISEDIT);
			if (!StringUtil.isNullOrEmpty(areaCode)) {
				if (!areaCode.equals("0010")) {
					if (isEdit.substring(0, 1).equals("0")) {
						// 如果暂存保单返回的品牌型号不可编辑
						setHaveDataItemShow(R.id.tv_brandName, mMainView.findViewById(R.id.ll_brandName_input), (String) tempMap.get(Safety.BRANDNAME));
						etBrandName.setVisibility(View.GONE);
					} else {
						etBrandName.setText((CharSequence) tempMap.get(Safety.BRANDNAME));
					}
					String enrollDate = (String) tempMap.get(Safety.ENROLLDATE);
						if (!StringUtil.isNull(enrollDate)) {
						// 如果暂存保单返回的注册日期不可编辑
						if (isEdit.substring(3, 4).equals("0")) {
							setHaveDataItemShow(R.id.tv_enrollDate_show, mMainView.findViewById(R.id.ll_enrollDate_input), (String) tempMap.get(Safety.ENROLLDATE));
							tvEnrollDate.setVisibility(View.GONE);
						} else {
							tvEnrollDate.setText(enrollDate);
						}
					} else {
						tvEnrollDate.setText(SafetyDataCenter.getInstance().getSysTime());
					}
	
					if (!StringUtil.isNullOrEmpty(tempMap.get(Safety.FUELTYPE))) {
						// 如果暂存保单返回的燃油类型不可编辑
						if (isEdit.substring(4, 5).equals("0")) {
							setHaveDataItemShow(R.id.tv_fuelType, spFuelType, SafetyDataCenter.fuelTypeCN.get(SafetyDataCenter.fuelTypeCode.indexOf(tempMap.get(Safety.FUELTYPE))));
						} else {
							spFuelType.setSelection(SafetyDataCenter.fuelTypeCode.indexOf(tempMap.get(Safety.FUELTYPE)));
						}
					} else {
						spFuelType.setSelection(0);
					}
				}
				if (isEdit.substring(1, 2).equals("0")) {
					setHaveDataItemShow(R.id.tv_engineNo, mMainView.findViewById(R.id.ll_engineNo_input), (String) tempMap.get(Safety.ENGINENO));
					etEngineNo.setVisibility(View.GONE);
				} else {
					etEngineNo.setText((String) tempMap.get(Safety.ENGINENO));
				}
				if (isEdit.substring(2, 3).equals("0")) {
					setHaveDataItemShow(R.id.tv_frameNo, mMainView.findViewById(R.id.ll_frameNo_input), (String) tempMap.get(Safety.FRAMENO));
					etFrameNo.setVisibility(View.GONE);
				} else {
					etFrameNo.setText((String) tempMap.get(Safety.FRAMENO));
				}
			} else {
				tvEnrollDate.setText(SafetyDataCenter.getInstance().getSysTime());
			}
			exampleClick();
		} else {
			tvEnrollDate.setText(SafetyDataCenter.getInstance().getSysTime());
			exampleClick();
		}
	}
}
