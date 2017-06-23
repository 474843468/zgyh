package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;

import java.util.List;

/**
 * Created by wangtong on 2016/8/11.
 */
public class EditFeeAccountModel {
    //签约账户
    private AccountBean signedAccount;
    //客户名称
    private String customerName;
    //新付费账户
    private AccountBean accountNew;
    //旧付费账户
    private String accountOld;
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
    //音频key签名数据
    private String mPlainData;
    //音频key签名数据结果
    private String mSignedData;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getmSignedData() {
        return mSignedData;
    }

    public void setmSignedData(String mSignedData) {
        this.mSignedData = mSignedData;
    }

    public AccountBean getSignedAccount() {
        return signedAccount;
    }

    public void setSignedAccount(AccountBean signedAccount) {
        this.signedAccount = signedAccount;
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

    public AccountBean getAccountNew() {
        return accountNew;
    }

    public void setAccountNew(AccountBean accountNew) {
        this.accountNew = accountNew;
    }

    public String getAccountOld() {
        return accountOld;
    }

    public void setAccountOld(String accountOld) {
        this.accountOld = accountOld;
    }
}
