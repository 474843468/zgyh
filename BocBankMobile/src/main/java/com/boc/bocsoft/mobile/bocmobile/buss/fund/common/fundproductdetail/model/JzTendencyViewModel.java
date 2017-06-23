package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview.IECharsData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 净值走势 view层数据
 * Created by lzc4524 on 2016/11/26.
 */
public class JzTendencyViewModel implements IECharsData, Serializable {
    /**
     * 上传参数
     */
    private String fundId;//	基金Id



    /**
     * 周期
     * m-一个月
     * q-三个月
     * h-半年
     * y-一年
     */
    private String fundCycle;

    public String getFundId() {
        return fundId;
    }
    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public void setFundCycle(String fundCycle) {
        this.fundCycle = fundCycle;
    }
    public String getFundCycle() {
        return fundCycle;
    }


    /**
     * 返回参数
     */
    private List<FundList> items;//	列表

    public List<FundList> getItems() {
        return items;
    }

    public void setItems(List<FundList> items) {
        this.items = items;
    }

    @Override
    public List<String> getXList() {
        if(items == null){
            return null;
        }

        List<String> xlist = new ArrayList<String>();

        for(FundList item : items){
            if(!StringUtil.isNullOrEmpty(item.getDwjz())&&!StringUtil.isNullOrEmpty(item.getJzTime())){
                xlist.add(item.getJzTime());
            }
        }
        return xlist;
    }

    @Override
    public List<String> getYList() {
        if(items == null){
            return null;
        }

        List<String> yList = new ArrayList<String>();

        for(FundList item : items){
            if(!StringUtil.isNullOrEmpty(item.getDwjz())&&!StringUtil.isNullOrEmpty(item.getJzTime())){
                yList.add(item.getDwjz());
            }
        }
        return yList;
    }

    @Override
    public String getTitle() {
        return "单位净值";
    }

    public class FundList {
        private String jzTime;//	净值时间
        private String dwjz;//	单位净值
        private String ljjz;//	累计净值
        private String changeOfDay;//	日涨跌幅

        public String getJzTime() {
            return jzTime;
        }

        public void setJzTime(String jzTime) {
            this.jzTime = jzTime;
        }

        public String getDwjz() {
            return dwjz;
        }

        public void setDwjz(String dwjz) {
            this.dwjz = dwjz;
        }

        public String getLjjz() {
            return ljjz;
        }

        public void setLjjz(String ljjz) {
            this.ljjz = ljjz;
        }

        public String getChangeOfDay() {
            return changeOfDay;
        }

        public void setChangeOfDay(String changeOfDay) {
            this.changeOfDay = changeOfDay;
        }
    }
}
