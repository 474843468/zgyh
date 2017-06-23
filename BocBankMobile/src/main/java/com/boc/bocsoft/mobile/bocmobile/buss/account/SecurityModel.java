package com.boc.bocsoft.mobile.bocmobile.buss.account;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyang
 *         16/6/29 09:53
 *         安全因子Model
 */
public class SecurityModel {

    /** name- 值‘Smc’需要输入手机验证码,值'Otp'需要输入动态口令,值'signedData'需要CA认证 */
    private List<FactorListBean> factorList;
    /** 手机验证码有效时间 */
    private String smcTrigerInterval;
    /** CA加签数据XML报文 */
    private String plainData;
    /** CA的DN值 */
    private String certDN;

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public String getPlainData() {
        return plainData;
    }

    public void setPlainData(String plainData) {
        this.plainData = plainData;
    }

    public String getCertDN() {
        return certDN;
    }

    public void setCertDN(String certDN) {
        this.certDN = certDN;
    }

    public void addFactorModel(FactorListBean factorModel){
        if(factorList == null)
            factorList = new ArrayList<>();
        factorList.add(factorModel);
    }
}
