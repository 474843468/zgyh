package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PassFreeInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PayQuotaViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.math.BigDecimal;

/**
 * Created by wangf on 2016/8/31.
 */
public class QRPayBaseContract {


    public interface QrServiceOpenBaseView{
        /*** 开通二维码服务成功 */
        void loadQRServiceOpenSuccess();

        /*** 开通二维码服务失败 */
        void loadQRServiceOpenFail(BiiResultErrorException biiResultErrorException);
    }


    public interface QrQueryBaseView {

        /*** 查询关联账户中的银联账户列表 成功 */
        void queryRelativeAccountListSuccess();

        /*** 查询关联账户中的银联账户列表 失败 */
        void queryRelativeAccountListFail(BiiResultErrorException biiResultErrorException);

        /*** 查询客户是否开通二维码服务 成功 */
        void loadQRServiceIsOpenSuccess(String flag);

        /*** 查询客户是否开通二维码服务 失败 */
        void loadQRServiceIsOpenFail(BiiResultErrorException biiResultErrorException);

        /*** 查询默认卡成功 */
        void loadQRPayGetDefaultCardSuccess(String actSeq);

        /*** 查询默认卡失败*/
        void loadQRPayGetDefaultCardFail(BiiResultErrorException biiResultErrorException);

        /*** 查询是否设置支付密码成功 */
        void loadQRPayIsPassSetSuccess(String flag);

        /*** 查询是否设置支付密码失败 */
        void loadQRPayIsPassSetFail(BiiResultErrorException biiResultErrorException);

    }

    public interface QRQueryPassFreeInfoBaseView{
        /*** 查询小额免密信息成功 */
        void loadQRPayGetPassFreeInfoSuccess(PassFreeInfoViewModel infoViewModel);

        /*** 查询小额免密信息失败 */
        void loadQRPayGetPassFreeInfoFail(BiiResultErrorException biiResultErrorException);

        /*** 查询支付限额成功*/
        void loadQRPayGetPayQuotaSuccess(PayQuotaViewModel quotaViewModel);

        /*** 查询支付限额失败*/
        void loadQRPayGetPayQuotaFail(BiiResultErrorException biiResultErrorException);
    }

    public interface QrSetCardBaseView{
        /*** 设置默认卡成功 */
        void loadQRPaySetDefaultCardSuccess();

        /*** 设置默认卡失败 */
        void loadQRPaySetDefaultCardFail(BiiResultErrorException biiResultErrorException);
    }

    public interface QrAccountBaseView {
        /** * 查询账户详情成功*/
        void queryAccountDetailsSuccess(BigDecimal availableBalance);

        /*** 查询账户详情失败 */
        void queryAccountDetailsFail(BiiResultErrorException biiResultErrorException);

        /** * 查询信用卡账户详情成功*/
        void queryCreditAccountDetailSuccess(BigDecimal availableBalance);

        /*** 查询信用卡账户详情失败 */
        void queryCreditAccountDetailFail(BiiResultErrorException biiResultErrorException);
    }

    public interface QrCodeBasePresenter extends BasePresenter {

        /**查询关联账户中的银联账户列表*/
        void queryQRPayGetRelativedAcctList();

        /**查询中行所有帐户列表*/
        void queryAllChinaBankAccount();

        /** 获取客户等级及信息 */
        void queryOprLoginInfo();

        /**获取服务器时间*/
        void querySystemDateTime();

        /*** 查询客户是否开通二维码服务 */
        void loadQRServiceIsOpen();

        /*** 开通二维码服务 */
        void loadQRServiceOpen();

        /*** 查询默认卡 */
        void loadQRPayGetDefaultCard();

        /*** 查询是否设置支付密码 */
        void loadQRPayIsPassSet();

        /*** 查询支付限额 */
        void loadQRPayGetPayQuota(String actSeq, String type);

        /*** 查询小额免密信息 */
        void loadQRPayGetPassFreeInfo(String actSeq);

        /*** 设置默认卡 */
        void loadQRPaySetDefaultCard(String actSeq);

        /*** 查询普通账户详情*/
        void queryAccountDetails(String accountID);

        /*** 查询信用卡账户详情*/
        void queryCreditAccountDetail(String accountId, String currencyId);
    }
}
