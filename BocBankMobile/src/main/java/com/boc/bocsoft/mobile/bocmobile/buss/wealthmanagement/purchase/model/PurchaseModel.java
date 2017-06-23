package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by wangtong on 2016/9/18.
 */
public class PurchaseModel implements Serializable {

    /**
     * 现钞
     */
    public static final String CODE_CASH = "01";
    /**
     * 现汇
     */
    public static final String CODE_REMIT = "02";
    /**
     * 人民币
     */
    public static final String CODE_CNY = "00";

    public static final String RESULT_YES = "1";

    public static final String CANCEL_ZERO = "0";
    public static final String CANCEL_ONE = "1";
    public static final String CANCEL_TWO = "2";

    public static final String PRODUCT_KIND_ONE = "1";

    public static final String PRODUCT_KIND_ZERO = "0";

    private final String PERIOD_LOCK_THREE = "3";

    private final String SELL_TYPE_FOUR = "04";

    public static final String REDEEM_DAY = "D";
    public static final String REDEEM_WEEK = "W";
    public static final String REDEEM_MONTH = "M";

    public static final String RISK_SUCCESS = "0";
    public static final String RISK_WARN = "1";
    public static final String RISK_FAIL = "2";

    public static final String ORDER_TIME = "0";

    //accountKey
    private String accountKey;
    //付费账户
    private String payAccountNum;
    //付费账户id
    private String payAccountId;
    //付费账户类型
    private String payAccountType;
    //付费账户开户行Id
    private String payAccountBancID;
    //付费账户状态
    private String payAccountStatus;
    //付费账户余额
    private BigDecimal payBalance;
    //付费账户余额(钞)
    private BigDecimal payBalanceCash;
    //付费账户余额(汇)
    private BigDecimal payBalanceRemit;
    //产品品质
    private String productKind;
    //产品代码
    private String prodCode;
    //产品名称
    private String prodName;
    //币种
    private String curCode;
    //钞汇标示
    private String cashRemitCode;
    //剩余额度
    private BigDecimal creditBalance;
    //认购/申购撤单设置
    private String isCanCancel;
    //购买类型 - 认购申购
    private String transTypeCode;
    //认购手续费（净值型）
    private String subscribeFee;
    //申购手续费（净值型）
    private String purchFee;
    //认申购起点金额
    private BigDecimal subAmount = new BigDecimal(0);
    private String subAmountStr;
    //追加认申购起点金额
    private BigDecimal addAmount = new BigDecimal(0);
    //购买基数
    private BigDecimal baseAmount;
    //是否允许指定日期赎回
    private String appdatered;
    //赎回开始日期
    private LocalDate redeemStartDate;
    //赎回结束日期
    private LocalDate redeemEndDate;
    //产品赎回日期
    private LocalDate redeemDate;
    //是否为业绩基准产品
    private String isLockPeriod;
    //产品期限（业绩基准）
    private String prodTimeLimit;
    //是否为周期性产品
    private boolean periodical;
    //周期性产品单价
    private String periodPrice;
    //投资期数
    private int periodNumber;
    //最大投资期数
    private int maxPeriod;
    //风险匹配结果
    private String riskMatch;
    //产品风险等级
    private String productRisk;
    //客户风险等级
    private String customerRisk;
    //风险信息
    private String riskMessage;
    //购买金额
    private BigDecimal buyAmount;
    //交易序号
    private String transNum;
    //柜员营销代码
    private String martCode;
    //赎回方式
    private String sellType;
    //赎回周期频率
    private String redEmperiodfReq;
    //赎回周期开始
    private String redEmperiodStart;
    //赎回周期结束
    private String redEmperiodEnd;
    //客户是否无投资经验买了有投资经验
    private boolean hasInvestXp;
    //是否处于挂单时间
    private String orderTime;
    //是否为周期滚续产品
    private boolean isPeriodProduct;
    //指令上送不可为空
    private String dealCode;
    // 产品风险类别	String	1：保本固定收益、 2：保本浮动收益、3：非保本浮动收益
    private String prodRiskType;
    //产品类型
    private String productType;
    // 产品期限特性
    private String productTermType;
    //产品起息日
    private String productBegin;
    //产品到期日
    private String productEnd;
    // 付息频率
    private String couponpayFreq;
    // 收益到帐日
    private String interestDate;
    private String priceDate;
    private String yearlyRR;// 预计年收益率（%）
    private String rateDetail;// 预计年收益率（%）(最大值)

