package com.boc.bocsoft.mobile.bii.bus.asset.model.PsnAssetBalanceQuery;



public class PsnAssetBalanceQueryResult {

  private String depositAmt	;//全行存款	String	包括①活期②定期（含大额存单）③信用卡贷方余额
  private String loanAmt			;//全行贷款	String
  private String fundAmt			;//全行基金	String	不包括①认购未正式成交部分
  private String tpccAmt			;//全行第三方存管	String
  private String xpadAmt			;//全行理财产品	String
  private String bondAmt			;//全行国债	String
  private String ibasAmt			;//全行保险	String
  private String actGoldAmt	;//全行账户贵金属	String
  private String cardAmt			;//全行信用卡消费	String
  private String metalAmt		;//全行贵金属	String
  private String aumAmt			;//全行总资产	String
  private String stockAmt		;//中银证券资产	String
  private String otherAmt		;//其他资产	String
  private String forexAmt		;//双向宝资产	String
  private String pltmAmt			;//纸铂金	String
  private String padmAmt			;//纸钯金	String
  private String jxjAmt			;//吉祥金	String
  private String autd				;//黄金T+D	String
  private String iccdAmt			;//电子现金资产	String
  private String otherBankAmt;//行外资产	String
  private String cardLoanAmt	;//信用卡欠款	String

  public String getDepositAmt() {
    return depositAmt;
  }

  public String getLoanAmt() {
    return loanAmt;
  }

  public String getFundAmt() {
    return fundAmt;
  }

  public String getTpccAmt() {
    return tpccAmt;
  }

  public String getXpadAmt() {
    return xpadAmt;
  }

  public String getBondAmt() {
    return bondAmt;
  }

  public String getIbasAmt() {
    return ibasAmt;
  }

  public String getActGoldAmt() {
    return actGoldAmt;
  }

  public String getCardAmt() {
    return cardAmt;
  }

  public String getMetalAmt() {
    return metalAmt;
  }

  public String getAumAmt() {
    return aumAmt;
  }

  public String getStockAmt() {
    return stockAmt;
  }

  public String getOtherAmt() {
    return otherAmt;
  }

  public String getForexAmt() {
    return forexAmt;
  }

  public String getPltmAmt() {
    return pltmAmt;
  }

  public String getPadmAmt() {
    return padmAmt;
  }

  public String getJxjAmt() {
    return jxjAmt;
  }

  public String getAutd() {
    return autd;
  }

  public String getIccdAmt() {
    return iccdAmt;
  }

  public String getOtherBankAmt() {
    return otherBankAmt;
  }

  public String getCardLoanAmt() {
    return cardLoanAmt;
  }
}

