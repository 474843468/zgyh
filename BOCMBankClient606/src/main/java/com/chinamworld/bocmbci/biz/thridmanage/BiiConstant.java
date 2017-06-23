package com.chinamworld.bocmbci.biz.thridmanage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;

public class BiiConstant {

	/**
	 * 账户类型
	 */
	public static class AccoutType {
		private static Map<String, String> ACCOUT_TYPE_MAP = new HashMap<String, String>();

		/** 中银信用卡 */
		public static final String ZHONG_YIN_CARD_CODE = "103";
		/** 长城信用卡 */
		public static final String CHANGCHENG_CARD_CODE = "104";
		/** 长城电子借记卡 */
		public static final String CHANGCHENG_DIANZI_CARD_CODE = "119";

		/**
		 * 返回账户类型
		 * 
		 * @param type
		 *            卡类型code
		 * @return
		 */
		public static String getAccoutTypeStr(String accoutType) {
			if (ACCOUT_TYPE_MAP.isEmpty()) {
				ACCOUT_TYPE_MAP.put("101", "普通活期");
				ACCOUT_TYPE_MAP.put(ZHONG_YIN_CARD_CODE, "中银信用卡");
				ACCOUT_TYPE_MAP.put(CHANGCHENG_CARD_CODE, "长城信用卡");
				ACCOUT_TYPE_MAP.put(CHANGCHENG_DIANZI_CARD_CODE, "长城电子借记卡");
				ACCOUT_TYPE_MAP.put("107", "单外币信用卡");
				ACCOUT_TYPE_MAP.put("108", "虚拟卡(贷记)");
				ACCOUT_TYPE_MAP.put("109", "虚拟卡(准贷记)");
				ACCOUT_TYPE_MAP.put("140", "存本取息");
				ACCOUT_TYPE_MAP.put("150", "零存整取");
				ACCOUT_TYPE_MAP.put("152", "教育储蓄");
				ACCOUT_TYPE_MAP.put("170", "定期一本通");
				ACCOUT_TYPE_MAP.put("188", "活期一本通");
				ACCOUT_TYPE_MAP.put("190", "网上专属理财账户");
				ACCOUT_TYPE_MAP.put("300", "电子现金账户");
			}
			if (ACCOUT_TYPE_MAP.containsKey(accoutType)) {
				return ACCOUT_TYPE_MAP.get(accoutType);
			}
			return "";
		}
	}

	/**
	 * 币种
	 */
	public static class CurrencyType {
		/** 人民币 */
		public static final String RMB = "001";

		private static Map<String, String> CURRENCY_TYPE_MAP = new HashMap<String, String>();

		public static String getCurrencyTypeStr(String currencyType) {
			if (CURRENCY_TYPE_MAP.isEmpty()) {
				CURRENCY_TYPE_MAP.put(RMB, getString(R.string.tran_currency_rmb));
				CURRENCY_TYPE_MAP.put("014", "美元");
				CURRENCY_TYPE_MAP.put("013", "港币元");
				CURRENCY_TYPE_MAP.put("012", "英镑");
				CURRENCY_TYPE_MAP.put("038", "欧元");
				CURRENCY_TYPE_MAP.put("027", "日元");
				CURRENCY_TYPE_MAP.put("028", "加拿大元");
				CURRENCY_TYPE_MAP.put("029", "澳大利亚元");
				CURRENCY_TYPE_MAP.put("015", "瑞士法郎");
				CURRENCY_TYPE_MAP.put("018", "新加坡元");
				CURRENCY_TYPE_MAP.put("081", "澳门元");
			}
			if (CURRENCY_TYPE_MAP.containsKey(currencyType)) {
				return CURRENCY_TYPE_MAP.get(currencyType);
			}
			return "";
		}
	}

	/**
	 * 转账方式
	 */
	public static class TransferWayType {
		/** 全部 */
		public static final String ALL = "A";
		/** 银行资金转证券保证金 */
		public static final String BANK_TO_STOCK_CODE = "C";
		/** 证券保证金转银行资金 */
		public static final String STOCK_TO_BANK_CODE = "D";

