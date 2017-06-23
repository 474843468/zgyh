package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.ui;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundDetail.PsnGetFundDetailResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.model.FundConversionInputModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by taoyongzhen on 2016/12/13.
 */

public class FundConversionContract {

    public interface FundConversionInputQueryView extends BaseView<Presenter> {
        /**
         * 基金转换输入失败
         * @param e
         */
        void fundConversionInputQueryFail(BiiResultErrorException e);

        /**
         * 基金转换输入成功
         * @param model
         */
        void fundConversionInputQuerySuccess(FundConversionInputModel model);

        /**
         * 查询基金详情失败
         * @param e
         */
        void queryFundInfoFail(BiiResultErrorException e);

        /**
         * 查询基金详情成功
         * @param result
         */
        void queryFundInfoSuccess(PsnGetFundDetailResult result);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 基金转换输入
         * @param parmas
         */
        void fundConversionInputQuery(FundConversionInputModel parmas);

        /**
         * 查询基金详情
         * @param params
         */
        void queryFundInfo(PsnGetFundDetailParams params);

    }
}
