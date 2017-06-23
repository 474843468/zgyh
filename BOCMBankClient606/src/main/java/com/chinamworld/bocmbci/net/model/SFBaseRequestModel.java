package com.chinamworld.bocmbci.net.model;

import com.chinamworld.bocmbci.constant.SystemConfig;

/**
 * 四方接口通信基类
 * Created by Administrator on 2016/10/19.
 */
public abstract class SFBaseRequestModel extends BaseRequestModel {
    @Override
    public String getUrl() {

          return SystemConfig.SF_URL;
    }
}
