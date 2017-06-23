package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;

/**
 * Created by WM on 2016/6/24.
 */
public class PsnEbpsRealTimePaymentConfirmResult {

    /**
     * smcTrigerInterval : null
     * factorList : [{"field":{"name":"_signedData","type":"hidden"}}]
     * _plainData : <?xml version="1.0" encoding="UTF-8"?><Rxml:lang="zh_CN"><S><V k="amount" c="s"><T>转账金额</T><D>123.00</D></V><I k="currency" c="s"><T>交易币种</T><D>001</D></I><Ik="userId"><D>lixd</D></I><I k="timestamp"><D>2011-04-21 16:30:38.703</D></I></S></R>
     * _certDN : CFCA0000000000000030
     */

    private String smcTrigerInterval;
    private String _plainData;
    private String _certDN;

    public String getNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(String needPassword) {
        this.needPassword = needPassword;
    }

    private String needPassword;
    /**
     * field : {"name":"_signedData","type":"hidden"}
     */

    private List<FactorListBean> factorList;

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

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

//    public static class FactorListBean {
//        /**
//         * name : _signedData
//         * type : hidden
//         */
//
//        private FieldBean field;
//
//        public FieldBean getField() {
//            return field;
//        }
//
//        public void setField(FieldBean field) {
//            this.field = field;
//        }
//
//        public static class FieldBean {
//            private String name;
//            private String type;
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//
//            public String getType() {
//                return type;
//            }
//
//            public void setType(String type) {
//                this.type = type;
//            }
//        }
//    }
}
