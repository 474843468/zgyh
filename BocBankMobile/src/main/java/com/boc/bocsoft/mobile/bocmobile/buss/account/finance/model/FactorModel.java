package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model;

/**
 * @author wangyang
 *         16/6/29 09:54
 */
public class FactorModel {


    private FieldBean field;

    public FieldBean getField() {
        return field;
    }

    public void setField(FieldBean field) {
        this.field = field;
    }

    public static class FieldBean {
        /**
         * 值
         */
        private String name;
        /**
         * 类型
         */
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

        public FieldBean(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }
}
