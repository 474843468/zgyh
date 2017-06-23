package com.boc.bocsoft.mobile.bocmobile.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;

/**
 * 结售汇基础bean1
 * Created by lxw on 2016/8/3 0003.
 */
public class FessBean  implements Parcelable{

    // 产品币种
    private String curCode;
    // 名称 画面显示用
    private String name;
    // 银行现汇买入价
    private BigDecimal buyRate;
    // 银行现汇卖出价
    private BigDecimal sellRate;
    // 银行现钞买入价
    private BigDecimal buyNoteRate;
    // 银行现钞卖出价
    private BigDecimal sellNoteRate;

    // 更新时间
    private String updateDate;

    // 0：加载中 1：正确返回 2：请求失败
    private String refreshState = "0";

    public FessBean(){

    }

    protected FessBean(Parcel in) {
        curCode = in.readString();
        name = in.readString();
        updateDate = in.readString();
        refreshState = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(curCode);
        dest.writeString(name);
        dest.writeString(updateDate);
        dest.writeString(refreshState);
    }

    public static final Creator<FessBean> CREATOR = new Creator<FessBean>() {
        @Override
        public FessBean createFromParcel(Parcel in) {
            return new FessBean(in);
        }

        @Override
        public FessBean[] newArray(int size) {
            return new FessBean[size];
        }
    };

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(BigDecimal buyRate) {
        this.buyRate = buyRate;
    }

    public BigDecimal getSellRate() {
        return sellRate;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
    }

    public BigDecimal getBuyNoteRate() {
        return buyNoteRate;
    }

    public void setBuyNoteRate(BigDecimal buyNoteRate) {
        this.buyNoteRate = buyNoteRate;
    }

    public BigDecimal getSellNoteRate() {
        return sellNoteRate;
    }

    public void setSellNoteRate(BigDecimal sellNoteRate) {
        this.sellNoteRate = sellNoteRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefreshState() {
        return refreshState;
    }

    public void setRefreshState(String refreshState) {
        this.refreshState = refreshState;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }


}
