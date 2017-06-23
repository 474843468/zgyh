package com.boc.bocsoft.mobile.bocmobile.buss.account.apply.ui;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.model.ApplyAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.presenter.ApplyContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.presenter.ApplyPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.MoreAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.MoreFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.List;

/**
 * 活期一本通
 * Created by liuyang on 2016/6/7.
 */
@SuppressLint("ValidFragment")
public class AccountApplyFragment extends BaseAccountFragment<ApplyPresenter> implements ApplyContract.QueryAccountView, OnClickListener, MClickableSpan.OnClickSpanListener {

    private final String COUNTRY_CODE_CHINA = "CN";

    //内容页,无数据页,在中国开立账户原因页面
    private ViewGroup svContent, rlNoData, llReason;
    //选择账户组件
    private EditChoiceWidget choiceAccount;
    //选择账户用途,在中国开立账户原因
    private SelectGridView gvPurpose, gvReason;
    //勾选的checkBox
    private CheckBox cbAgreement;
    //服务须知
    private SpannableString tvAgreement;
    //确认按钮
    private Button btnOk;
    //账户数据
    private AccountBean accountSelectBean;
    //申请账户类型
    private int applyType;
    private boolean isCanChangeAccount;

    public AccountApplyFragment(AccountBean accountBean, int applyType) {
        this.applyType = applyType;
        this.accountSelectBean = accountBean;
        if (accountBean == null)
            isCanChangeAccount = true;
    }

    /**
     * 初始化布局
     *
     * @param mInflater
     * @return
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_account_card, null);
    }

    /**
     * 修改标题文字
     *
     * @return
     */
    @Override
    protected String getTitleValue() {
        switch (applyType) {
            case ApplyAccountModel.APPLY_TYPE_CURRENT:
                return getString(R.string.boc_apply_currents);
            case ApplyAccountModel.APPLY_TYPE_REGULAR:
                return getString(R.string.boc_apply_regulars);
        }
        return null;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView() {
        svContent = (ViewGroup) mContentView.findViewById(R.id.sv_content);
        rlNoData = (ViewGroup) mContentView.findViewById(R.id.rl_no_data);
        llReason = (ViewGroup) mContentView.findViewById(R.id.ll_reason);
        choiceAccount = (EditChoiceWidget) mContentView.findViewById(R.id.choice_account);
        gvPurpose = (SelectGridView) mContentView.findViewById(R.id.gv_purpose);
        gvReason = (SelectGridView) mContentView.findViewById(R.id.gv_reason);
        cbAgreement = (CheckBox) mContentView.findViewById(R.id.cb_agreement);
        tvAgreement = (SpannableString) mContentView.findViewById(R.id.tv_agreement);
        btnOk = (Button) mContentView.findViewById(R.id.btn_ok);

        if (accountSelectBean != null)
            choiceAccount.setArrowImageGone(false);
        else
            //筛选账户
            choiceAccount.setOnClickListener(this);
    }

    @Override
    protected ApplyPresenter initPresenter() {
        return new ApplyPresenter(this);
    }

    /**
     * 事件监听
     */
    @Override
    public void setListener() {
        //确认信息
        btnOk.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        if (accountSelectBean == null) {
            List<AccountBean> accountBeans = ApplicationContext.getInstance().getChinaBankAccountList(AccountTypeUtil.getBroType());

            //如果没有借记卡,则提示用户
            if (accountBeans.isEmpty()) {
                svContent.setVisibility(View.GONE);
                rlNoData.setVisibility(View.VISIBLE);
                return;
            }

            accountSelectBean = accountBeans.get(0);
        }

        //账户用途信息
        gvPurpose.setData(ModelUtil.generatePurposeContent(mContext));

        //服务须知
        tvAgreement.setContent(getString(R.string.boc_apply_agree), getString(R.string.boc_service_bureau), this);

        //设置账号
        initChoiceAccount();

        //查询国籍信息
        queryCountryCode();
    }

    /**
     * 设置选择借记卡账户
     */
    private void initChoiceAccount() {
        if (accountSelectBean == null)
            return;

        choiceAccount.getChoiceContentTextView().setText(NumberUtils.formatCardNumber(accountSelectBean.getAccountNumber()));
    }

    /**
     * 初始化,开立账户原因
     */
    private void initReasonGridView() {
        llReason.setVisibility(View.VISIBLE);
        gvReason.setData(ModelUtil.generateReasonContent());
    }

    /**
     * 查询客户国籍信息
     */
    private void queryCountryCode() {
        if (accountSelectBean == null)
            return;

        //显示对话框,发起请求
        showLoadingDialog();
        getPresenter().queryCountryCode(accountSelectBean.getAccountId());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_point) {
            //do nothing
        } else if (v.getId() == R.id.choice_account) {
            //选择账户
            startForResult(SelectAccoutFragment.newInstance(AccountTypeUtil.getBroType()), SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
        } else if (v.getId() == R.id.btn_ok) {
            //生成选择账户用途
            StringBuilder builder = new StringBuilder();
            StringBuilder purposeBuilder = new StringBuilder();
            for (Content content : gvPurpose.getList()) {
                if (content.getSelected()) {
                    builder.append(Content.SELECTED);
                    purposeBuilder.append(content.getName() + ",");
                } else {
                    builder.append(Content.NOT_SELECTED);
                }
            }

            //检验是否选择用途
            if (!builder.toString().contains(Content.SELECTED)) {
                showErrorDialog(getString(R.string.boc_account_purpose_hint));
                return;
            }

            //检验是否选择在中国开户原因
            if (llReason.getVisibility() == View.VISIBLE && !gvReason.getAdapter().getItem(0).getSelected()) {
                showErrorDialog(getString(R.string.boc_account_purpose_china_hint));
                return;
            }
            boolean isChina = llReason.getVisibility() != View.VISIBLE;

            //检验是否勾选了须知
            if (!cbAgreement.isChecked()) {
                showErrorDialog(getString(R.string.boc_account_service_bureau_hint));
                return;
            }
            AccountApplyConfirmFragment fragment = new AccountApplyConfirmFragment(ModelUtil.generateApplyAccountModel(mContext, accountSelectBean, builder.toString(), purposeBuilder.toString(), isChina, applyType));
            fragment.setCallBack(callBack);
            start(fragment);

        }
    }

    /**
     * 跳转至须知页面
     */
    @Override
    public void onClickSpan() {
        start(new ServiceBureauFragment(true, ApplyAccountModel.APPLY_ENTY));
    }

    @Override
    public boolean onBack() {
        if (isCanChangeAccount)
            popTo(MoreFragment.class, false);
        else
            popTo(MoreAccountFragment.class, false);
        return false;
    }

    @Override
    public boolean onBackPress() {
        if (isCanChangeAccount)
            popTo(MoreFragment.class, false);
        else
            popTo(MoreAccountFragment.class, false);
        return true;
    }

    /**
     * 账户返回数据
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        Result数据
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode != SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT)
            return;

        //获取返回借记卡信息,如果不是之前选择的,则设置卡号,请求国籍
        AccountBean accountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
        if (accountBean == accountSelectBean)
            return;

        accountSelectBean = accountBean;

        //设置借记卡账号
        initChoiceAccount();

        //查询国籍信息
        queryCountryCode();
    }

    @Override
    public void queryCountryCodeSuccess(String countryCode) {
        closeProgressDialog();

        if (StringUtils.isEmptyOrNull(countryCode) || COUNTRY_CODE_CHINA.equals(countryCode)) {
            llReason.setVisibility(View.GONE);
            return;
        }
        //初始化开户原因
        initReasonGridView();
    }

}
