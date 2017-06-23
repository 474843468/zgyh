package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
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

/** 保费试算结果页面 */
public class CalculationDetilActivity extends CarSafetyBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 主显示布局 */
	private View mMainView;
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.safety_msgfill_title);
		findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_carsafety_calculation_detil, null);
		initView();
		setStep2();
		addView(mMainView);
	}
	
	private void initView() {
		mLeftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCommercial())) {
					// 如果商业险套餐查询接口数据为空，说明是从交强险查询返回跳转过来，直接finish
				} else if (!StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation())
						&& SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCommercial().get(Safety.CALFLAG).equals("1")) {
					// 否则就是车辆可以投商业险，当报价方式为统一报价时，清空报价返回数据，如果为实时报价，不用清空 
					SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation().clear();
				}
				finish();
			}
		});
		
		Map<String, Object> map = SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCompulsory();
		Map<String, Object> calculationMap = SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation();
		if (StringUtil.isNullOrEmpty(
				SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCompulsory().get(Safety.BIZINSBEGINDATE))
				|| StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCommercial().get(Safety.AVAINSURLIST))) {
			mMainView.findViewById(R.id.ll_totalBizStandPremium).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_totalBizRealPremium).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_bizEndClaimNum).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_bizEndClaimTotalMoney).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_bizRebate).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_bizLastYearEndDate).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_bizInsBeginDate).setVisibility(View.GONE);
			mMainView.findViewById(R.id.ll_bizInsEndDate).setVisibility(View.GONE);
		} else {
			((TextView) mMainView.findViewById(R.id.tv_totalBizStandPremium)).setText(StringUtil.parseStringPattern((String) calculationMap.get(Safety.TOTALBIZSTANDPREMIUM), 2));
			((TextView) mMainView.findViewById(R.id.tv_totalBizRealPremium)).setText(StringUtil.parseStringPattern((String) calculationMap.get(Safety.TOTALBIZREALPREMIUM), 2));
			if (!StringUtil.isNullOrEmpty(map.get(Safety.BIZENDCLAIMNUM))) {
				((TextView) mMainView.findViewById(R.id.tv_bizEndClaimNum)).setText((String) map.get(Safety.BIZENDCLAIMNUM));
			} else {
				mMainView.findViewById(R.id.ll_bizEndClaimNum).setVisibility(View.GONE);
			}
			if (!StringUtil.isNullOrEmpty(map.get(Safety.BIZENDCLAIMTOTALMONEY))) {
				((TextView) mMainView.findViewById(R.id.tv_bizEndClaimTotalMoney)).setText(Html.fromHtml("<font color=\"#ba001d\">" + StringUtil.parseStringPattern((String) map.get(Safety.BIZENDCLAIMTOTALMONEY), 2) + "</font>"));
			} else {
				mMainView.findViewById(R.id.ll_bizEndClaimTotalMoney).setVisibility(View.GONE);
			}
			((TextView) mMainView.findViewById(R.id.tv_bizEndClaimTotalMoney)).setText(Html.fromHtml("<font color=\"#ba001d\">" + StringUtil.parseStringPattern((String) map.get(Safety.BIZENDCLAIMTOTALMONEY), 2) + "</font>"));
			((TextView) mMainView.findViewById(R.id.tv_bizRebate)).setText((String) calculationMap.get(Safety.BIZREBATE));
			if (!StringUtil.isNullOrEmpty(map.get(Safety.BIZLASTYEARENDDATE))) {
				((TextView) mMainView.findViewById(R.id.tv_bizLastYearEndDate)).setText((String) map.get(Safety.BIZLASTYEARENDDATE));
			} else {
				mMainView.findViewById(R.id.ll_bizLastYearEndDate).setVisibility(View.GONE);
			}
			((TextView) mMainView.findViewById(R.id.tv_bizInsBeginDate)).setText((String) map.get(Safety.BIZINSBEGINDATE));
			((TextView) mMainView.findViewById(R.id.tv_bizInsEndDate)).setText((String) map.get(Safety.BIZINSENDDATE));
		}
		
