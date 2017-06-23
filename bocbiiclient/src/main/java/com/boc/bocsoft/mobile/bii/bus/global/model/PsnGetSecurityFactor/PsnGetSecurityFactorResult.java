package com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor;

import java.util.List;

/**
 * Created by feibin on 2016/6/16.
 * 安全因子结果Model
 */
public class PsnGetSecurityFactorResult {

    /**
     * 客户默认的安全因子组合  name:安全因子名称  id: 安全因子id
     * 0:虚拟 4:USBKey证书 8:动态口令令牌 32:短信认证码 40:动态口令令牌+短信认证码 96:短信认证码+硬件绑定
     */
    private CombinListBean _defaultCombin;
    /**
     * 客户默认的安全因子组合  name:安全因子名称  id: 安全因子id
     * 0:虚拟 4:USBKey证书 8:动态口令令牌 32:短信认证码 40:动态口令令牌+短信认证码 96:短信认证码+硬件绑定
     */
    private List<CombinListBean> _combinList;

    public List<CombinListBean> get_combinList() {
        return _combinList;
    }

    public void set_combinList(List<CombinListBean> _combinList) {
        this._combinList = _combinList;
    }

    public CombinListBean get_defaultCombin() {
        return _defaultCombin;
    }

    public void set_defaultCombin(CombinListBean _defaultCombin) {
        this._defaultCombin = _defaultCombin;
    }
    @Override
    public String toString() {
        return "PsnGetSecurityFactorResult{" +
                "_defaultCombin=" + _defaultCombin +
                ", _combinList=" + _combinList +
                '}';
    }
}
