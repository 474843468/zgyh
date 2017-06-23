package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryExternalBankInfo;

import java.util.List;

/**
 * Created by WM on 2016/6/16.
 */
public class PsnTransQueryExternalBankInfoResult {

    /**
     * list : [{"bankName":"中国农业银行喀什兵团支行营业部","cnapsCode":"103894078326"},{"bankName":"中国农业银行即墨市支行城北分理处","cnapsCode":"03452112064"}]
     * recordnumber : 13173
     */

    private Integer recordnumber;
    /**
     * bankName : 中国农业银行喀什兵团支行营业部
     * cnapsCode : 103894078326
     */

    private List<OpenBankBean> list;

    public Integer getRecordnumber() {
        return recordnumber;
    }

    public void setRecordnumber(Integer recordnumber) {
        this.recordnumber = recordnumber;
    }

    public List<OpenBankBean> getList() {
        return list;
    }

    public void setList(List<OpenBankBean> list) {
        this.list = list;
    }

    public static class OpenBankBean {
        private String bankName;
        private String cnapsCode;

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getCnapsCode() {
            return cnapsCode;
        }

        public void setCnapsCode(String cnapsCode) {
            this.cnapsCode = cnapsCode;
        }
    }
}
