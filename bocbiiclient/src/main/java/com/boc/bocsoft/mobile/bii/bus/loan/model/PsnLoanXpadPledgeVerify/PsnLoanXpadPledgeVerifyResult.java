package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeVerify;

import java.util.List;

/**
 * Created by XieDu on 2016/8/2.
 */
public class PsnLoanXpadPledgeVerifyResult {

    /**
     * CA的DN值
     */
    private Object _certDN;
    /**
     * 手机验证码有效时间
     */
    private String smcTrigerInterval;
    /**
     * CA加签数据XML报文
     */
    private Object _plainData;
    /**
     * 安全因子数组
     */
    private List<FactorListEntity> factorList;

    public Object get_certDN() { return _certDN;}

    public void set_certDN(Object _certDN) { this._certDN = _certDN;}

    public String getSmcTrigerInterval() { return smcTrigerInterval;}

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public Object get_plainData() { return _plainData;}

    public void set_plainData(Object _plainData) { this._plainData = _plainData;}

    public List<FactorListEntity> getFactorList() { return factorList;}

    public void setFactorList(List<FactorListEntity> factorList) { this.factorList = factorList;}

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
