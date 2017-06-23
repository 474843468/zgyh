package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail;

import java.math.BigDecimal;

/**
 * 4.9 009 PsnGetFundDetail基金基本信息查询（查询基金详情）
 */
public class PsnGetFundDetailResult {

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
  private String risklv;
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
  private String fntype;
  private String isFloatProfit;//是否展现浮动盈亏		Y：是  N：否
  /**
   * 0：不允许
   * 1：允许指定任意日期申购
   * 2：允许指定固定日期申购
   */
  private String isZisInvt;
  /**
   * Y：允许
   * N：不允许
   * 0：不允许
   * 1：允许指定任意日期赎回
   * 2：允许指定固定日期赎回
   */
  private String isZisSale;
  /**
   * 0：不允许
   * 1：允许指定任意日期认购
   * 2：允许指定固定日期认购
   */
  private String isZisTby;
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
  private String fntKind;

  private String sevenDayYield;// 七日年化收益率	String	（净值表） 若返回值为0.05，则前端显示为5%
  private String isBonusMod;// 是否允许修改分红方式	String	Y：是 N：否 备：此字段非601新增字段，原有接口漏写补充
  private String gIsBonusMod;// 是否允许挂单修改分红方式	String	Y：是N：否 备：此字段非601新增字段，原有接口漏写补充

  public String getCurrency() {
    return currency;
  }

  public String getFundCode() {
    return fundCode;
  }

  public String getFundName() {
    return fundName;
  }

  public String getFundState() {
    return fundState;
  }

  public String getFeeType() {
    return feeType;
  }

  public String getFundCompanyName() {
    return fundCompanyName;
  }

  public String getNetPrice() {
    return netPrice;
  }

  public String getTotalCount() {
    return totalCount;
  }

  public String getSellLowLimit() {
    return sellLowLimit;
  }

  public String getHoldLowCount() {
    return holdLowCount;
  }

  public String getSign() {
    return sign;
  }

  public String getFundCompanyCode() {
    return fundCompanyCode;
  }

  public String getOrderLowLimit() {
    return orderLowLimit;
  }

  public String getApplyLowLimit() {
    return applyLowLimit;
  }

  public String getScheduleApplyLowLimit() {
    return scheduleApplyLowLimit;
  }

  public String getFundSetDate() {
    return fundSetDate;
  }

  public String getFundInfoMdfDate() {
    return fundInfoMdfDate;
  }

  public String getFundToMod() {
    return fundToMod;
  }

  public String getConvertFlag() {
    return convertFlag;
  }

  public String getEbankTransFlag() {
    return ebankTransFlag;
  }

  public String getCanBuy() {
    return canBuy;
  }

  public String getIsBuy() {
    return isBuy;
  }

  public String getIsInvt() {
    return isInvt;
  }

  public String getgIsBuy() {
    return gIsBuy;
  }

  public String getgIsInvt() {
    return gIsInvt;
  }

  public String getCanScheduleBuy() {
    return canScheduleBuy;
  }

  public String getIsModDT() {
    return isModDT;
  }

  public String getIsDelDT() {
    return isDelDT;
  }

  public String getRisklv() {
    return risklv;
  }

  public String getFntype() {
    return fntype;
  }

  public String getIsFloatProfit() {
    return isFloatProfit;
  }

  public String getIsZisInvt() {
    return isZisInvt;
  }

  public String getIsZisSale() {
    return isZisSale;
  }

  public String getIsZisTby() {
    return isZisTby;
  }

  public String getIsAddSale() {
    return isAddSale;
  }

  public String getIsModSale() {
    return isModSale;
  }

  public String getIsDelSale() {
    return isDelSale;
  }

  public String getCashFlag() {
    return cashFlag;
  }

  public String getConversionIn() {
    return conversionIn;
  }

  public String getConversionOut() {
    return conversionOut;
  }

  public String getIsSale() {
    return isSale;
  }

  public String getIsChangeOut() {
    return isChangeOut;
  }

  public String getAlwRdptDat() {
    return alwRdptDat;
  }

  public String getChargeRate() {
    return chargeRate;
  }

  public String getDiscount() {
    return discount;
  }

  public String getDayIncomeRatio() {
    return dayIncomeRatio;
  }

  public String getFundIncomeUnit() {
    return fundIncomeUnit;
  }

  public String getFundIncomeRatio() {
    return fundIncomeRatio;
  }

  public String getEndDate() {
    return endDate;
  }

  public String getBuyAddLowLmt() {
    return buyAddLowLmt;
  }

  public String getDefaultBonus() {
    return defaultBonus;
  }

  public String getAddUpNetVal() {
    return addUpNetVal;
  }

  public String getIsQuickSale() {
    return isQuickSale;
  }

  public String getQuickSaleUpLimit() {
    return quickSaleUpLimit;
  }

  public String getQuickSaleLowLimit() {
    return quickSaleLowLimit;
  }

  public String getHoldQutyLowLimit() {
    return holdQutyLowLimit;
  }

  public String getPerLimit() {
    return perLimit;
  }

  public String getDayLimit() {
    return dayLimit;
  }

  public String getDayNumLimit() {
    return dayNumLimit;
  }

  public String getIsShortFund() {
    return isShortFund;
  }

  public String getIndiDayMaxSumBuy() {
    return indiDayMaxSumBuy;
  }

  public String getIndiDayMaxSumRedeem() {
    return indiDayMaxSumRedeem;
  }

  public String getFntKind() {
    return fntKind;
  }

  public String getSevenDayYield() {
    return sevenDayYield;
  }

  public String getIsBonusMod() {
    return isBonusMod;
  }

  public String getgIsBonusMod() {
    return gIsBonusMod;
  }

  public void setDayIncomeRatio(BigDecimal dayIncomeRatio) {
    //this.dayIncomeRatio = dayIncomeRatio;
  }

  public void setFundIncomeRatio(String fundIncomeRatio) {
    this.fundIncomeRatio = fundIncomeRatio;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }
  
  public void setNetPrice(String netPrice) {
    this.netPrice = netPrice;
  }
}
