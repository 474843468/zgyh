package com.chinamworld.bocmbci.biz.goldbonus.busitrade;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.biz.goldbonus.fixinvestmanager.FixInvestManagerActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cfca.mobile.sip.SipBox;
/**
 * 定投预约 详情页面  确认、结果
 * @author linyl
 *
 */
public class FixInvestSignDetailActivity extends GoldBonusBaseActivity implements OnClickListener {
	/**
	 * 枚举详情页面的标示  确认、结果
	 */
	public enum DetailView{
		/**确认页面**/
		confirmView,
		/**结果页面**/
		successView,
	}
	
	private TitleAndContentLayout mAgreeDetailInfo;
	private LinearLayout myContainer;
	private Button btnConfirm,btnFinish,smsBtn;
	private LinearLayout confirmTitle,successTitle;
	private DetailView detailView = DetailView.confirmView;
	/** 加密控件里的随机数 */
	protected String randomNumber;
	/** 中银E盾 */
	private UsbKeyText usbKeytext;
	/** 是否上传短信验证码 */
	private boolean isSms = false;
	/** 是否上传动态口令 */
	private boolean isOtp = false;
	/**定投签约 确认**/
	private Map<String, Object> fixInvestSignPreMap;
	/** 安全因子数组 */
	public List<Map<String, Object>> factorList;
	/** 手机交易码 */
	private LinearLayout ll_smc;
	/** 动态口令 */
	private LinearLayout ll_active_code;
	/** 加密控件——动态口令 */
	private SipBox sipBoxActiveCode = null;
	/** 加密控件——手机交易码 */
	private SipBox sipBoxSmc = null;
	/** 获取到的tokenId 保存 */
	private String tokenId;
	/**定投签约提交接口上送数据**/
	private Map<String,Object> reqFixInvestSignParamMap = new HashMap<String,Object>();
	/**成功页面 链接**/
	private LinearLayout ll_signLink;
	/**点击 这里 链接**/
	private TextView tv_link;
	private SpannableString msp = null;

	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_busitrademain_title);
