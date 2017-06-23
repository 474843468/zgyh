package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.customModel;

import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.psnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.wfssQueryMultipleQuotation.WFSSQueryMultipleQuotationResModel;

/**
 * 自定义 登录前 涨跌幅/值 和 买入卖出价 合并model
 * Created by yx on 2016/12/17.
 */

public class CustomLoginBeforeModel {
    /**
     * 涨跌幅/值 model
     */
    private WFSSQueryMultipleQuotationResModel.ItemsEntity itemsEntity;
    /**
     * 买入卖出价格 model
     */
    private PsnGetAllExchangeRatesOutlayResModel psnGetAllExchangeRatesOutlayResModel;

    public WFSSQueryMultipleQuotationResModel.ItemsEntity getItemsEntity() {
        return itemsEntity;
    }

    public PsnGetAllExchangeRatesOutlayResModel getPsnGetAllExchangeRatesOutlayResModel() {
        return psnGetAllExchangeRatesOutlayResModel;
    }

    public void setItemsEntity(WFSSQueryMultipleQuotationResModel.ItemsEntity itemsEntity) {
        this.itemsEntity = itemsEntity;
    }

    public void setPsnGetAllExchangeRatesOutlayResModel(PsnGetAllExchangeRatesOutlayResModel psnGetAllExchangeRatesOutlayResModel) {
        this.psnGetAllExchangeRatesOutlayResModel = psnGetAllExchangeRatesOutlayResModel;
    }
}
