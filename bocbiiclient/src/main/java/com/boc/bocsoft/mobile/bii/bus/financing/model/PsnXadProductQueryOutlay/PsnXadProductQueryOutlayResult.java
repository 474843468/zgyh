package com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXadProductQueryOutlay;

import java.util.List;

public class PsnXadProductQueryOutlayResult {

  private String recordNumber;//总记录数

  private List<XPadProductBean> List;

  public List<XPadProductBean> getList() {
    return List;
  }

  public String getRecordNumber() {
    return recordNumber;
  }

  public static class XPadProductBean {

    private String prodName; //产品名称	String
    private String prodCode; //产品代码	String
    private String curCode; //产品币种	String
    private String subAmount; //起购金额	BigDecimal
    private String prodTimeLimit;
        //产品期限	String	0：全部、1：3个月以内、2：3个月-6个月、3：6个月-12个月、4：12个月-24个月、5：24个月以上如果产品允许转活期收益则需显示“最低持有X天”
    private String yearlyRR; //预计年收益率（%）	String
    private String buyPrice; //购买价格	BigDecimal
    private String applyObj; //适用对象	String
    private String status; //产品销售状态	String	0：全部产品、1：在售产品、2：停售产品注：可购买标识为status为1且periodical为false
    private String prodRisklvl; //产品风险级别	String	0：低风险产品1：中低风险产品2：中等风险产品3：中高风险产品4：高风险产品
    private String periodical; //是否周期性产品	boolean	rue：是false：否注：可签约标识为status为1且periodical为ture
    private String remainCycleCount;//剩余可购买最大期数.非周期性产品返回空	String
    private String autoPermit;//是否允许定投与自动投资	String	0：是1：否注：可投资协议申请标识为autoPermit为0
    private String impawnPermit;//是否可组合购买	String	0：是1：否 注：可组合购买标识为impawnPermit为0
    private String progressionflag;//是否收益累计产品	String	0：否1：是
    private String price;//最新净值	String
    private String issueType;//产品类型	String	1：现金管理类产品2：净值开放类产品3：固定期限产品
    private String termType;//产品期限特性	String	0：有限期封闭式1：有限半开放式2：周期性3：无限开放式4：春夏秋冬
    private String priceDate;//净值日期	String	Yyyy/MM/dd
    private String isLockPeriod;
        //是否为业绩基准产品	String	0：非业绩基准产品1：业绩基准-锁定期转低收益 2：业绩基准-锁定期后入账 3：业绩基准-锁定期周期滚续
    private String isBuy;//是否显示购买连接	String	0：不显示1：显示
    private String isAgreement;//是否允许投资协议申请	String	0：不允许1：允许
    private String isProfiTest;//是否可收益试算	String	0：不允许1：允许
    private String rateDetail;//预计年收益率（%）(最大值)	String	不显示“%”，需要前端处理

    public String getProdName() {
      return prodName;
    }

    public String getProdCode() {
      return prodCode;
    }

    public String getCurCode() {
      return curCode;
    }

    public String getSubAmount() {
      return subAmount;
    }

    public String getProdTimeLimit() {
      return prodTimeLimit;
    }

    public String getYearlyRR() {
      return yearlyRR;
    }

    public String getBuyPrice() {
      return buyPrice;
    }

    public String getApplyObj() {
      return applyObj;
    }

    public String getStatus() {
      return status;
    }

    public String getProdRisklvl() {
      return prodRisklvl;
    }

    public String getPeriodical() {
      return periodical;
    }

    public String getRemainCycleCount() {
      return remainCycleCount;
    }

    public String getAutoPermit() {
      return autoPermit;
    }

    public String getImpawnPermit() {
      return impawnPermit;
    }

    public String getProgressionflag() {
      return progressionflag;
    }

    public String getPrice() {
      return price;
    }

    public String getIssueType() {
      return issueType;
    }

    public String getTermType() {
      return termType;
    }

    public String getPriceDate() {
      return priceDate;
    }

    public String getIsLockPeriod() {
      return isLockPeriod;
    }

    public String getIsBuy() {
      return isBuy;
    }

    public String getIsAgreement() {
      return isAgreement;
    }

    public String getIsProfiTest() {
      return isProfiTest;
    }

    public String getRateDetail() {
      return rateDetail;
    }
  }
}

