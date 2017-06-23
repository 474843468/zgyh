package com.chinamworld.bocmbci.biz.bocinvt_p603.tradequery;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.CommissionDealGroupProductListActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 中银理财历史交易查询详情页面
 * @author linyl
 *
 */
public class InverstQueryDetailActivity extends BocInvtBaseActivity implements OnClickListener {

	public static final String TAG = "InverstQueryDetailActivity";

	private TitleAndContentLayout v ;

	private String currency;

	private  Button mXpadQuery,mXpadHisQuery;

	private LinearLayout mll_query,mContainerLayout;

	private Intent intent; 

	private String mTranNum,mTranAmount;

	/**常规历史交易查询返回字段**/
	private String mState,trfType;

	/** 产品详情 */
	private Map<String, Object> historyDetailMap ,infoDetailMap,accItemMap,guarantyMap;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setContentView(R.layout.boc_invest_tradequery_querydetail_layout);
		getBackgroundLayout().setTitleText("历史交易查询");
		getBackgroundLayout().setRightButtonText("主界面");
		mll_query = (LinearLayout) findViewById(R.id.tradquery_ll_one);
		v =(TitleAndContentLayout) findViewById(R.id.titleAndContentLayout);
		mContainerLayout = (LinearLayout) v.findViewById(R.id.myContainerLayout);
		v.setTitleText("详    情");
		mXpadQuery = (Button) findViewById(R.id.boci_xpadquery);
		mXpadHisQuery = (Button) findViewById(R.id.boci_xpadhisquery);
		accItemMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_ACCINFO_MAP);
		intent = this.getIntent();
		//		if(intent.getStringExtra("type").equals("01")){//常规交易
		//			historyDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
		//					.getBizDataMap().get(ConstantGloble.BOCINVT_HISDETAIL_MAP);
		//			mState = String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES));
		//			trfType = String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRFTYPE_RES));
		//			initHisTradView();
		//		}else if(intent.getStringExtra("type").equals("02")){//组合购买
		//			guarantyMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_GUARANTY_MAP);
		//			initZHView();
		//		}
//		infoDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.BOCINVT_INFODETAIL_MAP);
//		historyDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
//									.getBizDataMap().get(ConstantGloble.BOCINVT_HISDETAIL_MAP);
//		currency = String.valueOf(infoDetailMap
//				.get(BocInvt.BOCINVT_INFODETAILQUERY_CURRENCY_RES));
//		mState = String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES));
//		mTranNum = (String) infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANNUM_RES);
//		mTranAmount = (String) infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANAMOUNT_RES);
		intent = this.getIntent();
		if(intent.getStringExtra("type").equals("02")){//组合购买
			guarantyMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_GUARANTY_MAP);
			initZHView();
		}else if(intent.getStringExtra("type").equals("01")){//常规交易
			infoDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
					.getBizDataMap().get(ConstantGloble.BOCINVT_INFODETAIL_MAP);
			historyDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
										.getBizDataMap().get(ConstantGloble.BOCINVT_HISDETAIL_MAP);
			currency = String.valueOf(infoDetailMap
					.get(BocInvt.BOCINVT_INFODETAILQUERY_CURRENCY_RES));
			mState = String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES));
			trfType = String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRFTYPE_RES));
			mTranNum = (String) infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANNUM_RES);
			mTranAmount = (String) infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANAMOUNT_RES);
			if(mState.equals("2")){//常规失败
				initFailView(); 
			}else if(mState.equals("1")){//常规成功
				if(String.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES)).equals("1")
						&& (historyDetailMap
								.get(BocInvt.BOCINCT_XPADTRAD_STANDARDPRO_RES).equals("0"))){//净值
					if(trfType.equals("00") || trfType.equals("01") || trfType.equals("02")){
						initJZView();
					}else{
						initHisTradView();//常规列表详情
					}
				}else if((String.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_STANDARDPRO_RES)).equals("1")) 
						&& String.valueOf(historyDetailMap
								.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES)).equals("0")
								&& (trfType.equals("02"))
						){//业绩型 并且 赎回成功
					initYJView();
				}else{//常规成功不能进成功详情的情况下 
					initHisTradView();//常规列表详情
				}
			}else if(mState.equals("3")||mState.equals("4") ||mState.equals("5")){
				initHisTradView();//常规列表详情
			}
		}
	}


	/**
	 * 加载交易页面的单个TextView 字段键值对
	 * @param resid
	 * @param valuetext
	 * @param color 设置字体颜色
	 * @return
	 */
	private LabelTextView createLabelTextView(int resid,String valuetext,TextColor...color){
		LabelTextView v = new LabelTextView(this);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		v.setLabelText(resid);
		v.setValueText(valuetext);
		if(color != null && color.length > 0){
			v.setValueTextColor(color[0]);
		}
		return v;
	} 

