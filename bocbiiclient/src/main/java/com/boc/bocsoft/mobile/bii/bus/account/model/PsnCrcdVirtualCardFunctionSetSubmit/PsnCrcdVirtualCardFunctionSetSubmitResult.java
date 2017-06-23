package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetSubmit;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardQuery.VirCardInfo;
import com.boc.bocsoft.mobile.bii.bus.account.model.QuestionResult;

/**
 * @author wangyang
 *         16/7/26 15:34
 *         虚拟银行卡交易限额修改提交
 */
public class PsnCrcdVirtualCardFunctionSetSubmitResult extends QuestionResult{

    /** 账户户名 */
    private String virCardAccountName;

    private VirCardInfo virCard;

    public String getVirCardAccountName() {
        return virCardAccountName;
    }

    public void setVirCardAccountName(String virCardAccountName) {
        this.virCardAccountName = virCardAccountName;
    }

    public VirCardInfo getVirCard() {
        return virCard;
    }

    public void setVirCard(VirCardInfo virCard) {
        this.virCard = virCard;
    }
}
