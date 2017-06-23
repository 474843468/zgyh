
package com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.AccountDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.FinanceICTransferViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.InquiryRangeQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.MedicalTransferDetailQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.TransDetailViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by wangf on 2016/6/8.
 */
public class TransDetailContract {

    public interface View {


        /**
         * 查询最大跨度和最长时间范围 成功回调
         */
        void queryInquiryRangeSuccess(InquiryRangeQueryViewModel rangeQueryViewModel);

        /**
         * 查询最大跨度和最长时间范围 失败回调
         */
        void queryInquiryRangeFail(BiiResultErrorException biiResultErrorException);


        /**
         * 查询账户详情成功回调
         */
        void queryAccountQueryAccountDetailSuccess(AccountDetailViewModel accountDetailViewModel);
        /**
         * 查询账户详情失败回调
         */
        void queryAccountQueryAccountDetailFail(BiiResultErrorException biiResultErrorException);


        /**
         * 查询交易明细列表成功回调 -- 活期
         */
        void queryTransDetailListSuccess(TransDetailViewModel transDetailViewModel);

        /**
         * 查询交易明细列表失败回调 -- 活期
         */
        void queryTransDetailListFail(BiiResultErrorException biiResultErrorException);

        /**
         * 查询交易明细列表成功回调 -- 医保账户
         */
        void queryMedicalInsurAcctTransferlListSuccess(MedicalTransferDetailQueryViewModel transDetailViewModel);

        /**
         * 查询交易明细列表失败回调 -- 医保账户
         */
        void queryMedicalInsurAcctTransferListFail(BiiResultErrorException biiResultErrorException);

        /**
         * 查询交易明细列表成功回调 -- 电子现金账户
         */
        void queryFinanceICTransferListSuccess(FinanceICTransferViewModel transDetailViewModel);

        /**
         * 查询交易明细列表失败回调 -- 电子现金账户
         */
        void queryFinanceICTransferListFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        /**
         * 查询最大跨度和最长时间范围
         */
        void queryInquiryRange();


        /**
         * 查询账户详情
         */
        void queryAccountQueryAccountDetail(String accountId);


        /**
         * 查询交易明细列表 -- 活期
         */
        void queryTransDetailList(TransDetailViewModel transDetailViewModel);

        /**
         * 查询交易明细列表 -- 医保账户
         */
        void queryMedicalTransferList(MedicalTransferDetailQueryViewModel medicalTransferViewModel);

        /**
         * 查询交易明细列表 -- 电子现金账户
         */
        void queryFinanceICTransferList(FinanceICTransferViewModel financeICTransferViewModel);
    }

}
