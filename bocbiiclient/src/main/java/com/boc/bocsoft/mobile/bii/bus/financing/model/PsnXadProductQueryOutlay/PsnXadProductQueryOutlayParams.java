package com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXadProductQueryOutlay;

public class PsnXadProductQueryOutlayParams {

  private String conversationId;

  private String productType;//		产品属性分类	String	M	0：全部1：自营产品 2：代销产品
  private String productSta;//		产品销售状态	String	M	目前只支持在售产品查询 0：全部产品、1：在售产品、2：停售产品
  private String sortFlag;//		排序方式	String	M	0:正序1:倒序
  private String sortSite;//		排序字段	String	M	0:产品销售期1:产品期限2:收益率3:购买起点金额
  private String pageSize;//		每页显示条数	String	M
  private String currentIndex;//		当前页	String	M	初始值赋值为1
  private String _refresh;//		刷新标识	String	M

  private String productRiskType;//		产品风险类型	String	O	0=全部 1=保本固定收益 2=保本浮动收益 3=非保本浮动收益
  private String prodTimeLimit;
      //		产品期限	String	O	0=全部 1=3个月以内 2=3个月-6个月 3=6个月-12个月 4=12个月-24个月 5=24个月以上
  private String productCurCode;//		产品币种	String	O
  private String productCode;//		产品代码	String	O
  private String productKind;//		产品性质	String	O	0:全部1:结构性理财产品2:类基金理财产品可不上送，不送时“产品性质”默认为“1:结构性理财”
  private String issueType;
      //		产品类型	String	O	1：现金管理类产品2：净值开放类产品3：固定期限产品默认上送0
  private String  dayTerm;//	产品期限（天数）	String	O	0：全部1：30天以内2：31-90天3：91-180天4：180天以上默认上送0
  private String proRisk;//	风险等级	String	O	0：全部1：低风险2：中低风险3：中等风险4：中高风险5：高风险 默认上送0
  private String isLockPeriod;//		是否支持业绩基准产品查询	String	O	0：不支持1：支持默认上送0

  private String id;
  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public void setProductSta(String productSta) {
    this.productSta = productSta;
  }

  public void setSortFlag(String sortFlag) {
    this.sortFlag = sortFlag;
  }

  public void setSortSite(String sortSite) {
    this.sortSite = sortSite;
  }

  public void setPageSize(String pageSize) {
    this.pageSize = pageSize;
  }

  public void setCurrentIndex(String currentIndex) {
    this.currentIndex = currentIndex;
  }

  public void set_refresh(String _refresh) {
    this._refresh = _refresh;
  }

  public void setProductRiskType(String productRiskType) {
    this.productRiskType = productRiskType;
  }

  public void setProdTimeLimit(String prodTimeLimit) {
    this.prodTimeLimit = prodTimeLimit;
  }

  public void setProductCurCode(String productCurCode) {
    this.productCurCode = productCurCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public void setProductKind(String productKind) {
    this.productKind = productKind;
  }

  public void setIssueType(String issueType) {
    this.issueType = issueType;
  }

  public void setProRisk(String proRisk) {
    this.proRisk = proRisk;
  }

  public void setIsLockPeriod(String isLockPeriod) {
    this.isLockPeriod = isLockPeriod;
  }

  public void setDayTerm(String dayTerm) {
    this.dayTerm = dayTerm;
  }
}
