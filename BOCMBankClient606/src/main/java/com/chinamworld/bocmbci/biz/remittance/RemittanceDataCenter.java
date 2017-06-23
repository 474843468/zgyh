package com.chinamworld.bocmbci.biz.remittance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.biz.remittance.adapter.model.RemittanceCollectionBankItem;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 跨境汇款数据中心
 * 
 * @author Zhi
 */
public class RemittanceDataCenter {

	private static RemittanceDataCenter instance;
	/** 模板列表总条数 */
	private String totalNumber;
	/** 扣款账户列表 */
	private List<Map<String, Object>> accList;
	/** 扣款账户详情 */
	private List<Map<String, Object>> accDetail;
	/** 模板详情 */
	private Map<String, Object> mapPsnInternationalTransferTemplateDetailQuery;
	/** 模板列表 */
	private List<Map<String, Object>> modelList;
	/** 跨境汇款预交易接口返回数据 */
	private Map<String, Object> mapPsnTransInternationalTransferConfirm;
	/** 汇款记录详情接口返回数据 */
	private Map<String, Object> mapPsnTransQueryTransferRecordDetail;
	/** 查询收款人常驻国家列表接口返回数据 */
	private List<Map<String, String>> listPsnQryInternationalTrans4CNYCountry;
	/** 查询收款银行SWIFT列表 */
	private List<Map<String, Object>> listPsnInternationalTransferSwiftQuery;
	/** 查询可用额度返回数据 */
	private Map<String, Object> mapPsnQueryNationalTransferLimit;
	/** 跨境汇款提交交易参数列表 */
	private Map<String, Object> submitParams;
	/** 跨境汇款用户输入数据 */
	private Map<String, Object> userInput;

	/** 费用试算返回数据 */
	private Map<String, Object> mapPsnTransGetInternationalTransferCommissionCharge;
	/** IBAN账号合法性校验返回数据 */
	private Map<String, Object> mapPsnIbanFormatCheck;
	/** 境外中行收款行列表*/
	//private List<Map<String, Object>> collectionBankList;
	/** 境外中行收款行地区（国家）列表*/
	private List<String> bocPayeeBankRegionList;
	/** 境外中行某一地区对应得收款行列表*/
	private List<Map<String, Object>> listBySear ;
	/** 境外中行收款行Map*/
	private Map<String, Object> mapPayeeBankOver;
	/** 获取用户输入数据 */
	public Map<String, Object> getUserInput() {
		return userInput;
	}

	/** 设置用户输入数据 */
	public void setUserInput(Map<String, Object> userInput) {
		this.userInput = userInput;
	}
	
	/** 获取模板列表总条数 */
	public String getTotalNumber() {
		return totalNumber;
	}

	/** 设置模板总条数 */
	public void setTotalNumber(String totalNumber) {
		this.totalNumber = totalNumber;
	}

	/** 获取IBAN账号合法性校验返回数据 */
	public Map<String, Object> getMapPsnIbanFormatCheck() {
		return mapPsnIbanFormatCheck;
	}

	/** 设置IBAN账号合法性校验返回数据 */
	public void setMapPsnIbanFormatCheck(
			Map<String, Object> mapPsnIbanFormatCheck) {
		this.mapPsnIbanFormatCheck = mapPsnIbanFormatCheck;
	}

	/** 获取费用试算返回数据 */
	public Map<String, Object> getMapPsnTransGetInternationalTransferCommissionCharge() {
		return mapPsnTransGetInternationalTransferCommissionCharge;
	}

	/** 设置费用试算返回数据 */
	public void setMapPsnTransGetInternationalTransferCommissionCharge(
			Map<String, Object> mapPsnTransGetInternationalTransferCommissionCharge) {
		this.mapPsnTransGetInternationalTransferCommissionCharge = mapPsnTransGetInternationalTransferCommissionCharge;
	}

