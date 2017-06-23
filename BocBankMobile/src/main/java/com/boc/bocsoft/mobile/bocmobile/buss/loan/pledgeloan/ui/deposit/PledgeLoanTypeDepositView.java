package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.deposit;

import android.content.Context;
import android.util.AttributeSet;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeAvaAndPersonalTimeAccount;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanTypeView;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 *
 */
public class PledgeLoanTypeDepositView extends PledgeLoanTypeView {

    private List<PledgeAvaAndPersonalTimeAccount> mPledgeAvaAndPersonalTimeAccountList;

    public PledgeLoanTypeDepositView(Context context) {
        super(context);
    }

    public PledgeLoanTypeDepositView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PledgeLoanTypeDepositView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        super.initView();
        tvTitle.setText(getContext().getString(R.string.boc_loan_pledge_title_deposit));
        tvDescription.setText(getContext().getString(R.string.boc_pledge_description_deposit));
        tvEmpty.setText(R.string.boc_pledge_deposit_empty);
    }

    @Override
    protected void applyLoan() {
        mBussFragment.start(PledgeLoanDepositReceiptSelectFragment.newInstance(mPledgeAvaAndPersonalTimeAccountList));
    }

    public void onLoadSuccess(
            List<PledgeAvaAndPersonalTimeAccount> pledgeAvaAndPersonalTimeAccountList) {
        mPledgeAvaAndPersonalTimeAccountList=pledgeAvaAndPersonalTimeAccountList;
        boolean hasData = !PublicUtils.isEmpty(pledgeAvaAndPersonalTimeAccountList);
        showData(hasData);
        if (hasData) {
            Observable.from(pledgeAvaAndPersonalTimeAccountList)
                      .map(new Func1<PledgeAvaAndPersonalTimeAccount, Integer>() {
                          @Override
                          public Integer call(
                                  PledgeAvaAndPersonalTimeAccount pledgeAvaAndPersonalTimeAccount) {
                              return pledgeAvaAndPersonalTimeAccount.getPersonalTimeAccountBeanList()
                                                                    .size();
                          }
                      })
                      .reduce(new Func2<Integer, Integer, Integer>() {
                          @Override
                          public Integer call(Integer integer, Integer integer2) {
                              return integer + integer2;
                          }
                      })
                      .subscribe(new Action1<Integer>() {
                          @Override
                          public void call(Integer integer) {
                              tvCount.setText(
                                      String.format(getContext().getString(R.string.boc_pledge_count_deposit),
                                              integer));
                          }
                      });
        }
    }

    public void onLoadFailed() {
        showData(false);
    }
}