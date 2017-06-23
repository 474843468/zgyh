package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery;

import android.annotation.SuppressLint;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * I42-4.37 037 查询客户理财账户信息PsnXpadAccountQuery -响应model
 * Created by yx on 2016/9/16
 */
public class PsnXpadAccountQueryResModel implements Serializable {

    /**
     * list : [{"accountId":140538260,"ibkNumber":"40142","xpadAccount":"2100*********8025","accountNo":"6217***********9944","accountType":"119","bancID":"20017","xpadAccountSatus":"1","accountKey":"2a7108d9-2560-4dd4-aa50-08bbaae5e38a"},{"accountId":137096858,"ibkNumber":"40142","xpadAccount":"2100*********8025","accountNo":"3285****1463","accountType":"190","bancID":"02027","xpadAccountSatus":"1","accountKey":"50ed81af-58bf-482b-b36f-77cbe6265859"}]
     */
    private List<XPadAccountEntity> list = new ArrayList<XPadAccountEntity>();

    public void setList(List<XPadAccountEntity> list) {
        this.list = list;
    }

    public List<XPadAccountEntity> getList() {
        return list;
    }

    //客户理财账户信息 -单条
    @SuppressLint("ParcelCreator")
    public static class XPadAccountEntity extends AccountBean implements  Serializable{
        /**
         * accountId : 140538260
         * ibkNumber : 40142
         * xpadAccount : 2100*********8025
         * accountNo : 6217***********9944
         * accountType : 119
         * bancID : 20017
         * xpadAccountSatus : 1
         * accountKey : 2a7108d9-2560-4dd4-aa50-08bbaae5e38a
         */

        /**
         * ibkNumber	省行联行号	String
         */
        private String ibkNumber;
        /**
         * xpadAccount	客户理财账户	String	加星
         */
        private String xpadAccount;
        /**
         * accountNo	资金账号	String	加星
         */
        private String accountNo;
        /**
         * bancID	账户开户行	String	核心系统中的账号开户行
         */
        private String bancID;
        /**
         * xpadAccountSatus	账户状态	String	0：停用;1：可用
         */
        private String xpadAccountSatus;
        /**
         * accountKey	账户缓存标识	String	所有账户均返回，客户非敏感数据
         */
        private String accountKey;

        public void setIbkNumber(String ibkNumber) {
            this.ibkNumber = ibkNumber;
        }

        public void setXpadAccount(String xpadAccount) {
            this.xpadAccount = xpadAccount;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public void setBancID(String bancID) {
            this.bancID = bancID;
        }

        public void setXpadAccountSatus(String xpadAccountSatus) {
            this.xpadAccountSatus = xpadAccountSatus;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }

        public String getIbkNumber() {
            return ibkNumber;
        }

        public String getXpadAccount() {
            return xpadAccount;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public String getBancID() {
            return bancID;
        }

        public String getXpadAccountSatus() {
            return xpadAccountSatus;
        }

        public String getAccountKey() {
            return accountKey;
        }
    }
}
