package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransferModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter.FinanceContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter.FinanceTransferPresenter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.BalanceHint.BalanceHintView;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangyang
 *         16/7/5 15:06
 *         电子现金账户充值基础界面
 */
public abstract class FinanceTransferBaseFragment extends BaseAccountFragment<FinanceTransferPresenter> implements View.OnClickListener, FinanceContract.FinanceAccountRechargeInputView {

    /**
     * 信用卡类型
     */
    private final String[] ACCOUNT_CREDIT_TYPE = {ApplicationConst.ACC_TYPE_ZHONGYIN, ApplicationConst.ACC_TYPE_GRE};
    /**
     * 转出账户,转入账户信息
     */
    protected AccountBean expendAccount;
    /**
     * 充值金额输入控件
     */
    protected EditMoneyInputWidget editMoney;
    /**
     * 转出账户控件
     */
    private EditChoiceWidget editExpend;
    /**
     * 最大可充值金额提示,可用余额人民币提示
     */
    protected BalanceHintView hvMaxAmount, hvBalance;
    /**
     * 下一步按钮
     */
    private Button btnNext;
    private boolean balance = true;

    @Override
    public void initView() {
        editExpend = (EditChoiceWidget) mContentView.findViewById(R.id.card_expend);
        editMoney = (EditMoneyInputWidget) mContentView.findViewById(R.id.edit_amount);
        hvMaxAmount = (BalanceHintView) mContentView.findViewById(R.id.hv_amount);
        hvBalance = (BalanceHintView) mContentView.findViewById(R.id.hv_expend);
        btnNext = (Button) mContentView.findViewById(R.id.btn_next);
        hvMaxAmount.setDataColor(R.color.boc_text_color_rest_gray,R.color.boc_text_color_rest_gray);
        hvBalance.setDataColor(R.color.boc_text_color_rest_gray,R.color.boc_text_color_yellow);
        editMoney.setMaxLeftNumber(11);
        editMoney.setMaxRightNumber(2);

    }

    /**
     * 设置页面的默认焦点
     */
    private void setDefaultFocus() {
        editMoney.getTitleTextView().setFocusable(true);
        editMoney.getTitleTextView().setFocusableInTouchMode(true);
        editMoney.getTitleTextView().requestFocus();
    }
    @Override
    public void initData() {

        editMoney.setContentHint(getString(R.string.boc_transfer_hint_input));
        TextView tvMoney = editMoney.getContentMoneyEditText();
        tvMoney.setTextColor(mContext.getResources().getColor(R.color.boc_text_money_color_red));

        //获取转出账户第一个账户作为默认值
        List<AccountBean> accountBeans = ApplicationContext.getInstance().getChinaBankAccountList(Arrays.asList(SelectFinanceAccountFragment.ACCOUNT_EXPEND_TYPE));
        if(accountBeans.isEmpty()){
            isCanCloseLoadingDialog = true;
            return;
        }

        expendAccount = accountBeans.get(0);
        //发送请求,获取转出账户余额
        initExpendAccount();
    }

    @Override
    public void setListener() {
        btnNext.setOnClickListener(this);
        editExpend.setOnClickListener(this);
        editMoney.setOnKeyBoardListener(new EditMoneyInputWidget.KeyBoardDismissOrShowCallBack() {
            @Override
            public void onKeyBoardDismiss() {

            }

            @Override
            public void onKeyBoardShow() {
                setDefaultFocus();
            }
        });
    }

    @Override
    protected FinanceTransferPresenter initPresenter() {
        return new FinanceTransferPresenter(this);
    }

