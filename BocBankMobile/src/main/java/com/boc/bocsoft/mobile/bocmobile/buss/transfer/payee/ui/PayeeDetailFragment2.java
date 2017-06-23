package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditDialogWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeDeletePayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeModifyAliasViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeModifyMobileViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransManagePayeeQueryPayeeListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.presenter.PayeeDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransRemitBlankFragment;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.List;

/**
 * 收款人明细
 * Created by zhx on 2016/6/23
 */
public class PayeeDetailFragment2 extends BussFragment implements PayeeDetailContact.View {
    public static final int RESULT_CODE_DELETE_SUCCESS = 101; // 结果码：删除成功
    public static final int RESULT_CODE_MODIFY_SUCCESS = 102; // 结果码：修改成功
    public static final String KEY_PAYEE_ENTITY = "payeeEntity"; // 键：payeeEntity

    protected View rootView;
    private TextView mTvName; // 姓名
    private TextView mTvPayeeAlias; // 别名
    private TextView mTvAccountNumber;
    private TextView mTvBankName;
    private TextView mTvAddress;
    private TextView mTvMobile;
    private PsnTransPayeeListqueryForDimViewModel.PayeeEntity mPayeeEntity;

    private PayeeDetailPresenter mPresenter;
    private boolean isModifyed = false; // 默认是没有修改过
    private boolean isDeleted = false; // 默认是没有删除过

