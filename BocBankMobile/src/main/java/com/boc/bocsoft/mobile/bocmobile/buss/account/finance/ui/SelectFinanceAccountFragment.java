package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.AccountListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangyang
 *         16/7/5 14:43
 *         选择电子现金账户
 */
@SuppressLint("ValidFragment")
public class SelectFinanceAccountFragment extends BussFragment {

    /**
     * 电子现金账户转出类型
     */
    public static final String[] ACCOUNT_EXPEND_TYPE = {ApplicationConst.ACC_TYPE_BRO, ApplicationConst.ACC_TYPE_ZHONGYIN, ApplicationConst.ACC_TYPE_GRE};

    /**
     * 电子现金账户签约账户类型
     */
    private final String[] ACCOUNT_SIGN_TYPE = {ApplicationConst.ACC_TYPE_BRO, ApplicationConst.ACC_TYPE_ZHONGYIN, ApplicationConst.ACC_TYPE_GRE, ApplicationConst.ACC_TYPE_SINGLEWAIBI};

    /**
     * 转入账户RequestCode
     */
    public static final int SELECT_ACCOUNT_INCOME = 1;
    /**
     * 转出账户RequestCode
     */
    public static final int SELECT_ACCOUNT_EXPEND = 2;
    /**
     * 签约账户RequestCode
     */
    public static final int SELECT_ACCOUNT_SIGN = 3;
    private FinanceModel mFinanceModel;
    /**
     * 页面跳转数据传递
     */
    public static final String ACCOUNT_SELECT = "AccountBean";
    public static final int RESULT_CODE_SELECT_ACCOUNT = 100;

    private int requestCode;

    private List<AccountBean> accountBeanList;

    public SelectFinanceAccountFragment(List<AccountBean> accountBeans) {
        super();
        this.accountBeanList = accountBeans;
        this.requestCode = SELECT_ACCOUNT_INCOME;
    }

    public SelectFinanceAccountFragment(int requestCode) {
        this.requestCode = requestCode;
    }

    public SelectFinanceAccountFragment(int requestCode,FinanceModel financeModel) {
        this.requestCode = requestCode;
        this.mFinanceModel = financeModel;
    }

    private View mRootView;
    private TextView tvAmountChoice;
    private TextView tvCenter;
    private ListView mListView;
    /**
     * 账户列表的adapter
     */
    private FinanceAccountListAdapter accountListAdapter;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_transdetail_selectaccount, null);
        return mRootView;
    }

    @Override
    public void initView() {
        mListView = (ListView) mRootView.findViewById(R.id.lv_transdetail_selectaccount);
        tvAmountChoice = (TextView) mRootView.findViewById(R.id.tv_amount_choice);
        tvCenter = (TextView) mRootView.findViewById(R.id.tv_center);
        tvAmountChoice.setVisibility(View.VISIBLE);
        if (requestCode == SELECT_ACCOUNT_INCOME) {
            tvAmountChoice.setText(getString(R.string.boc_overview_more_finance_change));
            tvAmountChoice.setPadding(getResources().getDimensionPixelSize(R.dimen.boc_space_between_40px), getResources().getDimensionPixelSize(R.dimen.boc_space_between_40px), getResources().getDimensionPixelSize(R.dimen.boc_space_between_px), getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px));
            mListView.setDividerHeight(getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px));
        } else if (requestCode == SELECT_ACCOUNT_SIGN) {
            tvAmountChoice.setText(getString(R.string.boc_finance_account_transfer_detail_sign_create));
        } else {
            tvAmountChoice.setText(getString(R.string.boc_account_select_out_account));
        }

        accountListAdapter = new FinanceAccountListAdapter(mContext);
        accountListAdapter.setAmountInfoVisible(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ACCOUNT_SELECT, accountListAdapter.getItem(position).getAccountBean());
                setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundle);
                if (requestCode == SELECT_ACCOUNT_INCOME){
                    if (position < accountListAdapter.getCount() - 1){
                        AccountBean bean = accountListAdapter.getItem(position).getAccountBean();
                        FinanceModel financeModel = new FinanceModel();
                        financeModel.setAccountId(bean.getAccountId());
                        financeModel.setFinanICNumber(bean.getAccountNumber());
                        financeModel.setFinanICType(bean.getAccountType());
                        start(new FinanceTransferSelfFragment(financeModel));
                    }else{
                        start(new FinanceTransferOtherFragment());
                    }
                    return;
                }
                if (requestCode == SELECT_ACCOUNT_SIGN){
                    AccountBean bean = accountListAdapter.getItem(position).getAccountBean();
                    mFinanceModel.setBankAccountId(bean.getAccountId());
                    mFinanceModel.setBankAccountNumber(bean.getAccountNumber());
                    start(new FinanceSignConfirmFragment(mFinanceModel));
                    return;
                }
                pop();
            }
        });

        mListView.setAdapter(accountListAdapter);
