package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.index.QuickIndexBar;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.FundRegCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter.SelectFundCoPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.adapter.FundCoAdapter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * TA账户登记-选择注册登记机构
 * Created by lyf7084 on 2016/12/23.
 */
public class SelectFundCoFragment extends MvpBussFragment<SelectFundCoContract.Presenter>
        implements SelectFundCoContract.View, ListView.OnItemClickListener {

    private View rootView;
    private SelectFundCoContract.Presenter mSelectFundCoPresenter;
    protected ListView lsvCompanyList;    // 基金公司列表
    protected QuickIndexBar quickIndexBar;     // 首字母快速索引栏

    private FundCoAdapter fundCoAdapter = null;    // 注册登记机构ListView适配器
    private FundRegCompanyModel viewModel = null;    // viewModel
    private Bundle bundle;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        viewModel = new FundRegCompanyModel();
        viewModel = (FundRegCompanyModel) getArguments().getSerializable("regCoModel");
        rootView = mInflater.inflate(R.layout.boc_fragment_select_fund_co, null);
        return rootView;
    }

    @Override
    public void initView() {
        lsvCompanyList = (ListView) rootView.findViewById(R.id.lsvCompanyList);
        lsvCompanyList.setOnItemClickListener(this);
        quickIndexBar = (QuickIndexBar) rootView.findViewById(R.id.quickIndexBar);
        quickIndexBar.setOnTouchLetterListener(new QuickIndexBar.OnTouchLetterListener() {

            @Override
            public void onTouchLetter(String word) {
                scrollToWord(word);
            }

            @Override
            public void onTouchUp() {

            }
        });
        updateViewModel(viewModel);
    }

    /**
     * 更新数据，从而重绘页面
     * 给Adapter赋值 显示 ListView
     *
     * @param viewModel
     */
    public void updateViewModel(FundRegCompanyModel viewModel) {
        if (null == fundCoAdapter) {
            fundCoAdapter = new FundCoAdapter(getContext());
            lsvCompanyList.setAdapter(fundCoAdapter);
        }
        this.viewModel = viewModel;
        fundCoAdapter.setViewModel(viewModel);
        fundCoAdapter.notifyDataSetChanged();
    }

    /**
     * 快速搜索栏的字母定位ListView滑动
     *
     * @param word
     */
    private void scrollToWord(String word) {
        int destIndex = 0;    // 目标ListView的Item的索引
        for (int i = 0; i < viewModel.getList().size(); i++) {
            String curLetter = viewModel.getList().get(i).getFundRegLetterTitle();
            if (StringUtils.isEmptyOrNull(curLetter)) {
                continue;
            }
            if (curLetter.compareTo(word) < 0) {
                destIndex = i;
            } else if (curLetter.compareTo(word) == 0) {
                destIndex = i;
                break;
            }
        }
        lsvCompanyList.setSelection(destIndex);
    }

    /**
     * 注册登记机构的选择与回调
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (viewModel == null || viewModel.getList() == null || viewModel.getList().size() <= position) {
            return;
        }

        FundRegCompanyModel.RegCompanyBean bean = viewModel.getList().get(position);

        Bundle bundle = new Bundle();
        bundle.putSerializable("selectFundCo", bean);
        setFramgentResult(1, bundle);
        pop();
    }

    @Override
    protected SelectFundCoContract.Presenter initPresenter() {
        return new SelectFundCoPresenter(this);
    }

    @Override
    public void setListener() {
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_select_reg_corporation);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

}