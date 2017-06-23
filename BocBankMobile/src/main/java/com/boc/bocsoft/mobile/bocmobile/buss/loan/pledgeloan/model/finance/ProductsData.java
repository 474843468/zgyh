package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * 作者：XieDu
 * 创建时间：2016/9/12 15:35
 * 描述：
 */
public class ProductsData implements Parcelable {
    private String conversationId;
    private List<PledgeProductBean> pledgeProductBeanList;

    public ProductsData(String conversationId,List<PledgeProductBean> pledgeProductBeanList) {
        this.conversationId=conversationId;
        this.pledgeProductBeanList = pledgeProductBeanList;
    }

    public List<PledgeProductBean> getPledgeProductBeanList() {
        return pledgeProductBeanList;
    }

    public void setPledgeProductBeanList(List<PledgeProductBean> pledgeProductBeanList) {
        this.pledgeProductBeanList = pledgeProductBeanList;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.conversationId);
        dest.writeTypedList(this.pledgeProductBeanList);
    }

    protected ProductsData(Parcel in) {
        this.conversationId = in.readString();
        this.pledgeProductBeanList = in.createTypedArrayList(PledgeProductBean.CREATOR);
    }

    public static final Parcelable.Creator<ProductsData> CREATOR =
            new Parcelable.Creator<ProductsData>() {
                @Override
                public ProductsData createFromParcel(Parcel source) {
                    return new ProductsData(source);
                }

                @Override
                public ProductsData[] newArray(int size) {return new ProductsData[size];}
            };
}
