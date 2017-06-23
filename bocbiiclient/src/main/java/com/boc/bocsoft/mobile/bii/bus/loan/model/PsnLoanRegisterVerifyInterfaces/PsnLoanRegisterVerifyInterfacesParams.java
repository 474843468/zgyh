package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRegisterVerifyInterfaces;

import java.math.BigDecimal;

/**
 * 额度签约申请预交易接口请求参数
 * Created by xintong on 2016/6/13.
 */
public class PsnLoanRegisterVerifyInterfacesParams {


    //创建交易会话后的id
    private String conversationId;
    //安全工具组合
    private String _combinId;
    //贷款产品编号，固定值OC-LOAN
    private String loanPrdNo;
    //客户姓名
    private String custName;
    //证件类型01  身份证
    private String cerType;
    //证件号，字母大写
    private String cerNo;
    //国家行政区划代码,必须具体到县
    private String zoneCode;
    //手机号
    private String mobile;
    /**
     * 街道信息
     * 用户输入的不包含市、区的街道信息
     * （例如客户地址信息是“河北省承德市市某某区马连洼街道永丰路299号”，
     * “北京市”、“海淀区”部分是APP端让客户在下拉框里选的，
     * 然后用户手输的“马连洼街道永丰路299号”这部分街道信息）
     */
    private String streetInfo;
    //联系地址,拼接了省市区和详细地址的地址信息
    private String linkAddress;
    //联系关系01-父母 02-配偶 03-兄弟 04-姐妹
    private String linkRelation;
    //联系姓名
    private String linkName;
    //联系电话
    private String linkMobile;
    //还款账户ID
    private String loanRepayAccountId;
    //还款账户，只支持借记
    private String loanRepayAccount;
    //合同模板编号
    private String contractFormId;
    //额度金额
    private BigDecimal applyQuotet;
    //币种,CNY
    private String currencyCode;
    //三方协议编号,数据共享及征信查询协议
    private String threeContractNo;
    //语言类型CHN-中文ENG-英文
    private String eLanguage;




    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getLoanPrdNo() {
        return loanPrdNo;
    }

    public void setLoanPrdNo(String loanPrdNo) {
        this.loanPrdNo = loanPrdNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCerType() {
        return cerType;
    }

    public void setCerType(String cerType) {
        this.cerType = cerType;
    }

    public String getCerNo() {
        return cerNo;
    }

    public void setCerNo(String cerNo) {
        this.cerNo = cerNo;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public String getLinkRelation() {
        return linkRelation;
    }

    public void setLinkRelation(String linkRelation) {
        this.linkRelation = linkRelation;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkMobile() {
        return linkMobile;
    }

    public void setLinkMobile(String linkMobile) {
        this.linkMobile = linkMobile;
    }

    public String getLoanRepayAccountId() {
        return loanRepayAccountId;
    }

    public void setLoanRepayAccountId(String loanRepayAccountId) {
        this.loanRepayAccountId = loanRepayAccountId;
    }

    public String getLoanRepayAccount() {
        return loanRepayAccount;
    }

    public void setLoanRepayAccount(String loanRepayAccount) {
        this.loanRepayAccount = loanRepayAccount;
    }

    public String getContractFormId() {
        return contractFormId;
    }

    public void setContractFormId(String contractFormId) {
        this.contractFormId = contractFormId;
    }

    public BigDecimal getApplyQuotet() {
        return applyQuotet;
    }

    public void setApplyQuotet(BigDecimal applyQuotet) {
        this.applyQuotet = applyQuotet;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getStreetInfo() {
        return streetInfo;
    }

    public void setStreetInfo(String streetInfo) {
        this.streetInfo = streetInfo;
    }

    public String getThreeContractNo() {
        return threeContractNo;
    }

    public void setThreeContractNo(String threeContractNo) {
        this.threeContractNo = threeContractNo;
    }

    public String geteLanguage() {
        return eLanguage;
    }

    public void seteLanguage(String eLanguage) {
        this.eLanguage = eLanguage;
    }
}
