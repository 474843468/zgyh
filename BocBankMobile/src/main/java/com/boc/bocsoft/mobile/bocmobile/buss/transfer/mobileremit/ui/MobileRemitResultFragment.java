package com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareInfoFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.ui.RemitQueryFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

/**
 * 汇往取款人操作结果页面
 * Created by liuweidong on 2016/7/26.
 */
public class MobileRemitResultFragment extends BussFragment implements OperationResultBottom.HomeBtnCallback {
    private View rootView;
    private BaseOperationResultView operationResultView;
    private AccountBean curAccountBean;
    private String resultInfo;

    public static final int NOTIFY = 0;// 微信分享好友
    public static final int AGAIN = 1;// 继续汇款
    public static final int RECORD = 2;// 汇出查询

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
        resultInfo = MoneyUtils.transMoneyFormat(MobileRemitFragment.getViewModel().getMoney(),
                MobileRemitFragment.getViewModel().getCurrencyCode())
                + getResources().getString(R.string.boc_transfer_mobile_remit_result_success);
        operationResultView.updateHead(OperationResultHead.Status.SUCCESS, resultInfo);

        String[] name = collectionName();
        String[] value = collectionValue();
        operationResultView.setHeadInfo(getResources().getString(R.string.boc_transfer_mobile_remit_head_info));
        operationResultView.addHeadInfo(name[0], value[0]);// 截止日期
        operationResultView.setDetailsName(getResources().getString(R.string.boc_transfer_details));
        operationResultView.addDetailRow(name[1], value[1], true, false);// 收款人名称
        operationResultView.addDetailRow(name[2], value[2], true, false);// 收款人手机号
        if (!StringUtils.isEmptyOrNull(value[3])) {
            operationResultView.addDetailRow(name[3], value[3], true, false);// 附言
        }
        operationResultView.addDetailRow(name[4], value[4], true, false);// 付款账户
        operationResultView.addDetailRow(name[5], value[5], false, false);// 付款账户交易后余额

        operationResultView.addContentItem(getResources().getString(R.string.boc_common_notify), NOTIFY);
        operationResultView.addContentItem(getResources().getString(R.string.boc_common_again_remit), AGAIN);// 继续汇款
        operationResultView.addContentItem(getResources().getString(R.string.boc_common_remit_record), RECORD);// 汇出查询
    }

    @Override
    public void setListener() {
        operationResultView.setgoHomeOnclick(this);
        operationResultView.setOnclick(new BaseOperationResultView.BtnCallback() {
            @Override
            public void onClickListener(View v) {
                int i = v.getId();
                if (i == NOTIFY) {// 微信分享好友
                    String[] moneyInfo = {"汇款金额", MoneyUtils.transMoneyFormat(MobileRemitFragment.getViewModel().getMoney(),
                            MobileRemitFragment.getViewModel().getCurrencyCode()) + "元"};
                    String[] values = collectionValue();
                    values[values.length-1] = "";// 付款账户交易后余额分享不显示
                    ShareInfoFragment fragment = ShareInfoFragment.newInstance("汇款交易成功！", collectionName(),
                            values, moneyInfo);
                    fragment.setOther(true);
                    start(fragment);
                } else if (i == AGAIN) {// 再汇一笔
                    popToAndReInit(MobileRemitFragment.class);
                } else if (i == RECORD) {// 汇出查询
                    Bundle bundle = new Bundle();
                    bundle.putInt(RemitQueryFragment.REMIT_QUERY_TYPE, RemitQueryFragment.REMIT_ACCOUNT_TYPE_COMMON);
                    bundle.putParcelable(RemitQueryFragment.REMIT_ACCOUNT_BEAN, curAccountBean);
                    RemitQueryFragment fragment = new RemitQueryFragment();
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
     * 名称
     *
     * @return
     */
    private String[] collectionName() {
        String[] name = new String[6];
        name[0] = getResources().getString(R.string.boc_transfer_mobile_remit_date);
        name[1] = getResources().getString(R.string.boc_transfer_mobile_withdraw_name);
        name[2] = getResources().getString(R.string.boc_transfer_mobile_withdraw_phone);
        name[3] = getResources().getString(R.string.boc_transfer_comment);
        name[4] = getResources().getString(R.string.boc_transfer_mobile_remit_account);
        name[5] = getResources().getString(R.string.boc_transfer_mobile_remit_balance_money);
        return name;
    }

    /**
     * 数据
     *
     * @return
     */
    private String[] collectionValue() {
        String money = MobileRemitFragment.getViewModel().getAvailableBalance().subtract(MobileRemitFragment.getViewModel().getMoney()).toString();
        String[] value = new String[6];
        value[0] = MobileRemitFragment.getViewModel().getDueDate();
        value[1] = MobileRemitFragment.getViewModel().getPayeeName();
        value[2] = NumberUtils.formatMobileNumber(MobileRemitFragment.getViewModel().getPayeeMobile());
        value[3] = MobileRemitFragment.getViewModel().getRemark();
        value[4] = NumberUtils.formatCardNumber(curAccountBean.getAccountNumber());
        value[5] = MoneyUtils.transMoneyFormat(money, MobileRemitFragment.getViewModel().getCurrencyCode());
        return value;
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(MobileRemitFragment.class);
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
