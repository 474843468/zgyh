package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.rankTendency;

import com.chinamworld.bocmbci.userwidget.echarsview.IECharsData;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/29 0029.
 * 排名变化--趋势图、历史排名变化
 */
public class RankTendencyResult {

    private List<RankTendencyItem> items;
    private String fundId;

    public List<RankTendencyItem> getItem() {
        return items;
    }

    public void setItem(List<RankTendencyItem> item) {
        this.items = item;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public List<String> getXList() {
        List<String> xlist = new ArrayList<String>();

        for(RankTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getRankScore())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getRank())&&!StringUtil.isNullOrEmpty(item.getYieldSection())){
                xlist.add(item.getJzTime());
            }
        }
        return xlist;
    }

    public List<String> getYList() {
        List<String> xlist = new ArrayList<String>();
        for(RankTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getRankScore())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getRank())&&!StringUtil.isNullOrEmpty(item.getYieldSection())){
                String rankScore = StringUtil.append2Decimals(Double.parseDouble(item.getRankScore())*100+"",2) ;
                xlist.add(rankScore);
            }
        }
        return xlist;
    }

    public List<String> getRankYList() {
        List<String> xlist = new ArrayList<String>();
        for(RankTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getRankScore())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getRank())&&!StringUtil.isNullOrEmpty(item.getYieldSection())){
                xlist.add(item.getRank());
            }
        }
        return xlist;
    }

    public List<String> getYieldSectionYList() {
        List<String> xlist = new ArrayList<String>();
        for(RankTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getRankScore())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getRank())&&!StringUtil.isNullOrEmpty(item.getYieldSection())){
                xlist.add(item.getYieldSection());
            }
        }
        return xlist;
    }

    public class RankTendencyItem{
        private String jzTime;	        //  净值时间
        private String rankScore;       //	排名分位数
        private String rank;            //	排名
        private String similarCount;    //  收益区间

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

        public String getYieldSection() {
            return similarCount;
        }

        public void setYieldSection(String similarCount) {
            this.similarCount = similarCount;
        }
    }

    public IECharsData getLine1(){
        LjEChars line1 = new LjEChars();
        line1.setxList(getXList());
        line1.setListY(getYList());
        line1.setTitle("排名分位数");
        return line1;
    }

    public IECharsData getLine2(){
        LjEChars line1 = new LjEChars();
        line1.setxList(getXList());
        line1.setListY(getRankYList());
        line1.setTitle("排名");
        return line1;
    }

    public IECharsData getLine3(){
        LjEChars line1 = new LjEChars();
        line1.setxList(getXList());
        line1.setListY(getYieldSectionYList());
        return line1;
    }

    public class LjEChars implements IECharsData{
        private List<String> xList;
        private List<String> yList;
        private String title;

        public void setTitle(String title){
            this.title = title;
        }

        public void setxList(List<String> xList) {
            this.xList = xList;
        }

        public void setListY(List<String> ylist){
            this.yList = ylist;
        }

        @Override
        public List<String> getXList() {
            return xList;

        }

        @Override
        public List<String> getYList() {
            return yList;
        }

        @Override
        public String getTitle() {
            return title;
        }
    }
}
