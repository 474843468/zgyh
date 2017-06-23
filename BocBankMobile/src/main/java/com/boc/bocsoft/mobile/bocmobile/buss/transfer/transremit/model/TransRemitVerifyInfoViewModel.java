package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyuan on 2016/6/28.
 * 从transFrament传递到确认页面所需要的所有数据
 */
public class TransRemitVerifyInfoViewModel implements Parcelable {
    private String  isAppointed;//是否是定向 0 不是，1 是.
    private BigDecimal  availableBalance;//所选的付款账户可用余额
    private String  conversationId;//因为预交易和提交交易需相同的conversationId
    private String	accountNumber; //转出账户
    private String	amount	;//转账金额
    private String	currency	;//币种
    private String	payeeActno	;//收款人账号
    private String	payeeName	;//收款人名称
    private String	bankName	;//开户行

    private String	payeeBankIbkNum	;//省行联行号（所属地区）
    private String	toOrgName	;//开户网点

    private String	payeeMobile	;//收款人手机号
    private String	remark	;//附言
    private BigDecimal preCommissionCharge	;//优惠后费率

    //2016-8-11 添加
    private String passbookPassword;
    private String passbookPassword_RC;
    private String atmPassword;
    private String atmPassword_RC;
    private String phoneBankPassword;
    private String phoneBankPassword_RC;
    private String _combinId;

    private String	payerName	;//收款人名称
    /**
     *2016-7-25 添加
     */
    private String accountIbkNum;
    private String accountType;

    private String cardNickName;
    private String payeeNickName;
    private boolean sendmessageYn;//是否发送短信
    private boolean saveAsPayeeYn;//是否保存为常用收款人

    private String memo;
    private String payeeActno2;
    private String payeeBankName;
    private String payeeCnaps;
    private String payeeOrgName;
    private String sendMsgFlag;
    private String nickName;
    public String getFunctionFrom() {
        return functionFrom;
    }
    private String functionFrom;//标记这是哪个功能跳过来的，如果是转账汇款 则为"",