		private static LinkedHashMap<String, String> TRANS_WAY_TYPE_MAP = new LinkedHashMap<String, String>();
		private static LinkedHashMap<String, String> TRANS_WAY_TYPE_SIMPLE_MAP = new LinkedHashMap<String, String>();

		static {
			// 全部
			TRANS_WAY_TYPE_MAP.put(ALL, getString(R.string.all));
			// 银行资金转证券保证金
			TRANS_WAY_TYPE_MAP.put(BANK_TO_STOCK_CODE, getString(R.string.third_btn_banktocecurity));
			// 证券保证金转银行资金
			TRANS_WAY_TYPE_MAP.put(STOCK_TO_BANK_CODE, getString(R.string.third_btn_cecuritytobank));

			// 全部
			TRANS_WAY_TYPE_SIMPLE_MAP.put(ALL, getString(R.string.all));
			// 银行资金转证券保证金
			TRANS_WAY_TYPE_SIMPLE_MAP.put(BANK_TO_STOCK_CODE, getString(R.string.bank_to_cecurity));
			// 证券保证金转银行资金
			TRANS_WAY_TYPE_SIMPLE_MAP.put(STOCK_TO_BANK_CODE, getString(R.string.cecurity_to_bank));
		}

		/**
		 * 返回转账方式描述
		 * 
		 * @param transferWayTypeCode
		 *            转账方式代码
		 */
		public static String getTransferWayTypeStr(String transferWayTypeCode) {
			if (TRANS_WAY_TYPE_MAP.containsKey(transferWayTypeCode)) {
				return TRANS_WAY_TYPE_MAP.get(transferWayTypeCode);
			}
			return "";
		}

		/**
		 * 返回简写转账方式描述
		 * 
		 * @param transferWayTypeCode
		 *            转账方式代码
		 */
		public static String getTransferWayTypeSimpleStr(String transferWayTypeCode) {
			if (TRANS_WAY_TYPE_SIMPLE_MAP.containsKey(transferWayTypeCode)) {
				return TRANS_WAY_TYPE_SIMPLE_MAP.get(transferWayTypeCode);
			}
			return "";
		}

		/**
		 * 通过描述返回代码表示
		 * 
		 * @param 转账方式描述
		 * @return 代码表示 如果描述为空返回null。 如：全部->A | 银行资金转证券保证金 或 银转证 ->C
		 */
		public static String getTransferWayTypeCode(String transferWayType) {
			for (Entry<String, String> map : TRANS_WAY_TYPE_MAP.entrySet()) {
				if (map.getValue().equals(transferWayType)) {
					return map.getKey();
				}
			}
			for (Entry<String, String> simpleMap : TRANS_WAY_TYPE_SIMPLE_MAP.entrySet()) {
				if (simpleMap.getValue().equals(transferWayType)) {
					return simpleMap.getKey();
				}
			}
			return "";
		}

		public static String[] getSortTransferWayTypeList() {
			Collection<String> values = TRANS_WAY_TYPE_SIMPLE_MAP.values();
			String[] sort = new String[values.size()];
			String[] result = values.toArray(sort);
			return result;
		}

	}

	/**
	 * 交易渠道
	 */
	public static class TransferChannelType {

		private static Map<String, String> TRANSFER_CHANNEL_TYPE = new HashMap<String, String>();

		public static String getTransferChannelTypeStr(String transferChannelType) {
			if (TRANSFER_CHANNEL_TYPE.isEmpty()) {
				TRANSFER_CHANNEL_TYPE.put("56", "BOCNET");
				TRANSFER_CHANNEL_TYPE.put("42", "CALLCENTER");
				TRANSFER_CHANNEL_TYPE.put("AM", "自助终端");
				TRANSFER_CHANNEL_TYPE.put("43", "短信平台");
				TRANSFER_CHANNEL_TYPE.put("B2", "BOC2000");
				TRANSFER_CHANNEL_TYPE.put("10", "银行柜台");
				TRANSFER_CHANNEL_TYPE.put("ST", "证券公司");
			}
			if (TRANSFER_CHANNEL_TYPE.containsKey(transferChannelType)) {
				return TRANSFER_CHANNEL_TYPE.get(transferChannelType);
			}
			return "";
		}
	}

