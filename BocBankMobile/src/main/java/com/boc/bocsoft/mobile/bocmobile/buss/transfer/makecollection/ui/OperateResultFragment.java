package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareInfoFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.adapter.PayerAccountAdapter2;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnBatchTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.ui.MobileRemitFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui.OrderListFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.List;

/**
 * 操作结果页面
 * Created by zhx on 2016/7/12
 */
public class OperateResultFragment extends BussFragment {
    private View mRootView;
    private int type; // 0表示单人，1表示多人
    private PsnBatchTransActCollectionVerifyViewModel psnBatchTransActCollectionVerifyViewModel;
    private TextView tv_total_num;
    private ListView lv_payer_list;
    private TextView tv_payer;
    private TextView tv_total_amount;
    private TextView tv_average_amount;
    private TextView tv_transfer_detail;
    private TextView tv_go_bank_me_make_collection;
    private TextView tv_payee_actno;
    private TextView tv_remark;
    private TextView tv_total_amount_notice;
    private TableRow tr_average_amount;
    private EditChoiceWidget ecw_collection_manage;
    private LinearLayout ll_type_common;
    private LinearLayout ll_payer_container;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_operate_result, null);
        return mRootView;
    }

    @Override
    public void initView() {
        tv_total_num = (TextView) mRootView.findViewById(R.id.tv_total_num);
        tv_payer = (TextView) mRootView.findViewById(R.id.tv_payer);
        lv_payer_list = (ListView) mRootView.findViewById(R.id.lv_payer_list);
        tv_total_amount = (TextView) mRootView.findViewById(R.id.tv_total_amount);
        tv_average_amount = (TextView) mRootView.findViewById(R.id.tv_average_amount);
        tv_transfer_detail = (TextView) mRootView.findViewById(R.id.tv_transfer_detail);
        tv_payee_actno = (TextView) mRootView.findViewById(R.id.tv_payee_actno);
        tv_remark = (TextView) mRootView.findViewById(R.id.tv_remark);
        tv_total_amount_notice = (TextView) mRootView.findViewById(R.id.tv_total_amount_notice);
        tv_go_bank_me_make_collection = (TextView) mRootView.findViewById(R.id.tv_go_bank_me_make_collection);
        tr_average_amount = (TableRow) mRootView.findViewById(R.id.tr_average_amount);
        ecw_collection_manage = (EditChoiceWidget) mRootView.findViewById(R.id.ecw_collection_manage);

        ll_type_common = (LinearLayout) mRootView.findViewById(R.id.ll_type_common);
        ll_payer_container = (LinearLayout) mRootView.findViewById(R.id.ll_payer_container);
        ll_payer_container.setVisibility(View.GONE);

        ll_type_common.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        type = getArguments().getInt("type");

        if (type == 0) { // 单人
            PsnTransActCollectionVerifyViewModel psnTransActCollectionVerifyViewModel = (PsnTransActCollectionVerifyViewModel) getArguments().getSerializable("psnTransActCollectionVerifyViewModel");

            // 设置页面的数据
            tv_total_num.setText("已向1人发起收款");
            tv_total_amount_notice.setText("收款金额");
            tv_total_amount.setText(psnTransActCollectionVerifyViewModel.getNotifyPayeeAmount());
            tr_average_amount.setVisibility(View.GONE);
            tv_payee_actno.setText(psnTransActCollectionVerifyViewModel.getPayeeActno());
            tv_remark.setText(psnTransActCollectionVerifyViewModel.getRemark());
            tv_payer.setText(psnTransActCollectionVerifyViewModel.getPayerName());
            lv_payer_list.setVisibility(View.GONE);

        } else { // 多人
            psnBatchTransActCollectionVerifyViewModel = (PsnBatchTransActCollectionVerifyViewModel) getArguments().getSerializable("psnBatchTransActCollectionVerifyViewModel");
            tv_total_num.setText(String.format("已向%s人发起收款", psnBatchTransActCollectionVerifyViewModel.getTotalNum())); // 总笔数
            tv_total_amount.setText(psnBatchTransActCollectionVerifyViewModel.getTotalAmount()); // 总金额
            tv_average_amount.setText(psnBatchTransActCollectionVerifyViewModel.getNotifyPayeeAmount()); // 人均额
            tv_payee_actno.setText(NumberUtils.formatCardNumberStrong(psnBatchTransActCollectionVerifyViewModel.getPayeeActno())); // 收款帐号
            tv_remark.setText(psnBatchTransActCollectionVerifyViewModel.getRemark()); // 附言

            List<PsnBatchTransActCollectionVerifyViewModel.PayerEntity> payerList = psnBatchTransActCollectionVerifyViewModel.getPayerList();
            lv_payer_list.setAdapter(new PayerAccountAdapter2(mActivity, payerList));

            // 手动计算列表布局的高度
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) mContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float density = metrics.density;

            int itemHeight = (int) activity.getResources().getDimension(R.dimen.boc_space_between_62px);
            LinearLayout.LayoutParams latyoutParams = (LinearLayout.LayoutParams) lv_payer_list.getLayoutParams();
            latyoutParams.height = itemHeight * payerList.size();
            lv_payer_list.setLayoutParams(latyoutParams);
        }

        tv_payer.setVisibility(View.GONE);
        lv_payer_list.setVisibility(View.GONE);
    }

    @Override
    public void setListener() {


        tv_transfer_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_type_common.setVisibility(View.VISIBLE);
                ll_payer_container.setVisibility(View.VISIBLE);
                tv_transfer_detail.setVisibility(View.GONE);

                if (type == 0) { // 单人
                    tv_payer.setVisibility(View.VISIBLE);
                } else { // 多人
                    lv_payer_list.setVisibility(View.VISIBLE);
                }
            }
        });

        tv_go_bank_me_make_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回“我要收款”的首页
                OperateResultFragment.this.pop();
                MeMakeCollectionFragment fragment = new MeMakeCollectionFragment();
                start(fragment);
            }
        });

        ecw_collection_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateResultFragment.this.pop();
                start(new OrderListFragment());
            }
        });
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
}
