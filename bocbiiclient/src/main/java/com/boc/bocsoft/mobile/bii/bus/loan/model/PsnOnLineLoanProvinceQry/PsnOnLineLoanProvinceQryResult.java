package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanProvinceQry;

import java.util.List;

/**
 * Created by XieDu on 2016/7/19.
 */
public class PsnOnLineLoanProvinceQryResult {

    private List<PsnOnLineLoanProvinceBean> list;

    public List<PsnOnLineLoanProvinceBean> getList() { return list;}

    public void setList(List<PsnOnLineLoanProvinceBean> list) { this.list = list;}

    public static class PsnOnLineLoanProvinceBean {
        private String provinceCode;
        private String provinceName;

        public String getProvinceCode() { return provinceCode;}

        public void setProvinceCode(String provinceCode) { this.provinceCode = provinceCode;}

        public String getProvinceName() { return provinceName;}

        public void setProvinceName(String provinceName) { this.provinceName = provinceName;}
    }
}
