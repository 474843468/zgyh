package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

import java.util.List;

/**
 * 由044接口 PsnQueryTaAccountDetail   Ta账户信息查询
 * 上送参数：空
 * 返回参数：List<TaAccountBean>
 * Created by lyf7084 on 2016/12/13.
 */
public class QueryTaAccountDetailResModel {
    /**
     * 基金TA账户列表
     */
    private List<TaAccountBean> taAccountList;

    public List<TaAccountBean> getTaAccountList() {
        return taAccountList;
    }

    public void setTaAccountList(List<TaAccountBean> taAccountList) {
        this.taAccountList = taAccountList;
    }

    public class TaAccountBean {
        /**
         * 基金TA账户
         */
        private String taAccountNo;

        /**
         * 注册基金公司名称
         */
        private String fundRegName;

        /**
         * 注册基金公司代码
         */
        private String fundRegCode;

        /**
         * 账户状态
         * 00：正常
         * 01：销户处理中
         * 02：取消处理中
         * 03：冻结
         */
        private String accountStatus;

        /**
         * 是否有持仓
         * Y：是
         * N：否
         */
        private String isPosition;

        /**
         * 是否有在途交易
         * Y：是
         * N：否
         */
        private String isTrans;

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
        }

        public void setTaAccountNo(String taAccountNo) {
            this.taAccountNo = taAccountNo;
        }

        public void setIsPosition(String isPosition) {
            this.isPosition = isPosition;
        }

        public void setIsTrans(String isTrans) {
            this.isTrans = isTrans;
        }

        public void setFundRegName(String fundRegName) {
            this.fundRegName = fundRegName;
        }

        public String getAccountStatus() {
            return accountStatus;
        }

        public String getTaAccountNo() {
            return taAccountNo;
        }

        public String getIsPosition() {
            return isPosition;
        }

        public String getIsTrans() {
            return isTrans;
        }

        public String getFundRegName() {
            return fundRegName;
        }

        public String getFundRegCode() {
            return fundRegCode;
        }

        public void setFundRegCode(String fundRegCode) {
            this.fundRegCode = fundRegCode;
        }
    }
}
