package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.presenter.SelectMarginAccountPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SelectMarginAccountFragment extends MvpBussFragment<SelectMarginAccountContract.Presenter> implements SelectMarginAccountContract.View,AdapterView.OnItemClickListener {

    private View rootView;
    private SelectMarginAccountContract.Presenter mSelectMarginAccountPresenter;
    private ListView lvMarginAccount;
    private SelectMarginAccountAdapter adapter;
    private String currency;
    private String currencyAccount;
    private LongShortForexService longShortForexService;
    public static final int REQUEST_CODE_SELECT_ACCOUNT = 200;
    public static final int RESULT_CODE_SELECT_ACCOUNT = 2000;
    public static final String CURRENCY = "Currency";
    public static final String CURRENCYACCOUNT = "CurrencyAccount";

    private List<VFGBailListQueryViewModel.BailItemEntity> listVFGBailListQueryViewModel = new ArrayList<>();


    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_select_margin_account, null);
        return rootView;
    }

    @Override
    public void initView() {
        lvMarginAccount = (ListView) rootView.findViewById(R.id.account_list_view);
        System.out.println("SelectMargin---->>>>>initView");
    }

    @Override
    public void initData() {
        getListVFGBailListQueryViewModel();
        adapter = new SelectMarginAccountAdapter(mContext,listVFGBailListQueryViewModel);
        lvMarginAccount.setAdapter(adapter);
        adapter.setDatas((List)listVFGBailListQueryViewModel);
    }

    @Override
    protected SelectMarginAccountContract.Presenter initPresenter() {
        return new SelectMarginAccountPresenter(this);
    }

    @Override
    public void setListener() {
        lvMarginAccount.setOnItemClickListener(this);
    }

    @Override
    protected String getTitleValue() {
        return "选择保证金账户";
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        currency = ((TextView)view.findViewById(R.id.tv_currency)).getText().toString();
        currencyAccount = ((TextView)view.findViewById(R.id.tv_account)).getText().toString();
        bundle.putString(CURRENCY, currency);
        bundle.putString(CURRENCYACCOUNT, currencyAccount);
        setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundle);
        pop();
    }

    @Override
    public void psnXpadBailListQuerySuccess(VFGBailListQueryViewModel viewModel) {

        System.out.println("接口调用成功");

        //listVFGBailListQueryViewModel = viewModel.getEntityList();
        adapter = new SelectMarginAccountAdapter(mContext,listVFGBailListQueryViewModel);
        lvMarginAccount.setAdapter(adapter);
        adapter.setDatas((ArrayList)listVFGBailListQueryViewModel);

        System.out.println("viewModel的长度 " + viewModel.getEntityList().size());
        System.out.println("listVFG的长度 " + listVFGBailListQueryViewModel.size());
        System.out.println("listVFG中的内容 " + listVFGBailListQueryViewModel.get(0).getSettleCurrency());

    }

    @Override
    public void psnXpadBailListQueryFail(BiiResultErrorException biiResultErrorException) {

    }

    public void setListVFGBailListQueryViewModel(List<VFGBailListQueryViewModel.BailItemEntity> listBailItemEntity){
        listVFGBailListQueryViewModel = listBailItemEntity;
    }

    public List<VFGBailListQueryViewModel.BailItemEntity> getListVFGBailListQueryViewModel(){
        return listVFGBailListQueryViewModel;
    }
}