package com.boc.bocsoft.remoteopenacc.buss.model.applyonlineopenaccount;

import com.boc.bocma.serviceinterface.op.interfacemodel.applyonlineopenaccount.MAOPApplyOnlineOpenAccountResponseModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

/**
 * 1.2.5 【SA9182】在线开户申请
 * 
 * @author lxw
 * 
 */
public class ApplyOnlineOpenAccountResponseModel implements
		BaseResultModel<ApplyOnlineOpenAccountResponseModel> {

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

	@Override
	public ApplyOnlineOpenAccountResponseModel parseResultModel(
			Object resultModel) {
		MAOPApplyOnlineOpenAccountResponseModel result = (MAOPApplyOnlineOpenAccountResponseModel) resultModel;
		vcardNo = result.vcardNo;// 二类账户卡号 String(19) N
		vcardExNo = result.vcardExNo;// 三类账户卡号 String(19) N
		failCode = result.failCode;// 错误码 String(100) N
		failReason = result.failReason;// 错误原因 String(100) N
		creatVcardStat = result.creatVcardStat;// 开立电子卡状态 String(2) N
		bindVcardStat = result.bindVcardStat;// 绑定电子卡状态 String(2) N
		creatVcardExStat = result.creatVcardExStat;// 开立三类账户状态 String(2) N
		bindVcardExStat = result.bindVcardExStat;// 绑定三类账户状态 String(2) N
		openBocnetCode = result.openBocnetCode;// 开通网银返回码 String(100) N
		openBocnetStat = result.openBocnetStat;// 开通网银状态 String(2) N
		openEzCode = result.openEzCode;// 开通易商返回码 String(100) N
		openEzStat = result.openEzStat;// 开通易商状态 String(2) N
		bindEzCode = result.bindEzCode;// 二类账户绑定易商返回码 String(100) N
		bindEzStat = result.bindEzStat;// 二类账户绑定易商状态 String(2) N
		bindEzExCode = result.bindEzExCode;// 三类账户绑定易商返回码 String(100) N
		bindEzExStat = result.bindEzExStat;// 三类账户绑定易商状态 String(2) N
		openPhoneCode = result.openPhoneCode;// 开通电话银行返回码 String(100) N
//		bindPhoneCode = result.bindPhoneCode;// 绑定电话银行返回码 String(100) N
		openPhoneStat = result.openPhoneStat;// 开通电话银行状态 String(2) N
//		bindPhoneStat = result.bindPhoneStat;// 绑定电话银行状态 String(2) N
		isOnlineSubmit = result.isOnlineSubmit;// 是否是联机提交 String(2) 0：联机提交 1：走调度
		inputInfSaveUuid = result.inputInfSaveUuid;// 提交数据的uid N 用于重发功能
		resultCode = result.resultCode;// 联机提交返回code N 返回code码
		resultMsg = result.resultMsg;// 联机提交返回msg N 返回提交结果信息
		return this;
	}
}
