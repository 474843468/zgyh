package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by XieDu on 2016/8/2.
 */
public class PledgeAvaAndPersonalTimeAccount implements Parcelable {
    private PledgeAvaAccountBean pledgeAvaAccountBean;
    private List<PersonalTimeAccountBean> personalTimeAccountBeanList;

    public PledgeAvaAndPersonalTimeAccount(PledgeAvaAccountBean pledgeAvaAccountBean,
            List<PersonalTimeAccountBean> personalTimeAccountBeanList) {
        this.pledgeAvaAccountBean = pledgeAvaAccountBean;
        this.personalTimeAccountBeanList = personalTimeAccountBeanList;
    }

    public PledgeAvaAccountBean getPledgeAvaAccountBean() {
        return pledgeAvaAccountBean;
    }

    public void setPledgeAvaAccountBean(PledgeAvaAccountBean pledgeAvaAccountBean) {
        this.pledgeAvaAccountBean = pledgeAvaAccountBean;
    }

    public List<PersonalTimeAccountBean> getPersonalTimeAccountBeanList() {
        return personalTimeAccountBeanList;
    }

    public void setPersonalTimeAccountBeanList(
            List<PersonalTimeAccountBean> personalTimeAccountBeanList) {
        this.personalTimeAccountBeanList = personalTimeAccountBeanList;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.pledgeAvaAccountBean, flags);
        dest.writeTypedList(this.personalTimeAccountBeanList);
    }

    protected PledgeAvaAndPersonalTimeAccount(Parcel in) {
        this.pledgeAvaAccountBean = in.readParcelable(PledgeAvaAccountBean.class.getClassLoader());
        this.personalTimeAccountBeanList = in.createTypedArrayList(PersonalTimeAccountBean.CREATOR);
    }

    public static final Parcelable.Creator<PledgeAvaAndPersonalTimeAccount> CREATOR =
            new Parcelable.Creator<PledgeAvaAndPersonalTimeAccount>() {
                @Override
                public PledgeAvaAndPersonalTimeAccount createFromParcel(
                        Parcel source) {return new PledgeAvaAndPersonalTimeAccount(source);}

                @Override
                public PledgeAvaAndPersonalTimeAccount[] newArray(
                        int size) {return new PledgeAvaAndPersonalTimeAccount[size];}
            };
}
