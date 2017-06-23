package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.GlobalParams;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialog.MessageDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.loadingDialog.GlobalLoadingDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransferModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         16/6/23 15:49
 *         电子账户详情
 */
public class FinanceTransferOtherFragment extends FinanceTransferBaseFragment {

    /**
     * 收款人,转入账户,确认转入账户
     */
    private EditClearWidget etName, etIncome, etIncomeConfirm;
    private EditChoiceWidget exPend;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_finance_transfer_recharge_other, null);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_finance_account_recharge_other);
    }

    @Override
    public void initView() {
        super.initView();
        etName = (EditClearWidget) mContentView.findViewById(R.id.et_name);
        etIncome = (EditClearWidget) mContentView.findViewById(R.id.et_income);
        exPend = (EditChoiceWidget) mContentView.findViewById(R.id.card_expend);
        etIncomeConfirm = (EditClearWidget) mContentView.findViewById(R.id.et_income_confirm);
        etIncomeConfirm.setVisibility(View.GONE);
        etIncome.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);

    }

    @Override
    public void initData() {
        super.initData();
        hvMaxAmount.setTextContent(getString(R.string.boc_finance_account_recharge_other_money), getString(R.string.boc_finance_account_transfer_recharge_blance_money));
    }


    @Override
    protected TransferModel generateTransModel() {
        return ModelUtil.generateTransferModelOther(expendAccount, etIncome.getEditWidgetContent(), etName.getEditWidgetContent(), editMoney.getContentMoney());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            if (checkCommit()) {
                showLoadingDialog();
                getPresenter().psnTransQuotaQuery();
            }
        } else
            super.onClick(v);
    }

    @Override
    protected boolean checkCommit() {
        //校验收款人
        if (StringUtils.isEmptyOrNull(etName.getEditWidgetContent())) {
            showErrorDialog(getString(R.string.boc_finance_account_transfer_error_income_name));
            return false;
        }

        //校验转入账户
        if (StringUtils.isEmptyOrNull(etIncome.getEditWidgetContent())) {
            showErrorDialog(getString(R.string.boc_finance_account_transfer_error_income));
            return false;
        }

        //充值金额不能大于5000
        if (!StringUtils.isEmptyOrNull(editMoney.getContentMoney())) {
            double money = Double.parseDouble(editMoney.getContentMoney());
            if (money > 5000) {
                showErrorDialog(getString(R.string.boc_finance_account_recharge_other_max_money));
                return false;
            }
        }
        return super.checkCommit();
    }

    @Override
    public GlobalLoadingDialog showLoadingDialog() {
        isCanCloseLoadingDialog = true;
        return super.showLoadingDialog();
    }

    private MessageDialog messageDialog;

    @Override
    public void psnTransQuotaQuery(BigDecimal bigDecimal) {
        closeProgressDialog();

        if (bigDecimal == null || bigDecimal.doubleValue() <= GlobalParams.MAX_QUOTA){
            start(new FinanceTransferConfirmFragment(generateTransModel()));
            return;
        }

        showQuotaDialog();
    }

    private void showQuotaDialog() {
        if (messageDialog == null)
            messageDialog = new MessageDialog(mContext);

        messageDialog.setLeftButtonText(getString(R.string.boc_common_cancel));
        messageDialog.setRightButtonText(getString(R.string.boc_common_sure));
        messageDialog.setRightButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageDialog.dismiss();
                start(new FinanceTransferConfirmFragment(generateTransModel()));
            }
        });

        messageDialog.showDialog(getString(R.string.boc_account_limit_hint));
    }
}
