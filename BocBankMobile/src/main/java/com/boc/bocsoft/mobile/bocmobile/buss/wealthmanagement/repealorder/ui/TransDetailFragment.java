package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadDelegateCancelViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.presenter.TransDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAutTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * Fragment：中银理财-委托交易详情
 * Created by zhx on 2016/9/20
 */
public class TransDetailFragment extends BussFragment implements TransDetailContact.View {
    private View rootView;
    private XpadAutTradStatusViewModel.AutTradEntitiy mAutTradEntitiy;
    private TextView tv_amount;
    private TextView tv_amount_notice;
    private TextView tv_prod_name;
    private TextView tv_prod_code;
    private TextView tv_trf_type;
    private TextView tv_tran_atrr;
    private TextView tv_future_date;
    private TextView tv_payment_date;
    private TextView tv_account_number;
    private TextView btn_delegate_cancel;
    private TransDetailPresenter transDetailPresenter;
    private String mNoticeType;
    private XpadAccountQueryViewModel.XPadAccountEntity currentQueryAccount;
    private int selectPagePosition;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(mActivity, "撤单成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction("startQuery");
            intent.putExtra("noticeType", Integer.valueOf(mNoticeType).intValue()); // 0表示“页面选中”通知（共4种通知类型，页面选中通知，筛选条件选择通知，账户选择通知，撤单成功通知。这4种都有可能进行重新查询操作）
            intent.putExtra("selectPagePosition", selectPagePosition);
            mActivity.sendBroadcast(intent);
            TransDetailFragment.this.pop();
        }
    };

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_wealth_trans_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        tv_amount = (TextView) rootView.findViewById(R.id.tv_amount);
        tv_amount_notice = (TextView) rootView.findViewById(R.id.tv_amount_notice);
        tv_prod_name = (TextView) rootView.findViewById(R.id.product_name);
        tv_prod_code = (TextView) rootView.findViewById(R.id.product_code);
        tv_trf_type = (TextView) rootView.findViewById(R.id.tv_trf_type);
        tv_tran_atrr = (TextView) rootView.findViewById(R.id.tv_tran_atrr);
        tv_future_date = (TextView) rootView.findViewById(R.id.tv_future_date);
        tv_payment_date = (TextView) rootView.findViewById(R.id.tv_payment_date);
        tv_account_number = (TextView) rootView.findViewById(R.id.tv_account_number);
        btn_delegate_cancel = (TextView) rootView.findViewById(R.id.btn_delegate_cancel);
    }

    @Override
    public void initData() {
        transDetailPresenter = new TransDetailPresenter(this);
        mAutTradEntitiy = getArguments().getParcelable("autTradEntitiy");
        currentQueryAccount = getArguments().getParcelable("currentQueryAccount");
        mNoticeType = getArguments().getString("noticeType", "3");
        selectPagePosition = getArguments().getInt("selectPagePosition", -1);

        displayData();
    }

    // 展示数据
    private void displayData() {
        String moneyFormat = MoneyUtils.transMoneyFormat(mAutTradEntitiy.getAmount(), mAutTradEntitiy.getCurrencyCode());
        String transAmout = MoneyUtils.transMoneyFormat(mAutTradEntitiy.getTrfAmount(),mAutTradEntitiy.getCurrencyCode());
        String cashRemit = "";
        if ("001".equals(mAutTradEntitiy.getCurrencyCode())) {
            cashRemit = "元";
        } else {
            cashRemit = PublicCodeUtils.getCurrency(mActivity, mAutTradEntitiy.getCurrencyCode());
            if ("01".equals(mAutTradEntitiy.getCashRemit())) {
                cashRemit += "/钞";
            } else if ("02".equals(mAutTradEntitiy.getCashRemit())) {
                cashRemit += "/汇";
            }
        }

        if ("0.00".equals(moneyFormat) && (!"0.00".equals(transAmout))||"0".equals(moneyFormat) && (!"0".equals(transAmout))) {
            tv_amount_notice.setText("委托份额 (份)");
            tv_amount.setText(transAmout);
        } else {
            tv_amount_notice.setText("委托金额 (" + cashRemit + ")");
            tv_amount.setText(moneyFormat);
        }

//        tv_prod_name.addTextAndButtonContent(getResources().getString(R.string.boc_wealth_product_name), mAutTradEntitiy.getProdName()," ("+mAutTradEntitiy.getProdCode()+")");
//
//        tv_prod_name.setRightTvListener(new DetailContentView.DetailContentRightTvOnClickListener() {
//            @Override
//            public void onClickRightTextView() {
//                WealthDetailsFragment detailsFragment = new WealthDetailsFragment();
//                Bundle bundle = new Bundle();
//                DetailsRequestBean detailsRequestBean = new DetailsRequestBean();
//                detailsRequestBean.setProdCode(mAutTradEntitiy.getProdCode());
//                detailsRequestBean.setProdKind(mAutTradEntitiy.getProductKind());
//                detailsRequestBean.setIbknum(currentQueryAccount.getIbkNumber());
//                bundle.putBoolean(WealthDetailsFragment.OTHER, true);
//                bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, detailsRequestBean);
//                detailsFragment.setArguments(bundle);
//                start(detailsFragment);
//            }
//        });

        tv_prod_name.setText(mAutTradEntitiy.getProdName());
        tv_prod_code.setText("(" + mAutTradEntitiy.getProdCode() + ")");
        tv_trf_type.setText(PublicCodeUtils.getTransferType(mActivity, mAutTradEntitiy.getTrfType())); // 交易类型
        String transferAttr = PublicCodeUtils.getTransferAttr(mActivity, mAutTradEntitiy.getTranAtrr());
        tv_tran_atrr.setText(transferAttr); // 交易属性
        tv_future_date.setText(mAutTradEntitiy.getFutureDate().format(DateFormatters.dateFormatter1));
        tv_payment_date.setText(mAutTradEntitiy.getPaymentDate().format(DateFormatters.dateFormatter1));
        tv_account_number.setText(NumberUtils.formatCardNumberStrong(mAutTradEntitiy.getAccountNumber()));

        if("0".equals(mAutTradEntitiy.getStatus()) && "0".equals(mAutTradEntitiy.getCanBeCanceled())) {
            btn_delegate_cancel.setVisibility(View.VISIBLE);
        } else {
            btn_delegate_cancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void setListener() {
        if ("0".equals(mAutTradEntitiy.getCanBeCanceled())) {
            // 撤单按钮的点击事件
            btn_delegate_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TitleAndBtnDialog dialog = new TitleAndBtnDialog(mActivity);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) dialog.getTvNotice().getLayoutParams();
                    layoutParams.height = 260;
                    dialog.setTitle("");
                    dialog.setNoticeContent("请您再次确认，是否撤单此委托交易？");
                    dialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_main_bg_color),getResources().getColor(R.color.boc_main_bg_color),getResources().getColor(R.color.boc_text_color_red),getResources().getColor(R.color.boc_text_color_red));
                    dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                        @Override
                        public void onLeftBtnClick(View view) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onRightBtnClick(View view) {
                            dialog.dismiss();
                            // 撤单的逻辑
                            XpadDelegateCancelViewModel xpadDelegateCancelViewModel = new XpadDelegateCancelViewModel();
                            BeanConvertor.toBean(mAutTradEntitiy, xpadDelegateCancelViewModel);

                            xpadDelegateCancelViewModel.setTransSeq(mAutTradEntitiy.getTranSeq());
                            xpadDelegateCancelViewModel.setAmount(mAutTradEntitiy.getAmount());
                            transDetailPresenter.psnXpadDelegateCancel(xpadDelegateCancelViewModel);
                        }
                    });
                    dialog.show();
                }
            });
        }

        // 点击进去“产品详情”页面
        tv_prod_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WealthDetailsFragment detailsFragment = new WealthDetailsFragment();
                Bundle bundle = new Bundle();
                DetailsRequestBean detailsRequestBean = new DetailsRequestBean();
                detailsRequestBean.setProdCode(mAutTradEntitiy.getProdCode());
                detailsRequestBean.setProdKind(mAutTradEntitiy.getProductKind());
                detailsRequestBean.setIbknum(currentQueryAccount.getIbkNumber());
                bundle.putBoolean(WealthDetailsFragment.OTHER, true);
                bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, detailsRequestBean);
                detailsFragment.setArguments(bundle);
                start(detailsFragment);
            }
        });
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "明细";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    // 成功回调：撤单
    @Override
    public void psnXpadDelegateCancelSuccess(XpadDelegateCancelViewModel viewModel) {
        handler.sendEmptyMessageDelayed(0, 0);
    }

    // 失败回调：撤单
    @Override
    public void psnXpadDelegateCancelFail(BiiResultErrorException biiResultErrorException) {
        Toast.makeText(mActivity, "撤单失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(TransDetailContact.Presenter presenter) {
    }
}
