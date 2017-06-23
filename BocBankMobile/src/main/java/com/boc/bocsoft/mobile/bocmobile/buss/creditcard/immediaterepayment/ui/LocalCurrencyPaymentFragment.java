package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyNoTitleWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.AccountInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.CrcdAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.CrcdBillInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.RepaymentInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model.TransCommissionChargeBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.presenter.RepayPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.presenter.RepaymentContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.widget.SelectTabRow;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xwg on 16/11/22 13:48
 * 人民币还款
 */
public class LocalCurrencyPaymentFragment extends MvpBussFragment<RepayPresenter> implements RepaymentContract.RepaymentView, View.OnClickListener {


    private View rootView;
    private EditChoiceWidget mChoiceAccount;
    private LinearLayout layoutContent;
    private ArrayList<String> accountTypeList;// 过滤的账户类型
    private SelectTabRow tabRowFullAmount;
    private SelectTabRow tabRowMini;
    private SelectTabRow tabRowCustom;

    private AccountBean curAccountBean;// 当前选中的账户数据

    private EditMoneyNoTitleWidget etMoney;
    private LinearLayout ll_input;
    private Button btnNext;
    private TextView tvRMBRemain;
    private RelativeLayout rl_remain;

    /**
     *  本币余额标识 true为存款，false为欠款
     */
    private boolean localRemainFlag=true;


    //汇 钞标识
    private String cashRemit;
    /**
     *  本币 实时账单
     */
    private CrcdBillInfoBean localCurrencyInfo;

    /**
     *   本币余额
     */
    private BigDecimal  loacalCurrencyRemain;

    private TextView tvPayway;
    /**
     *  还款信息实体类
     */
    private RepaymentInfoBean repaymentInfoBean;
    private List<AccountBean> accountList;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_crcd_repayment, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();

        mChoiceAccount = (EditChoiceWidget) rootView.findViewById(R.id.choice_account);
        layoutContent = (LinearLayout) rootView.findViewById(R.id.layout_content);
        ll_input = (LinearLayout) rootView.findViewById(R.id.ll_input);

        btnNext=(Button)rootView.findViewById(R.id.btn_next);

        rl_remain=(RelativeLayout)rootView.findViewById(R.id.rl_remain);
        tvRMBRemain=(TextView)rootView.findViewById(R.id.tv_rmb_remain);
        tvPayway=(TextView)rootView.findViewById(R.id.tv_crcd_payway);

        etMoney=(EditMoneyNoTitleWidget) rootView.findViewById(R.id.trans_money);
        etMoney.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_money_color_red));
