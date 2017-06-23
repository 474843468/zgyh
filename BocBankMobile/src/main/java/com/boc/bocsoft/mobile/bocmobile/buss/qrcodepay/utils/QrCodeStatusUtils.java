package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils;

/**
 * Created by wangf on 2016/9/19.
 */
public class QrCodeStatusUtils {

    /**
     * 交易状态
     *
     * @param status
     * @return
     */
    public static String getResultStatus(String status) {
        String state;
        switch (status) {
            case "0":
                state = "成功";
                break;
            case "1":
                state = "失败";
                break;
            default:
                state = "银行处理中";
                break;
        }
        return state;
    }

    /**
     * 交易列表 -- 交易状态
     * added by wangf on 2016-11-7 20:41:53
     *
     * @param status
     * @return
     */
    public static String getResultStatusList(String status) {
        String state;
        switch (status) {
            case "0":
                state = "";//列表中的成功不需要显示
                break;
            case "1":
                state = "失败";
                break;
            default:
                state = "银行处理中";
                break;
        }
        return state;
    }


    /**
     * 交易记录 -- 交易说明
     *
     * @param status
     * @return
     */
    public static String getTranRemark(String status) {
        String remark;
        switch (status) {
            case "0":
                remark = "";
                break;
            case "1":
                remark = "已退货";
                break;
            case "2":
                remark = "已撤销";
                break;
            default:
                remark = "";
                break;
        }
        return remark;
    }

}
