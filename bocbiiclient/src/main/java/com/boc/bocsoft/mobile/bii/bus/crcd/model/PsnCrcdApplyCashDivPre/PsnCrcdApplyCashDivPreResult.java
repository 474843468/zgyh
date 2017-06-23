package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdApplyCashDivPre;

import java.util.List;

/**
 * Created by cry7096 on 2016/11/22.
 */
public class PsnCrcdApplyCashDivPreResult {
    /**
     * _certDN : CA的DN值
     * smcTrigerInterval : 安全因子数组
     * _plainData : CA加签数据XML报文
     * factorList : 安全因子列表
     */
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


        /**
         * name : 安全因子名称
         * type : 安全因子类型
         */
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
