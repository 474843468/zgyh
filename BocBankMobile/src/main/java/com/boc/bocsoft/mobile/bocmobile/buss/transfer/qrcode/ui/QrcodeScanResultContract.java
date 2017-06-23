package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.AccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.ScanTransferParamViewModel;

/**
 * Created by xdy4486 on 2016/6/24.
 */
public class QrcodeScanResultContract {
    public interface View {
        void onError(String msg);

        //TODO 需要什么参数类型？
        void setTranoutAccountList();

        void setTranoutAccountDetail(AccountDetailModel model);

        void onNoCNYTranoutAccount();

        /**获取安全因子成功回调*/
        void onSecurityFactorSuccess(PsnGetSecurityFactorResult psnGetSecurityFactorResult);

        //TODO 预交易成功传入什么参数
        /**预交易成功*/
        void onTransBocTransferVerify();
    }

    public interface Presenter {
        /**
         * 请求转出账户列表数据
         */
        void requestTranoutAccountList();
        /**
         * 请求账户详情
         */
        void requestAccountDetail(AccountBean cardItem);

        /**
         * 查询安全因子
         */
        void requestSecurityFactor();

        /**
         * 预交易
         */
        void requestForTransBocTransferVerify(ScanTransferParamViewModel viewModel);
    }
}
