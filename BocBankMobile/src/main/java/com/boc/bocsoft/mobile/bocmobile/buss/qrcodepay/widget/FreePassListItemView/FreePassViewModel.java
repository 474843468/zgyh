package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.FreePassListItemView;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PassFreeInfoViewModel;

/**
 * 小额免密
 * Created by wangf on 2016/9/22.
 */
public class FreePassViewModel implements Parcelable {

    /**
     * 账户信息
     */
    private AccountBean accountBean;
    /**
     * 小额免密信息
     */
    private PassFreeInfoViewModel freeInfoViewModel;

    private int position;

    public AccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(AccountBean accountBean) {
        this.accountBean = accountBean;
    }

    public PassFreeInfoViewModel getFreeInfoViewModel() {
        return freeInfoViewModel;
    }

    public void setFreeInfoViewModel(PassFreeInfoViewModel freeInfoViewModel) {
        this.freeInfoViewModel = freeInfoViewModel;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FreePassViewModel that = (FreePassViewModel) o;

        return accountBean != null ? accountBean.equals(that.accountBean)
                : that.accountBean == null;
    }

    @Override
    public int hashCode() {
        return accountBean != null ? accountBean.hashCode() : 0;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.accountBean, flags);
        dest.writeParcelable(this.freeInfoViewModel, flags);
        dest.writeInt(this.position);
    }

    public FreePassViewModel() {
    }

    protected FreePassViewModel(Parcel in) {
        this.accountBean = in.readParcelable(AccountBean.class.getClassLoader());
        this.freeInfoViewModel = in.readParcelable(PassFreeInfoViewModel.class.getClassLoader());
        this.position = in.readInt();
    }

    public static final Creator<FreePassViewModel> CREATOR = new Creator<FreePassViewModel>() {
        @Override
        public FreePassViewModel createFromParcel(Parcel source) {
            return new FreePassViewModel(source);
        }

        @Override
        public FreePassViewModel[] newArray(int size) {
            return new FreePassViewModel[size];
        }
    };
}
