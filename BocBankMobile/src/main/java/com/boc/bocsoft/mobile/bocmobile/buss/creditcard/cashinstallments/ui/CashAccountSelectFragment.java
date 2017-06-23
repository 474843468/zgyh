package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.presenter.CashAccountSelectPresenter;

import java.util.ArrayList;

/**
 * Created by cry7096 on 2016/11/30.
 */
public class CashAccountSelectFragment extends SelectAccoutFragment<CashAccountSelectContract.Presenter>
        implements CashAccountSelectContract.View {

    private AccountBean mAccountBean;
    public static final String ACCOUNT_SELECT_BALANCE = "AccountBalance";
    private String accountIdPre = "1";

    public static CashAccountSelectFragment newInstance(ArrayList<String> typelist) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST, typelist);
        CashAccountSelectFragment fragment = new CashAccountSelectFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected CashAccountSelectContract.Presenter initPresenter() {
        return new CashAccountSelectPresenter(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAccountBean = accountListAdapter.getItem(position).getAccountBean();
        //判断是否为107的卡
        if(mAccountBean.getAccountType() != ApplicationConst.ACC_TYPE_SINGLEWAIBI) {
            //判断是否和上次选择一致
            if(!mAccountBean.getAccountId().equals(accountIdPre)) {
                showLoadingDialog();
                getPresenter().cashSelectAccount(mAccountBean);
            }
            else
                pop();
        }else{
            showErrorDialog(getString(R.string.boc_crcd_credit_card_not_107));
        }
    }

    @Override
    public void onQueryCashDivBalanceSuccess(String availableBalance){
        closeProgressDialog();
        if(Float.valueOf(availableBalance) >= 500.0f){
            Bundle bundle = new Bundle();
            bundle.putParcelable(ACCOUNT_SELECT, mAccountBean);
            bundle.putString(ACCOUNT_SELECT_BALANCE, availableBalance);
            setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundle);
            accountIdPre = mAccountBean.getAccountId();
            pop();
        }
        else{
            showErrorDialog(getString(R.string.boc_crcd_div_balance_limit));
        }
    }
}
