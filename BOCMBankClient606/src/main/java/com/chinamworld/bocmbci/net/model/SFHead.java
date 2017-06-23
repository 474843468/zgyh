package com.chinamworld.bocmbci.net.model;

/**
 * 四方公司接口返回报文头
 * Created by yuht on 2016/10/19.
 */
public class SFHead {
    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }

    private String stat;
    private String result;


    private String date;


    /** 是否是异常通信 */
    public boolean isError(){
         return !"00".equals(stat);
    }

    public String getErrorMessage(){
        return result;
    }

    public String getErrorCode(){
        return stat;
    }
}
