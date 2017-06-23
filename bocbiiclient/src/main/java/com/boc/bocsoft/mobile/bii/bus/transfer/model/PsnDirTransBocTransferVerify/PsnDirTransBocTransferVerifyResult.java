package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;

import java.util.List;

/**
 * Created by WM on 2016/6/12.
 */
public class PsnDirTransBocTransferVerifyResult {

    /**
     * method : PsnDirTransBocTransferVerify
     * id : 8
     * status : 01
     * result : {"smcTrigerInterval":null,"toAccountType":"\u201d119\u201d","payeeBankNum":"\u201d40740\u201d","factorList":[{"field":{"name":"_signedData","type":"hidden"}}],"_plainData":"<?xml version=\"1.0\" encoding=\"UTF-8\"?><R xml:lang=\"zh_CN\"><S><I k=\"fromAccountId\" c=\"s\"><T>转出账户标识<\/T><D>100032106<\/D><\/I><V k=\"payeeActno\" c=\"s\"><T>收款人账号<\/T><D>99522222104<\/D><\/V><V k=\"payeeName\" c=\"s\"><T>收款人姓名<\/T><D>孙克克<\/D><\/V><V k=\"amount\" c=\"s\"><T>转账金额<\/T><D>0.12<\/D><\/V><I k=\"userId\"><D>gryh0025<\/D><\/I><I k=\"timestamp\"><D>2011-04-21 10:02:07.421<\/D><\/I><\/S><\/R>","_certDN":"CFCA0000000000000035"}
     * error : null
         * smcTrigerInterval : null
         * toAccountType : ”119”
         * payeeBankNum : ”40740”
         * factorList : [{"field":{"name":"_signedData","type":"hidden"}}]
         * _plainData : <?xml version="1.0" encoding="UTF-8"?><R xml:lang="zh_CN"><S><I k="fromAccountId" c="s"><T>转出账户标识</T><D>100032106</D></I><V k="payeeActno" c="s"><T>收款人账号</T><D>99522222104</D></V><V k="payeeName" c="s"><T>收款人姓名</T><D>孙克克</D></V><V k="amount" c="s"><T>转账金额</T><D>0.12</D></V><I k="userId"><D>gryh0025</D></I><I k="timestamp"><D>2011-04-21 10:02:07.421</D></I></S></R>
         * _certDN : CFCA0000000000000035
         */

            private String smcTrigerInterval;
            private String toAccountType;
            private String payeeBankNum;
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


}
