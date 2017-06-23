package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryChinaBankAccount;

import java.util.List;

/**
 * Created by WYme on 2016/8/1.
 */
public class PsnQueryChinaBankAccountResult {

    /**
     * list : [{"accountId":183159755,"currencyCode2":null,"cardDescription":null,"hasOldAccountFlag":"0","accountName":"漫雪","accountNumber":"102058194370","accountIbkNum":"43016","accountType":"188","branchId":50978,"nickName":"活期一本通 ","accountStatus":"V","customerId":155575276,"currencyCode":"001","branchName":"陕西省分行营业部","cardDescriptionCode":null,"isECashAccount":null,"isMedicalAccount":"0","ecard":"0","verifyFactor":null},{"accountId":183159779,"currencyCode2":null,"cardDescription":null,"hasOldAccountFlag":"0","accountName":"漫雪","accountNumber":"102458194659","accountIbkNum":"43016","accountType":"101","branchId":50978,"nickName":"普通活期","accountStatus":"V","customerId":155575276,"currencyCode":"001","branchName":"陕西省分行营业部","cardDescriptionCode":null,"isECashAccount":null,"isMedicalAccount":"0","ecard":"0","verifyFactor":null},{"accountId":183160941,"currencyCode2":null,"cardDescription":null,"hasOldAccountFlag":"0","accountName":"漫雪","accountNumber":"4563513600036772275","accountIbkNum":"43016","accountType":"119","branchId":50978,"nickName":"长城电子借记卡 ","accountStatus":"V","customerId":155575276,"currencyCode":"001","branchName":"陕西省分行营业部","cardDescriptionCode":null,"isECashAccount":null,"isMedicalAccount":"0","ecard":"0","verifyFactor":null},{"accountId":183784663,"currencyCode2":null,"cardDescription":null,"hasOldAccountFlag":"0","accountName":"漫雪","accountNumber":"103258208915","accountIbkNum":"43016","accountType":"188","branchId":1942,"nickName":"活期一本通","accountStatus":"V","customerId":155575276,"currencyCode":"001","branchName":"中国银行陕西省分行","cardDescriptionCode":null,"isECashAccount":null,"isMedicalAccount":"0","ecard":"0","verifyFactor":null}]
     * recordCount : 4
     */

    private int recordCount;
    /**
     * accountId : 183159755
     * currencyCode2 : null
     * cardDescription : null
     * hasOldAccountFlag : 0
     * accountName : 漫雪
     * accountNumber : 102058194370
     * accountIbkNum : 43016
     * accountType : 188
     * branchId : 50978
     * nickName : 活期一本通
     * accountStatus : V
     * customerId : 155575276
     * currencyCode : 001
     * branchName : 陕西省分行营业部
     * cardDescriptionCode : null
     * isECashAccount : null
     * isMedicalAccount : 0
     * ecard : 0
     * verifyFactor : null
     */

    private List<AccountBean> list;

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public List<AccountBean> getList() {
        return list;
    }

    public void setList(List<AccountBean> list) {
        this.list = list;
    }

    public static class AccountBean {
        private int accountId;
        private String currencyCode2;
        private String cardDescription;
        private String hasOldAccountFlag;
        private String accountName;
        private String accountNumber;
        private String accountIbkNum;
        private String accountType;
        private int branchId;
        private String nickName;
        private String accountStatus;
        private int customerId;
        private String currencyCode;
        private String branchName;
        private String cardDescriptionCode;
        private String isECashAccount;
        private String isMedicalAccount;
        private String ecard;
        private String verifyFactor;

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public Object getCurrencyCode2() {
            return currencyCode2;
        }

        public void setCurrencyCode2(String currencyCode2) {
            this.currencyCode2 = currencyCode2;
        }

        public String getCardDescription() {
            return cardDescription;
        }

        public void setCardDescription(String cardDescription) {
            this.cardDescription = cardDescription;
        }

        public String getHasOldAccountFlag() {
            return hasOldAccountFlag;
        }

        public void setHasOldAccountFlag(String hasOldAccountFlag) {
            this.hasOldAccountFlag = hasOldAccountFlag;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAccountIbkNum() {
            return accountIbkNum;
        }

        public void setAccountIbkNum(String accountIbkNum) {
            this.accountIbkNum = accountIbkNum;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public int getBranchId() {
            return branchId;
        }

        public void setBranchId(int branchId) {
            this.branchId = branchId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public Object getCardDescriptionCode() {
            return cardDescriptionCode;
        }

        public void setCardDescriptionCode(String cardDescriptionCode) {
            this.cardDescriptionCode = cardDescriptionCode;
        }

        public String getIsECashAccount() {
            return isECashAccount;
        }

        public void setIsECashAccount(String isECashAccount) {
            this.isECashAccount = isECashAccount;
        }

        public String getIsMedicalAccount() {
            return isMedicalAccount;
        }

        public void setIsMedicalAccount(String isMedicalAccount) {
            this.isMedicalAccount = isMedicalAccount;
        }

        public String getEcard() {
            return ecard;
        }

        public void setEcard(String ecard) {
            this.ecard = ecard;
        }

        public String getVerifyFactor() {
            return verifyFactor;
        }

        public void setVerifyFactor(String verifyFactor) {
            this.verifyFactor = verifyFactor;
        }
    }
}
