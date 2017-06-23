package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay;

import java.math.BigDecimal;

/**
 * 登录前基金详情查询 返回结果
 * Created by lxw on 2016/8/16 0016.
 */
public class PsnFundDetailQueryOutlayResult {


    /**
     * currency : 001
     * sign : 1
     * endDate : null
     * fundCode : 380001
     * fundName : 中行普通1
     * fundCompanyName : 国泰基金管理有限公司
     * isBuy : Y
     * gIsBuy : Y
     * isInvt : Y
     * gIsInvt : Y
     * netPrice : null
     * fundCompanyCode : 50400000
     * cashFlag : CAS
     * feeType : 1
     * totalCount : null
     * dayLimit : 20000
     * chargeRate : 1
     * fntKind : 01
     * isShortFund : Y
     * indiDayMaxSumBuy : 100000
     * indiDayMaxSumRedeem : 100000
     * fundState : 0
     * orderLowLimit : 0
     * applyLowLimit : 0
     * scheduleApplyLowLimit : 0
     * sellLowLimit : 10
     * holdLowCount : 100
     * fundSetDate : 2011/01/01
     * fundInfoMdfDate : 2011/01/01
     * fundToMod : 1
     * convertFlag : ??ˉ
     * canBuy : true
     * canScheduleBuy : true
     * risklv : 3
     * fntype : 05
     * defaultBonus : 2
     * isSale : Y
     * isChangeIn : Y
     * isChangeOut : Y
     * isBonusMod : Y
     * gIsSale : Y
     * gIsChgOut : Y
     * gIsChgIn : Y
     * gIsBonusMod : Y
     * addUpNetVal : null
     * fundIncomeUnit : null
     * fundIncomeRatio : null
     * dayIncomeRatio : null
     * conversionIn : YY
     * conversionOut : YY
     * isDate : null
     * isModSale : Y
     * isDelSale : Y
     * alwRdptDat : null
     * discount : null
     * isQuickSale : N
     * quickSaleUpLimit : 99999999999999
     * quickSaleLowLimit : 0
     * holdQutyLowLimit : 0
     * perLimit : 10000
     * dayNumLimit : 10
     * isZisTby : 2
     * ebankTransFlag : true
     * isFloatProfit : Y
     * isZisInvt : 0
     * isZisSale : 0
     * isaddSale : Y
     * iSAddSale : Y
     * buyAddLowLmt : 0
     */




    /*private String currency;
    private String sign;
    private Object endDate;
    private String fundCode;
    private String fundName;
    private String fundCompanyName;
    private String isBuy;
    private String gIsBuy;
    private String isInvt;
    private String gIsInvt;
    private BigDecimal netPrice;
    private String fundCompanyCode;
    private String cashFlag;
    private String feeType;
    private Object totalCount;
    private String dayLimit;
    private String chargeRate;
    private String fntKind;
    private String isShortFund;
    private String indiDayMaxSumBuy;
    private String indiDayMaxSumRedeem;
    private String fundState;
    private BigDecimal orderLowLimit;
    private int applyLowLimit;
    private int scheduleApplyLowLimit;
    private int sellLowLimit;
    private int holdLowCount;
    private String fundSetDate;
    private String fundInfoMdfDate;
    private String fundToMod;
    private String convertFlag;
    private boolean canBuy;
    private boolean canScheduleBuy;
    private String risklv;
    private String fntype;
    private String defaultBonus;
    private String isSale;
    private String isChangeIn;
    private String isChangeOut;
    private String isBonusMod;
    private String gIsSale;
    private String gIsChgOut;
    private String gIsChgIn;
    private String gIsBonusMod;
    private BigDecimal addUpNetVal;
    private BigDecimal fundIncomeUnit;
    private BigDecimal fundIncomeRatio;
    private BigDecimal dayIncomeRatio;
    private String conversionIn;
    private String conversionOut;
    private Object isDate;
    private String isModSale;
    private String isDelSale;
    private Object alwRdptDat;
    private Object discount;
    private String isQuickSale;
    private String quickSaleUpLimit;
    private String quickSaleLowLimit;
    private String holdQutyLowLimit;
    private String perLimit;
    private String dayNumLimit;
    private String isZisTby;
    private boolean ebankTransFlag;
    private String isFloatProfit;
    private String isZisInvt;
    private String isZisSale;
    private String isaddSale;
    private String iSAddSale;
    private String buyAddLowLmt;*/


