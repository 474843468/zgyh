package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.ui;

import android.annotation.TargetApi;
import android.os.Build;
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
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
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
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.widget.DoubleDataDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.widget.SelectTabRow;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.widget.SelectedDialogNew;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xwg on 16/11/22 13:48
 * 外币还款
 */
public class ForeignCurrencyPaymentFragment extends MvpBussFragment<RepayPresenter> implements RepaymentContract.ForeignRepaymentView, View.OnClickListener, SelectTabRow.CashRemitClickListener {

    private View rootView;
    private EditChoiceWidget mChoiceAccount;
    private LinearLayout layoutContent;
    private ArrayList<String> accountTypeList;// 过滤的账户类型
    final List<String> paywayList = new ArrayList<>(); //还款方式
    private SelectTabRow tabRowFullAmount;
    private SelectTabRow tabRowMini;
    private SelectTabRow tabRowCustom;

    private AccountBean curAccountBean;// 当前选中的账户数据

    private EditMoneyNoTitleWidget etMoney;
    private LinearLayout ll_input;
    private Button btnNext;
    private TextView tvRMBRemain;
    private RelativeLayout rl_remain;

    //    人民币购汇金额
    private BigDecimal payedAmt,exchangeRate;
    /**
     *  是否支持购汇
     */
    private boolean isSupportExchenge =true;

    /**
     *   自定义还款 还款方式是否为人民币购汇
     */
    private boolean isCustomRMBExchenge=true;
    /**
     *   页面是否初始化完毕
     */
    private boolean isInited=false;
    /**
     *  本币余额标识 true为存款，false为欠款
     */
    private boolean localRemainFlag=true;

    /**
     *  外币币余额标识 true为存款，false为欠款
     */
    private boolean foreingRemainFlag=true;


    //汇 钞标识
    private String cashRemit;
    /**
     *  外币 实时账单
     */
    private CrcdBillInfoBean foreignCurrencyBillInfo;

    /**
     *   本币余额
     */
    private BigDecimal loacalCurrencyRemain;
    /**
     *  外币 现钞余额
     */
    private BigDecimal foreignCurrencyCashRemain;
    /**
     *  外币 现汇余额
     */
    private BigDecimal foreignCurrencyRemitRemain;

    private TextView tvPayway;
    /**
     *  还款信息实体类
     */
    private RepaymentInfoBean repaymentInfoBean;
    private Button btnPayway;
    private SelectTabRow tabRowRMBFullAmount;

    private SelectedDialogNew paywayDialog;
    private TextView tvForeignRemainCash,tvForeignRemainRemit;
    private List<AccountBean> accountList;
    private DoubleDataDialog dataDialog;


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
        tvForeignRemainCash=(TextView)rootView.findViewById(R.id.tv_foreign_remain_cash);
        tvForeignRemainRemit=(TextView)rootView.findViewById(R.id.tv_foreign_remain_remit);
        tvPayway=(TextView)rootView.findViewById(R.id.tv_crcd_payway);
        btnPayway=(Button)rootView.findViewById(R.id.btn_payway);

        etMoney=(EditMoneyNoTitleWidget) rootView.findViewById(R.id.trans_money);
        etMoney.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_money_color_red));
//        etMoney.getContentMoneyEditText().setTextSize(getResources().getDimensionPixelOffset(R.dimen.boc_text_size_common));
        etMoney.setBackgroudRes(0);
        etMoney.setETPadding(getResources().getDimensionPixelOffset(R.dimen.boc_space_between_16px),0,
                getResources().getDimensionPixelOffset(R.dimen.boc_space_between_16px),0);
        etMoney.setHeight(getResources().getDimensionPixelOffset(R.dimen.boc_space_between_90px));

        layoutContent.removeAllViews();
    }

    @Override
    public void initData() {
        super.initData();

        filterAccountType();
        accountList = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);

        curAccountBean = new AccountBean();

        foreignCurrencyBillInfo=findFragment(RepaymentMainFragment.class).getForeignCurrencyInfo();

        //从本地缓存读取上次还款账户id  再根据此id找到对应的accountBean
        String accIdFromBackup= BocCloudCenter.getInstance().getAccountId(AccountType.ACC_TYPE_CRCD_FOREIGN_REPAY);;//上次还款账户id
        if (!StringUtil.isNullOrEmpty(accIdFromBackup)){
            curAccountBean=getLinkedAccByAccId(accIdFromBackup);
            mChoiceAccount.setChoiceTextContent(NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()));

            if (curAccountBean.getAccountId().equals(foreignCurrencyBillInfo.getCrcdAccId())){
                mChoiceAccount.setChoiceTextContent(getResources().getString(R.string.boc_common_select));
            }
        }else{
            if (accountList.size()>0&&!foreignCurrencyBillInfo.getCrcdAccNo().equals(accountList.get(0).getAccountNumber())
                    &&!ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountList.get(0).getAccountType())   ){
                curAccountBean=accountList.get(0);
                mChoiceAccount.setChoiceTextContent(NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()));
            }else {
                mChoiceAccount.setChoiceTextContent(getResources().getString(R.string.boc_common_select));
            }
        }
        mChoiceAccount.getChoiceWidgetArrowImageView().setVisibility(View.VISIBLE);
        mChoiceAccount.setChoiceTextName(getResources().getString(R.string.boc_creditcard_repayment_acc));

