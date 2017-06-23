package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCommonQuerySystemDateTime;

/**
 * Created by huixiaobo on 2016/6/29.
 * 获取后台服务时间返回参数
 */
public class PsnQuerySystemDateTimeResult {
    /**服务器时间*/
    private String dateTme;

    public String getDateTme() {
        return dateTme;
    }

    public void setDateTme(String dateTme) {
        this.dateTme = dateTme;
    }

    @Override
    public String toString() {
        return "PsnQuerySystemDateTimeResult{" +
                "dateTme='" + dateTme + '\'' +
                '}';
    }
}