	/**
	 * 交易状态
	 */
	public static class TransactionStatus {
		/** 交易成功 */
		public static final String SUCCESS_CODE = "0";
		/** 交易失败 */
		public static final String FAIL_CODE = "5";
		/** 柜台交易处理中 */
		public static final String COUNTER_CODE = "H";
		private static Map<String, String> TRANSACTION_STATUS_TYPE = new HashMap<String, String>();
		static {
			TRANSACTION_STATUS_TYPE.put(SUCCESS_CODE, "交易成功");
			TRANSACTION_STATUS_TYPE.put("4", "交易失败");
			TRANSACTION_STATUS_TYPE.put(FAIL_CODE, "交易失败");
			TRANSACTION_STATUS_TYPE.put(COUNTER_CODE, "柜台交易处理中");
			TRANSACTION_STATUS_TYPE.put("R", "柜台交易处理中");
		}

		public static String getTransactionStatusStr(String transactionStatusCode) {
			if (TRANSACTION_STATUS_TYPE.containsKey(transactionStatusCode)) {
				return TRANSACTION_STATUS_TYPE.get(transactionStatusCode);
			}
			return "";
		}
	}

	/**
	 * @ClassName: IdentifyType
	 * @Description: 证件类型
	 * @author lql
	 * @date 2013-8-19 上午10:29:11
	 */
	public static class IdentifyType {
		/** 身份证 */
		public static final String IDENTIFICATION_CARD_CODE = "1";
		private static Map<String, String> IDENTIFY_TYPE_MAP = new HashMap<String, String>();
		static {
			IDENTIFY_TYPE_MAP.put(IDENTIFICATION_CARD_CODE, "身份证");
			IDENTIFY_TYPE_MAP.put("2", "临时居民身份证");
			IDENTIFY_TYPE_MAP.put("3", "户口簿");
			IDENTIFY_TYPE_MAP.put("4", "军人身份证");
			IDENTIFY_TYPE_MAP.put("5", "武装警察身份证");
			IDENTIFY_TYPE_MAP.put("6", "港澳居民通行证");
			IDENTIFY_TYPE_MAP.put("7", "台湾居民通行证");
			IDENTIFY_TYPE_MAP.put("8", "护照");
			IDENTIFY_TYPE_MAP.put("9", "其他证件");
			IDENTIFY_TYPE_MAP.put("10", "港澳台居民往来内地通行证");
			IDENTIFY_TYPE_MAP.put("11", "外交人员身份证");
			IDENTIFY_TYPE_MAP.put("12", "外国人居留许可证");
			IDENTIFY_TYPE_MAP.put("13", "边民出入境通行证");
			IDENTIFY_TYPE_MAP.put("14", "其他");
		}

		public static String getIdentifyTypeStr(String identifyTypeCode) {
			if (IDENTIFY_TYPE_MAP.containsKey(identifyTypeCode)) {
				return IDENTIFY_TYPE_MAP.get(identifyTypeCode);
			}
			return "";
		}
	}

	/**
	 * @ClassName: BookingStatus
	 * @Description: 预约状态
	 * @author lql
	 * @date 2013-8-21 下午04:38:11
	 * 
	 */
	public static class BookingStatus {
		private static Map<String, String> BOOKING_STATUS_MAP = new HashMap<String, String>();
		static {
			BOOKING_STATUS_MAP.put("0", "已开户");
			BOOKING_STATUS_MAP.put("1", "已预约");
		}

		public static String getBookingStatusStr(String bookingStatus) {
			if (BOOKING_STATUS_MAP.containsKey(bookingStatus)) {
				return BOOKING_STATUS_MAP.get(bookingStatus);
			}
			return "";
		}
	}

	public static class ThridProvinceType {
		static class ProvinceItem {
			public ProvinceItem(String oldCode, String newCode, String province) {
				this.oldCode = oldCode;
				this.newCode = newCode;
				this.province = province;
			}

			String oldCode;
			String newCode;
			String province;
		}

