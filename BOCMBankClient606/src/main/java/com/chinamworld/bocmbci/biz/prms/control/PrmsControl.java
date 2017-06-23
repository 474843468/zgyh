package com.chinamworld.bocmbci.biz.prms.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.myaccount.PrmsAccSettingActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.RegCode;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贵金属控制中心
 * 
 * @author xyl
 * 
 */
public class PrmsControl {
	/** 判断是否401批次的功能 */
	public static boolean is401 = true;

	/** 判断是否603批次的功能 */
	public static boolean is603 = true;
	public static boolean isSale = false;
	
	private static PrmsControl instance = null;
	/**
	 * QueryInvtBindingInfo 账户信息
	 */
	public Map<String, String> accMessage = null;
	public String accId;
	public String accNum;
	// public String inveId;
	/** 市价即时 */
	public static final int PRMS_TRADEMETHOD_NOW = 0;
	/** 限价即时 */
	public static final int PRMS_TRADEMETHOD_LIMIT = 1;
	/** 获利委托 */
	public static final int PRMS_TRADEMETHOD_WIN = 2;
	/** 止损委托 */
	public static final int PRMS_TRADEMETHOD_LOSE = 3;
	/** 二选一委托 */
	public static final int PRMS_TRADEMETHOD_ONEINWTO = 4;
	
	/**追击止损委托**/
	public static final int PRMS_TRADEMETHOD_RUNLOST = 11;
	/**
	 * 是否开通中银理财
	 */
	public boolean ifInvestMent = false;
	/**
	 * 是否有贵金属账户
	 */
	public boolean ifhavPrmsAcc = false;

	public Map<String, Object> dataMap;
	// /** 判断是否第一次设定 */
	// public boolean isFirstSetPrmsAcc = true;
	/**
	 * 可设定为默认账户的贵金属账户
	 */
	public List<Map<String, String>> prmsAccList;
	/**
	 * 贵金属账户持仓
	 */
	public List<Map<String, Object>> accBalanceList;

	private List<Map<String, Object>> buyCurrencyList;
	private List<Map<String, Object>> saleCurrencyList;

	public List<Map<String, Object>> queryEntrustNowList;
	//方便贵金属成交状况查询，中的交易金额使用
	public Map<String, Object> queryDealMap;//成交状况数据字典

	private PrmsControl() {
		dataMap = new HashMap<String, Object>();
		accMessage = new HashMap<String, String>();
	}

	public static PrmsControl getInstance() {
		if (instance == null)
			instance = new PrmsControl();
		return instance;
	}

	/**
	 * 判断是否是第一次交易
	 * 
	 * @param context
	 * @return
	 */
	public static boolean ifFirstPrmsTrade(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConstantGloble.PRMS_FIRST_TRADE, Context.MODE_PRIVATE);
		if (sharedPreferences.getInt(ConstantGloble.PRMS_TRSDETIMES, 0) == 0) {
			return true;
		} else {
			return false;
		}
	}

