package com.chinamworld.bocmbci.model.httpmodel.sifang.queryAverageTendency;

import com.chinamworld.bocmbci.userwidget.echarsview.IECharsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class QueryAverageTendencyResult implements IECharsData {
    @Override
    public String getTitle() {
        return "参考价格";
    }

    private List<QueryAverageTendencyItem> tendencyList;


    public class QueryAverageTendencyItem {
        public String getTimestamp() {
            return timestamp;
        }

        public String getAveragePrice() {
            return averagePrice;
        }

        private String timestamp;
        private String averagePrice;
    }
    @Override
    public List<String> getXList(){
        List<String> xlist = new ArrayList<String>();

        for(QueryAverageTendencyItem item : tendencyList){
            xlist.add(item.getTimestamp());
        }
        return xlist;
    }
    @Override
    public List<String> getYList(){
        List<String> xlist = new ArrayList<String>();
        for(QueryAverageTendencyItem item : tendencyList){
            xlist.add(item.getAveragePrice());
        }
        return xlist;
    }
}
