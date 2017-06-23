package com.boc.bocsoft.mobile.bii.bus.account.model.psnSsmMessageChange;

import java.util.List;

/**
 * Created by wangtong on 2016/6/27.
 */
public class PsnSsmMessageChangeResult {

    private Object _certDN;
    private String smcTrigerInterval;
    private Object _plainData;

    private List<FactorListBean> factorList;

    public Object get_certDN() {
        return _certDN;
    }

    public void set_certDN(Object _certDN) {
        this._certDN = _certDN;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public Object get_plainData() {
        return _plainData;
    }

    public void set_plainData(Object _plainData) {
        this._plainData = _plainData;
    }

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

    public static class FactorListBean {
        /**
         * name : Smc
         * type : password
         */

        private FieldBean field;

        public FieldBean getField() {
            return field;
        }

        public void setField(FieldBean field) {
            this.field = field;
        }

        public static class FieldBean {
            private String name;
            private String type;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
