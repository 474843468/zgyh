package com.chinamworld.bocmbci.biz.goldbonus.busitrade;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 贵金属积利金 买卖交易 买卖确认、结果页面详情信息
 * @author linyl
 *
 */
public class BusiTradeDetailActivity extends GoldBonusBaseActivity implements OnClickListener {
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
	private Button btnConfirm,btnFinish;
	private LinearLayout confirmTitle,successTitle;
	/** 买卖标示  1：买，2：卖 **/
	private String busiFlag,weight,buyprice,sellprice,issueTypeFlag;
	private DetailView detailView = DetailView.confirmView;
	/**买卖交易接口上送参数**/
	private Map<String,Object> reqParamsMap = new HashMap<String,Object>();
	/**买卖交易接口返回数据**/
	private Map<String,Object> resBusiTradeMap = new HashMap<String,Object>();
	private String tokenId;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_busitrademain_title);
		//		getBackgroundLayout().setRightButtonText("主界面");
		//		getBackgroundLayout().setLeftButtonClickListener(backClickListener);
		//		getBackgroundLayout().setPaddingWithParent(20, 0, 0, 20);
		setContentView(R.layout.goldbonus_busitrade_detail);
		mAgreeDetailInfo = (TitleAndContentLayout) findViewById(R.id.titleAndContentLayout);
		mAgreeDetailInfo.setTitleVisibility(View.GONE);
		myContainer = (LinearLayout) findViewById(R.id.myContainerLayout);
		confirmTitle = (LinearLayout) findViewById(R.id.depositpro_confirm_info_title);
		successTitle = (LinearLayout) findViewById(R.id.depositpro_success_info_title);
		tv = (TextView) findViewById(R.id.tv_succ_title);
		tv.getPaint().setFakeBoldText(true);
		btnConfirm = (Button) findViewById(R.id.depositpro_confirm);
		btnFinish = (Button) findViewById(R.id.depositpro_finish);
		btnConfirm.setOnClickListener(this);
		btnFinish.setOnClickListener(this);
		busiFlag = this.getIntent().getStringExtra("busiFlag");
		weight = this.getIntent().getStringExtra("weight");
		buyprice = this.getIntent().getStringExtra("buyprice");
		sellprice = this.getIntent().getStringExtra("sellprice");
		issueTypeFlag = this.getIntent().getStringExtra("issueTypeFlag");
		initDetailView();
	}
	/**
	 * 初始化详情页面元素展示
	 */
	private void initDetailView() {
		myContainer.removeAllViews();
		if(detailView == DetailView.successView){//结果页面
			myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_bankSN, (String)resBusiTradeMap.get("transactionId"),null));
		}
		myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_accno, StringUtil
				.getForSixForString((String) GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery
						.get(GoldBonus.ACCOUNTNUM))+" "+DictionaryData.getKeyByValue((String)GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery.get("acctType"),
								DictionaryData.goldbonusAcctTypeList),null));
		myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_traCode, 
				"1".equals(busiFlag) ? "买入活期贵金属积利产品" : "卖出活期贵金属积利产品",null));
		myContainer.addView(createLabelTextView(R.string.goldbonus_timedeposit_proName, this.getIntent().getStringExtra("proName"),null));
		if(detailView == DetailView.confirmView){//确认页面
			myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_curCode, "人民币元",null));
		}
