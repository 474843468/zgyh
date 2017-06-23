package com.chinamworld.bocmbci.biz.safety;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.utils.StringUtil;

public class SafetyDataCenter {

	public static SafetyDataCenter instance;
	/** 系统时间，无时分秒 */
	private String sysTime;
	/** 完全的系统时间，带时分秒 */
	private String sysTimeFull;

	/** 产品详情 */
	private Map<String, Object> productInfoMap;
	/** 银行卡列表 */
	private List<Map<String, Object>> acctList;
	/** 地址省份列表  */
	private List<Map<String, Object>> proviceList;
	private List<Map<String, Object>> proviceListHS;
	/** 第二页地址数据   */
	private List<Map<String, Object>> secondCityList;
	private List<Map<String, Object>> secondCountryList;
	/** 第三页地址数据   */
	private List<Map<String, Object>> thirdCityList;
	private List<Map<String, Object>> thirdCountryList;
	private List<Map<String, Object>> thirdCityListHS;
	private List<Map<String, Object>> thirdCountryListHS;
	/** 迁往地区  */
	private List<Map<String, String>> countyList;
	/** 保费试算返回   */
	private Map<String, Object> countMap;
	/** 修改标志   */
	private boolean isChange;
	/** 随机数*/
	private String randomNumer = "";
	/** 持有保单产品信息*/
	private Map<String, Object> holdProDetail;
	/** 续保查询*/
	private Map<String, Object> insuranceContinueQuery;
	/** 新单投保预交易*/
	private Map<String, Object> insuranceNewVerify;
	/** 提交交易*/
	private Map<String, Object> insuranceNewSubmit;
	
	// ///////////以下为车险所调接口返回的数据.命名规则：数据类型 + 调用的接口名称去掉Psn字样//////////////
	/** 车辆补充信息查询返回 */
	private Map<String, Object> mapAutoInsuranceQueryAutoDetail;
	/** 车型查询返回 */
	private List<Map<String, Object>> listAutoInsuranceQueryAutoType;
	/** 开通省份代码返回数据 */
	private List<String> listAutoInsuranceQueryAllowArea;
	/** 交强险查询返回数据 */
	private Map<String, Object> mapAutoInsuranceQueryCompulsory;
	/** 商业险套餐查询返回数据 */
	private Map<String, Object> mapAutoInsuranceQueryCommercial;
	/** 保費計算返回數據 */
	private Map<String, Object> mapAutoInsuranceCalculation;
	/** 车险中用户输入的数据 */
	private Map<String, Object> mapCarSafetyUserInput;
	/** 车险投保单生成返回数据 */
	private Map<String, Object> mapAutoInsuranceCreatPolicy;
	/** 车险投保缴费提交交易返回数据 */
	private Map<String, Object> mapAutoInsurancePaySubmit;
	/** 查询客户资料数据 */
	private Map<String, Object> mapSVRPsnInfoQuery;
	/** 车险暂存单保存/更新接口参数列表 */
	private Map<String, Object> mapSaveParams;
	/** 是否点击过车险暂存保单按钮 false-未点击 true-已点击 */
	public boolean isSaved = false;
	/** 是否是从暂存保单模块跳转到投保流程 */
	public boolean isSaveToThere = false;
	/** 是否是续保 */
	public boolean isHoldToThere = false;
	/** 之前是否返回过 */
	public boolean isReturn = false;
	/** 车险暂存单ID */
	private String policyId;
	/** 车险暂存单详情 */
	private Map<String, Object> mapAutoInsurPolicyDetailQry;
	/** 车险投保预交易返回数据 */
	private Map<String, Object> mapAutoInsurancePayVerify;
	/** 寿险产品列表 */
	private List<Map<String, Object>> listLifeInsuranceProductQuery;
	/** 缴费种类信息查询 */
	private List<Map<String, Object>> listInsurancePayTypeInfoQuery;
	/** 寿险风险评估信息查询返回数据 */
	private Map<String, Object> mapInsuranceRiskEvaluationQuery;
	/** 用户输入数据 */
	private Map<String , Object> mapUserInput;
	/** 产品栏位控制信息查询 */
	private Map<String, Object> mapInsuranceFieldControlInfoQuery;
	/** 寿险字段代码为A的控制信息 */
	private List<String[]> controlInfoA;
	/** 寿险字段代码为B的控制信息 */
	private List<String[]> controlInfoB;
	/** 寿险字段代码为C的控制信息 */
	private List<String[]> controlInfoC;
	/** 寿险字段代码为D的控制信息 */
	private List<String[]> controlInfoD;
	/** 寿险字段代码为E的控制信息 */
	private List<String[]> controlInfoE;
	/** 寿险保费试算返回数据 */
	private Map<String, Object> mapLifeInsuranceCalculation;
	/** 寿险预交易返回数据 */
	private Map<String, Object> mapLifeInsurancePayVerify;
	/** 寿险提交交易返回数据 */
	private Map<String, Object> mapLifeInsurancePaySubmit;

	/** 获取寿险提交交易返回数据 */
	public Map<String, Object> getMapLifeInsurancePaySubmit() {
		return mapLifeInsurancePaySubmit;
	}

	/** 设置寿险提交交易返回数据 */
	public void setMapLifeInsurancePaySubmit(Map<String, Object> mapLifeInsurancePaySubmit) {
		this.mapLifeInsurancePaySubmit = mapLifeInsurancePaySubmit;
	}

	/** 获取寿险预交易返回数据 */
	public Map<String, Object> getMapLifeInsurancePayVerify() {
		return mapLifeInsurancePayVerify;
	}

	/** 设置寿险预交易返回数据 */
	public void setMapLifeInsurancePayVerify(Map<String, Object> mapLifeInsurancePayVerify) {
		this.mapLifeInsurancePayVerify = mapLifeInsurancePayVerify;
	}

	/** 获取寿险保费试算返回数据 */
	public Map<String, Object> getMapLifeInsuranceCalculation() {
		return mapLifeInsuranceCalculation;
	}

	/** 设置寿险保费试算返回数据 */
	public void setMapLifeInsuranceCalculation(Map<String, Object> mapLifeInsuranceCalculation) {
		this.mapLifeInsuranceCalculation = mapLifeInsuranceCalculation;
	}

	/** 获取产品栏位控制信息查询 */
	public Map<String, Object> getMapInsuranceFieldControlInfoQuery() {
		return mapInsuranceFieldControlInfoQuery;
	}

	/** 设置产品栏位控制信息查询 */
	public void setMapInsuranceFieldControlInfoQuery(Map<String, Object> mapInsuranceFieldControlInfoQuery) {
		this.mapInsuranceFieldControlInfoQuery = mapInsuranceFieldControlInfoQuery;
	}

	public List<String[]> getControlInfoA() {
		return controlInfoA;
	}

	public void setControlInfoA(List<String[]> a) {
		this.controlInfoA = a;
	}

	public List<String[]> getControlInfoB() {
		return controlInfoB;
	}

	public void setControlInfoB(List<String[]> b) {
		this.controlInfoB = b;
	}

	public List<String[]> getControlInfoC() {
		return controlInfoC;
	}

	public void setControlInfoC(List<String[]> c) {
		this.controlInfoC = c;
	}

	public List<String[]> getControlInfoD() {
		return controlInfoD;
	}

	public void setControlInfoD(List<String[]> d) {
		this.controlInfoD = d;
	}

	public List<String[]> getControlInfoE() {
		return controlInfoE;
	}

	public void setControlInfoE(List<String[]> e) {
		this.controlInfoE = e;
	}

	/** 获取缴费种类信息查询 */
	public List<Map<String, Object>> getListInsurancePayTypeInfoQuery() {
		return listInsurancePayTypeInfoQuery;
	}

	/** 设置缴费种类信息查询 */
	public void setListInsurancePayTypeInfoQuery(List<Map<String, Object>> listInsurancePayTypeInfoQuery) {
		this.listInsurancePayTypeInfoQuery = listInsurancePayTypeInfoQuery;
	}

	/** 获取寿险产品列表 */
	public List<Map<String, Object>> getListLifeInsuranceProductQuery() {
		return listLifeInsuranceProductQuery;
	}

	/** 设置寿险产品列表 */
	public void setListLifeInsuranceProductQuery(List<Map<String, Object>> listLifeInsuranceProductQuery) {
		this.listLifeInsuranceProductQuery = listLifeInsuranceProductQuery;
	}

	/** 获取用户输入数据 */
	public Map<String, Object> getMapUserInput() {
		return mapUserInput;
	}

	/** 设置用户输入数据 */
	public void setMapUserInput(Map<String, Object> mapUserInput) {
		this.mapUserInput = mapUserInput;
	}

	/** 获取寿险风险评估信息查询返回数据 */
	public Map<String, Object> getMapInsuranceRiskEvaluationQuery() {
		return mapInsuranceRiskEvaluationQuery;
	}

	/** 设置寿险风险评估信息查询返回数据 */
	public void setMapInsuranceRiskEvaluationQuery(Map<String, Object> mapInsuranceRiskEvaluationQuery) {
		this.mapInsuranceRiskEvaluationQuery = mapInsuranceRiskEvaluationQuery;
	}

	/** 获取车险投保预交易返回数据 */
	public Map<String, Object> getMapAutoInsurancePayVerify() {
		return mapAutoInsurancePayVerify;
	}

	/** 设置车险投保预交易返回数据 */
	public void setMapAutoInsurancePayVerify(
			Map<String, Object> mapAutoInsurancePayVerify) {
		this.mapAutoInsurancePayVerify = mapAutoInsurancePayVerify;
	}

	/** 获取车险暂存单详情数据 */
	public Map<String, Object> getMapAutoInsurPolicyDetailQry() {
		return mapAutoInsurPolicyDetailQry;
	}

	/** 设置车险暂存单详情数据 */
	public void setMapAutoInsurPolicyDetailQry(
			Map<String, Object> mapAutoInsurPolicyDetailQry) {
		this.mapAutoInsurPolicyDetailQry = mapAutoInsurPolicyDetailQry;
	}

	/** 获取完全的系统时间 */
	public String getSysTimeFull() {
		return sysTimeFull;
	}

	/** 设置完全的系统时间 */
	public void setSysTimeFull(String sysTimeFull) {
		this.sysTimeFull = sysTimeFull;
	}
	
