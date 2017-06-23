package com.chinamworld.bocmbci.biz.finc.control;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.fundnotice.FundnoticeResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsContent.NewsContentResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsList.NewsListResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryFundBasicDetail.QueryFundBasicDetailResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryMultipleFund.QueryMultipleFundResult;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.StringUtil;

public class FincControl {
	private static FincControl instance = null;
	
	public static boolean is401 = true;
	/**判断是否由推荐栏点击进入*/
	public static boolean isRecommend=false;
	
	/**标记是由基金推荐进入的全部行情*/
	
	public static boolean recommendEnterMarket = false;
	
	/**是否由基金关注进入基金详情*/
	public static boolean isAttentionFlag = false;
	
	/**
	 * 基金行情数据存放
	 */
	public List<Map<String, Object>> fundPriceDataList = new ArrayList<Map<String, Object>>();
	/** 基金行情  公司列表数据存放 */
	public List<Map<String, String>> fundCompanyList;
	/**
	 * 资金账户列表
	 */
	public List<Map<String, Object>> fundAccList;

	/**
	 * 基金账户开户 登记 传输的 字段
	 */
	public Map<String, String> registAccFund;
	/** 基金交易 字段 */
	public Map<String, Object> tradeFundDetails;
	/** 基金详情交易 字段 */
	public Map<String, Object> fundDetails;
	/** 快速赎回额度查询 字段 */
	public Map<String, Object> fastSellFundDetails;
	/**基金基本信息查询 字段 */
	public Map<String, Object> fincFundDetails;
	
	/**基金基本信息查询  */
	public Map<String, Object> fincFundBasicDetails;
	
	/**基金短期理财产品信息*/
	public List<Map<String, Object>> fincFundDueDateQuery;
	/**
	 * 基金账户id
	 */
	public String invAccId ;
	/**网银机构号*/
	public String bankId ;

	/** 资金账户号码 */
	public String accNum ;
	/** 资金账户类型*/
	public String accountType ;
	/** 资金账户id */
	public String accId ;
	
	public Map<String,String>  accDetailsMap;
	
	public String registAccConversationId ;
	
	
	public List<Map<String,Object>> factorList; 
	/** 关注的基金  */
	public List<Map<String,Object>> attentionFundList; 
	/** 推荐的基金 */
	public List<Map<String,Object>> recommendFundList; 

	/**
	 * 判断是否有资金账户
	 */
	public Boolean ifhaveaccId = false;
	/**
	 * 判断是否做了风险评估
	 */
	public Boolean ifdorisk = false;
	/**
	 * 标记当前基金是否被关注
	 */
	private Boolean attentionFlag = false;
	
	public String userRiskLevel;
	/**
	 * 是否开通理财服务
	 */
	public Boolean ifInvestMent = false;

	public Boolean iffirstTrade = true;
	/**
	 * 当日交易查询的结果
	 */
	public List<Map<String, Object>> queryTodayDataList;
	/** 定期定额申请查询  结果 */
	public List<Map<String,Object>>   queryDTDataList;
	/**
	 * 历史为交易查询的结果说
	 */
	public List<Map<String, Object>> list;

	/** 基金持仓列表  */
	public List<Map<String, Object>> fundBalancList;
	
	/** 基金持仓信息 */
	public Map<String,Object> fundBalancMap;
	
	/** 定期定额申请  查询  */
	public Map<String, Object> fundScheduQueryMap ;
	public String randomNumber;
	public ArrayList<SoftReference<Activity>>  rfLists = new ArrayList<SoftReference<Activity>>();
	/** 基金关注总数*/
	private int attentionCount;
	
	/** 持仓  浮动盈亏测算   基金转换   设置分红 */
	public static final int FINCINFO = 1, FINCTRANS = 3, SETBOUNDS = 4;
	//我的基金操作标志  持仓、试算、转换、分红
	public static int myfincOperationFlag = FINCINFO;
	public Map<String, Object> fundCompanyInfo;//基金公司信息查询
	
