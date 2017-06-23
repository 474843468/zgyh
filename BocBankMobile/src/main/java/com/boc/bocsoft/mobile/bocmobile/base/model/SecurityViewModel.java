package com.boc.bocsoft.mobile.bocmobile.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 安全因子接口返回的数据
 * Created by liuweidong on 2016/6/12.
 */
public class SecurityViewModel implements Parcelable {

    /**
     * 客户默认的安全因子组合  name:安全因子名称  id: 安全因子id
     * 0:虚拟 4:USBKey证书 8:动态口令令牌 32:短信认证码 40:动态口令令牌+短信认证码 96:短信认证码+硬件绑定
     */
    private CombinBean _defaultCombin;
    /**
     * 客户默认的安全因子组合  name:安全因子名称  id: 安全因子id
     * 0:虚拟 4:USBKey证书 8:动态口令令牌 32:短信认证码 40:动态口令令牌+短信认证码 96:短信认证码+硬件绑定
     */
    private List<CombinBean> _combinList;

    public List<CombinBean> get_combinList() {
        return _combinList;
    }

    public void set_combinList(List<CombinBean> _combinList) {
        this._combinList = _combinList;
    }

    public CombinBean get_defaultCombin() {
        return _defaultCombin;
    }

    public void set_defaultCombin(CombinBean _defaultCombin) {
        this._defaultCombin = _defaultCombin;
    }

    public SecurityViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this._defaultCombin, flags);
        dest.writeTypedList(this._combinList);
    }

    protected SecurityViewModel(Parcel in) {
        this._defaultCombin = in.readParcelable(CombinBean.class.getClassLoader());
        this._combinList = in.createTypedArrayList(CombinBean.CREATOR);
    }

    public static final Creator<SecurityViewModel> CREATOR = new Creator<SecurityViewModel>() {
        @Override
        public SecurityViewModel createFromParcel(Parcel source) {
            return new SecurityViewModel(source);
        }

        @Override
        public SecurityViewModel[] newArray(int size) {
            return new SecurityViewModel[size];
        }
    };
}
