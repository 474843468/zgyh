package com.chinamworld.bocmbci.biz.goldbonus.busitrade;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.biz.goldbonus.accountmanager.GoldbounsReminderActivity;
import com.chinamworld.bocmbci.biz.investTask.GoldBonusTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贵金属积利金  买卖交易 主界面 
 * @author linyl
 *
 */
public class BusiTradeAvtivity extends GoldBonusBaseActivity {

	private static final String TAG = "BusiTradeAvtivity";
	private Button btnNext;
	private RadioButton rbBuy,rbSell,rbImmediately,rbReserve;
	private TextView tvReference;
	private RadioGroup rg_tracode,rg_issueType;
	private TextView tvSellType;
	/**买卖方向  1 买，2 卖**/
	private String busiFlag = "1";
	/**交易方式 1立即执行 2定投预约**/
	private String issueTypeFlag = "1";
	private EditText et_InputNum;
	/**可用余额、参考金额、参考信息、整个买卖主界面布局**/
	private LinearLayout ll_Balance,ll_AmountCankao,ll_CankaoInfo,ll_rate_current;
	private TextView buyprice,sellprice,accNO,rateCurrent;
	private TextView proName;
	/**产品编号**/
	String issueNoStr;
	/**产品查询上送数据**/
	private Map<String,Object> reqInfoQueryMap = new HashMap<String, Object>();
	/**可用余额 买--金额；卖--黄金克数**/
	private TextView balance;
	/**买卖交易金额试算**/
	private TextView saleAmt;
	/**买卖交易金额试算接口上送字段**/
	private Map<String,Object> reqTrialParamMap = new HashMap<String, Object>();
	/**c从账户管理进来买卖交易传值标示   买卖方向 **/
	private int AccFlag;
	/**定投管理进入买卖交易 交易方向标示 定投预约状态**/
	private String FixInvestIssueType = null;
	/**签约标示、外置登录时 账户缓存id**/
	String linkAccFlag,accIdOutLay;
	// 第一次调用7秒查询
	private boolean isfirstError = true;
	/**外置登录标示  1 为外置进入**/
	int outLayFlag = 0 ;
	/**存储是输入的交易数量**/
	String inputNumStr, inputNumStrTwo, inputNumStrThr;
	ScrollView sc_price,sc_busiDetail;
	/**定投预约-->立即执行   true 为定投预约到立即执行**/
	boolean Reserve_Immediately = false;
//	/**记录上一次金额试算值**/
//	String saleAmtLast = null;
//	/**前后金额试算值比较   true 不一样，false 一样**/
//	boolean saleAmtChange = true;
	/**银行卖出价1、2  用于判断两次牌价是否变化，金额试算时会比较**/
	String sellpriceStr,sellpriceStrTwo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_busitrademain_title);
		getBackgroundLayout().setRightButtonNewText(null);
		getBackgroundLayout().setLeftButtonNewClickListener(backClickListener);
		setLeftSelectedPosition("goldbonusManager_2");//买卖交易
		if(GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery == null){//外置
			/*****测试外置直接进入买卖页面 签约接口返回空指针异常 再次调用签约接口******/
			/** 签约标示关联判断 **/
			outLayFlag = 1;
			getHttpTools()
					.requestHttp(GoldBonus.PSNGOLDBONUSSIGNINFOQUERY,
							"requestPsnGoldBonusSignInfoQueryCallBack",
							null, false);
		}else{//登录后
			linkAccFlag = (String) GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery.get("linkAcctFlag");
			if("2".equals(linkAccFlag)){//签约未关联
				Intent intent = new Intent(this, GoldbounsReminderActivity.class);
				intent.putExtra("title", "买卖交易");
				startActivity(intent);
				return;
			}
			GoldBonusTask task = GoldBonusTask.getInstance(this);
			registActivityEvent(task);
			task.doTask(new IAction() {
				@Override
				public void SuccessCallBack(Object param) {
					init();
					requestCommConversationId();
				}
			},null);
		}
	}
	/**
	 * 签约接口回调
	 * @param resultObj
	 */
	public void requestPsnGoldBonusSignInfoQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery = resultMap;
		linkAccFlag = (String) resultMap.get("linkAcctFlag");
		accIdOutLay = (String) resultMap.get("accountId");
		if("2".equals(linkAccFlag)){//签约未关联
			Intent intent = new Intent(this, GoldbounsReminderActivity.class);
			intent.putExtra("title", "买卖交易");
			startActivity(intent);
			return;
		}
		GoldBonusTask task = GoldBonusTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {
			@Override
			public void SuccessCallBack(Object param) {
				init();
				requestCommConversationId();
			}
			
		},null);