	/******P405改造新增*******/
	public Map<String, Object> fundStatementBalance;//基金对账单持仓查询
	public Map<String, Object> fundStatementBalanceDetail;//基金对账单持仓详情
	public List<Map<String, Object>> transSactionList;//基金交易流水
	public Map<String, Object> transSactionDetail;//基金对账单交易流水详情
	public Map<String, Object> fundUnavailableResult;//已失效定投定赎结果
	public List<Map<String, Object>> fundUnavailableList;//已失效定投定赎列表
	public Map<String, Object> fundUnavailableMap;//已失效定投定赎详细字典
	public Map<String, Object> fundUAvailableResult;//有效定投定赎结果
	public List<Map<String, Object>> fundAvailableList;//有效定投定赎列表
	public Map<String, Object> fundAvailableMap;//有效定投定赎详细字典
	public Map<String, Object> scheduledBuySellDetail;//定投定赎申请明细查询
	public Map<String, Object> pauseResumeResult;//暂停与恢复交易结果
	/** 指令产品查询结果 */
	public Map<String, Object> OcrmProductMap;
	/** 指令产品查询列表 */
	public List<Map<String, Object>> OcrmProductList;
	public Map<String, Object> OcrmProductDetailMap;//指令产品详情
	public List<Map<String, String>> fundIssueScopeList;//指令产品查询列表
	public String currentTime;
	public List<QueryMultipleFundResult.QueryMultipleFundItem> fincList;//行情列表
	public QueryMultipleFundResult.QueryMultipleFundItem item;//选中的行情
	/** 基金行情选中项 */
	public Map<String, Object> fundListSelect;
	public FundnoticeResult.FundnoticeItem noticeItem;//公告列表选中项
	public NewsListResult.NewsListItem newsItem;//新闻列表选中项
	public NewsContentResult contentDetail;
	public List<Map<String, Object>> historyList;//历史净值列表数据
	
	private FincControl() {
	}

	public static FincControl getInstance() {
		if (instance == null)
			instance = new FincControl();
		return instance;
	}

//	public void notLogin() {
//		Intent intent = new Intent();
//		intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//				LoginActivity.class);
//		BaseDroidApp
//				.getInstanse()
//				.getCurrentAct()
//				.startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
//	}

