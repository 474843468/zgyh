package com.chinamworld.bocmbci.constant;

import java.security.Key;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.utils.KeyAndValueItem;

/**
 * 数据字典
 * @author yuht
 *
 */
public class DictionaryData {



	
	//[start] 字典对外提供的方法
	
	/**
	 * 通过 key在指定的数据列表中找到对应 的Item项
	 * @param key
	 * @param dataList
	 * @return
	 */
	public static KeyAndValueItem getKeyAndValueItemByKey(String key,List<KeyAndValueItem> dataList){
		for(KeyAndValueItem item : dataList){
			if(item.getKey().equals(key))
				return  item;
		}
		return null;
	}
	
	/**
	 * 通过 value在指定的数据列表中找到对应 的Item项
	 * @param value
	 * @param dataList
	 * @return
	 */
	public static KeyAndValueItem getKeyAndValueItemByValue(String value,List<KeyAndValueItem> dataList){
		for(KeyAndValueItem item : dataList){
			if(item.getValue().equals(value))
				return  item;
		}
		return null;
	}
	 
	/**
	 * 通过 key在指定的数据列表中找到对应 的Value值
	 * @param key
	 * @param dataList
	 * @return
	 */
	public static String getValueByKey(String key,List<KeyAndValueItem> dataList){
		KeyAndValueItem  item=getKeyAndValueItemByKey(key, dataList);
		if(item == null)
			return null;
		return item.getValue();
	}
	
	/**
	 * 通过 value在指定的数据列表中找到对应 的Key值
	 * @param value
	 * @param dataList
	 * @return
	 */
	public static String getKeyByValue(String value,List<KeyAndValueItem> dataList){
		KeyAndValueItem  item = getKeyAndValueItemByValue(value, dataList);
		if(item==null)
			return null;
		return item.getKey();
	}

	/**
	 * 通过 Key 和 value在指定的数据列表中找到对应KeyAndValueItem
	 * @param key
	 * @param value
	 * @param dataList
	 * @return
	 */
	public static KeyAndValueItem getItemByKeyAndValue(String key,String value,List<KeyAndValueItem> dataList){
		for(KeyAndValueItem item : dataList){
			if(item.getKey().equals(key) && item.getValue().equals(value))
				return item;
		}
		return null;
	}

	/**
	 * 通过 Key 和 value在指定的数据列表中找到对应 的Param值
	 * @param key
	 * @param value
	 * @param dataList
	 * @return
	 */
	public static Object getParamByKeyAndValue(String key,String value,List<KeyAndValueItem> dataList){
		KeyAndValueItem item=  getItemByKeyAndValue(key,value,dataList);
		if(item == null)
			return null;
		return item.getParam();
	}

	//[end]
	
	/**
	 * 积利金 交易查询 交易类型字典
	 */
	public  static List<KeyAndValueItem> SaleTypeList= new ArrayList<KeyAndValueItem>(){
	
		private static final long serialVersionUID = 1L;
		{
			add(new KeyAndValueItem("请选择",""));
			add(new KeyAndValueItem("买入活期贵金属积利","0"));
			add(new KeyAndValueItem("卖出活期贵金属积利","1"));
			add(new KeyAndValueItem("贵金属积利活期转为定期","2"));
			add(new KeyAndValueItem("贵金属积利定期转为活期","3"));
		}
	};

	/**
	 * 积利金 交易查询 交易状态字典
	 */
	public  static List<KeyAndValueItem> TransStatusList= new ArrayList<KeyAndValueItem>(){

		private static final long serialVersionUID = 1L;
		{
//			add(new KeyAndValueItem("全部","0"));
			add(new KeyAndValueItem("成功","0"));
			add(new KeyAndValueItem("失败","1"));
//			add(new KeyAndValueItem("成功","2"));
		}
	};

