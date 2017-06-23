package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActDeletePayer.PsnTransActDeletePayerResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActModifyPayerMobile.PsnTransActModifyPayerMobileResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditDialogWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.model.PayerBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.model.PayerListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.presenter.PayerListPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * Created by liuyang on 2016/7/21.
 */
public class PayerDetailFragment extends BussFragment implements PayerListContract.DetailedOperate {

    //根布局
    protected View rootView;
    //删除按钮
    protected TextView tv_delete_payee;
    //业务处理
    private PayerListPresenter mListPresenter;
    //数据model
    private PayerListModel mListModel;
    //弹框组件
    private EditDialogWidget dialog;
    //修改后手机号
    private String newMobile;
    //删除dialog
    private TitleAndBtnDialog btnDialog;
    private PayerBean listBean;
    //页面数据传递
    public static final int REQUEST_CODE = 102;
    public static final int RESULT_CODE = 103;
    //付款人姓名
    private String payerName;
    private TextView tv_mobile;
    private ImageView iv_modify_payer_mobile;
    private TextView tv_customer_type;
    private TextView tv_customer_no;
    private TextView tv_name;
    public static final String ACTION_NOTIFY = "com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.PayerDetailFragment.NotifyDataChange";


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_payer_detail, null);
        return rootView;
    }


    @Override
    public void initView() {
        tv_delete_payee = (TextView) rootView.findViewById(R.id.tv_delete_payee);
        tv_name = (TextView) rootView.findViewById(R.id.tv_name);
        tv_mobile = (TextView) rootView.findViewById(R.id.tv_mobile);
        tv_customer_type = (TextView) rootView.findViewById(R.id.tv_customer_type);
        tv_customer_no = (TextView) rootView.findViewById(R.id.tv_customer_no);
        iv_modify_payer_mobile = (ImageView) rootView.findViewById(R.id.iv_modify_payer_mobile);


    }

    @Override
    public void initData() {
        mListPresenter = new PayerListPresenter(this);
        mListModel = new PayerListModel();
        //接受页面传递数据
        listBean = (PayerBean) getArguments().getParcelable("DetailsInfo");
        payerName = listBean.getPayerName();

        tv_mobile.setText(NumberUtils.formatMobileNumber(listBean.getPayerMobile()));
        iv_modify_payer_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出dialog修改手机号
                dialog = new EditDialogWidget(mContext, 11, true);


                //设置Dialog初始值
                dialog.setClearEditTextContent(listBean.getPayerMobile().trim().replace(" ", ""));
                dialog.setEditDialogListener(new EditDialogWidget.EditDialogCallBack() {
                    @Override
                    public void onClick(String strEditTextContent) {
                        newMobile = strEditTextContent;
                        if (TextUtils.isEmpty(newMobile)) {
                            Toast.makeText(getContext(), "付款人手机号不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!NumberUtils.checkMobileNumber(newMobile)) {
                            Toast.makeText(getContext(), "付款人手机号：11位数字", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        showLoadingDialog();
                        mListPresenter.revisePayerList(buildPayerViewModel());
                    }
                });
                dialog.show();
            }
        });

        tv_name.setText(listBean.getPayerName());
        tv_customer_type.setText(PublicCodeUtils.getPayerCustomer(mContext, listBean.getIdentifyType()));
        tv_customer_no.setText(String.valueOf(listBean.getPayerCustomerId()));
    }

    /**
     * 封装修改手机号上送参数
     *
     * @return
     */
    private PayerListModel buildPayerViewModel() {
        mListModel.setPayerId(String.valueOf(listBean.getPayerId()));
        //更改后手机号
        mListModel.setPayerMobile(newMobile);
        return mListModel;
    }

    @Override
    public void setListener() {
        tv_delete_payee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] btn = new String[]{"取消", "确认"};
                btnDialog = new TitleAndBtnDialog(mContext);
                btnDialog.setNoticeContent("请您再次确认是否删除该付款人?");
                btnDialog.isShowTitle(false);
                btnDialog.isShowBottomBtn(true);
                btnDialog.setBtnName(btn);
                btnDialog.show();
                btnDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                    @Override
                    public void onLeftBtnClick(View view) {
                        btnDialog.dismiss();
                    }

                    @Override
                    public void onRightBtnClick(View view) {
                        showLoadingDialog();
                        mListPresenter.deletePayerList(buildDeletePayerListModel());
                    }
                });


            }
        });
    }

    /**
     * 封装删除列表的上送参数
     *
     * @return
     */
    private PayerListModel buildDeletePayerListModel() {
        mListModel.setPayerId(String.valueOf(listBean.getPayerId()));
        return mListModel;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_payer_detail);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 标题栏左侧图标点击事件
     */
    protected void titleLeftIconClick() {
        if (listBean.getPayerMobile() == newMobile) {
            setFramgentResult(104, null);
        }
        pop();
    }


    @Override
    public void revisePayerSuccess(PsnTransActModifyPayerMobileResult psnTransActModifyPayerMobileResult) {
        dialog.dismiss();
        closeProgressDialog(); // 关闭对话框
        Toast.makeText(mActivity, "修改成功", Toast.LENGTH_SHORT).show();
        tv_mobile.setText(NumberUtils.formatMobileNumber(newMobile));
        listBean.setPayerMobile(NumberUtils.formatMobileNumber(newMobile));

        // 发送广播，更新界面
        Intent intent = new Intent(ACTION_NOTIFY);
        mActivity.sendBroadcast(intent);
    }

    @Override
    public void revisePayerFail(BiiResultErrorException biiResultErrorException) {
        dialog.dismiss();
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }


    @Override
    public void deletePayerSuccess(PsnTransActDeletePayerResult psnMobileWithdrawalQueryResult) {
        btnDialog.dismiss();
        closeProgressDialog();
        setFramgentResult(103, null);
        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
        pop();

    }

    @Override
    public void deletePayerFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }


    @Override
    public void setPresenter(PayerListContract.Presenter presenter) {

    }
}