//	/** 请求中银理财 交易状况详细信息查询 接口 **/
//	@SuppressWarnings("unchecked")
//	public void requestPsnXpadTransInfoDetailQuery(){
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADTRANSINFODETAILQUERY_API);
//		//		biiRequestBody.setConversationId((String)BaseDroidApp.getInstanse()
//		//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		Map<String,Object> paramsmap = new HashMap<String, Object>();
//		//		Map<String, Object> map = BociDataCenter.getInstance().getBocinvtAcctList().get(mIndex);
//		//		paramsmap.put(BocInvt.BOCINVT_INFODETAILQUERY_TYPEOFACCOUNT_REQ, map.get(Comm.ACCOUNT_TYPE));
//		Map<String,Object> historyDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_HISDETAIL_MAP);
//		paramsmap.put(BocInvt.BOCINVT_INFODETAILQUERY_TRANSEQ_REQ, String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRANSEQ_RES)));
//		//		paramsmap.put(BocInvt.BOCINVT_INFODETAILQUERY_ACCOUNTKEY_REQ, map.get(BocInvt.ACCOUTKEY));
//		paramsmap.put(BocInvt.BOCINVT_INFODETAILQUERY_ACCOUNTKEY_REQ, accItemMap.get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES));
//		biiRequestBody.setParams(paramsmap);
//		BiiHttpEngine.showProgressDialog();
//		HttpManager.requestBii(biiRequestBody, this, "requestPsnXpadTransInfoDetailQueryCallBack");
//
//	}
//	/**
//	 * 中银理财 常规交易状况详细信息查询接口回调
//	 * @param resultObj
//	 * 服务端返回数据
//	 */
//	@SuppressWarnings("unchecked")
//	public void requestPsnXpadTransInfoDetailQueryCallBack(Object resultObj){
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		// 结束通讯,隐藏通讯框
//		BiiHttpEngine.dissMissProgressDialog();
//		infoDetailMap = (Map<String, Object>) biiResponseBody
//				.getResult();
//		if (StringUtil.isNullOrEmpty(infoDetailMap)) {
//			return;
//		}
//		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_INFODETAIL_MAP, infoDetailMap);
//		initTransInfoDetailQueryView();
//
//	}
//	/**初始化常规历史交易详细信息页面**/
//	@SuppressWarnings("unchecked")
//	private void initTransInfoDetailQueryView(){
//		mXpadHisQuery.setVisibility(View.GONE);
//		mContainerLayout.removeAllViews();
//		infoDetailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.BOCINVT_INFODETAIL_MAP);
//		currency = String.valueOf(infoDetailMap
//				.get(BocInvt.BOCINVT_INFODETAILQUERY_CURRENCY_RES));
//		mTranNum = (String) infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANNUM_RES);
//		mTranAmount = (String) infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANAMOUNT_RES);
//		//		mState = String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES));
//		//	    trfType = String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRFTYPE_RES));
//		if(mState.equals("2")||mState.equals("3")||mState.equals("4") ||mState.equals("5")){//失败
//			initFailView(); 
//		}else if(mState.equals("1")){//成功
//			if(String.valueOf(historyDetailMap
//					.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES)).equals("1")
//					&& (historyDetailMap
//							.get(BocInvt.BOCINCT_XPADTRAD_STANDARDPRO_RES).equals("0"))){//净值
//				if(trfType.equals("00") || trfType.equals("01") || trfType.equals("02")){
//					initJZView();
//				}else if((String.valueOf(historyDetailMap
//						.get(BocInvt.BOCINCT_XPADTRAD_STANDARDPRO_RES)).equals("1")) 
//						&& String.valueOf(historyDetailMap
//								.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES)).equals("0")
//								&& (trfType.equals("02"))
//						){//业绩型 并且 赎回成功
//					initYJView();
//				}
//			}
//		}
//	}

	/**常规历史交易查询列表详情1**/
	private void initHisTradView(){
		String tranAmount = String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRFAMOUNT_RES));
		String amount = String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_AMOUNT_RES));
		String curCode = String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_CURRENCYCODE_RES));
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_paymentDate, String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_PAYMENTDATE_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.crcd_setUp_confirm_name, String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODNAME_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.save_deal_type, LocalData.bociHisTrfTypeMap.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRFTYPE_RES)))));
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_tranAtrr, LocalData.bociTranAtrrMap.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRANATRR_RES)))));//返回值类型待确认
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt__commission_date, 
				StringUtil.isNullOrEmpty(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_FUTUREDATE_RES)) ? "-" : String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_FUTUREDATE_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.curCode, LocalData.Currency.get(curCode)));
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_tv_40, LocalData.cashRemitMapValue.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_CASHREMIT_RES)))));
		if(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRFAMOUNT_RES) == null || StringUtil.splitStringwith2point(
				tranAmount, 2).equals("0") || tranAmount.trim().equals("") || StringUtil.splitStringwith2point(
						tranAmount, 2).equals("0.00")){
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeAmount_jiaoyi_colon, "-",TextColor.Red));
		}else{
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeAmount_jiaoyi_colon, StringUtil.splitStringwith2point(tranAmount,2),TextColor.Red));
		}
		if(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_AMOUNT_RES) == null || StringUtil.splitStringwithCode(curCode,
				amount, 2).equals("0") || amount.trim().equals("") || StringUtil.splitStringwithCode(curCode,
						amount, 2).equals("0.00")){
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeamount_colon,"-",TextColor.Red));
		}else{
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeamount_colon,StringUtil.parseStringCodePattern(curCode,amount,2),TextColor.Red));
		}
		mContainerLayout.addView(createLabelTextView(R.string.isForex_query_qudao,LocalData.bociChannelMap.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_CHANNEL_RES)))));
		mContainerLayout.addView(createLabelTextView(R.string.finc_scheduled_status, LocalData.bociHisStatusMap.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES)))));
