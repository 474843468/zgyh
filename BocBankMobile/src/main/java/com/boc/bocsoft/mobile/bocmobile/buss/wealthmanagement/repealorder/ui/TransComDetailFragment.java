package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.adapter.GuarantyProductAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadAutComTradStatusViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadQueryGuarantyProductViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.model.XpadRemoveGuarantyProductViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.presenter.TransComDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.model.XpadAccountQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import java.util.List;

/**
 * Fragment：中银理财-组合购买详情
 * Created by zhx on 2016/9/20
 */
public class TransComDetailFragment extends BussFragment implements TransComDetailContact.View {
    private View rootView;
    private XpadAutComTradStatusViewModel.AutComTradEntity mAutComTradEntity;
    private TransComDetailPresenter transComDetailPresenter;
    private XpadAccountQueryViewModel.XPadAccountEntity mCurrentQueryAccount;
    private LinearLayout ll_container;
    private TextView tv_amount_notice;
    private TextView tv_amount;
    private TextView tv_prod_name;
    private TextView tv_prod_code;
    private TextView tv_return_date;
    private ListView lv_list;
    private int mSelectPagePosition;

    private TextView btn_delegate_cancel;
    private boolean isBackPress = false;
    private String mNoticeType;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!isBackPress) {
                Toast.makeText(mActivity, "撤单成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction("startQuery");
                intent.putExtra("noticeType", Integer.valueOf(mNoticeType).intValue()); // 0表示“页面选中”通知（共4种通知类型，页面选中通知，筛选条件选择通知，账户选择通知，撤单成功通知。这4种都有可能进行重新查询操作）
                intent.putExtra("selectPagePosition", mSelectPagePosition);
                mActivity.sendBroadcast(intent);
                TransComDetailFragment.this.pop();
            }
        }
    };

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_wealth_trans_com_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        ll_container = (LinearLayout) rootView.findViewById(R.id.ll_container);
        tv_amount = (TextView) rootView.findViewById(R.id.tv_amount);
        tv_amount_notice = (TextView) rootView.findViewById(R.id.tv_amount_notice);
        tv_prod_name = (TextView) rootView.findViewById(R.id.product_name);
        tv_prod_code = (TextView) rootView.findViewById(R.id.product_code);
        tv_return_date = (TextView) rootView.findViewById(R.id.tv_return_date);
        lv_list = (ListView) rootView.findViewById(R.id.lv_list);
        btn_delegate_cancel = (TextView) rootView.findViewById(R.id.btn_delegate_cancel);

    }

    @Override
    public void initData() {
        mAutComTradEntity = getArguments().getParcelable("autComTradEntity");
        mNoticeType = getArguments().getString("noticeType", "3");
        mSelectPagePosition = getArguments().getInt("selectPagePosition", -1);
        mCurrentQueryAccount = getArguments().getParcelable("currentQueryAccount");

        transComDetailPresenter = new TransComDetailPresenter(this);
        XpadQueryGuarantyProductViewModel viewModel = new XpadQueryGuarantyProductViewModel();
        viewModel.setTranSeq(mAutComTradEntity.getTranSeq());
        viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
        viewModel.setIbknum(mCurrentQueryAccount.getIbkNumber());
        viewModel.setTypeOfAccount(mCurrentQueryAccount.getAccountType());
        transComDetailPresenter.psnXpadQueryGuarantyProductResult(viewModel);

    }

    // 展示数据
    private void displayData() {
        // 设置界面上显示的数据
        String moneyFormat = MoneyUtils.transMoneyFormat(mAutComTradEntity.getBuyAmt(), mAutComTradEntity.getCurrency());
        String cashRemit = "";
        if ("001".equals(mAutComTradEntity.getCurrency())) {
            cashRemit = "元";
        } else {
            cashRemit = PublicCodeUtils.getCurrency(mActivity, mAutComTradEntity.getCurrency());
            if ("1".equals(mAutComTradEntity.getCashRemit())) {
                cashRemit += "/钞";
            } else if ("2".equals(mAutComTradEntity.getCashRemit())) {
                cashRemit += "/汇";
            }
        }

        tv_amount_notice.setText("购买金额 (" + cashRemit + ")");
        tv_amount.setText(moneyFormat);
//        tv_prod_name.addTextAndButtonContent(getResources().getString(R.string.boc_wealth_product_name),mAutComTradEntity.getProdName()," ("+mAutComTradEntity.getProdCode()+")");
//        tv_prod_name.setRightTvListener(new DetailContentView.DetailContentRightTvOnClickListener() {
//            @Override
//            public void onClickRightTextView() {
//                WealthDetailsFragment detailsFragment = new WealthDetailsFragment();
//                Bundle bundle = new Bundle();
//                DetailsRequestBean detailsRequestBean = new DetailsRequestBean();
//                detailsRequestBean.setProdCode(mAutComTradEntity.getProdCode());
//                detailsRequestBean.setProdKind(mAutComTradEntity.getProductKind());
//                detailsRequestBean.setIbknum(mCurrentQueryAccount.getIbkNumber());
//                bundle.putBoolean(WealthDetailsFragment.OTHER, true);
//                bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, detailsRequestBean);
//                detailsFragment.setArguments(bundle);
//                start(detailsFragment);
//            }
//        });
        tv_prod_name.setText(mAutComTradEntity.getProdName());
        tv_prod_code.setText("(" + mAutComTradEntity.getProdCode() + ")");
        tv_return_date.setText(mAutComTradEntity.getReturnDate().format(DateFormatters.dateFormatter1)); // 交易日期
        btn_delegate_cancel.setVisibility(("0".equals(mAutComTradEntity.getStatus()) && "0".equals(mAutComTradEntity.getCanBeCanceled())) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setListener() {
        if ("0".equals(mAutComTradEntity.getCanBeCanceled())) {
            // 撤单按钮的点击事件
            btn_delegate_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TitleAndBtnDialog dialog = new TitleAndBtnDialog(mActivity);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) dialog.getTvNotice().getLayoutParams();
                    layoutParams.height = 260;
                    dialog.setTitle("");
                    dialog.setNoticeContent("请您再次确认，是否撤单此组合交易？");
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
                            XpadRemoveGuarantyProductViewModel viewModel = new XpadRemoveGuarantyProductViewModel();
                            viewModel.setTranSeq(mAutComTradEntity.getTranSeq());
                            viewModel.setCurrency(mAutComTradEntity.getCurrency());
                            viewModel.setBuyAmt(mAutComTradEntity.getBuyAmt());
                            viewModel.setXpadCode(mAutComTradEntity.getProdCode());
                            viewModel.setXpadName(mAutComTradEntity.getProdName());
                            viewModel.setCashRemit(mAutComTradEntity.getCashRemit());
                            viewModel.setStatus(mAutComTradEntity.getStatus());
                            viewModel.setChannel(mAutComTradEntity.getChannel());
                            viewModel.setAccountKey(mCurrentQueryAccount.getAccountKey());
                            viewModel.setIbknum(mCurrentQueryAccount.getIbkNumber());
                            viewModel.setTypeOfAccount(mCurrentQueryAccount.getAccountType());
                            transComDetailPresenter.psnXpadRemoveGuarantyProductResult(viewModel);
                        }
                    });
                    dialog.show();
                }
            });
        }
        tv_prod_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WealthDetailsFragment detailsFragment = new WealthDetailsFragment();
                Bundle bundle = new Bundle();
                DetailsRequestBean detailsRequestBean = new DetailsRequestBean();
                detailsRequestBean.setProdCode(mAutComTradEntity.getProdCode());
                detailsRequestBean.setProdKind(mAutComTradEntity.getProductKind());
                detailsRequestBean.setIbknum(mCurrentQueryAccount.getIbkNumber());
                bundle.putBoolean(WealthDetailsFragment.OTHER, true);
                bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, detailsRequestBean);
                detailsFragment.setArguments(bundle);
                start(detailsFragment);
            }
        });
    }

    @Override
    public boolean onBackPress() {
        isBackPress = true;
        Log.e("ljljlj", "onBackPress()");
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "明细";
    }

    // 成功回调：组合购买已押押品查询
    @Override
    public void psnXpadQueryGuarantyProductResultSuccess(XpadQueryGuarantyProductViewModel viewModel) {
        List<XpadQueryGuarantyProductViewModel.GuarantyProductEntity> list = viewModel.getList();
        lv_list.setAdapter(new GuarantyProductAdapter(mActivity, list));

        displayData();
        ll_container.setVisibility(View.VISIBLE);
    }

    // 失败回调：组合购买已押押品查询
    @Override
    public void psnXpadQueryGuarantyProductResultFail(BiiResultErrorException biiResultErrorException) {
        Toast.makeText(mActivity, "撤单失败", Toast.LENGTH_SHORT).show();
    }

    // 成功回调：组合购买解除
    @Override
    public void psnXpadRemoveGuarantyProductResultSuccess(XpadRemoveGuarantyProductViewModel viewModel) {
        handler.sendEmptyMessageDelayed(0, 0);
    }

    // 失败回调：组合购买解除
    @Override
    public void psnXpadRemoveGuarantyProductResultFail(BiiResultErrorException biiResultErrorException) {
        ToastUtils.show("撤单失败");
    }

    @Override
    public void setPresenter(TransComDetailContact.Presenter presenter) {
    }
}
