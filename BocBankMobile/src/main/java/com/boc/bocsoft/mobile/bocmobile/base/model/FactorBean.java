package com.boc.bocsoft.mobile.bocmobile.base.model;

import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ConvertIfDiff;
import java.io.Serializable;

/**
 * 预交易接口返回的数据
 * Created by liuweidong on 2016/7/30.
 */
public class FactorBean implements Serializable {
    /**
     * name : Smc
     * type : password
     */
    @ConvertIfDiff
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
