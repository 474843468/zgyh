package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareInfoFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnBatchTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui.OrderListFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作结果页面
 * Created by zhx on 2016/7/12
 */
public class OperateResultFragment1 extends BussFragment implements OperationResultBottom.HomeBtnCallback {
    private View mRootView;
    private int type; // 0表示单人，1表示多人
    private PsnBatchTransActCollectionVerifyViewModel psnBatchTransActCollectionVerifyViewModel;
    //    private TextView tv_total_num;
    //    private ListView lv_payer_list;
    //    private TextView tv_payer;
    //    private TextView tv_go_bank_me_make_collection;
    //    private TextView tv_payee_actno;
    //    private TextView tv_remark;
    //    private EditChoiceWidget ecw_collection_manage;
    //    private LinearLayout ll_type_common;
    //    private LinearLayout ll_payer_container;
    //    private TextView tv_payer_name;
    //    private TextView tv_payer_mobile;
    //    private TextView tv_payer_channel;
    //    private TextView tv_payee_mobile;
    //    private TextView tv_notifyid;
    //    private EditChoiceWidget ecw_share_to_wei_xin;
    private PsnTransActCollectionVerifyViewModel psnTransActCollectionVerifyViewModel;
    private BaseOperationResultView operationResultView;
    private int isSuccess;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_operate_result1, null);
        return mRootView;
    }

    @Override
    public void initView() {
        //        tv_notifyid = (TextView) mRootView.findViewById(R.id.tv_notifyid);
        //        tv_total_num = (TextView) mRootView.findViewById(R.id.tv_total_num);
        //        tv_payer_name = (TextView) mRootView.findViewById(R.id.tv_payer_name);
        //        tv_payer_mobile = (TextView) mRootView.findViewById(R.id.tv_payer_mobile);
        //        tv_payer_channel = (TextView) mRootView.findViewById(R.id.tv_payer_channel);
        //        tv_payee_mobile = (TextView) mRootView.findViewById(R.id.tv_payee_mobile);
        //        tv_payer = (TextView) mRootView.findViewById(R.id.tv_payer);
        //        lv_payer_list = (ListView) mRootView.findViewById(R.id.lv_payer_list);
        //        tv_payee_actno = (TextView) mRootView.findViewById(R.id.tv_payee_actno);
        //        tv_remark = (TextView) mRootView.findViewById(R.id.tv_remark);
        //        tv_go_bank_me_make_collection = (TextView) mRootView.findViewById(R.id.tv_go_bank_me_make_collection);
        //        ecw_collection_manage = (EditChoiceWidget) mRootView.findViewById(R.id.ecw_collection_manage);
        //        ecw_share_to_wei_xin = (EditChoiceWidget) mRootView.findViewById(R.id.ecw_share_to_wei_xin);
        //        ll_type_common = (LinearLayout) mRootView.findViewById(R.id.ll_type_common);
        //        ll_payer_container = (LinearLayout) mRootView.findViewById(R.id.ll_payer_container);
        //        ll_payer_container.setVisibility(View.GONE);
        //
        //        ll_type_common.setVisibility(View.GONE);

        operationResultView = (BaseOperationResultView) mRootView.findViewById(R.id.rv_result);
        operationResultView.isShowBottomInfo(true);
    }

    @Override
    public void initData() {
        type = getArguments().getInt("type");
        isSuccess = getArguments().getInt("isSuccess");


        if (type == 0) { // 单人
            psnTransActCollectionVerifyViewModel = (PsnTransActCollectionVerifyViewModel) getArguments().getSerializable("psnTransActCollectionVerifyViewModel");

            String[] name = collectionName();
            String[] value = collectionValue();

            setResultStatus();

            operationResultView.setDetailsName(getResources().getString(R.string.boc_transfer_details));
            operationResultView.addHeadInfo(name[0], value[0]);

            for (int i = 1; i < value.length; i++) {
                operationResultView.addDetailRow(name[i], value[i], true, false);
            }

            // 再取一笔
            operationResultView.addContentItem("微信提醒对方付款", 0);
            operationResultView.addContentItem("继续收款", 1);
            operationResultView.addContentItem("收/付款管理", 2);

            // 设置页面的数据
            //            tv_notifyid.setText(psnTransActCollectionVerifyViewModel.getNotifyId() + "");
            //            tv_total_num.setText(String.format("%s元收款提交成功", psnTransActCollectionVerifyViewModel.getNotifyPayeeAmount()));
            //            tv_payee_actno.setText(psnTransActCollectionVerifyViewModel.getPayeeActno());
            //            tv_remark.setText(psnTransActCollectionVerifyViewModel.getRemark());
            //            tv_payer.setText(psnTransActCollectionVerifyViewModel.getPayerName());
            //            tv_payer_name.setText(psnTransActCollectionVerifyViewModel.getPayerName());
            //            tv_payer_mobile.setText(psnTransActCollectionVerifyViewModel.getPayerMobile());
            //            tv_payer_channel.setText("1".equals(psnTransActCollectionVerifyViewModel.getPayerChannel()) ? "网上银行用户" : "手机银行用户");
            //            tv_payee_mobile.setText(psnTransActCollectionVerifyViewModel.getPayeeMobile());
            //            lv_payer_list.setVisibility(View.GONE);

        } else { // 多人
            //            psnBatchTransActCollectionVerifyViewModel = (PsnBatchTransActCollectionVerifyViewModel) getArguments().getSerializable("psnBatchTransActCollectionVerifyViewModel");
            //            tv_total_num.setText(String.format("已向%s人发起收款", psnBatchTransActCollectionVerifyViewModel.getTotalNum())); // 总笔数
            //            tv_payee_actno.setText(NumberUtils.formatCardNumberStrong(psnBatchTransActCollectionVerifyViewModel.getPayeeActno())); // 收款帐号
            //            tv_remark.setText(psnBatchTransActCollectionVerifyViewModel.getRemark()); // 附言
            //
            //            List<PsnBatchTransActCollectionVerifyViewModel.PayerEntity> payerList = psnBatchTransActCollectionVerifyViewModel.getPayerList();
            //            lv_payer_list.setAdapter(new PayerAccountAdapter2(mActivity, payerList));
            //
            //            // 手动计算列表布局的高度
            //            DisplayMetrics metrics = new DisplayMetrics();
            //            Activity activity = (Activity) mContext;
            //            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            //            float density = metrics.density;
            //
            //            int itemHeight = (int) activity.getResources().getDimension(R.dimen.boc_space_between_62px);
            //            LinearLayout.LayoutParams latyoutParams = (LinearLayout.LayoutParams) lv_payer_list.getLayoutParams();
            //            latyoutParams.height = itemHeight * payerList.size();
            //            lv_payer_list.setLayoutParams(latyoutParams);
        }

        //        tv_payer.setVisibility(View.GONE);
        //        lv_payer_list.setVisibility(View.GONE);
    }

    /**
     * 名称
     *
     * @return
     */
    private String[] collectionName() {
        String[] name = new String[6];
        name[0] = "指令序号";
        name[1] = "付款人名称";
        name[2] = "付款人手机号";
        name[3] = "附言";
        name[4] = "收款账户";
        name[5] = "收款人手机号";

        String[] value = new String[6];
        value[0] = psnTransActCollectionVerifyViewModel.getNotifyId() + "";
        value[1] = psnTransActCollectionVerifyViewModel.getPayerName();
        value[2] = NumberUtils.formatMobileNumber(psnTransActCollectionVerifyViewModel.getPayerMobile());
        value[3] = psnTransActCollectionVerifyViewModel.getRemark();
        value[4] = NumberUtils.formatCardNumberStrong(psnTransActCollectionVerifyViewModel.getPayeeActno());
        value[5] = NumberUtils.formatMobileNumber(psnTransActCollectionVerifyViewModel.getPayeeMobile());

        List<String> nameList = new ArrayList<String>();
        for (int i = 0; i < value.length; i++) {
            if (!TextUtils.isEmpty(value[i].trim())) {
                nameList.add(name[i]);
            }
        }

        String[] filterName = nameList.toArray(new String[nameList.size()]);
        return filterName;
    }

    /**
     * 数据
     *
     * @return
     */
    private String[] collectionValue() {
        String money = psnTransActCollectionVerifyViewModel.getTotalAmount();
        String[] value = new String[6];
        value[0] = psnTransActCollectionVerifyViewModel.getNotifyId() + "";
        value[1] = psnTransActCollectionVerifyViewModel.getPayerName();
        value[2] = NumberUtils.formatMobileNumber(psnTransActCollectionVerifyViewModel.getPayerMobile());
        value[3] = psnTransActCollectionVerifyViewModel.getRemark();
        value[4] = NumberUtils.formatCardNumberStrong(psnTransActCollectionVerifyViewModel.getPayeeActno());
        value[5] = NumberUtils.formatMobileNumber(psnTransActCollectionVerifyViewModel.getPayeeMobile());

        List<String> valueList = new ArrayList<String>();
        for (int i = 0; i < value.length; i++) {
            if (!TextUtils.isEmpty(value[i].trim())) {
                valueList.add(value[i]);
            }
        }

        String[] filterValue = (String[]) valueList.toArray(new String[valueList.size()]);

        return filterValue;
    }

    @Override
    public void setListener() {
        //        ecw_share_to_wei_xin.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                String[] moneyInfo = {"收款金额", MoneyUtils.transMoneyFormat(psnTransActCollectionVerifyViewModel.getNotifyPayeeAmount(),
        //                        psnTransActCollectionVerifyViewModel.getCurrency()) + "元"};
        //                ShareInfoFragment fragment = ShareInfoFragment.newInstance("发起收款交易成功！", collectionName(),
        //                        collectionValue(), moneyInfo);
        //                start(fragment);
        //            }
        //        });
        //
        //        tv_go_bank_me_make_collection.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                // 返回“我要收款”的首页
        //                OperateResultFragment1.this.pop();
        //                MeMakeCollectionFragment fragment = new MeMakeCollectionFragment();
        //                start(fragment);
        //            }
        //        });
        //
        //        ecw_collection_manage.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                OperateResultFragment1.this.pop();
        //                start(new OrderListFragment());
        //            }
        //        });
        operationResultView.setgoHomeOnclick(this);
        operationResultView.setOnclick(new BaseOperationResultView.BtnCallback() {
            @Override
            public void onClickListener(View v) {
                int i = v.getId();
                if (i == 0) { // 分享给微信好友

                    String[] moneyInfo = {"收款金额", MoneyUtils.transMoneyFormat(psnTransActCollectionVerifyViewModel.getNotifyPayeeAmount(),
                            psnTransActCollectionVerifyViewModel.getCurrency()) + "元"};
                    ShareInfoFragment fragment = ShareInfoFragment.newInstance(psnTransActCollectionVerifyViewModel.getPayeeName() + "给您发起一笔主动收款，请登录中行网银或手机银行进行确认付款。", collectionName(),
                            collectionValue(), moneyInfo);
                    fragment.setOther(true);
                    start(fragment);
                } else if (i == 1) { // 继续收款
                    popToAndReInit(MeMakeCollectionFragment1.class);
                } else { // 收款管理
                    OrderListFragment toFragment = new OrderListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(toFragment.RERUAN_CODE, 1);
                    toFragment.setArguments(bundle);
                    start(toFragment);
                }
            }
        });

    }

    /**
     * 设置交易结果状态
     */
    private void setResultStatus() {
        String money = MoneyUtils.transMoneyFormat(psnTransActCollectionVerifyViewModel.getNotifyPayeeAmount(),
                psnTransActCollectionVerifyViewModel.getCurrency());

        if (isSuccess == 0) { // 成功的情况
            operationResultView.updateHead(OperationResultHead.Status.SUCCESS,
                    String.format("%s元收款提交成功", money));
        } else { // 失败的情况
            operationResultView.updateHead(OperationResultHead.Status.FAIL,
                    String.format("%s元收款提交失败", money));
        }
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "操作结果";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(MeMakeCollectionFragment1.class);
    }

    @Override
    public void onHomeBack() {
        ActivityManager.getAppManager().finishActivity();
    }

    @Override
    public boolean onBack() {
        ActivityManager.getAppManager().finishActivity();
        return true;
    }
}
