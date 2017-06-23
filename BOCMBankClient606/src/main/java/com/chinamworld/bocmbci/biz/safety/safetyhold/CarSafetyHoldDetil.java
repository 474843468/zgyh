package com.chinamworld.bocmbci.biz.safety.safetyhold;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.SafetyProductListActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 车险持有保险查询详情界面
 * 
 * @author Zhi
 */
public class CarSafetyHoldDetil extends SafetyBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 主显示视图 */
	private View mMainView;
	/** 获取保险详情数据 */
	Map<String, Object> holdProDetail = SafetyDataCenter.getInstance().getHoldProDetail();
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_hold_carsafety_detail, null);
		initView();
		addView(mMainView);
		setTitle(getString(R.string.safety_hold_pro_detail_second_title));
	}
	
	@SuppressWarnings("unchecked")
	private void initView() {
		holdProDetail = SafetyDataCenter.getInstance().getHoldProDetail();
		List<Map<String, Object>> ownInsurList = (List<Map<String, Object>>) holdProDetail.get(Safety.OWNINSURLIST);
		showSafetyList(ownInsurList);
		((TextView) mMainView.findViewById(R.id.tv_insuCode)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.INSUCODE)));
		((TextView) mMainView.findViewById(R.id.tv_riskName)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.RISKNAME)));
		((TextView) mMainView.findViewById(R.id.tv_riskType)).setText(StringUtil.valueOf1(StringUtil.valueOf1(SafetyDataCenter.insuranceType.get((String) holdProDetail.get(Safety.RISKTYPE)))));
		((TextView) mMainView.findViewById(R.id.tv_company)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.SAFETY_HOLD_INSU_NAME)));
		((TextView) mMainView.findViewById(R.id.tv_policyNo)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.SAFETY_HOLD_POLICY_NO)));
		((TextView) mMainView.findViewById(R.id.tv_channel)).setText(StringUtil.valueOf1(SafetyDataCenter.channelFlag.get(holdProDetail.get(Safety.SAFETY_HOLD_CHANNEL))));
		((TextView) mMainView.findViewById(R.id.tv_applDate)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.SAFETY_HOLD_APPL_DATE)));
		((TextView) mMainView.findViewById(R.id.tv_currency)).setText(StringUtil.valueOf1(StringUtil.valueOf1(LocalData.Currency.get((String) holdProDetail.get(Safety.SAFETY_HOLD_CURRENCY)))));
		((TextView) mMainView.findViewById(R.id.tv_polEffDate)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.POLEFFDATE)));
		((TextView) mMainView.findViewById(R.id.tv_riskPrem)).setText(StringUtil.valueOf1(StringUtil.parseStringPattern((String) holdProDetail.get(Safety.SAFETY_HOLD_RISK_PREM), 2)));
		((TextView) mMainView.findViewById(R.id.tv_polEndDate)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.SAFETY_HOLD_POL_END_DATE)));
		
		if (StringUtil.isNullOrEmpty((String) holdProDetail.get(Safety.LICENSENO))) {
			((TextView) mMainView.findViewById(R.id.tv_licenseNo)).setText("新车无车牌号");
		} else {
			((TextView) mMainView.findViewById(R.id.tv_licenseNo)).setText((String) holdProDetail.get(Safety.LICENSENO));
		}
		
		((TextView) mMainView.findViewById(R.id.tv_vehicleModel)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.VEHICLEMODEL)));
		((TextView) mMainView.findViewById(R.id.tv_frameNo)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.FRAMENO)));
		((TextView) mMainView.findViewById(R.id.tv_engineNo)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.ENGINENO)));
		((TextView) mMainView.findViewById(R.id.tv_enrollDate)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.ENROLLDATE)));
		
		((TextView) mMainView.findViewById(R.id.tv_carOwnerName)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.CAROWNERNAME)));
		((TextView) mMainView.findViewById(R.id.tv_carOwnerIdType)).setText(StringUtil.valueOf1(SafetyDataCenter.credType.get((String) holdProDetail.get(Safety.CAROWNERIDTYPE))));
		((TextView) mMainView.findViewById(R.id.tv_carOwnerIdNo)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.CAROWNERIDNO)));
		
		((TextView) mMainView.findViewById(R.id.tv_applName)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.APPL_NAME)));
		((TextView) mMainView.findViewById(R.id.tv_applIdType)).setText(StringUtil.valueOf1(SafetyDataCenter.credType.get(holdProDetail.get(Safety.APPL_IDTYPE))));
		((TextView) mMainView.findViewById(R.id.tv_applEmail)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.APPL_EMAIL)));
		((TextView) mMainView.findViewById(R.id.tv_applIdNo)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.APPL_IDNO)));
		
		((TextView) mMainView.findViewById(R.id.tv_benName)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.BEN_NAME)));
		((TextView) mMainView.findViewById(R.id.tv_benIdType)).setText(StringUtil.valueOf1(SafetyDataCenter.credType.get(holdProDetail.get(Safety.BEN_IDTYPE))));
		((TextView) mMainView.findViewById(R.id.tv_benIdNo)).setText(StringUtil.valueOf1((String) holdProDetail.get(Safety.BEN_IDNUM)));
		
