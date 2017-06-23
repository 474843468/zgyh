package com.chinamworld.bocmbci.biz.goldbonus.fixinvestmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.ShowDialogTools;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贵金属积利金模块 定投管理 提前终止确认页面
 * @author linyl
 *
 */
public class FixInvestStopDetailActivity extends GoldBonusBaseActivity implements OnClickListener {
	/**详情信息展示布局**/
	private TitleAndContentLayout mAgreeDetailInfo;
	private Button btn_confirm,smsBtn;
	/**动态添加元素的布局**/
	private LinearLayout mContainerLayout;
	/**安全工具布局**/
	private LinearLayout combinIdConfirm;
	/**定投提前终止确认页面  备注布局**/
	private LinearLayout cancelConfirm_ll;
	/**列表项详情信息**/
	private Map<String,Object> itemDetailMap;
	/**贵金属积利定投计划终止确认返回数据**/
	private Map<String,Object> fixInvestStopPreMap;
	/**贵金属积利定投计划终止提交 上送参数**/
	private Map<String,Object> requestStopCommitParamMap = new HashMap<String,Object>();
	/** 加密控件里的随机数 */
	private String randomNumber;
	/** 中银E盾 */
	private UsbKeyText usbKeytext;
//	/** 是否上传短信验证码 */
//	private boolean isSms = false;
//	/** 是否上传动态口令 */
//	private boolean isOtp = false;
	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
//	/** 手机交易码 */
//	private LinearLayout ll_smc;
//	/** 动态口令 */
//	private LinearLayout ll_active_code;
//	/** 加密控件——动态口令 */
//	private SipBox sipBoxActiveCode = null;
//	/** 加密控件——手机交易码 */
//	private SipBox sipBoxSmc = null;
	/** 获取到的tokenId 保存 */
	private String tokenId;
	String fixCancelPsStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_fixinvestmanager);
		setContentView(R.layout.goldbonus_fixinvest_stopdetail);
		mAgreeDetailInfo = (TitleAndContentLayout) findViewById(R.id.titleAndContentLayout_detailinfo);
		mAgreeDetailInfo.setTitleVisibility(View.GONE);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);//终止确认页面 确认按钮
		randomNumber = this.getIntent().getStringExtra(Acc.RANDOMNUMBER);
		fixCancelPsStr = this.getIntent().getStringExtra("fixCancelPs");
		fixInvestStopPreMap = GoldbonusLocalData.getInstance().FixInvestStopPreMap;
		mContainerLayout = (LinearLayout) mAgreeDetailInfo.findViewById(R.id.myContainerLayout);
		itemDetailMap = GoldbonusLocalData.getInstance().FixInvestListDetailQueryMap;
		combinIdConfirm = (LinearLayout) findViewById(R.id.combinId_confirm);
		usbKeytext = (UsbKeyText)findViewById(R.id.sip_usbkey);
		//动态口令
//		ll_active_code = (LinearLayout)findViewById(R.id.ll_active_code);
//		// 手机交易码
//		ll_smc = (LinearLayout)findViewById(R.id.ll_smc);
//		sipBoxSmc = (SipBox)findViewById(R.id.sipbox_smc);
//		sipBoxActiveCode = (SipBox)findViewById(R.id.sipbox_active);
		smsBtn = (Button)findViewById(R.id.smsbtn);
		btn_confirm.setOnClickListener(this);
		initDetailView();
	}

	
	/**
	 * 详情页面展示元素
	 */
	private void initDetailView() {
		mContainerLayout.removeAllViews();
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_settime, (String)itemDetailMap.get("crtDate"),null));
		mContainerLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_proName, (String)itemDetailMap.get("issueName"),null));
		mContainerLayout.addView(createLabelTextView(R.string.goldbonus_fixinvest_status, 
							DictionaryData.getKeyByValue((String)itemDetailMap.get("fixStatus"),DictionaryData.FixStatusList),null));
		if("0".equals((String)itemDetailMap.get("fixTermType"))){//日
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tracycle, DictionaryData.getKeyByValue((String)itemDetailMap.get("fixTermType"),DictionaryData.goldbonusfixTermTypeList),null));
		}else if("1".equals((String)itemDetailMap.get("fixTermType"))){//周
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tracycle, 
					DictionaryData.getKeyByValue((String)itemDetailMap.get("fixTermType"),DictionaryData.goldbonusfixTermTypeList) + "," +
							DictionaryData.getKeyByValue((String)itemDetailMap.get("fixPayDateValue"),DictionaryData.goldbonusfixPayDateValueWeekList),null));
		}else if("2".equals((String)itemDetailMap.get("fixTermType"))){//月
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tracycle, 
					DictionaryData.getKeyByValue((String)itemDetailMap.get("fixTermType"),DictionaryData.goldbonusfixTermTypeList) + "," +
							DictionaryData.getKeyByValue((String)itemDetailMap.get("fixPayDateValue"),DictionaryData.goldbonusfixPayDateValueMounthList),null));
		}else{
			mContainerLayout.addView(createLabelTextView(R.string.fixinvest_tracycle,"-",null));
		}
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_buynum, StringUtil.parseStringPattern(StringUtil.deleateNumber((String)itemDetailMap.get("weight")),0)+" 克",null));
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_fixPendCnt,
				"成功 "+String.valueOf((Long.parseLong((String)itemDetailMap.get("fixPendCnt")) - Long.parseLong((String)itemDetailMap.get("fixCount")))) +" 次，失败 " + (String)itemDetailMap.get("fixCount") +" 次",null));
		mContainerLayout.addView(createLabelTextView(R.string.fixinvest_remaindcnt, (String)itemDetailMap.get("unfCount") +	" 次",null));
		if("2".equals((String)itemDetailMap.get("fixStatus")) || "3".equals((String)itemDetailMap.get("fixStatus"))){//状态为客户终止或者银行终止
			if(!"".equals(itemDetailMap.get("remark").toString().trim()) && StringUtil.isNullOrEmpty(itemDetailMap.get("remark"))){
				mContainerLayout.addView(createRemarkLabelTextView(R.string.fixinvest_cancel_ps, "-", null));
			}else{
				mContainerLayout.addView(createRemarkLabelTextView(R.string.fixinvest_cancel_ps, 
						(String)itemDetailMap.get("remark") , null));
			}
		}
			mContainerLayout.addView(createRemarkLabelTextView(R.string.fixinvest_cancel_ps, fixCancelPsStr,null));
			factorList = (List<Map<String, Object>>) fixInvestStopPreMap.get(Acc.LOSSCONFIRM_ACC_FACTORLIST_RES);
			String mmconversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
						.get(ConstantGloble.CONVERSATION_ID);
			usbKeytext.InitAll(mmconversationId, randomNumber, fixInvestStopPreMap, this);
		    usbKeytext.setUsbKeyLabelColor(getResources().getColor(R.color.boc_text_color_common_gray));
