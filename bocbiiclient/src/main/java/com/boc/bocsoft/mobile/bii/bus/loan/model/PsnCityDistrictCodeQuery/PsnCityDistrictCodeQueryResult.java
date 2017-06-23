package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCityDistrictCodeQuery;

import java.util.List;

/**
 * 省市区联动查询接口返回参数
 * Created by xintong on 2016/6/13.
 */
public class PsnCityDistrictCodeQueryResult {

        //字母码
        private String orgCode;
        //名称
        private String orgName;
        //省代码
        private String privCode;
        //省名称
        private String privName;
        //市级行政区划代码
        private String cityCode;
        //市名称
        private String cityName;
        //区/县级行政区划代码
        private String areaCode;
        //县名称
        private String areaName;
        //6位行政区划代码
        private String zoneCode;



       public String getOrgCode() {
           return orgCode;
       }

       public void setOrgCode(String orgCode) {
           this.orgCode = orgCode;
       }

       public String getOrgName() {
           return orgName;
       }

       public void setOrgName(String orgName) {
           this.orgName = orgName;
       }

       public String getPrivCode() {
           return privCode;
       }

       public void setPrivCode(String privCode) {
           this.privCode = privCode;
       }

       public String getPrivName() {
           return privName;
       }

       public void setPrivName(String privName) {
           this.privName = privName;
       }

       public String getCityCode() {
           return cityCode;
       }

       public void setCityCode(String cityCode) {
           this.cityCode = cityCode;
       }

       public String getCityName() {
           return cityName;
       }

       public void setCityName(String cityName) {
           this.cityName = cityName;
       }

       public String getAreaCode() {
           return areaCode;
       }

       public void setAreaCode(String areaCode) {
           this.areaCode = areaCode;
       }

       public String getAreaName() {
           return areaName;
       }

       public void setAreaName(String areaName) {
           this.areaName = areaName;
       }

       public String getZoneCode() {
           return zoneCode;
       }

       public void setZoneCode(String zoneCode) {
           this.zoneCode = zoneCode;
       }

}
