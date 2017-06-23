package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceConfirm.PsnCrcdDividedPayAdvanceConfirmResult;
import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangle on 2016/11/24.
 */
public class VerifyViewModel implements Parcelable {

    /**
     * CA加签数据XML报文
     */
    private String _plainData;
    /**
     * 手机验证码有效时间
     */
    private String smcTrigerInterval;
    /**
     * CA的DN值
     */
    private String _certDN;
    /**
     * name值"Smc"-需要输入手机验证码值为"Otp"-需要输入动态口令
     */
    private List<FactorBean> factorList;

    public String get_plainData() {
        return _plainData;
    }

    public void set_plainData(String _plainData) {
        this._plainData = _plainData;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
    }

    public List<FactorBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorBean> factorList) {
        this.factorList = factorList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._plainData);
        dest.writeString(this.smcTrigerInterval);
        dest.writeString(this._certDN);
        dest.writeList(this.factorList);
    }

    protected VerifyViewModel(Parcel in) {
        this._plainData = in.readString();
        this.smcTrigerInterval = in.readString();
        this._certDN = in.readString();
        this.factorList = new ArrayList<>();
        in.readList(this.factorList, FactorBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<VerifyViewModel> CREATOR = new Parcelable.Creator<VerifyViewModel>() {
        @Override
        public VerifyViewModel createFromParcel(Parcel source) {
            return new VerifyViewModel(source);
        }

        @Override
        public VerifyViewModel[] newArray(int size) {
            return new VerifyViewModel[size];
        }
    };

    public VerifyViewModel() {

    }

    public static VerifyViewModel newInstanceFromResult(PsnCrcdDividedPayAdvanceConfirmResult result) {
        VerifyViewModel verifyModel = new VerifyViewModel();
        verifyModel.set_plainData(result.get_plainData());
        verifyModel.set_certDN(result.get_certDN());
        verifyModel.setSmcTrigerInterval(result.getSmcTrigerInterval());

        List<PsnCrcdDividedPayAdvanceConfirmResult.FactorListBean> resultFactorList = result.getFactorList();
         List<FactorBean> list = new ArrayList<>();
        if (resultFactorList != null) {
            for (PsnCrcdDividedPayAdvanceConfirmResult.FactorListBean factorListBean : resultFactorList) {
               list.add(BeanConvertor.toBean(factorListBean, new FactorBean()));
            }
            verifyModel.setFactorList(list);
        }
        return verifyModel;
    }



}
