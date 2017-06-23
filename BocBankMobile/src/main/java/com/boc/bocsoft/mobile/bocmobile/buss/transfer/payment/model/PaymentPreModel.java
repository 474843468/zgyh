package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;

import java.math.BigDecimal;
import java.util.List;


/**
 * Created by wangtong on 2016/6/30.
 */
public class PaymentPreModel {
    //备注
    private String furInfo;
    //收款人账号
    private String payeeAccountNumber;
    //收款人账户类型
    private String payeeAccountType;
    //收款人联行号
    private String payeeIbk;
    //收款人手机
    private String payeeMobile;
    //收款人姓名
    private String payeeName;
    //付款人账号
    private String payerAccountNumber;
    //付款账户ID
    private String payerAccountId;
    //催款金额
    private String requestAmount;
    //交易状态
    private String status;
    //实付金额
    private String trfAmount;
    //币种
    private String trfCur;
    //优惠后金额
    private String discountAmount;
    //交易序号
    private String transferNum;
    //交易ID
    private String notifyId;
    //交易渠道
    private String createChanel;
    //催款日期
    private String createDate;
    //交易客户号
    private String payerCustId;
    //付款人手机
    private String payerMobile;
    //付款人姓名
    private String payerName;
    //付款日期
    private String payDate;
    //付款渠道
    private String payChanel;
    //安全因子
    private SecurityFactorModel factorModel;
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

    private String mPlainData;
    //密码验证类型
    private String needPassword;
    //密码加密随机数
    private String[] encryptRandomNumsPass;
    //密码加密密文
    private String[] encryptPasswordsPass;
    //账户余额
    private BigDecimal remainAmount;

    private boolean queryStates;

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }

    public boolean isQueryStates() {
        return queryStates;
    }

    public void setQueryStates(boolean queryStates) {
        this.queryStates = queryStates;
    }

    public String getPayChanel() {
        return payChanel;
    }

    public void setPayChanel(String payChanel) {
        this.payChanel = payChanel;
    }

    public void transFromPaymentModel(ConfirmPaymentModel model) {
        furInfo = model.getDetail().getFurInfo();
        payeeAccountNumber = model.getDetail().getPayeeAccountNumber();
        payeeAccountType = model.getDetail().getPayeeAccountType();
        payeeIbk = model.getDetail().getPayeeIbk();
        payeeMobile = model.getDetail().getPayeeMobile();
        payeeName = model.getDetail().getPayeeName();
        payerAccountNumber = model.getDetail().getPayerAccountNumber();
        payerAccountId = model.getAccountId();
        requestAmount = model.getDetail().getRequestAmount();
        status = model.getDetail().getStatus();
        trfAmount = model.getDetail().getTrfAmount() + "";
        trfCur = model.getDetail().getTrfCur();
        transferNum = model.getDetail().getNotifyId();
        notifyId = model.getDetail().getNotifyId();
        createChanel = model.getDetail().getCreateChannel();
        createDate = model.getDetail().getCreateDate();
        payerCustId = model.getDetail().getPayerCustomerId();
        payerMobile = model.getDetail().getPayerMobile();
        payerName = model.getDetail().getPayerName();
        payDate = model.getDetail().getPaymentDate();
        payChanel = model.getDetail().getPaymentDate();
        remainAmount = model.getRemainAmount();
    }

    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public String[] getEncryptRandomNumsPass() {
        return encryptRandomNumsPass;
    }

    public void setEncryptRandomNumsPass(String[] encryptRandomNumsPass) {
        this.encryptRandomNumsPass = encryptRandomNumsPass;
    }

    public String[] getEncryptPasswordsPass() {
        return encryptPasswordsPass;
    }

    public void setEncryptPasswordsPass(String[] encryptPasswordsPass) {
        this.encryptPasswordsPass = encryptPasswordsPass;
    }

    public String getNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(String needPassword) {
        this.needPassword = needPassword;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPayerAccountId() {
        return payerAccountId;
    }

    public void setPayerAccountId(String payerAccountId) {
        this.payerAccountId = payerAccountId;
    }

    public String getCreateChanel() {
        return createChanel;
    }

    public void setCreateChanel(String createChanel) {
        this.createChanel = createChanel;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPayerCustId() {
        return payerCustId;
    }

    public void setPayerCustId(String payerCustId) {
        this.payerCustId = payerCustId;
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

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public SecurityFactorModel getFactorModel() {
        return factorModel;
    }

    public void setFactorModel(SecurityFactorModel factorModel) {
        this.factorModel = factorModel;
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

    public String getmPlainData() {
        return mPlainData;
    }

    public void setmPlainData(String mPlainData) {
        this.mPlainData = mPlainData;
    }

    public String getTransferNum() {
        return transferNum;
    }

    public void setTransferNum(String transferNum) {
        this.transferNum = transferNum;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getFurInfo() {
        return furInfo;
    }

    public void setFurInfo(String furInfo) {
        this.furInfo = furInfo;
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

    public String getPayerAccountNumber() {
        return payerAccountNumber;
    }

    public void setPayerAccountNumber(String payerAccountNumber) {
        this.payerAccountNumber = payerAccountNumber;
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

    public String getTrfAmount() {
        return trfAmount;
    }

    public void setTrfAmount(String trfAmount) {
        this.trfAmount = trfAmount;
    }

    public String getTrfCur() {
        return trfCur;
    }

    public void setTrfCur(String trfCur) {
        this.trfCur = trfCur;
    }

}
