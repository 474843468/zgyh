package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;

/**
 * Created by wangtong on 2016/9/24.
 */
public class PortfolioPurchaseInputModel implements Parcelable {
    private WealthDetailsBean detailsBean;
    private WealthAccountBean accountBean;// 当前筛选的账户

    public WealthDetailsBean getDetailsBean() {
        return detailsBean;
    }

    public void setDetailsBean(WealthDetailsBean detailsBean) {
        this.detailsBean = detailsBean;
    }

    public WealthAccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(WealthAccountBean accountBean) {
        this.accountBean = accountBean;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.detailsBean, flags);
        dest.writeParcelable(this.accountBean, flags);
    }

    public PortfolioPurchaseInputModel() {}

    protected PortfolioPurchaseInputModel(Parcel in) {
        this.detailsBean = in.readParcelable(WealthDetailsBean.class.getClassLoader());
        this.accountBean = in.readParcelable(WealthAccountBean.class.getClassLoader());
    }

    public static final Creator<PortfolioPurchaseInputModel> CREATOR =
            new Creator<PortfolioPurchaseInputModel>() {
                @Override
                public PortfolioPurchaseInputModel createFromParcel(
                        Parcel source) {return new PortfolioPurchaseInputModel(source);}

                @Override
                public PortfolioPurchaseInputModel[] newArray(
                        int size) {return new PortfolioPurchaseInputModel[size];}
            };
}
