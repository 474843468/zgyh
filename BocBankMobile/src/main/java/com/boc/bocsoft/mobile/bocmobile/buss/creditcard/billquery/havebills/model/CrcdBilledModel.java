package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model;

import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ListItemType;

import java.util.List;

/**
 * 4.7 007查询信用卡已出账单PsnCrcdQueryBilledTrans
 * 接口响应参数
 *
 * 作者：xwg on 16/12/20 15:32
 * Modified by liuweidong on 2016/12/28.
 */
public class CrcdBilledModel {
    //账单日期
    private String billDate;
    //客户信息
    private CrcdCustomerInfoBean crcdCustomerInfo;
    //账户信息
    @ListItemType(instantiate = CrcdAccountInfoListBean.class)
    private List<CrcdAccountInfoListBean> crcdAccountInfoList;
    //积分信息表
    @ListItemType(instantiate = CrcdScoreInfoListBean.class)
    private List<CrcdScoreInfoListBean> crcdScoreInfoList;
    //账单信息表
    @ListItemType(instantiate = String.class)
    private List<String> crcdBillInfoList;

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public CrcdCustomerInfoBean getCrcdCustomerInfo() {
        return crcdCustomerInfo;
    }

    public void setCrcdCustomerInfo(CrcdCustomerInfoBean crcdCustomerInfo) {
        this.crcdCustomerInfo = crcdCustomerInfo;
    }

    public List<CrcdAccountInfoListBean> getCrcdAccountInfoList() {
        return crcdAccountInfoList;
    }

    public void setCrcdAccountInfoList(List<CrcdAccountInfoListBean> crcdAccountInfoList) {
        this.crcdAccountInfoList = crcdAccountInfoList;
    }

    public List<String> getCrcdBillInfoList() {
        return crcdBillInfoList;
    }

    public void setCrcdBillInfoList(List<String> crcdBillInfoList) {
        this.crcdBillInfoList = crcdBillInfoList;
    }

    public List<CrcdScoreInfoListBean> getCrcdScoreInfoList() {
        return crcdScoreInfoList;
    }

    public void setCrcdScoreInfoList(List<CrcdScoreInfoListBean> crcdScoreInfoList) {
        this.crcdScoreInfoList = crcdScoreInfoList;
    }

    public static class CrcdCustomerInfoBean {
        //地址1
        private String address1;
        //地址2
        private String address2;
        //地址3
        private String address3;
        //账单日期
        private String billDate;
        //卡号
        private String cardNo;
        //账户ID
        private String creditcardId;
        //币种1本期余额 - 该余额为非负数，由curTermBalanceflag1该字段判断是存款还是欠款或者余额0。
        private String curTermBalance1;
        //币种2本期余额 - 该余额为非负数，由curTermBalanceflag2该字段判断是存款还是欠款或者余额0。
        private String curTermBalance2;
        //币种1本期余额标识位 - “0”-欠款  “1”-存款  “2”-余额0
        private String curTermBalanceflag1;
        //币种2本期余额标识位 - “0”-欠款 “1”-存款  “2”-余额0
        private String curTermBalanceflag2;
        //币种1
        private String currencyCode1;
        //币种2
        private String currencyCode2;
        //币种1最低还款额
        private String lowestRepayAmount1;
        //币种2最低还款额
        private String lowestRepayAmount2;
        //名
        private String name;
        //到期还款日
        private String repayDate;
        //姓
        private String surname;
        //称谓
        private String title;
        //邮编
        private String zipCode;

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getAddress3() {
            return address3;
        }

        public void setAddress3(String address3) {
            this.address3 = address3;
        }

        public String getBillDate() {
            return billDate;
        }

