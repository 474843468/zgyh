package com.chinamworld.bocmbci.model.httpmodel.sifang;


import com.chinamworld.bocmbci.net.model.SFBaseRequestModel;

/**
 * 四方外汇、贵金属上送请求基类
 * Created by yuht on 2016/10/25.
 */
public abstract class BaseSFForexRequestModel extends SFBaseRequestModel {
    @Override
    public String getUrl() {
        return super.getUrl() + "forex/" + getExtendParam();
    }
}
