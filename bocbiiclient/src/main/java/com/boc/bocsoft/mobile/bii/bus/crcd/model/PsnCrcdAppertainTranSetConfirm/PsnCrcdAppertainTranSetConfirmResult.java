package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranSetConfirm;

import java.util.List;

/**
 * Name: liukai
 * Time：2016/12/2 15:30.
 * Created by lk7066 on 2016/12/2.
 * It's used to
 */

public class PsnCrcdAppertainTranSetConfirmResult {

    private String _certDN;
    private String smcTrigerInterval;
    private String _plainData;
    private List<FactorListBean> factorList;

    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
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

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

    public static class FactorListBean {

        private FieldBean field;

        public FieldBean getField() {
            return field;
        }

    }

    public static class FieldBean{

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