	/** 获取跨境汇款提交交易参数列表 */
	public Map<String, Object> getSubmitParams() {
		return submitParams;
	}

	/** 设置跨境汇款提交交易参数列表 */
	public void setSubmitParams(Map<String, Object> submitParams) {
		this.submitParams = submitParams;
	}


	private String bankSearsOver;
	/** 获取跨境汇款境外中行收款行地区 */
	public String getbankSearsOver() {
		return bankSearsOver;
	}
	/** 设置跨境汇款境外中行收款行地区  */
	public void setbankSearsOver(String bankSearsOver) {
		this.bankSearsOver=bankSearsOver;
	}

	/** 获取查询可用额度返回数据 */
	public Map<String, Object> getMapPsnQueryNationalTransferLimit() {
		return mapPsnQueryNationalTransferLimit;
	}

	/** 设置查询可用额度返回数据 */
	public void setMapPsnQueryNationalTransferLimit(
			Map<String, Object> mapPsnQueryNationalTransferLimit) {
		this.mapPsnQueryNationalTransferLimit = mapPsnQueryNationalTransferLimit;
	}

	/** 获取查询收款银行SWIFT列表 */
	public List<Map<String, Object>> getListPsnInternationalTransferSwiftQuery() {
		return listPsnInternationalTransferSwiftQuery;
	}

	/** 设置查询收款银行SWIFT列表 */
	public void setListPsnInternationalTransferSwiftQuery(
			List<Map<String, Object>> listPsnInternationalTransferSwiftQuery) {
		this.listPsnInternationalTransferSwiftQuery = listPsnInternationalTransferSwiftQuery;
	}

	/** 获取查询收款人常驻国家列表接口返回数据 */
	public List<Map<String, String>> getListPsnQryInternationalTrans4CNYCountry() {
		return listPsnQryInternationalTrans4CNYCountry;
	}

	/** 设置查询收款人常驻国家列表接口返回数据 */
	public void setListPsnQryInternationalTrans4CNYCountry(
			List<Map<String, String>> listPsnQryInternationalTrans4CNYCountry) {
		this.listPsnQryInternationalTrans4CNYCountry = listPsnQryInternationalTrans4CNYCountry;
	}

	/** 获取汇款记录详情接口返回数据 */
	public Map<String, Object> getMapPsnTransQueryTransferRecordDetail() {
		return mapPsnTransQueryTransferRecordDetail;
	}

	/** 设置汇款记录详情接口返回数据 */
	public void setMapPsnTransQueryTransferRecordDetail(
			Map<String, Object> mapPsnTransQueryTransferRecordDetail) {
		this.mapPsnTransQueryTransferRecordDetail = mapPsnTransQueryTransferRecordDetail;
	}

	/** 获取跨境汇款预交易接口返回数据 */
	public Map<String, Object> getMapPsnTransInternationalTransferConfirm() {
		return mapPsnTransInternationalTransferConfirm;
	}

	/** 设置跨境汇款预交易接口返回数据 */
	public void setMapPsnTransInternationalTransferConfirm(
			Map<String, Object> mapPsnTransInternationalTransferConfirm) {
		this.mapPsnTransInternationalTransferConfirm = mapPsnTransInternationalTransferConfirm;
	}

	/** 跨境汇款提交返回数据 */
	private Map<String, Object> mapPsnTransInternationalTransferSubmit;

	/** 获取跨境汇款提交返回数据 */
	public Map<String, Object> getMapPsnTransInternationalTransferSubmit() {
		return mapPsnTransInternationalTransferSubmit;
	}

	/** 设置跨境汇款提交返回数据 */
	public void setMapPsnTransInternationalTransferSubmit(
			Map<String, Object> mapPsnTransInternationalTransferSubmit) {
		this.mapPsnTransInternationalTransferSubmit = mapPsnTransInternationalTransferSubmit;
	}

	/** 获取模板详情 */
	public Map<String, Object> getMapPsnInternationalTransferTemplateDetailQuery() {
		return mapPsnInternationalTransferTemplateDetailQuery;
	}

