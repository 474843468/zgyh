package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.PartialLoadView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.adapter.FinanceAccountAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter.FinanceContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter.FinancePresenter;
import com.boc.bocsoft.mobile.framework.widget.listview.ListAdapterHelper;

import java.util.Arrays;
import java.util.List;

/**
 * @author wangyang
 *         16/6/20 16:35
 *         电子现金账户概览界面
 */
public class FinanceAccountViewFragment extends BaseAccountFragment<FinancePresenter> implements FinanceContract.FinanceAccountView, ListAdapterHelper.OnClickChildViewInItemItf<FinanceModel>, AdapterView.OnItemClickListener {

    /**
     * 账户列表控件
     */
    private ListView lvAccount;
    private FinanceAccountAdapter adapter;

    private TextView tvNoData;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_finance_account_view, null);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_finance_account_view_title);
    }

    @Override
    protected FinancePresenter initPresenter() {
        return new FinancePresenter(this);
    }

    @Override
    public void initView() {
        lvAccount = (ListView) mContentView.findViewById(R.id.lv_account);
        tvNoData = (TextView) mContentView.findViewById(R.id.tv_no_data);

        adapter = new FinanceAccountAdapter(getActivity());
        lvAccount.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        adapter.setOnClickChildViewInItemItf(this);
        lvAccount.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        showLoadingDialog();
        //请求账户概览列表
        getPresenter().psnFinanceAccountView();
    }

    /**
     * 账户概览列表ItemClick
     *
     * @param position
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == adapter.getCount() - 1) {
            //获取转出账户第一个账户作为默认值
            List<AccountBean> accountBeans = ApplicationContext.getInstance().getChinaBankAccountList(Arrays.asList(SelectFinanceAccountFragment.ACCOUNT_EXPEND_TYPE));
            if (accountBeans.isEmpty()) {
                showErrorDialog(getString(R.string.boc_finance_account_recharge_out_other));
                return;
            }
            start(new FinanceTransferOtherFragment());
            return;
        }

        FinanceModel financeModel = adapter.getFinanceModel(position);

        //跳转账户交易界面
        start(new FinanceTransferDetailFragment(financeModel, false));
    }

    @Override
    public void onClickChildViewInItem(int position, FinanceModel item, View childView) {
        if (childView.getId() != R.id.iv_loading)
            return;
        ((PartialLoadView) childView).setLoadStatus(PartialLoadView.LoadStatus.LOADING);
        getPresenter().psnFinanceICAccountDetail(item);
        ListAdapterHelper.setOnClickChildViewInItemItf(position, item, childView, this);
    }

    /**
     * 获取账户列表,设置Adapter
     *
     * @param models
     */
    @Override
    public void psnFinanceAccountView(List<FinanceModel> models) {
        closeProgressDialog();

        lvAccount.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);

        adapter.setDatas(models);

        if(!models.isEmpty()){
            startPresenter();
            getPresenter().psnFinanceICAccountDetail(models);
        }
    }

    /**
     * 局部更新账户Adapter
     *
     * @param financeModel
     */
    @Override
    public void psnFinanceAccountBalanceView(FinanceModel financeModel) {
        adapter.updateItemView(lvAccount,financeModel);
    }

    @Override
    public FinanceModel getFinanceModel(FinanceModel financeModel) {
        return adapter.getFinanceModel(financeModel);
    }
}
