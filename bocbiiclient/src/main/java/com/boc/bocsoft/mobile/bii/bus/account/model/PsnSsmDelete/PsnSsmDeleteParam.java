package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmDelete;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtong on 2016/8/16.
 */
public class PsnSsmDeleteParam {

    private String conversationId;
    private String accountId;
    private String accountNumber;
    private String token;

    private List<MaplistBean> Maplist;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<MaplistBean> getMaplist() {
        return Maplist;
    }

    public void setMaplist(MaplistBean bean) {
        this.Maplist = new ArrayList<>();
        this.Maplist.add(bean);
    }

    public static class MaplistBean {
        private String ssmserviceid;
        private String pushchannel;
        private String pushterm;
        private String loweramount;
        private String upperamount;
        private String discountmodel;
        private String chargemethod;

        public String getSsmserviceid() {
            return ssmserviceid;
        }

        public void setSsmserviceid(String ssmserviceid) {
            this.ssmserviceid = ssmserviceid;
        }

        public String getPushchannel() {
            return pushchannel;
        }

        public void setPushchannel(String pushchannel) {
            this.pushchannel = pushchannel;
        }

        public String getPushterm() {
            return pushterm;
        }

        public void setPushterm(String pushterm) {
            this.pushterm = pushterm;
        }

        public String getLoweramount() {
            return loweramount;
        }

        public void setLoweramount(String loweramount) {
            this.loweramount = loweramount;
        }

        public String getUpperamount() {
            return upperamount;
        }

        public void setUpperamount(String upperamount) {
            this.upperamount = upperamount;
        }

        public String getDiscountmodel() {
            return discountmodel;
        }

        public void setDiscountmodel(String discountmodel) {
            this.discountmodel = discountmodel;
        }

        public String getChargemethod() {
            return chargemethod;
        }

        public void setChargemethod(String chargemethod) {
            this.chargemethod = chargemethod;
        }
    }
}
