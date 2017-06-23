package com.chinamworld.llbt.model;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/27.
 */
public class AccountItem {

    private String accountName;
    private String accountNum;
    private String accountType;

    private Object source;

    public AccountItem(Map<String,Object> accountMap){
        source = accountMap;
        accountType = (String)accountMap.get("accountType");
        accountNum = (String)accountMap.get("accountNumber");
        accountName = (String)accountMap.get("accountName");
    }

    public AccountItem(){

    }


    public Object getSource(){
        return source;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

}
