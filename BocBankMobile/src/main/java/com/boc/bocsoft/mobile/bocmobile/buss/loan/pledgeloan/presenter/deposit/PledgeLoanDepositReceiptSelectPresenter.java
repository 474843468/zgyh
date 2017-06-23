package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.deposit;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANMultipleQuery.PsnLOANMultipleQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANMultipleQuery.PsnLOANMultipleQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.LoanDepositMultipleQueryBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.deposit.PledgeLoanDepositReceiptSelectContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import java.util.ArrayList;
import rx.Observable;
import rx.functions.Func1;

public class PledgeLoanDepositReceiptSelectPresenter extends RxPresenter
        implements PledgeLoanDepositReceiptSelectContract.Presenter {

    private PledgeLoanDepositReceiptSelectContract.View mPledgeLoanDepositReceiptSelectView;
    protected PsnLoanService mLoanService;
    protected GlobalService mGlobalService;
    protected String mConversationId;

    public PledgeLoanDepositReceiptSelectPresenter(
            PledgeLoanDepositReceiptSelectContract.View view) {
        mPledgeLoanDepositReceiptSelectView = view;
        mLoanService = new PsnLoanService();
        mGlobalService = new GlobalService();
    }

    @Override
    public void qryDepositPledgeParamsData(final String accountId, final String currencyCode) {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                      .flatMap(new Func1<String, Observable<PsnLOANMultipleQueryResult>>() {
                          @Override
                          public Observable<PsnLOANMultipleQueryResult> call(
                                  String conversationId) {
                              mConversationId = conversationId;
                              PsnLOANMultipleQueryParams params = new PsnLOANMultipleQueryParams();
                              params.setConversationId(mConversationId);
                              params.setAccountId(accountId);
                              PsnLOANMultipleQueryParams.CnyListEntity cnyListEntity =
                                      new PsnLOANMultipleQueryParams.CnyListEntity();
                              cnyListEntity.setCurrencyCode(currencyCode);
                              ArrayList<PsnLOANMultipleQueryParams.CnyListEntity> cnyList =
                                      new ArrayList<>();
                              cnyList.add(cnyListEntity);
                              params.setCnyList(cnyList);
                              return mLoanService.psnLOANMultipleQuery(params);
                          }
                      })
                      .map(new Func1<PsnLOANMultipleQueryResult, LoanDepositMultipleQueryBean>() {
                          @Override
                          public LoanDepositMultipleQueryBean call(
                                  PsnLOANMultipleQueryResult psnLOANMultipleQueryResult) {
                              return BeanConvertor.toBean(psnLOANMultipleQueryResult,
                                      new LoanDepositMultipleQueryBean());
                          }
                      })
                      .compose(this.<LoanDepositMultipleQueryBean>bindToLifecycle())
                      .compose(SchedulersCompat.<LoanDepositMultipleQueryBean>applyIoSchedulers())
                      .subscribe(new BIIBaseSubscriber<LoanDepositMultipleQueryBean>() {
                          @Override
                          public void handleException(
                                  BiiResultErrorException biiResultErrorException) {

                          }

                          @Override
                          public void onCompleted() {

                          }

                          @Override
                          public void onNext(
                                  LoanDepositMultipleQueryBean loanDepositMultipleQueryBean) {
                              loanDepositMultipleQueryBean.setConversationId(mConversationId);
                              mPledgeLoanDepositReceiptSelectView.onQryDepositPledgeParamsDataSuccess(
                                      loanDepositMultipleQueryBean);
                          }
                      });
    }
}