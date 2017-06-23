package com.boc.bocsoft.mobile.bocmobile.buss.account.loss.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.loss.model.LossViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.OverviewFragment;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * 临时挂失结果页面
 * Created by liuweidong on 2016/6/13.
 */
public class LossResultFragment extends BussFragment implements View.OnClickListener {

    private View rootView;
    private TextView txtLossStatus;// 挂失状态
    private TextView txtLossWarn;// 挂失提示信息
    private Button btnBackHome;// 返回首页

    private AccountBean curAccountBean;// 当前账户信息
    private LossViewModel mViewModel;// 页面数据

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_account_loss_success, null);
        Bundle bundle = getArguments();
        curAccountBean = bundle.getParcelable("AccountBean");
        mViewModel = (LossViewModel) bundle.getSerializable("LossViewModel");
        return rootView;
    }

    @Override
    public void initView() {
        txtLossStatus = (TextView) rootView.findViewById(R.id.txt_loss_status);
        txtLossWarn = (TextView) rootView.findViewById(R.id.txt_loss_warn);
        btnBackHome = (Button) rootView.findViewById(R.id.btn_back_home);
    }

    @Override
    public void initData() {
        if (curAccountBean != null) {
            switch (curAccountBean.getAccountType()) {
                case ApplicationConst.ACC_TYPE_BRO:// 借记卡
                    setDebitcardResultInfo();
                    break;
                default:// 活期一本通（其他）
                    setAccountResultInfo();
                    break;
            }
        }
    }

    @Override
    public void setListener() {
        btnBackHome.setOnClickListener(this);
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_account_loss_success_title);
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
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back_home) {
            mActivity.finish();
        }
    }

    @Override
    public boolean onBack() {
        popToAndReInit(OverviewFragment.class);
        return false;
    }

    @Override
    public boolean onBackPress() {
        popToAndReInit(OverviewFragment.class);
        return true;
    }

    /**
     * 设置借记卡的结果信息
     */
    private void setDebitcardResultInfo() {
        String resultMessage = "";//结果状态信息
        String resultWarn = "";//结果提示信息

        if (LossFragment.LOSS_TYPE_YES.equals(mViewModel.getAccFlozenFlag())) {// 冻结账户
            if (mViewModel.isReportLossStatus()) {// 挂失成功
                if (mViewModel.isAccFrozenStatus()) {// 冻结成功
                    resultMessage = getResources().getString(R.string.boc_account_loss_success_freeze_success);
                    if (LossFragment.LOSS_DAY_5.equals(mViewModel.getLossDays())) {// 5日
                        resultWarn = getResources().getString(R.string.boc_account_loss_success_freeze_success_5);
                    } else if (LossFragment.LOSS_DAY_FOREVER.equals(mViewModel.getLossDays())) {// 长期
                        resultWarn = getResources().getString(R.string.boc_account_loss_success_freeze_success_forever_start)
                                + mViewModel.getTime().format(DateFormatters.dateFormatter1)
                                + getResources().getString(R.string.boc_account_loss_success_freeze_success_forever_end);
                    }
                } else {// 冻结失败
                    resultMessage = getResources().getString(R.string.boc_account_loss_success_freeze_fail);
                    if (LossFragment.LOSS_DAY_5.equals(mViewModel.getLossDays())) {// 5日
                        resultWarn = getResources().getString(R.string.boc_account_loss_success_freeze_fail_5);
                    } else if (LossFragment.LOSS_DAY_FOREVER.equals(mViewModel.getLossDays())) {// 长期
                        resultWarn = getResources().getString(R.string.boc_account_loss_success_freeze_fail_forever_start)
                                + mViewModel.getTime().format(DateFormatters.dateFormatter1)
                                + getResources().getString(R.string.boc_account_loss_success_freeze_fail_forever_end);
                    }
                }
            } else {// 挂失失败
                if (mViewModel.isAccFrozenStatus()) {// 冻结成功
                    resultMessage = getResources().getString(R.string.boc_account_loss_fail_freeze_success);
                    resultWarn = getResources().getString(R.string.boc_account_loss_fail_freeze_success_warn);
                } else {// 冻结失败
                    Drawable select = getResources().getDrawable(R.drawable.boc_operator_fail);
                    txtLossStatus.setCompoundDrawablesWithIntrinsicBounds(null, select, null, null);
                    resultMessage = getResources().getString(R.string.boc_account_loss_fail_freeze_fail);
                }
            }
        } else {// 不冻结账户
            if (mViewModel.isReportLossStatus()) {
                resultMessage = getResources().getString(R.string.boc_account_loss_success);
                if (LossFragment.LOSS_DAY_5.equals(mViewModel.getLossDays())) {// 5日
                    resultWarn = getResources().getString(R.string.boc_account_loss_success_5);
                } else if (LossFragment.LOSS_DAY_FOREVER.equals(mViewModel.getLossDays())) {// 长期
                    resultWarn = getResources().getString(R.string.boc_account_loss_success_forever_start)
                            + mViewModel.getTime().format(DateFormatters.dateFormatter1)
                            + getResources().getString(R.string.boc_account_loss_success_forever_end);
                }
            } else {
                Drawable select = getResources().getDrawable(R.drawable.boc_operator_fail);
                txtLossStatus.setCompoundDrawablesWithIntrinsicBounds(null, select, null, null);
                resultMessage = getResources().getString(R.string.boc_account_loss_fail);
            }
        }

        txtLossStatus.setText(resultMessage);
        txtLossWarn.setText(resultWarn);
    }

    /**
     * 活期一本通的操作结果信息
     */
    private void setAccountResultInfo() {
        String resultWarn = "";
        if (LossFragment.LOSS_DAY_5.equals(mViewModel.getLossDays())) {// 5日
            resultWarn = getResources().getString(R.string.boc_account_freeze_success_5);
        } else if (LossFragment.LOSS_DAY_FOREVER.equals(mViewModel.getLossDays())) {// 长期
            resultWarn = getResources().getString(R.string.boc_account_freeze_success_forever_start)
                    + mViewModel.getTime().format(DateFormatters.dateFormatter1)
                    + getResources().getString(R.string.boc_account_freeze_success_forever_end);
        }
        txtLossStatus.setText(getResources().getString(R.string.boc_account_freeze_success));
        txtLossWarn.setText(resultWarn);
    }

    /**
     * 信用卡的操作结果信息
     */
    @Deprecated
    private void setCreditCardResultInfo() {
        if (LossFragment.LOSS_TYPE_NORMAL.equals(mViewModel.getSelectLossType())) {// 挂失

        } else if (LossFragment.LOSS_TYPE_REAPPLY_CARD.equals(mViewModel.getSelectLossType())) {// 挂失及补卡

        }
    }
}
