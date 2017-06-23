package com.boc.bocma.serviceinterface.op.business.financemanager;

import android.content.Context;

import com.boc.bocma.serviceinterface.op.MAOPBaseInterface;
import com.boc.bocma.serviceinterface.op.OnOPResultCallback;
import com.boc.bocma.serviceinterface.op.interfacemodel.applyonlineopenaccount.MAOPApplyOnlineOpenAccountParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.applyonlineopenaccount.MAOPApplyOnlineOpenAccountResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.cardbinquerybycardno.MAOCardBinQueryByCardNoParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.cardbinquerybycardno.MAOCardBinQueryByCardNoResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.checkpersonidentity.MAOPCheckPersonIdentityParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.checkpersonidentity.MAOPCheckPersonIdentityResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.get_random_num.MAOPGetRandomNumParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.get_random_num.MAOPGetRandomNumResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.querycommonotherbanklist.MAOPQueryCommonOtherBanklistParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.querycommonotherbanklist.MAOPQueryCommonOtherBanklistResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccattr.MAOPQueryElecAccAttrParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccattr.MAOPQueryElecAccAttrResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccopeningbank.MAOPQueryElecAccOpeningBankParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryelecaccopeningbank.MAOPQueryElecAccOpeningBankResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryopenaccountprogress.MAOPQueryOpenAccountProgressParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryopenaccountprogress.MAOPQueryOpenAccountProgressResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryopeningbank.MAOPQueryOpeningBankParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryopeningbank.MAOPQueryOpeningBankResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryothopenbankbybankid.MAOQueryOthOpenBankByBankIdParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryothopenbankbybankid.MAOQueryOthOpenBankByBankIdResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryprovinceandcity.MAOPQueryProvinceAndCityParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryprovinceandcity.MAOPQueryProvinceAndCityResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.querysysdate.MAOPQuerySysDateParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.querysysdate.MAOPQuerySysDateResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.remoteopenacconlinecheck2pivs.MAORemoteOpenAccOnLineCheck2PivsParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.remoteopenacconlinecheck2pivs.MAORemoteOpenAccOnLineCheck2PivsResponsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.resendopenaccount.MAOPResendOpenAccountParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.resendopenaccount.MAOPResendOpenAccountResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.sendmobileidentifycode.MAOPSendMobileIdentifyCodeParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.sendmobileidentifycode.MAOPSendMobileIdentifyCodeResponseModel;

public class MAOPRemoteOpenAccInterface extends MAOPBaseInterface {

	private Context mContext;

	private static MAOPRemoteOpenAccInterface instance;

	private MAOPRemoteOpenAccInterface(Context context) {
		super(context);
		this.mContext = context;
	}

	public static synchronized MAOPRemoteOpenAccInterface getInstance(
			Context mContext) {
		if (instance == null) {
			instance = new MAOPRemoteOpenAccInterface(mContext);
		}
		return instance;
	}

	/**
	 * 开户前校验接口
	 * 
	 * @param params
	 * @param callback
	 */
	public void checkPersonIdentity(MAOPCheckPersonIdentityParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOPCheckPersonIdentityResponseModel.class, callback);
	}

	/**
	 * 常用他行列表查询接口
	 * 
	 * @param params
	 * @param callback
	 */
	public void queryCommonOtherbankList(
			MAOPQueryCommonOtherBanklistParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOPQueryCommonOtherBanklistResponseModel.class,
				callback);
	}

	/**
	 * 开户行模糊查询
	 * 
	 * @param params
	 * @param callback
	 */
	public void queryOpeningBank(MAOPQueryOpeningBankParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOPQueryOpeningBankResponseModel.class, callback);
	}

	/**
	 * 电子账户归属地查询
	 * 
	 * @param params
	 * @param callback
	 */
	public void queryElecAccAttr(MAOPQueryElecAccAttrParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOPQueryElecAccAttrResponseModel.class, callback);
	}

	/**
	 * 电子账户开户行查询
	 * 
	 * @param params
	 * @param callback
	 */
	public void queryElecAccOpeningBank(
			MAOPQueryElecAccOpeningBankParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOPQueryElecAccOpeningBankResponseModel.class,
				callback);
	}

	/**
	 * 在线开户申请
	 * 
	 * @param params
	 * @param callback
	 */
	public void applyOnlineOpenAccount(
			MAOPApplyOnlineOpenAccountParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOPApplyOnlineOpenAccountResponseModel.class, callback);
	}

	/**
	 * 查询开户进度
	 * 
	 * @param params
	 * @param callback
	 */
	public void queryOpenAccountProgress(
			MAOPQueryOpenAccountProgressParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOPQueryOpenAccountProgressResponseModel.class,
				callback);
	}

	/**
	 * 发送手机验证码
	 * 
	 * @param params
	 * @param callback
	 */
	public void sendmobileidentifycode(
			MAOPSendMobileIdentifyCodeParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOPSendMobileIdentifyCodeResponseModel.class, callback);
	}

	/**
	 * 生成服务器随机数
	 * 
	 * @param params
	 * @param callback
	 */
	public void getRandomNum(MAOPGetRandomNumParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOPGetRandomNumResponseModel.class, callback);
	}

	/**
	 * 获取系统日期
	 * 
	 * @param params
	 * @param callback
	 */
	public void querysysdate(MAOPQuerySysDateParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOPQuerySysDateResponseModel.class, callback);
	}

	/**
	 * 查询所属地
	 * 
	 * @param params
	 * @param callback
	 */
	public void queryProvinceAndCity(
			MAOPQueryProvinceAndCityParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOPQueryProvinceAndCityResponseModel.class, callback);
	}

	/**
	 * 1.2.12 【SA01458】重新发送开户请求接口
	 * 
	 * @param params
	 * @param callback
	 */
	public void resendOpenAccount(MAOPResendOpenAccountParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOPResendOpenAccountResponseModel.class, callback);
	}

	/**
	 * 1.2.17 【SA9207】远程开户单笔联网核查接口tv0001
	 * 
	 * @param params
	 * @param callback
	 */
	public void remoteOpenAccOnLineCheck(
			MAORemoteOpenAccOnLineCheck2PivsParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAORemoteOpenAccOnLineCheck2PivsResponsModel.class,
				callback);
	}

	/**
	 * 1.2.14 【SA9180】查询非农补卡BIN信息接口
	 * 
	 * @param params
	 * @param callback
	 */
	public void cardBinQueryByCardNo(MAOCardBinQueryByCardNoParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOCardBinQueryByCardNoResponseModel.class, callback);
	}

	/**
	 * 1.2.15 【SA9205】非农补卡BIN信息查询开户行接口
	 * 
	 * @param params
	 * @param callback
	 */
	public void queryOthOpenBankByBankId(
			MAOQueryOthOpenBankByBankIdParamsModel params,
			OnOPResultCallback callback) {
		execute(params, MAOQueryOthOpenBankByBankIdResponseModel.class,
				callback);
	}
}