//		requestCommConversationId();
	}

	private void init() {
		final View view = LayoutInflater.from(this).inflate(R.layout.goldbonus_busitrade_main, null);
		this.getBackgroundLayout().addView(view);
		tvReference = (TextView) view.findViewById(R.id.tv_cankao);
		rg_tracode = (RadioGroup) view.findViewById(R.id.rg_tracode);
		rg_tracode.setOnCheckedChangeListener(rgTracodeOnClick);
		rg_issueType = (RadioGroup) view.findViewById(R.id.rg_issuetype);
		rg_issueType.setOnCheckedChangeListener(rgIssueTypeOnClick);
		rbBuy = (RadioButton) view.findViewById(R.id.rb_buy);
		rbSell = (RadioButton) view.findViewById(R.id.rb_sell);
		rbImmediately = (RadioButton) view.findViewById(R.id.rb_immediately);
		rbReserve = (RadioButton) view.findViewById(R.id.rb_reserve);
		tvSellType = (TextView) view.findViewById(R.id.tv_sell_issuetype);
		et_InputNum = (EditText) view.findViewById(R.id.et_inputnum);
		ll_Balance = (LinearLayout) view.findViewById(R.id.ll_balance);
		ll_AmountCankao = (LinearLayout) view.findViewById(R.id.ll_amount_cankao);
		ll_CankaoInfo = (LinearLayout) view.findViewById(R.id.ll_cankaoinfo);
		buyprice = (TextView) findViewById(R.id.prms_price_listiterm1_buyprice);
		sellprice = (TextView) findViewById(R.id.prms_price_listiterm1_saleprice);
		accNO = (TextView) findViewById(R.id.tv_accno);
		proName = (TextView) findViewById(R.id.goldbonus_account_cash_type);
		ll_rate_current = (LinearLayout) findViewById(R.id.ll_rate_current);
		rateCurrent = (TextView) findViewById(R.id.goldbonus_rate_current);
		balance = (TextView) findViewById(R.id.goldbonus_account_balance);
		saleAmt = (TextView) findViewById(R.id.goldbonus_cankao_amount);
		sc_price = (ScrollView) view.findViewById(R.id.sc_price);
		sc_busiDetail = (ScrollView) view.findViewById(R.id.sc_busidetail);
//		et_InputNum.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//
//				if(!et_InputNum.getText().toString().trim().equals("") && "1".equals(busiFlag)){
//					if(et_InputNum.getText().toString().trim().equals("0") || 
//							Long.valueOf(et_InputNum.getText().toString().trim()) == 0 ||
//							et_InputNum.getText().toString().trim().startsWith("0") ||
//							"2".equals(issueTypeFlag)){
//						return;
//					}
//					ll_AmountCankao.setVisibility(View.VISIBLE);
//					//调金额试算接口
//					requestPsnGoldBonusTradeChargeTrial();
//				}else{
//					ll_AmountCankao.setVisibility(View.GONE);
//				}
//			}
//
//
//		});
		/***********************************************************************************************************/
		View root =  this.findViewById(R.id.rltotal);
		root.setOnTouchListener(touchListener);
		sc_price.setOnTouchListener(touchListener);
		sc_busiDetail.setOnTouchListener(touchListener);
		
		/***********************************************************************************************************/
		//						et_InputNum.setOnEditorActionListener(new EditText.OnEditorActionListener() {
		//				
		//						    @Override
		//						    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		//						        if (actionId == EditorInfo.IME_ACTION_DONE) {
		//						            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		//						            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		//						            if(!et_InputNum.getText().toString().trim().equals("") && "1".equals(busiFlag)){
		//										if(et_InputNum.getText().toString().trim().equals("0") || 
		//												Integer.valueOf(et_InputNum.getText().toString().trim()) == 0){
		//											 return false;
		//										}
		//										ll_AmountCankao.setVisibility(View.VISIBLE);
		//										//调金额试算接口
		//										requestPsnGoldBonusTradeChargeTrial();
		//									}else{
		//										ll_AmountCankao.setVisibility(View.GONE);
		//									}
		//						            return true;  
		//						        }
		//						        return false;
		//						    }
		//						});


		btnNext = (Button) view.findViewById(R.id.btnnext);
		btnNext.setOnClickListener(nextListener);
		//		// 调用登陆后牌价行情查询
		//		getHttpTools().requestHttp(
		//				GoldBonus.PSNGOLDBONUSPRICELISTQUERYLOGIN,
		//				"requestPsnGoldBonusPriceListQueryCallBack", null, false);

		// 调用登陆后牌价行情查询，此接口7秒刷新一次，上半部分数据实时更新
		goldBonusPriceListPollingQuery();
		accNO.setText(StringUtil
				.getForSixForString((String) GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery
						.get(GoldBonus.ACCOUNTNUM))+" "+DictionaryData.getKeyByValue((String)GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery.get("acctType"),
								DictionaryData.goldbonusAcctTypeList));
		if(1 != outLayFlag){
			balance.setText("人民币元  "+StringUtil.parseStringPattern(GoldbonusLocalData.getInstance().availbalance,2));
			if(GoldbonusLocalData.getInstance().availbalance == null || sellprice.getText().toString() == null){
				tvReference.setText("您的最大可买入数量为 0 克");
				return;
			}
		}
		if(rbBuy.isChecked() && GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery != null){
			double weight = (Double.valueOf(GoldbonusLocalData.getInstance().availbalance))/(Double.valueOf((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE)));
			BigDecimal bigdecimal = new BigDecimal(weight);
			String weightStr = StringUtil.deleateNumber((bigdecimal.toString()));
			tvReference.setText("您的最大可买入数量为 "+ StringUtil.parseStringPattern(weightStr, 0) +" 克");
		}
//		AccFlag = this.getIntent().getIntExtra(GoldBonus.PASSFLAG, 0);
//		if(AccFlag == REQUEST_LOGIN_CODE_SELL){//账户管理模块进入买卖交易状态
//			rbSell.setChecked(true);
//			rg_tracode.setOnCheckedChangeListener(rgTracodeOnClick);
//		}
//		//定投管理进入买卖交易状态
//		FixInvestIssueType = this.getIntent().getStringExtra("issueType");
//		if(FixInvestIssueType != null && GoldbonusLocalData.FIXINVESTINTENTFLAG.equals(FixInvestIssueType)){
//			rbReserve.setChecked(true);
//			rg_issueType.setOnCheckedChangeListener(rgIssueTypeOnClick);
//		}
//		requestCommConversationId();
		btnNext.setOnClickListener(nextListener);
	}
	/**
	 * 交易数量 输入框 金额试算 接口调用
	 */
	OnTouchListener touchListener = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			InputMethodManager imm = (InputMethodManager) BusiTradeAvtivity.this
				    .getSystemService(Context.INPUT_METHOD_SERVICE);
			 imm.hideSoftInputFromWindow(et_InputNum.getWindowToken(), 0);
