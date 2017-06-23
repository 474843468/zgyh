package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.model.QRPayGetPayeeInfoModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 查询收款人信息
 * Created by wangf on 2016/10/16.
 */
public class QRPayGetPayeeInfoContract {

    public interface QRPayGetPayeeInfoView{
        /*** 查询收款人信息成功 */
        void loadGetPayeeInfoSuccess(QRPayGetPayeeInfoModel viewModel);

        /*** 查询收款人信息失败 */
        void loadGetPayeeInfoFail(BiiResultErrorException biiResultErrorException);
    }

    public interface GetPayeeInfoPresenter extends BasePresenter {
        /*** 查询收款人信息  */
        void loadGetPayeeInfo(String qrNo);
    }

}
