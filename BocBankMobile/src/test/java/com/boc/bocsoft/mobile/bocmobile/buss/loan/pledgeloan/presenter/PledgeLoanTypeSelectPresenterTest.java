package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanPledgeAvaAccountQuery.PsnLoanPledgeAvaAccountQueryResult;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * 作者：XieDu
 * 创建时间：2016/8/29 10:23
 * 描述：
 */
public class PledgeLoanTypeSelectPresenterTest {

    @Test
    public void testGetDepositList() throws Exception {
        List<PsnLoanPledgeAvaAccountQueryResult> psnLoanPledgeAvaAccountQueryResults = new ArrayList<>();
        Observable.just(psnLoanPledgeAvaAccountQueryResults)
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
                  .filter(new Func1<PsnLoanPledgeAvaAccountQueryResult, Boolean>() {
                      @Override
                      public Boolean call(PsnLoanPledgeAvaAccountQueryResult psnLoanPledgeAvaAccountQueryResult) {
                          return psnLoanPledgeAvaAccountQueryResult!=null;
                      }
                  })
                  .toList()
                  .subscribe(new Subscriber<List<PsnLoanPledgeAvaAccountQueryResult>>() {
                      @Override
                      public void onCompleted() {
                          System.out.println("onCompleted");
                      }

                      @Override
                      public void onError(Throwable throwable) {
                          System.out.println("onError:" + throwable);
                      }

                      @Override
                      public void onNext(
                              List<PsnLoanPledgeAvaAccountQueryResult> psnLoanPledgeAvaAccountQueryResults) {
                          System.out.println(
                                  "onNext:" + psnLoanPledgeAvaAccountQueryResults.size());
                      }
                  });
    }
}