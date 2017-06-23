package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model;

import android.support.annotation.NonNull;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryGuarantyProductList.PsnXpadQueryGuarantyProductListResult;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;

import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by wangtong on 2016/9/12.
 */
public class PortfolioPurchaseModel {
    /**
     * PortfolioPurchaseFragment用到的数据
     */
    //产品名称
    private String prodName = "中银智慧理财211期";
    //产品代码
    private String prodCode = "2185435";
    //币种
    private String curCode = "001";
    //认申购起点金额
    private double subAmount = 7000.00;
    //追加认申购起点金额
    private double addAmount = 200.00;
    //产品类型
    private String productType = "1";
    private String productKind;// 产品性质
    //购买基数
    private String baseAmount = "100";
    //账户id
    private String accountId = "184153456";
    //账户
    private String accountNum = "1231561321515231";

    private String accountKey;// 账户缓存标识

    //购买价格
    private String buyPrice;
    //风险匹配结果
    private String riskMatch;
    //风险匹配结果
    private String productRisk;
    //风险匹配结果
    private String customerRisk;
    //风险匹配结果
    private String riskMessage;
    //被组合产品数
    private int portfolioNum;
    //被组合份额
    private double portfolioAmount;
    //购买金额
    private double payAmount;
    //钞汇标示
    private String cashRemit = "0";
    //总钞份额
    private double cashTotalAmount;
    //总汇份额
    private double remitTotalAmount;

    private String prodBegin;// 产品起息日
    private String prodEnd;// 产品到期日
    private String productTermType;// 产品期限特性

    private BitSet mySelectStates;

    //持有的组合产品
    private List<Product> myProductList = new ArrayList<>();
    //持有的组合产品(钞)
    private List<Product> cashProductList = new ArrayList<>();
    //持有的组合产品(汇)
    private List<Product> remitProductList = new ArrayList<>();

    public PortfolioPurchaseModel() {

    }

    public PortfolioPurchaseModel(PortfolioPurchaseInputModel inputModel) {
        productKind = inputModel.getDetailsBean().getProductKind();
        prodName = inputModel.getDetailsBean().getProdName();
        prodCode = inputModel.getDetailsBean().getProdCode();
        curCode = inputModel.getDetailsBean().getCurCode();
        productType = inputModel.getDetailsBean().getProductType();
        buyPrice = inputModel.getDetailsBean().getBuyPrice();
        subAmount = StringUtils.isEmpty(inputModel.getDetailsBean().getSubAmount()) ? 0
                : Double.parseDouble(inputModel.getDetailsBean().getSubAmount());
        addAmount = StringUtils.isEmpty(inputModel.getDetailsBean().getAddAmount()) ? 0
                : Double.parseDouble(inputModel.getDetailsBean().getAddAmount());
        accountId = inputModel.getAccountBean().getAccountId();
        baseAmount = inputModel.getDetailsBean().getBaseAmount();
        if (StringUtils.isEmpty(baseAmount) || Double.parseDouble(baseAmount) == 0) {
            baseAmount = "100";
        }
        accountNum = inputModel.getAccountBean().getAccountNo();
        accountKey = inputModel.getAccountBean().getAccountKey();
        prodBegin = inputModel.getDetailsBean().getProdBegin();
        prodEnd = inputModel.getDetailsBean().getProdEnd();
        productTermType = inputModel.getDetailsBean().getProductTermType();
    }

    public void setProduct(PsnXpadQueryGuarantyProductListResult result) {
        List<PsnXpadQueryGuarantyProductListResult.ListBean> list = result.getList();
        mySelectStates=new BitSet(list.size());
        for (int i = 0; i < list.size(); i++) {
            PsnXpadQueryGuarantyProductListResult.ListBean item = list.get(i);
            Product product = new Product();
            BeanConvertor.toBean(item, product);
            product.serialNumber=i;
            myProductList.add(product);
            if (product.cashShare > 0) {
                cashProductList.add(product);
                cashTotalAmount += product.cashShare;
            }

            if (product.remitShare > 0) {
                remitProductList.add(product);
                remitTotalAmount += product.remitShare;
            }
        }
    }

