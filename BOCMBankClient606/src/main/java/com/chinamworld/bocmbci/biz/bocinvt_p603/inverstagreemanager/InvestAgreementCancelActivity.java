package com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
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
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财 投资协议 终止页面
 * @author linyl
 */
public class InvestAgreementCancelActivity extends BocInvtBaseActivity implements OnClickListener {
	public static final String TAG = "InvestAgreementCancelActivity";
	/** 终止协议页面布局 **/
	private LinearLayout mCancelInfo_ll,mCancelConfirm_ll;
	/** 页面内容展示布局 **/
	private TitleAndContentLayout mCancelInfo_content,mCancelConfirm_content;
	/** 页面按钮 **/
	private Button mBtnCancel,mBtnConfirm_one,mBtnConfirm_two;
	/**协议信息文本显示**/
	private TextView mAgrCancelInfo,mAgrConfirmText;
	/**正常协议详情信息**/
	private Map<String,Object> agrInfoQueryMap,itemMap;
	/** 终止协议 上送字段 **/
	private String mAccountKey,mCustAgrCode,mAgrtype,mProductName,mBuyPeriod,mAmountType,mCurrencyCode,
					mCashRemit,mBaseAmount,mMinAmount,mMaxAmount,tokenId,mCustAgrCodeReq,mInstType,
					mproductName;
	/** 投资协议 终止 接口结果 返回数据 **/
	private Map<String,Object> mAgrCancelMap,mBenchMarkMap;
	/** 投资协议终止 接口返回 数据 字段 **/
	private String mAgrName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		getBackgroundLayout().setTitleText(R.string.boc_invest_agrcancel_title);
		getBackgroundLayout().setRightButtonText("主界面");
		getBackgroundLayout().setPadding(0, 0, 0, 0);
		setContentView(R.layout.boc_agrmentcancel);
		init();
		requestCommConversationId();
		BaseHttpEngine.showProgressDialogCanGoBack();
	}
	/**
	 * 初始化页面
	 */
	@SuppressWarnings("unchecked")
	private void init(){
		mCancelInfo_ll = (LinearLayout) findViewById(R.id.agr_cancel_info_ll);
		mCancelConfirm_ll = (LinearLayout) findViewById(R.id.agr_cancel_info_confirm_ll);
		mCancelInfo_content = (TitleAndContentLayout) findViewById(R.id.agr_cancel_info_content);
		mCancelInfo_content.setTitleVisibility(View.GONE);
		mAgrCancelInfo = (TextView) findViewById(R.id.contenttextview);
//		mBtnCancel = (Button) findViewById(R.id.agrcancelinfo_cancel);
		mBtnConfirm_one = (Button) findViewById(R.id.agrcancelinfo_confirm);
		//注册按钮监听事件
//		mBtnCancel.setOnClickListener(this);
		mBtnConfirm_one.setOnClickListener(this);
		itemMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BOCINVT_CAPACITYQUERY_MAP);
		agrInfoQueryMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BOCINVT_AGREEMENTINFOQUERY_MAP);
		if(StringUtil.isNullOrEmpty(agrInfoQueryMap)) return;
		//赋值
		mAccountKey = String.valueOf(itemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES));
