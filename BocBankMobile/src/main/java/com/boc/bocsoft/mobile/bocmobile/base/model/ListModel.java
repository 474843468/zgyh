package com.boc.bocsoft.mobile.bocmobile.base.model;

import java.io.Serializable;

/**
 * Created by liuweidong on 2016/8/13.
 */
public class ListModel implements Serializable {
    private String nameID;
    private String name;
    private String value;

    public String getNameID() {
        return nameID;
    }

    public void setNameID(String nameID) {
        this.nameID = nameID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
