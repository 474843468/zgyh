package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.deposit;

import android.support.annotation.NonNull;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeSubmit.PsnLOANPledgeSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeSubmit.PsnLOANPledgeSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeVerify.PsnLOANPledgeVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPledgeVerify.PsnLOANPledgeVerifyResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess.BaseConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PersonalTimeAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeDepositInfoFillViewModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

public class PledgeLoanDepositInfoConfirmPresenter
        extends BaseConfirmPresenter<PledgeDepositInfoFillViewModel, String> {

    public PledgeLoanDepositInfoConfirmPresenter(BaseConfirmContract.View<String> view) {
        super(view);
    }

    @NonNull
    @Override
    protected Observable<VerifyBean> getVerifyBean(PledgeDepositInfoFillViewModel fillInfo) {
        PsnLOANPledgeVerifyParams params =
                BeanConvertor.fromBean(fillInfo, new PsnLOANPledgeVerifyParams());
        return mLoanService.psnLOANPledgeVerify(params)
                           .compose(this.<PsnLOANPledgeVerifyResult>bindToLifecycle())
                           .map(new Func1<PsnLOANPledgeVerifyResult, VerifyBean>() {
                               @Override
                               public VerifyBean call(
                                       PsnLOANPledgeVerifyResult psnLOANPledgeVerifyResult) {
                                   return BeanConvertor.toBean(psnLOANPledgeVerifyResult,
                                           new VerifyBean());
                               }
                           });
    }

    @Override
    public void submit(final PledgeDepositInfoFillViewModel fillInfo,
            final BaseSubmitBean submitBean) {
        Observable.from(fillInfo.getPersonalTimeAccountBeanArrayList())
                  .map(new Func1<PersonalTimeAccountBean, PsnLOANPledgeSubmitParams.VolNoAndCerNoListEntity>() {
                      @Override
                      public PsnLOANPledgeSubmitParams.VolNoAndCerNoListEntity call(
                              PersonalTimeAccountBean personalTimeAccountBean) {
                          PsnLOANPledgeSubmitParams.VolNoAndCerNoListEntity
                                  volNoAndCerNoListEntity =
                                  new PsnLOANPledgeSubmitParams.VolNoAndCerNoListEntity();
                          volNoAndCerNoListEntity.setCerNo(personalTimeAccountBean.getCdNumber());
                          volNoAndCerNoListEntity.setCurrencyCode(
                                  personalTimeAccountBean.getCurrencyCode());
                          volNoAndCerNoListEntity.setPingNo(personalTimeAccountBean.getPingNo());
                          volNoAndCerNoListEntity.setVolNo(
                                  personalTimeAccountBean.getVolumeNumber());
                          volNoAndCerNoListEntity.setAvailableBalance(
                                  personalTimeAccountBean.getAvailableBalance()
                                                         .stripTrailingZeros()
                                                         .toPlainString());
                          return volNoAndCerNoListEntity;
                      }
                  })
                  .toList()
                  .map(new Func1<List<PsnLOANPledgeSubmitParams.VolNoAndCerNoListEntity>, PsnLOANPledgeSubmitParams>() {

                      @Override
                      public PsnLOANPledgeSubmitParams call(
                              List<PsnLOANPledgeSubmitParams.VolNoAndCerNoListEntity> volNoAndCerNoListEntities) {
                          PsnLOANPledgeSubmitParams params =
                                  BeanConvertor.fromBean(fillInfo, new PsnLOANPledgeSubmitParams());
                          params = BeanConvertor.fromBean(submitBean, params);
                          params.setVolNoAndCerNoList(volNoAndCerNoListEntities);
                          return params;
                      }
                  })
                  .flatMap(
                          new Func1<PsnLOANPledgeSubmitParams, Observable<PsnLOANPledgeSubmitResult>>() {
                              @Override
                              public Observable<PsnLOANPledgeSubmitResult> call(
                                      PsnLOANPledgeSubmitParams psnLOANPledgeSubmitParams) {
                                  return mLoanService.psnLOANPledgeSubmit(
                                          psnLOANPledgeSubmitParams);
                              }
                          })
                  .compose(this.<PsnLOANPledgeSubmitResult>bindToLifecycle())
                  .compose(SchedulersCompat.<PsnLOANPledgeSubmitResult>applyIoSchedulers())
                  .subscribe(new BIIBaseSubscriber<PsnLOANPledgeSubmitResult>() {
                      @Override
                      public void handleException(BiiResultErrorException biiResultErrorException) {

                      }

                      @Override
                      public void onCompleted() {

                      }

                      @Override
                      public void onNext(PsnLOANPledgeSubmitResult psnLOANPledgeSubmitResult) {
                          mView.onSubmitSuccess(psnLOANPledgeSubmitResult.getTransactionId());
                      }
                  });
    }
}