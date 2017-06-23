package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 指令交易产品查询_结果
 * Created by Wan mengxin on 2016/9/26.
 */
public class PsnOcrmProductQueryResult {

    // 总笔数	String
    private String recordNumber;//

    private List<OcrmDetail> resultList = new ArrayList<>();

    // 产品查询详情
    public static class OcrmDetail {

        //	产品代码	String
        private String productCode;
        //产品名称	String
        private String productName;
        /**
         * 产品币种	String	001- 人民币元
         * 012- 英镑
         * 013- 港币元
         * 014- 美元
         * 028- 加拿大元
         * 027- 日元
         * 081-- 澳门元
         * 038- 欧元
         **/
        private String currencyCode;

        //钞汇标识	String	1 钞  2 汇
        private String charCode;
        //	交易份额	String
        private String transCount;
        //	交易金额	String
        private String transSum;

        /**
         * 交易类型	String
         * 01.对私基金认购和申购
         * 02.对私基金赎回
         * 03.基金定投购买
         * 04.基金定投赎回
         * 05.中银理财计划I产品购买交易
         * 06.中银理财计划I产品赎回交易
         * 07.中银理财计划II购买
         * 08.中银理财计划II赎回
         **/
        private String transType;

        /**
         * 定投/定赎日期	String  按月 01-28  按周 01-05
         */
        private String scheduleDate;

        /**
         * 定投/定赎方式	String	0 按月  1 按周
         **/
        private String scheduleType;

        /**
         * 结束方式	String	0：无
         * 1：指定日期
         * 2：累计赎回/购买成功次数
         * 3：累计成功赎回份额/累计成功购买金额
         **/
        private String endType;

        //结束条件内容	String
        private String endCondition;

        // 失效日期	String（yyyy-mm-dd）
        private String unvailableDate;

        //连续赎回标志	String	0 不连续赎回  1 连续赎回
        private String continueFlag;

        //是否自动续存	String
        private String isRenew;
        //买入期数	String
        private String boughtCount;

        /**
         * 申请日期	String（yyyy-mm-dd）
         **/
        private String applyDate;
        //客户姓名	String
        private String cusName;
        //理财经理姓名	String
        private String empcName;
        //基金净值	String
        private String fundNetVal;
        //指令交易后台交易ID	String
        private String dealCode;
        //可推送渠道	String
        private String transChannel;
        //产品系列编号	String
        private String serialCode;
        //是否周期性产品	String	0：是1：否
        private String isPre;
        //基础金额模式	String	0-定额 1-不定额
        private String amountType;
        //基础金额	String
        private String amount;
        //	最低预留金额	String
        private String minAmount;
        //最大扣款金额	String
        private String maxAmount;
        //	最大购买期数	String
        private String maxPeriods;
        //	产品系列名称	String
        private String serialName;

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCharCode() {
            return charCode;
        }

        public void setCharCode(String charCode) {
            this.charCode = charCode;
        }

        public String getTransCount() {
            return transCount;
        }

        public void setTransCount(String transCount) {
            this.transCount = transCount;
        }

        public String getTransSum() {
            return transSum;
        }

        public void setTransSum(String transSum) {
            this.transSum = transSum;
        }

        public String getTransType() {
            return transType;
        }

        public void setTransType(String transType) {
            this.transType = transType;
        }

        public String getScheduleDate() {
            return scheduleDate;
        }

        public void setScheduleDate(String scheduleDate) {
            this.scheduleDate = scheduleDate;
        }

        public String getScheduleType() {
            return scheduleType;
        }

        public void setScheduleType(String scheduleType) {
            this.scheduleType = scheduleType;
        }

        public String getEndType() {
            return endType;
        }

        public void setEndType(String endType) {
            this.endType = endType;
        }

        public String getEndCondition() {
            return endCondition;
        }

        public void setEndCondition(String endCondition) {
            this.endCondition = endCondition;
        }

        public String getUnvailableDate() {
            return unvailableDate;
        }

        public void setUnvailableDate(String unvailableDate) {
            this.unvailableDate = unvailableDate;
        }

        public String getContinueFlag() {
            return continueFlag;
        }

        public void setContinueFlag(String continueFlag) {
            this.continueFlag = continueFlag;
        }

        public String getIsRenew() {
            return isRenew;
        }

        public void setIsRenew(String isRenew) {
            this.isRenew = isRenew;
        }

        public String getBoughtCount() {
            return boughtCount;
        }

        public void setBoughtCount(String boughtCount) {
            this.boughtCount = boughtCount;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public String getCusName() {
            return cusName;
        }

        public void setCusName(String cusName) {
            this.cusName = cusName;
        }

        public String getEmpcName() {
            return empcName;
        }

        public void setEmpcName(String empcName) {
            this.empcName = empcName;
        }

        public String getFundNetVal() {
            return fundNetVal;
        }

        public void setFundNetVal(String fundNetVal) {
            this.fundNetVal = fundNetVal;
        }

        public String getDealCode() {
            return dealCode;
        }

        public void setDealCode(String dealCode) {
            this.dealCode = dealCode;
        }

        public String getTransChannel() {
            return transChannel;
        }

        public void setTransChannel(String transChannel) {
            this.transChannel = transChannel;
        }

        public String getSerialCode() {
            return serialCode;
        }

        public void setSerialCode(String serialCode) {
            this.serialCode = serialCode;
        }

        public String getIsPre() {
            return isPre;
        }

        public void setIsPre(String isPre) {
            this.isPre = isPre;
        }

        public String getAmountType() {
            return amountType;
        }

        public void setAmountType(String amountType) {
            this.amountType = amountType;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(String minAmount) {
            this.minAmount = minAmount;
        }

        public String getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(String maxAmount) {
            this.maxAmount = maxAmount;
        }

        public String getMaxPeriods() {
            return maxPeriods;
        }

        public void setMaxPeriods(String maxPeriods) {
            this.maxPeriods = maxPeriods;
        }

        public String getSerialName() {
            return serialName;
        }

        public void setSerialName(String serialName) {
            this.serialName = serialName;
        }
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<OcrmDetail> getResultList() {
        return resultList;
    }

    public void setResultList(List<OcrmDetail> resultList) {
        this.resultList = resultList;
    }
}
