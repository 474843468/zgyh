package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model;

import java.util.List;

/**
 * ViewModel：中银理财-组合购买已押押品查询
 * Created by zhx on 2016/9/20
 */
public class XpadQueryGuarantyProductViewModel {
    // 组合交易流水号
    private String tranSeq;
    // 账号缓存标识
    private String accountKey;
    // 省行联行号
    private String ibknum;
    // 账户类型（SY-活一本交易 CD-借记卡交易 MW-网上专属理财 GW-长城信用卡 必输）
    private String typeOfAccount;

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public void setIbknum(String ibknum) {
        this.ibknum = ibknum;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }

    public void setTypeOfAccount(String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String getIbknum() {
        return ibknum;
    }

    public String getTranSeq() {
        return tranSeq;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    private List<GuarantyProductEntity> list;

    public List<GuarantyProductEntity> getList() {
        return list;
    }

    public void setList(List<GuarantyProductEntity> list) {
        this.list = list;
    }

    public static class GuarantyProductEntity {
        // 押品代码
        private String prodCode;
        // 押品名称
        private String prodName;
        // 币种（001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
        private String curCode;
        // 钞汇标识（1：钞 2：汇 0：人民币）
        private String cashRemit;
        // 押品份额
        private String freezeUnit;
        // 质押日期
        private String prodBegin;
        // 押品到期日
        private String prodEnd;
        // 组合购买状态（0：未成交 1：成交成功 2：成交失败）
        private String impawnPermit;

        public void setFreezeUnit(String freezeUnit) {
            this.freezeUnit = freezeUnit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public void setImpawnPermit(String impawnPermit) {
            this.impawnPermit = impawnPermit;
        }

        public void setProdEnd(String prodEnd) {
            this.prodEnd = prodEnd;
        }

        public void setCurCode(String curCode) {
            this.curCode = curCode;
        }

        public void setProdCode(String prodCode) {
            this.prodCode = prodCode;
        }

        public void setProdBegin(String prodBegin) {
            this.prodBegin = prodBegin;
        }

        public String getFreezeUnit() {
            return freezeUnit;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public String getProdName() {
            return prodName;
        }

        public String getImpawnPermit() {
            return impawnPermit;
        }

        public String getProdEnd() {
            return prodEnd;
        }

        public String getCurCode() {
            return curCode;
        }

        public String getProdCode() {
            return prodCode;
        }

        public String getProdBegin() {
            return prodBegin;
        }
    }
}
