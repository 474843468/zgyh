package com.boc.bocsoft.remoteopenacc.buss.service;

import android.content.Context;

import com.boc.bocma.serviceinterface.op.business.financemanager.MAOPRemoteOpenAccInterface;
import com.boc.bocsoft.remoteopenacc.buss.model.getrandomnum.GetRandomNumParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.getrandomnum.GetRandomNumResponseModel;
import com.boc.bocsoft.remoteopenacc.common.service.BaseService;
import com.boc.bocsoft.remoteopenacc.common.service.HandlerAdapte;
import com.boc.bocsoft.remoteopenacc.common.service.OnTaskFinishListener;
import com.boc.bocsoft.remoteopenacc.common.service.SimpleServiceCallback;

/**
 * 公共业务service
 * 
 * @author lxw
 * 
 */
public class CommonService extends BaseService {
	private Context mContext;
	private OnTaskFinishListener mListener;
	private MAOPRemoteOpenAccInterface mInterface;

	public CommonService(Context context, OnTaskFinishListener listener) {
		super(context, listener);
		mContext = context;
		mListener = listener;
		mInterface = MAOPRemoteOpenAccInterface.getInstance(context);
	}

	/**
	 * 查询它行开户行
	 */
	// public void queryOpeningBank(QueryOpeningBankParamsModel params, int
	// requestCod){
	// SimpleServiceCallback mCommonCallback = new
	// SimpleServiceCallback(mContext, requestCod,
	// QueryOpeningBankResponseModel.class, mListener);
	// HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
	// //mInterface.queryopeningbank(params.transformParamsModel(), handler);
	//
	// }

	/**
	 * 生成服务器随机数
	 * 
	 * @param params
	 * @param callback
	 */
	public void getRandomNum(GetRandomNumParamsModel params, int requestCod) {
		SimpleServiceCallback mCommonCallback = new SimpleServiceCallback(
				mContext, requestCod, GetRandomNumResponseModel.class,
				mListener);
		HandlerAdapte handler = new HandlerAdapte(mCommonCallback);
		mInterface.getRandomNum(params.transformParamsModel(), handler);
	}
}
