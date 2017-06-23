package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.pie.Cercle;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui.InvestTreatyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadreferprofitquery.PsnXpadReferProfitQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialPositionContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialTypeOutStandDetaileQueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCommonUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.ui.RedeemFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.ui.ShareConversionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

/**
 * 持仓详情---业绩基准-详情-收益汇总图标显示-业绩基准详情界面
 * Created by zn on 2016/10/9.
 */
public class FinancialTypeOutStandDetaileQueryFragment extends MvpBussFragment<FinancialTypeOutStandDetaileQueryPresenter>
        implements View.OnClickListener, FinancialPositionContract.FinancialTypeOutStandQueryView {
    private static final String TAG = "FinancialTypeOutStandDetaileQueryFragment";
// =================================上下文对象定义=================start=================
    /**
     * 页面根视图
     */
    private View mRootView;
    //    内容显示区域
    private ScrollView sl_content_view;
    // =================================上下文对象定义=================end=================

    // =================================view定义=================start=================
    /**
     * 业绩基准-持有份额
     */
    private DetailTableHead outstand_fragment_detailtabhead;
    //内容显示
    private DetailContentView outstand_fragment_detailcontent_view1;
    /**
     * （有详情）显示内容（-产品名称，预期年化收益率，资金帐户，分红方式，参考收益）
     * （无详情）显示内容（-产品名称，业绩基准，资金帐户，）
     */
    private DetailContentView outstand_fragment_detailcontent_view2;
    //暂无收益信息
    private TextView fragment_outstand_noearn;
    //无详情 图表显示
    private LinearLayout outstand_fragment_linear_list_chart;
    /**
     * 当前收益
     */
    private TextView outstand_fragment_current_income;
    /**
     * 计息周期
     */
    private TextView outstand_fragment_interest_time;
    /**
     * 百分比显示，图表
     */
    private Cercle outstand_fragment_cercle;
    /**
     * LinearLayout  收益明细
     */
    private LinearLayout outstand_fragment_income_defauilt_linear;
    /**
     * 按钮———收益明细
     */
    private TextView outstand_fragment_income_defauilt_test;
    /**
     * 投资提示
     */
    private TextView outstand_fragment_message_defauilt;
    /**
     * 分红类型Dialog
     */
    protected SelectStringListDialog mPeriodDialog;
    //业绩基准的预期年华收益率
    private FinancialPsnXpadExpectYieldQueryFragment yieldQueryFragment;

    //37接口数据
    private PsnXpadAccountQueryResModel.XPadAccountEntity mItemXPadAccountEntity = null;

    // =================================view定义=================end=================

    // =================================接口code=================start=================

    // =================================接口code定义=================end=================

    // =================================变量义=================start=================
    //收益汇总查询
    private PsnXpadReferProfitQueryResModel mReferProfitQueryDate;
    //客户持仓信息
    private PsnXpadProductBalanceQueryResModel banlanceDeta;
    //收益明细列表
    private FinancialTypeReferDetailFragment referdetail = null;
    //赎回Fragment
    private RedeemFragment redeemFragment;
    //产品详情
    private PsnXpadProductDetailQueryResModel productDeta;
    //item详细
    private PsnXpadQuantityDetailResModel.ListEntity mListInfo;
    //份额转换页
    private ShareConversionFragment mShareConversionFragment;
    //赎回
    private TextView outstandquerys_redeem;
    //份额转换
    private TextView outstandquerys_goonbuy;
    //底部按钮
    private LinearLayout outstandquerys_bottonbtn;
    //会话ID
    private String mConversationID;
    //参考收益名称
    private TextView fragment_outstand_shoyiinfo;

    // =================================变量定义=================end=================

    /**
     * 加载布局
     *
     * @param mInflater
     * @return
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_financial_outstand_quear_main, null);
        return mRootView;
    }


    /**
     * 标题栏左侧图标点击事件
     */
    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    /**
     * 标题栏右侧图标点击事件
     */
    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    /**
     * 设置标题
     */
    @Override
    protected String getTitleValue() {
        return getContext().getString(R.string.boc_trans_financial_netvalue_main_title);
    }

    /**
     * 头部风格
     */
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    /**
     * 头部右侧标题设置
     */
    @Override
    protected View getTitleBarView() {
        if ("1".equals(mListInfo.getCanAgreementMange())) {
            TitleBarView titleBarView = (TitleBarView) super.getTitleBarView();
            //投资协议点击事件
            titleBarView.setRightButton(getResources().getString(R.string.boc_trans_financial_fixedterm_agteement), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start(new InvestTreatyFragment());
                }
            });
            return titleBarView;
        } else {
            TitleBarView titleBarView = (TitleBarView) super.getTitleBarView();
            //投资协议点击事件
            titleBarView.setRightButton(null, null);
            return titleBarView;
        }
    }

    /**
     * 初始化view 之前
     */
    @Override
    public void beforeInitView() {
    }

    /**
     * 是否显示右侧标题按钮
     */
    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    /**
     * 初始化view
     */
    @Override
    public void initView() {
        //内容显示
        sl_content_view = (ScrollView) mRootView.findViewById(R.id.sl_content_view);
        outstand_fragment_linear_list_chart = (LinearLayout) mRootView.findViewById(R.id.outstand_fragment_linear_list_chart);
        outstand_fragment_linear_list_chart.setVisibility(View.VISIBLE);
        outstand_fragment_detailtabhead = (DetailTableHead) mRootView.findViewById(R.id.outstand_fragment_detailtabhead);
        outstand_fragment_detailcontent_view2 = (DetailContentView) mRootView.findViewById(R.id.outstand_fragment_detailcontent_view2);
        outstand_fragment_current_income = (TextView) mRootView.findViewById(R.id.outstand_fragment_current_income);
        outstand_fragment_interest_time = (TextView) mRootView.findViewById(R.id.outstand_fragment_interest_time);
        outstand_fragment_cercle = (Cercle) mRootView.findViewById(R.id.outstand_fragment_cercle);
        outstand_fragment_income_defauilt_linear = (LinearLayout) mRootView.findViewById(R.id.outstand_fragment_income_defauilt_linear);
        outstand_fragment_income_defauilt_test = (TextView) mRootView.findViewById(R.id.outstand_fragment_income_defauilt_test);
        outstand_fragment_income_defauilt_test.setOnClickListener(this);
        outstand_fragment_message_defauilt = (TextView) mRootView.findViewById(R.id.outstand_fragment_message_defauilt);
        fragment_outstand_noearn = (TextView) mRootView.findViewById(R.id.fragment_outstand_noearn);
        fragment_outstand_shoyiinfo = (TextView) mRootView.findViewById(R.id.fragment_outstand_shoyiinfo);
        //底部按钮
        outstandquerys_bottonbtn = (LinearLayout) mRootView.findViewById(R.id.outstandquerys_bottonbtn);
        //赎回
        outstandquerys_redeem = (TextView) mRootView.findViewById(R.id.outstandquerys_redeem);
        //份额转换
        outstandquerys_goonbuy = (TextView) mRootView.findViewById(R.id.outstandquerys_goonbuy);

    }

    /**
     * 初始化接口调用Presenter
     *
     * @return
     */
    @Override
    protected FinancialTypeOutStandDetaileQueryPresenter initPresenter() {
        return new FinancialTypeOutStandDetaileQueryPresenter(this);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        //参考收益描述
        fragment_outstand_shoyiinfo.setText(getString(R.string.boc_trans_financial_position_main_reference) + getOutStandInfo());
        //根据产品详情返回字段判断按钮显示，隐藏
        /**
         * 添加详情
         */
        //头部-持有份额  内容显示
        outstand_fragment_detailtabhead.updateData(getStringResources(R.string.boc_position_redeem_shares_held),
                MoneyUtils.transMoneyFormat(banlanceDeta.getHoldingQuantity(), "001"));
        //可用份额
        outstand_fragment_detailtabhead.setTableRow(getStringResources(R.string.boc_trans_shareconversion_result_useableshare),
                MoneyUtils.transMoneyFormat(banlanceDeta.getAvailableQuantity(), "001"));
        //份额面值
        outstand_fragment_detailtabhead.setTableRowTwo(getStringResources(R.string.boc_position_redeem_par_value_tranches),
                MoneyUtils.transMoneyFormat(banlanceDeta.getSellPrice(), banlanceDeta.getCurCode()));
        //产品名称
        outstand_fragment_detailcontent_view2.addTextAndButtonContent(getStringResources(R.string.boc_position_redeem_product_name), banlanceDeta.getProdName(), "（" + banlanceDeta.getProdCode() + ")");

        outstand_fragment_detailcontent_view2.setRightTvListener(new DetailContentView.DetailContentRightTvOnClickListener() {
            @Override
            public void onClickRightTextView() {
                //产品名称点击事件
                LogUtils.d("yx------------点击了右侧按钮");
                WealthDetailsFragment fragment = new WealthDetailsFragment();
                DetailsRequestBean detailsModel = new DetailsRequestBean();
                detailsModel.setProdCode(banlanceDeta.getProdCode());
                detailsModel.setProdKind(banlanceDeta.getProductKind());
                detailsModel.setIssueType(banlanceDeta.getIssueType());
                detailsModel.setIbknum("");
                Bundle bundle = new Bundle();
                bundle.putBoolean(WealthDetailsFragment.OTHER, true);
                bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, detailsModel);
                fragment.setArguments(bundle);
                start(fragment);
            }
        });

        /**
         * 不带%号，如果不为0，与yearlyRR字段组成区间
         * yearlyRR  yearlyRRMax
         */
        if (!FinancialPositionCommonUtil.isShowMax(banlanceDeta.getYearlyRRMax())) {
            //业绩基准
            outstand_fragment_detailcontent_view2.addTextCntent(
                    getStringResources(R.string.boc_trans_financial_position_main_title),
                    banlanceDeta.getYearlyRR() + "%",
                    getStringResources(R.string.boc_finance_account_transfer_detail_title),
                    new RateOnClickListener());
        } else {
            //业绩基准
            outstand_fragment_detailcontent_view2.addTextCntent(
                    getStringResources(R.string.boc_trans_financial_position_main_title),
                    banlanceDeta.getYearlyRR() + "~" + banlanceDeta.getYearlyRRMax() + "%",
                    getStringResources(R.string.boc_finance_account_transfer_detail_title),
                    new RateOnClickListener());
        }
        //资金账户
        outstand_fragment_detailcontent_view2.addDetail(getStringResources(R.string.boc_wealth_capital_account),
                NumberUtils.formatStringNumber(mListInfo.getBancAccount()), false);
        if (("-1").equalsIgnoreCase(banlanceDeta.getProdEnd())) {
            //产品到期日
            outstand_fragment_detailcontent_view2.addTextCntent(getStringResources(R.string.boc_trans_financial_position_duedate), "长期");
        } else {
            //产品到期日
            outstand_fragment_detailcontent_view2.addTextCntent(getStringResources(R.string.boc_trans_financial_position_duedate),
                    banlanceDeta.getProdEnd());
        }
        if (!FinancialPositionCommonUtil.isUnequalToZero(mListInfo.getExpProfit())) {
            outstand_fragment_linear_list_chart.setVisibility(View.GONE);
            fragment_outstand_noearn.setVisibility(View.VISIBLE);
        } else {
            if ("027".equalsIgnoreCase(banlanceDeta.getCurCode())) {
                outstand_fragment_cercle.setIsRiyuan(true);
            }
            if (mReferProfitQueryDate != null) {
                //显示为饼图
                outstand_fragment_cercle.setShowText(Float.valueOf(mReferProfitQueryDate.getPayprofit()),
                        Float.valueOf(mReferProfitQueryDate.getUnpayprofit()));
                outstand_fragment_current_income.setText("当期收益为 " + MoneyUtils.transMoneyFormat(mReferProfitQueryDate.getProfit(), "001") + "元");
                outstand_fragment_interest_time.setText("上一计息周期为 " + mReferProfitQueryDate.getIntsdate()
                        + " -" + mReferProfitQueryDate.getIntedate());
            } else {
                outstand_fragment_linear_list_chart.setVisibility(View.GONE);
                fragment_outstand_noearn.setVisibility(View.VISIBLE);
            }
        }
        LogUtils.d("yx---------是否可以赎回---->"+mListInfo.getCanRedeem());
        LogUtils.d("yx---------是否可以份额转换---->"+mListInfo.getCanQuantityExchange());
        //判断是否可赎回
        if ("0".equals(mListInfo.getCanRedeem())) {
            LogUtils.d("yx---------可以赎回---->");
            outstandquerys_bottonbtn.setVisibility(View.VISIBLE);
            outstandquerys_redeem.setVisibility(View.VISIBLE);
        }
        //  canQuantityExchange	是否可份额转换 0：是 1：否
        if ("0".equals(mListInfo.getCanQuantityExchange())) {
            LogUtils.d("yx---------可以份额转换---->");
            outstandquerys_bottonbtn.setVisibility(View.VISIBLE);
            outstandquerys_goonbuy.setVisibility(View.VISIBLE);
            outstandquerys_goonbuy.setText(getStringResources(R.string.boc_trans_shareconversion_main_title));
        }
        if (!"0".equals(banlanceDeta.getCanRedeem()) && !"0".equals(banlanceDeta.getCanQuantityExchange())) {
            outstandquerys_bottonbtn.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, PublicUtils.dip2px(mContext, 10), 0, 0);
            sl_content_view.setLayoutParams(lp);
        }
    }

    /**
     * 初始化监听
     */
    @Override
    public void setListener() {
        outstandquerys_redeem.setOnClickListener(this);
        outstandquerys_goonbuy.setOnClickListener(this);
    }

    /**
     * 监听事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        //赎回
        if (v.getId() == R.id.outstandquerys_redeem) {
            redeemFragment = new RedeemFragment();
            redeemFragment.setData(banlanceDeta, productDeta, mListInfo, mItemXPadAccountEntity);
            start(redeemFragment);
        }
        //份额转换
        if (v.getId() == R.id.outstandquerys_goonbuy) {
            //执行份额转换
            mShareConversionFragment = new ShareConversionFragment();
            mShareConversionFragment.setShareConversionDeta(banlanceDeta, productDeta, mListInfo, mItemXPadAccountEntity);
            start(mShareConversionFragment);
        }
        if (v.getId() == R.id.outstand_fragment_income_defauilt_test) {
            //查询收益明细
            //收益明细点击事件
            referdetail = new FinancialTypeReferDetailFragment();
            referdetail.setReferDetail(mConversationID, banlanceDeta,mListInfo.getTranSeq());
            start(referdetail);
//            getFixedtermDetailNet();
//            showLoadingDialog();
        }
    }

    /**
     * 为业绩基准图表页设置数据
     *
     * @param referProfitQueryDate
     */
    public void setDetaileQueryDeta(PsnXpadReferProfitQueryResModel referProfitQueryDate,
                                    PsnXpadProductBalanceQueryResModel banlanceDeta,
                                    PsnXpadProductDetailQueryResModel productDeta,
                                    PsnXpadQuantityDetailResModel.ListEntity mListInfo,
                                    String mConversationID, PsnXpadAccountQueryResModel.XPadAccountEntity mItemXPadAccountEntity) {
        this.mReferProfitQueryDate = referProfitQueryDate;
        this.banlanceDeta = banlanceDeta;
        this.productDeta = productDeta;
        this.mListInfo = mListInfo;
        this.mConversationID = mConversationID;
        this.mItemXPadAccountEntity = mItemXPadAccountEntity;
    }
    // =================================网络请求处理段落=================start=================

    //    /**
