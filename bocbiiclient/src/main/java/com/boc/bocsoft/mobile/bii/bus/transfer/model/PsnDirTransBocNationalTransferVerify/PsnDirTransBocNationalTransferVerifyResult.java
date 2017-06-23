package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;

/**
 * Created by WM on 2016/6/16.
 * 国内跨行定向汇款预交易
 */
public class PsnDirTransBocNationalTransferVerifyResult {

    /**
     * smcTrigerInterval : null
     * factorList : [{"field":{"name":"_signedData","type":"hidden"}}]
     * _plainData : <?xml version="1.0" encoding="UTF-8"?><R xml:lang="zh_CN"><S><I k="fromAccountId" c="s"><T>转出账户标识</T><D>100032106</D></I><V k="payeeActno" c="s"><T>收款人账号</T><D>99522222107</D></V><V k="payeeName" c="s"><T>收款人姓名</T><D>1</D></V><V k="amount" c="s"><T>转账金额</T><D>300.55</D></V><I k="userId"><D>gryh0025</D></I><I k="timestamp"><D>2011-04-21 10:53:07.125</D></I></S></R>
     * _certDN : CFCA0000000000000035
     */

    private String smcTrigerInterval;
    private String _plainData;
    private String _certDN;
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
