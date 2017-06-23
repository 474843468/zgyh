package com.boc.bocma.serviceinterface.op.interfacemodel.queryopenaccountprogress;

import org.json.JSONObject;

/**
 * 1.2.6 【SA9184】查询开户进度 list model
 * 
 * @author gwluo
 * 
 */
public class MAOQueryOpenAccountProgressResultModel {
	private final static String ORGCODE = "orgCode";
	private final static String ORGNAME = "orgName";
	private final static String OPENACCOUNTTYPE = "openAccountType";
	private final static String UUID = "uuid";
	private final static String VCARDNO = "vcardNo";
	private final static String VCARDEXNO = "vcardExNo";
	private final static String OTHCARDNO = "othCardNo";
	private final static String QUERYRCPSSTAT = "queryRcpsStat";
	private final static String CREATCIFSTAT = "creatCifStat";
	private final static String CREATVCARDSTAT = "creatVcardStat";
	private final static String BINDVCARDSTAT = "bindVcardStat";
	private final static String CREATVCARDEXSTAT = "creatVcardExStat";
	private final static String BINDVCARDEXSTAT = "bindVcardExStat";
	private final static String OPENBOCNETSTAT = "openBocnetStat";
	private final static String OPENBOCNETCODE = "openBocnetCode";
	private final static String OPENEZSTAT = "openEzStat";
	private final static String OPENEZCODE = "openEzCode";
	private final static String BINDEZSTAT = "bindEzStat";
	private final static String BINDEZCODE = "bindEzCode";
	private final static String BINDEZEXSTAT = "bindEzExStat";
	private final static String BINDEZEXCODE = "bindEzExCode";
	private final static String OPENPHONESTAT = "openPhoneStat";
	private final static String OPENPHONECODE = "openPhoneCode";
//	private final static String BINDPHONESTAT = "bindPhoneStat";
//	private final static String BINDPHONECODE = "bindPhoneCode";
	private final static String MSGSTAT = "msgStat";
	private final static String FAILCODE = "failCode";
	private final static String FAILREASON = "failReason";

	public String orgCode;// 归属机构号 String(5) Y
	public String orgName;// 归属机构名称 String(200) Y
	// 开卡类型 String(2) Y 01开立二类账户
	// 02开立三类账户
	// 03同时开立二、三类账户
	public String openAccountType;

	public String uuid;// uuid String(32) Y
	public String vcardNo;// 电子卡号(二类) String(19) Y
	public String vcardExNo;// 电子卡号(三类) String(19) Y
	public String othCardNo;// 他行卡号 String(19) Y 只显示前4后4位，其余*号屏蔽
	public String queryRcpsStat;// 他行卡查询结果状态 String(2) Y 00-初始，01-成功，02-失败
	public String creatCifStat;// 开立客户号状态 String(2) Y 00-初始，01-成功，02-失败
	public String creatVcardStat;// 开立二类账户状态 String(2) Y
									// 00-初始，01-成功，02-失败，03-未明，06-放弃
	public String bindVcardStat;// 绑定二类账户状态 String(2) Y
								// 00-初始，01-成功，02-失败，03-未，06-放弃
	public String creatVcardExStat;// 开立三类账户状态 String(2) Y
									// 00-初始，01-成功，02-失败，03-未，06-放弃
	public String bindVcardExStat;// 绑定三类账户状态 String(2) Y
									// 00-初始，01-成功，02-失败，03-未明，06-放弃
	public String openBocnetStat;// 开通网银状态 String(2) Y 00-初始，01-成功，02-失败,06-放弃
	public String openBocnetCode;// 开通网银返回码 String(100) Y
	public String openEzStat;// 开通易商状态 String(2) Y 00-初始，01-成功，02-失败,06-放弃
	public String openEzCode;// 开通易商返回码 String(100) Y
	public String bindEzStat;// 二类账户绑定易商状态 String(2) Y 00-初始，01-成功，02-失败,06-放弃
	public String bindEzCode;// 二类账户绑定易商返回码 String(100) Y
	public String bindEzExStat;// 三类账户绑定易商状态 String(2) Y 00-初始，01-成功，02-失败,06-放弃
	public String bindEzExCode;// 三类账户绑定易商返回码 String(100) Y
	public String openPhoneStat;// 开通电话银行状态 String(2) Y 00-初始，01-成功，02-失败，06-放弃
	public String openPhoneCode;// 开通电话银行返回码 String(100) Y
	//2016-05-26 20:15:26 yx  删除  改造
//	public String bindPhoneStat;// 绑定电话银行状态 String(2) Y 00-初始，01-成功，02-失败,06-放弃
//	public String bindPhoneCode;// 绑定电话银行返回码 String(100) Y
	public String msgStat;// 短信发送状态 String(2) Y
							// -1-初始化，00-待发,01-成功，02-失败，03-未明，04-流程处理中，05-短信通知处理中
	public String failCode;// 错误码 String(100) Y 开户或开服务失败时后台返回的错误码
	public String failReason;// 进度详情 String(100) Y 开户或开服务失败时返回的错误信息

	public void parserJson(JSONObject json) {
		if (json.toString() == null) {
			return;
		}
		orgCode = json.optString(ORGCODE);
		orgName = json.optString(ORGNAME);
		openAccountType = json.optString(OPENACCOUNTTYPE);
		uuid = json.optString(UUID);
		vcardNo = json.optString(VCARDNO);
		vcardExNo = json.optString(VCARDEXNO);
		othCardNo = json.optString(OTHCARDNO);
		queryRcpsStat = json.optString(QUERYRCPSSTAT);
		creatCifStat = json.optString(CREATCIFSTAT);
		creatVcardStat = json.optString(CREATVCARDSTAT);
		bindVcardStat = json.optString(BINDVCARDSTAT);
		creatVcardExStat = json.optString(CREATVCARDEXSTAT);
		bindVcardExStat = json.optString(BINDVCARDEXSTAT);
		openBocnetStat = json.optString(OPENBOCNETSTAT);
		openBocnetCode = json.optString(OPENBOCNETCODE);
		openEzStat = json.optString(OPENEZSTAT);
		openEzCode = json.optString(OPENEZCODE);
		bindEzStat = json.optString(BINDEZSTAT);
		bindEzCode = json.optString(BINDEZCODE);
		bindEzExStat = json.optString(BINDEZEXSTAT);
		bindEzExCode = json.optString(BINDEZEXCODE);
		openPhoneStat = json.optString(OPENPHONESTAT);
		openPhoneCode = json.optString(OPENPHONECODE);
//		bindPhoneStat = json.optString(BINDPHONESTAT);
//		bindPhoneCode = json.optString(BINDPHONECODE);
		msgStat = json.optString(MSGSTAT);
		failCode = json.optString(FAILCODE);
		failReason = json.optString(FAILREASON);
	}

}