//		getBackgroundLayout().setRightButtonText("主界面");
		setContentView(R.layout.goldbonus_busitrade_sign);
		mAgreeDetailInfo = (TitleAndContentLayout) findViewById(R.id.titleAndContentLayout);
		mAgreeDetailInfo.setTitleVisibility(View.GONE);
		fixInvestSignPreMap = GoldbonusLocalData.getInstance().FixInvestSignPreMap;
		myContainer = (LinearLayout) findViewById(R.id.myContainerLayout);
		confirmTitle = (LinearLayout) findViewById(R.id.sign_confirm_info_title);
		successTitle = (LinearLayout) findViewById(R.id.sign_success_info_title);
		ll_signLink = (LinearLayout) findViewById(R.id.ll_sign_success_href);
		tv_link = (TextView) findViewById(R.id.tv_link);
		msp = new SpannableString("*您可点击这里对定投预约进行管理。");
		msp.setSpan(new UnderlineSpan(), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		msp.setSpan(new ForegroundColorSpan(Color.BLUE), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_link.setText(getClickableSpan());
		tv_link.setMovementMethod(LinkMovementMethod.getInstance());  
		btnConfirm = (Button) findViewById(R.id.sign_confirm);
		btnFinish = (Button) findViewById(R.id.sign_finish);
		/**随机数*/
		randomNumber = this.getIntent().getStringExtra(Acc.RANDOMNUMBER);
		factorList = (List<Map<String, Object>>) fixInvestSignPreMap.get(Acc.LOSSCONFIRM_ACC_FACTORLIST_RES);
		usbKeytext = (UsbKeyText)findViewById(R.id.sip_usbkey);
		//动态口令
		ll_active_code = (LinearLayout)findViewById(R.id.ll_active_code);
		// 手机交易码
		ll_smc = (LinearLayout)findViewById(R.id.ll_smc);
		sipBoxSmc = (SipBox)findViewById(R.id.sipbox_smc);
		sipBoxActiveCode = (SipBox)findViewById(R.id.sipbox_active);
		smsBtn = (Button)findViewById(R.id.smsbtn);
		btnConfirm.setOnClickListener(this);
		btnFinish.setOnClickListener(this);
		initDetailView();
	}
	private SpannableString getClickableSpan() {
		 //监听器  
       View.OnClickListener listener = new View.OnClickListener() {  
           @Override  
           public void onClick(View v) {  
           	Intent intent2 = new Intent(FixInvestSignDetailActivity.this, FixInvestManagerActivity.class);
   			startActivity(intent2);
   			finish();
           }  
       };  
       SpannableString spanableInfo = new SpannableString("*您可点击这里对定投预约进行管理。");
       spanableInfo.setSpan(new Clickable(listener), 5, 7, Spanned.SPAN_MARK_MARK);
		return spanableInfo;
	}
	
	class Clickable extends ClickableSpan implements View.OnClickListener {  
       private final View.OnClickListener mListener;  
 
       public Clickable(View.OnClickListener listener) {  
           mListener = listener;  
       }  
 
       @Override  
       public void onClick(View view) {  
           mListener.onClick(view);  
       }  
	}
	
	/**
	 * 确认、结果页面的元素展示
	 */
	private void initDetailView() {
		myContainer.removeAllViews();
		if(detailView == DetailView.successView){//结果页
			myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_bankSN, 
					(String)GoldbonusLocalData.getInstance().FixInvestSignMap.get("transactionId"),null));
			myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_fixid, 
					(String)GoldbonusLocalData.getInstance().FixInvestSignMap.get("fixId"),null));
			
		}
		myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_accno, StringUtil
				.getForSixForString((String) GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery
						.get(GoldBonus.ACCOUNTNUM))+" "+DictionaryData.getKeyByValue((String)GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery.get("acctType"),
								DictionaryData.goldbonusAcctTypeList),null));
		myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_traCode, "买入活期贵金属积利产品",null));
		myContainer.addView(createLabelTextView(R.string.goldbonus_timedeposit_proName, (String) GoldbonusLocalData.getInstance().ProductInfoQueryList.get(0).get("issueName"),null));
		myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_curCode, "人民币元",null));
		myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_num, StringUtil.parseStringPattern(this.getIntent().getStringExtra("weight"),0) +" 克",null));
		myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_issuetype, "定投预约（设置定期定额买入）",null));
		myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_fixFreque, this.getIntent().getStringExtra("termUnit"),null));
		myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_fixPayDate, this.getIntent().getStringExtra("traDate"),null));
		myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_end, "累计交易次数  "+this.getIntent().getStringExtra("end"),null));
		if(detailView == DetailView.confirmView){//确认页
			String mmconversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.CONVERSATION_ID);
			usbKeytext.Init(mmconversationId, randomNumber, fixInvestSignPreMap, this);
			isOtp = usbKeytext.getIsOtp();
			isSms = usbKeytext.getIsSmc();
			usbKeytext.setUsbKeyLabelColor(getResources().getColor(R.color.boc_text_color_common_gray));
			ll_smc.setVisibility(View.GONE);
			ll_active_code.setVisibility(View.GONE);
			if (isOtp) {
				ll_active_code.setVisibility(View.VISIBLE);
			
				SipBoxUtils.initSipBoxWithTwoType(sipBoxActiveCode, 6, 6, SipBoxUtils.KEYBOARDTYPE_NUMBOER,
						randomNumber, this);
				// 动态口令
//				sipBoxActiveCode.setCipherType(SystemConfig.CIPHERTYPE);
//				sipBoxActiveCode.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//				sipBoxActiveCode.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//				sipBoxActiveCode.setId(10002);
//				sipBoxActiveCode.setBackgroundResource(R.drawable.bg_for_edittext);
//				sipBoxActiveCode.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//				sipBoxActiveCode.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//				sipBoxActiveCode.setSingleLine(true);
//				sipBoxActiveCode.setSipDelegator(this);
//				sipBoxActiveCode.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//				sipBoxActiveCode.setRandomKey_S(randomNumber);
			}
			if (isSms) {
				ll_smc.setVisibility(View.VISIBLE);
				SipBoxUtils.initSipBoxWithTwoType(sipBoxSmc, 6, 6, SipBoxUtils.KEYBOARDTYPE_NUMBOER,
						randomNumber, this);
//				sipBoxSmc.setCipherType(SystemConfig.CIPHERTYPE);
//				sipBoxSmc.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//				sipBoxSmc.setPasswordMinLength(ConstantGloble.MIN_LENGTH);
//				sipBoxSmc.setId(10002);
//				sipBoxSmc.setBackgroundResource(R.drawable.bg_for_edittext);
//				sipBoxSmc.setPasswordMaxLength(ConstantGloble.MIN_LENGTH);
//				sipBoxSmc.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//				sipBoxSmc.setSingleLine(true);
//				sipBoxSmc.setSipDelegator(this);
//				sipBoxSmc.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//				sipBoxSmc.setRandomKey_S(randomNumber);
				SmsCodeUtils.getInstance().addSmsCodeListner(smsBtn, new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 发送验证码到手机
						sendMSCToMobile();
					}
				});
			}
		}
		
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sign_confirm:
			if (fixInvestSignPreMap.containsKey(Acc.LOSSCONFIRM_ACC_FACTORLIST_RES)) {
//				BaseHttpEngine.showProgressDialog();
				checkDate();
			} else {
//				BaseHttpEngine.showProgressDialog();
				pSNGetTokenId();
			}
			break;
		case R.id.sign_finish:
