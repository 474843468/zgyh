package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCrcdChargeOnRMBAccountQuery;

/**
 * Created by WYme on 2016/12/8.
 */
public class PsnCrcdChargeOnRMBAccountQueryResult {
    private String description;
    private String alias;
    private boolean openFlag;
    private String displayNum;
    public String getDisplayNum() {
        return displayNum;
    }

    public void setDisplayNum(String displayNum) {
        this.displayNum = displayNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(boolean openFlag) {
        this.openFlag = openFlag;
    }


}
