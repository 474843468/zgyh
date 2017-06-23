package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import android.content.Context;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnBatchTransActCollectionSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnBatchTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionSubmitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.SavePayerViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contract:主动收款确认（预交易和正式交易，单人和多人）
 * Created by zhx on 2016/8/11
 */
public class PayConfirmContact {
    public interface View extends BaseView<Presenter> {

        /**
         * 成功回调：获取随机数
         */
        void getRandomSuccess(String random);

        /**
         * 失败回调：获取随机数
         */
        void getRandomFailed(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：主动收款预交易（单人）
         */
        void collectionVerifySuccess(PsnTransActCollectionVerifyViewModel viewModel);

        /**
         * 失败回调：主动收款预交易（单人）
         */
        void collectionVerifyFailed(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：主动收款提交（单人）
         */
        void collectionSubmitSuccess(PsnTransActCollectionSubmitViewModel viewModel);

        /**
         * 失败回调：主动收款提交（单人）
         */
        void collectionSubmitFailed(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：主动收款预交易（多人）
         */
        void psnBatchTransActCollectionVerifySuccess(PsnBatchTransActCollectionVerifyViewModel viewModel);

        /**
         * 失败回调：主动收款预交易（多人）
         */
        void psnBatchTransActCollectionVerifyFailed(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：主动收款提交（单人）
         */
        void psnBatchTransActCollectionSubmitSuccess(PsnBatchTransActCollectionSubmitViewModel viewModel);

        /**
         * 失败回调：主动收款提交（单人）
         */
        void psnBatchTransActCollectionSubmitFailed(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：
         * 主动收款保存常用付款人
         */
        void savePayerSuccess(SavePayerViewModel savePayerViewModel);

        /**
         * 失败回调：
         * 主动收款保存常用付款人
         */
        void savePayerFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 获取随机数
         */
        void getRandom();

        /**
         * 主动收款预交易（单人）
         */
        void collectionVerify(PsnTransActCollectionVerifyViewModel viewModel);

        /**
         * 主动收款提交（单人）
         */
        void collectionSubmit(PsnTransActCollectionSubmitViewModel viewModel, final String[] randomNums,
                              final String[] encryptPasswords, final int curCombinID, final Context context);

        /**
         * 主动收款预交易（多人）
         */
        void psnBatchTransActCollectionVerify(PsnBatchTransActCollectionVerifyViewModel viewModel);

        /**
         * 主动收款提交（多人）
         */
        void psnBatchTransActCollectionSubmit(PsnBatchTransActCollectionSubmitViewModel viewModel, final String[] randomNums,
                                              final String[] encryptPasswords, final int curCombinID, final Context context);

        /**
         * 主动收款保存常用付款人
         */
        void savePayer(SavePayerViewModel savePayerViewModel);
    }
}