//			 imm.hideSoftInputFromWindow(et_InputNum.getWindowToken(), 0);
			 inputNumStr = et_InputNum.getText().toString().trim();
			 sellpriceStr = sellprice.getText().toString().trim();
			if(!et_InputNum.getText().toString().trim().equals("") && "1".equals(busiFlag)){
				if(et_InputNum.getText().toString().trim().equals("0") || 
						Long.valueOf(et_InputNum.getText().toString().trim()) == 0 ||
						et_InputNum.getText().toString().trim().startsWith("0") ||
						"2".equals(issueTypeFlag)){
					ll_AmountCankao.setVisibility(View.GONE);
					return false;
				}
				ll_AmountCankao.setVisibility(View.VISIBLE);
				/*if(!inputNumStr.equals(inputNumStrTwo)){
					//调金额试算接口
					requestPsnGoldBonusTradeChargeTrial();
				}*/
				//调金额试算接口(连续两次试算金额不同 则继续调用接口,数量不同或者牌价不同时)
				if(!(inputNumStr.equals(inputNumStrTwo) && sellpriceStr.equals(sellpriceStrTwo))){
					requestPsnGoldBonusTradeChargeTrial();
				}
			}else{
				ll_AmountCankao.setVisibility(View.GONE);
			}
			return false;
		}
	};
	
	/**
	 * 买卖交易金额试算接口请求
	 */
	private void requestPsnGoldBonusTradeChargeTrial() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod("PsnGoldBonusTradeChargeTrial");
		reqTrialParamMap.put("weight", et_InputNum.getText().toString().trim());
		reqTrialParamMap.put("saleType", "1".equals(busiFlag) ? "0" : "1"); // 0买 1卖
		if("1".equals(busiFlag)){//买
			reqTrialParamMap.put("xpadgBuyPrice", (String) GoldbonusLocalData.getInstance().bankSellPrice);
		}else{
			reqTrialParamMap.put("tranPrice", (String) GoldbonusLocalData.getInstance().BankBuyPrice);
		}
		biiRequestBody.setParams(reqTrialParamMap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnGoldBonusTradeChargeTrialCallBack");
	}
	/**
	 * 买卖交易金额试算接口请求  回调
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnGoldBonusTradeChargeTrialCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> responseList = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = responseList.get(0);
		Map<String,Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		Reserve_Immediately = false;
//		saleAmtChange = !StringUtil.parseStringPattern((String)result.get("saleAmt"),2).equals(saleAmtLast);
		inputNumStrTwo = et_InputNum.getText().toString().trim();
		sellpriceStrTwo = sellprice.getText().toString().trim();
		saleAmt.setText("约折合人民币元   "+StringUtil.parseStringPattern((String)result.get("saleAmt"),2)+" ，具体金额以实际成交为准。");
//		saleAmtLast = StringUtil.parseStringPattern((String)result.get("saleAmt"),2);
	}

	/** 7秒轮询 */
	private void goldBonusPriceListPollingQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(GoldBonus.PSNGOLDBONUSPRICELISTQUERYLOGIN);
		HttpManager.requestPollingBii(biiRequestBody, pollingHandler,
				ConstantGloble.FOREX_REFRESH_TIMES);
	}

	// 登陆后排挤行情查询回调
	@SuppressWarnings("unchecked")
	public void requestPsnGoldBonusPriceListQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}

		GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery = resultMap;
		/**金额格式**/
		//		buyprice.setText(StringUtil.parseStringPattern2(
		//				(String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.BIDPRICE), 2));
		//		sellprice.setText(StringUtil.parseStringPattern2(
		//				(String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE), 2));
		/**牌价格式  去尾0   为0显示 -**/
		buyprice.setText(paseEndZero(
				"0".equals((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.BIDPRICE)) ||
				    "0.00".equals((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.BIDPRICE)) ? "-" : 
					       (String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.BIDPRICE)));
		sellprice.setText(paseEndZero(
				"0".equals((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE)) ||
					"0.00".equals((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE)) ? "-" :
						  (String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE)));
		// 平判断涨或跌 大于0表示涨
		if ((Double.parseDouble((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.UPDOWN))) > 0) {
			buyprice.setTextColor(Color.RED);
			sellprice.setTextColor(Color.RED);
		} else if ((Double.parseDouble((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.UPDOWN))) < 0) {
			//			buyprice.setTextColor(Color.GREEN);
			//			sellprice.setTextColor(Color.GREEN);
			buyprice.setTextColor(this.getResources().getColor(R.color.greens));
			sellprice.setTextColor(this.getResources().getColor(R.color.greens));
		} else {
			buyprice.setTextColor(Color.BLACK);
			sellprice.setTextColor(Color.BLACK);
		}
