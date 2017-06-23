package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplySubmit;

import java.math.BigDecimal;

public class VirCard{
        /** 实体卡号 */
        private String creditCardNo;
        /** 虚拟卡号 */
        private String virtualCardNo;
        /** 生效日期 */
        private String startDate;
        /** 失效日期 */
        private String endDate;
        /** 单笔交易限额 */
        private BigDecimal singLeamt;
        /** 累计交易限额 */
        private BigDecimal totaLeamt;
        /** 已累计交易限额 */
        private BigDecimal atotaLeamt;
        /** 状态 1 有效,2 已注销 */
        private String status;
        /** 建卡渠道  4=家居银行,2=手机银行,1=网上银行,CSR=电话银行,EISS=客服人工,JFEN=积分365,BCSP=缤纷生活 */
        private String creatChannel;
        /** 关联网银状态  1 成功,0 失败 */
        private String isRelatedNetwork;

        private String lastUpdateUser;

        private String lastUpdates;

        public String getLastUpdates() {
            return lastUpdates;
        }

        public void setLastUpdates(String lastUpdates) {
            this.lastUpdates = lastUpdates;
        }

        public String getLastUpdateUser() {
            return lastUpdateUser;
        }

        public void setLastUpdateUser(String lastUpdateUser) {
            this.lastUpdateUser = lastUpdateUser;
        }

        public String getCreditCardNo() {
            return creditCardNo;
        }

        public void setCreditCardNo(String creditCardNo) {
            this.creditCardNo = creditCardNo;
        }

        public String getVirtualCardNo() {
            return virtualCardNo;
        }

        public void setVirtualCardNo(String virtualCardNo) {
            this.virtualCardNo = virtualCardNo;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public BigDecimal getSingLeamt() {
            return singLeamt;
        }

        public void setSingLeamt(BigDecimal singLeamt) {
            this.singLeamt = singLeamt;
        }

        public BigDecimal getTotaLeamt() {
            return totaLeamt;
        }

        public void setTotaLeamt(BigDecimal totaLeamt) {
            this.totaLeamt = totaLeamt;
        }

        public BigDecimal getAtotaLeamt() {
            return atotaLeamt;
        }

        public void setAtotaLeamt(BigDecimal atotaLeamt) {
            this.atotaLeamt = atotaLeamt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatChannel() {
            return creatChannel;
        }

        public void setCreatChannel(String creatChannel) {
            this.creatChannel = creatChannel;
        }

        public String getIsRelatedNetwork() {
            return isRelatedNetwork;
        }

        public void setIsRelatedNetwork(String isRelatedNetwork) {
            this.isRelatedNetwork = isRelatedNetwork;
        }
    }