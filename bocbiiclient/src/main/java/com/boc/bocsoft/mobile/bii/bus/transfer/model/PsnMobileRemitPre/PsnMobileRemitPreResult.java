package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitPre;

import java.util.ArrayList;
import java.util.List;

/**
 * 汇往取款人预交易响应
 *
 * Created by liuweidong on 2016/7/12.
 */
public class PsnMobileRemitPreResult {

    /**
     * CA的DN值
     */
    private String _certDN;
    /**
     * 手机验证码有效时间
     */
    private String smcTrigerInterval;
    /**
     * CA加签数据XML
     */
    private String _plainData;

    private List<FactorListBean> factorList = new ArrayList<FactorListBean>();

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
