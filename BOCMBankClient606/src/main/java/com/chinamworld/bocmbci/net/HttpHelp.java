package com.chinamworld.bocmbci.net;

import android.content.Context;

import com.chinamworld.bocmbci.net.model.BaseOkHttpModel;
import com.chinamworld.bocmbci.net.model.BaseRequestModel;
import com.chinamworld.bocmbci.net.model.IHttpErrorCallBack;
import com.chinamworld.bocmbci.net.model.IHttpResponseCallBack;
import com.chinamworld.bocmbci.net.model.IOkHttpErrorCode;
import com.chinamworld.bocmbci.net.model.SFResponseData;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;

/**
 * 网络通信工具类
 * Created by yuht on 2016/10/17.
 */
public class HttpHelp{

    private HttpHelp(){}

    public static HttpHelp getInstance(){
        return new HttpHelp();
    }


    /** 正常数据返回接口 */
    private IHttpResponseCallBack mHttpResponseCallBack;
    /** 正常数据返回接口 */
    public void setHttpResponseCallBack(IHttpResponseCallBack callBack){
        mHttpResponseCallBack = callBack;
    }
    /** 通讯异常返回接口 */
    private IHttpErrorCallBack mHttpErrorCallBack;
    /** 通讯异常返回接口 */
    public void setHttpErrorCallBack(IHttpErrorCallBack httpErrorCallBack){
        mHttpErrorCallBack = httpErrorCallBack;
    }
    /** 通讯数据异常，处理异常接口 */
    private IOkHttpErrorCode mOkHttpErrorCode;
    /** 设置通讯数据异常，处理异常接口 */
    public void setOkHttpErrorCode(IOkHttpErrorCode listener){
        mOkHttpErrorCode = listener;
    }

    public RequestEngine getHttpFromSF(Context context, BaseOkHttpModel baseOkHttpModel){
        RequestEngine r = new RequestEngine();
        r.setHttpCallBackListener(createListener(true));
        r.getHttp(context,baseOkHttpModel);
        return r;
    }

    /**
     * 调用四方接口
     * */
    public<T extends BaseRequestModel> RequestEngine postHttpFromSF(Context context, T baseOkHttpModel){
        RequestEngine r = new RequestEngine();
        r.setHttpCallBackListener(createListener(true));
        r.postHttp(context,baseOkHttpModel);
        return r;
    }

    public IHttpCallBackListener createListener(final boolean flag){
        return new IHttpCallBackListener(){
            @Override
            public boolean onResponse(Context context,String response, Object extendParams) {
                if(flag == true) {
                    SFResponseData<Object> data = GsonTools.fromJson(response,SFResponseData.class);
                    if(data.isError()){
                        if(mOkHttpErrorCode != null && true == mOkHttpErrorCode.handlerErrorCode(data,extendParams)) {
                            return true;
                        }
                        MessageDialog.showMessageDialog(context,data.getErrorMessage());
                        return true;
                    }
                }
                if(mHttpResponseCallBack != null && mHttpResponseCallBack.responseCallBack(response,extendParams))
                    return false;
                return false;
            }

            @Override
            public boolean onError(Context context,String exceptionMessage, Object extendParams) {
                if(mHttpErrorCallBack != null && mHttpErrorCallBack.onError(exceptionMessage,extendParams))
                    return false;
                MessageDialog.showMessageDialog(context,"网络异常");
                return false;
            }
        };
    }




}