package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundposition.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by taoyongzhen on 2016/11/30.
 */

public class FundFloatingProfileLossModel {
    // 上报请求参数


    /**
     * 基金代码
     */
    private String fundCode;

    /**
     * 开始日期
     */
    private String startDate;
    /**
     * 截止日期
     */
    private String endDate;


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }


    private List<ResultListBean> resultList;


    public List<ResultListBean> getResultList() {
        return resultList;
    }

    public void setResultList(List<ResultListBean> resultList) {
        this.resultList = resultList;
    }

    public static class ResultListBean implements Parcelable{
        /**
         *基金信息对象
         */
        private String fundInfo;

        /**
         * 网银抓包存在该字段，基金文档无此字段
         */

        private String cashFlag;

        /**
         * 基金代码
         */
        private String fundCode;

        /**
         * 币种
         */

        private String curceny;
        /**
         * 期末持仓市值
         */
        private String endCost;
        /**
         * 期末持仓浮动盈亏
         */
        private String endFloat;
        /**
         * 基金名称
         */
        private String fundName;

        /**
         * 回收金额（1）
         */
        private String hsAmount;
        /**
         * 交易费用（3）
         */
        private String jyAmount;
        /**
         * 期间总盈亏
         */
        private String middleFloat;
        /**
         * 期间已实现盈亏
         */
        private String resultFloat;
        /**
         * 期初持仓市值--抓包"c"小写
         */
        private String startcost;
        /**
         * 投入金额（2）
         */
        private String trAmount;



        public String getFundInfo() {
            return fundInfo;
        }

        public void setFundInfo(String fundInfo) {
            this.fundInfo = fundInfo;
        }

        public String getCashFlag() {
            return cashFlag;
        }

        public void setCashFlag(String cashFlag) {
            this.cashFlag = cashFlag;
        }

        public String getCurceny() {
            return curceny;
        }

        public void setCurceny(String curceny) {
            this.curceny = curceny;
        }

        public String getEndCost() {
            return endCost;
        }

        public void setEndCost(String endCost) {
            this.endCost = endCost;
        }

        public String getEndFloat() {
            return endFloat;
        }

        public void setEndFloat(String endFloat) {
            this.endFloat = endFloat;
        }

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

        public String getHsAmount() {
            return hsAmount;
        }

        public void setHsAmount(String hsAmount) {
            this.hsAmount = hsAmount;
        }

        public String getJyAmount() {
            return jyAmount;
        }

        public void setJyAmount(String jyAmount) {
            this.jyAmount = jyAmount;
        }

        public String getMiddleFloat() {
            return middleFloat;
        }

        public void setMiddleFloat(String middleFloat) {
            this.middleFloat = middleFloat;
        }

        public String getResultFloat() {
            return resultFloat;
        }

        public void setResultFloat(String resultFloat) {
            this.resultFloat = resultFloat;
        }

        public String getStartcost() {
            return startcost;
        }

        public void setStartcost(String startcost) {
            this.startcost = startcost;
        }

        public String getTrAmount() {
            return trAmount;
        }

        public void setTrAmount(String trAmount) {
            this.trAmount = trAmount;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(fundInfo);
            dest.writeString(cashFlag);
            dest.writeString(fundCode);
            dest.writeString(endCost);
            dest.writeString(endFloat);
            dest.writeString(fundName);
            dest.writeString(hsAmount);
            dest.writeString(jyAmount);
            dest.writeString(middleFloat);
            dest.writeString(resultFloat);
            dest.writeString(startcost);
            dest.writeString(trAmount);
        }
        public ResultListBean(){

        }



        protected ResultListBean(Parcel in){
            this.fundInfo = in.readString();
            this.cashFlag = in.readString();
            this.fundCode =  in.readString();
            this.endCost = in.readString();
            this.endFloat = in.readString();
            this.fundName = in.readString();
            this.hsAmount = in.readString();
            this.jyAmount = in.readString();
            this.middleFloat = in.readString();
            this.resultFloat = in.readString();
            this.startcost = in.readString();
            this.trAmount = in.readString();
        }

        public static final Parcelable.Creator<ResultListBean> CREATOR = new Parcelable.Creator<ResultListBean>() {
            @Override
            public ResultListBean createFromParcel(Parcel source) {
                return new ResultListBean(source);
            }

            @Override
            public ResultListBean[] newArray(int size) {
                return new ResultListBean[size];
            }
        };
    }


}
