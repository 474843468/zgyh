package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPledgeAvaAccountQuery.PsnLoanPledgeAvaAccountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPledgeAvaAccountQuery.PsnLoanPledgeAvaAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeProductQry.PsnLoanXpadPledgeProductQryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeProductQry.PsnLoanXpadPledgeProductQryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnQueryPersonalTimeAccount.PsnQueryPersonalTimeAccountParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnQueryPersonalTimeAccount.PsnQueryPersonalTimeAccountResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.DepositsData;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PersonalTimeAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeAvaAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeAvaAndPersonalTimeAccount;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeProductBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.ProductsData;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanTypeSelectContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import java.math.BigDecimal;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

public class PledgeLoanTypeSelectPresenter extends RxPresenter
        implements PledgeLoanTypeSelectContract.Presenter {

    private PledgeLoanTypeSelectContract.View mPledgeLoanTypeSelectView;
    private PsnLoanService mLoanService;
    private GlobalService mGlobalService;
    private String mConversationId;

    public PledgeLoanTypeSelectPresenter(PledgeLoanTypeSelectContract.View view) {
        mPledgeLoanTypeSelectView = view;
        mLoanService = new PsnLoanService();
        mGlobalService = new GlobalService();
    }

    @Override
    public void loadDepositData() {
        mLoanService.psnLoanPledgeAvaAccountQuery(new PsnLoanPledgeAvaAccountQueryParams())
                    .filter(new Func1<List<PsnLoanPledgeAvaAccountQueryResult>, Boolean>() {
                        @Override
                        public Boolean call(
                                List<PsnLoanPledgeAvaAccountQueryResult> psnLoanPledgeAvaAccountQueryResults) {
                            return !PublicUtils.isEmpty(psnLoanPledgeAvaAccountQueryResults);
                        }
                    })
                    .concatMap(
                            new Func1<List<PsnLoanPledgeAvaAccountQueryResult>, Observable<PsnLoanPledgeAvaAccountQueryResult>>() {
                                @Override
                                public Observable<PsnLoanPledgeAvaAccountQueryResult> call(
                                        List<PsnLoanPledgeAvaAccountQueryResult> psnLoanPledgeAvaAccountQueryResults) {
                                    return Observable.from(psnLoanPledgeAvaAccountQueryResults);
                                }
                            })
                    .map(new Func1<PsnLoanPledgeAvaAccountQueryResult, PledgeAvaAccountBean>() {
                        @Override
                        public PledgeAvaAccountBean call(
                                PsnLoanPledgeAvaAccountQueryResult psnLoanPledgeAvaAccountQueryResult) {
                            return BeanConvertor.toBean(psnLoanPledgeAvaAccountQueryResult,
                                    new PledgeAvaAccountBean());
                        }
                    })
                    .concatMap(
                            new Func1<PledgeAvaAccountBean, Observable<PledgeAvaAndPersonalTimeAccount>>() {
                                @Override
                                public Observable<PledgeAvaAndPersonalTimeAccount> call(
                                        final PledgeAvaAccountBean pledgeAvaAccountBean) {
                                    PsnQueryPersonalTimeAccountParams params =
                                            new PsnQueryPersonalTimeAccountParams();
                                    params.setAccountId(pledgeAvaAccountBean.getAccountId());
                                    return mLoanService.psnQueryPersonalTimeAccount(params)
                                                       .filter(new Func1<List<PsnQueryPersonalTimeAccountResult>, Boolean>() {
                                                           @Override
                                                           public Boolean call(
                                                                   List<PsnQueryPersonalTimeAccountResult> psnQueryPersonalTimeAccountResults) {
                                                               return !PublicUtils.isEmpty(
                                                                       psnQueryPersonalTimeAccountResults);
                                                           }
                                                       })
                                                       .concatMap(
                                                               new Func1<List<PsnQueryPersonalTimeAccountResult>, Observable<PsnQueryPersonalTimeAccountResult>>() {
                                                                   @Override
                                                                   public Observable<PsnQueryPersonalTimeAccountResult> call(
                                                                           List<PsnQueryPersonalTimeAccountResult> psnQueryPersonalTimeAccountResults) {
                                                                       return Observable.from(
                                                                               psnQueryPersonalTimeAccountResults);
                                                                   }
                                                               })
                                                       .filter(new Func1<PsnQueryPersonalTimeAccountResult, Boolean>() {
                                                           @Override
                                                           public Boolean call(
                                                                   PsnQueryPersonalTimeAccountResult psnQueryPersonalTimeAccountResult) {
                                                               return "00".equals(
                                                                       psnQueryPersonalTimeAccountResult
                                                                               .getStatus())
                                                                       //存单必须是整存整取
                                                                       && "110".equals(
                                                                       psnQueryPersonalTimeAccountResult
                                                                               .getType())
                                                                       && "R".equals(
                                                                       psnQueryPersonalTimeAccountResult
                                                                               .getConvertType()) &&
                                                                       BigDecimal.ZERO.compareTo(
                                                                               psnQueryPersonalTimeAccountResult
                                                                                       .getHoldAmount())
                                                                               == 0;
                                                           }
                                                       })
                                                       .map(new Func1<PsnQueryPersonalTimeAccountResult, PersonalTimeAccountBean>() {
                                                           @Override
                                                           public PersonalTimeAccountBean call(
                                                                   PsnQueryPersonalTimeAccountResult psnQueryPersonalTimeAccountResult) {
                                                               return BeanConvertor.toBean(
                                                                       psnQueryPersonalTimeAccountResult,
                                                                       new PersonalTimeAccountBean());
                                                           }
                                                       })
                                                       .toList()
                                                       .map(new Func1<List<PersonalTimeAccountBean>, PledgeAvaAndPersonalTimeAccount>() {
                                                           @Override
                                                           public PledgeAvaAndPersonalTimeAccount call(
                                                                   List<PersonalTimeAccountBean> personalTimeAccountBeanList) {
                                                               return (PublicUtils.isEmpty(
                                                                       personalTimeAccountBeanList))
                                                                       ? null
                                                                       : new PledgeAvaAndPersonalTimeAccount(
                                                                               pledgeAvaAccountBean,
                                                                               personalTimeAccountBeanList);
                                                           }
                                                       })
                                                       .filter(new Func1<PledgeAvaAndPersonalTimeAccount, Boolean>() {
                                                           @Override
                                                           public Boolean call(
                                                                   PledgeAvaAndPersonalTimeAccount pledgeAvaAndPersonalTimeAccount) {
                                                               return pledgeAvaAndPersonalTimeAccount
                                                                       != null;
                                                           }
                                                       });
                                }
                            })
                    .toList()
                    .map(new Func1<List<PledgeAvaAndPersonalTimeAccount>, DepositsData>() {
                        @Override
                        public DepositsData call(
                                List<PledgeAvaAndPersonalTimeAccount> pledgeAvaAndPersonalTimeAccountList) {
                            return new DepositsData(pledgeAvaAndPersonalTimeAccountList);
                        }
                    })
                    .compose(this.<DepositsData>bindToLifecycle())
                    .compose(SchedulersCompat.<DepositsData>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<DepositsData>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {
                            mPledgeLoanTypeSelectView.onLoadDepositDataFailed();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(DepositsData depositsData) {
                            mPledgeLoanTypeSelectView.onLoadDepositDataSuccess(
                                    depositsData.getPledgeAvaAndPersonalTimeAccountList());
                        }
                    });
    }

    @Override
    public void loadProductData() {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                      .flatMap(
                              new Func1<String, Observable<List<PsnLoanXpadPledgeProductQryResult>>>() {
                                  @Override
                                  public Observable<List<PsnLoanXpadPledgeProductQryResult>> call(
                                          String conversationId) {
                                      mConversationId = conversationId;
                                      PsnLoanXpadPledgeProductQryParams params =
                                              new PsnLoanXpadPledgeProductQryParams();
                                      params.setConversationId(mConversationId);
                                      return mLoanService.psnLoanXpadPledgeProductQry(params);
                                  }
                              })
                      .filter(new Func1<List<PsnLoanXpadPledgeProductQryResult>, Boolean>() {
                          @Override
                          public Boolean call(
                                  List<PsnLoanXpadPledgeProductQryResult> psnLoanXpadPledgeProductQryResults) {
                              return !PublicUtils.isEmpty(psnLoanXpadPledgeProductQryResults);
                          }
                      })
                      .flatMap(
                              new Func1<List<PsnLoanXpadPledgeProductQryResult>, Observable<PsnLoanXpadPledgeProductQryResult>>() {
                                  @Override
                                  public Observable<PsnLoanXpadPledgeProductQryResult> call(
                                          List<PsnLoanXpadPledgeProductQryResult> psnLoanXpadPledgeProductQryResults) {
                                      return Observable.from(psnLoanXpadPledgeProductQryResults);
                                  }
                              })
                      .map(new Func1<PsnLoanXpadPledgeProductQryResult, PledgeProductBean>() {
                          @Override
                          public PledgeProductBean call(
                                  PsnLoanXpadPledgeProductQryResult psnLoanXpadPledgeProductQryResult) {
                              return BeanConvertor.toBean(psnLoanXpadPledgeProductQryResult,
                                      new PledgeProductBean());
                          }
                      })
                      .toList()
                      .map(new Func1<List<PledgeProductBean>, ProductsData>() {
                          @Override
                          public ProductsData call(List<PledgeProductBean> pledgeProductBeen) {
                              return new ProductsData(mConversationId, pledgeProductBeen);
                          }
                      })
                      .compose(this.<ProductsData>bindToLifecycle())
                      .compose(SchedulersCompat.<ProductsData>applyIoSchedulers())
                      .subscribe(new BIIBaseSubscriber<ProductsData>() {
                          @Override
                          public void handleException(
                                  BiiResultErrorException biiResultErrorException) {
                              mPledgeLoanTypeSelectView.onLoadProductDataFailed();
                          }

                          @Override
                          public void onCompleted() {

                          }

                          @Override
                          public void onNext(ProductsData productsData) {
                              mPledgeLoanTypeSelectView.onLoadProductDataSuccess(productsData);
                          }
                      });
    }
}