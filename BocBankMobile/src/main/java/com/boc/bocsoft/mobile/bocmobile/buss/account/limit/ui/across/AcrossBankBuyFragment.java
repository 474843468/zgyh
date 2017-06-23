package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.across;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.adapter.AcrossBankAccountAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter.AcrossBankContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter.AcrossBankPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;

import java.util.List;

/**
 * 跨行订购
 * Created by zhx on 2016/10/14
 */
public class AcrossBankBuyFragment extends BaseAccountFragment<AcrossBankPresenter> implements AcrossBankContract.AcrossBankView, View.OnClickListener, AdapterView.OnItemClickListener {

    private List<LimitModel> openModels, closeModels;

    private ListView lvAccount;

    private ImageView ivAdd;

    private TextView tvContent;

    private AcrossBankAccountAdapter adapter;

    @Override
    protected AcrossBankPresenter initPresenter() {
        return new AcrossBankPresenter(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_across_bank_title);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_across_bank_open, null);
    }

    @Override
    public void initView() {
        lvAccount = (ListView) mContentView.findViewById(R.id.lv_account);
        ivAdd = (ImageView) mContentView.findViewById(R.id.iv_add);
        tvContent = (TextView) mContentView.findViewById(R.id.tv_content);
    }

    @Override
    public void setListener() {
        ivAdd.setOnClickListener(this);
        lvAccount.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        List<AccountBean> accountBeans = ApplicationContext.getInstance().getChinaBankAccountList(AccountTypeUtil.getBroType());

        if (accountBeans.isEmpty())
            return;

        adapter = new AcrossBankAccountAdapter(mContext);
        lvAccount.setAdapter(adapter);

        showLoadingDialog();
        getPresenter().queryService(accountBeans);
    }

    @Override
    public void onClick(View v) {
        if(closeModels == null || closeModels.isEmpty()){
            showErrorDialog(getString(R.string.boc_account_across_bank_closed_no_data));
            return;
        }
        start(new AcrossBankClosedFragment(closeModels));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        start(new AcrossBankFragment(adapter.getItem(position)));
    }

    @Override
    public void queryService(List<LimitModel> openModels, List<LimitModel> closeModels) {
        closeProgressDialog();

        this.openModels = openModels;
        this.closeModels = closeModels;

        if (!isCurrentFragment())
            return;

        showPanel(openModels);
    }

    private void showPanel(List<LimitModel> openModels) {
        if (openModels != null && !openModels.isEmpty()) {
            tvContent.setText(getString(R.string.boc_account_across_bank_had_data));
            adapter.setDatas(this.openModels);
        } else
            tvContent.setText(getString(R.string.boc_account_across_bank_no_data));
    }

    public void refreshLimitModel(LimitModel limitModel){
        showLoadingDialog();
        if(limitModel.isOpen()){
            closeModels.remove(limitModel);
            openModels.add(limitModel);
        }else{
            openModels.remove(limitModel);
            closeModels.add(limitModel);
        }
        showPanel(openModels);
        closeProgressDialog();
    }
}
