package com.chinamworld.bocmbci.model.httpmodel.sifang.queryktendency;


import com.chinamworld.bocmbci.userwidget.sfkline.BaseSFKLineData;
import com.chinamworld.bocmbci.userwidget.sfkline.OHLCItem;
import com.chinamworld.bocmbci.userwidget.sfkline.StickItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public class QueryKTendencyResult extends BaseSFKLineData {
    public ArrayList<KLineItem> getkLine() {
        return kList;
    }



    private ArrayList<KLineItem> kList;

    public void setCcygrpNm(String ccygrpNm) {
        this.ccygrpNm = ccygrpNm;
    }

    public void setCcygrp(String ccygrp) {
        this.ccygrp = ccygrp;
    }

    public void setkLine(ArrayList<KLineItem> kLine) {
        this.kList = kLine;
    }

    private String ccygrpNm;

    private String ccygrp;



    /** 获得柱状图数据 */
    @Override
    public List<OHLCItem> getOHLCList() {
        ArrayList<OHLCItem> ohlcList = new ArrayList<OHLCItem>();
        if(getkLine() == null || getkLine().size() <=0){
            return ohlcList;
        }
        ohlcList.clear();
        OHLCItem tmp;
        for(KLineItem item : getkLine()){
            try {
                tmp = new OHLCItem();
                tmp.setOpen(Double.parseDouble(item.getOpenPrice()));
                tmp.setClose(Double.parseDouble(item.getClosePrice()));
                tmp.setHigh(Double.parseDouble(item.getMaxPrice()));
                tmp.setLow(Double.parseDouble(item.getMinPrice()));
                tmp.setDate(item.getTimeZone());
                tmp.setTimeStamp(item.getTimeStamp());
                ohlcList.add(tmp);
            }
            catch (Exception e){

            }
        }

        return ohlcList;
    }

    /** 获得均线图数据 */
    @Override
    public List<StickItem> getStickList() {
        ArrayList<StickItem> stickList = new ArrayList<StickItem>();

        if(getkLine() == null || getkLine().size() <=0){
            return stickList;
        }
        stickList.clear();
        StickItem stick = new StickItem();
        for(KLineItem item : getkLine()){
            try {
                stick.setHigh(Double.parseDouble(item.getMaxPrice()));
                stick.setLow(Double.parseDouble(item.getMinPrice()));
                stick.setDate(item.getTimeStamp());
                stickList.add(stick);
            }
            catch (Exception e){
            }
        }

        return stickList;
    }


    public class KLineItem{
        public String getTimeStamp() {
            return timestamp;
        }

        public String getCloseTime() {
            return closeTime;
        }

        public String getOpenPrice() {
            return openPrice;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public String getMinPrice() {
            return minPrice;
        }

        public String getClosePrice() {
            return closePrice;
        }

        public String getTimeZone() {
            return timeZone;
        }

        private String timestamp;
        private String closeTime;
        private String openPrice;
        private String maxPrice;
        private String minPrice;
        private String closePrice;
        private String timeZone;

        public void setClosePrice(String closePrice) {
            this.closePrice = closePrice;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public void setMinPrice(String minPrice) {
            this.minPrice = minPrice;
        }

        public void setMaxPrice(String maxPrice) {
            this.maxPrice = maxPrice;
        }

        public void setOpenPrice(String openPrice) {
            this.openPrice = openPrice;
        }

        public void setCloseTime(String closeTime) {
            this.closeTime = closeTime;
        }


    }
}
