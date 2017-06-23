package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.ui;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActDeletePayer.PsnTransActDeletePayerResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActModifyPayerMobile.PsnTransActModifyPayerMobileResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.model.PayerListModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by liuyang on 2016/7/21.
 */
public class PayerListContract {
    public interface QueryList extends BaseView<Presenter> {
       //查询付款人列表成功回调
        void queryPayerListSuccess(PayerListModel queryPayerListModel);
        //查询付款人列表失败回调
        void queryPayerListFail(BiiResultErrorException biiResultErrorException);
    }


    public interface DetailedOperate extends BaseView<Presenter> {

        //修改付款人成功回调
        void revisePayerSuccess(PsnTransActModifyPayerMobileResult psnTransActModifyPayerMobileResult);
        //修改付款人失败回调
        void revisePayerFail(BiiResultErrorException biiResultErrorException);
        //删除付款人成功回调
        void deletePayerSuccess(PsnTransActDeletePayerResult psnMobileWithdrawalQueryResult);
        //删除付款人失败回调
        void deletePayerFail(BiiResultErrorException biiResultErrorException);


    }

    public interface Presenter extends BasePresenter {
       //查询付款人列表
        void queryPayerList();
        //修改手机号
        void revisePayerList(PayerListModel deletePayerViewModel);
        //删除手机号
        void deletePayerList(PayerListModel deletePayerViewModel);


    }
}
