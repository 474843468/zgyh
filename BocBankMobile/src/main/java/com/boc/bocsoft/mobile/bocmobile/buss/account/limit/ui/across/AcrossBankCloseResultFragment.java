package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.across;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;

/**
 * @author wangyang
 *         2016/11/1 16:35
 *         关闭跨行订购结果页
 */
@SuppressLint("ValidFragment")
public class AcrossBankCloseResultFragment extends BaseAccountFragment implements BaseOperationResultView.BtnCallback, OperationResultBottom.HomeBtnCallback {

    private LimitModel limitModel;

    public AcrossBankCloseResultFragment(LimitModel limitModel) {
        this.limitModel = limitModel;
    }

    private BaseOperationResultView borvResult;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_account_limit_result, null);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_loss_success_title);
    }

    @Override
    public void initView() {
        borvResult = (BaseOperationResultView) mContentView.findViewById(R.id.rv_result);
    }

    @Override
    public void initData() {
        borvResult.setTopDividerVisible(true);
        //设置头部信息
        if (limitModel.isOpen()) {
            borvResult.updateHead(OperationResultHead.Status.FAIL, getString(R.string.boc_account_across_bank_close_result_fail, limitModel.getAccountNumber()));
            return;
        }
        borvResult.updateHead(OperationResultHead.Status.SUCCESS, getString(R.string.boc_account_across_bank_close_result_success, limitModel.getAccountNumber()));

        if (!limitModel.isOpen())
            borvResult.addContentItem(getString(R.string.boc_account_across_bank_open), 0);
    }

    @Override
    public void setListener() {
        borvResult.setOnclick(this);
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    public void onClickListener(View v) {
        pop();
    }

    @Override
    public boolean onBack() {
        popTo(AcrossBankBuyFragment.class,false);
        findFragment(AcrossBankBuyFragment.class).refreshLimitModel(limitModel);
        return false;
    }

    @Override
    public boolean onBackPress() {
        popTo(AcrossBankBuyFragment.class,false);
        findFragment(AcrossBankBuyFragment.class).refreshLimitModel(limitModel);
        return true;
    }

    @Override
    public void onHomeBack() {
        mActivity.finish();
    }
}
