package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.OverviewFragment;

/**
 * @author wangyang
 *         16/6/27 20:26
 *         充值结果页面
 */
@SuppressLint("ValidFragment")
public class FinanceSignResultFragment extends BaseAccountFragment implements BaseOperationResultView.BtnCallback, OperationResultBottom.HomeBtnCallback {

    /**
     * 充值结果Model
     */
    private FinanceModel financeModel;
    /**
     * 充值页面View
     */
    private BaseOperationResultView borvResult;

    public FinanceSignResultFragment(FinanceModel financeModel) {
        super();
        this.financeModel = financeModel;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_finance_result, null);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_loss_success_title);
    }

    @Override
    public void initView() {
        borvResult = (BaseOperationResultView) mContentView.findViewById(R.id.rv_result);
        borvResult.updateButtonStyle();
    }

    @Override
    public void setListener() {
        borvResult.setOnclick(this);
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    public void initData() {
        borvResult.setTopDividerVisible(true);
        //设置头部信息
        borvResult.updateHead(OperationResultHead.Status.SUCCESS, getString(R.string.boc_finance_account_sign_success));
        borvResult.setDetailVisibility(View.GONE);

        //设置按钮
        borvResult.addContentItem(getString(R.string.boc_finance_account_recharge),0);
    }

    @Override
    public void onClickListener(View v) {
        //跳转至页面
        start(new FinanceTransferSelfFragment(financeModel));
    }

    @Override
    public void onHomeBack() {
        mActivity.finish();
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
}
