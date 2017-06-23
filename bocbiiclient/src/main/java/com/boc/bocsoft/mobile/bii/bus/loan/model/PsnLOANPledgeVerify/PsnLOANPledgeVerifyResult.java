package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeVerify;

import java.util.List;

/**
 * 存款质押贷款预交易
 * Created by XieDu on 2016/8/2.
 */
public class PsnLOANPledgeVerifyResult {

    /**
     * CA加签数据XML报文
     */
    private String _plainData;
    /**
     * 手机验证码有效时间
     */
    private String smcTrigerInterval;
    /**
     * CA的DN值
     */
    private String _certDN;
    /**
     * name值"Smc"-需要输入手机验证码值为"Otp"-需要输入动态口令
     */
    private List<FactorListEntity> factorList;

    public String get_plainData() { return _plainData;}

    public void set_plainData(String _plainData) { this._plainData = _plainData;}

    public String getSmcTrigerInterval() { return smcTrigerInterval;}

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public String get_certDN() { return _certDN;}

    public void set_certDN(String _certDN) { this._certDN = _certDN;}

    public List<FactorListEntity> getFactorList() { return factorList;}

    public void setFactorList(List<FactorListEntity> factorList) { this.factorList = factorList;}

    public static class FactorListEntity {

        private FieldEntity field;

        public FieldEntity getField() { return field;}

        public void setField(FieldEntity field) { this.field = field;}

        public static class FieldEntity {
            private String name;
            private String type;

            public String getName() { return name;}

            public void setName(String name) { this.name = name;}

            public String getType() { return type;}

            public void setType(String type) { this.type = type;}
        }
    }
}
