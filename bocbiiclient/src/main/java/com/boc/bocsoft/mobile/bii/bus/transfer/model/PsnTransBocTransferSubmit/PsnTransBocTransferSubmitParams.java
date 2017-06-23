package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferSubmit;

/**
 * Created by Wangyuan on 2016/6/12.
 */



    public  class PsnTransBocTransferSubmitParams {


    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    private String executeDate;
    private String dueDate;
    /**
         * fromAccountId : 183159755
         * payeeName : 赵信
         * payeeActno : 4563513600036764876
         * toAccountType : 119
         * payeeBankNum : 43016
         * payeeId :
         * amount : 14.00
         * currency : 001
         * remark : 转账转账
         * executeType : 0
         * _signedData : null
         * token : gjwtcagu
         * devicePrint : version%3D3%2E4%2E0%2E0%5F2%26pm%5Ffpua%3Dmozilla%2F5%2E0%20%28windows%20nt%206%2E1%3B%20wow64%3B%20trident%2F7%2E0%3B%20rv%3A11%2E0%29%20like%20gecko%7C5%2E0%20%28Windows%20NT%206%2E1%3B%20WOW64%3B%20Trident%2F7%2E0%3B%20rv%3A11%2E0%29%20like%20Gecko%7CWin32%26pm%5Ffpsc%3D24%7C1366%7C768%7C728%26pm%5Ffpsw%3D%26pm%5Ffptz%3D8%26pm%5Ffpln%3Dlang%3Dzh%2DCN%7Csyslang%3Dzh%2DCN%7Cuserlang%3Dzh%2DCN%26pm%5Ffpjv%3D1%26pm%5Ffpco%3D1%26pm%5Ffpasw%3D%26pm%5Ffpan%3DMicrosoft%20Internet%20Explorer%26pm%5Ffpacn%3DMozilla%26pm%5Ffpol%3Dtrue%26pm%5Ffposp%3D%26pm%5Ffpup%3D%26pm%5Ffpsaw%3D1366%26pm%5Ffpspd%3D24%26pm%5Ffpsbd%3D0%26pm%5Ffpsdx%3D96%26pm%5Ffpsdy%3D96%26pm%5Ffpslx%3D96%26pm%5Ffpsly%3D96%26pm%5Ffpsfse%3Dtrue%26pm%5Ffpsui%3D0%26pm%5Fos%3DWindows%26pm%5Fbrmjv%3DNaN%26pm%5Fbr%3DMozilla%26pm%5Finpt%3D2029%26pm%5Fexpt%3D%2D1
         * payeeMobile : 17878789988
         * activ : 200102051
         * state : TUFDPTI4LTkyLTRhLTQ4LTY3LWJkO0lQPTAuMC4wLjA7TUFDPTg0LTRiLWY1LTU0LTExLTU4O0lQ
         PTE5Mi4xNjguMC4xMzM7RElTS0lEPTUyMmFmNjlhO0JPQ0dVSUQ9ezAzNjBBRDZDLTIxQTUtNDE3
         QS1CMUU1LTcwOUI5NTc2QkNGNX07T1M9NzYwMSh4ODYpO0lFPTExLjAuOTYwMC4xNjQyODtTVEFU
         RUNPREU9MDAwMDAwMDA7

         * Otp : zRdiHuoTdrqaJuTD+zHwTA==
         * Otp_RC : M9pKiinSvVdo48L8eEcFPi3gC2fCRVnsTe6VJ2FJ90xx5/2yVVNC4WsewzTqaEKhE6kMgFw12B4Y
         A7WrMz78rLfXpsC8z+VFaLW0+EItYkmvJnGHqMs/Rja+r6NNI/cnVXunNfabtm1LIYvZlBbKbA==


         * Smc : AnpiN5OdpVqnbIqA5v+XRA==
         * Smc_RC : Fh6jNrsgNFw/mNSAdxWMC3EkOiXlOHmzJ+WSmLNYhXFEPIuNYMBkhFgt1YwQUBQ6L7Vm4VOJEwY0
         wThEOXcmug1WWScNaKq+dgCH5sOIlGC/6xMwb+t8gkUZsT/W6GmwrYknTdzZmjY1zhgVwIPM1A==
         * passbookPassword : C/pCdhLR6+OZVkj/7OAAlQ==
         * passbookPassword_RC : inb8pBg+UqnFyWRk/Vad/W1lYKIPsETvTLPeVlAIEX9tUQNMVC6x80DitEUHTZKsw29MQRNBNqgm
         9urQr5KfQbFXgDBYSDx9AGC33c6Tc5z8vaKT6zF+kYYoGk+NyiQNH8lO5vMma+/kEiy8Qot3Xg==
         */


        private String fromAccountId;
        private String payeeName;
        private String payeeActno;
        private String toAccountType;
        private String payeeBankNum;
        private String payeeId;
        private String amount;
        private String currency;
        private String remark;
        private String executeType;
        private String _signedData;
        private String token;
        private String devicePrint;
        private String payeeMobile;
        private String activ;
        private String state;
        private String Smc;
        private String Smc_RC;
        private String passbookPassword;
        private String passbookPassword_RC;
    private String atmPassword;
    private String atmPassword_RC;
    private String phoneBankPassword;
    private String phoneBankPassword_RC;

    private String Otp;
    private String Otp_RC;
    private String conversationId;

    private String deviceInfo;
    private String deviceInfo_RC;

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


    public String getPhoneBankPassword_RC() {
        return phoneBankPassword_RC;
    }

    public void setPhoneBankPassword_RC(String phoneBankPassword_RC) {
        this.phoneBankPassword_RC = phoneBankPassword_RC;
    }

    public String getAtmPassword_RC() {
        return atmPassword_RC;
    }

    public void setAtmPassword_RC(String atmPassword_RC) {
        this.atmPassword_RC = atmPassword_RC;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


    public String getAtmPassword() {
        return atmPassword;
    }

    public void setAtmPassword(String atmPassword) {
        this.atmPassword = atmPassword;
    }

    public String getPhoneBankPassword() {
        return phoneBankPassword;
    }

    public void setPhoneBankPassword(String phoneBankPassword) {
        this.phoneBankPassword = phoneBankPassword;
    }


        public String getFromAccountId() {
            return fromAccountId;
        }

        public void setFromAccountId(String fromAccountId) {
            this.fromAccountId = fromAccountId;
        }

        public String getPayeeName() {
            return payeeName;
        }

        public void setPayeeName(String payeeName) {
            this.payeeName = payeeName;
        }

        public String getPayeeActno() {
            return payeeActno;
        }

        public void setPayeeActno(String payeeActno) {
            this.payeeActno = payeeActno;
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

        public String getPayeeId() {
            return payeeId;
        }

        public void setPayeeId(String payeeId) {
            this.payeeId = payeeId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getExecuteType() {
            return executeType;
        }

        public void setExecuteType(String executeType) {
            this.executeType = executeType;
        }

        public String get_signedData() {
            return _signedData;
        }

        public void set_signedData(String _signedData) {
            this._signedData = _signedData;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getDevicePrint() {
            return devicePrint;
        }

        public void setDevicePrint(String devicePrint) {
            this.devicePrint = devicePrint;
        }

        public String getPayeeMobile() {
            return payeeMobile;
        }

        public void setPayeeMobile(String payeeMobile) {
            this.payeeMobile = payeeMobile;
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

        public String getPassbookPassword() {
            return passbookPassword;
        }

        public void setPassbookPassword(String passbookPassword) {
            this.passbookPassword = passbookPassword;
        }

        public String getPassbookPassword_RC() {
            return passbookPassword_RC;
        }

        public void setPassbookPassword_RC(String passbookPassword_RC) {
            this.passbookPassword_RC = passbookPassword_RC;
        }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String Otp) {
        this.Otp = Otp;
    }

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String Otp_RC) {
        this.Otp_RC = Otp_RC;
    }
}
