package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmSign;

import java.util.List;

/**
 * Created by wangtong on 2016/8/16.
 */
public class PsnSsmSignParam {

    private String conversationId;
    private String accountId;
    private String accountNumber;
    private String accAlias;
    private String accountInfo;
    private String feestandard;
    private String feetype;
    private String feeway;
    private int highloweramount;
    private boolean openStatus;
    private boolean isFree;
    private String token;
    private String _combinId;
    private String feeAccountNum;
    private String ssmfeeAccountId;
    private String Smc;
    private String Smc_RC;
    private String Otp;
    private String Otp_RC;
    private String activ;
    private String state;
    private String tabLan;
    private String deviceInfo;
    private String deviceInfo_RC;
    private String devicePrint;
    private String _signedData;

    private List<ListBean> list;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getDeviceInfo_RC() {
        return deviceInfo_RC;
    }

    public void setDeviceInfo_RC(String deviceInfo_RC) {
        this.deviceInfo_RC = deviceInfo_RC;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String otp_RC) {
        Otp_RC = otp_RC;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccAlias() {
        return accAlias;
    }

    public void setAccAlias(String accAlias) {
        this.accAlias = accAlias;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public String getFeestandard() {
        return feestandard;
    }

    public void setFeestandard(String feestandard) {
        this.feestandard = feestandard;
    }

    public String getFeetype() {
        return feetype;
    }

    public void setFeetype(String feetype) {
        this.feetype = feetype;
    }

    public String getFeeway() {
        return feeway;
    }

    public void setFeeway(String feeway) {
        this.feeway = feeway;
    }

    public int getHighloweramount() {
        return highloweramount;
    }

    public void setHighloweramount(int highloweramount) {
        this.highloweramount = highloweramount;
    }

    public boolean isOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(boolean openStatus) {
        this.openStatus = openStatus;
    }

    public boolean isIsFree() {
        return isFree;
    }

    public void setIsFree(boolean isFree) {
        this.isFree = isFree;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getFeeAccountNum() {
        return feeAccountNum;
    }

    public void setFeeAccountNum(String feeAccountNum) {
        this.feeAccountNum = feeAccountNum;
    }

    public String getSsmfeeAccountId() {
        return ssmfeeAccountId;
    }

    public void setSsmfeeAccountId(String ssmfeeAccountId) {
        this.ssmfeeAccountId = ssmfeeAccountId;
    }

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String Smc) {
        this.Smc = Smc;
    }

    public String getSmc_RC() {
        return Smc_RC;
    }

    public void setSmc_RC(String Smc_RC) {
        this.Smc_RC = Smc_RC;
    }

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTabLan() {
        return tabLan;
    }

    public void setTabLan(String tabLan) {
        this.tabLan = tabLan;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String accountId;
        private String ssmserviceid;
        private String pushchannel;
        private String pushterm;
        private String mobileConfirmCode;
        private String feestandard;
        private String feeway;
        private String feetype;
        private String loweramount;
        private String upperamount;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getSsmserviceid() {
            return ssmserviceid;
        }

        public void setSsmserviceid(String ssmserviceid) {
            this.ssmserviceid = ssmserviceid;
        }

        public String getPushchannel() {
            return pushchannel;
        }

        public void setPushchannel(String pushchannel) {
            this.pushchannel = pushchannel;
        }

        public String getPushterm() {
            return pushterm;
        }

        public void setPushterm(String pushterm) {
            this.pushterm = pushterm;
        }

        public String getMobileConfirmCode() {
            return mobileConfirmCode;
        }

        public void setMobileConfirmCode(String mobileConfirmCode) {
            this.mobileConfirmCode = mobileConfirmCode;
        }

        public String getFeestandard() {
            return feestandard;
        }

        public void setFeestandard(String feestandard) {
            this.feestandard = feestandard;
        }

        public String getFeeway() {
            return feeway;
        }

        public void setFeeway(String feeway) {
            this.feeway = feeway;
        }

        public String getFeetype() {
            return feetype;
        }

        public void setFeetype(String feetype) {
            this.feetype = feetype;
        }

        public String getLoweramount() {
            return loweramount;
        }

        public void setLoweramount(String loweramount) {
            this.loweramount = loweramount;
        }

        public String getUpperamount() {
            return upperamount;
        }

        public void setUpperamount(String upperamount) {
            this.upperamount = upperamount;
        }
    }
}
