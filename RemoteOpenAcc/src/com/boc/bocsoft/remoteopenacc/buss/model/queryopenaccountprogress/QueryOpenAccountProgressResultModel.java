package com.boc.bocsoft.remoteopenacc.buss.model.queryopenaccountprogress;

/**
 * 1.2.6 【SA9184】查询开户进度 list model
 * 
 * @author gwluo
 * 
 */
public class QueryOpenAccountProgressResultModel {

	public String orgCode;// 归属机构号 String(5) Y
	public String orgName;// 归属机构名称 String(200) Y
	/**
	 * 开卡类型 String(2) Y 01开立二类账户 02开立三类账户 03同时开立二、三类账户
	 */
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
	//2016-05-26 20:16:11  yx 删除 改造
//	public String bindPhoneStat;// 绑定电话银行状态 String(2) Y 00-初始，01-成功，02-失败,06-放弃
//	public String bindPhoneCode;// 绑定电话银行返回码 String(100) Y
	public String msgStat;// 短信发送状态 String(2) Y
							// -1-初始化，00-待发,01-成功，02-失败，03-未明，04-流程处理中，05-短信通知处理中
	public String failCode;// 错误码 String(100) Y 开户或开服务失败时后台返回的错误码
	public String failReason;// 进度详情 String(100) Y 开户或开服务失败时返回的错误信息
}