//		//显示查看按钮
//		if(mState.equals("2")||mState.equals("3")||mState.equals("4") ||mState.equals("5")){//失败
//			//			mXpadHisQuery.setVisibility(View.VISIBLE);
//		}else if(mState.equals("1")){//成功
//			if(String.valueOf(historyDetailMap
//					.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES)).equals("1")
//					&& (historyDetailMap
//							.get(BocInvt.BOCINCT_XPADTRAD_STANDARDPRO_RES).equals("0"))){//净值
//				if(trfType.equals("00") || trfType.equals("01") || trfType.equals("02")){
//					//					mXpadHisQuery.setVisibility(View.VISIBLE);
//				}else if((String.valueOf(historyDetailMap
//						.get(BocInvt.BOCINCT_XPADTRAD_STANDARDPRO_RES)).equals("1")) 
//						&& String.valueOf(historyDetailMap
//								.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES)).equals("0")
//								&& (trfType.equals("02"))
//						){//业绩型 并且 赎回成功
//					//					mXpadHisQuery.setVisibility(View.VISIBLE);
//				}
//			}
//		}
		//		mXpadHisQuery.setOnClickListener(this);
	}
	/**净值型**/
	private void initJZView(){
		//		initPublicView();
		String handlingCostRes = (String) infoDetailMap
				.get(BocInvt.BOCINVT_INFODETAILQUERY_HANDLINGCOST_RES);//手续费用
		String contrFeeRes = (String) infoDetailMap
				.get(BocInvt.BOCINVT_INFODETAILQUERY_CONTRFEE_RES);//业绩报酬
		String curCode = String.valueOf(infoDetailMap
				.get(BocInvt.BOCINVT_INFODETAILQUERY_CURRENCY_RES));
		//		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_paymentDate, String.valueOf(infoDetailMap
		//				.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANDATE_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.crcd_setUp_confirm_name, String.valueOf(infoDetailMap
				.get(BocInvt.BOCINVT_INFODETAILQUERY_PRODNAME_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.save_deal_type, LocalData.bociTrfTypeMap.get(String
				.valueOf(infoDetailMap
						.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANTYPE_RES)))));
		mContainerLayout.addView(createLabelTextView(R.string.curCode, LocalData.Currency
				.get(curCode)));
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_tv_40, LocalData.cashRemitMapValue
				.get(String.valueOf(infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_CASHREMIT_RES)))));
		/**交易属性**/
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_tranAtrr, LocalData.bociTranAtrrMap
				.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRANATRR_RES)))));
		/**委托日期**/
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt__commission_date,
				StringUtil.isNullOrEmpty(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_FUTUREDATE_RES)) ? "-" : String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_FUTUREDATE_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.finc_realdate_colon, String.valueOf(infoDetailMap
				.get(BocInvt.BOCINVT_INFODETAILQUERY_CONFIRMDATE_RES))));
		/**交易渠道**/
		mContainerLayout.addView(createLabelTextView(R.string.trade_channel, LocalData.bociChannelMap
				.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_CHANNEL_RES)))));//从常规历史交易查询取值
		mContainerLayout.addView(createLabelTextView(R.string.finc_tradestate_colon, LocalData.bociIndoDetailStatusMap
				.get(String.valueOf(infoDetailMap
						.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANSTATUS_RES)))));
		mContainerLayout.addView(createLabelTextView(R.string.finc_chengjiaojingzhi, StringUtil.append2Decimals(String.valueOf(infoDetailMap
				.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANNETVAL_RES)), 4),TextColor.Red));//保留四位小数
		/**成交份额**/
		if(infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANNUM_RES) == null ||  StringUtil.splitStringwith2point(
				mTranNum, 2).equals("0") || mTranNum.trim().equals("") || StringUtil.splitStringwith2point(
						mTranNum, 2).equals("0.00")){
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeAmount1_colon, "-",TextColor.Red));
		}else{
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeAmount1_colon, 
					StringUtil.splitStringwith2point(mTranNum, 2),TextColor.Red));//成交份额
		}
		/**成交金额**/
		if((String) infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANAMOUNT_RES) == null || StringUtil.splitStringwithCode(curCode,
				mTranAmount, 2).equals("0") || mTranAmount.trim().equals("") || StringUtil.splitStringwithCode(curCode,
						mTranAmount, 2).equals("0.00")){
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeamount2_colon, "-",TextColor.Red));
		}else{
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeamount2_colon, 
					StringUtil.parseStringCodePattern(currency,
							mTranAmount, 2),TextColor.Red));
		}
		mContainerLayout.addView(createLabelTextView(R.string.bond_history_transFee_1, StringUtil.parseStringCodePattern(currency,
				handlingCostRes, 2),TextColor.Red));//不收取手续费时 显示0
		mContainerLayout.addView(createLabelTextView(R.string.boci_licai_yjbc, StringUtil.parseStringCodePattern(currency,
				contrFeeRes, 2),TextColor.Red));//不收取业绩报酬时 显示0
	}
	/**业绩型**/
	private void initYJView(){
		//		initPublicView();
		String curCode = String.valueOf(infoDetailMap
				.get(BocInvt.BOCINVT_INFODETAILQUERY_CURRENCY_RES));
		//		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_paymentDate, String.valueOf(infoDetailMap
		//				.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANDATE_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.crcd_setUp_confirm_name, String.valueOf(infoDetailMap
				.get(BocInvt.BOCINVT_INFODETAILQUERY_PRODNAME_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.save_deal_type, LocalData.bociTrfTypeMap.get(String
				.valueOf(infoDetailMap
						.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANTYPE_RES)))));
		mContainerLayout.addView(createLabelTextView(R.string.curCode, LocalData.Currency
				.get(String.valueOf(infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_CURRENCY_RES)))));
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_tv_40, LocalData.cashRemitMapValue
				.get(String.valueOf(infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_CASHREMIT_RES)))));
		/**交易属性**/
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_tranAtrr, LocalData.bociTranAtrrMap
				.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRANATRR_RES)))));
		/**委托日期**/
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt__commission_date,
				StringUtil.isNullOrEmpty(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_FUTUREDATE_RES)) ? "-" : String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_FUTUREDATE_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.finc_realdate_colon, String.valueOf(infoDetailMap
				.get(BocInvt.BOCINVT_INFODETAILQUERY_CONFIRMDATE_RES))));
		/**交易渠道**/
		mContainerLayout.addView(createLabelTextView(R.string.trade_channel, LocalData.bociChannelMap
				.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_CHANNEL_RES)))));//从常规历史交易查询取值
		mContainerLayout.addView(createLabelTextView(R.string.finc_tradestate_colon, LocalData.bociIndoDetailStatusMap
				.get(String.valueOf(infoDetailMap
						.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANSTATUS_RES)))));
		mContainerLayout.addView(createLabelTextView(R.string.yearsjRR_outlay, String.valueOf(infoDetailMap
				.get(BocInvt.BOCINVT_INFODETAILQUERY_YEARLYRR_RES))+"%"));//实际年收益
		/**成交份额**/
		if(infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANNUM_RES) == null || StringUtil.parseStringCodePattern(curCode,
				mTranNum, 2).equals("0") || mTranNum.trim().equals("") || StringUtil.parseStringCodePattern(curCode,
						mTranNum, 2).equals("0.00")){
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeAmount1_colon, "-",TextColor.Red));
		}else{
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeAmount1_colon, StringUtil
					.splitStringwith2point(mTranNum, 2),TextColor.Red));//成交份额
		}
		/**成交金额**/
		if((String) infoDetailMap.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANAMOUNT_RES) == null || StringUtil.parseStringCodePattern(curCode,
				mTranAmount, 2).equals("0") || mTranAmount.trim().equals("") || StringUtil.parseStringCodePattern(curCode,
						mTranAmount, 2).equals("0.00")){
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeamount2_colon, "-",TextColor.Red));
		}else{
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeamount2_colon, StringUtil
					.parseStringCodePattern(currency,
							mTranAmount, 2),TextColor.Red));
		}
	}
	/**交易失败**/
	private void initFailView(){
		//		initPublicView();
		//		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_paymentDate, String.valueOf(infoDetailMap
		//				.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANDATE_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.crcd_setUp_confirm_name, String.valueOf(historyDetailMap
				.get(BocInvt.BOCINCT_XPADTRAD_PRODNAME_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.save_deal_type, LocalData.bociTrfTypeMap.get(String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_TRFTYPE_RES)))));
		mContainerLayout.addView(createLabelTextView(R.string.curCode, LocalData.Currency
				.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_CURRENCYCODE_RES)))));
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_tv_40, LocalData.cashRemitMapValue
				.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_CASHREMIT_RES)))));
		/**交易属性**/
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_tranAtrr, LocalData.bociTranAtrrMap
				.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRANATRR_RES)))));
		/** 委托日期 **/
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt__commission_date,
				StringUtil.isNullOrEmpty(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_FUTUREDATE_RES)) ? "-" : String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_FUTUREDATE_RES))));
		/**成交份额**/
		if(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_TRFAMOUNT_RES) == null || mTranNum.equals("0") ||  mTranNum.trim().equals("") || mTranNum.equals("0.000000") || mTranNum.equals("0.00")){
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeAmount_jiaoyi_colon, "-",TextColor.Red));
		}else{
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeAmount_jiaoyi_colon, StringUtil
					.splitStringwith2point(mTranNum, 2),TextColor.Red));//成交份额
		}
		/**成交金额**/
		if((String) historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_AMOUNT_RES) == null || mTranAmount.equals("0") || mTranAmount.trim().equals("") || mTranAmount.equals("0.000000") || mTranAmount.equals("0.00")){
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeamount_colon, "-",TextColor.Red));
		}else{
			mContainerLayout.addView(createLabelTextView(R.string.finc_tradeamount_colon, StringUtil
					.parseStringCodePattern(currency,mTranAmount, 2),TextColor.Red));
		}
		//TODO...确认日期待确认
		mContainerLayout.addView(createLabelTextView(R.string.finc_realdate_confirm_colon, String.valueOf(infoDetailMap
				.get(BocInvt.BOCINVT_INFODETAILQUERY_CONFIRMDATE_RES))));//确认日期
		/**交易渠道**/
		mContainerLayout.addView(createLabelTextView(R.string.trade_channel, LocalData.bociChannelMap
				.get(String.valueOf(historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_CHANNEL_RES)))));
		mContainerLayout.addView(createLabelTextView(R.string.finc_tradestate_colon, LocalData.bociIndoDetailStatusMap.get(String
				.valueOf(historyDetailMap
						.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES)))));//交易状态
		mContainerLayout.addView(createLabelTextView(R.string.trans_mobile_failReason, String.valueOf(infoDetailMap
				.get(BocInvt.BOCINVT_INFODETAILQUERY_FAILREASON_RES))));
	}
	/**组合购买**/
	private void initZHView(){
		getBackgroundLayout().setTitleText("历史交易查询");
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_paymentDate, String.valueOf(guarantyMap
				.get(BocInvt.BOCINVT_XPADQUERY_RETURNDATE_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.prodTraName, String.valueOf(guarantyMap
				.get(BocInvt.BOCINVT_XPADQUERY_PRODNAME_RES))));
		mContainerLayout.addView(createLabelTextView(R.string.currency1, LocalData.Currency
				.get(String.valueOf(guarantyMap.get(BocInvt.BOCINVT_XPADQUERY_CURRENCY_RES)))));//币种
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_tv_40, LocalData.cashRemitMapValue
				.get(String.valueOf(guarantyMap.get(BocInvt.BOCINVT_XPADQUERY_CASHREMIT_RES)))));
		mContainerLayout.addView(createLabelTextView(R.string.bocinvt_buy_price, StringUtil.parseStringCodePattern(currency,
				(String) guarantyMap
				.get(BocInvt.BOCINVT_XPADQUERY_BUYAMT_RES), 2),TextColor.Red));//购买金额
		mContainerLayout.addView(createLabelTextView(R.string.finc_tradeamount2_colon_1, StringUtil.parseStringCodePattern(currency,
				(String) guarantyMap
				.get(BocInvt.BOCINVT_XPADQUERY_AMOUNT_RES), 2),TextColor.Red));//成交金额
		mContainerLayout.addView(createLabelTextView(R.string.trade_channel, LocalData.bociChannelMap.get(String
				.valueOf(guarantyMap
						.get(BocInvt.BOCINVT_XPADQUERY_CHANNEL_RES)))));
		mContainerLayout.addView(createLabelTextView(R.string.finc_scheduled_status, LocalData.bociGuarantyMap.get(String
				.valueOf(guarantyMap
						.get(BocInvt.BOCINVT_XPADQUERY_STATUS_RES)))));//交易状态
		//显示组合购买列表查询按钮
		mXpadQuery.setVisibility(View.VISIBLE);
		mXpadQuery.setOnClickListener(this);
	}
	/**公用字段加载**/
	//	private void initPublicView(){
	//
	//		v.addView(createLabelTextView(R.string.bocinvt_paymentDate, String.valueOf(infoDetailMap
	//				.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANDATE_RES))));
	//		v.addView(createLabelTextView(R.string.crcd_setUp_confirm_name, String.valueOf(infoDetailMap
	//				.get(BocInvt.BOCINVT_INFODETAILQUERY_PRODNAME_RES))));
	//		v.addView(createLabelTextView(R.string.save_deal_type, LocalData.bociTrfTypeMap.get(String
	//				.valueOf(infoDetailMap
	//						.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANTYPE_RES)))));
	//		v.addView(createLabelTextView(R.string.dept_currency, dept_currency_value(2)));
	//		v.addView(createLabelTextView(R.string.finc_realdate_colon, String.valueOf(infoDetailMap
	//				.get(BocInvt.BOCINVT_INFODETAILQUERY_CONFIRMDATE_RES))));
	//		v.addView(createLabelTextView(R.string.finc_tradestate_colon, LocalData.bociIndoDetailStatusMap.get(String
	//				.valueOf(infoDetailMap
	//						.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANSTATUS_RES)))));
	//		v.addView(createLabelTextView(R.string.finc_tradeAmount1_colon, StringUtil.parseStringPattern(
	//				(String) infoDetailMap
	//				.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANNUM_RES), 2)));
	//		v.addView(createLabelTextView(R.string.finc_tradeamount2_colon, StringUtil.parseStringCodePattern(currency,
	//				(String) infoDetailMap
	//				.get(BocInvt.BOCINVT_INFODETAILQUERY_TRANAMOUNT_RES), 2)));
	//
	//	}
	//	/**
	//	 * 获取交易币种
	//	 * @param type  类型标示
	//	 * @return
	//	 */
	//	private String dept_currency_value(int type){
	//		switch (type) {
	//		case 1://常规交易
	//			String currency = String.valueOf(historyDetailMap
	//					.get(BocInvt.BOCINCT_XPADTRAD_CURRENCYCODE_RES));
	//			if (!StringUtil.isNull(LocalData.Currency.get(currency))) {
	//				if (LocalData.Currency.get(currency).equals(ConstantGloble.ACC_RMB)) {
	//					/** 产品币种 */
	//					return LocalData.Currency.get(currency);
	//				} else {
	//					return LocalData.Currency.get(currency
	//							+ LocalData.cashMapValue.get(String.valueOf(historyDetailMap
	//									.get(BocInvt.BOCINCT_XPADTRAD_CASHREMIT_RES))));
	//				}
	//			}
	//		case 2://组合购买
	//			String currency_2 = String.valueOf(guarantyMap
	//					.get(BocInvt.BOCINVT_XPADQUERY_CURRENCY_RES));
	//			if (!StringUtil.isNull(LocalData.Currency.get(currency_2))) {
	//				if (LocalData.Currency.get(currency_2).equals(ConstantGloble.ACC_RMB)) {
	//					/** 产品币种 */
	//					return LocalData.Currency.get(currency_2);
	//				} else {
	//					return LocalData.Currency.get(currency_2
	//							+ LocalData.cashMapValue.get(String.valueOf(guarantyMap
	//									.get(BocInvt.BOCINVT_XPADQUERY_CASHREMIT_RES))));
	//				}
	//			}
	//		}
	//		return null;
	//	}

	//	/**
	//	 * 判断是否可撤单
	//	 * @return
	//	 */
	//	private boolean canBeCanceled(){
	//		String cancancel = (String) historyDetailMap.get(BocInvt.BOCINCT_XPADTRAD_CANBECANCELED_RES);
	//		if (StringUtil.isNullOrEmpty(cancancel)) {
	//			return false;
	//		}else if(cancancel.equals("true") || cancancel.equals("0")){
	//			return true;
	//		}else if(cancancel.equals("false") || cancancel.equals("1")){
	//			return false;
	//		}else {
	//			return false;
	//		}
	//	}
	//按钮点击事件
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.boci_xpadquery://被组合产品列表
			Intent intent = new Intent (InverstQueryDetailActivity.this,CommissionDealGroupProductListActivity.class);
			intent.putExtra("tranSeq2", String.valueOf(guarantyMap.get(BocInvt.BOCINVT_XPADQUERY_TRANSEQ_RES)));
			intent.putExtra("ibknum", String.valueOf(accItemMap.get(BocInvt.IBKNUMBER)));
			intent.putExtra("typeOfAccount",String.valueOf(accItemMap.get(Comm.ACCOUNT_TYPE)));
			intent.putExtra("accountKey", String.valueOf(accItemMap.get(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES)));
			intent.putExtra("currency", String.valueOf(guarantyMap.get(BocInvt.BOCINVT_XPADQUERY_CURRENCY_RES)));
			intent.putExtra("temp", "hisQueryDetail");
			startActivity(intent);
			break;
//		case R.id.boci_xpadhisquery://列表详情页面查看按钮
//			requestPsnXpadTransInfoDetailQuery();
//			break;

		default:
			break;
		}


	}

}
