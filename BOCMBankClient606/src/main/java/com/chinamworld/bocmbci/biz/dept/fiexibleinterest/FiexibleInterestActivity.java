package com.chinamworld.bocmbci.biz.dept.fiexibleinterest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
;

public class FiexibleInterestActivity extends DeptBaseActivity implements
		OnCheckedChangeListener, OnClickListener {

	private View view;
	private RadioGroup radioGroup;
	private RadioButton bbk_btn, enrichment_btn;
	/** 电子卡信息 */
	private Map<String, Object> ecardMap;
	/** 步步高是否签约 */
	private boolean bbkissign = false;
	/** 聚财通是否签约 */
	private boolean enrichmentissign = false;
	/** 签约类型 */
	private int interestProductType = 0;
	/** 签约日期 */
	private String signedDate;
	/** 签约渠道 */
	private String signedChannel;
	/** 签约状态 */
	private String signedStatus;

	/** 电子卡信息 */
	private TextView crcd_type_value, crcd_account_num, crcd_account_nickname;

	/** 产品信息 */
	private LabelTextView ltv_interestproducttype, ltv_beginAmount_begintime,
			ltv_Limit_date, ltv_time/*, ltv_markettype*/;

	/* private LinearLayout ll_interestdetai_sign; */
	private LabelTextView ltv_signedDate, ltv_signedChannel, ltv_status;
	private Button btn_infodetail,btn_sign;
	private TextView tv_detail,Limit_date_info, tv_info;
	/** 完成界面返回 */
	private boolean backfalg = false;
	private boolean iscancel = false;

	private RelativeLayout rl_unsign;
	private LinearLayout ll_issign;
	private TextView tv_infodetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.dept_fiexibleinterest_tittle));
		ecardMap = BaseDroidApp.getInstanse().getEcardMap();
		// initViews();
		BiiHttpEngine.showProgressDialog();
		interestProductType = 15;
		requestPsnInterestSignedList(interestProductType);
	}

	private void requestPsnInterestSignedList(int interestProductType) {
		// interestProductType 15:中银步步高 16：中银聚财通
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.flexibleInterest_PsnInterestSignedList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Comm.ACCOUNT_ID, ecardMap.get(Comm.ACCOUNT_ID));
		map.put(Dept.flexibleInterest_interestProductType,
				String.valueOf(interestProductType));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInterestSignedListCallback");

	}

	public void requestPsnInterestSignedListCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		List<Map<String, Object>> signedDetailList = (List<Map<String, Object>>) resultMap
				.get("signedDetailList");
		String status = null;
		if (!StringUtil.isNullOrEmpty(signedDetailList)) {
			status = (String) signedDetailList.get(0).get(
					Dept.flexibleInterest_Status);
		}

		if (interestProductType == 15) {
			if ("E".equals(status)) {
				signedDate = (String) signedDetailList.get(0).get(
						Dept.flexibleInterest_signedDate);
				signedChannel = (String) signedDetailList.get(0).get(
						Dept.flexibleInterest_signedChannel);
				signedStatus = (String) signedDetailList.get(0).get(
						Dept.flexibleInterest_Status);
				bbkissign = true;
			}
			if (bbkissign) {
				interestProductType = 15;
				requestPsnInterestSignedDetail(interestProductType);
			} else {
				interestProductType = 16;
				requestPsnInterestSignedList(interestProductType);
			}
		} else if (interestProductType == 16) {
			if ("E".equals(status)) {
				enrichmentissign = true;
				signedDate = (String) signedDetailList.get(0).get(
						Dept.flexibleInterest_signedDate);
				signedChannel = (String) signedDetailList.get(0).get(
						Dept.flexibleInterest_signedChannel);
				signedStatus = (String) signedDetailList.get(0).get(
						Dept.flexibleInterest_Status);
			}
			if (enrichmentissign) {
				interestProductType = 16;
				requestPsnInterestSignedDetail(interestProductType);
			} else {
				interestProductType = 15;
				requestPsnInterestProductInfo(interestProductType);
//				BiiHttpEngine.dissMissProgressDialog();
//				initViews();
			}
		}

	}

	private void requestPsnInterestSignedDetail(int interestProductType) {
		// interestProductType 15:中银步步高 16：中银聚财通
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.flexibleInterest_PsnInterestSignedDetail);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Comm.ACCOUNT_ID, ecardMap.get(Comm.ACCOUNT_ID));
		map.put(Dept.flexibleInterest_interestProductType,
				String.valueOf(interestProductType));
		map.put("BBKSignedDate", signedDate);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInterestSignedDetailCallback");

	}

	public void requestPsnInterestSignedDetailCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (backfalg) {
			BiiHttpEngine.dissMissProgressDialog();
			if (interestProductType == 15) {
				// 存储数据
				DeptDataCenter.getInstance().setBbkMap(resultMap);
				bbkissign = true;
				bbk_btn.performClick();
				initData(DeptDataCenter.getInstance().getBbkMap(), bbkissign);
			} else if (interestProductType == 16) {
				DeptDataCenter.getInstance().setEnrichmentMap(resultMap);
				enrichmentissign = true;
				enrichment_btn.performClick();
				initData(DeptDataCenter.getInstance().getEnrichmentMap(),
						enrichmentissign);
			}

		} else {
			if (interestProductType == 15) {
				// 存储数据
				DeptDataCenter.getInstance().setBbkMap(resultMap);
				interestProductType = 16;
				requestPsnInterestProductInfo(interestProductType);

			} else if (interestProductType == 16) {
				DeptDataCenter.getInstance().setEnrichmentMap(resultMap);
				interestProductType = 15;
				requestPsnInterestProductInfo(interestProductType);
			}
//			BiiHttpEngine.dissMissProgressDialog();
//			initViews();
		}

	}
