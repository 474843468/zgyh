package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceConfirm;

import java.util.List;

/**
 * Created by yangle on 2016/11/22.
 */
public class PsnCrcdDividedPayAdvanceConfirmResult {

    /**
     * field : {"name":"Otp","type":"password"}
     */

    private List<FactorListBean> factorList;
    private String smcTrigerInterval;
    private String _plainData;
    private String _certDN;

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

    public static class FactorListBean {
        /**
         * name : Otp
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
