package com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.model.MobileWithdrawViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.ui.WithdrawalQueryFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

/**
 * 代理点取款操作结果页
 * Created by liuweidong on 2016/8/4.
 */
public class MobileWithdrawResultFragment extends BussFragment implements OperationResultBottom.HomeBtnCallback {
    private View rootView;
    private BaseOperationResultView operationResultView;
    private MobileWithdrawViewModel mViewModel;
    private String resultInfo;// 结果信息

    public static final int NOTIFY = 0;// 微信通知好友
    public static final int AGAIN = 1;// 再取一笔
    public static final int RECORD = 2;// 取款记录

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_finance_result, null);
        mViewModel = (MobileWithdrawViewModel) getArguments().getSerializable("MobileWithdrawViewModel");
        return rootView;
    }

    @Override
    public void initView() {
        operationResultView = (BaseOperationResultView) rootView.findViewById(R.id.rv_result);
        operationResultView.isShowBottomInfo(true);
    }

    @Override
    public void initData() {
        resultInfo = MoneyUtils.transMoneyFormat(String.valueOf(mViewModel.getRemitAmount()),
                mViewModel.getCurrencyCode()) + getResources().getString(R.string.boc_transfer_mobile_withdraw_result_success);
        operationResultView.updateHead(OperationResultHead.Status.SUCCESS, resultInfo);

        String[] name = collectionName();
        String[] value = collectionValue();
//        operationResultView.addHeadInfo(name[0], value[0]);
        operationResultView.setDetailsName(getResources().getString(R.string.boc_transfer_details));
        operationResultView.addDetailRow(name[0], value[0], true, false);// 汇款编号
        operationResultView.addDetailRow(name[1], value[1], true, false);// 收款人名称
        operationResultView.addDetailRow(name[2], value[2], true, false);// 收款人手机号
        if (!StringUtils.isEmptyOrNull(value[3])) {
            operationResultView.addDetailRow(name[3], value[3], false, false);// 附言
        }

        // 微信通知好友
//        operationResultView.addContentItem(getResources().getString(R.string.boc_common_notify), NOTIFY);
        // 再取一笔
//        operationResultView.addContentItem(getResources().getString(R.string.boc_common_again), AGAIN);
        operationResultView.addContentItem(getResources().getString(R.string.boc_transfer_withdrawal_query_title), RECORD);// 取款查询
    }

    @Override
    public void setListener() {
        operationResultView.setgoHomeOnclick(this);

        operationResultView.setOnclick(new BaseOperationResultView.BtnCallback() {
            @Override
            public void onClickListener(View v) {
                int i = v.getId();
                if (i == RECORD) {// 取款查询
                    Bundle bundle = new Bundle();
                    bundle.putInt(WithdrawalQueryFragment.WITHDRAWAL_QUERY_TYPE, WithdrawalQueryFragment.WITHDRAWAL_TYPE_COMMON);
                    WithdrawalQueryFragment fragment = new WithdrawalQueryFragment();
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
        String[] name = new String[4];
        name[0] = getResources().getString(R.string.boc_transfer_mobile_withdraw_num);
        name[1] = getResources().getString(R.string.boc_transfer_mobile_withdraw_name);
        name[2] = getResources().getString(R.string.boc_transfer_mobile_withdraw_phone);
        name[3] = getResources().getString(R.string.boc_transfer_comment);
        return name;
    }

    /**
     * 数据
     *
     * @return
     */
    private String[] collectionValue() {
        String[] value = new String[4];
        value[0] = mViewModel.getRemitNo();
        value[1] = mViewModel.getPayeeName();
        value[2] = NumberUtils.formatMobileNumber(mViewModel.getPayeeMobile());
        value[3] = mViewModel.getRemark();
        return value;
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(MobileWithdrawFragment.class);
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
