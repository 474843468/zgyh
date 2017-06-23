package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.Map;

import android.os.Bundle;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 车险——车辆补充信息页面
 * 
 * @author Zhi
 */
public class CarMoreInfoInputActivity extends CarMoreInfoInputBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		System.out.println("CarMoreInfoInputActivity");
	}
	
	@Override
	public void initView() {
		super.initView();
		btnSave.setOnClickListener(saveClickListener);
		btnCommitQuery.setOnClickListener(listener);
		initViewData();
	}

	/** 初始化控件显示数据 */
	public void initViewData() {
		// 取出车辆补充信息查询返回的数据
		Map<String, Object> map = SafetyDataCenter.getInstance().getMapAutoInsuranceQueryAutoDetail();
		
		if (!StringUtil.isNullOrEmpty(map)) {
			setHaveDataItemShow(R.id.tv_engineNo, mMainView.findViewById(R.id.ll_engineNo_input), (String) map.get(Safety.ENGINENO));
			setHaveDataItemShow(R.id.tv_frameNo, mMainView.findViewById(R.id.ll_frameNo_input), (String) map.get(Safety.FRAMENO));
			etBrandName.setVisibility(View.GONE);
			etEngineNo.setVisibility(View.GONE);
			etFrameNo.setVisibility(View.GONE);
			if (!SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.ZONENO).equals("0010")) {
				setHaveDataItemShow(R.id.tv_brandName, mMainView.findViewById(R.id.ll_brandName_input), (String) map.get(Safety.BRANDNAME));
				if (!StringUtil.isNullOrEmpty(map.get(Safety.ENROLLDATE))) {
					setHaveDataItemShow(R.id.tv_enrollDate_show, mMainView.findViewById(R.id.ll_enrollDate_input), (String) map.get(Safety.ENROLLDATE));
					tvEnrollDate.setVisibility(View.GONE);
				} else {
					tvEnrollDate.setText(SafetyDataCenter.getInstance().getSysTime());
				}
				if (!StringUtil.isNullOrEmpty(map.get(Safety.FUELTYPE))
						&&SafetyDataCenter.fuelTypeCode.indexOf(map.get(Safety.FUELTYPE))>=0) {
						setHaveDataItemShow(R.id.tv_fuelType, spFuelType, SafetyDataCenter.fuelTypeCN.get(SafetyDataCenter.fuelTypeCode.indexOf(map.get(Safety.FUELTYPE))));
				} else {
					spFuelType.setSelection(0);
				}
			}
		} else {
			tvEnrollDate.setText(SafetyDataCenter.getInstance().getSysTime());
			exampleClick();
		}
	}
	
//	
//	/** 提交数据校验 */
//	private boolean submitRegexp(boolean required) {
//		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//		
//		// 品牌型号
//		if (onlyRegular(required, etBrandName.getText().toString()) && (etBrandName.getVisibility() == View.VISIBLE)) {
//			RegexpBean buyerName = new RegexpBean(SafetyConstant.BRANDNAME, etBrandName.getText().toString(), SafetyConstant.BRAND_NAME);
//			lists.add(buyerName);
//		}
//		// 发动机号
//		if (onlyRegular(required, etEngineNo.getText().toString()) && (etEngineNo.getVisibility() == View.VISIBLE)) {
//			RegexpBean buyerName = new RegexpBean(SafetyConstant.ENGINENO, etEngineNo.getText().toString(), SafetyConstant.ENGINE_NO);
//			lists.add(buyerName);
//		}
//		// 车辆识别代码
//		if (onlyRegular(required, etFrameNo.getText().toString()) && (etFrameNo.getVisibility() == View.VISIBLE)) {
//			RegexpBean buyerName = new RegexpBean(SafetyConstant.FRAMENO, etFrameNo.getText().toString(), SafetyConstant.FRAME_NO);
//			lists.add(buyerName);
//		}
//		if (RegexpUtils.regexpDate(lists)) {
//			return true;
//		}
//		return false;
//	}
//		
//	
//	/** 只作正则校验  */
//	private boolean onlyRegular(Boolean required, String content){
//		if ((!required && !StringUtil.isNull(content)) || required) {
//			return true;
//		}
//		return false;
//	}
}
