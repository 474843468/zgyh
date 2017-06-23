package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryMasterAndSupplInfo;

import java.util.List;

/**
 * Name: liukai
 * Time：2016/12/2 11:13.
 * Created by lk7066 on 2016/12/2.
 * It's used to
 */

public class PsnCrcdQueryMasterAndSupplInfoResult {

    /**
     * 产品ID
     * */
    private String productId;

    /**
     * 主卡类型
     * */
    private String masterCrcdType;

    /**
     * 主卡卡号
     * */
    private String masterCrcdNum;

    /**
     * 主卡账户标识
     * */
    private String masterCrcdId;

    /**
     * 主卡产品名
     * */
    private String masterCrcdProductName;

    /**
     * 主卡分行号
     * */
    private String masterCrcdIbkNum;

    private List<ListBean> list;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMasterCrcdType() {
        return masterCrcdType;
    }

    public void setMasterCrcdType(String masterCrcdType) {
        this.masterCrcdType = masterCrcdType;
    }

    public String getMasterCrcdNum() {
        return masterCrcdNum;
    }

    public void setMasterCrcdNum(String masterCrcdNum) {
        this.masterCrcdNum = masterCrcdNum;
    }

    public String getMasterCrcdId() {
        return masterCrcdId;
    }

    public void setMasterCrcdId(String masterCrcdId) {
        this.masterCrcdId = masterCrcdId;
    }

    public String getMasterCrcdIbkNum() {
        return masterCrcdIbkNum;
    }

    public void setMasterCrcdIbkNum(String masterCrcdIbkNum) {
        this.masterCrcdIbkNum = masterCrcdIbkNum;
    }

    public String getMasterCrcdProductName() {
        return masterCrcdProductName;
    }

    public void setMasterCrcdProductName(String masterCrcdProductName) {
        this.masterCrcdProductName = masterCrcdProductName;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {

        private String subCrcdNum;
        private String subCrcdHolder;

        public String getSubCrcdNum() {
            return subCrcdNum;
        }

        public void setSubCrcdNum(String subCrcdNum) {
            this.subCrcdNum = subCrcdNum;
        }

        public String getSubCrcdHolder() {
            return subCrcdHolder;
        }

        public void setSubCrcdHolder(String subCrcdHolder) {
            this.subCrcdHolder = subCrcdHolder;
        }
    }
}
