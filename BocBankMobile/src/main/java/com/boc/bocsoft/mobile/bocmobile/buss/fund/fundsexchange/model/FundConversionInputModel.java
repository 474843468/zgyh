package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.model;

import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/13.
 */

public class FundConversionInputModel {
    //请求上报参数
    String fromFundCode;

    public String getFromFundCode() {
        return fromFundCode;
    }

    public void setFromFundCode(String fromFundCode) {
        this.fromFundCode = fromFundCode;
    }

    //返回参数

    /**
     * fundCode : 220020
     * fundName : 华夏超短
     * canBuy : true
     * tranState :
     */

    private List<InputBean> listBeans;

    public List<InputBean> getListBeans() {
        return listBeans;
    }

    public void setListBeans(List<InputBean> listBeans) {
        this.listBeans = listBeans;
    }


    public static class InputBean {

        private String fundCode;
        private String fundName;
        private String canBuy;
        private String tranState;

        public String getFundCode() {
            return fundCode;
        }

        public void setFundCode(String fundCode) {
            this.fundCode = fundCode;
        }

        public String getFundName() {
            return fundName;
        }

        public void setFundName(String fundName) {
            this.fundName = fundName;
        }

        public String getCanBuy() {
            return canBuy;
        }

        public void setCanBuy(String canBuy) {
            this.canBuy = canBuy;
        }

        public String getTranState() {
            return tranState;
        }

        public void setTranState(String tranState) {
            this.tranState = tranState;
        }

    }
}
