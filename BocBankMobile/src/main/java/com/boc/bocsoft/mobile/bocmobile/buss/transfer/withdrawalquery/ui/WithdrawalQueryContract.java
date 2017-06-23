
package com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.model.WithdrawalQueryDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.model.WithdrawalQueryViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by wangf on 2016/6/8.
 */
public class WithdrawalQueryContract {

    public interface View {

        /**
         * 取款查询列表成功回调
         */
        void queryWithdrawalQueryListSuccess(WithdrawalQueryViewModel withdrawalQueryViewModel);

        /**
         * 取款查询列表失败回调
         */
        void queryWithdrawalQueryListFail(BiiResultErrorException biiResultErrorException);

        /**
         * 取款查询详情查询成功
         * @param queryResult
         */
//        void queryWithdrawalDetailInfoSuccess(WithdrawalQueryDetailInfoViewModel detailInfoViewModel);
        void queryWithdrawalDetailInfoSuccess(String queryResult);

        /**
         * 取款查询 详情查询失败
         * @param biiResultErrorException
         */
        void queryWithdrawalDetailInfoFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 取款查询列表
         */
        void queryWithdrawalQueryList(WithdrawalQueryViewModel withdrawalQueryViewModel);

        /**
         * 取款查询 详情页面请求
         * @param detailInfoViewModel
         */
        void queryWithdrawalDetailInfo(WithdrawalQueryDetailInfoViewModel detailInfoViewModel);
    }

}
