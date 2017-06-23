package com.boc.bocma.serviceinterface.op.interfacemodel.querynearbybranches;

import android.text.TextUtils;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 网点地理位置信息
 */
public class MAOPBranchGEOInfo extends MAOPBaseResponseModel {
    /**
     * 网点名称
     */
    private static final String BRANCH_NAME_KEY = "brchname";
    /**
     * 网点号
     */
    private static final String BRANCH_NUMBER_KEY = "brchno";
    /**
     * 距离
     */
    private static final String DISTANCE_KEY = "distance";
    /**
     * 经度
     */
    private static final String LONGITUDE_KEY = "longitude";
    /**
     * 纬度
     */
    private static final String LATITUDE_KEY = "latitude";
    /**
     * 地址
     */
    private static final String ADDRESS_KEY = "addr";
    /**
     * 地点类型
     */
    private static final String TYPE_KEY = "type";

    private static final int ENABLE = 1;
    
    private static final int DISABLE = 0;
    
    /**
     * 是否可以预约  “人民币大额取现” （1：是 0：否）
     */
    private static final String IF_BUS_TYPE1_KEY = "ifbustype1";
    /**
     * 是否可以预约  “外币取现” （1：是 0：否）
     */
    private static final String IF_BUS_TYPE2_KEY = "ifbustype2";
    /**
     * 是否可以预约  “换卡/挂失取卡” （1：是 0：否）
     */
    private static final String IF_BUS_TYPE3_KEY = "ifbustype3";
    /**
     * “预约时间段” 如 ： 08001100:11001400:14001700:17001800   表示四个时间段，第一个时间段是08：00-11：00
     */
    private static final String BUSTTIME_KEY = "busttime";
    /**
     * 预约币种 以“,”号分隔
     */
    private static final String CURRENCY_KEY = "currency";
    
    public static final Creator<MAOPBranchGEOInfo> CREATOR = new Creator<MAOPBranchGEOInfo>(){
        @Override
        public MAOPBranchGEOInfo createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPBranchGEOInfo(jsonResponse);
        }
    };
    
    public MAOPBranchGEOInfo(JSONObject jsonResponse) {
        branchName = jsonResponse.optString(BRANCH_NAME_KEY);
        branchNo = jsonResponse.optString(BRANCH_NUMBER_KEY);
        distance = jsonResponse.optLong(DISTANCE_KEY);
        longitude = jsonResponse.optString(LONGITUDE_KEY);
        latitude = jsonResponse.optString(LATITUDE_KEY);
        address = jsonResponse.optString(ADDRESS_KEY);
        type = jsonResponse.optInt(TYPE_KEY);
        reminbiWithdrawOrderFlag = jsonResponse.optInt(IF_BUS_TYPE1_KEY);
        foreignCurrencyWithdrawOrderFlag = jsonResponse.optInt(IF_BUS_TYPE2_KEY);
        cardServiceOrderFlag = jsonResponse.optInt(IF_BUS_TYPE3_KEY);
        acceptableOrderTimes = extractAcceptableOrderTime(jsonResponse.optString(BUSTTIME_KEY));
        acceptableCurrencyList = extractAcceptableCurrency(jsonResponse.optString(CURRENCY_KEY));
    }
    
    private List<String> extractAcceptableCurrency(String currenciesInJson) {
        List<String> list = new ArrayList<String>();
        if (! TextUtils.isEmpty(currenciesInJson)) {
            String[] currencies = currenciesInJson.split(",");
            for (String currency : currencies) {
                list.add(currency);
            }
        }
        return list;
    }

    /**
     * 从服务器报文中解析预约时间段
     */
    private String extractAcceptableOrderTime(String original) {
        String translated = "";
        if (TextUtils.isEmpty(original) || "null".equalsIgnoreCase(original)) {
            translated = "00002400";
        } else {
            String[] orderTimes = original.split("\\" + SEPARATOR);
            translated = orderTimes[0];
        }
        return translated;
    }
    
    private static final String SEPARATOR = ":";
    
    /**
     * @return 网点名称
     */
    public String getBranchName() {
        return branchName;
    }
    /**
     * @return 网点号
     */
    public String getBranchNo() {
        return branchNo;
    }
    /**
     * @return 距离
     */
    public long getDistance() {
        return distance;
    }
    /**
     * @return 经度
     */
    public String getLongitude() {
        return longitude;
    }
    /**
     * @return 纬度
     */
    public String getLatitude() {
        return latitude;
    }
    /**
     * @return 地址
     */
    public String getAddress() {
        return address;
    }
    /**
     * @return 地点类型
     */
    public int getType() {
        return type;
    }
    /**
     * 是否可以预约  “人民币大额取现“
     */
    public boolean isReminbiWithdrawOrderAcceptable() {
        return reminbiWithdrawOrderFlag == ENABLE;
    }
    /**
     * 是否可以预约  “外币取现”
     */
    public boolean isForeignCurrencyWithdrawOrderAcceptable() {
        return foreignCurrencyWithdrawOrderFlag == ENABLE;
    }
    /**
     * 是否可以预约  “换卡/挂失取卡”
     */
    public boolean isCardServiceOrderAcceptable() {
        return cardServiceOrderFlag == ENABLE;
    }
    /**
     * 可预约时间段 格式08001100
     */
    public String getAcceptableOrderTime() {
        return acceptableOrderTimes;
    }
    
    /**
     * 可预约货币种类
     */
    public List<String> getAcceptableCurrencyList() {
        return acceptableCurrencyList;
    }
    
    public boolean isReminbiWithdrawOrderAcceptable = true;
    public boolean isForeignCurrencyWithdrawOrderAcceptable = true;
    public boolean isCardServiceOrderAcceptable = true;
    private String branchName;
    private String branchNo;
    private long distance;
    private String longitude;
    private String latitude;
    private String address;
    private int type;
    private int reminbiWithdrawOrderFlag;
    private int foreignCurrencyWithdrawOrderFlag;
    private int cardServiceOrderFlag;
    private String acceptableOrderTimes;
    private List<String> acceptableCurrencyList = new ArrayList<String>();
}