//		if("0".equals((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE))){
//			tvReference.setText("您的最大可买入数量为 - 克");
//			return;
//		}
//		double weight = (Double.valueOf(GoldbonusLocalData.getInstance().availbalance))/(Double.valueOf((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE)));
//		BigDecimal bigdecimal = new BigDecimal(weight);
//		String weightStr = StringUtil.deleateNumber((bigdecimal.toString()));
//		tvReference.setText("您的最大可买入数量为 "+ StringUtil.parseStringPattern(weightStr, 0) +" 克");

	}

	private Handler pollingHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			// http状态码
			String resultHttpCode = (String) ((Map<String, Object>) msg.obj)
					.get(ConstantGloble.HTTP_RESULT_CODE);
			// 返回数据
			Object resultObj = ((Map<String, Object>) msg.obj)
					.get(ConstantGloble.HTTP_RESULT_DATA);
			// 回调对象
			HttpObserver callbackObject = (HttpObserver) ((Map<String, Object>) msg.obj)
					.get(ConstantGloble.HTTP_CALLBACK_OBJECT);
			// 回调方法
			String callBackMethod = (String) ((Map<String, Object>) msg.obj)
					.get(ConstantGloble.HTTP_CALLBACK_METHOD);

			switch (msg.what) {
			case ConstantGloble.HTTP_STAGE_CONTENT:
				// 执行全局前拦截器
				if (BaseDroidApp.getInstanse()
						.httpRequestCallBackPre(resultObj)) {
					break;
				}
				// 执行callbackObject回调前拦截器
				if (httpRequestCallBackPre(resultObj)) {
					break;
				}
				// 清空更新时间
				// clearTimes();

				BiiResponse biiResponse = (BiiResponse) ((Map<String, Object>) msg.obj)
						.get(ConstantGloble.HTTP_RESULT_DATA);
				List<BiiResponseBody> biiResponseBodys = biiResponse
						.getResponse();
				BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
				Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
						.getResult();
				if (StringUtil.isNullOrEmpty(resultMap)) {
					return;
				}

				GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery = resultMap;
				GoldbonusLocalData.getInstance().BankBuyPrice = StringUtil
						.parseStringPattern2(
								(String) resultMap.get(GoldBonus.BIDPRICE), 2);
				GoldbonusLocalData.getInstance().bankSellPrice = StringUtil
						.parseStringPattern2(
								(String) resultMap.get(GoldBonus.ASKPRICE), 2);
				//				buyprice.setText(StringUtil.parseStringPattern2(
				//						(String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.BIDPRICE), 2));
				//				sellprice.setText(StringUtil.parseStringPattern2(
				//						(String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE), 2));
				/**牌价格式  去尾0   为0显示 -**/
				buyprice.setText(paseEndZero(
						"0".equals((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.BIDPRICE)) ||
						    "0.00".equals((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.BIDPRICE)) ? "-" : 
							       (String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.BIDPRICE)));
				sellprice.setText(paseEndZero(
						"0".equals((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE)) ||
							"0.00".equals((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE)) ? "-" :
								  (String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE)));
				// 平判断涨或跌 大于0表示涨
				if ((Double.parseDouble((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.UPDOWN))) > 0) {
					buyprice.setTextColor(Color.RED);
					sellprice.setTextColor(Color.RED);
				} else if ((Double.parseDouble((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.UPDOWN))) < 0) {
					buyprice.setTextColor(BusiTradeAvtivity.this.getResources().getColor(R.color.greens));
					sellprice.setTextColor(BusiTradeAvtivity.this.getResources().getColor(R.color.greens));
				} else {
					buyprice.setTextColor(Color.BLACK);
					sellprice.setTextColor(Color.BLACK);
				}

				// 执行callbackObject回调后拦截器
				if (httpRequestCallBackAfter(resultObj)) {
					break;
				}

				// 执行全局后拦截器
				if (BaseDroidApp.getInstanse().httpRequestCallBackAfter(
						resultObj)) {
					break;
				}
				break;
			case ConstantGloble.HTTP_STAGE_CODE:
				// 执行code error 全局前拦截器
				if (BaseDroidApp.getInstanse().httpCodeErrorCallBackPre(
						resultHttpCode)) {
					break;
				}
				// 执行callbackObject error code 回调前拦截器
				if (callbackObject.httpCodeErrorCallBackPre(resultHttpCode)) {
					break;
				}
				Method httpCodeCallbackMethod = null;
				try {
					// 回调
					httpCodeCallbackMethod = callbackObject.getClass()
							.getMethod(callBackMethod, String.class);
					httpCodeCallbackMethod.invoke(callbackObject,
							resultHttpCode);
				} catch (SecurityException e) {
					LogGloble.e(TAG, "SecurityException ", e);
				} catch (NoSuchMethodException e) {
					LogGloble.e(TAG, "NoSuchMethodException ", e);
				} catch (IllegalArgumentException e) {
					LogGloble.e(TAG, "IllegalArgumentException ", e);
				} catch (IllegalAccessException e) {
					LogGloble.e(TAG, "IllegalAccessException ", e);
				} catch (InvocationTargetException e) {
					LogGloble.e(TAG, "InvocationTargetException ", e);
				} catch (NullPointerException e) {
					LogGloble.e(TAG, "NullPointerException ", e);
					throw e;
				} catch (ClassCastException e) {
					LogGloble.e(TAG, "ClassCastException ", e);
					throw e;
				}
				// 执行code error 全局后拦截器
				if (BaseDroidApp.getInstanse().httpCodeErrorCallBackAfter(
						resultHttpCode)) {
					break;
				}
				// 执行callbackObject code error 后拦截器
				if (callbackObject.httpCodeErrorCallBackAfter(resultHttpCode)) {
					break;
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPsnGoldBonusProductInfoQuery(0,true);
	}
	/**
	 * 贵金属积利产品信息查询    活期数据
	 * @param currentInex  当前页
	 * @param isRefresh  是否刷新
	 */
	private void requestPsnGoldBonusProductInfoQuery(int currentInex,  final boolean isRefresh) {
		reqInfoQueryMap.put("xpadgPeriodType", "0");//0 活期  1 定期 
		reqInfoQueryMap.put("ordType", "");
		reqInfoQueryMap.put("currentIndex", Integer.toString(currentInex));
		reqInfoQueryMap.put("pageSize", "10");
		reqInfoQueryMap.put("_refresh", Boolean.toString(isRefresh));
		String conversationId = isRefresh ? (String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID) : null ;
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusProductInfoQuery", reqInfoQueryMap, conversationId,new IHttpResponseCallBack<Map<String,Object>>(){

			@SuppressWarnings("unchecked")
			@Override	
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					ll_rate_current.setVisibility(View.GONE);
					return;
				}
				GoldbonusLocalData.getInstance().ProductInfoQueryList = (List<Map<String,Object>>)(result.get("list"));//存储返回数据
				List<Map<String,Object>> list = (List<Map<String,Object>>)(result.get("list"));
				if (list == null || list.size() == 0) {
					ll_rate_current.setVisibility(View.GONE);
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							BusiTradeAvtivity.this
							.getString(R.string.acc_transferquery_null));
					return;
				}
				proName.setText((String) list.get(0).get("issueName"));
				issueNoStr = (String) list.get(0).get("issueNo");
				ll_rate_current.setVisibility(View.VISIBLE);
				rateCurrent.setText("（当前年化利率 "+ paseEndZero((String) list.get(0).get("issueRate")) +"%）");
				AccFlag = BusiTradeAvtivity.this.getIntent().getIntExtra(GoldBonus.PASSFLAG, 0);
				if(AccFlag == REQUEST_LOGIN_CODE_SELL){//账户管理模块进入买卖交易状态
					rbSell.setChecked(true);
					rg_tracode.setOnCheckedChangeListener(rgTracodeOnClick);
					
				}
				//定投管理进入买卖交易状态
				FixInvestIssueType = BusiTradeAvtivity.this.getIntent().getStringExtra("issueType");
				if(FixInvestIssueType != null && GoldbonusLocalData.FIXINVESTINTENTFLAG.equals(FixInvestIssueType)){
					rbReserve.setChecked(true);
					rg_issueType.setOnCheckedChangeListener(rgIssueTypeOnClick);
				}
				requestPsnAccQuery();
			}
		});
	}

	/**
	 * 公共接口 账户余额（合并调用，获取账户可用余额）
	 */
	private void requestPsnAccQuery(){
		// 调用公共接口，查询账户余额，上送accountId,不要conversion
		Map<String, Object> ParamMapthis = new HashMap<String, Object>();
		if(StringUtil.isNullOrEmpty(GoldbonusLocalData.getInstance().accountIdOld) && StringUtil.isNullOrEmpty(accIdOutLay)){//登记账户后进入
			ParamMapthis.put(GoldBonus.ACCOUNTID, GoldbonusLocalData.getInstance().accountIdBusi);
		}else{
			ParamMapthis.put(GoldBonus.ACCOUNTID, 
					StringUtil.isNullOrEmpty(GoldbonusLocalData.getInstance().accountIdOld) ? accIdOutLay : GoldbonusLocalData.getInstance().accountIdOld);
		}
		getHttpTools().requestHttp(GoldBonus.PSNACCOUNTQUERYACCOUNTDETAIL,
				"requestPsnAccountQueryAccountDetailCallBack", ParamMapthis,
				false);
	}
	/**
	 * 公共接口  查询账户可用余额
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnAccountQueryAccountDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		List<Map<String, Object>> AccDetailList = (List<Map<String, Object>>) resultMap
				.get("accountDetaiList");
		for (int i = 0; i < (AccDetailList).size(); i++) {
			if (AccDetailList.get(i).get("currencyCode").equals("001")) {
				GoldbonusLocalData.getInstance().availbalance = (String) AccDetailList
						.get(i).get("availableBalance");
			}
		}
		// 如果账户没有人民币余额？？？？未作判断
		if (StringUtil.isNull(GoldbonusLocalData.getInstance().availbalance)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					BusiTradeAvtivity.this.getString(R.string.goldbonus_no_rmb));
			balance.setText("人民币元  "+" (无子账户)");
		} else {
			if(AccFlag != REQUEST_LOGIN_CODE_SELL){//账户管理模块进入买卖交易状态
				balance.setText("人民币元  "+StringUtil.parseStringPattern2(
						GoldbonusLocalData.getInstance().availbalance, 2));
			}
			
			if(rbBuy.isChecked() && GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery != null){
				double weight = (Double.valueOf(GoldbonusLocalData.getInstance().availbalance))/(Double.valueOf((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE)));
				BigDecimal bigdecimal = new BigDecimal(weight);
				String weightStr = StringUtil.deleateNumber((bigdecimal.toString()));
				tvReference.setText("您的最大可买入数量为 "+ StringUtil.parseStringPattern(weightStr, 0) +" 克");
			}
		}
		//贵金属积利金账户查询接口
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("ordType", "");
		param.put("currentIndex", "0");
		param.put("pageSize", "10");
		param.put("_refresh", "true");
		this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusAccountQuery", param,
				(String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID), 
				new IHttpResponseCallBack<Map<String,Object>>(){

			@Override	
			public void httpResponseSuccess(Map<String,Object> result, String method) {
				BaseHttpEngine.dissMissProgressDialog();
				if (StringUtil.isNullOrEmpty(result)) {
					return;
				}
				GoldbonusLocalData.getInstance().PsnGoldBonusAccountQuery = result;
				rg_tracode.setOnCheckedChangeListener(rgTracodeOnClick);
			}
		});
	}

	/**买卖方向**/
	private OnCheckedChangeListener rgTracodeOnClick = new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_buy://买卖方向--->买
				busiFlag = "1";
				et_InputNum.setText("");
				balance.setText("人民币元  "+StringUtil.parseStringPattern(GoldbonusLocalData.getInstance().availbalance,2));
				//				balance.setTextColor(getResources().getColor(R.color.red));
				ll_rate_current.setVisibility(View.VISIBLE);
				rg_issueType.setVisibility(View.VISIBLE);
				tvSellType.setVisibility(View.GONE);
				rbImmediately.setChecked(true);
				if(GoldbonusLocalData.getInstance().availbalance == null || sellprice.getText().toString() == null){
					tvReference.setText("您的最大可买入数量为 0 克");
					return;
				}
				if(GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery != null){
					if("0".equals((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE)) || "-".equals(sellprice.getText().toString())){
						tvReference.setText("您的最大可买入数量为 - 克");
						return;
					}
					double weight = (Double.valueOf(GoldbonusLocalData.getInstance().availbalance))/(Double.valueOf((String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE)));
					BigDecimal bigdecimal = new BigDecimal(weight);
					String weightStr = StringUtil.deleateNumber((bigdecimal.toString()));
					tvReference.setText("您的最大可买入数量为 "+ StringUtil.parseStringPattern(weightStr, 0) +" 克");
				}
				if(!et_InputNum.getText().toString().trim().equals("")){
					ll_AmountCankao.setVisibility(View.VISIBLE);
				}
