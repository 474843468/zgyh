package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtong on 2016/6/23.
 */
public class SecurityFactorModel implements Serializable{
    /**
     * 客户默认的安全因子组合  name:安全因子名称  id: 安全因子id
     * 0:虚拟 4:USBKey证书 8:动态口令令牌 32:短信认证码 40:动态口令令牌+短信认证码 96:短信认证码+硬件绑定
     */
    private CombinListBean defaultCombin;
    /**
     * 客户默认的安全因子组合  name:安全因子名称  id: 安全因子id
     * 0:虚拟 4:USBKey证书 8:动态口令令牌 32:短信认证码 40:动态口令令牌+短信认证码 96:短信认证码+硬件绑定
     */
    private List<CombinListBean> combinList;

    public SecurityFactorModel() {
    }

    public SecurityFactorModel(PsnGetSecurityFactorResult result) {
        defaultCombin = result.get_defaultCombin();
        combinList = new ArrayList<CombinListBean>(result.get_combinList());
    }

    public List<CombinListBean> getCombinList() {
        return combinList;
    }

    public void setCombinList(List<CombinListBean> combinList) {
        this.combinList = combinList;
    }

    public CombinListBean getDefaultCombin() {
        return defaultCombin;
    }

    public void setDefaultCombin(CombinListBean defaultCombin) {
        this.defaultCombin = defaultCombin;
    }
}
