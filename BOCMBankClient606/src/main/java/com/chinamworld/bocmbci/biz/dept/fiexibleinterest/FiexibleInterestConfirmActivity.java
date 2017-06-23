package com.chinamworld.bocmbci.biz.dept.fiexibleinterest;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IntentSpan;


public class FiexibleInterestConfirmActivity extends DeptBaseActivity implements
		OnClickListener {
	private View view;
	private int interestProductType = 0;
	private boolean iscancel;
	private TextView confirm_info, Limit_date_info,tv_info;
	/** 电子卡信息 */
	private Map<String, Object> ecardMap;
	private LabelTextView ltv_interestaccnum, ltv_interestproducttype,
			ltv_beginAmount_begintime, ltv_Limit_date, ltv_time;
	private Button btn_cancel, btn_corfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ecardMap = BaseDroidApp.getInstanse().getEcardMap();
		view = addView(R.layout.dept_fiexibleinterest_confirm_layout);
		ibBack.setVisibility(View.GONE);
		interestProductType = getIntent().getIntExtra(
				Dept.flexibleInterest_interestProductType, -1);
		iscancel = getIntent().getBooleanExtra(Dept.IS_CHACEL, false);
		confirm_info = (TextView) view.findViewById(R.id.confirm_info);
		Limit_date_info=(TextView) view.findViewById(R.id.Limit_date_info);
		tv_info = (TextView) view.findViewById(R.id.tv_info);
		if (iscancel) {
			setTitle(getString(R.string.dept_fiexibleinterest_cancel));
			confirm_info
					.setText(getString(R.string.dept_fiexibleinterest_cancel_confirm));
			Limit_date_info.setVisibility(View.VISIBLE);
			tv_info.setVisibility(View.GONE);
		} else {
			setTitle(getString(R.string.dept_fiexibleinterest_sign));
			confirm_info
					.setText(getString(R.string.dept_fiexibleinterest_sign_confirm));
			SpannableString sp = null;
			if (interestProductType == 15) {

				sp = new SpannableString(getResources().getString(
						R.string.dept_fiexibleinterest_confirm_bbk));
			} else {
				sp = new SpannableString(getResources().getString(
						R.string.dept_fiexibleinterest_confirm_enrichment));
			}

			final Intent userIntent = new Intent();
			userIntent.setClass(FiexibleInterestConfirmActivity.this,
					FiexibleInterestProductInfoActivity.class);
			userIntent.putExtra(Dept.flexibleInterest_interestProductType,
					interestProductType);
			sp.setSpan(
					new IntentSpan(new OnClickListener() {

						public void onClick(View view) {

							FiexibleInterestConfirmActivity.this
									.startActivity(userIntent);

						}

					}), sp.length() - 5, sp.length() - 3,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			tv_info.setText(sp);
			tv_info.setMovementMethod(LinkMovementMethod.getInstance());
			tv_info.setVisibility(View.VISIBLE);
		}
		itViews();
		// interestProductType 15:中银步步高 16：中银聚财通
		if (interestProductType == 15) {
			initData(DeptDataCenter.getInstance().getBbkMap(), iscancel);
		} else if (interestProductType == 16) {
			initData(DeptDataCenter.getInstance().getEnrichmentMap(), iscancel);
		}
	}

	private void itViews() {
		ltv_interestaccnum = (LabelTextView) view
				.findViewById(R.id.ltv_interestaccnum);
		ltv_interestproducttype = (LabelTextView) view
				.findViewById(R.id.ltv_interestproducttype);
		ltv_beginAmount_begintime = (LabelTextView) view
				.findViewById(R.id.ltv_beginAmount_begintime);
		ltv_Limit_date = (LabelTextView) view.findViewById(R.id.ltv_Limit_date);
		ltv_time = (LabelTextView) view.findViewById(R.id.ltv_time);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		btn_corfirm = (Button) view.findViewById(R.id.btn_corfirm);
		btn_corfirm.setOnClickListener(this);

	}

	private void initData(Map<String, Object> map, boolean iscancel) {
		ltv_interestaccnum.setValueText(StringUtil
				.getForSixForString((String) ecardMap.get(Comm.ACCOUNTNUMBER)));
		// String interestproducttype=(String) map
		// .get(Dept.flexibleInterest_interestProductType);
		//
		// /*String marketType = (String)
		// map.get(Dept.flexibleInterest_marketType);*/
		// List<Map<String, Object>> interestDetail = (List<Map<String,
		// Object>>) map
		// .get("interestDetail");
		if("Y".equals( map.get(Dept.stepByStep_agreementLimitUnit))){
			ltv_time.setValueText((String) map.get(Dept.stepByStep_agreementLimit)+"年");	
		}else{
			if("M".equals( map.get(Dept.stepByStep_agreementLimitUnit))){
				ltv_time.setValueText((String) map.get(Dept.stepByStep_agreementLimit)+"个月");	
			}else{
				ltv_time.setValueText("12个月");
			}
		}
		if (interestProductType == 15) {

			// 步步高
			ltv_interestproducttype.setValueText(getResources().getString(
					R.string.dept_fiexibleinterest_bbk));
			if (iscancel) {
				// 解约
				ltv_Limit_date.setLabelText(getResources().getString(
						R.string.dept_fiexibleinterest_Limit_date_cancel));
				if(!StringUtil.isNullOrEmpty(map
						.get(Dept.flexibleInterest_beginAmountDays))){
					ltv_Limit_date.setValueText((String) map
							.get(Dept.flexibleInterest_beginAmountDays)+"天");
				}else{
					ltv_Limit_date.setValueText("-天");	
				}
				
				Limit_date_info.setText(getResources().getString(R.string.dept_fiexibleinterest_cancel_bbk));
			} else {
				// 签约
				ltv_Limit_date.setLabelText(getResources().getString(
						R.string.dept_fiexibleinterest_Limit_date));
				ltv_Limit_date.setValueText("90天");
			}

			ltv_beginAmount_begintime.setLabelText(getResources().getString(
					R.string.dept_fiexibleinterest_beginAmount));
			ltv_beginAmount_begintime.setValueText("人民币5000.00元");

		} else {

			// 聚财通
			ltv_interestproducttype.setValueText(getResources().getString(
					R.string.dept_fiexibleinterest_enrichment));
			if (iscancel) {
				// 解约
				ltv_Limit_date.setLabelText(getResources().getString(
						R.string.dept_fiexibleinterest_Limit_account_cancel));
				if(!StringUtil.isNullOrEmpty(map.get("beginTotalAmount"))){
					ltv_Limit_date.setValueText(StringUtil.parseStringPattern((String)map.get("beginTotalAmount"),2) +"元");
						
				}else{
					ltv_Limit_date.setValueText("-元");
						
				}
				Limit_date_info.setText(getResources().getString(R.string.dept_fiexibleinterest_cancel_enrichment));
			} else {
				// 签约
				ltv_Limit_date.setLabelText(getResources().getString(
						R.string.dept_fiexibleinterest_Limit_account));
				ltv_Limit_date.setValueText("900000.00元");
			}
			
			
			ltv_beginAmount_begintime.setLabelText(getResources().getString(
					R.string.dept_fiexibleinterest_begintime));
			ltv_beginAmount_begintime.setValueText("90天");
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.btn_corfirm:
			BiiHttpEngine.showProgressDialog();
			requestCommConversationId();
			break;

		/*
		 * case R.id.tv_detail: Intent intent = new
		 * Intent(FiexibleInterestConfirmActivity.this,
		 * InterestDetailActivity.class);
		 * intent.putExtra(Dept.flexibleInterest_interestProductType,
		 * interestProductType); startActivity(intent); break;
		 */
		default:
			break;
		}

	}

	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	/**
	 * token数据的返回
	 */
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.TOKEN_ID);
		requestSignandescission(token);
	}

	private void requestSignandescission(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();

		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put(Comm.ACCOUNT_ID, ecardMap.get(Comm.ACCOUNT_ID));
		parms.put(Comm.PUBLIC_TOKEN, token);
		if (interestProductType == 15) {
			parms.put(Dept.BBK_NUM, "04");
			if (iscancel) {
				biiRequestBody
						.setMethod(Dept.flexibleInterest_PsnBBKRescissionSubmit);
			} else {
				biiRequestBody
						.setMethod(Dept.flexibleInterest_PsnBBKSignSubmit);
			}
		} else if (interestProductType == 16) {
			parms.put(Dept.ENRICHMENT_NO, "01");

			if (iscancel) {
				biiRequestBody
						.setMethod(Dept.flexibleInterest_PsnEnrichmentRescissionSubmit);

			} else {

				biiRequestBody
						.setMethod(Dept.flexibleInterest_PsnEnrichmentSignSubmit);
			}
		}
		biiRequestBody.setParams(parms);
		HttpManager.requestBii(biiRequestBody, this,
				"requestSignandescissionCallBack");
	}

	public void requestSignandescissionCallBack(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();

		Intent intent = new Intent(FiexibleInterestConfirmActivity.this,
				FiexibleInterestFinishActivity.class);
		intent.putExtra(Dept.IS_CHACEL, iscancel);
		intent.putExtra(Dept.flexibleInterest_interestProductType,
				interestProductType);
		startActivityForResult(intent, 5);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK, data);
			finish();
			break;

		default:
			break;
		}

	}
}