//        initSelectTabRow();

        etMoney.setContentHint(getResources().getString(R.string.boc_creditcard_repayment_amount_hint));
        etMoney.setMaxLeftNumber(12);
        etMoney.setScrollView(mContentView);

        //如果是单外币账单
        if (findFragment(RepaymentMainFragment.class).getFragmentListSize()==1){
            startInitData();
        }

    }

    /**
     *  设置界面初始化完成标志
     *
     *  当需要reInit时 设为false
     *
     */
    public void setInited(boolean inited) {
        isInited = inited;
        layoutContent.removeAllViews();
        layoutContent.setPadding(0,0,0,0);
    }

    /**
     *  初始化界面
     */
    public void startInitData(){
        if (!isInited) {
            layoutContent.removeAllViews();
            if (findFragment(RepaymentMainFragment.class).getCurrentIndex()==1||
                    findFragment(RepaymentMainFragment.class).getFragmentListSize()==1 )
                showLoadingDialog();
            layoutContent.addView(ll_input);
            etMoney.setmContentMoneyEditText("");
            foreignCurrencyBillInfo=findFragment(RepaymentMainFragment.class).getForeignCurrencyInfo();
            //查询信用卡购汇还款信息
            getPresenter().crcdQueryForeignPayOff(foreignCurrencyBillInfo.getCrcdAccId(), foreignCurrencyBillInfo.getCurrency());

        }
    }

    /**
     *  添加选择条
     */
    private void initSelectTabRow(BigDecimal payedAmt) {
        tabRowRMBFullAmount=new SelectTabRow(mContext);
        tabRowRMBFullAmount.setContent(getResources().getString(R.string.boc_creditcard_payway_rmbtoforeign_full),"（人民币元 "+MoneyUtils.transMoneyFormat(payedAmt,ApplicationConst.CURRENCY_CNY)+"）");
        layoutContent.addView(tabRowRMBFullAmount,0);
        //默认选中状态
        tabRowRMBFullAmount.setTabChecked(true);

        tabRowFullAmount=new SelectTabRow(mContext);
        tabRowFullAmount.setContent(getForeignCurrent()+getResources().getString(R.string.boc_creditcard_payway_foreign_full),"（"+getForeignCurrent()+" "+MoneyUtils.transMoneyFormat(foreignCurrencyBillInfo.getHaveNotRepayAmout(),foreignCurrencyBillInfo.getCurrency())+"）");
        layoutContent.addView(tabRowFullAmount,1);

        tabRowMini=new SelectTabRow(mContext);
        tabRowMini.setContent(getForeignCurrent()+getResources().getString(R.string.boc_creditcard_payway_limit),"（"+getForeignCurrent()+" "+MoneyUtils.transMoneyFormat(foreignCurrencyBillInfo.getBillLimitAmout(),foreignCurrencyBillInfo.getCurrency())+"）");
        layoutContent.addView(tabRowMini,2);

        tabRowCustom=new SelectTabRow(mContext);
        tabRowCustom.setContent(getResources().getString(R.string.boc_creditcard_repayment_custom));
        tabRowCustom.showUnderLine(false);
//        if (isCustomRMBExchenge)
//            etMoney.setContentHint("最大输入"+MoneyUtils.transMoneyFormat(payedAmt,ApplicationConst.CURRENCY_CNY));
//        else
//            etMoney.setContentHint("最大输入"+MoneyUtils.transMoneyFormat(payedAmt,foreignCurrencyBillInfo.getCurrency()));

        layoutContent.addView(tabRowCustom,3);

        tabRowRMBFullAmount.setOnClickListener(this);
        tabRowFullAmount.setOnClickListener(this);
        tabRowMini.setOnClickListener(this);
        tabRowCustom.setOnClickListener(this);

        tabRowFullAmount.setCashRemitBoxListener(this);
        tabRowMini.setCashRemitBoxListener(this);

    }

    /**
     *  显示钞汇选择框
     */
    private void  showCashRemitBox(View view){
        if (tvForeignRemainCash.getVisibility()==View.VISIBLE&&
                tvForeignRemainRemit.getVisibility()==View.VISIBLE){
            if (view==tabRowFullAmount){
                tabRowFullAmount.setCashRemitBoxDisplay(true);
                tabRowMini.setCashRemitBoxDisplay(false);
            }else if (view==tabRowMini){
                tabRowFullAmount.setCashRemitBoxDisplay(false);
                tabRowMini.setCashRemitBoxDisplay(true);
            }else{
                tabRowMini.setCashRemitBoxDisplay(false);
                tabRowFullAmount.setCashRemitBoxDisplay(false);
            }

        }
    }

    /**
     *  更新tab可选状态
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void updateTabState(){
        //清空自定义还款方式
        paywayList.clear();
        //如果该还款账户下有人民币
        if (tvRMBRemain.getVisibility()==View.VISIBLE&&isSupportExchenge){
            tabRowRMBFullAmount.setTableCheckable(true);
        }
        else
            tabRowRMBFullAmount.setTableCheckable(false);
        //如果是该卡下有相对应的币种
        if (tvForeignRemainCash.getVisibility()==View.VISIBLE||tvForeignRemainRemit.getVisibility()==View.VISIBLE)
            tabRowFullAmount.setTableCheckable(true);
        else
            tabRowFullAmount.setTableCheckable(false);

        if (!tabRowRMBFullAmount.isCheckable()&&tabRowFullAmount.isCheckable()&&!tabRowCustom.isChecked()){
            clearSelectedState();
            tabRowFullAmount.setTabChecked(true);
            showCashRemitBox(tabRowFullAmount);
        }else if (tabRowRMBFullAmount.isCheckable()&&!tabRowFullAmount.isCheckable()&&!tabRowCustom.isChecked()){
            clearSelectedState();
            tabRowRMBFullAmount.setTabChecked(true);
        }

        tabRowMini.setTableCheckable(tabRowFullAmount.isCheckable());

        tabRowCustom.setTableCheckable(tabRowFullAmount.isCheckable()||tabRowRMBFullAmount.isCheckable());
        if (tabRowCustom.isChecked()&&tabRowCustom.isCheckable())
            ll_input.setVisibility(View.VISIBLE);
        else
            ll_input.setVisibility(View.GONE);


        if (tabRowRMBFullAmount.isCheckable()){
            paywayList.add(getResources().getString(R.string.boc_creditcard_payway_rmbtoforeign));
        }
        if (tvForeignRemainCash.getVisibility()==View.VISIBLE){
            paywayList.add(getString(R.string.boc_creditcard_unit_payway,getForeignCurrent(),getString(R.string.boc_creditcard_unit_cash)));
        }
        if (tvForeignRemainRemit.getVisibility()==View.VISIBLE){
            paywayList.add(getString(R.string.boc_creditcard_unit_payway,getForeignCurrent(),getString(R.string.boc_creditcard_unit_remit)));
        }
        etMoney.setmContentMoneyEditText("");
        if (paywayList.size()==1){
            btnPayway.setCompoundDrawables(null,null,null,null);
            btnPayway.setClickable(false);
            updatePaway(paywayList.get(0));
        }else if (paywayList.size()>1){
            btnPayway.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.fund_type_arrow_down),null);
            btnPayway.setClickable(true);
            updatePaway(paywayList.get(0));
        }
    }

    private String getForeignCurrent(){
        return  PublicCodeUtils.getCurrency(mContext,foreignCurrencyBillInfo.getCurrency());
    }

    //通过accoutId 找到关联账户
    public AccountBean getLinkedAccByAccId(String accountId){
        boolean haveAccount = false;
        for (int i = 0; i < accountList.size(); i++) {
            String sha256String = BocCloudCenter.getSha256String(accountList.get(i).getAccountId());
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
    protected boolean isHaveTitleBarView() {
        return false;
    }

    /**
     *   根据卡片种类查询卡片余额等信息
     */
    private void queryAccountDetail(){
//        showLoadingDialog();
        rl_remain.setVisibility(View.GONE);
        tvRMBRemain.setVisibility(View.GONE);
        tvForeignRemainCash.setVisibility(View.GONE);
        tvForeignRemainRemit.setVisibility(View.GONE);

        if (curAccountBean==null||StringUtil.isNullOrEmpty(curAccountBean.getAccountId())
                ||curAccountBean.getAccountId().equals(foreignCurrencyBillInfo.getCrcdAccId())){
            closeProgressDialog();
            return;
        }


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
        btnPayway.setOnClickListener(this);
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
                    if (foreignCurrencyBillInfo.getCrcdAccNo().equals(accountBean.getAccountNumber())){
                        ToastUtils.show(getResources().getString(R.string.boc_creditcard_formacc_toacc_same));
                        return;
                    }
                    //如果支持人民币购汇并且账户下有人民币
                    if (!isSupport(accountBean)){
                        ToastUtils.show(getResources().getString(R.string.boc_transfer_account_no_money));
                        return;
                    }
                    selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, bundle);
                    selectAccoutFragment.pop();
                }
            });

            startForResult(selectAccoutFragment,SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
        } else if (view.getId() == R.id.btn_next) {// 下一步
            if (mContext.getString(R.string.boc_common_select).equals(mChoiceAccount.getChoiceTextContent())||rl_remain.getVisibility()==View.GONE) {
                showErrorDialog(mContext.getString(R.string.boc_select_account));
                return;
            }
            requestNextFragment();
        }else if (view == tabRowRMBFullAmount){
            if (tabRowRMBFullAmount.isCheckable())
                clearSelectedState();
            showCashRemitBox(tabRowRMBFullAmount);
            tabRowRMBFullAmount.setTabChecked(true);
        }else if (view == tabRowFullAmount){
            if (tabRowFullAmount.isCheckable())
                clearSelectedState();
            showCashRemitBox(tabRowFullAmount);
            tabRowFullAmount.setTabChecked(true);
        }else if (view == tabRowMini){
            if (tabRowMini.isCheckable())
                clearSelectedState();
            showCashRemitBox(tabRowMini);
            tabRowMini.setTabChecked(true);
        }else if (view == tabRowCustom){
            clearSelectedState();
            if (!tabRowCustom.isCheckable()){
                return;}
            showCashRemitBox(tabRowCustom);
            ll_input.setVisibility(View.VISIBLE);
            btnPayway.setVisibility(View.VISIBLE);
            tabRowCustom.setTabChecked(true);
            layoutContent.setPadding(0,0,0,getResources().getDimensionPixelOffset(R.dimen.boc_space_between_40px));
        }else if(view.getId()==R.id.btn_payway){
            showChoosePaywayDialog();
        }
    }

    /**
     *  判断还款账户是否满足还款信息
     */
    private boolean isSupport(AccountBean accountBean){
        boolean isSupport=false;
        if (!StringUtil.isNullOrEmpty(accountBean.getCurrencyCode())&&!StringUtil.isNullOrEmpty(accountBean.getCurrencyCode2())){
            if  (ApplicationConst.CURRENCY_CNY.equals(accountBean.getCurrencyCode())||ApplicationConst.CURRENCY_CNY.equals(accountBean.getCurrencyCode2())||
                    foreignCurrencyBillInfo.getCurrency().equals(accountBean.getCurrencyCode())||foreignCurrencyBillInfo.getCurrency().equals(accountBean.getCurrencyCode2()))
                isSupport=true;
        }else {
            if  (ApplicationConst.CURRENCY_CNY.equals(accountBean.getCurrencyCode())||foreignCurrencyBillInfo.getCurrency().equals(accountBean.getCurrencyCode()))
                isSupport=true;
        }
        return isSupport;
    }

