package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.adapter.OpenAccountBankAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.BankEntity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransQueryExternalBankInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.presenter.ChooseOpenBankPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择开户行
 * Created by zhx on 2016/7/21
 */
public class ChooseOpenAcountBankFragment extends BussFragment implements ChooseOpenBankContact.View {
    private View mRootView;
    private BankEntity bankEntity;

    private PsnTransQueryExternalBankInfoViewModel viewModel;
    private int mCurrentIndex = 0;
    private ChooseOpenBankPresenter chooseOpenBankPresenter;

    private List<PsnTransQueryExternalBankInfoViewModel.OpenBankBean> viewList = new ArrayList<PsnTransQueryExternalBankInfoViewModel.OpenBankBean>();
    private OpenAccountBankAdapter mAdapter;
    private TextView tv_search;
    private EditText et_search_content;
    private PullToRefreshLayout prl_refresh_layout;
    private PullableListView plv_pull;
    private TextView tv_search_no_data;
    private boolean isLoadMore;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_choose_open_account_bank, null);
        return mRootView;
    }

    @Override
    public void initView() {
        tv_search = (TextView) mRootView.findViewById(R.id.tv_search);
        et_search_content = (EditText) mRootView.findViewById(R.id.et_search_content);
        prl_refresh_layout = (PullToRefreshLayout) mRootView.findViewById(R.id.prl_refresh_layout);
        plv_pull = (PullableListView) mRootView.findViewById(R.id.plv_pull);
        tv_search_no_data = (TextView) mRootView.findViewById(R.id.tv_search_no_data);

    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        bankEntity = bundle.getParcelable("bank");

        // 显示Loading条
        showLoadingDialog();
        viewModel = new PsnTransQueryExternalBankInfoViewModel();
        viewModel.setToBankCode(bankEntity.getBankBtp()); // 此处的问题导致了不能获取银行列表
        if ("江苏银行股份有限公司".equals(bankEntity.getBankName())) {
            viewModel.setBankName("江苏银行");
        } else {
            viewModel.setBankName(""); // 此处的问题导致了不能获取银行列表
        }
        viewModel.setPageSize(15);
        viewModel.setCurrentIndex(mCurrentIndex);
        chooseOpenBankPresenter = new ChooseOpenBankPresenter(this);
        chooseOpenBankPresenter.psnTransQueryExternalBankInfo(viewModel);
    }

    @Override
    public void setListener() {
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bankName = et_search_content.getText().toString().trim();
                viewModel.setBankName(bankName); // 此处的问题导致了不能获取银行列表
                mCurrentIndex = 0;
                viewModel.setCurrentIndex(mCurrentIndex);
                viewModel.getList().clear();
                mAdapter.notifyDataSetChanged();
                showLoadingDialog("加载中...");
                chooseOpenBankPresenter.psnTransQueryExternalBankInfo(viewModel);
            }
        });

        prl_refresh_layout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (viewModel != null) {
                    if (viewList.size() < viewModel.getRecordnumber()) {
                        mCurrentIndex = viewList.size();
                        viewModel.setCurrentIndex(mCurrentIndex);
                        viewModel.getList().clear();
                        isLoadMore = true;
                        chooseOpenBankPresenter.psnTransQueryExternalBankInfo(viewModel);
                    } else {
                        prl_refresh_layout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    // 暂时什么也不做
                }
            }
        });
        plv_pull.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PsnTransQueryExternalBankInfoViewModel.OpenBankBean mCurrentOpenBankBean = viewList.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("openBank", mCurrentOpenBankBean);
                setFramgentResult(Activity.RESULT_OK, bundle);
                pop();
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return "选择开户行";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void psnTransQueryExternalBankInfoSuccess(PsnTransQueryExternalBankInfoViewModel viewModel) {
        closeProgressDialog();
        prl_refresh_layout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);

        if (mAdapter == null) {
            viewList.addAll(viewModel.getList());
            mAdapter = new OpenAccountBankAdapter(mActivity, viewList);
            plv_pull.setAdapter(mAdapter);
        } else {
            if (isLoadMore) {
            } else {
                viewList.clear();
            }
            viewList.addAll(viewModel.getList());
            mAdapter.notifyDataSetChanged();

            if (isLoadMore) {
                plv_pull.setSelection(viewList.size() - 1);
            } else {
                if (viewList.size() > 0) {
                    plv_pull.setSelection(0);
                }
            }
        }

        isLoadMore = false;

        // ----设置“无结果”字样的显示与否----
        if (viewList.size() == 0) {
            tv_search_no_data.setVisibility(View.VISIBLE);
        } else {
            tv_search_no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onBack() {
        hideSoftInput();
        return super.onBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideSoftInput();
    }

    @Override
    public void psnTransQueryExternalBankInfoFailed(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(ChooseOpenBankContact.Presenter presenter) {
    }
}