    public Product findProductByCode(String code) {
        Product product = null;
        for (int i = 0; i < myProductList.size(); i++) {
            if (myProductList.get(i).productCode.equals(code)) {
                product = myProductList.get(i);
                break;
            }
        }
        return product;
    }

    public void clearSelectProductList() {
        if (selectProductList.size() > 0) {
            selectProductList.clear();
            mySelectStates.clear();
        }
    }

    /**
     * PortfolioSelectFragment用到的数据
     */
    private List<Product> selectProductList = new ArrayList<>();

    public List<String> getSelectProductListCode() {
        List<String> prodCode = new ArrayList<>();
        for (int i = 0; i < selectProductList.size(); i++) {
            prodCode.add(selectProductList.get(i).productCode);
        }
        return prodCode;
    }

    public List<String> getSelectProductListAmount() {
        List<String> prodAmount = new ArrayList<>();
        for (int i = 0; i < selectProductList.size(); i++) {
            prodAmount.add(selectProductList.get(i).portfolioAmount + "");
        }
        return prodAmount;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public BitSet getMySelectStates() {
        return mySelectStates;
    }

    public void setMySelectStates(BitSet mySelectStates) {
        this.mySelectStates = mySelectStates;
    }

    public static class Product implements Comparable<Product> {
        //持有现钞份额
        public double cashShare;
        //持有现汇份额
        public double remitShare;
        //被组合份额
        public double portfolioAmount;
        public String productCode;
        public String productName;
        public String accountNumber;
        public int serialNumber;//序号

        @Override
        public int compareTo(@NonNull Product another) {
            if (serialNumber < another.serialNumber) {
                return -1;
            }
            if (serialNumber == another.serialNumber) {
                return 0;
            }
            return 1;
        }
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public double getSubAmount() {
        return subAmount;
    }

    public void setSubAmount(double subAmount) {
        this.subAmount = subAmount;
    }

    public double getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(double addAmount) {
        this.addAmount = addAmount;
    }

    public double getMinStartAmount() {
        return Math.min(subAmount, addAmount);
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getRiskMatch() {
        return riskMatch;
    }

    public void setRiskMatch(String riskMatch) {
        this.riskMatch = riskMatch;
    }

    public String getProductRisk() {
        return productRisk;
    }

    public void setProductRisk(String productRisk) {
        this.productRisk = productRisk;
    }

    public String getCustomerRisk() {
        return customerRisk;
    }

    public void setCustomerRisk(String customerRisk) {
        this.customerRisk = customerRisk;
    }

    public String getRiskMessage() {
        return riskMessage;
    }

    public void setRiskMessage(String riskMessage) {
        this.riskMessage = riskMessage;
    }

    public int getPortfolioNum() {
        return portfolioNum;
    }

    public void setPortfolioNum(int portfolioNum) {
        this.portfolioNum = portfolioNum;
    }

    public double getPortfolioAmount() {
        return portfolioAmount;
    }

    public void setPortfolioAmount(double portfolioAmount) {
        this.portfolioAmount = portfolioAmount;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public double getCashTotalAmount() {
        return cashTotalAmount;
    }

    public void setCashTotalAmount(double cashTotalAmount) {
        this.cashTotalAmount = cashTotalAmount;
    }

    public double getRemitTotalAmount() {
        return remitTotalAmount;
    }

    public void setRemitTotalAmount(double remitTotalAmount) {
        this.remitTotalAmount = remitTotalAmount;
    }

    public String getProdBegin() {
        return prodBegin;
    }

    public void setProdBegin(String prodBegin) {
        this.prodBegin = prodBegin;
    }

    public String getProdEnd() {
        return prodEnd;
    }

    public void setProdEnd(String prodEnd) {
        this.prodEnd = prodEnd;
    }

    public String getProductTermType() {
        return productTermType;
    }

    public void setProductTermType(String productTermType) {
        this.productTermType = productTermType;
    }

    public List<Product> getMyProductList() {
        return myProductList;
    }

    public List<Product> getCashProductList() {
        return cashProductList;
    }

    public List<Product> getRemitProductList() {
        return remitProductList;
    }

    public List<Product> getSelectProductList() {
        return selectProductList;
    }

    public String getProductKind() {
        return productKind;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }
}
