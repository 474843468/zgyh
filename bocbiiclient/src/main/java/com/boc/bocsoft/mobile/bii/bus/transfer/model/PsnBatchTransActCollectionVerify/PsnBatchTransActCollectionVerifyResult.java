package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnBatchTransActCollectionVerify;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;

/**
 * 批量主动收款预交易
 * Created by zhx on 2016/8/13
 */
public class PsnBatchTransActCollectionVerifyResult {
    /**
     * 安全因子数组
     */
    private List<FactorListBean> factorList;
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
     * 批量提交失败结果集
     */
    private List<FailEntity> failList;

    public class FailEntity {
        /**
         * 序号
         */
        private String seqNo;
        /**
         * 失败原因
         */
        private String errorMessage;

        public String getSeqNo() {
            return seqNo;
        }

        public void setSeqNo(String seqNo) {
            this.seqNo = seqNo;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

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

    public List<FailEntity> getFailList() {
        return failList;
    }

    public void setFailList(List<FailEntity> failList) {
        this.failList = failList;
    }
}