//				goldBonusPriceListPollingQuery();
				break;
			case R.id.rb_sell://买卖方向--->卖
				busiFlag = "2";
				issueTypeFlag = "1";
				et_InputNum.setText("");
				ll_rate_current.setVisibility(View.GONE);
				rg_issueType.setVisibility(View.GONE);
				tvSellType.setVisibility(View.VISIBLE);
				ll_CankaoInfo.setVisibility(View.VISIBLE);
				ll_AmountCankao.setVisibility(View.GONE);
				ll_Balance.setVisibility(View.VISIBLE);
				/**设置交易方式 避免选交易方式后再选择卖出时 未清除交易方式的选择状态**/
				rbReserve.setChecked(false);
				if(!StringUtil.isNullOrEmpty(GoldbonusLocalData.getInstance().PsnGoldBonusAccountQuery) && 
						!StringUtil.isNullOrEmpty(GoldbonusLocalData.getInstance().ProductInfoQueryList)){//非外置进入
					balance.setText((String)GoldbonusLocalData.getInstance().ProductInfoQueryList.get(0).get("issueName") +" "+StringUtil.parseStringPattern(StringUtil.deleateNumber((String)GoldbonusLocalData.getInstance()
							.PsnGoldBonusAccountQuery.get("balance")),0)+" 克");
					balance.setTextColor(getResources().getColor(R.color.black));
					tvReference.setText("您的最大可卖出数量为  "+ StringUtil.parseStringPattern(StringUtil.deleateNumber((String)GoldbonusLocalData.getInstance()
							.PsnGoldBonusAccountQuery.get("balance")),0) +" 克");
				}else{//外置登录直接进入买卖
					//贵金属积利金账户查询接口
					Map<String,Object> param = new HashMap<String, Object>();
					param.put("ordType", "");
					param.put("currentIndex", "0");
					param.put("pageSize", "10");
					param.put("_refresh", "true");
					BusiTradeAvtivity.this.getHttpTools().requestHttpWithConversationId("PsnGoldBonusAccountQuery", param,
							(String)BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID), 
							new IHttpResponseCallBack<Map<String,Object>>(){

						@Override	
						public void httpResponseSuccess(Map<String,Object> result, String method) {
							BaseHttpEngine.dissMissProgressDialog();
							if (StringUtil.isNullOrEmpty(result)) {
								return;
							}
							GoldbonusLocalData.getInstance().PsnGoldBonusAccountQuery = result;
//							rg_tracode.setOnCheckedChangeListener(rgTracodeOnClick);
							balance.setText((String)GoldbonusLocalData.getInstance().ProductInfoQueryList.get(0).get("issueName") +" "+StringUtil.parseStringPattern(StringUtil.deleateNumber((String)GoldbonusLocalData.getInstance()
									.PsnGoldBonusAccountQuery.get("balance")),0)+" 克");
							balance.setTextColor(getResources().getColor(R.color.black));
							tvReference.setText("您的最大可卖出数量为  "+ StringUtil.parseStringPattern(StringUtil.deleateNumber((String)GoldbonusLocalData.getInstance()
									.PsnGoldBonusAccountQuery.get("balance")),0) +" 克");
						}
					});
				}
				break;
			}
		}
	};
	/**交易方式**/
	private OnCheckedChangeListener rgIssueTypeOnClick = new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_immediately://立即执行
				ll_Balance.setVisibility(View.VISIBLE);
				ll_CankaoInfo.setVisibility(View.VISIBLE);
				issueTypeFlag = "1";
