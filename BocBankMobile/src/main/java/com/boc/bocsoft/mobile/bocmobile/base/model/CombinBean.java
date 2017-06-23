package com.boc.bocsoft.mobile.bocmobile.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 安全因子接口返回数据的子条目数据
 * Created by liuweidong on 2016/7/28.
 */
public class CombinBean implements Parcelable{
    /**
     * id: 安全因子id
     */
    private String id;
    /**
     * name:安全因子名称
     */
    private String name;
    private List<String> safetyFactorList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSafetyFactorList() {
        return safetyFactorList;
    }

    public void setSafetyFactorList(List<String> safetyFactorList) {
        this.safetyFactorList = safetyFactorList;
    }

    @Override
    public String toString() {
        return "CombinBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", safetyFactorList=" + safetyFactorList +
                '}';
    }

    public CombinBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeStringList(this.safetyFactorList);
    }

    protected CombinBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.safetyFactorList = in.createStringArrayList();
    }

    public static final Creator<CombinBean> CREATOR = new Creator<CombinBean>() {
        @Override
        public CombinBean createFromParcel(Parcel source) {
            return new CombinBean(source);
        }

        @Override
        public CombinBean[] newArray(int size) {
            return new CombinBean[size];
        }
    };
}