	/** 获取车险暂存单ID */
	public String getPolicyId() {
		return policyId;
	}

	/** 设置车险暂存单ID */
	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	/** 获取车险暂存单保存/更新接口参数列表 */
	public Map<String, Object> getMapSaveParams() {
		return mapSaveParams;
	}
	
	/** 设置车险暂存单保存/更新接口参数列表 */
	public void setMapSaveParams(Map<String, Object> mapSaveParams) {
		this.mapSaveParams = mapSaveParams;
	}
	
	/** 获取查询客户资料返回数据 */
	public Map<String, Object> getMapSVRPsnInfoQuery() {
		return mapSVRPsnInfoQuery;
	}
	/** 设置查询客户资料数据 */
	public void setMapSVRPsnInfoQuery(Map<String, Object> mapSVRPsnInfoQuery) {
		this.mapSVRPsnInfoQuery = mapSVRPsnInfoQuery;
	}
	
	/** 获取车险投保缴费提交交易返回数据 */
	public Map<String, Object> getMapAutoInsurancePaySubmit() {
		return mapAutoInsurancePaySubmit;
	}
	/** 设置车险投保缴费提交交易返回数据 */
	public void setMapAutoInsurancePaySubmit(Map<String, Object> mapAutoInsurancePaySubmit) {
		this.mapAutoInsurancePaySubmit = mapAutoInsurancePaySubmit;
	}
	
	/** 获取车险投保单生成返回数据 */
	public Map<String, Object> getMapAutoInsuranceCreatPolicy() {
		return mapAutoInsuranceCreatPolicy;
	}
	/** 设置车险投保单生成返回数据*/
	public void setMapAutoInsuranceCreatPolicy(Map<String, Object> mapAutoInsuranceCreatPolicy) {
		this.mapAutoInsuranceCreatPolicy = mapAutoInsuranceCreatPolicy;
	}
	
	/** 获取车险中用户输入的数据 */
	public Map<String, Object> getMapCarSafetyUserInput() {
		return mapCarSafetyUserInput;
	}
	/** 设置车险中用户输入的数据 */
	public void setMapCarSafetyUserInput(Map<String, Object> mapCarSafetyUserInput) {
		this.mapCarSafetyUserInput = mapCarSafetyUserInput;
	}
	
	/** 獲取保費計算返回數據 */
	public Map<String, Object> getMapAutoInsuranceCalculation() {
		return mapAutoInsuranceCalculation;
	}
	/** 設置保費計算返回數據 */
	public void setMapAutoInsuranceCalculation(Map<String, Object> mapAutoInsuranceCalculation) {
		this.mapAutoInsuranceCalculation = mapAutoInsuranceCalculation;
	}
	
	/** 获取商业险套餐查询返回数据 */
	public Map<String, Object> getMapAutoInsuranceQueryCommercial() {
		return mapAutoInsuranceQueryCommercial;
	}
	/** 设置商业险套餐查询返回数据 */
	public void setMapAutoInsuranceQueryCommercial(
			Map<String, Object> mapAutoInsuranceQueryCommercial) {
		this.mapAutoInsuranceQueryCommercial = mapAutoInsuranceQueryCommercial;
	}
	
	/** 获取交强险查询返回数据 */
	public Map<String, Object> getMapAutoInsuranceQueryCompulsory() {
		return mapAutoInsuranceQueryCompulsory;
	}
	/** 设置交强险查询返回数据 */
	public void setMapAutoInsuranceQueryCompulsory(
			Map<String, Object> mapAutoInsuranceQueryCompulsory) {
		this.mapAutoInsuranceQueryCompulsory = mapAutoInsuranceQueryCompulsory;
	}
	
	/** 获取开通车险省份代码 */
	public List<String> getListAutoInsuranceQueryAllowArea() {
		return listAutoInsuranceQueryAllowArea;
	}
	/** 设置开通车险省份代码 */
	public void setListAutoInsuranceQueryAllowArea(
			List<String> listAutoInsuranceQueryAllowArea) {
		this.listAutoInsuranceQueryAllowArea = listAutoInsuranceQueryAllowArea;
	}
	/** 获取车型查询返回的数据 */
	public List<Map<String, Object>> getListAutoInsuranceQueryAutoType() {
		return listAutoInsuranceQueryAutoType;
	}
	/** 设置车型查询返回的数据 */
	public void setListAutoInsuranceQueryAutoType(List<Map<String, Object>> listAutoInsuranceQueryAutoType) {
		this.listAutoInsuranceQueryAutoType = listAutoInsuranceQueryAutoType;
	}
	/** 获取车辆补充信息查询返回数据 */
	public Map<String, Object> getMapAutoInsuranceQueryAutoDetail() {
		return mapAutoInsuranceQueryAutoDetail;
	}
	/** 设置车辆补充信息查询返回数据 */
	public void setMapAutoInsuranceQueryAutoDetail(Map<String, Object> mapAutoInsuranceQueryAutoDetail) {
		this.mapAutoInsuranceQueryAutoDetail = mapAutoInsuranceQueryAutoDetail;
	}

	public static SafetyDataCenter getInstance() {
		if (instance == null) {
			instance = new SafetyDataCenter();
		}
		return instance;
	}

	public Map<String, Object> getHoldProDetail() {
		return holdProDetail;
	}

	public void setHoldProDetail(Map<String, Object> holdProDetail) {
		this.holdProDetail = holdProDetail;
	}

	public Map<String, Object> getInsuranceNewSubmit() {
		return insuranceNewSubmit;
	}

	public void setInsuranceNewSubmit(Map<String, Object> insuranceNewSubmit) {
		this.insuranceNewSubmit = insuranceNewSubmit;
	}

	public Map<String, Object> getInsuranceNewVerify() {
		return insuranceNewVerify;
	}

	public void setInsuranceNewVerify(Map<String, Object> insuranceNewVerify) {
		this.insuranceNewVerify = insuranceNewVerify;
	}

	public Map<String, Object> getInsuranceContinueQuery() {
		return insuranceContinueQuery;
	}

	public void setInsuranceContinueQuery(Map<String, Object> insuranceContinueQuery) {
		this.insuranceContinueQuery = insuranceContinueQuery;
	}

	public String getRandomNumer() {
		return randomNumer;
	}

	public void setRandomNumer(String randomNumer) {
		this.randomNumer = randomNumer;
	}

	public String getSysTime() {
		return sysTime;
	}

	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	public Map<String, Object> getProductInfoMap() {
		return productInfoMap;
	}

	public void setProductInfoMap(Map<String, Object> productInfoMap) {
		this.productInfoMap = productInfoMap;
	}

	public List<Map<String, Object>> getAcctList() {
		return acctList;
	}

	public void setAcctList(List<Map<String, Object>> acctList) {
		this.acctList = acctList;
	}

	public List<Map<String, Object>> getProviceList() {
		return proviceList;
	}

	public void setProviceList(List<Map<String, Object>> proviceList) {
		this.proviceList = proviceList;
	}

	public List<Map<String, Object>> getProviceListHS() {
		return proviceListHS;
	}

	public void setProviceListHS(List<Map<String, Object>> proviceListHS) {
		this.proviceListHS = proviceListHS;
	}

	public List<Map<String, Object>> getSecondCityList() {
		return secondCityList;
	}

	public void setSecondCityList(List<Map<String, Object>> secondCityList) {
		this.secondCityList = secondCityList;
	}

	public List<Map<String, Object>> getSecondCountryList() {
		return secondCountryList;
	}

	public void setSecondCountryList(List<Map<String, Object>> secondCountryList) {
		this.secondCountryList = secondCountryList;
	}

	public List<Map<String, Object>> getThirdCityList() {
		return thirdCityList;
	}

	public void setThirdCityList(List<Map<String, Object>> thirdCityList) {
		this.thirdCityList = thirdCityList;
	}

	public List<Map<String, Object>> getThirdCountryList() {
		return thirdCountryList;
	}

	public void setThirdCountryList(List<Map<String, Object>> thirdCountryList) {
		this.thirdCountryList = thirdCountryList;
	}

	public List<Map<String, Object>> getThirdCityListHS() {
		return thirdCityListHS;
	}

	public void setThirdCityListHS(List<Map<String, Object>> thirdCityListHS) {
		this.thirdCityListHS = thirdCityListHS;
	}

	public List<Map<String, Object>> getThirdCountryListHS() {
		return thirdCountryListHS;
	}

	public void setThirdCountryListHS(List<Map<String, Object>> thirdCountryListHS) {
		this.thirdCountryListHS = thirdCountryListHS;
	}

	public List<Map<String, String>> getCountyList() {
		return countyList;
	}

	public void setCountyList(List<Map<String, String>> countyList) {
		this.countyList = countyList;
	}

	public Map<String, Object> getCountMap() {
		return countMap;
	}

	public void setCountMap(Map<String, Object> countMap) {
		this.countMap = countMap;
	}