    private String currency					   ; //  币种	String
    private String fundCode					   ; //  基金代码	String
    private String fundName					   ; //  基金名称	String
    private String fundState					   ; //  基金状态	String
    private String feeType						   ; //  收费方式	String
    private String fundCompanyName		   ; //  基金公司名称	String
    private String totalCount				   ; //  基金总份额	String
    private BigDecimal sellLowLimit			   ; //  赎回下限	BigDecimal
    private BigDecimal holdLowCount			   ; //  最低持有份额	BigDecimal
    private String sign							   ; //  是否需要电子签名	String	0：不需要 1：券商电子合同 2：中登电子合同
    private String fundCompanyCode		   ; //  基金公司代码	String
    private BigDecimal orderLowLimit			   ; //  个人认购最低金额	BigDecimal   首次认购下限
    private BigDecimal applyLowLimit			   ; //  个人申购最低金额	BigDecimal
    private BigDecimal scheduleApplyLowLimit;	//定期定额申购下限金额	BigDecimal
    private String fundSetDate						;//基金设立日期	String
    private String fundInfoMdfDate				;//基金信息更改日期	String
    private String fundToMod							;//转托管模式	String
    private String convertFlag						;//是否可转换（判断基金转换是否展示链接使用）	String	汉字“是/否”
    private boolean ebankTransFlag				;//网银交易标识	boolean
    private boolean canBuy								;//是否可买入	boolean	ture/false
    private String isBuy									;//是否可认购	String
    private String isInvt								;//是否可申购	String
    private String gIsBuy								;//是否可挂单认购	String
    private String gIsInvt								;//是否可挂单申购	String
    private boolean canScheduleBuy				;//是否可定期定额申购申请	boolean
    private String isModDT								;//是否可定期定额申购修改	String	Y：是  N：否
    private String isDelDT								;//是否可定期定额申购撤销	String	Y：是  N：否
    private String risklv								;//风险级别	String	1：保守型（低风险） 2：稳健型（中低风险）3：平衡型（中风险）4：成长型（中高风险）5：进取型（高风险
    private String fntype								;//产品类型	String	基金产品类别  01：理财型基金 02：QDII基金 03：ETF基金 04：保本型基金 05：指数型基金 06：货币型基金 07：股票型基金 08：债券型基金 09：混合型基金 10：其他基金
    private String isFloatProfit					;//是否展现浮动盈亏	String	Y：是  N：否
    private String isZisInvt 						;//是否允许指定申购	String	0：不允许 1：允许指定任意日期申购 2：允许指定固定日期申购
    private String isZisSale 						;//是否允许指定赎回	String	0：不允许 1：允许指定任意日期赎回 2：允许指定固定日期赎回
    private String isZisTby							;//是否允许指定日期认购	String	0：不允许 1：允许指定任意日期认购 2：允许指定固定日期认购
    private String isAddSale 						;//是否允许定期定额赎回申请	String	Y：允许  N：不允许
    private String isModSale 						;//是否允许定期定额赎回修改	String	Y：允许  N：不允许
    private String isDelSale 						;//是否允许定期定额赎回撤销	String	Y：允许  N：不允许
    private String cashFlag							;//钞汇标示	String	CAS代表钞TRN代表汇
    private String conversionIn					;//是否可转入（详情页面使用）	String	Y：允许 N:不允许
    private String conversionOut					;//是否可转出（详情页面使用）	String	Y：允许 N:不允许
    private String isSale								;//是否允许赎回	String	Y：允许 N:不允许
    private String isChangeOut						;//是否可转出	String	Y：允许 N:不允许
    private String alwRdptDat						;//可赎回日期	String	短期理财产品显示
    private String chargeRate						;//手续费率	String
    private String discount							;//优惠信息	String
    private BigDecimal netPrice							;//基金净值	BigDecimal
    private BigDecimal dayIncomeRatio				;//日净值增长率(%)	String	若返回值为0.05，则前端显示为0.05%
    private BigDecimal fundIncomeUnit				;//每万份基金单位收益	String
    private BigDecimal fundIncomeRatio				;//年收益率/净值增长率(%)	String	若返回值为0.05，则前端显示为0.05%
    private String endDate								;//净值截至日期	String
    private String addUpNetVal						;//累计净值	String
    //private BigDecimal orderLowLimit					;//首次认购下限	String
    private String buyAddLowLmt					;//追加认购下限	String
    private String defaultBonus					;//默认分红方式	String
    private String isQuickSale						;//是否允许快速赎回	String	Y:允许N:不允许 用来判读是否支持快速赎回
    private String quickSaleUpLimit			;//对私快速赎回上限	String
    private String quickSaleLowLimit			;//对私快速赎回下限	String
    private String holdQutyLowLimit			;//对私快速赎回最低持有份额	String	用来判断最低持有份额
    private String perLimit							;//单人单笔额度	String
    private String dayLimit							;//单人单天额度	String
    private String dayNumLimit						;//单人单天笔数	String
    private String isShortFund						;//是否控制理财型基金交易(短期理财)	String	Y：是 N：否
    private String indiDayMaxSumBuy			;//个人当日累计购买最大金额	String
    private String indiDayMaxSumRedeem		;//个人当日累计赎回最大份额	String
  /**
   * 	产品种类	String	01：开放式基金产品 02：一对多产品 03：券商产品
   04：信托产品
   05：银行理财
   06：保险
   07：资管计划产品

   其中（
   02：一对多
   03：券商
   07：资管计划产品
   均展示为“资管计划产品”
   ）
   */
    private String fntKind							 ; //
    private String sevenDayYield					;//七日年化收益率	String	若返回值为0.05，则前端显示为5%
  /**
   * 是否允许修改分红方式	String	Y：是
   N：否
   备：此字段非601新增字段，原有接口漏写补充
   gIsBonusMod	是否允许挂单修改分红方式	String	Y：是
   N：否
   备：此字段非601新增字段，原有接口漏写补充
   */
  private String isBonusMod						;//

