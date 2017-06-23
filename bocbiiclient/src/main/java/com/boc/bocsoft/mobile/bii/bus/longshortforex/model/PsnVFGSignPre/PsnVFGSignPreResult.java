package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignPre;

import java.util.List;

/**
 * Result：双向宝签约预交易
 * Created by zhx on 2016/11/21
 */
public class PsnVFGSignPreResult {

    /**
     * factorList : [{"field":{"name":"Smc","type":"password"}},{"field":{"name":"Otp","type":"password"}}]
     * _plainData : null
     * smcTrigerInterval : 60
     * _certDN : null
     */
    // 安全因子数组(name值"Smc"—需要输入手机验证码 值为"Otp"—需要输入动态口令 值为” _signedData”—需要CA认证)
    private List<FactorListEntity> factorList;
    // 手机验证码有效时间
    private String smcTrigerInterval;
    // CA加签数据XML报文
    private String _plainData;
    // CA的DN值
    private String _certDN;

    public void setFactorList(List<FactorListEntity> factorList) {
        this.factorList = factorList;
    }

    public void set_plainData(String _plainData) {
        this._plainData = _plainData;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
    }

    public List<FactorListEntity> getFactorList() {
        return factorList;
    }

    public String get_plainData() {
        return _plainData;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public String get_certDN() {
        return _certDN;
    }

    public static class FactorListEntity {
        /**
         * field : {"name":"Smc","type":"password"}
         */
        private FieldEntity field;

        public void setField(FieldEntity field) {
            this.field = field;
        }

        public FieldEntity getField() {
            return field;
        }

        public static class FieldEntity {
            /**
             * name : Smc
             * type : password
             */
            private String name;
            private String type;

            public void setName(String name) {
                this.name = name;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public String getType() {
                return type;
            }
        }
    }
}
