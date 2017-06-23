package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.Map;

import android.os.Bundle;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.utils.StringUtil;

public class CarMoreInfoInputHoldActivity extends CarMoreInfoInputBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		System.out.println("CarMoreInfoInputHoldActivity");
	}
	
	@Override
	public void initView() {
		super.initView();
		btnSave.setVisibility(View.GONE);
		btnCommitQuery.setVisibility(View.GONE);
		btnCommitQueryBig.setVisibility(View.VISIBLE);
		btnCommitQueryBig.setOnClickListener(listener);
		initViewData();
	}
	
	@Override
	public void initViewData() {
		// 取出车辆补充信息查询返回的数据
		Map<String, Object> map = SafetyDataCenter.getInstance().getMapAutoInsuranceQueryAutoDetail();
		// 取出持有保单详情返回的数据
		Map<String, Object> holdProDetel = SafetyDataCenter.getInstance().getHoldProDetail();
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
		} else if (!StringUtil.isNullOrEmpty(holdProDetel)) {
			setHaveDataItemShow(R.id.tv_brandName, mMainView.findViewById(R.id.ll_brandName_input), (String) holdProDetel.get(Safety.VEHICLEMODEL));
			setHaveDataItemShow(R.id.tv_engineNo, mMainView.findViewById(R.id.ll_engineNo_input), (String) holdProDetel.get(Safety.ENGINENO));
			setHaveDataItemShow(R.id.tv_frameNo, mMainView.findViewById(R.id.ll_frameNo_input), (String) holdProDetel.get(Safety.FRAMENO));
			etBrandName.setVisibility(View.GONE);
			etEngineNo.setVisibility(View.GONE);
			etFrameNo.setVisibility(View.GONE);
			if (!SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.ZONENO).equals("0010")) {
				if (!StringUtil.isNullOrEmpty(holdProDetel.get(Safety.ENROLLDATE))) {
					setHaveDataItemShow(R.id.tv_enrollDate_show, mMainView.findViewById(R.id.ll_enrollDate_input), (String) holdProDetel.get(Safety.ENROLLDATE));
					tvEnrollDate.setVisibility(View.GONE);
				} else {
					tvEnrollDate.setText(SafetyDataCenter.getInstance().getSysTime());
				}
			}
		} else {
			tvEnrollDate.setText(SafetyDataCenter.getInstance().getSysTime());
			exampleClick();
		}
	}
}
