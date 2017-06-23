package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadQueryGuarantyProductResultModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.List;
/**
 * * Fragment：中银理财-历史交易-组合购买交易详情
 * Created by zc on 2016/9/12
 *
 */
public class HistoryComPurchaseDetailContract {

    public interface View {
        /**
         * 成功回调：
         * 查询组合购买详情
         */
        void psnXpadQueryCombinationProductComSuccess(List<XpadQueryGuarantyProductResultModel.QueryGuarantyProductResultEntity> viewModel);

        /**
         * 失败回调：
         * 查询组合购买详情
         */
        void psnXpadQueryCombinationGuarantyProductFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 中银理财-组合购买详情
         */
        void psnXpadQueryCombinationGuarantyProductResult(XpadQueryGuarantyProductResultModel viewModel);

    }

}
