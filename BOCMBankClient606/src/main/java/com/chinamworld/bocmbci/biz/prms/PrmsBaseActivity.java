package com.chinamworld.bocmbci.biz.prms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.BaseRUtil;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.biz.prms.control.PrmsControl;
import com.chinamworld.bocmbci.biz.prms.myaccount.PrmsAccActivity401;
import com.chinamworld.bocmbci.biz.prms.myaccount.PrmsAccMeneActivity;
import com.chinamworld.bocmbci.biz.prms.price.PrmsPricesActivity;
import com.chinamworld.bocmbci.biz.prms.query.PrmsQueryActivity;
import com.chinamworld.bocmbci.biz.prms.query.PrmsQueryDealActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.server.LocalDataService;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 贵金属交易目录的基类
 * 
 * @author xyl
 * 
 */
public class PrmsBaseActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "PrmsBaseActivity";
	protected ActivityTaskManager activityTaskManager = ActivityTaskManager
			.getInstance();
	protected boolean ifLogin = ifLogin();
	/**
	 * 返回按钮
	 */
	protected Button back;
	/**
	 * 右边按钮
	 */
	protected Button right;
	/**
	 * 主页面布局
	 */
	protected LinearLayout tabcontent;
	private  Button btn_show;
	/**
	 * 获取文件
	 */
	protected LayoutInflater mainInflater;

	protected static final PrmsControl prmsControl = PrmsControl.getInstance();

	/**
	 * taskPopCloseButton:任务提示框右上角关闭按钮
	 */
	protected ImageView taskPopCloseButton = null;
	protected View accButtonView = null;
	protected View moneyButtonView = null;
	protected View accTextView = null;
	protected View moneyTextView = null;
	protected ImageView finishPopButton = null;
	protected Button startTrade = null;
	
	private final String  PARITIESTYP = "G";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GetPhoneInfo.initActFirst(this);
		super.onCreate(savedInstanceState);
		baseinit();
	}

	// /**
	// * 完成任务提示框
	// */
	// public void finishPopupWindow() {
	// // 加载任务提示框的布局
	// // popupView:完成任务提示框的布局
	// View popupView = LayoutInflater.from(this).inflate(
	// R.layout.forex_task_finish, null);
	// finishPopButton = (ImageView) popupView
	// .findViewById(R.id.top_right_close);
	// startTrade = (Button) popupView.findViewById(R.id.forex_trade_button);
	// finishPopButton.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// BaseDroidApp.getInstanse().dismissMessageDialog();
	// }
	// });
	// // 开始交易
	// startTrade.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// BaseDroidApp.getInstanse().dismissMessageDialog();
	// }
	// });
	// BaseDroidApp.getInstanse().showAccountMessageDialog(popupView);
	// }

	/**
	 * 获取贵金属交易账户信息
	 */
	protected void queryPrmsAcc() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Prms.QUERY_PRMSACC);
		Map<String, String> map = new HashMap<String, String>();
		// 查询贵金属账户信息
		map.put(Prms.QUERY_PRMSACC_INVTTYPE,
				ConstantGloble.PRMS_ACCSETTIN_INVTTYPE);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "queryPrmsAccCallBack");
	}

	/**
	 * 获取贵金属交易账户信息 回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void queryPrmsAccCallBack(Object resultObj) {
		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// if (biiResponseBody.getResult() == null) {
		// prmsControl.ifhavPrmsAcc = false;
		// prmsControl.accMessage = null;
		// prmsControl.accId = null;
		// } else {
		// prmsControl.accMessage = (Map<String, String>) biiResponseBodys
		// .get(0).getResult();
		// // prmsControl.accId = prmsControl.accMessage
		// // .get(Prms.QUERY_PRMSACC_ACCOUNT);
		// if (StringUtil.isNullOrEmpty(prmsControl.accMessage
		// .get(Prms.QUERY_PRMSACC_ACCOUNT))) {
		// prmsControl.accId = "返回结果为空";
		// } else {
		// prmsControl.accId = prmsControl.accMessage
		// .get(Prms.QUERY_PRMSACC_ACCOUNT);
		// }
		// prmsControl.ifhavPrmsAcc = true;
		// }
	}

	/**
	 * 判断是否登录
	 * 
	 * @Author xyl
	 * @return 是否登录
	 */
	boolean ifLogin() {
		return BaseDroidApp.getInstanse().isLogin();
	}

	/**
	 * 请求查询贵金属详情
	 */
	protected void queryPrmsPrice() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Prms.QUERY_TRADERATE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "queryPrmsPriceCallBack");
	}

	/**
	 * 查询贵金属行情 回调
	 * 
	 * @param resultObj
	 */
	public void queryPrmsPriceCallBack(Object resultObj) {
	}

	/**
	 * @param isToAccBalanceAc 是否要跳转到持仓页面
	 * 查询贵金属账户账户余额 持仓
	 */
	protected void queryPrmsAccBalance(boolean isToAccBalanceAc) {
		isbalance  =isToAccBalanceAc;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Prms.QUERY_PEMSACTBALANCE);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"queryPrmsAccBalanceCallBack");
	}
	protected void queryPrmsAccBalance() {
		queryPrmsAccBalance(false);
	}

	/**
	 * 查询贵金属账户余额回调处理
	 * 
	 * 
	 * @param resultObj
	 */
	public void queryPrmsAccBalanceCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			prmsControl.accBalanceList = new ArrayList<Map<String, Object>>();
		} else {
			prmsControl.accBalanceList = (List<Map<String, Object>>) (biiResponseBody
					.getResult());
		}
		if (isbalance) {
			BaseHttpEngine.dissMissProgressDialog();
			ActivityTaskManager.getInstance().removeAllActivity();
			// Intent intent = new Intent(this, PrmsAccActivity.class);
			Intent intent = new Intent(this, PrmsAccActivity401.class);
			startActivity(intent);
			return;
		}

	}

	/**
	 * 查询可设置为默认账户的账户信息
	 */
	protected void queryPrmsAccs() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		// biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
		// .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Prms.QUERY_PRMSACCS);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "queryPrmsAccsCallBack");
	}

	/**
	 * 
	 * 查询可设置为默认账户的所有账户
	 * 
	 * @param resultObj
	 */
	public void queryPrmsAccsCallBack(Object resultObj) {
	}

	/**
	 * 成交状况查询 带展示 dialog
	 */
	protected void queryPrmsTradeDeale(String startDate, String endDate,
			String currentIndex, String pageSize, String currencyPair,
			Boolean refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Prms.PRMS_QUERY_DEAL);
		Map<String, Object> map = new HashMap<String, Object>();
		// 成交状况查询
		map.put(Prms.PRMS_QUERY_DEAL_QUERYTYPE,
				ConstantGloble.PRMS_QUERY_TYPE_HISTORY);
		map.put(Prms.PRMS_QUERY_DEAL_STARTDATE, startDate);
		map.put(Prms.PRMS_QUERY_DEAL_ENDDATE, endDate);
		map.put(Prms.PRMS_QUERY_DEAL_CURRENTINDEX, currentIndex);
		// map.put(Prms.PRMS_QUERY_DEAL_CURRENCYPAIR, currencyPair);
		map.put(Prms.PRMS_QUERY_DEAL_PAGESIZE, pageSize);
		map.put(Prms.PRMS_QUERY_DEAL_REFRESH, String.valueOf(refresh));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"queryPrmsTradeDealeCallBack");
	}

	/**
	 * 成交状况查询 回调
	 * 
	 * @param resultObj
	 */
	public void queryPrmsTradeDealeCallBack(Object resultObj) {
	}

	/**
	 * 历史委托查询 // 暂时不做
	 */
	protected void queryPrmsTradeEntrustHistory(String startDate,
			String endDate, Integer currentIndex, Integer pageSize,
			String currencyPair, boolean refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		// TODO commConversationId
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Prms.PRMS_QUERY_DEAL);
		Map<String, Object> map = new HashMap<String, Object>();
		// 成交状况查询
		map.put(Prms.PRMS_QUERY_DEAL_QUERYTYPE,
				ConstantGloble.PRMS_QUERY_TYPE_ENTRUST_HISTORY);
		map.put(Prms.PRMS_STARTDATE, startDate);
		map.put(Prms.PRMS_ENDDATE, endDate);
		map.put("currentIndex", currentIndex);
		map.put("currencyPair", currencyPair);
		map.put("pageSize", pageSize);
		map.put("_refresh", String.valueOf(refresh));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"queryPrmsTradeEntrustHistoryCallBack");
	}

	/**
	 * 历史委托查询 回调 // 暂时不做了
	 * 
	 * @param resultObj
	 */
	public void queryPrmsTradeEntrustHistoryCallBack(Object resultObj) {
	}

	/**
	 * 当前有效委托查询 //暂时不做了
	 * 
	 * @param currentIndex
	 * @param pageSize
	 * @param refresh
	 */
	protected void queryPrmsTradeEntrustNow(Integer currentIndex,
			Integer pageSize, boolean refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Prms.PRMS_QUERY_DEAL);
		Map<String, Object> map = new HashMap<String, Object>();
		// 成交状况查询
		map.put(Prms.PRMS_QUERY_DEAL_QUERYTYPE,
				ConstantGloble.PRMS_QUERY_TYPE_ENTRUST_NOW);
		map.put("currentIndex", currentIndex);
		map.put("pageSize", pageSize);
		map.put("_refresh", String.valueOf(refresh));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"queryPrmsTradeEntrustNowCallBack");
	}

	/**
	 * 当前有效委托查询 回调
	 * 
	 * @param resultObj
	 */
	public void queryPrmsTradeEntrustNowCallBack(Object resultObj) {
	}

	/**
	 * 获取单个账户详细信息 根据id
	 */
	protected void queryPrmsAccDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Prms.PRMS_QUERY_ACCOUNT_DETAIL);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Prms.PRMS_QUERY_ACCOUNT_DETAIL_ACCOUNTID, accountId.toString());
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"queryPrmsAccDetailCallBack");
	}

	/**
	 * 获取单个账户详细信息 回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void queryPrmsAccDetailCallBack(Object resultObj) {

	}

	/**
	 * 设置贵金属账户
	 * 
	 * @author xyl
	 * @param accountId
	 *            资金账户id
	 * @param token
	 *            防重机制
	 */
	protected void prmsAccSetting(String accountId, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Prms.PRMS_ACCSETTING);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Prms.PRMS_ACCSETTING_ACCOUNTID, accountId);
		map.put(Prms.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "prmsAccSettingCallBack");
	}

	/**
	 * 设置贵金属账户结果回调处理
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void prmsAccSettingCallBack(Object resultObj) {
	}

	/**
	 * 贵金属买卖确认
	 * 
	 * @param tradeMethod
	 *            交易方式 08市价即时07限价即时03获利委托04止损委托05二选一委托
	 * @param transFlag
	 *            买卖 0 买入，1 卖出
	 * @param tradeNum
	 *            交易数量(买/卖)buyNum/sellNum
	 * @param currencyCode
	 *            交易货币码
	 * @param cashRemit
	 *            钞汇
	 * @param buyTradeType
	 *            买入贵金属种类标识 买入时不能为空 G:黄金 S:白银
	 */
	protected void prmsTradeConfirm(String tradeMethod, String transFlag,
			BigDecimal tradeNum, String currencyCode, String cashRemit,
			String buyTradeType) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (transFlag.equals(ConstantGloble.PRMS_TRADE_TRANSFLAG_BUY)) {
			map.put(Prms.PRMS_TTADE_CONFIRM_BUYNUM, tradeNum);
		} else {
			map.put(Prms.PRMS_TTADE_CONFIRM_SALENUM, tradeNum);
		}
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Prms.PRMS_TTADE_CONFIRM);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		map.put(Prms.PRMS_TTADE_CONFIRM_TRADEMETHOD, tradeMethod);
		map.put(Prms.PRMS_TTADE_CONFIRM_TRANSFLAG, transFlag);
		map.put(Prms.PRMS_TTADE_CONFIRM_CURRENCYCODE, currencyCode);
		map.put(Prms.PRMS_TTADE_CONFIRM_CASHREMIT, cashRemit);
		map.put(Prms.PRMS_TTADE_CONFIRM_BUYTRADETYPE, buyTradeType);
		biiRequestBody.setParams(map);
		HttpManager
				.requestBii(biiRequestBody, this, "prmsTradeConfirmCallBack");
	}

	/**
	 * 贵金属买卖确认回调处理
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void prmsTradeConfirmCallBack(Object resultObj) {
	}

	/**
	 * 贵金属交易买卖结果
	 * 
	 * @param tradeMethod
	 *            交易方式08市价即时07限价即时03获利委托04止损委托05二选一委托
	 * @param transFlag
	 *            买卖 0 买入，1 卖出
	 * @param tradeNum
	 *            交易数量(买/卖)buyNum/sellNum
	 * @param currencyCode
	 *            交易货币码
	 * @param cashRemit
	 *            钞汇
	 * @param buyTradeType
	 *            买入贵金属种类标识 买入时不能为空 G:黄金 S:白银
	 * @param direct
	 *            直接交易确认交易标志 0确认成交，1直接成交
	 * @param limitPrice
	 *            限价价格 限价交易时传入
	 * @param losePrice
	 *            止损价格 止损委托（或二选一）时传入
	 * @param winPrice
	 *            获利价格 获利委托（或二选一）时传入
	 * @param endDate
	 *            截止日
	 * @param endHour
	 *            截止时
	 * @param marketPrice
	 *            即时交易确认成交汇率 PsnGoldTradeConfirm接口返回
	 * @param token
	 *            防重机制
	 */
	protected void prmsTradeResult(String tradeMethod, String transFlag,
			BigDecimal tradeNum, String currencyCode, String cashRemit,
			String buyTradeType, String direct, String limitPrice,
			String losePrice, String winPrice, String endDate, String endHour,
			String marketPrice, String token) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Prms.PRMS_TTADE_RESOUT_TRADEMETHOD, tradeMethod);
		map.put(Prms.PRMS_TTADE_RESOUT_TRANSFLAG, transFlag);
		if (transFlag.equals(ConstantGloble.PRMS_TRADE_TRANSFLAG_BUY)) {
			map.put(Prms.PRMS_TTADE_RESOUT_BUYNUM, tradeNum);
		} else {
			map.put(Prms.PRMS_TTADE_RESOUT_SALENUM, tradeNum);
		}
		map.put(Prms.PRMS_TTADE_RESOUT_CURRENCYCODE, currencyCode);
		map.put(Prms.PRMS_TTADE_RESOUT_CASHREMIT, cashRemit);
		map.put(Prms.PRMS_TTADE_RESOUT_BUYTRADETYPE, buyTradeType);
		map.put(Prms.PRMS_TTADE_RESOUT_DIRECT, direct);
		map.put(Prms.PRMS_TTADE_RESOUT_LIMITPRICE, limitPrice);
		map.put(Prms.PRMS_TTADE_RESOUT_LOSEPRICE, losePrice);
		map.put(Prms.PRMS_TTADE_RESOUT_WINPRICE, winPrice);
		map.put(Prms.PRMS_TTADE_RESOUT_ENDDATE, endDate);
		map.put(Prms.PRMS_TTADE_RESOUT_ENDHOUR, endHour);
		map.put(Prms.PRMS_TTADE_RESOUT_MARKETPRICE, marketPrice);
		map.put(Prms.TOKEN, token);

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Prms.PRMS_TTADE_RESOUT);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "prmsTradeResultCallBack");
	}

	/**
	 * 贵金属买卖结果回调处理
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void prmsTradeResultCallBack(Object resultObj) {
	}

	/**
	 * 贵金属市价即时交易买卖结果
	 * 
	 * @param transFlag
	 *            买卖 0 买入，1 卖出
	 * @param tradeNum
	 *            交易数量(买/卖)buyNum/sellNum
	 * @param currencyCode
	 *            交易货币码
	 * @param cashRemit
	 *            钞汇
	 * @param buyTradeType
	 *            买入贵金属种类标识 买入时不能为空 G:黄金 S:白银
	 * @param direct
	 *            直接交易确认交易标志 0确认成交，1直接成交
	 * @param marketPrice
	 *            即时交易确认成交汇率 PsnGoldTradeConfirm接口返回
	 * @param token
	 *            防重机制
	 */
	protected void prmsTradeResult08(String transFlag, String tradeNum,
			String currencyCode, String cashRemit, String buyTradeType,
			String direct, String marketPrice, String token) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Prms.PRMS_TTADE_RESOUT_TRADEMETHOD,
				ConstantGloble.PRMS_TRADEMETHOD_NOW);
		map.put(Prms.PRMS_TTADE_RESOUT_TRANSFLAG, transFlag);
		if (transFlag.equals(ConstantGloble.PRMS_TRADE_TRANSFLAG_BUY)) {
			map.put(Prms.PRMS_TTADE_RESOUT_BUYNUM, tradeNum);
		}
		if (transFlag.equals(ConstantGloble.PRMS_TRADE_TRANSFLAG_SALE)) {
			map.put(Prms.PRMS_TTADE_RESOUT_SALENUM, tradeNum);
		}
		map.put(Prms.PRMS_TTADE_RESOUT_CURRENCYCODE, currencyCode);
		map.put(Prms.PRMS_TTADE_RESOUT_CASHREMIT, cashRemit);
		map.put(Prms.PRMS_TTADE_RESOUT_DIRECT, direct);
		map.put(Prms.PRMS_TTADE_RESOUT_BUYTRADETYPE, buyTradeType);
		map.put(Prms.PRMS_TTADE_RESOUT_MARKETPRICE, marketPrice);
		map.put(Prms.TOKEN, token);

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Prms.PRMS_TTADE_RESOUT);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"prmsTradeResult08CallBack");
	}

	/**
	 * 市价即时交易
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void prmsTradeResult08CallBack(Object resultObj) {
	}

	/**
	 * 限价即时交易
	 * 
	 * @Author xyl
	 * @param transFlag
	 * @param tradeNum
	 * @param currencyCode
	 * @param cashRemit
	 * @param buyTradeType
	 * @param direct
	 * @param marketPrice
	 * @param token
	 */
	protected void prmsTradeResultLimit(String transFlag, String tradeNum,
			String limitPric, String currencyCode, String cashRemit,
			String buyTradeType, String direct, String marketPrice, String token) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Prms.PRMS_TTADE_RESOUT_TRADEMETHOD,
				ConstantGloble.PRMS_TRADEMETHOD_LIMIT);
		map.put(Prms.PRMS_TTADE_RESOUT_LIMITPRICE, limitPric);
		map.put(Prms.PRMS_TTADE_RESOUT_TRANSFLAG, transFlag);
		if (transFlag.equals(ConstantGloble.PRMS_TRADE_TRANSFLAG_BUY)) {
			map.put(Prms.PRMS_TTADE_RESOUT_BUYNUM, tradeNum);
		}
		if (transFlag.equals(ConstantGloble.PRMS_TRADE_TRANSFLAG_SALE)) {
			map.put(Prms.PRMS_TTADE_RESOUT_SALENUM, tradeNum);
		}
		map.put(Prms.PRMS_TTADE_RESOUT_CURRENCYCODE, currencyCode);
		map.put(Prms.PRMS_TTADE_RESOUT_CASHREMIT, cashRemit);
		map.put(Prms.PRMS_TTADE_RESOUT_DIRECT, direct);
		map.put(Prms.PRMS_TTADE_RESOUT_BUYTRADETYPE, buyTradeType);
		map.put(Prms.PRMS_TTADE_RESOUT_MARKETPRICE, marketPrice);
		map.put(Prms.TOKEN, token);

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Prms.PRMS_TTADE_RESOUT);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"prmsTradeResultLimitCallBack");
	}

	/**
	 * 限价即时交易s
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void prmsTradeResultLimitCallBack(Object resultObj) {
	}

	/**
	 * 
	 * @param transFlag
	 * @param winPrice
	 * @param losPrice
	 * @param tradeNum
	 * @param currencyCode
	 * @param cashRemit
	 * @param buyTradeType  买入的时候传 卖出的时候可以不传
	 * @param direct
	 * @param token 
	 * @param tradeMethod
	 * @param endDate
	 * @param endHour
	 */
	protected void prmsTradeResultEntrust(String transFlag, String winPrice,
			String losPrice, String tradeNum, String currencyCode,
			String cashRemit, String buyTradeType, String direct, String token,
			String tradeMethod,String endDate,String endHour) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Prms.PRMS_TTADE_RESOUT_TRADEMETHOD, tradeMethod);
		switch (PrmsControl.tradeMethodSwitch(tradeMethod)) {
		case PrmsControl.PRMS_TRADEMETHOD_LOSE:
			map.put(Prms.PRMS_TTADE_RESOUT_LOSEPRICE, losPrice);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_WIN:
			map.put(Prms.PRMS_TTADE_RESOUT_WINPRICE, winPrice);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_ONEINWTO:
			map.put(Prms.PRMS_TTADE_RESOUT_LOSEPRICE, losPrice);
			map.put(Prms.PRMS_TTADE_RESOUT_WINPRICE, winPrice);
			break;
		case PrmsControl.PRMS_TRADEMETHOD_RUNLOST://追击止损委托
			map.put(Prms.PRMS_TTADE_RESOUT_LOSEPRICE, losPrice);
			break;
		}
		map.put(Prms.PRMS_TTADE_RESOUT_TRANSFLAG, transFlag);
		if (transFlag.equals(ConstantGloble.PRMS_TRADE_TRANSFLAG_BUY)) {
			map.put(Prms.PRMS_TTADE_RESOUT_BUYNUM, tradeNum);
			map.put(Prms.PRMS_TTADE_RESOUT_BUYTRADETYPE, buyTradeType);
		}
		if (transFlag.equals(ConstantGloble.PRMS_TRADE_TRANSFLAG_SALE)) {
			map.put(Prms.PRMS_TTADE_RESOUT_SALENUM, tradeNum);
		}
		map.put(Prms.PRMS_TTADE_RESOUT_CURRENCYCODE, currencyCode);
		map.put(Prms.PRMS_TTADE_RESOUT_CASHREMIT, cashRemit);
		map.put(Prms.PRMS_TTADE_RESOUT_DIRECT, direct);
		map.put(Prms.PRMS_TTADE_RESOUT_ENDDATE, endDate);
		map.put(Prms.PRMS_TTADE_RESOUT_ENDHOUR, endHour);
		map.put(Prms.TOKEN, token);

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Prms.PRMS_TTADE_RESOUT);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"prmsTradeResultEntrustCallBack");
	}

	/**
	 * 委托交易 结果 处理
	 * 
	 */
	public void prmsTradeResultEntrustCallBack(Object resultObj) {
	}

	/**
	 * 贵金属委托撤单 撤销
	 * 
	 * @param token
	 *            防重
	 * @param consignNumber
	 *            委托序号
	 * @param sellCurrency
	 *            卖出币种
	 * @param buyCurrency
	 *            买入币种
	 * @param cashRemit
	 *            炒汇
	 * @param transType
	 *            交易类型03获利委托，04止损委托，05二选一委托
	 * @param sellAmount
	 *            卖出金额
	 * @param buyAmount
	 *            买入金额
	 * @param profitRate
	 *            获利价格
	 * @param loseRate
	 *            止损价格
	 */
	public void prmsConserDeal(String token, String consignNumber,
			String sellCurrency, String buyCurrency, String cashRemit,
			String transType, String sellAmount, String buyAmount,
			String profitRate, String loseRate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Prms.TOKEN, token);
		map.put(Prms.PRMS_QUSH_SUBMIT_CONSIGNNUMBER, consignNumber);
		map.put(Prms.PRMS_QUSH_SUBMIT_SELLCURRENCY, sellCurrency);
		map.put(Prms.PRMS_QUSH_SUBMIT_BUYCURRENCY, buyCurrency);
		map.put(Prms.PRMS_QUSH_SUBMIT_CASHREMIT, cashRemit);
		map.put(Prms.PRMS_QUSH_SUBMIT_SELLAMOUNT, sellAmount);
		map.put(Prms.PRMS_QUSH_SUBMIT_BUYAMOUNT, buyAmount);
		map.put(Prms.PRMS_QUSH_SUBMIT_PROFITRATE, profitRate);
		map.put(Prms.PRMS_QUSH_SUBMIT_LOSERATE, loseRate);
		map.put(Prms.PRMS_QUSH_SUBMIT_TRANSTYPE, transType);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Prms.PRMS_QUSH_SUBMIT);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "prmsConserDealCallBack");
	}

	/**
	 * 贵金属委托撤单
	 */
	public void prmsConserDealCallBack(Object resultObj) {

	}

	/**
	 * 获取tocken
	 */
	protected void requestPSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPSNGetTokenIdCallback");
	}

	/**
	 * 获取tokenId----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPSNGetTokenIdCallback(Object resultObj) {
	}

	/** 是否要跳转到持仓页面 */
	public boolean isbalance = false;

	/**
	 * 菜单处理
	 * 
	 * @author xyl
	 */
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		Intent intent = new Intent();
		String menuId = menuItem.MenuID;
		if(menuId.equals("prmsManage_1")){
			// 到贵金属详情页面
			prmsControl.cleanAll();
//						ActivityTaskManager.getInstance().removeAllActivity();
			intent.setClass(this, PrmsPricesActivity.class);
			context.startActivity(intent);
		}
		else if(menuId.equals("prmsManage_2")){
			// 到我的账户贵金属页面
			prmsControl.cleanAll();
//						ActivityTaskManager.getInstance().removeAllActivity();
			intent.setClass(this, PrmsAccMeneActivity.class);
			context.startActivity(intent);
		}
		else if(menuId.equals("prmsManage_3")){
			// 到贵金属查询页面
			prmsControl.cleanAll();
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof PrmsQueryActivity)) {
				prmsControl.cleanAll();
//							ActivityTaskManager.getInstance().removeAllActivity();
				if (PrmsControl.is401) {
					intent.setClass(this, PrmsQueryActivity.class);
				} else {
					intent.setClass(this, PrmsQueryDealActivity.class);
				}
				context.startActivity(intent);
			}
		}
		return true;
		
		
		
