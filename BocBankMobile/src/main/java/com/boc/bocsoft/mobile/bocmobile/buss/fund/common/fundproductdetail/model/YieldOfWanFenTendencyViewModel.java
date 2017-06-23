package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview.IECharsData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 万份收益率view层model
 * Created by lzc4524 on 2016/12/2.
 */
public class YieldOfWanFenTendencyViewModel implements IECharsData, Serializable {
    /**
     * 上送参数
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
            if(!StringUtil.isNullOrEmpty(item.getYieldOfTenThousand())&&!StringUtil.isNullOrEmpty(item.getYieldOfTenThousand())){
                yList.add(item.getYieldOfTenThousand());
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
        private String yieldOfTenThousand;//	万份收益

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getYieldOfTenThousand() {
            return yieldOfTenThousand;
        }

        public void setYieldOfTenThousand(String yieldOfTenThousand) {
            this.yieldOfTenThousand = yieldOfTenThousand;
        }
    }
}
