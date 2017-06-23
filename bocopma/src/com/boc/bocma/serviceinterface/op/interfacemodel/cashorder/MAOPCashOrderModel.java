package com.boc.bocma.serviceinterface.op.interfacemodel.cashorder;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 现钞预约接口结构类
 */
public class MAOPCashOrderModel extends MAOPBaseResponseModel {
    private static final String AGREE_NUMBER_KEY = "agreenum";
    
    public static final String ORDER_NOT_ACCEPT = "5920026";
    
    private String agreeNumber;
    
    /**
     * @return
     * 1、交易成功时返回预约号。
     * 2、交易失败时：
     *  当返回码为：“5920026”时：指定日期不受理特殊事项”时，距离本日最近一天的可预约的工作日在错误信息返回，格式YYYYMMDD。
     *  当返回码为：“5920025”时：在在错误信息返回中填写金额下限。格式X（12），字符型，无小数点正整数，如:100000。
     */
    public String getAgreeNumber() {
        return agreeNumber;
    }

    public MAOPCashOrderModel(JSONObject jsonResponse) throws JSONException {
        agreeNumber = jsonResponse.getString(AGREE_NUMBER_KEY);
    }

    
    public static final Creator<MAOPCashOrderModel> CREATOR = new Creator<MAOPCashOrderModel>() {

        @Override
        public MAOPCashOrderModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPCashOrderModel(jsonResponse);
        }
        
    };
}
