package com.chinamworld.bocmbci.net.model;


import com.chinamworld.bocmbci.net.GsonTools;

/**
 * Created by Administrator on 2016/10/19.
 */
public abstract class BaseRequestModel extends BaseOkHttpModel {
    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public String toJson() {
        return GsonTools.toJson(this);
    }
}
