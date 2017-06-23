package com.boc.bocsoft.mobile.bocmobile.buss.system.life.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by eyding on 16/8/2.
 */
public class LifeVo {


  public static class ProvinceVo implements Parcelable{

    private String name;
    private String shortName;
    private List<CityVo> cityVoList;

    public ProvinceVo(){}
    public ProvinceVo(String name,String shortName){
      this.name = name;
      this.shortName = shortName;
    }
    protected ProvinceVo(Parcel in) {
      name = in.readString();
      shortName = in.readString();
      cityVoList = in.createTypedArrayList(CityVo.CREATOR);
    }

    public static final Creator<ProvinceVo> CREATOR = new Creator<ProvinceVo>() {
      @Override public ProvinceVo createFromParcel(Parcel in) {
        return new ProvinceVo(in);
      }

      @Override public ProvinceVo[] newArray(int size) {
        return new ProvinceVo[size];
      }
    };

    public String getName() {
      return name;
    }

    public String getShortName() {
      return shortName;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setShortName(String shortName) {
      this.shortName = shortName;
    }

    public List<CityVo> getCityVoList() {
      return cityVoList;
    }

    public void setCityVoList(List<CityVo> cityVoList) {
      this.cityVoList = cityVoList;
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(name);
      dest.writeString(shortName);
      dest.writeTypedList(cityVoList);
    }
  }

  public static class CityVo implements Parcelable{
    private ProvinceVo provinceVo;
    private String name;//名称
    private String code;//代码

    public CityVo(){}
    public CityVo(String name,String code){
      this.name = name;
      this.code = code;
    }
    public CityVo(String name,String code,String proName,String proShortName){
      this.name = name;
      this.code = code;
      this.provinceVo = new ProvinceVo(proName,proShortName);
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeParcelable(provinceVo, flags);
      dest.writeString(name);
      dest.writeString(code);
    }

    @Override public int describeContents() {
      return 0;
    }

    public static final Creator<CityVo> CREATOR = new Creator<CityVo>() {
      @Override public CityVo createFromParcel(Parcel in) {
        return new CityVo(in);
      }

      @Override public CityVo[] newArray(int size) {
        return new CityVo[size];
      }
    };

    public String getName() {
      return name;
    }

    public String getCode() {
      return code;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public ProvinceVo getProvinceVo() {
      return provinceVo;
    }

    public void setProvinceVo(ProvinceVo provinceVo) {
      this.provinceVo = provinceVo;
    }




    protected CityVo(Parcel in) {
      name = in.readString();
      code = in.readString();

    }

  }
}
