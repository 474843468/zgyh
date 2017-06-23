package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryGeneralInfo;

import java.util.List;

/**
 * 信用卡综合信息查询
 * Created by wangf on 2016/11/22.
 */
public class PsnCrcdQueryGeneralInfoResult {

    //开户行
    private String acctBank;
    //户名
    private String acctName;
    //信用卡卡号
    private String acctNum;
    //年费减免情况 - “1”免年费 “0”不免年费
    private String annualFee;
    //账单日
    private String billDate;
    //卡有效截至日期
    private String carAvaiDate;
    //主副卡标识 - “1”主卡 “0”附卡
    private String carFlag;
    //卡状态
    private String carStatus;
    //本期到期还款日 - 无账单时返回空
    private String dueDate;
    //产品名称
    private String productName;
    //启用日期
    private String startDate;
    //年费减免结束日期 - 格式yyyy-MM-dd。如annualFee为“1”，且该字段返回年份为2500，表示永久免年费
    private String waiveMemFeeEndDate;

    private List<ActListBean> actList;

    public String getAcctBank() {
        return acctBank;
    }

    public void setAcctBank(String acctBank) {
        this.acctBank = acctBank;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(String annualFee) {
        this.annualFee = annualFee;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getCarAvaiDate() {
        return carAvaiDate;
    }

    public void setCarAvaiDate(String carAvaiDate) {
        this.carAvaiDate = carAvaiDate;
    }

    public String getCarFlag() {
        return carFlag;
    }

    public void setCarFlag(String carFlag) {
        this.carFlag = carFlag;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getWaiveMemFeeEndDate() {
        return waiveMemFeeEndDate;
    }

    public void setWaiveMemFeeEndDate(String waiveMemFeeEndDate) {
        this.waiveMemFeeEndDate = waiveMemFeeEndDate;
    }

    public List<ActListBean> getActList() {
        return actList;
    }

    public void setActList(List<ActListBean> actList) {
        this.actList = actList;
    }

    public static class ActListBean {
        //本期账单金额
        private String billAmout;
        //本期最低还款金额
        private String billLimitAmout;
        //取现可用额
        private String cashBalance;
        //取现额度
        private String cashLimit;
        //币种
        private String currency;
        //分期可用额
        private String dividedPayAvaiBalance;
        //分期额度
        private String dividedPayLimit;
        //本期未还款金额
        private String haveNotRepayAmout;
        //实时余额 - 该字段值为非负数，由rtBalanceFlag字段判断是存款还是欠款或者余额0
        private String realTimeBalance;
        //实时余额标志位 - “0”-欠款 “1”-存款 “2”-余额0
        private String rtBalanceFlag;
        //整体可用额
        private String toltalBalance;
        //整体额度 - 该字段用于页面“信用卡额度”
        private String totalLimt;

        public String getBillAmout() {
            return billAmout;
        }

        public void setBillAmout(String billAmout) {
            this.billAmout = billAmout;
        }

        public String getBillLimitAmout() {
            return billLimitAmout;
        }

        public void setBillLimitAmout(String billLimitAmout) {
            this.billLimitAmout = billLimitAmout;
        }

        public String getCashBalance() {
            return cashBalance;
        }

        public void setCashBalance(String cashBalance) {
            this.cashBalance = cashBalance;
        }

        public String getCashLimit() {
            return cashLimit;
        }

        public void setCashLimit(String cashLimit) {
            this.cashLimit = cashLimit;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDividedPayAvaiBalance() {
            return dividedPayAvaiBalance;
        }

        public void setDividedPayAvaiBalance(String dividedPayAvaiBalance) {
            this.dividedPayAvaiBalance = dividedPayAvaiBalance;
        }

        public String getDividedPayLimit() {
            return dividedPayLimit;
        }

        public void setDividedPayLimit(String dividedPayLimit) {
            this.dividedPayLimit = dividedPayLimit;
        }

        public String getHaveNotRepayAmout() {
            return haveNotRepayAmout;
        }

        public void setHaveNotRepayAmout(String haveNotRepayAmout) {
            this.haveNotRepayAmout = haveNotRepayAmout;
        }

        public String getRealTimeBalance() {
            return realTimeBalance;
        }

        public void setRealTimeBalance(String realTimeBalance) {
            this.realTimeBalance = realTimeBalance;
        }

        public String getRtBalanceFlag() {
            return rtBalanceFlag;
        }

        public void setRtBalanceFlag(String rtBalanceFlag) {
            this.rtBalanceFlag = rtBalanceFlag;
        }

        public String getToltalBalance() {
            return toltalBalance;
        }

        public void setToltalBalance(String toltalBalance) {
            this.toltalBalance = toltalBalance;
        }

        public String getTotalLimt() {
            return totalLimt;
        }

        public void setTotalLimt(String totalLimt) {
            this.totalLimt = totalLimt;
        }
    }
}
