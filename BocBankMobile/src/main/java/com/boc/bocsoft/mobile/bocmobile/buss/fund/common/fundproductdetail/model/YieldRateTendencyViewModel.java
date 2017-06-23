package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview.IECharsData;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 累计收益view层数据
 * Created by lzc4524 on 2016/12/2.
 */
public class YieldRateTendencyViewModel implements IECharsData, Serializable {
    /**
     * 上送参数
     */
    private String fundId;//基金Id
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
    private List<FundList> items;

    public List<FundList> getItems() {
        return items;
    }

    public void setItems(List<FundList> item) {
        this.items = item;
    }


    public List<String> getXList() {
        List<String> xlist = new ArrayList<String>();

        for(FundList item : getItems()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSzzjYieldRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                xlist.add(item.getJzTime());
            }
        }
        return xlist;
    }

    @Override
    public List<String> getYList() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    public List<String> getXList2() {//理财
        List<String> xlist = new ArrayList<String>();

        for(FundList item : getItems()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSectionDepositRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                xlist.add(item.getJzTime());
            }
        }
        return xlist;
    }

    /**
     * 把数字转换成百分数，不带%
     * @param value
     */
    private String getFormatResult(String value){
        if(StringUtils.isEmptyOrNull(value) || value.trim().equals("")){
            return "";
        }

        double dValue = Double.parseDouble(value);
        return (new DecimalFormat("#.00")).format(dValue);
//        return String.valueOf(dValue * 100);
    }

    /**
     * 累计收益率
     * @return
     */
    public List<String> getLjYieldRateList() { //1:非理财型 2：理财型
        List<String> ljYieldRateList = new ArrayList<String>();
        for(FundList item : getItems()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSzzjYieldRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                ljYieldRateList.add(getFormatResult(item.getLjYieldRate()));
            }
        }
        return ljYieldRateList;
    }

    /**
     * 累计收益率
     * @return
     */
    public List<String> getLjYieldRateList2() { //1:非理财型 2：理财型
        List<String> ljYieldRateList = new ArrayList<String>();
        for(FundList item : getItems()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSectionDepositRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                ljYieldRateList.add(getFormatResult(item.getLjYieldRate()));
            }
        }
        return ljYieldRateList;
    }

    /**
     * 上证综指收益率
     * @return
     */
    public List<String> getSzzjYieldRateList() {
        List<String> szzjYieldRate = new ArrayList<String>();
        for(FundList item : getItems()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSzzjYieldRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                szzjYieldRate.add(getFormatResult(item.getSzzjYieldRate()));
            }
        }
        return szzjYieldRate;
    }

    /**
     * 业绩比较基准收益率
     * @return
     */
    public List<String> getYjbjjzYieldRateList() {
        List<String> yjbjjzYieldRate = new ArrayList<String>();
        for(FundList item : getItems()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSzzjYieldRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                yjbjjzYieldRate.add(getFormatResult(item.getYjbjjzYieldRate()));
            }else if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSectionDepositRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                yjbjjzYieldRate.add(getFormatResult(item.getYjbjjzYieldRate()));
            }
        }
        return yjbjjzYieldRate;
    }

    /**
     * 业绩比较基准收益率
     * @return
     */
    public List<String> getYjbjjzYieldRateList2() {
        List<String> yjbjjzYieldRate = new ArrayList<String>();
        for(FundList item : getItems()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSectionDepositRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                yjbjjzYieldRate.add(getFormatResult(item.getYjbjjzYieldRate()));
            }
        }
        return yjbjjzYieldRate;
    }

    /**
     * 区间存款利率
     * @return
     */
    public List<String> getSectionDepositRateList() {
        List<String> sectionDepositRate = new ArrayList<String>();
        for(FundList item : getItems()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSectionDepositRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                sectionDepositRate.add(getFormatResult(item.getSectionDepositRate()));
            }
        }
        return sectionDepositRate;
    }

    /**
     * 累计收益率
     * @return
     */
    public IECharsData getLine1(){
        LjEChars line1 = new LjEChars();
        line1.setxList(getXList());
        line1.setListY(getLjYieldRateList());
        line1.setTitle("累计收益率");
        return line1;
    }

    /**
     * 累计收益率 理财型
     * @return
     */
    public IECharsData getLine12(){
        LjEChars line1 = new LjEChars();
        line1.setxList(getXList2());
        line1.setListY(getLjYieldRateList2());
        line1.setTitle("累计收益率");
        return line1;
    }

    /**
     * 上证综指收益率
     * @return
     */
    public IECharsData getLine2(){
        LjEChars line2 = new LjEChars();
        line2.setxList(getXList());
        line2.setListY(getSzzjYieldRateList());
        line2.setTitle("上证综指收益率");
        return line2;
    }

    /**
     * 业绩比较基准收益率
     * @return
     */
    public IECharsData getLine3(){
        LjEChars line3 = new LjEChars();
        line3.setxList(getXList());
        line3.setListY(getYjbjjzYieldRateList());
        line3.setTitle("业绩比较基准收益率");
        return line3;
    }

    /**
     * 业绩比较基准收益率 理财型
     * @return
     */
    public IECharsData getLine32(){
        LjEChars line3 = new LjEChars();
        line3.setxList(getXList2());
        line3.setListY(getYjbjjzYieldRateList2());
        line3.setTitle("业绩比较基准收益率");
        return line3;
    }

    /**
     * 区间存款利率
     * @return
     */
    public IECharsData getLine4(){
        LjEChars line4 = new LjEChars();
        line4.setxList(getXList2());
        line4.setListY(getSectionDepositRateList());
        line4.setTitle("区间存款利率");
        return line4;
    }

    public class FundList {
        private String jzTime;//	净值时间
        private String ljYieldRate;//	累计收益率
        private String szzjYieldRate;//	上证综指收益率
        private String yjbjjzYieldRate;//	业绩比较基准收益率
        //	区间存款利率,理财型的基金显示区间存款利率
        private String sectionDepositRate;

        public String getJzTime() {
            return jzTime;
        }

        public void setJzTime(String jzTime) {
            this.jzTime = jzTime;
        }

        public String getLjYieldRate() {
            return ljYieldRate;
        }

        public void setLjYieldRate(String ljYieldRate) {
            this.ljYieldRate = ljYieldRate;
        }

        public String getSzzjYieldRate() {
            return szzjYieldRate;
        }

        public void setSzzjYieldRate(String szzjYieldRate) {
            this.szzjYieldRate = szzjYieldRate;
        }

        public String getYjbjjzYieldRate() {
            return yjbjjzYieldRate;
        }

        public void setYjbjjzYieldRate(String yjbjjzYieldRate) {
            this.yjbjjzYieldRate = yjbjjzYieldRate;
        }

        public String getSectionDepositRate() {
            return sectionDepositRate;
        }

        public void setSectionDepositRate(String sectionDepositRate) {
            this.sectionDepositRate = sectionDepositRate;
        }
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
