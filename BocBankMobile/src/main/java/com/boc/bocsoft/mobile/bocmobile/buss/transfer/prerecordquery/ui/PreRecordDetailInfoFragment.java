package com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.ResultStatusConst;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowGroup;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.model.PreRecordDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.presenter.PreRecordPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.ResultStatusUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * 预约管理 -- 预约交易详情查询
 * Created by wangf on 2016/7/22.
 */
public class PreRecordDetailInfoFragment extends MvpBussFragment<PreRecordContract.Presenter> implements PreRecordContract.DetailInfoView {
    private View rootView;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private DetailTableRowGroup detailTableRowGroup;
    //底部按钮
    private Button btnBottom;

//    //预约查询service通信处理类
//    private PreRecordPresenter mPreRecordPresenter;

    //详情数据
    private PreRecordDetailInfoViewModel detailInfoViewModel;
    private String dateType;

    //页面跳转
    public static final int RESULT_CODE_DETAIL_INFO_SUCCESS = 202;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_account_transdetail_info, null);
        Bundle bundle = getArguments();
        detailInfoViewModel = bundle.getParcelable("DetailsInfo");
        dateType = bundle.getString("DateType");
        return rootView;
    }

    @Override
    public void initView() {
        detailTableHead = (DetailTableHead) rootView.findViewById(R.id.head_view);
        detailContentView = (DetailContentView) rootView.findViewById(R.id.body_view);
        detailTableRowGroup = (DetailTableRowGroup) rootView.findViewById(R.id.bottom_view);
        detailTableRowGroup.setVisibility(View.GONE);
        btnBottom = (Button) rootView.findViewById(R.id.bottom_button);
        btnBottom.setBackgroundColor(getResources().getColor(R.color.boc_common_cell_color));
        btnBottom.setTextColor(getResources().getColor(R.color.boc_text_color_red));
    }

    @Override
    public void initData() {
//        //初始化汇款查询的presenter
//        mPreRecordPresenter = new PreRecordPresenter(this);

        detailTableHead.updateData("转账金额" + "(" + PublicCodeUtils.getCurrency(mContext, detailInfoViewModel.getCurrency()) + ")",
                MoneyUtils.transMoneyFormat(detailInfoViewModel.getAmount(), detailInfoViewModel.getCurrency()));
        String status = ResultStatusUtils.getPreRecordStatus(mContext, false, detailInfoViewModel.getStatus());
        detailTableHead.setHeadStatus(status, ResultStatusUtils.changeTextBackground(status));
        detailTableHead.addDetail("收款账号", NumberUtils.formatCardNumberStrong(detailInfoViewModel.getPayeeAccountNumber()));

        if (!StringUtils.isEmpty(detailInfoViewModel.getPayeeIbknum())) {
            detailContentView.addDetailRow("收款账户所属地区", PublicCodeUtils.getTransferIbk(mContext, detailInfoViewModel.getPayeeIbknum()));
        }
        if (!StringUtils.isEmpty(detailInfoViewModel.getPayeeBankName())) {
            detailContentView.addDetailRow("收款账户开户行", detailInfoViewModel.getPayeeBankName());
        }
        detailContentView.addDetailRow("预约类型", getStrTransMode(detailInfoViewModel.getTransMode()));
        detailContentView.addDetailRow("预约日期", detailInfoViewModel.getFirstSubmitDate().format(
                DateFormatters.dateFormatter1));
        detailContentView.addDetailRow("执行日期", detailInfoViewModel.getPaymentDate().format(DateFormatters.dateFormatter1));

        //TransMode对应转账方式，0立即执行，1预约日期，2预约周期
        if ("2".equals(detailInfoViewModel.getTransMode())) {
            if (!StringUtils.isEmpty(detailInfoViewModel.getAllAmount())) {
                detailContentView.addDetailRow("需要执行次数", detailInfoViewModel.getAllAmount());
            }
            if (!StringUtils.isEmpty(detailInfoViewModel.getExecuteAmount())) {
                detailContentView.addDetailRow("已执行次数", detailInfoViewModel.getExecuteAmount());
            }
            if (!StringUtils.isEmpty(detailInfoViewModel.getNotExecuteAmount())) {
                detailContentView.addDetailRow("待执行次数", detailInfoViewModel.getNotExecuteAmount());
            }
        }
        if (!StringUtils.isEmpty(detailInfoViewModel.getFurInfo())) {
            detailContentView.addDetailRow("附言", detailInfoViewModel.getFurInfo());
        }
        detailContentView.addDetailRow("付款账户", NumberUtils.formatCardNumberStrong(detailInfoViewModel.getPayerAccountNumber()));
        detailContentView.addDetailRow("付款账户所属地区", PublicCodeUtils.getTransferIbk(mContext, detailInfoViewModel.getPayerIbknum()));
        detailContentView.addDetailRow("交易渠道", ResultStatusUtils.getTransChannel(mContext, detailInfoViewModel.getChannel()));
        detailContentView.addDetailRow("交易序号", detailInfoViewModel.getTransactionId());
        detailContentView.addDetailRow("批次号", detailInfoViewModel.getBatSeq());

        //在交易状态值为提交成功且当天不是执行日期的前提下，这条记录可被删除
        if (ResultStatusConst.STATUS_I.equals(detailInfoViewModel.getStatus()) ||
                ResultStatusConst.STATUS_7.equals(detailInfoViewModel.getStatus())) {
            if (!ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().isEqual(detailInfoViewModel.getPaymentDate())) {
                btnBottom.setVisibility(View.VISIBLE);
                btnBottom.setText("删除");
            }
        }

    }


    /**
     * 将预约类型转换为字符串
     *
     * @param mode
     * @return
     */
    private String getStrTransMode(String mode) {
        String strTransMode = "";
        if (StringUtils.isEmpty(mode)) {
            return "";
        }
        if ("0".equals(mode)) {
            strTransMode = "立即执行";
        } else if ("1".equals(mode)) {
            strTransMode = "预约日期执行";
        } else if ("2".equals(mode)) {
            strTransMode = "预约周期执行";
        }
        return strTransMode;
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
        dialog.setNoticeContent(getResources().getString(R.string.boc_transfer_prerecord_query_details_delete));
        dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
                dialog.dismiss();
            }

            @Override
            public void onRightBtnClick(View view) {
                //请求 撤销交易 接口
                showLoadingDialog(false);
                getPresenter().loadDeletePreRecord(detailInfoViewModel.getBatSeq(), dateType, detailInfoViewModel.getTransactionId());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void loadDeletePreRecordSuccess() {
        Toast.makeText(mContext, getResources().getString(R.string.boc_transfer_cancel_success), Toast.LENGTH_SHORT).show();
        closeProgressDialog();
        Bundle bundle = new Bundle();
        setFramgentResult(RESULT_CODE_DETAIL_INFO_SUCCESS, bundle);
        pop();
    }

    @Override
    public void loadDeletePreRecordFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }


    @Override
    protected PreRecordContract.Presenter initPresenter() {
        return new PreRecordPresenter(this);
    }
}
