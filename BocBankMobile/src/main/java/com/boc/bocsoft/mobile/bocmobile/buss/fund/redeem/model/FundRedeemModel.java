package com.boc.bocsoft.mobile.bocmobile.buss.fund.redeem.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zcy7065 on 2016/12/5.
 */
public class FundRedeemModel {

    /**
     * I41 026 基金卖出 & 027 基金挂单卖出
     * */
    //上送
    private String assignedDate;//指定日期
    private String executeType;//交易类型 0：立即执行  1：指定日期执行
    //private String fundCode;//基金代码
    private String sellAmount;//赎回份额
    //private String feeType;//收费方式  1：前收 2：后收
    private String fundSellFlag;//连续赎回（顺延赎回） 0：取消赎回 1：顺延赎回
    private String token;
    private String conversationId;
    //下传
    private String isSignedFundContract;
    private String fundSeq;
    private String affirmFlag;
    private String isSignedFundEval;
    private String tranState;//交易状态  1：成功
    private String isMatchEval;
    private String isSignedFundStipulation;
    private String consignSeq;//挂单或实时交易中的预交易时后台返回的交易序号
    private String transactionId;//交易流水号

    /**
     * I41 015 PsnFundQueryFundBalance 查询基金持仓信息
     * */
    private String totalAvailableBalance;

    /**
     * 061 基金公司信息查询
     * */
    private String companyName;//基金管理公司名称
    private String companyPhone;//客户服务电话

    /**
     * I41 063 可交易日期查询接口
     * */
//  private String fundCode;//基金代码
    private String appointFlag;//指定日期交易标识 0：购买 1：赎回

    /**
     * I00 001查询投资交易账号绑定信息
     * */
    //投资交易类型, 外汇：10；黄金：11；基金：12；国债：13；XPAD（中银理财计划I）：23；贵金属代理：26
    private String invtType;//投资交易类型
    private String accountId; //资金账户ID
    private String accountType; //资金账户类型
    private String investAccount; //投资交易账户
    private String account; //资金账号
    private String accountNickName; //资金账号别名
    private String bankId; //网银机构号
    private String ibkNum; //省行联行号

    /**
     * I41 051 PsnFundQuickSell 基金快速赎回
     * */
    //private int fincSellAmount;//赎回份额
    //private String fundCode;//基金代码
    //private String token;

    //private String fundCode;//基金代码
    //private String fundSeq;//基金交易流水号
    //private String sellAmount;//赎回份额
    //private String fundName;//基金名称
    //private String transactionId;//交易流水号

    /**
     * I41 064 PsnQuickSellQuotaQuery 基金快速赎回额度查询
     * */
    //private String dayLimit;//单人单天额度
    private String dayCompleteShare;//当日已快速赎回份额
    private String totalLimit;//产品总额度
    private String dayCompleteNum;//当日已快速赎回额度
    private String totalBalance;//当日总余额
    private String perDealNum;//单人单天笔数
    private String dayFrozenLimit;//当日冻结总余额
    private String dayQuickSellNum;//当日可快速赎回笔数
    //private String perLimit;//单人单笔额度
    private String dayQuickSellLimit;//当天可快速赎回额度

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
    private String isHK;//是否是香港基金
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

    public String getIsHK() {
        return isHK;
    }

    public void setIsHK(String isHK) {
        this.isHK = isHK;
    }

    public String getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(String netPrice) {
        this.netPrice = netPrice;
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

    public String getFundCompanyName() {
        return fundCompanyName;
    }

    public void setFundCompanyName(String fundCompanyName) {
        this.fundCompanyName = fundCompanyName;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getFundCompanyCode() {
        return fundCompanyCode;
    }

    public void setFundCompanyCode(String fundCompanyCode) {
        this.fundCompanyCode = fundCompanyCode;
    }

    public String getTotalAvailableBalance() {
        return totalAvailableBalance;
    }

    public void setTotalAvailableBalance(String totalAvailableBalance) {
        this.totalAvailableBalance = totalAvailableBalance;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getInvtType() {
        return invtType;
    }

    public void setInvtType(String invtType) {
        this.invtType = invtType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public List<String> getDealDatelist() {
        return dealDatelist;
    }

    public void setDealDatelist(List<String> dealDatelist) {
        this.dealDatelist = dealDatelist;
    }

    public String getAppointFlag() {
        return appointFlag;
    }

    public void setAppointFlag(String appointFlag) {
        this.appointFlag = appointFlag;
    }

    private List<String> dealDatelist;//可交易日期列表





    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(String sellAmount) {
        this.sellAmount = sellAmount;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFundSellFlag() {
        return fundSellFlag;
    }

    public void setFundSellFlag(String fundSellFlag) {
        this.fundSellFlag = fundSellFlag;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getIsSignedFundContract() {
        return isSignedFundContract;
    }

    public void setIsSignedFundContract(String isSignedFundContract) {
        this.isSignedFundContract = isSignedFundContract;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public String getAffirmFlag() {
        return affirmFlag;
    }

    public void setAffirmFlag(String affirmFlag) {
        this.affirmFlag = affirmFlag;
    }

    public String getIsSignedFundEval() {
        return isSignedFundEval;
    }

    public void setIsSignedFundEval(String isSignedFundEval) {
        this.isSignedFundEval = isSignedFundEval;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }

    public String getIsMatchEval() {
        return isMatchEval;
    }

    public void setIsMatchEval(String isMatchEval) {
        this.isMatchEval = isMatchEval;
    }

    public String getIsSignedFundStipulation() {
        return isSignedFundStipulation;
    }

    public void setIsSignedFundStipulation(String isSignedFundStipulation) {
        this.isSignedFundStipulation = isSignedFundStipulation;
    }

    public String getConsignSeq() {
        return consignSeq;
    }

    public void setConsignSeq(String consignSeq) {
        this.consignSeq = consignSeq;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getDayCompleteShare() {
        return dayCompleteShare;
    }

    public void setDayCompleteShare(String dayCompleteShare) {
        this.dayCompleteShare = dayCompleteShare;
    }

    public String getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(String totalLimit) {
        this.totalLimit = totalLimit;
    }

    public String getDayCompleteNum() {
        return dayCompleteNum;
    }

    public void setDayCompleteNum(String dayCompleteNum) {
        this.dayCompleteNum = dayCompleteNum;
    }

    public String getPerDealNum() {
        return perDealNum;
    }

    public void setPerDealNum(String perDealNum) {
        this.perDealNum = perDealNum;
    }

    public String getDayFrozenLimit() {
        return dayFrozenLimit;
    }

    public void setDayFrozenLimit(String dayFrozenLimit) {
        this.dayFrozenLimit = dayFrozenLimit;
    }

    public String getDayQuickSellNum() {
        return dayQuickSellNum;
    }

    public void setDayQuickSellNum(String dayQuickSellNum) {
        this.dayQuickSellNum = dayQuickSellNum;
    }

    public String getDayQuickSellLimit() {
        return dayQuickSellLimit;
    }

    public void setDayQuickSellLimit(String dayQuickSellLimit) {
        this.dayQuickSellLimit = dayQuickSellLimit;
    }
}