//		mCustAgrCode = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AGRCODE_RES));
		mCustAgrCodeReq = String.valueOf(itemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_CUSTAGECODE_RES));
		mAgrtype = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AGRTYPE_RES));
		mProductName = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_PRONAME_RES));
		mBuyPeriod = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_BUYPERIOD_RES));
		mAmountType = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNTTYPE_RES));
		mBaseAmount = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNT_RES));
		mMinAmount = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_MINANOUNT_RES));
		mMaxAmount = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_MAXAMOUNT_RES));
		mCashRemit = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_CASHREMIT_RES));
		mCurrencyCode = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_PROCUR_RES));
		mInstType = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_INSTTYPE_RES));
		mproductName = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AGRNAME_RES));
		//设置各协议类型的显示的终止协议文本信息
		setAgrCancelTextView(Integer.valueOf(mAgrtype));
	}
	/**
	 * 重写父类方法   请求conversationId 接口回调
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}
	
	/**
	 * 请求TokenId 
	 */
	public void requestPSNGetTokenId(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}
	/**
	 * 请求TokenId 回调
	 * @param resultObj
	 */
	public void aquirePSNGetTokenId(Object resultObj){
		tokenId = this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog(); return;
		}
		BaseHttpEngine.dissMissProgressDialog();
	}
	
	/**
	 * 请求投资协议终止的接口 调用
	 */
	public void requestPsnXpadInvestAgreementCancel(String TokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADINVESTAGREEMENTCANCEL_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String,Object>paramsMap = new HashMap<String, Object>();
		paramsMap.put(BocInvt.BOCINVT_AGRCANCEL_ACCOUNTKEY_REQ, mAccountKey);
		paramsMap.put(BocInvt.BOCINVT_AGRCANCEL_CUSTAGRCODE_REQ, mCustAgrCodeReq);
		paramsMap.put(BocInvt.BOCINVT_AGRCANCEL_AGRTYPE_REQ, mAgrtype);
		paramsMap.put(BocInvt.BOCINVT_AGRCANCEL_PRONAME_REQ, mProductName);
		paramsMap.put(BocInvt.BOCINVT_AGRCANCEL_TOTALPERIOD_REQ, mBuyPeriod);
		paramsMap.put(BocInvt.BOCINVT_AGRCANCEL_AMOUNTTYPE_REQ, mAmountType);
		paramsMap.put(BocInvt.BOCINVT_AGRCANCEL_BASEAMOUNT_REQ,mBaseAmount.equals("-1.00") ? "0.00" : mBaseAmount);
		paramsMap.put(BocInvt.BOCINVT_AGRCANCEL_MINAMOUNT_REQ, mMinAmount);
		paramsMap.put(BocInvt.BOCINVT_AGRCANCEL_MAXAMOUNT_REQ, mMaxAmount);
		paramsMap.put(BocInvt.BOCINVT_AGRCANCEL_CURRENCYCODE_REQ, mCurrencyCode);
		paramsMap.put(BocInvt.BOCINVT_AGRCANCEL_CASHREMIT_REQ, mCashRemit);
		paramsMap.put(BocInvt.BOCINVT_AGRCANCEL_TOKEN_REQ, TokenId);
		biiRequestBody.setParams(paramsMap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnXpadInvestAgreementCancelCallBack");
	}
	
	/**
	 * 请求智能投资协议 终止 的接口 回调
	 * @param resultObj 服务器返回数据
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadInvestAgreementCancelCallBack(Object resultObj){
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> responseList = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = responseList.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		mAgrCancelMap = (Map<String, Object>) biiResponseBody.getResult();
		if(StringUtil.isNullOrEmpty(mAgrCancelMap)) return;
		mAgrName = String.valueOf(mAgrCancelMap.get(BocInvt.BOCINVT_AGRCANCEL_AGRNAME_RES));
		cancelSuccess(mAgrName);
	}
	/**
	 * 协议终止成功操作
	 * @param agrName
	 */
	public void cancelSuccess(String agrName) {
		mCancelInfo_ll.setVisibility(View.GONE);
		mCancelConfirm_ll.setVisibility(View.VISIBLE);
		getBackgroundLayout().setLeftButtonText(null);
		mCancelConfirm_content = (TitleAndContentLayout) findViewById(R.id.agr_cancel_info_content_confirm);
		mCancelConfirm_content.setTitleVisibility(View.GONE);
		mBtnConfirm_two = (Button) mCancelConfirm_ll.findViewById(R.id.agrcancelinfo_confirm_two);
		mAgrConfirmText = (TextView) mCancelConfirm_content.findViewById(R.id.agrconfirm_textview);
		mAgrConfirmText.setText("您的"+agrName+"终止操作已成功");
		mBtnConfirm_two.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InvestAgreementCancelActivity.this,InvestInvalidAgreeQueryActivity.class);
				startActivity(intent);
				InvestAgreementCancelActivity.this.finish();
			}
		});
	}
	
	/**
	 * 页面按钮点击事件
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.agrcancelinfo_cancel://取消
//			finish();
//			break;
		case R.id.agrcancelinfo_confirm://确定1
			if(mAgrtype.equals("5") && mInstType.equals("8")){//周期滚续 业绩基准产品协议 终止
				requestPsnXpadBenchmarkMaintainResult(tokenId,"2");
			}else{
				requestPsnXpadInvestAgreementCancel(tokenId);
			}
			break;
		}
	}
	/**
	 * 设置终止信息信息的文本内容
	 * @param agrType  协议类型
	 * @return
	 */
	private void setAgrCancelTextView(int agrType){
		switch (agrType) {
		case 1://智能投资
			mAgrCancelInfo.setText(R.string.boc_invest_agrcancel_info_zhineng);
			break;
		case 3://周期混续
		case 5://业绩基准周期滚续
			mAgrCancelInfo.setText(R.string.boc_invest_agrcancel_info_zhouqi);
			break;
		case 2://定时定额
		case 4://余额理财
			mAgrCancelInfo.setText(R.string.boc_invest_agrcancel_info_yueanddsde);
			break;
		}
	}
	/**
	 * 业绩基准周期滚续产品更新/终止
	 * @param TokenId
	 * @param opt 操作类型
	 */
	public void requestPsnXpadBenchmarkMaintainResult(String TokenId,String opt){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADBENCHMARKMAINTAINRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String,Object>paramsMap = new HashMap<String, Object>();
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_CUSTAGRCODE_REQ, mCustAgrCodeReq);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_ACCOUNTKEY_REQ, mAccountKey);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_PRODUCTNAME_REQ, mproductName);//暂定协议名称
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_CURRENCYCODE_REQ, mCurrencyCode);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_CASHREMIT_REQ, mCashRemit);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_TATALPERIOD_REQ, mBuyPeriod);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_AMOUNTTYPE_REQ, mAmountType);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_BASEAMOUNT_REQ, mBaseAmount);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_OPT_REQ, opt);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_TOKEN_REQ, TokenId);
		biiRequestBody.setParams(paramsMap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnXpadBenchmarkMaintainResultCallBack");
	}
	/**
	 *  业绩基准周期滚续产品更新/终止 回调
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadBenchmarkMaintainResultCallBack(Object resultObj){
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> responseList = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = responseList.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		mBenchMarkMap = (Map<String, Object>) biiResponseBody.getResult();
		if(StringUtil.isNullOrEmpty(mBenchMarkMap)) return;
		cancelSuccess(mproductName);
	}

}
