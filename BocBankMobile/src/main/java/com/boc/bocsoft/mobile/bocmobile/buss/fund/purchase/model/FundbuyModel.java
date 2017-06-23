package com.boc.bocsoft.mobile.bocmobile.buss.fund.purchase.model;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zcy7065 on 2016/11/23.
 */
public class FundbuyModel {



    /**
     * I41 002 查询账户风险评估等级
     * */
    private boolean isEvaluated;//是否做过风险评估
    private String userRiskLevel;//用户风险等级
    private String evaluationDate;//评估日期
    /**
     * I41 009 基金基本信息查询
     * */
    private String currency;//币种	String
    private String fundCode;//基金代码	String
    private String fundName;//基金名称	String
    private String fundState;//基金状态	String
    private String feeType;//收费方式	String
    private String fundCompanyName;//基金公司名称	String
    private String netPrice;//基金净值	BigDecimal 	String
    private String totalCount;//基金总份额	String
    private String sellLowLimit;//赎回下限	BigDecimal
    private String holdLowCount;//最低持有份额	BigDecimal
    private String sign;//是否需要电子签名	String	Y需要 N不需要
    private String fundCompanyCode;//基金公司代码	String
    private String orderLowLimit;//个人认购最低金额	BigDecimal //首次认购下限	String
    private String applyLowLimit;//个人申购最低金额	BigDecimal
    private String scheduleApplyLowLimit;//定期定额申购下限金额	BigDecimal
    private String fundSetDate;//基金设立日期	String
    private String fundInfoMdfDate;//基金信息更改日期	String
    private String fundToMod;//转托管模式	String
    private String convertFlag;//是否可转换（判断基金转换是否展示链接使用）	String	汉字是/否
    private String ebankTransFlag;//网银交易标识	boolean
    private String canBuy;//是否可买入	boolean	ture/false
    private String isBuy;//是否可认购	String
    private String isInvt;//是否可申购	String
    private String gIsBuy;//是否可挂单认购	String
    private String gIsInvt;//是否可挂单申购	String
    private String canScheduleBuy;//是否可定期定额申购申请	boolean
    private String isModDT;//是否可定期定额申购修改	String	Y：是  N：否
    private String isDelDT;//是否可定期定额申购撤销	String	Y：是  N：否
    /**
     * 1：保守型
     * （低风险）
     * 2：稳健型
     * （中低风险）
     * 3：平衡型
     * （中风险）
     * 4：成长型
     * （中高风险）
     * 5：进取型
     * （高风险
     */
    private String risklv;//风险等级
    /**
     * 基金产品类别
     * 基金产品类别
     * 07：股票基金
     * 08：债券基金
     * 06：货币基金
     * 02：QDII基金
     *
     * 01：理财基金
     * 03：ETF基金
     * 04：保本基金
     * 05：指数基金
     * 09：混合基金
     * 10：其他基金
     */
    private String fntype;//基金产品类型
    private String isFloatProfit;//是否展现浮动盈亏		Y：是  N：否
    /**
     * 0：不允许
     * 1：允许指定任意日期申购
     * 2：允许指定固定日期申购
     */
    private String isZisInvt;//是否允许指定申购
    /**
     * Y：允许
     * N：不允许
     * 0：不允许
     * 1：允许指定任意日期赎回
     * 2：允许指定固定日期赎回
     */
    private String isZisSale;//是否允许指定赎回
    /**
     * 0：不允许
     * 1：允许指定任意日期认购
     * 2：允许指定固定日期认购
     */
    private String isZisTby;//是否允许指定日期认购
    private String isAddSale;//是否允许定期定额赎回申请		Y：允许  N：不允许
    private String isModSale;//是否允许定期定额赎回修改		Y：允许  N：不允许
    private String isDelSale;//是否允许定期定额赎回撤销		Y：允许  N：不允许
    private String cashFlag;//钞汇标示		CAS代表钞 TRN代表汇
    private String conversionIn;//是否可转入（详情页面使用）	String	Y：允许N:不允许
    private String conversionOut;//是否可转出（详情页面使用）	String	Y：允许N:不允许
    private String isSale;//是否允许赎回	String	Y：允许N:不允许
    private String isChangeOut;//是否可转出	String	Y：允许N:不允许
    private String alwRdptDat;//可赎回日期	Date	短期理财产品显示
    private String chargeRate;//手续费率	String
    private String discount;//优惠信息	String
    private String dayIncomeRatio;//日净值增长率	String	若返回值为0.05，则前端显示为0.05%
    private String fundIncomeUnit;//每万份基金单位收益	String
    private String fundIncomeRatio;//年收益率/净值增长率	String	若返回值为0.05，则前端显示为0.05%
    private String endDate;//净值截至日期	String
    private String buyAddLowLmt;//追加认购下限	String
    private String defaultBonus;//默认分红方式	String
    private String addUpNetVal;//累计净值	String
    private String isQuickSale;//是否允许快速赎回	String	Y:允许N:不允许用来判读是否支持快速赎回
    private String quickSaleUpLimit;//对私快速赎回上限	String
    private String quickSaleLowLimit;//对私快速赎回下限	String
    private String holdQutyLowLimit;//对私快速赎回最低持有份额	String	用来判断最低持有份额
    private String perLimit;//单人单笔额度	String
    private String dayLimit;//单人单天额度	String
    private String dayNumLimit;//单人单天笔数	String
    private String isShortFund;//是否控制理财型基金交易(短期理财)		Y：是N：否
    private String indiDayMaxSumBuy;//个人当日累计购买最大金额
    private String indiDayMaxSumRedeem;//个人当日累计赎回最大份额
    /**
     * 01：开放式基金产品
     * 02：一对多产品
     * 03：券商产品
     * 04：信托产品
     * 05：银行理财
     * 06：保险
     * 07：资管计划产品
     *
     * 其中（
     * 02：一对多
     * 03：券商
     * 07：资管计划产品
     * 均展示为“资管计
     * 划产品”
     * ）
     */
    private String fntKind;//
    private String sevenDayYield;// 七日年化收益率	String	（净值表） 若返回值为0.05，则前端显示为5%
    private String isBonusMod;// 是否允许修改分红方式	String	Y：是 N：否 备：此字段非601新增字段，原有接口漏写补充
    private String gIsBonusMod;// 是否允许挂单修改分红方式	String	Y：是N：否 备：此字段非601新增字段，原有接口漏写补充
    private  String isHK;//是否是香港基金

