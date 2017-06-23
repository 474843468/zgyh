package com.chinamworld.bocmbci.biz.plps;

import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * @author Administrator
 *
 */
public class PlpsDataCenter {
	private static PlpsDataCenter instance;
	/** 系统时间  **/
	private String sysDate;
	/** 我的民生二级菜单  **/
	private List<Map<String, Object>> liveMenus;
	/** 常用缴费详情  **/
	private Map<String, Object> historyRecodDetail;
	/** 养老金计划列表  **/
	private List<Map<String, Object>> planList;
	/** 养老金账户信息列表  **/
	private List<Map<String, Object>> acctInfoList;
	/** 养老金信息 **/
	private List<Map<String, Object>> annuityList;
	/** 养老金个人信息  **/
	private Map<String, Object> annuityUserInfo;
	/** 签约信息查询  **/
	private List<Map<String, Object>> signList;
	/** 账户列表  **/
	private List<Map<String, Object>> acctList;
	/** 业务类型  **/
	private List<Map<String, Object>> serviceType;
	/** 名声缴费项目的省列表 */
//	private List<Map<String, String>> prvcListName;
	/**养老金申请变更查询详情*/
	private Map<String, Object> annuityListDetail;
	/**省简称*/
	private String prvcShortName;
	/**声明称*/
	private String prvcDispName;
	/**城市*/
	private String cityDispName;
	/**城市列表*/
	private List<Map<String, Object>> cityList;
	/**预付卡类型*/
//	private List<Map<String, Object>> resultCardType;
	/** 预付卡充值预交易页面用户输入数据 */
	private Map<String, Object> mapPrepaidCardRechPre;
	/** 预付卡充值提交交易返回数据 */
	private Map<String, Object> mapPrepaidCardQuerySupportCardType;
	/**某地区所有缴费项目*/
	private List<Map<String, Object>> allPaymentList;
	/**常用缴费项目*/
	private List<Map<String, Object>> commonPaymentList;
	/**查询所支持的商户*/
	private List<Map<String, Object>> queryFlowFile;
	/**市多语言代码*/
	private String displayNo;
	/**预交易返回*/
	private Map<String, Object> resultObj;
	/**服务小类item*/
	private String itemId;
	/**服务小类catName*/
	private String catName;
	/**服务大类*/
	private String menuName;
	/**商户名*/
	private String flowFileName;
	private String flowFileId;
	/**根据决定书编号查询交通罚款返回信息*/
	private Map<String, Object> DecisionBookNo;
	/**交通罚款预交易返回*/
	private Map<String, Object> interMapresult;
	/**交通罚款提交交易返回信息*/
	private Map<String, Object> interMapSubmitresult;
	/**交通罚款查询详情返回*/
	private Map<String, Object> interDetailQueryResult;
	/**常用服务页面 服务图标 删除图标显示标识*/
	private Boolean isDelete;
	/**是否回显数据域*/
	private String isDisp;
	/**步骤名称*/
	private String stepName;
	/**回显域键值结合*/
	private Map<String, Object> dispMap;
	
