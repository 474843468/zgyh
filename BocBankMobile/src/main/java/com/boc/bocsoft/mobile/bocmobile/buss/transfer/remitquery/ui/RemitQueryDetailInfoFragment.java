package com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.ResultStatusConst;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowGroup;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SmsVerifyView;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.model.RemitQueryDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.model.ResetSendSmsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.presenter.RemitQueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.ResultStatusUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * 汇出查询 -- 详情页面
 * Created by wangf on 2016/6/28
 */
public class RemitQueryDetailInfoFragment extends MvpBussFragment<RemitQueryContract.Presenter> implements RemitQueryContract.DetailInfoView {

    private View rootView;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private DetailTableRowGroup detailTableRowGroup;
    //重发短信的Layout
    private RelativeLayout rlSmsVerify;
    //收款人手机号
    private TextView tvPayeeMobilePhone;
    // 获取手机验证码按钮
    protected SmsVerifyView btnVerifyCode;
    //底部按钮
    private Button btnBottom;

//    //汇出查询service通信处理类
//    private RemitQueryPresenter mRemitQueryPresenter;

    /**
     * 详情数据
     */
    private RemitQueryDetailInfoViewModel detailsInfoViewModel;

    //渠道标识
    private String channel;

    /**
     * 页面跳转
     */
    public static final int RESULT_CODE_DETAIL_INFO_SUCCESS = 202;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_account_transdetail_info, null);
        Bundle bundle = getArguments();
        detailsInfoViewModel = bundle.getParcelable("DetailsInfo");
        channel = bundle.getString("Channel");
        return rootView;
    }

    @Override
    public void initView() {
        detailTableHead = (DetailTableHead) rootView.findViewById(R.id.head_view);
        detailContentView = (DetailContentView) rootView.findViewById(R.id.body_view);
        detailTableRowGroup = (DetailTableRowGroup) rootView.findViewById(R.id.bottom_view);
        detailTableRowGroup.setVisibility(View.GONE);
        rlSmsVerify = (RelativeLayout) rootView.findViewById(R.id.rl_sms_verify);
        tvPayeeMobilePhone = (TextView) rootView.findViewById(R.id.tv_phone);
        btnVerifyCode = (SmsVerifyView) rootView.findViewById(R.id.btn_verify_code);
        btnBottom = (Button) rootView.findViewById(R.id.bottom_button);
    }

    @Override
    public void initData() {
//        //初始化汇款查询的presenter
//        mRemitQueryPresenter = new RemitQueryPresenter(this);

        // 当汇款状态为 OU,CR,L3,L6时，显示取消链接
        if (ResultStatusConst.STATUS_OU.equals(detailsInfoViewModel.getRemitStatus())
                || ResultStatusConst.STATUS_CR.equals(detailsInfoViewModel.getRemitStatus())
                || ResultStatusConst.STATUS_L3.equals(detailsInfoViewModel.getRemitStatus())
                || ResultStatusConst.STATUS_L6.equals(detailsInfoViewModel.getRemitStatus())) {
            btnBottom.setVisibility(View.VISIBLE);
            btnBottom.setText(getResources().getString(R.string.boc_transfer_remit_query_details_delete_confirm));
        }
        //当状态为OU时，显示重新发送短信链接
        if (ResultStatusConst.STATUS_OU.equals(detailsInfoViewModel.getRemitStatus())) {
            tvPayeeMobilePhone.setText(NumberUtils.formatMobileNumber(detailsInfoViewModel.getPayeeMobile()));
            rlSmsVerify.setVisibility(View.VISIBLE);
        }


        detailTableHead.updateData(getResources().getString(R.string.boc_transfer_mobile_remit_money) + "(" +
                        PublicCodeUtils.getCurrency(mContext, detailsInfoViewModel.getCurrencyCode()) + ")",
                MoneyUtils.transMoneyFormat(detailsInfoViewModel.getRemitAmount(), detailsInfoViewModel.getCurrencyCode()));
        detailTableHead.addDetail(getResources().getString(R.string.boc_transfer_atm_draw_query_details_account), detailsInfoViewModel.getFromName() + " " + NumberUtils.formatCardNumber(detailsInfoViewModel.getCardNo()));

        //当状态为 OK时,显示汇款编号
        if (ResultStatusConst.STATUS_OK.equals(detailsInfoViewModel.getRemitStatus())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_withdrawal_query_details_no), detailsInfoViewModel.getRemitNo());
        }
        // 将所有字段修改为 若为空则不显示 by wangf on 2016-10-28 09:05:43
        if (!StringUtils.isEmpty(detailsInfoViewModel.getRemitStatus())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_remit_query_details_remit_status),
                    ResultStatusUtils.getRemitStatus(mContext, detailsInfoViewModel.getRemitStatus()));
        }
        if (detailsInfoViewModel.getTranDate() != null) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_remit_query_details_remit_date), detailsInfoViewModel.getTranDate().format(
                    DateFormatters.dateFormatter1));
        }
        if (!StringUtils.isEmpty(detailsInfoViewModel.getPayeeName())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_mobile_withdraw_name), detailsInfoViewModel.getPayeeName());
        }
        if (!StringUtils.isEmpty(detailsInfoViewModel.getPayeeMobile())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_mobile_withdraw_phone), NumberUtils.formatMobileNumber(detailsInfoViewModel.getPayeeMobile()));
        }
        //去掉钞汇信息 by wangf on 2016/09/05