	/**
	 * 设定基金他账户
	 * 
	 * @return
	 */
	public String[] getStepsForRegistFundTa() {
		String step1 = 1 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources()
				.getString(R.string.finc_step_registfundta_first);
		String step2 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_registfundta_second);
		String step3 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_registfundta_three);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}

	/** 基金买入 */
	public String[] getStepsFortradeBuy() {
		String step1 = 1 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources().getString(R.string.finc_step_tradebuy_first);
		String step2 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_tradebuy_second);
		String step3 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_tradebuy_three);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}
	/** 基金指定日期买入 1*/
	public String[] getStepsForExtradeDayBuy1() {
		String step1 = 1 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources().getString(R.string.finc_step_tradebuy_first);
		String step2 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_extraday_tradebuy_second);
		String step3 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_tradebuy_second);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}
	/** 基金指定日期买入 2*/
	public String[] getStepsForExtradeDayBuy2() {
		String step1 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources().getString(R.string.finc_step_extraday_tradebuy_second);
		String step2 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_tradebuy_second);
		String step3 = 4 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_tradebuy_three);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}
	
	/** 赎回 */
	public String[] getStepsForSell() {
		String step1 = 1 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources().getString(R.string.finc_top_one);
		String step2 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_top_two);
		String step3 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_top_three);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}
	/** 基金指定日期买入 1*/
	public String[] getStepsForExtradeDaySell1() {
		String step1 = 1 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources().getString(R.string.finc_top_one);
		String step2 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_extraday_tradebuy_second);
		String step3 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_top_two);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}
	/** 基金指定日期赎回 2*/
	public String[] getStepsForExtradeDaySell2() {
		String step1 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources().getString(R.string.finc_step_extraday_tradebuy_second);
		String step2 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_top_two);
		String step3 = 4 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_top_three);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}

	/**
	 * 定投步骤
	 * 
	 * @return
	 */
	public String[] getStepsForDingTou() {
		String step1 = 1 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources().getString(R.string.finc_step_tradebuy_first);
		String step2 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_tradebuy_second);
		String step3 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_dingtou_three);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}

	/**
	 * 基金账户开户步骤条 1
	 * 
	 * @return
	 */
	public String[] getStepsForRegistAcc1() {
		String step1 = 1 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources().getString(R.string.finc_step_checkIn_acc_first);
		String step2 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_regist_acc_second);
		String step3 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_tradebuy_second);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}

	/**
	 * 基金账户开户步骤条 2
	 * 
	 * @return
	 */
	public String[] getStepsForRegistAcc2() {
		String step1 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources().getString(R.string.finc_step_regist_acc_second);
		String step2 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_tradebuy_second);
		String step3 = 4 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_regist_acc_four);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}

	/**
	 * 登记账户步骤条
	 * 
	 * @return
	 */
	public String[] getStepsForCheckInAcc() {
		String step1 = 1 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources().getString(R.string.finc_step_regist_acc_first);
		String step2 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_tradebuy_second);
		String step3 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.finc_step_checkIn_acc_three);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}

	/**
	 * 清理缓存
	 */
	public void cleanAllData() {
		fincFundBasicDetails = null;
		attentionFundList = null;
		recommendFundList = null;
		fundPriceDataList = null;
		fundAccList = null;
		registAccFund = null;
		tradeFundDetails = null;
		fundDetails = null;
		fastSellFundDetails=null;
		fincFundDetails=null;
		queryTodayDataList = null;
		list = null;
		fundBalancList = null;
		fundAccList = null;
//		ifhaveaccId = false;
//		ifdorisk = false;
//		ifInvestMent = false;
//		iffirstTrade = true;
		fundScheduQueryMap = null;
		fundCompanyInfo = null;
		fundStatementBalance = null;
		fundStatementBalanceDetail = null;
		transSactionList = null;
		transSactionDetail = null;
		fundUnavailableResult = null;
		fundUnavailableList = null;
		fundUnavailableMap = null;
		fundUAvailableResult = null;
		fundAvailableList = null;
		fundAvailableMap = null;
		scheduledBuySellDetail = null;
		pauseResumeResult = null;
		OcrmProductMap = null;
		OcrmProductList = null;
		OcrmProductDetailMap = null;
		fundIssueScopeList = null;
		fundBalancMap = null;
		fincList = null;
		item = null;
		noticeItem = null;
		newsItem = null;
		reMoveAllActivity();
		BaseDroidApp.getInstanse().getBizDataMap().remove(Finc.D_FDYKMAP);
	}

	/**
	 * 清理交易信息
	 */
	public void cleanTrade() {
		if (!StringUtil.isNullOrEmpty(tradeFundDetails)) {
			tradeFundDetails.clear();
		}
	}
	
	public void reMoveAllActivity(){
		for (int i = 0; i < rfLists.size(); i++) {
			if(rfLists.get(i).get()!=null){
				rfLists.get(i).get().finish();
			}
		}
	}
	public void addActivit(Activity activity){
		rfLists.add(new SoftReference<Activity>(activity));
	}
	
	/**
	 * 根据币种 和炒汇 判断显示  如果不是人民币   显示币种和炒汇
	 * @param currencyCode   币种
	 * @param cashFlagCode  炒汇
	 * @return
	 */
	public static String fincCurrencyAndCashFlag(String currencyCode,String cashFlagCode){
		if(currencyCode==null){
			return "-";
		}
		if(ConstantGloble.PRMS_CODE_RMB.equals(currencyCode)){
			return LocalData.Currency.get(currencyCode);
		}else{
			return LocalData.Currency.get(currencyCode)+"/"+LocalData.CurrencyCashremit.get(cashFlagCode);
		}
	}
	/**
	 * 如果显示为"-"时,为黑色
	 * @param tv
	 * @param context
	 */
	public static void setTextColor(TextView tv,Context context){
//		if(tv.getText().toString().equals("-")){
//			tv.setTextColor(context.getResources().getColor(R.color.black));
//		}
	}
	
	/**
	 * 盈亏为负时显示绿色，盈亏为正时显示红色
	 * @param tv
	 * @param context
	 */
	public static void setTextColorAboutYK(TextView tv,Context context){
		float yk = Float.parseFloat(tv.getText().toString());
		if(yk < 0){
			tv.setTextColor(context.getResources().getColor(R.color.greens));
		}else if(yk > 0){
			tv.setTextColor(context.getResources().getColor(R.color.red));
		}
	}
	/**
	 * 交易基金  把基金代码和名称 拼起来
	 * @param fundCode
	 * @param fundName
	 * @return
	 */
	public static final String connFundCodeAndeName(String fundCode,String fundName){
		if(StringUtil.isNull(fundCode)&&StringUtil.isNull(fundName)){
			return "-";
		}else{
			return StringUtil.valueOf1(fundCode)+"/"+StringUtil.valueOf1(fundName);
		}
	}
	/**
	 * 
	 * @param regPric
	 *            校验前缀 ex:(限价价格)
	 * @param sourceCurrency
	 *            源币种 ex:034
	 * @param price
	 *            用户输入的价格 ex:(100.00)
	 * @return
	 * note:美元银和人民币银为 三位小数 其他为两位小数
	 */
	public static final RegexpBean getRegexpBeanPriceBySourceCurrency(String regPric,
			String sourceCurrency, String price) {
		RegexpBean regexpPrice;
		if (sourceCurrency.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
				|| sourceCurrency.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)) {// 美元银和人民币银
			regexpPrice = new RegexpBean(regPric, price, "prmssilverprice");
		} else {
			regexpPrice = new RegexpBean(regPric, price, "price");
		}
		return regexpPrice;
	}
	/**
	 * 按千位分割格式格式化数字
	 * 
	 * @param text
	 *            原数字
	 * @param sourceCurrency
	 *          源币种
	 * @return
	 * 
	 * note: 美元银和人民币银  小数点后三位,其他币种小数点后两位
	 */
	public static final String parsePriceByCurrency(String text, String sourceCurrency) {
		if (sourceCurrency.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
				|| sourceCurrency.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)) {// 美元银和人民币银
			return StringUtil.parseStringPattern(text, 3);
		} else {
			return StringUtil.parseStringPattern(text, 2);
		}
	}
	
	/**
	 * 在途交易   交易类型转换
	 * @param speCode 特殊交易标识
	 * @param tradeType  交易码
	 * @return
	 */
	public static String transTypeResult = "";//最终得到的交易类型
	public static final String parseSpeCodeTradeTypeEffective(String speCode
			, String tradeType){
		if(speCode != null && tradeType != null)
		{
			if(speCode.equals("0") && (tradeType.equals("020") || tradeType.equals("022")))
				return "购买";
			if(speCode.equals("1") && (tradeType.equals("020") || tradeType.equals("022")))
				return "挂单购买";
			if(speCode.equals("0") && (tradeType.equals("024")))
				return "赎回";
			if(speCode.equals("1") && (tradeType.equals("024")))
				return "挂单赎回";
			if(speCode.equals("2") && (tradeType.equals("020")))
				return "指定日期认购";
			if(speCode.equals("2") && (tradeType.equals("022")))
				return "指定日期申购";
			if(speCode.equals("2") && (tradeType.equals("024")))
				return "指定日期赎回";
		}
		if(tradeType != null){
			if(tradeType.equals("029"))
				return "设置分红方式";
			if(tradeType.equals("036") || tradeType.equals("037") || tradeType.equals("038"))
				return "基金转换";
			if(tradeType.equals("001") || tradeType.equals("008"))
				return "登记基金TA账户";
			if(tradeType.equals("059") || tradeType.equals("039"))
				return "定期定额申购";
			if(tradeType.equals("062") || tradeType.equals("024"))
				return "定期定额赎回";
			if(tradeType.equals("009") )
				return "取消基金TA账户关联";
			if(tradeType.equals("002") )
				return "基金TA账户销户";
			if(tradeType.equals("026") || tradeType.equals("027") || tradeType.equals("028"))
				return "转托管转出入";
		}
		return LocalData.fincTradeTypeCodeToStr.get(tradeType);
	}
	
	/**
	 * 历史交易   交易类型转换
	 * @param speCode 特殊交易标识
	 * @param tradeType  交易码
	 * @return
	 */
	public static final String parseSpeCodeTradeTypeHistory(String speCode
			, String tradeType){
		if(speCode != null && tradeType != null){
			if(speCode.equals("0") && goumai.contains(tradeType))
				return "购买";
			if(speCode.equals("1") && goumai.contains(tradeType))
				return "挂单购买";
			if(speCode.equals("2") && goumai.contains(tradeType))
				return "指定日期申购";
			if(speCode.equals("0") && shuhui.contains(tradeType))
				return "赎回";
			if(speCode.equals("1") && shuhui.contains(tradeType))
				return "挂单赎回";
			if(speCode.equals("2") && shuhui.contains(tradeType))
				return "指定日期赎回";
			
			if(speCode.equals("3") && dingtou.contains(tradeType))
				return "定期定额申购";
			if(speCode.equals("4") && dingtou.contains(tradeType))
				return "定期定额赎回";
			if(speCode.equals("0") && hisjijinzh.contains(tradeType))
				return "基金转换";
			if(speCode.equals("1") && hisjijinzh.contains(tradeType))
				return "挂单基金转换";
			if(speCode.equals("0") && fastRansom.contains(tradeType))
				return "快速赎回";
		}
		if(tradeType != null){
			if(fenhong.contains(tradeType))
				return "设置分红方式";
			if(tradeType.equals("611"))
				return "现金分红";
			if(tradeType.equals("612"))
				return "红利再投资";
			if(tradeType.equals("008") || tradeType.equals("108"))
				return "登记基金TA账户";
			if(tradeType.equals("009") || tradeType.equals("109"))
				return "取消基金TA账户关联";
			if(tradeType.equals("002") || tradeType.equals("102"))
				return "基金TA账户销户";
			if(tradeType.equals("026") || tradeType.equals("027") || tradeType.equals("028"))
				return "转托管转出入";
		}
		return LocalData.fincTradeTypeCodeToStr.get(tradeType);
	}
	
	/**
	 * 循环遍历设置浮动框
	 * @param textViews控件列表
	 */
	public static void setOnShowAllTextListener(Context context, TextView... textViews){
		if(textViews == null || textViews.length <= 0)
			return;
		for(TextView tv : textViews){
			if(tv != null)
				PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
						tv);
		}
	}
	
	/**
	 * 按关键字排序列表清单，由近及远，递减排序
	 * @param list 原列表
	 * @param key 排序关键字
	 * @return 已排序列表
	 */
	public static List<Map<String, Object>> sortListByKey(List<Map<String, Object>> list, String key){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try{
			for(Map<String, Object> map : list){
				if(result.size() == 0) result.add(map);
				else{
					boolean isAdd = false;
					String currentValue = (String)map.get(key);
					for(int i = 0; i < result.size(); i ++){
						String value = (String)result.get(i).get(key);
						if(currentValue.compareTo(value) < 0){
							isAdd = true;
							result.add(i, map);
							break;
						}
					}
					if(!isAdd)
						result.add(map);
				}
			}
		}catch(Exception e){
			LogGloble.e("sortByDate", e.toString());
		}
		return result;
	}
	
	public static ArrayList<String> goumai = new ArrayList<String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("020");
			add("022");
			add("120");
			add("122");
			add("130");
		}
	};
	
	public static ArrayList<String> shuhui = new ArrayList<String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("024");
			add("124");
			add("142");
		}
	};
	
	public static ArrayList<String> dingtou = new ArrayList<String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("039");
			add("059");
			add("060");
			add("061");
			add("139");
			add("062");
			add("063");
			add("064");
			add("024");
			add("124");
		}
	};
	
	
	public static ArrayList<String> fenhong = new ArrayList<String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("029");
			add("129");
		}
	};
	
	/**
	 * 供在途使用
	 */
	public static ArrayList<String> jijinzh = new ArrayList<String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("036");
			add("037");
			add("038");
		}
	};
	
	public static ArrayList<String> hisjijinzh = new ArrayList<String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("036");
			add("037");
			add("038");
			add("136");
			add("137");
			add("138");
		}
	};
	
	public static ArrayList<String> fastRansom = new ArrayList<String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("098");
		}
	};

	public Boolean getAttentionFlag() {
		return attentionFlag;
	}

	public void setAttentionFlag(Boolean attentionFlag) {
		this.attentionFlag = attentionFlag;
	}
	
	public int getAttentionCount() {
		return attentionCount;
	}

	public void setAttentionCount(int attentionCount) {
		this.attentionCount = attentionCount;
	}
	
	public void addAttentionCount1() {
		this.attentionCount ++;
	}
	public void minusAttentionCount1() {
		this.attentionCount --;
	}
	
	

}
