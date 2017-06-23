package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.AccountListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.CollectionAccountCheckReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.CollectionAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.presenter.ChangeAccountPresenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 账户管理类
 * <p>
 * Created by xintong on 2016/6/25.
 */
public class AccoutFragment extends MvpBussFragment<ChangeAccountPresenter> implements ChangeAccountContract.AccountListView {
    private static final String TAG = "AccoutFragment";
    private View mRootView;

    private ListView mListView;
//    private ChangeAccountPresenter changeAccountPresenter;

    public static final int RequestCode = 111;
    public static final int ResultCode = 222;
    private List<AccountBean> list;
    private boolean processing;
    /**
     * 账户列表的adapter
     */
    private AccountListAdapter accountListAdapter;
    //是否是变更账户
    private boolean isChangeAccount;

    private AccountType accountType;
    private String conversationId;

    private String accountId;
    private String account;
    /**
     * 账户列表数据
     */
    private List<QueryAllChinaBankAccountRes> resultAccountList;

    private String currentAccount, type;
    private RelativeLayout rl_tip;
    private TextView tv;
    private String availableBalance;
    private String mcurrencyCode;//币种
    /**
     * 是否是中银E贷
     */
    private boolean isBocELoan = true;
    /**
     * 筛选币种-非中银E贷需要
     */
    private String currencyCode = "";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_eloan_selectaccount, null);
        return mRootView;
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initView() {
        currencyCode = transforCodeFormat(this.currencyCode);
//        changeAccountPresenter = new ChangeAccountPresenter(this);
        getPresenter().setConversationId(conversationId);
        //币种必须为 CNY 格式
        getPresenter().setIsBocELoan(isBocELoan, currencyCode);


        mListView = (ListView) mRootView.findViewById(R.id.lv_transdetail_selectaccount);
        rl_tip = (RelativeLayout) mRootView.findViewById(R.id.rl_tip);
        tv = (TextView) mRootView.findViewById(R.id.tv);

        accountListAdapter = new AccountListAdapter(mContext);
        accountListAdapter.setAmountInfoVisible(false);
    }

    /**
     * 设置是否是中银E贷功能进入的
     *
     * @param isBocELoan
     */
    public void setIsBocELoan(boolean isBocELoan) {
        this.isBocELoan = isBocELoan;
    }

    /**
     * @param isBocELoan   设置是否是中银E贷功能进入的
     * @param currencyCode 设置非中银E贷 提前还款功能-币种筛选
     */
    public void setIsBocELoan(boolean isBocELoan, String currencyCode) {
        this.isBocELoan = isBocELoan;
        this.currencyCode = currencyCode;
    }

    private String transforCodeFormat(String currencyCode){
        String mCode = "";
        if (!StringUtils.isEmptyOrNull(currencyCode)) {
            if (Pattern.compile("(?i)[A-Z]").matcher(currencyCode).find()) {
                mCode = PublicCodeUtils.getCurrencyCode(getContext(), currencyCode);
            } else  if (Pattern.compile("(?i)[0-9]").matcher(currencyCode).find()) {
                mCode = currencyCode;
            }else{
                mCode = PublicCodeUtils.getCurrency(getContext(), currencyCode);
            }
        }
        return mCode;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public ListView getmListView() {
        return mListView;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_select_account);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    /**
     * 设置币种过滤筛选-非中银E贷需要
     *
     * @param currencyCode
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }


    public enum AccountType {
        SELECT_REPAYMENTACCOUNT, SELECT_COLLECTIONACCOUNT, CHANGE_REPAYMENTACCOUNT;
    }

    @Override
    public void initData() {
        currentAccount = getArguments().getString("currentAccount");
        type = getArguments().getString("type");

        String mNprepay = getArguments().getString("commonType") + "";//非中银E贷提前还款 的标识（特殊）
//        if(accountType==AccountType.CHANGE_REPAYMENTACCOUNT){
//            //变更还款账户预交易
//            changeAccountPresenter.changeAccountVerify();
//            showLoadingDialog();
//        }
        List<String> list = new ArrayList<String>();

//        list.add("101");
//        list.add("103");
//        list.add("104");
//        list.add("107");
//        list.add("108");
//        list.add("109");
        //2016年10月20日 15:38:51 yx add  为非中银E贷的提前还款使用
        if (("prepay".equalsIgnoreCase(type) && "Nprepay".equalsIgnoreCase(mNprepay))) {
            list.add("101");//普活101
            list.add("188");//活一本188
        }
        //非中银E贷的用款、变更还款账户
        if (("reloan".equalsIgnoreCase(mNprepay) && "change".equalsIgnoreCase(type)) ||
                (("reloan".equalsIgnoreCase(mNprepay) && "draw".equalsIgnoreCase(type)))) {
            list.add("101");//普活101
            list.add("188");//活一本188
        }
        //长城电子借记卡
        list.add("119");

        //传参，过滤借记卡
        getPresenter().setAccountType(list);
        showLoadingDialog();
        //查询账户列表
        getPresenter().queryAllChinaBankAccount();

    }

    @Override
    public void setListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!processing) {
                    if (resultAccountList != null) {
                        if (accountType == AccountType.SELECT_REPAYMENTACCOUNT) {//提前还款  激活
                            account = resultAccountList.get(position).getAccountNumber();
                            accountId = resultAccountList.get(position).getAccountId() + "";
                            getPresenter().setConversationId(conversationId);
                            RepaymentAccountCheckReq repaymentAccountCheckReq
                                    = new RepaymentAccountCheckReq();
                            repaymentAccountCheckReq.setAccountId(accountId);
//                            repaymentAccountCheckReq.setCurrencyCode(resultAccountList.get(position).getCurrencyCode());
                            if (isBocELoan) {
                                repaymentAccountCheckReq.setCurrencyCode(resultAccountList.get(position).getCurrencyCode());
                            } else {
                                LogUtil.d("yx-------------币种1---->" + currencyCode);
//                                repaymentAccountCheckReq.setCurrencyCode(currencyCode);
//                                repaymentAccountCheckReq.setCurrencyCode(PublicCodeUtils.getCurrencyCode(mContext,currencyCode));
                                if (Pattern.compile("(?i)[A-Z]").matcher(currencyCode).find()) {
                                    repaymentAccountCheckReq.setCurrencyCode(PublicCodeUtils.getCurrencyCode(mContext, currencyCode));
                                } else if (Pattern.compile("(?i)[0-9]").matcher(currencyCode).find()) {
                                    repaymentAccountCheckReq.setCurrencyCode(currencyCode);
                                }else{
                                    repaymentAccountCheckReq.setCurrencyCode(PublicCodeUtils.getCurrency(mContext, currencyCode));
                                }
                            }

                            getPresenter().setRepaymentAccountCheckReq(repaymentAccountCheckReq);
                            showLoadingDialog();
                            //还款账户检查
                            getPresenter().checkRepaymentAccount();

//                        processing = true;

                        }
                        if (accountType == AccountType.SELECT_COLLECTIONACCOUNT) {//用款
                            account = resultAccountList.get(position).getAccountNumber();
                            accountId = resultAccountList.get(position).getAccountId() + "";

                            if (!com.boc.bocsoft.mobile.common.utils.StringUtils.isEmptyOrNull(currentAccount) && currentAccount.equals(account)) {
                                //选择的收款账户与原有收款账户相同
                                showErrorDialog(getString(R.string.boc_loan_payee_account_same));
                                return;
                            } else {
                                //选择新的收款账户，做收款账户检查
                                getPresenter().setConversationId(conversationId);
                                CollectionAccountCheckReq collectionAccountCheckReq
                                        = new CollectionAccountCheckReq();
                                collectionAccountCheckReq.setAccountId(accountId);
                                if(isBocELoan){
                                    collectionAccountCheckReq.setCurrencyCode(ApplicationConst.CURRENCY_CNY);
                                }
                                else{
                                    collectionAccountCheckReq.setCurrencyCode(currencyCode);
                                }
                                getPresenter().setCollectionAccountCheckReq(collectionAccountCheckReq);
                                showLoadingDialog();
                                //收款账户检查
                                getPresenter().checkCollectionAccount();
                            }
                        }
                        if (accountType == AccountType.CHANGE_REPAYMENTACCOUNT) {//变更还款账户
                            account = resultAccountList.get(position).getAccountNumber();
                            accountId = resultAccountList.get(position).getAccountId() + "";
                            getPresenter().setConversationId(conversationId);
                            if (TextUtils.isEmpty(currentAccount)) {
                                RepaymentAccountCheckReq repaymentAccountCheckReq
                                        = new RepaymentAccountCheckReq();
                                repaymentAccountCheckReq.setAccountId(accountId);
//                                repaymentAccountCheckReq.setCurrencyCode(resultAccountList.get(position).getCurrencyCode());
                                if (isBocELoan) {
                                    repaymentAccountCheckReq.setCurrencyCode(resultAccountList.get(position).getCurrencyCode());
                                } else {
                                    LogUtil.d("yx-------------币种1---->" + currencyCode);
//                                    repaymentAccountCheckReq.setCurrencyCode(currencyCode);
//                                    repaymentAccountCheckReq.setCurrencyCode(PublicCodeUtils.getCurrencyCode(mContext,currencyCode));
                                    if (Pattern.compile("(?i)[A-Z]").matcher(currencyCode).find()) {
                                        repaymentAccountCheckReq.setCurrencyCode(PublicCodeUtils.getCurrencyCode(mContext, currencyCode));
                                    } else if (Pattern.compile("(?i)[0-9]").matcher(currencyCode).find()) {
                                        repaymentAccountCheckReq.setCurrencyCode(currencyCode);
                                    }else{
                                        repaymentAccountCheckReq.setCurrencyCode(PublicCodeUtils.getCurrency(mContext, currencyCode));
                                    }
                                }
                                getPresenter().setRepaymentAccountCheckReq(repaymentAccountCheckReq);
                                showLoadingDialog();
                                //还款账户检查
                                getPresenter().checkRepaymentAccount();

                            } else {
                                if (!currentAccount.equals(account)) {
                                    RepaymentAccountCheckReq repaymentAccountCheckReq
                                            = new RepaymentAccountCheckReq();
                                    repaymentAccountCheckReq.setAccountId(accountId);
                                    if (isBocELoan) {
                                        repaymentAccountCheckReq.setCurrencyCode(resultAccountList.get(position).getCurrencyCode());
                                    } else {
                                        LogUtil.d("yx-------------币种1---->" + currencyCode);
                                        if (Pattern.compile("(?i)[A-Z]").matcher(currencyCode).find()) {
                                            repaymentAccountCheckReq.setCurrencyCode(PublicCodeUtils.getCurrencyCode(mContext, currencyCode));
                                        } else if (Pattern.compile("(?i)[0-9]").matcher(currencyCode).find()) {
                                            repaymentAccountCheckReq.setCurrencyCode(currencyCode);
                                        }else{
                                            repaymentAccountCheckReq.setCurrencyCode(PublicCodeUtils.getCurrency(mContext, currencyCode));
                                        }

                                    }
                                    getPresenter().setRepaymentAccountCheckReq(repaymentAccountCheckReq);
                                    showLoadingDialog();
                                    //还款账户检查
                                    getPresenter().checkRepaymentAccount();
                                } else {
                                    if (resultAccountList.size() > 1) {
                                        showErrorDialog(getString(R.string.boc_loan_change_account_same));
                                    } else {
                                        showErrorDialog("没有其他可作为还款账户的借记卡");

                                    }
                                }
                            }

//                        processing = true;
                        }
                    }
                } else {
//                    showLoadingDialog("请稍等，正在处理中...");
//                    showErrorDialog("请选择变更还款账户！");
                }
            }
        });
    }

    @Override
    public void doRepaymentAccountCheckSuccess(RepaymentAccountCheckRes result) {
        LogUtils.i(TAG, "------------>还款账户检查成功!");
        if (accountType == AccountType.CHANGE_REPAYMENTACCOUNT) {  //变更还款账号
            LogUtils.i(TAG, "------------>AccountType.CHANGE_REPAYMENTACCOUNT");

            if ("01".equals(result.getCheckResult().get(0))) {
                doBack();
            } else if ("02".equals(result.getCheckResult().get(0))) {
//                showErrorDialog("贷款账户与收款账户币种不一致");
                //2016年10月15日 17:20:11 yx 修改
                showErrorDialog("您选择的还款账户支持币种与贷款币种不符，请更换还款账户后再试。");
            } else if ("03".equals(result.getCheckResult().get(0))) {
//                showErrorDialog("收款账户为钞户");
                //2016年10月15日 17:20:11 yx 修改
                showErrorDialog("您选择的还款账户暂不支持现汇类型，请更换还款账户或向该账户存入同种现汇类型外币后再试。");
            }
        }

        if (accountType == AccountType.SELECT_REPAYMENTACCOUNT) {   //变更提前还款账号
            LogUtils.i(TAG, "------------>AccountType.SELECT_REPAYMENTACCOUNT");

            if ("01".equals(result.getCheckResult().get(0))) {

//            	changeAccountPresenter.prepayCheckAccountDetail(accountId);

                if ("apply".equalsIgnoreCase(type)) {
                    doBack();
                    LogUtils.i(TAG, "cq------激活------>apply");
                } else if ("prepay".equalsIgnoreCase(type)) {
                    getPresenter().prepayCheckAccountDetail(accountId);
                    LogUtils.i(TAG, "cq------提前还款------>prepay");
                }

            } else if ("02".equals(result.getCheckResult().get(0))) {
                showErrorDialog("您选择的还款账户支持币种与贷款币种不符，请更换还款账户后再试。");
            } else if ("03".equals(result.getCheckResult().get(0))) {
                showErrorDialog("您选择的还款账户暂不支持现汇类型，请更换还款账户或向该账户存入同种现汇类型外币后再试。");
            }
        }
    }

    @Override
    public void doRepaymentAccountCheckFail(ErrorException e) {
        LogUtils.i(TAG, "------------>还款账户检查失败!");
        closeProgressDialog();
        processing = false;
    }

    @Override
    public void prepayCheckAccountDetailSuccess(PrepayAccountDetailModel.AccountDetaiListBean result) {
        LogUtils.i(TAG, "提前还款查询账户详情成功！");
//        MoneyUtils.getNormalMoneyFormat(prepay.getEditWidgetContent()
//        MoneyUtils.transMoneyFormat(mPrepayDetailModel.getRemainCapital(), "021")
        availableBalance = String.valueOf(result.getAvailableBalance());
         mcurrencyCode = transforCodeFormat(result.getCurrencyCode());
        if (!TextUtils.isEmpty(availableBalance)) {
            doBack();
        }
//        availableAvl.setEditWidgetContent("人民币元 "+ MoneyUtils.transMoneyFormat(availableBalance, "021"));
//        closeProgressDialog();

    }

    @Override
    public void prepayCheckAccountDetailFail(ErrorException e) {
        LogUtils.i(TAG, "提前还款查询账户详情失败！");
        closeProgressDialog();
    }

    @Override
    public void doCollectionAccountCheckSuccess(CollectionAccountCheckRes result) {
        LogUtils.i(TAG, "------------>收款账户检查成功!");
        if (accountType == AccountType.SELECT_COLLECTIONACCOUNT) {
            LogUtils.i(TAG, "------------>AccountType.SELECT_COLLECTIONACCOUNT");

            if ("01".equals(result.getCheckResult().get(0))) {
                doBack();
            } else if ("02".equals(result.getCheckResult().get(0))) {
                showErrorDialog(getString(R.string.boc_loan_draw_currency_not_same));
            } else if ("03".equals(result.getCheckResult().get(0))) {
                showErrorDialog(getString(R.string.boc_loan_draw_currency_not_same));
            } else if ("04".equals(result.getCheckResult().get(0))) {
//                showErrorDialog("您好，该贷款仅可用于个人合法合理的消费支出，\n" +
//                        "不得用于投资股票、基金等各类投资用途，请您明确知悉。");
                showErrorDialog(getString(R.string.boc_loan_draw_signed_new));
            }
        }
//        closeProgressDialog();
    }

    @Override
    public void doCollectionAccountCheckFail(ErrorException e) {
        LogUtils.i(TAG, "------------>收款账户检查失败!");
        closeProgressDialog();
    }

    private void doBack() {
        Bundle bundle = new Bundle();
        bundle.putString("account", account);
        bundle.putString("accountId", accountId);
        bundle.putString("availableBalance", availableBalance);
        bundle.putString("currencyCode", mcurrencyCode);
        LogUtils.i(TAG, "------------>account" + account);
        LogUtils.i(TAG, "------------>accountId" + accountId);
        LogUtils.i(TAG, "------------>availableBalance" + availableBalance);
        LogUtils.i(TAG, "------------>mcurrencyCode" + mcurrencyCode);
        setFramgentResult(ResultCode, bundle);
        processing = false;
        pop();
        closeProgressDialog();
    }

    @Override
    public void obtainAllChinaBankAccountSuccess(final List<QueryAllChinaBankAccountRes> result) {
        LogUtils.i(TAG, "------------>获取账户列表成功!");
        if (null != result && result.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            rl_tip.setVisibility(View.GONE);
            this.resultAccountList = result;
            list = new ArrayList<AccountBean>();
            for (int i = 0; i < result.size(); i++) {
                AccountBean accountBean = new AccountBean();

                accountBean.setAccountNumber(result.get(i).getAccountNumber());
                accountBean.setAccountName(result.get(i).getAccountName());
                accountBean.setAccountType(result.get(i).getAccountType());
                accountBean.setNickName(result.get(i).getNickName());
//                accountBean.setAccountStatus(result.get(i).getAccountStatus());

                list.add(accountBean);
            }
            accountListAdapter.setDatas(buildAccountModel(list));
            mListView.setAdapter(accountListAdapter);
            LogUtils.i(TAG, "------------>mListView显示");
        } else {
            mListView.setVisibility(View.GONE);
            rl_tip.setVisibility(View.VISIBLE);
            if ("apply".equalsIgnoreCase(type)) {
                tv.setText("您名下暂无可作为还款账户的借记卡");
            } else if ("draw".equalsIgnoreCase(type)) {
                tv.setText("您名下暂无可作为收款账户的借记卡");
            } else if ("prepay".equalsIgnoreCase(type)) {
                tv.setText("您名下暂无可作为还款账户的借记卡");
            } else if ("change".equalsIgnoreCase(type)) {
                tv.setText("您名下暂无可作为还款账户的借记卡");
            }
            LogUtils.i(TAG, "------------>rl_tip显示");
        }
        closeProgressDialog();
    }

    @Override
    public void obtainAllChinaBankAccountFail(ErrorException e) {
        LogUtils.i(TAG, "------------>获取账户列表失败!");
        closeProgressDialog();
    }

    @Override
    public void setPresenter(ChangeAccountContract.Presenter presenter) {
    }

    /**
     * 将AccountBean的List转换成AccountListItemViewModel的List
     *
     * @param accountBeanList
     * @return
     */
    private List<AccountListItemViewModel> buildAccountModel(List<AccountBean> accountBeanList) {
        List<AccountListItemViewModel> viewModelList = new ArrayList<AccountListItemViewModel>();
        for (int i = 0; i < accountBeanList.size(); i++) {
            AccountListItemViewModel listItemViewModel = new AccountListItemViewModel();
            listItemViewModel.setAccountBean(accountBeanList.get(i));

            viewModelList.add(listItemViewModel);
        }
        return viewModelList;
    }


    @Override
    protected ChangeAccountPresenter initPresenter() {
        return new ChangeAccountPresenter(this);
    }
}
