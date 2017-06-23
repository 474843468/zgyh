package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActReminderOrderDetail.PsnReminderOrderDetailResult;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;

import java.util.List;

/**
 * Created by wangtong on 2016/6/28.
 */
public class ReminderDetailModel {
    //发起渠道
    private String createChannel;
    //催款日期
    private String createDate;
    //备注
    private String furInfo;
    //指令序号
    private String notifyId;
    //收款人账号
    private String payeeAccountNumber;
    //收款人账户ID
    private String payeeAccountId;
    //收款人账户类型
    private String payeeAccountType;
    //收款人联行号
    private String payeeIbk;
    //收款人手机
    private String payeeMobile;
    //收款人姓名
    private String payeeName;
    //付款人客户号
    private String payerCustomerId;
    //付款人手机
    private String payerMobile;
    //付款人姓名
    private String payerName;
    //催款金额
    private String requestAmount;
    //实付金额
    private String trfAmount = "0";
    //交易状态
    private String status;
    //交易序号
    private String transactionId;
    //币种
    private String trfCur;
    //付款人账号
    private String payerAccount;
    //付款日期
    private String payerDate;
    //付款渠道
    private String payerChannel;
    //安全因子
    private SecurityFactorModel factorModel;
    //安全认证方式
    private String securityFactor;
    //默认安全工具
    private String defaultFactor;
    //随机数
    private String randomNum;
    //预交易返回安全因子
    private List<FactorListBean> prefactorList;
    //最终选择的安全因子
    private String selectedFactorId;
    //安全加密随机数
    private String[] encryptRandomNums;
    //安全加密密文
    private String[] encryptPasswords;
    //预交易返回音频key数据
    private String plainData;
    //音频key加密数据
    private String signedData;
    //短信发送状态
    private String smsStatus;

    public String getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        this.smsStatus = smsStatus;
    }

    public String getPlainData() {
        return plainData;
    }

    public void setPlainData(String plainData) {
        this.plainData = plainData;
    }

    public String getSignedData() {
        return signedData;
    }

    public void setSignedData(String signedData) {
        this.signedData = signedData;
    }

    public void transFromPresent(PsnReminderOrderDetailResult result) {
        createChannel = result.getCreateChannel();
        createDate = result.getCreateDate();
        furInfo = result.getFurInfo();
        notifyId = result.getNotifyId() + "";
        payeeAccountNumber = result.getPayeeAccountNumber();
        payeeAccountType = result.getPayeeAccountType();
        payeeName = result.getPayeeName();
        payeeMobile = result.getPayeeMobile();
        payerCustomerId = result.getPayerCustomerId();
        payerMobile = result.getPayerMobile();
        payerName = result.getPayerName();
        requestAmount = result.getRequestAmount();
        status = result.getStatus();
        trfCur = result.getTrfCur();
        transactionId = result.getTransactionId();
        payerAccount = result.getPayerAccountNumber();
        payerDate = result.getPaymentDate();
        payerChannel = result.getTrfChannel();
        trfAmount = result.getTrfAmount();
    }

    public String getPayeeAccountId() {
        return payeeAccountId;
    }

    public void setPayeeAccountId(String payeeAccountId) {
        this.payeeAccountId = payeeAccountId;
    }

    public String getTrfAmount() {
        return trfAmount;
    }

    public void setTrfAmount(String trfAmount) {
        this.trfAmount = trfAmount;
    }

    public SecurityFactorModel getFactorModel() {
        return factorModel;
    }

    public void setFactorModel(SecurityFactorModel factorModel) {
        this.factorModel = factorModel;
    }

    public String getSecurityFactor() {
        return securityFactor;
    }

    public void setSecurityFactor(String securityFactor) {
        this.securityFactor = securityFactor;
    }

    public String getDefaultFactor() {
        return defaultFactor;
    }

    public void setDefaultFactor(String defaultFactor) {
        this.defaultFactor = defaultFactor;
    }

    public String getRandomNum() {
        return randomNum;
    }

    public void setRandomNum(String randomNum) {
        this.randomNum = randomNum;
    }

    public List<FactorListBean> getPrefactorList() {
        return prefactorList;
    }

    public void setPrefactorList(List<FactorListBean> prefactorList) {
        this.prefactorList = prefactorList;
    }

    public String getSelectedFactorId() {
        return selectedFactorId;
    }

    public void setSelectedFactorId(String selectedFactorId) {
        this.selectedFactorId = selectedFactorId;
    }

    public String[] getEncryptRandomNums() {
        return encryptRandomNums;
    }

    public void setEncryptRandomNums(String[] encryptRandomNums) {
        this.encryptRandomNums = encryptRandomNums;
    }

    public String[] getEncryptPasswords() {
        return encryptPasswords;
    }

    public void setEncryptPasswords(String[] encryptPasswords) {
        this.encryptPasswords = encryptPasswords;
    }

    public String getCreateChannel() {
        return createChannel;
    }

    public void setCreateChannel(String createChannel) {
        this.createChannel = createChannel;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getFurInfo() {
        return furInfo;
    }

    public void setFurInfo(String furInfo) {
        this.furInfo = furInfo;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public String getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }

    public String getPayerDate() {
        return payerDate;
    }

    public void setPayerDate(String payerDate) {
        this.payerDate = payerDate;
    }

    public String getPayerChannel() {
        return payerChannel;
    }

    public void setPayerChannel(String payerChannel) {
        this.payerChannel = payerChannel;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getPayeeAccountNumber() {
        return payeeAccountNumber;
    }

    public void setPayeeAccountNumber(String payeeAccountNumber) {
        this.payeeAccountNumber = payeeAccountNumber;
    }

    public String getPayeeAccountType() {
        return payeeAccountType;
    }

    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
    }

    public String getPayeeIbk() {
        return payeeIbk;
    }

    public void setPayeeIbk(String payeeIbk) {
        this.payeeIbk = payeeIbk;
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

    public String getPayerCustomerId() {
        return payerCustomerId;
    }

    public void setPayerCustomerId(String payerCustomerId) {
        this.payerCustomerId = payerCustomerId;
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

    public String getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(String requestAmount) {
        this.requestAmount = requestAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTrfCur() {
        return trfCur;
    }

    public void setTrfCur(String trfCur) {
        this.trfCur = trfCur;
    }
}
