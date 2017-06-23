package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmQuery.PsnSsmQueryResult;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtong on 2016/6/16.
 */
public class AccountNotifyModel {
    //签约账户
    private AccountBean account;
    //短信通知手机号
    private String smsPhone;
    //是否开通短信提醒
    private boolean status;
    //费率
    private String feeRate = "0";
    //缴费账户
    private String feeAccount;
    //用户名
    private String userName;
    //短信开通详情数据模型
    private List<PsnSsmQueryResult.MaplistBean> mapList;
    //优惠模式
    private String discountmodel;
    //是否有大额通
    private boolean isContainBigAmount = false;

    public boolean isContainBigAmount() {
        return isContainBigAmount;
    }

    public void setContainBigAmount(boolean containBigAmount) {
        isContainBigAmount = containBigAmount;
    }

    public boolean isStatus() {
        return status;
    }

    public String getDiscountmodel() {
        return discountmodel;
    }

    public void setDiscountmodel(String discountmodel) {
        this.discountmodel = discountmodel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFeeAccount() {
        return feeAccount;
    }

    public void setFeeAccount(String feeAccount) {
        this.feeAccount = feeAccount;
    }

    public List<PsnSsmQueryResult.MaplistBean> getMapList() {
        return mapList;
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public void setMapList(List<PsnSsmQueryResult.MaplistBean> list) {
        List<PsnSsmQueryResult.MaplistBean> map = new ArrayList<>();
        for (PsnSsmQueryResult.MaplistBean bean : list) {
            if (bean.getServiceid().equals("S001")) {
                map.add(bean);
            }
            if (bean.getServiceid().equals("S003")) {
                isContainBigAmount = true;
            }
        }
        this.mapList = map;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

    public String getSmsPhone() {
        return smsPhone;
    }

    public void setSmsPhone(String smsPhone) {
        this.smsPhone = smsPhone;
    }

}
