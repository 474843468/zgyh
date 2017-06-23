package com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQuery;

/**
 * 4.40 040产品详情查询 PsnXpadProductDetailQuery 登陆后
 */
public class PsnXpadProductDetailQueryParams {


  private String productCode;//产品代码 String M

  private String ibknum;//省行联行号 String O 返回项需展示(剩余额度、工作时间、挂单时间)，此项必输 根据PsnXpadAccountQuery接口的返回项进行上送
  private String productKind;//产品性质 String O 0:结构性理财产品 1:类基金理财产品 可不上送，不送时“产品性质”默认为“0:结构性理财”

  public void setIbknum(String ibknum) {
    this.ibknum = ibknum;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public void setProductKind(String productKind) {
    this.productKind = productKind;
  }
}
