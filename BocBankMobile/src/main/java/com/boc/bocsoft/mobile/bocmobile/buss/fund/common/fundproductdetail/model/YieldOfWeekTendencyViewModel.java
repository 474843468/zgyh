package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview.IECharsData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 七日年化收益率view层model
 * Created by lzc4524 on 2016/12/2.
 */
public class YieldOfWeekTendencyViewModel implements IECharsData, Serializable {
    /**
     * 上送数据
     */
    private String fundId;//	基金Id
    private String fundCycle;

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getFundCycle() {
        return fundCycle;
    }

    public void setFundCycle(String fundCycle) {
        this.fundCycle = fundCycle;
    }

    /**
     * 返回数据
     */
    private List<FundList> items;//列表

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
            if(!StringUtil.isNullOrEmpty(item.getDate())&&!StringUtil.isNullOrEmpty(item.getDate())){
                xlist.add(item.getDate());
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
            if(!StringUtil.isNullOrEmpty(item.getYieldOfWeek())&&!StringUtil.isNullOrEmpty(item.getYieldOfWeek())){
                yList.add(item.getYieldOfWeek());
            }
        }
        return yList;
    }

    @Override
    public String getTitle() {
        return null;
    }

    public class FundList {
        private String date;//	日期
        private String yieldOfWeek;//	七日年化收益率

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getYieldOfWeek() {
            return yieldOfWeek;
        }

        public void setYieldOfWeek(String yieldOfWeek) {
            this.yieldOfWeek = yieldOfWeek;
        }
    }
}
