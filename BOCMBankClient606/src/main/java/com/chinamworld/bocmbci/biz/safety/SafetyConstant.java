package com.chinamworld.bocmbci.biz.safety;

public class SafetyConstant {
	
	/** intent key */
	public static final String PRODUCTORSAVE = "productlist_or_savelist";
	public static final String RANDOMNUMBER = "randomNumber";
	public static final String LASTSELECTED = "lastselectecounty";
	public static final String NEEDDELETE = "needdelete";
	public static final String RISKFLAG = "riskFlag";

	/** 校验规则  */
	public static final String SMS = "carsafetySms";// 手机验证码
	public static final String LICENSE_NO = "licenseNo";// 车牌号
	public static final String CAROWNER_NAME = "carOwnerName";// 车主姓名
	public static final String CAROWNER_ID = "carSafetyIdentityNumber";// 只能输入18位证件号码
	public static final String CARSAFETY_OTHERIDTYPE = "carSafetyOtherIdType";// 车险其他证件号码
	public static final String FAPIAO_NO = "faPiaoNo";
	public static final String OTHER_IDTYPE = "otherIdType";// 其他证件号码
	public static final String BRAND_NAME = "brandName";// 品牌型号
	public static final String FRAME_NO = "frameNo";// 车辆识别代码
	public static final String ENGINE_NO = "engineNo";// 发动机号
	public static final String CARSAFETY_ADRESS = "carSafetyAdress";// 车险发票配送地址
	public static final String NAME = "safetyname";//姓名
	public static final String IDENTITYNUM = "safetyIdentityNumber";//证件号
	public static final String MOBILEPHONE = "shoujiH_01_15";//手机号
	public static final String MOBILE_PHONE = "mobilePhone_06_20";// 电话号码，可包含数字、空格和转机符-
	public static final String MOBILE = "mobile";// 11位手机号码
	public static final String PHONE = "phone";//电话
	public static final String ADRESS = "safetyadress";//地址 单位名称
	public static final String ADRESS_k = "safetyadress_k";// 单位名称 后台控制长度
	public static final String POST = "postcode";//邮政编码
	public static final String CARSAFETY_POST = "carsafetypostcode";// 车险邮编
	public static final String EMAIL = "safetyemail";//电子邮箱
	public static final String CARSAFETYEMAIL = "carSafetyemail";// 车险电子邮箱
	public static final String CARSAFETY_BYNAME = "carSafetyname";// 车险被保人姓名
	public static final String CARSAFETY_MOBILE = "carSafetyMobile";// 车险手机号码
	public static final String FAPIAOTITLE = "safetyfapiaotitle";//发票抬头
	public static final String CARSAFETYFAPIAOTITLE = "fapiaotitle";// 车险发票抬头
	public static final String TEMP_NAME = "tempName";// 暂存单名称
	public static final String LIFEINSURAMOUNT = "lifeInsurAmount";// 寿险金额
	public static final String FOURNUMBER = "fourNumber";// 4位整数
	public static final String SEVENNUMBER = "serviceRecommNo";// 7位整数，寿险，业务推荐号
	public static final String NOTEMPTY = "notEmpty";// 非空
	public static final String LIFEPHONE = "lifePhone";// 11到18位字符
	public static final String LIFEWUJINGIDTYPE = "lifeWuJingIdType";
	public static final String LIFEADRESS = "lifeAdress";
	public static final String HOMEANDOFFICE = "lifehomeofficephone"; //家庭电话.办公电话
	public static final String LIFEINSURAMOUNT_K = "lifeInsurAmount_k";// 家庭年收入，个人投保预算

	
	/** 提示语  */
	public static final String LICENSENO = "车牌号";
	public static final String CAROWNERNAME = "车主姓名";
	public static final String IDENTITYNUM_CAROWNER = "证件号码";
	public static final String OTHERIDTYPE = "证件号码";
	public static final String BRANDNAME = "品牌型号";
	public static final String ENGINENO = "发动机号";
	public static final String FRAMENO = "车辆识别代码";
	public static final String FAPIAONUMBER = "发票号";
	
	public static final String NAME_BUYER = "投保人姓名";
	public static final String IDENTITYNUM_BUYER = "投保人证件号码";
	public static final String PHONE_BUYER = "投保人手机号码";
	public static final String MOBILE_PHONE_BUYER = "移动电话";
	public static final String ADRESS_BUYER = "投保人通讯地址";
	public static final String ADRESS_NO_BUYER = "通讯地址";
	public static final String POST_BUYER = "投保人邮编";
	public static final String EMAIL_BUYER = "投保人电子邮箱";
	public static final String BUY_EMAIL = "电子邮箱";
	public static final String MDADRESS_BUYER = "请选择前往目的地";
	
	public static final String NAME_HOLD = "被保人姓名";
	public static final String IDENTITYNUM_HOLD = "被保人证件号码";
	public static final String PHONE_HOLD = "被保人手机号码";
	public static final String ADRESS_HOLD = "被保人通讯地址";
	public static final String POST_HOLD = "被保人邮编";
	public static final String EMAIL_HOLD = "被保人电子邮箱";
	public static final String HSADRESS_HOLD = "房屋财产坐落地址";
	
	public static final String FAPIAO_TITLE = "发票抬头";
	public static final String FAPIAO_ADRESS = "发票邮寄地址";
	public static final String SEND_ADRESS = "配送地址";
	public static final String FAPIAO_POST = "发票邮寄地址邮编";
	public static final String _POST = "邮编";
	public static final String FAPIAO_NAME = "收件人姓名";
	public static final String FAPIAO_PHONE = "收件人电话";
	public static final String HOMETEL = "家庭电话";
	public static final String OFFICETEL = "办公电话";
	public static final String OFFICENAME = "单位名称";
	public static final String APPLINCOME = "投保人年收入";
	public static final String FAMILYINCOME = "家庭年收入";
	public static final String PREMBUDGET = "个人保费预算";
	
	public static final String BNFT_NAME = "受益人姓名";
	public static final String BNFT_ADRESS = "受益人通讯地址";
	public static final String BNFT_IDENTITYNUM = "受益人证件号码";
	public static final String ARBITRATIONNAME = "仲裁机构名称";
	public static final String SERVICERECOMMNO = "推荐网点号";
	public static final String GETYRAR = "领取期限";
	public static final String GETSTARTAGE = "领取年龄";
	public static final String POSTADDR = "保单邮寄地址";
	
	public static final String REFERRERNAME = "推荐人";
	
	public static final String ALIAS = "请输入投保单名称";
	public static final String TEMPNAME = "投保单名称";
	
	public static final String COPIES = "投保份数";
	public static final String EIGHTNUMBER = "eightNumber";
	public static final String SAFETYEMAIL2 = "safetyemail2";
	public static final String RISKPREM = "保费";
	public static final String COVERAGE = "保额";
	
	/** 每页显示条数 */
	public static final String PAGESIZE = "10";
	
	/** 国籍  --只支持中国 */
	public static final String COUNTRY = "CN";
	/** 投保份数 --固定 1份*/
	public static final String RISKUNIT = "1";
	/** 币种  --- 固定 为  rmb*/
	public static final String CURRENCY = "CNY";
	/** 中间业务号   0-电子银行 */
	public static final String BUSIBELONG = "0";
	/** 是否职业外  固定值  0*/
	public static final String ITFLAG = "0";
	/** 是否指定受益人  固定值 0 */
	public static final String BENIFBNFFLAG = "0";
	/** 证件到期日  */
	public static final String IDVALID = "3000/01/01";
	/** 模块退出时返回码 */
	public static final int QUIT_RESULT_CODE = 4444;
}