	/**
	 * 积利金 交易查询 定投状态字典
	 */
	public static List<KeyAndValueItem> FixStatusList = new ArrayList<KeyAndValueItem>(){

		private static final long serialVersionUID = 1L;
		{
			add(new KeyAndValueItem("全部", ""));
			add(new KeyAndValueItem("有效", "1"));
			add(new KeyAndValueItem("已完成", "4"));
			add(new KeyAndValueItem("提前终止（银行）", "3"));
			add(new KeyAndValueItem("提前终止（客户）", "2"));
		}
	};
	/**
	 * 定投状态
	 */
	public static List<KeyAndValueItem> fixStatusResList = new ArrayList<KeyAndValueItem>(){
		private static final long serialVersionUID = 1L;
		{
			add(new KeyAndValueItem("成功", "1"));
			add(new KeyAndValueItem("失败", "2"));
		}
	};
	/**
	 * 贵金属积利金 账户类型
	 */
	public static List<KeyAndValueItem> goldbonusAcctTypeList = new ArrayList<KeyAndValueItem>(){
		private static final long serialVersionUID = 1L;
		{
			add(new KeyAndValueItem("活期一本通", "SY"));
			add(new KeyAndValueItem("长城电子借记卡", "CD"));
			add(new KeyAndValueItem("活期一本通", "188"));
			add(new KeyAndValueItem("长城电子借记卡", "119"));
		}
	};
	/**
	 * 贵金属积利金 账户查询 期限单位
	 */
	public static List<KeyAndValueItem> goldbonuslimitUnitList = new ArrayList<KeyAndValueItem>(){
		private static final long serialVersionUID = 1L;
		{
			add(new KeyAndValueItem("天", "0"));
			add(new KeyAndValueItem("周", "1"));
			add(new KeyAndValueItem("个月", "2"));
		}
	};
	/**
	 * 贵金属积利金 定投管理周期
	 */
	public static List<KeyAndValueItem> goldbonusfixTermTypeList = new ArrayList<KeyAndValueItem>(){
		private static final long serialVersionUID = 1L;
		{
			add(new KeyAndValueItem("每日", "0"));
			add(new KeyAndValueItem("每周", "1"));
			add(new KeyAndValueItem("每月", "2"));
		}
	};
	/**
	 * 贵金属积利金 定投管理扣款日 - 周
	 */
	public static List<KeyAndValueItem> goldbonusfixPayDateValueWeekList = new ArrayList<KeyAndValueItem>(){
		private static final long serialVersionUID = 1L;
		{
			add(new KeyAndValueItem("周一", "1"));
			add(new KeyAndValueItem("周二", "2"));
			add(new KeyAndValueItem("周三", "3"));
			add(new KeyAndValueItem("周四", "4"));
			add(new KeyAndValueItem("周五", "5"));
			add(new KeyAndValueItem("周六", "6"));
			add(new KeyAndValueItem("周日", "7"));
		}
	};
	/**
	 * 贵金属积利金 定投管理扣款日 - 月
	 */
	public static List<KeyAndValueItem> goldbonusfixPayDateValueMounthList = new ArrayList<KeyAndValueItem>(){
		private static final long serialVersionUID = 1L;
		{
			add(new KeyAndValueItem("1号", "1"));
			add(new KeyAndValueItem("2号", "2"));
			add(new KeyAndValueItem("3号", "3"));
			add(new KeyAndValueItem("4号", "4"));
			add(new KeyAndValueItem("5号", "5"));
			add(new KeyAndValueItem("6号", "6"));
			add(new KeyAndValueItem("7号", "7"));
			add(new KeyAndValueItem("8号", "8"));
			add(new KeyAndValueItem("9号", "9"));
			add(new KeyAndValueItem("10号", "10"));
			add(new KeyAndValueItem("11号", "11"));
			add(new KeyAndValueItem("12号", "12"));
			add(new KeyAndValueItem("13号", "13"));
			add(new KeyAndValueItem("14号", "14"));
			add(new KeyAndValueItem("15号", "15"));
			add(new KeyAndValueItem("16号", "16"));
			add(new KeyAndValueItem("17号", "17"));
			add(new KeyAndValueItem("18号", "18"));
			add(new KeyAndValueItem("19号", "19"));
			add(new KeyAndValueItem("20号", "20"));
			add(new KeyAndValueItem("21号", "21"));
			add(new KeyAndValueItem("22号", "22"));
			add(new KeyAndValueItem("23号", "23"));
			add(new KeyAndValueItem("24号", "24"));
			add(new KeyAndValueItem("25号", "25"));
			add(new KeyAndValueItem("26号", "26"));
			add(new KeyAndValueItem("27号", "27"));
			add(new KeyAndValueItem("28号", "28"));
			add(new KeyAndValueItem("29号", "29"));
			add(new KeyAndValueItem("30号", "30"));
			add(new KeyAndValueItem("31号", "31"));
		}
	};
	/**
	 * 贵金属积利金 定投管理扣款日---日
	 */
	public static List<KeyAndValueItem> goldbonusfixPayDateValueList2_1 = new ArrayList<KeyAndValueItem>(){
		private static final long serialVersionUID = 1L;
		{
			add(new KeyAndValueItem("每天", "1"));
		}
	};
	/**
	 * 贵金属积利金 定投管理扣款日---周
	 */
	public static List<KeyAndValueItem> goldbonusfixPayDateValueList2_2 = new ArrayList<KeyAndValueItem>(){
		private static final long serialVersionUID = 1L;
		{
			add(new KeyAndValueItem("周三", "3"));
		}
	};
	/**
	 * 贵金属积利金 定投管理扣款日---月
	 */
	public static List<KeyAndValueItem> goldbonusfixPayDateValueList2_3 = new ArrayList<KeyAndValueItem>(){
		private static final long serialVersionUID = 1L;
		{
			add(new KeyAndValueItem("15号", "15"));
		}
	};
	/**
	 * 贵金属积利金 定投管理扣款日期限单位
	 */
	public static List<KeyAndValueItem> goldbonusfixPayDateQryList = new ArrayList<KeyAndValueItem>(){
		private static final long serialVersionUID = 1L;
		{
			add(new KeyAndValueItem("日", "0"));
			add(new KeyAndValueItem("周", "1"));
			add(new KeyAndValueItem("月", "2"));
		}
	};

