package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.limit;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirCardPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.overview.VirtualCardListFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * 修改交易限额结果
 * Created by liuyang on 2016/6/13.
 */
@SuppressLint("ValidFragment")
public class VirtualUpdateLimitResultFragment extends BaseAccountFragment<VirCardPresenter> implements OperationResultBottom.HomeBtnCallback {

    /**
     * Model
     */
    private VirtualCardModel model;
    /**
     * 结果页
     */
    private BaseOperationResultView borvResult;

    public VirtualUpdateLimitResultFragment(VirtualCardModel model) {
        super();
        this.model = model;
    }

    @Override
    protected VirCardPresenter initPresenter() {
        return new VirCardPresenter();
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
    }

    @Override
    public void setListener() {
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    public void initData() {
        borvResult.setTopDividerVisible(true);
        //设置头部信息
        borvResult.updateHead(OperationResultHead.Status.SUCCESS, getString(R.string.boc_virtual_account_detail_update_success));


        //设置详情
        borvResult.addDetailRow(getString(R.string.boc_virtual_account_detail_update_number), NumberUtils.formatCardNumber(model.getAccountNumber()));
        borvResult.addDetailRow(getString(R.string.boc_virtual_account_detail_update_account), NumberUtils.formatCardNumber2(model.getAccountIbkNum()));
        borvResult.addDetailRow(getString(R.string.boc_virtual_account_detail_update_account_name), model.getAccountName());

        String content = model.getStartDate().format(DateFormatters.dateFormatter1) + "~" + model.getEndDate().format(DateFormatters.dateFormatter1);
        borvResult.addDetailRow(getString(R.string.boc_virtual_account_apply_result_date_valid), content);
        borvResult.addDetailRow(getString(R.string.boc_virtual_account_detail_update_account_atotal), PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getAtotalLimit(), model.getCurrencyCode()));
        borvResult.addDetailRow(getString(R.string.boc_virtual_account_apply_result_limit_single), PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getSignleLimit(), model.getCurrencyCode()));
        borvResult.addDetailRow(getString(R.string.boc_virtual_account_apply_result_limit_total), PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getTotalLimit(), model.getCurrencyCode()));

    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(VirtualCardListFragment.class);
    }

    @Override
    public void onHomeBack() {
        mActivity.finish();
    }

    @Override
    public boolean onBack() {
        popToAndReInit(VirtualCardListFragment.class);
        return true;
    }
}