//		super.setSelectedMenu(clickIndex);
//		ActivityTaskManager.getInstance().removeAllSecondActivity();
//		Intent intent = new Intent();
//		switch (clickIndex) {
//		case 0:// 到贵金属详情页面
//			prmsControl.cleanAll();
////			ActivityTaskManager.getInstance().removeAllActivity();
//			intent.setClass(this, PrmsPricesActivity.class);
//			startActivity(intent);
//			break;
//		case 1:// 到我的账户贵金属页面
//			prmsControl.cleanAll();
////			ActivityTaskManager.getInstance().removeAllActivity();
//			intent.setClass(this, PrmsAccMeneActivity.class);
//			startActivity(intent);
//			break;
//		case 2:// 到贵金属查询页面
//			prmsControl.cleanAll();
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof PrmsQueryActivity)) {
//				prmsControl.cleanAll();
////				ActivityTaskManager.getInstance().removeAllActivity();
//				if (PrmsControl.is401) {
//					intent.setClass(this, PrmsQueryActivity.class);
//				} else {
//					intent.setClass(this, PrmsQueryDealActivity.class);
//				}
//				startActivity(intent);
//			}
//			break;
//		default:// 到贵金属详情页面
//			break;
//		}
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	protected void baseinit() {
		setContentView(R.layout.biz_activity_606_layout);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		mainInflater = LayoutInflater.from(this);
		back = (Button) findViewById(R.id.ib_back);
		right = (Button) findViewById(R.id.ib_top_right_btn);
//		btn_show =  (Button) findViewById(R.id.btn_show);
//		btn_show.setVisibility(View.GONE);
		back.setOnClickListener(this);
		initPulldownBtn();
		initFootMenu();
//		initLeftSideList(this, LocalData.prmsManagerlistData);
		setLeftButtonPopupGone();
	}

	protected void settingbaseinit() {
//		setContentView(R.layout.biz_activity_layout_withnofooter);//606换肤前旧布局
		setContentView(R.layout.biz_activity_606_layout_1);//606换肤新布局
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		mainInflater = LayoutInflater.from(this);
		back = (Button) findViewById(R.id.ib_back);
//		btn_show =  (Button) findViewById(R.id.btn_show);
//		btn_show.setVisibility(View.GONE);
		right = (Button) findViewById(R.id.ib_top_right_btn);
		back.setOnClickListener(this);
		right.setVisibility(View.VISIBLE);
	}





	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		default:
			break;
		}
	}

	/** 主界面 公共加了 废弃 */
	protected void initRightBtnForMain() {
		// right.setText(getString(R.string.forex_right));
		// right.setVisibility(View.VISIBLE);
		// right.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// activityTaskManager.removeAllActivity();
		// }
		// });
	}

	/**
	 * 任务提示框
	 */
	public void getPopup() {
		View popupView = LayoutInflater.from(this).inflate(
				R.layout.prms_task_notify, null);
		// 右上角的关闭按钮
		ImageView taskPopCloseButton = (ImageView) popupView
				.findViewById(R.id.top_right_close);
		// accButtonView:设置账户按钮
		View accButtonView = popupView.findViewById(R.id.forex_acc_button_show);
		View moneyButtonView = popupView
				.findViewById(R.id.forex_money_button_show);
		// accTextView:设置账户文本框
		View accTextView = popupView.findViewById(R.id.forex_acc_text_hide);
		// moneyTextView:理财服务文本框
		View moneyTextView = popupView.findViewById(R.id.forex_money_text_hide);
		if (prmsControl.ifInvestMent && prmsControl.ifhavPrmsAcc) {// 如果都开通了
			// finishPopupWindow();
			// BaseDroidApp.getInstanse().dismissMessageDialog();
			return;
		}
		if (prmsControl.ifInvestMent) {// 已经开通投资理财
			moneyButtonView.setVisibility(View.GONE);
			moneyTextView.setVisibility(View.VISIBLE);
		} else {// 没有开通投资理财服务
			moneyButtonView.setVisibility(View.VISIBLE);
			moneyTextView.setVisibility(View.GONE);
			moneyButtonView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 跳转到投资理财服务协议页面
					Intent gotoIntent = new Intent(BaseDroidApp.getInstanse()
							.getCurrentAct(), InvesAgreeActivity.class);
					startActivityForResult(gotoIntent,
							ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE);
				}
			});
		}
		if (prmsControl.ifhavPrmsAcc) {// 有贵金属账户
			accButtonView.setVisibility(View.GONE);
			accTextView.setVisibility(View.VISIBLE);
		} else {
			accButtonView.setVisibility(View.VISIBLE);
			accTextView.setVisibility(View.GONE);
			if (prmsControl.ifInvestMent) {
				accButtonView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						BaseHttpEngine.showProgressDialog();
						queryPrmsAccs();
					}
				});
			} else {
				accButtonView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						CustomDialog.toastInCenter(
								BaseDroidApp.getInstanse().getCurrentAct(),
								BaseDroidApp
										.getInstanse()
										.getCurrentAct()
										.getString(
												R.string.bocinvt_task_toast_1));

					}
				});
			}

		}

		taskPopCloseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				BaseDroidApp.getInstanse().getCurrentAct().finish();
			}
		});
		BaseDroidApp.getInstanse().showAccountMessageDialog(popupView);
	}

	public void checkRequestPsnInvestmentManageIsOpen() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"checkRequestPsnInvestmentManageIsOpenCallback");
	}

	public void checkRequestPsnInvestmentManageIsOpenCallback(Object resultObj) {

	}

	// @Override
	// public boolean httpRequestCallBackPre(Object resultObj) {
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {//
	// 返回的是错误码
	// if (biiResponseBody.getError().getCode()
	// .equals(ErrorCode.SETTING_NODEFAULTACC_ERROR)&&biiResponseBody.getMethod().equals(Prms.QUERY_TRADERATE))
	// {
	// return true;
	// }
	//
	// }
	// return super.httpRequestCallBackPre(resultObj);
	//
	// }
	/** listView 的header view 适用于主页面 */
	protected void initListHeaderView(View v, int text1ResID, int textRes2Id,
			int textRes3Id) {
		TextView headerView1 = (TextView) v.findViewById(R.id.list_header_tv1);
		TextView headerView2 = (TextView) v.findViewById(R.id.list_header_tv2);
		TextView headerView3 = (TextView) v.findViewById(R.id.list_header_tv3);
		headerView1.setText(text1ResID);
		headerView2.setText(textRes2Id);
		headerView3.setText(textRes3Id);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView3);
	}
	
	/**
	 * 获取持仓信息
	 * @param sourceData
	 * @param flag 1:资金持仓     2:金属持仓
	 * @return
	 */
	protected List<Map<String, Object>> getAccData(List<Map<String, Object>> sourceData, int flag){
		if (StringUtil.isNullOrEmpty(sourceData)) {
			return null;
		}
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		if(flag == 1){
			//账户资金信息
			for(Map<String, Object> map : sourceData){
				String code = String.valueOf(map.get(Prms.PRMS_CODE));
				if(LocalData.prmsBuyCurrencyMap.containsKey(code))
					data.add(map);
			}
		}else{
			//持仓状况
			for(Map<String, Object> map : sourceData){
				String code = String.valueOf(map.get(Prms.PRMS_CODE));
				if(!LocalData.prmsBuyCurrencyMap.containsKey(code))
					data.add(map);
			}
		}
		return data;
	}
	
	
	
	/**
	 * 黄金行情查询登录之前发送
	 */
	//wuhan
	protected void queryPrmsPricePoliPreLogin() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Prms.QUERY_TRADERATE_PRELOGIN);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Prms.IBKNUM, LocalDataService.getInstance().getIbkNum(ConstantGloble.Prms));
		map.put(Prms.PARITIESTYPE, PARITIESTYP);
		map.put("offerType", "R");
		biiRequestBody.setParams(map);
		HttpManager.requestOutlayBii(biiRequestBody, this, "queryPrmsPreLoginCallBack");
//		HttpManager.requestPollingBii(biiRequestBody,httpHandlerPreLogin , 7);// 7秒刷新
	}

	public void queryPrmsPreLoginCallBack(Object resultObj){
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		boolean login = BaseDroidApp.getInstanse().isLogin();
		onResumeFromLogin(login);
	}
	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
	
	
	/**
	 * 贵金属挂单点差范围查询
	 */
	//wuhan
	protected void queryGoldDianCha(String currencyCode,String buyTradeType,String transFlag) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Prms.QUERY_GOLDPENDINGSETRANGEQUERY);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Prms.GOLD_CURRENCYCODE, currencyCode);////交易货币码
		map.put(Prms.GOLD_BUYTRADETYPE, buyTradeType);//买入贵金属种类标识
		map.put("transFlag", transFlag);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "queryGoldDianChaCallBack");
	}
	
	
	public void queryGoldDianChaCallBack(Object resultObj){
		
	}
}
