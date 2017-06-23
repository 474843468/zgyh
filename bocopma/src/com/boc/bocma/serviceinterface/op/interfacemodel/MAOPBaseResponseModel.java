package com.boc.bocma.serviceinterface.op.interfacemodel;

import com.boc.bocma.exception.MAOPException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 开放平台返回报文解析基类。
 * 所有继承该类的子类都必须定义一个实现Creator的公共final静态成员变量，名称为"CREATOR"。
 * <p>A typical implementation of MAOPBaseResponseModel is:</p>
 * 
 * <pre>
 * public class MyResponseModel extends MAOPBaseResponseModel {
 *     private static final int MY_KEY = "mykey";
 *     private String mData;
 *
 *     public static final MAOPBaseResponseModel.Creator&lt;MyResponseModel&gt; CREATOR
 *             = new MAOPBaseResponseModel.Creator&lt;MyResponseModel&gt;() {
 *         public MyResponseModel createFromJson(JSONObject in) {
 *             return new MyResponseModel(in);
 *         }
 *     };
 *     
 *     private MyResponseModel(JSONObject in) {
 *         mData = in.optString(MY_KEY);
 *     }
 * }
 * </pre>
 */
public class MAOPBaseResponseModel {
    public static interface Creator<T> {
        public static final String FIELD_NAME = "CREATOR";
        public T createFromJson(JSONObject jsonResponse) throws JSONException;
    }
    
    public static String RESPONSE_STATUS = "serviceResponse";
    public static String RESPONSE_CODE = "responseCode";
    public static String RESPONSE_MSG = "responseMsg";
    public static String MSGCDE = "msgcde";//有些接口的失败返回
    public static String RTNMSG = "rtnmsg";//有些接口的失败返回
    
    public static String RESPONSE_OK = "0000000"; 

    private MAOPException exception;
    
    public void setException(MAOPException e) {
        this.exception = new MAOPException(e);
    }
    
    public void setException(Exception e) {
        this.exception = new MAOPException(e);
    }
    
    public MAOPException getException() {
        return exception;
    }
}