	public Map<String, Object> getDispMap() {
		return dispMap;
	}
	public void setDispMap(Map<String, Object> dispMap) {
		this.dispMap = dispMap;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getIsDisp() {
		return isDisp;
	}
	public void setIsDisp(String isDisp) {
		this.isDisp = isDisp;
	}
	
	public Map<String, Object> getInterMapSubmitresult() {
		return interMapSubmitresult;
	}
	public void setInterMapSubmitresult(Map<String, Object> interMapSubmitresult) {
		this.interMapSubmitresult = interMapSubmitresult;
	}
	public Boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	public Map<String, Object> getInterDetailQueryResult() {
		return interDetailQueryResult;
	}
	public void setInterDetailQueryResult(Map<String, Object> interDetailQueryResult) {
		this.interDetailQueryResult = interDetailQueryResult;
	}
	public Map<String, Object> getInterMapresult() {
		return interMapresult;
	}
	public void setInterMapresult(Map<String, Object> interMapresult) {
		this.interMapresult = interMapresult;
	}
	public Map<String, Object> getDecisionBookNo() {
		return DecisionBookNo;
	}
	public void setDecisionBookNo(Map<String, Object> decisionBookNo) {
		DecisionBookNo = decisionBookNo;
	}
	public String getFlowFileId() {
		return flowFileId;
	}
	public void setFlowFileId(String flowFileId) {
		this.flowFileId = flowFileId;
	}

	/**预付卡充值返回预交易结果*/
	private Map<String, Object> prepaidResultMap;
	/**预付卡余额查询随机数*/
	private String random;
	
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	public Map<String, Object> getPrepaidResultMap() {
		return prepaidResultMap;
	}
	public void setPrepaidResultMap(Map<String, Object> prepaidResultMap) {
		this.prepaidResultMap = prepaidResultMap;
	}
	public String getFlowFileName() {
		return flowFileName;
	}
	public void setFlowFileName(String flowFileName) {
		this.flowFileName = flowFileName;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public List<Map<String, Object>> getCommonPaymentList() {
		return commonPaymentList;
	}
	public void setCommonPaymentList(List<Map<String, Object>> commonPaymentList) {
		this.commonPaymentList = commonPaymentList;
	}
	/** 获取预付卡充值提交交易返回数据 */
	public Map<String, Object> getMapPrepaidCardQuerySupportCardType() {
		return mapPrepaidCardQuerySupportCardType;
	}
	/** 设置预付卡充值提交交易返回数据 */
	public void setMapPrepaidCardQuerySupportCardType(Map<String, Object> mapPrepaidCardQuerySupportCardType) {
		this.mapPrepaidCardQuerySupportCardType = mapPrepaidCardQuerySupportCardType;
	}
	/** 获取预付卡充值预交易页面用户输入数据 */
	public Map<String, Object> getMapPrepaidCardRechPre() {
		return mapPrepaidCardRechPre;
	}
	/** 设置预付卡充值预交易页面用户输入数据 */
	public void setMapPrepaidCardRechPre(Map<String, Object> mapPrepaidCardRechPre) {
		this.mapPrepaidCardRechPre = mapPrepaidCardRechPre;
	}

	public Map<String, Object> getResultObj() {
		return resultObj;
	}
	public void setResultObj(Map<String, Object> resultObj) {
		this.resultObj = resultObj;
	}
	public String getDisplayNo() {
		return displayNo;
	}
	public void setDisplayNo(String displayNo) {
		this.displayNo = displayNo;
	}
	public String getCityDispName() {
		return cityDispName;
	}
	public void setCityDispName(String cityDispName) {
		this.cityDispName = cityDispName;
	}
	public List<Map<String, Object>> getQueryFlowFile() {
		return queryFlowFile;
	}
	public void setQueryFlowFile(List<Map<String, Object>> queryFlowFile) {
		this.queryFlowFile = queryFlowFile;
	}
	public List<Map<String, Object>> getAllPaymentList() {
		return allPaymentList;
	}
	public void setAllPaymentList(List<Map<String, Object>> allPaymentList) {
		this.allPaymentList = allPaymentList;
	}

	public static PlpsDataCenter getInstance() {
		if (instance == null) {
			instance = new PlpsDataCenter();
		}
		return instance;
	}

	/*public List<Map<String, Object>> getResultCardType() {
		return resultCardType;
	}*/

	/*public void setResultCardType(List<Map<String, Object>> resultCardType) {
		this.resultCardType = resultCardType;
	}*/

	public List<Map<String, Object>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, Object>> cityList) {
		this.cityList = cityList;
	}

	public String getPrvcDispName() {
		return prvcDispName;
	}

	public void setPrvcDispName(String prvcDispName) {
		this.prvcDispName = prvcDispName;
	}

	public String getPrvcShortName() {
		return prvcShortName;
	}

	public void setPrvcShortName(String prvcShortName) {
		this.prvcShortName = prvcShortName;
	}

	public Map<String, Object> getAnnuityListDetail() {
		return annuityListDetail;
	}


	public void setAnnuityListDetail(Map<String, Object> annuityListDetail) {
		this.annuityListDetail = annuityListDetail;
	}


//	public List<Map<String, String>> getPrvcListName() {
//		return prvcListName;
//	}
//
//	public void setPrvcListName(List<Map<String, String>> prvcListName) {
//		this.prvcListName = prvcListName;
//	}

	public String getSysDate() {
		return sysDate;
	}

	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}