//			setResult(1001, getIntent());
//			finish();
			ActivityIntentTools.intentToActivity(this, BusiTradeAvtivity.class);
			break;
		}
		
	}
	
	/** 验证动态口令 */
	private void checkDate() {
		/** 音频Key安全认证工具 */
		usbKeytext.checkDataUsbKey(sipBoxActiveCode, sipBoxSmc, new IUsbKeyTextSuccess() {

			@Override
			public void SuccessCallBack(String result, int errorCode) {
//				BaseHttpEngine.showProgressDialog();
				pSNGetTokenId();
			}
		});
	}
	
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
		requestPsnGoldBonusFixInvestSign();
	}
	
	/**
	 * 贵金属积利定投计划签约 接口
	 */
	public void requestPsnGoldBonusFixInvestSign(){
		String conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID);
		Map<String,Object> map = GoldbonusLocalData.getInstance().ProductInfoQueryList.get(0);
		reqFixInvestSignParamMap.put("issueNo", (String)map.get("issueNo"));
		reqFixInvestSignParamMap.put("issueName", (String)map.get("issueName"));
		reqFixInvestSignParamMap.put("xpadIssueType", (String)map.get("issueType"));
		reqFixInvestSignParamMap.put("issueKind", (String)map.get("issueKind"));
		reqFixInvestSignParamMap.put("fixFreque", this.getIntent().getStringExtra("fixFreque"));
		reqFixInvestSignParamMap.put("fixPayDate",this.getIntent().getStringExtra("fixPayDate"));
		reqFixInvestSignParamMap.put("fixValidMonth", this.getIntent().getStringExtra("fixValidMonth"));
		reqFixInvestSignParamMap.put("fixAmt", this.getIntent().getStringExtra("weight"));
		reqFixInvestSignParamMap.put("token", tokenId);
//		reqFixInvestSignParamMap.put("Smc", "");
//		reqFixInvestSignParamMap.put("Otp", "");
//		reqFixInvestSignParamMap.put("_signedData", "");
		/**安全工具随机参数获取*/
		usbKeytext.InitUsbKeyResult(reqFixInvestSignParamMap);
		SipBoxUtils.setSipBoxParams(reqFixInvestSignParamMap);
		BaseHttpEngine.showProgressDialog();
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusFixInvestSign", reqFixInvestSignParamMap, conversationId,new IHttpResponseCallBack<Map<String,Object>>(){

			@Override
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					return;
				}
				detailView = DetailView.successView;
				confirmTitle.setVisibility(View.GONE);
				successTitle.setVisibility(View.VISIBLE);
				btnConfirm.setVisibility(View.GONE);
				ll_signLink.setVisibility(View.VISIBLE);
//				tv_click.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
				btnFinish.setVisibility(View.VISIBLE);
				ll_smc.setVisibility(View.GONE);
				ll_active_code.setVisibility(View.GONE);
				usbKeytext.setVisibility(View.GONE);
				FixInvestSignDetailActivity.this.getBackgroundLayout().setLeftButtomNewVisibility(View.GONE);
//				transactionId = (String) result.get("transactionId");
				GoldbonusLocalData.getInstance().FixInvestSignMap = result;
				initDetailView();
			}
		});
		
	}

}
