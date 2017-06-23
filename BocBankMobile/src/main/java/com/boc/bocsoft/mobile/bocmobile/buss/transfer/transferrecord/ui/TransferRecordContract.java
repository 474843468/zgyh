
package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.RemitReturnInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.TransferRecordDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.TransferRecordViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.TransferRecordViewModelNew;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by wangf on 2016/6/8.
 */
public class TransferRecordContract {

    public interface View {

        /**
         * 查询转账记录列表成功回调
         */
        void queryTransferRecordListSuccess(TransferRecordViewModel transferRecordViewModel);

        /**
         * 查询转账记录列表成功回调 - 新接口
         */
        void queryTransferRecordListSuccessNew(TransferRecordViewModelNew transferRecordViewModel);

        /**
         * 查询转账记录列表失败回调
         */
        void queryTransferRecordListFail(BiiResultErrorException biiResultErrorException);

        /**
         * 查询转账记录详细信息成功回调
         */
        void queryTransferRecordDetailInfoSuccess(TransferRecordDetailInfoViewModel infoViewModel);

        /**
         * 查询转账记录详细信息失败回调
         */
        void queryTransferRecordDetailInfoFail(BiiResultErrorException biiResultErrorException);

        /**
         * 查询退汇交易信息成功回调
         */
        void queryRemitReturnInfoSuccess(RemitReturnInfoViewModel infoViewModel);

        /**
         * 查询退汇交易信息失败回调
         */
        void queryRemitReturnInfoFail(BiiResultErrorException biiResultErrorException);
    }


    public interface Presenter extends BasePresenter {
        /**
         * 查询转账记录列表
         */
        void queryTransferRecordList(TransferRecordViewModel transferRecordViewModel);

        /**
         * 查询转账记录列表 - 新接口
         */
        void queryTransferRecordListNew(TransferRecordViewModelNew transferRecordViewModel);

        /**
         * 转账查询详细记录
         *
         * @param transactionId
         */
        void queryTransferRecordDetailInfo(String transactionId);

        /**
         * 退汇交易信息
         *
         * @param transactionId
         */
        void queryDetailAndRemitReturnInfo(String transactionId);
    }

}
