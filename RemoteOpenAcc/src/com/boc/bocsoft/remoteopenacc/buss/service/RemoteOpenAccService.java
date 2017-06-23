package com.boc.bocsoft.remoteopenacc.buss.service;

import android.content.Context;

import com.boc.bocma.serviceinterface.op.business.financemanager.MAOPFinanceManagerInterface;
import com.boc.bocma.serviceinterface.op.business.financemanager.MAOPRemoteOpenAccInterface;
import com.boc.bocsoft.remoteopenacc.buss.model.applyonlineopenaccount.ApplyOnlineOpenAccountParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.applyonlineopenaccount.ApplyOnlineOpenAccountResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.cardbinquerybycardno.CardBinQueryByCardNoParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.cardbinquerybycardno.CardBinQueryByCardNoResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.checkpersonidentity.CheckPersonIdentityParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.checkpersonidentity.CheckPersonIdentityResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.msgcode.SendMobileIdentifyCodeParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.msgcode.SendMobileIdentifyCodeResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryelecaccopeningbank.QueryElecAccOpeningBankParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryelecaccopeningbank.QueryElecAccOpeningBankResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryopenaccountprogress.QueryOpenAccountProgressParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryopenaccountprogress.QueryOpenAccountProgressResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryothopenbankbybankid.QueryOthOpenBankByBankIdParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryothopenbankbybankid.QueryOthOpenBankByBankIdResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryprovinceandcity.QueryProvinceAndCityParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryprovinceandcity.QueryProvinceAndCityResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.querysysdate.QuerySysDateParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.querysysdate.QuerySysDateResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.remoteopenacconlinecheck2pivs.RemoteOpenAccOnLineCheck2PivsParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.remoteopenacconlinecheck2pivs.RemoteOpenAccOnLineCheck2PivsResponsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.resendopenaccount.ResendOpenAccountParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.resendopenaccount.ResendOpenAccountResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.uploadidcard.UploadidcardParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.uploadidcard.UploadidcardResponseModel;
import com.boc.bocsoft.remoteopenacc.common.service.BaseService;
import com.boc.bocsoft.remoteopenacc.common.service.HandlerAdapte;
import com.boc.bocsoft.remoteopenacc.common.service.OnTaskFinishListener;
import com.boc.bocsoft.remoteopenacc.common.service.SimpleServiceCallback;

/**
 * 远程开户service
 * 
 * @author lxw
 * 
 */
public class RemoteOpenAccService extends BaseService {

	private Context mContext;
	private OnTaskFinishListener mListener;
	private MAOPRemoteOpenAccInterface mInterface;
	private MAOPFinanceManagerInterface financeManagerIngerface;

	public RemoteOpenAccService(Context context, OnTaskFinishListener listener) {
		super(context, listener);
		mContext = context;
		mListener = listener;
		mInterface = MAOPRemoteOpenAccInterface.getInstance(context);
		financeManagerIngerface = MAOPFinanceManagerInterface
				.getInstance(context);
	}

	/**
	 * 查询开户进度
	 * 
	 * @param params
	 * @param callback
	 */
	public void queryOpenAccountProgress(
			QueryOpenAccountProgressParamsModel params, int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod,
				QueryOpenAccountProgressResponseModel.class, mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		mInterface.queryOpenAccountProgress(params.transformParamsModel(),
				handler);
	}

	/**
	 * 查询短信验证码
	 * 
	 * @param params
	 * @param callback
	 */
	public void sendmobileidentifycode(
			SendMobileIdentifyCodeParamsModel params, int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod,
				SendMobileIdentifyCodeResponseModel.class, mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		mInterface.sendmobileidentifycode(params.transformParamsModel(),
				handler);
	}

	/**
	 * 查询它行开户行
	 */
	// public void queryCommonOtherbankList(
	// QueryCommonOtherBanklistParamsModel params, int requestCod) {
	// SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
	// mContext, requestCod,
	// QueryCommonOtherBanklistResponseModel.class, mListener);
	// HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
	// mInterface.queryCommonOtherbankList(params.transformParamsModel(),
	// handler);
	//
	// }

