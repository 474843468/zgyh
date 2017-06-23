package com.chinamworld.bocmbci.net;

import android.content.Context;

import com.chinamworld.bocmbci.net.model.BaseOkHttpModel;


/**
 * Created by Administrator on 2016/10/19.
 */
public class RequestEngine implements IHttpCallBackListener{

    private BaseOkHttpModel mBaseOkHttpModel;
    private Context mContext;
    private IHttpCallBackListener mHttpCallBackListener;

    private OkHttpNet mOkHttpNet;

    public void cancel(){
        mOkHttpNet.cancel();
    }

    public void setHttpCallBackListener(IHttpCallBackListener callback) {
        mHttpCallBackListener = callback;
    }

    public void getHttp(Context context, BaseOkHttpModel baseOkHttpModel){
        OkHttpNet net = createOkHttpNet(context,baseOkHttpModel);
        net.getHttp(baseOkHttpModel.getUrl());
    }

    public void postHttp(Context context, BaseOkHttpModel baseOkHttpModel){
        OkHttpNet net = createOkHttpNet(context,baseOkHttpModel);
        net.postHttp(baseOkHttpModel.getUrl(),baseOkHttpModel.toJson());
    }
    private OkHttpNet createOkHttpNet(Context context, BaseOkHttpModel baseOkHttpModel){
        mContext = context;
        mBaseOkHttpModel = baseOkHttpModel;
        mOkHttpNet = new OkHttpNet();
        mOkHttpNet.setHttpCallBackListener(this);
        return mOkHttpNet;
    }

    @Override
    public boolean onResponse(Context context, String response, Object extendParams) {
        if(mHttpCallBackListener != null)
            mHttpCallBackListener.onResponse(mContext,response,mBaseOkHttpModel.getExtendParam());
        return false;
    }

    @Override
    public boolean onError(Context context, String exceptionMessage, Object extendParams) {
        if(mHttpCallBackListener != null && mHttpCallBackListener.onError(mContext,exceptionMessage,mBaseOkHttpModel.getExtendParam()))
            return true;

        return false;
    }


}
