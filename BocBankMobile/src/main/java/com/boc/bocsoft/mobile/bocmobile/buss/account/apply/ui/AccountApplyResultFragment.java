package com.boc.bocsoft.mobile.bocmobile.buss.account.apply.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.model.ApplyAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.OverviewFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.HashMap;

/**
 * 申请账户结果Fragment
 * Created by liuyang on 2016/6/13.
 */
@SuppressLint("ValidFragment")
public class AccountApplyResultFragment extends BaseAccountFragment implements BaseOperationResultView.BtnCallback, OperationResultBottom.HomeBtnCallback {

    public final int TRANSFER = 0;
    public final int REGULAR = 1;


    /**
     * 申请Model
     */
    private ApplyAccountModel applyAccountModel;
    /**
     * 结果页
     */
    private BaseOperationResultView borvResult;

    public AccountApplyResultFragment(ApplyAccountModel applyAccountModel) {
        super();
        this.applyAccountModel = applyAccountModel;
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
        if (applyAccountModel.isSuccess())
            borvResult.updateHead(OperationResultHead.Status.SUCCESS, getString(R.string.boc_account_apply_success));
        else
            borvResult.updateHead(OperationResultHead.Status.FAIL, getString(R.string.boc_account_apply_failed));


        //设置详情
        borvResult.addDetailRow(getString(R.string.boc_accounts), NumberUtils.formatCardNumber(applyAccountModel.getAccountNewNumber()));
        borvResult.addDetailRow(getString(R.string.boc_account), NumberUtils.formatCardNumber(applyAccountModel.getAccountNumber()));
        borvResult.addDetailRow(getString(R.string.boc_accountTypeSMS), applyAccountModel.getAccountTypeSMS() + getString(R.string.boc_account_name));
        borvResult.addDetailRow(getString(R.string.boc_accountPurpose), applyAccountModel.getAccountPurposeString());

        //设置按钮
        if (applyAccountModel.getAccountType() == ApplicationConst.ACC_TYPE_REG)
            borvResult.addContentItem(getString(R.string.boc_regular_deposit_new), REGULAR);
        else
            borvResult.addContentItem(getString(R.string.boc_account_current_transfer), TRANSFER);
    }

    @Override
    public void onClickListener(View v) {
        switch (v.getId()) {
            case REGULAR:
                ModuleActivityDispatcher.dispatch(mActivity, ModuleCode.MODEUL_DEPTSTORAGE);
                break;
            case TRANSFER:
                ModuleActivityDispatcher.dispatch(mActivity, ModuleCode.MODULE_TRANSER_0000);
                break;
        }
    }

    @Override
    public void onHomeBack() {
        if (callBack != null) {
            callBack.callBack(generateCallBackParams(true), applyAccountModel.isSuccess());
            mActivity.finish();
            return;
        }
        mActivity.finish();
    }

    @Override
    public boolean onBack() {
        if (callBack != null) {
            callBack.callBack(generateCallBackParams(false), applyAccountModel.isSuccess());
            mActivity.finish();
            return false;
        }
        popToAndReInit(OverviewFragment.class);
        return false;
    }

    private Object generateCallBackParams(boolean isHome) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isHome", isHome);
        map.put("applyStatus", applyAccountModel.getApplyStatus());
        map.put("linkStatus", applyAccountModel.getLinkStatus());
        map.put("accountNum", applyAccountModel.getAccountNewNumber());
        return map;
    }

    @Override
    public boolean onBackPress() {
        if (callBack != null) {
            callBack.callBack(generateCallBackParams(false), applyAccountModel.isSuccess());
            mActivity.finish();
            return false;
        }
        popToAndReInit(OverviewFragment.class);
        return true;
    }
}