	public boolean isChange() {
		return isChange;
	}

	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}

	/** 产品类型 **/
	public static final Map<String, String> insuranceType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("1", "分红型");
			put("2", "万能型");
			put("3", "投连型");
			put("4", "家财险");
			put("5", "意外险");
			put("6", "保险卡");
			put("7", "双享贷");
			put("8", "车险");
		}
	};

	/** 渠道标识 **/
	public static final Map<String, String> channelFlag = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("0", "其它");
			put("1", "柜面");
			put("2", "网银");
			put("3", "手机银行");
			put("4", "自助终端");
			put("5", "IVR（电话银行）");
			put("6", "CSR（人工坐席）");
		}
	};

	/** 性别 **/
	public static final Map<String, String> gender = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("0", "女");
			put("1", "男");
		}
	};

	/** 证件类型-保险 **/
	public static final Map<String, String> credType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("01", "居民身份证");
			put("02", "临时居民身份证");
			put("03", "护照");
			put("04", "户口簿");
			put("05", "军人身份证");
			put("06", "武装警察身份证");
			put("07", "港澳台居民来往内地通行证");
			put("08", "外交人员身份证");
			put("09", "外国人居留许可证");
			put("10", "边民出入境通行证");
			put("11", "其他证件");
			put("47", "港澳居民来往内地通行证（香港）");
			put("48", "港澳居民来往内地通行证（澳门）");
			put("49", "台湾居民来往大陆通行证");
		}
	};
	
	/** 登录证件类型与保险上送证件类型代码映射 */
	public static final Map<String, String> IDENTITYTYPE_credType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("1", "01");
			put("2", "02");
			put("3", "04");
			put("4", "05");
			put("5", "06");
			put("6", "07");
			put("7", "07");
			put("8", "03");
			put("9", "11");
			put("10", "07");
			put("11", "08");
			put("12", "09");
			put("13", "10");
			put("47", "47");
			put("48", "48");
			put("49", "49");
		}	
	};

	/** 证件类型-登录 */
	public static Map<String, String> IDENTITYTYPE = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("1", "居民身份证");
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
			put("47", "港澳居民来往内地通行证");
			put("48", "港澳居民来往内地通行证");
			put("49", "台湾居民来往大陆通行证");
		}
	};
	
	/** 证件类型-代码列表 */
	public static final List<String> rqcredType = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		
		{
			add("01");
			add("02");
			add("03");
			add("04");
			add("05");
			add("06");
			add("07");
			add("08");
			add("09");
			add("10");
			add("11");
			add("47");
			add("48");
			add("49");
		}
	};

	/** 居民身份证 */
	public static final List<String> credTypeList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("居民身份证");
			add("临时居民身份证");
			add("护照");
			add("户口簿");
			add("军人身份证");
			add("武装警察身份证");
			add("港澳台居民来往内地通行证");
			add("外交人员身份证");
			add("外国人居留许可证");
			add("边民出入境通行证");
			add("其他证件");
			add("港澳居民来往内地通行证（香港）");
			add("港澳居民来往内地通行证（澳门）");
			add("台湾居民来往大陆通行证");
		}
	};

	/** 交易类型 **/
	public static final Map<String, String> transType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("01", "缴费出单");
			put("02", "续期缴费");
			put("03", "当日契撤");
			put("04", "出单补录");
			put("05", "非实时出单");
			put("06", "犹豫期内退保");
			put("07", "犹豫期外退保");
			put("08", "售卡");
			put("09", "售卡撤销");
			put("10", "双享贷暂保出单");
			put("11", "双享贷暂保退保");
			put("12", "双享贷申请保单");
			put("13", "双享贷结清退保");
			put("14", "双享贷展期出单");
			put("15", "双享贷逾期申报");
			put("16", "双享贷当日契撤");
			put("20", "车险自助缴费");
			put("21", "出国金融缴费");
			put("22", "车险网银缴费");
			put("23", "满期给付");
			put("24", "转保");
		}
	};
	
	/** 婚姻状态 */
	public static final List<String> lifeMargiList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		
		{
			add("未婚");
			add("已婚");
			add("离异");
			add("丧偶");
		}
	};

	public static final List<String> lifeMargiListrq = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		
		{
			add("0");
			add("1");
			add("6");
			add("5");
		}
	};
	
	/** 婚姻状态 */
	public static final List<String> margiList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