    private String redEmptionStartDate;// 赎回开始日期
    private String redEmptionEndDate;// 赎回结束日期
    private String redEmptionHoliday;// 允许节假日赎回
    private String trfPrice;//预交易返回字段



    public PurchaseModel() {
    }

    public PurchaseModel(PurchaseInputMode inputModel) {
        creditBalance = inputModel.getCreditBalance();
        dealCode = inputModel.dealCode;
        accountKey = inputModel.accountKey;
        prodName = inputModel.prodName;
        prodCode = inputModel.prodCode;
        curCode = inputModel.curCode;
        if (ApplicationConst.CURRENCY_CNY.equals(curCode))
            cashRemitCode = CODE_CNY;
        else
            cashRemitCode = CODE_REMIT;

        priceDate = inputModel.priceDate;
        yearlyRR = inputModel.yearlyRR;
        rateDetail = inputModel.rateDetail;
        payAccountNum = inputModel.payAccountNum;
        payAccountId = inputModel.payAccountId;
        payAccountType = inputModel.payAccountType;
        payAccountStatus = inputModel.payAccountStatus;
        payAccountBancID = inputModel.payAccountBancID;
        productKind = inputModel.productKind;
        isCanCancel = inputModel.isCanCancle;
        transTypeCode = inputModel.transTypeCode;
        subscribeFee = inputModel.subscribeFee;
        purchFee = inputModel.purchFee;
        prodRiskType = inputModel.prodRiskType;
        productType = inputModel.productType;
        productTermType = inputModel.productTermType;
        productBegin = inputModel.productBegin;
        productEnd = inputModel.productEnd;
        maxPeriod = inputModel.maxPeriodNumber;
        this.couponpayFreq = inputModel.couponpayFreq;
        this.interestDate = inputModel.interestDate;

        if (StringUtils.isEmptyOrNull(inputModel.buyAmount))
            buyAmount = new BigDecimal(0.00);
        else
            buyAmount = new BigDecimal(inputModel.buyAmount);

        subAmountStr = inputModel.subAmount;
        if (!StringUtils.isEmptyOrNull(inputModel.subAmount))
            subAmount = new BigDecimal(inputModel.subAmount);
        else
            subAmount = new BigDecimal(0.00);

        if (!StringUtils.isEmptyOrNull(inputModel.addAmount))
            addAmount = new BigDecimal(inputModel.addAmount);
        else
            addAmount = new BigDecimal(0.00);

        if (buyAmount.doubleValue() <= 0)
            buyAmount = subAmount;

        if (subAmount.compareTo(addAmount) > 0)
            subAmount = addAmount;

        if (StringUtils.isEmptyOrNull(inputModel.baseAmount) || Double.parseDouble(inputModel.baseAmount) <= 0)
            baseAmount = new BigDecimal(1000);
        else
            baseAmount = new BigDecimal(inputModel.baseAmount);

        appdatered = inputModel.appdatered;
        isLockPeriod = inputModel.isLockPeriod;

        if (isFundProKindZero() && isLockPeriod())
            isPeriodProduct = true;
        else
            isPeriodProduct = false;

        prodTimeLimit = inputModel.prodTimeLimit;
        periodical = inputModel.periodical;
        periodPrice = inputModel.periodPrice;


        sellType = inputModel.sellType;
        redEmperiodfReq = inputModel.redEmperiodfReq;
        redEmperiodStart = inputModel.redEmperiodStart;
        redEmperiodEnd = inputModel.redEmperiodEnd;
        redEmptionHoliday = inputModel.redEmptionHoliday;
        redEmptionStartDate = inputModel.redEmptionStartDate;
        redEmptionEndDate = inputModel.redEmptionEndDate;

        if (!StringUtils.isEmptyOrNull(inputModel.redEmptionStartDate))
            redeemStartDate = LocalDate.parse(inputModel.redEmptionStartDate, DateFormatters.dateFormatter1);
        if (!StringUtils.isEmptyOrNull(inputModel.redEmptionEndDate))
            redeemEndDate = LocalDate.parse(inputModel.redEmptionEndDate, DateFormatters.dateFormatter1);

        if (REDEEM_WEEK.equals(redEmperiodfReq) && redEmperiodEnd.equals("7"))
            redEmperiodEnd = "日";
    }

