package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPaymentUnsignVerify;

import java.util.List;

/**
 * 作者：XieDu
 * 创建时间：2016/9/5 15:10
 * 描述：支付解约预交易结果
 */
public class PsnLoanPaymentUnsignVerifyResult {
    /**
     * 安全因子数组
     */
    private List<FactorListEntity> factorList;
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

    public List<FactorListEntity> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListEntity> factorList) {
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

    public static class FactorListEntity {
        /**
         * name : Smc
         * type : password
         */

        private FieldEntity field;

        public FieldEntity getField() { return field;}

        public void setField(FieldEntity field) { this.field = field;}

        public static class FieldEntity {
            private String name;
            private String type;

            public String getName() { return name;}

            public void setName(String name) { this.name = name;}

            public String getType() { return type;}

            public void setType(String type) { this.type = type;}
        }
    }
}