//     * 请求收益详情列表
//     */
//    private void getFixedtermDetailNet() {
//        PsnXpadReferProfitDetailQueryParams params = new PsnXpadReferProfitDetailQueryParams();
//        params.setAccountKey(banlanceDeta.getBancAccountKey());
//        params.setProductCode(banlanceDeta.getProdCode());
//        params.setProgressionflag(banlanceDeta.getProgressionflag());
//        params.setKind(banlanceDeta.getProductKind());
//        params.setStartDate("");
//        params.setEndDate("");
//        params.setPageSize("10");
//        params.setCurrentIndex("1");
//        params.set_refresh("true");
//        params.setCashRemit(banlanceDeta.getCashRemit());
//        params.setProductCode(banlanceDeta.getProdCode());
//        params.setTranSeq(banlanceDeta.getTranSeq());
//        showLoadingDialog();
//        getPresenter().getPsnXpadReferProfitDetailQuery(params);
//    }
    // =================================网络请求处理段落=================end=================
    // =================================接口响应处理段落=================start=================
    @Override
    public void setPresenter(BasePresenter presenter) {
    }
    // =================================接口响应处理段落=================end=================
    // =================================自定义方法段落=================start=================

    /**
     * 得到颜色资源文件
     *
     * @param color
     * @return
     */
    private int getColorResources(int color) {
        return mContext.getResources().getColor(color);
    }

    //（有明细）产品名称--详情点击事件
    private class ProdNameOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ToastUtils.show("查看详情");
        }
    }

    //（有明细）业绩基准--详情点击事件
    private class RateOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (yieldQueryFragment != null) {
                yieldQueryFragment.setYieldQueryDeta(banlanceDeta.getProdCode(), true);
                start(yieldQueryFragment);
            } else {
                FinancialPsnXpadExpectYieldQueryFragment fragment = new FinancialPsnXpadExpectYieldQueryFragment();
                fragment.setYieldQueryDeta(banlanceDeta.getProdCode(), true);
                start(fragment);
            }
        }
    }
    // =================================自定义方法段落=================end=================
    // =================================接口调用段落=================start=================
    // =================================接口调用段落=================end=================
    // =================================公共方法处理段=================start=================

    /**
     * 得到颜色资源文件
     *
     * @param stringCode
     * @return
     */
    private String getStringResources(int stringCode) {
        return mContext.getResources().getString(stringCode);
    }

    //参考收益单位描述
    private String getOutStandInfo() {
        if ("00".equalsIgnoreCase(banlanceDeta.getCashRemit())) {
            return "(元)";
        } else if ("01".equalsIgnoreCase(banlanceDeta.getCashRemit())) {
//                钞汇标识	String	01：钞   02：汇   00：人民币
            return "(" + PublicCodeUtils.getCurrency(mContext, banlanceDeta.getCurCode()) + "/钞)";
        } else if ("02".equalsIgnoreCase(banlanceDeta.getCashRemit())) {
            return "(" + PublicCodeUtils.getCurrency(mContext, banlanceDeta.getCurCode()) + "/汇)";
        }
        return "";
    }
    // =================================公共方法处理段=================end=================
}
