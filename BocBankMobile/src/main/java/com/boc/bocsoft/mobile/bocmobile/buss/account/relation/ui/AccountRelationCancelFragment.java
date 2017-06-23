package com.boc.bocsoft.mobile.bocmobile.buss.account.relation.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.relation.presenter.AccountRelationContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.relation.presenter.AccountRelationPresenter;

/**
 * @author wangyang
 *         16/7/7 10:44
 *         取消关联
 */
@SuppressLint("ValidFragment")
public class AccountRelationCancelFragment extends BaseAccountFragment<AccountRelationPresenter> implements AccountRelationContract.AccountRelationView, View.OnClickListener, TitleAndBtnDialog.DialogBtnClickCallBack {

    /**
     * 选择取消关联账户
     */
    private AccountBean accountBean;

    private SelectAccountButton btnAccount;

    private Button btnOk;
    /**
     * 取消关联对话框
     */
    private TitleAndBtnDialog cancelDialog;

    private boolean isCanSelectAccount;


    /**
     * 如果AccountBean不为空,从账户详情跳转进来,不能选择其他账户。否则是从更多跳转进来,可以选择账户,默认为账户列表第一个
     *
     * @param accountBean
     */
    public AccountRelationCancelFragment(AccountBean accountBean) {
        this.accountBean = accountBean;
        if (accountBean == null)
            this.isCanSelectAccount = true;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_account_relation_cancel, null);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_overview_account_more_cancel_relation);
    }

    @Override
    protected AccountRelationPresenter initPresenter() {
        return new AccountRelationPresenter(this);
    }

    @Override
    public void initView() {
        btnAccount = (SelectAccountButton) mContentView.findViewById(R.id.btn_account);
        btnOk = (Button) mContentView.findViewById(R.id.btn_ok);
    }

    @Override
    public void setListener() {
        if (isCanSelectAccount)
            btnAccount.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (isCanSelectAccount && accountBean == null)
            accountBean = ApplicationContext.getInstance().getChinaBankAccountList(null).get(0);
        btnAccount.setData(accountBean);
        if (accountBean != null) {
            btnAccount.setArrowVisible(false);
        } else {
            btnAccount.setArrowVisible(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_account) {
            startForResult(new SelectAccoutFragment(), SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
        } else if (v.getId() == R.id.btn_ok) {
            if (cancelDialog == null) {
                cancelDialog = new TitleAndBtnDialog(getActivity());
                cancelDialog.setBtnName(new String[]{"我再想想", "取消关联"});
                cancelDialog.setNoticeContent(getString(R.string.boc_account_relation_cancel_notice));
                cancelDialog.setTitle(getString(R.string.boc_account_relation_cancel_dialog_notice));
                cancelDialog.setTitleBackground(getResources().getColor(R.color.boc_common_cell_color));
                cancelDialog.setGravity(Gravity.CENTER_VERTICAL);
                cancelDialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                        getResources().getColor(R.color.boc_common_cell_color),
                        getResources().getColor(R.color.boc_common_cell_color),
                        getResources().getColor(R.color.boc_text_color_red));
                cancelDialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                        getResources().getColor(R.color.boc_text_color_red),
                        getResources().getColor(R.color.boc_text_color_red),
                        getResources().getColor(R.color.boc_common_cell_color));
                cancelDialog.isShowTitle(true);
                cancelDialog.setDialogBtnClickListener(this);
            }
            cancelDialog.show();
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode != SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT)
            return;

        accountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
        btnAccount.setData(accountBean);
        btnAccount.setArrowVisible(false);
    }

    @Override
    public void cancelAccountRelation() {
        closeProgressDialog();
        start(new AccountRelationCancelResultFragment(accountBean));
    }

    /**
     * 取消对话框,取消按钮
     *
     * @param view
     */
    @Override
    public void onLeftBtnClick(View view) {
        cancelDialog.dismiss();
    }

    /**
     * 取消对话框,确认按钮
     *
     * @param view
     */
    @Override
    public void onRightBtnClick(View view) {
        cancelDialog.dismiss();

        //取消签约
        showLoadingDialog();
        getPresenter().cancelAccountRelation(accountBean);
    }
}
