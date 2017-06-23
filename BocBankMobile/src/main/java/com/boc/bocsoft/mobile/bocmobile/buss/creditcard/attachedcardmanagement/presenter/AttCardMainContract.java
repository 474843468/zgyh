package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryMasterAndSupplInfo.PsnCrcdQueryMasterAndSupplInfoResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * Name: liukai
 * Time：2016/12/5 9:57.
 * Created by lk7066 on 2016/12/5.
 * It's used to
 */

public class AttCardMainContract {

    public interface AttCardMainView extends BaseView<AttCardMainPresenter>{

        /*主附卡信息查询回调*/
        void masterAndSupplInfoSuccess(List<PsnCrcdQueryMasterAndSupplInfoResult> psnCrcdQueryMasterAndSupplInfoResult);
        void masterAndSupplInfoFailed(BiiResultErrorException exception);

    }

    public interface AttCardMainPresenter extends BasePresenter{

        /*主附卡信息查询*/
        void queryMasterAndSupplInfo(String accountId);

    }

}