//
	private void requestPsnInterestProductInfo(int interestProductType) {

		// interestProductType 15:中银步步高 16：中银聚财通
		// 签约产品编号步步高是04，聚财通是01
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.flexibleInterest_PsnInterestProductInfo);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Comm.ACCOUNT_ID, ecardMap.get(Comm.ACCOUNT_ID));
		map.put(Dept.flexibleInterest_interestProductType,
				String.valueOf(interestProductType));
		if (interestProductType == 15) {
			map.put(Dept.flexibleInterest_interestNo, "04");
		} else if (interestProductType == 16) {
			map.put(Dept.flexibleInterest_interestNo, "01");
		}

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInterestProductInfoCallback");

	}

	public void requestPsnInterestProductInfoCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (backfalg) {

			if (interestProductType == 15) {
				// 存储数据
				DeptDataCenter.getInstance().setBbkMap(resultMap);
				bbkissign = false;

			} else if (interestProductType == 16) {

				DeptDataCenter.getInstance().setEnrichmentMap(resultMap);
				enrichmentissign = false;
			}

			bbk_btn.performClick();
			initData(DeptDataCenter.getInstance().getBbkMap(), bbkissign);
		} else {
			if (interestProductType == 15) {
				// 存储数据
				DeptDataCenter.getInstance().setBbkMap(resultMap);
				if (!enrichmentissign) {
					interestProductType = 16;
					BiiHttpEngine.showProgressDialog();
					requestPsnInterestProductInfo(interestProductType);
				} else {
					initViews();
				}

			} else if (interestProductType == 16) {

				DeptDataCenter.getInstance().setEnrichmentMap(resultMap);
				initViews();
			}
		}

	}

	/**
	 * 初始化布局
	 */
	private void initViews() {

		view = addView(R.layout.dept_fiexibleinterest_layout);
		view.findViewById(R.id.top_ll).setVisibility(View.VISIBLE);

		tv_info = (TextView) view.findViewById(R.id.tv_info);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(FiexibleInterestActivity.this,
				tv_info);
		Limit_date_info=(TextView) view.findViewById(R.id.Limit_date_info);
		crcd_type_value = (TextView) view.findViewById(R.id.crcd_type_value);
		crcd_account_num = (TextView) view.findViewById(R.id.crcd_account_num);
		crcd_account_nickname = (TextView) view
				.findViewById(R.id.crcd_account_nickname);
		
		crcd_account_num.setText(StringUtil
				.getForSixForString((String) ecardMap.get(Comm.ACCOUNTNUMBER)));
		String accountCatalog=null;
		if(ecardMap.containsKey("accountCatalog")){
			accountCatalog=(String) ecardMap.get("accountCatalog");

		}
		if(StringUtil.isNullOrEmpty(accountCatalog)==false){

			if("2".equals(accountCatalog)||"3".equals(accountCatalog)){
				crcd_type_value.setText("中银E财账户 ");
			}else{
				crcd_type_value.setText(LocalData.AccountType.get((String) ecardMap.get(Comm.ACCOUNT_TYPE)));
			}
		}else{
			crcd_type_value.setText(LocalData.AccountType.get((String) ecardMap.get(Comm.ACCOUNT_TYPE)));
		}
//		String cardNo = (String) ecardMap.get(Comm.ACCOUNTNUMBER);
//		cardNo = cardNo.substring(0, 4);
//		if("6216".equals(cardNo)){
//
//			crcd_type_value.setText("中银E财账户 ");
//		}else{
//			crcd_type_value.setText(LocalData.AccountType.get((String) ecardMap
//					.get(Comm.ACCOUNT_TYPE)));
//		}
		crcd_account_nickname
		.setText((String) ecardMap.get(Comm.NICKNAME));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				crcd_type_value);

		radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(this);
		bbk_btn = (RadioButton) view.findViewById(R.id.bbk_btn);
		enrichment_btn = (RadioButton) view.findViewById(R.id.enrichment_btn);

		ltv_interestproducttype = (LabelTextView) view
				.findViewById(R.id.ltv_interestproducttype);
		ltv_beginAmount_begintime = (LabelTextView) view
				.findViewById(R.id.ltv_beginAmount_begintime);
		ltv_Limit_date = (LabelTextView) view.findViewById(R.id.ltv_Limit_date);
		ltv_time = (LabelTextView) view.findViewById(R.id.ltv_time);
		ltv_signedDate = (LabelTextView) view.findViewById(R.id.ltv_signedDate);
		ltv_signedChannel = (LabelTextView) view
				.findViewById(R.id.ltv_signedChannel);
		ltv_status = (LabelTextView) view.findViewById(R.id.ltv_status);
		tv_detail = (TextView) view.findViewById(R.id.tv_detail);
		tv_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tv_detail.setOnClickListener(this);
		btn_infodetail=(Button) view.findViewById(R.id.btn_infodetail);
		btn_infodetail.setOnClickListener(this);
		btn_sign = (Button) view.findViewById(R.id.btn_sign);
		btn_sign.setOnClickListener(this);

		rl_unsign = (RelativeLayout) view.findViewById(R.id.rl_unsign);
		ll_issign = (LinearLayout) view.findViewById(R.id.ll_issign);
		tv_infodetail = (TextView) view.findViewById(R.id.tv_infodetail);

		if (enrichmentissign) {
			enrichment_btn.performClick();
		} else {
			bbk_btn.performClick();
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.enrichment_btn:
			initData(DeptDataCenter.getInstance().getEnrichmentMap(),
					enrichmentissign);
			break;
		case R.id.bbk_btn:
			initData(DeptDataCenter.getInstance().getBbkMap(), bbkissign);
			break;

		default:
			break;
		}

	}

	

	
	@SuppressLint("ResourceAsColor")
	private void initData(Map<String, Object> map, boolean issign) {

		if (bbk_btn.isChecked()) {
			if (issign) {
				displayData(map);
				ll_issign.setVisibility(View.VISIBLE);
				rl_unsign.setVisibility(View.GONE);

			} else {
				
				if(bbkissign||enrichmentissign){
					btn_sign.setVisibility(View.GONE);	
					tv_info.setVisibility(View.INVISIBLE);
				}else{
					btn_sign.setText(getResources().getString(
							R.string.dept_fiexibleinterest_sign));	
					tv_info.setVisibility(View.VISIBLE);
					tv_info.setText("请选择签约产品");
					tv_info.setTextColor(getResources().getColor(R.color.black));
				}
				ll_issign.setVisibility(View.GONE);
				rl_unsign.setVisibility(View.VISIBLE);
				tv_infodetail.setText(getResources().getString(R.string.dept_fiexibleinterest_bbk_info));
				
			}

		} else if (enrichment_btn.isChecked()) {
			if (issign) {
				displayData(map);
				ll_issign.setVisibility(View.VISIBLE);
				rl_unsign.setVisibility(View.GONE);
			} else {
				if(bbkissign||enrichmentissign){
					btn_sign.setVisibility(View.GONE);	
					tv_info.setVisibility(View.INVISIBLE);
				}else{
					btn_sign.setText(getResources().getString(
							R.string.dept_fiexibleinterest_sign));	
					tv_info.setVisibility(View.VISIBLE);
					tv_info.setText("请选择签约产品");
					tv_info.setTextColor(getResources().getColor(R.color.black));
				}
				ll_issign.setVisibility(View.GONE);
				rl_unsign.setVisibility(View.VISIBLE);
				tv_infodetail.setText(getResources().getString(R.string.dept_fiexibleinterest_enrichment_info));
			}
		}

	}

	private void displayData(Map<String, Object> map) {
//		ltv_time.setValueText((String) map.get(Dept.stepByStep_agreementLimit)
//				+ (String) map.get(Dept.stepByStep_agreementLimitUnit));
		/*String marketType = (String) map.get(Dept.flexibleInterest_marketType);*/
		if("Y".equals( map.get(Dept.stepByStep_agreementLimitUnit))){
			ltv_time.setValueText((String) map.get(Dept.stepByStep_agreementLimit)+"年");	
		}else{
			if("M".equals( map.get(Dept.stepByStep_agreementLimitUnit))){
				ltv_time.setValueText((String) map.get(Dept.stepByStep_agreementLimit)+"个月");	
			}else{
				ltv_time.setValueText("12个月");
			}
		}
		
		if (bbk_btn.isChecked()) {
			ltv_interestproducttype.setValueText(getString(R.string.dept_fiexibleinterest_bbk));
			List<Map<String, Object>> interestDetail = (List<Map<String, Object>>) map
					.get("interestDetail");
			// 步步高
			Limit_date_info.setText(getResources().getString(R.string.dept_fiexibleinterest_cancel_bbk));
//			ltv_Limit_date.setLabelText(getResources().getString(
//					R.string.dept_fiexibleinterest_Limit_date));
//			ltv_Limit_date.setValueText((String) interestDetail.get(0).get(
//					Dept.stepByStep_gradeLimit)
//					+ "天"/* (String)map.get(Dept.flexibleInterest_gradeType) */);
			ltv_Limit_date.setLabelText(getResources().getString(
					R.string.dept_fiexibleinterest_Limit_date_cancel));
			if(!StringUtil.isNullOrEmpty(map
					.get(Dept.flexibleInterest_beginAmountDays))){
				ltv_Limit_date.setValueText((String) map
						.get(Dept.flexibleInterest_beginAmountDays)+"天");
			}else{
				ltv_Limit_date.setValueText("-天");	
			}
			ltv_beginAmount_begintime.setLabelText(getResources().getString(
					R.string.dept_fiexibleinterest_beginAmount));
			ltv_beginAmount_begintime
					.setValueText("人民币5000.00元");
		} else {
			ltv_interestproducttype.setValueText(getString(R.string.dept_fiexibleinterest_enrichment));
			List<Map<String, Object>> interestDetail = (List<Map<String, Object>>) map
					.get("interestDetail");
			// 聚财通
			Limit_date_info.setText(getResources().getString(R.string.dept_fiexibleinterest_cancel_enrichment));
			ltv_Limit_date.setLabelText(getResources().getString(
					R.string.dept_fiexibleinterest_Limit_account_cancel));
			if(!StringUtil.isNullOrEmpty(map.get("beginTotalAmount"))){
				ltv_Limit_date.setValueText(StringUtil.parseStringPattern((String)map.get("beginTotalAmount"),2) +"元");
					
			}else{
				ltv_Limit_date.setValueText("-元");
					
			}
			//			ltv_Limit_date.setLabelText(getResources().getString(
//					R.string.dept_fiexibleinterest_Limit_account));
//			ltv_Limit_date.setValueText(StringUtil.parseStringPattern(
//					(String) interestDetail.get(0).get(
//							Dept.polyCaiTong_gradeAmount), 0)
//					+ "元"/* (String)map.get(Dept.polyCaiTong_beginTimeType) */);
			ltv_beginAmount_begintime.setLabelText(getResources().getString(
					R.string.dept_fiexibleinterest_begintime));
			ltv_beginAmount_begintime.setValueText("90天");
		}
		
			tv_info.setVisibility(View.VISIBLE);
			tv_info.setText("中银步步高与中银聚财通产品互斥，只能签约其中一种产品");
			tv_info.setTextColor(getResources().getColor(R.color.red));
			ltv_signedDate.setValueText(DateUtils.DateFormatter(signedDate));
			ltv_signedChannel.setValueText(LocalData.signedChannel
					.get(signedChannel));
			ltv_status.setValueText(LocalData.Status.get(signedStatus));
			btn_sign.setVisibility(View.VISIBLE);
			btn_sign.setText(getResources().getString(
					R.string.dept_fiexibleinterest_cancel));
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_infodetail){
			Intent intent = new Intent(FiexibleInterestActivity.this, FiexibleInterestProductActivity.class);
			if (bbk_btn.isChecked()) {
				intent.putExtra(Dept.flexibleInterest_interestProductType, 15);
			}else{
				intent.putExtra(Dept.flexibleInterest_interestProductType, 16);
			}
			startActivity(intent);
		}
		if (v.getId() == R.id.btn_sign) {
			Intent intent = new Intent();
			if (bbk_btn.isChecked()) {
				// 步步高
				if (bbkissign) {
					intent.setClass(FiexibleInterestActivity.this,
							FiexibleInterestConfirmActivity.class);
					intent.putExtra(Dept.IS_CHACEL, bbkissign);
				} else {
					intent.setClass(FiexibleInterestActivity.this,
							FiexibleInterestAgreeActivity.class);

				}

				intent.putExtra(Dept.flexibleInterest_interestProductType, 15);

			} else {
				// 聚财通
				if (enrichmentissign) {
					intent.setClass(FiexibleInterestActivity.this,
							FiexibleInterestConfirmActivity.class);
					intent.putExtra(Dept.IS_CHACEL, enrichmentissign);
				} else {
					intent.setClass(FiexibleInterestActivity.this,
							FiexibleInterestAgreeActivity.class);

				}
				intent.putExtra(Dept.flexibleInterest_interestProductType, 16);

			}
			startActivityForResult(intent, 5);
		}
		if (v.getId() == R.id.tv_detail) {

			Intent intent = new Intent(FiexibleInterestActivity.this,
					InterestDetailActivity.class);
			if (bbk_btn.isChecked()) {
				// 步步高
				intent.putExtra(Dept.flexibleInterest_interestProductType, 15);
			} else {
				// 聚财通
				intent.putExtra(Dept.flexibleInterest_interestProductType, 16);

			}
			startActivity(intent);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case RESULT_OK:
			if (!StringUtil.isNullOrEmpty(data)) {
				backfalg = true;
				interestProductType = data.getIntExtra(
						Dept.flexibleInterest_interestProductType, 15);
				iscancel = data.getBooleanExtra(Dept.IS_CHACEL, false);
				
				if (iscancel) {
					// 解约返回
					requestPsnInterestProductInfo(interestProductType);
					
//					if (interestProductType == 15) {
//						// 存储数据
////						DeptDataCenter.getInstance().setBbkMap(null);
//						bbkissign = false;
//		
//					} else if (interestProductType == 16) {
//		
////						DeptDataCenter.getInstance().setEnrichmentMap(null);
//						enrichmentissign = false;
//					}
//		
//					bbk_btn.performClick();
//					initData(DeptDataCenter.getInstance().getBbkMap(), bbkissign);
						
				} else {
					// 签约返回
					BiiHttpEngine.showProgressDialog();
					requestPsnInterestSignedList(interestProductType);
					// requestPsnInterestSignedDetail(interestProductType);
				}
			}
			break;

		default:
			break;
		}

	}
}
