package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model;

/**
 * 投资 - >优选投资itemVO
 * Created by dingeryue on 2016年09月06.
 */
public class InvestItemVo {

  private boolean isSuccess = true;//当前接口是否请求成功
  private boolean isRefrensh = false;//是否刷新
  private boolean isLoadNoLoginApi = false;
  private boolean isLoadLoginApi = false;

  private String productType;//产品类型	0基金，1理财产品
  private String productName;//产品名称
  private String productCode;//产品代码
  private String rateValue;//预计年收益率 	仅理财产品才有百分比

  private String minMoney;//理财 起购金额	人民币元
  private String productNature;//产品性质	0：结构性，1类基金

  private String price;//净值
  private String priceDate;//净值日期

  private String availamt;//剩余额度

  private String timeLimit;//期限


  //理财  - 风险等级  保本与否  预计年化收益率 单位净值 产品期限

  private String prodRisklvl; //产品风险级别	String	0：低风险产品1：中低风险产品2：中等风险产品3：中高风险产品4：高风险产品
  private String prodRiskType; //产品风险类别	String	1：保本固定收益、2：保本浮动收益、3：非保本浮动收益

  //基金   - 基金类型(股票型 货币型)
  //股票型  - 日增长率  最新净值 申购？ 起购
  //货币型 - 7日年化收益率 万分收益 起购金额


  // - 基金

  /*
   * 基金产品类型
   * 01：理财型基金 02：QDII基金 03：ETF基金 04：保本型基金 05：指数型基金 06：货币型基金
   * 07：股票型基金 08：债券型基金 09：混合型基金 10：其他基金
   */
  private String fntype;

  /**
   *  日净值增长率(%)
   */
  private String dayIncomeRatio;

  /*
   * 申购费率
   */
  private String chargeRate;
  /**
   * 七日年化收益率	String	若返回值为0.05，则前端显示为5%
   */
  private String sevenDayYield;

  /**
   * 每万份基金单位收益	String
   */
  private String fundIncomeUnit;

  /**
   * 最低起购金额 - 基金
   */
  private String lowLimit;



  /**
   * 是否为业绩基准产品	String	0：非业绩基准产品
   * 1：业绩基准-锁定期转低收益
   * 2：业绩基准-锁定期后入账
   * 3：业绩基准-锁定期周期滚续
   */
  private String isLockPeriod;


  private boolean isBuy = true;

  // ----

  // ----


  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public String getRateValue() {
    return rateValue;
  }

  public void setRateValue(String rateValue) {
    this.rateValue = rateValue;
  }


  public String getMinMoney() {
    return minMoney;
  }

  public void setMinMoney(String minMoney) {
    this.minMoney = minMoney;
  }

  public String getProductNature() {
    return productNature;
  }

  public void setProductNature(String productNature) {
    this.productNature = productNature;
  }

  public String getPrice() {
    return price;
  }

  public String getAvailamt() {
    return availamt;
  }

  public void setAvailamt(String availamt) {
    this.availamt = availamt;
  }

  public String getPriceDate() {
    return priceDate;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public void setPriceDate(String priceDate) {
    this.priceDate = priceDate;
  }

  public void setTimeLimit(String timeLimit) {
    this.timeLimit = timeLimit;
  }

  public void setProdRisklvl(String prodRisklvl) {
    this.prodRisklvl = prodRisklvl;
  }

  public void setProdRiskType(String prodRiskType) {
    this.prodRiskType = prodRiskType;
  }

  public String getProdRisklvl() {
    return prodRisklvl;
  }

  public String getProdRiskType() {
    return prodRiskType;
  }

  public String getTimeLimit() {
    return timeLimit;
  }

  public String getFntype() {
    return fntype;
  }

  public void setFntype(String fntype) {
    this.fntype = fntype;
  }

  public String getDayIncomeRatio() {
    return dayIncomeRatio;
  }

  public void setDayIncomeRatio(String dayIncomeRatio) {
    this.dayIncomeRatio = dayIncomeRatio;
  }

  public String getChargeRate() {
    return chargeRate;
  }

  public void setChargeRate(String chargeRate) {
    this.chargeRate = chargeRate;
  }

  public String getSevenDayYield() {
    return sevenDayYield;
  }

  public void setSevenDayYield(String sevenDayYield) {
    this.sevenDayYield = sevenDayYield;
  }

  public String getFundIncomeUnit() {
    return fundIncomeUnit;
  }

  public void setFundIncomeUnit(String fundIncomeUnit) {
    this.fundIncomeUnit = fundIncomeUnit;
  }

  public String getLowLimit() {
    return lowLimit;
  }

  public void setLowLimit(String lowLimit) {
    this.lowLimit = lowLimit;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public void setRefrensh(boolean refrensh) {
    isRefrensh = refrensh;
  }

  public boolean isRefrensh() {
    return isRefrensh;
  }

  public void setSuccess(boolean success) {
    isSuccess = success;
  }

  public void setLoadLoginApi(boolean loadLoginApi) {
    isLoadLoginApi = loadLoginApi;
  }

  public void setLoadNoLoginApi(boolean loadNoLoginApi) {
    isLoadNoLoginApi = loadNoLoginApi;
  }

  public boolean isLoadLoginApi() {
    return isLoadLoginApi;
  }

  public boolean isLoadNoLoginApi() {
    return isLoadNoLoginApi;
  }

  public String getIsLockPeriod() {
    return isLockPeriod;
  }

  public void setIsLockPeriod(String isLockPeriod) {
    this.isLockPeriod = isLockPeriod;
  }

  public boolean isBuy() {
    return isBuy;
  }

  public void setBuy(boolean buy) {
    isBuy = buy;
  }

  @Override public String toString() {
    return "InvestItemVo{" +
        "productType='" + productType + '\'' +
        ", productName='" + productName + '\'' +
        ", ProductCode='" + productCode + '\'' +
        ", rateValue='" + rateValue + '\'' +
        ", minMoney='" + minMoney + '\'' +
        ", productNature='" + productNature + '\'' +
        '}';
  }
}
