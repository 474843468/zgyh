package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSVRAccQuery;


import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;

import java.util.List;

/**
 * 账户管家-账户查询返回报文
 * Created by niuguobin on 2016/6/7.
 */
public class PsnSVRAccQueryResult {

    /**
     * 关联账户只有一个关联卡时不出现“取消关联”的链接,标识为"true"
     */
    private Object showFlg;
    /**
     *个人定期账户列表
     */
    private Object termList;
    /**
     * 个人信用卡账户列表
     */
    private List<PsnCommonQueryAllChinaBankAccountResult> accountList;


    public Object getShowFlg() {
        return showFlg;
    }

    public void setShowFlg(Object showFlg) {
        this.showFlg = showFlg;
    }

    public Object getTermList() {
        return termList;
    }

    public void setTermList(Object termList) {
        this.termList = termList;
    }

    public List<PsnCommonQueryAllChinaBankAccountResult> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<PsnCommonQueryAllChinaBankAccountResult> accountList) {
        this.accountList = accountList;
    }
}
