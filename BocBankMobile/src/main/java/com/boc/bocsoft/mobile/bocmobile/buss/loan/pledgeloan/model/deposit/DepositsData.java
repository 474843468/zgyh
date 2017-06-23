package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit;

import java.util.List;

/**
 * 作者：XieDu
 * 创建时间：2016/9/12 15:34
 * 描述：
 */
public class DepositsData {
    private List<PledgeAvaAndPersonalTimeAccount> pledgeAvaAndPersonalTimeAccountList;

    public DepositsData(List<PledgeAvaAndPersonalTimeAccount> pledgeAvaAndPersonalTimeAccountList) {
        this.pledgeAvaAndPersonalTimeAccountList = pledgeAvaAndPersonalTimeAccountList;
    }

    public List<PledgeAvaAndPersonalTimeAccount> getPledgeAvaAndPersonalTimeAccountList() {
        return pledgeAvaAndPersonalTimeAccountList;
    }

    public void setPledgeAvaAndPersonalTimeAccountList(
            List<PledgeAvaAndPersonalTimeAccount> pledgeAvaAndPersonalTimeAccountList) {
        this.pledgeAvaAndPersonalTimeAccountList = pledgeAvaAndPersonalTimeAccountList;
    }
}
