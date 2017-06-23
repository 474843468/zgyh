package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.io.Serializable;
import java.util.List;

/**
 * ViewModel:主动收款预交易
 * Created by zhx on 2016/8/11
 */
public class PsnTransActCollectionVerifyViewModel implements Serializable {
    /**
     * 收款人账ID
     */
    private String toAccountId;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 币种
     */
    private String currency;
    /**
     * 收款金额
     */
    private String notifyPayeeAmount;
    /**
     * 付款人客户号
     */
    private String payerCustId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 付款人姓名
     */
    private String payerName;
    /**
     * 收款人账号
     */
    private String payeeActno;
    /**
     * 付款人手机
     */
    private String payerMobile;
    /**
     * 付款人类型
     */
    private String payerChannel;
    /**
     * 收款人手机
     */
    private String payeeMobile;
    /**
     * 安全因子组合id
     */
    private String _combinId;
    /**
     * 安全因子组合id(名字)
     */
    private String _combinName;
    /**
     * 会话ID
     */
    private String conversationId;

    private String totalAmount;

    private long notifyId;

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    /**
     * 手机验证码有效时间
     */
    private String smcTrigerInterval;
    /**
     * CA加签数据XML报文
     */
    private String _plainData;
    /**
     * CA的DN值
     */
    private String _certDN;
    /**
     * field : {"name":"_signedData","type":"hidden"}
     */

    /**
     * 安全因子数组
     */
    private List<FactorListBean> factorList;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNotifyPayeeAmount() {
        return notifyPayeeAmount;
    }

    public void setNotifyPayeeAmount(String notifyPayeeAmount) {
        this.notifyPayeeAmount = notifyPayeeAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPayerCustId() {
        return payerCustId;
    }

    public void setPayerCustId(String payerCustId) {
        this.payerCustId = payerCustId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerChannel() {
        return payerChannel;
    }

    public void setPayerChannel(String payerChannel) {
        this.payerChannel = payerChannel;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public String get_plainData() {
        return _plainData;
    }

    public void set_plainData(String _plainData) {
        this._plainData = _plainData;
    }

    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
    }

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

    public String get_combinName() {
        return _combinName;
    }

    public void set_combinName(String _combinName) {
        this._combinName = _combinName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(long notifyId) {
        this.notifyId = notifyId;
    }
}
