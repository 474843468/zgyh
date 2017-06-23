package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.yieldOfWeekTendency;

import com.chinamworld.bocmbci.biz.finc.finc_p606.util.FincUtil;
import com.chinamworld.bocmbci.userwidget.echarsview.IECharsData;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/29 0029.
 * 七日年化收益率--趋势图、历史
 */
public class YieldOfWeekTendencyResult implements IECharsData {

    private List<YieldOfWeekTendencyItem> items;

    @Override
    public String getTitle() {
        return "七日年化收益率";
    }

    private String fundId;

    public List<YieldOfWeekTendencyItem> getItem() {
        return items;
    }

    public void setItem(List<YieldOfWeekTendencyItem> item) {
        this.items = item;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    @Override
    public List<String> getXList() {
        List<String> xlist = new ArrayList<String>();

        for(YieldOfWeekTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getYieldOfWeek())&&!StringUtil.isNullOrEmpty(item.getDate())){
                xlist.add(FincUtil.getcurrentDate(item.getDate()));
            }
        }
        return xlist;
    }

    @Override
    public List<String> getYList() {
        List<String> ylist = new ArrayList<String>();
        for(YieldOfWeekTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getYieldOfWeek())&&!StringUtil.isNullOrEmpty(item.getDate())){
                ylist.add(StringUtil.parseStringPattern(item.getYieldOfWeek(),3));
            }
        }
        return ylist;
    }

    public class YieldOfWeekTendencyItem{
        private String date;       //  日期
        private String yieldOfWeek; //   七日年化收益率

        public String getDate() {
            return date;
        }

        public String getYieldOfWeek() {
            return yieldOfWeek;
        }
    }
}