    public void setFunctionFrom(String functionFrom) {
        this.functionFrom = functionFrom;
    }
       public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayeeBankIbkNum() {
        return payeeBankIbkNum;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getIsAppointed() {
        return isAppointed;
    }

    public void setIsAppointed(String isAppointed) {
        this.isAppointed = isAppointed;
    }

    public String getPayeeIbkNum() {
        return payeeBankIbkNum;
    }

    public void setPayeeBankIbkNum(String payeeBankIbkNum) {
        this.payeeBankIbkNum = payeeBankIbkNum;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getPassbookPassword_RC() {
        return passbookPassword_RC;
    }

    public void setPassbookPassword_RC(String passbookPassword_RC) {
        this.passbookPassword_RC = passbookPassword_RC;
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

    public String getPassbookPassword() {
        return passbookPassword;
    }

    public void setPassbookPassword(String passbookPassword) {
        this.passbookPassword = passbookPassword;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCardNickName() {
        return cardNickName;
    }

    public void setCardNickName(String cardNickName) {
        this.cardNickName = cardNickName;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public String getPayeeNickName() {
        return payeeNickName;
    }

    public void setPayeeNickName(String payeeNickName) {
        this.payeeNickName = payeeNickName;
    }

    public boolean isSendmessageYn() {
        return sendmessageYn;
    }

    public void setSendmessageYn(boolean sendmessageYn) {
        this.sendmessageYn = sendmessageYn;
    }

    public boolean isSaveAsPayeeYn() {
        return saveAsPayeeYn;
    }

    public void setSaveAsPayeeYn(boolean saveAsPayeeYn) {
        this.saveAsPayeeYn = saveAsPayeeYn;
    }


    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private String payeeType;

//2016-7-25 添加

    public String getTransoutaccparent() {
        return transoutaccparent;
    }

    public void setTransoutaccparent(String transoutaccparent) {
        this.transoutaccparent = transoutaccparent;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPayeeActno2() {
        return payeeActno2;
    }

    public void setPayeeActno2(String payeeActno2) {
        this.payeeActno2 = payeeActno2;
    }

    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    public String getPayeeCnaps() {
        return payeeCnaps;
    }

    public void setPayeeCnaps(String payeeCnaps) {
        this.payeeCnaps = payeeCnaps;
    }

    public String getPayeeOrgName() {
        return payeeOrgName;
    }

    public void setPayeeOrgName(String payeeOrgName) {
        this.payeeOrgName = payeeOrgName;
    }

    public String getSendMsgFlag() {
        return sendMsgFlag;
    }

    public void setSendMsgFlag(String sendMsgFlag) {
        this.sendMsgFlag = sendMsgFlag;
    }


    public BigDecimal getNeedCommissionCharge() {
        return needCommissionCharge;
    }

    public void setNeedCommissionCharge(BigDecimal needCommissionCharge) {
        this.needCommissionCharge = needCommissionCharge;
    }
    private String transoutaccparent;

    private BigDecimal needCommissionCharge;
    //
    /**
     * 手机验证码有效时间
     */
    private String smcTrigerInterval;
    /**
     * 收款账户类型
     */
    private String toAccountType;
    /**
     * 收款行转入账户地区
     */
    private String payeeBankNum;
    /**
     * CA加签数据XML报文
     */
    private String _plainData;
    /**
     * CA的DN值
     */
    private String _certDN;

    public String getNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(String needPassword) {
        this.needPassword = needPassword;
    }

    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
    }

    public String get_plainData() {
        return _plainData;
    }

    public void set_plainData(String _plainData) {
        this._plainData = _plainData;
    }

    public static Creator<TransRemitVerifyInfoViewModel> getCREATOR() {
        return CREATOR;
    }

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    /**
     *是否验证验证账户的ATM取款密码
     */
    private String needPassword;

    /**
     * 安全因子数组
     */
    private List<FactorListBean> factorList;

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    //
    private String cashRemit;
    private String	dueDate	;
    private String	_signedData	;
    private String	activ	;
    private String	cycleSelect	;
    private String	devicePrint	;

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
    private String	deviceInfo	;
    private String  deviceInfo_RC;
    private String	endDate	;
    private String	executeDate	;//预约执行日期
    private String	executeType	;

    public String getExecuteTypeName() {
        return executeTypeName;
    }

    public void setExecuteTypeName(String executeTypeName) {
        this.executeTypeName = executeTypeName;
    }

    private String	executeTypeName	;
    private String	fromAccountId	;
    private String	Otp	;

    public String getOtp_RC() {
        return Otp_RC;
    }

    public void setOtp_RC(String otp_RC) {
        Otp_RC = otp_RC;
    }

    private String	Otp_RC	;
    private String	payeeId	;
    private String	remittanceInfo	;
    private String	Smc	;

    public String getSmc_RC() {
        return Smc_RC;
    }
    public void setSmc_RC(String smc_RC) {
        Smc_RC = smc_RC;
    }

    private String  Smc_RC;
    private String	startDate	;
    private String	state	;
    private String	token	;
    private String	cnapsCode	;
    private String	openChangeBooking	;
    private String	toAccountId	;
    public String get_signedData() {
        return _signedData;
    }

    public void set_signedData(String _signedData) {
        this._signedData = _signedData;
    }

    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    public String getAtmPassword() {
        return atmPassword;
    }

    public void setAtmPassword(String atmPassword) {
        this.atmPassword = atmPassword;
    }

    public String getCnapsCode() {
        return cnapsCode;
    }

    public void setCnapsCode(String cnapsCode) {
        this.cnapsCode = cnapsCode;
    }


    public String getCycleSelect() {
        return cycleSelect;
    }

    public void setCycleSelect(String cycleSelect) {
        this.cycleSelect = cycleSelect;
    }

    public String getDevicePrint() {
        return devicePrint;
    }

    public void setDevicePrint(String devicePrint) {
        this.devicePrint = devicePrint;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getOpenChangeBooking() {
        return openChangeBooking;
    }

    public void setOpenChangeBooking(String openChangeBooking) {
        this.openChangeBooking = openChangeBooking;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
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

    public String getPhoneBankPassword() {
        return phoneBankPassword;
    }

    public void setPhoneBankPassword(String phoneBankPassword) {
        this.phoneBankPassword = phoneBankPassword;
    }

    public String getRemittanceInfo() {
        return remittanceInfo;
    }

    public void setRemittanceInfo(String remittanceInfo) {
        this.remittanceInfo = remittanceInfo;
    }

    public String getSmc() {
        return Smc;
    }

    public void setSmc(String smc) {
        Smc = smc;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public BigDecimal getPreCommissionCharge() {
        return preCommissionCharge;
    }

    public void setPreCommissionCharge(BigDecimal preCommissionCharge) {
        this.preCommissionCharge = preCommissionCharge;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getToOrgName() {
        return toOrgName;
    }

    public void setToOrgName(String toOrgName) {
        this.toOrgName = toOrgName;
    }


    public TransRemitVerifyInfoViewModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.isAppointed);
        dest.writeSerializable(this.availableBalance);
        dest.writeString(this.conversationId);
        dest.writeString(this.accountNumber);
        dest.writeString(this.amount);
        dest.writeString(this.currency);
        dest.writeString(this.payeeActno);
        dest.writeString(this.payeeName);
        dest.writeString(this.bankName);
        dest.writeString(this.payeeBankIbkNum);
        dest.writeString(this.toOrgName);
        dest.writeString(this.payeeMobile);
        dest.writeString(this.remark);
        dest.writeSerializable(this.preCommissionCharge);
        dest.writeString(this.passbookPassword);
        dest.writeString(this.passbookPassword_RC);
        dest.writeString(this.atmPassword);
        dest.writeString(this.atmPassword_RC);
        dest.writeString(this.phoneBankPassword);
        dest.writeString(this.phoneBankPassword_RC);
        dest.writeString(this._combinId);
        dest.writeString(this.payerName);
        dest.writeString(this.accountIbkNum);
        dest.writeString(this.accountType);
        dest.writeString(this.cardNickName);
        dest.writeString(this.payeeNickName);
        dest.writeByte(this.sendmessageYn ? (byte) 1 : (byte) 0);
        dest.writeByte(this.saveAsPayeeYn ? (byte) 1 : (byte) 0);
        dest.writeString(this.memo);
        dest.writeString(this.payeeActno2);
        dest.writeString(this.payeeBankName);
        dest.writeString(this.payeeCnaps);
        dest.writeString(this.payeeOrgName);
        dest.writeString(this.sendMsgFlag);
        dest.writeString(this.nickName);
        dest.writeString(this.functionFrom);
        dest.writeString(this.payeeType);
        dest.writeString(this.transoutaccparent);
        dest.writeSerializable(this.needCommissionCharge);
        dest.writeString(this.smcTrigerInterval);
        dest.writeString(this.toAccountType);
        dest.writeString(this.payeeBankNum);
        dest.writeString(this._plainData);
        dest.writeString(this._certDN);
        dest.writeString(this.needPassword);
        dest.writeList(this.factorList);
        dest.writeString(this.cashRemit);
        dest.writeString(this.dueDate);
        dest.writeString(this._signedData);
        dest.writeString(this.activ);
        dest.writeString(this.cycleSelect);
        dest.writeString(this.devicePrint);
        dest.writeString(this.deviceInfo);
        dest.writeString(this.deviceInfo_RC);
        dest.writeString(this.endDate);
        dest.writeString(this.executeDate);
        dest.writeString(this.executeType);
        dest.writeString(this.executeTypeName);
        dest.writeString(this.fromAccountId);
        dest.writeString(this.Otp);
        dest.writeString(this.Otp_RC);
        dest.writeString(this.payeeId);
        dest.writeString(this.remittanceInfo);
        dest.writeString(this.Smc);
        dest.writeString(this.Smc_RC);
        dest.writeString(this.startDate);
        dest.writeString(this.state);
        dest.writeString(this.token);
        dest.writeString(this.cnapsCode);
        dest.writeString(this.openChangeBooking);
        dest.writeString(this.toAccountId);
    }

    protected TransRemitVerifyInfoViewModel(Parcel in) {
        this.isAppointed = in.readString();
        this.availableBalance = (BigDecimal) in.readSerializable();
        this.conversationId = in.readString();
        this.accountNumber = in.readString();
        this.amount = in.readString();
        this.currency = in.readString();
        this.payeeActno = in.readString();
        this.payeeName = in.readString();
        this.bankName = in.readString();
        this.payeeBankIbkNum = in.readString();
        this.toOrgName = in.readString();
        this.payeeMobile = in.readString();
        this.remark = in.readString();
        this.preCommissionCharge = (BigDecimal) in.readSerializable();
        this.passbookPassword = in.readString();
        this.passbookPassword_RC = in.readString();
        this.atmPassword = in.readString();
        this.atmPassword_RC = in.readString();
        this.phoneBankPassword = in.readString();
        this.phoneBankPassword_RC = in.readString();
        this._combinId = in.readString();
        this.payerName = in.readString();
        this.accountIbkNum = in.readString();
        this.accountType = in.readString();
        this.cardNickName = in.readString();
        this.payeeNickName = in.readString();
        this.sendmessageYn = in.readByte() != 0;
        this.saveAsPayeeYn = in.readByte() != 0;
        this.memo = in.readString();
        this.payeeActno2 = in.readString();
        this.payeeBankName = in.readString();
        this.payeeCnaps = in.readString();
        this.payeeOrgName = in.readString();
        this.sendMsgFlag = in.readString();
        this.nickName = in.readString();
        this.functionFrom = in.readString();
        this.payeeType = in.readString();
        this.transoutaccparent = in.readString();
        this.needCommissionCharge = (BigDecimal) in.readSerializable();
        this.smcTrigerInterval = in.readString();
        this.toAccountType = in.readString();
        this.payeeBankNum = in.readString();
        this._plainData = in.readString();
        this._certDN = in.readString();
        this.needPassword = in.readString();
        this.factorList = new ArrayList<FactorListBean>();
        in.readList(this.factorList, FactorListBean.class.getClassLoader());
        this.cashRemit = in.readString();
        this.dueDate = in.readString();
        this._signedData = in.readString();
        this.activ = in.readString();
        this.cycleSelect = in.readString();
        this.devicePrint = in.readString();
        this.deviceInfo = in.readString();
        this.deviceInfo_RC = in.readString();
        this.endDate = in.readString();
        this.executeDate = in.readString();
        this.executeType = in.readString();
        this.executeTypeName = in.readString();
        this.fromAccountId = in.readString();
        this.Otp = in.readString();
        this.Otp_RC = in.readString();
        this.payeeId = in.readString();
        this.remittanceInfo = in.readString();
        this.Smc = in.readString();
        this.Smc_RC = in.readString();
        this.startDate = in.readString();
        this.state = in.readString();
        this.token = in.readString();
        this.cnapsCode = in.readString();
        this.openChangeBooking = in.readString();
        this.toAccountId = in.readString();
    }

    public static final Creator<TransRemitVerifyInfoViewModel> CREATOR = new Creator<TransRemitVerifyInfoViewModel>() {
        @Override
        public TransRemitVerifyInfoViewModel createFromParcel(Parcel source) {
            return new TransRemitVerifyInfoViewModel(source);
        }

        @Override
        public TransRemitVerifyInfoViewModel[] newArray(int size) {
            return new TransRemitVerifyInfoViewModel[size];
        }
    };
}