    /**
     * I41 024&025 基金买入&挂单接口
     * */
    private String assignedDate;//指定日期
    private String buyAmount;//买入份数
    //private String fundCode;//基金代码
    private String affirmFlag;//客户是否确认风险标示
    private String token;//防重标示
    private String executeType;
    private String isSignedFundContract;//是否签署电子签名合同
    private String fundSeq;//基金交易流水号
    private String isSignedFundEval;//是否做过风险评估
    private String tranState;//交易状态 E136
    private String isMatchEval;//风险评估结果是否匹配
    private String isSignedFundStipulation;//是否签署电子签名约定书
    private String consignSeq;//
    private String transactionId;//交易流水号

    /**
     * I41 063 可交易日期查询接口
     * */
//  private String fundCode;//基金代码
    private String appointFlag;//指定日期交易标识 0：购买 1：赎回
    private List<String> dealDatelist;//可交易日期列表

    /**
     * I41 061 基金公司信息查询
     * */
//  private String fundCode;//基金代码
    private String companyName;//基金管理公司名称
    private String companyPhone;//客户服务电话

    /**
     * I00 001查询投资交易账号绑定信息
     * */
    //投资交易类型, 外汇：10；黄金：11；基金：12；国债：13；XPAD（中银理财计划I）：23；贵金属代理：26
    private String invtType;
    private String accountId; //资金账户ID
    private String accountType; //资金账户类型
    private String investAccount; //投资交易账户
    private String account; //资金账号
    private String accountNickName; //资金账号别名
    private String bankId; //网银机构号
    private String ibkNum; //省行联行号

    /**
     * I05 003 查询账户详情
     * */
   // private String accountId; //账户标识
    private String accOpenBank;
   // private String accountType;
    private String accountStatus;
    private String accOpenDate;

