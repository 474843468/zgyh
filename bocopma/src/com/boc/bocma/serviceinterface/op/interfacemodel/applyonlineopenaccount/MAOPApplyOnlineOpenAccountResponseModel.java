package com.boc.bocma.serviceinterface.op.interfacemodel.applyonlineopenaccount;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * 1.2.5 【SA9182】在线开户申请
 */
public class MAOPApplyOnlineOpenAccountResponseModel extends
		MAOPBaseResponseModel {
	private final String VCARDNO = "vcardNo";
	private final String VCARDEXNO = "vcardExNo";
	private final String FAILCODE = "failCode";
	private final String FAILREASON = "failReason";
	private final String CREATVCARDSTAT = "creatVcardStat";
	private final String BINDVCARDSTAT = "bindVcardStat";
	private final String CREATVCARDEXSTAT = "creatVcardExStat";
	private final String BINDVCARDEXSTAT = "bindVcardExStat";
	private final String OPENBOCNETCODE = "openBocnetCode";
	private final String OPENBOCNETSTAT = "openBocnetStat";
	private final String OPENEZCODE = "openEzCode";
	private final String OPENEZSTAT = "openEzStat";
	private final String BINDEZCODE = "bindEzCode";
	private final String BINDEZSTAT = "bindEzStat";
	private final String BINDEZEXCODE = "bindEzExCode";
	private final String BINDEZEXSTAT = "bindEzExStat";
	private final String OPENPHONECODE = "openPhoneCode";
//	private final String BINDPHONECODE = "bindPhoneCode";
	private final String OPENPHONESTAT = "openPhoneStat";
//	private final String BINDPHONESTAT = "bindPhoneStat";
	private final String ISONLINESUBMIT = "isOnlineSubmit";
	private final String INPUTINFSAVEUUID = "inputInfSaveUuid";
	private final String RESULTCODE = "resultCode";
	private final String RESULTMSG = "resultMsg";

	public String vcardNo;// 二类账户卡号 String(19) N
	public String vcardExNo;// 三类账户卡号 String(19) N
	public String failCode;// 错误码 String(100) N
	public String failReason;// 错误原因 String(100) N
	public String creatVcardStat;// 开立电子卡状态 String(2) N 00-初始，01-成功，02-失败，03-未明
	public String bindVcardStat;// 绑定电子卡状态 String(2) N 00-初始，01-成功，02-失败，03-未明
	public String creatVcardExStat;// 开立三类账户状态 String(2) N
	public String bindVcardExStat;// 绑定三类账户状态 String(2) N
	public String openBocnetCode;// 开通网银返回码 String(100) N
	public String openBocnetStat;// 开通网银状态 String(2) N 00-初始，01-成功，02-失败,06-放弃
	public String openEzCode;// 开通易商返回码 String(100) N
	public String openEzStat;// 开通易商状态 String(2) N 00-初始，01-成功，02-失败,06-放弃
	public String bindEzCode;// 二类账户绑定易商返回码 String(100) N
	public String bindEzStat;// 二类账户绑定易商状态 String(2) N 00-初始，01-成功，02-失败,06-放弃
	public String bindEzExCode;// 三类账户绑定易商返回码 String(100) N
	public String bindEzExStat;// 三类账户绑定易商状态 String(2) N 00-初始，01-成功，02-失败,06-放弃
	public String openPhoneCode;// 开通电话银行返回码 String(100) N
//	public String bindPhoneCode;// 绑定电话银行返回码 String(100) N
	public String openPhoneStat;// 开通电话银行状态 String(2) N 00-初始，01-成功，02-失败，06-放弃
//	public String bindPhoneStat;// 绑定电话银行状态 String(2) N 00-初始，01-成功，02-失败,06-放弃
	public String isOnlineSubmit;// 是否是联机提交 String(2) 0：联机提交 1：走调度
	public String inputInfSaveUuid;// 提交数据的uid N 用于重发功能
	public String resultCode;// 联机提交返回code N 返回code码
	public String resultMsg;// 联机提交返回msg N 返回提交结果信息

	public MAOPApplyOnlineOpenAccountResponseModel(JSONObject jsonResponse) {
		vcardNo = jsonResponse.optString(VCARDNO);
		vcardExNo = jsonResponse.optString(VCARDEXNO);
		failCode = jsonResponse.optString(FAILCODE);
		failReason = jsonResponse.optString(FAILREASON);
		creatVcardStat = jsonResponse.optString(CREATVCARDSTAT);
		bindVcardStat = jsonResponse.optString(BINDVCARDSTAT);
		creatVcardExStat = jsonResponse.optString(CREATVCARDEXSTAT);
		bindVcardExStat = jsonResponse.optString(BINDVCARDEXSTAT);
		openBocnetCode = jsonResponse.optString(OPENBOCNETCODE);
		openBocnetStat = jsonResponse.optString(OPENBOCNETSTAT);
		openEzCode = jsonResponse.optString(OPENEZCODE);
		openEzStat = jsonResponse.optString(OPENEZSTAT);
		bindEzCode = jsonResponse.optString(BINDEZCODE);
		bindEzStat = jsonResponse.optString(BINDEZSTAT);
		bindEzExCode = jsonResponse.optString(BINDEZEXCODE);
		bindEzExStat = jsonResponse.optString(BINDEZEXSTAT);
		openPhoneCode = jsonResponse.optString(OPENPHONECODE);
//		bindPhoneCode = jsonResponse.optString(BINDPHONECODE);
		openPhoneStat = jsonResponse.optString(OPENPHONESTAT);
//		bindPhoneStat = jsonResponse.optString(BINDPHONESTAT);
		isOnlineSubmit = jsonResponse.optString(ISONLINESUBMIT);
		inputInfSaveUuid = jsonResponse.optString(INPUTINFSAVEUUID);
		resultCode = jsonResponse.optString(RESULTCODE);
		resultMsg = jsonResponse.optString(RESULTMSG);
	}

	public static final Creator<MAOPApplyOnlineOpenAccountResponseModel> CREATOR = new Creator<MAOPApplyOnlineOpenAccountResponseModel>() {
		@Override
		public MAOPApplyOnlineOpenAccountResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAOPApplyOnlineOpenAccountResponseModel(jsonResponse);
		}

	};
}
