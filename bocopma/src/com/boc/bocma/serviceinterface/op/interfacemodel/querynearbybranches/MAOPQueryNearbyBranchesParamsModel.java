package com.boc.bocma.serviceinterface.op.interfacemodel.querynearbybranches;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 查询附近网点地理位置参数类
 */
public class MAOPQueryNearbyBranchesParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "unlogin/querygeoinfo";
    
    /**
     * 经度
     */
    private static final String LONGITUDE_KEY = "longitude";
    /**
     * 纬度
     */
    private static final String LATITUDE_KEY = "latitude";
    /**
     * 地点类型 0-All, 1-ATM, 2-网点，3-商户
     */
    private static final String TYPE_KEY = "type";
    /**
     * 范围半径
     */
    private static final String RANGE_KEY = "range";

    /**
     * 地点类型
     */
    public static final class LocationType {
        /**
         * All
         */
        public static final int All = 0;
        /**
         * ATM
         */
        public static final int ATM = 1;
        /**
         * 网点
         */
        public static final int BRANCH = 2;
        /**
         * 商户
         */
        public static final int ENTERPRISE = 3;
    }
    
    private String longitude;

    /**
     * @param longitude 经度
     */
    public void setLogitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @param latitude 纬度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    /**
     * @param type 地点类型 0-All, 1-ATM, 2-网点，3-商户
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @param range 范围半径，总长度5，最大为9999米
     */
    public void setRange(int range) {
        this.range = range;
    }

    private String latitude;

    private int type;

    private int range;
    
    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.putOpt(LONGITUDE_KEY, longitude);
        body.putOpt(LATITUDE_KEY, latitude);
        body.putOpt(TYPE_KEY, type);
        body.putOpt(RANGE_KEY, range);
        return body.toString();
    }

}
