package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.across;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.adapter.AcrossBankAccountAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;

import java.util.List;

/**
 * 选择账户开通页面
 * Created by zhx on 2016/10/14
 */
@SuppressLint("ValidFragment")
public class AcrossBankClosedFragment extends BaseAccountFragment implements AdapterView.OnItemClickListener {

    private List<LimitModel> closeModels;

    private ListView lvAccount;

    private AcrossBankAccountAdapter adapter;


    public AcrossBankClosedFragment(List<LimitModel> closeModels) {
        this.closeModels = closeModels;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_select_account);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_across_bank_closed, null);
    }

    @Override
    public void initView() {
        lvAccount = (ListView) mContentView.findViewById(R.id.lv_account);
    }

    @Override
    public void initData() {
        adapter = new AcrossBankAccountAdapter(mContext);
        lvAccount.setAdapter(adapter);
        adapter.setDatas(closeModels);
    }

    @Override
    public void setListener() {
        lvAccount.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        start(new AcrossBankFragment(adapter.getItem(position)));
    }
}
