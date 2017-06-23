package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationVerify;

import java.util.List;

/**
 * Created by yangle on 2016/12/15.
 * 描述:
 */
public class PsnHCEQuickPassActivationVerifyResult {

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

        private FieldBean field;

        public FactorListBean(FieldBean field) {
            this.field = field;
        }

        public FactorListBean() {
        }

        public FieldBean getField() {
            return field;
        }

        public void setField(FieldBean field) {
            this.field = field;
        }

        public static class FieldBean {
            /**
             * name : Otp
             * type : password
             */
            private String name;
            private String type;

            public FieldBean(String name, String type) {
                this.name = name;
                this.type = type;
            }
            public FieldBean() {
            }

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
