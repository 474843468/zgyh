package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.model;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;

import java.io.Serializable;
import java.util.List;

/**
 * 开通理财服务ui model
 * Created by wangtong on 2016/10/
 * Modified by liuweidong on 2016/12/2.
 */
public class OpenWealthModel implements Serializable {
    /*安全因子接口——响应*/
    private SecurityFactorModel factorModel;// 安全因子
    /*随机数接口——响应*/
    private String randomNum;// 随机数
    public String[] encryptRandomNums;// 安全加密随机数
    public String[] encryptPasswords;// 安全加密密文
    public List<FactorListBean> preFactorList;// 预交易返回安全因子
    public String selectedFactorId;// 最终选择的安全因子
    public String mPlainData;// 音频key上送参数
    public String mSignData;// 音频key返回参数

    public SecurityFactorModel getFactorModel() {
        return factorModel;
    }

    public void setFactorModel(SecurityFactorModel factorModel) {
        this.factorModel = factorModel;
    }

    public String getRandomNum() {
        return randomNum;
    }

    public void setRandomNum(String randomNum) {
        this.randomNum = randomNum;
    }
}
