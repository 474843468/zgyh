package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPayFreePwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.FreePassListItemView.FreePassViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.List;

/**
 * Created by wangf on 2016/9/18.
 */
public class QRPayFreePwdContract {

    public interface QrPaySecurityFactorView{
        /*** 查询安全因子成功 */
        void loadFreePwdSecurityFactorSuccess(SecurityViewModel securityViewModel);

        /*** 查询安全因子失败 */
        void loadFreePwdSecurityFactorFail(BiiResultErrorException biiResultErrorException);
    }


    public interface QRPayQueryPassFreeInfoView{
        /*** 查询小额免密信息成功 */
        void loadQRPayGetPassFreeInfoSuccess(FreePassViewModel passViewModel);

        /*** 查询小额免密信息失败 */
        void loadQRPayGetPassFreeInfoFail(BiiResultErrorException biiResultErrorException);
    }



    public interface QRPayFreePwdView{
        /*** 开通小额免密服务预交易 成功 */
        void loadQRPayOpenPassFreeServicePreSuccess(QRPayFreePwdViewModel freePwdViewModel);

        /*** 开通小额免密服务预交易 失败 */
        void loadQRPayOpenPassFreeServicePreFail(BiiResultErrorException biiResultErrorException);

        /*** 开通小额免密提交交易 成功 */
        void loadQRPayOpenPassFreeServiceSuccess();

        /*** 开通小额免密提交交易 失败 */
        void loadQRPayOpenPassFreeServiceFail(BiiResultErrorException biiResultErrorException);

        /*** 关闭小额免密服务 成功 */
        void loadQRPayClosePassFreeServiceSuccess();

        /*** 关闭小额免密服务 失败 */
        void loadQRPayClosePassFreeServiceFail(BiiResultErrorException biiResultErrorException);

    }





    public interface QrCodeFreePwdPresenter extends BasePresenter {
        /*** 查询安全因子 */
        void loadFreePwdSecurityFactor();

        /*** 查询小额免密信息 */
        void loadQRPayGetPassFreeInfo(AccountBean accountBean);

        /*** 并发 查询小额免密信息 */
        void loadAllPassFreeInfo(List<AccountBean> accountBeans);

        /*** 开通小额免密服务预交易 */
        void loadQRPayOpenPassFreeServicePre(QRPayFreePwdViewModel freePwdViewModel);

        /*** 开通小额免密提交交易 */
        void loadQRPayOpenPassFreeService(QRPayFreePwdViewModel freePwdViewModel);

        /*** 关闭小额免密服务 */
        void loadQRPayClosePassFreeService(String actSeq);

    }
}
