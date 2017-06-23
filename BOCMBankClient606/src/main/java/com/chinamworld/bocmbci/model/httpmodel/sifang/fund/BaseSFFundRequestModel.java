package com.chinamworld.bocmbci.model.httpmodel.sifang.fund;


import com.chinamworld.bocmbci.net.model.SFBaseRequestModel;

/**
 * 四方基金上送请求基类
 *
 */
public abstract class BaseSFFundRequestModel extends SFBaseRequestModel {
    @Override
    public String getUrl() {
        return super.getUrl() + "fund/" + getExtendParam();
    }
}
