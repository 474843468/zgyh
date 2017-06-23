package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmQuery;

import org.threeten.bp.LocalDate;

import java.util.List;

/**
 * Created by wangtong on 2016/6/15.
 */
public class PsnSsmQueryResult {

    private int arrearageAmountAll;
    private String feeAccountNum;
    private double highloweramount;
    private String status;
    private String accAlias;

    private List<MaplistBean> maplist;

    public int getArrearageAmountAll() {
        return arrearageAmountAll;
    }

    public void setArrearageAmountAll(int arrearageAmountAll) {
        this.arrearageAmountAll = arrearageAmountAll;
    }

    public String getFeeAccountNum() {
        return feeAccountNum;
    }

    public void setFeeAccountNum(String feeAccountNum) {
        this.feeAccountNum = feeAccountNum;
    }

    public double getHighloweramount() {
        return highloweramount;
    }

    public void setHighloweramount(double highloweramount) {
        this.highloweramount = highloweramount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccAlias() {
        return accAlias;
    }

    public void setAccAlias(String accAlias) {
        this.accAlias = accAlias;
    }

    public List<MaplistBean> getMaplist() {
        return maplist;
    }

    public void setMaplist(List<MaplistBean> maplist) {
        this.maplist = maplist;
    }

    public static class MaplistBean {
        private String pushchannel;
        private String pushterm;
        private String serviceid;
        private double upperamount;
        private double loweramount;
        private String chargemethod;
        private String discountmodel;
        private String signdate;
        private String signchannel;
        private String signprvno;
        private String signinit;
        private Object changedate;
        private Object nextdate;
        private String servicestatus;
        private int oweamount;
        private LocalDate discountdate;
        private String feescale;
        private String feetype;
        private String feestandard;

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

        public String getServiceid() {
            return serviceid;
        }

        public void setServiceid(String serviceid) {
            this.serviceid = serviceid;
        }

        public double getUpperamount() {
            return upperamount;
        }

        public void setUpperamount(double upperamount) {
            this.upperamount = upperamount;
        }

        public double getLoweramount() {
            return loweramount;
        }

        public void setLoweramount(double loweramount) {
            this.loweramount = loweramount;
        }

        public String getChargemethod() {
            return chargemethod;
        }

        public void setChargemethod(String chargemethod) {
            this.chargemethod = chargemethod;
        }

        public String getDiscountmodel() {
            return discountmodel;
        }

        public void setDiscountmodel(String discountmodel) {
            this.discountmodel = discountmodel;
        }

        public String getSigndate() {
            return signdate;
        }

        public void setSigndate(String signdate) {
            this.signdate = signdate;
        }

        public String getSignchannel() {
            return signchannel;
        }

        public void setSignchannel(String signchannel) {
            this.signchannel = signchannel;
        }

        public String getSignprvno() {
            return signprvno;
        }

        public void setSignprvno(String signprvno) {
            this.signprvno = signprvno;
        }

        public String getSigninit() {
            return signinit;
        }

        public void setSigninit(String signinit) {
            this.signinit = signinit;
        }

        public Object getChangedate() {
            return changedate;
        }

        public void setChangedate(Object changedate) {
            this.changedate = changedate;
        }

        public Object getNextdate() {
            return nextdate;
        }

        public void setNextdate(Object nextdate) {
            this.nextdate = nextdate;
        }

        public String getServicestatus() {
            return servicestatus;
        }

        public void setServicestatus(String servicestatus) {
            this.servicestatus = servicestatus;
        }

        public int getOweamount() {
            return oweamount;
        }

        public void setOweamount(int oweamount) {
            this.oweamount = oweamount;
        }

        public LocalDate getDiscountdate() {
            return discountdate;
        }

        public void setDiscountdate(LocalDate discountdate) {
            this.discountdate = discountdate;
        }

        public String getFeescale() {
            return feescale;
        }

        public void setFeescale(String feescale) {
            this.feescale = feescale;
        }

        public String getFeetype() {
            return feetype;
        }

        public void setFeetype(String feetype) {
            this.feetype = feetype;
        }

        public String getFeestandard() {
            return feestandard;
        }

        public void setFeestandard(String feestandard) {
            this.feestandard = feestandard;
        }
    }
}