    public String getSubAmountStr() {
        return MoneyUtils.getLoanAmountShownRMB1(subAmountStr, curCode);
    }

    public String getPriceDate() {
        return priceDate;
    }

    public String getYearlyRR() {
        return yearlyRR;
    }

    public String getRateDetail() {
        return rateDetail;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getDealCode() {
        return dealCode;
    }

    public int getMaxPeriod() {
        return maxPeriod;
    }

    public String getPayAccountNum() {
        return payAccountNum;
    }

    public void setPayAccountNum(String payAccountNum) {
        this.payAccountNum = payAccountNum;
    }

    public String getPayAccountId() {
        return payAccountId;
    }

    public void setPayAccountId(String payAccountId) {
        this.payAccountId = payAccountId;
    }

    public boolean isFundProKind() {
        return PRODUCT_KIND_ONE.equals(productKind);
    }

    public boolean isFundProKindZero() {
        return PRODUCT_KIND_ZERO.equals(productKind);
    }

    public String getProductKind() {
        return productKind;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getCashRemitCode() {
        return cashRemitCode;
    }

    public void setCashRemitCode(String cashRemitCode) {
        this.cashRemitCode = cashRemitCode;
    }

    public BigDecimal getCreditBalance() {
        return creditBalance == null ? new BigDecimal(0.00) : creditBalance;
    }

    public String getIsCanCancel() {
        return isCanCancel;
    }

    public String getSubscribeFee() {
        return subscribeFee;
    }

    public String getPurchFee() {
        return purchFee;
    }

    public String getFormatPurchFee() {
        purchFee = purchFee.replaceAll("\\|", "\n");
        return purchFee;
    }

    public void setPurchFee(String purchFee) {
        this.purchFee = purchFee;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public boolean isCanRedeem() {
        return RESULT_YES.equals(appdatered);
    }

    public String getAppdatered() {
        return appdatered;
    }

    public void setAppdatered(String appdatered) {
        this.appdatered = appdatered;
    }

    public LocalDate getRedeemStartDate() {
        return redeemStartDate;
    }

    public LocalDate getRedeemEndDate() {
        return redeemEndDate;
    }

    public LocalDate getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(LocalDate redeemDate) {
        this.redeemDate = redeemDate;
    }

    public boolean isLockPeriod() {
        return PERIOD_LOCK_THREE.equals(isLockPeriod);
    }

    public String getIsLockPeriod() {
        return isLockPeriod;
    }

    public void setIsLockPeriod(String isLockPeriod) {
        this.isLockPeriod = isLockPeriod;
    }

    public String getProdTimeLimit() {
        return prodTimeLimit;
    }

    public void setProdTimeLimit(String prodTimeLimit) {
        this.prodTimeLimit = prodTimeLimit;
    }

    public boolean isPeriodical() {
        return periodical;
    }

    public void setPeriodical(boolean periodical) {
        this.periodical = periodical;
    }

    public String getPeriodPrice() {
        return periodPrice;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = periodNumber;
    }

    public String getRiskMatch() {
        return riskMatch;
    }

    public void setRiskMatch(String riskMatch) {
        this.riskMatch = riskMatch;
    }

    public String getProductRisk() {
        switch (productRisk) {
            case "0":
                return "低风险产品";
            case "1":
                return "中低风险产品";
            case "2":
                return "中等风险产品";
            case "3":
                return "中高风险产品";
            case "4":
                return "高风险产品";
        }
        return "";
    }

    public void setProductRisk(String productRisk) {
        this.productRisk = productRisk;
    }

    public String getCustomerRisk() {
        switch (customerRisk) {
            case "1":
                return "保守型投资者";
            case "2":
                return "稳健型投资者";
            case "3":
                return "平衡型投资者";
            case "4":
                return "成长型投资者";
            case "5":
                return "进取型投资者";
        }
        return "";
    }

    public void setCustomerRisk(String customerRisk) {
        this.customerRisk = customerRisk;
    }

    public String getRiskMessage() {
        switch (riskMessage) {
            case "0":
                return "本理财产品有投资风险，只能保证获得合同明确承诺的收益，您应充分认识投资风险，谨慎投资";
            case "1":
                return "本理财产品有投资风险，只保障理财资金本金，不保证理财收益，您应当充分认识投资风险，谨慎投资";
            case "2":
                return "本产品为非保本浮动收益理财产品，并不保证理财资金本金和收益，投资者可能会因市场变动而蒙受不同程度的损失，投资者应充分认识投资风险，谨慎投资";
        }
        return "";
    }

    public void setRiskMessage(String riskMessage) {
        this.riskMessage = riskMessage;
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public String getTransNum() {
        return transNum;
    }

    public void setTransNum(String transNum) {
        this.transNum = transNum;
    }

    public String getMartCode() {
        return martCode;
    }

    public void setMartCode(String martCode) {
        this.martCode = martCode;
    }

    public boolean isCycleRedeem() {
        return SELL_TYPE_FOUR.equals(sellType);
    }

    public String getSellType() {
        return sellType;
    }

    public String getRedEmperiodfReq() {
        return redEmperiodfReq;
    }

    public String getRedEmperiodStart() {
        return redEmperiodStart;
    }

    public String getRedEmperiodEnd() {
        return redEmperiodEnd;
    }

    public boolean isHasInvestXp() {
        return hasInvestXp;
    }

    public void setHasInvestXp(boolean hasInvestXp) {
        this.hasInvestXp = hasInvestXp;
    }

    public boolean isOrderTime() {
        return ORDER_TIME.equals(orderTime);
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public boolean isPeriodProduct() {
        return isPeriodProduct;
    }

    public BigDecimal getPayBalance() {
        return payBalance;
    }

    public void setPayBalance(BigDecimal payBalance) {
        this.payBalance = payBalance;
    }

    public BigDecimal getPayBalanceCash() {
        return payBalanceCash == null ? new BigDecimal(0.00) : payBalanceCash;
    }

    public void setPayBalanceCash(BigDecimal payBalanceCash) {
        this.payBalanceCash = payBalanceCash;
    }

    public BigDecimal getPayBalanceRemit() {
        return payBalanceRemit == null ? new BigDecimal(0.00) : payBalanceRemit;
    }

    public void setPayBalanceRemit(BigDecimal payBalanceRemit) {
        this.payBalanceRemit = payBalanceRemit;
    }

    public BigDecimal getSubAmount() {
        return subAmount == null ? new BigDecimal(0.00) : subAmount;
    }

    public BigDecimal getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(BigDecimal addAmount) {
        this.addAmount = addAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        this.buyAmount = buyAmount;
    }

    public String getProdRiskType() {
        return prodRiskType;
    }

    public String getProductType() {
        return productType;
    }

    public String getProductTermType() {
        return productTermType;
    }

    public String getProductBegin() {
        return productBegin;
    }

    public String getProductEnd() {
        return productEnd;
    }

    public String getInterestDate() {
        return interestDate;
    }

    public String getCouponpayFreq() {
        return couponpayFreq;
    }

    public String getRedEmptionStartDate() {
        return redEmptionStartDate;
    }

    public String getRedEmptionEndDate() {
        return redEmptionEndDate;
    }

    public String getRedEmptionHoliday() {
        return redEmptionHoliday;
    }

    public String getPayAccountType() {
        return payAccountType;
    }

    public void setPayAccountType(String payAccountType) {
        this.payAccountType = payAccountType;
    }

    public String getPayAccountBancID() {
        return payAccountBancID;
    }

    public void setPayAccountBancID(String payAccountBancID) {
        this.payAccountBancID = payAccountBancID;
    }

    public String getPayAccountStatus() {
        return payAccountStatus;
    }

    public void setPayAccountStatus(String payAccountStatus) {
        this.payAccountStatus = payAccountStatus;
    }

    public String getTrfPrice() {
        return trfPrice;
    }

    public void setTrfPrice(String trfPrice) {
        this.trfPrice = trfPrice;
    }
}