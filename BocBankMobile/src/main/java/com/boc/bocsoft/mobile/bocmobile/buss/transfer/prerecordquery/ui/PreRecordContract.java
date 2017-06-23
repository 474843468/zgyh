package com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.model.PreRecordDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.model.PreRecordViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * Created by wangf on 2016/7/22.
 */
public class PreRecordContract {

    public interface View{

        /**
         * 预约管理查询 列表 成功回调
         *
         * @param preRecordViewModel
         */
        void queryPreRecordListSuccess(PreRecordViewModel preRecordViewModel);

        /**
         * 预约管理查询 列表 失败回调
         *
         * @param biiResultErrorException
         */
        void queryPreRecordListFail(BiiResultErrorException biiResultErrorException);

        /**
         * 预约管理查询 详情 成功回调
         *
         * @param infoViewModel
         */
        void queryPreRecordInfoSuccess(PreRecordDetailInfoViewModel infoViewModel);

        /**
         * 预约管理查询 详情 失败回调
         *
         * @param biiResultErrorException
         */
        void queryPreRecordInfoFail(BiiResultErrorException biiResultErrorException);

    }


    public interface DetailInfoView {
        /**
         * 预约管理 删除交易 成功回调
         */
        void loadDeletePreRecordSuccess();

        /**
         * 预约管理 删除交易 失败回调
         *
         * @param biiResultErrorException
         */
        void loadDeletePreRecordFail(BiiResultErrorException biiResultErrorException);
    }


    public interface Presenter extends BasePresenter {
        /**
         * 预约管理列表查询
         */
        void queryPreRecordList(PreRecordViewModel preRecordViewModel);

        /**
         * 预约管理详情查询
         * @param dateType      日期查询类型 0：按执行日期查 1：按预约日期查询
         * @param transactionId 网银交易序号
         * @param batSeq        转账批次号
         */
        void queryPreRecordInfo(String dateType, String transactionId, String batSeq);

        /**
         * 预约交易删除
         * @param batSeq 转账批次号
         * @param dateType 日期查询类型
         * @param transactionId 网银交易序号
         */
        void loadDeletePreRecord(String batSeq, String dateType, String transactionId);

    }
}
