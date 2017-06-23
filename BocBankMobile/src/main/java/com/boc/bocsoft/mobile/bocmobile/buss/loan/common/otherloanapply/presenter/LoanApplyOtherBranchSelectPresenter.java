package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanBranchQry.PsnOnLineLoanBranchQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnOnLineLoanBranchQry.PsnOnLineLoanBranchQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.BranchSelectViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanBranchBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui.LoanApplyOtherBranchSelectContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import rx.Observable;
import rx.functions.Func1;

public class LoanApplyOtherBranchSelectPresenter extends RxPresenter
        implements LoanApplyOtherBranchSelectContract.Presenter {

    private LoanApplyOtherBranchSelectContract.View mLoanApplyOtherBranchSelectView;
    private PsnLoanService mLoanService;
    private GlobalService mGlobalService;
    private String mConversationId;

    public LoanApplyOtherBranchSelectPresenter(LoanApplyOtherBranchSelectContract.View view) {
        mLoanApplyOtherBranchSelectView = view;
        mLoanService = new PsnLoanService();
        mGlobalService = new GlobalService();
    }

    @Override
    public void getOnLineLoanBranch(final BranchSelectViewModel viewModel) {
        final PsnOnLineLoanBranchQryParams params = new PsnOnLineLoanBranchQryParams();
        params.setCityCode(viewModel.getCityCode());
        params.setProductCode(viewModel.getProductCode());
        params.set_refresh(viewModel.get_refresh());
        params.setCurrentIndex(String.valueOf(viewModel.getCurrentIndex()));
        params.setPageSize(String.valueOf(viewModel.getPageSize()));
        (StringUtils.isEmpty(mConversationId)?
                mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                      :Observable.just(mConversationId))
                      .flatMap(new Func1<String, Observable<PsnOnLineLoanBranchQryResult>>() {
                          @Override
                          public Observable<PsnOnLineLoanBranchQryResult> call(
                                  String conversationId) {
                              if (StringUtils.isEmpty(mConversationId)) mConversationId=conversationId;
                              params.setConversationId(conversationId);
                              return mLoanService.psnOnLineLoanBranchQry(params);
                          }
                      })
                      .compose(this.<PsnOnLineLoanBranchQryResult>bindToLifecycle())
                      .compose(SchedulersCompat.<PsnOnLineLoanBranchQryResult>applyIoSchedulers())
                      .subscribe(new BIIBaseSubscriber<PsnOnLineLoanBranchQryResult>() {
                          @Override
                          public void handleException(
                                  BiiResultErrorException biiResultErrorException) {
                              mLoanApplyOtherBranchSelectView.onLoadFailed();
                          }

                          @Override
                          public void onCompleted() {

                          }

                          @Override
                          public void onNext(
                                  PsnOnLineLoanBranchQryResult psnOnLineLoanBranchQryResult) {
                              if (psnOnLineLoanBranchQryResult != null) {
                                  viewModel.setRecordNumber(Integer.valueOf(
                                          psnOnLineLoanBranchQryResult.getRecordNumber()));
                                  if (!PublicUtils.isEmpty(psnOnLineLoanBranchQryResult.getList())) {
                                      for (PsnOnLineLoanBranchQryResult.ListEntity listEntity : psnOnLineLoanBranchQryResult
                                              .getList()) {
                                          OnLineLoanBranchBean onLineLoanBranchBean =
                                                  BeanConvertor.toBean(listEntity,
                                                          new OnLineLoanBranchBean());
                                          viewModel.getList().add(onLineLoanBranchBean);
                                      }
                                  }
                                  mLoanApplyOtherBranchSelectView.onLoadSuccess(viewModel);
                              }
                          }
                      });
    }
}