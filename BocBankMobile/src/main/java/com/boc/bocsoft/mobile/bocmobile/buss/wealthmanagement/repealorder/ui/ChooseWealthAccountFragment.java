package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.adapter.WealthAccountAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.presenter.ChooseWealthAccountPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;

import java.util.List;

/**
 * Fragment：中银理财-选择理财帐号页面
 * Created by zhx on 2016/9/7
 */
public class ChooseWealthAccountFragment extends BussFragment implements ChooseWealthAccountContact.View {
    private View rootView;
    private ListView lv_list;
    private ChooseWealthAccountPresenter chooseWealthAccountPresenter;
    private List<XpadAccountQueryViewModel.XPadAccountEntity> accountList;

    public static final int RESULT_CODE_CHOOSE_QUERY_ACCOUNT = 101; // 选择要查询的账户

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_choose_wealth_account, null);
        return rootView;
    }

    @Override
    public void initView() {
        lv_list = (ListView) rootView.findViewById(R.id.lv_list);
    }

    @Override
    public void initData() {
        XpadAccountQueryViewModel viewModel = new XpadAccountQueryViewModel();
        viewModel.setQueryType("0");
        viewModel.setXpadAccountSatus("1");
        chooseWealthAccountPresenter = new ChooseWealthAccountPresenter(this);
        chooseWealthAccountPresenter.psnXpadAccountQuery(viewModel);
    }

    @Override
    public void setListener() {
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 发送选中账户的广播
                Intent intent = new Intent("chooseAccount");
                intent.putExtra("account", accountList.get(position));

//                TransInquireFragment transInquireFragment = findFragment(TransInquireFragment.class);
                mActivity.sendBroadcast(intent);
                pop();
            }
        });
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
    protected String getTitleValue() {
        return "选择账户";
    }

    @Override
    public void psnXpadAccountQuerySuccess(XpadAccountQueryViewModel xpadAccountQueryViewModel) {
        accountList = xpadAccountQueryViewModel.getList();
        WealthAccountAdapter accountAdapter = new WealthAccountAdapter(mActivity, accountList);
        lv_list.setAdapter(accountAdapter);
    }

    @Override
    public void psnXpadAccountQueryFail(BiiResultErrorException biiResultErrorException) {
    }

    @Override
    public void setPresenter(ChooseWealthAccountContact.Presenter presenter) {
    }
}
