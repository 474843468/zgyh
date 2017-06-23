package com.chinamworld.bocmbci.biz.remittance;

/**
 * 杂七杂八的也不知道是啥玩意儿反正都是一些个字段
 * 
 * @author Zhi
 */
public class RemittanceContent {
	/** 模板管理列表分页每次请求条数 */
	public static final String PAGESIZE = "10";
	/** 跨境汇款选择国家的文件 */
	public static final String COUNTRYFILENAME = "RemittanceCountry.txt";
	/** 跨境汇款须知页返回时的返回码 */
	public static final int RESULT_CODE_MUSTKNOW_RESULT = 1;
	/** 页面跳转标识字段 */
	public static final String JUMPFLAG = "jumpFlag";
	/** 跳转选择国家页面请求码 */
	public static final int CHOOSE_CHOUNTRY = 1000;
	/** 查询SWIFT码的请求返回码 */
	public static int QUERY_SWIFT = 3333;
	/** 查询模板请求码 */
	public static int CHOOSE_MODE = 1001;
	
	/** 跨境汇款须知页返回时的返回码 */
	public static final int RESULT_CODE_COLLECTION_BANK_RESULT = 105;
	public static final int RESULT_CODE_PAY_BANK_AREA= 106;
	
	/***********************************************正则校验提示中的字段名***************************************************/
	
	public static final String REMITTERNAME_CN = "汇款人名称（英文或拼音）";
	public static final String REMITTERADDRESS_CN = "汇款人地址";
	public static final String REMITTERPOST_CN = "汇款人邮编";
	public static final String REMITTERPHONE_CN = "汇款人电话";
	
	public static final String PAYEENAME_CN = "收款人名称";
	public static final String PAYEEADDRESS_CN = "收款人地址";
	public static final String PAYEEACCOUNTNUMBER_CN = "收款人账号";
	public static final String PAYEESWIFTCODE_CN = "收款银行SWIFT代码";
	public static final String PAYEEBANKFULLNAME_CN = "收款银行全称";
	public static final String PAYEEBANKADDRESS_CN = "收款银行地址";
	public static final String PAYEEBANKNUMBER_CN = "收款行行号";
	public static final String PAYEEPHONE_CN = "收款人联系电话";
	

	public static final String PAYNUMBER_CN = "汇款金额";
	public static final String TOPAYEEMESSAGE_CN = "给收款人的留言";
	public static final String PAYUSE_CN = "汇款用途详细说明";
	
	public static final String MODELNAME_CN="模板名称";
	public static final String BANKNAME_CN="银行名称";
	public static final String BANKPAYEEBANKFULLNAME_CN="收款银行全称";
	public static final String QUERYSWIFT_CN="SWIFT代码";

	//赵政强加
	public static final String PAYBANKCOUNTRY_CN = "收款银行所在国家（地区）";
	public static final String PAYMANCOUNTRY_CN = "收款人常驻国家（地区）";
	/***********************************************正则校验规则名称***************************************************/
	
	public static final String REMITTERNAME = "remitterName";
	public static final String REMITTERADDRESS = "remitterAdress";
//	public static final String REMITTERADRESS_70 = "remitterAdress_70";
	public static final String PAYEENAME = "payeeName";
	public static final String REMITTANCECODE = "remittanceCode";
	public static final String REMITTERPHONE = "remitterPhone";
	public static final String REMITTANCEMODELNAME="remittanceModelName";

	//赵政强加
	public static final String BANKNAME = "bankName" ;
	public static final String PAYBANKCOUNTRY = "payBankCountry";
	public static final String PAYMANCOUNTRY = "payManCountry";
	
	public static final String PAYEEADDRESS = "payeeAddress";
	public static final String PAYEEACCOUNTNUMBER = "payeeAccNumber";
	public static final String PAYEESWIFTCODE = "SWIFTCode";
	public static final String PAYEEBANKFULLNAME = "payeeBankFullName";
	public static final String PAYEEBANKNUMBER = "payeeBankNumber";
	public static final String PAYEEPHONE = "payeePhone";
	
	public static final String PAYNUMBER = "payNumber";
	public static final String TOPAYEEMESSAGE = "toPayeeMessage";
	public static final String PAYUSE = "payUse";
	public static final String REMITTANCEPAYEEBANKNOCA="remittancePayeeBankNoCA";
	public static final String REMITTANCEQUERYSWIFTCODE="remittanceQuerySWIFTCode";
	public static final String REMITTANCEQUERYBANKNAME="remittanceQueryBankName";
	public static final String REMITTANCEPAYEEBANKNAME="remittancePayeeBankName";
	public static final String PAYACCNUMBER="payAccNumber";
	public static final String REMITTANCEPAYEEACCOUNTADD="remittancePayeeAccountAdd";
	public static final String REMITTANCEPAYEEACCOUNT="remittancePayeeAccount";
	public static final String REMITTERPAYEEADRESS="remitterPayeeAdress";
}
