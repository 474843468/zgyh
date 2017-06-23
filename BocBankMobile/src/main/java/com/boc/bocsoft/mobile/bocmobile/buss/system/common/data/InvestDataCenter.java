package com.boc.bocsoft.mobile.bocmobile.buss.system.common.data;

import com.boc.bocsoft.mobile.cr.bus.product.model.CRgetProductList.CRgetProductListResult;

import java.util.List;

/**
 * Created by dingeryue on 2016年12月06.
 * 优选投资数据，推荐的产品
 */

public class InvestDataCenter {
    private static InvestDataCenter dataCenter;

    private InvestDataCenter() {
    }

    public static InvestDataCenter getInstance() {
        if (dataCenter != null) return dataCenter;
        synchronized (InvestDataCenter.class) {
            if (dataCenter != null) return dataCenter;
            return new InvestDataCenter();
        }
    }

    private List<CRgetProductListResult.ProductBean> productBeen;

    /**
     * 刷新推荐产品
     *
     * @param productBeen
     */
    public void updateInvestProducts(List<CRgetProductListResult.ProductBean> productBeen) {
        this.productBeen = productBeen;
    }

    public List<CRgetProductListResult.ProductBean> getInvestProducts() {
        return productBeen;
    }
}
