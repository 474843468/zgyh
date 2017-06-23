package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.buyprocedure.BuyProcedureWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.adapter.LikeGridAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPositionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseInputModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.TransInquireFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.ResultConvertUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.WealthPublicUtils;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.tencent.mm.sdk.openapi.SendMessageToWX;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 组合购买结果页面
 * Created by wangtong on 2016/9/17.
 */
public class PortfolioResultFragment extends BussFragment
        implements AdapterView.OnItemClickListener {

    protected BaseOperationResultView layoutOperator;
    private View rootView;
    private PortfolioPurchaseModel model;
    private PortfolioPurchaseInputModel inputModel;
    private WealthDetailsBean wealthDetailsBean;
    private List<WealthListBean> wealthList;
    private final static String PARAM_WEALTH_LIST = "wealthList";

    public static PortfolioResultFragment newInstance(ArrayList<WealthListBean> wealthList) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(PARAM_WEALTH_LIST, wealthList);
        PortfolioResultFragment fragment = new PortfolioResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_portfolio_result, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        layoutOperator = (BaseOperationResultView) rootView.findViewById(R.id.layout_operator);
    }

    @Override
    public void initData() {
        super.initData();
        if (getArguments() != null) {
            wealthList = getArguments().getParcelableArrayList(PARAM_WEALTH_LIST);
        }
        if (!PublicUtils.isEmpty(wealthList)) {
            layoutOperator.setYouLikeAdapter(new LikeGridAdapter(mContext, wealthList), this);
        }
        model = findFragment(PortfolioPurchaseFragment.class).model;
        inputModel = findFragment(PortfolioPurchaseFragment.class).inputModel;
        wealthDetailsBean =
                findFragment(PortfolioPurchaseFragment.class).inputModel.getDetailsBean();
        String currency = PublicCodeUtils.getCurrency(getActivity(), model.getCurCode());
        layoutOperator.setDetailsName(getString(R.string.boc_see_detail));
        if (!WealthConst.PRODUCT_KIND_0.equals(model.getProductKind()) || (
                WealthConst.PRODUCT_KIND_0.equals(model.getProductKind())
                        && WealthConst.IS_LOCK_PERIOD_0.equals(
                        inputModel.getDetailsBean().getIsLockPeriod()))) {
            layoutOperator.setProcedureLayoutVisible(true, true);
            setProcedureData();
        }
        layoutOperator.updateHead(OperationResultHead.Status.SUCCESS, "组合购买申请已提交");
        String productHead =
                getString(R.string.boc_purchase_product_head, currency, model.getProdName(),
                        model.getProdCode());
        layoutOperator.addDetailRow("产品名称", productHead);
        String payAmount =
                MoneyUtils.transMoneyFormat(model.getPayAmount() + "", model.getCurCode());
        if (!ApplicationConst.CURRENCY_CNY.equals(model.getCurCode())) {
            if (model.getCashRemit().equals("1")) {
                currency = currency + "/钞";
            } else if (model.getCashRemit().equals("2")) {
                currency = currency + "/汇";
            }
        }
        layoutOperator.addDetailRow("组合购买金额", currency + " " + payAmount);
        layoutOperator.addDetailRow("被组合产品数", model.getPortfolioNum() + "");
        layoutOperator.addDetailRow("被组合产品总份额",
                MoneyUtils.transMoneyFormat(model.getPortfolioAmount() + "", model.getCurCode()));
        layoutOperator.addContentItem("分享产品", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWechat();
            }
        });
        layoutOperator.addContentItem("我的持仓", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinancialPositionFragment financialPositionFragment =
                        new FinancialPositionFragment();
                financialPositionFragment.setBackToHome(true);
                start(financialPositionFragment);
            }
        });
        layoutOperator.addContentItem("交易记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransInquireFragment.newinstance(mActivity, 1);
            }
        });
    }

    private void shareToWechat() {

        if (wealthDetailsBean == null) {
            return;
        }

        String url = WealthConst.getShareProductUrl(wealthDetailsBean.getProdCode(),
                wealthDetailsBean.getProductKind());
        String title = wealthDetailsBean.getProdName();
        String type;
        String[] values = new String[3];
        String date = "";
        date = ResultConvertUtils.convertDate(wealthDetailsBean.getProductKind(),
                wealthDetailsBean.getProdTimeLimit(), wealthDetailsBean.getIsLockPeriod(),
                wealthDetailsBean.getProductTermType());
        if (WealthConst.PRODUCT_TYPE_2.equals(wealthDetailsBean.getProductType())) {// 净值
            type = "1";
            values[0] = MoneyUtils.getRoundNumber(wealthDetailsBean.getPrice(), 4,
                    BigDecimal.ROUND_HALF_UP);
            values[1] = date;
            values[2] = wealthDetailsBean.getSubAmount();
        } else {// 非净值
            type = "0";
            values[0] = ResultConvertUtils.convertRate(wealthDetailsBean.getYearlyRR(),
                    wealthDetailsBean.getRateDetail());
            values[1] = date;
            values[2] = wealthDetailsBean.getSubAmount();
        }
        String content = WealthPublicUtils.buildShareStr(type, values, wealthDetailsBean.getCurCode());
        SendMessageToWX.Req req = ShareUtils.shareWebPage(0, url, title, content);
        if (getApi() != null) {
            getApi().sendReq(req);//跳转到朋友圈或会话列表
        }
    }

    private void setProcedureData() {
        BuyProcedureWidget.CompleteStatus completeStatus = BuyProcedureWidget.CompleteStatus.PAY;
        String[] textStr = new String[3];
        String[] dateStr = new String[3];
        textStr[0] = "今日发起";
        dateStr[0] = LocalDate.now().format(DateFormatters.dateFormatter1);
        textStr[1] = "组合生效";
        if (WealthConst.PRODUCT_TYPE_3.equals(model.getProductType())) {// 固定期限产品
            dateStr[1] = model.getProdBegin();
            LocalDate prodBegin =
                    LocalDate.parse(model.getProdBegin(), DateFormatters.dateFormatter1);
            if (LocalDate.now().isBefore(prodBegin)) {
                completeStatus = BuyProcedureWidget.CompleteStatus.SUBMIT;
            }
        } else {
            dateStr[1] = "付息规则";
            layoutOperator.getBuyProcedureWidget()
                    .setMiddHint(ResultConvertUtils.convertCouponpayFreq(
                            wealthDetailsBean.getCouponpayFreq(),
                            wealthDetailsBean.getInterestDate()));
        }
        if (WealthConst.TERM_TYPE_0.equals(model.getProductTermType())) {// 有限期封闭式
            textStr[2] = "产品到期";
            dateStr[2] = model.getProdEnd();
        } else if (WealthConst.TERM_TYPE_1.equals(model.getProductTermType())) {// 有限半开放式
            textStr[2] = "产品可赎回";
            dateStr[2] = model.getProdEnd();
        } else {
            textStr[2] = "产品赎回";
            dateStr[2] = "赎回开放规则";
            layoutOperator.getBuyProcedureWidget()
                    .setRightHint(ResultConvertUtils.convertBuyType(wealthDetailsBean));
        }
        layoutOperator.getBuyProcedureWidget().setText(textStr);
        layoutOperator.getBuyProcedureWidget().setDate(dateStr);
        layoutOperator.getBuyProcedureWidget().setStatus(completeStatus);
    }

    @Override
    public void setListener() {
        super.setListener();
        layoutOperator.setgoHomeOnclick(new OperationResultBottom.HomeBtnCallback() {
            @Override
            public void onHomeBack() {
                ActivityManager.getAppManager().finishActivity();
            }
        });
    }

    @Override
    public boolean onBackPress() {
        titleLeftIconClick();
        return true;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        popToAndReInit(WealthProductFragment.class);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "操作结果";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        start(WealthDetailsFragment.newInstance(wealthList.get(position)));
    }
}
