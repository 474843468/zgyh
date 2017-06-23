package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui;

import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetAccountCatalog.QRPayGetAccountCatalogResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.List;

/**
 * Created by fanbin on 16/10/14.
 */
public class QRPayGetAccountCatalogContract {
    public interface QRPayGetAccountCatalogBaseView{
        /*** 查询账户类别成功 */
        void loadQRPayGetAccountCatalogSuccess(QRPayGetAccountCatalogResult qrPayGetAccountCatalogResult);
        /*** 查询账户类别失败 */
        void loadQRPayGetAccountCatalogFail(BiiResultErrorException biiResultErrorException);
    }
    public interface QRPayGetAccountCatalogBasePresenter extends BasePresenter{
        /*** 查询账户类别 */
        void qRPayGetAccountCatalog(String actSeq);
        /*** 并发 查询账户类别 */
        void qRPayGetAccountCatalog(List<AccountBean> accountBeans);
    }
}
