package com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.ResultStatusConst;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowGroup;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model.ATMWithDrawDetailsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.presenter.ATMWithDrawPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.ResultStatusUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * ATM无卡取款查询详情
 * Created by liuweidong on 2016/6/25.
 */
public class ATMWithDrawQueryDetailsFragment extends MvpBussFragment<ATMWithDrawContract.Presenter> implements View.OnClickListener, ATMWithDrawContract.CancelView {
    private View rootView;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private DetailTableRowGroup detailTableRowGroup;
    private Button bottomBtn;

    private ATMWithDrawDetailsViewModel detailsInfo;// 详情数据
    private String curAccountNum;// 当前账户

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_account_transdetail_info, null);
        Bundle bundle = getArguments();
        detailsInfo = bundle.getParcelable("DetailsInfo");
        curAccountNum = bundle.getString("AccountNum");
        return rootView;
    }

    @Override
    public void initView() {
        detailTableHead = (DetailTableHead) rootView.findViewById(R.id.head_view);
        detailContentView = (DetailContentView) rootView.findViewById(R.id.body_view);
        detailTableRowGroup = (DetailTableRowGroup) rootView.findViewById(R.id.bottom_view);
        bottomBtn = (Button) rootView.findViewById(R.id.bottom_button);

        String statusStr = ResultStatusUtils.setResultStatus(mContext, detailsInfo.getStatus());
        detailTableHead.setHeadStatus(statusStr, ResultStatusUtils.changeTextBackground(statusStr));
        detailTableRowGroup.setVisibility(View.GONE);

        // 判断撤销按钮的显示（OU、CR、L3、L6）
        if (detailsInfo.getStatus().equals(ResultStatusConst.STATUS_OU)
                || detailsInfo.getStatus().equals(ResultStatusConst.STATUS_CR)
                || detailsInfo.getStatus().equals(ResultStatusConst.STATUS_L3)
                || detailsInfo.getStatus().equals(ResultStatusConst.STATUS_L6)) {
            bottomBtn.setVisibility(View.VISIBLE);
            bottomBtn.setText(getResources().getString(R.string.boc_transfer_details_button_text));
        }
    }

    @Override
    public void initData() {
        detailTableHead.updateData(getResources().getString(R.string.boc_transfer_atm_draw_query_details_amount), MoneyUtils.transMoneyFormat(detailsInfo.getPaymentAmount().toString(), detailsInfo.getPaymentCode()));
        detailTableHead.addDetail(getResources().getString(R.string.boc_transfer_mobile_remit_info_date),
                detailsInfo.getDueDate());

        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_atm_draw_num), detailsInfo.getRemitNumber());
//        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_atm_draw_query_details_name), detailsInfo.getToName());
        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_atm_draw_phone), NumberUtils.formatMobileNumber(detailsInfo.getToMobile()));
        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_atm_draw_query_details_date), detailsInfo.getPaymentDate());
        if (!StringUtils.isEmptyOrNull(detailsInfo.getComment())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_atm_draw_query_details_comment), detailsInfo.getComment());
        }
        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_atm_draw_account),
                NumberUtils.formatCardNumber(curAccountNum));
    }

    @Override
    public void setListener() {
        bottomBtn.setOnClickListener(this);
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_details_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected ATMWithDrawContract.Presenter initPresenter() {
        return new ATMWithDrawPresenter(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bottom_button) {
            final TitleAndBtnDialog dialog = new TitleAndBtnDialog(mContext);
            dialog.isShowTitle(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setNoticeContent(getResources().getString(R.string.boc_transfer_atm_draw_query_details_cancel_warn));
            dialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color));
            dialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_common_cell_color),
                    getResources().getColor(R.color.boc_text_color_red));
            dialog.show();
            dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                @Override
                public void onLeftBtnClick(View view) {
                    dialog.dismiss();
                }

                @Override
                public void onRightBtnClick(View view) {
                    dialog.dismiss();
                    // 网络请求
                    showLoadingDialog(false);
                    getPresenter().cancelATMWithDraw(detailsInfo);
                }
            });
        }
    }

    @Override
    public void cancelATMWithDrawSuccess() {
        Toast.makeText(mContext, getResources().getString(R.string.boc_transfer_cancel_success), Toast.LENGTH_SHORT).show();
        closeProgressDialog();
        Bundle bundle = new Bundle();
        setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, bundle);
        pop();
    }

    @Override
    public void cancelATMWithDrawFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }
}
