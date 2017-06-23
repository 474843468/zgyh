package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;

import java.util.List;

/**
 * Created by wangtong on 2016/7/11.
 */
public class ConfirmEditModel {
    //编辑短信通知的旧数据
    private SmsNotifyEditModel oldEditModel;
    //编辑界面数据
    private SmsNotifyEditModel editModel;
    //安全加密随机数
    private String[] encryptRandomNums;
    //安全加密密文
    private String[] encryptPasswords;
    //安全因子
    private SecurityFactorModel factorModel;
    //随机数
    private String randomNum;
    //预交易返回安全因子
    private List<FactorListBean> preFactorList;
    //最终选择的安全因子
    private String selectedFactorId;
    //音频key上送参数
    private String mPlainData;
    //音频key返回参数
    private String mSignData;

    public String getmSignData() {
        return mSignData;
    }

    public void setmSignData(String mSignData) {
        this.mSignData = mSignData;
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

    public List<FactorListBean> getPreFactorList() {
        return preFactorList;
    }

    public void setPreFactorList(List<FactorListBean> prefactorList) {
        this.preFactorList = prefactorList;
    }

    public String getSelectedFactorId() {
        return selectedFactorId;
    }

    public void setSelectedFactorId(String selectedFactorId) {
        this.selectedFactorId = selectedFactorId;
    }

    public String getmPlainData() {
        return mPlainData;
    }

    public void setmPlainData(String mPlainData) {
        this.mPlainData = mPlainData;
    }

    public SmsNotifyEditModel getOldEditModel() {
        return oldEditModel;
    }

    public void setOldEditModel(SmsNotifyEditModel oldEditModel) {
        this.oldEditModel = oldEditModel;
    }

    public SmsNotifyEditModel getEditModel() {
        return editModel;
    }

    public void setEditModel(SmsNotifyEditModel editModel) {
        this.editModel = editModel;
    }

}