	/**
	 * 开户行模糊查询
	 */
	// public void queryOpeningBank(QueryOpeningBankParamsModel params,
	// int requestCod) {
	// SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
	// mContext, requestCod, QueryOpeningBankResponseModel.class,
	// mListener);
	// HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
	// mInterface.queryOpeningBank(params.transformParamsModel(), handler);
	//
	// }

	/**
	 * 开户前身份验证
	 * 
	 * @param params
	 * @param callback
	 */
	public void checkPersonIdentity(CheckPersonIdentityParamsModel params,
			int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod, CheckPersonIdentityResponseModel.class,
				mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		mInterface.checkPersonIdentity(params.transformParamsModel(), handler);
	}

	/**
	 * 电子账户归属地查询
	 * 
	 * @param params
	 * @param callback
	 */
	// public void queryElecAccAttr(QueryElecAccAttrParamsModel params,
	// int requestCod) {
	// SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
	// mContext, requestCod, QueryElecAccAttrResponseModel.class,
	// mListener);
	// HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
	// mInterface.queryElecAccAttr(params.transformParamsModel(), handler);
	// }

	/**
	 * 电子账户开户行查询
	 * 
	 * @param params
	 * @param callback
	 */
	public void queryElecAccoOeningBank(
			QueryElecAccOpeningBankParamsModel params, int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod,
				QueryElecAccOpeningBankResponseModel.class, mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		mInterface.queryElecAccOpeningBank(params.transformParamsModel(),
				handler);
	}

	/**
	 * 在线开户申请
	 * 
	 * @param params
	 * @param callback
	 */
	public void applyonlineopenaccount(
			ApplyOnlineOpenAccountParamsModel params, int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod,
				ApplyOnlineOpenAccountResponseModel.class, mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		mInterface.applyOnlineOpenAccount(params.transformParamsModel(),
				handler);
	}

	/**
	 * 身份证图像上传
	 * 
	 * @param params
	 * @param callback
	 */
	public void uploadidcard(UploadidcardParamsModel params, int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod, UploadidcardResponseModel.class,
				mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		financeManagerIngerface.uploadidcard(params.transformParamsModel(),
				handler);
	}

	/**
	 * 获取系统日期
	 * 
	 * @param params
	 * @param callback
	 */
	public void querysysdate(QuerySysDateParamsModel params, int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod, QuerySysDateResponseModel.class,
				mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		mInterface.querysysdate(params.transformParamsModel(), handler);
	}

	/**
	 * 查询所属地
	 * 
	 * @param params
	 * @param callback
	 */
	public void queryProvinceAndCity(QueryProvinceAndCityParamsModel params,
			int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod, QueryProvinceAndCityResponseModel.class,
				mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		mInterface.queryProvinceAndCity(params.transformParamsModel(), handler);
	}

	/**
	 * 重新发送开户请求接口
	 * 
	 * @param params
	 * @param callback
	 */
	public void getResendOpenAccountParamsModel(
			ResendOpenAccountParamsModel params, int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod, ResendOpenAccountResponseModel.class,
				mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		mInterface.resendOpenAccount(params.transformParamsModel(), handler);
	}

	/**
	 * 1.2.17 【SA9207】远程开户单笔联网核查接口tv0001
	 * 
	 * @param params
	 * @param requestCod
	 */
	public void getRemoteOpenAccOnLineCheck(
			RemoteOpenAccOnLineCheck2PivsParamsModel params, int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod,
				RemoteOpenAccOnLineCheck2PivsResponsModel.class, mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		mInterface.remoteOpenAccOnLineCheck(params.transformParamsModel(),
				handler);
	}

	/**
	 * 1.2.14 【SA9180】查询非农补卡BIN信息接口
	 * 
	 * @param params
	 * @param requestCod
	 */
	public void getCardBinQueryByCardNo(CardBinQueryByCardNoParamsModel params,
			int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod, CardBinQueryByCardNoResponseModel.class,
				mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		mInterface.cardBinQueryByCardNo(params.transformParamsModel(), handler);
	}

	/**
	 * 1.2.15 【SA9205】非农补卡BIN信息查询开户行接口
	 * 
	 * @param params
	 * @param requestCod
	 */
	public void getQueryOthOpenBankByBankId(
			QueryOthOpenBankByBankIdParamsModel params, int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod,
				QueryOthOpenBankByBankIdResponseModel.class, mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		mInterface.queryOthOpenBankByBankId(params.transformParamsModel(),
				handler);
	}
}
