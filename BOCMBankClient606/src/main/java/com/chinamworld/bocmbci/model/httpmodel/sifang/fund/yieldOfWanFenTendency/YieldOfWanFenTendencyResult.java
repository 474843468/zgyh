package com.chinamworld.bocmbci.model.httpmodel.sifang.fund.yieldOfWanFenTendency;

import com.chinamworld.bocmbci.biz.finc.finc_p606.util.FincUtil;
import com.chinamworld.bocmbci.userwidget.echarsview.IECharsData;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/29 0029.
 * 万份收益率--趋势图、历史
 */
public class YieldOfWanFenTendencyResult implements IECharsData {

    private List<YieldOfWanFenTendencyItem> items;

    @Override
    public String getTitle() {
        return "万份收益";
    }

    private String fundId;

    public List<YieldOfWanFenTendencyItem> getItem() {
        return items;
    }

    public void setItem(List<YieldOfWanFenTendencyItem> item) {
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

        for(YieldOfWanFenTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getYieldOfTenThousand())&&!StringUtil.isNullOrEmpty(item.getDate())){
                xlist.add(FincUtil.getcurrentDate(item.getDate()));
            }
        }
        return xlist;
    }

    @Override
    public List<String> getYList() {
        List<String> ylist = new ArrayList<String>();
        for(YieldOfWanFenTendencyItem item : getItem()){
            if(!StringUtil.isNullOrEmpty(item.getYieldOfTenThousand())&&!StringUtil.isNullOrEmpty(item.getDate())){
                ylist.add(StringUtil.parseStringPattern(item.getYieldOfTenThousand(),3));
            }
        }
        return ylist;
    }

    public class YieldOfWanFenTendencyItem{
        private String date;                      //  日期
        private String yieldOfTenThousand;       //	万份收益

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