    private List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> mPayeeEntityList; // 收款人列表

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 在这里处理返回上一个页面的逻辑
            Bundle bundle = new Bundle();
            setFramgentResult(RESULT_CODE_DELETE_SUCCESS, bundle);
            pop();
        }
    };
    private TextView tv_bank_name_notice_txt;
    private LinearLayout mLlAddreess;
    private TextView tv_dingxiang_notice;
    private View view_separate;
    private TextView tv_to_trans_remit;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_payee_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        mTvName = (TextView) rootView.findViewById(R.id.tv_name);
        mTvPayeeAlias = (TextView) rootView.findViewById(R.id.tv_payee_alias);
        mTvAccountNumber = (TextView) rootView.findViewById(R.id.tv_account_number);
        mTvBankName = (TextView) rootView.findViewById(R.id.tv_bank_name);
        mTvAddress = (TextView) rootView.findViewById(R.id.tv_address);
        mTvMobile = (TextView) rootView.findViewById(R.id.tv_mobile);
        tv_bank_name_notice_txt = (TextView) rootView.findViewById(R.id.tv_bank_name_notice_txt);
        mLlAddreess = (LinearLayout) rootView.findViewById(R.id.ll_addreess);
        tv_dingxiang_notice = (TextView) rootView.findViewById(R.id.tv_dingxiang_notice);
        view_separate = rootView.findViewById(R.id.view_separate);
        tv_to_trans_remit = (TextView) rootView.findViewById(R.id.tv_to_trans_remit);
    }

    @Override
    public void initData() {
        mPresenter = new PayeeDetailPresenter(this);

        Bundle bundle = getArguments();
        mPayeeEntity = bundle.getParcelable(KEY_PAYEE_ENTITY);

        setWidgetDisplayState();

        if (mPayeeEntity.getPayeetId() == null) { // 如果收款人ID为空，那么查询并更新此条数据
            PsnTransPayeeListqueryForDimViewModel viewModel = new PsnTransPayeeListqueryForDimViewModel();
            String[] bocFlag = {"0", "1", "3"};
            viewModel.setBocFlag(bocFlag);
            viewModel.setPageSize("500");
            viewModel.setPayeeName("");
            viewModel.setCurrentIndex("0");
            viewModel.setIsAppointed("");
            mPresenter.psnTransPayeeListqueryForDim(viewModel);
        }
    }

    // 设置界面组件的显示
    private void setWidgetDisplayState() {
        if ("1".equals(mPayeeEntity.getBocFlag())) {
            tv_bank_name_notice_txt.setText("所属地区");
            mLlAddreess.setVisibility(View.GONE);
            rootView.findViewById(R.id.view_address_line).setVisibility(View.GONE);
        }

        if (mPayeeEntity.getBankName() != null && mPayeeEntity.getBankName().contains("中国银行")) {
            tv_bank_name_notice_txt.setText("所属地区");
            mLlAddreess.setVisibility(View.GONE);
            rootView.findViewById(R.id.view_address_line).setVisibility(View.GONE);

//            mTvBankName.setText(mPayeeEntity.getBankName().replace("中国银行", "").replace("省分行", ""));
            String accountIbkNum = mPayeeEntity.getAccountIbkNum();
            if (!TextUtils.isEmpty(accountIbkNum)) {
                mTvBankName.setText(PublicCodeUtils.getTransferIbk(mActivity, accountIbkNum));
            } else {
                mTvBankName.setText("");
            }
        } else {
            mTvBankName.setText(mPayeeEntity.getBankName());
        }

        if ("1".equals(mPayeeEntity.getIsAppointed())) {
            tv_dingxiang_notice.setVisibility(View.VISIBLE);
            view_separate.setVisibility(View.GONE);
        } else {
            tv_dingxiang_notice.setVisibility(View.GONE);
            view_separate.setVisibility(View.VISIBLE);
        }

        if ("3".equals(mPayeeEntity.getBocFlag())) { // 实时
            mLlAddreess.setVisibility(View.GONE);
            rootView.findViewById(R.id.view_address_line).setVisibility(View.GONE);
        }

        mTvName.setText(mPayeeEntity.getAccountName());
        mTvPayeeAlias.setText(mPayeeEntity.getPayeeAlias());
        mTvAccountNumber.setText(NumberUtils.formatCardNumberStrong(mPayeeEntity.getAccountNumber()));
        mTvAddress.setText(mPayeeEntity.getAddress());
        mTvMobile.setText(NumberUtils.formatMobileNumberDynamically(mPayeeEntity.getMobile()));

    }

    @Override
    public boolean onBack() {
        // 在这里处理返回上一个页面的逻辑
        if (isDeleted) {
            return true;
        }

        if (isModifyed) { // 如果是修改成功，那么在上一个页面，只要把对应payeeEntity的“手机号码”和“别名”字段修改了，然后，重新刷新界面即可
            Bundle bundle = new Bundle();
            bundle.putParcelable("payeeEntity", mPayeeEntity);
            setFramgentResult(RESULT_CODE_MODIFY_SUCCESS, bundle);
        }
        return true;
    }

    @Override
    public void setListener() {
        tv_to_trans_remit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到我要转账页面
                TransRemitBlankFragment bussFragment = new TransRemitBlankFragment();
                Bundle bundle = new Bundle();
//                bundle.putString(TransRemitBlankFragment.ACCOUNT_FROM_ACCOUNTMANAGEMENT, mPayeeEntity.getAccountId());
                bundle.putParcelable(TransRemitBlankFragment.ACCOUNT_FROM_PAYEEMANAGEMENT, mPayeeEntity);
                bussFragment.setArguments(bundle);
                start(bussFragment);
            }
        });

        // 修改收款人别名
        rootView.findViewById(R.id.iv_modify_payee_alias).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditDialogWidget dialog = new EditDialogWidget(mContext, 20, true);
                // 设置Dialog初始值
                dialog.setClearEditTextContent(mPayeeEntity.getPayeeAlias() == null ? "" : mPayeeEntity.getPayeeAlias());
                dialog.setEditDialogListener(new EditDialogWidget.EditDialogCallBack() {
                    @Override
                    public void onClick(String strEditTextContent) {
                        // TODO 别名的校验规则
                        if (!TextUtils.isEmpty(dialog.getStringText())) {
                            if (!StringUtils.checkAlias(dialog.getStringText())) { // 就是这里出的问题，导致校验一直通不过：dialog.getStringText()而不是mPayeeEntity.getPayeeAlias()
                                Toast.makeText(mActivity, "别名：请输入半角英文、半角数字、中文，可包含_，最长20个字符", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        PsnTransManagePayeeModifyAliasViewModel viewModel = generateAliasViewModel(mPayeeEntity, dialog.getClearEditText().getText().toString().trim());
                        mPresenter.psnTransManagePayeeModifyAlias(viewModel);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        // 修改收款人手机号
        rootView.findViewById(R.id.iv_modify_payee_mobile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditDialogWidget dialog = new EditDialogWidget(mContext, 11, true);
                dialog.getClearEditText().setInputType(InputType.TYPE_CLASS_PHONE);
                // 设置Dialog初始值
                dialog.setClearEditTextContent(mPayeeEntity.getMobile() == null ? "" : mPayeeEntity.getMobile().replace(" ", ""));
                dialog.setEditDialogListener(new EditDialogWidget.EditDialogCallBack() {
                    @Override
                    public void onClick(String strEditTextContent) {
                        String mobile = dialog.getClearEditText().getText().toString().trim();
                        mobile = mobile.replace(" ", ""); // 把手机号码中间的空格去掉

//                        if (TextUtils.isEmpty(mobile)) {
//                            Toast.makeText(mActivity, "手机号码不能为空", Toast.LENGTH_SHORT).show();
//                            return;
//                        }

                        if (!TextUtils.isEmpty(mobile)) {
                            if (!NumberUtils.checkMobileNumber(mobile)) {
                                Toast.makeText(mActivity, "收款人手机号：11位数字", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        PsnTransManagePayeeModifyMobileViewModel viewModel = generateMobileViewModel(mPayeeEntity, dialog.getClearEditText().getText().toString().trim());
                        mPresenter.psnTransManagePayeeModifyMobile(viewModel);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        // 删除付款人
        rootView.findViewById(R.id.tv_delete_payee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TitleAndBtnDialog dialog = new TitleAndBtnDialog(mActivity);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) dialog.getTvNotice().getLayoutParams();
                layoutParams.height = 260;
                dialog.setTitle("");
                dialog.setNoticeContent("请您再次确认是否删除该收款人？");
                dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                    @Override
                    public void onLeftBtnClick(View view) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onRightBtnClick(View view) {
                        PsnTransManagePayeeDeletePayeeViewModel viewModel = new PsnTransManagePayeeDeletePayeeViewModel();

                        String[] payeeIds = new String[1];
                        payeeIds[0] = String.valueOf(mPayeeEntity.getPayeetId());
                        viewModel.setPayeeId(payeeIds);
                        mPresenter.psnTransManagePayeeDeletePayee(viewModel);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    private PsnTransManagePayeeModifyMobileViewModel generateMobileViewModel(PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity, String mobile) {
        PsnTransManagePayeeModifyMobileViewModel viewModel = new PsnTransManagePayeeModifyMobileViewModel();

        viewModel.setPayeeId(String.valueOf(payeeEntity.getPayeetId())); // 收款人ID
        viewModel.setOldMobile(payeeEntity.getMobile()); // 收款人旧手机号
        viewModel.setMobile(mobile); // 收款人手机号
        viewModel.setDevicePrint(DeviceInfoUtils.getDevicePrint(mActivity)); // 设备指纹
        viewModel.setToUserName(payeeEntity.getAccountName()); // 收款人姓名
        viewModel.setToAccountNo(payeeEntity.getAccountNumber()); // 收款人账号

        return viewModel;
    }

    /**
     * 生成ViewModel
     *
     * @param payeeEntity
     * @return
     */
    private PsnTransManagePayeeModifyAliasViewModel generateAliasViewModel(PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity, String payeeAlias) {
        PsnTransManagePayeeModifyAliasViewModel viewModel = new PsnTransManagePayeeModifyAliasViewModel();

        viewModel.setPayeeId(String.valueOf(payeeEntity.getPayeetId())); // 收款人ID
        viewModel.setOldAlias(payeeEntity.getPayeeAlias()); // 收款人旧别名
        viewModel.setPayeeAlias(payeeAlias); // 收款人别名
        viewModel.setDevicePrint(DeviceInfoUtils.getDevicePrint(mActivity)); // 设备指纹
        viewModel.setToUserName(payeeEntity.getAccountName()); // 收款人姓名
        viewModel.setToAccountNo(payeeEntity.getAccountNumber()); // 收款人账号

        return viewModel;
    }

    /**
     * 修改标题文字
     *
     * @return
     */
    @Override
    protected String getTitleValue() {
        return "收款人详情";
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 修改标题样式
     *
     * @return
     */
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void psnTransManagePayeeModifyMobileSuccess(PsnTransManagePayeeModifyMobileViewModel viewModel) {
        // 暂且认为此方法调用就一定成功 TODO
        isModifyed = true;
        mPayeeEntity.setMobile(viewModel.getMobile());
        mTvMobile.setText(NumberUtils.formatMobileNumberDynamically(viewModel.getMobile()));

        showErrorDialog("修改成功");
    }

    @Override
    public void psnTransManagePayeeModifyMobileFailed(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void psnTransManagePayeeModifyAliasSuccess(PsnTransManagePayeeModifyAliasViewModel viewModel) {
        // 暂且认为此方法调用就一定成功 TODO
        isModifyed = true;
        mPayeeEntity.setPayeeAlias(viewModel.getPayeeAlias());
        mTvPayeeAlias.setText(viewModel.getPayeeAlias());

        showErrorDialog("修改成功");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("ljljlj", "PayeeDetailFragment onDestroy()");

        //        // 在这里处理返回上一个页面的逻辑
        //        if (isDeleted) {
        //            return;
        //        }
        //
        //        if (isModifyed) { // 如果是修改成功，那么在上一个页面，只要把对应payeeEntity的“手机号码”和“别名”字段修改了，然后，重新刷新界面即可
        //            Bundle bundle = new Bundle();
        //            bundle.putParcelable("payeeEntity", mPayeeEntity);
        //            setFramgentResult(RESULT_CODE_MODIFY_SUCCESS, bundle);
        //            return;
        //        }
    }

    @Override
    public void psnTransManagePayeeModifyAliasFailed(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void psnTransManagePayeeDeletePayeeSuccess(PsnTransManagePayeeDeletePayeeViewModel viewModel) {
        Log.e("ljljlj", "psnTransManagePayeeDeletePayeeSuccess()");

        // 暂且认为此方法调用就一定成功 TODO
        isDeleted = true;

        Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();

        mHandler.sendEmptyMessageDelayed(0, 1500); // 发送消息退出页面
    }

    @Override
    public void psnTransManagePayeeDeletePayeeFailed(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void psnTransManagePayeeQueryPayeeListSuccess(PsnTransManagePayeeQueryPayeeListViewModel viewModel) {

    }

    @Override
    public void psnTransManagePayeeQueryPayeeListFailed(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void psnTransPayeeListqueryForDimSuccess(PsnTransPayeeListqueryForDimViewModel viewModel) {
        mPayeeEntityList = viewModel.getPayeeEntityList();

        for (PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity : mPayeeEntityList) {
            if (payeeEntity.getAccountNumber().endsWith(mPayeeEntity.getAccountNumber())) {
//                mPayeeEntity.setPayeetId(payeeEntity.getPayeetId());
//                mPayeeEntity.setBankName(payeeEntity.getBankName());
                BeanConvertor.toBean(payeeEntity, mPayeeEntity);
                if (mPayeeEntity.getBankName() != null && mPayeeEntity.getBankName().contains("中国银行")) {
                    tv_bank_name_notice_txt.setText("所属地区");
                    mLlAddreess.setVisibility(View.GONE);
                    rootView.findViewById(R.id.view_address_line).setVisibility(View.GONE);

                    mTvBankName.setText(mPayeeEntity.getBankName().replace("中国银行", "").replace("省分行", "").replace("市分行", ""));
                } else {
                    mTvBankName.setText(mPayeeEntity.getBankName());
                }
                break;
            }
        }
    }

    @Override
    public void psnTransPayeeListqueryForDimFailed(BiiResultErrorException biiResultErrorException) {
    }


    @Override
    public void setPresenter(PayeeDetailContact.Presenter presenter) {
    }
}
