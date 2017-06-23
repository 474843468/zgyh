package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.apply;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.ClipBoardWidget;
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
 * 申请账户结果Fragment
 * Created by liuyang on 2016/6/13.
 */
@SuppressLint("ValidFragment")
public class VirtualCardApplyResultFragment extends BaseAccountFragment<VirCardPresenter> implements BaseOperationResultView.BtnCallback, OperationResultBottom.HomeBtnCallback, DetailTableRowButton.BtnCallback {

    /**
     * 申请Model
     */
    private VirtualCardModel model;
    /**
     * 结果页
     */
    private BaseOperationResultView borvResult;

    public VirtualCardApplyResultFragment(VirtualCardModel model) {
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
        borvResult.setOnclick(this);
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    public void initData() {
        borvResult.setTopDividerVisible(true);
        //设置头部信息
        if (model.isApplySuccess())
            borvResult.updateHead(OperationResultHead.Status.SUCCESS, getString(R.string.boc_account_apply_success));
        else
            borvResult.updateHead(OperationResultHead.Status.FAIL, getString(R.string.boc_account_apply_failed));


        //设置详情
        borvResult.addDetailRow(getString(R.string.boc_virtual_account_apply_result_account), NumberUtils.formatCardNumber(model.getAccountNumber()));
        borvResult.addDetailRow(getString(R.string.boc_virtual_account_apply_result_name), model.getAccountName());
        borvResult.addDetailRowBtn(getString(R.string.boc_virtual_account_apply_result_card), model.getAccountIbkNum(), "复制卡号", 0, this);

        String content = model.getStartDate().format(DateFormatters.dateFormatter1) + " ~ " + model.getEndDate().format(DateFormatters.dateFormatter1);
        borvResult.addDetailRow(getString(R.string.boc_virtual_account_apply_result_date_valid), content);
        borvResult.addDetailRow(getString(R.string.boc_virtual_account_apply_result_limit_single), PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getSignleLimit(), model.getCurrencyCode()));
        borvResult.addDetailRow(getString(R.string.boc_virtual_account_apply_result_limit_total), PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getTotalLimit(), model.getCurrencyCode()));

        borvResult.addContentItem(getString(R.string.boc_virtual_account_apply_result_button_sms), 0);
    }

    @Override
    public void onClickListener(View v) {
        getPresenter().psnCrcdVirtualCardSendMessage(model);
    }

    @Override
    public void onClickListener() {
        /**显示完整账号*/
        String cardNum = NumberUtils.formatCardNumber2(model.getAccountIbkNum());
        ClipBoardWidget clipBoardWidget = new ClipBoardWidget(mContext, cardNum);
        clipBoardWidget.setCopyBtnVisibility(true);
        clipBoardWidget.show();
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
