package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.presenter.SignAccountSelectPresenter;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import java.util.ArrayList;

public class SignAccountSelectFragment
        extends SelectAccoutFragment<SignAccountSelectContract.Presenter>
        implements SignAccountSelectContract.View {

    private AccountBean mAccountBean;
    private String mOldSignedAccount;

    public static final String CONVERSATIONID = "conversationId";
    public static final String OLD_SIGNED_ACCOUNT = "oldSignedAccount";
    public static final String LOAN_CURRENCY = "loanCurrency";

    public static SignAccountSelectFragment newInstance(ArrayList<String> typelist,
            String conversationId, String oldsignedAccount, String loanCurrencyCode) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST, typelist);
        bundle.putString(CONVERSATIONID, conversationId);
        bundle.putString(OLD_SIGNED_ACCOUNT, oldsignedAccount);
        bundle.putString(LOAN_CURRENCY, loanCurrencyCode);
        SignAccountSelectFragment fragment = new SignAccountSelectFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData() {
        super.initData();
        if (PublicUtils.isEmpty(accountListAdapter.getDatas())){
            showNoDataView(getString(R.string.boc_eloan_payment_sign_account_list_empty));
            return;
        }
        getPresenter().setInitData(getArguments().getString(CONVERSATIONID),
                getArguments().getString(LOAN_CURRENCY));
        mOldSignedAccount = getArguments().getString(OLD_SIGNED_ACCOUNT);
    }

    @Override
    protected SignAccountSelectContract.Presenter initPresenter() {
        return new SignAccountSelectPresenter(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAccountBean = accountListAdapter.getItem(position).getAccountBean();
        //检查签约
        if (mOldSignedAccount != null && mOldSignedAccount.equals(
                mAccountBean.getAccountNumber())) {
            backWithResult();
        } else {
            showLoadingDialog();
            getPresenter().checkAssignAccount(mAccountBean);
        }
    }

    @Override
    public void onCheckAssignAccountSuccess() {
        closeProgressDialog();
        backWithResult();
    }

    private void backWithResult() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACCOUNT_SELECT, mAccountBean);
        setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundle);
        pop();
    }
}