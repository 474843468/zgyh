package com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre;

import java.io.Serializable;

/**
 * Created by feib on 2016/6/27.
 */
public class FactorListBean implements Serializable {
    /**
     * name : Smc
     * type : password
     */
    private FieldBean field;

    public FieldBean getField() {
        return field;
    }

    public void setField(FieldBean field) {
        this.field = field;
    }

    public static class FieldBean implements Serializable {
        private String name;
        private String type;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