		private static List<ProvinceItem> provinceItems = new ArrayList<ProvinceItem>();

		private static void init() {
			if (provinceItems.isEmpty()) {
				provinceItems.add(new ProvinceItem("00002", "40142", "北京"));
				provinceItems.add(new ProvinceItem("00003", "40202", "天津"));
				provinceItems.add(new ProvinceItem("00004", "40740", "河北"));
				provinceItems.add(new ProvinceItem("00005", "41041", "山西"));
				provinceItems.add(new ProvinceItem("00006", "41405", "内蒙古"));
				provinceItems.add(new ProvinceItem("00007", "41785", "辽宁"));
				provinceItems.add(new ProvinceItem("00008", "42208", "吉林"));
				provinceItems.add(new ProvinceItem("00009", "42465", "黑龙江"));
				provinceItems.add(new ProvinceItem("00010", "40303", "上海"));
				provinceItems.add(new ProvinceItem("00011", "44433", "江苏"));
				provinceItems.add(new ProvinceItem("00012", "45129", "浙江"));
				provinceItems.add(new ProvinceItem("00013", "44899", "安徽"));
				provinceItems.add(new ProvinceItem("00014", "45481", "福建"));
				provinceItems.add(new ProvinceItem("00015", "47370", "江西"));
				provinceItems.add(new ProvinceItem("00016", "43810", "山东"));
				provinceItems.add(new ProvinceItem("00017", "46243", "河南"));
				provinceItems.add(new ProvinceItem("00018", "46405", "湖北"));
				provinceItems.add(new ProvinceItem("00019", "46955", "湖南"));
				provinceItems.add(new ProvinceItem("00020", "47504", "广东（不含深圳）"));
				provinceItems.add(new ProvinceItem("00021", "48051", "广西"));
				provinceItems.add(new ProvinceItem("00022", "47806", "海南"));
				provinceItems.add(new ProvinceItem("00023", "48631", "四川"));
				provinceItems.add(new ProvinceItem("00024", "48882", "贵州"));
				provinceItems.add(new ProvinceItem("00025", "49146", "云南"));
				provinceItems.add(new ProvinceItem("00026", "40600", "西藏"));
				provinceItems.add(new ProvinceItem("00027", "43016", "陕西"));
				provinceItems.add(new ProvinceItem("00028", "43251", "甘肃"));
				provinceItems.add(new ProvinceItem("00029", "43469", "青海"));
				provinceItems.add(new ProvinceItem("00030", "43347", "宁夏"));
				provinceItems.add(new ProvinceItem("00031", "43600", "新疆"));
				provinceItems.add(new ProvinceItem("00032", "48642", "重庆"));
				provinceItems.add(new ProvinceItem("00033", "47669", "深圳"));
			}
		}

		/**
		 * 获取所有地区代码
		 * 
		 * @return 所有地区代码
		 */
		public static List<String> getOldProvincesCode() {
			init();
			List<String> provincesCode = new ArrayList<String>();
			for (ProvinceItem item : provinceItems) {
				provincesCode.add(item.oldCode);
			}
			return provincesCode;
		}

		/**
		 * 根据地区获取code代码
		 * 
		 * @param provincesCode 地区
		 * @return 地区代码,如果不包含返回空
		 */
		public static String getOldProvincesName(String provincesOldCode) {
			init();
			for (ProvinceItem item : provinceItems) {
				if (item.oldCode.equals(provincesOldCode)) {
					return item.province;
				}
			}
			return "";
		}

		/**
		 * 比较新code和旧code是否通一个地区
		 * 
		 * @param 旧code
		 * @param 新code
		 * @return
		 */
		public static boolean isProvinceByCode(String oldCode, String newCode) {
			init();
			for (ProvinceItem item : provinceItems) {
				if (item.oldCode.equals(oldCode) && item.newCode.equals(newCode)) {
					return true;
				}
			}
			return false;
		}
	}

	private static String getString(int resId) {
		String string = BaseDroidApp.getContext().getString(resId);
		if (string != null) {
			return string;
		}
		return "null";
	}
}