//				usbKeytext.Init(mmconversationId, randomNumber, fixInvestStopPreMap, this);
//				isOtp = usbKeytext.getIsOtp();
//				isSms = usbKeytext.getIsSmc();
//				combinIdConfirm.setVisibility(View.VISIBLE);
//				ll_smc.setVisibility(View.GONE);
//				ll_active_code.setVisibility(View.GONE);
//				if (isOtp) {// 动态口令
//					ll_active_code.setVisibility(View.VISIBLE);
//					SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, 6, 6, SipBoxUtils.KEYBOARDTYPE_NUMBOER,
//							randomNumber, this);
//					
//					sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
//					sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//					sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//					sipBoxActiveCode.setId(10002);
//					sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//					sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//					sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//					sipBoxActiveCode.setSingleLine(true);
//					sipBoxActiveCode.setSipDelegator(this);
//					sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//					sipBoxActiveCode.setRandomKey_S(randomNumber);
//				}
//				if (isSms) {
//					ll_smc.setVisibility(View.VISIBLE);
//					SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, 6, 6, SipBoxUtils.KEYBOARDTYPE_NUMBOER,
//							randomNumber, this);
//					sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
//					sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//					sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//					sipBoxSmc.setId(10002);
//					sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//					sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//					sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//					sipBoxSmc.setSingleLine(true);
//					sipBoxSmc.setSipDelegator(this);
//					sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//					sipBoxSmc.setRandomKey_S(randomNumber);
//					SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn, new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							// 发送验证码到手机
//							sendMSCToMobile();
//						}
//					});
//				}
	}
	
	public void sendMSCToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	public void sendMSCToMobileCallback(Object resultObj) {
		
	}
	
	/** 验证动态口令 */
//	private void checkDate() {
//		/** 音频Key安全认证工具 */
//		usbKeytext.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {
//
//			@Override
//			public void SuccessCallBack(String result, int errorCode) {
////				BaseHttpEngine.showProgressDialog();
//				pSNGetTokenId();
//			}
//		});
//	}
	
	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
//		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}
	
	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
//			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		requestPsnGoldBonusFixInvestStop();
	}
	/**
	 * 贵金属积利定投计划终止  接口请求
	 */
	private void requestPsnGoldBonusFixInvestStop() {
		String conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID);
		requestStopCommitParamMap.put("fixId",
				GoldbonusLocalData.getInstance().FixInvestListDetailQueryMap.get("fixId"));
		requestStopCommitParamMap.put("xpadRemark",fixCancelPsStr);
		requestStopCommitParamMap.put("token", tokenId);
//		requestStopCommitParamMap.put("Smc", "");
//		requestStopCommitParamMap.put("Otp", "");
//		requestStopCommitParamMap.put("_signedData", "");
		/**安全工具随机参数获取*/
		usbKeytext.InitUsbKeyResult(requestStopCommitParamMap);
		SipBoxUtils.setSipBoxParams(requestStopCommitParamMap);
		BaseHttpEngine.showProgressDialog();
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusFixInvestStop", requestStopCommitParamMap, conversationId,new IHttpResponseCallBack<Map<String,Object>>(){

			@Override
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					return;
				}
				
				Intent intent = new Intent(FixInvestStopDetailActivity.this, FixInvestStopSuccssActivity.class);
				intent.putExtra("fixCancelPs", fixCancelPsStr);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm://终止确认页面  确认---跳终止完成页面
				/** 音频Key安全工具认证 */
				usbKeytext.checkDataAllUsbKey(new IUsbKeyTextSuccess() {
					@Override
					public void SuccessCallBack(String result, int errorCode) {
						//请求token
						ShowDialogTools.Instance.showProgressDialog();
						pSNGetTokenId();
					}
				});
			break;
		case R.id.set_get:
			//获取并发送手机验证码
			getHttpTools().requestHttp(Setting.SET_SENDMSC, "sendMSCToMobileCallback",
					new HashMap<String, Object>(), true);
			break;
		}
	}

}