	/** 投资理财排序币种 */
	private static List<KeyAndValueItem> ivestCurrencyCodeList = new ArrayList<KeyAndValueItem>();
	/** 投资理财排序币种，以及格式化小数位数 */
	public static List<KeyAndValueItem> getInvestCurrencyCodeList(){
		if(ivestCurrencyCodeList.size() > 0)
			return ivestCurrencyCodeList;
		// 贵金属
		KeyAndValueItem item = new KeyAndValueItem("035","001",2);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("068","001",3);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("034","014",2);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("036","014",3);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("845","001",2);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("844","001",2);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("045","014",2);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("841","014",2);
		ivestCurrencyCodeList.add(item);

		//外汇，双向宝
		item = new KeyAndValueItem("038","014",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("014","027",2);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("029","014",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("012","014",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("014","028",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("038","013",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("014","013",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("029","027",2);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("013","027",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("014","015",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("038","029",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("014","018",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("038","012",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("012","027",2);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("029","028",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("038","027",2);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("012","013",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("038","028",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("028","027",2);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("029","013",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("028","013",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("018","013",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("012","029",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("038","015",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("012","028",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("029","015",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("015","028",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("015","013",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("038","018",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("018","027",2);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("012","018",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("015","027",2);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("012","015",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("029","018",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("028","018",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("014","081",4);
		ivestCurrencyCodeList.add(item);
		item = new KeyAndValueItem("015","018",4);
		ivestCurrencyCodeList.add(item);
		return ivestCurrencyCodeList;
	}

	/** 所有货币对 翻译 */
	private static List<KeyAndValueItem> CurrencyList = new ArrayList<KeyAndValueItem>();

	public static List<KeyAndValueItem> getCurrencyList() {
		if(CurrencyList != null && CurrencyList.size() > 0)
			return CurrencyList;

		CurrencyList.add(new KeyAndValueItem("不可选择","000"));
		CurrencyList.add(new KeyAndValueItem("人民币","001"));
		CurrencyList.add(new KeyAndValueItem("人民币","CNY"));
		CurrencyList.add(new KeyAndValueItem("英镑","012"));
		CurrencyList.add(new KeyAndValueItem("英镑","GBP"));
		CurrencyList.add(new KeyAndValueItem("港币","013"));
		CurrencyList.add(new KeyAndValueItem("港币","HKD"));
		CurrencyList.add(new KeyAndValueItem("美元","014"));
		CurrencyList.add(new KeyAndValueItem("美元","USD"));
		CurrencyList.add(new KeyAndValueItem("瑞士法郎","015"));
		CurrencyList.add(new KeyAndValueItem("瑞士法郎","CHF"));
		CurrencyList.add(new KeyAndValueItem("德国马克","016"));
		CurrencyList.add(new KeyAndValueItem("德国马克","DEM"));
		CurrencyList.add(new KeyAndValueItem("法国法郎","017"));
		CurrencyList.add(new KeyAndValueItem("法国法郎","FRF"));
		CurrencyList.add(new KeyAndValueItem("新加坡元","018"));
		CurrencyList.add(new KeyAndValueItem("新加坡元","SGD"));
		CurrencyList.add(new KeyAndValueItem("荷兰盾","020"));
		CurrencyList.add(new KeyAndValueItem("荷兰盾","NLG"));
		CurrencyList.add(new KeyAndValueItem("瑞典克朗","021"));
		CurrencyList.add(new KeyAndValueItem("瑞典克朗","SEK"));
		CurrencyList.add(new KeyAndValueItem("丹麦克朗","022"));
		CurrencyList.add(new KeyAndValueItem("丹麦克朗","DKK"));
		CurrencyList.add(new KeyAndValueItem("挪威克朗","023"));
		CurrencyList.add(new KeyAndValueItem("挪威克朗","NOK"));
		CurrencyList.add(new KeyAndValueItem("奥地利先令","024"));
		CurrencyList.add(new KeyAndValueItem("奥地利先令","ATS"));
		CurrencyList.add(new KeyAndValueItem("比利时法郎","025"));
		CurrencyList.add(new KeyAndValueItem("比利时法郎","BEF"));
		CurrencyList.add(new KeyAndValueItem("意大利里拉","026"));
		CurrencyList.add(new KeyAndValueItem("意大利里拉","ITL"));
		CurrencyList.add(new KeyAndValueItem("日元","027"));
		CurrencyList.add(new KeyAndValueItem("日元","JPY"));
		CurrencyList.add(new KeyAndValueItem("加拿大元","028"));
		CurrencyList.add(new KeyAndValueItem("加拿大元","CAD"));
		CurrencyList.add(new KeyAndValueItem("澳大利亚元","029"));
		CurrencyList.add(new KeyAndValueItem("澳大利亚元","AUD"));
		CurrencyList.add(new KeyAndValueItem("白银（克）","068"));
		CurrencyList.add(new KeyAndValueItem("白银（克）","SLV"));
		CurrencyList.add(new KeyAndValueItem("白银（盎司）","036"));
		CurrencyList.add(new KeyAndValueItem("白银（盎司）","XAG"));
		CurrencyList.add(new KeyAndValueItem("黄金（盎司）","034"));
		CurrencyList.add(new KeyAndValueItem("黄金（盎司）","XAU"));
		CurrencyList.add(new KeyAndValueItem("黄金（克）","035"));
		CurrencyList.add(new KeyAndValueItem("黄金（克）","GLD"));
		CurrencyList.add(new KeyAndValueItem("钯金（克）","844"));
		CurrencyList.add(new KeyAndValueItem("钯金（克）","PLD"));
		CurrencyList.add(new KeyAndValueItem("铂金（克）","845"));
		CurrencyList.add(new KeyAndValueItem("铂金（克）","PLT"));
		CurrencyList.add(new KeyAndValueItem("钯金（盎司）","841"));
		CurrencyList.add(new KeyAndValueItem("钯金（盎司）","XPD"));
		CurrencyList.add(new KeyAndValueItem("铂金（盎司）","045"));
		CurrencyList.add(new KeyAndValueItem("铂金（盎司）","XPT"));
		CurrencyList.add(new KeyAndValueItem("欧元","038"));
		CurrencyList.add(new KeyAndValueItem("欧元","EUR"));
		CurrencyList.add(new KeyAndValueItem("芬兰马克","042"));
		CurrencyList.add(new KeyAndValueItem("芬兰马克","FIM"));
		CurrencyList.add(new KeyAndValueItem("澳门元","081"));
		CurrencyList.add(new KeyAndValueItem("澳门元","MOP"));
		CurrencyList.add(new KeyAndValueItem("菲律宾比索","082"));
		CurrencyList.add(new KeyAndValueItem("菲律宾比索","PHP"));
		CurrencyList.add(new KeyAndValueItem("泰国铢","084"));
		CurrencyList.add(new KeyAndValueItem("泰国铢","THB"));
		CurrencyList.add(new KeyAndValueItem("新西兰元","087"));
		CurrencyList.add(new KeyAndValueItem("新西兰元","NZD"));
		CurrencyList.add(new KeyAndValueItem("韩元","088"));
		CurrencyList.add(new KeyAndValueItem("韩元","KRW"));
		CurrencyList.add(new KeyAndValueItem("记账美元","094"));
		CurrencyList.add(new KeyAndValueItem("清算瑞士法郎","095"));
		CurrencyList.add(new KeyAndValueItem("印尼盾","056"));
		CurrencyList.add(new KeyAndValueItem("印尼盾","IDR"));
		CurrencyList.add(new KeyAndValueItem("越南盾","064"));
		CurrencyList.add(new KeyAndValueItem("越南盾","VND"));
		CurrencyList.add(new KeyAndValueItem("阿联酋迪拉姆","096"));
		CurrencyList.add(new KeyAndValueItem("阿联酋迪拉姆","AED"));
		CurrencyList.add(new KeyAndValueItem("阿根廷比索","126"));
		CurrencyList.add(new KeyAndValueItem("阿根廷比索","ARP"));
		CurrencyList.add(new KeyAndValueItem("巴西雷亚尔","134"));
		CurrencyList.add(new KeyAndValueItem("巴西雷亚尔","BRL"));
		CurrencyList.add(new KeyAndValueItem("埃及磅","053"));
		CurrencyList.add(new KeyAndValueItem("埃及磅","EGP"));
		CurrencyList.add(new KeyAndValueItem("印度卢比","085"));
		CurrencyList.add(new KeyAndValueItem("印度卢比","INR"));
		CurrencyList.add(new KeyAndValueItem("约旦第纳尔","057"));
		CurrencyList.add(new KeyAndValueItem("约旦第纳尔","JOD"));
		CurrencyList.add(new KeyAndValueItem("蒙古图格里克","179"));
		CurrencyList.add(new KeyAndValueItem("蒙古图格里克","MNT"));
		CurrencyList.add(new KeyAndValueItem("尼日尼亚奈拉","076"));
		CurrencyList.add(new KeyAndValueItem("尼日尼亚奈拉","NGN"));
		CurrencyList.add(new KeyAndValueItem("罗马尼亚列伊","062"));
		CurrencyList.add(new KeyAndValueItem("罗马尼亚列伊","ROL"));
		CurrencyList.add(new KeyAndValueItem("土耳其里拉","093"));
		CurrencyList.add(new KeyAndValueItem("土耳其里拉","TRL"));
		CurrencyList.add(new KeyAndValueItem("乌克兰格里夫纳","246"));
		CurrencyList.add(new KeyAndValueItem("乌克兰格里夫纳","UAH"));
		CurrencyList.add(new KeyAndValueItem("南非兰特","070"));
		CurrencyList.add(new KeyAndValueItem("南非兰特","ZAR"));
		CurrencyList.add(new KeyAndValueItem("哈萨克斯坦坚戈","101"));
		CurrencyList.add(new KeyAndValueItem("哈萨克斯坦坚戈","KZT"));
		CurrencyList.add(new KeyAndValueItem("赞比亚克瓦查","080"));
		CurrencyList.add(new KeyAndValueItem("赞比亚克瓦查","ZMK"));
		CurrencyList.add(new KeyAndValueItem("匈牙利福林","065"));
		CurrencyList.add(new KeyAndValueItem("匈牙利福林","HUF"));
		CurrencyList.add(new KeyAndValueItem("林吉特","032"));
		CurrencyList.add(new KeyAndValueItem("林吉特","MYR"));
		CurrencyList.add(new KeyAndValueItem("通配","-1"));
		CurrencyList.add(new KeyAndValueItem("阿富汗尼","251"));
		CurrencyList.add(new KeyAndValueItem("阿富汗尼","AFN"));
		CurrencyList.add(new KeyAndValueItem("阿尔巴尼亚列克","061"));
		CurrencyList.add(new KeyAndValueItem("阿尔巴尼亚列克","ALL"));
		CurrencyList.add(new KeyAndValueItem("阿尔及利亚第纳尔","041"));
		CurrencyList.add(new KeyAndValueItem("阿尔及利亚第纳尔","DZD"));
		CurrencyList.add(new KeyAndValueItem("阿塞拜疆马纳特","128"));
		CurrencyList.add(new KeyAndValueItem("阿塞拜疆马纳特","AZM"));
		CurrencyList.add(new KeyAndValueItem("巴哈马元","135"));
		CurrencyList.add(new KeyAndValueItem("巴哈马元","BSD"));
		CurrencyList.add(new KeyAndValueItem("巴林第纳尔","009"));
		CurrencyList.add(new KeyAndValueItem("巴林第纳尔","BHD"));
		CurrencyList.add(new KeyAndValueItem("孟加拉国塔卡","010"));
		CurrencyList.add(new KeyAndValueItem("孟加拉国塔卡","BDT"));
		CurrencyList.add(new KeyAndValueItem("亚美尼亚德拉姆","123"));
		CurrencyList.add(new KeyAndValueItem("亚美尼亚德拉姆","AMD"));
		CurrencyList.add(new KeyAndValueItem("巴巴多斯元","129"));
		CurrencyList.add(new KeyAndValueItem("巴巴多斯元","BBD"));
		CurrencyList.add(new KeyAndValueItem("百慕大元","130"));
		CurrencyList.add(new KeyAndValueItem("百慕大元","BMD"));
		CurrencyList.add(new KeyAndValueItem("玻利维亚玻利瓦诺","132"));
		CurrencyList.add(new KeyAndValueItem("玻利维亚玻利瓦诺","BOB"));
		CurrencyList.add(new KeyAndValueItem("伯利兹元","138"));
		CurrencyList.add(new KeyAndValueItem("伯利兹元","BZD"));
		CurrencyList.add(new KeyAndValueItem("所罗门群岛元","199"));
		CurrencyList.add(new KeyAndValueItem("所罗门群岛元","SBD"));
		CurrencyList.add(new KeyAndValueItem("保加利亚列弗","066"));
		CurrencyList.add(new KeyAndValueItem("保加利亚列弗","BGL"));
		CurrencyList.add(new KeyAndValueItem("缅元","178"));
		CurrencyList.add(new KeyAndValueItem("缅元","MMK"));
		CurrencyList.add(new KeyAndValueItem("布隆迪法郎","051"));
		CurrencyList.add(new KeyAndValueItem("布隆迪法郎","BIF"));
		CurrencyList.add(new KeyAndValueItem("佛得角埃斯库多","144"));
		CurrencyList.add(new KeyAndValueItem("佛得角埃斯库多","CVE"));
		CurrencyList.add(new KeyAndValueItem("开曼群岛元","168"));
		CurrencyList.add(new KeyAndValueItem("开曼群岛元","KYD"));
		CurrencyList.add(new KeyAndValueItem("斯里兰卡卢比","040"));
		CurrencyList.add(new KeyAndValueItem("斯里兰卡卢比","LKR"));
		CurrencyList.add(new KeyAndValueItem("智利比索","140"));
		CurrencyList.add(new KeyAndValueItem("智利比索","CLP"));
		CurrencyList.add(new KeyAndValueItem("哥伦比亚比索","141"));
		CurrencyList.add(new KeyAndValueItem("哥伦比亚比索","COP"));
		CurrencyList.add(new KeyAndValueItem("科摩罗法郎","167"));
		CurrencyList.add(new KeyAndValueItem("科摩罗法郎","KMF"));
		CurrencyList.add(new KeyAndValueItem("哥斯达黎加科郎","142"));
		CurrencyList.add(new KeyAndValueItem("哥斯达黎加科郎","CRC"));
		CurrencyList.add(new KeyAndValueItem("克罗地亚库纳","159"));
		CurrencyList.add(new KeyAndValueItem("克罗地亚库纳","HRK"));
		CurrencyList.add(new KeyAndValueItem("塞浦路斯镑","052"));
		CurrencyList.add(new KeyAndValueItem("塞浦路斯镑","CYP"));
		CurrencyList.add(new KeyAndValueItem("捷克克朗","145"));
		CurrencyList.add(new KeyAndValueItem("捷克克朗","CZK"));
		CurrencyList.add(new KeyAndValueItem("多米尼加比索","147"));
		CurrencyList.add(new KeyAndValueItem("多米尼加比索","DOP"));
		CurrencyList.add(new KeyAndValueItem("萨尔瓦多科郎","206"));
		CurrencyList.add(new KeyAndValueItem("萨尔瓦多科郎","SVC"));
		CurrencyList.add(new KeyAndValueItem("埃塞俄比亚比尔","054"));
		CurrencyList.add(new KeyAndValueItem("埃塞俄比亚比尔","ETB"));
		CurrencyList.add(new KeyAndValueItem("厄立特里亚纳克法","235"));
		CurrencyList.add(new KeyAndValueItem("厄立特里亚纳克法","ERN"));
		CurrencyList.add(new KeyAndValueItem("爱沙尼亚克罗姆","150"));
		CurrencyList.add(new KeyAndValueItem("爱沙尼亚克罗姆","EEK"));
		CurrencyList.add(new KeyAndValueItem("福克兰群岛镑","152"));
		CurrencyList.add(new KeyAndValueItem("福克兰群岛镑","FKP"));
		CurrencyList.add(new KeyAndValueItem("斐济元","151"));
		CurrencyList.add(new KeyAndValueItem("斐济元","FJD"));
		CurrencyList.add(new KeyAndValueItem("吉布提法郎","146"));
		CurrencyList.add(new KeyAndValueItem("吉布提法郎","DJF"));
		CurrencyList.add(new KeyAndValueItem("冈比亚达拉西","154"));
		CurrencyList.add(new KeyAndValueItem("冈比亚达拉西","GMD"));
		CurrencyList.add(new KeyAndValueItem("直布罗陀镑","153"));
		CurrencyList.add(new KeyAndValueItem("直布罗陀镑","GIP"));
		CurrencyList.add(new KeyAndValueItem("危地马拉格查尔","155"));
		CurrencyList.add(new KeyAndValueItem("危地马拉格查尔","GTQ"));
		CurrencyList.add(new KeyAndValueItem("几内亚法郎","050"));
		CurrencyList.add(new KeyAndValueItem("几内亚法郎","GNF"));
		CurrencyList.add(new KeyAndValueItem("圭亚那元","157"));
		CurrencyList.add(new KeyAndValueItem("圭亚那元","GYD"));
		CurrencyList.add(new KeyAndValueItem("海地古德","160"));
		CurrencyList.add(new KeyAndValueItem("海地古德","HTG"));
		CurrencyList.add(new KeyAndValueItem("洪都拉斯伦皮拉","158"));
		CurrencyList.add(new KeyAndValueItem("洪都拉斯伦皮拉","HNL"));
		CurrencyList.add(new KeyAndValueItem("冰岛克朗","163"));
		CurrencyList.add(new KeyAndValueItem("冰岛克朗","ISK"));
		CurrencyList.add(new KeyAndValueItem("伊朗里亚尔","048"));
		CurrencyList.add(new KeyAndValueItem("伊朗里亚尔","IRR"));
		CurrencyList.add(new KeyAndValueItem("伊拉克第纳尔","044"));
		CurrencyList.add(new KeyAndValueItem("伊拉克第纳尔","IQD"));
		CurrencyList.add(new KeyAndValueItem("新以色列谢克尔","162"));
		CurrencyList.add(new KeyAndValueItem("新以色列谢克尔","ILS"));
		CurrencyList.add(new KeyAndValueItem("牙买加元","164"));
		CurrencyList.add(new KeyAndValueItem("牙买加元","JMD"));
		CurrencyList.add(new KeyAndValueItem("肯尼亚先令","058"));
		CurrencyList.add(new KeyAndValueItem("肯尼亚先令","KES"));
		CurrencyList.add(new KeyAndValueItem("北朝鲜圆","063"));
		CurrencyList.add(new KeyAndValueItem("北朝鲜圆","KPW"));
		CurrencyList.add(new KeyAndValueItem("科威特第纳尔","059"));
		CurrencyList.add(new KeyAndValueItem("科威特第纳尔","KWD"));
		CurrencyList.add(new KeyAndValueItem("吉尔吉斯斯坦索姆","165"));
		CurrencyList.add(new KeyAndValueItem("吉尔吉斯斯坦索姆","KGS"));
		CurrencyList.add(new KeyAndValueItem("老挝基普","169"));
		CurrencyList.add(new KeyAndValueItem("老挝基普","LAK"));
		CurrencyList.add(new KeyAndValueItem("黎巴嫩镑","170"));
		CurrencyList.add(new KeyAndValueItem("黎巴嫩镑","LBP"));
		CurrencyList.add(new KeyAndValueItem("拉脱维亚拉特","175"));
		CurrencyList.add(new KeyAndValueItem("拉脱维亚拉特","LVL"));
		CurrencyList.add(new KeyAndValueItem("利比里亚元","171"));
		CurrencyList.add(new KeyAndValueItem("利比里亚元","LRD"));
		CurrencyList.add(new KeyAndValueItem("利比亚第纳尔","071"));
		CurrencyList.add(new KeyAndValueItem("利比亚第纳尔","LYD"));
		CurrencyList.add(new KeyAndValueItem("立陶宛立特","173"));
		CurrencyList.add(new KeyAndValueItem("立陶宛立特","LTL"));
		CurrencyList.add(new KeyAndValueItem("马维拉克瓦查","183"));
		CurrencyList.add(new KeyAndValueItem("马维拉克瓦查","MWK"));
		CurrencyList.add(new KeyAndValueItem("马尔代夫卢菲亚","182"));
		CurrencyList.add(new KeyAndValueItem("马尔代夫卢菲亚","MVR"));
		CurrencyList.add(new KeyAndValueItem("马尔他里拉","181"));
		CurrencyList.add(new KeyAndValueItem("马尔他里拉","MTL"));
		CurrencyList.add(new KeyAndValueItem("毛里塔尼亚乌吉亚","180"));
		CurrencyList.add(new KeyAndValueItem("毛里塔尼亚乌吉亚","MRO"));
		CurrencyList.add(new KeyAndValueItem("毛里求斯卢比","075"));
		CurrencyList.add(new KeyAndValueItem("毛里求斯卢比","MUR"));
		CurrencyList.add(new KeyAndValueItem("墨西哥比索","184"));
		CurrencyList.add(new KeyAndValueItem("墨西哥比索","MXN"));
		CurrencyList.add(new KeyAndValueItem("摩尔多瓦列伊","176"));
		CurrencyList.add(new KeyAndValueItem("摩尔多瓦列伊","MDL"));
		CurrencyList.add(new KeyAndValueItem("摩洛哥迪拉姆","046"));
		CurrencyList.add(new KeyAndValueItem("摩洛哥迪拉姆","MAD"));
		CurrencyList.add(new KeyAndValueItem("莫桑比克麦梯卡尔","185"));
		CurrencyList.add(new KeyAndValueItem("莫桑比克麦梯卡尔","MZM"));
		CurrencyList.add(new KeyAndValueItem("阿曼里亚尔","188"));
		CurrencyList.add(new KeyAndValueItem("阿曼里亚尔","OMR"));
		CurrencyList.add(new KeyAndValueItem("纳米比亚元","186"));
		CurrencyList.add(new KeyAndValueItem("纳米比亚元","NAD"));
		CurrencyList.add(new KeyAndValueItem("尼泊尔卢比","049"));
		CurrencyList.add(new KeyAndValueItem("尼泊尔卢比","NPR"));
		CurrencyList.add(new KeyAndValueItem("荷属安的列斯盾","124"));
		CurrencyList.add(new KeyAndValueItem("荷属安的列斯盾","ANG"));
		CurrencyList.add(new KeyAndValueItem("阿鲁巴盾","127"));
		CurrencyList.add(new KeyAndValueItem("阿鲁巴盾","AWG"));
		CurrencyList.add(new KeyAndValueItem("瓦努阿图瓦图","219"));
		CurrencyList.add(new KeyAndValueItem("瓦努阿图瓦图","VUV"));
		CurrencyList.add(new KeyAndValueItem("尼加拉瓜金科多巴","187"));
		CurrencyList.add(new KeyAndValueItem("尼加拉瓜金科多巴","NIO"));
		CurrencyList.add(new KeyAndValueItem("巴基斯坦卢比","019"));
		CurrencyList.add(new KeyAndValueItem("巴基斯坦卢比","PKR"));
		CurrencyList.add(new KeyAndValueItem("巴拿马巴波亚","189"));
		CurrencyList.add(new KeyAndValueItem("巴拿马巴波亚","PAB"));
		CurrencyList.add(new KeyAndValueItem("巴布亚新几内亚基那","077"));
		CurrencyList.add(new KeyAndValueItem("巴布亚新几内亚基那","PGK"));
		CurrencyList.add(new KeyAndValueItem("巴拉圭瓜拉尼","194"));
		CurrencyList.add(new KeyAndValueItem("巴拉圭瓜拉尼","PYG"));
		CurrencyList.add(new KeyAndValueItem("秘鲁索尔","190"));
		CurrencyList.add(new KeyAndValueItem("秘鲁索尔","PEN"));
		CurrencyList.add(new KeyAndValueItem("几内亚比绍比索","156"));
		CurrencyList.add(new KeyAndValueItem("几内亚比绍比索","GWP"));
		CurrencyList.add(new KeyAndValueItem("东帝汶埃斯库多","211"));
		CurrencyList.add(new KeyAndValueItem("东帝汶埃斯库多","TPE"));
		CurrencyList.add(new KeyAndValueItem("卡塔尔里亚尔","195"));
		CurrencyList.add(new KeyAndValueItem("卡塔尔里亚尔","QAR"));
		CurrencyList.add(new KeyAndValueItem("卢旺达法郎","078"));
		CurrencyList.add(new KeyAndValueItem("卢旺达法郎","RWF"));
		CurrencyList.add(new KeyAndValueItem("圣赫勒拿镑","200"));
		CurrencyList.add(new KeyAndValueItem("圣赫勒拿镑","SHP"));
		CurrencyList.add(new KeyAndValueItem("圣多美和普林西比多布拉","205"));
		CurrencyList.add(new KeyAndValueItem("圣多美和普林西比多布拉","STD"));
		CurrencyList.add(new KeyAndValueItem("沙特里亚尔","198"));
		CurrencyList.add(new KeyAndValueItem("沙特里亚尔","SAR"));
		CurrencyList.add(new KeyAndValueItem("塞舌尔卢比","079"));
		CurrencyList.add(new KeyAndValueItem("塞舌尔卢比","SCR"));
		CurrencyList.add(new KeyAndValueItem("塞拉利昂利昂","047"));
		CurrencyList.add(new KeyAndValueItem("塞拉利昂利昂","SLL"));
		CurrencyList.add(new KeyAndValueItem("斯洛伐克克朗","202"));
		CurrencyList.add(new KeyAndValueItem("斯洛伐克克朗","SKK"));
		CurrencyList.add(new KeyAndValueItem("斯洛文尼亚托拉尔","201"));
		CurrencyList.add(new KeyAndValueItem("斯洛文尼亚托拉尔","SIT"));
		CurrencyList.add(new KeyAndValueItem("索马里先令","203"));
		CurrencyList.add(new KeyAndValueItem("索马里先令","SOS"));
		CurrencyList.add(new KeyAndValueItem("津巴布韦元","224"));
		CurrencyList.add(new KeyAndValueItem("津巴布韦元","ZWD"));
		CurrencyList.add(new KeyAndValueItem("苏丹第纳尔","242"));
		CurrencyList.add(new KeyAndValueItem("苏丹第纳尔","SDD"));
		CurrencyList.add(new KeyAndValueItem("斯威士兰里兰吉尼","207"));
		CurrencyList.add(new KeyAndValueItem("斯威士兰里兰吉尼","SZL"));
		CurrencyList.add(new KeyAndValueItem("叙利亚镑","089"));
		CurrencyList.add(new KeyAndValueItem("叙利亚镑","SYP"));
		CurrencyList.add(new KeyAndValueItem("汤加邦加","210"));
		CurrencyList.add(new KeyAndValueItem("汤加邦加","TOP"));
		CurrencyList.add(new KeyAndValueItem("特立尼达和多巴哥元","212"));
		CurrencyList.add(new KeyAndValueItem("特立尼达和多巴哥元","TTD"));
		CurrencyList.add(new KeyAndValueItem("突尼斯第纳尔","091"));
		CurrencyList.add(new KeyAndValueItem("突尼斯第纳尔","TND"));
		CurrencyList.add(new KeyAndValueItem("土库曼斯坦马纳特","209"));
		CurrencyList.add(new KeyAndValueItem("土库曼斯坦马纳特","TMM"));
		CurrencyList.add(new KeyAndValueItem("乌干达先令","215"));
		CurrencyList.add(new KeyAndValueItem("乌干达先令","UGX"));
		CurrencyList.add(new KeyAndValueItem("马其顿第纳尔","177"));
		CurrencyList.add(new KeyAndValueItem("马其顿第纳尔","MKD"));
		CurrencyList.add(new KeyAndValueItem("坦桑尼亚先令","030"));
		CurrencyList.add(new KeyAndValueItem("坦桑尼亚先令","TZS"));
		CurrencyList.add(new KeyAndValueItem("乌拉圭比索","216"));
		CurrencyList.add(new KeyAndValueItem("乌拉圭比索","UYU"));
		CurrencyList.add(new KeyAndValueItem("乌兹别克斯坦苏姆","217"));
		CurrencyList.add(new KeyAndValueItem("乌兹别克斯坦苏姆","UZS"));
		CurrencyList.add(new KeyAndValueItem("委内瑞拉玻利瓦尔","247"));
		CurrencyList.add(new KeyAndValueItem("委内瑞拉玻利瓦尔","VEF"));
		CurrencyList.add(new KeyAndValueItem("萨摩亚塔拉","220"));
		CurrencyList.add(new KeyAndValueItem("萨摩亚塔拉","WST"));
		CurrencyList.add(new KeyAndValueItem("也门里亚尔","098"));
		CurrencyList.add(new KeyAndValueItem("也门里亚尔","YER"));
		CurrencyList.add(new KeyAndValueItem("南斯拉夫第纳尔","221"));
		CurrencyList.add(new KeyAndValueItem("南斯拉夫第纳尔","YUM"));
		CurrencyList.add(new KeyAndValueItem("加纳塞地","252"));
		CurrencyList.add(new KeyAndValueItem("加纳塞地","GHC"));
		CurrencyList.add(new KeyAndValueItem("新土耳其里拉","245"));
		CurrencyList.add(new KeyAndValueItem("新土耳其里拉","TRY"));
		CurrencyList.add(new KeyAndValueItem("CAF 法郎 BEAC","820"));
		CurrencyList.add(new KeyAndValueItem("CAF 法郎 BEAC","XAF"));
		CurrencyList.add(new KeyAndValueItem("东加勒比元","825"));
		CurrencyList.add(new KeyAndValueItem("东加勒比元","XCD"));
		CurrencyList.add(new KeyAndValueItem("CFA 法郎 BCEAO","099"));
		CurrencyList.add(new KeyAndValueItem("CFA 法郎 BCEAO","XOF"));
		CurrencyList.add(new KeyAndValueItem("CFP 法郎","842"));
		CurrencyList.add(new KeyAndValueItem("CFP 法郎","XPF"));
		CurrencyList.add(new KeyAndValueItem("马达加斯加阿里亚里","248"));
		CurrencyList.add(new KeyAndValueItem("马达加斯加阿里亚里","MGA"));
		CurrencyList.add(new KeyAndValueItem("塔吉克索莫尼","244"));
		CurrencyList.add(new KeyAndValueItem("塔吉克索莫尼","TJS"));
		CurrencyList.add(new KeyAndValueItem("白俄罗斯卢布","250"));
		CurrencyList.add(new KeyAndValueItem("白俄罗斯卢布","BYR"));
		CurrencyList.add(new KeyAndValueItem("保加利亚列弗","229"));
		CurrencyList.add(new KeyAndValueItem("保加利亚列弗","BGN"));
		CurrencyList.add(new KeyAndValueItem("格鲁吉亚拉里","236"));
		CurrencyList.add(new KeyAndValueItem("格鲁吉亚拉里","GEL"));
		CurrencyList.add(new KeyAndValueItem("波兰兹罗提","191"));
		CurrencyList.add(new KeyAndValueItem("波兰兹罗提","PLN"));
		CurrencyList.add(new KeyAndValueItem("安哥拉宽扎","226"));
		CurrencyList.add(new KeyAndValueItem("安哥拉宽扎","AOA"));
		CurrencyList.add(new KeyAndValueItem("博茨瓦纳普拉","039"));
		CurrencyList.add(new KeyAndValueItem("博茨瓦纳普拉","BWP"));
		CurrencyList.add(new KeyAndValueItem("文莱币","131"));
		CurrencyList.add(new KeyAndValueItem("文莱币","BND"));
		CurrencyList.add(new KeyAndValueItem("柬埔寨瑞尔","166"));
		CurrencyList.add(new KeyAndValueItem("柬埔寨瑞尔","KHR"));
		CurrencyList.add(new KeyAndValueItem("其他币种","ZZZ"));
		CurrencyList.add(new KeyAndValueItem("其他币种","zzz"));
		CurrencyList.add(new KeyAndValueItem("俄罗斯贸易卢布","072"));
		CurrencyList.add(new KeyAndValueItem("俄罗斯贸易卢布","RUR"));
		CurrencyList.add(new KeyAndValueItem("卢布","196"));
		CurrencyList.add(new KeyAndValueItem("卢布","RUB"));
		CurrencyList.add(new KeyAndValueItem("印尼卢比","056"));
		CurrencyList.add(new KeyAndValueItem("印尼卢比","IDR"));
		CurrencyList.add(new KeyAndValueItem("巴西里亚尔","134"));
		CurrencyList.add(new KeyAndValueItem("巴西里亚尔","BRL"));
		CurrencyList.add(new KeyAndValueItem("新台币","213"));
		CurrencyList.add(new KeyAndValueItem("新台币","TWD"));

		return CurrencyList;
	}
	

	/** 将币种翻译为中文 */
	public static String transCurrency(String currencyCode){
		String str = getKeyByValue(currencyCode,getCurrencyList());
		if(str == null)
			return "-";
		return str;
	}


	
}
