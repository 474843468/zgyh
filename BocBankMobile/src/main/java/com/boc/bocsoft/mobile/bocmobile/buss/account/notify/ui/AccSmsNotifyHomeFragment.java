package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmQuery.PsnSsmQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.User;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact.AccountNotifyContact;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.AccountNotifyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.SmsNotifyEditModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.SmsNotifyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.presenter.AccountNotifyPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**动户通知首页
 * Created by wangtong on 2016/6/15.
 */
public class AccSmsNotifyHomeFragment extends BussFragment implements AccountNotifyContact.View, SelectAccoutFragment.ItemListener {

    public static final String NOTIFY_ACCOUNT = "notify_account";
    protected View rootView;
    //账户卡号
    protected SelectAccountButton cardRow;
    //导航标题
    protected ListView listView;
    /**
     * 继续添加手机号
     */
    protected TextView btnContinueOpen;
    /**
     * 开通短信通知
     */
    protected TextView btnOpen;
    protected TextView feeAccount;
    //protected TextView feeStandard;
    protected TextView changeFeeAccount;//更改缴费账户
    protected LinearLayout groupView;
    protected LinearLayout rootGroup;
    //业务处理
    private AccountNotifyPresenter presenter;
    //数据模型
    private AccountNotifyModel model;
    //会话ID
    public static String conversationId;
    //账户列表
    private List<AccountBean> accountList;
    private AccountBean defaultAccount;
    private MobileAdapter adapter;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_account_notify, null);
        return rootView;
    }

    @Override
    public void initView() {
        cardRow = (SelectAccountButton) rootView.findViewById(R.id.card_row);
        listView = (ListView) rootView.findViewById(R.id.list_view);
        btnContinueOpen = (TextView) rootView.findViewById(R.id.btn_continue_open);
        feeAccount = (TextView) rootView.findViewById(R.id.fee_account);
        changeFeeAccount = (TextView) rootView.findViewById(R.id.change_fee_account);
        groupView = (LinearLayout) rootView.findViewById(R.id.group_view);
        btnOpen = (TextView) rootView.findViewById(R.id.btn_open);
        rootGroup = (LinearLayout) rootView.findViewById(R.id.root_group);
    }

    @Override
    public void initData() {
        model = new AccountNotifyModel();
        presenter = new AccountNotifyPresenter(this);

        defaultAccount = getArguments().getParcelable(NOTIFY_ACCOUNT);
        if (defaultAccount != null) {
            cardRow.setArrowVisible(false);
            updateAccount(defaultAccount);
        } else {
            List<String> accountType = new ArrayList<>();
            accountType.add("188");
            accountType.add("170");
            accountType.add("101");
            accountType.add("119");
            accountType.add("199");
            accountList = ApplicationContext.getInstance().getChinaBankAccountList(accountType);
            if (accountList != null && accountList.size() > 0) {
                AccountBean bean = accountList.get(0);
                updateAccount(bean);
            }
        }

        String name = ApplicationContext.getInstance().getUser().getCustomerName();
        model.setUserName(name);
    }

    @Override
    public void setListener() {
        super.setListener();
        cardRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (defaultAccount == null) {
                    List<String> accountType = new ArrayList<>();
                    accountType.add("188");
                    accountType.add("170");
                    accountType.add("101");
                    accountType.add("119");
                    accountType.add("199");

                    SelectAccoutFragment fragment = new SelectAccoutFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST,
                            (ArrayList<String>) accountType);
                    fragment.setArguments(bundle);
                    fragment.setMedicalEcashVisible(true);
                    startForResult(fragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
                }
            }
        });

        btnContinueOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.isContainBigAmount()) {
                    showErrorDialog("该账户已添加大额交易提醒服务，如需开通动户通知服务请通过其他渠道删除大额交易提醒服务。");
                } else {
                    startOpenFragment();
                }
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.isContainBigAmount()) {
                    showErrorDialog("该账户已添加大额交易提醒服务，如需开通动户通知服务请通过其他渠道删除大额交易提醒服务。");
                } else {
                    UserLimitFragment fragment = new UserLimitFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("userName", model.getUserName());
                    bundle.putBoolean("isContinueOpen", true);
                    fragment.setArguments(bundle);
                    start(fragment);
                }
            }
        });

        changeFeeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> accountType = new ArrayList<>();
                accountType.add("101");
                accountType.add("119");
                accountType.add("188");
                SelectAccoutFragment fragment = new SelectAccoutFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST,
                        (ArrayList<String>) accountType);
                fragment.setArguments(bundle);
                fragment.isRequestNet(true);
                fragment.setOnItemListener(AccSmsNotifyHomeFragment.this);
                fragment.setArrowVisible(true);
                fragment.setFragmentTitle("更改缴费账户");
                fragment.setAccountTip("选择新的缴费账户");
                start(fragment);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PsnSsmQueryResult.MaplistBean bean = (PsnSsmQueryResult.MaplistBean) adapter.getItem(position);
                startSmsNotifyFragment(bean);

                if (bean.getDiscountmodel().equals("00")) {
                    model.setFeeRate("2");
                } else {
                    model.setFeeRate("0");
                }
            }
        });
    }

    private void startSmsNotifyFragment(PsnSsmQueryResult.MaplistBean bean) {
        SmsNotifyDetailFragment fragment = new SmsNotifyDetailFragment();
        SmsNotifyModel smsNotifyModel = new SmsNotifyModel();
        smsNotifyModel.convertToSmsNotifyModel(bean);
        smsNotifyModel.setFeeAccount(model.getFeeAccount());
        smsNotifyModel.setSignAccount(model.getAccount());
        smsNotifyModel.setFeeRate(model.getFeeRate());
        smsNotifyModel.setUserName(model.getUserName());
        Bundle bundle = new Bundle();
        bundle.putParcelable(SmsNotifyEditFragment.KEY_MODEL, smsNotifyModel);
        fragment.setArguments(bundle);
        start(fragment);
    }

    private void updateAccount(AccountBean bean) {
        model.setAccount(bean);
        cardRow.setData(bean);
        User user = ApplicationContext.getInstance().getUser();
        model.setAccount(bean);
        model.setSmsPhone(user.getMobile());
        presenter.psnSmsQuery();
    }

    private void startOpenFragment() {
        start(getOpenFragment());
    }

    public SmsNotifyEditFragment getOpenFragment() {
        SmsNotifyEditFragment fragment = new SmsNotifyEditFragment();
        Bundle bundle = new Bundle();
        SmsNotifyEditModel editModel = new SmsNotifyEditModel();
        editModel.setSignAccount(model.getAccount());
        editModel.setFeeRate(model.getFeeRate());
        if (model.getMapList().size() > 0) {
            editModel.setPhoneNum("");
        } else {
            editModel.setPhoneNum(model.getSmsPhone());
        }
        editModel.setFeeAccount(model.getFeeAccount());
        editModel.setUserName(model.getUserName());
        bundle.putBoolean(SmsNotifyEditFragment.KEY_IS_EDIT, false);
        bundle.putParcelable(SmsNotifyEditFragment.KEY_MODEL, editModel);
        if (model.getMapList().size() > 0) {
            bundle.putBoolean("isFirstOpen", false);
        } else {
            bundle.putBoolean("isFirstOpen", true);
        }

        fragment.setArguments(bundle);
        return fragment;
    }

    public void refreshSmsList() {
        presenter.psnSmsQuery();
    }

    public void refreshFeeAccount(String account) {
        model.setFeeAccount(account);
        feeAccount.setText(getString(R.string.boc_fee_account_num, NumberUtils.formatCardNumber(account)));
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT == resultCode && data != null) {
            AccountBean accountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            updateAccount(accountBean);
        }
    }

    @Override
    public void requestFailed(BiiResultErrorException biiResultErrorException) {
    }

    @Override
    public void psnSmsQueryReturned() {
        rootGroup.setVisibility(View.VISIBLE);
        if (model.getMapList().size() > 0) {
            if (adapter == null) {
                adapter = new MobileAdapter();
                listView.setAdapter(adapter);
            } else {
                adapter.refreshData();
            }
            feeAccount.setText(getString(R.string.boc_fee_account_num, NumberUtils.formatCardNumber(model.getFeeAccount())));
            groupView.setVisibility(View.VISIBLE);
            btnOpen.setVisibility(View.GONE);
        } else {
            groupView.setVisibility(View.GONE);
            btnOpen.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public AccountNotifyModel getModel() {
        return model;
    }

    @Override
    public void setPresenter(AccountNotifyContact.Presenter presenter) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    @Override
    public void onItemClick(Bundle bundle) {
        AccountBean accountBean = bundle.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
        ModifyPayAccConfirmFragment fragment = new ModifyPayAccConfirmFragment();
        Bundle b = new Bundle();
        b.putString("customerName", model.getUserName());
        b.putString("oldAccountNum", model.getFeeAccount());
        b.putParcelable("newAccount", accountBean);
        b.putParcelable("signedAccount", model.getAccount());
        fragment.setArguments(b);
        start(fragment);
    }

    public boolean isMobileOpenned(String mobile) {
        for (PsnSsmQueryResult.MaplistBean bean : model.getMapList()) {
            if (bean.getPushterm().equals(mobile)) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        setFramgentResult(BussFragment.RESULT_OK, new Bundle());
        super.titleLeftIconClick();
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "账户动户通知";
    }

    @Override
    public boolean onBackPress() {
        setFramgentResult(BussFragment.RESULT_OK, new Bundle());
        super.titleLeftIconClick();
        return true;
    }

    class MobileAdapter extends BaseAdapter {

        private List<PsnSsmQueryResult.MaplistBean> mobileList;

        public MobileAdapter() {
            mobileList = model.getMapList();
        }

        public void refreshData() {
            mobileList = model.getMapList();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mobileList.size();
        }

        @Override
        public Object getItem(int position) {
            return mobileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.boc_item_account_notify, null);
            }

            final PsnSsmQueryResult.MaplistBean bean = mobileList.get(position);
            TextView mobile = (TextView) convertView.findViewById(R.id.mobile_num);
            TextView fee = (TextView) convertView.findViewById(R.id.fee_stand);
            mobile.setText(NumberUtils.formatMobileNumber(bean.getPushterm()));
            if (bean.getDiscountmodel().equals("00")) {
                fee.setText("2元/月");
            } else {
                fee.setText("免费");
            }

            return convertView;
        }
    }
}
