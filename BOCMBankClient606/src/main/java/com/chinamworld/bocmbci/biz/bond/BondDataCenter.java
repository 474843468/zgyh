package com.chinamworld.bocmbci.biz.bond;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;

import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.utils.StringUtil;

public class BondDataCenter {

	private static BondDataCenter instance;
	/** 特定activity管理 */
	private ArrayList<Activity> acList;
	/** 系统时间 */
	private String sysTime;
	/** 债券行情列表 */
	private List<Map<String, Object>> bondList;
	/** 债券详情 */
	private Map<String, Object> bondMap;
	/** 卖出信息 */
	private Map<String, Object> myBondDetailMap;
	/** 卖出预交易返回信息 */
	private Map<String, Object> sellConfirmMap;
	/** 历史交易详情 */
	private Map<String, Object> historyDetailMap;
	/** 买入预交易返回结果 */
	private Map<String, Object> buyConfirmResult;
	/** 买入提交返回结果 */
	private Map<String, Object> buyComitResult;
	/** 购买下拉框数据 */
	private List<String> nameList;
	/** 账号信息 */
	private Map<String, Object> accMap;
	/** 客户信息 */
	private Map<String, Object> customInfoMap;
	/** 资金账户列表 */
	private List<Map<String, Object>> bankAccList;
	/** 开户信息 */
	private Map<String, Object> openBondCustomerInfoMap;
	/** 开户确认返回数据 */
	private Map<String, Object> openBondConfirm;
	private boolean isResetup = false;
	/** 买入操作 */
	private boolean isBuy = false;

	public static BondDataCenter getInstance() {
		if (instance == null) {
			instance = new BondDataCenter();
		}
		return instance;
	}

	public String getSysTime() {
		return sysTime;
	}

	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	public List<Map<String, Object>> getBondList() {
		return bondList;
	}

	public void setBondList(List<Map<String, Object>> bondList) {
		this.bondList = bondList;
	}

	public Map<String, Object> getBondMap() {
		return bondMap;
	}

	public void setBondMap(Map<String, Object> bondMap) {
		this.bondMap = bondMap;
	}

	public Map<String, Object> getMyBondDetailMap() {
		return myBondDetailMap;
	}

	public void setMyBondDetailMap(Map<String, Object> myBondDetailMap) {
		this.myBondDetailMap = myBondDetailMap;
	}

	public Map<String, Object> getSellConfirmMap() {
		return sellConfirmMap;
	}

	public void setSellConfirmMap(Map<String, Object> sellConfirmMap) {
		this.sellConfirmMap = sellConfirmMap;
	}

	public Map<String, Object> getHistoryDetailMap() {
		return historyDetailMap;
	}

	public void setHistoryDetailMap(Map<String, Object> historyDetailMap) {
		this.historyDetailMap = historyDetailMap;
	}

	public Map<String, Object> getBuyConfirmResult() {
		return buyConfirmResult;
	}

	public void setBuyConfirmResult(Map<String, Object> buyConfirmResult) {
		this.buyConfirmResult = buyConfirmResult;
	}

