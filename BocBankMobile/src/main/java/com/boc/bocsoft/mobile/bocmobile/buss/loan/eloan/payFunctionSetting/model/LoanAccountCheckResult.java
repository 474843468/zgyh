package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model;

/**
 * 作者：XieDu
 * 创建时间：2016/9/2 15:45
 * 描述：
 */
public class LoanAccountCheckResult {
    /**
     * 借记卡账号
     */
    private String accountNo;
    /**
     * 账户是否支持签约
     * Y:支持签约;N:不支持签约
     */
    private String isSign;
    /**
     * 账户是否已签约
     * 0:未签约 1:已签约-存款偏好 2:已签约-贷款偏好
     */
    private String signedOrNot;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }

    public String getSignedOrNot() {
        return signedOrNot;
    }

    public void setSignedOrNot(String signedOrNot) {
        this.signedOrNot = signedOrNot;
    }
}
