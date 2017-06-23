package com.chinamworld.bocmbci.userwidget.sfkline;

/**
 * K线图，数据刷新接口
 * Created by yuht on 2016/10/21.
 */
public interface IRefreshKLineDataListener {


    /**
     *  /** 当前刷新数据监听
     * @param nIndex  当前K 线图，tabView选中项
     * @param nShowType  ： 当前显示的图像类型 1：k线图 2 折线图
     * @param isBackgroundRefresh  ： 是否是后台刷新
     */
    void onRefreshKLineDataCallBack(int nIndex, int nShowType, boolean isBackgroundRefresh);
}
