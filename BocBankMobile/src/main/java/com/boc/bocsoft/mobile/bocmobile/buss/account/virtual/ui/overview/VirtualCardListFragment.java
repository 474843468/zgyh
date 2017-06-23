package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.adapter.VirtualCardAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirCardPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirtualCardContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.apply.ApplyVirtualCardFragment;

import java.util.List;

/**
 * @author wangyang
 *         16/7/19 17:24
 *         虚拟银行卡列表
 */
public class VirtualCardListFragment extends BaseAccountFragment<VirCardPresenter> implements View.OnClickListener, VirtualCardContract.VirCardView, AdapterView.OnItemClickListener {

    /**
     * 信用卡
     */
    private SelectAccountButton btnAccount;
    /**
     * 内容布局界面
     */
    private ViewGroup svContent;
    /**
     * 虚拟卡列表
     */
    private ListView lvAccount;
    /**
     * 虚拟卡列表Adapter
     */
    private VirtualCardAdapter adapter;
    /**
     * 虚拟卡数量Tv,无数据提示Tv
     */
    private TextView tvRecord, tvNoData;

    private AccountBean creditAccountBean;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_virtual_list, null);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_virtual_account_list_title);
    }

    @Override
    public void initView() {
        btnAccount = (SelectAccountButton) mContentView.findViewById(R.id.btn_account);
        svContent = (ScrollView) mContentView.findViewById(R.id.sv_content);
        lvAccount = (ListView) mContentView.findViewById(R.id.lv_account);
        tvRecord = (TextView) mContentView.findViewById(R.id.tv_record);
        tvNoData = (TextView) mContentView.findViewById(R.id.tv_no_data);
    }

    @Override
    public void setListener() {
        btnAccount.setOnClickListener(this);
        lvAccount.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        if (creditAccountBean == null) {
            //获取信用卡列表
            List<AccountBean> accountBeans = ApplicationContext.getInstance().getChinaBankAccountList(AccountTypeUtil.getCreditType());

            //没有信用卡,显示暂无虚拟银行卡
            if (accountBeans.isEmpty()) {
                svContent.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
                tvNoData.setText(getString(R.string.boc_virtual_credit_account_no_data));
                return;
            }

            //显示内容界面
            svContent.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);

            //设置信用卡
            creditAccountBean = accountBeans.get(0);
            btnAccount.setData(creditAccountBean);

            //设置Adapter
            adapter = new VirtualCardAdapter(getActivity());
            lvAccount.setAdapter(adapter);
        }

        //请求虚拟银行卡列表
        showLoadingDialog();
        getPresenter().psnVirtualVircardListQuery(creditAccountBean);
    }

    @Override
    public void reInit() {
        initData();
    }

    @Override
    protected VirCardPresenter initPresenter() {
        return new VirCardPresenter(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_account) {
            startForResult(SelectAccoutFragment.newInstance(AccountTypeUtil.getCreditType()), SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
        }
        if (v.getId() == R.id.btn_apply) {
            if (adapter.getCount() >= 20)
                showErrorDialog(getString(R.string.boc_virtual_account_apply_error));
            else
                start(new ApplyVirtualCardFragment(creditAccountBean, false));
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode != SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT)
            return;


        showLoadingDialog();
        creditAccountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
        getPresenter().psnVirtualVircardListQuery(creditAccountBean);
    }

    @Override
    public void virtualCardListQuery(List<VirtualCardModel> models) {
        closeProgressDialog();

        requestFocus(mTitleBarView);
        btnAccount.setData(creditAccountBean);
        if (models != null && !models.isEmpty()){
            tvNoData.setVisibility(View.GONE);
            adapter.setDatas(models);
            //显示虚拟卡记录数
            tvRecord.setVisibility(View.VISIBLE);
            tvRecord.setText(getContext().getString(R.string.boc_virtual_account_list_record, adapter.getCount()));
        }
        else{
            adapter.setDatas(null);
            tvRecord.setVisibility(View.INVISIBLE);
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText(getString(R.string.boc_virtual_account_list_no_data));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        start(new VirtualCardDetailFragment(adapter.getItem(position)));
    }
}