    /**
     * 转出账户不为信用卡,请求余额信息,否则显示转出账户
     */
    protected void initExpendAccount() {
        showLoadingDialog(false);
        editExpend.setChoiceTextName(getString(R.string.boc_finance_account_transfer_recharge_account));
        editExpend.setChoiceTextContent(NumberUtils.formatCardNumber(expendAccount.getAccountNumber()));
        if (Arrays.binarySearch(ACCOUNT_CREDIT_TYPE, expendAccount.getAccountType()) >= 0) {
            initExpendAccount(null,"");
        }else {
            getPresenter().psnBankAccount(expendAccount.getAccountId());
        }
    }

    /**
     * 设置转出账户标题,账号,余额
     *
     * @param availableBalance
     */
    protected void initExpendAccount(BigDecimal availableBalance,String currency) {

        if (Arrays.binarySearch(ACCOUNT_CREDIT_TYPE, expendAccount.getAccountType()) >= 0 || availableBalance == null) {
            hvBalance.setVisibility(View.GONE);
            balance = false;
        }else {
            hvBalance.setData(getString(R.string.boc_finance_account_transfer_recharge_balance), availableBalance,currency);
        }
        closeProgressDialog();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.card_expend){
            startForResult(new SelectFinanceAccountFragment(SelectFinanceAccountFragment.SELECT_ACCOUNT_EXPEND), SelectFinanceAccountFragment.SELECT_ACCOUNT_EXPEND);
        }else if(v.getId() == R.id.btn_next){
            //校验页面输入,生成转账详情,跳转确认页面
            if (checkCommit())
                start(new FinanceTransferConfirmFragment(generateTransModel()));
        }
    }

    /**
     * 生成转账信息Model
     *
     * @return
     */
    protected abstract TransferModel generateTransModel();

    /**
     * 校验输入规则
     *
     * @return
     */
    protected boolean checkCommit() {
        //校验转账金额
        if (StringUtils.isEmptyOrNull(editMoney.getContentMoney())) {
            showErrorDialog(getString(R.string.boc_finance_account_transfer_error_money));
            return false;
        }

        //校验转出账户
        if (expendAccount == null || StringUtils.isEmptyOrNull(expendAccount.getAccountId())) {
            showErrorDialog(getString(R.string.boc_finance_account_transfer_error_expend));
            return false;
        }
        //校验币种
        if (!ApplicationConst.CURRENCY_CNY.equals(expendAccount.getCurrencyCode())) {
            showErrorDialog(getString(R.string.boc_finance_account_transfer_error_money_expend));
            return false;
        }

        //校验转账金额不小于0,且是否大于余额
        double money = Double.parseDouble(editMoney.getContentMoney());
        if (money == 0) {
            showErrorDialog(getString(R.string.boc_finance_account_transfer_zero_money));
            return false;
        }
        if (balance) {
            if (hvBalance.getValue() < money && Arrays.binarySearch(ACCOUNT_CREDIT_TYPE, expendAccount.getAccountType()) < 0) {
                showErrorDialog(getString(R.string.boc_finance_account_transfer_error_out_balance));
                return false;
            }
        }
        return true;
    }

    /**
     * 选择账户页面关闭回调
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        Result数据
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        //结果不成功,跳过
        if (resultCode != SelectFinanceAccountFragment.RESULT_CODE_SELECT_ACCOUNT)
            return;

        //获取选择账户
        AccountBean accountBean = data.getParcelable(SelectFinanceAccountFragment.ACCOUNT_SELECT);
        switch (requestCode) {
            //如果为选择转出账户
            case SelectFinanceAccountFragment.SELECT_ACCOUNT_EXPEND:
                if (accountBean == expendAccount)
                    break;

                //缓存账号Id,请求余额
                expendAccount = accountBean;
                initExpendAccount();
                break;
        }
    }

    @Override
    public void psnFinanceAccount(FinanceModel financeModel) {
        throw new RuntimeException("请在子类实现该方法");
    }

    @Override
    public void psnFinanceExpendAccount(BigDecimal availableBalance,String currency) {
        initExpendAccount(availableBalance,currency);
    }

    @Override
    public void psnTransQuotaQuery(BigDecimal bigDecimal) {

    }
}