//        detailContentView.addDetailRow(getResources().getString(R.string.boc_account_detail_cash_remit), detailsInfoViewModel.getCashRemit());
        if (!StringUtils.isEmpty(detailsInfoViewModel.getRemark())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_comment), detailsInfoViewModel.getRemark());
        }
        //当交易状态为“OU-已汇款未收款/CR-解付冲正/L3-密码错3次锁定”时，显示到期日期
        if (ResultStatusConst.STATUS_OU.equals(detailsInfoViewModel.getRemitStatus())
                || ResultStatusConst.STATUS_CR.equals(detailsInfoViewModel.getRemitStatus())
                || ResultStatusConst.STATUS_L3.equals(detailsInfoViewModel.getRemitStatus())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_mobile_remit_info_date), detailsInfoViewModel.getDueDate().format(DateFormatters.dateFormatter1));
        }
        //当状态为 OK时，显示取款日期
        if (ResultStatusConst.STATUS_OK.equals(detailsInfoViewModel.getRemitStatus())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_withdrawal_query_details_date), detailsInfoViewModel.getReceiptDate().format(DateFormatters.dateFormatter1));
        }
        //当状态为 OK时，显示代理点名称
        if (ResultStatusConst.STATUS_OK.equals(detailsInfoViewModel.getRemitStatus())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_remit_query_details_agent_name), detailsInfoViewModel.getAgentName());
        }
        //当状态为 OK时，显示代理点编号
        if (ResultStatusConst.STATUS_OK.equals(detailsInfoViewModel.getRemitStatus())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_withdrawal_query_details_agentnum), detailsInfoViewModel.getAgentNum());
        }
//        detailContentView.addDetailRowNotLine("渠道标识", RemitStatusUtils.getRemitChannel(channel));

    }


    @Override
    public void setListener() {
        //撤销交易
        btnBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCancelTransDialog();

            }
        });

        //重新发送短信
        btnVerifyCode.setOnSmsActionListener(new SmsVerifyView.SmsActionListener() {
            @Override
            public void sendSms() {
                //请求 重新发送短信 接口
                showLoadingDialog();
                getPresenter().loadResetSendSms(buildResetSendSmsViewModel());
            }

            @Override
            public void onSmsReceived(String code) {

            }
        });
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_details_title);
    }


    /**
     * 显示确认撤销交易的对话框
     */
    private void showCancelTransDialog() {
        final TitleAndBtnDialog dialog = new TitleAndBtnDialog(mContext);
        dialog.isShowTitle(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setNoticeContent(getResources().getString(R.string.boc_transfer_remit_query_details_delete));
        dialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                getResources().getColor(R.color.boc_text_color_red),
                getResources().getColor(R.color.boc_text_color_red),
                getResources().getColor(R.color.boc_common_cell_color));
        dialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                getResources().getColor(R.color.boc_common_cell_color),
                getResources().getColor(R.color.boc_common_cell_color),
                getResources().getColor(R.color.boc_text_color_red));
        dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
                dialog.dismiss();
            }

            @Override
            public void onRightBtnClick(View view) {
                //请求 撤销交易 接口
                showLoadingDialog(false);
                getPresenter().loadCancelTrans(detailsInfoViewModel.getRemitNo());
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 封装页面数据 -- 重新发送短信
     *
     * @return
     */
    private ResetSendSmsViewModel buildResetSendSmsViewModel() {
        ResetSendSmsViewModel resetSendSmsViewModel = new ResetSendSmsViewModel();
        resetSendSmsViewModel.setFromName(detailsInfoViewModel.getFromName());
        resetSendSmsViewModel.setPayeeMobile(detailsInfoViewModel.getPayeeMobile());
        resetSendSmsViewModel.setRemitAmount(MoneyUtils.getRoundNumber(detailsInfoViewModel.getRemitAmount(), 2));
        resetSendSmsViewModel.setRemitCurrencyCode(detailsInfoViewModel.getCurrencyCode());
        resetSendSmsViewModel.setRemitNo(detailsInfoViewModel.getRemitNo());
        resetSendSmsViewModel.setRemitStatus(detailsInfoViewModel.getRemitStatus());

        return resetSendSmsViewModel;
    }


    /**
     * 重新发送短信 成功
     */
    @Override
    public void loadResetSendSmsSuccess() {
        closeProgressDialog();
    }

    /**
     * 重新发送短信 失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void loadResetSendSmsFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        btnVerifyCode.stopCountDown();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    /**
     * 撤销交易 成功
     */
    @Override
    public void loadCancelTransSuccess() {
        Toast.makeText(mContext, getResources().getString(R.string.boc_transfer_cancel_success), Toast.LENGTH_SHORT).show();
        closeProgressDialog();
        Bundle bundle = new Bundle();
        setFramgentResult(RESULT_CODE_DETAIL_INFO_SUCCESS, bundle);
        pop();
    }

    /**
     * 撤销交易 失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void loadCancelTransFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    @Override
    protected RemitQueryContract.Presenter initPresenter() {
        return new RemitQueryPresenter(this);
    }
}
