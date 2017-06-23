package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.recommend.ui;


import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by lzc4524 on 2016/12/26.
 */
public class FundRecommendContract {
    public interface View extends BaseView<Presenter> {
        //查询基金推荐成功
        void queryFundsRecommendSuccess(PsnOcrmProductQueryResult result);
        //查询基金推荐失败
        void queryFundsRecommendFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 查询基金推荐
         * @param params
         */
        void queryFundsRecommend(PsnOcrmProductQueryParams params);
    }
}