//		((TextView) mMainView.findViewById(R.id.tv_taxCurrent)).setText(StringUtil.parseStringPattern((String) map.get(Safety.TAXCURRENT), 2));
//		((TextView) mMainView.findViewById(R.id.tv_taxFormer)).setText(StringUtil.parseStringPattern((String) map.get(Safety.TAXFORMER), 2));
//		((TextView) mMainView.findViewById(R.id.tv_taxLatafee)).setText(StringUtil.parseStringPattern((String) map.get(Safety.TAXLATAFEE), 2));
//		((TextView) mMainView.findViewById(R.id.tv_totalTax)).setText(StringUtil.parseStringPattern((String) map.get(Safety.TOTALTAX), 2));
		((TextView) mMainView.findViewById(R.id.tv_jqxPremium)).setText(StringUtil.parseStringPattern((String) map.get(Safety.JQXPREMIUM), 2));
		((TextView) mMainView.findViewById(R.id.tv_totalTax1)).setText(StringUtil.parseStringPattern((String) map.get(Safety.TOTALTAX), 2));
		((TextView) mMainView.findViewById(R.id.tv_totalStandPremium)).setText(StringUtil.parseStringPattern((String) calculationMap.get(Safety.TOTALSTANDPREMIUM), 2));
		((TextView) mMainView.findViewById(R.id.tv_jqxPremium2)).setText(StringUtil.parseStringPattern((String) map.get(Safety.JQXPREMIUM), 2));
		((TextView) mMainView.findViewById(R.id.tv_totalTax2)).setText(StringUtil.parseStringPattern((String) map.get(Safety.TOTALTAX), 2));
		((TextView) mMainView.findViewById(R.id.tv_totalRealPremium2)).setText(StringUtil.parseStringPattern((String) calculationMap.get(Safety.TOTALREALPREMIUM), 2));
		if (!StringUtil.isNullOrEmpty(map.get(Safety.JQXLASTYEARENDDATE))) {
			((TextView) mMainView.findViewById(R.id.tv_jqxLastYearEndDate)).setText((String) map.get(Safety.JQXLASTYEARENDDATE));
		} else {
			mMainView.findViewById(R.id.ll_jqxLastYearEndDate).setVisibility(View.GONE);
		}
		((TextView) mMainView.findViewById(R.id.tv_jqxInsBeginDate)).setText((String) map.get(Safety.JQXINSBEGINDATE));
		((TextView) mMainView.findViewById(R.id.tv_jqxInsEndDate)).setText((String) map.get(Safety.JQXINSENDDATE));
		if (!StringUtil.isNullOrEmpty(map.get(Safety.JQXENDCLAIMNUM))) {
			((TextView) mMainView.findViewById(R.id.tv_jqxEndClaimNum)).setText((String) map.get(Safety.JQXENDCLAIMNUM));
		} else {
			mMainView.findViewById(R.id.ll_jqxEndClaimNum).setVisibility(View.GONE);
		}
		if (!StringUtil.isNullOrEmpty(map.get(Safety.JQXENDCLAIMTOTALMONEY))) {
			((TextView) mMainView.findViewById(R.id.tv_jqxEndClaimTotalMoney)).setText(Html.fromHtml("<font color=\"#ba001d\">" + StringUtil.parseStringPattern((String) map.get(Safety.JQXENDCLAIMTOTALMONEY), 2) + "</font>"));
		} else {
			mMainView.findViewById(R.id.ll_jqxEndClaimTotalMoney).setVisibility(View.GONE);
		}
		((TextView) mMainView.findViewById(R.id.tv_jqxRebate)).setText((String) map.get(Safety.JQXREBATE));
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_keyForjqxLastYearEndDate));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_keyForbizLastYearEndDate));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_keyForjqxEndClaimNum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_keyForjqxEndClaimTotalMoney));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_keyForbizEndClaimNum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_keyForbizEndClaimTotalMoney));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_keyForjqxRebate));
		
		double totalStandPremium = Double.parseDouble((String) calculationMap.get(Safety.TOTALSTANDPREMIUM));
		double totalRealPremium = Double.parseDouble((String) calculationMap.get(Safety.TOTALREALPREMIUM));
		((TextView) mMainView.findViewById(R.id.tv_youhuijia)).setText("优惠后，为您节省了" + StringUtil.parseStringPattern(String.valueOf((totalStandPremium - totalRealPremium)), 2) + "元。");
		Button btnNext = (Button) mMainView.findViewById(R.id.btnNext);
		Button btnNextBig = (Button) mMainView.findViewById(R.id.btnNext_big);
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
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCommercial())) {
				// 如果商业险套餐查询接口数据为空，说明是从交强险查询返回跳转过来，直接finish
				return true;
			} else if (!StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation())
					&& SafetyDataCenter.getInstance().getMapAutoInsuranceQueryCommercial().get(Safety.CALFLAG).equals("1")) {
				// 否则就是车辆可以投商业险，当报价方式为统一报价时，清空报价返回数据，如果为实时报价，不用清空 
				SafetyDataCenter.getInstance().getMapAutoInsuranceCalculation().clear();
				return true;
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	};

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
			if (SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.CONTINUEFLAG).equals("true")) {
				BaseHttpEngine.showProgressDialog();
				httpTools.requestHttp(Safety.METHOD_PSNSVRPSNINFOQUERY, "requestPsnSVRPsnInfoQueryCallBack", null, false);
			} else {
				Intent intent = new Intent(CalculationDetilActivity.this, OtherInfoInputActivity.class);
				startActivityForResult(intent, 4);
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
	
	/** 查询客户资料回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnSVRPsnInfoQueryCallBack(Object resultObj) {
		Map<String, Object> userInfo = this.getHttpTools().getResponseResult(resultObj);
		SafetyDataCenter.getInstance().setMapSVRPsnInfoQuery(userInfo);
		BaseHttpEngine.dissMissProgressDialog();

		Intent intent = new Intent(CalculationDetilActivity.this, OtherInfoInputActivity.class);
		startActivityForResult(intent, 4);
	}
}
