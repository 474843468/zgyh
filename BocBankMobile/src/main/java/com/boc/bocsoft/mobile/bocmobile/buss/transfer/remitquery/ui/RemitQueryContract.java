
package com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.model.RemitQueryDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.model.RemitQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.model.ResetSendSmsViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by wangf on 2016/6/8.
 */
public class RemitQueryContract {

    public interface View {

        /**
         * 汇出查询 列表 成功回调
         *
         * @param remitQueryViewModel
         */
        void queryRemitQueryListSuccess(RemitQueryViewModel remitQueryViewModel);

        /**
         * 汇出查询 列表 失败回调
         *
         * @param biiResultErrorException
         */
        void queryRemitQueryListFail(BiiResultErrorException biiResultErrorException);

        /**
         * 汇出查询 详情 成功回调
         *
         * @param infoViewModel
         */
        void queryRemitDetailInfoSuccess(RemitQueryDetailInfoViewModel infoViewModel);

        /**
         * 汇出查询 详情 失败回调
         *
         * @param biiResultErrorException
         */
        void queryRemitDetailInfoFail(BiiResultErrorException biiResultErrorException);

    }

    public interface DetailInfoView {
        /**
         * 汇出查询 重新发送短信 成功回调
         */
        void loadResetSendSmsSuccess();

        /**
         * 汇出查询 重新发送短信 失败回调
         *
         * @param biiResultErrorException
         */
        void loadResetSendSmsFail(BiiResultErrorException biiResultErrorException);

        /**
         * 汇出查询 撤销交易 成功回调
         */
        void loadCancelTransSuccess();

        /**
         * 汇出查询 撤销交易 失败回调
         *
         * @param biiResultErrorException
         */
        void loadCancelTransFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 汇出查询列表
         */
        void queryRemitQueryList(RemitQueryViewModel remitQueryViewModel);

        /**
         * 汇出详情查询
         *
         * @param infoViewModel
         */
        void queryRemitDetailInfo(RemitQueryDetailInfoViewModel infoViewModel);

        /**
         * 撤销交易
         *
         * @param remitNo
         */
        void loadCancelTrans(String remitNo);

        /**
         * 重新发送短信
         *
         * @param resetSendSmsViewModel
         */
        void loadResetSendSms(ResetSendSmsViewModel resetSendSmsViewModel);
    }

}
