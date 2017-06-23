package com.chinamworld.bocmbci.biz.loan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoanData {
	/** 贷款品种数据字典 */
	public static final Map<String, String> loanTypeData = new HashMap<String, String>() {
		{
			put("PLAA", "一手楼住房贷款");
			put("PLAB", "住房散户贷款");
			put("PLAF", "高档房贷款");
			put("PLCB", "商业用房贷款");
			put("PLAC", "公积金贷款");
			put("PLBA", "个人汽车贷款");
			put("PLDA", "商业教育贷款 ");
			put("PLDB", "国家助学贷款 ");
			put("PLCA", "个人投资经营贷款 ");
			put("PLEA", "存款质押贷款 ");
			put("PLFA", "旅游贷款");
			put("PLFB", "家居装修贷款");
			put("PLFC", "耐用消费品贷款");
			put("PLGA", "个人信用贷款");
			put("PLBC", "汽车金融公司委托贷款");
			put("PLCC", "工程营运车贷款");
			put("PLAD", "法人住房贷款");
			put("PLBB", "法人汽车贷款");
			put("PLAE", "房地产开发贷款");
			put("PLHA", "小企业法人贷款");
			put("PLHC", "法人商铺贷款");
			put("PLHB", "汽车经销商贷款");
			put("PLBD", "法人金融公司委托贷款");
			put("060001", "个人住房贷款");
			put("060002", "个人住房贷款");
			put("060003", "个人住房贷款");
			put("060004", "个人住房贷款");
			put("060005", "个人住房贷款");
			put("060006", "个人住房贷款");
			put("060007", "个人住房贷款");
			put("070001", "个人消费汽车贷款");
			put("070002", "个人消费汽车贷款");
			put("080001", "国家助学贷款");
			put("080002", "个人商业性助学贷款");
			put("080003", "个人其他贷款");
			put("080004", "个人其他贷款");
			put("080005", "个人质押贷款");
			put("080006", "自助贷款");
			put("080007", "个人外汇留学贷款");
			put("090001", "个人商业用房贷款");
			put("090002", "个人商业用房贷款");
			put("090003", "个人营运汽车贷款");
			put("090004", "个人经营贷款");
			put("090005", "下岗失业人员小额担保贷款");
			put("100001", "个人住房公积金贷款");
			put("100002", "汽车委托贷款");
			put("100003", "个人委托贷款");
			put("110001", "个人住房贷款");
			put("110002", "个人住房贷款");
			put("110003", "个人住房贷款");
			put("110004", "个人住房贷款");
			put("110005", "个人住房贷款");
			put("110006", "个人住房贷款");
			put("110007", "个人住房贷款");
			put("120001", "个人消费汽车贷款");
			put("120002", "个人消费汽车贷款");
			put("130001", "国家助学贷款");
			put("130002", "个人商业性助学贷款");
			put("130003", "个人其他贷款");
			put("130004", "个人其他贷款");
			put("130005", "个人质押贷款");
			put("130006", "自助贷款");
			put("130007", "个人外汇留学贷款");
			put("140001", "个人商业用房贷款");
			put("140002", "个人商业用房贷款");
			put("140003", "个人营运汽车贷款");
			put("140004", "个人经营贷款");
			put("140005", "下岗失业人员小额担保贷款");
			put("150001", "个人住房公积金贷款");
			put("150002", "汽车委托贷款");
			put("150003", "个人委托贷款");
			put("1035", "个人住房贷款");
			put("1036", "个人住房贷款");
			put("1037", "个人住房贷款");
			put("1038", "个人住房贷款");
			put("1039", "个人住房贷款");
			put("1040", "个人住房贷款");
			put("1041", "个人住房贷款");
			put("1042", "个人消费汽车贷款");
			put("1043", "个人消费汽车贷款");
			put("1044", "国家助学贷款");
			put("1045", "个人商业性助学贷款");
			put("1046", "个人其他贷款");
			put("1047", "个人其他贷款");
			put("1048", "个人质押贷款");
			put("1049", "自助贷款");
			put("1050", "个人外汇留学贷款");
			put("1051", "个人商业用房贷款");
			put("1052", "个人商业用房贷款");
			put("1053", "个人营运汽车贷款");
			put("1054", "个人经营贷款");
			put("1055", "下岗失业人员小额担保贷款");
			put("1056", "个人住房公积金贷款");
			put("1057", "汽车委托贷款");
			put("1058", "个人委托贷款");
			put("1077", "卡贷通");
			put("1083", "新人新办法贷款");
			put("9999", "其他贷款");
			put("080010", "新人新办法贷款");
			put("130012", "中银E贷");
			put("080012", "网络消费贷款");
			put("140006", "网络经营贷款");
			put("1160", "中银E贷");
			put("1125", "网络消费贷款");
			put("1131", "网络经营贷款");
		}
	};
	public static final Map<String, String> loanTypeDataLimit = new HashMap<String, String>() {
		{
			put("1046", "个人无抵质押循环贷款额度");
			put("1047", "个人抵质押循环贷款额度");
			put("1054", "个人经营贷款额度");
			put("1077", "卡贷通额度");
			put("1160", "中银E贷");
			put("1125", "网络消费贷款");
			put("1131", "网络经营贷款");
			put("130012", "中银E贷");
			put("080012", "网络消费贷款");
			put("140006", "网络经营贷款");
		}
	};

	/** 额度查询错误信息字典 */
	public static final Map<String, String> loanTypeDataLimitError = new HashMap<String, String>() {
		{
			put("BANCS.1756", "暂未查询到您名下相关贷款额度信息,请前往中行网点或咨询95566.");
		}
	};
	/** 贷款管理详情页面——还款方式数据字典 */
	public static final Map<String, String> loanInterestType_mondy = new HashMap<String, String>() {
		{
			put("F", "等额本息");
			put("G", "等额本金");
			put("B", "按期还息到期还本");
			put("N", "按期还息灵活还本");
		}
	};
	public static final Map<String, String> loanUnit = new HashMap<String, String>() {
		{
			put("D", "(日)");
			put("W", "(周)");
			put("M", "(月)");
			put("Q", "(季)");
			put("Y", "(年)");
		}
	};
	/** 历史还款中还款方式字典 */
	public static final Map<String, String> historyTypeMap = new HashMap<String, String>() {
		{
			put("2", "还款");
			put("3", "提前还款");
			put("5", "还款");
			put("6", "还款");
			put("8", "还款");
			put("11034", "还款");
			put("11044", "提前还款");
			put("11045", "还款");
			put("11046", "还款");
			put("11048", "提前还款");
			put("16034", "还款");
			put("16044", "提前还款");
			put("16045", "还款");
			put("16046", "还款");
			put("11032", "还款");
			put("11040", "提前还款 ");
		}
	};
	/** 个人循环贷款的贷款品种----1046、1047 */
	public static final List<String> loanCycleList = new ArrayList<String>() {
		{
			add("080003");
			add("130003");
			add("080004");
			add("130004");
		}
	};
	/** 贷款状态字典 */
	public static final Map<String, String> loanStatusMap = new HashMap<String, String>() {
		{
			put("1", "正在处理中");
			put("2", "正在处理中");
			put("3", "未成功处理");
			put("4", "已完成");

		}
	};
	/**
	 * 拒绝原因字典 贷款状态=3“未成功处理”时有值 1:电话/mail有误，无法联系客户 2:电话无人接听，无法联系客户 3:客户贷款意愿取消
	 * */
	public static final Map<String, String> loanRepulseMap = new HashMap<String, String>() {
		{
			put("1", "电话/mail有误，无法联系客户");
			put("2", "电话无人接听，无法联系客户");
			put("3", "客户贷款意愿取消");

		}
	};

	/** 担保方式 */
	public static final Map<String, String> loanGuaWayMap = new HashMap<String, String>() {
		{
			put("1", "房产抵押");
			put("2", "有价权利质押");
			put("3", "其他");
		}
	};

	/** 判断男（1） 女（2） */
	public static final Map<String, String> loanAppSexMap = new HashMap<String, String>() {
		{
			put("1", "男");
			put("2", "女");
		}
	};

	/**
	 * 通过中文省份名称查找相对应的省份code值
	 */
	public static Map<String, String> Province = new HashMap<String, String>() {
		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;

		{
			put("北京", "40142");
			put("天津", "40202");
			put("上海", "40303");
			put("上海总部", "40005");
			put("上海自贸区", "40308");
			put("西藏", "40600");
			put("河北", "40740");
			put("山西", "41041");
			put("内蒙古", "41405");
			put("辽宁", "41785");
			put("吉林", "42208");
			put("黑龙江", "42465");
			put("陕西", "43016");
			put("甘肃", "43251");
			put("宁夏", "43347");
			put("青海", "43469");
			put("新疆", "43600");
			put("山东", "43810");
			put("江苏", "44433");
			put("安徽", "44899");
			put("浙江", "45129");
			put("福建", "45481");
			put("河南", "46243");
			put("湖北", "46405");
			put("湖南", "46955");
			put("江西", "47370");
			put("广东（不含深圳）", "47504");
			put("深圳", "47669");
			put("海南", "47806");
			put("广西", "48051");
			put("四川", "48631");
			put("重庆", "48642");
			put("贵州", "48882");
			put("云南", "49146");

		}
	};
	/**个贷申请 上送字典*/
	public static final Map<String, String> ApplyCurcodeMap = new HashMap<String, String>() {
		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;

		{
			
			put("人民币元", "001");
			put("美元", "1");
			put("英镑", "5");
			put("港币", "4");
			put("加元", "7");
			put("澳大利亚元", "6");
			put("欧元", "3");
			put("日元", "2");
		}
	};
	public static final Map<String, String> bociCurcodeMap = new HashMap<String, String>() {
		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;

		{
			put("人民币元", "001");
			put("美元", "014");
			put("英镑", "012");
			put("港币", "013");
			put("加元", "028");
			put("澳大利亚元", "029");
			put("欧元", "038");
			put("日元", "027");
		}
	};

	public static final Map<String, String> LoanApplyCurcodeMap = new HashMap<String, String>() {
		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;

		{
			
//			 @"1":   @"美元",
//           @"2":    @"日元",
//           @"3":    @"欧元",
//           @"4":    @"港币",
//           @"5":    @"英镑",
//           @"6":   @"澳元",
//           @"7":   @"加元"
			
			put( "1","美元");
			put( "5","英镑");
			put( "4","港币");
			put( "7","加元");
			put( "6","澳大利亚元");
			put( "3","欧元");
			put( "2","日元");

		}
	};

	
	/** 币种列表 */
	public static final List<String> currencyList = new ArrayList<String>() {
		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;

		{
			add("请选择");
			add("美元");
			add("日元");
			add("欧元");
			add("港币");
			add("英镑");
			add("澳大利亚元");
			add("加元");
		}
	};
	/** 担保方式 */
	public static final List<String> guaranteeWayList = new ArrayList<String>() {
		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;

		{
			add("请选择");
			add("房产抵押");
			add("有价权利质押");
			add("其他");
		}
	};
	/** 担保类别 */
	public static final List<String> guaranteeList = new ArrayList<String>() {
		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;

		{
			add("请选择");
			add("住房");
			add("商铺");
			add("土地");
			add("其他固定资产");
		}
	};
	/** 担保类别 */
	public static final Map<String, String> guaranteeKeyList = new HashMap<String, String>() {
		/**
		 * 1:住房 2:商铺 3:土地 4:其他固定资产
		 */
		private static final long serialVersionUID = 1L;

		{
			
			put("住房", "1");
			put("商铺", "2");
			put("土地", "3");
			put("其他固定资产", "4");

		}
	};
	/**603币种校验转化
	 * PsnLOANPayerAcountCheck还款账户检查
	 * PsnLOANPayeeAcountCheck收款账户检查
	 * 货币
	 */
	public static Map<String, String> Currency = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{//欧元、港币、日元、澳元
			put("001", "001");
			put("CNY", "001");
			put("012", "012");
			put("GBP", "012");
			// put("013", "港币");
			// put("HKD", "港币");
			// change sunh
			put("013", "013");
			put("HKD", "013");
			put("014", "014");
			put("USD", "014");
			put("015", "015");
			put("CHF", "015");
			put("016", "016");
			put("DEM", "016");
			put("017", "017");
			put("FRF", "017");
			put("018", "018");
			put("SGD", "018");
			put("020", "020");
			put("NLG", "020");
			put("021", "021");
			put("SEK", "021");
			put("022", "022");
			put("DKK", "022");
			put("023", "023");
			put("NOK", "023");
			put("024", "024");
			put("ATS", "024");
			put("025", "025");
			put("BEF", "025");
			put("026", "意大利里拉");
			put("ITL", "意大利里拉");
			put("027", "日元");
			put("JPY", "日元");
			put("028", "加拿大元");
			put("CAD", "加拿大元");
			put("029", "澳大利亚元");
			put("AUD", "澳大利亚元");
			put("068", "人民币银");
			put("036", "美元银");
			put("034", "美元金");
			put("XAU", "美元金");
			put("035", "人民币金");
			put("GLD", "人民币金");
			put("844", "人民币钯金");
			put("845", "人民币铂金");
			put("841", "美元钯金");
			put("045", "美元铂金");
			put("038", "欧元");
			put("EUR", "欧元");
			put("042", "芬兰马克");
			put("FIM", "芬兰马克");
			put("081", "澳门元");
			put("MOP", "澳门元");
			put("082", "菲律宾比索");
			put("PHP", "菲律宾比索");
			put("084", "泰国铢");
			put("THB", "泰国铢");
			put("087", "新西兰元");
			put("NZD", "新西兰元");
			put("088", "韩元");
			put("KRW", "韩元");
			put("094", "记账美元");
			put("095", "清算瑞士法郎");
			put("056", "印尼盾");
			put("IDR", "印尼盾");
			put("064", "越南盾");
			put("VND", "越南盾");
			// put("777", "阿联酋迪拉姆");
			put("096", "阿联酋迪拉姆");
			put("AED", "阿联酋迪拉姆");
			put("126", "阿根廷比索");
			put("ARP", "阿根廷比索");
			put("134", "巴西雷亚尔");
			put("BRL", "巴西雷亚尔");
			put("053", "埃及磅");
			put("EGP", "埃及磅");
			put("085", "印度卢比");
			put("INR", "印度卢比");
			put("057", "约旦第纳尔");
			put("JOD", "约旦第纳尔");
			put("179", "蒙古图格里克");
			put("MNT", "蒙古图格里克");
			put("076", "尼日尼亚奈拉");
			put("NGN", "尼日尼亚奈拉");
			put("062", "罗马尼亚列伊");
			put("ROL", "罗马尼亚列伊");
			put("093", "土耳其里拉");
			put("TRL", "土耳其里拉");
			put("246", "乌克兰格里夫纳");
			put("UAH", "乌克兰格里夫纳");
			put("070", "南非兰特");
			put("ZAR", "南非兰特");
			put("101", "哈萨克斯坦坚戈");
			put("KZT", "哈萨克斯坦坚戈");
			put("080", "赞比亚克瓦查");
			put("ZMK", "赞比亚克瓦查");
			put("065", "匈牙利福林");
			put("HUF", "匈牙利福林");
			// put("032", "林吉特");
			// put("MYR", "林吉特");
			// change sunh
			put("032", "林吉特");
			put("MYR", "林吉特");
			put("-1", "通配");
			// put("096", "美元指数");
			put("251", "阿富汗尼");
			put("AFN", "阿富汗尼");
			put("061", "阿尔巴尼亚列克");
			put("ALL", "阿尔巴尼亚列克");
			put("041", "阿尔及利亚第纳尔");
			put("DZD", "阿尔及利亚第纳尔");
			put("128", "阿塞拜疆马纳特");
			put("AZM", "阿塞拜疆马纳特");
			put("135", "巴哈马元");
			put("BSD", "巴哈马元");
			put("009", "巴林第纳尔");
			put("BHD", "巴林第纳尔");
			put("010", "孟加拉国塔卡");
			put("BDT", "孟加拉国塔卡");
			put("123", "亚美尼亚德拉姆");
			put("AMD", "亚美尼亚德拉姆");
			put("129", "巴巴多斯元");
			put("BBD", "巴巴多斯元");
			put("130", "百慕大元");
			put("BMD", "百慕大元");
			put("132", "玻利维亚玻利瓦诺");
			put("BOB", "玻利维亚玻利瓦诺");
			put("138", "伯利兹元");
			put("BZD", "伯利兹元");
			put("199", "所罗门群岛元");
			put("SBD", "所罗门群岛元");
			put("066", "保加利亚列弗");
			put("BGL", "保加利亚列弗");
			put("178", "缅元");
			put("MMK", "缅元");
			put("051", "布隆迪法郎");
			put("BIF", "布隆迪法郎");
			put("144", "佛得角埃斯库多");
			put("CVE", "佛得角埃斯库多");
			put("168", "开曼群岛元");
			put("KYD", "开曼群岛元");
			put("040", "斯里兰卡卢比");
			put("LKR", "斯里兰卡卢比");
			put("140", "智利比索");
			put("CLP", "智利比索");
			put("141", "哥伦比亚比索");
			put("COP", "哥伦比亚比索");
			put("167", "科摩罗法郎");
			put("KMF", "科摩罗法郎");
			put("142", "哥斯达黎加科郎");
			put("CRC", "哥斯达黎加科郎");
			put("159", "克罗地亚库纳");
			put("HRK", "克罗地亚库纳");
			put("052", "塞浦路斯镑");
			put("CYP", "塞浦路斯镑");
			put("145", "捷克克朗");
			put("CZK", "捷克克朗");
			put("147", "多米尼加比索");
			put("DOP", "多米尼加比索");
			put("206", "萨尔瓦多科郎");
			put("SVC", "萨尔瓦多科郎");
			put("054", "埃塞俄比亚比尔");
			put("ETB", "埃塞俄比亚比尔");
			put("235", "厄立特里亚纳克法");
			put("ERN", "厄立特里亚纳克法");
			put("150", "爱沙尼亚克罗姆");
			put("EEK", "爱沙尼亚克罗姆");
			put("152", "福克兰群岛镑");
			put("FKP", "福克兰群岛镑");
			put("151", "斐济元");
			put("FJD", "斐济元");
			put("146", "吉布提法郎");
			put("DJF", "吉布提法郎");
			put("154", "冈比亚达拉西");
			put("GMD", "冈比亚达拉西");
			put("153", "直布罗陀镑");
			put("GIP", "直布罗陀镑");
			put("155", "危地马拉格查尔");
			put("GTQ", "危地马拉格查尔");
			put("050", "几内亚法郎");
			put("GNF", "几内亚法郎");
			put("157", "圭亚那元");
			put("GYD", "圭亚那元");
			put("160", "海地古德");
			put("HTG", "海地古德");
			put("158", "洪都拉斯伦皮拉");
			put("HNL", "洪都拉斯伦皮拉");
			put("163", "冰岛克朗");
			put("ISK", "冰岛克朗");
			put("048", "伊朗里亚尔");
			put("IRR", "伊朗里亚尔");
			put("044", "伊拉克第纳尔");
			put("IQD", "伊拉克第纳尔");
			put("162", "新以色列谢克尔");
			put("ILS", "新以色列谢克尔");
			put("164", "牙买加元");
			put("JMD", "牙买加元");
			put("058", "肯尼亚先令");
			put("KES", "肯尼亚先令");
			put("063", "北朝鲜圆");
			put("KPW", "北朝鲜圆");
			put("059", "科威特第纳尔");
			put("KWD", "科威特第纳尔");
			put("165", "吉尔吉斯斯坦索姆");
			put("KGS", "吉尔吉斯斯坦索姆");
			put("169", "老挝基普");
			put("LAK", "老挝基普");
			put("170", "黎巴嫩镑");
			put("LBP", "黎巴嫩镑");
			put("175", "拉脱维亚拉特");
			put("LVL", "拉脱维亚拉特");
			put("171", "利比里亚元");
			put("LRD", "利比里亚元");
			put("071", "利比亚第纳尔");
			put("LYD", "利比亚第纳尔");
			put("173", "立陶宛立特");
			put("LTL", "立陶宛立特");
			put("183", "马维拉克瓦查");
			put("MWK", "马维拉克瓦查");
			put("182", "马尔代夫卢菲亚");
			put("MVR", "马尔代夫卢菲亚");
			put("181", "马尔他里拉");
			put("MTL", "马尔他里拉");
			put("180", "毛里塔尼亚乌吉亚");
			put("MRO", "毛里塔尼亚乌吉亚");
			put("075", "毛里求斯卢比");
			put("MUR", "毛里求斯卢比");
			put("184", "墨西哥比索");
			put("MXN", "墨西哥比索");
			put("176", "摩尔多瓦列伊");
			put("MDL", "摩尔多瓦列伊");
			put("046", "摩洛哥迪拉姆");
			put("MAD", "摩洛哥迪拉姆");
			put("185", "莫桑比克麦梯卡尔");
			put("MZM", "莫桑比克麦梯卡尔");
			put("188", "阿曼里亚尔");
			put("OMR", "阿曼里亚尔");
			put("186", "纳米比亚元");
			put("NAD", "纳米比亚元");
			put("049", "尼泊尔卢比");
			put("NPR", "尼泊尔卢比");
			put("124", "荷属安的列斯盾");
			put("ANG", "荷属安的列斯盾");
			put("127", "阿鲁巴盾");
			put("AWG", "阿鲁巴盾");
			put("219", "瓦努阿图瓦图");
			put("VUV", "瓦努阿图瓦图");
			put("187", "尼加拉瓜金科多巴");
			put("NIO", "尼加拉瓜金科多巴");
			put("019", "巴基斯坦卢比");
			put("PKR", "巴基斯坦卢比");
			put("189", "巴拿马巴波亚");
			put("PAB", "巴拿马巴波亚");
			put("077", "巴布亚新几内亚基那");
			put("PGK", "巴布亚新几内亚基那");
			put("194", "巴拉圭瓜拉尼");
			put("PYG", "巴拉圭瓜拉尼");
			put("190", "秘鲁索尔");
			put("PEN", "秘鲁索尔");
			put("156", "几内亚比绍比索");
			put("GWP", "几内亚比绍比索");
			put("211", "东帝汶埃斯库多");
			put("TPE", "东帝汶埃斯库多");
			put("195", "卡塔尔里亚尔");
			put("QAR", "卡塔尔里亚尔");
			put("078", "卢旺达法郎");
			put("RWF", "卢旺达法郎");
			put("200", "圣赫勒拿镑");
			put("SHP", "圣赫勒拿镑");
			put("205", "圣多美和普林西比多布拉");
			put("STD", "圣多美和普林西比多布拉");
			put("198", "沙特里亚尔");
			put("SAR", "沙特里亚尔");
			put("079", "塞舌尔卢比");
			put("SCR", "塞舌尔卢比");
			put("047", "塞拉利昂利昂");
			put("SLL", "塞拉利昂利昂");
			put("202", "斯洛伐克克朗");
			put("SKK", "斯洛伐克克朗");
			put("201", "斯洛文尼亚托拉尔");
			put("SIT ", "斯洛文尼亚托拉尔");
			put("203", "索马里先令");
			put("SOS", "索马里先令");
			put("224", "津巴布韦元");
			put("ZWD", "津巴布韦元");
			put("242", "苏丹第纳尔");
			put("SDD", "苏丹第纳尔");
			put("207", "斯威士兰里兰吉尼");
			put("SZL", "斯威士兰里兰吉尼");
			put("089", "叙利亚镑");
			put("SYP", "叙利亚镑");
			put("210", "汤加邦加");
			put("TOP", "汤加邦加");
			put("212", "特立尼达和多巴哥元");
			put("TTD", "特立尼达和多巴哥元");
			put("091", "突尼斯第纳尔");
			put("TND", "突尼斯第纳尔");
			put("209", "土库曼斯坦马纳特");
			put("TMM", "土库曼斯坦马纳特");
			put("215", "乌干达先令");
			put("UGX", "乌干达先令");
			put("177", "马其顿第纳尔");
			put("MKD", "马其顿第纳尔");
			put("030", "坦桑尼亚先令");
			put("TZS", "坦桑尼亚先令");
			put("216", "乌拉圭比索");
			put("UYU", "乌拉圭比索");
			put("217", "乌兹别克斯坦苏姆");
			put("UZS", "乌兹别克斯坦苏姆");
			put("247", "委内瑞拉玻利瓦尔");
			put("VEF", "委内瑞拉玻利瓦尔");
			put("220", "萨摩亚塔拉");
			put("WST", "萨摩亚塔拉");
			put("098", "也门里亚尔");
			put("YER", "也门里亚尔");
			put("221", "南斯拉夫第纳尔");
			put("YUM", "南斯拉夫第纳尔");
			put("252", "加纳塞地");
			put("GHC", "加纳塞地");
			put("245", "新土耳其里拉");
			put("TRY", "新土耳其里拉");
			put("820", "CAF 法郎 BEAC");
			put("XAF", "CAF 法郎 BEAC");
			put("825", "东加勒比元");
			put("XCD", "东加勒比元");
			put("099", "CFA 法郎 BCEAO");
			put("XOF", "CFA 法郎 BCEAO");
			put("842", "CFP 法郎");
			put("XPF", "CFP 法郎");
			put("248", "马达加斯加阿里亚里");
			put("MGA", "马达加斯加阿里亚里");
			put("244", "塔吉克索莫尼");
			put("TJS", "塔吉克索莫尼");
			put("250", "白俄罗斯卢布");
			put("BYR", "白俄罗斯卢布");
			put("229", "保加利亚列弗");
			put("BGN", "保加利亚列弗");
			put("236", "格鲁吉亚拉里");
			put("GEL", "格鲁吉亚拉里");
			put("191", "波兰兹罗提");
			put("PLN", "波兰兹罗提");
			put("226", "安哥拉宽扎");
			put("AOA", "安哥拉宽扎");
			put("039", "博茨瓦纳普拉");
			put("BWP", "博茨瓦纳普拉");
			put("131", "文莱币");
			put("BND", "文莱币");
			put("166", "柬埔寨瑞尔");
			put("KHR", "柬埔寨瑞尔");
			// put("213", "新台湾元");
			// put("TWD", "新台湾元");
			put("ZZZ", "其他币种");
			put("zzz", "其他币种");

			put("072", "俄罗斯贸易卢布");
			put("RUR", "俄罗斯贸易卢布");

			put("196", "卢布");
			put("RUB", "卢布");

			// add sunh
			put("056", "印尼卢比");
			put("IDR", "印尼卢比");
			put("134", "巴西里亚尔");
			put("BRL", "巴西里亚尔");
			put("213", "新台币");
			put("TWD", "新台币");

		}
	};
	
}
