package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 信用卡首页主model
 * Created by liuweidong on 2016/11/22.
 */
public class CrcdModel implements Parcelable {
    private AccountBean accountBean;
    /*4.93 093 信用卡综合信息查询PsnCrcdQueryGeneralInfo 响应参数*/
    private String acctBank;// 开户行
    private String acctName;// 户名
    private String acctNum;// 信用卡卡号
    private String annualFee;// 年费减免情况 - “1”免年费 “0”不免年费
    private String billDate;// 账单日
    private String carAvaiDate;// 卡有效截至日期
    private String carFlag;// 主副卡标识 - “1”主卡 “0”附卡
    private String carStatus;// 卡状态
    private String dueDate;// 本期到期还款日 - 无账单时返回空
    private String productName;// 产品名称
    private String startDate;// 启用日期
    private String waiveMemFeeEndDate;// 年费减免结束日期 - 格式yyyy-MM-dd。如annualFee为“1”，且该字段返回年份为2500，表示永久免年费
    private List<CrcdInfoBean> actList = new ArrayList<>();

    private String crcdPoint = "";// 信用卡有效积分
    private String isBillExist;// 是否已出账单 0：没有出；1：已出

    public AccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(AccountBean accountBean) {
        this.accountBean = accountBean;
    }

    public String getAcctBank() {
        return acctBank;
    }

    public void setAcctBank(String acctBank) {
        this.acctBank = acctBank;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(String annualFee) {
        this.annualFee = annualFee;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getCarAvaiDate() {
        return carAvaiDate;
    }

    public void setCarAvaiDate(String carAvaiDate) {
        this.carAvaiDate = carAvaiDate;
    }

    public String getCarFlag() {
        return carFlag;
    }

    public void setCarFlag(String carFlag) {
        this.carFlag = carFlag;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getWaiveMemFeeEndDate() {
        return waiveMemFeeEndDate;
    }

    public void setWaiveMemFeeEndDate(String waiveMemFeeEndDate) {
        this.waiveMemFeeEndDate = waiveMemFeeEndDate;
    }

    public List<CrcdInfoBean> getActList() {
        return actList;
    }

    public void setActList(List<CrcdInfoBean> actList) {
        this.actList = actList;
    }

    public String getCrcdPoint() {
        return crcdPoint;
    }

    public void setCrcdPoint(String crcdPoint) {
        this.crcdPoint = crcdPoint;
    }

    public String getIsBillExist() {
        return isBillExist;
    }

    public void setIsBillExist(String isBillExist) {
        this.isBillExist = isBillExist;
    }

    public CrcdModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.accountBean, flags);
        dest.writeString(this.acctBank);
        dest.writeString(this.acctName);
        dest.writeString(this.acctNum);
        dest.writeString(this.annualFee);
        dest.writeString(this.billDate);
        dest.writeString(this.carAvaiDate);
        dest.writeString(this.carFlag);
        dest.writeString(this.carStatus);
        dest.writeString(this.dueDate);
        dest.writeString(this.productName);
        dest.writeString(this.startDate);
        dest.writeString(this.waiveMemFeeEndDate);
        dest.writeTypedList(this.actList);
        dest.writeString(this.crcdPoint);
        dest.writeString(this.isBillExist);
    }

    protected CrcdModel(Parcel in) {
        this.accountBean = in.readParcelable(AccountBean.class.getClassLoader());
        this.acctBank = in.readString();
        this.acctName = in.readString();
        this.acctNum = in.readString();
        this.annualFee = in.readString();
        this.billDate = in.readString();
        this.carAvaiDate = in.readString();
        this.carFlag = in.readString();
        this.carStatus = in.readString();
        this.dueDate = in.readString();
        this.productName = in.readString();
        this.startDate = in.readString();
        this.waiveMemFeeEndDate = in.readString();
        this.actList = in.createTypedArrayList(CrcdInfoBean.CREATOR);
        this.crcdPoint = in.readString();
        this.isBillExist = in.readString();
    }

    public static final Creator<CrcdModel> CREATOR = new Creator<CrcdModel>() {
        @Override
        public CrcdModel createFromParcel(Parcel source) {
            return new CrcdModel(source);
        }

        @Override
        public CrcdModel[] newArray(int size) {
            return new CrcdModel[size];
        }
    };
}