	public List<String> getNameList() {
		return nameList;
	}

	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}

	public Map<String, Object> getAccMap() {
		return accMap;
	}

	public void setAccMap(Map<String, Object> accMap) {
		this.accMap = accMap;
	}

	public Map<String, Object> getBuyComitResult() {
		return buyComitResult;
	}

	public void setBuyComitResult(Map<String, Object> buyComitResult) {
		this.buyComitResult = buyComitResult;
	}

	public Map<String, Object> getCustomInfoMap() {
		return customInfoMap;
	}

	public void setCustomInfoMap(Map<String, Object> customInfoMap) {
		this.customInfoMap = customInfoMap;
	}

	public List<Map<String, Object>> getBankAccList() {
		return bankAccList;
	}

	public void setBankAccList(List<Map<String, Object>> bankAccList) {
		this.bankAccList = bankAccList;
	}

	public Map<String, Object> getOpenBondCustomerInfoMap() {
		return openBondCustomerInfoMap;
	}

	public void setOpenBondCustomerInfoMap(
			Map<String, Object> openBondCustomerInfoMap) {
		this.openBondCustomerInfoMap = openBondCustomerInfoMap;
	}

	public Map<String, Object> getOpenBondConfirm() {
		return openBondConfirm;
	}

	public void setOpenBondConfirm(Map<String, Object> openBondConfirm) {
		this.openBondConfirm = openBondConfirm;
	}

	public boolean isResetup() {
		return isResetup;
	}

	public void setResetup(boolean isResetup) {
		this.isResetup = isResetup;
	}

	public boolean isBuy() {
		return isBuy;
	}

	public void setBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}

	/** 债券类型 1--存储式 2--记账式 目前只做记账式 */
	public static final List<String> bondType_re = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("1");
			add("2");
		}
	};
	// 行情 ----与持仓表示刚好相反
	public static final Map<String, String> bondType_hq = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("1", "储蓄国债");
			put("2", "记账式债券");
		}
	};
	// 持仓
	public static final Map<String, String> bondType_cc = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("1", "记账式债券");
			put("2", "储蓄国债");
		}
	};

	/*** 交易类型 */
	public static final Map<String, String> tranType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("06", "申购");
			put("07", "买入");
		}
	};

	/** 利息类型 **/
	public static final Map<String, String> bondInterestType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("S", "标准");
			put("C", "固定");
			put("D", "贴现");
			put("G", "分段");
			put("F", "浮动");
		}
	};

	/** 新利息类型 **/
	public static final Map<String, String> bondInterestTypeNew = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("S", "标准利率");
			put("C", "固定利率");
			put("D", "贴现利率");
			put("G", "分段利率");
			put("F", "浮动利率");
		}
	};
	
	/*** 债券状态 **/
	public static final Map<String, String> bondStatus = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("Y", "可买卖");
			put("N", "可申购");
		}
	};

	public List<String> bondType() {
		ArrayList<String> bType = new ArrayList<String>();
		bType.add("记账式债券");
		return bType;
	}

	/** 初始化购买债券下拉框 */
	public void initBondName(List<Map<String, Object>> bondList) {
		ArrayList<String> nameList = new ArrayList<String>();
		for (int i = 0; i < bondList.size(); i++) {
			nameList.add(bondList.get(i).get(Bond.BOND_CODE) + "/"
					+ bondList.get(i).get(Bond.BOND_SHORTNAME));
		}
		setNameList(nameList);
	}

	/** popupwd更多 */
	public static String[] btnMore = { "注销债券账户", "重置查询密码" };

	/** 账户类型 */
	public static List<String> accountTypeList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("101");//
			add("103");
			add("104");
			add("119");//
			add("107");
			add("108");
			add("109");
			add("140");
			add("150");
			add("152");
			add("170");
			add("188");//
			add("190");
			add("300");

		};
	};

	/** 重置查询密码证件类型 **/
	public static final Map<String, String> identityType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("00", "居民身份证");
			put("01", "临时居民身份证");
			put("02", "户口簿");
			put("10", "军官证");
			put("11", "警官证");
			put("12", "文职证");
			put("13", "士兵证");
			put("14", "军事院校学院证");
			put("15", "离休干部荣誉证");
			put("16", "军官退休证");
			put("17", "文职干部退休证");
			put("20", "中国护照");
			put("21", "外国公民护照");
			put("22", "港澳通行证");
			put("23", "台湾通行证");
			put("99", "其它");
		}
	};

	/** 开户证件类型 */
	public static Map<String, String> IDENTITYTYPE = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("1", "身份证");
			put("2", "临时居民身份证");
			put("3", "户口簿");
			put("4", "军人身份证");
			put("5", "武装警察身份证");
			put("6", "港澳居民通行证");
			put("7", "台湾居民通行证");
			put("8", "护照");
			put("9", "其他证件");
			put("10", "港澳台居民往来内地通行证");
			put("11", "外交人员身份证");
			put("12", "外国人居留许可证");
			put("13", "边民出入境通行证");
			put("14", "营业执照");
			put("47", "港澳居民来往内地通行证（香港）");
			put("48", "港澳居民来往内地通行证（澳门）");
			put("49", "台湾居民来往大陆通行证");
			put("50", "机构信用代码");
			put("99", "其他");
		}
	};

	/** 开户债券类型 0存储和记账 1存储式 2记账式 目前只做记账式 */
	public static final List<String> bondAcctType_re = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("0");
			add("1");
			add("2");
		}
	};

	/** 性别 **/
	public static final Map<String, String> gender_hq = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("0", "女");
			put("1", "男");
		}
	};
	/** 国籍 **/
	public static final Map<String, String> countryCode = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("CN", "中国");
			put("AF", "阿富汗");
			put("AL", "阿尔巴尼亚");
			put("AO", "安哥拉");
			put("AR", "阿根廷");
			put("AU", "澳大利亚");
			put("AZ", "阿塞拜疆");
			put("BE", "比利时");
			put("BH", "巴林");
			put("BI", "布隆迪");
			put("BJ", "贝宁");
			put("BK", "巴哈马");
			put("BT", "不丹");
			put("BY", "白俄罗斯");
			put("BZ", "伯利兹");
			put("CA", "加拿大");
			put("CF", "中非");
			put("CG", "刚果");
			put("CH", "瑞士");
			put("CK", "库克群岛");
			put("CL", "智利");
			put("CM", "喀麦隆");
			put("CO", "哥伦比亚");
			put("CR", "哥斯达黎加");
			put("CU", "古巴");
			put("CZ", "捷克");
			put("EE", "爱沙尼亚");
			put("EG", "埃及");
			put("EH", "西撒哈拉");
			put("EN", "英国");
			put("ET", "埃塞俄比亚");
			put("FI", "芬兰");
			put("FJ", "斐济");
			put("FR", "法国");
			put("GA", "加蓬");
			put("GD", "格林纳达");
			put("GF", "法属圭亚那");
			put("GH", "加纳");
			put("GI", "直布罗陀");
			put("GL", "格陵兰");
			put("GM", "冈比亚");
			put("GN", "几内亚");
			put("GP", "瓜德罗普");
			put("GR", "希腊");
			put("GU", "关岛");
			put("GW", "几内亚比绍");
			put("HK", "香港");
			put("HN", "洪都拉斯");
			put("HR", "克罗地亚");
			put("HT", "海地");
			put("HU", "匈牙利");
			put("ID", "印度尼西亚");
			put("IL", "以色列");
			put("IN", "印度");
			put("IQ", "伊拉克");
			put("IR", "伊朗");
			put("IS", "冰岛");
			put("IT", "意大利");
			put("JM", "牙买加");
			put("JP", "日本");
			put("KE", "肯尼亚");
			put("KH", "柬埔寨");
			put("KI", "基里巴斯");
			put("KM", "科摩罗");
			put("KN", "圣基茨和尼维斯");
			put("KP", "朝鲜");
			put("KR", "韩国");
			put("KW", "科威特");
			put("KY", "开曼群岛");
			put("KZ", "哈萨克斯坦");
			put("LA", "老挝");
			put("LB", "黎巴嫩");
			put("LI", "列支敦士登");
			put("LK", "斯里兰卡");
			put("LR", "利比里亚");
			put("LS", "莱索托");
			put("LT", "立陶宛");
			put("LU", "卢森堡");
			put("LV", "拉脱维亚");
			put("LY", "利比亚");
			put("MA", "摩洛哥");
			put("MC", "摩纳哥");
			put("MG", "马达加斯加");
			put("MH", "马绍尔群岛");
			put("ML", "马里");
			put("MN", "蒙古");
			put("MO", "澳门");
			put("MP", "北马里亚纳");
			put("MQ", "马提尼克");
			put("MR", "毛里塔尼亚");
			put("MS", "蒙特塞拉特");
			put("MT", "马耳他");
			put("MU", "毛里求斯");
			put("MV", "马尔代夫");
			put("MW", "马拉维");
			put("MX", "墨西哥");
			put("MY", "马来西亚");
			put("MZ", "莫桑比克");
			put("NA", "纳米比亚");
			put("NC", "新喀里多尼亚");
			put("NE", "尼日尔");
			put("NG", "尼日利亚");
			put("NI", "尼加拉瓜");
			put("NL", "荷兰");
			put("NO", "挪威");
			put("NP", "尼泊尔");
			put("NR", "瑙鲁");
			put("NU", "纽埃");
			put("NZ", "新西兰");
			put("OM", "阿曼");
			put("PA", "巴拿马");
			put("PE", "秘鲁");
			put("PF", "法属波利尼西亚");
			put("PG", "巴布亚新几内亚");
			put("PH", "菲律宾");
			put("PK", "巴基斯坦");
			put("PL", "波兰");
			put("PR", "波多黎各");
			put("PT", "葡萄牙");
			put("PY", "巴拉圭");
			put("QA", "卡塔尔");
			put("RE", "留尼汪");
			put("RO", "罗马尼亚");
			put("RU", "俄罗斯");
			put("RW", "卢旺达");
			put("SA", "沙特阿拉伯");
			put("SB", "所罗门群岛");
			put("SC", "塞舌尔");
			put("SD", "苏丹");
			put("SG", "新加坡");
			put("SH", "圣赫勒拿");
			put("SI", "斯洛文尼亚");
			put("SJ", "斯瓦尔巴群岛");
			put("SK", "斯洛伐克");
			put("SL", "塞拉利昂");
			put("SM", "圣马力诺");
			put("SN", "塞内加尔");
			put("SO", "索马里");
			put("SR", "苏里南");
			put("SY", "叙利亚");
			put("SZ", "斯威士兰");
			put("TC", "特克斯科斯群岛");
			put("TD", "乍得");
			put("TG", "多哥");
			put("TH", "泰国");
			put("TK", "托克劳");
			put("TM", "土库曼斯坦");
			put("TN", "突尼斯");
			put("TO", "汤加");
			put("TP", "东帝汶");
			put("TR", "土耳其");
			put("TT", "特立尼达和多巴哥");
			put("TV", "图瓦卢");
			put("TW", "台湾");
			put("TZ", "坦桑尼亚");
			put("UA", "乌克兰");
			put("US", "美国");
			put("UY", "乌拉圭");
			put("UZ", "乌兹别克斯坦");
			put("VA", "梵蒂冈");
			put("VE", "委内瑞拉");
			put("VI", "美属维尔京群岛");
			put("VU", "瓦努阿图");
			put("VF", "瓦利斯和富图纳群岛");
			put("YE", "也门");
			put("YU", "南斯拉夫");
			put("ZA", "南非");
			put("ZM", "赞比亚");
			put("ZR", "扎伊尔");
			put("ZW", "津巴布韦");
		}
	};

	/** 管理买入、卖出信息填写页Activity */
	public void addActivity(Activity ac) {
		if (acList == null) {
			acList = new ArrayList<Activity>();
		}
		if (!acList.contains(ac)) {
			acList.add(ac);
		}
	}

	public void finshActivity() {
		if (acList != null && acList.size() != 0) {
			for (int i = 0; i < acList.size(); i++) {
				acList.get(i).finish();
			}
		}
	}

	/** 清除所有临时存储数据 **/
	public void clearAllData() {
		if (!StringUtil.isNullOrEmpty(acList)) {
			acList.clear();
		}
		if (!StringUtil.isNullOrEmpty(bondList)) {
			bondList.clear();
		}
		if (!StringUtil.isNullOrEmpty(bondMap)) {
			bondMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(myBondDetailMap)) {
			myBondDetailMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(sellConfirmMap)) {
			sellConfirmMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(historyDetailMap)) {
			historyDetailMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(buyConfirmResult)) {
			buyConfirmResult.clear();
		}
		if (!StringUtil.isNullOrEmpty(buyComitResult)) {
			buyComitResult.clear();
		}
		if (!StringUtil.isNullOrEmpty(nameList)) {
			nameList.clear();
		}
		if (!StringUtil.isNullOrEmpty(accMap)) {
			accMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(customInfoMap)) {
			customInfoMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(bankAccList)) {
			bankAccList.clear();
		}
		if (!StringUtil.isNullOrEmpty(openBondCustomerInfoMap)) {
			openBondCustomerInfoMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(openBondConfirm)) {
			openBondConfirm.clear();
		}
//		if (!StringUtil.isNullOrEmpty(countryCode)) {
//			countryCode.clear();
//		}
		isBuy = false;
	}

}
