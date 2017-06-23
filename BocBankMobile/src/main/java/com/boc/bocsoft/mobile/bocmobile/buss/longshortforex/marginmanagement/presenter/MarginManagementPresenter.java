package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery.PsnVFGBailListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGQueryMaxAmount.PsnVFGQueryMaxAmountParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGQueryMaxAmount.PsnVFGQueryMaxAmountResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGQueryMaxAmountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.model.XpadVFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui.MarginManagementContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hty7062
 */

public class MarginManagementPresenter extends RxPresenter implements MarginManagementContract.Presenter {

    private MarginManagementContract.View mMarginManagementView;
    //private SelectMarginAccountFragment.
    private LongShortForexService longShortForexService;
    private AccountService accountService;
    private BigDecimal bookBalance;

    public MarginManagementPresenter(MarginManagementContract.View view) {
        this.mMarginManagementView = view;

        longShortForexService = new LongShortForexService();
        accountService = new AccountService();

    }

    /**
     * 交易账户查询
     * @param xpadVFGGetBindAccountViewModel
     */
    @Override
    public void psnVFGGetBindAccount(final XpadVFGGetBindAccountViewModel xpadVFGGetBindAccountViewModel) {
        //((BussFragment) mMarginManagementView).showLoadingDialog("请稍候...");
        final PsnVFGGetBindAccountParams psnVFGGetBindAccountParams = new PsnVFGGetBindAccountParams();
        longShortForexService.psnVFGGetBindAccount(psnVFGGetBindAccountParams)
                .compose(SchedulersCompat.<PsnVFGGetBindAccountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGGetBindAccountResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mMarginManagementView.psnVFGGetBindAccountFail(biiResultErrorException);
                        System.out.print("Exception------------>>>>>>>>>---异常");
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGGetBindAccountResult psnVFGGetBindAccountResult) {

                        BeanConvertor.toBean(psnVFGGetBindAccountResult, xpadVFGGetBindAccountViewModel);
                        mMarginManagementView.psnVFGGetBindAccountSuccess(xpadVFGGetBindAccountViewModel);
                    }
                });
    }

    /**
     * 账户余额查询
     * @param accountId
     */
    public void psnAccountQueryAccountDetail(String accountId){
        PsnAccountQueryAccountDetailParams psnAccountQueryAccountDetailParams = new PsnAccountQueryAccountDetailParams();
        psnAccountQueryAccountDetailParams.setAccountId(accountId);
        accountService.psnAccountQueryAccountDetail(psnAccountQueryAccountDetailParams)
                .compose(SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        //mMarginManagementView.psnVFGGetBindAccountFail(biiResultErrorException);
                        System.out.print("Exception------------>>>>>>>>>---异常");
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnAccountQueryAccountDetailResult psnAccountQueryAccountDetailResult) {
                        mMarginManagementView.psnAccountQueryAccountSuccess(psnAccountQueryAccountDetailResult);

                    }
                });
    }


    /**
     * 保证金账户查询
     * @param viewmodel
     */
    @Override
    public void psnXpadBailListQuery(final VFGBailListQueryViewModel viewmodel) {
        PsnVFGBailListQueryParams psnVFGBailListQueryParams = new PsnVFGBailListQueryParams();
        longShortForexService.psnVFGBailListQuery(psnVFGBailListQueryParams)
                .compose(SchedulersCompat.<List<PsnVFGBailListQueryResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnVFGBailListQueryResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mMarginManagementView.psnXpadBailListQueryFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<PsnVFGBailListQueryResult> psnVFGBailListQueryResult) {
                        BeanConvertor.toBean(psnVFGBailListQueryResult, viewmodel);
                        System.out.println("hjjhjj--->>>>" + psnVFGBailListQueryResult.size());
                        mMarginManagementView.psnXpadBailListQuerySuccess(generateXpadBailListQueryViewModel(psnVFGBailListQueryResult,viewmodel));
                    }
                });
    }

    private VFGBailListQueryViewModel generateXpadBailListQueryViewModel(List<PsnVFGBailListQueryResult> results, VFGBailListQueryViewModel viewModel) {
        if (results != null) {
            List<VFGBailListQueryViewModel.BailItemEntity> viewList = new ArrayList<VFGBailListQueryViewModel.BailItemEntity>();
            for (PsnVFGBailListQueryResult queryresult : results) {
                VFGBailListQueryViewModel.BailItemEntity guarantyProductEntity = new VFGBailListQueryViewModel.BailItemEntity();
                BeanConvertor.toBean(queryresult, guarantyProductEntity);
                viewList.add(guarantyProductEntity);
            }
            viewModel.setEntityList(viewList);
        }
        return viewModel;
    }


    /**
     * 保证金余额查询
     * @param vfgQueryMaxAmountViewModel
     */
    @Override
    public void psnVFGQueryMaxAmount(final VFGQueryMaxAmountViewModel vfgQueryMaxAmountViewModel) {
        PsnVFGQueryMaxAmountParams params = new PsnVFGQueryMaxAmountParams();
        params.setCashRemit(vfgQueryMaxAmountViewModel.getCashRemit());
        params.setCurrencyCode(vfgQueryMaxAmountViewModel.getCurrencyCode());
        longShortForexService.psnVFGQueryMaxAmount(params)
                .compose(SchedulersCompat.<PsnVFGQueryMaxAmountResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGQueryMaxAmountResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        if ("1".equals(vfgQueryMaxAmountViewModel.getCashRemit())){
                            mMarginManagementView.psnVFGQueryMaxCashAmountFail(biiResultErrorException);
                        }
                        else{
                            mMarginManagementView.psnVFGQueryMaxSpotAmountFail(biiResultErrorException);
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnVFGQueryMaxAmountResult psnVFGQueryMaxAmountResult) {
                        BeanConvertor.toBean(psnVFGQueryMaxAmountResult, vfgQueryMaxAmountViewModel);
                        System.out.println("vfgQueryMax------>>>" + vfgQueryMaxAmountViewModel.getCashRemit());
                        if ("0".equals(vfgQueryMaxAmountViewModel.getCashRemit())){
                            mMarginManagementView.psnVFGQueryMaxCashAmountSuccess(vfgQueryMaxAmountViewModel);
                        }else if ("1".equals(vfgQueryMaxAmountViewModel.getCashRemit())){
                            mMarginManagementView.psnVFGQueryMaxCashAmountSuccess(vfgQueryMaxAmountViewModel);
                        } else{
                            mMarginManagementView.psnVFGQueryMaxSpotAmountSuccess(vfgQueryMaxAmountViewModel);
                        }
                    }
                });
    }
}

