package com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.ResultStatusConst;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

/**
 * ATM无卡取款操作结果页面
 * Created by liuweidong on 2016/7/19.
 */
public class ATMWithDrawResultFragment extends BussFragment implements OperationResultBottom.HomeBtnCallback {
    private View rootView;
    private BaseOperationResultView operationResultView;
    private AccountBean curAccountBean;

    public static final int AGAIN = 0;// 继续取款
    public static final int RECORD = 1;// 取款记录
    public static final String isSuccess = "SUCCESS";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_finance_result, null);
        curAccountBean = getArguments().getParcelable("AccountBean");
        return rootView;
    }

    @Override
    public void initView() {
        operationResultView = (BaseOperationResultView) rootView.findViewById(R.id.rv_result);
        operationResultView.isShowBottomInfo(true);
    }

    @Override
    public void initData() {
        setResultStatus();
        String[] name = collectionName();
        String[] value = collectionValue();
        operationResultView.addHeadInfo(name[0], value[0]);// 取款编号
        operationResultView.addHeadInfo(name[1], value[1]);// 截止日期
        operationResultView.setDetailsName(getResources().getString(R.string.boc_transfer_details));
        operationResultView.addDetailRow(name[2], value[2], true, false);// 本人手机号
        if (!StringUtils.isEmptyOrNull(value[3])) {
            operationResultView.addDetailRow(name[3], value[3], true, false);// 附言
        }
        operationResultView.addDetailRow(name[4], value[4], true, false);// 取款账户
        operationResultView.addDetailRow(name[5], value[5], false, false);// 取款账户交易后余额

        operationResultView.addContentItem(getResources().getString(R.string.boc_common_again), AGAIN);// 继续取款
        operationResultView.addContentItem(getResources().getString(R.string.boc_common_record), RECORD);// 取款记录
    }

    @Override
    public void setListener() {
        operationResultView.setgoHomeOnclick(this);
        operationResultView.setOnclick(new BaseOperationResultView.BtnCallback() {
            @Override
            public void onClickListener(View v) {
                int i = v.getId();
                if (i == AGAIN) {
                    popToAndReInit(ATMWithDrawFragment.class);
                } else if (i == RECORD) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("AccountBean", curAccountBean);
                    ATMWithDrawQueryFragment fragment = new ATMWithDrawQueryFragment();
                    fragment.setStartFragment(true);
                    fragment.setArguments(bundle);
                    start(fragment);
                } else {
                }
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_fragment_result_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 设置交易结果状态
     */
    private void setResultStatus() {
        if (ResultStatusConst.STATUS_A.equals(ATMWithDrawFragment.getViewModel().getStatus())) {
            operationResultView.updateHead(OperationResultHead.Status.SUCCESS,
                    MoneyUtils.transMoneyFormat(ATMWithDrawFragment.getViewModel().getMoney(),
                            ATMWithDrawFragment.getViewModel().getCurrencyCode())
                            + getResources().getString(R.string.boc_transfer_atm_draw_result_success));
        } else {
            operationResultView.updateHead(OperationResultHead.Status.FAIL,
                    MoneyUtils.transMoneyFormat(ATMWithDrawFragment.getViewModel().getMoney(),
                            ATMWithDrawFragment.getViewModel().getCurrencyCode())
                            + getResources().getString(R.string.boc_transfer_atm_draw_result_fail));
        }
    }

    /**
     * 名称
     *
     * @return
     */
    private String[] collectionName() {
        String[] name = new String[6];
        name[0] = getResources().getString(R.string.boc_transfer_atm_draw_num);
        name[1] = getResources().getString(R.string.boc_transfer_mobile_remit_date);
        name[2] = getResources().getString(R.string.boc_transfer_atm_draw_phone);
        name[3] = getResources().getString(R.string.boc_transfer_comment);
        name[4] = getResources().getString(R.string.boc_transfer_atm_draw_account);
        name[5] = getResources().getString(R.string.boc_transfer_atm_draw_balance_money);
        return name;
    }

    /**
     * 数据
     *
     * @return
     */
    private String[] collectionValue() {
        String money = "";
        if (getArguments().getBoolean(isSuccess)) {
            money = ATMWithDrawFragment.getViewModel().getAvailableBalance().toPlainString();
        } else {
            money = ATMWithDrawFragment.getViewModel().getAvailableBalance().subtract(ATMWithDrawFragment.getViewModel().getMoney()).toPlainString();
        }
        String[] value = new String[6];
        value[0] = ATMWithDrawFragment.getViewModel().getRemitNo();
        value[1] = ATMWithDrawFragment.getViewModel().getDueDate();
        value[2] = NumberUtils.formatMobileNumber(ApplicationContext.getInstance().getUser().getMobile());
        value[3] = ATMWithDrawFragment.getViewModel().getFurInf();
        value[4] = NumberUtils.formatCardNumber(curAccountBean.getAccountNumber());
        value[5] = MoneyUtils.transMoneyFormat(money, ATMWithDrawFragment.getViewModel().getCurrencyCode());
        return value;
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(ATMWithDrawFragment.class);
    }

    @Override
    public void onHomeBack() {
        ActivityManager.getAppManager().finishActivity();
    }

    @Override
    public boolean onBack() {
        ActivityManager.getAppManager().finishActivity();
        return true;
    }

}
