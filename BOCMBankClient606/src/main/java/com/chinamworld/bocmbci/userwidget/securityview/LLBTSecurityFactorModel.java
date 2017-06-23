package com.chinamworld.bocmbci.userwidget.securityview;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.chinamworld.bocmbci.base.application.CommonApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 联龙安全工具数据Model
 * Created by Administrator on 2016/9/20.
 */
public class LLBTSecurityFactorModel extends SecurityFactorModel {


    /**
     * 客户默认的安全因子组合  name:安全因子名称  id: 安全因子id
     * 0:虚拟 4:USBKey证书 8:动态口令令牌 32:短信认证码 40:动态口令令牌+短信认证码 96:短信认证码+硬件绑定
     */
    private List<CombinListBean> mCombinList;


    public LLBTSecurityFactorModel(ArrayList<String> securityIdList,ArrayList<String> securityNameList,String defaultId){
        mCombinList = new ArrayList<CombinListBean>();
        this.setCombinList(mCombinList);
        for(int i = 0; i <  securityIdList.size(); ++i) {
            CombinListBean comBean = new CombinListBean();
            comBean.setId(securityIdList.get(i));
            comBean.setName(securityNameList.get(i));
            mCombinList.add(comBean);
            if(defaultId.equals(securityIdList.get(i))){
                setDefaultCombin(comBean);
            }
        }

    }
}