//	/**
//	 * 没有登录的跳转
//	 *
//	 * @Author xyl
//	 */
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
	 * 没有贵金属账户的跳转
	 */
	public void noPrmsAcc() {
		Intent intent = new Intent();
		intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
				PrmsAccSettingActivity.class);
		BaseDroidApp
				.getInstanse()
				.getCurrentAct()
				.startActivityForResult(intent,
						ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE);
	}

	/**
	 * 没有开通中银理财
	 * 
	 * @Author xyl
	 */
	public void notInvestMent() {
		Intent intent = new Intent();
		intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
				PrmsAccSettingActivity.class);
		BaseDroidApp
				.getInstanse()
				.getCurrentAct()
				.startActivityForResult(intent,
						ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE);
	}

	/**
	 * 获取贵金属设定默认账户 step 信息
	 * 
	 * @Author xyl
	 * @return
	 */
	public String[] getStepsForAccSetting() {
		String step1 = 1 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources().getString(R.string.prms_accsetting_setp1);
		String step2 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.prms_accsetting_setp2);
		String step3 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.prms_accsetting_setp3);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}

	/**
	 * 贵金属交易的步骤信息
	 * 
	 * @Author xyl
	 * @return
	 */
	public String[] getStepsForPrmsTrade() {
		String step1 = 1 + BaseDroidApp.getInstanse().getCurrentAct()
				.getResources().getString(R.string.prms_trade_setp1);
		String step2 = 2 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.prms_trade_setp2);
		String step3 = 3 + BaseDroidApp.getInstanse().getCurrentAct()
				.getString(R.string.prms_trade_setp3);
		String[] steps = new String[3];
		steps[0] = step1;
		steps[1] = step2;
		steps[2] = step3;
		return steps;
	}

	public void cleanAll() {
		BaseDroidApp.getInstanse().getBizDataMap().remove(Prms.PRMS_PRICE);
		dataMap = null;
		accMessage = null;
		prmsAccList = null;
		queryEntrustNowList = null;
		queryDealMap = null;
	}

	/**
	 * 买入币种 支持 人民币 美元
	 * 
	 * @param balancList
	 * @return 账户余额列表
	 */
	public List<Map<String, Object>> getBuyCurrencyList(
			List<Map<String, Object>> balancList) {
		List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : balancList) {
			if (map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
					ConstantGloble.PRMS_CODE_RMB)
					|| // 人民币
					map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
							ConstantGloble.PRMS_CODE_DOLOR)) {// 美元
				tempList.add(map);
			}
		}
		setBuyCurrencyList(tempList);
		return tempList;
	}

	/**
	 * 根据持仓 获取 卖出时显示的列表项
	 * 
	 * @param balancList
	 * @return
	 */
	public List<Map<String, Object>> getSaleCurrencyList(
			List<Map<String, Object>> balancList) {
		List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : balancList) {
			if (map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
					ConstantGloble.PRMS_CURRENCYCODE_DOLORG)
					|| // 美元金
					map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
							ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
					|| // 美元银
					map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
							ConstantGloble.PRMS_CURRENCYCODE_RMBG)
					|| // 人民币金
					map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
							ConstantGloble.PRMS_CURRENCYCODE_RMBS)
					|| map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
							ConstantGloble.PRMS_CURRENCYCODE_RMBBAG) // 人民币钯金
					|| map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
							ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)// 美元钯金
					|| map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
							ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)// 美元铂金
					|| map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
							ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)// 美元铂金
			) {
				String balance = (String) map
						.get(Prms.QUERY_PEMSACTBALANCE_AVAILABLEBALANCE);
				if (Double.valueOf(balance) > 0) {
					tempList.add(map);
				}
			}
		}
		setSaleCurrencyList(tempList);
		return tempList;
	}

	/**
	 * 获取指定 币种的行情信息
	 * 
	 * @param sourcList
	 * @param currencyCode
	 * @return
	 */
	public static Map<String, String> getPrmsDetailsByCurrencyCode(
			List<Map<String, String>> sourcList, String currencyCode) {
		if (currencyCode.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBG)) {
			for (Map<String, String> map : sourcList) {
				final String sourceCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
				final String targetCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
				if (sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBG)
						&& targetCurrencyCode
								.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币金
					return map;
				}
			}
		}

		if (currencyCode.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORG)) {// 美元金
			for (Map<String, String> map : sourcList) {
				final String sourceCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
				final String targetCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
				if (sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORG)
						&& targetCurrencyCode
								.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元金
					return map;
				}
			}
		}
		if (currencyCode.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)) {// 人民币银
			for (Map<String, String> map : sourcList) {
				final String sourceCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
				final String targetCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
				if (sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)
						&& targetCurrencyCode
								.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币银
					return map;
				}
			}
		}
		if (currencyCode.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)) {// 美元银
			for (Map<String, String> map : sourcList) {
				final String sourceCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
				final String targetCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
				if (sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
						&& targetCurrencyCode
								.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元银
					return map;
				}
			}
		}
		if (currencyCode.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG)) {// 人民币钯金
			for (Map<String, String> map : sourcList) {
				final String sourceCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
				final String targetCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
				if (sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG)
						&& targetCurrencyCode
								.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币钯金
					return map;
				}
			}
		}
		if (currencyCode.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)) {// 美元钯金
			for (Map<String, String> map : sourcList) {
				final String sourceCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
				final String targetCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
				if (sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)
						&& targetCurrencyCode
								.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元钯金
					return map;
				}
			}
		}
		if (currencyCode.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)) {// 人民币铂金
			for (Map<String, String> map : sourcList) {
				final String sourceCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
				final String targetCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
				if (sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)
						&& targetCurrencyCode
								.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币铂金
					return map;
				}
			}
		}
		if (currencyCode.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)) {// 美元铂金
			for (Map<String, String> map : sourcList) {
				final String sourceCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
				final String targetCurrencyCode = (String) map
						.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
				if (sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)
						&& targetCurrencyCode
								.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元铂金
					return map;
				}
			}
		}

		return null;

	}

	public List<Map<String, Object>> getBuyCurrencyList() {
		getBuyCurrencyList(accBalanceList);
		return buyCurrencyList;
	}

	public void setBuyCurrencyList(List<Map<String, Object>> buyCurrencyList) {
		this.buyCurrencyList = buyCurrencyList;
	}

	public List<Map<String, Object>> getSaleCurrencyList() {
		return saleCurrencyList;
	}

	public void setSaleCurrencyList(List<Map<String, Object>> saleCurrencyList) {
		this.saleCurrencyList = saleCurrencyList;
	}

	public List<Map<String, Object>> getAccBalanceList() {
		return accBalanceList;
	}

	public void setAccBalanceList(List<Map<String, Object>> accBalanceList) {
		this.accBalanceList = accBalanceList;
	}

	/**
	 * @param targetCurrencyCode
	 *            目标币种
	 * @param tradeType
	 *            G 还是 S
	 * @return
	 */
	public String getCurrencyCode(String targetCurrencyCode, String tradeType) {
		if (targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)
				&& tradeType.equals(ConstantGloble.PRMS_TRADETYPE_GODE)) {// 人民币金
			return ConstantGloble.PRMS_CURRENCYCODE_RMBG;
		} else if (targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)
				&& tradeType.equals(ConstantGloble.PRMS_TRADETYPE_SILVER)) {// 人民币银
			return ConstantGloble.PRMS_CURRENCYCODE_RMBS;
		} else if (targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_DOLOR)
				&& tradeType.equals(ConstantGloble.PRMS_TRADETYPE_GODE)) {// 美元金
			return ConstantGloble.PRMS_CURRENCYCODE_DOLORG;
		} else if (targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_DOLOR)
				&& tradeType.equals(ConstantGloble.PRMS_TRADETYPE_SILVER)) {// 美元银
			return ConstantGloble.PRMS_CURRENCYCODE_DOLORS;
		}
		if (targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)
				&& tradeType.equals(ConstantGloble.PRMS_TRADETYPE_BAG)) {// 人民币
																			// 钯金
			return ConstantGloble.PRMS_CURRENCYCODE_RMBBAG;
		}
		if (targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_DOLOR)
				&& tradeType.equals(ConstantGloble.PRMS_TRADETYPE_BAG)) {// 美元
																			// 钯金
			return ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG;
		}
		if (targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)
				&& tradeType.equals(ConstantGloble.PRMS_TRADETYPE_BOG)) {// 人民币
																			// 铂金
			return ConstantGloble.PRMS_CURRENCYCODE_RMBBOG;
		} else { // 美元铂金
			return ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG;
		}
	}

	/**
	 * 根据行情查询币种列表
	 * 
	 * @param currencyList
	 * @return
	 */
	public List<String> getSourceCurrencyCode(
			List<Map<String, String>> currencyList) {
		List<String> resultList = new ArrayList<String>();
		for (Map<String, String> map : currencyList) {
			String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			if (sourceCurrencyCode != null) {
				resultList.add(sourceCurrencyCode);
			}
		}
		return resultList;
	}

	/**
	 * 贵金属行情 筛选
	 * 
	 * @param sourcList
	 * @param context
	 * @return
	 */
	public List<Map<String, String>> getDataList(
			List<Map<String, String>> sourcList, Context context) {
		String temp;
		List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
		for (Map<String, String> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBG)
					&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币金
				temp = context.getResources().getString(R.string.prms_rembg);
				map.put("temp", temp);
				tempList.add(map);
			}
		}

		for (Map<String, String> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORG)
					&& targetCurrencyCode
							.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元金
				temp = context.getResources().getString(R.string.prms_dolorg);
				map.put("temp", temp);
				tempList.add(map);

			}
		}
		for (Map<String, String> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)
					&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币银
				temp = context.getResources().getString(R.string.prms_rembs);
				map.put("temp", temp);
				tempList.add(map);
			}
		}
		for (Map<String, String> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
					&& targetCurrencyCode
							.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元银
				temp = context.getResources().getString(R.string.prms_dolors);
				map.put("temp", temp);
				tempList.add(map);
			}
		}

		for (Map<String, String> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)
					&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币铂金
				// temp = "人民币铂金/克";
				temp = context.getResources().getString(R.string.prms_rmbboG);
				map.put("temp", temp);
				tempList.add(map);
			}
		}
		for (Map<String, String> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)
					&& targetCurrencyCode
							.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元bo金
				// temp = "美元钯金/盎司";
				temp = context.getResources().getString(R.string.prms_dolorboG);
				map.put("temp", temp);
				tempList.add(map);
			}
		}
		for (Map<String, String> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG)
					&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币巴金
				// temp = "人民币钯金/克";
				temp = context.getResources().getString(R.string.prms_rmbbaG);
				map.put("temp", temp);
				tempList.add(map);
			}
		}
		for (Map<String, String> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)
					&& targetCurrencyCode
							.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元钯金
				// temp = "美元钯金/盎司";
				temp = context.getResources().getString(R.string.prms_dolorbaG);
				map.put("temp", temp);
				tempList.add(map);
			}
		}
		return tempList;

	}

	/**
	 * 贵金属行情未登录情况 筛选
	 * 
	 * @param sourcList
	 * @param context
	 * @return
	 */
	public List<Map<String, Object>> getPreLoginDataList(
			List<Map<String, Object>> sourcList, Context context) {
		String temp;
		List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBG)
					&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币金
				temp = context.getResources().getString(R.string.prms_rembg);
				map.put("temp", temp);
				tempList.add(map);
			}
		}

		for (Map<String, Object> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORG)
					&& targetCurrencyCode
							.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元金
				temp = context.getResources().getString(R.string.prms_dolorg);
				map.put("temp", temp);
				tempList.add(map);

			}
		}
		for (Map<String, Object> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)
					&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币银
				temp = context.getResources().getString(R.string.prms_rembs);
				map.put("temp", temp);
				tempList.add(map);
			}
		}
		for (Map<String, Object> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
					&& targetCurrencyCode
							.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元银
				temp = context.getResources().getString(R.string.prms_dolors);
				map.put("temp", temp);
				tempList.add(map);
			}
		}

		for (Map<String, Object> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)
					&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币铂金
				// temp = "人民币铂金/克";
				temp = context.getResources().getString(R.string.prms_rmbboG);
				map.put("temp", temp);
				tempList.add(map);
			}
		}
		for (Map<String, Object> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)
					&& targetCurrencyCode
							.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元bo金
				// temp = "美元钯金/盎司";
				temp = context.getResources().getString(R.string.prms_dolorboG);
				map.put("temp", temp);
				tempList.add(map);
			}
		}
		for (Map<String, Object> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG)
					&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币巴金
				// temp = "人民币钯金/克";
				temp = context.getResources().getString(R.string.prms_rmbbaG);
				map.put("temp", temp);
				tempList.add(map);
			}
		}
		for (Map<String, Object> map : sourcList) {
			final String sourceCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
			final String targetCurrencyCode = (String) map
					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
			if (sourceCurrencyCode
					.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)
					&& targetCurrencyCode
							.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元钯金
				// temp = "美元钯金/盎司";
				temp = context.getResources().getString(R.string.prms_dolorbaG);
				map.put("temp", temp);
				tempList.add(map);
			}
		}
		return tempList;

		

	}
	
	
	// public String getStyleBySourceCurrencyCode(String currency){
	// String resultStr = null;
	// if()
	//
	//
	// return resultStr;
	// }

	/**
	 * 根据贵金属源币种 格式化持仓 注意是持仓
	 * 
	 * @param sourceCurrencyCode
	 * @param text
	 */
	public static String parseStringPatternPrmAccBalance(
			String sourceCurrencyCode, String text) {
		if (sourceCurrencyCode.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBG)// 人民币金
																			// 整数
				|| sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)// 人民币银 整数
				|| sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG)// 人民币钯金整数
				|| sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)) // 人民币
																			// 铂金
																			// 整数
		{
			return text;
		} else if (sourceCurrencyCode
				.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORG)// 美元金 两位小数
				|| sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)// 美元银
																		// 两位小数
				|| sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)// 美元铂金
																			// 两位小数
				|| sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)) // 美元钯金两位小数
		{
			return StringUtil.append2Decimals(text, 2);
		}

		return StringUtil.parseStringPattern(text, 2);
	}

	/**
	 * 根据贵金属源币种 格式化 买入卖出数量
	 * 
	 * @param sourceCurrencyCode
	 * @param text
	 * @return
	 */
	public static String parseStringPatternTradeNum(String sourceCurrencyCode,
			String text) {
		if (sourceCurrencyCode == null) {
			return StringUtil.parseStringPattern(text, 1);
		}
		if (sourceCurrencyCode.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBG)// 人民币金
																			// 整数
				|| sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)// 人民币银整数
				|| sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG)// 人民币
																		// 钯金 整数
				|| sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)) {// 人民币铂金
																			// 整数
			return StringUtil.parseStringPattern(text, 0);
		}
		// else if (sourceCurrencyCode
		// .equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORG)//美元金 1位小数
		// || sourceCurrencyCode
		// .equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)) {// 美元银 1位小数
		// return StringUtil.parseStringPattern(text, 1);
		// }
		else {// 美元钯金铂金
			return StringUtil.parseStringPattern(text, 1);
		}
	}

	/**
	 * 按贵金属交易方式  返回
	 * @param tradeMethod
	 * @return
	 */
	public static int tradeMethodSwitch(String tradeMethod) {
		if (tradeMethod.equals(ConstantGloble.PRMS_TRADEMETHOD_NOW)) {// 市价即时
			return PRMS_TRADEMETHOD_NOW;
		} else if (tradeMethod.equals(ConstantGloble.PRMS_TRADEMETHOD_LIMIT)) {
			return PRMS_TRADEMETHOD_LIMIT;
		} else if (tradeMethod.equals(ConstantGloble.PRMS_TRADEMETHOD_WIN)) {
			return PRMS_TRADEMETHOD_WIN;
		} else if (tradeMethod.equals(ConstantGloble.PRMS_TRADEMETHOD_LOSE)) {
			return PRMS_TRADEMETHOD_LOSE;
		}else if(tradeMethod.equals(ConstantGloble.PRMS_TRADEMETHOD_RUNLOSE)){
			return PRMS_TRADEMETHOD_RUNLOST;//追击止损委托
		} 
		else
		// if(tradeMethod.equals(ConstantGloble.PRMS_TRADEMETHOD_ONEINWTO))
		{
			return PRMS_TRADEMETHOD_ONEINWTO;
		}
	}

	/**
	 * 
	 * @param sourceCurrency
	 *            币种
	 * @param rexTradeNumStr
	 *            买入数量/卖出数量
	 * @param tradeNumStr
	 * 			 用户输入的数量
	 * @return
	 */
	public static RegexpBean getRegexpBeanBySourceCurrency(
			String rexTradeNumStr, String sourceCurrency, String tradeNumStr) {
		RegexpBean regexpTradeNum;

		if (sourceCurrency.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORG)) {// 美元金格式校验
			regexpTradeNum = new RegexpBean(rexTradeNumStr, tradeNumStr,
					"bondAmount1");
		} else if (sourceCurrency
				.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)) {// 美元银格式校验
			regexpTradeNum = new RegexpBean(rexTradeNumStr, tradeNumStr,
					"bondAmount5");
		} else if (sourceCurrency.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBG)) {// 人民币金格式校验
			regexpTradeNum = new RegexpBean(rexTradeNumStr, tradeNumStr,
					"bondAmount10");

		} else if (sourceCurrency.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)) {// 人民币银格式校验
			regexpTradeNum = new RegexpBean(rexTradeNumStr, tradeNumStr,
					RegCode.BONDAMOUNT100);
		} else if (sourceCurrency
				.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG)
				|| sourceCurrency
						.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)) {
			regexpTradeNum = new RegexpBean(rexTradeNumStr, tradeNumStr,
					RegCode.BONDAMOUNTINT1);
		} else {
			regexpTradeNum = new RegexpBean(rexTradeNumStr, tradeNumStr,
					RegCode.BONDAMOUNTFLOAT1);
		}
		return regexpTradeNum;
	}

	/**
	 * 
	 * @param min
	 *            当前分钟
	 * @param hour
	 *            当前小时
	 * @return
	 */
	public static final int getMinEndHourByTime(String min, String hour) {
//		int intHour = Integer.valueOf(hour);
//		int intMin = Integer.valueOf(min);
//		if (intMin < 30) {
//			return intHour + 1;
//		} else {
//			return intHour + 2;
//		}
		return 0;
	}

	/**
	 * 如果价格是空 单位不显示
	 * 
	 * @param priceTv
	 *            价格
	 * @param unitTv
	 *            单位
	 */
	public static final void setUtilTvShow(TextView priceTv, TextView unitTv) {
		if (priceTv.getText().toString()
				.equals(ConstantGloble.BOCINVT_DATE_ADD)) {
			unitTv.setText("");
			priceTv.setTextColor(BaseDroidApp.getInstanse().getCurrentAct()
					.getResources().getColor(R.color.black));
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
	 * 贵金属交易金额单位 add by fsm
	 */
	public static Map<String, String> prmsTradeAmountUnit = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("844", "人民币元");
			put("845", "人民币元");
			put("841", "美元");
			put("045", "美元");
			put("035", "人民币元");
			put("068", "人民币元");
			put("034", "美元");
			put("036", "美元");
			put("001", "人民币元");
			put("014", "美元");
		}
	};
}