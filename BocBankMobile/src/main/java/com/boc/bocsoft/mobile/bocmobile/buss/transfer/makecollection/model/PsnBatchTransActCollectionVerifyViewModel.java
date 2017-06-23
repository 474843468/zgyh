package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.io.Serializable;
import java.util.List;

/**
 * ViewModel:主动收款预交易
 * Created by zhx on 2016/8/13
 */
public class PsnBatchTransActCollectionVerifyViewModel implements Serializable {
    /**
     * 收款人账ID
     */
    private String toAccountId;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 收款人账号
     */
    private String payeeActno;
    /**
     * 币种
     */
    private String currency;
    /**
     * 人均收款金额
     */
    private String notifyPayeeAmount;
    /**
     * 总金额
     */
    private String totalAmount;
    /**
     * 总笔数
     */
    private String totalNum;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 备注
     */
    private String remark;
    /**
     * 安全因子组合id
     */
    private String _combinId;
    /**
     * 安全因子组合id(名字)
     */
    private String _combinName;
    /**
     * 付款人列表
     */
    private List<PayerEntity> payerList;

    /**
     * 安全因子数组
     */
    private List<FactorListBean> factorList;

    public static class PayerEntity implements Serializable {
        /**
         * 付款人顺序号(从1开始，顺序递增)
         */
        private String payerNo;
        /**
         * 付款人客户号
         */
        private String payerCustId;
        /**
         * 付款人手机
         */
        private String payerMobile;
        /**
         * 付款人姓名
         */
        private String payerName;
        /**
         * 付款人类型(1：WEB渠道 2：手机渠道)
         */
        private String payerChannel;

        public String getPayerNo() {
            return payerNo;
        }

        public void setPayerNo(String payerNo) {
            this.payerNo = payerNo;
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

        public String getPayerChannel() {
            return payerChannel;
        }

        public void setPayerChannel(String payerChannel) {
            this.payerChannel = payerChannel;
        }
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public List<PayerEntity> getPayerList() {
        return payerList;
    }

    public void setPayerList(List<PayerEntity> payerList) {
        this.payerList = payerList;
    }

    public String get_combinName() {
        return _combinName;
    }

    public void set_combinName(String _combinName) {
        this._combinName = _combinName;
    }

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//

}
