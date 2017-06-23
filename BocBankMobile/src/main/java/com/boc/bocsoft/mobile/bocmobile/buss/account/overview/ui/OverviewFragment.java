package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.OnRefreshListener;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui.FinanceTransferDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.adapter.OverviewAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter.OverviewContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter.OverviewPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.ModuleDispatcher;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.DictKey;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import java.util.List;

/**
 * @author wangyang
 *         16/8/10 17:56
 *         账户概览
 */
public class OverviewFragment extends BaseOverviewFragment<OverviewPresenter> implements OverviewContract.Overview, OnRefreshListener, AdapterView.OnItemClickListener {

    /**
     * 账户列表控件
     */
    private ListView lvAccount;
    private OverviewAdapter adapter;

    @Override
    protected OverviewPresenter initPresenter() {
        return new OverviewPresenter(this);
    }

    protected OverviewPresenter getOverviewPresenter() {
        OverviewPresenter presenter = getPresenter();
        startPresenter();
        return presenter;
    }

    public OverviewAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_overview, null);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    @Override
    protected View getTitleBarView() {
        TitleBarView titleBarView = (TitleBarView) super.getTitleBarView();
        titleBarView.setRightButton(getResources().getDrawable(R.drawable.boc_overview_more));
        return titleBarView;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_overview_title);
    }

    @Override
    public void initView() {
        lvAccount = (ListView) mContentView.findViewById(R.id.lv_account);
    }

    @Override
    public void setListener() {
        lvAccount.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        //获取所有账户
        List<AccountBean> accountList = ApplicationContext.getInstance().getChinaBankAccountList(null);

        if (accountList== null || accountList.isEmpty())
            return;

        //初始化RecycelView,并设置Adapter
        adapter = new OverviewAdapter(lvAccount, mContext);
        lvAccount.setAdapter(adapter);

        //设置账户数据,加载余额
        adapter.setDatas(ModelUtil.generateAccountListItemViewModels(getActivity(), accountList));
        adapter.setOnRefreshListener(this);

        String limitString = BocCloudCenter.getInstance().getDirt(DictKey.QUERYACCLIMIT);

        if (!StringUtils.isEmptyOrNull(limitString)) {
            int limit = Integer.parseInt(limitString);

            if (limit < AccountTypeUtil.getCanQueryDetailCount())
                return;
        }

        adapter.loadAmountInfo();
        //请求接口查询余额
        getOverviewPresenter().queryAccountDetail(accountList);
    }

    @Override
    public void reInit() {
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPresenter();
    }

    @Override
    protected void titleRightIconClick() {
        startForResult(new MoreFragment(getMedicalAccount()), REQUEST_CODE_REFRESH_FRAGMENT);
    }

    @Override
    public void onRefresh(int position, AccountListItemViewModel item) {
        getOverviewPresenter().queryAccountDetail(item.getAccountBean());
    }

    @Override
    public void queryAccountDetail(AccountListItemViewModel model) {
        if (model != null)
            adapter.updateItem(model);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AccountBean accountBean = adapter.getItem(position).getAccountBean();

        switch (accountBean.getAccountType()) {
            //信用卡
            case ApplicationConst.ACC_TYPE_ZHONGYIN:
            case ApplicationConst.ACC_TYPE_GRE:
            case ApplicationConst.ACC_TYPE_SINGLEWAIBI:
                ModuleDispatcher.gotoCardmessageAndSetActivity(mActivity, ModelUtil.generateCredit2Map(), accountBean.getAccountId());
                break;
            //电子现金账户
            case ApplicationConst.ACC_TYPE_ECASH:
                startForResult(new FinanceTransferDetailFragment(ModelUtil.generateFinanceModel(accountBean), true), REQUEST_CODE_REFRESH_FRAGMENT);
                break;
            //定期账户
            case ApplicationConst.ACC_TYPE_CBQX:
            case ApplicationConst.ACC_TYPE_ZOR:
            case ApplicationConst.ACC_TYPE_EDU:
            case ApplicationConst.ACC_TYPE_REG:
                start(new RegularFragment(accountBean));
                break;
            //虚拟卡
            case ApplicationConst.ACC_TYPE_XNCRCD1:
            case ApplicationConst.ACC_TYPE_XNCRCD2:
                start(new VirtualDetailFragment(accountBean));
                break;
            //活期账户
            default:
                startForResult(new CurrentFragment(adapter.getItem(position)), REQUEST_CODE_REFRESH_FRAGMENT);
                break;
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQUEST_CODE_REFRESH_FRAGMENT)
            initData();
    }
}
