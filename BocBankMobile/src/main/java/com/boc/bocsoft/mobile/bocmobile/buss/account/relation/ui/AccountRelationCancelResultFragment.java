package com.boc.bocsoft.mobile.bocmobile.buss.account.relation.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.OverviewFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * @author wangyang
 *         16/6/27 20:26
 *         取消关联结果页面
 */
@SuppressLint("ValidFragment")
public class AccountRelationCancelResultFragment extends BaseAccountFragment implements  OperationResultBottom.HomeBtnCallback {

    /** 取消关联Model */
    private AccountBean accountBean;
    /** 结果View */
    private BaseOperationResultView borvResult;

    public AccountRelationCancelResultFragment(AccountBean accountBean) {
        this.accountBean = accountBean;
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
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    public void initData() {
        //设置头部信息
        borvResult.updateHead(OperationResultHead.Status.SUCCESS,getString(R.string.boc_account_relation_cancel_result_success)+ NumberUtils.formatCardNumber(accountBean.getAccountNumber()));
        borvResult.setTopDividerVisible(true);

        //隐藏详情及底部按钮
        borvResult.setDetailVisibility(View.GONE);
        borvResult.setBodyBtnVisibility(View.VISIBLE);
        borvResult.addContentItem(getString(R.string.boc_account_name_content), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popToAndReInit(OverviewFragment.class);
            }
        });
    }

    @Override
    public boolean onBackPress() {
        popToAndReInit(OverviewFragment.class);
        return true;
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
}
