package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.deposit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.LoanDepositMultipleQueryBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PersonalTimeAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeAvaAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeAvaAndPersonalTimeAccount;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeDepositReceiptViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.deposit.PledgeLoanDepositReceiptSelectPresenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 作者：XieDu
 * 创建时间：2016/8/11 9:19
 * 描述：存款质押贷款选择存单
 */
public class PledgeLoanDepositReceiptSelectFragment
        extends MvpBussFragment<PledgeLoanDepositReceiptSelectContract.Presenter>
        implements PledgeLoanDepositReceiptSelectContract.View, View.OnClickListener,
        AdapterView.OnItemClickListener, PledgeReceiptAdapter.OnSelectListener {

    private static final String PARAM = "param";
    protected SelectAccountButton btnSelectAccount;
    protected ListView lvDepositReceipt;
    protected TextView tvAvailableAmount;
    protected Button btnUseMoney;
    private View rootView;
    private List<PledgeAvaAndPersonalTimeAccount> mPledgeAvaAndPersonalTimeAccountList;
    private PledgeReceiptAdapter mAdapter;
    private SelectAccoutFragment mSelectAccoutFragment;
    private PledgeAvaAndPersonalTimeAccount mPledgeAvaAndPersonalTimeAccount;
    private BigDecimal mTotalAmount, mAvailableAmount;
    private String mCurrency;

    /**
     * 使用该静态方法快速创建该fragment的一个实例，它接收了指定的参数
     */
    public static PledgeLoanDepositReceiptSelectFragment newInstance(
            List<PledgeAvaAndPersonalTimeAccount> pledgeAvaAndPersonalTimeAccountList) {
        ArrayList<PledgeAvaAndPersonalTimeAccount> data =
                pledgeAvaAndPersonalTimeAccountList instanceof ArrayList
                        ? (ArrayList<PledgeAvaAndPersonalTimeAccount>) pledgeAvaAndPersonalTimeAccountList
                        : new ArrayList<>(pledgeAvaAndPersonalTimeAccountList);
        PledgeLoanDepositReceiptSelectFragment fragment =
                new PledgeLoanDepositReceiptSelectFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(PARAM, data);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView =
                mInflater.inflate(R.layout.boc_fragment_pledge_loan_deposit_receipt_select, null);
        return rootView;
    }

    @Override
    public void initView() {
        mTitleBarView.setDividerBottomVisible(true);
        View headview =
                View.inflate(mContext, R.layout.boc_view_pledge_loan_deposit_receipt_list_head,
                        null);
        btnSelectAccount = (SelectAccountButton) headview.findViewById(R.id.btn_select_account);
        lvDepositReceipt = (ListView) rootView.findViewById(R.id.lv_deposit_receipt);
        tvAvailableAmount = (TextView) rootView.findViewById(R.id.tv_available_amount);
        btnUseMoney = (Button) rootView.findViewById(R.id.btn_use_money);
        lvDepositReceipt.addHeaderView(headview, null, false);
    }

    @Override
    public void initData() {
        mTotalAmount = BigDecimal.ZERO;
        mPledgeAvaAndPersonalTimeAccountList = getArguments().getParcelableArrayList(PARAM);
        mAdapter = new PledgeReceiptAdapter(mContext);
        mAdapter.setOnSelectListener(this);
        lvDepositReceipt.setAdapter(mAdapter);
        Observable.from(mPledgeAvaAndPersonalTimeAccountList)
                  .map(new Func1<PledgeAvaAndPersonalTimeAccount, AccountBean>() {
                      @Override
                      public AccountBean call(
                              PledgeAvaAndPersonalTimeAccount pledgeAvaAndPersonalTimeAccount) {
                          PledgeAvaAccountBean pledgeAvaAccountBean =
                                  pledgeAvaAndPersonalTimeAccount.getPledgeAvaAccountBean();
                          AccountBean accountBean = new AccountBean();
                          accountBean.setAccountId(pledgeAvaAccountBean.getAccountId());
                          accountBean.setAccountNumber(pledgeAvaAccountBean.getAccountNumber());
                          accountBean.setAccountName(pledgeAvaAccountBean.getAccountName());
                          accountBean.setAccountType(pledgeAvaAccountBean.getAccountType());
                          accountBean.setNickName(pledgeAvaAccountBean.getNickName());
                          return accountBean;
                      }
                  })
                  .toList()
                  .subscribe(new Action1<List<AccountBean>>() {
                      @Override
                      public void call(List<AccountBean> accountBeen) {
                          ArrayList<AccountBean> data = (accountBeen instanceof ArrayList)
                                  ? (ArrayList<AccountBean>) accountBeen
                                  : new ArrayList<>(accountBeen);
                          updateView(data.get(0));
                          if (data.size() == 1) {
                              btnSelectAccount.setArrowVisible(false);
                              return;
                          }
                          btnSelectAccount.setOnClickListener(
                                  PledgeLoanDepositReceiptSelectFragment.this);
                          mSelectAccoutFragment = SelectAccoutFragment.newInstanceWithData(data);
                      }
                  });
    }

    @Override
    public void setListener() {
        btnUseMoney.setOnClickListener(this);
        lvDepositReceipt.setOnItemClickListener(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_loan_pledge_deposit);
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
    protected PledgeLoanDepositReceiptSelectContract.Presenter initPresenter() {
        return new PledgeLoanDepositReceiptSelectPresenter(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_select_account) {
            startForResult(mSelectAccoutFragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
        } else if (view.getId() == R.id.btn_use_money) {
            //没选择存单的话提示报错
            if (mAdapter.getSelectedCount() == 0) {
                showErrorDialog(getString(R.string.boc_pledge_deposit_receipt_need_select));
                return;
            }
            //调用综合查询接口
            showLoadingDialog();
            getPresenter().qryDepositPledgeParamsData(
                    mPledgeAvaAndPersonalTimeAccount.getPledgeAvaAccountBean().getAccountId(),
                    mAdapter.getSelectedCurrencyCode());
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT == requestCode) {
            AccountBean accountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            updateView(accountBean);
        }
    }

    private void updateView(final AccountBean accountBean) {
        if (mPledgeAvaAndPersonalTimeAccount != null
                && mPledgeAvaAndPersonalTimeAccount.getPledgeAvaAccountBean()
                                                   .getAccountId()
                                                   .equals(accountBean.getAccountId())) {
            return;
        }
        mPledgeAvaAndPersonalTimeAccount = getPledgeAccount(accountBean);
        btnSelectAccount.setData(accountBean);
        mAdapter.setDatas(mPledgeAvaAndPersonalTimeAccount.getPersonalTimeAccountBeanList());
        onUnselectAll();
    }

    private PledgeAvaAndPersonalTimeAccount getPledgeAccount(final AccountBean accountBean) {
        for (PledgeAvaAndPersonalTimeAccount pledgeAvaAndPersonalTimeAccount : mPledgeAvaAndPersonalTimeAccountList) {
            if (pledgeAvaAndPersonalTimeAccount.getPledgeAvaAccountBean()
                                               .getAccountId()
                                               .equals(accountBean.getAccountId())) {
                return pledgeAvaAndPersonalTimeAccount;
            }
        }
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            mAdapter.onCheck(position - 1);
        }
    }

    @Override
    public void onFirstSelect() {
        mCurrency = PublicCodeUtils.getCurrency(mContext, mAdapter.getSelectedCurrencyCode());
        tvAvailableAmount.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUnselectAll() {
        mTotalAmount = BigDecimal.ZERO;
        mCurrency = null;
        tvAvailableAmount.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSelect(int position, boolean selectedState) {
        PersonalTimeAccountBean item = mAdapter.getItem(position);
        if (selectedState) {
            mTotalAmount = mTotalAmount.add(item.getAvailableBalance());
        } else {
            mTotalAmount = mTotalAmount.subtract(item.getAvailableBalance());
        }
        String amount =
                MoneyUtils.transMoneyFormatNoLossAccuracy(mTotalAmount, mAdapter.getSelectedCurrencyCode());
        tvAvailableAmount.setText(
                String.format(getString(R.string.boc_pledge_loan_amount_available),
                        new Object[] { mCurrency, amount }));
    }

    @Override
    public void onQryDepositPledgeParamsDataSuccess(
            LoanDepositMultipleQueryBean loanDepositMultipleQueryBean) {
        closeProgressDialog();
        // 如果1000元比单笔金额下线大，用1000元作比较，如果可贷金额小于1000，直接提示可贷金额不能小于1000元，反之则跳转
        // 如果1000比单笔金额下线小，用单笔金额下作比较，如果可贷金额小于单笔金额下线，直接提示可贷金额不能小于单笔金额下线
        BigDecimal singleQuotaMin;
        BigDecimal thousand = new BigDecimal(1000);
        mAvailableAmount = getAvailableAmount(loanDepositMultipleQueryBean);
        if (StringUtils.isEmpty(loanDepositMultipleQueryBean.getSingleQuotaMin()) ||
                thousand.compareTo(singleQuotaMin =
                        new BigDecimal(loanDepositMultipleQueryBean.getSingleQuotaMin())) > 0) {
            loanDepositMultipleQueryBean.setSingleQuotaMin("1000");
            if (mAvailableAmount.compareTo(thousand) < 0) {
                showErrorDialog(
                        getString(R.string.boc_pledge_deposit_available_amount_too_little_1000));
                return;
            }
        } else if (mAvailableAmount.compareTo(singleQuotaMin) < 0) {
            showErrorDialog(String.format(
                    getString(R.string.boc_pledge_deposit_available_amount_too_little),
                    MoneyUtils.transMoneyFormatNoLossAccuracy(singleQuotaMin, ApplicationConst.CURRENCY_CNY)));
            return;
        }

        //进入填写信息页面
        PledgeDepositReceiptViewModel viewModel = new PledgeDepositReceiptViewModel();
        viewModel.setAccountNumber(
                mPledgeAvaAndPersonalTimeAccount.getPledgeAvaAccountBean().getAccountNumber());
        viewModel.setAccountId(
                mPledgeAvaAndPersonalTimeAccount.getPledgeAvaAccountBean().getAccountId());
        viewModel.setAvailableAmount(mAvailableAmount);
        viewModel.setPersonalTimeAccountBeanArrayList(mAdapter.getSelectDepositReceiptList());
        viewModel.setLoanDepositMultipleQueryBean(loanDepositMultipleQueryBean);
        start(PledgeLoanDepositInfoFillFragment.newInstance(viewModel));
    }

    /**
     * 用款可用额度为贷款可用额度 、单笔限额上限两个值取最小值；
     * 选择存单币种为人民币贷款可用额度为：页面合计金额*质押率 ；
     * 如果选择的币种是外币，则贷款可用额度为：页面合计金额*质押率*汇率
     */
    protected BigDecimal getAvailableAmount(
            LoanDepositMultipleQueryBean loanDepositMultipleQueryBean) {
        BigDecimal availableLoanAmount;
        if (ApplicationConst.CURRENCY_CNY.equals(mAdapter.getSelectedCurrencyCode())) {
            availableLoanAmount = mTotalAmount.multiply(
                    MoneyUtils.transRate(loanDepositMultipleQueryBean.getPledgeRate_ZH()));
        } else {
            BigDecimal pledgeRate =
                    ApplicationConst.CURRENCY_USD.equals(mAdapter.getSelectedCurrencyCode())
                            ? MoneyUtils.transRate(loanDepositMultipleQueryBean.getPledgeRate_US())
                            : MoneyUtils.transRate(loanDepositMultipleQueryBean.getPledgeRate_OT());
            availableLoanAmount = mTotalAmount.multiply(pledgeRate)
                                              .multiply(MoneyUtils.transRate(
                                                      loanDepositMultipleQueryBean.getExchangeRate()));
        }
        if (StringUtils.isEmpty(loanDepositMultipleQueryBean.getSingleQuotaMax())) {
            return availableLoanAmount;
        } else {
            return new BigDecimal(loanDepositMultipleQueryBean.getSingleQuotaMax()).min(
                    availableLoanAmount);
        }
    }
}