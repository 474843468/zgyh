package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 其他页面往明细跳转需要数据
 * Created by liuweidong on 2016/10/25.
 */
public class DetailsRequestBean implements Parcelable {
    /*明细接口请求数据*/
    private String prodCode;// 产品代码
    private String prodKind;// 产品性质 0:结构性理财产品1:类基金理财产品 （不上送默认为0）
    private String ibknum;// 省行联行号 返回项需展示(剩余额度、工作时间、挂单时间)，此项必输

    private String issueType;// 产品类型 1：现金管理类产品2：净值开放类产品3：固定期限产品
    /*明细页需要额外数据*/
    private List<WealthAccountBean> list = new ArrayList<>();
    private WealthAccountBean accountBean;// 当前筛选的账户
    private String isBuy;// 是否购买
    private String groupBuy;// 是否组合购买
    private String isAgreement;// 是否协议请求
    private String isProfitTest;// 是否可以收益试算

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdKind() {
        return prodKind;
    }

    public void setProdKind(String prodKind) {
        this.prodKind = prodKind;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIbknum() {
        return ibknum;
    }

    public void setIbknum(String ibknum) {
        this.ibknum = ibknum;
    }

    public List<WealthAccountBean> getList() {
        return list;
    }

    public void setList(List<WealthAccountBean> list) {
        this.list = list;
    }

    public WealthAccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(WealthAccountBean accountBean) {
        this.accountBean = accountBean;
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }

    public String getGroupBuy() {
        return groupBuy;
    }

    public void setGroupBuy(String groupBuy) {
        this.groupBuy = groupBuy;
    }

    public String getIsAgreement() {
        return isAgreement;
    }

    public void setIsAgreement(String isAgreement) {
        this.isAgreement = isAgreement;
    }

    public String getIsProfitTest() {
        return isProfitTest;
    }

    public void setIsProfitTest(String isProfitTest) {
        this.isProfitTest = isProfitTest;
    }

    public DetailsRequestBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.prodCode);
        dest.writeString(this.prodKind);
        dest.writeString(this.issueType);
        dest.writeString(this.ibknum);
        dest.writeTypedList(this.list);
        dest.writeParcelable(this.accountBean, flags);
        dest.writeString(this.isBuy);
        dest.writeString(this.groupBuy);
        dest.writeString(this.isAgreement);
        dest.writeString(this.isProfitTest);
    }

    protected DetailsRequestBean(Parcel in) {
        this.prodCode = in.readString();
        this.prodKind = in.readString();
        this.issueType = in.readString();
        this.ibknum = in.readString();
        this.list = in.createTypedArrayList(WealthAccountBean.CREATOR);
        this.accountBean = in.readParcelable(WealthAccountBean.class.getClassLoader());
        this.isBuy = in.readString();
        this.groupBuy = in.readString();
        this.isAgreement = in.readString();
        this.isProfitTest = in.readString();
    }

    public static final Creator<DetailsRequestBean> CREATOR = new Creator<DetailsRequestBean>() {
        @Override
        public DetailsRequestBean createFromParcel(Parcel source) {
            return new DetailsRequestBean(source);
        }

        @Override
        public DetailsRequestBean[] newArray(int size) {
            return new DetailsRequestBean[size];
        }
    };
}
