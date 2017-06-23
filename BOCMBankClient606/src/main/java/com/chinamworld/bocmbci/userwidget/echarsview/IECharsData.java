package com.chinamworld.bocmbci.userwidget.echarsview;

import java.util.List;

/**
 * EChars折线图数据结构接口
 * Created by yuht on 2016/10/25.
 */
public interface IECharsData {
    List<String> getXList();

    List<String> getYList();

    String getTitle();
}
