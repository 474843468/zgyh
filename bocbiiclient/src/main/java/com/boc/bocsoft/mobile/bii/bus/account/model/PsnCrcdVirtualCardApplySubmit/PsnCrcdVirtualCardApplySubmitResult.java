package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplySubmit;

import com.boc.bocsoft.mobile.bii.bus.account.model.QuestionResult;

/**
 * @author wangyang
 *         16/7/25 14:53
 *         申请虚拟银行卡提交
 */
public class PsnCrcdVirtualCardApplySubmitResult extends QuestionResult{

    /** 虚拟卡标识 */
    private String virCardAccountId;
    /** 虚拟卡信息 */
    private VirCard vCardInfo;

    public String getVirCardAccountId() {
        return virCardAccountId;
    }

    public void setVirCardAccountId(String virCardAccountId) {
        this.virCardAccountId = virCardAccountId;
    }

    public VirCard getvCardInfo() {
        return vCardInfo;
    }

    public void setvCardInfo(VirCard vCardInfo) {
        this.vCardInfo = vCardInfo;
    }
}