	public List<Map<String, Object>> getLiveMenus() {
		return liveMenus;
	}

	public void setLiveMenus(List<Map<String, Object>> liveMenus) {
		this.liveMenus = liveMenus;
	}
	
	public Map<String, Object> getHistoryRecodDetail() {
		return historyRecodDetail;
	}

	public void setHistoryRecodDetail(Map<String, Object> historyRecodDetail) {
		this.historyRecodDetail = historyRecodDetail;
	}

	public List<Map<String, Object>> getPlanList() {
		return planList;
	}

	public void setPlanList(List<Map<String, Object>> planList) {
		this.planList = planList;
	}

	public List<Map<String, Object>> getAcctInfoList() {
		return acctInfoList;
	}

	public void setAcctInfoList(List<Map<String, Object>> acctInfoList) {
		this.acctInfoList = acctInfoList;
	}

	public List<Map<String, Object>> getAnnuityList() {
		return annuityList;
	}

	public void setAnnuityList(List<Map<String, Object>> annuityList) {
		this.annuityList = annuityList;
	}

	public Map<String, Object> getAnnuityUserInfo() {
		return annuityUserInfo;
	}

	public void setAnnuityUserInfo(Map<String, Object> annuityUserInfo) {
		this.annuityUserInfo = annuityUserInfo;
	}

	public List<Map<String, Object>> getSignList() {
		return signList;
	}

	public void setSignList(List<Map<String, Object>> signList) {
		this.signList = signList;
	}

	public List<Map<String, Object>> getAcctList() {
		return acctList;
	}

	public void setAcctList(List<Map<String, Object>> acctList) {
		this.acctList = acctList;
	}

	public List<Map<String, Object>> getServiceType() {
		return serviceType;
	}

	public void setServiceType(List<Map<String, Object>> serviceType) {
		this.serviceType = serviceType;
	}

	/** 养老金服务项目   */
	public static String[] annuity = new String[]{
		("养老金账户"),
		("业务信息查询"),
//		("信息变更申请"),
		("申请结果查询")
	};
	
	/** 签约缴费服务   */
	public static String[] payment = new String[]{
		("签约信息查询"),
		("签约")
	};
	/**预付卡充值项目*/
	public static String[] prepaid = new String[]{
		("预付卡余额查询"),
		("预付卡充值"),
		("充值结果查询")
	};
	

