package com.boc.bocsoft.mobile.bocmobile.buss.easybuss.presenter;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnRedirectEzuc.PsnRedirectEzucParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnRedirectEzuc.PsnRedirectEzucResult;
import com.boc.bocsoft.mobile.bii.bus.login.service.PsnLoginService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageIsOpen.PsnInvestmentManageIsOpenParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.presenter.WebPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.easybuss.model.RedirectEzucBean;
import com.boc.bocsoft.mobile.bocmobile.buss.easybuss.ui.EasyBussContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import rx.functions.Func1;

public class EasyBussPresenter extends WebPresenter implements EasyBussContract.Presenter {

    private EasyBussContract.View mEasyBussView;
    private PsnLoginService mLoginService;
    private WealthManagementService wealthService;// 理财Service

    public EasyBussPresenter(EasyBussContract.View view) {
        super(view);
        mEasyBussView = view;
        mLoginService = new PsnLoginService();
        wealthService=new WealthManagementService();
    }

    @Override
    public void qryInvestOpenState() {
        PsnInvestmentManageIsOpenParams params = new PsnInvestmentManageIsOpenParams();
        wealthService.psnInvestmentManageIsOpen(params)
                     .compose(this.<Boolean>bindToLifecycle())
                     .compose(SchedulersCompat.<Boolean>applyIoSchedulers())
                     .subscribe(new BIIBaseSubscriber<Boolean>() {

                         @Override
                         public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                         }

                         @Override
                         public void handleException(BiiResultErrorException biiResultErrorException) {
                             mEasyBussView.onQryInvestOpenStateFailed(biiResultErrorException.getErrorMessage());
                         }

                         @Override
                         public void onCompleted() {

                         }

                         @Override
                         public void onNext(Boolean result) {
                             mEasyBussView.onQryInvestOpenStateSuccess(result);
                         }
                     });
    }

    @Override
    public void onQryRedirectEzuc() {
        mLoginService.psnRedirectEzuc(new PsnRedirectEzucParams())
                     .compose(this.<PsnRedirectEzucResult>bindToLifecycle())
                     .map(new Func1<PsnRedirectEzucResult, RedirectEzucBean>() {
                         @Override
                         public RedirectEzucBean call(PsnRedirectEzucResult psnRedirectEzucResult) {
                             RedirectEzucBean bean = new RedirectEzucBean();
                             bean.setBocnetUserId(psnRedirectEzucResult.getUID());
                             bean.setCustId(psnRedirectEzucResult.getCID());
                             bean.setTicketValue(psnRedirectEzucResult.getTicketInfo());
                             return bean;
                         }
                     })
                     .compose(SchedulersCompat.<RedirectEzucBean>applyIoSchedulers())
                     .subscribe(new BIIBaseSubscriber<RedirectEzucBean>() {

                         @Override
                         public void commonHandleException(
                                 BiiResultErrorException biiResultErrorException) {
                         }

                         @Override
                         public void handleException(
                                 BiiResultErrorException biiResultErrorException) {
                             mEasyBussView.onQryRedirectEzucFailed(
                                     biiResultErrorException.getErrorMessage());
                         }

                         @Override
                         public void onCompleted() {

                         }

                         @Override
                         public void onNext(RedirectEzucBean redirectEzucBean) {
                             mEasyBussView.onQryRedirectEzucSuccess(redirectEzucBean);
                         }
                     });
    }
}