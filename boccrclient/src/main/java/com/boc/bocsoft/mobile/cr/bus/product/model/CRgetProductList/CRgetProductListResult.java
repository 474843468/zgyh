package com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList;

import java.util.List;

/**
 * 产品推荐列表
 */
public class CRgetProductListResult {

  private List<ProductBean> rows;

  public List<ProductBean> getArrayList() {
    return rows;
  }

  public static class ProductBean {

    private String type;//产品类型	0基金，1理财产品
    private String name;//产品名称
    private String productCode;//产品代码
    private String productNature;//产品性质	0：结构性，1类基金
    private String topFlag;//是否置顶	0:是，1:否
    private String rateValue;//预计年收益率  理财产品才有百分比

    private String productLimit;//产品期限	单位月
    private String minMoney;//起购金额	人民币元

    private String remark;//推荐理由

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getProductCode() {
      return productCode;
    }

    public void setProductCode(String productCode) {
      this.productCode = productCode;
    }

    public String getProductNature() {
      return productNature;
    }

    public void setProductNature(String productNature) {
      this.productNature = productNature;
    }

    public String getTopFlag() {
      return topFlag;
    }

    public void setTopFlag(String topFlag) {
      this.topFlag = topFlag;
    }

    public String getRateValue() {
      return rateValue;
    }

    public void setRateValue(String rateValue) {
      this.rateValue = rateValue;
    }

    public String getProductLimit() {
      return productLimit;
    }

    public void setProductLimit(String productLimit) {
      this.productLimit = productLimit;
    }

    public String getMinMoney() {
      return minMoney;
    }

    public void setMinMoney(String minMoney) {
      this.minMoney = minMoney;
    }

    public String getRemark() {
      return remark;
    }

    public void setRemark(String remark) {
      this.remark = remark;
    }
  }
}
