package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.module;

/**
 * Created by zcy7065 on 2016/11/1.
 */
public class LoanTypeItemModle {
    private int loanTypeImage;
    private String loanTypeName;
    private String loanTypeDescrip;
    private String lableVisable;

    public LoanTypeItemModle(int loantypeimage, String loantypename,
                             String loantypedescrip, String lableVisable){
        this.loanTypeImage = loantypeimage;
        this.loanTypeName = loantypename;
        this.loanTypeDescrip = loantypedescrip;
        this.lableVisable = lableVisable;
    }


    public int getLoanTypeImage() {
        return loanTypeImage;
    }

    public String getLoanTypeName() {
        return loanTypeName;
    }

    public String getLoanTypeDescrip() {
        return loanTypeDescrip;
    }

    public String isLableVisable() {
        return lableVisable;
    }

}
