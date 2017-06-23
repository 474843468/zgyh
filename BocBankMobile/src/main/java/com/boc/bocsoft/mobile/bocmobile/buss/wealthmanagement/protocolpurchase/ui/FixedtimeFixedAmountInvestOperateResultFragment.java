package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.adapter.LikeGridAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui.InvestTreatyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPositionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.XpadApplyAgreementResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;
import com.tencent.mm.sdk.openapi.SendMessageToWX;

import java.util.ArrayList;

/**
 * 余额理财投资结果页面
 * Created by zhx on 2016/11/9
 */
public class FixedtimeFixedAmountInvestOperateResultFragment extends BussFragment implements OperationResultBottom.HomeBtnCallback, AdapterView.OnItemClickListener {
    private View mRootView;
    private BaseOperationResultView operationResultView;
    private ProtocolModel mViewModel;
    private XpadApplyAgreementResultViewModel resultViewModel;
    private ArrayList<WealthListBean> likeList;
    private String clickOprLock = "locklock111";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_operate_result1, null);
        return mRootView;
    }

    @Override
    public void initView() {
        operationResultView = (BaseOperationResultView) mRootView.findViewById(R.id.rv_result);
        operationResultView.isShowBottomInfo(true);
    }

    @Override
    public void initData() {
        mViewModel = getArguments().getParcelable(ProtocolSelectFragment.PROTOCOL_PURCHASE);
        resultViewModel = getArguments().getParcelable("resultViewModel");
        likeList = getArguments().getParcelableArrayList("likeList");

        operationResultView.updateHead(OperationResultHead.Status.SUCCESS,
                "投资协议申请已提交");

        operationResultView.setDetailsName(getResources().getString(R.string.boc_see_detail));
        operationResultView.addDetailRow("交易序号", resultViewModel.getTransSeq(), true, false);
        operationResultView.addDetailRow("协议序号", resultViewModel.getContractSeq(), true, false);
        // 产品代码
        String currency = PublicCodeUtils.getCurrency(mActivity, mViewModel.getCurCode());
        String prodName = mViewModel.getProdName();
        String proId = mViewModel.getProId();
        operationResultView.addDetailRow("产品名称", "[" + currency + "]" + prodName + " (" + proId + ")", true, false);

        if ("0".equals(resultViewModel.getTimeInvestType())) {
            // 申购起点金额
            String subAmountFormat = MoneyUtils.transMoneyFormat(mViewModel.getSubAmount(), mViewModel.getCurCode());
            operationResultView.addDetailRow("申购起点金额", subAmountFormat, true, false);
            // 追加申购起点金额
            String addAmountFormat = MoneyUtils.transMoneyFormat(mViewModel.getAddAmount(), mViewModel.getCurCode());
            operationResultView.addDetailRow("追加申购起点金额", addAmountFormat, true, false);
        } else {
            String lowLimitAmount = mViewModel.getLowLimitAmount();
            operationResultView.addDetailRow("赎回起点份额", lowLimitAmount, true, false);
        }

        // 钞/汇
        String cashRemit = resultViewModel.getCashRemit();
        String cashRemitStr = "";
        if ("1".equals(cashRemit)) {
            cashRemitStr = "现钞";
            operationResultView.addDetailRow("钞/汇", cashRemitStr, true, false);
        } else if ("2".equals(cashRemit)) {
            cashRemitStr = "现汇";
            operationResultView.addDetailRow("钞/汇", cashRemitStr, true, false);
        } else {
            // TODO: 2016/11/10
        }

        // 定投类型
        String timeInvestType = resultViewModel.getTimeInvestType();
        operationResultView.addDetailRow("定投类型", "0".equals(timeInvestType) ? "购买" : "赎回", true, false);

        // 总期数
        String totalPeriod = resultViewModel.getTotalPeriod();
        String totalPeriodStr = "";
        if ("-1".equals(totalPeriod)) {
            totalPeriodStr = "不限期";
        } else {
            totalPeriodStr = totalPeriod;
        }
        operationResultView.addDetailRow("总期数", totalPeriodStr, true, false);
        // 定投金额
        if ("0".equals(timeInvestType)) {
            operationResultView.addDetailRow("定投金额", MoneyUtils.transMoneyFormat(resultViewModel.getRedeemAmount(), mViewModel.getCurCode()), true, false);
        } else {
            operationResultView.addDetailRow("定赎份额", MoneyUtils.transMoneyFormat(resultViewModel.getRedeemAmount(), mViewModel.getCurCode()), true, false);
        }
        // 定投频率
        String rateStr = "";
        String timeInvestRate = resultViewModel.getTimeInvestRate();
        String timeInvestRateFlag = resultViewModel.getTimeInvestRateFlag();
        if ("y".equals(timeInvestRateFlag)) {
            rateStr = "每年一次";
        } else if ("w".equals(timeInvestRateFlag)) {
            rateStr = "每周一次";
        } else if ("d".equals(timeInvestRateFlag)) {
            rateStr = timeInvestRate + "天一次";
        } else if ("m".equals(timeInvestRateFlag)) {
            rateStr = timeInvestRate + "个月一次";
            if ("1".equals(timeInvestRate)) {
                rateStr = "每月一次";
            }
            if ("3".equals(timeInvestRate)) {
                rateStr = "每三月一次";
            }
        }
        operationResultView.addDetailRow("定投频率", rateStr, true, false);

        // 首次投资日
        operationResultView.addDetailRow("首次投资日", resultViewModel.getInvestTime(), true, false);
        // 理财账户
        String accountNo = "";
        if (mViewModel.getAccountList() != null && mViewModel.getAccountList().size() > 0) {
            accountNo = mViewModel.getAccountList().get(0).getAccountNo();
            String start = accountNo.substring(0, 4);
            String end = accountNo.substring(accountNo.length() - 4, accountNo.length());
            operationResultView.addDetailRow("理财交易账户", start + " ****** " + end, true, false);
        }


        // 再取一笔
        operationResultView.addContentItem("分享产品", 0);
        operationResultView.addContentItem("我的持仓", 1);
        operationResultView.addContentItem("交易记录", 2);

        if (likeList != null && !likeList.isEmpty()) {
            operationResultView.setYouLikeAdapter(new LikeGridAdapter(mContext, likeList), this);
        }
    }

    @Override
    public void setListener() {
        operationResultView.setOnclick(new BaseOperationResultView.BtnCallback() {
            @Override
            public void onClickListener(View v) {
                if (!ButtonClickLock.isCanClick(clickOprLock)) {// 防止暴力点击
                    return;
                }

                int i = v.getId();
                if (i == 0) { // 分享产品
                    SendMessageToWX.Req req = ShareUtils.shareWebPage(0, WealthConst.getShareProductUrl(resultViewModel.getProductCode(),mViewModel.getProductKind()));
                    if (getApi() != null)
                        getApi().sendReq(req);//跳转到朋友圈或会话列表
                } else if (i == 1) { // 我的持仓
                    start(new FinancialPositionFragment());
                } else { // 交易记录
                    start(new InvestTreatyFragment(InvestTreatyFragment.WEALTHPRODUCTFRAGMENT));
                }
            }
        });

        operationResultView.setgoHomeOnclick(this);
    }

    @Override
    protected String getTitleValue() {
        return "操作结果";
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
    public boolean onBack() {
        handleBackEvent();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButtonClickLock.removeLock(clickOprLock);
    }

    // 处理返回事件
    private void handleBackEvent() {
        WealthProductFragment fragment = findFragment(WealthProductFragment.class);
        if (fragment == null) {
            mActivity.finish();
        } else {
            popToAndReInit(WealthProductFragment.class);
        }
    }

    @Override
    public boolean onBackPress() {
        handleBackEvent();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        start(WealthDetailsFragment.newInstance(likeList.get(position)));
    }

    @Override
    public void onHomeBack() {
        mActivity.finish();
    }
}