	/** 设置模板详情 */
	public void setMapPsnInternationalTransferTemplateDetailQuery(
			Map<String, Object> mapPsnInternationalTransferTemplateDetailQuery) {
		this.mapPsnInternationalTransferTemplateDetailQuery = mapPsnInternationalTransferTemplateDetailQuery;
	}

	/** 获取扣款账户列表 */
	public List<Map<String, Object>> getAccList() {
		return accList;
	}

	/** 设置扣款账户列表 */
	public void setAccList(List<Map<String, Object>> accList) {
		this.accList = accList;
	}

	/** 获取扣款账户详情 */
	public List<Map<String, Object>> getAccDetail() {
		return accDetail;
	}

	/** 设置扣款账户详情 */
	public void setAccDetail(List<Map<String, Object>> accDetail) {
		this.accDetail = accDetail;
	}

	/** 获取模板列表 */
	public List<Map<String, Object>> getModelList() {
		return modelList;
	}

	/** 设置模板列表 */
	public void setModelList(List<Map<String, Object>> modelList) {
		this.modelList = modelList;
	}
	
	/** 设置境外中行收款行列表 */
//	public void setCollectionBankList(List<Map<String, Object>> collectionBankList) {
//		this.collectionBankList = collectionBankList;
//	}
	
	/** 获取境外中行收款行列表 */
//	public List<Map<String, Object>> getCollectionBankList() {
//		return collectionBankList;
//	}

	/** 设置境外中行收款行地区（国家）列表 */
	public void setBocPayeeBankRegionList(List<String> bocPayeeBankRegionList) {
		this.bocPayeeBankRegionList = bocPayeeBankRegionList;
	}

	/** 获取境外中行收款行地区（国家）列表集合 */
	public List<String> getBocPayeeBankRegionList() {
		return bocPayeeBankRegionList;
	}

	/** 设置境外中行某一地区收款行列表 */
	public void setlistBySear(List<Map<String, Object>> listBySear) {
		this.listBySear = listBySear;
	}

	/** 获取境外中行某一地区收款行列表 */
	public List<Map<String, Object>> getlistBySear() {
		return listBySear;
	}

	/** 设置境外中行收款行Map */
	public void setmapPayeeBankOver(Map<String, Object> mapPayeeBankOver) {
		this.mapPayeeBankOver = mapPayeeBankOver;
	}

	/** 获取境外中行某一地区收款行列表 */
	public Map<String, Object> getmapPayeeBankOver() {
		return mapPayeeBankOver;
	}
	/** 构造方法 */

	private RemittanceDataCenter() {
	}

	/** 获取数据中心单例 */
	public static RemittanceDataCenter getInstance() {
		if (instance == null) {
			instance = new RemittanceDataCenter();
			return instance;
		} else {
			return instance;
		}
	}

	/** 汇款地区中文 */
	public static List<String> payeeArea = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("请选择收款地区");
			add("澳洲");
			add("欧盟国家/地区");
			add("英国");
			add("加拿大");
			add("日本");
			add("其它地区");
		}
	};

	/** 汇款类型 */
	public static List<String> remittanceMold = new ArrayList<String>() {
		{
			add("向境外中行汇款");
			add("向境外他行汇款");

		}
	};

	/** 汇款地区代码，与中文列表一一对应，不用操作下标 */
	public static List<String> payeeAreaCode = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("");
			add("AU");
			add("EU");
			add("GB");
			add("CA");
			add("JP");
			add("ZZ");
		}
	};

	/**
	 * 国家对应代码
	 */
	public static Map<String, String> payeeAreaCodeByCountry = new HashMap<String, String>() {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "AU");
			put("2", "EU");
			put("3", "GB");
			put("4", "CA");
			put("5", "JP");
			put("6", "ZZ");

		}
	};

	/** 汇款人信息请求转出账户类型 */
	public static List<String> payAcc = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("119");
			add("188");
		}
	};
	
	/** 特殊币种-日元 */
	public static List<String> spetialCodeList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("027");
			add("JPY");
			add("088");
			add("KRW");
		}
	};

	/** 汇款用途代码 */
	public static List<String> listUseCode = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("");
			add("223022");
			add("223023");
			add("223021");
			add("223010");
			add("223029");
			add("122030");
			add("227030");
			add("421010");
			add("421990");
			add("228050");
			add("228021");
			add("222040");
			add("231020");
			add("227010");
			add("227020");
			add("228024");
			add("226000");
			add("228022");
			add("228023");