//    private void showNoticeDialog() {
//        if (dataDialog==null) {
//            dataDialog = new DoubleDataDialog(mContext);
//            dataDialog.setBtnText("我知道了");
//            dataDialog.addData("问：", "为什么不能选择？");
//            dataDialog.addData("答：", "您选择的账户没有相应币种");
//            dataDialog.setOnBottomViewClickListener(new DoubleDataDialog.OnBottomViewClickListener() {
//                @Override
//                public void onBottomViewClick() {
//                    dataDialog.dismiss();
//                }
//            });
//        }
//        if (!dataDialog.isShowing())
//            dataDialog.show();
//    }

    private void clearSelectedState(){
        ll_input.setVisibility(View.GONE);
        tabRowRMBFullAmount.setTabChecked(false);
        tabRowFullAmount.setTabChecked(false);
        tabRowMini.setTabChecked(false);
        tabRowCustom.setTabChecked(false);
        layoutContent.setPadding(0,0,0,0);
    }

    /**
     * 点击下一步按钮
     */
    private void requestNextFragment() {

        if (foreignCurrencyBillInfo.getHaveNotRepayAmout().doubleValue()==0.0){
            showErrorDialog(getResources().getString(R.string.boc_creditcard_no_bill));
            return;
        }
        if (rl_remain.getVisibility()==View.GONE){
            showErrorDialog(getResources().getString(R.string.boc_transfer_account_no_money));
            return;
        }

        //如果是人民币购汇选中状态，但人民币余额不足  或是外币全额还款选中 但外币剩余金额不足 或是外币最低还款选中 但金额小于最低还款额度
        if (!localRemainFlag||(tabRowRMBFullAmount.isChecked()&&(loacalCurrencyRemain==null||loacalCurrencyRemain.doubleValue()<payedAmt.doubleValue()))
                ){
            ToastUtils.show(getResources().getString(R.string.boc_creditcard_remain_not_enough));
            return;
        }else if(tabRowFullAmount.isChecked()){
            if (!foreingRemainFlag||!(foreignCurrencyCashRemain!=null&&foreignCurrencyCashRemain.doubleValue()>foreignCurrencyBillInfo.getHaveNotRepayAmout().doubleValue()
                    ||(foreignCurrencyRemitRemain!=null&&foreignCurrencyRemitRemain.doubleValue()>foreignCurrencyBillInfo.getHaveNotRepayAmout().doubleValue()))){
                ToastUtils.show(getResources().getString(R.string.boc_creditcard_remain_not_enough));
                return;
            }
        } else if(tabRowMini.isChecked()){
            if (!foreingRemainFlag||!(foreignCurrencyCashRemain!=null&&foreignCurrencyCashRemain.doubleValue()>foreignCurrencyBillInfo.getBillLimitAmout().doubleValue()
                    ||(foreignCurrencyRemitRemain!=null&&foreignCurrencyRemitRemain.doubleValue()>foreignCurrencyBillInfo.getBillLimitAmout().doubleValue()))){
                ToastUtils.show(getResources().getString(R.string.boc_creditcard_remain_not_enough));
                return;
            }
        }else if(tabRowCustom.isChecked()){
            if(("0.00".equals(etMoney.getContentMoney())||"".equals(etMoney.getContentMoney()))){
                showErrorDialog(getResources().getString(R.string.boc_creditcard_input_empty));
                return;
            }

            if ("00".equals(cashRemit)){
                if (!localRemainFlag||loacalCurrencyRemain!=null&&Double.parseDouble(etMoney.getContentMoney().replace(",",""))>loacalCurrencyRemain.doubleValue()){
                    ToastUtils.show(getResources().getString(R.string.boc_creditcard_remain_not_enough));
                    return;
                }
            }else if ("01".equals(cashRemit)){
                if (!foreingRemainFlag||Double.parseDouble(etMoney.getContentMoney().replace(",",""))>foreignCurrencyCashRemain.doubleValue()){
                    ToastUtils.show(getResources().getString(R.string.boc_creditcard_remain_not_enough));
                    return;
                }
            }else if ("02".equals(cashRemit)){
                if (!foreingRemainFlag||Double.parseDouble(etMoney.getContentMoney().replace(",",""))>foreignCurrencyRemitRemain.doubleValue()){
                    ToastUtils.show(getResources().getString(R.string.boc_creditcard_remain_not_enough));
                    return;
                }
            }

        }

        showLoadingDialog();

        repaymentInfoBean=new RepaymentInfoBean();
        repaymentInfoBean.setFromAccountId(curAccountBean.getAccountId());
        repaymentInfoBean.setFromAccountNo(curAccountBean.getAccountNumber());
        if ((tabRowCustom.isChecked()&&isCustomRMBExchenge)||tabRowRMBFullAmount.isChecked())
            repaymentInfoBean.setCurrency(ApplicationConst.CURRENCY_CNY);
        else
            repaymentInfoBean.setCurrency(foreignCurrencyBillInfo.getCurrency());

        repaymentInfoBean.setBillCurrency(foreignCurrencyBillInfo.getCurrency());
        repaymentInfoBean.setToName(curAccountBean.getAccountName());
        repaymentInfoBean.setAccountName(curAccountBean.getAccountName());
        repaymentInfoBean.setAmount(getPaymentAmount());
        repaymentInfoBean.setToAccountNo(foreignCurrencyBillInfo.getCrcdAccNo());
        repaymentInfoBean.setToAccountId(foreignCurrencyBillInfo.getCrcdAccId());
        repaymentInfoBean.setConversationId(getPresenter().getConversationId());

        if (tabRowRMBFullAmount.isChecked()||(tabRowCustom.isChecked()&&isCustomRMBExchenge))
            cashRemit="00";
        else if(tabRowFullAmount.isChecked()&&tvForeignRemainRemit.getVisibility()==View.VISIBLE)
            cashRemit=tabRowFullAmount.getCashRemit();
        else if(tabRowMini.isChecked()&&tvForeignRemainRemit.getVisibility()==View.VISIBLE)
            cashRemit=tabRowMini.getCashRemit();
        else if ((tabRowFullAmount.isChecked()||tabRowMini.isChecked())&&tvForeignRemainRemit.getVisibility()==View.GONE)
            cashRemit="01";


        repaymentInfoBean.setCashRemit(cashRemit);
        repaymentInfoBean.setPayway(getPayway());
        repaymentInfoBean.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
        repaymentInfoBean.setExchangeRate(exchangeRate);
        repaymentInfoBean.setPayLocalBill(false);

        LogUtils.e("TAG", "requestNextFragment-->"+  "cashRemit=="+cashRemit+"\n amount=="+getPaymentAmount()+"\n payway=="+getPayway());

        //信息校验
        if (getPayway()==RepaymentMainFragment.PAY_WAY_FOREIGN_RMB_FULL||getPayway()==RepaymentMainFragment.PAY_WAY_FOREIGN_CUST_CASHREMIT)
            getPresenter().crcdForeignPayoffFee(repaymentInfoBean);
        else
            getPresenter().checkPaymentInfo(repaymentInfoBean);

    }


    /**
     *   获取还款金额
     *   返回值需要金额格式化
     */
    private String getPaymentAmount() {
        if(tabRowRMBFullAmount.isChecked()){
            return MoneyUtils.transMoneyFormat(payedAmt,ApplicationConst.CURRENCY_CNY);
        }else if(tabRowFullAmount.isChecked()){
            return MoneyUtils.transMoneyFormat(foreignCurrencyBillInfo.getHaveNotRepayAmout(),foreignCurrencyBillInfo.getCurrency());
        }else if (tabRowMini.isChecked()){
            return MoneyUtils.transMoneyFormat(foreignCurrencyBillInfo.getBillLimitAmout(),foreignCurrencyBillInfo.getCurrency());
        }else {
            if (isCustomRMBExchenge)
                return  MoneyUtils.transMoneyFormat(etMoney.getContentMoney(),ApplicationConst.CURRENCY_CNY);
            else
                return MoneyUtils.transMoneyFormat(etMoney.getContentMoney(),foreignCurrencyBillInfo.getCurrency());
        }

    }

    private int getPayway(){
        if (tabRowRMBFullAmount.isChecked())
            return RepaymentMainFragment.PAY_WAY_FOREIGN_RMB_FULL;
        else if (tabRowFullAmount.isChecked())
            return RepaymentMainFragment.PAY_WAY_FOREIGN_FULL;
        else if (tabRowMini.isChecked())
            return  RepaymentMainFragment.PAY_WAY_FOREIGN_MINI_LIMIT;
        else{
            if (isCustomRMBExchenge)
                return RepaymentMainFragment.PAY_WAY_FOREIGN_CUST_CASHREMIT;
            else
                return RepaymentMainFragment.PAY_WAY_FOREIGN_CUST;
        }
    }

    public void setTvPayway(){
//        if("1".equals(paywayOpentFlag)){//自动还款
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

    /**
     *  还款方式对话框
     */
    private void showChoosePaywayDialog() {

        if (paywayDialog==null)
            paywayDialog=new SelectedDialogNew(mContext);
        paywayDialog.setTitle("选择自定义还款方式");
        paywayDialog.showDialog(paywayList);
        paywayDialog.setListener(new SelectedDialogNew.OnItemSelectDialogClicked() {
            @Override
            public void onListItemClicked(int index) {
                updatePaway(paywayList.get(index));
                paywayDialog.dismiss();
            }
        });
        if (!paywayDialog.isShowing())
            paywayDialog.show();

    }

    private void updatePaway(String paway){
        btnPayway.setText(paway);
        if (paway.equals(getString(R.string.boc_creditcard_unit_payway,getForeignCurrent(),getString(R.string.boc_creditcard_unit_cash)))){
            isCustomRMBExchenge=false;
            cashRemit="01";
            etMoney.setCurrency(foreignCurrencyBillInfo.getCurrency());
//                    etMoney.setContentHint("最大输入"+MoneyUtils.transMoneyFormat(foreignCurrencyBillInfo.getHaveNotRepayAmout(),foreignCurrencyBillInfo.getCurrency()));
        }else if(paway.equals(getString(R.string.boc_creditcard_unit_payway,getForeignCurrent(),getString(R.string.boc_creditcard_unit_remit)))){
            cashRemit="02";
            isCustomRMBExchenge=false;
            etMoney.setCurrency(foreignCurrencyBillInfo.getCurrency());
//                    etMoney.setContentHint("最大输入"+MoneyUtils.transMoneyFormat(foreignCurrencyBillInfo.getHaveNotRepayAmout(),foreignCurrencyBillInfo.getCurrency()));
        }else if (paway.equals(getString(R.string.boc_creditcard_payway_rmbtoforeign))){
            isCustomRMBExchenge=true;
            cashRemit="00";
            etMoney.setCurrency(ApplicationConst.CURRENCY_CNY);
//                    etMoney.setContentHint("最大输入"+MoneyUtils.transMoneyFormat(payedAmt,ApplicationConst.CURRENCY_CNY));
        }
    }


    @Override
    protected RepayPresenter initPresenter() {
        return new RepayPresenter(this);
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public void onCashRemitBoxClicked(View view, String cashRemit) {
        this.cashRemit=cashRemit;
    }

    @Override
    public void queryNormalAccountDetails(List<AccountInfoViewModel> viewModelList) {
        closeProgressDialog();
        //遍历获取账户余额
        for (AccountInfoViewModel accountInfoViewModel : viewModelList) {
            if (ApplicationConst.CURRENCY_CNY.equals(accountInfoViewModel.getCurrencyCode())) {
                rl_remain.setVisibility(View.VISIBLE);
                tvRMBRemain.setVisibility(View.VISIBLE);
                localRemainFlag=true;
                loacalCurrencyRemain = accountInfoViewModel.getAvailableBalance();
                tvRMBRemain.setText(getResources().getString(R.string.boc_transfer_money_unit,"存款 ",
                        MoneyUtils.transMoneyFormat(String.valueOf(accountInfoViewModel.getAvailableBalance()),ApplicationConst.CURRENCY_CNY)));
            } else if (foreignCurrencyBillInfo.getCurrency().equals(accountInfoViewModel.getCurrencyCode())){//如果该卡有对应的外币币种

                if ("01".equals(accountInfoViewModel.getCashRemit())) {
                    rl_remain.setVisibility(View.VISIBLE);
                    foreingRemainFlag=true;
                    foreignCurrencyCashRemain=accountInfoViewModel.getAvailableBalance();
                    tvForeignRemainCash.setVisibility(View.VISIBLE);
                    tvForeignRemainCash.setText(getString(R.string.boc_creditcard_unit_cash_remit,"",PublicCodeUtils.getCurrency(mContext,accountInfoViewModel.getCurrencyCode()),
                            getString(R.string.boc_creditcard_unit_cash),
                            MoneyUtils.transMoneyFormat(String.valueOf(accountInfoViewModel.getAvailableBalance()), accountInfoViewModel.getCurrencyCode())));
                }
                if ("02".equals(accountInfoViewModel.getCashRemit())) {
                    rl_remain.setVisibility(View.VISIBLE);
                    foreingRemainFlag=true;
                    foreignCurrencyRemitRemain=accountInfoViewModel.getAvailableBalance();
                    tvForeignRemainRemit.setVisibility(View.VISIBLE);
                    tvForeignRemainRemit.setText(getString(R.string.boc_creditcard_unit_cash_remit,"",PublicCodeUtils.getCurrency(mContext,accountInfoViewModel.getCurrencyCode()),
                            getString(R.string.boc_creditcard_unit_remit),
                            MoneyUtils.transMoneyFormat(String.valueOf(accountInfoViewModel.getAvailableBalance()), accountInfoViewModel.getCurrencyCode())));
                }
            }
        }
        if (rl_remain.getVisibility()==View.GONE){
            showErrorDialog(getResources().getString(R.string.boc_transfer_account_no_money));
            return;
        }
        updateTabState();

    }

    @Override
    public void queryCrcdAccountDetails(List<CrcdAccountViewModel> crcdAccountViewModels) {
        closeProgressDialog();
        //遍历获取账户余额
        for (CrcdAccountViewModel accountInfoViewModel : crcdAccountViewModels) {
            if (ApplicationConst.CURRENCY_CNY.equals(accountInfoViewModel.getCurrency())) {
                //人民币账户非欠款状态
                rl_remain.setVisibility(View.VISIBLE);
                tvRMBRemain.setVisibility(View.VISIBLE);
                loacalCurrencyRemain=accountInfoViewModel.getLoanBalanceLimit();//取实时余额
                if (!"0".equals(accountInfoViewModel.getLoanBalanceLimitFlag())){
                    localRemainFlag=true;
                    tvRMBRemain.setText(getResources().getString(R.string.boc_transfer_money_unit,
                            "存款 ",
                            MoneyUtils.transMoneyFormat(String.valueOf(accountInfoViewModel.getLoanBalanceLimit()), ApplicationConst.CURRENCY_CNY)));
                }else {
                    localRemainFlag=false;
                    loacalCurrencyRemain=accountInfoViewModel.getLoanBalanceLimit();//取实时余额
                    tvRMBRemain.setText(getResources().getString(R.string.boc_transfer_money_unit,
                            "欠款 ",
                            MoneyUtils.transMoneyFormat(String.valueOf(accountInfoViewModel.getLoanBalanceLimit()), ApplicationConst.CURRENCY_CNY)));
                }

            }else if (foreignCurrencyBillInfo.getCurrency().equals(accountInfoViewModel.getCurrency())){//如果该卡有对应的外币币种

                rl_remain.setVisibility(View.VISIBLE);
                foreignCurrencyCashRemain=accountInfoViewModel.getLoanBalanceLimit();
                //信用卡只有钞没有汇
                if ("01".equals(accountInfoViewModel.getCashRemit())) {
                    tvForeignRemainCash.setVisibility(View.VISIBLE);
                    if (!"0".equals(accountInfoViewModel.getLoanBalanceLimitFlag())) {
                        foreingRemainFlag=true;
                        tvForeignRemainCash.setText(getString(R.string.boc_creditcard_unit_cash_remit, "存款 ",PublicCodeUtils.getCurrency(mContext, accountInfoViewModel.getCurrency()),
                                "",
                                MoneyUtils.transMoneyFormat(String.valueOf(accountInfoViewModel.getLoanBalanceLimit()), accountInfoViewModel.getCurrency())));
                    }else{
                        foreingRemainFlag=false;
                        tvForeignRemainCash.setText(getString(R.string.boc_creditcard_unit_cash_remit,"欠款 ", PublicCodeUtils.getCurrency(mContext, accountInfoViewModel.getCurrency()),
                                "",
                                MoneyUtils.transMoneyFormat(String.valueOf(accountInfoViewModel.getLoanBalanceLimit()), accountInfoViewModel.getCurrency())));
                    }
                }
            }
        }
        if (rl_remain.getVisibility()==View.GONE){
            showErrorDialog(getResources().getString(R.string.boc_transfer_account_no_money));
            return;
        }
        updateTabState();
    }

    @Override
    public void crcdForeignPayQuery(List<String> accountNoList) {
        if (accountNoList.contains(foreignCurrencyBillInfo.getCrcdAccNo())){
            isSupportExchenge=true;
        }else{
            isSupportExchenge=false;
        }
        updateTabState();
    }

    /**
     *  信用卡查询购汇还款信息
     */
    @Override
    public void crcdQueryForeignPayOffResult(BigDecimal payedAmt, BigDecimal exchangeRate) {
        this.payedAmt=payedAmt;
        this.exchangeRate=exchangeRate;
        initSelectTabRow(payedAmt);
        //查询该卡是否支持人民币购汇
        getPresenter().crcdForeignPayQuery(foreignCurrencyBillInfo.getCrcdAccNo());
        //查询账户余额
        queryAccountDetail();

        //初始化界面完毕
        isInited=true;
    }


    @Override
    public void checkPaymentInfoSucc(String crcdInvalidDate, String crcdOverdueFlag, String crcdState, final RepaymentInfoBean repaymentInfo) {
        if ("1".equals(crcdOverdueFlag)||!"".equals(crcdState)){
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
                    getPresenter().getBocTransCommCharge(repaymentInfo);
                }
            });
            dialog.show();
        }else {
            getPresenter().getBocTransCommCharge(repaymentInfo);
        }
    }

    /**
     *  试算查询结果回调
     */
    @Override
    public void transferCommissionChargeResult(TransCommissionChargeBean transCommissionChargeBean) {
        closeProgressDialog();

        //如果还款金额大于等于未还账单金额 则此外币账单设为已清
        repaymentInfoBean.setForeignBillClear((Double.parseDouble(repaymentInfoBean.getAmount().replace(",",""))-foreignCurrencyBillInfo.getHaveNotRepayAmout().doubleValue())>=0);

        RepaymentConfirmFragment confirmFragment= new RepaymentConfirmFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(RepaymentConfirmFragment.TRANS_COMMISSION_CHARGE_KEY,transCommissionChargeBean);
        bundle.putParcelable(RepaymentConfirmFragment.REPAYMENT_INFO_KEY,repaymentInfoBean);
        confirmFragment.setArguments(bundle);

        start(confirmFragment);
    }


    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode==SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT)
            if (requestCode==SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT){
                curAccountBean= data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                mChoiceAccount.setChoiceTextContent(NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()));

                showLoadingDialog();
                queryAccountDetail();
            }
    }

}