//		((TextView) mMainView.findViewById(R.id.tv_taxCurrent)).setText(StringUtil.valueOf1(StringUtil.parseStringPattern((String) map.get(Safety.TAXCURRENT), 2)));
//		((TextView) mMainView.findViewById(R.id.tv_taxFormer)).setText(StringUtil.valueOf1(StringUtil.parseStringPattern((String) map.get(Safety.TAXFORMER), 2)));
//		((TextView) mMainView.findViewById(R.id.tv_taxLataFee)).setText(StringUtil.valueOf1(StringUtil.parseStringPattern((String) map.get(Safety.TAXLATAFEE), 2)));
//		((TextView) mMainView.findViewById(R.id.tv_taxTotal)).setText(StringUtil.valueOf1(StringUtil.parseStringPattern((String) map.get(Safety.TAXTOTAL), 2)));
		((TextView) mMainView.findViewById(R.id.tv_accInfo)).setText(StringUtil.valueOf1(StringUtil.getForSixForString((String) holdProDetail.get(Safety.ACCINFO))));
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyHoldDetil.this, ((TextView) mMainView.findViewById(R.id.tv_riskName)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyHoldDetil.this, ((TextView) mMainView.findViewById(R.id.tv_policyNo)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyHoldDetil.this, ((TextView) mMainView.findViewById(R.id.tv_vehicleModel)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyHoldDetil.this, ((TextView) mMainView.findViewById(R.id.tv_benIdNo)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyHoldDetil.this, ((TextView) mMainView.findViewById(R.id.tv_carOwnerIdNo)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyHoldDetil.this, ((TextView) mMainView.findViewById(R.id.tv_applIdNo)));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyHoldDetil.this, ((TextView) mMainView.findViewById(R.id.tv_applEmail)));
		
		if (holdProDetail.get(Safety.CONTINUEFLAG).equals("1")) {
			// 支持续保
			mMainView.findViewById(R.id.btnContinue).setVisibility(View.VISIBLE);
			mMainView.findViewById(R.id.btnContinue).setOnClickListener(listener);
		} else if(holdProDetail.get(Safety.CONTINUEFLAG).equals("0")) {
			// 不支持续保
			mMainView.findViewById(R.id.btnContinue).setVisibility(View.GONE);
		}
	}
	/** 添加险别列表数据 */
	private void showSafetyList(final List<Map<String, Object>> ownInsurList) {
		// 为提高页面加载速度，使用多线程来加载
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if (ownInsurList.size() == 0) {
					return;
				}
				
				/** 标识是否有不计免赔合计 */
				boolean haveBj = false;
				for (int i = 0; i < ownInsurList.size(); i++) {
					View bizItem = LayoutInflater.from(CarSafetyHoldDetil.this).inflate(R.layout.safety_carsafety_carbiz_tvitem, null);
					Map<String, Object> bizMap = ownInsurList.get(i);
					((TextView) bizItem.findViewById(R.id.tv_insName)).setText((String) bizMap.get(Safety.INSCODE));
					
					if (((String) bizMap.get(Safety.INSCODE)).equals("玻璃单独破碎险")) {
						if (bizMap.get(Safety.AMOUNTINFO).equals("0")) {
							((TextView) bizItem.findViewById(R.id.tv_Amount)).setText("国产");
						} else {
							((TextView) bizItem.findViewById(R.id.tv_Amount)).setText("进口");
						}
					} else if (((String) bizMap.get(Safety.INSCODE)).equals("不计免赔合计")) {
						((TextView) mMainView.findViewById(R.id.tv_bj)).setText(StringUtil.parseStringPattern((String) bizMap.get(Safety.PREMIUM), 2));
						haveBj = true;
						continue;
					} else {
						if (((String) bizMap.get(Safety.INSCODE)).contains("交通事故责任强制")) {
							((TextView) bizItem.findViewById(R.id.tv_keyForAmount)).setText("赔偿限额/保额：");
							bizItem.findViewById(R.id.ll_isMp).setVisibility(View.GONE);
						}
						((TextView) bizItem.findViewById(R.id.tv_Amount)).setText(StringUtil.parseStringPattern((String) bizMap.get(Safety.AMOUNTINFO), 2));
					}
					if (StringUtil.isNullOrEmpty(bizMap.get(Safety.ISMP))) {
						((TextView) bizItem.findViewById(R.id.tv_isMp)).setText("-");
					} else if (bizMap.get(Safety.ISMP).equals("1")) {
						((TextView) bizItem.findViewById(R.id.tv_isMp)).setText("是");
					} else {
						((TextView) bizItem.findViewById(R.id.tv_isMp)).setText("否");
					}
					
					((TextView) bizItem.findViewById(R.id.tv_Premium)).setText(StringUtil.parseStringPattern((String) bizMap.get(Safety.PREMIUM), 2));
					
					PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyHoldDetil.this, ((TextView) bizItem.findViewById(R.id.tv_insName)));
					PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyHoldDetil.this, ((TextView) bizItem.findViewById(R.id.tv_Amount)));
					PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyHoldDetil.this, ((TextView) bizItem.findViewById(R.id.tv_Premium)));
					PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyHoldDetil.this, ((TextView) bizItem.findViewById(R.id.tv_keyForAmount)));
					PopupWindowUtils.getInstance().setOnShowAllTextListener(CarSafetyHoldDetil.this, ((TextView) bizItem.findViewById(R.id.tv_keyForisMp)));
					
					((LinearLayout) mMainView.findViewById(R.id.ll_safetyItemInfo)).addView(bizItem);
				}
				if (!haveBj) {
					// 如果没有不计免赔合计险别，要把该字段隐藏
					mMainView.findViewById(R.id.ll_bj).setVisibility(View.GONE);
				}
			}
		}).start();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (!StringUtil.isNullOrEmpty(holdProDetail.get(Safety.LICENSENO))) {
				requestCommConversationId();
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.safety_carsafety_nonsupportTip));
				return;
			}
			
			// 清空暂存保单详情列表，避免持有保单与暂存单来回切换后进入投保流程发生错误
			Map<String, Object> policyDetail = SafetyDataCenter.getInstance().getMapAutoInsurPolicyDetailQry();
			if (!StringUtil.isNullOrEmpty(policyDetail)) {
				policyDetail.clear();
			}
		}
	};
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 已登录会话id返回 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCEQUERYALLOWAREA, "requestPsnAutoInsuranceQueryAllowAreaCallBack", null, true);
	}
	
	/** 查询开通车险省份代码回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsuranceQueryAllowAreaCallBack(Object resultObj) {
		Map<String, Object> map = (Map<String, Object>) this.getHttpTools().getResponseResult(resultObj);
		List<Map<String, String>> list = (List<Map<String, String>>) map.get(Safety.OPENAREALIST);
		/** 开通车险地区城市码列表，接口返回的城市码只有省份城市码，6位，末4位全0 */
		List<String> areaCodeList = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				// 只需要开通车险省份的代码的字段，其他字段暂不处理
				String areaCode = list.get(i).get(Safety.AREACODE);
				if (StringUtil.isNull(areaCode) || areaCode.equals("")) {
					continue;
				}
				areaCodeList.add(areaCode);
			}
		}
		if (StringUtil.isNullOrEmpty(areaCodeList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog("后台数据返回异常");
			return;
		}

		SafetyDataCenter.getInstance().setListAutoInsuranceQueryAllowArea(areaCodeList);
		
		String licenseNoZone = ((String) holdProDetail.get(Safety.LICENSENO)).substring(0, 1);
		String cityCode = SafetyDataCenter.mapCNS_CityCode.get(licenseNoZone);
		// 如果车牌号能取到简称且返回的城市码中有简称表达的地区，说明该地区支持车险投保
		if (!StringUtil.isNullOrEmpty(cityCode) && areaCodeList.contains(cityCode)) {
			startActivity(new Intent(CarSafetyHoldDetil.this, SafetyProductListActivity.class)
			.putExtra(Safety.CONTINUEFLAG, "true"));
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.safety_carsafety_nonsupportTip));
			return;
		}
	}
}