        public void setBillDate(String billDate) {
            this.billDate = billDate;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getCreditcardId() {
            return creditcardId;
        }

        public void setCreditcardId(String creditcardId) {
            this.creditcardId = creditcardId;
        }

        public String getCurTermBalance1() {
            return curTermBalance1;
        }

        public void setCurTermBalance1(String curTermBalance1) {
            this.curTermBalance1 = curTermBalance1;
        }

        public String getCurTermBalance2() {
            return curTermBalance2;
        }

        public void setCurTermBalance2(String curTermBalance2) {
            this.curTermBalance2 = curTermBalance2;
        }

        public String getCurTermBalanceflag1() {
            return curTermBalanceflag1;
        }

        public void setCurTermBalanceflag1(String curTermBalanceflag1) {
            this.curTermBalanceflag1 = curTermBalanceflag1;
        }

        public String getCurTermBalanceflag2() {
            return curTermBalanceflag2;
        }

        public void setCurTermBalanceflag2(String curTermBalanceflag2) {
            this.curTermBalanceflag2 = curTermBalanceflag2;
        }

        public String getCurrencyCode1() {
            return currencyCode1;
        }

        public void setCurrencyCode1(String currencyCode1) {
            this.currencyCode1 = currencyCode1;
        }

        public String getCurrencyCode2() {
            return currencyCode2;
        }

        public void setCurrencyCode2(String currencyCode2) {
            this.currencyCode2 = currencyCode2;
        }

        public String getLowestRepayAmount1() {
            return lowestRepayAmount1;
        }

        public void setLowestRepayAmount1(String lowestRepayAmount1) {
            this.lowestRepayAmount1 = lowestRepayAmount1;
        }

        public String getLowestRepayAmount2() {
            return lowestRepayAmount2;
        }

        public void setLowestRepayAmount2(String lowestRepayAmount2) {
            this.lowestRepayAmount2 = lowestRepayAmount2;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRepayDate() {
            return repayDate;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }
    }

    public static class CrcdAccountInfoListBean {
        //帐户类型
        private String acntType;
        //自动还款账号 - 此域为空，表示该账户不需要进行还款提醒
        private String autoRepayAccount;
        //可用余额
        private String avaiBal;
        //账单日期
        private String billDt;
        //信用额度
        private String cdtLmtAmt;
        //分期可用余额
        private String instalAvaiBal;
        //上期帐户余额 - 该余额为非负数，由lastTermAcntBalflag该字段判断是存款还是欠款或者余额0。
        private String lastTermAcntBal;
        //上期帐户余额标识位 - “0”-欠款 “1”-存款 “2”-余额0
        private String lastTermAcntBalflag;
        //最低还款额
        private String lowestRepayAmount;
        //本期余额 - 该余额为非负数，由periodAvailbleCreditLimit flag该字段判断是存款还是欠款或者余额0。
        private String periodAvailbleCreditLimit;
        //本期余额标识位 - “0”-欠款 “1”-存款 “2”-余额0
        private String periodAvailbleCreditLimitflag;
        //可取现额度
        private String periodGetCashCreditLimit;
        //到期还款日
        private String reapyDt;
        //存入总计
        private String totalDeposit;
        //支出总计
        private String totalExpend;

        public String getAcntType() {
            return acntType;
        }

        public void setAcntType(String acntType) {
            this.acntType = acntType;
        }

        public String getAutoRepayAccount() {
            return autoRepayAccount;
        }

        public void setAutoRepayAccount(String autoRepayAccount) {
            this.autoRepayAccount = autoRepayAccount;
        }

        public String getAvaiBal() {
            return avaiBal;
        }

        public void setAvaiBal(String avaiBal) {
            this.avaiBal = avaiBal;
        }

        public String getBillDt() {
            return billDt;
        }

        public void setBillDt(String billDt) {
            this.billDt = billDt;
        }

        public String getCdtLmtAmt() {
            return cdtLmtAmt;
        }

        public void setCdtLmtAmt(String cdtLmtAmt) {
            this.cdtLmtAmt = cdtLmtAmt;
        }

        public String getInstalAvaiBal() {
            return instalAvaiBal;
        }

        public void setInstalAvaiBal(String instalAvaiBal) {
            this.instalAvaiBal = instalAvaiBal;
        }

        public String getLastTermAcntBal() {
            return lastTermAcntBal;
        }

        public void setLastTermAcntBal(String lastTermAcntBal) {
            this.lastTermAcntBal = lastTermAcntBal;
        }

        public String getLastTermAcntBalflag() {
            return lastTermAcntBalflag;
        }

        public void setLastTermAcntBalflag(String lastTermAcntBalflag) {
            this.lastTermAcntBalflag = lastTermAcntBalflag;
        }

        public String getLowestRepayAmount() {
            return lowestRepayAmount;
        }

        public void setLowestRepayAmount(String lowestRepayAmount) {
            this.lowestRepayAmount = lowestRepayAmount;
        }

        public String getPeriodAvailbleCreditLimit() {
            return periodAvailbleCreditLimit;
        }

        public void setPeriodAvailbleCreditLimit(String periodAvailbleCreditLimit) {
            this.periodAvailbleCreditLimit = periodAvailbleCreditLimit;
        }

        public String getPeriodAvailbleCreditLimitflag() {
            return periodAvailbleCreditLimitflag;
        }

        public void setPeriodAvailbleCreditLimitflag(String periodAvailbleCreditLimitflag) {
            this.periodAvailbleCreditLimitflag = periodAvailbleCreditLimitflag;
        }

        public String getPeriodGetCashCreditLimit() {
            return periodGetCashCreditLimit;
        }

        public void setPeriodGetCashCreditLimit(String periodGetCashCreditLimit) {
            this.periodGetCashCreditLimit = periodGetCashCreditLimit;
        }

        public String getReapyDt() {
            return reapyDt;
        }

        public void setReapyDt(String reapyDt) {
            this.reapyDt = reapyDt;
        }

        public String getTotalDeposit() {
            return totalDeposit;
        }

        public void setTotalDeposit(String totalDeposit) {
            this.totalDeposit = totalDeposit;
        }

        public String getTotalExpend() {
            return totalExpend;
        }

        public void setTotalExpend(String totalExpend) {
            this.totalExpend = totalExpend;
        }
    }

    public static class CrcdScoreInfoListBean {
        //上月积分余额
        private String bonusFromLastTerm;
        //积分ID
        private String bonusId;
        //本期积分余额
        private String bonusToNextTerm;
        //本期兑换积分
        private String currTermExchangeBonus;
        //本期累计积分
        private String currTermTotalBonus;
        //本期赢取积分
        private String currTermWinBonus;
        //积分到期日
        private String deadline;

        public String getBonusFromLastTerm() {
            return bonusFromLastTerm;
        }

        public void setBonusFromLastTerm(String bonusFromLastTerm) {
            this.bonusFromLastTerm = bonusFromLastTerm;
        }

        public String getBonusId() {
            return bonusId;
        }

        public void setBonusId(String bonusId) {
            this.bonusId = bonusId;
        }

        public String getBonusToNextTerm() {
            return bonusToNextTerm;
        }

        public void setBonusToNextTerm(String bonusToNextTerm) {
            this.bonusToNextTerm = bonusToNextTerm;
        }

        public String getCurrTermExchangeBonus() {
            return currTermExchangeBonus;
        }

        public void setCurrTermExchangeBonus(String currTermExchangeBonus) {
            this.currTermExchangeBonus = currTermExchangeBonus;
        }

        public String getCurrTermTotalBonus() {
            return currTermTotalBonus;
        }

        public void setCurrTermTotalBonus(String currTermTotalBonus) {
            this.currTermTotalBonus = currTermTotalBonus;
        }

        public String getCurrTermWinBonus() {
            return currTermWinBonus;
        }

        public void setCurrTermWinBonus(String currTermWinBonus) {
            this.currTermWinBonus = currTermWinBonus;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }
    }
}