//				et_InputNum.setText("");
				//从定投预约点击立即执行  调用金额金额试算 逻辑
				inputNumStrThr = et_InputNum.getText().toString().trim();
				if(!inputNumStrThr.equals("")){
					if(et_InputNum.getText().toString().trim().equals("0") || 
							Long.valueOf(et_InputNum.getText().toString().trim()) == 0 ||
							et_InputNum.getText().toString().trim().startsWith("0")){
						ll_AmountCankao.setVisibility(View.GONE);
						return ;
					}
					ll_AmountCankao.setVisibility(View.VISIBLE);
//					if(Reserve_Immediately && (!inputNumStrThr.equals(inputNumStrTwo) || !inputNumStrThr.equals(inputNumStr))){
//						//调金额试算接口
//						requestPsnGoldBonusTradeChargeTrial();
//					}
					if(Reserve_Immediately){//随牌价信息更新 时时更新试算信息
						//调金额试算接口
						requestPsnGoldBonusTradeChargeTrial();
					}
				}
				break;
			case R.id.rb_reserve://定投预约
				if(rbReserve.isChecked()){
					Reserve_Immediately = true;
					issueTypeFlag = "2";
//					et_InputNum.setText("");
					ll_Balance.setVisibility(View.GONE);
					ll_CankaoInfo.setVisibility(View.GONE);
					ll_AmountCankao.setVisibility(View.GONE);
				}
				break;

			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		if ("1".equals(linkAccFlag)) {
			HttpManager.stopPolling();
		}
	}
	/**返回键处理事件**/
	OnClickListener backClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	};
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		GoldbonusLocalData.cleanInstance();
	}
	
	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		BaseHttpEngine.dissMissProgressDialog();
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodyList.get(0);
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {
				if (GoldBonus.PSNGOLDBONUSPRICELISTQUERYLOGIN
						.equals(biiResponseBody.getMethod())) {//牌价行情接口
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null && biiError.getCode() != null) {

						if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
							return super.doBiihttpRequestCallBackPre(response);
						}
						if (biiError.getCode().equals("XPADG.EG051")
								|| biiError.getCode().equals("XPADG.EG052")) {
							buyprice.setText("-");
							sellprice.setText("-");
							buyprice.setTextColor(Color.BLACK);
							sellprice.setTextColor(Color.BLACK);
							// 第一次报错显示报错信息对话框，之后不显示
							if (isfirstError) {
								isfirstError = false;
								return super.doBiihttpRequestCallBackPre(response);
							}
							if(rbBuy.isChecked()){
								tvReference.setText("您的最大可买入数量为 - 克");
							}
						}
						if (StringUtil.isNullOrEmpty(GoldbonusLocalData
								.getInstance().PsnGoldBonusPriceListQuery)) {
							buyprice.setText("-");
							sellprice.setText("-");
							buyprice.setTextColor(Color.BLACK);
							sellprice.setTextColor(Color.BLACK);
						}
						return true;
					}
				}
				else if (GoldBonus.PSNCOMMONQUERYFILTEREDACCOUNTS.equals(biiResponseBody.getMethod()) || 
						"PsnAccountQueryAccountDetail".equals(biiResponseBody.getMethod())) {//查帐户人民币余额报错信息
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null && biiError.getCode() != null) {
						if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
							return super.doBiihttpRequestCallBackPre(response);
						}
						if (biiError.getCode().equals("AccQueryDetailAction.NoSubAccount")) {
							//这个报错屏蔽，并显示
							balance.setText(getResources().getString(R.string.goldbonus_account_rmb_yuan)+" (无子账户)");
							tvReference.setText("您的最大可买入数量为 - 克");
							return true;
						}else {
							//其他报错不屏蔽，显示-
							balance.setText(getResources().getString(R.string.goldbonus_account_rmb_yuan)+" -");
							tvReference.setText("您的最大可买入数量为 - 克");
							return super.doBiihttpRequestCallBackPre(response);
						}
						
					}else {
						return super.doBiihttpRequestCallBackPre(response);
					}
				}
				else if("PsnGoldBonusTradeChargeTrial".equals(biiResponseBody.getMethod())){//屏蔽金额试算信息
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					if (biiError != null && biiError.getCode() != null) {
						saleAmt.setText("约折合人民币元 -，具体金额以实际成交为准。");
						return true;
					}
				}
			}

		} 
		else {
			return super.doBiihttpRequestCallBackPre(response);
		}

		return super.doBiihttpRequestCallBackPre(response);
	}
	
	/**
	 * 下一步监听事件
	 */
	OnClickListener nextListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if("".equals(et_InputNum.getText().toString().trim())){
				BaseDroidApp.getInstanse().showInfoMessageDialog("请输入交易数量"); return;
			}
