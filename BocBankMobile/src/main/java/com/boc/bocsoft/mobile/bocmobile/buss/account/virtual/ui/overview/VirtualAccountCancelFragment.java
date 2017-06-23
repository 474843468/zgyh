package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.overview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirCardPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirtualCardContract;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * @author wangyang
 *         16/8/26 17:04
 *         虚拟卡注销
 */
@SuppressLint("ValidFragment")
public class VirtualAccountCancelFragment extends BaseAccountFragment<VirCardPresenter> implements View.OnClickListener, TitleAndBtnDialog.DialogBtnClickCallBack,VirtualCardContract.VirCardCancelView {

    private DetailTableRow dtrAccount,dtrNumber, dtrName, dtrValid, dtrSingleLimit, dtrTotalLimit;

    private Button btnOk;

    private VirtualCardModel model;

    private TitleAndBtnDialog cancelDialog;

    public VirtualAccountCancelFragment(VirtualCardModel model) {
        this.model = model;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_cancel);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_virtual_cancel, null);
    }

    @Override
    public void initView() {
        super.initView();
        dtrAccount = (DetailTableRow) mContentView.findViewById(R.id.dtr_account);
        dtrNumber = (DetailTableRow) mContentView.findViewById(R.id.dtr_number);
        dtrName = (DetailTableRow) mContentView.findViewById(R.id.dtr_name);
        dtrValid = (DetailTableRow) mContentView.findViewById(R.id.dtr_valid);
        dtrSingleLimit = (DetailTableRow) mContentView.findViewById(R.id.dtr_single_limit);
        dtrTotalLimit = (DetailTableRow) mContentView.findViewById(R.id.dtr_total_limit);
        btnOk = (Button) mContentView.findViewById(R.id.btn_ok);

        int height = getResources().getDimensionPixelOffset(R.dimen.boc_button_height_96px);
        dtrAccount.setBodyHeight(height);
        dtrNumber.setBodyHeight(height);
        dtrName.setBodyHeight(height);
        dtrValid.setBodyHeight(height);
        dtrSingleLimit.setBodyHeight(height);
        dtrTotalLimit.setBodyHeight(height);
    }

    @Override
    public void setListener() {
        super.setListener();
        btnOk.setOnClickListener(this);
    }

    @Override
    protected VirCardPresenter initPresenter() {
        return new VirCardPresenter(this);
    }

    @Override
    public void initData() {
        dtrAccount.updateValue(NumberUtils.formatCardNumber(model.getAccountNumber()));
        dtrNumber.updateValue(NumberUtils.formatCardNumber2(model.getAccountIbkNum()));
        dtrName.updateValue(model.getAccountName());
        dtrValid.updateValue(model.getStartDate().format(
                DateFormatters.dateFormatter1) + "~" + model.getEndDate().format(DateFormatters.dateFormatter1));
        dtrSingleLimit.updateValue(PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getSignleLimit(), model.getCurrencyCode()));
        dtrTotalLimit.updateValue(PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getTotalLimit(), model.getCurrencyCode()));
    }

    @Override
    public void onClick(View v) {
        if (cancelDialog == null) {
            cancelDialog = new TitleAndBtnDialog(getActivity());
            cancelDialog.setBtnName(new String[]{getString(R.string.boc_common_cancel), getString(R.string.boc_common_sure)});
            cancelDialog.setNoticeContent(getString(R.string.boc_virtual_account_cancel_dialog_content));
            cancelDialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color));
            cancelDialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red));
            cancelDialog.setDialogBtnClickListener(this);
        }
        cancelDialog.show();
    }

    @Override
    public void onLeftBtnClick(View view) {
        cancelDialog.dismiss();
    }

    @Override
    public void onRightBtnClick(View view) {
        showLoadingDialog();
        cancelDialog.dismiss();
        getPresenter().psnCrcdVirtualCardCancel(model);
    }

    @Override
    public void psnCrcdVirtualCardCancel(boolean isCancel) {
        closeProgressDialog();
        ToastUtils.show(getString(R.string.boc_virtual_account_cancel_success));
        popToAndReInit(VirtualCardListFragment.class);
    }
}
