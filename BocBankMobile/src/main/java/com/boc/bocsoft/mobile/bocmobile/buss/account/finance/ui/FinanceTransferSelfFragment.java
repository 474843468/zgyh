package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransferModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * @author wangyang
 *         16/6/23 15:49
 *         电子账户详情
 */
@SuppressLint("ValidFragment")
public class FinanceTransferSelfFragment extends FinanceTransferBaseFragment {

    /**
     * 电子信息账户信息
     */
    private FinanceModel financeModel;
    /**
     * 转入账户控件
     */
    private EditChoiceWidget inCome;

    /**
     * 初始化传入账户信息
     *
     * @param financeModel
     */
    public FinanceTransferSelfFragment(FinanceModel financeModel) {
        this.financeModel = financeModel;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_finance_transfer_recharge_self, null);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_finance_account_is_self);
    }

    @Override
    public void setListener() {
        super.setListener();
    }

    @Override
    public void initView() {
        super.initView();
        inCome = (EditChoiceWidget) mContentView.findViewById(R.id.in_come);
    }

    @Override
    public void initData() {
        isCanCloseLoadingDialog = false;
        //获取转入账户信息
        getPresenter().psnFinanceAccount(financeModel);
        super.initData();
    }

    /**
     * 设置转入账户信息
     */
    private void initIncomeAccount() {
        if (financeModel != null) {
            if (financeModel.getMaxLoadingAmount() == null){
                hvMaxAmount.setVisibility(View.GONE);
            }else {
                hvMaxAmount.setVisibility(View.VISIBLE);
                //设置充值金额提示
                hvMaxAmount.setData(getString(R.string.boc_finance_account_transfer_recharge_max), financeModel.getMaxLoadingAmount());
            }
            //设置转入账户
            inCome.setChoiceTextContent(NumberUtils.formatCardNumber(financeModel.getFinanICNumber()));
            inCome.getChoiceWidgetArrowImageView().setVisibility(View.GONE);

        } else {
            hvMaxAmount.setVisibility(View.GONE);
        }
        closeProgressDialog();
    }

    @Override
    protected TransferModel generateTransModel() {
        return ModelUtil.generateTransferModelSelf(expendAccount, financeModel, editMoney.getContentMoney());
    }

    @Override
    protected boolean checkCommit() {
        //充值金额不能大于电子现金账户的MaxLoadingAmount
        if (!StringUtils.isEmptyOrNull(editMoney.getContentMoney())) {
            double money = Double.parseDouble(editMoney.getContentMoney());
            if (financeModel.getMaxLoadingAmount() != null) {
                if (financeModel.getMaxLoadingAmount().doubleValue() < money) {
                    showErrorDialog(getString(R.string.boc_finance_account_transfer_can_max));
                    return false;
                }
            } else {
                return true;
            }
        }
        return super.checkCommit();
    }

    /**
     * 选择转入或者转出账户回调
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        Result数据
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode != SelectFinanceAccountFragment.RESULT_CODE_SELECT_ACCOUNT)
            return;

        //获取选择的账户
        AccountBean accountBean = data.getParcelable(SelectFinanceAccountFragment.ACCOUNT_SELECT);
        switch (requestCode) {
            //获取转入账户
            case SelectFinanceAccountFragment.SELECT_ACCOUNT_INCOME:
                //开启对话框,由于不是同步请求多个接口,所以设置关闭状态
                showLoadingDialog(false);
                getPresenter().psnFinanceAccount(ModelUtil.generateFinanceModel(accountBean));
                break;
            //获取转出账户
            default:
                super.onFragmentResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * 查询转入电子现金银行账户回调--对应从主页跳转来的场景
     *
     * @param financeModel
     */
    @Override
    public void psnFinanceAccount(FinanceModel financeModel) {
        this.financeModel = financeModel;

        //设置转入账户信息
        initIncomeAccount();
    }
}
