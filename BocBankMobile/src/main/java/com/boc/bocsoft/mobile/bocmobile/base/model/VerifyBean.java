package com.boc.bocsoft.mobile.bocmobile.base.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ListItemType;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：XieDu
 * 创建时间：2016/9/18 10:27
 * 描述：
 */
public class VerifyBean implements Parcelable {
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
    @ListItemType(instantiate = FactorBean.class)
    private List<FactorBean> factorList;

    public String get_plainData() { return _plainData;}

    public void set_plainData(String _plainData) { this._plainData = _plainData;}

    public String getSmcTrigerInterval() { return smcTrigerInterval;}

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public String get_certDN() { return _certDN;}

    public void set_certDN(String _certDN) { this._certDN = _certDN;}

    public List<FactorBean> getFactorList() { return factorList;}

    public void setFactorList(List<FactorBean> factorList) { this.factorList = factorList;}

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._plainData);
        dest.writeString(this.smcTrigerInterval);
        dest.writeString(this._certDN);
        dest.writeList(this.factorList);
    }

    public VerifyBean() {}

    protected VerifyBean(Parcel in) {
        this._plainData = in.readString();
        this.smcTrigerInterval = in.readString();
        this._certDN = in.readString();
        this.factorList = new ArrayList<FactorBean>();
        in.readList(this.factorList, FactorBean.class.getClassLoader());
    }

    public static final Creator<VerifyBean> CREATOR = new Creator<VerifyBean>() {
        @Override
        public VerifyBean createFromParcel(Parcel source) {return new VerifyBean(source);}

        @Override
        public VerifyBean[] newArray(int size) {return new VerifyBean[size];}
    };
}