//		if(detailView == DetailView.successView){//结果页面
//			myContainer.addView(createLabelTextView(R.string.fixinvest_traprice, "1"));
//		}
		myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_num, StringUtil.parseStringPattern(weight,0) +" 克",null));
		if(detailView == DetailView.confirmView){//确认页面
			if("1".equals(busiFlag)){
				myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_cankaoprice, sellprice +" 人民币元/克",null));//参考牌价
			}else{
				myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_cankaoprice, buyprice +" 人民币元/克",null));
			}
		}
		if(detailView == DetailView.successView){//结果页面
//			myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_baseprice,  StringUtil.parseStringPattern((String)resBusiTradeMap.get("standardPrice"),2)+" 人民币元/克",null));//基础牌价   standardPrice
//			myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_favourprice,  StringUtil.parseStringPattern((String)resBusiTradeMap.get("tranPrice"),2)," 人民币元/克",TextColor.Red,TextColor.Black));//实际交易牌价  tranPrice
			myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_baseprice,  paseEndZero((String)resBusiTradeMap.get("standardPrice"))+" 人民币元/克",null));//基础牌价   standardPrice
			myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_favourprice,  paseEndZero((String)resBusiTradeMap.get("tranPrice"))," 人民币元/克",REDBLACK));//实际交易牌价  tranPrice
			myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_traAmount,"人民币元  ",StringUtil.parseStringPattern((String)resBusiTradeMap.get("saleAmount"),2),BLACKRED));//成交金额
		}
		if("1".equals(issueTypeFlag)){
			myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_issuetype, "立即执行（市价即时方式）",null));
		}else if("2".equals(issueTypeFlag)){
			myContainer.addView(createLabelTextView(R.string.goldbonus_busitrade_issuetype, "定投预约（设置定期定额买入）",null));
		}
	}
	
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		//获取Token
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	   
	}
	
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestPsnGoldBonusTradeSubmit();
	}
	
	/**
	 * 贵金属积利买卖交易 接口请求
	 */
	private void requestPsnGoldBonusTradeSubmit() {
		reqParamsMap.put("token", tokenId);
		reqParamsMap.put("saleType", "1".equals(busiFlag) ? "0" : "1");
		reqParamsMap.put("weight", weight);
		reqParamsMap.put("tranPrice", buyprice);
		reqParamsMap.put("xpadgBuyPrice", sellprice);
		reqParamsMap.put("accountId", StringUtil.isNullOrEmpty(GoldbonusLocalData.getInstance().accountIdOld) ?
				GoldbonusLocalData.getInstance().accountIdBusi : GoldbonusLocalData.getInstance().accountIdOld);//登记后直接进买卖交易 accountidold = null
		reqParamsMap.put("issueNo",  this.getIntent().getStringExtra("issueNo"));
		reqParamsMap.put("issueName", this.getIntent().getStringExtra("proName"));
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusTradeSubmit", reqParamsMap, (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID),new IHttpResponseCallBack<Map<String,Object>>(){

			@Override
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					return;
				}
				resBusiTradeMap = result;
				detailView = DetailView.successView;
				confirmTitle.setVisibility(View.GONE);
				successTitle.setVisibility(View.VISIBLE);
				btnConfirm.setVisibility(View.GONE);
				btnFinish.setVisibility(View.VISIBLE);
				BusiTradeDetailActivity.this.getBackgroundLayout().setLeftButtomNewVisibility(View.GONE);
				initDetailView();
			}
		});
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.depositpro_confirm://确认
			//调买卖交易的提交接口  
			requestCommConversationId();
			break;
		case R.id.depositpro_finish://完成
//			finish();
			ActivityIntentTools.intentToActivity(this, BusiTradeAvtivity.class);
			break;
		}
	}

	//	/**返回键处理事件**/
	//	OnClickListener backClickListener = new OnClickListener() {
	//		@Override
	//		public void onClick(View v) {
	//			backClick();
	//		}
	//	};
	//	
	//	@Override
	//	public void onBackPressed() {
	//		backClick();
	//	}
	//
	//	/**
	//	 * 返回事件的处理
	//	 */
	//	private void backClick(){
	//		if(temp == 1){
	//			temp = 0;
	//			confirmTitle.setVisibility(View.VISIBLE);
	//			successTitle.setVisibility(View.GONE);
	//			btnConfirm.setVisibility(View.VISIBLE);
	//			btnFinish.setVisibility(View.GONE);
	//			this.getBackgroundLayout().setLeftButtonText(null);
	//			initDetailView();
	//		}else{
	//			BusiTradeDetailActivity.this.finish();
	//		}
	//	}

}
