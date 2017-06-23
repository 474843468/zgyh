package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetConfirm;


import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lq7090 on 2016/11/17.
 */
public class PsnCrcdDividedPayBillSetConfirmResult {


    /**
     * chargeMode : 1
     * minimalAmountNow : 500
     * firstInAmount : 1000
     * instmtCharge : 1
     * restAmount : 1234
     * restPerTimeInAmount : 521
     * securityMap : {"smcTrigerInterval":"60","factorList":[{"field":{"name":"Smc","type":"password"}},{"field":{"name":"Otp","type":"password"}}],"_plainData":null,"_certDN":null}
     */

    private String chargeMode;
    private BigDecimal minimalAmountNow;
    private BigDecimal firstInAmount;
    private BigDecimal instmtCharge;
    private BigDecimal restAmount;
    private BigDecimal restPerTimeInAmount;


    /**
     * _certDN : null
     * smcTrigerInterval : null
     * _plainData : null
     * factorList : [{"field":{"name":"Otp","type":"password"}}]
     */

    private SecurityMapBean securityMap;

    /**
     * smcTrigerInterval : 60
     * factorList : [{"field":{"name":"Smc","type":"password"}},{"field":{"name":"Otp","type":"password"}}]
     * _plainData : null
     * _certDN : null
     */


    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public BigDecimal getMinimalAmountNow() {
        return minimalAmountNow;
    }

    public void setMinimalAmountNow(BigDecimal minimalAmountNow) {
        this.minimalAmountNow = minimalAmountNow;
    }

    public BigDecimal getFirstInAmount() {
        return firstInAmount;
    }

    public void setFirstInAmount(BigDecimal firstInAmount) {
        this.firstInAmount = firstInAmount;
    }

    public BigDecimal getInstmtCharge() {
        return instmtCharge;
    }

    public void setInstmtCharge(BigDecimal instmtCharge) {
        this.instmtCharge = instmtCharge;
    }

    public BigDecimal getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(BigDecimal restAmount) {
        this.restAmount = restAmount;
    }

    public BigDecimal getRestPerTimeInAmount() {
        return restPerTimeInAmount;
    }

    public void setRestPerTimeInAmount(BigDecimal restPerTimeInAmount) {
        this.restPerTimeInAmount = restPerTimeInAmount;
    }


//
//    public String getSmcTrigerInterval() {
//        return smcTrigerInterval;
//    }
//
//    public void setSmcTrigerInterval(String smcTrigerInterval) {
//        this.smcTrigerInterval = smcTrigerInterval;
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
//    public String get_certDN() {
//        return _certDN;
//    }
//
//    public void set_certDN(String _certDN) {
//        this._certDN = _certDN;
//    }
//
//    public List<FactorListBean> getFactorList() {
//        return factorList;
//    }
//
//    public void setFactorList(List<FactorListBean> factorList) {
//        this.factorList = factorList;
//    }

    public SecurityMapBean getSecurityMap() {
        return securityMap;
    }

    public void setSecurityMap(SecurityMapBean securityMap) {
        this.securityMap = securityMap;
    }
//
//    public static class FactorListBean {
//        private FieldBean field;
//
//        public FieldBean getField() {
//            return field;
//        }
//
//        public void setField(FieldBean field) {
//            this.field = field;
//        }
//    }
//
//    public static class FieldBean{
//
//
//        /**
//         * name : 安全因子名称
//         * type : 安全因子类型
//         */
//        private String name;
//        private String type;
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//    }


    public static class SecurityMapBean {
        private String _certDN;
        private String smcTrigerInterval;
        private String _plainData;
        /**
         * field : {"name":"Otp","type":"password"}
         */

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
}



