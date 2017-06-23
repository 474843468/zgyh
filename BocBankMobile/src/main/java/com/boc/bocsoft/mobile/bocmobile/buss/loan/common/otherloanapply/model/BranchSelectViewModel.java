package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model;

import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.LoadMoreListHelper;

/**
 * Created by XieDu on 2016/7/27.
 */
public class BranchSelectViewModel extends LoadMoreListHelper.ViewModel<OnLineLoanBranchBean> {
    private String cityCode;
    private String productCode;
    private String _refresh;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

}
