package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.ChangeCardReqModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter.FundChangeCardPresenter;

import java.util.ArrayList;

/**
 * 基金-账户管理-基金交易账户-变更资金帐户页面
 * Created by lyf7084 on 2016/12/08.
 */

public class FundChangeCardFragment
        extends SelectAccoutFragment<FundChangeCardContract.Presenter>
        implements FundChangeCardContract.View {

    private View rootView;
    private AccountBean mAccountBean;    // 账户样式组件须设置AccountBean

    /*  接收前一页面的数据 */
    public static FundChangeCardFragment newInstanceWithData(ArrayList<AccountBean> accountBeanList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SelectAccoutFragment.ACCOUNT_LIST, accountBeanList);
        FundChangeCardFragment fragment = new FundChangeCardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected FundChangeCardContract.Presenter initPresenter() {
        return new FundChangeCardPresenter(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showLoadingDialog();
        mAccountBean = accountListAdapter.getItem(position).getAccountBean();
        ChangeCardReqModel reqModel = new ChangeCardReqModel();
        reqModel.setNewAccountId(mAccountBean.getAccountId());
        getPresenter().queryChangeCard(reqModel);
    }

    @Override
    public void queryChangeCardSuccess() {
        closeProgressDialog();
//        用Bundle将新账户ID传给账户管理页面通知刷新
//        Bundle bundle = new Bundle();
//        bundle.putString(DataUtils.KEY_New_ACCOUNT_ID, mAccountBean.getAccountId());
//        AccountManagementFragment fragment = new AccountManagementFragment();
//        fragment.setArguments(bundle);
//        start(fragment);
        Toast.makeText(mContext, "资金帐户变更成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void queryChangeCardFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        Toast.makeText(mContext, "资金帐户变更失败", Toast.LENGTH_LONG).show();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_change2);
    }

    @Override
    public boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

}