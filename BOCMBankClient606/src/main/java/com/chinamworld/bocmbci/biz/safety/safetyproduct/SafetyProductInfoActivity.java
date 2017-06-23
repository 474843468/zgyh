package com.chinamworld.bocmbci.biz.safety.safetyproduct;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.biz.safety.safetytemp.SafetyTempProductListActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 保险详情
 * 
 * @author panwe
 * 
 */
public class SafetyProductInfoActivity extends SafetyBaseActivity {
	private View mMainView;
	private boolean formTag;
	private String companyName;
	private String subCompanyName;
	private String prodictInfo;
	private String itemUrl;
	private boolean isLogin = false;
	/** 详情页点右上角登录和点投保跳登录，返回的时候是否跳转须知页，右上角登录后留在本页面，投保登录后跳转须知页 */
	private boolean isToMustKnow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = View.inflate(this, R.layout.safety_product_info, null);
		addView(mMainView);
		getFromIntent();
		if (!isLogin) {
			initLoginFalseView();
		}
		initViews();
	}

	private void getFromIntent() {
		isLogin = getIntent().getBooleanExtra("ISLOGIN", false);
		formTag = getIntent().getBooleanExtra(SafetyConstant.PRODUCTORSAVE, false);
		companyName = getIntent().getStringExtra(Safety.INSURANCE_COMANY);
		prodictInfo = getIntent().getStringExtra(Safety.PROD_INFO);
		itemUrl = getIntent().getStringExtra(Safety.ITEM_URL);
	}
	
	/** 初始化未登录界面 */
	private void initLoginFalseView() {
		showTitlebarLoginButton();
	}
	
	/** 初始化已登录界面 */
	private void initLoginTrueView() {
//		setRightTopGone();
		hineTitlebarLoginButton();
	}
	
	private void initViews() {
//		mLeftButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent data = new Intent();
//				data.putExtra("ISLOGIN", isLogin);
//				System.out.println("详情页发送-->" + isLogin);
//			}
//		});
		TextView company = (TextView) mMainView.findViewById(R.id.company);
		TextView subCompany = (TextView) mMainView.findViewById(R.id.tv_subCompany);
		TextView productName = (TextView) mMainView.findViewById(R.id.product_name);
		TextView productAlias = (TextView) mMainView.findViewById(R.id.alias);
		TextView productIntro = (TextView) mMainView.findViewById(R.id.product_introduce);
		mMainView.findViewById(R.id.item_detail).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtil.isNull(itemUrl)) {
					itemUrl = (String)SafetyDataCenter.getInstance().getProductInfoMap().get(Safety.ITEM_URL);
				}
				productDescription(itemUrl);
			}
		});

		Map<String, Object> productInfo = SafetyDataCenter.getInstance().getProductInfoMap();
		initButton(checkShowBtn(productInfo));
		if (formTag) {
			setTitle(getString(R.string.safety_product_info_title));
			prodictInfo = (String) productInfo.get(Safety.PROD_INFO);
		} else {
			setTitle(getString(R.string.safety_product_info_title2));
			((LinearLayout) mMainView.findViewById(R.id.layout_temp)).setVisibility(View.VISIBLE);
			companyName = (String) productInfo.get(Safety.INSURANCE_COMANY);
			subCompanyName = (String) productInfo.get(Safety.SUBINSUNAME);
			mMainView.findViewById(R.id.ll_subCompany).setVisibility(View.VISIBLE);
			subCompany.setText(StringUtil.valueOf1(subCompanyName));
			/** 产品别名 */
			productAlias.setText((String) productInfo.get(Safety.ALIAS_ID));
		}
		/** 公司名称  */
		company.setText(companyName);
		/** 产品名称 */
		productName.setText((String) productInfo.get(Safety.RISKNAME));
		/** 产品介绍  */
		productIntro.setText(prodictInfo);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, company);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, productName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, productAlias);
	}
	
	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("ISLOGIN", isLogin);
		setResult(100000, data);
		System.out.println("详情页发送-->" + isLogin);
		super.finish();
	}

	private void initButton(boolean isShow) {
		if (isShow && formTag) {
			((Button) mMainView.findViewById(R.id.btnbuy)).setVisibility(View.VISIBLE);
		} else if (!formTag && isShow) {
			((Button) mMainView.findViewById(R.id.btndelete)).setVisibility(View.VISIBLE);
			((Button) mMainView.findViewById(R.id.btnedit)).setVisibility(View.VISIBLE);
		}
	}

	private boolean checkShowBtn(Map<String, Object> map) {
		if (!formTag) {
			return true;
		}
		if (map.get(Safety.RISKTYPE).equals(Safety.RISKTYPE_JIACAI)
				|| map.get(Safety.RISKTYPE).equals(Safety.RISKTYPE_YIWAI)) {
			return true;
		}
		return false;
	}

	/**
	 * 投保
	 * @param v
	 */
	public void insurBuyBtnOnclick(View v) {
		if (!isLogin) {
			isToMustKnow = true;
//			Intent intent = new Intent(SafetyProductInfoActivity.this, LoginActivity.class);
//			startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
			BaseActivity.getLoginUtils(SafetyProductInfoActivity.this).exe(new LoginTask.LoginCallback() {

				@Override
				public void loginStatua(boolean isLogin) {
					if(isLogin){
						isLogin = true;
						initLoginTrueView();
						initViews();
						if (isToMustKnow) {
							toNextPage();
						}
					}
				}
			});
			return;
		}
		toNextPage();
	}
	
	/**
	 * 删除
	 * @param v
	 */
	public void insurDeleteOnclick(View v){
		showNotiDialog();
	}
	
	/**
	 * 修改
	 * @param v
	 */
	public void insurEditBtnOnclick(View v){
		BaseHttpEngine.showProgressDialog();
		requestSystemDateTime();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE) {
				isLogin = true;
				initLoginTrueView();
				initViews();
				if (isToMustKnow) {
					toNextPage();
				}
			}
		}
	}
	
	/**
	 * 产品明细
	 * @param v
	 */
	public void detailPDFOnclick(View v){
	}
	
	private void showNotiDialog(){
		BaseDroidApp.getInstanse().showErrorDialog(
				SafetyProductInfoActivity.this.getString(R.string.safety_delete_tip),
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

	private void toNextPage() {
		Map<String, Object> infoMap = SafetyDataCenter.getInstance().getProductInfoMap();
		Intent mIntent = new Intent();
		mIntent.putExtra(Safety.RISKNAME, (String) infoMap.get(Safety.RISKNAME));
		mIntent.putExtra(Safety.INSURANCE_ID, (String) infoMap.get(Safety.INSURANCE_ID));
		mIntent.putExtra(Safety.INSURANCE_COMANY, companyName);
		if (formTag) {
			mIntent.putExtra(Safety.RISKTYPE, (String) infoMap.get(Safety.RISKTYPE));
			mIntent.setClass(this, SafetyCustomerKnowActivity.class);
			mIntent.putExtra(Safety.NOTICE, (String) infoMap.get(Safety.NOTICE));
			mIntent.putExtra(Safety.EXCEPTIONPROFE,(String) infoMap.get(Safety.EXCEPTIONPROFE));
			mIntent.putExtra(Safety.INSUCODE,(String) infoMap.get(Safety.INSUCODE));
			mIntent.putExtra(SafetyConstant.PRODUCTORSAVE,true);
		} else {
			mIntent.putExtra(Safety.INSUCODE, (String)infoMap.get(Safety.INSUCODE));
			mIntent.putExtra(Safety.RISKTYPE, (String) infoMap.get(Safety.TRANTYPE_FLAG));
			mIntent.setClass(this, SafetyProductBuyMsgFillActivity.class).putExtra(SafetyConstant.PRODUCTORSAVE, false);
		}
		startActivity(mIntent);
	}
	
	/** 请求系统时间返回 */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		SafetyDataCenter.getInstance().setSysTime(dateTime);
		requestBankAcctList(SafetyDataCenter.accountTypeList);
	}
	
	/** 银行卡列表返回  */
	@Override
	public void bankAccListCallBack(Object resultObj) {
		super.bankAccListCallBack(resultObj);
		SafetyUtils.initCityData(this,false);
		BaseHttpEngine.dissMissProgressDialog();
		toNextPage();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		pSNGetTokenId();
	}
	
	/** 请求token */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}
	
	public void aquirePSNGetTokenId(Object resultObj) {
		String tokenId = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog(); return;
		}
		deleteInsurance(tokenId);
	}
	
	/**
	 * 请求删除保单
	 * @param tokenId
	 */
	private void deleteInsurance(String tokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Safety.METHOD_DELETEINSUR);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> infoMap = SafetyDataCenter.getInstance().getProductInfoMap();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(Safety.TOKEN, tokenId);
		param.put(Safety.ALIAS_ID, (String)infoMap.get(Safety.ALIAS_ID));
		param.put(Safety.DEPETE_INSURID, (String)infoMap.get(Safety.DEPETE_INSURID));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "insurDeleteCallBack");
	}
	
	public void insurDeleteCallBack(Object resultObj) {
		CustomDialog.toastShow(this,
				this.getString(R.string.safety_delete_success));
		ActivityTaskManager.getInstance().removeAllActivity();
		startActivity(new Intent(this, SafetyTempProductListActivity.class));
	}
}
