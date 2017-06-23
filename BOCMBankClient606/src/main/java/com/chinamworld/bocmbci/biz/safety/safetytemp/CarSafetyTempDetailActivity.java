package com.chinamworld.bocmbci.biz.safety.safetytemp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.SafetyProductListActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 车险暂存保单详情页面
 * 
 * @author Zhi
 */
public class CarSafetyTempDetailActivity extends SafetyBaseActivity implements OnClickListener{

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 主显示视图 */
	private View mMainView;
	/** 保险产品暂存单详情查询返回数据 */
	private Map<String, Object> map;
	/** 产品代码 */
	private TextView tvProductCode;
	/** 产品名称 */
	private TextView tvProductName;
	/** 保险公司 */
	private TextView tvCompany;
	/** 保险类型 */
	private TextView tvProductType;
	/** 暂存日期 */
	private TextView tvTempDate;
	/** 投保单名称 */
	private TextView tvTempName;
	/** 切换开关，标识用户点击的是哪个按钮 0-删除保单 1-继续投保 */
	private String trigger;
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		map = SafetyDataCenter.getInstance().getMapAutoInsurPolicyDetailQry();
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_carsafety_temp_detail, null);
		addView(mMainView);
		setTitle(R.string.safety_product_info_title2);
		initView();
	}
	
	/** 初始化视图 */
	private void initView() {
		tvProductCode = (TextView) mMainView.findViewById(R.id.tv_productCode);
		tvProductName = (TextView) mMainView.findViewById(R.id.tv_productName);
		tvCompany = (TextView) mMainView.findViewById(R.id.tv_company);
		tvProductType = (TextView) mMainView.findViewById(R.id.tv_productType);
		tvTempDate = (TextView) mMainView.findViewById(R.id.tv_tempDate);
		tvTempName = (TextView) mMainView.findViewById(R.id.tv_tempName);
		
		tvProductCode.setText((String) map.get(Safety.INSURSCODE));
		tvProductName.setText((String) map.get(Safety.RISKNAME));
		tvCompany.setText((String) map.get(Safety.INSURCOMPANY));
		tvProductType.setText(SafetyDataCenter.insuranceType.get(map.get(Safety.RISKTYPE)));
		tvTempDate.setText((String) map.get(Safety.RECEIVINGDATE));
		tvTempName.setText((String) map.get(Safety.POLICYNAME));
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvProductCode);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvProductName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvCompany);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvTempDate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvTempName);
		
		mMainView.findViewById(R.id.btnDelete).setOnClickListener(this);
		mMainView.findViewById(R.id.btnContinue).setOnClickListener(this);
	}
	
	private void showNotiDialog(){
		BaseDroidApp.getInstanse().showErrorDialog(
				CarSafetyTempDetailActivity.this.getString(R.string.safety_delete_tip),
				R.string.cancle, R.string.confirm,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (Integer.parseInt(v.getTag() + "")) {
						case CustomDialog.TAG_SURE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
							BaseHttpEngine.showProgressDialog();
							requestCommConversationId();
							break;
						case CustomDialog.TAG_CANCLE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
							break;
						}
					}
				});
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDelete:
			trigger = "0";
			showNotiDialog();
			break;

		case R.id.btnContinue:
			trigger = "1";
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
			
			Map<String, Object> holdProDetail = SafetyDataCenter.getInstance().getHoldProDetail();
			// 清空持有保单详情列表，避免持有保单与暂存单来回切换后进入投保流程发生错误
			if (!StringUtil.isNullOrEmpty(holdProDetail)) {
				holdProDetail.clear();
			}
			
			break;
		}
	}
	
	/** conversationId回调 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (trigger.equals("0")) {
			httpTools.requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API, "requestPSNGetTokenIdCallBack", null, true);
		} else {
			httpTools.requestHttp(Safety.METHOD_PSNAUTOINSURANCEQUERYALLOWAREA, "requestPsnAutoInsuranceQueryAllowAreaCallBack", null, true);
		}
	}
	
	/** 查询开通车险省份代码回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnAutoInsuranceQueryAllowAreaCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) this.getHttpTools().getResponseResult(resultObj);
		List<Map<String, String>> list = (List<Map<String, String>>) resultMap.get(Safety.OPENAREALIST);
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
		String licenseNo = (String) map.get(Safety.LICENSENO);
		if (StringUtil.isNull(licenseNo)) {
			// 如果车牌号为空，说明暂存的时候是未上牌，直接跳转
			Intent intent = new Intent(CarSafetyTempDetailActivity.this, SafetyProductListActivity.class)
			.putExtra(JUMPFLAG, "save");
			startActivity(intent);
		} else {
			String cityCode = ((String) map.get(Safety.CITYCODE));
			// 如果返回的城市码中有简称表达的地区，说明该地区支持车险投保
			if (!StringUtil.isNull(cityCode) && areaCodeList.contains(cityCode.substring(0, 2) + "0000")) {
				Intent intent = new Intent(CarSafetyTempDetailActivity.this, SafetyProductListActivity.class)
				.putExtra(JUMPFLAG, "save");
				startActivity(intent);
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.safety_carsafety_nonsupportTip));
				return;
			}
		}
		finish();
	}
	
	/** tokenId回调 */
	@SuppressWarnings("unchecked")
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		String tokenId = (String) this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog(); return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Safety.POLICYID, map.get(Safety.POLICYID));
		params.put(Comm.TOKEN_REQ, tokenId);
		
		Map<String, Object> logInfo = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		params.put(Safety.IDENTITYTYPE, SafetyDataCenter.IDENTITYTYPE_credType.get(logInfo.get(Comm.IDENTITYTYPE)));
		params.put(Safety.IDENTITYNUMBER, logInfo.get(Comm.IDENTITYNUMBER));
		
		httpTools.requestHttp(Safety.PSNAUTOINSURPOLICYDEL, "requestPsnAutoInsurPolicyDelCallBack", params, true);
	}
	
	/** 车险暂存单删除回调 */
	public void requestPsnAutoInsurPolicyDelCallBack(Object resultObj) {
		CustomDialog.toastShow(this, this.getString(R.string.safety_delete_success));
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		startActivity(new Intent(this, SafetyTempProductListActivity.class));
	}
}
