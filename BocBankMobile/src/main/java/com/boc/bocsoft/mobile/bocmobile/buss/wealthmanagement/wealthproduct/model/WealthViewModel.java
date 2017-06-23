package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 中银理财产品数据
 * Created by liuweidong on 2016/9/9.
 */
public class WealthViewModel {
    private String recordNumber = "0";// 总记录数
    private boolean isSearch = false;// 点击搜索
    private boolean isSelect = false;// 点击筛选确认按钮
    private boolean isLoginBeforeI = false;// 判断登录前或登录后接口
    private boolean isFirst = false;// 是否第一次进入列表

    private List<WealthListBean> productList;// 理财产品列表
    private List<WealthAccountBean> accountList = new ArrayList<>();// 理财账户信息
    private WealthAccountBean accountBean;// 列表页当前筛选的账户（登录后）
    /*产品列表请求参数*/
    private String productCode = "";// 产品代码（搜索;字段暂时不用，参数传值）
    private String issueType = "0";// 产品类型 1：现金管理类产品 2：净值开放类产品 3：固定期限产品 默认上送0
    private String productCurCode = "001";// 产品币种
    private String dayTerm = "0";// 产品期限 0：全部 1：30天以内 2：31-90天 3：91-180天 4：180天以上
    private String productRiskType = "0";// 收益类型 0：全部 1：保本固定收益、2：保本浮动收益、3：非保本浮动收益
    private String proRisk = "0";// 风险等级 0：全部 1：低风险 2：中低风险 3：中等风险 4：中高风险 5：高风险
    private String sortType = "2";// 排序类型 0:产品销售期 1:产品期限 2:收益率 3:购买起点金额
    private String sortFlag = "1";// 排序方式 0:正序 1:倒序
    /*最近操作的理财账号响应参数*/
    private String xpadAccount;// 客户理财账户
    private String accountNo;// 资金账户
    private String accountType;// 账户类型
    private String bancID;// 账户开户行
    private String accountKey;// 资金账号缓存标识

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean search) {
        isSearch = search;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isLoginBeforeI() {
        return isLoginBeforeI;
    }

    public void setLoginBeforeI(boolean loginBeforeI) {
        isLoginBeforeI = loginBeforeI;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public void setProductList(List<WealthListBean> productList) {
        this.productList = productList;
    }

    public List<WealthListBean> getProductList() {
        return productList;
    }

    public List<WealthAccountBean> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<WealthAccountBean> accountList) {
        this.accountList = accountList;
    }

    public WealthAccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(WealthAccountBean accountBean) {
        this.accountBean = accountBean;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getProductCurCode() {
        return productCurCode;
    }

    public void setProductCurCode(String productCurCode) {
        this.productCurCode = productCurCode;
    }

    public String getDayTerm() {
        return dayTerm;
    }

    public void setDayTerm(String dayTerm) {
        this.dayTerm = dayTerm;
    }

    public String getProductRiskType() {
        return productRiskType;
    }

    public void setProductRiskType(String productRiskType) {
        this.productRiskType = productRiskType;
    }

    public String getProRisk() {
        return proRisk;
    }

    public void setProRisk(String proRisk) {
        this.proRisk = proRisk;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortFlag() {
        return sortFlag;
    }

    public void setSortFlag(String sortFlag) {
        this.sortFlag = sortFlag;
    }

    public String getXpadAccount() {
        return xpadAccount;
    }

    public void setXpadAccount(String xpadAccount) {
        this.xpadAccount = xpadAccount;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBancID() {
        return bancID;
    }

    public void setBancID(String bancID) {
        this.bancID = bancID;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }
}
