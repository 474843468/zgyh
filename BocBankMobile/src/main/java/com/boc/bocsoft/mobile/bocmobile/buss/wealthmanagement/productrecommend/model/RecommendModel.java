package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品推荐数据模型
 * Created by Wan mengxin on 2016/9/26.
 */
public class RecommendModel implements Serializable {

    /**
     * 请求上送字段
     */

    //交易分类	String
    private String protpye;
    //交易类型	String
    private String tradeType;
    //	页面大小	String
    private String pageSize;
    //当前页索引	String
    private String currentIndex;

    /**
     * true：重新查询结果(在交易改变查询条件时是需要重新查询的,
     * currentIndex需上送0)
     * false:不需要重新查询，使用缓存中的结果	String
     **/
    private String _refresh;


    /**
     * 返回的数据
     */

    //会话id
    private String conversationId;
    //tokenid
    private String tokenId;
    // 总笔数	String
    private String recordNumber;//

    // 产品详情查询集合
    private List<OcrmDetail> resultList;

    public List<OcrmDetail> getResultList() {
        return resultList;
    }

    public void setResultList(List<OcrmDetail> resultList) {
        this.resultList = resultList;
    }

    public String getProtpye() {
        return protpye;
    }

    public void setProtpye(String protpye) {
        this.protpye = protpye;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    /**
     * 推荐产品查询详情  PsnOcrmProductQuery
     */
    public static class OcrmDetail implements Serializable {

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

    /**
     * 36 查询用户持仓详情 psnXpadProductBalanceQuery
     */
    public balanceMsg balanceMsg = new balanceMsg();

    private List<balanceMsg> balanceList = new ArrayList();

    public List<RecommendModel.balanceMsg> getBalanceList() {
        return balanceList;
    }

    public void setBalanceList(List<RecommendModel.balanceMsg> balanceList) {
        this.balanceList = balanceList;
    }

    public static class balanceMsg implements Serializable {
        /**
         * 赎回价格
         */
        private String sellPrice;
        /**
         * 客户理财账户
         */
        private String xpadAccount;
        /**
         * 资金账号
         */
        private String bancAccount;
        /**
         * 产品代码
         */
        private String prodCode;
        /**
         * 产品名称
         */
        private String prodName;
        /**
         * 产品币种
         * 001：人民币元
         * 014：美元
         * 012：英镑
         * 013：港币
         * 028: 加拿大元
         * 029：澳元
         * 038：欧元
         * 027：日元
         */
        private String curCode;
        /**
         * 预计年收益率（%）
         */
        private String yearlyRR;
        /**
         * 产品起息日
         */
        private String prodBegin;
        /**
         * 产品到期日
         * 无限开放式产品返回“-1”表示“无限期”
         */
        private String prodEnd;
        /**
         * 持有份额
         */
        private String holdingQuantity;
        /**
         * 可用份额
         */
        private String availableQuantity;
        /**
         * 是否可赎回
         * 0：是
         * 1：否
         */
        private String canRedeem;
        /**
         * 是否允许部分赎回
         * 0：是
         * 1：否
         */
        private String canPartlyRedeem;
        /**
         * 是否可修改分红方式
         * 0：否
         * 1：是
         */
        private String canChangeBonusMode;
        /**
         * 当前分红方式
         * 0：红利再投资、
         * 1：现金分红
         */
        private String currentBonusMode;
        /**
         * 最低持有份额
         */
        private String lowestHoldQuantity;
        /**
         * 赎回起点金额
         */
        private String redeemStartingAmount;
        /**
         * 钞汇标识
         * 01：钞
         * 02：汇
         * 00：人民币
         */
        private String cashRemit;
        /**
         * 是否收益累计产品
         * 0：否
         * 1：是
         */
        private String progressionflag;
        /**
         * 资金账号缓存标识
         */
        private String bancAccountKey;
        /**
         *
         */
        private String productKind;
        /**
         * 参考收益
         */
        private String expProfit;
        /**
         * 单位净值
         */
        private String price;
        /**
         * 净值日期
         */
        private String priceDate;
        /**
         * 参考市值
         */
        private String expAmt;
        /**
         * 产品期限特性
         * 00-有限期封闭式
         * 01-有限期半开放式
         * 02-周期连续
         * 03-周期不连续
         * 04-无限开放式
         * 05-春夏秋冬
         * 06-其它
         */
        private String termType;
        /**
         * 是否可追加购买
         */
        private String canAddBuy;
        /**
         * 是否为业绩基准产品
         * 0：非业绩基准产品
         * 1：业绩基准-锁定期转低收益
         * 2：业绩基准-锁定期后入账
         * 3：业绩基准-锁定期周期滚续
         * 业绩基准产品允许查看份额明细
         */
        private String standardPro;
        /**
         * 是否可投资协议管理
         * 0：不允许
         * 1：允许
         */
        private String canAgreementMange;
        /**
         * 产品期限
         * 固定期限类产品有效
         */
        private String productTerm;
        /**
         *
         */
        private String canAssignDate;
        /**
         *
         */
        private String shareValue;
        /**
         * 当前期数
         */
        private String currPeriod;
        /**
         * 总期数
         */
        private String totalPeriod;
        /**
         * 是否可份额转换	String	业绩基准类产品有效0：是 1：否
         */
        private String canQuantityExchange;
        /**
         * 预计年收益率（最大值）	String	不带%号，如果不为0，与yearlyRR字段组成区间
         */
        private String yearlyRRMax;
        /**
         * 交易流水号
         */
        private String tranSeq;
        /**
         *
         */
        private String issueType;

        public void setSellPrice(String sellPrice) {
            this.sellPrice = sellPrice;
        }

        public String getSellPrice() {
            return this.sellPrice;
        }

        public void setXpadAccount(String xpadAccount) {
            this.xpadAccount = xpadAccount;
        }

        public String getXpadAccount() {
            return this.xpadAccount;
        }

        public void setBancAccount(String bancAccount) {
            this.bancAccount = bancAccount;
        }

        public String getBancAccount() {
            return this.bancAccount;
        }

        public void setProdCode(String prodCode) {
            this.prodCode = prodCode;
        }

        public String getProdCode() {
            return this.prodCode;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public String getProdName() {
            return this.prodName;
        }

        public void setCurCode(String curCode) {
            this.curCode = curCode;
        }

        public String getCurCode() {
            return this.curCode;
        }

        public void setYearlyRR(String yearlyRR) {
            this.yearlyRR = yearlyRR;
        }

        public String getYearlyRR() {
            return this.yearlyRR;
        }

        public void setProdBegin(String prodBegin) {
            this.prodBegin = prodBegin;
        }

        public String getProdBegin() {
            return this.prodBegin;
        }

        public void setProdEnd(String prodEnd) {
            this.prodEnd = prodEnd;
        }

        public String getProdEnd() {
            return this.prodEnd;
        }

        public void setHoldingQuantity(String holdingQuantity) {
            this.holdingQuantity = holdingQuantity;
        }

        public String getHoldingQuantity() {
            return this.holdingQuantity;
        }

        public void setAvailableQuantity(String availableQuantity) {
            this.availableQuantity = availableQuantity;
        }

        public String getAvailableQuantity() {
            return this.availableQuantity;
        }

        public void setCanRedeem(String canRedeem) {
            this.canRedeem = canRedeem;
        }

        public String getCanRedeem() {
            return this.canRedeem;
        }

        public void setCanPartlyRedeem(String canPartlyRedeem) {
            this.canPartlyRedeem = canPartlyRedeem;
        }

        public String getCanPartlyRedeem() {
            return this.canPartlyRedeem;
        }

        public void setCanChangeBonusMode(String canChangeBonusMode) {
            this.canChangeBonusMode = canChangeBonusMode;
        }

        public String getCanChangeBonusMode() {
            return this.canChangeBonusMode;
        }

        public void setCurrentBonusMode(String currentBonusMode) {
            this.currentBonusMode = currentBonusMode;
        }

        public String getCurrentBonusMode() {
            return this.currentBonusMode;
        }

        public void setLowestHoldQuantity(String lowestHoldQuantity) {
            this.lowestHoldQuantity = lowestHoldQuantity;
        }

        public String getLowestHoldQuantity() {
            return this.lowestHoldQuantity;
        }

        public void setRedeemStartingAmount(String redeemStartingAmount) {
            this.redeemStartingAmount = redeemStartingAmount;
        }

        public String getRedeemStartingAmount() {
            return this.redeemStartingAmount;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public String getCashRemit() {
            return this.cashRemit;
        }

        public void setProgressionflag(String progressionflag) {
            this.progressionflag = progressionflag;
        }

        public String getProgressionflag() {
            return this.progressionflag;
        }

        public void setBancAccountKey(String bancAccountKey) {
            this.bancAccountKey = bancAccountKey;
        }

        public String getBancAccountKey() {
            return this.bancAccountKey;
        }

        public void setProductKind(String productKind) {
            this.productKind = productKind;
        }

        public String getProductKind() {
            return this.productKind;
        }

        public void setExpProfit(String expProfit) {
            this.expProfit = expProfit;
        }

        public String getExpProfit() {
            return this.expProfit;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPrice() {
            return this.price;
        }

        public void setPriceDate(String priceDate) {
            this.priceDate = priceDate;
        }

        public String getPriceDate() {
            return this.priceDate;
        }

        public void setExpAmt(String expAmt) {
            this.expAmt = expAmt;
        }

        public String getExpAmt() {
            return this.expAmt;
        }

        public void setTermType(String termType) {
            this.termType = termType;
        }

        public String getTermType() {
            return this.termType;
        }

        public void setCanAddBuy(String canAddBuy) {
            this.canAddBuy = canAddBuy;
        }

        public String getCanAddBuy() {
            return this.canAddBuy;
        }

        public void setStandardPro(String standardPro) {
            this.standardPro = standardPro;
        }

        public String getStandardPro() {
            return this.standardPro;
        }

        public void setCanAgreementMange(String canAgreementMange) {
            this.canAgreementMange = canAgreementMange;
        }

        public String getCanAgreementMange() {
            return this.canAgreementMange;
        }

        public void setProductTerm(String productTerm) {
            this.productTerm = productTerm;
        }

        public String getProductTerm() {
            return this.productTerm;
        }

        public void setCanAssignDate(String canAssignDate) {
            this.canAssignDate = canAssignDate;
        }

        public String getCanAssignDate() {
            return this.canAssignDate;
        }

        public void setShareValue(String shareValue) {
            this.shareValue = shareValue;
        }

        public String getShareValue() {
            return this.shareValue;
        }

        public void setCurrPeriod(String currPeriod) {
            this.currPeriod = currPeriod;
        }

        public String getCurrPeriod() {
            return this.currPeriod;
        }

        public void setTotalPeriod(String totalPeriod) {
            this.totalPeriod = totalPeriod;
        }

        public String getTotalPeriod() {
            return this.totalPeriod;
        }

        public void setCanQuantityExchange(String canQuantityExchange) {
            this.canQuantityExchange = canQuantityExchange;
        }

        public String getCanQuantityExchange() {
            return this.canQuantityExchange;
        }

        public void setYearlyRRMax(String yearlyRRMax) {
            this.yearlyRRMax = yearlyRRMax;
        }

        public String getYearlyRRMax() {
            return this.yearlyRRMax;
        }

        public void setTranSeq(String tranSeq) {
            this.tranSeq = tranSeq;
        }

        public String getTranSeq() {
            return this.tranSeq;
        }

        public void setIssueType(String issueType) {
            this.issueType = issueType;
        }

        public String getIssueType() {
            return this.issueType;
        }
    }

    //查询借口状态标志
    private boolean balanceQueryResult;//持仓信息查询

    public boolean isBalanceQueryResult() {
        return balanceQueryResult;
    }

    public void setBalanceQueryResult(boolean balanceQueryResult) {
        this.balanceQueryResult = balanceQueryResult;
    }
}
