package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui;

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
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * @author wangyang
 *         2016/10/18 11:20
 *         小额凭签名免密/境外磁条交易
 */
@SuppressLint("ValidFragment")
public class LimitFragment extends BaseAccountFragment<LimitTransactionPresenter> implements CompoundButton.OnCheckedChangeListener, TitleAndBtnDialog.DialogBtnClickCallBack, LimitContract.LimitCloseView, EditMoneyInputWidget.RightViewClickListener, View.OnClickListener {

    private LimitModel mainModel, otherModel;

    private TextView tvAccount, tvTitle;

    private CheckBox cbStatus;

    private EditMoneyInputWidget etLimit;

    private Button btnNext;

    private TitleAndBtnDialog closeDialog;

    public LimitFragment(LimitModel mainModel, LimitModel otherModel) {
        this.mainModel = mainModel;
        this.otherModel = otherModel;
    }

    @Override
    protected LimitTransactionPresenter initPresenter() {
        return new LimitTransactionPresenter(this);
    }

    /**
     * 初始化布局
     *
     * @param mInflater
     * @return
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_account_limit_info, null);
    }

    /**
     * 修改标题文字
     *
     * @return
     */
    @Override
    protected String getTitleValue() {
        return mainModel.getLimitType().getTitle(mContext);
    }

    @Override
    public void initView() {
        tvAccount = (TextView) mContentView.findViewById(R.id.tv_account);
        tvTitle = (TextView) mContentView.findViewById(R.id.tv_title);
        cbStatus = (CheckBox) mContentView.findViewById(R.id.cb_status);
        etLimit = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_limit);
        btnNext = (Button) mContentView.findViewById(R.id.btn_next);
    }

    @Override
    public void setListener() {
        cbStatus.setChecked(mainModel.isOpen());
        cbStatus.setOnCheckedChangeListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tvAccount.setText(getString(R.string.boc_account_limit_update_account, NumberUtils.formatCardNumber(mainModel.getAccountNumber())));

        tvTitle.setText(mainModel.getLimitType().getTitle(mContext));
        etLimit.setEditWidgetTitle(mainModel.getLimitType().getLimitTitle(mContext));

        if (mainModel.getQuota() != null)
            etLimit.setmContentMoneyEditText(mainModel.getQuota().toString());

        etLimit.setShowRightText(mainModel.isOpen(), this);
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

        LimitModel updateModel = ModelUtil.generateLimitModel(mainModel, quota);
        if (mainModel.isOpen()) {
            start(new LimitUpdateConfirmFragment(updateModel));
            return;
        }

        //进入开通服务
        start(new LimitOpenConfirmFragment(updateModel, otherModel));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!mainModel.isOpen())
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

    /**
     * 弹出关闭交易对话框
     */
    private void showCloseDialog() {
        if (closeDialog == null) {
            closeDialog = new TitleAndBtnDialog(getActivity());
            closeDialog.setBtnName(new String[]{getString(R.string.boc_common_cancel), getString(R.string.boc_common_sure)});
            closeDialog.setNoticeContent(getString(R.string.boc_account_limit_close_notice, NumberUtils.formatCardNumber(mainModel.getAccountNumber()), mainModel.getLimitType().getTitle(mContext)));
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
    public void onLeftBtnClick(View view) {
        cbStatus.setChecked(true);
        closeDialog.dismiss();
    }

    @Override
    public void onRightBtnClick(View view) {
        closeDialog.dismiss();
        showLoadingDialog();
        getPresenter().closeServicePre(mainModel);
    }

    @Override
    public void closeService(LimitModel limitModel) {
        closeProgressDialog();

        this.mainModel = limitModel;
        etLimit.setShowRightText(mainModel.isOpen(), this);
        if (mainModel.isOpen()) {
            ToastUtils.show(getString(R.string.boc_account_limit_close_fail));
            return;
        }
        ToastUtils.show(getString(R.string.boc_account_limit_close_success));
        findFragment(AccountLimitFragment.class).queryLimit(mainModel);
    }

    /**
     * 重新进入界面
     *
     * @param mainModel
     * @param otherModel
     */
    protected void reload(LimitModel mainModel, LimitModel otherModel) {
        showLoadingDialog();
        this.mainModel = mainModel;

        if (otherModel != null)
            this.otherModel = otherModel;
        initData();

        findFragment(AccountLimitFragment.class).queryLimit(mainModel);
        closeProgressDialog();
    }
}
