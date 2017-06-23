package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitType;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.across.AcrossBankBuyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.limit.LimitSetFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * @author wangyang
 *         2016/10/22 15:25
 *         小额/凭签名免密/境外磁条交易
 */
@SuppressLint("ValidFragment")
public class LimitResultFragment extends BaseAccountFragment implements BaseOperationResultView.BtnCallback, OperationResultBottom.HomeBtnCallback {

    private final int OPERATION_QUOTA = 1;

    private final int OPERATION_LIMIT = 2;

    private final int OPERATION_ACROSS = 3;

    private LimitModel mainModel,otherModel;

    public LimitResultFragment(LimitModel mainModel, LimitModel otherModel) {
        this.mainModel = mainModel;
        this.otherModel = otherModel;
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
        if (!mainModel.isOpen()){
            borvResult.updateHead(OperationResultHead.Status.FAIL, getString(R.string.boc_account_limit_result_fail,mainModel.getLimitType().getTitle(mContext)));
            return;
        }
        borvResult.updateHead(OperationResultHead.Status.SUCCESS, getString(R.string.boc_account_limit_result_success,mainModel.getLimitType().getTitle(mContext)));

        if(!StringUtils.isEmptyOrNull(mainModel.getCifMobile())){
            borvResult.isShowOther(true,getString(R.string.boc_account_limit_result_mobile_title));
            borvResult.addDetailRow(getString(R.string.boc_account_limit_result_mobile), NumberUtils.formatMobileNumberWithAsterrisk(mainModel.getCifMobile()));
        }else
            borvResult.isShowOther(true,getString(R.string.boc_account_limit_result_mobile_title_no));

        borvResult.addDetailRow(getString(R.string.boc_account_limit_account),NumberUtils.formatCardNumber(mainModel.getAccountNumber()));
        borvResult.addDetailRow(mainModel.getLimitType().getLimitTitle(mContext), MoneyUtils.transMoneyFormat(mainModel.getQuota(), ApplicationConst.CURRENCY_CNY));

        if(mainModel.getLimitType() != LimitType.ACROSS){
            borvResult.addContentItem(getString(R.string.boc_account_limit_result_operation_quota),OPERATION_QUOTA);
            if(!otherModel.isOpen())
                borvResult.addContentItem(getString(R.string.boc_account_limit_result_operation_limit,otherModel.getLimitType().getTitle(mContext)),OPERATION_LIMIT);
        }else{
            borvResult.addContentItem(getString(R.string.boc_account_limit_result_operation_across),OPERATION_ACROSS);
        }
    }

    @Override
    public void setListener() {
        borvResult.setOnclick(this);
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    public void onClickListener(View v) {
        switch (v.getId()){
            case OPERATION_QUOTA:
                popFragments(LimitFragment.class,this.getClass());
                start(new LimitSetFragment(ModelUtil.generateQuotaModel(mainModel)));
                break;
            case OPERATION_LIMIT:
                goLimitFragment(otherModel,mainModel);
                break;
            case OPERATION_ACROSS:
                goAccountLimit();
                break;
        }
    }

    private void goLimitFragment(LimitModel mainModel, LimitModel otherModel) {
        popTo(LimitFragment.class,false);
        findFragment(LimitFragment.class).reload(mainModel,otherModel);
    }

    @Override
    public boolean onBack() {
        goAccountLimit();
        return false;
    }

    private void goAccountLimit() {
        switch (mainModel.getLimitType()){
            case ACROSS:
                popTo(AcrossBankBuyFragment.class,false);
                findFragment(AcrossBankBuyFragment.class).refreshLimitModel(mainModel);
                break;
            default:
                popTo(AccountLimitFragment.class,false);
                findFragment(AccountLimitFragment.class).queryLimit(mainModel);
                break;
        }
    }

    @Override
    public boolean onBackPress() {
        goAccountLimit();
        return true;
    }

    @Override
    public void onHomeBack() {
        mActivity.finish();
    }
}
