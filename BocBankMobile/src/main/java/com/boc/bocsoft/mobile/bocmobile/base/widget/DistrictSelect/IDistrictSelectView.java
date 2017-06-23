package com.boc.bocsoft.mobile.bocmobile.base.widget.DistrictSelect;

import java.util.List;

/**
 * Created by XieDu on 2016/7/20.
 */
public interface IDistrictSelectView {

    void getFirstTabData();

    /**
     * 在第tabPosition层点击了某item，根据其code获得下一层的数据
     * @param tabPosition 第几个tab
     * @param code 被点击的item的地址代码
     */
    void getDistrictData(int tabPosition,String code);
    void setSelectDistrict(List<IAddress> list);
}
