package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.jzTendency;

import com.chinamworld.bocmbci.biz.finc.finc_p606.util.FincUtil;
import com.chinamworld.bocmbci.userwidget.echarsview.IECharsData;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/29 0027.
 * 净值走势-趋势图、历史净值返回字段
 */
public class JzTendencyResult implements IECharsData {

    private List<JzTendencyItem> items;
    private String fundId;

    public List<JzTendencyItem> getItem() {
        return items;
    }

    public void setItem(List<JzTendencyItem> item) {
        this.items = item;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    @Override
    public List<String> getXList(){
        List<String> xlist = new ArrayList<String>();

        for(JzTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getDwjz())&&!StringUtil.isNullOrEmpty(item.getJzTime())){
                xlist.add(FincUtil.getcurrentDate(item.getJzTime()));
            }
        }
        return xlist;
    }
    @Override
    public List<String> getYList(){
        List<String> xlist = new ArrayList<String>();
        for(JzTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getDwjz())&&!StringUtil.isNullOrEmpty(item.getJzTime())){
                xlist.add(StringUtil.parseStringPattern(item.getDwjz(),3));
            }
        }
        return xlist;
    }

    @Override
    public String getTitle() {
        return "单位净值";
    }

    public class JzTendencyItem{
        public String getJzTime() {
            return jzTime;
        }

        public String getDwjz() {
            return dwjz;
        }

        public String getLjjz() {
            return ljjz;
        }

        public String getCurrPercentDiff() {
            return currPercentDiff;
        }

        private String jzTime;//	净值时间
        private String dwjz;//	单位净值
        private String ljjz;//	累计净值
        private String currPercentDiff;//日涨跌幅
    }
}
