package com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.sfkline;

import java.util.List;

/**
 * 四方K线图数据获取接口
 * Created by Administrator on 2016/10/21.
 */
public abstract class BaseSFKLineData {
    /**  获得柱状图数据 */
    public abstract List<OHLCItem> getOHLCList();

    /** 获得均线图数据 */
    public abstract List<StickItem> getStickList();

    /** 新增数据项 */
    public void add(BaseSFKLineData data){
        List<OHLCItem> newOHLCList = data.getOHLCList();
        List<StickItem> newStickList = data.getStickList();
        if(getOHLCList() != null && getOHLCList().size() > 0){
            getOHLCList().remove(getOHLCList().size() - 1);
        }
        if(getStickList() != null && getStickList().size() > 0){
            getStickList().remove(getStickList().size() - 1);
        }
        for(OHLCItem item :newOHLCList){
            getOHLCList().add(item);
        }
        for(StickItem item :newStickList){
            getStickList().add(item);
        }
    }

    /** 获得最新的时间区间，用于增量更新 */
    public String getNewTimeZone(){
        if(getOHLCList() == null || getOHLCList().size() <=0)
            return "";
        return getOHLCList().get(getOHLCList().size() - 1).getDate();
    }
}
