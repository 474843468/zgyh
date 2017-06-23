package com.chinamworld.bocmbci.biz.foreign;

import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 外汇数据中心
 * @author luqp 2016/10/8
 */
public class ForeignDataCenter {
    private static ForeignDataCenter dataCenter;
    private ForeignDataCenter() {}

    public static ForeignDataCenter getInstance() {
        if (dataCenter == null) {
            dataCenter = new ForeignDataCenter();
        }
        return dataCenter;
    }
    /** 用户定制货币对*/
    private List<Map<String, Object>> userCurrencyData;
    /** 选中项列表*/
    private Map<String, Object> selectCurrencyMap;
    /** 全部汇率数据*/
    private List<Map<String,Object>> allRateListData;

    public List<Map<String, Object>> getAllRateListData() {
        return allRateListData;
    }

    public void setAllRateListData(List<Map<String, Object>> allRateListData) {
        if (null == allRateListData)
            this.allRateListData = new ArrayList<Map<String, Object>>();
        else
            this.allRateListData = allRateListData;
    }



    public List<Map<String, Object>> getUserCurrencyData() {
        return userCurrencyData;
    }

    public void setUserCurrencyData(List<Map<String, Object>> myRegAccountList) {
        if (null == myRegAccountList)
            this.userCurrencyData = new ArrayList<Map<String, Object>>();
        else
            this.userCurrencyData = myRegAccountList;
    }


    public Map<String, Object> getSelectCurrencyMap() {
        return selectCurrencyMap;
    }

    public void setSelectCurrencyMap(Map<String, Object> selectCurrencyMap) {
        if (null == selectCurrencyMap)
            this.selectCurrencyMap = new HashMap<String, Object>();
        else
            this.selectCurrencyMap = selectCurrencyMap;
    }

    /** 清除datacenter所有数据*/
    public void clearForeignData() {
        if (!StringUtil.isNullOrEmpty(userCurrencyData)) {
            userCurrencyData.clear();
        }
        if (!StringUtil.isNullOrEmpty(selectCurrencyMap)){
            selectCurrencyMap.clear();
        }
        if (!StringUtil.isNullOrEmpty(allRateListData)){
            allRateListData.clear();
        }
    }
}
