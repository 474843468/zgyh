package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;

/**
 * Created by xdy4486
 */
public class PsnTransBocTransferVerifyResult {


    /**
     * 手机验证码有效时间
     */
    private String smcTrigerInterval;
    /**
     * 收款账户类型
     */
    private String toAccountType;
    /**
     * 收款行转入账户地区
     */
    private String payeeBankNum;
    /**
     * CA加签数据XML报文
     */
    private String _plainData;
    /**
     * CA的DN值
     */
    private String _certDN;
    /**
     *是否验证验证账户的ATM取款密码
     */
    private String needPassword;

    /**
     * 安全因子数组
     */
    private List<FactorListBean> factorList;

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public String getPayeeBankNum() {
        return payeeBankNum;
    }

    public void setPayeeBankNum(String payeeBankNum) {
        this.payeeBankNum = payeeBankNum;
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

    public String getNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(String needPassword) {
        this.needPassword = needPassword;
    }
//
//    public static class FactorListBean {
//        /**
//         * name : _signedData
//         * type : hidden
//         */
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
//            /**
//             * name值"Smc"-需要输入手机验证码，值为"Otp"-需要输入动态口令
//             */
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