//        etMoney.getContentMoneyEditText().setTextSize(getResources().getDimensionPixelOffset(R.dimen.boc_text_size_common));
        etMoney.setBackgroudRes(0);
        etMoney.setETPadding(getResources().getDimensionPixelOffset(R.dimen.boc_space_between_16px),0,
                getResources().getDimensionPixelOffset(R.dimen.boc_space_between_16px),0);
        etMoney.setHeight(getResources().getDimensionPixelOffset(R.dimen.boc_space_between_90px));
    }

    @Override
    public void initData() {
        super.initData();

        localCurrencyInfo= findFragment(RepaymentMainFragment.class).getLocalCurrencyInfo();


        filterAccountType();
        accountList = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);

        curAccountBean = new AccountBean();
        //从本地缓存读取上次还款账户id  再根据此id找到对应的accountBean
        String accIdFromBackup= BocCloudCenter.getInstance().getUserDict(AccountType.ACC_TYPE_CRCD_LOCAL_REPAY);;//上次还款账户id
        if (!StringUtil.isNullOrEmpty(accIdFromBackup)){
            curAccountBean=getLinkedAccByAccId(accIdFromBackup);
            mChoiceAccount.setChoiceTextContent(NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()));

            if (curAccountBean.getAccountId().equals(localCurrencyInfo.getCrcdAccId())||
                    ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(curAccountBean.getAccountType())){
                closeProgressDialog();
                mChoiceAccount.setChoiceTextContent(getResources().getString(R.string.boc_common_select));
            }else{
                queryAccountDetail();
            }
        }else{
            if (accountList.size()>0&&!localCurrencyInfo.getCrcdAccNo().equals(accountList.get(0).getAccountNumber())
                    &&!ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountList.get(0).getAccountType())){
                curAccountBean=accountList.get(0);
                mChoiceAccount.setChoiceTextContent(NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()));
                //查询账户余额
                queryAccountDetail();
            }else {
                closeProgressDialog();
                mChoiceAccount.setChoiceTextContent(getResources().getString(R.string.boc_common_select));
            }
        }
        mChoiceAccount.getChoiceWidgetArrowImageView().setVisibility(View.VISIBLE);
        mChoiceAccount.setChoiceTextName(getResources().getString(R.string.boc_creditcard_repayment_acc));


        initSelectTabRow();

        etMoney.setContentHint(getResources().getString(R.string.boc_creditcard_repayment_amount_hint));
        etMoney.setMaxLeftNumber(12);
        etMoney.setScrollView(mContentView);

        if (tabRowCustom.isChecked())
            ll_input.setVisibility(View.VISIBLE);
        else
            ll_input.setVisibility(View.GONE);

    }

    /**
     *  添加选择条
     */
    private void initSelectTabRow() {
        tabRowFullAmount=new SelectTabRow(mContext);
        tabRowFullAmount.setContent(getResources().getString(R.string.boc_creditcard_payway_full),"（人民币元 "+MoneyUtils.transMoneyFormat(localCurrencyInfo.getHaveNotRepayAmout(),localCurrencyInfo.getCurrency())+"）");
        tabRowFullAmount.setTabChecked(true);
        layoutContent.addView(tabRowFullAmount,0);

        tabRowMini=new SelectTabRow(mContext);
        tabRowMini.setContent(getResources().getString(R.string.boc_creditcard_payway_limit),"（人民币元 "+MoneyUtils.transMoneyFormat(localCurrencyInfo.getBillLimitAmout(),localCurrencyInfo.getCurrency())+"）");
        layoutContent.addView(tabRowMini,1);

        tabRowCustom=new SelectTabRow(mContext);
        tabRowCustom.setContent(getResources().getString(R.string.boc_creditcard_repayment_custom));
        tabRowCustom.showUnderLine(false);
        layoutContent.addView(tabRowCustom,2);

        tabRowFullAmount.setOnClickListener(this);
        tabRowMini.setOnClickListener(this);
        tabRowCustom.setOnClickListener(this);
    }

    /**
     *  重新初始化数据
     */
    public void reInitData(){
        queryAccountDetail();
        localCurrencyInfo= findFragment(RepaymentMainFragment.class).getLocalCurrencyInfo();
        tabRowFullAmount.setContent(getResources().getString(R.string.boc_creditcard_payway_full),"（人民币元 "+MoneyUtils.transMoneyFormat(localCurrencyInfo.getHaveNotRepayAmout(),localCurrencyInfo.getCurrency())+"）");
        tabRowMini.setContent(getResources().getString(R.string.boc_creditcard_payway_limit),"（人民币元 "+MoneyUtils.transMoneyFormat(localCurrencyInfo.getBillLimitAmout(),localCurrencyInfo.getCurrency())+"）");
        etMoney.setmContentMoneyEditText("");
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    /**
     *   根据卡片种类查询卡片余额等信息
     */
    private void queryAccountDetail(){
        rl_remain.setVisibility(View.GONE);

        if (curAccountBean==null||StringUtil.isNullOrEmpty(curAccountBean.getAccountId())){
            closeProgressDialog();
            return;
        }

        showLoadingDialog();

        if (ApplicationConst.ACC_TYPE_BRO.equals(curAccountBean.getAccountType()))
            getPresenter().queryNormalAccountDetail(curAccountBean);
        else
            getPresenter().queryCrcdAccountDetail(curAccountBean);
    }

    @Override
    public void setListener() {
        super.setListener();
        mChoiceAccount.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.choice_account) {// 选择账户列表
            final SelectAccoutFragment selectAccoutFragment=SelectAccoutFragment.newInstance(accountTypeList);
            selectAccoutFragment.isRequestNet(true);
            selectAccoutFragment.setOnItemListener(new SelectAccoutFragment.ItemListener(){

                @Override
                public void onItemClick(Bundle bundle) {
                    AccountBean accountBean = bundle.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                    if (localCurrencyInfo.getCrcdAccNo().equals(accountBean.getAccountNumber())){
                        ToastUtils.show(getResources().getString(R.string.boc_creditcard_formacc_toacc_same));
                        return;
                    }
                    if (ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountBean.getAccountType())){
                        ToastUtils.show(getResources().getString(R.string.boc_transfer_account_no_money));
                        return;
                    }
                    curAccountBean=accountBean;
                    selectAccoutFragment.pop();
                    mChoiceAccount.setChoiceTextContent(NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()));
                    queryAccountDetail();
                }
            });

            start(selectAccoutFragment);
        } else if (view.getId() == R.id.btn_next) {// 下一步
            if (mContext.getString(R.string.boc_common_select).equals(mChoiceAccount.getChoiceTextContent())||rl_remain.getVisibility()==View.GONE) {
                showErrorDialog(mContext.getString(R.string.boc_select_account));
                return;
            }
            requestNextFragment();
        }else if (view == tabRowFullAmount){
            clearSelectedState();
            tabRowFullAmount.setTabChecked(true);
        }else if (view == tabRowMini){
            clearSelectedState();
            tabRowMini.setTabChecked(true);
        }else if (view == tabRowCustom){
            clearSelectedState();
            ll_input.setVisibility(View.VISIBLE);
            tabRowCustom.setTabChecked(true);
            layoutContent.setPadding(0,0,0,getResources().getDimensionPixelOffset(R.dimen.boc_space_between_40px));
        }
    }

    private void clearSelectedState(){
        ll_input.setVisibility(View.GONE);
        tabRowFullAmount.setTabChecked(false);
        tabRowMini.setTabChecked(false);
        tabRowCustom.setTabChecked(false);
        layoutContent.setPadding(0,0,0,0);
    }

    /**
     * 点击下一步按钮
     */
    private void requestNextFragment() {

        if (localCurrencyInfo.getHaveNotRepayAmout().doubleValue()==0.0){
            showErrorDialog(getResources().getString(R.string.boc_creditcard_no_bill));
            return;
        }

        if (loacalCurrencyRemain==null){
            showErrorDialog(getResources().getString(R.string.boc_transfer_account_no_rmb));
            return;
        }

        if (tabRowCustom.isChecked()&&("0.00".equals(etMoney.getContentMoney())||"".equals(etMoney.getContentMoney()))){
            showErrorDialog(getResources().getString(R.string.boc_creditcard_input_empty));
            return;
        }

        if (!localRemainFlag||(tabRowFullAmount.isChecked()&&loacalCurrencyRemain.doubleValue()<localCurrencyInfo.getHaveNotRepayAmout().doubleValue())||
                (tabRowMini.isChecked()&&loacalCurrencyRemain.doubleValue()<localCurrencyInfo.getBillLimitAmout().doubleValue())||
                (tabRowCustom.isChecked()&&Double.parseDouble(etMoney.getContentMoney().replace(",",""))>loacalCurrencyRemain.doubleValue())){
            ToastUtils.show(getResources().getString(R.string.boc_creditcard_remain_not_enough));
            return;
        }



        showLoadingDialog();

        repaymentInfoBean=new RepaymentInfoBean();
        repaymentInfoBean.setFromAccountId(curAccountBean.getAccountId());
        repaymentInfoBean.setFromAccountNo(curAccountBean.getAccountNumber());
        repaymentInfoBean.setCurrency(curAccountBean.getCurrencyCode());
        repaymentInfoBean.setToName(curAccountBean.getAccountName());
        repaymentInfoBean.setAccountName(curAccountBean.getAccountName());

        repaymentInfoBean.setAmount(MoneyUtils.transMoneyFormat(getPaymentAmount(),localCurrencyInfo.getCurrency()));
        repaymentInfoBean.setToAccountNo(localCurrencyInfo.getCrcdAccNo());
        repaymentInfoBean.setToAccountId(localCurrencyInfo.getCrcdAccId());

        repaymentInfoBean.setCashRemit(cashRemit);
        repaymentInfoBean.setBillCurrency(localCurrencyInfo.getCurrency());
        repaymentInfoBean.setPayway(getPayway());
        repaymentInfoBean.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
        repaymentInfoBean.setHaveNotRepay(localCurrencyInfo.getHaveNotRepayAmout().doubleValue()-Double.parseDouble(getPaymentAmount()));
        repaymentInfoBean.setPayLocalBill(true);

        //信息校验
        getPresenter().checkPaymentInfo(repaymentInfoBean);

    }


    /**
     *   获取还款金额
     *   返回值需要金额格式化
     */
    private String getPaymentAmount() {
        if(tabRowFullAmount.isChecked()){
            return localCurrencyInfo.getHaveNotRepayAmout().toString();
        }else if (tabRowMini.isChecked()){
            return localCurrencyInfo.getBillLimitAmout().toString();
        }else {
            return etMoney.getContentMoney();
        }

    }

    private int getPayway(){
        if (tabRowFullAmount.isChecked())
            return RepaymentMainFragment.PAY_WAY_LOCAL_FULL;
        else if (tabRowMini.isChecked())
            return  RepaymentMainFragment.PAY_WAY_LOCAL_MINI_LIMIT;
        else
            return RepaymentMainFragment.PAY_WAY_LOCAL_CUST;
    }

    public void setTvPayway(){
//       if("1".equals(paywayOpentFlag)){//自动还款
        tvPayway.setVisibility(View.VISIBLE);
        tvPayway.setText("您已开通自动还款功能");
//        }
    }

    /**
     * 过滤账户类型
     */
    private void filterAccountType() {
        accountTypeList = new ArrayList<String>();
        // 借记卡 119
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);
        // 中银系列信用卡 103
        accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);
        //长城信用卡 104
        accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);
        // 单外币信用卡 107
        accountTypeList.add(ApplicationConst.ACC_TYPE_SINGLEWAIBI);

    }

    //通过accoutId 找到关联账户
    public AccountBean getLinkedAccByAccId(String accountId){

        boolean haveAccount = false;
        for (int i = 0; i < accountList.size(); i++) {
            String sha256String = BocCloudCenter.getSha256String(accountList.get(i).getAccountId());
//            String sha256String = accountList.get(i).getAccountId();
            if (accountId.equals(sha256String)) {
                haveAccount = true;
                curAccountBean = accountList.get(i);
                break;
            }
        }
        if (!haveAccount) {
            curAccountBean = accountList.get(0);
        }
        return curAccountBean;
    }


    @Override
    protected RepayPresenter initPresenter() {
        return new RepayPresenter(this);
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void queryNormalAccountDetails(List<AccountInfoViewModel> viewModelList) {
        if (findFragment(RepaymentMainFragment.class).getCurrentIndex()==0)
            closeProgressDialog();
        if (viewModelList == null || viewModelList.size() == 0) {
            rl_remain.setVisibility(View.GONE);
            showErrorDialog(getResources().getString(R.string.boc_transfer_account_no_rmb));
            return;
        }
        //遍历获取人民币账户余额
        for (AccountInfoViewModel accountInfoViewModel : viewModelList) {
            if (ApplicationConst.CURRENCY_CNY.equals(accountInfoViewModel.getCurrencyCode())) {
                rl_remain.setVisibility(View.VISIBLE);
                tvRMBRemain.setVisibility(View.VISIBLE);
                cashRemit="00";
                localRemainFlag=true;
                loacalCurrencyRemain=accountInfoViewModel.getAvailableBalance();
                tvRMBRemain.setText(getResources().getString(R.string.boc_transfer_money_unit,"存款 ",
                        MoneyUtils.transMoneyFormat(String.valueOf(accountInfoViewModel.getAvailableBalance()), accountInfoViewModel.getCurrencyCode())));
            }
        }
        if (rl_remain.getVisibility()==View.GONE)
            showErrorDialog(getResources().getString(R.string.boc_transfer_account_no_rmb));
    }

    @Override
    public void queryCrcdAccountDetails(List<CrcdAccountViewModel> crcdAccountViewModels) {
        if (findFragment(RepaymentMainFragment.class).getCurrentIndex()==0)
            closeProgressDialog();
        if (crcdAccountViewModels==null||crcdAccountViewModels.size()==0){
            rl_remain.setVisibility(View.GONE);
            showErrorDialog(getResources().getString(R.string.boc_transfer_account_no_rmb));
            return;
        }
        //遍历获取人民币账户余额
        for (CrcdAccountViewModel accountInfoViewModel : crcdAccountViewModels) {
            if (ApplicationConst.CURRENCY_CNY.equals(accountInfoViewModel.getCurrency())) {
                rl_remain.setVisibility(View.VISIBLE);
                tvRMBRemain.setVisibility(View.VISIBLE);
                cashRemit="00";//人民币没有钞汇标示
                loacalCurrencyRemain=accountInfoViewModel.getLoanBalanceLimit();//取实时余额
                if (!"0".equals(accountInfoViewModel.getLoanBalanceLimitFlag())){
                    localRemainFlag=true;
                    tvRMBRemain.setText(getResources().getString(R.string.boc_transfer_money_unit,"存款 ",
                            MoneyUtils.transMoneyFormat(String.valueOf(accountInfoViewModel.getLoanBalanceLimit()), accountInfoViewModel.getCurrency())));
                } else{
                    localRemainFlag=false;
                    tvRMBRemain.setText(getResources().getString(R.string.boc_transfer_money_unit,"欠款 ",
                            MoneyUtils.transMoneyFormat(String.valueOf(accountInfoViewModel.getLoanBalanceLimit()), accountInfoViewModel.getCurrency())));
                }
            }
        }
        if (rl_remain.getVisibility()==View.GONE)
            showErrorDialog(getResources().getString(R.string.boc_transfer_account_no_rmb));
    }

    @Override
    public void checkPaymentInfoSucc(String crcdInvalidDate, String crcdOverdueFlag, String crcdState, final RepaymentInfoBean repaymentInfoBean) {
        if ("1".equals(crcdOverdueFlag)||!"".equals(crcdState)){
//            showErrorDialog("对不起，您的信用卡已过期");
            closeProgressDialog();
            final TitleAndBtnDialog dialog = new TitleAndBtnDialog(getContext());
            String[] btnNames = new String[]{"取消", "确认"};
            dialog.setBtnName(btnNames);
            dialog.setNoticeContent("您的信用卡状态异常，确认是否进行还款");
            dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                @Override
                public void onLeftBtnClick(View view) {
                    dialog.dismiss();
                }

                @Override
                public void onRightBtnClick(View view) {
                    dialog.dismiss();
                    showLoadingDialog();
                    getPresenter().getBocTransCommCharge(repaymentInfoBean);
                }
            });
            dialog.show();
        }else {
            getPresenter().getBocTransCommCharge(repaymentInfoBean);
        }
//        else if("ACTP".equals(crcdState)){
//            closeProgressDialog();
//            showErrorDialog("该卡尚未激活，请您先激活");
//        }

    }

    /**
     *  试算查询结果回调
     */
    @Override
    public void transferCommissionChargeResult(TransCommissionChargeBean transCommissionChargeBean) {
        closeProgressDialog();

        RepaymentConfirmFragment confirmFragment= new RepaymentConfirmFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(RepaymentConfirmFragment.TRANS_COMMISSION_CHARGE_KEY,transCommissionChargeBean);
        bundle.putParcelable(RepaymentConfirmFragment.REPAYMENT_INFO_KEY,repaymentInfoBean);
        confirmFragment.setArguments(bundle);

        start(confirmFragment);
    }

}
