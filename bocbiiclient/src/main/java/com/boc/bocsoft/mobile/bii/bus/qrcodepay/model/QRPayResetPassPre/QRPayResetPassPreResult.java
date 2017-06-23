//package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayResetPassPre;
//
//import java.util.List;
//
///**
// * 重置支付密码预交易
// * Created by wangf on 2016/9/12.
// */
//public class QRPayResetPassPreResult {
//
//    //CA的DN值
//    private String _certDN;
//    //CA加签数据XML报文
//    private String _plainData;
//    //手机验证码有效时间
//    private String smcTrigerInterval;
//    //安全因子数组
//    private List<FactorListBean> factorList;
//
//    public String get_certDN() {
//        return _certDN;
//    }
//
//    public void set_certDN(String _certDN) {
//        this._certDN = _certDN;
//    }
//
//    public String get_plainData() {
//        return _plainData;
//    }
//
//    public void set_plainData(String _plainData) {
//        this._plainData = _plainData;
//    }
//
//    public String getSmcTrigerInterval() {
//        return smcTrigerInterval;
//    }
//
//    public void setSmcTrigerInterval(String smcTrigerInterval) {
//        this.smcTrigerInterval = smcTrigerInterval;
//    }
//
//    public List<FactorListBean> getFactorList() {
//        return factorList;
//    }
//
//    public void setFactorList(List<FactorListBean> factorList) {
//        this.factorList = factorList;
//    }
//
//    public static class FactorListBean {
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
//
//}
