package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview.IECharsData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 排名变化view层数据
 * Created by lzc4524 on 2016/12/2.
 */
public class RankTendencyViewModel implements IECharsData, Serializable {
    /**
     * 上送参数
     */
    private String fundId;//基金Id
    private String fundCycle;

    public String getFundCycle() {
        return fundCycle;
    }

    public void setFundCycle(String fundCycle) {
        this.fundCycle = fundCycle;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public List<FundList> getItems() {
        return items;
    }

    public void setItems(List<FundList> items) {
        this.items = items;
    }

    /**
     * 返回参数
     */
    private List<FundList> items;//	列表

    @Override
    public List<String> getXList() {
        if(items == null){
            return null;
        }

        List<String> xlist = new ArrayList<String>();

        for(FundList item : items){
            if(!StringUtil.isNullOrEmpty(item.getJzTime())&&!StringUtil.isNullOrEmpty(item.getRank())){
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
            if(!StringUtil.isNullOrEmpty(item.getRank())&&!StringUtil.isNullOrEmpty(item.getJzTime())){
                yList.add(item.getRank());
            }
        }
        return yList;
    }

    @Override
    public String getTitle() {
        return "排名";
    }

    public class FundList {
        private String jzTime;// 净值时间
        private String rankScore;// 排名分位数
        private String rank;// 排名
        private String similarCount;// 同类总量

        public String getSimilarCount() {
            return similarCount;
        }

        public void setSimilarCount(String similarCount) {
            this.similarCount = similarCount;
        }

        public String getJzTime() {
            return jzTime;
        }

        public void setJzTime(String jzTime) {
            this.jzTime = jzTime;
        }

        public String getRankScore() {
            return rankScore;
        }

        public void setRankScore(String rankScore) {
            this.rankScore = rankScore;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

    }

}