    /**
     是否允许挂单修改分红方式
     Y：是
     N：否
     备：此字段非601新增字段，原有接口漏写补充
     */
    private String gIsBonusMod;


    // --- 没有的

    private String isChangeIn;
    private String gIsSale;
    private String gIsChgOut;
    private String gIsChgIn;
    private Object isDate;


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundCompanyName() {
        return fundCompanyName;
    }

    public void setFundCompanyName(String fundCompanyName) {
        this.fundCompanyName = fundCompanyName;
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }

    public String getGIsBuy() {
        return gIsBuy;
    }

    public void setGIsBuy(String gIsBuy) {
        this.gIsBuy = gIsBuy;
    }

    public String getIsInvt() {
        return isInvt;
    }

    public void setIsInvt(String isInvt) {
        this.isInvt = isInvt;
    }

    public String getGIsInvt() {
        return gIsInvt;
    }

    public void setGIsInvt(String gIsInvt) {
        this.gIsInvt = gIsInvt;
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public String getFundCompanyCode() {
        return fundCompanyCode;
    }

    public void setFundCompanyCode(String fundCompanyCode) {
        this.fundCompanyCode = fundCompanyCode;
    }

    public String getCashFlag() {
        return cashFlag;
    }

    public void setCashFlag(String cashFlag) {
        this.cashFlag = cashFlag;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Object getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(String dayLimit) {
        this.dayLimit = dayLimit;
    }

    public String getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(String chargeRate) {
        this.chargeRate = chargeRate;
    }

    public String getFntKind() {
        return fntKind;
    }

    public void setFntKind(String fntKind) {
        this.fntKind = fntKind;
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

    public String getFundState() {
        return fundState;
    }

    public void setFundState(String fundState) {
        this.fundState = fundState;
    }

    public BigDecimal getOrderLowLimit() {
        return orderLowLimit;
    }

    public void setOrderLowLimit(BigDecimal orderLowLimit) {
        this.orderLowLimit = orderLowLimit;
    }

    public BigDecimal getApplyLowLimit() {
        return applyLowLimit;
    }

    public void setApplyLowLimit(BigDecimal applyLowLimit) {
        this.applyLowLimit = applyLowLimit;
    }

    public BigDecimal getScheduleApplyLowLimit() {
        return scheduleApplyLowLimit;
    }

    public void setScheduleApplyLowLimit(BigDecimal scheduleApplyLowLimit) {
        this.scheduleApplyLowLimit = scheduleApplyLowLimit;
    }

    public BigDecimal getSellLowLimit() {
        return sellLowLimit;
    }

    public void setSellLowLimit(BigDecimal sellLowLimit) {
        this.sellLowLimit = sellLowLimit;
    }

    public BigDecimal getHoldLowCount() {
        return holdLowCount;
    }

    public void setHoldLowCount(BigDecimal holdLowCount) {
        this.holdLowCount = holdLowCount;
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

    public boolean isCanBuy() {
        return canBuy;
    }

    public void setCanBuy(boolean canBuy) {
        this.canBuy = canBuy;
    }

    public boolean isCanScheduleBuy() {
        return canScheduleBuy;
    }

    public void setCanScheduleBuy(boolean canScheduleBuy) {
        this.canScheduleBuy = canScheduleBuy;
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

    public String getDefaultBonus() {
        return defaultBonus;
    }

    public void setDefaultBonus(String defaultBonus) {
        this.defaultBonus = defaultBonus;
    }

    public String getIsSale() {
        return isSale;
    }

    public void setIsSale(String isSale) {
        this.isSale = isSale;
    }

    public String getIsChangeIn() {
        return isChangeIn;
    }

    public void setIsChangeIn(String isChangeIn) {
        this.isChangeIn = isChangeIn;
    }

    public String getIsChangeOut() {
        return isChangeOut;
    }

    public void setIsChangeOut(String isChangeOut) {
        this.isChangeOut = isChangeOut;
    }

    public String getIsBonusMod() {
        return isBonusMod;
    }

    public void setIsBonusMod(String isBonusMod) {
        this.isBonusMod = isBonusMod;
    }

    public String getGIsSale() {
        return gIsSale;
    }

    public void setGIsSale(String gIsSale) {
        this.gIsSale = gIsSale;
    }

    public String getGIsChgOut() {
        return gIsChgOut;
    }

    public void setGIsChgOut(String gIsChgOut) {
        this.gIsChgOut = gIsChgOut;
    }

    public String getGIsChgIn() {
        return gIsChgIn;
    }

    public void setGIsChgIn(String gIsChgIn) {
        this.gIsChgIn = gIsChgIn;
    }

    public String getGIsBonusMod() {
        return gIsBonusMod;
    }

    public void setGIsBonusMod(String gIsBonusMod) {
        this.gIsBonusMod = gIsBonusMod;
    }

    public String getAddUpNetVal() {
        return addUpNetVal;
    }

    public void setAddUpNetVal(String addUpNetVal) {
        this.addUpNetVal = addUpNetVal;
    }

    public BigDecimal getFundIncomeUnit() {
        return fundIncomeUnit;
    }

    public void setFundIncomeUnit(BigDecimal fundIncomeUnit) {
        this.fundIncomeUnit = fundIncomeUnit;
    }

    public BigDecimal getFundIncomeRatio() {
        return fundIncomeRatio;
    }

    public void setFundIncomeRatio(BigDecimal fundIncomeRatio) {
        this.fundIncomeRatio = fundIncomeRatio;
    }

    public BigDecimal getDayIncomeRatio() {
        return dayIncomeRatio;
    }

    public void setDayIncomeRatio(BigDecimal dayIncomeRatio) {
        this.dayIncomeRatio = dayIncomeRatio;
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

    public Object getIsDate() {
        return isDate;
    }

    public void setIsDate(Object isDate) {
        this.isDate = isDate;
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

    public Object getAlwRdptDat() {
        return alwRdptDat;
    }

    public void setAlwRdptDat(String alwRdptDat) {
        this.alwRdptDat = alwRdptDat;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
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

    public String getDayNumLimit() {
        return dayNumLimit;
    }

    public void setDayNumLimit(String dayNumLimit) {
        this.dayNumLimit = dayNumLimit;
    }

    public String getIsZisTby() {
        return isZisTby;
    }

    public void setIsZisTby(String isZisTby) {
        this.isZisTby = isZisTby;
    }

    public boolean isEbankTransFlag() {
        return ebankTransFlag;
    }

    public void setEbankTransFlag(boolean ebankTransFlag) {
        this.ebankTransFlag = ebankTransFlag;
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


    public String getBuyAddLowLmt() {
        return buyAddLowLmt;
    }

    public void setBuyAddLowLmt(String buyAddLowLmt) {
        this.buyAddLowLmt = buyAddLowLmt;
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

    public String getIsAddSale() {
        return isAddSale;
    }

    public void setIsAddSale(String isAddSale) {
        this.isAddSale = isAddSale;
    }

    public String getSevenDayYield() {
        return sevenDayYield;
    }

    public void setSevenDayYield(String sevenDayYield) {
        this.sevenDayYield = sevenDayYield;
    }

    public String getgIsSale() {
        return gIsSale;
    }

    public void setgIsSale(String gIsSale) {
        this.gIsSale = gIsSale;
    }

    public String getgIsChgOut() {
        return gIsChgOut;
    }

    public void setgIsChgOut(String gIsChgOut) {
        this.gIsChgOut = gIsChgOut;
    }

    public String getgIsChgIn() {
        return gIsChgIn;
    }

    public void setgIsChgIn(String gIsChgIn) {
        this.gIsChgIn = gIsChgIn;
    }

    public String getgIsBonusMod() {
        return gIsBonusMod;
    }

    public void setgIsBonusMod(String gIsBonusMod) {
        this.gIsBonusMod = gIsBonusMod;
    }

}