//			add("请选择");
			add("未婚");
			add("已婚");
			add("初婚");
			add("再婚");
			add("复婚");
			add("丧偶");
			add("离婚");
			add("其他");
		}
	};
	
	public static final List<String> margiListrq = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		
		{
//			add("");
			add("0");
			add("1");
			add("2");
			add("3");
			add("4");
			add("5");
			add("6");
			add("7");
		}
	};
	
	/** 居民类型中文 */
	public static final List<String> residentsType_CN = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("城镇");
			add("农村");
		}
	};

	/** 居民类型代码 */
	public static final List<String> residentsType_CODE = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("0");
			add("1");
		}
	};

	/** 关系 */
	public static final List<String> relation = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("本人");
			add("丈夫");
			add("妻子");
			add("父亲");
			add("母亲");
			add("儿子");
			add("女儿");
			add("祖父");
			add("祖母");
			add("孙子");
			add("孙女");
			add("外祖父");
			add("外祖母");
			add("外孙");
			add("外孙女");
			add("哥哥");
			add("姐姐");
			add("弟弟");
			add("妹妹");
			add("其他");
		}
	};
	
	public static final List<String> relation_M = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		
		{
			add("本人");
			add("丈夫");
			add("父亲");
			add("儿子");
			add("祖父");
			add("孙子");
			add("外祖父");
			add("外孙");
			add("哥哥");
			add("弟弟");
			add("其他");
		}
	};
	
	public static final List<String> relation_W = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		
		{
			add("本人");
			add("妻子");
			add("母亲");
			add("女儿");
			add("祖母");
			add("孙女");
			add("外祖母");
			add("外孙女");
			add("姐姐");
			add("妹妹");
			add("其他");
		}
	};
	
	public static final List<String> relationrq = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		
		{
			add("01");
			add("02");
			add("03");
			add("04");
			add("05");
			add("06");
			add("07");
			add("08");
			add("09");
			add("10");
			add("11");
			add("12");
			add("13");
			add("14");
			add("15");
			add("16");
			add("17");
			add("18");
			add("19");
			add("99");
		}
	};
	
	public static final List<String> relationrq_M = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		
		{
			add("01");
			add("02");
			add("04");
			add("06");
			add("08");
			add("10");
			add("12");
			add("14");
			add("16");
			add("18");
			add("99");
		}
	};
	public static final List<String> relationrq_W = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		
		{
			add("01");
			add("03");
			add("05");
			add("07");
			add("09");
			add("11");
			add("13");
			add("15");
			add("17");
			add("19");
			add("99");
		}
	};

	/** 账户类型 */
	public static List<String> accountTypeList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("101");//普通活期
			add("103");// 中银信用卡-P503批次增加对信用卡支持
			add("104");// 准贷记卡-即QCC长城信用卡-P503批次增加对信用卡支持
			add("108");// 贷记虚拟卡-P503批次增加对信用卡支持
			add("109");// 准贷记虚拟卡-P503批次增加对信用卡支持
			add("119");//长城借记卡
			add("188");//活期一本通
		};
	};
	
	/** 寿险缴费支持的账户类型 */
	public static List<String> lifeAccountTypeList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		
		{
			add("101");//普通活期
//			add("103");// 中银信用卡-P503批次增加对信用卡支持
//			add("104");// 准贷记卡-即QCC长城信用卡-P503批次增加对信用卡支持
//			add("108");// 贷记虚拟卡-P503批次增加对信用卡支持
//			add("109");// 准贷记虚拟卡-P503批次增加对信用卡支持
			add("119");//长城借记卡
			add("188");//活期一本通
		};
	};
	
	/** 保险支付方式  */
	public static Map<String,String> payMethod = new HashMap<String,String>() {
		private static final long serialVersionUID = 1L;
		
		{
			put("101", "2");
			put("103", "5");
			put("104", "5");
			put("108", "5");
			put("109", "5");
			put("119", "1");
			put("188", "2");
		};
	};
	
	/** 国家 */
	public static final Map<String, String> countryMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("CN", "中国");
		};
	};

	
	/** 房屋类型   */
	public static List<String> houseTypeList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		
		{
			add("钢结构");
			add("钢筋混凝土");
			add("混合结构");
			add("砖木结构");
			
		};
	};
	
	public static List<String> houseTypeListrq = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("0001");
			add("0002");
			add("0003");
			add("0004");
			
		};
	};
	
	/** 显示外险条款公司标识  **/
	public static final List<String> insurCode = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("2315D10031");
			add("2315D10032");
			add("2315D10036");
		}
	};
	
	/** 显示前往目的地  **/
	public static final List<String> destination = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("2315D10037");
			add("2315D10038");
			add("2315D10039");
			add("2315D10040");
			add("2315D10041");
			add("2315D10042");
		}
	};
	
	public static final Map<String,String> insurTrantyperp = new HashMap<String,String>() {
		private static final long serialVersionUID = 1L;
		{
			put("0", "家财险");
			put("1", "意外险");
		}
	};
	
	/** 保险类型 */
	public static final List<String> safetyType = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("车险");
			add("家财险");
			add("意外险");
			add("寿险");
		}
	};
	
	/** 保费试算请求方法   */
	public static final Map<String,String> method = new HashMap<String,String>() {
		private static final long serialVersionUID = 1L;
		{
			put("0", Safety.METHOD_FAMILY_COUNT);
			put("1", Safety.METHOD_ACCIDENT_COUNT);
			put("4", Safety.METHOD_FAMILY_COUNT);
			put("5", Safety.METHOD_ACCIDENT_COUNT);
		}
	};
	
	/** 寿险职业代码，页面显示arrays.xml中的Job列表，接口上送时根据下标选择上送 */
	public static final List<String> job = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("01");
			add("02");
			add("03");
			add("04");
			add("05");
			add("06");
			add("07");
		}
	};
	
	/** 燃油类型全国——中文 */
	public static final List<String> fuelTypeCN = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("燃油");
			add("纯电动");
			add("燃料电池");
			add("插电式混合动力");
			add("其他混合动力");
		}
	};
	/** 燃油类型全国——代码 */
	public static final List<String> fuelTypeCode = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("0");
			add("1");
			add("2");
			add("3");
			add("4");
		}
	};
	/** 燃油类型北京——中文 */
	public static final List<String> BJfuelTypeCN = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("汽油");
			add("柴油");
			add("电");
			add("混合油");
			add("天然气");
			add("液化石油气");
			add("甲醇");
			add("乙醇");
			add("太阳能");
			add("混合动力");
			add("无");
			add("其他");
		}
	};
	/** 燃油类型北京——代码 */
	public static final List<String> BJfuelTypeCode = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("A");
			add("B");
			add("C");
			add("D");
			add("E");
			add("F");
			add("L");
			add("M");
			add("N");
			add("O");
			add("Y");
			add("Z");
		}
	};
	/** 车辆落户区域-代码 */
	public static final List<String> carOnZoneCode = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("01");
			add("02");
			add("03");
			add("04");
			add("05");
			add("06");
			add("07");
			add("08");
			add("09");
			add("10");
			add("11");
			add("12");
			add("13");
			add("14");
			add("15");
			add("16");
			add("17");
			add("18");
		}
	};
	/** 车辆落户区域-名称 */
	public static final List<String> carOnZoneCN = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("和平区");
			add("河西区");
			add("河北区");
			add("河东区");
			add("南开区");
			add("红桥区");
			add("东丽区");
			add("西青区");
			add("津南区");
			add("北辰区");
			add("武清");
			add("宝坻");
			add("蓟县");
			add("静海");
			add("宁河");
			add("塘沽");
			add("大港");
			add("汉沽");
		}
	};
	
	/** 省份简称 */
	public static final Map<String, String> mapCityCode_CNS = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("110000", "京");
			put("120000", "津");
			put("130000", "冀");
			put("140000", "晋");
			put("150000", "蒙");
			put("210000", "辽");
			put("220000", "吉");
			put("230000", "黑");
			put("310000", "沪");
			put("320000", "苏");
			put("330000", "浙");
			put("340000", "皖");
			put("350000", "闽");
			put("360000", "赣");
			put("370000", "鲁");
			put("410000", "豫");
			put("420000", "鄂");
			put("430000", "湘");
			put("440000", "粤");
			put("450000", "桂");
			put("460000", "琼");
			put("500000", "渝");
			put("510000", "川");
			put("520000", "贵");
			put("530000", "云");
			put("540000", "藏");
			put("610000", "陕");
			put("620000", "甘");
			put("630000", "青");
			put("640000", "宁");
			put("650000", "新");
		}
	};
	
	/** 简称省份 */
	public static final Map<String, String> mapCNS_CityCode = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("京", "110000");
			put("津", "120000");
			put("冀", "130000");
			put("晋", "140000");
			put("蒙", "150000");
			put("辽", "210000");
			put("吉", "220000");
			put("黑", "230000");
			put("沪", "310000");
			put("苏", "320000");
			put("浙", "330000");
			put("皖", "340000");
			put("闽", "350000");
			put("赣", "360000");
			put("鲁", "370000");
			put("豫", "410000");
			put("鄂", "420000");
			put("湘", "430000");
			put("粤", "440000");
			put("桂", "450000");
			put("琼", "460000");
			put("渝", "500000");
			put("川", "510000");
			put("贵", "520000");
			put("云", "530000");
			put("藏", "540000");
			put("陕", "610000");
			put("甘", "620000");
			put("青", "630000");
			put("宁", "640000");
			put("新", "650000");
		}
	};
	
	/** 省份简称排好顺序的省份代码 */
	public static final List<String> listCNS = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("510000");
			add("420000");
			add("360000");
			add("620000");
			add("450000");
			add("520000");
			add("230000");
			add("310000");
			add("220000");
			add("130000");
			add("120000");
			add("140000");
			add("110000");
			add("210000");
			add("370000");
			add("150000");
			add("350000");
			add("640000");
			add("630000");
			add("460000");
			add("610000");
			add("320000");
			add("340000");
			add("430000");
			add("650000");
			add("500000");
			add("410000");
			add("440000");
			add("530000");
			add("540000");
			add("330000");
		}
	};
	
	/** 省份全称拼音排序后的代码顺序 */
	public static final List<String> listCN = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("340000");
			add("110000");
			add("500000");
			add("350000");
			add("620000");
			add("440000");
			add("450000");
			add("520000");
			add("460000");
			add("130000");
			add("410000");
			add("230000");
			add("420000");
			add("430000");
			add("220000");
			add("320000");
			add("360000");
			add("210000");
			add("150000");
			add("640000");
			add("630000");
			add("370000");
			add("140000");
			add("610000");
			add("310000");
			add("510000");
			add("120000");
			add("540000");
			add("650000");
			add("530000");
			add("330000");
		}
	};
	
	/** 省份全称拼音排序后的代码顺序 */
	public static final Map<String, String> mapCode_CN = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("110000", "北京");
			put("120000", "天津");
			put("130000", "河北");
			put("140000", "山西");
			put("150000", "内蒙古");
			put("210000", "辽宁");
			put("220000", "吉林");
			put("230000", "黑龙江");
			put("310000", "上海");
			put("320000", "江苏");
			put("330000", "浙江");
			put("340000", "安徽");
			put("350000", "福建");
			put("360000", "江西");
			put("370000", "山东");
			put("410000", "河南");
			put("420000", "湖北");
			put("430000", "湖南");
			put("440000", "广东");
			put("450000", "广西");
			put("460000", "海南");
			put("500000", "重庆");
			put("510000", "四川");
			put("520000", "贵州");
			put("530000", "云南");
			put("540000", "西藏");
			put("610000", "陕西");
			put("620000", "甘肃");
			put("630000", "青海");
			put("640000", "宁夏");
			put("650000", "新疆");
		}
	};

	/** 省份代码前两位在listCityCode中的位置 */
	public static final Map<String, String> mapCityCodeIndex = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("11", "0");
			put("12", "1");
			put("13", "2");
			put("14", "14");
			put("15", "26");
			put("21", "39");
			put("22", "54");
			put("23", "64");
			put("31", "78");
			put("32", "79");
			put("33", "93");
			put("34", "105");
			put("35", "122");
			put("36", "132");
			put("37", "144");
			put("41", "162");
			put("42", "180");
			put("43", "194");
			put("44", "209");
			put("45", "231");
			put("46", "246");
			put("50", "249");
			put("51", "250");
			put("52", "272");
			put("53", "282");
			put("54", "299");
			put("61", "307");
			put("62", "318");
			put("63", "333");
			put("64", "342");
			put("65", "348");
		}
	};
	
	/** 城市代码列表 */
	public static final List<String> listCityCode = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("110000");
			add("120000");
			add("130000");
			add("130100");
			add("130200");
			add("130300");
			add("130400");
			add("130500");
			add("130600");
			add("130700");
			add("130800");
			add("130900");
			add("131000");
			add("131100");
			add("140000");
			add("140100");
			add("140200");
			add("140300");
			add("140400");
			add("140500");
			add("140600");
			add("140700");
			add("140800");
			add("140900");
			add("141000");
			add("141100");
			add("150000");
			add("150100");
			add("150200");
			add("150300");
			add("150400");
			add("150500");
			add("150600");
			add("150700");
			add("150800");
			add("150900");
			add("152200");
			add("152500");
			add("152900");
			add("210000");
			add("210100");
			add("210200");
			add("210300");
			add("210400");
			add("210500");
			add("210600");
			add("210700");
			add("210800");
			add("210900");
			add("211000");
			add("211100");
			add("211200");
			add("211300");
			add("211400");
			add("220000");
			add("220100");
			add("220200");
			add("220300");
			add("220400");
			add("220500");
			add("220600");
			add("220700");
			add("220800");
			add("222400");
			add("230000");
			add("230100");
			add("230200");
			add("230300");
			add("230400");
			add("230500");
			add("230600");
			add("230700");
			add("230800");
			add("230900");
			add("231000");
			add("231100");
			add("231200");
			add("232700");
			add("310000");
			add("320000");
			add("320100");
			add("320200");
			add("320300");
			add("320400");
			add("320500");
			add("320600");
			add("320700");
			add("320800");
			add("320900");
			add("321000");
			add("321100");
			add("321200");
			add("321300");
			add("330000");
			add("330100");
			add("330200");
			add("330300");
			add("330400");
			add("330500");
			add("330600");
			add("330700");
			add("330800");
			add("330900");
			add("331000");
			add("331100");
			add("340000");
			add("340100");
			add("340200");
			add("340300");
			add("340400");
			add("340500");
			add("340600");
			add("340700");
			add("340800");
			add("341000");
			add("341100");
			add("341200");
			add("341300");
			add("341500");
			add("341600");
			add("341700");
			add("341800");
			add("350000");
			add("350100");
			add("350200");
			add("350300");
			add("350400");
			add("350500");
			add("350600");
			add("350700");
			add("350800");
			add("350900");
			add("360000");
			add("360100");
			add("360200");
			add("360300");
			add("360400");
			add("360500");
			add("360600");
			add("360700");
			add("360800");
			add("360900");
			add("361000");
			add("361100");
			add("370000");
			add("370100");
			add("370200");
			add("370300");
			add("370400");
			add("370500");
			add("370600");
			add("370700");
			add("370800");
			add("370900");
			add("371000");
			add("371100");
			add("371200");
			add("371300");
			add("371400");
			add("371500");
			add("371600");
			add("371700");
			add("410000");
			add("410100");
			add("410200");
			add("410300");
			add("410400");
			add("410500");
			add("410600");
			add("410700");
			add("410800");
			add("410900");
			add("411000");
			add("411100");
			add("411200");
			add("411300");
			add("411400");
			add("411500");
			add("411600");
			add("411700");
			add("420000");
			add("420100");
			add("420200");
			add("420300");
			add("420500");
			add("420600");
			add("420700");
			add("420800");
			add("420900");
			add("421000");
			add("421100");
			add("421200");
			add("421300");
			add("422800");
			add("430000");
			add("430100");
			add("430200");
			add("430300");
			add("430400");
			add("430500");
			add("430600");
			add("430700");
			add("430800");
			add("430900");
			add("431000");
			add("431100");
			add("431200");
			add("431300");
			add("433100");
			add("440000");
			add("440100");
			add("440200");
			add("440300");
			add("440400");
			add("440500");
			add("440600");
			add("440700");
			add("440800");
			add("440900");
			add("441200");
			add("441300");
			add("441400");
			add("441500");
			add("441600");
			add("441700");
			add("441800");
			add("441900");
			add("442000");
			add("445100");
			add("445200");
			add("445300");
			add("450000");
			add("450100");
			add("450200");
			add("450300");
			add("450400");
			add("450500");
			add("450600");
			add("450700");
			add("450800");
			add("450900");
			add("451000");
			add("451100");
			add("451200");
			add("451300");
			add("451400");
			add("460000");
			add("460100");
			add("460200");
			add("500000");
			add("510000");
			add("510100");
			add("510300");
			add("510400");
			add("510500");
			add("510600");
			add("510700");
			add("510800");
			add("510900");
			add("511000");
			add("511100");
			add("511300");
			add("511400");
			add("511500");
			add("511600");
			add("511700");
			add("511800");
			add("511900");
			add("512000");
			add("513200");
			add("513300");
			add("513400");
			add("520000");
			add("520100");
			add("520200");
			add("520300");
			add("520400");
			add("520500");
			add("520600");
			add("522300");
			add("522600");
			add("522700");
			add("530000");
			add("530100");
			add("530300");
			add("530400");
			add("530500");
			add("530600");
			add("530700");
			add("530800");
			add("530900");
			add("532300");
			add("532500");
			add("532600");
			add("532800");
			add("532900");
			add("533100");
			add("533300");
			add("533400");
			add("540000");
			add("540100");
			add("542100");
			add("542200");
			add("542300");
			add("542400");
			add("542500");
			add("542600");
			add("610000");
			add("610100");
			add("610200");
			add("610300");
			add("610400");
			add("610500");
			add("610600");
			add("610700");
			add("610800");
			add("610900");
			add("611000");
			add("620000");
			add("620100");
			add("620200");
			add("620300");
			add("620400");
			add("620500");
			add("620600");
			add("620700");
			add("620800");
			add("620900");
			add("621000");
			add("621100");
			add("621200");
			add("622900");
			add("623000");
			add("630000");
			add("630100");
			add("632100");
			add("632200");
			add("632300");
			add("632500");
			add("632600");
			add("632700");
			add("632800");
			add("640000");
			add("640100");
			add("640200");
			add("640300");
			add("640400");
			add("640500");
			add("650000");
			add("650100");
			add("650200");
			add("652100");
			add("652200");
			add("652300");
			add("652700");
			add("652800");
			add("652900");
			add("653000");
			add("653100");
			add("653200");
			add("654000");
			add("654200");
			add("654300");
		}
	};
	
	/** 城市代码-地区代码*/
	public static final Map<String, String> mapCityCode_ZoneNo = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("110000", "0010");
			put("120000", "0022");
			put("130000", "0311");
			put("130100", "0311");
			put("130200", "0311");
			put("130300", "0311");
			put("130400", "0311");
			put("130500", "0311");
			put("130600", "0311");
			put("130700", "0311");
			put("130800", "0311");
			put("130900", "0311");
			put("131000", "0311");
			put("131100", "0311");
			put("140000", "0351");
			put("140100", "0351");
			put("140200", "0351");
			put("140300", "0351");
			put("140400", "0351");
			put("140500", "0351");
			put("140600", "0351");
			put("140700", "0351");
			put("140800", "0351");
			put("140900", "0351");
			put("141000", "0351");
			put("141100", "0351");
			put("150000", "0471");
			put("150100", "0471");
			put("150200", "0471");
			put("150300", "0471");
			put("150400", "0471");
			put("150500", "0471");
			put("150600", "0471");
			put("150700", "0471");
			put("150800", "0471");
			put("150900", "0471");
			put("152200", "0471");
			put("152500", "0471");
			put("152900", "0471");
			put("210000", "0024");
			put("210100", "0024");
			put("210200", "0411");
			put("210300", "0024");
			put("210400", "0024");
			put("210500", "0024");
			put("210600", "0024");
			put("210700", "0024");
			put("210800", "0024");
			put("210900", "0024");
			put("211000", "0024");
			put("211100", "0024");
			put("211200", "0024");
			put("211300", "0024");
			put("211400", "0024");
			put("220000", "0431");
			put("220100", "0431");
			put("220200", "0431");
			put("220300", "0431");
			put("220400", "0431");
			put("220500", "0431");
			put("220600", "0431");
			put("220700", "0431");
			put("220800", "0431");
			put("222400", "0431");
			put("230000", "0451");
			put("230100", "0451");
			put("230200", "0451");
			put("230300", "0451");
			put("230400", "0451");
			put("230500", "0451");
			put("230600", "0451");
			put("230700", "0451");
			put("230800", "0451");
			put("230900", "0451");
			put("231000", "0451");
			put("231100", "0451");
			put("231200", "0451");
			put("232700", "0451");
			put("310000", "0021");
			put("320000", "0025");
			put("320100", "0025");
			put("320200", "0025");
			put("320300", "0025");
			put("320400", "0025");
			put("320500", "0512");
			put("320600", "0025");
			put("320700", "0025");
			put("320800", "0025");
			put("320900", "0025");
			put("321000", "0025");
			put("321100", "0025");
			put("321200", "0025");
			put("321300", "0025");
			put("330000", "0571");
			put("330100", "0571");
			put("330200", "0574");
			put("330300", "0571");
			put("330400", "0571");
			put("330500", "0571");
			put("330600", "0571");
			put("330700", "0571");
			put("330800", "0571");
			put("330900", "0571");
			put("331000", "0571");
			put("331100", "0571");
			put("340000", "0551");
			put("340100", "0551");
			put("340200", "0551");
			put("340300", "0551");
			put("340400", "0551");
			put("340500", "0551");
			put("340600", "0551");
			put("340700", "0551");
			put("340800", "0551");
			put("341000", "0551");
			put("341100", "0551");
			put("341200", "0551");
			put("341300", "0551");
			put("341500", "0551");
			put("341600", "0551");
			put("341700", "0551");
			put("341800", "0551");
			put("350000", "0591");
			put("350100", "0591");
			put("350200", "0592");
			put("350300", "0591");
			put("350400", "0591");
			put("350500", "0591");
			put("350600", "0591");
			put("350700", "0591");
			put("350800", "0591");
			put("350900", "0591");
			put("360000", "0791");
			put("360100", "0791");
			put("360200", "0791");
			put("360300", "0791");
			put("360400", "0791");
			put("360500", "0791");
			put("360600", "0791");
			put("360700", "0791");
			put("360800", "0791");
			put("360900", "0791");
			put("361000", "0791");
			put("361100", "0791");
			put("370000", "0531");
			put("370100", "0531");
			put("370200", "0532");
			put("370300", "0531");
			put("370400", "0531");
			put("370500", "0531");
			put("370600", "0531");
			put("370700", "0531");
			put("370800", "0531");
			put("370900", "0531");
			put("371000", "0531");
			put("371100", "0531");
			put("371200", "0531");
			put("371300", "0531");
			put("371400", "0531");
			put("371500", "0531");
			put("371600", "0531");
			put("371700", "0531");
			put("410000", "0371");
			put("410100", "0371");
			put("410200", "0371");
			put("410300", "0371");
			put("410400", "0371");
			put("410500", "0371");
			put("410600", "0371");
			put("410700", "0371");
			put("410800", "0371");
			put("410900", "0371");
			put("411000", "0371");
			put("411100", "0371");
			put("411200", "0371");
			put("411300", "0371");
			put("411400", "0371");
			put("411500", "0371");
			put("411600", "0371");
			put("411700", "0371");
			put("420000", "0027");
			put("420100", "0027");
			put("420200", "0027");
			put("420300", "0027");
			put("420500", "0027");
			put("420600", "0027");
			put("420700", "0027");
			put("420800", "0027");
			put("420900", "0027");
			put("421000", "0027");
			put("421100", "0027");
			put("421200", "0027");
			put("421300", "0027");
			put("422800", "0027");
			put("430000", "0731");
			put("430100", "0731");
			put("430200", "0731");
			put("430300", "0731");
			put("430400", "0731");
			put("430500", "0731");
			put("430600", "0731");
			put("430700", "0731");
			put("430800", "0731");
			put("430900", "0731");
			put("431000", "0731");
			put("431100", "0731");
			put("431200", "0731");
			put("431300", "0731");
			put("433100", "0731");
			put("440000", "0020");
			put("440100", "0020");
			put("440200", "0020");
			put("440300", "0755");
			put("440400", "0020");
			put("440500", "0020");
			put("440600", "0020");
			put("440700", "0020");
			put("440800", "0020");
			put("440900", "0020");
			put("441200", "0020");
			put("441300", "0020");
			put("441400", "0020");
			put("441500", "0020");
			put("441600", "0020");
			put("441700", "0020");
			put("441800", "0020");
			put("441900", "0020");
			put("442000", "0020");
			put("445100", "0020");
			put("445200", "0020");
			put("445300", "0020");
			put("450000", "0771");
			put("450100", "0771");
			put("450200", "0771");
			put("450300", "0771");
			put("450400", "0771");
			put("450500", "0771");
			put("450600", "0771");
			put("450700", "0771");
			put("450800", "0771");
			put("450900", "0771");
			put("451000", "0771");
			put("451100", "0771");
			put("451200", "0771");
			put("451300", "0771");
			put("451400", "0771");
			put("460000", "0898");
			put("460100", "0898");
			put("460200", "0898");
			put("500000", "0023");
			put("510000", "0028");
			put("510100", "0028");
			put("510300", "0028");
			put("510400", "0028");
			put("510500", "0028");
			put("510600", "0028");
			put("510700", "0028");
			put("510800", "0028");
			put("510900", "0028");
			put("511000", "0028");
			put("511100", "0028");
			put("511300", "0028");
			put("511400", "0028");
			put("511500", "0028");
			put("511600", "0028");
			put("511700", "0028");
			put("511800", "0028");
			put("511900", "0028");
			put("512000", "0028");
			put("513200", "0028");
			put("513300", "0028");
			put("513400", "0028");
			put("520000", "0851");
			put("520100", "0851");
			put("520200", "0851");
			put("520300", "0851");
			put("520400", "0851");
			put("520500", "0851");
			put("520600", "0851");
			put("522300", "0851");
			put("522600", "0851");
			put("522700", "0851");
			put("530000", "0871");
			put("530100", "0871");
			put("530300", "0871");
			put("530400", "0871");
			put("530500", "0871");
			put("530600", "0871");
			put("530700", "0871");
			put("530800", "0871");
			put("530900", "0871");
			put("532300", "0871");
			put("532500", "0871");
			put("532600", "0871");
			put("532800", "0871");
			put("532900", "0871");
			put("533100", "0871");
			put("533300", "0871");
			put("533400", "0871");
			put("540000", "0891");
			put("540100", "0891");
			put("542100", "0891");
			put("542200", "0891");
			put("542300", "0891");
			put("542400", "0891");
			put("542500", "0891");
			put("542600", "0891");
			put("610000", "0029");
			put("610100", "0029");
			put("610200", "0029");
			put("610300", "0029");
			put("610400", "0029");
			put("610500", "0029");
			put("610600", "0029");
			put("610700", "0029");
			put("610800", "0029");
			put("610900", "0029");
			put("611000", "0029");
			put("620000", "0931");
			put("620100", "0931");
			put("620200", "0931");
			put("620300", "0931");
			put("620400", "0931");
			put("620500", "0931");
			put("620600", "0931");
			put("620700", "0931");
			put("620800", "0931");
			put("620900", "0931");
			put("621000", "0931");
			put("621100", "0931");
			put("621200", "0931");
			put("622900", "0931");
			put("623000", "0931");
			put("630000", "0971");
			put("630100", "0971");
			put("632100", "0971");
			put("632200", "0971");
			put("632300", "0971");
			put("632500", "0971");
			put("632600", "0971");
			put("632700", "0971");
			put("632800", "0971");
			put("640000", "0951");
			put("640100", "0951");
			put("640200", "0951");
			put("640300", "0951");
			put("640400", "0951");
			put("640500", "0951");
			put("650000", "0991");
			put("650100", "0991");
			put("650200", "0991");
			put("652100", "0991");
			put("652200", "0991");
			put("652300", "0991");
			put("652700", "0991");
			put("652800", "0991");
			put("652900", "0991");
			put("653000", "0991");
			put("653100", "0991");
			put("653200", "0991");
			put("654000", "0991");
			put("654200", "0991");
			put("654300", "0991");

		}
	};
	
	/** citycode-中文map*/
	public static final Map<String, String> mapCityCode_CN = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("110000", "北京市");
			put("120000", "天津市");
			put("130000", "河北省");
			put("130100", "石家庄市");
			put("130200", "唐山市");
			put("130300", "秦皇岛市");
			put("130400", "邯郸市");
			put("130500", "邢台市");
			put("130600", "保定市");
			put("130700", "张家口市");
			put("130800", "承德市");
			put("130900", "沧州市");
			put("131000", "廊坊市");
			put("131100", "衡水市");
			put("140000", "山西省");
			put("140100", "太原市");
			put("140200", "大同市");
			put("140300", "阳泉市");
			put("140400", "长治市");
			put("140500", "晋城市");
			put("140600", "朔州市");
			put("140700", "晋中市");
			put("140800", "运城市");
			put("140900", "忻州市");
			put("141000", "临汾市");
			put("141100", "吕梁市");
			put("150000", "内蒙古自治区");
			put("150100", "呼和浩特市");
			put("150200", "包头市");
			put("150300", "乌海市");
			put("150400", "赤峰市");
			put("150500", "通辽市");
			put("150600", "鄂尔多斯市");
			put("150700", "呼伦贝尔市");
			put("150800", "巴彦淖尔市");
			put("150900", "乌兰察布市");
			put("152200", "兴安盟");
			put("152500", "锡林郭勒盟");
			put("152900", "阿拉善盟");
			put("210000", "辽宁省");
			put("210100", "沈阳市");
			put("210200", "大连市");
			put("210300", "鞍山市");
			put("210400", "抚顺市");
			put("210500", "本溪市");
			put("210600", "丹东市");
			put("210700", "锦州市");
			put("210800", "营口市");
			put("210900", "阜新市");
			put("211000", "辽阳市");
			put("211100", "盘锦市");
			put("211200", "铁岭市");
			put("211300", "朝阳市");
			put("211400", "葫芦岛市");
			put("220000", "吉林省");
			put("220100", "长春市");
			put("220200", "吉林市");
			put("220300", "四平市");
			put("220400", "辽源市");
			put("220500", "通化市");
			put("220600", "白山市");
			put("220700", "松原市");
			put("220800", "白城市");
			put("222400", "延边朝鲜族自治州");
			put("230000", "黑龙江省");
			put("230100", "哈尔滨市");
			put("230200", "齐齐哈尔市");
			put("230300", "鸡西市");
			put("230400", "鹤岗市");
			put("230500", "双鸭山市");
			put("230600", "大庆市");
			put("230700", "伊春市");
			put("230800", "佳木斯市");
			put("230900", "七台河市");
			put("231000", "牡丹江市");
			put("231100", "黑河市");
			put("231200", "绥化市");
			put("232700", "大兴安岭地区");
			put("310000", "上海市");
			put("320000", "江苏省");
			put("320100", "南京市");
			put("320200", "无锡市");
			put("320300", "徐州市");
			put("320400", "常州市");
			put("320500", "苏州市");
			put("320600", "南通市");
			put("320700", "连云港市");
			put("320800", "淮安市");
			put("320900", "盐城市");
			put("321000", "扬州市");
			put("321100", "镇江市");
			put("321200", "泰州市");
			put("321300", "宿迁市");
			put("330000", "浙江省");
			put("330100", "杭州市");
			put("330200", "宁波市");
			put("330300", "温州市");
			put("330400", "嘉兴市");
			put("330500", "湖州市");
			put("330600", "绍兴市");
			put("330700", "金华市");
			put("330800", "衢州市");
			put("330900", "舟山市");
			put("331000", "台州市");
			put("331100", "丽水市");
			put("340000", "安徽省");
			put("340100", "合肥市");
			put("340200", "芜湖市");
			put("340300", "蚌埠市");
			put("340400", "淮南市");
			put("340500", "马鞍山市");
			put("340600", "淮北市");
			put("340700", "铜陵市");
			put("340800", "安庆市");
			put("341000", "黄山市");
			put("341100", "滁州市");
			put("341200", "阜阳市");
			put("341300", "宿州市");
			put("341500", "六安市");
			put("341600", "亳州市");
			put("341700", "池州市");
			put("341800", "宣城市");
			put("350000", "福建省");
			put("350100", "福州市");
			put("350200", "厦门市");
			put("350300", "莆田市");
			put("350400", "三明市");
			put("350500", "泉州市");
			put("350600", "漳州市");
			put("350700", "南平市");
			put("350800", "龙岩市");
			put("350900", "宁德市");
			put("360000", "江西省");
			put("360100", "南昌市");
			put("360200", "景德镇市");
			put("360300", "萍乡市");
			put("360400", "九江市");
			put("360500", "新余市");
			put("360600", "鹰潭市");
			put("360700", "赣州市");
			put("360800", "吉安市");
			put("360900", "宜春市");
			put("361000", "抚州市");
			put("361100", "上饶市");
			put("370000", "山东省");
			put("370100", "济南市");
			put("370200", "青岛市");
			put("370300", "淄博市");
			put("370400", "枣庄市");
			put("370500", "东营市");
			put("370600", "烟台市");
			put("370700", "潍坊市");
			put("370800", "济宁市");
			put("370900", "泰安市");
			put("371000", "威海市");
			put("371100", "日照市");
			put("371200", "莱芜市");
			put("371300", "临沂市");
			put("371400", "德州市");
			put("371500", "聊城市");
			put("371600", "滨州市");
			put("371700", "菏泽市");
			put("410000", "河南省");
			put("410100", "郑州市");
			put("410200", "开封市");
			put("410300", "洛阳市");
			put("410400", "平顶山市");
			put("410500", "安阳市");
			put("410600", "鹤壁市");
			put("410700", "新乡市");
			put("410800", "焦作市");
			put("410900", "濮阳市");
			put("411000", "许昌市");
			put("411100", "漯河市");
			put("411200", "三门峡市");
			put("411300", "南阳市");
			put("411400", "商丘市");
			put("411500", "信阳市");
			put("411600", "周口市");
			put("411700", "驻马店市");
			put("420000", "湖北省");
			put("420100", "武汉市");
			put("420200", "黄石市");
			put("420300", "十堰市");
			put("420500", "宜昌市");
			put("420600", "襄樊市");
			put("420700", "鄂州市");
			put("420800", "荆门市");
			put("420900", "孝感市");
			put("421000", "荆州市");
			put("421100", "黄冈市");
			put("421200", "咸宁市");
			put("421300", "随州市");
			put("422800", "恩施土家族苗族自治州");
			put("430000", "湖南省");
			put("430100", "长沙市");
			put("430200", "株洲市");
			put("430300", "湘潭市");
			put("430400", "衡阳市");
			put("430500", "邵阳市");
			put("430600", "岳阳市");
			put("430700", "常德市");
			put("430800", "张家界市");
			put("430900", "益阳市");
			put("431000", "郴州市");
			put("431100", "永州市");
			put("431200", "怀化市");
			put("431300", "娄底市");
			put("433100", "湘西土家族苗族自治州");
			put("440000", "广东省");
			put("440100", "广州市");
			put("440200", "韶关市");
			put("440300", "深圳市");
			put("440400", "珠海市");
			put("440500", "汕头市");
			put("440600", "佛山市");
			put("440700", "江门市");
			put("440800", "湛江市");
			put("440900", "茂名市");
			put("441200", "肇庆市");
			put("441300", "惠州市");
			put("441400", "梅州市");
			put("441500", "汕尾市");
			put("441600", "河源市");
			put("441700", "阳江市");
			put("441800", "清远市");
			put("441900", "东莞市");
			put("442000", "中山市");
			put("445100", "潮州市");
			put("445200", "揭阳市");
			put("445300", "云浮市");
			put("450000", "广西壮族自治区");
			put("450100", "南宁市");
			put("450200", "柳州市");
			put("450300", "桂林市");
			put("450400", "梧州市");
			put("450500", "北海市");
			put("450600", "防城港市");
			put("450700", "钦州市");
			put("450800", "贵港市");
			put("450900", "玉林市");
			put("451000", "百色市");
			put("451100", "贺州市");
			put("451200", "河池市");
			put("451300", "来宾市");
			put("451400", "崇左市");
			put("460000", "海南省");
			put("460100", "海口市");
			put("460200", "三亚市");
			put("500000", "重庆市");
			put("510000", "四川省");
			put("510100", "成都市");
			put("510300", "自贡市");
			put("510400", "攀枝花市");
			put("510500", "泸州市");
			put("510600", "德阳市");
			put("510700", "绵阳市");
			put("510800", "广元市");
			put("510900", "遂宁市");
			put("511000", "内江市");
			put("511100", "乐山市");
			put("511300", "南充市");
			put("511400", "眉山市");
			put("511500", "宜宾市");
			put("511600", "广安市");
			put("511700", "达州市");
			put("511800", "雅安市");
			put("511900", "巴中市");
			put("512000", "资阳市");
			put("513200", "阿坝藏族羌族自治州");
			put("513300", "甘孜藏族自治州");
			put("513400", "凉山彝族自治州");
			put("520000", "贵州省");
			put("520100", "贵阳市");
			put("520200", "六盘水市");
			put("520300", "遵义市");
			put("520400", "安顺市");
			put("520500", "毕节市");
			put("520600", "铜仁市");
			put("522300", "黔西南布依族苗族自治州");
			put("522600", "黔东南苗族侗族自治州");
			put("522700", "黔南布依族苗族自治州");
			put("530000", "云南省");
			put("530100", "昆明市");
			put("530300", "曲靖市");
			put("530400", "玉溪市");
			put("530500", "保山市");
			put("530600", "昭通市");
			put("530700", "丽江市");
			put("530800", "普洱市(*)");
			put("530900", "临沧市");
			put("532300", "楚雄彝族自治州");
			put("532500", "红河哈尼族彝族自治州");
			put("532600", "文山壮族苗族自治州");
			put("532800", "西双版纳傣族自治州");
			put("532900", "大理白族自治州");
			put("533100", "德宏傣族景颇族自治州");
			put("533300", "怒江傈僳族自治州");
			put("533400", "迪庆藏族自治州");
			put("540000", "西藏自治区");
			put("540100", "拉萨市");
			put("542100", "昌都地区");
			put("542200", "山南地区");
			put("542300", "日喀则地区");
			put("542400", "那曲地区");
			put("542500", "阿里地区");
			put("542600", "林芝地区");
			put("610000", "陕西省");
			put("610100", "西安市");
			put("610200", "铜川市");
			put("610300", "宝鸡市");
			put("610400", "咸阳市");
			put("610500", "渭南市");
			put("610600", "延安市");
			put("610700", "汉中市");
			put("610800", "榆林市");
			put("610900", "安康市");
			put("611000", "商洛市");
			put("620000", "甘肃省");
			put("620100", "兰州市");
			put("620200", "嘉峪关市");
			put("620300", "金昌市");
			put("620400", "白银市");
			put("620500", "天水市");
			put("620600", "武威市");
			put("620700", "张掖市");
			put("620800", "平凉市");
			put("620900", "酒泉市");
			put("621000", "庆阳市");
			put("621100", "定西市");
			put("621200", "陇南市");
			put("622900", "临夏回族自治州");
			put("623000", "甘南藏族自治州");
			put("630000", "青海省");
			put("630100", "西宁市");
			put("632100", "海东地区");
			put("632200", "海北藏族自治州");
			put("632300", "黄南藏族自治州");
			put("632500", "海南藏族自治州");
			put("632600", "果洛藏族自治州");
			put("632700", "玉树藏族自治州");
			put("632800", "海西蒙古族藏族自治州");
			put("640000", "宁夏回族自治区");
			put("640100", "银川市");
			put("640200", "石嘴山市");
			put("640300", "吴忠市");
			put("640400", "固原市");
			put("640500", "中卫市");
			put("650000", "新疆维吾尔自治区");
			put("650100", "乌鲁木齐市");
			put("650200", "克拉玛依市");
			put("652100", "吐鲁番地区");
			put("652200", "哈密地区");
			put("652300", "昌吉回族自治州");
			put("652700", "博尔塔拉蒙古自治州");
			put("652800", "巴音郭楞蒙古自治州");
			put("652900", "阿克苏地区");
			put("653000", "克孜勒苏柯尔克孜自治州");
			put("653100", "喀什地区");
			put("653200", "和田地区");
			put("654000", "伊犁哈萨克自治州");
			put("654200", "塔城地区");
			put("654300", "阿勒泰地区");

		}
	};

	/** 中文-citycode map*/
	public static final Map<String, String> mapCN_CityCode = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("北京市", "110000");
			put("天津市", "120000");
			put("河北省", "130000");
			put("石家庄市", "130100");
			put("唐山市", "130200");
			put("秦皇岛市", "130300");
			put("邯郸市", "130400");
			put("邢台市", "130500");
			put("保定市", "130600");
			put("张家口市", "130700");
			put("承德市", "130800");
			put("沧州市", "130900");
			put("廊坊市", "131000");
			put("衡水市", "131100");
			put("山西省", "140000");
			put("太原市", "140100");
			put("大同市", "140200");
			put("阳泉市", "140300");
			put("长治市", "140400");
			put("晋城市", "140500");
			put("朔州市", "140600");
			put("晋中市", "140700");
			put("运城市", "140800");
			put("忻州市", "140900");
			put("临汾市", "141000");
			put("吕梁市", "141100");
			put("内蒙古自治区", "150000");
			put("呼和浩特市", "150100");
			put("包头市", "150200");
			put("乌海市", "150300");
			put("赤峰市", "150400");
			put("通辽市", "150500");
			put("鄂尔多斯市", "150600");
			put("呼伦贝尔市", "150700");
			put("巴彦淖尔市", "150800");
			put("乌兰察布市", "150900");
			put("兴安盟", "152200");
			put("锡林郭勒盟", "152500");
			put("阿拉善盟", "152900");
			put("辽宁省", "210000");
			put("沈阳市", "210100");
			put("大连市", "210200");
			put("鞍山市", "210300");
			put("抚顺市", "210400");
			put("本溪市", "210500");
			put("丹东市", "210600");
			put("锦州市", "210700");
			put("营口市", "210800");
			put("阜新市", "210900");
			put("辽阳市", "211000");
			put("盘锦市", "211100");
			put("铁岭市", "211200");
			put("朝阳市", "211300");
			put("葫芦岛市", "211400");
			put("吉林省", "220000");
			put("长春市", "220100");
			put("吉林市", "220200");
			put("四平市", "220300");
			put("辽源市", "220400");
			put("通化市", "220500");
			put("白山市", "220600");
			put("松原市", "220700");
			put("白城市", "220800");
			put("延边朝鲜族自治州", "222400");
			put("黑龙江省", "230000");
			put("哈尔滨市", "230100");
			put("齐齐哈尔市", "230200");
			put("鸡西市", "230300");
			put("鹤岗市", "230400");
			put("双鸭山市", "230500");
			put("大庆市", "230600");
			put("伊春市", "230700");
			put("佳木斯市", "230800");
			put("七台河市", "230900");
			put("牡丹江市", "231000");
			put("黑河市", "231100");
			put("绥化市", "231200");
			put("大兴安岭地区", "232700");
			put("上海市", "310000");
			put("江苏省", "320000");
			put("南京市", "320100");
			put("无锡市", "320200");
			put("徐州市", "320300");
			put("常州市", "320400");
			put("苏州市", "320500");
			put("南通市", "320600");
			put("连云港市", "320700");
			put("淮安市", "320800");
			put("盐城市", "320900");
			put("扬州市", "321000");
			put("镇江市", "321100");
			put("泰州市", "321200");
			put("宿迁市", "321300");
			put("浙江省", "330000");
			put("杭州市", "330100");
			put("宁波市", "330200");
			put("温州市", "330300");
			put("嘉兴市", "330400");
			put("湖州市", "330500");
			put("绍兴市", "330600");
			put("金华市", "330700");
			put("衢州市", "330800");
			put("舟山市", "330900");
			put("台州市", "331000");
			put("丽水市", "331100");
			put("安徽省", "340000");
			put("合肥市", "340100");
			put("芜湖市", "340200");
			put("蚌埠市", "340300");
			put("淮南市", "340400");
			put("马鞍山市", "340500");
			put("淮北市", "340600");
			put("铜陵市", "340700");
			put("安庆市", "340800");
			put("黄山市", "341000");
			put("滁州市", "341100");
			put("阜阳市", "341200");
			put("宿州市", "341300");
			put("六安市", "341500");
			put("亳州市", "341600");
			put("池州市", "341700");
			put("宣城市", "341800");
			put("福建省", "350000");
			put("福州市", "350100");
			put("厦门市", "350200");
			put("莆田市", "350300");
			put("三明市", "350400");
			put("泉州市", "350500");
			put("漳州市", "350600");
			put("南平市", "350700");
			put("龙岩市", "350800");
			put("宁德市", "350900");
			put("江西省", "360000");
			put("南昌市", "360100");
			put("景德镇市", "360200");
			put("萍乡市", "360300");
			put("九江市", "360400");
			put("新余市", "360500");
			put("鹰潭市", "360600");
			put("赣州市", "360700");
			put("吉安市", "360800");
			put("宜春市", "360900");
			put("抚州市", "361000");
			put("上饶市", "361100");
			put("山东省", "370000");
			put("济南市", "370100");
			put("青岛市", "370200");
			put("淄博市", "370300");
			put("枣庄市", "370400");
			put("东营市", "370500");
			put("烟台市", "370600");
			put("潍坊市", "370700");
			put("济宁市", "370800");
			put("泰安市", "370900");
			put("威海市", "371000");
			put("日照市", "371100");
			put("莱芜市", "371200");
			put("临沂市", "371300");
			put("德州市", "371400");
			put("聊城市", "371500");
			put("滨州市", "371600");
			put("菏泽市", "371700");
			put("河南省", "410000");
			put("郑州市", "410100");
			put("开封市", "410200");
			put("洛阳市", "410300");
			put("平顶山市", "410400");
			put("安阳市", "410500");
			put("鹤壁市", "410600");
			put("新乡市", "410700");
			put("焦作市", "410800");
			put("濮阳市", "410900");
			put("许昌市", "411000");
			put("漯河市", "411100");
			put("三门峡市", "411200");
			put("南阳市", "411300");
			put("商丘市", "411400");
			put("信阳市", "411500");
			put("周口市", "411600");
			put("驻马店市", "411700");
			put("湖北省", "420000");
			put("武汉市", "420100");
			put("黄石市", "420200");
			put("十堰市", "420300");
			put("宜昌市", "420500");
			put("襄樊市", "420600");
			put("鄂州市", "420700");
			put("荆门市", "420800");
			put("孝感市", "420900");
			put("荆州市", "421000");
			put("黄冈市", "421100");
			put("咸宁市", "421200");
			put("随州市", "421300");
			put("恩施土家族苗族自治州", "422800");
			put("湖南省", "430000");
			put("长沙市", "430100");
			put("株洲市", "430200");
			put("湘潭市", "430300");
			put("衡阳市", "430400");
			put("邵阳市", "430500");
			put("岳阳市", "430600");
			put("常德市", "430700");
			put("张家界市", "430800");
			put("益阳市", "430900");
			put("郴州市", "431000");
			put("永州市", "431100");
			put("怀化市", "431200");
			put("娄底市", "431300");
			put("湘西土家族苗族自治州", "433100");
			put("广东省", "440000");
			put("广州市", "440100");
			put("韶关市", "440200");
			put("深圳市", "440300");
			put("珠海市", "440400");
			put("汕头市", "440500");
			put("佛山市", "440600");
			put("江门市", "440700");
			put("湛江市", "440800");
			put("茂名市", "440900");
			put("肇庆市", "441200");
			put("惠州市", "441300");
			put("梅州市", "441400");
			put("汕尾市", "441500");
			put("河源市", "441600");
			put("阳江市", "441700");
			put("清远市", "441800");
			put("东莞市", "441900");
			put("中山市", "442000");
			put("潮州市", "445100");
			put("揭阳市", "445200");
			put("云浮市", "445300");
			put("广西壮族自治区", "450000");
			put("南宁市", "450100");
			put("柳州市", "450200");
			put("桂林市", "450300");
			put("梧州市", "450400");
			put("北海市", "450500");
			put("防城港市", "450600");
			put("钦州市", "450700");
			put("贵港市", "450800");
			put("玉林市", "450900");
			put("百色市", "451000");
			put("贺州市", "451100");
			put("河池市", "451200");
			put("来宾市", "451300");
			put("崇左市", "451400");
			put("海南省", "460000");
			put("海口市", "460100");
			put("三亚市", "460200");
			put("重庆市", "500000");
			put("四川省", "510000");
			put("成都市", "510100");
			put("自贡市", "510300");
			put("攀枝花市", "510400");
			put("泸州市", "510500");
			put("德阳市", "510600");
			put("绵阳市", "510700");
			put("广元市", "510800");
			put("遂宁市", "510900");
			put("内江市", "511000");
			put("乐山市", "511100");
			put("南充市", "511300");
			put("眉山市", "511400");
			put("宜宾市", "511500");
			put("广安市", "511600");
			put("达州市", "511700");
			put("雅安市", "511800");
			put("巴中市", "511900");
			put("资阳市", "512000");
			put("阿坝藏族羌族自治州", "513200");
			put("甘孜藏族自治州", "513300");
			put("凉山彝族自治州", "513400");
			put("贵州省", "520000");
			put("贵阳市", "520100");
			put("六盘水市", "520200");
			put("遵义市", "520300");
			put("安顺市", "520400");
			put("毕节市", "520500");
			put("铜仁市", "520600");
			put("黔西南布依族苗族自治州", "522300");
			put("黔东南苗族侗族自治州", "522600");
			put("黔南布依族苗族自治州", "522700");
			put("云南省", "530000");
			put("昆明市", "530100");
			put("曲靖市", "530300");
			put("玉溪市", "530400");
			put("保山市", "530500");
			put("昭通市", "530600");
			put("丽江市", "530700");
			put("普洱市(*)", "530800");
			put("临沧市", "530900");
			put("楚雄彝族自治州", "532300");
			put("红河哈尼族彝族自治州", "532500");
			put("文山壮族苗族自治州", "532600");
			put("西双版纳傣族自治州", "532800");
			put("大理白族自治州", "532900");
			put("德宏傣族景颇族自治州", "533100");
			put("怒江傈僳族自治州", "533300");
			put("迪庆藏族自治州", "533400");
			put("西藏自治区", "540000");
			put("拉萨市", "540100");
			put("昌都地区", "542100");
			put("山南地区", "542200");
			put("日喀则地区", "542300");
			put("那曲地区", "542400");
			put("阿里地区", "542500");
			put("林芝地区", "542600");
			put("陕西省", "610000");
			put("西安市", "610100");
			put("铜川市", "610200");
			put("宝鸡市", "610300");
			put("咸阳市", "610400");
			put("渭南市", "610500");
			put("延安市", "610600");
			put("汉中市", "610700");
			put("榆林市", "610800");
			put("安康市", "610900");
			put("商洛市", "611000");
			put("甘肃省", "620000");
			put("兰州市", "620100");
			put("嘉峪关市", "620200");
			put("金昌市", "620300");
			put("白银市", "620400");
			put("天水市", "620500");
			put("武威市", "620600");
			put("张掖市", "620700");
			put("平凉市", "620800");
			put("酒泉市", "620900");
			put("庆阳市", "621000");
			put("定西市", "621100");
			put("陇南市", "621200");
			put("临夏回族自治州", "622900");
			put("甘南藏族自治州", "623000");
			put("青海省", "630000");
			put("西宁市", "630100");
			put("海东地区", "632100");
			put("海北藏族自治州", "632200");
			put("黄南藏族自治州", "632300");
			put("海南藏族自治州", "632500");
			put("果洛藏族自治州", "632600");
			put("玉树藏族自治州", "632700");
			put("海西蒙古族藏族自治州", "632800");
			put("宁夏回族自治区", "640000");
			put("银川市", "640100");
			put("石嘴山市", "640200");
			put("吴忠市", "640300");
			put("固原市", "640400");
			put("中卫市", "640500");
			put("新疆维吾尔自治区", "650000");
			put("乌鲁木齐市", "650100");
			put("克拉玛依市", "650200");
			put("吐鲁番地区", "652100");
			put("哈密地区", "652200");
			put("昌吉回族自治州", "652300");
			put("博尔塔拉蒙古自治州", "652700");
			put("巴音郭楞蒙古自治州", "652800");
			put("阿克苏地区", "652900");
			put("克孜勒苏柯尔克孜自治州", "653000");
			put("喀什地区", "653100");
			put("和田地区", "653200");
			put("伊犁哈萨克自治州", "654000");
			put("塔城地区", "654200");
			put("阿勒泰地区", "654300");
		}
	};
	
	/** popupwd更多 */
	public static String[] btnMore = {"投保","删除"};
	
	/** 清除所有临时存储数据 **/
	public void clearAllData() {
		if (!StringUtil.isNullOrEmpty(productInfoMap)) {
			productInfoMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(acctList)) {
			acctList.clear();
		}
		if (!StringUtil.isNullOrEmpty(proviceList)) {
			proviceList.clear();
		}
		if (!StringUtil.isNullOrEmpty(proviceListHS)) {
			proviceListHS.clear();
		}
		if (!StringUtil.isNullOrEmpty(secondCityList)) {
			secondCityList.clear();
		}
		if (!StringUtil.isNullOrEmpty(secondCountryList)) {
			secondCountryList.clear();
		}
		if (!StringUtil.isNullOrEmpty(thirdCityList)) {
			thirdCityList.clear();
		}
		if (!StringUtil.isNullOrEmpty(thirdCountryList)) {
			thirdCountryList.clear();
		}
		if (!StringUtil.isNullOrEmpty(thirdCityListHS)) {
			thirdCityListHS.clear();
		}
		if (!StringUtil.isNullOrEmpty(thirdCountryListHS)) {
			thirdCountryListHS.clear();
		}
		if (!StringUtil.isNullOrEmpty(countyList)) {
			countyList.clear();
		}
		if (!StringUtil.isNullOrEmpty(countMap)) {
			countMap.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapAutoInsuranceQueryAutoDetail)) {
			mapAutoInsuranceQueryAutoDetail.clear();
		}
		if (!StringUtil.isNullOrEmpty(listAutoInsuranceQueryAllowArea)) {
			listAutoInsuranceQueryAllowArea.clear();
		}
		if (!StringUtil.isNullOrEmpty(listAutoInsuranceQueryAutoType)) {
			listAutoInsuranceQueryAutoType.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapAutoInsuranceQueryCompulsory)) {
			mapAutoInsuranceQueryCompulsory.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapAutoInsuranceQueryCommercial)) {
			mapAutoInsuranceQueryCommercial.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapCarSafetyUserInput)) {
			mapCarSafetyUserInput.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapAutoInsuranceCalculation)) {
			mapAutoInsuranceCalculation.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapAutoInsuranceCreatPolicy)) {
			mapAutoInsuranceCreatPolicy.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapAutoInsurancePaySubmit)) {
			mapAutoInsurancePaySubmit.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapSVRPsnInfoQuery)) {
			mapSVRPsnInfoQuery.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapSaveParams)) {
			mapSaveParams.clear();
		}
		if (!StringUtil.isNull(policyId)) {
			policyId = null;
		}
		if (!StringUtil.isNullOrEmpty(mapAutoInsurPolicyDetailQry)) {
			mapAutoInsurPolicyDetailQry.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapAutoInsurancePayVerify)) {
			mapAutoInsurancePayVerify.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapInsuranceRiskEvaluationQuery)) {
			mapInsuranceRiskEvaluationQuery.clear();
		}
		if (!StringUtil.isNullOrEmpty(listLifeInsuranceProductQuery)) {
			listLifeInsuranceProductQuery.clear();
		}
		if (!StringUtil.isNullOrEmpty(listInsurancePayTypeInfoQuery)) {
			listInsurancePayTypeInfoQuery.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapInsuranceFieldControlInfoQuery)) {
			mapInsuranceFieldControlInfoQuery.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapUserInput)) {
			mapUserInput.clear();
		}
		if (!StringUtil.isNullOrEmpty(controlInfoA)) {
			controlInfoA.clear();
		}
		if (!StringUtil.isNullOrEmpty(controlInfoB)) {
			controlInfoB.clear();
		}
		if (!StringUtil.isNullOrEmpty(controlInfoC)) {
			controlInfoC.clear();
		}
		if (!StringUtil.isNullOrEmpty(controlInfoD)) {
			controlInfoD.clear();
		}
		if (!StringUtil.isNullOrEmpty(controlInfoE)) {
			controlInfoE.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapLifeInsuranceCalculation)) {
			mapLifeInsuranceCalculation.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapLifeInsurancePayVerify)) {
			mapLifeInsurancePayVerify.clear();
		}
		if (!StringUtil.isNullOrEmpty(mapLifeInsurancePaySubmit)) {
			mapLifeInsurancePaySubmit.clear();
		}
		isSaved = false;
		isSaveToThere = false;
		isHoldToThere = false;
		isReturn = false;
	}
}