//			add("232000");
//			add("822030");
		}
	};

	/** 汇款用途中文 */
	public static List<String> listUseCN = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("请选择汇款用途");
			add("留学及教育相关旅行（一年以上）");
			add("留学及教育相关旅行（一年及一年以下）");
			add("就医及健康相关旅行");
			add("公务及商务旅行");
			add("其他私人旅行");
			add("未纳入海关统计的网络购物");
			add("信息服务（非批量订购报纸、期刊、书籍、电子出版物等）");
			add("个人间捐赠及无偿援助");
			add("其他捐赠及无偿援助");
			add("服务交易佣金");
			add("法律服务");
			add("邮政及寄递服务");
			add("研发成果使用费");
			add("电信服务");
			add("计算机服务");
			add("广告服务（广告设计、创意、媒体投放等）");
			add("金融服务（银行收取各项费用）");
			add("会计服务");
			add("管理咨询和公共关系服务");
//			add("别处未覆盖的政府货物和服务");
//			add("非居民向境外付款");
		}
	};

	/** 外币跨境汇款币种-数字代码 */
	public static List<String> currency_NUM = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("014");
			add("029");
			add("028");
			add("015");
			add("038");
			add("012");
			add("013");
			add("027");
			add("018");
			add("022");
			add("021");
			add("023");
			add("087");
			add("081");
			add("196");
			add("070");
			add("084");
			add("032");
			add("088");
		}
	};

	/** 外币跨境汇款币种-数字代码 --境外中行*/
	public static List<String> currency_over_NUM = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("014");
			add("029");
			add("028");
			add("013");
			add("012");

			add("038");
			add("027");
			add("087");
			add("018");
			add("084");

			add("088");
			add("015");
			add("021");
			add("022");
			add("196");

			add("023");
			add("081");
			add("070");

		}
	};

	/** 外币跨境汇款币种-字母代码 */
	public static List<String> currency_CHAR = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("USD");
			add("AUD");
			add("CAD");
			add("CHF");
			add("EUR");
			add("GBP");
			add("HKD");
			add("JPY");
			add("SGD");
			add("DKK");
			add("SEK");
			add("NOK");
			add("NZD");
			add("MOP");
			add("RUB");
			add("ZAR");
			add("THB");
			add("MYR");
			add("KRW");
		}
	};

	/** 非居民证件类型列表，此列表内的证件类型不显示填写外管申报信息 */
	public static List<String> FResident = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("6");
			add("7");
			add("8");
			add("9");
			add("10");
			add("12");
			add("13");
			add("47");
			add("48");
			add("49");
		}
	};

	/** 转账状态 */
	public static Map<String, String> channel = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("A", "交易成功");
			put("8", "已删除");
			put("B", "交易失败");
			put("12", "交易失败");
			put("G", "银行处理中");
			put("7", "提交成功");
			put("H", "银行处理中");
			put("F", "待银行处理");
			put("I", "提交成功");
			put("K", "支付未明");
			put("L", "银行已受理");
		}
	};

	public void clearAllData() {
		if (!StringUtil.isNullOrEmpty(accList)) {
			accList.clear();
		}
		if (!StringUtil.isNullOrEmpty(accDetail)) {
			accDetail.clear();
		}
		if (!StringUtil
				.isNullOrEmpty(mapPsnInternationalTransferTemplateDetailQuery)) {
			mapPsnInternationalTransferTemplateDetailQuery.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapPsnTransInternationalTransferSubmit)) {
			mapPsnTransInternationalTransferSubmit.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapPsnTransInternationalTransferConfirm)) {
			mapPsnTransInternationalTransferConfirm.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapPsnTransQueryTransferRecordDetail)) {
			mapPsnTransQueryTransferRecordDetail.clear();
		}
		if (!StringUtil.isNullOrEmpty(listPsnQryInternationalTrans4CNYCountry)) {
			listPsnQryInternationalTrans4CNYCountry.clear();
		}
		if (!StringUtil.isNullOrEmpty(listPsnInternationalTransferSwiftQuery)) {
			listPsnInternationalTransferSwiftQuery.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapPsnQueryNationalTransferLimit)) {
			mapPsnQueryNationalTransferLimit.clear();
		}
		if (!StringUtil.isNullOrEmpty(submitParams)) {
			submitParams.clear();
		}
		if (!StringUtil
				.isNullOrEmpty(mapPsnTransGetInternationalTransferCommissionCharge)) {
			mapPsnTransGetInternationalTransferCommissionCharge.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapPsnIbanFormatCheck)) {
			mapPsnIbanFormatCheck.clear();
		}
		if (!StringUtil.isNullOrEmpty(modelList)) {
			modelList.clear();
		}
		if (!StringUtil.isNullOrEmpty(userInput)) {
			userInput.clear();
		}
	}

	/** 交易渠道状态 */
	public static Map<String, String> channel_new = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("1", "网上银行");
			put("2", "手机银行");
			put("6", "银企对接");
			put("4", "家居银行");
			put("5", "微信银行");
		}
	};
	/** 执行方式 */
	public static Map<String, String> run_modle = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("0", "立即执行");
			put("1", "预约执行");
			put("2", "预约周期");

		}
	};
	/** 五个风险国 */
	public static List<String> fiveCountry =new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("IRN");
			add("SYR");
			add("SDN");
			add("CUB");
			add("PRK");
		}
	};
	/** 收款人常驻国家（地区） */
	public static Map<String, String> countryMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("ZZZ", "其他");
			put("ALB", "阿尔巴尼亚");
			put("DZA", "阿尔及利亚");
			put("AFG", "阿富汗");
			put("ARG", "阿根廷");
			put("ARE", "阿联酋");
			put("ABW", "阿鲁巴");
			put("OMN", "阿曼");
			put("AZE", "阿塞拜疆");
			put("EGY", "埃及");
			put("ETH", "埃塞俄比亚");
			put("IRL", "爱尔兰");
			put("EST", "爱沙尼亚");
			put( "AND","安道尔");
			put( "AGO","安哥拉");
			put( "AIA","安圭拉");
			put( "ATG","安提瓜和巴布达");
			put( "AUT","奥地利");
			put( "AUS","澳大利亚");
			put( "MAC","澳门");
			put( "BRB","巴巴多斯");
			put( "PNG","巴布亚新几内亚");
			put("BHS", "巴哈马");

			put("PAK", "巴基斯坦");
			put("PRY", "巴拉圭");
			put("PSE", "巴勒斯坦");
			put("BHR", "巴林");
			put("PAN", "巴拿马");
			put("BRA", "巴西");
			put("BLR", "白俄罗斯");
			put("BMU", "百慕大");
			put("MNP", "北马里亚纳");
			put("BEN", "贝宁");
			put("BEL", "比利时");
			put("PER", "秘鲁");
			put("ISL", "冰岛");
			put("PRI", "波多黎各");
			put("BIH", "波黑");
			put("POL", "波兰");
			put("BOL", "玻利维亚");
			put("BLZ", "伯利兹");
			put("BWA", "博茨瓦纳");
			put("BTN", "不丹");
			put("BFA", "布基纳法索");
			put("BDI", "布隆迪");
			put("BVT", "布维岛");
			put("PRK", "朝鲜");
			put("GNQ", "赤道几内亚");
			put("DNK", "丹麦");
			put("DEU", "德国");
			put("TLS", "东帝汶");
			put("TGO", "多哥");
			put("DOM", "多米尼加");
			put("DMA", "多米尼克");
			put( "RUS","俄罗斯联邦");
			put("ECU", "厄瓜多尔");
			put("ERI", "厄立特里亚");
			put("FRA", "法国");
			put("FRO", "法罗群岛");
			put("PYF", "法属波利尼西亚");
			put("GUF", "法属圭亚那");
			put("ATF", "法属南部领地");
			put("VAT", "梵蒂冈");
			put("PHL", "菲律宾");
			put("FJI", "斐济");
			put("FIN", "芬兰");
			put("CPV", "佛得角");
			put("FLK", "福克兰群岛(马尔维纳斯)");

			put("GMB", "冈比亚");
			put("COG", "刚果(布)");
			put("COD", "刚果(金)");
			put("COL", "哥伦比亚");
			put("CRI", "哥斯达黎加");
			put("GRD", "格林纳达");
			put("GRL", "格陵兰");
			put("GEO", "格鲁吉亚");
			put("CUB", "古巴");
			put("GLP", "瓜德罗普");
			put("GUM", "关岛");
			put("GUY", "圭亚那");
			put("KAZ", "哈萨克斯坦");
			put("HTI", "海地");
			put("KOR", "韩国");
			put("NLD", "荷兰");
			put("ANT", "荷属安的列斯");
			put("HMD", "赫德岛和麦克唐纳岛");
			put("HND", "洪都拉斯");
			put("KIR", "基里巴斯");
			put("DJI", "吉布提");
			put("KGZ", "吉尔吉斯斯坦");
			put("GIN", "几内亚");
			put("GNB", "几内亚比绍");
			put("CAN", "加拿大");
			put("GHA", "加纳");
			put("GAB", "加蓬");
			put("KHM", "柬埔寨");
			put("CZE", "捷克");
			put("ZWE", "津巴布韦");
			put("CMR", "喀麦隆");
			put("QAT", "卡塔尔");
			put( "CYM","开曼群岛");
			put("CCK", "科科斯(基林)群岛");
			put("COM", "科摩罗");
			put("CIV", "科特迪瓦");
			put("KWT", "科威特");
			put("HRV", "克罗地亚");
			put("KEN", "肯尼亚");
			put("COK", "库克群岛");
			put("LVA", "拉脱维亚");
			put("LSO", "莱索托");
			put("LAO", "老挝");
			put("LBN", "黎巴嫩");
			put("LTU", "立陶宛");
			put("LBR", "利比里亚");
			put("LBY", "利比亚");
			put("LIE", "列支敦士登");
			put("REU", "留尼汪");
			put("LUX", "卢森堡");
			put("RWA", "卢旺达");
			put("ROM", "罗马尼亚");
			put("MDG", "马达加斯加");
			put("MDV", "马尔代夫");
			put("MLT", "马耳他");
			put("MWI", "马拉维");
			put("MYS", "马来西亚");
			put("MLI", "马里");
			put("MHL", "马绍尔群岛");
			put("MTQ", "马提尼克");
			put("MYT", "马约特");
			put("MUS", "毛里求斯");
			put("MRT", "毛里塔尼亚");
			put("USA", "美国");
			put("UMI", "美国本土外小岛屿");
			put("ASM", "美属萨摩亚");
			put("VIR", "美属维尔京群岛");
			put("MNG", "蒙古");
			put("MSR", "蒙特塞拉特");
			put("BGD", "孟加拉国");
			put("FSM", "密克罗尼西亚联邦");
			put("MMR", "缅甸");
			put("MDA", "摩尔多瓦");
			put("MAR", "摩洛哥");
			put("MCO", "摩纳哥");
			put("MOZ", "莫桑比克");
			put("MEX", "墨西哥");
			put("NAM", "纳米比亚");
			put("ZAF", "南非");
			put("ATA", "南极洲");
			put("SGS", "南乔治亚岛和南桑德韦奇岛");
			put("YUG", "南斯拉夫");
			put("NRU", "瑙鲁");
			put("NPL", "尼泊尔");
			put("NIC", "尼加拉瓜");
			put("NER", "尼日尔");
			put("NGA", "尼日利亚");
			put("NIU", "纽埃");
			put("NOR", "挪威");
			put("NFK", "诺福克岛");
			put("PLW", "帕劳");
			put("PCN", "皮特凯恩");
			put("PRT", "葡萄牙");
			put("MKD", "前南马其顿");
			put("JPN", "日本");
			put("SWE", "瑞典");
			put("CHE", "瑞士");
			put("SLV", "萨尔瓦多");
			put("WSM", "萨摩亚");
			put("SLE", "塞拉利昂");
			put("SEN", "塞内加尔");
			put("CYP", "塞浦路斯");
			put("SYC", "塞舌尔");
			put("SAU", "沙特阿拉伯");
			put("CXR", "圣诞岛");
			put("STP", "圣多美和普林西比");
			put("SHN", "圣赫勒拿");
			put("KNA", "圣基茨和尼维斯");
			put("LCA", "圣卢西亚");
			put("SMR", "圣马力诺");
			put("SPM", "圣皮埃尔和密克隆");
			put("VCT", "圣文森特和格林纳丁斯");
			put("LKA", "斯里兰卡");
			put("SVK", "斯洛伐克");
			put("SVN", "斯洛文尼亚");
			put("SJM", "斯瓦尔巴岛和扬马延岛");
			put("SWZ", "斯威士兰");
			put("SDN", "苏丹");
			put("SUR", "苏里南");
			put("SLB", "所罗门群岛");
			put("SOM", "索马里");
			put("TJK", "塔吉克斯坦");
			put("THA", "泰国");
			put("TZA", "坦桑尼亚");
			put("TON", "汤加");
			put("TCA", "特克斯和凯科斯群岛");
			put("TTO", "特立尼达和多巴哥");
			put("TUN", "突尼斯");
			put("TUV", "图瓦卢");
			put("TUR", "土耳其");
			put("TKM", "土库曼斯坦");
			put("TKL", "托克劳");
			put("WLF", "瓦利斯和富图纳");
			put("VUT", "瓦努阿图");
			put("GTM", "危地马拉");
			put("VEN", "委内瑞拉");
			put("BRN", "文莱");
			put("UGA", "乌干达");
			put("UKR", "乌克兰");
			put("URY", "乌拉圭");
			put("UZB", "乌兹别克斯坦");
			put("ESP", "西班牙");
			put("ESH", "西撒哈拉");
			put("GRC", "希腊");
			put("HKG", "香港");
			put("SGP", "新加坡");
			put("NCL", "新喀里多尼亚");
			put("NZL", "新西兰");
			put("HUN", "匈牙利");
			put("SYR", "叙利亚");
			put("JAM", "牙买加");
			put("ARM", "亚美尼亚");
			put("YEM", "也门");
			put("IRQ", "伊拉克");
			put("IRN", "伊朗");
			put("ISR", "以色列");
			put("ITA", "意大利");
			put("IND", "印度");
			put("IDN", "印度尼西亚");
			put("GBR", "英国");
			put("IOT", "英属印度洋领地");
			put("VNM", "越南");
			put("TCD", "乍得");
			put("GIB", "直布罗陀");
			put("CHN", "中国");
			put("VGB", "英属维尔京群岛");
			put("JOR", "约旦");
			put("ZMB", "赞比亚");
			put("CHL", "智利");
			put("CAF", "中非");
			put("TWN", "中国台湾");

		}
	};

}
