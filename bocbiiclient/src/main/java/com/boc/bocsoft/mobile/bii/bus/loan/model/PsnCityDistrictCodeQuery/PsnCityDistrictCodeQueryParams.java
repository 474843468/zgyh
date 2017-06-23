package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCityDistrictCodeQuery;

/**
 * 省市区联动查询接口请求参数
 * Created by xintong on 2016/6/13.
 */
public class PsnCityDistrictCodeQueryParams {
    /**
     * 6位行政区划代码
     * 当查询省列表时，初始zoneCode送000000
     * 查市或县时，将上一级查询返回的zoneCode作为上送项。
     */
    private String zoneCode;

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }
}
