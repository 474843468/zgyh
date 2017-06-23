package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;

import java.util.ArrayList;


/**
 * Created by yangle on 2017/1/3.
 * 描述: 选择分期记录卡列表界面
 */
public class InstallmentSelectAccountFragment extends SelectAccoutFragment{


    public static InstallmentSelectAccountFragment newInstance_(ArrayList<String> typelist) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST, typelist);
        InstallmentSelectAccountFragment fragment = new InstallmentSelectAccountFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData() {
        super.initData();
        if (PublicUtils.isEmpty(accountListAdapter.getDatas())){
            super.showNoDataView("无可支持分期的的账户");
            return;
        }
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        AccountBean accountBean = accountListAdapter.getItem(position).getAccountBean();
        if (accountBean != null) {
            start(InstallmentHistoryFragment.newInstance(accountBean));
        }
    }

    @Override
    protected String getTitleValue() {
        return "选择分期记录信用卡";
    }
}
