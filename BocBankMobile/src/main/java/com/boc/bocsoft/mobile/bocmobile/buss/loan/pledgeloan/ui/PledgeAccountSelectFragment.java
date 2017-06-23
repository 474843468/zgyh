package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.PledgeLoanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.PledgeAccountSelectPresenter;
import com.boc.bocsoft.mobile.common.utils.LoggerUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：XieDu
 * 创建时间：2016/9/8 15:21
 * 描述：
 */
public class PledgeAccountSelectFragment
        extends SelectAccoutFragment<PledgeAccountSelectContract.Presenter>
        implements PledgeAccountSelectContract.View {

    private AccountBean mAccountBean;
    private boolean isPayee;
    private String mCurrencyCode;

    public static final String IS_PAYEE = "isPayee";
    public static final String CHECK_SIGN_ACCOUNT_RESULT = "checkResult";
    public static final String CONVERSATIONID = "conversationId";
    public static final String CURRENCYCODE = "currencycode";

    public static PledgeAccountSelectFragment newInstance(ArrayList<String> typelist,
            String conversationId,String currencyCode) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST, typelist);
        bundle.putString(CONVERSATIONID, conversationId);
        bundle.putString(CURRENCYCODE, currencyCode);
        PledgeAccountSelectFragment fragment = new PledgeAccountSelectFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setIsPayee(boolean isPayee) {
        this.isPayee = isPayee;
    }

    @Override
    public void initData() {
        LoggerUtils.Log(getClass().getSimpleName() + "  initData");
        super.initData();
        mCurrencyCode=getArguments().getString(CURRENCYCODE);
        getPresenter().setConversationId(getArguments().getString(CONVERSATIONID));
    }

    @Override
    protected PledgeAccountSelectContract.Presenter initPresenter() {
        return new PledgeAccountSelectPresenter(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAccountBean = accountListAdapter.getItem(position).getAccountBean();
        //检查签约
        showLoadingDialog();
        if (isPayee) {
            getPresenter().checkPayeeAccount(mAccountBean,mCurrencyCode);
        } else {
            getPresenter().checkPayerAccount(mAccountBean,mCurrencyCode);
        }
    }

    @Override
    public void onCheckPayeeAccountSuccess(List<String> checkResult) {
        closeProgressDialog();
        if ("01".equals(checkResult.get(0))) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ACCOUNT_SELECT, mAccountBean);
            setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundle);
            pop();
        } else {
            showErrorDialog(PledgeLoanConst.checkPayeeResultMap.get(checkResult.get(0)));
        }
    }

    @Override
    public void onCheckPayerAccountSuccess(List<String> checkResult) {
        closeProgressDialog();
        if ("01".equals(checkResult.get(0))) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ACCOUNT_SELECT, mAccountBean);
            setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundle);
            pop();
        } else {
            showErrorDialog(PledgeLoanConst.checkPayerResultMap.get(checkResult.get(0)));
        }
    }
}