	/** 预付卡充值/查询 账户类型 */
	public static final List<String> plpsPrepaidCardAccountType = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("119");
			add("188");
			add("103");
			add("104");
		}
	};
	/**预付卡类型添加*/
	public static final List<String> plpsPrepaidCardType = new ArrayList<String>(){
		private static final  long serialVersionUID = 1L;{
			add("请选择");
			add( "中银通");
			add("中铁银通");
		}
	};
	/**预付卡类型number*/
	public static  final List<String> plpsPrepaidCardNo = new ArrayList<String>(){
		private static final  long serialVersionUID = 1L;
		{
			add("00000001");
			add("00000002");
		}
	};
	/**省地址*/
	public static final List<String> provList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("安徽");
			add("北京");
			add("重庆");
			add("福建");
			add("甘肃");
			add("广东");
			add("广西");
			add("贵州");
			add("海南");
			add("河北");
			add("河南");
			add("黑龙江");
			add("湖北");
			add("湖南");
			add("江苏");
			add("江西");
			add("吉林");
			add("辽宁");
			add("内蒙古");
			add("宁夏");
			add("青海");
			add("山东");
			add("陕西");
			add("上海");
			add("山西");
			add("四川");
			add("深圳");
			add("天津");
			add("新疆");
			add("西藏");
			add("云南");
			add("浙江");
		}
	};
	/**省地址*/
	public static final List<String> provListt = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("安徽");
			add("北京");
			add("重庆");
			add("福建");
			add("甘肃");
			add("广东");
			add("广西");
			add("贵州");
			add("海南");
			add("河北");
			add("河南");
			add("黑龙江");
			add("湖北");
			add("湖南");
			add("江苏");
			add("江西");
			add("吉林");
			add("辽宁");
			add("内蒙古");
			add("宁夏");
			add("青海");
			add("山东");
			add("陕西");
			add("上海");
			add("山西");
			add("四川");
			add("深圳");
			add("天津");
			add("新疆");
			add("西藏");
			add("云南");
			add("浙江");
		}
	};
	/** 省份和省简称 */
	public static final Map<String, String> mapCode_prov = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("安徽","AH");
			put("北京","BJ");
			put("重庆","CQ");
			put("福建", "FJ");
			put("广东", "GD");
			put("甘肃", "GS");
			put("广西", "GX");
			put("贵州", "GZ");
			put("河南", "HA");
			put("湖北", "HB");
			put("河北", "HE");
			put("海南", "HI");
			put("深圳", "SZ");
			put("黑龙江", "HL");
			put("湖南", "HN");
			put("吉林", "JL");
			put("江苏", "JS");
			put("江西", "JX");
			put("辽宁", "LN");
			put("浙江", "ZJ");
			put("内蒙古", "NM");
			put("宁夏", "NX");
			put("青海", "QH");
			put("四川", "SC");
			put("山东", "SD");
			put("上海", "SH");
			put("陕西", "SN");
			put("天津", "TJ");
			put("山西", "SX");
			put("云南", "YN");
			put("新疆", "XJ");
			put("西藏","XZ");
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
			put("40142", "北京");
			put("40202", "天津");
			put("40303", "上海");
			put("40005", "上海总部");
			put("40308", "上海自贸区");
			put("40600", "西藏");
			put("40740", "河北");
			put("41041", "山西");
			put("41405", "内蒙古");
			put("41785", "辽宁");
			put("42208", "吉林");
			put("42465", "黑龙江");
			put("43016", "陕西");
			put("43251", "甘肃");
			put("43347", "宁夏");
			put("43469", "青海");
			put("43600", "新疆");
			put("43810", "山东");
			put("44433", "江苏");
			put("44899", "安徽");
			put("45129", "浙江");
			put("45481", "福建");
			put("46243", "河南");
			put("46405", "湖北");
			put("46955", "湖南");
			put("47370", "江西");
			put("47504", "广东（不含深圳）");
			put("47669", "深圳");
			put("47806", "海南");
			put("48051", "广西");
			put("48631", "四川");
			put("48642", "重庆");
			put("48882", "贵州");
			put("49146", "云南");

		}
	};
	

	/** 缴费类型  **/
	public static final Map<String, String> paymentType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("0", "正常缴费");
			put("1", "特殊缴费");
		}
	};
	
	/** 支付方式  **/
	public static final Map<String, String> payType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("0", "分期支付");
			put("1", "定额支付");
			put("2", "一次性支付");
			put("3", "任意支付");
		}
	};
	
	/** 支付原因  **/
	public static final Map<String, String> paysRez = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("0", "退休");
			put("1", "出境定居");
			put("2", "死亡");
			put("3", "残疾");
			put("9", "其他");
		}
	};
	
	/** 转移方式  **/
	public static final Map<String, String> transferType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("0", "年金计划内转移");
			put("1", "跨年金计划转移");
			put("2", "系统外转移");
			put("3", "转保留计划");
			put("4", "转保留计划并留在原企业计划内");
			put("5", "子企业计划间转移");
			put("6", "账户平移");
		}
	};
	
	/** 转移原因  **/
	public static final Map<String, String> transferRez = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("0", "退休");
			put("1", "出境定居");
			put("2", "死亡");
			put("3", "残疾");
			put("4", "离职-合同期满");
			put("5", "离职-企业原因合同终止");
			put("6", "离职-职工原因合同终止");
			put("7", "离职-内部调动");
			put("8", "离职-解雇");
			put("9", "其它");
		}
	};
	
	/** 划转类型  **/
	public static final Map<String, String> transFerType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("0", "企业账户向企业账户");
			put("1", "企业账户向个人账户");
			put("2", "个人账户向个人账户");
			put("3", "个人账户向企业账户");
			put("4", "单账户向多账户");
			put("5", "多账户向单账户");
			put("6", "多账户向多账户");
		}
	};
	
	/** 划转方向  **/
	public static final Map<String, String> transferDirection = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("0", "转出");
			put("1", "转入");
		}
	};
	
	/** 计划状态   **/
	public static final Map<String, String> planStatus = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("0", "未生效");
			put("1", "正常");
			put("2", "支付中");
			put("3", "转出中");
			put("4", "支付终止");
			put("5", "冻结");
			put("6", "转出终止");
			put("7", "注销");
			put("9", "变更中");
		}
	};
	/**彩色的服务大类*/
	public static final Map<String, String> serviceTypes = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("32","0");//彩票投注服务
			put("45","1");//更多服务
			put("41","2");//工资单查询
			put("51","3");//公积金服务
			put("16","4");//公益捐款服务
			put("37","5");//行政事业缴费
			put("20","6");//交通出行购票
			put("49","7");//就医挂号服务
			put("1","8");//家居生活缴费
			put("12","9");//教育考试充值
			put("53","10");//社会保障服务
			put("8","11");//通讯服务缴费
			put("26","12");//娱乐休闲购物
		}
	};
	/**彩色的服务小类*/
	public static final Map<String, String> smallServictTypes = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("22","0");//ETC卡
			put("28","1");//Q币充值
			put("30","2");//报刊订阅
			put("常用服务项目","3");//常用服务项目
			put("1355","4");//超值有礼
			put("455","5");//代收货款
			put("46","6");//党费
			put("3","7");//电费
			put("755","8");//电影卡充值
			put("电影票","9");//电影票
			put("256","10");//飞机票
			put("33","11");//福彩
			put("39","12");//工商管理
			put("155","13");//工商注册
			put("955","14");//公租房租金
			put("10","15");//固话
			put("1255","16");//广东活利宝
			put("40","17");//国税地税
			put("25","18");//火车票
			put("23","19");//加油卡
			put("38","20");//交通罚款
			put("21","21");//交通卡
			put("156","22");//交易所开户
			put("29","23");//景点门票
			put("31","24");//酒店团购
			put("15","25");//考试费
			put("11","26");//宽带使用
			put("157","27");//礼品积分兑换
			put("24","28");//汽车票
			put("签约代缴服务","29");//签约代缴服务
			put("5","30");//取暖费
			put("4","31");//燃气费
			put("54","32");//社会保障服务
			put("355","33");//实物贵金属产品代销
			put("2","34");//水费
			put("34","35");//体彩
			put("47","36");//团费
			put("655","37");//网络快捷贷
			put("255","38");//物管通
			put("6","39");//物业费
			put("13","40");//校园卡
			put("14","41");//学费
			put("48","42");//烟草购烟款
			put("养老金服务","43");
			put("9","44");//移动通讯
			put("27","45");//游戏点卡
			put("7","46");//有线电视费
			put("预付卡充值","47");
			put("中银易房通","48");
			put("158","49");//中银移动市民卡
			put("1455","50");//住房公积金
			put("55","51");//综合缴费
			put("1456","52");//北京青少年发展基金会
			put("18","53");//慈善总会
			put("1655","54");//非税缴费
			put("42","55");//工资单查询
			put("52","56");//公积金服务
			put("17","57");//红十字会
			put("555","58");//教育基金会
			put("1155","59");//上海市慈善基金会
			put("1555","60");//线上贷款
			put("1055","61");//小贷掌上通
			put("19","62");//壹基金
			put("855","63");//志愿服务基金会
			put("158","64");//中银移动市民卡
			put("跨省异地交通罚款", "65");
		}
	};
	/**灰色的服务大类*/
	public static final Map<String, String> serviceTypesGray = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
		put("32","0");//彩票投注服务
		put("45", "1");//更多服务
		put("41", "2");//工资单查询
		put("51", "3");//公积金服务
		put("16", "4");//公益捐款服务
		put("37", "5");//行政事业缴费
		put("1", "6");//家居生活缴费
		put("20","7");//交通出行购票
		put("12", "8");//教育考试充值
		put("49", "9");//就医挂号服务	
		put("53", "10");//社会保障服务
		put("8", "11");//通讯服务缴费
		put("26", "12");//娱乐休闲购物
		}
	};
	/** 灰色服务小类 */
	public static final Map<String, String> smallServiceTypesGray = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("22", "0");//ETC卡
			put("28", "1");//Q币充值
			put("30", "2");//报刊订阅
			put("常用服务项目", "3");
			put("1355", "4");//超值有礼
			put("455", "5");//代收货款
			put("46", "6");//党费
			put("3", "7");//电费
			put("755", "8");//电影卡充值
			put("电影票", "9");//电影票
			put("256", "10");//飞机票
			put("33", "11");//福彩
			put("39", "12");//工商管理
			put("155", "13");//工商注册
			put("955", "14");//公租房租金
			put("10", "15");//固话
			put("1255", "16");//广东活利宝
			put("40", "17");//国税地税
			put("25", "18");//火车票
			put("23", "19");//加油卡
			put("38", "20");//交通罚款
			put("21", "21");//交通卡
			put("156", "22");//交易所开户
			put("29", "23");//景点门票
			put("31", "24");//酒店团购
			put("15", "25");//考试费
			put("11", "26");//宽带使用
			put("157", "27");//礼品积分兑换
			put("24", "28");//汽车票
			put("签约代缴服务", "29");//签约代缴服务
			put("5", "30");//取暖费
			put("4", "31");//燃气费
			put("54", "32");//社会保障服务
			put("355", "33");//实物贵金属产品代销
			put("2", "34");//水费
			put("34", "35");//体彩
			put("47", "36");//团费
			put("655", "37");//网络快捷贷
			put("255", "38");//物管通
			put("6", "39");//物业费
			put("13", "40");//校园卡
			put("14", "41");//学费
			put("48", "42");//烟草购烟款
			put("养老金服务", "43");//养老金服务
			put("9", "44");//移动通讯
			put("27", "45");//游戏点卡
			put("7", "46");//有线电视费
			put("预付卡充值", "47");
			put("中银易房通", "48");//中银易房通
			put("158", "49");//中银移动市民卡
			put("1455", "50");//住房公积金
			put("55", "51");//综合缴费
			put("1456","52");//北京青少年发展基金会
			put("18","53");//慈善总会
			put("1655","54");//非税缴费
			put("42","55");//工资单查询
			put("52","56");//公积金服务
			put("17","57");//红十字会
			put("555","58");//教育基金会
			put("1155","59");//上海市慈善基金会
			put("1555","60");//线上贷款
			put("1055","61");//小贷掌上通
			put("19","62");//壹基金
			put("855","63");//志愿服务基金会
			put("158","64");//中银移动市民卡
			put("跨省异地交通违法罚款", "65");
		}
	};
	/** 计划类型  **/
	public static final Map<String, String> planType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("0", "雇员计划");
			put("1", "保留计划");
		}
	};
	/**交通罚款 交易渠道*/
	public static final Map<String, Object> channelType = new HashMap<String, Object>(){
		private static final long serialVersionUID = 1L;
		{
			put("00", "柜面");
			put("01", "Internet渠道");
			put("02", "银企对接/网上商城");
			put("03", "短信");
			put("04", "IVR");
			put("05", "CSR");
			put("06", "FAX");
			put("07", "自助终端");
			put("08", "第三方渠道");
			put("09", "ATM");
			put("10", "手机银行");
			put("11", "家居银行");
			put("12", "POS");
			put("13", "OTC");
			put("14", "客户端");
			put("15", "银行卡柜台前端（GCS）");
			put("99", "其他");
		}
	};
	
	/** 养老金查询类型   **/
	public static final List<String> queryType = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("缴费");
			add("投资组合单位净值历史");
			add("支付");
			add("转移");
			add("个人投资转换");
			add("账户划转");
			add("公共账户权益分配");
