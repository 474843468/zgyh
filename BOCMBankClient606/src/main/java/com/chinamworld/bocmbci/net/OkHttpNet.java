/*
 * Copyright (C) 2016 Shenzhen Forms Syntron Information Co.,Ltd. All Rights Reserved.
 */

package com.chinamworld.bocmbci.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.chinamworld.bocmbci.log.LogGloble;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpNet implements Callback {

    private IHttpCallBackListener mHttpCallBackListener;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Call mCall;

    /** 取消通信 */
    public void cancel(){
        try{
            if(mCall != null){
                mCall.cancel();
            }
        }
        catch (Exception e){

        }

    }

    public void setHttpCallBackListener(IHttpCallBackListener callBack){
        mHttpCallBackListener = callBack;
    }

    private OkHttpClient createOkHttpClient(){
        return new OkHttpClient();
    }

    /**
     * 通过get方式上送数据
     * @param url
     */
    public void getHttp(String url){
        OkHttpClient okHttpClient = createOkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        mCall = okHttpClient.newCall(request);
        mCall.enqueue(this);
    }

    /**
     * 通过Post发送请求
     * @param url : 上送URL地址
     * @param postData ：上送数据
     */
    public void postHttp(String url, String postData){
        LogGloble.i("OKHttpNet", "上送数据:" + postData);
        OkHttpClient okHttpClient = createOkHttpClient();
        RequestBody body = RequestBody.create(JSON, postData);
//        FormEncodingBuilder builder = new FormEncodingBuilder();
//        builder.build();
        Request request = creatBuilder().url(url).post(body).build();
        mCall = okHttpClient.newCall(request);
        mCall.enqueue(this);
    }

    private Request.Builder creatBuilder(){
        Request.Builder builder = new Request.Builder();
        builder.addHeader("Accept-Language","zh-cn");
        builder.addHeader("bfw-ctrl","json");
        builder.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");

        builder.header("clentid","1033");
        builder.header("userid","");
        builder.header("acton","");
        builder.header("chnflg","sjyh");
        builder.header("clientType","android");

        return builder;
    }


    @Override
    public void onFailure(Call call, IOException e) {
        sendMessage(null,false);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(response.isSuccessful()) {
            sendMessage(response.body().string(),true);
        }
        else {
            sendMessage(null,false);
        }

    }

    private void sendMessage(String response, boolean isResponseSuccessful){
        Message msg = new Message();
        if(isResponseSuccessful == true) {
            msg.what = 1;
            msg.obj = response;
        }
        else {
            msg.what = 2;
        }
        mHandler.sendMessage(msg);
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    LogGloble.i("OKHttpNet", "返回数据:" + (String) msg.obj);
                    if(mHttpCallBackListener != null)
                        mHttpCallBackListener.onResponse(null,(String) msg.obj,null);
                    break;
                case 2:
                    if(mHttpCallBackListener != null && mHttpCallBackListener.onError(null,(String)msg.obj,null))
                        return true;
                    break;
            }
            return false;
        }
    });
}
