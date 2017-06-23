package com.boc.bocsoft.mobile.bii.bus.setting.model.NamePasswordModMobile;

/**
 * Created by feib on 16/7/14.
 */
public class NamePasswordModMobileParams {

    /**
     * activ : 305100001
     * cardNoSixLast : 382272
     * combinId : 8
     * conversationId : 6a250d0a-4519-4d25-94ff-72eb6365bb44
     * id : 876765830
     * loginName :
     * newPass : tUZ0GR1E8gjrZ9mXpmwuzZLojp99liayxS2/lGLRRx0=
     * newPass2 : xYD09m/z1AJEK4RoCH9m19Bm9pDn35c7+xv67nw3SdI=
     * newPass2_RC : OMIccF6v3cxGAqtqdQUoS51YdZJnuhE0TgUOz8RQkuHs5Cu+I7rfs/gVcQ5TVlclpkyr/H7QdW4r2U3OKyBdBhpeE1v3VgQrUWSqNz/m6noYKwLNULvv2WsLcu9X++09bZolT5zlOCDzwnXNOW/l4Q==
     * newPass_RC : /Fh//xKuDSsFpzLEcW/nPkKPY41U55hL55V3CZU3iEyg2Fj/jZesPwSvYuoUTvKxjyeFS5A1hP9k3pWuopCUbPOPvKY/7hKG1vCH3eqHhTe8G1E0ROj02YbMdlQ54gg+nluxA3V3nUM/qnAc2u7Tdw==
     * oldPass : fLxLgUdTuPhb4YSIcDNKOF8JNxR76pFlp7cATnkhBBM=
     * oldPass_RC : lkeKDlWs48xsG0cvmrFDW+lspUdUTBjHjxNH0WxsrTUFjODF//91yzz9hGfiZ5BAA7hm9RimG/pTXNvhkydf5sG/MmabxZYfzvherThlEI1k2Z7dlbmutw5NfL78S4OpZ0AkOfLkpq3h2HmJy/cMYg==
     * state : 41943040
     */

    //卡号后六位
    private String cardNoSixLast;
    //安全组合编号
    private String combinId;
    //会话ID
    private String conversationId;
    //登录名
    private String loginName;
    //新密码
    private String newPass;
    //确认密码
    private String newPass2;
    private String newPass2_RC;
    private String newPass_RC;
    //原密码
    private String oldPass;
    private String oldPass_RC;
    private String state;
    private String activ;
    public String getActiv() {
        return activ;
    }

    public void setActiv(String activ) {
        this.activ = activ;
    }

    public String getCardNoSixLast() {
        return cardNoSixLast;
    }

    public void setCardNoSixLast(String cardNoSixLast) {
        this.cardNoSixLast = cardNoSixLast;
    }

    public String getCombinId() {
        return combinId;
    }

    public void setCombinId(String combinId) {
        this.combinId = combinId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getNewPass2() {
        return newPass2;
    }

    public void setNewPass2(String newPass2) {
        this.newPass2 = newPass2;
    }

    public String getNewPass2_RC() {
        return newPass2_RC;
    }

    public void setNewPass2_RC(String newPass2_RC) {
        this.newPass2_RC = newPass2_RC;
    }

    public String getNewPass_RC() {
        return newPass_RC;
    }

    public void setNewPass_RC(String newPass_RC) {
        this.newPass_RC = newPass_RC;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getOldPass_RC() {
        return oldPass_RC;
    }

    public void setOldPass_RC(String oldPass_RC) {
        this.oldPass_RC = oldPass_RC;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
