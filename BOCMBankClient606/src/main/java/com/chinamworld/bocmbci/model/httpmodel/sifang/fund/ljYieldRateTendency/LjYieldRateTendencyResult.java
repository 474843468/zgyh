package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.ljYieldRateTendency;

import com.chinamworld.bocmbci.biz.finc.finc_p606.util.FincUtil;
import com.chinamworld.bocmbci.userwidget.echarsview.IECharsData;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/29 0029.
 * 基金累计收益率--趋势图、历史累计收益
 */
public class LjYieldRateTendencyResult {

    private List<LjYieldRateTendencyItem> items;
    private String fundId;

    public List<LjYieldRateTendencyItem> getItem() {
        return items;
    }

    public void setItem(List<LjYieldRateTendencyItem> item) {
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

        for(LjYieldRateTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSzzjYieldRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                xlist.add(item.getJzTime());
            }
        }
        return xlist;
    }

    public List<String> getXList2() {//理财
        List<String> xlist = new ArrayList<String>();

        for(LjYieldRateTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSectionDepositRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                xlist.add(item.getJzTime());
            }
        }
        return xlist;
    }

    /**
     * 累计收益率
     * @return
     */
    public List<String> getLjYieldRateList() { //1:非理财型 2：理财型
        List<String> ljYieldRateList = new ArrayList<String>();
        for(LjYieldRateTendencyItem item : getItem()){
                if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                        &&!StringUtil.isNullOrEmpty(item.getSzzjYieldRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                    ljYieldRateList.add(FincUtil.setCent2(item.getLjYieldRate()));
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
        for(LjYieldRateTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSectionDepositRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                ljYieldRateList.add(FincUtil.setCent2(item.getLjYieldRate()));
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
        for(LjYieldRateTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSzzjYieldRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                szzjYieldRate.add(FincUtil.setCent2(item.getSzzjYieldRate()));
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
        for(LjYieldRateTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSzzjYieldRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                yjbjjzYieldRate.add(FincUtil.setCent2(item.getYjbjjzYieldRate()));
            }else if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSectionDepositRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                yjbjjzYieldRate.add(FincUtil.setCent2(item.getYjbjjzYieldRate()));
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
        for(LjYieldRateTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSectionDepositRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                yjbjjzYieldRate.add(FincUtil.setCent2(item.getYjbjjzYieldRate()));
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
        for(LjYieldRateTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getLjYieldRate())&&!StringUtil.isNullOrEmpty(item.getJzTime())
                    &&!StringUtil.isNullOrEmpty(item.getSectionDepositRate())&&!StringUtil.isNullOrEmpty(item.getYjbjjzYieldRate())){
                sectionDepositRate.add(FincUtil.setCent2(item.getSectionDepositRate()));
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

    public class LjYieldRateTendencyItem{
        private String jzTime;      //	净值时间
        private String ljYieldRate;      //	累计收益率
        private String szzjYieldRate;      //	上证综指收益率
        private String yjbjjzYieldRate;     //  业绩比较基准收益率
        private String sectionDepositRate;  //区间存款利率

        public String getLjYieldRate() {
            return ljYieldRate;
        }

        public String getJzTime() {
            return jzTime;
        }

        public String getSzzjYieldRate() {
            return szzjYieldRate;
        }

        public String getYjbjjzYieldRate() {
            return yjbjjzYieldRate;
        }

        public String getSectionDepositRate() {
            return sectionDepositRate;
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
