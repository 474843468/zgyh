package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitType;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter.LimitContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter.LimitPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.limit.LimitSetFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;

/**
 * @author wangyang
 *         2016/10/11 10:12
 *         借记卡限额设置
 */
@SuppressLint("ValidFragment")
public class AccountLimitFragment extends BaseAccountFragment<LimitPresenter> implements LimitContract.LimitView, View.OnClickListener {

    private final int GO_PASSWORD = 1;

    private final int GO_BORDER = 2;

    /**
     * 借记卡账户
     */
    private AccountBean accountBean;
    /**
     * 当前账户
     */
    private SelectAccountButton btnAccount;
    /**
     * 限额设置,小额/凭签名免密,境外磁条交易
     */
    private EditChoiceWidget etLimit, etPassword, etBorder;

    public AccountLimitFragment(AccountBean accountBean) {
        this.accountBean = accountBean;
    }

    private int mode;

    /**
     * 初始化布局
     *
     * @param mInflater
     * @return
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_account_limit, null);
    }

    /**
     * 修改标题文字
     *
     * @return
     */
    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_limit_title);
    }

    @Override
    protected LimitPresenter initPresenter() {
        return new LimitPresenter(this);
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView() {
        btnAccount = (SelectAccountButton) mContentView.findViewById(R.id.btn_account);
        etLimit = (EditChoiceWidget) mContentView.findViewById(R.id.et_limit);
        etPassword = (EditChoiceWidget) mContentView.findViewById(R.id.et_password);
        etBorder = (EditChoiceWidget) mContentView.findViewById(R.id.et_border);

        etPassword.getChoiceContentTextView().setTextColor(getResources().getColor(R.color.boc_text_color_red));
        etBorder.getChoiceContentTextView().setTextColor(getResources().getColor(R.color.boc_text_color_red));
    }

    @Override
    public void setListener() {
        etLimit.setOnClickListener(this);
        etPassword.setOnClickListener(this);
        etBorder.setOnClickListener(this);
    }

    @Override
    public void initData() {
        btnAccount.setData(accountBean);

        requestData();
    }

    private void requestData() {
        isCanCloseLoadingDialog = false;
        showLoadingDialog();
        getPresenter().queryLimit(accountBean, LimitType.LIMIT_PASSWORD);
        getPresenter().queryLimit(accountBean, LimitType.LIMIT_BORDER);
    }

    @Override
    public void onClick(View v) {
        if (v == etLimit)
            start(new LimitSetFragment(ModelUtil.generateQuotaModel(accountBean)));
        else if (v == etBorder) {
            mode = GO_BORDER;
            goLimitFragment(etBorder.getTag(), etPassword.getTag());
        } else {
            mode = GO_PASSWORD;
            goLimitFragment(etPassword.getTag(), etBorder.getTag());
        }
    }

    private void goLimitFragment(Object mainModel, Object otherModel) {
        if (mainModel == null || otherModel == null) {
            requestData();
            return;
        }

        start(new LimitFragment((LimitModel) mainModel, (LimitModel) otherModel));
    }

    private void setServiceStatus(EditChoiceWidget editText, LimitModel limitModel) {
        if (isCurrentFragment())
            editText.setChoiceTextContent(limitModel.getStatusString());
        editText.setTag(limitModel);
    }

    @Override
    public void queryLimit(LimitModel limitModel) {
        if (limitModel == null) {
            closeProgressDialog();
            return;
        }

        switch (limitModel.getLimitType()) {
            case PASSWORD:
            case SMALL:
                setServiceStatus(etPassword, limitModel);
                break;
            case BORDER:
                setServiceStatus(etBorder, limitModel);
                break;
        }

        if (isCanCloseLoadingDialog == true && mode > 0) {
            switch (mode) {
                case GO_BORDER:
                    goLimitFragment(etBorder.getTag(), etPassword.getTag());
                    break;
                case GO_PASSWORD:
                    goLimitFragment(etPassword.getTag(), etBorder.getTag());
                    break;
            }
            mode = 0;
        }
        closeProgressDialog();
    }
}
