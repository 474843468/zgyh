package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanCityQry;

import java.util.List;

/**
 * Created by XieDu on 2016/7/19.
 */
public class PsnOnLineLoanCityQryResult {


    private List<PsnOnLineLoanCityQryBean> list;

    public List<PsnOnLineLoanCityQryBean> getList() { return list;}

    public void setList(List<PsnOnLineLoanCityQryBean> list) { this.list = list;}

    public static class PsnOnLineLoanCityQryBean {
        private String cityCode;
        private String cityName;

        public String getCityCode() { return cityCode;}

        public void setCityCode(String cityCode) { this.cityCode = cityCode;}

        public String getCityName() { return cityName;}

        public void setCityName(String cityName) { this.cityName = cityName;}
    }
}
