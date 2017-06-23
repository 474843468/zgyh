package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.adapter.PayeeAccountAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment：从收款账户列表中选择收款账户
 * Created by zhx on 2016/7/11
 */
public class ChoosePayeeAccountFragment extends BussFragment {
    private View mRootView;
    private ListView lv_payee_account_list;

    private PayeeAccountAdapter mAdapter;

    /**
     * 页面跳转数据传递
     */
    public static final String ACCOUNT_SELECT = "AccountBean";
    public static final int RESULT_CODE_SELECT_PAYEE_ACCOUNT = 100;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_choose_payee_account, null);
        return mRootView;
    }

    @Override
    public void initView() {
        lv_payee_account_list = (ListView) mRootView.findViewById(R.id.lv_payee_account_list);
    }

    @Override
    public void initData() {
        // 从ApplicationContext中获取账户列表
        List<AccountBean> accountBeanList = ApplicationContext.getInstance().getChinaBankAccountList(null);
        List<AccountBean> accountBeanLitsFilter = new ArrayList<AccountBean>();

        if (accountBeanList != null) {
            for (AccountBean accountBean : accountBeanList) {
                if (accountBean.getAccountType().equals("119") || accountBean.getAccountType().equals("103") || accountBean.getAccountType().equals("104")) {
                    accountBeanLitsFilter.add(accountBean);
                }
            }
        }


        mAdapter = new PayeeAccountAdapter(accountBeanLitsFilter, mContext);
        lv_payee_account_list.setAdapter(mAdapter);
        lv_payee_account_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(ACCOUNT_SELECT, mAdapter.getItem(position));
                setFramgentResult(RESULT_CODE_SELECT_PAYEE_ACCOUNT, bundle);
                pop();
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return "选择账户";
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
