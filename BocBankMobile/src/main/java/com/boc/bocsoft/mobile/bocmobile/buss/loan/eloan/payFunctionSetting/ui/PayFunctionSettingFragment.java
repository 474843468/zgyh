package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.assignment.SelectAgreementView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.emptyview.CommonEmptyView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentContractModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentInfo;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentInitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.PaymentSignViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model.QryPaymentInfoParams;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.presenter.PayFunctionSettingPresenter;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用{@link PayFunctionSettingFragment#newInstance}静态方法来创建该fragment的一个实例
 */
public class PayFunctionSettingFragment
        extends MvpBussFragment<PayFunctionSettingContract.Presenter>
        implements PayFunctionSettingContract.View, View.OnClickListener,
        SelectAgreementView.OnClickContractListener, SelectGridView.ClickListener {

    private static final String PARAM = "param";
    protected TextView tvAmountLeast;
    protected EditChoiceWidget btnSignAccount;
    protected EditChoiceWidget btnLoanPeriod;
    protected TextView tvPayType;
    protected LinearLayout lytPayType;
    protected SelectGridView gvPref;
    protected LinearLayout viewData;
    protected CommonEmptyView viewEmpty;
    private TextView tvDescription;
    private SelectAgreementView viewAgreement;
    private Button btnNext;
    private View rootView;

    private SignAccountSelectFragment mAssignAccoutFragment;
    private AccountBean mAccountBean;
    private ArrayList<String> mAccountSignTypeList;
    private PaymentSignViewModel mPaymentSignViewModel, mOldPaymentSignViewModel;
    private String mConversationId;
    private PaymentInitBean mPaymentInitBean;
    private SelectStringListDialog mPeriodDialog;
    private PaymentInfo mPaymentInfo;
    private String mContract;
    //单选组件被选择的子项
    private Content mUsePref;
    /**
     * 1:签约
     * 2：修改
     * 3：解约
     */
    private int mSignType;

    private boolean isSigned;

    /**
     * true:中银E贷
     * false:个人循环贷
     */
    private boolean mLoanTypeFlag;

    private final static int SIGN_TYPE_SIGN = 1;
    private final static int SIGN_TYPE_CHANGE = 2;
    private final static int SIGN_TYPE_UNSIGN = 3;

    private String[] monthArray;

    /**
     * 使用该静态方法快速创建该fragment的一个实例，它接收了指定的参数
     */
    public static PayFunctionSettingFragment newInstance(PaymentInitBean paymentInitBean) {
        PayFunctionSettingFragment fragment = new PayFunctionSettingFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM, paymentInitBean);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_pay_function_setting, null);
        return rootView;
    }

    @Override
    public void initView() {
        tvDescription = (TextView) rootView.findViewById(R.id.tv_description);
        viewAgreement = (SelectAgreementView) rootView.findViewById(R.id.view_agreement);
        btnNext = (Button) rootView.findViewById(R.id.btn_next);
        tvAmountLeast = (TextView) rootView.findViewById(R.id.tv_amount_least);
        btnSignAccount = (EditChoiceWidget) rootView.findViewById(R.id.btn_sign_account);
        btnLoanPeriod = (EditChoiceWidget) rootView.findViewById(R.id.btn_loan_period);
        tvPayType = (TextView) rootView.findViewById(R.id.tv_pay_type);
        lytPayType = (LinearLayout) rootView.findViewById(R.id.lyt_pay_type);
        gvPref = (SelectGridView) rootView.findViewById(R.id.gv_pref);
        viewData = (LinearLayout) rootView.findViewById(R.id.view_data);
        viewEmpty = (CommonEmptyView) rootView.findViewById(R.id.view_empty);
        viewData.setVisibility(View.GONE);
        mTitleBarView.getRightTextButton()
                     .setTextColor(getResources().getColor(R.color.boc_text_color_red));
    }

    @Override
    public void initData() {
        if (getArguments() == null
                || (mPaymentInitBean = getArguments().getParcelable(PARAM)) == null) {
            return;
        }
        mAccountSignTypeList = new ArrayList<>();
        mAccountSignTypeList.add(ApplicationConst.ACC_TYPE_BRO);
        monthArray = new String[] {
                "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
        };
        mLoanTypeFlag = "F".equals(mPaymentInitBean.getQuoteFlag());
        mPaymentInfo = mPaymentInitBean.getPaymentInfo();
        mPaymentSignViewModel = new PaymentSignViewModel();
        List<Content> prefs = new ArrayList<>();
        Content content1 = new Content();
        content1.setContentNameID("BOR");
        content1.setName(getString(R.string.boc_eloan_payment_loan_first));
        prefs.add(content1);
        Content content2 = new Content();
        content2.setContentNameID("DEP");
        content2.setName(getString(R.string.boc_eloan_payment_deposit_first));
        prefs.add(content2);
        gvPref.setData(prefs);
        onSelectPref(content1);
        mPaymentSignViewModel.setQuoteFlag(mPaymentInitBean.getQuoteFlag());
        if (mLoanTypeFlag) {
            mPaymentSignViewModel.setQuoteOrActNo(mPaymentInitBean.getQuoteNo());
            mPaymentSignViewModel.setProductBigType("1160");
            mPaymentSignViewModel.setProductCatType("1001");
            btnLoanPeriod.setVisibility(View.VISIBLE);
        } else {
            mPaymentSignViewModel.setQuoteOrActNo(mPaymentInitBean.getLoanNo());
            btnLoanPeriod.setVisibility(View.GONE);
        }
        mPaymentSignViewModel.setQuoteType(mPaymentInitBean.getQuoteType());
        mPaymentSignViewModel.setPayAccount(mPaymentInitBean.getPayAccount());
        mPaymentSignViewModel.setMinLoanAmount("1000.00");
        mPaymentSignViewModel.setMinLoanAmtCur(ApplicationConst.CURRENCY_CNY);
        mPaymentSignViewModel.setRate(mPaymentInitBean.getRate());
        tvAmountLeast.setText(
                String.format(getContext().getString(R.string.boc_eloan_payment_amount_least_value),
                        PublicCodeUtils.getCurrency(getContext(), ApplicationConst.CURRENCY_CNY),
                        MoneyUtils.transMoneyFormat("1000.00", ApplicationConst.CURRENCY_CNY)));
        showLoadingDialog();
        QryPaymentInfoParams params = new QryPaymentInfoParams();
        params.setQuoteOrActNo(mPaymentSignViewModel.getQuoteOrActNo());
        params.setLoanTypeFlag(mLoanTypeFlag);
        getPresenter().qryPaymentInfo(params);
    }

    @Override
    public void setListener() {
        btnSignAccount.setOnClickListener(this);
        btnLoanPeriod.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        viewAgreement.setOnClickContractListener(this);
        gvPref.setListener(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_pay_func_setting);
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
    protected void titleRightIconClick() {

        toDoVerify(SIGN_TYPE_UNSIGN);
    }

    @Override
    public void onQryPaymentInfoSuccess(String conversationId, PaymentInfo paymentInfo) {
        closeProgressDialog();
        showDataView();
        mConversationId = conversationId;
        if (!mLoanTypeFlag) {
            mPaymentInfo = paymentInfo;
        }
        if (mPaymentInfo == null) {
            mPaymentInfo = new PaymentInfo();
        }
        mPaymentSignViewModel.setConversationId(mConversationId);
        mAssignAccoutFragment =
                SignAccountSelectFragment.newInstance(mAccountSignTypeList, mConversationId,
                        mPaymentInfo.getSignAccountNum(), ApplicationConst.CURRENCY_CNY);
        //根据返回信息决定显示哪个界面
        //如果该贷款账户未签约支付，则此字段为空
        if (StringUtils.isEmpty(mPaymentInfo.getSignAccountNum())) {
            isSigned = false;
            setPaymentSignView();
        } else {
            isSigned = true;
            setPaymentAlterView();
        }
    }

    @Override
    public void onQryPaymentInfoFailed(String errorMessage) {
        //TODO 空页面怎么处理
        closeProgressDialog();
        hideDataView(errorMessage);
    }

    @Override
    public void onQryContractSuccess(String contract) {
        closeProgressDialog();
        mContract = contract;
        //进入合同页面
        start(ContractFragment.newInstance(getContractUrl(), getContractModel()));
    }

    @Override
    public void onQrySecurityFactorSuccess(SecurityFactorModel securityFactorModel) {
        CombinListBean combinBean = SecurityVerity.getInstance(getActivity()).
                getDefaultSecurityFactorId(securityFactorModel);
        mPaymentSignViewModel.set_combinId(combinBean.getId());
        mPaymentSignViewModel.setCombinName(combinBean.getName());
        if (mOldPaymentSignViewModel != null) {
            mOldPaymentSignViewModel.set_combinId(combinBean.getId());
            mOldPaymentSignViewModel.setCombinName(combinBean.getName());
        }
        switch (mSignType) {
            case SIGN_TYPE_SIGN:
                getPresenter().verifySign(mPaymentSignViewModel);
                break;
            case SIGN_TYPE_CHANGE:
                getPresenter().verifyChange(mPaymentSignViewModel);
                break;
            case SIGN_TYPE_UNSIGN:
                mOldPaymentSignViewModel.setSignType(SIGN_TYPE_UNSIGN);
                getPresenter().verifyUnSign(mOldPaymentSignViewModel);
                break;
            default:
                break;
        }
    }

    @Override
    public void onVerifySignSuccess(VerifyBean verifyBean) {
        closeProgressDialog();
        startNext(verifyBean);
    }

    @Override
    public void onVerifyUnSignSuccess(VerifyBean verifyBean) {
        closeProgressDialog();
        startNext(verifyBean);
    }

    @Override
    public void onVerifyChangeSuccess(VerifyBean verifyBean) {
        closeProgressDialog();
        startNext(verifyBean);
    }

    private void startNext(VerifyBean verifyBean) {
        switch (mSignType) {
            case SIGN_TYPE_SIGN:
                start(PaymentSignConfirmFragment.newInstance(mPaymentSignViewModel, verifyBean));
                break;
            case SIGN_TYPE_CHANGE:
                start(PaymentSignChangeConfirmFragment.newInstance(mPaymentSignViewModel,
                        verifyBean));
                break;
            case SIGN_TYPE_UNSIGN:
                start(PaymentUnSignConfirmFragment.newInstance(mOldPaymentSignViewModel,
                        verifyBean));
                break;
            default:
                break;
        }
    }

    private void setPaymentSignView() {
        btnSignAccount.setChoiceTextContent(getString(R.string.boc_common_select));
        btnLoanPeriod.setChoiceTextContent(getString(R.string.boc_common_select));
        tvPayType.setText("");
        lytPayType.setVisibility(View.GONE);
        viewAgreement.setSelected(false);
        viewAgreement.setVisibility(View.VISIBLE);
        viewAgreement.setContractName(getString(
                mLoanTypeFlag ? R.string.boc_eloan_payment_contractname_F
                        : R.string.boc_eloan_payment_contractname_L));
    }

    private void setPaymentAlterView() {
        mTitleBarView.setRightButton(getString(R.string.boc_eloan_payment_unsign));
        for (AccountBean accountBean : ApplicationContext.getInstance()
                                                         .getChinaBankAccountList(
                                                                 mAccountSignTypeList)) {
            if (accountBean.getAccountNumber().equals(mPaymentInfo.getSignAccountNum())) {
                mPaymentSignViewModel.setSignAccountId(accountBean.getAccountId());
                break;
            }
        }
        mPaymentSignViewModel.setSignAccountNum(mPaymentInfo.getSignAccountNum());
        mPaymentSignViewModel.setOldsignAccount(mPaymentInfo.getSignAccountNum());
        btnSignAccount.setChoiceTextContent(
                StringUtils.isEmpty(mPaymentSignViewModel.getSignAccountId()) ? getString(
                        R.string.boc_common_select)
                        : NumberUtils.formatCardNumber(mPaymentInfo.getSignAccountNum()));
        if ("BOR".equals(mPaymentInfo.getUsePref())) {
            onSelectPref(gvPref.getAdapter().getItem(0));
        } else {
            onSelectPref(gvPref.getAdapter().getItem(1));
        }
        if (mLoanTypeFlag) {
            int period = Integer.valueOf(mPaymentInfo.getSignPeriod());
            onSelectPeriod(period,
                    String.format(getContext().getString(R.string.boc_pledge_info_period_month),
                            period));
        }
        viewAgreement.setVisibility(View.GONE);
        mOldPaymentSignViewModel =
                BeanConvertor.toBean(mPaymentSignViewModel, new PaymentSignViewModel());
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (data == null) {
            return;
        }
        if (SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT == requestCode) {
            AccountBean accountBean = data.getParcelable(SignAccountSelectFragment.ACCOUNT_SELECT);
            if (accountBean == null || mAccountBean == accountBean) {
                return;
            }
            mAccountBean = accountBean;
            mPaymentSignViewModel.setSignAccountId(mAccountBean.getAccountId());
            mPaymentSignViewModel.setSignAccountNum(mAccountBean.getAccountNumber());
            btnSignAccount.setChoiceTextContent(
                    NumberUtils.formatCardNumber(accountBean.getAccountNumber()));
        }
    }

    @Override
    public void onClickContract(int index) {
        if (checkInfoBeforeClickContract()) {
            //查询合同内容
            showLoadingDialog();
            getPresenter().qryContract("CHN", mPaymentInitBean.getQuoteTypeCode());
        }
    }

    private Object getContractModel() {
        PaymentContractModel model = new PaymentContractModel();
        model.setCustName(ApplicationContext.getInstance().getUser().getCustomerName());
        model.setCustCerNo(NumberUtils.formatIDNumber(
                ApplicationContext.getInstance().getUser().getIdentityNumber()));
        model.setLimitNo(mPaymentInitBean.getQuoteNo());
        model.setLoanAccount(mPaymentInitBean.getLoanNo());
        model.setRelDebitAccount(
                NumberUtils.formatCardNumber(mPaymentSignViewModel.getSignAccountNum()));
        model.setPreference("BOR".equals(mPaymentSignViewModel.getUsePref()) ? getString(
                R.string.boc_eloan_payment_loan_first)
                : getString(R.string.boc_eloan_payment_deposit_first));
        if (mLoanTypeFlag) {
            model.setTimeLimit(
                    String.format(getContext().getString(R.string.boc_pledge_info_period_month),
                            Integer.parseInt(mPaymentSignViewModel.getSignPeriod())));
            model.setRepayMode(mPaymentSignViewModel.getPayTypeString());
        }
        model.setMinLoanAmount(
                MoneyUtils.transMoneyFormat("1000.00", ApplicationConst.CURRENCY_CNY));
        model.setDealType(mPaymentInitBean.getQuoteTypeCode());
        model.setContract(mContract);
        return model;
    }

    private String getContractUrl() {

        return "file:///android_asset/webviewcontent/loan/eloan/payment/LoanManagement/paymentAgreement.html";
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_sign_account) {
            startForResult(mAssignAccoutFragment,
                    SignAccountSelectFragment.REQUEST_CODE_SELECT_ACCOUNT);
        } else if (view.getId() == R.id.btn_next) {
            if (checkInfo()) {
                toDoVerify(StringUtils.isEmpty(mPaymentInfo.getSignAccountNum()) ? SIGN_TYPE_SIGN
                        : SIGN_TYPE_CHANGE);
            }
        } else if (view.getId() == R.id.btn_loan_period) {
            selectPeriod();
        }
    }

    private void toDoVerify(int signType) {
        mSignType = signType;
        mPaymentSignViewModel.setSignType(mSignType);
        showLoadingDialog(false);
        //需要设置服务码
        String serviceId = null;
        switch (signType) {
            case SIGN_TYPE_SIGN:
                serviceId = "PB125";
                break;
            case SIGN_TYPE_CHANGE:
                serviceId = "PB126";
                break;
            case SIGN_TYPE_UNSIGN:
                serviceId = "PB127";
                break;
        }
        getPresenter().qrySecurityFactor(serviceId);
    }

    /**
     * 选择还款期限
     * 用户选择1月－6月，还款方式默认为到期一次性还本付息，用户选择7-12月还款方式为按期还息到期还本。
     */
    private void selectPeriod() {
        if (mPeriodDialog == null) {
            mPeriodDialog = new SelectStringListDialog(getContext());
            List<String> periodList = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                periodList.add(
                        String.format(getContext().getString(R.string.boc_pledge_info_period_month),
                                i));
            }
            mPeriodDialog.setListData(periodList);
            mPeriodDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                @Override
                public void onSelect(int position, String model) {
                    onSelectPeriod(position + 1, model);
                    mPeriodDialog.dismiss();
                }
            });
        }
        mPeriodDialog.show();
    }

    private void onSelectPeriod(int period, String model) {
        btnLoanPeriod.setChoiceTextContent(model);
        mPaymentSignViewModel.setSignPeriod(monthArray[period - 1]);
        //  签约期限1-3个月，上送1：一次性还本付息 ；签约期限4-12个月，上送2：按月付息到期还本
        mPaymentSignViewModel.setRepayFlag(period <= 3 ? "1" : "2");
        mPaymentSignViewModel.setPayCycle(period <= 3 ? "98" : "01");
        tvPayType.setText(mPaymentSignViewModel.getPayTypeString());
        lytPayType.setVisibility(View.VISIBLE);
    }

    private boolean checkInfo() {
        if (StringUtils.isEmpty(mPaymentSignViewModel.getSignAccountNum())) {
            showErrorDialog(getString(R.string.boc_eloan_payment_sign_account_empty));
            return false;
        }
        if (mLoanTypeFlag && StringUtils.isEmpty(mPaymentSignViewModel.getSignPeriod())) {
            showErrorDialog(getString(R.string.boc_eloan_payment_sign_period_empty));
            return false;
        }
        if (!isSigned && !viewAgreement.isSelected()) {
            showErrorDialog(getString(R.string.boc_agreement_not_select));
            return false;
        }
        return true;
    }

    private boolean checkInfoBeforeClickContract() {
        if (StringUtils.isEmpty(mPaymentSignViewModel.getSignAccountNum())) {
            showErrorDialog(getString(R.string.boc_eloan_payment_sign_account_empty_before));
            return false;
        }
        if (mLoanTypeFlag && StringUtils.isEmpty(mPaymentSignViewModel.getSignPeriod())) {
            showErrorDialog(getString(R.string.boc_eloan_payment_sign_period_empty_before));
            return false;
        }
        return true;
    }

    @Override
    public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
        onSelectPref(gvPref.getAdapter().getItem(position));
    }

    private void onSelectPref(Content item) {
        mUsePref = item;
        mPaymentSignViewModel.setUsePref(mUsePref.getContentNameID());
        for (Content content : gvPref.getAdapter().getData()) {
            content.setSelected(content == item);
        }
        gvPref.getAdapter().notifyDataSetChanged();
        if ("BOR".equals(item.getContentNameID())) {
            tvDescription.setText(R.string.boc_eloan_payment_loan_first_description);
        } else {
            tvDescription.setText(R.string.boc_eloan_payment_deposit_first_description);
        }
    }

    @Override
    protected PayFunctionSettingContract.Presenter initPresenter() {
        return new PayFunctionSettingPresenter(this);
    }

    public void showDataView() {
        showDataView(true, null);
    }

    public void hideDataView(String errorMessage) {
        showDataView(false, errorMessage);
    }

    private void showDataView(boolean hasData, String errorMessage) {
        if (hasData) {
            viewData.setVisibility(View.VISIBLE);
            viewEmpty.setVisibility(View.GONE);
        } else {
            viewData.setVisibility(View.GONE);
            viewEmpty.setVisibility(View.VISIBLE);
            viewEmpty.setEmptyTips(errorMessage, R.drawable.boc_load_failed);
        }
    }
}