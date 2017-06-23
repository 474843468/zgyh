package com.boc.bocsoft.mobile.bii.bus.account.model;

public class FactorListBean {

        private FieldBean field;

        public FieldBean getField() {
            return field;
        }

        public void setField(FieldBean field) {
            this.field = field;
        }

        public static class FieldBean {
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