//			add("业务申报");
		}
	};
	/**直辖市*/
	public static final List<String> municiplGovernment = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("北京");
			add("天津");
			add("上海");
			add("重庆");
			add("深圳");
		}
	};
	
	/** 签约类型  **/
	public static final Map<String, String> signType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("0", "已签约");
			put("1", "短信邀缴");
		}
	};
	
	/** 签约渠道  **/
	public static final Map<String, String> signChannel = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("01", "柜台");
			put("02", "自助终端");
			put("03", "网银");
			put("04", "手机银行");
			put("05", "家居银行");
		}
	};
	
	/** 交易渠道 */
	public static final Map<String, String> channel = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("01", "柜台");
			put("02", "自助");
			put("03", "网银");
			put("04", "手机银行");
			put("05", "家居银行");
		}
	};
	/** 账户类型 */
	public static List<String> accountTypeList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("101");//普通活期
			add("119");//长城借记卡
			add("188");//活期一本通

		};
	};
	
	/**
	 * 清除缓存
	 */
	public void clearAllData() {
		if (!StringUtil.isNullOrEmpty(liveMenus)) {
			liveMenus.clear();
		}
		if (!StringUtil.isNullOrEmpty(historyRecodDetail)) {
			historyRecodDetail.clear();
		}
		if (!StringUtil.isNullOrEmpty(planList)) {
			planList.clear();
		}
		if (!StringUtil.isNullOrEmpty(acctInfoList)) {
			acctInfoList.clear();
		}
		if (!StringUtil.isNullOrEmpty(annuityUserInfo)) {
			annuityUserInfo.clear();
		}
		if (!StringUtil.isNullOrEmpty(signList)) {
			signList.clear();
		}
		if (!StringUtil.isNullOrEmpty(acctList)) {
			acctList.clear();
		}
		if (!StringUtil.isNullOrEmpty(serviceType)) {
			serviceType.clear();
		}
		if (!StringUtil.isNullOrEmpty(annuityList)) {
			annuityList.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapPrepaidCardRechPre)) {
			mapPrepaidCardRechPre.clear();
		}
		if(!StringUtil.isNullOrEmpty(annuityListDetail)){
			annuityListDetail.clear();
		}
		if(!StringUtil.isNullOrEmpty(cityList)){
			cityList.clear();
		}
		/*if(!StringUtil.isNullOrEmpty(resultCardType)){
			resultCardType.clear();
		}*/
		if(!StringUtil.isNullOrEmpty(mapPrepaidCardRechPre)){
			mapPrepaidCardRechPre.clear();
		}
		if(!StringUtil.isNullOrEmpty(mapPrepaidCardQuerySupportCardType)){
			mapPrepaidCardQuerySupportCardType.clear();
		}
		if(!StringUtil.isNullOrEmpty(allPaymentList)){
			allPaymentList.clear();
		}
		if(!StringUtil.isNullOrEmpty(commonPaymentList)){
			commonPaymentList.clear();
		}
		if(!StringUtil.isNullOrEmpty(queryFlowFile)){
			queryFlowFile.clear();
		}
		if(!StringUtil.isNullOrEmpty(resultObj)){
			resultObj.clear();
		}
		if(!StringUtil.isNullOrEmpty(prepaidResultMap)){
			prepaidResultMap.clear();
		}
		if(!StringUtil.isNull(prvcShortName)){
			prvcShortName = null;
		}
		if(!StringUtil.isNull(prvcDispName)){
			prvcDispName=null;
		}
		if(!StringUtil.isNull(cityDispName)){
			cityDispName=null;
		}
		if(!StringUtil.isNull(cityDispName)){
			cityDispName= null;
		}
		if(!StringUtil.isNull(displayNo)){
			displayNo=null;
		}
		if(!StringUtil.isNull(itemId)){
			itemId=null;
		}
		if(!StringUtil.isNull(catName)){
			catName=null;
		}
		if(!StringUtil.isNull(menuName)){
			menuName=null;
		}
		if(!StringUtil.isNull(flowFileName)){
			flowFileName=null;
		}
		if(!StringUtil.isNull(random)){
			random=null;
		}
	}
}