//        if (accountBeanList == null && requestCode == SELECT_ACCOUNT_SIGN) {
//            tvCenter.setText(getString(R.string.boc_account_select_account_sign));
//            mListView.setVisibility(View.GONE);
//            tvAmountChoice.setVisibility(View.GONE);
//            tvCenter.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    protected String getTitleValue() {
        if (requestCode == SELECT_ACCOUNT_INCOME) {
            return getString(R.string.boc_overview_more_finance_recharge);
        } else if (requestCode == SELECT_ACCOUNT_SIGN) {
            return getString(R.string.boc_finance_account_transfer_detail_sign_build);
        }
        return  getString(R.string.boc_account_select_account);
    }

    public static List<AccountBean> getAllFinance(){
        List<AccountBean> beans = ApplicationContext.getInstance().getChinaBankAccountList(null);
        List<AccountBean> finances = new ArrayList<>();
        for (AccountBean accountBean:beans) {
            if(accountBean.getAccountType().equals(ApplicationConst.ACC_TYPE_ECASH)){
                finances.add(accountBean);
                continue;
            }
            if("1".equals(accountBean.getIsECashAccount())){
                finances.add(accountBean);
                continue;
            }
        }
        return finances;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void initData() {
        switch (requestCode) {
            case SELECT_ACCOUNT_INCOME:
                //选择电子现金转出账户
                accountBeanList = getAllFinance();
                break;
            case SELECT_ACCOUNT_EXPEND:
                //选择电子现金转出账户
                accountBeanList = ApplicationContext.getInstance().getChinaBankAccountList(Arrays.asList(ACCOUNT_EXPEND_TYPE));
                break;
            case SELECT_ACCOUNT_SIGN:
                //选择电子现金签约账户
                accountBeanList = ApplicationContext.getInstance().getChinaBankAccountList(Arrays.asList(ACCOUNT_SIGN_TYPE));
                break;
        }
        accountListAdapter.setDatas(buildAccountModel(accountBeanList));
    }


    /**
     * 将AccountBean的List转换成AccountListItemViewModel的List
     *
     * @param accountBeanList
     * @return
     */
    private List<AccountListItemViewModel> buildAccountModel(List<AccountBean> accountBeanList) {
        List<AccountListItemViewModel> viewModelList = new ArrayList<AccountListItemViewModel>();
        for (int i = 0; i < accountBeanList.size(); i++) {
            AccountListItemViewModel listItemViewModel = new AccountListItemViewModel();
            listItemViewModel.setAccountBean(accountBeanList.get(i));

            viewModelList.add(listItemViewModel);
        }
        return viewModelList;
    }

    @Override
    public void setListener() {
        super.setListener();
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    private class FinanceAccountListAdapter extends AccountListAdapter {

        public FinanceAccountListAdapter(Context context) {
            super(context);
        }

        @Override
        public int getCount() {
            //如果为转入账户,添加一条数据
            if (requestCode == SELECT_ACCOUNT_INCOME)
                return super.getCount() + 1;

            return super.getCount();
        }

        @Override
        public AccountListItemViewModel getItem(int position) {
            //如果为转入账户,添加一条数据
            if (requestCode == SELECT_ACCOUNT_INCOME && position == getCount() - 1) {
                AccountListItemViewModel model = new AccountListItemViewModel();
                AccountBean accountBean = new AccountBean();
                accountBean.setAccountNumber(getString(R.string.boc_finance_account_recharge_other));
                model.setAccountBean(accountBean);
                return model;
            }
            return super.getItem(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (requestCode == SELECT_ACCOUNT_INCOME) {
                if (position < getCount() - 1) {
                    SelectAccountButton accountListItemView = convertView == null ? new SelectAccountButton(mContext)
                            : (SelectAccountButton) convertView;
                    AccountListItemViewModel viewModel = getItem(position);
                    accountListItemView.setData(viewModel.getAccountBean());
                    accountListItemView.setArrowVisible(true);
                    return accountListItemView;
                } else {
                    convertView = View.inflate(mContext, R.layout.boc_button_select_finance, null);
                    TextView tvTips = (TextView) convertView.findViewById(R.id.tv_tips);
                    AccountListItemViewModel viewModel = getItem(position);
                    tvTips.setText(viewModel.getAccountBean().getAccountNumber());
                    return convertView;
                }

            }
            return super.getView(position, convertView, parent);
        }
    }
}