//			if(et_InputNum.getText().toString().trim().startsWith("0")){
//				BaseDroidApp.getInstanse().showInfoMessageDialog("交易数量只能为大于0的整数"); return;
//			}
			if(et_InputNum.getText().toString().trim().startsWith("0") || Long.valueOf(et_InputNum.getText().toString()) > 3000){
				BaseDroidApp.getInstanse().showInfoMessageDialog("交易数量应为整数，且不应低于1克或高于3000克");
				return;
			}
			if(rbSell.isChecked()){//卖出时 数量比对
				if(Long.valueOf(et_InputNum.getText().toString()) > Long
						.valueOf(StringUtil.deleateNumber((String)GoldbonusLocalData.getInstance()
								.PsnGoldBonusAccountQuery.get("balance")))){//输入数量不可大于活期余额
					BaseDroidApp.getInstanse().showInfoMessageDialog("交易数量不能大于可用余额");
					return;
				}
				/**新增立即执行的提示信息 ---下一步时 判断**/
				if("-".equals(buyprice.getText().toString())){
					BaseDroidApp.getInstanse().showInfoMessageDialog("无交易牌价信息，暂不可进行立即执行的贵金属积利产品买卖。感谢您的使用。");
					return;
				}
				
			}
			if(rbReserve.isChecked()){//定投预约
				Intent intent = new Intent(BusiTradeAvtivity.this, FixInvestSignActivity.class);
				intent.putExtra("weight", et_InputNum.getText().toString().trim());
				startActivity(intent);
			}else{
				/**新增立即执行的提示信息 ---下一步时 判断**/
				if((rbBuy.isChecked() && "-".equals(sellprice.getText().toString())) || 
						(rbSell.isChecked() && "-".equals(buyprice.getText().toString()))){//卖出时判断买入价  买入时判断卖出价
					BaseDroidApp.getInstanse().showInfoMessageDialog("无交易牌价信息，暂不可进行立即执行的贵金属积利产品买卖。感谢您的使用。");
					return;
				}
//				if(balance.getText().toString().contains("无子账户") || balance.getText().toString().contains("-")){
//					BaseDroidApp.getInstanse().showInfoMessageDialog(
//							BusiTradeAvtivity.this.getString(R.string.goldbonus_no_rmb)); return;
//				}
				Intent intent = new Intent(BusiTradeAvtivity.this, BusiTradeDetailActivity.class);
				intent.putExtra("weight", et_InputNum.getText().toString().trim());
				intent.putExtra("busiFlag", busiFlag);
				//					intent.putExtra("buyprice", StringUtil.parseStringPattern2(
				//							(String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.BIDPRICE), 2));
				//					intent.putExtra("sellprice", StringUtil.parseStringPattern2(
				//							(String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE), 2));
//				intent.putExtra("buyprice", paseEndZero(
//						(String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.BIDPRICE)));
//				intent.putExtra("sellprice", paseEndZero(
//						(String) GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery.get(GoldBonus.ASKPRICE)));
				intent.putExtra("buyprice", buyprice.getText().toString());
				intent.putExtra("sellprice", sellprice.getText().toString());
				intent.putExtra("issueTypeFlag", issueTypeFlag);
				intent.putExtra("proName", proName.getText().toString());//产品名称
				intent.putExtra("issueNo", issueNoStr);
				startActivity(intent);
			}
		}
		
	};
	
	/**
	 * 点击键盘完成键  事件监听  调金额试算信息接口
	 */
	@Override
	public boolean dispatchKeyEvent(android.view.KeyEvent event) {
		 if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){  
	            /*隐藏软键盘*/  
	            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	            if(inputMethodManager.isActive()){  
	                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);  
	            }  
	            if(!et_InputNum.getText().toString().trim().equals("") && "1".equals(busiFlag)){
					if(et_InputNum.getText().toString().trim().equals("0") || 
							Long.valueOf(et_InputNum.getText().toString().trim()) == 0 ||
							et_InputNum.getText().toString().trim().startsWith("0") ||
							"2".equals(issueTypeFlag)){
						ll_AmountCankao.setVisibility(View.GONE);
						return false;
					}
					ll_AmountCankao.setVisibility(View.VISIBLE);
					/*if(!inputNumStr.equals(inputNumStrTwo)){
						//调金额试算接口
						requestPsnGoldBonusTradeChargeTrial();
					}*/
					//调金额试算接口  随牌价信息时时试算金额
					requestPsnGoldBonusTradeChargeTrial();
					return true;
				}else{
					ll_AmountCankao.setVisibility(View.GONE);
					return false;
				}
		 };
		 return super.dispatchKeyEvent(event);
	}
	
}
