package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryGeneralInfo.PsnCrcdQueryGeneralInfoResult;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model.CrcdInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model.CrcdModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 信用卡数据封装——响应
 * Created by liuweidong on 2016/12/10.
 */

public class CrcdResponseResult {

    public static void copyResultCrcdInfo(CrcdModel item, PsnCrcdQueryGeneralInfoResult result) {
        item.setAcctBank(result.getAcctBank());
        item.setAcctName(result.getAcctName());
        item.setAcctNum(result.getAcctNum());
        item.setAnnualFee(result.getAnnualFee());
        item.setBillDate(result.getBillDate());
        item.setCarAvaiDate(result.getCarAvaiDate());
        item.setCarFlag(result.getCarFlag());
        item.setCarStatus(result.getCarStatus());
        item.setDueDate(result.getDueDate());
        item.setProductName(result.getProductName());
        item.setStartDate(result.getStartDate());
        item.setWaiveMemFeeEndDate(result.getWaiveMemFeeEndDate());
        List<CrcdInfoBean> list = new ArrayList<>();
        for(PsnCrcdQueryGeneralInfoResult.ActListBean resultItem : result.getActList()){
            CrcdInfoBean crcdInfoBean = new CrcdInfoBean();
            crcdInfoBean.setBillAmout(resultItem.getBillAmout());
            crcdInfoBean.setBillLimitAmout(resultItem.getBillLimitAmout());
            crcdInfoBean.setCashBalance(resultItem.getCashBalance());
            crcdInfoBean.setCashLimit(resultItem.getCashLimit());
            crcdInfoBean.setCurrency(resultItem.getCurrency());
            crcdInfoBean.setDividedPayAvaiBalance(resultItem.getDividedPayAvaiBalance());
            crcdInfoBean.setDividedPayLimit(resultItem.getDividedPayLimit());
            crcdInfoBean.setHaveNotRepayAmout(resultItem.getHaveNotRepayAmout());
            crcdInfoBean.setRealTimeBalance(resultItem.getRealTimeBalance());
            crcdInfoBean.setRtBalanceFlag(resultItem.getRtBalanceFlag());
            crcdInfoBean.setToltalBalance(resultItem.getToltalBalance());
            crcdInfoBean.setTotalLimt(resultItem.getTotalLimt());
            list.add(crcdInfoBean);
        }
        item.setActList(list);
    }
}