    private List<AccountDetaiListBean> accountDetaiList;

    public static class AccountDetaiListBean {
        private String currencyCode;
        private String cashRemit;
        private BigDecimal bookBalance;
        private BigDecimal availableBalance;
        private String volumeNumber;
        private String type;
        private String interestRate;
        private String status;
        private BigDecimal monthBalance;
        private String cdNumber;
        private String cdPeriod;
        private LocalDate openDate;
        private LocalDate interestStartsDate;
        private LocalDate interestEndDate;
        private LocalDate settlementDate;
        private String convertType;
        private String pingNo;
        private String holdAmount;
        private String appointStatus;

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public BigDecimal getBookBalance() {
            return bookBalance;
        }

        public void setBookBalance(BigDecimal bookBalance) {
            this.bookBalance = bookBalance;
        }

        public BigDecimal getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(BigDecimal availableBalance) {
            this.availableBalance = availableBalance;
        }

        public String getVolumeNumber() {
            return volumeNumber;
        }

        public void setVolumeNumber(String volumeNumber) {
            this.volumeNumber = volumeNumber;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getInterestRate() {
            return interestRate;
        }

        public void setInterestRate(String interestRate) {
            this.interestRate = interestRate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public BigDecimal getMonthBalance() {
            return monthBalance;
        }

        public void setMonthBalance(BigDecimal monthBalance) {
            this.monthBalance = monthBalance;
        }

        public String getCdNumber() {
            return cdNumber;
        }

        public void setCdNumber(String cdNumber) {
            this.cdNumber = cdNumber;
        }

        public String getCdPeriod() {
            return cdPeriod;
        }

        public void setCdPeriod(String cdPeriod) {
            this.cdPeriod = cdPeriod;
        }

        public LocalDate getOpenDate() {
            return openDate;
        }

        public void setOpenDate(LocalDate openDate) {
            this.openDate = openDate;
        }

        public LocalDate getInterestStartsDate() {
            return interestStartsDate;
        }

        public void setInterestStartsDate(LocalDate interestStartsDate) {
            this.interestStartsDate = interestStartsDate;
        }

        public LocalDate getInterestEndDate() {
            return interestEndDate;
        }

        public void setInterestEndDate(LocalDate interestEndDate) {
            this.interestEndDate = interestEndDate;
        }

        public LocalDate getSettlementDate() {
            return settlementDate;
        }

        public void setSettlementDate(LocalDate settlementDate) {
            this.settlementDate = settlementDate;
        }

        public String getConvertType() {
            return convertType;
        }

        public void setConvertType(String convertType) {
            this.convertType = convertType;
        }

        public String getPingNo() {
            return pingNo;
        }

        public void setPingNo(String pingNo) {
            this.pingNo = pingNo;
        }

        public String getHoldAmount() {
            return holdAmount;
        }

        public void setHoldAmount(String holdAmount) {
            this.holdAmount = holdAmount;
        }

        public String getAppointStatus() {
            return appointStatus;
        }

        public void setAppointStatus(String appointStatus) {
            this.appointStatus = appointStatus;
        }
    }

    public String getAccOpenDate() {
        return accOpenDate;
    }

    public void setAccOpenDate(String accOpenDate) {
        this.accOpenDate = accOpenDate;
    }

    public String getAccOpenBank() {
        return accOpenBank;
    }

    public void setAccOpenBank(String accOpenBank) {
        this.accOpenBank = accOpenBank;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public List<AccountDetaiListBean> getAccountDetaiList() {
        return accountDetaiList;
    }

    public void setAccountDetaiList(List<AccountDetaiListBean> accountDetaiList) {
        this.accountDetaiList = accountDetaiList;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundState() {
        return fundState;
    }

    public void setFundState(String fundState) {
        this.fundState = fundState;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFundCompanyName() {
        return fundCompanyName;
    }

    public void setFundCompanyName(String fundCompanyName) {
        this.fundCompanyName = fundCompanyName;
    }

    public String getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(String netPrice) {
        this.netPrice = netPrice;
    }

    public String getSellLowLimit() {
        return sellLowLimit;
    }

    public void setSellLowLimit(String sellLowLimit) {
        this.sellLowLimit = sellLowLimit;
    }

    public String getHoldLowCount() {
        return holdLowCount;
    }

    public void setHoldLowCount(String holdLowCount) {
        this.holdLowCount = holdLowCount;
    }

    public String getFundCompanyCode() {
        return fundCompanyCode;
    }

    public void setFundCompanyCode(String fundCompanyCode) {
        this.fundCompanyCode = fundCompanyCode;
    }

    public String getOrderLowLimit() {
        return orderLowLimit;
    }

    public void setOrderLowLimit(String orderLowLimit) {
        this.orderLowLimit = orderLowLimit;
    }

    public String getApplyLowLimit() {
        return applyLowLimit;
    }

    public void setApplyLowLimit(String applyLowLimit) {
        this.applyLowLimit = applyLowLimit;
    }

    public String getScheduleApplyLowLimit() {
        return scheduleApplyLowLimit;
    }

    public void setScheduleApplyLowLimit(String scheduleApplyLowLimit) {
        this.scheduleApplyLowLimit = scheduleApplyLowLimit;
    }

    public String getFundSetDate() {
        return fundSetDate;
    }

    public void setFundSetDate(String fundSetDate) {
        this.fundSetDate = fundSetDate;
    }

    public String getFundInfoMdfDate() {
        return fundInfoMdfDate;
    }

    public void setFundInfoMdfDate(String fundInfoMdfDate) {
        this.fundInfoMdfDate = fundInfoMdfDate;
    }

    public String getFundToMod() {
        return fundToMod;
    }

    public void setFundToMod(String fundToMod) {
        this.fundToMod = fundToMod;
    }

    public String getConvertFlag() {
        return convertFlag;
    }

    public void setConvertFlag(String convertFlag) {
        this.convertFlag = convertFlag;
    }

    public String getEbankTransFlag() {
        return ebankTransFlag;
    }

    public void setEbankTransFlag(String ebankTransFlag) {
        this.ebankTransFlag = ebankTransFlag;
    }

    public String getCanBuy() {
        return canBuy;
    }

    public void setCanBuy(String canBuy) {
        this.canBuy = canBuy;
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }

    public String getIsInvt() {
        return isInvt;
    }

    public void setIsInvt(String isInvt) {
        this.isInvt = isInvt;
    }

    public String getgIsBuy() {
        return gIsBuy;
    }

    public void setgIsBuy(String gIsBuy) {
        this.gIsBuy = gIsBuy;
    }

    public String getgIsInvt() {
        return gIsInvt;
    }

    public void setgIsInvt(String gIsInvt) {
        this.gIsInvt = gIsInvt;
    }

    public String getCanScheduleBuy() {
        return canScheduleBuy;
    }

    public void setCanScheduleBuy(String canScheduleBuy) {
        this.canScheduleBuy = canScheduleBuy;
    }

    public String getIsModDT() {
        return isModDT;
    }

    public void setIsModDT(String isModDT) {
        this.isModDT = isModDT;
    }

    public String getIsDelDT() {
        return isDelDT;
    }

    public void setIsDelDT(String isDelDT) {
        this.isDelDT = isDelDT;
    }

    public String getRisklv() {
        return risklv;
    }

    public void setRisklv(String risklv) {
        this.risklv = risklv;
    }

    public String getFntype() {
        return fntype;
    }

    public void setFntype(String fntype) {
        this.fntype = fntype;
    }

    public String getIsFloatProfit() {
        return isFloatProfit;
    }

    public void setIsFloatProfit(String isFloatProfit) {
        this.isFloatProfit = isFloatProfit;
    }

    public String getIsZisInvt() {
        return isZisInvt;
    }

    public void setIsZisInvt(String isZisInvt) {
        this.isZisInvt = isZisInvt;
    }

    public String getIsZisSale() {
        return isZisSale;
    }

    public void setIsZisSale(String isZisSale) {
        this.isZisSale = isZisSale;
    }

    public String getIsZisTby() {
        return isZisTby;
    }

    public void setIsZisTby(String isZisTby) {
        this.isZisTby = isZisTby;
    }

    public String getIsAddSale() {
        return isAddSale;
    }

    public void setIsAddSale(String isAddSale) {
        this.isAddSale = isAddSale;
    }

    public String getIsModSale() {
        return isModSale;
    }

    public void setIsModSale(String isModSale) {
        this.isModSale = isModSale;
    }

    public String getIsDelSale() {
        return isDelSale;
    }

    public void setIsDelSale(String isDelSale) {
        this.isDelSale = isDelSale;
    }

    public String getCashFlag() {
        return cashFlag;
    }

    public void setCashFlag(String cashFlag) {
        this.cashFlag = cashFlag;
    }

    public String getConversionIn() {
        return conversionIn;
    }

    public void setConversionIn(String conversionIn) {
        this.conversionIn = conversionIn;
    }

    public String getConversionOut() {
        return conversionOut;
    }

    public void setConversionOut(String conversionOut) {
        this.conversionOut = conversionOut;
    }

    public String getIsSale() {
        return isSale;
    }

    public void setIsSale(String isSale) {
        this.isSale = isSale;
    }

    public String getIsChangeOut() {
        return isChangeOut;
    }

    public void setIsChangeOut(String isChangeOut) {
        this.isChangeOut = isChangeOut;
    }

    public String getAlwRdptDat() {
        return alwRdptDat;
    }

    public void setAlwRdptDat(String alwRdptDat) {
        this.alwRdptDat = alwRdptDat;
    }

    public String getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(String chargeRate) {
        this.chargeRate = chargeRate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDayIncomeRatio() {
        return dayIncomeRatio;
    }

    public void setDayIncomeRatio(String dayIncomeRatio) {
        this.dayIncomeRatio = dayIncomeRatio;
    }

    public String getFundIncomeUnit() {
        return fundIncomeUnit;
    }

    public void setFundIncomeUnit(String fundIncomeUnit) {
        this.fundIncomeUnit = fundIncomeUnit;
    }

    public String getFundIncomeRatio() {
        return fundIncomeRatio;
    }

    public void setFundIncomeRatio(String fundIncomeRatio) {
        this.fundIncomeRatio = fundIncomeRatio;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBuyAddLowLmt() {
        return buyAddLowLmt;
    }

    public void setBuyAddLowLmt(String buyAddLowLmt) {
        this.buyAddLowLmt = buyAddLowLmt;
    }

    public String getDefaultBonus() {
        return defaultBonus;
    }

    public void setDefaultBonus(String defaultBonus) {
        this.defaultBonus = defaultBonus;
    }

    public String getAddUpNetVal() {
        return addUpNetVal;
    }

    public void setAddUpNetVal(String addUpNetVal) {
        this.addUpNetVal = addUpNetVal;
    }

    public String getIsQuickSale() {
        return isQuickSale;
    }

    public void setIsQuickSale(String isQuickSale) {
        this.isQuickSale = isQuickSale;
    }

    public String getQuickSaleUpLimit() {
        return quickSaleUpLimit;
    }

    public void setQuickSaleUpLimit(String quickSaleUpLimit) {
        this.quickSaleUpLimit = quickSaleUpLimit;
    }

    public String getQuickSaleLowLimit() {
        return quickSaleLowLimit;
    }

    public void setQuickSaleLowLimit(String quickSaleLowLimit) {
        this.quickSaleLowLimit = quickSaleLowLimit;
    }

    public String getHoldQutyLowLimit() {
        return holdQutyLowLimit;
    }

    public void setHoldQutyLowLimit(String holdQutyLowLimit) {
        this.holdQutyLowLimit = holdQutyLowLimit;
    }

    public String getPerLimit() {
        return perLimit;
    }

    public void setPerLimit(String perLimit) {
        this.perLimit = perLimit;
    }

    public String getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(String dayLimit) {
        this.dayLimit = dayLimit;
    }

    public String getDayNumLimit() {
        return dayNumLimit;
    }

    public void setDayNumLimit(String dayNumLimit) {
        this.dayNumLimit = dayNumLimit;
    }

    public String getIsShortFund() {
        return isShortFund;
    }

    public void setIsShortFund(String isShortFund) {
        this.isShortFund = isShortFund;
    }

    public String getIndiDayMaxSumBuy() {
        return indiDayMaxSumBuy;
    }

    public void setIndiDayMaxSumBuy(String indiDayMaxSumBuy) {
        this.indiDayMaxSumBuy = indiDayMaxSumBuy;
    }

    public String getIndiDayMaxSumRedeem() {
        return indiDayMaxSumRedeem;
    }

    public void setIndiDayMaxSumRedeem(String indiDayMaxSumRedeem) {
        this.indiDayMaxSumRedeem = indiDayMaxSumRedeem;
    }

    public String getFntKind() {
        return fntKind;
    }

    public void setFntKind(String fntKind) {
        this.fntKind = fntKind;
    }

    public String getSevenDayYield() {
        return sevenDayYield;
    }

    public void setSevenDayYield(String sevenDayYield) {
        this.sevenDayYield = sevenDayYield;
    }

    public String getIsBonusMod() {
        return isBonusMod;
    }

    public void setIsBonusMod(String isBonusMod) {
        this.isBonusMod = isBonusMod;
    }

    public String getgIsBonusMod() {
        return gIsBonusMod;
    }

    public void setgIsBonusMod(String gIsBonusMod) {
        this.gIsBonusMod = gIsBonusMod;
    }



    public boolean isEvaluated() {
        return isEvaluated;
    }

    public void setEvaluated(boolean evaluated) {
        isEvaluated = evaluated;
    }

    public String getUserRiskLevel() {
        return userRiskLevel;
    }

    public void setUserRiskLevel(String riskLevel) {
        this.userRiskLevel = riskLevel;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }



    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getInvestAccount() {
        return investAccount;
    }

    public void setInvestAccount(String investAccount) {
        this.investAccount = investAccount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountNickName() {
        return accountNickName;
    }

    public void setAccountNickName(String accountNickName) {
        this.accountNickName = accountNickName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getIbkNum() {
        return ibkNum;
    }

    public void setIbkNum(String ibkNum) {
        this.ibkNum = ibkNum;
    }
    public String getInvtType() {
        return invtType;
    }

    public void setInvtType(String invtType) {
        this.invtType = invtType;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }


    public List<String> getDealDatelist() {
        return dealDatelist;
    }

    public void setDealDatelist(List<String> list) {
        this.dealDatelist = list;
    }


    public void setAppointFlag(String appointFlag) {
        this.appointFlag = appointFlag;
    }

    public String getAppointFlag() {
        return appointFlag;
    }

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    public void setBuyAmount(String buyAmount) {
        this.buyAmount = buyAmount;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public void setAffirmFlag(String affirmFlag) {
        this.affirmFlag = affirmFlag;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public String getBuyAmount() {
        return buyAmount;
    }

    public String getFundCode() {
        return fundCode;
    }

    public String getAffirmFlag() {
        return affirmFlag;
    }


    public String getToken() {
        return token;
    }

    public void setIsSignedFundContract(String isSignedFundContract) {
        this.isSignedFundContract = isSignedFundContract;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }


    public void setIsSignedFundEval(String isSignedFundEval) {
        this.isSignedFundEval = isSignedFundEval;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }

    public void setIsMatchEval(String isMatchEval) {
        this.isMatchEval = isMatchEval;
    }

    public void setIsSignedFundStipulation(String isSignedFundStipulation) {
        this.isSignedFundStipulation = isSignedFundStipulation;
    }

    public void setConsignSeq(String consignSeq) {
        this.consignSeq = consignSeq;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getIsSignedFundContract() {
        return isSignedFundContract;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public String getIsSignedFundEval() {
        return isSignedFundEval;
    }

    public String getTranState() {
        return tranState;
    }

    public String getIsMatchEval() {
        return isMatchEval;
    }

    public String getIsSignedFundStipulation() {
        return isSignedFundStipulation;
    }

    public String getConsignSeq() {
        return consignSeq;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setEvaluationDate(String evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getEvaluationDate() {
        return evaluationDate;
    }

    public String getIsHK() {
        return isHK;
    }

    public void setIsHK(String isHK) {
        this.isHK = isHK;
    }
}
