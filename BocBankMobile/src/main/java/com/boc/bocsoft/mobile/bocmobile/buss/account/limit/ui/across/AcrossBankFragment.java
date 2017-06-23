package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.across;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter.LimitContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter.LimitTransactionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.LimitOpenConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.LimitUpdateConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 跨行订购(账户条目详情界面)
 * Created by zhx on 2016/10/14
 */
@SuppressLint("ValidFragment")
public class AcrossBankFragment extends BaseAccountFragment<LimitTransactionPresenter> implements LimitContract.LimitCloseView, CompoundButton.OnCheckedChangeListener, View.OnClickListener, EditMoneyInputWidget.RightViewClickListener, TitleAndBtnDialog.DialogBtnClickCallBack {

    private LimitModel limitModel;

    private TextView tvTitle;

    private CheckBox cbStatus;

    private EditMoneyInputWidget etLimit;

    private Button btnNext;

    private TitleAndBtnDialog closeDialog;

    public AcrossBankFragment(LimitModel limitModel) {
        this.limitModel = limitModel;
    }

    @Override
    protected LimitTransactionPresenter initPresenter() {
        return new LimitTransactionPresenter(this);
    }

    @Override
    protected String getTitleValue() {
        return NumberUtils.formatCardNumber(limitModel.getAccountNumber());
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_across_bank, null);
    }

    @Override
    public void initView() {
        tvTitle = (TextView) mContentView.findViewById(R.id.tv_title);
        cbStatus = (CheckBox) mContentView.findViewById(R.id.cb_status);
        etLimit = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_limit);
        btnNext = (Button) mContentView.findViewById(R.id.btn_next);
    }

    @Override
    public void setListener() {
        cbStatus.setChecked(limitModel.isOpen());
        cbStatus.setOnCheckedChangeListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tvTitle.setText(limitModel.getLimitType().getTitle(mContext));
        etLimit.setEditWidgetTitle(limitModel.getLimitType().getLimitTitle(mContext));

        if (limitModel.getQuota() != null)
            etLimit.setmContentMoneyEditText(limitModel.getQuota().toString());

        if (limitModel.isOpen())
            etLimit.setShowRightText(true, this);
    }

    @Override
    public void onClick(View v) {
        if (StringUtils.isEmptyOrNull(etLimit.getContentMoney())) {
            showErrorDialog(getString(R.string.boc_account_limit_error));
            return;
        }

        double quota = Double.parseDouble(etLimit.getContentMoney());
        if (quota <= 0) {
            showErrorDialog(getString(R.string.boc_account_limit_error1));
            return;
        }

        LimitModel updateModel = ModelUtil.generateLimitModel(limitModel, quota);
        if (limitModel.isOpen()) {
            start(new LimitUpdateConfirmFragment(updateModel));
            return;
        }

        //进入开通服务
        start(new LimitOpenConfirmFragment(updateModel, limitModel));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!limitModel.isOpen())
                btnNext.setVisibility(View.VISIBLE);
            return;
        }

        btnNext.setVisibility(View.GONE);
        showCloseDialog();
    }

    @Override
    public void onRightClick(boolean isUpdateMode) {
        if (isUpdateMode)
            btnNext.setVisibility(View.VISIBLE);
        else
            btnNext.setVisibility(View.GONE);
    }

    @Override
    public void onLeftBtnClick(View view) {
        cbStatus.setChecked(true);
        closeDialog.dismiss();
    }

    @Override
    public void onRightBtnClick(View view) {
        closeDialog.dismiss();
        showLoadingDialog();
        getPresenter().closeServicePre(limitModel);
    }

    /**
     * 弹出关闭交易对话框
     */
    private void showCloseDialog() {
        if (closeDialog == null) {
            closeDialog = new TitleAndBtnDialog(getActivity());
            closeDialog.setBtnName(new String[]{getString(R.string.boc_common_cancel), getString(R.string.boc_common_sure)});
            closeDialog.setNoticeContent(getString(R.string.boc_account_limit_close_notice, NumberUtils.formatCardNumber(limitModel.getAccountNumber()), limitModel.getLimitType().getTitle(mContext)));
            closeDialog.setTitleBackground(getResources().getColor(R.color.boc_common_cell_color));
            closeDialog.setGravity(Gravity.CENTER_VERTICAL);
            closeDialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red));
            closeDialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color));
            closeDialog.setDialogBtnClickListener(this);
        }
        closeDialog.show();
    }


    @Override
    public void closeService(LimitModel limitModel) {
        closeProgressDialog();

        this.limitModel = limitModel;
        etLimit.setShowRightText(limitModel.isOpen(), this);
        if (limitModel.isOpen()) {
            ToastUtils.show(getString(R.string.boc_account_limit_close_fail));
            return;
        }

        initData();
        start(new AcrossBankCloseResultFragment(limitModel));
    }
}
