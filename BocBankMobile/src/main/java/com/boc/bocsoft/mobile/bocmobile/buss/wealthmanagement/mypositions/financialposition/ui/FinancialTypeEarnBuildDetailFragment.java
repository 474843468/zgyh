package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReferProfitDetailQuery.PsnXpadReferProfitDetailQueryParams;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTabRowTextButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.financelist.FinanceListItemView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.pie.Cercle;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui.InvestTreatyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadSetBonusMode.psnXpadSetBonusModeResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadreferprofitdetailquery.PsnXpadReferProfitDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadreferprofitquery.PsnXpadReferProfitQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialPositionContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialTypeEarBuildDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCodeModeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCommonUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.ui.RedeemFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

/**
 * 中银理财-我的持仓（收益累进）持仓详情
 * Created by cff on 2016/9/19.
 */
public class FinancialTypeEarnBuildDetailFragment extends MvpBussFragment<FinancialTypeEarBuildDetailPresenter> implements View.OnClickListener, FinancialPositionContract.FinancialTypeEarnBuildDetailView {
    // ====================view定义=================start=========
    /**
     * 页面根视图
     */
    private View mRootView;
    //    内容显示区域
    private ScrollView sl_content_view;
    //持仓详情头部
    private DetailTableHead fragment_fixedtermdetail_head_view;
    //持仓详情内容
    private DetailContentView detailContent;
    //列表内容
    private LinearLayout fixedtermList;
    //圆饼图
    private Cercle cash_cercle;
    //提示信息1
    private TextView fixedterm_careinfo;
    //赎回
    private TextView cashmanagement_redeem;
    //继续购买
    private TextView cashmanagement_goonbuy;
    //参考收益
    private TextView fixedterm_expProfit;
    //底部按钮
    private LinearLayout cashmanagment_bottonbtn;
    //赎回Fragment
    private RedeemFragment redeemFragment;
    //无收益时显示
    private TextView earnbuild_noearn;

    private LinearLayout earnbuild_havedate;

    private TextView earnbuild_cashinfo;
    //继续购买得到数据 -账户详情
    private PsnXpadAccountQueryResModel.XPadAccountEntity xpadAccountEntity;
    //更改分红方式 view
    private DetailTabRowTextButton mDetailTabRowTextButton = null;

    // ====================view定义=================end===========
    // ===================接口code===============start=============

    // ===================接口code===============end=============

    // ===================变量义=================start===========

    //产品详情
    private PsnXpadProductDetailQueryResModel productDeta;
    //客户持仓信息
    private PsnXpadProductBalanceQueryResModel banlanceDeta;
    //分红方式选择Dialog
    private SelectStringListDialog yearlyTypeDialog;
    //汇总查询结果
    private PsnXpadReferProfitQueryResModel cashreferfit_info;

    private String mConversationID;
    //当前分红方式
    private String mCurrentBonusMode = "";
    //是否需要列表界面刷新数据
    private boolean isReqFinancialHome = false;


    // ===================变量义=================end===========

    //设置标题头部类型
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_cashmanagement, null);
        return mRootView;
    }

    /**
     * 设置页面标题
     *
     * @return
     */
    @Override
    protected String getTitleValue() {
        return getStringResources(R.string.boc_trans_financial_netvalue_main_title);
    }

    /**
     * 标题栏左侧图标点击事件
     */
    @Override
    protected void titleLeftIconClick() {
        if (isReqFinancialHome) {
            popToAndReInit(FinancialPositionFragment.class);

        } else {
            super.titleLeftIconClick();
        }

    }

    @Override
    protected View getTitleBarView() {
        if ("1".equals(banlanceDeta.getCanAgreementMange())) {
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


    @Override
    public void initView() {

        //内容显示
        sl_content_view = (ScrollView) mRootView.findViewById(R.id.sl_content_view);
        //头部标题显示
        fragment_fixedtermdetail_head_view = (DetailTableHead) mRootView.findViewById(R.id.fragment_cash_head_view);
        //内容显示
        detailContent = (DetailContentView) mRootView.findViewById(R.id.fragment_cash_content_view);
        //参考收益描述
        fixedterm_expProfit = (TextView) mRootView.findViewById(R.id.fixedterm_expProfit);
        //列表添加显示
        fixedtermList = (LinearLayout) mRootView.findViewById(R.id.fixedtermList);
        cash_cercle = (Cercle) mRootView.findViewById(R.id.cash_cercle);
        //底部功能按钮
        cashmanagement_redeem = (TextView) mRootView.findViewById(R.id.cashmanagement_redeem);
        cashmanagement_goonbuy = (TextView) mRootView.findViewById(R.id.cashmanagement_goonbuy);
        cashmanagement_redeem.setOnClickListener(this);
        cashmanagement_goonbuy.setOnClickListener(this);
        //底部提示信息
        fixedterm_careinfo = (TextView) mRootView.findViewById(R.id.fixedterm_careinfo);
        cashmanagment_bottonbtn = (LinearLayout) mRootView.findViewById(R.id.cashmanagment_bottonbtn);
        earnbuild_noearn = (TextView) mRootView.findViewById(R.id.earnbuild_noearn);
        earnbuild_havedate = (LinearLayout) mRootView.findViewById(R.id.earnbuild_havedate);

        earnbuild_cashinfo = (TextView) mRootView.findViewById(R.id.earnbuild_cashinfo);

    }

    @Override
    public void initData() {

        //详情头部描述
        fragment_fixedtermdetail_head_view.setTvSumMargin(getResources().getDimensionPixelOffset(R.dimen.boc_space_between_62px), 0, 0, 0);
        fragment_fixedtermdetail_head_view.updateData(getStringResources(R.string.boc_position_redeem_shares_held), MoneyUtils.transMoneyFormat(banlanceDeta.getHoldingQuantity(), "001"));
        fragment_fixedtermdetail_head_view.setTableRow(getStringResources(R.string.boc_trans_shareconversion_result_useableshare), MoneyUtils.transMoneyFormat(banlanceDeta.getAvailableQuantity(), "001"));
        fragment_fixedtermdetail_head_view.setTableRowTwo(getStringResources(R.string.boc_position_redeem_par_value_tranches), MoneyUtils.transMoneyFormat(banlanceDeta.getSellPrice(), banlanceDeta.getCurCode()));

        //产品详情描述
        detailContent.addTextAndButtonContent(getStringResources(R.string.boc_position_redeem_product_name), banlanceDeta.getProdName(), "（" + banlanceDeta.getProdCode() + ")");

        detailContent.setRightTvListener(new DetailContentView.DetailContentRightTvOnClickListener() {
            @Override
            public void onClickRightTextView() {
                //产品代码点击事件
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
        //预计年化收益率
        if (!FinancialPositionCommonUtil.isShowMax(banlanceDeta.getYearlyRRMax())) {
            detailContent.addTextCntent(getString(R.string.boc_invest_treaty_rate_detail), banlanceDeta.getYearlyRR() + "%", getString(R.string.boc_finance_account_transfer_detail_title), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FinancialTypeProgressQueryFragment progressFragment = new FinancialTypeProgressQueryFragment();
                    progressFragment.setReferDetail(banlanceDeta.getProdName(),
                            banlanceDeta.getProdCode(), banlanceDeta.getBancAccountKey(), true);
                    start(progressFragment);
                }
            });
        } else {
            detailContent.addTextCntent(getString(R.string.boc_invest_treaty_rate_detail), banlanceDeta.getYearlyRR() + "~" + banlanceDeta.getYearlyRRMax() + "%", getString(R.string.boc_finance_account_transfer_detail_title), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FinancialTypeProgressQueryFragment progressFragment = new FinancialTypeProgressQueryFragment();
                    progressFragment.setReferDetail(banlanceDeta.getProdName(),
                            banlanceDeta.getProdCode(), banlanceDeta.getBancAccountKey(), true);
                    start(progressFragment);
                }
            });
        }

        detailContent.addDetail(getStringResources(R.string.boc_wealth_capital_account), NumberUtils.formatStringNumber(banlanceDeta.getBancAccount()), false);
        //  当前分红方式:--> 0：红利再投资、 1：现金分红
        if ("0".equalsIgnoreCase(banlanceDeta.getCurrentBonusMode())) {
            mCurrentBonusMode = "0";
        } else if ("1".equalsIgnoreCase(banlanceDeta.getCurrentBonusMode())) {
            mCurrentBonusMode = "1";
        }
        /**canChangeBonusMode	是否可修改分红方式	String	0：是 1：否  */
        if ("0".equals(banlanceDeta.getCanChangeBonusMode())) {
            mDetailTabRowTextButton = detailContent.addTextCntent(getStringResources(R.string.boc_trans_financial_position_shareouttype), getCurrentBonusModeType(banlanceDeta.getCurrentBonusMode()), getStringResources(R.string.boc_qrpay_change), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更改点击事件
                    showYearlyDialog();
                }
            });
        } else {
            detailContent.addTextCntent(getStringResources(R.string.boc_trans_financial_position_shareouttype), getCurrentBonusModeType(banlanceDeta.getCurrentBonusMode()));
        }
        earnbuild_cashinfo.setText(getString(R.string.boc_trans_financial_position_main_reference) + getCashInfo());
        fixedterm_careinfo.setText(getStringResources(R.string.boc_trans_financial_earnbuild_infoone) + "\n" + getStringResources(R.string.boc_trans_financial_earnbuild_infotwo));

        //判断是否可赎回
        if ("0".equals(banlanceDeta.getCanRedeem())) {
            cashmanagment_bottonbtn.setVisibility(View.VISIBLE);
            cashmanagement_redeem.setVisibility(View.VISIBLE);
        }
        //判断是否可继续购买
        if ("0".equals(banlanceDeta.getCanAddBuy())) {
            cashmanagment_bottonbtn.setVisibility(View.VISIBLE);
            cashmanagement_goonbuy.setVisibility(View.VISIBLE);
        }
        if (!"0".equals(banlanceDeta.getCanRedeem()) && !"0".equals(banlanceDeta.getCanAddBuy())) {
            cashmanagment_bottonbtn.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp2.setMargins(0, PublicUtils.dip2px(mContext, 10), 0, 0);
            sl_content_view.setLayoutParams(lp2);
        }
        //无收益时

        if (!FinancialPositionCommonUtil.isUnequalToZero(banlanceDeta.getExpProfit())) {
            earnbuild_havedate.setVisibility(View.GONE);
            earnbuild_noearn.setVisibility(View.VISIBLE);
        } else {
            //请求参考收益
            showLoadingDialog();
            getPresenter().getPsnXpadReferProfitQuery(banlanceDeta.getBancAccountKey(), banlanceDeta.getProdCode(), banlanceDeta.getProductKind(), banlanceDeta.getCashRemit(), banlanceDeta.getTranSeq());
        }

    }

    @Override
    public void setListener() {
        super.setListener();
    }


    @Override
    public void onClick(View v) {
        if (R.id.cashmanagement_redeem == v.getId()) {
            //赎回操作
//            if (null == redeemFragment) {
            redeemFragment = new RedeemFragment();
//            }
            redeemFragment.setData(banlanceDeta, productDeta, xpadAccountEntity);
            start(redeemFragment);
        } else if (R.id.cashmanagement_goonbuy == v.getId()) {
            if (xpadAccountEntity != null) {
                //继续购买操作
                PurchaseFragment fragment = PurchaseFragment.newInstance(FinancialPositionCodeModeUtil.buildPurchaseInputModeItemParams(productDeta, xpadAccountEntity), null);
                start(fragment);
            } else {
                showErrorDialog("该账户非电子银行关联账户，请您更换账户或者先进行关联再购买。");
            }

        }
    }

    //=================================接口回调处理段落================start==================
//    FinancialTypeEarnBuildDetaileListAdapter m;
    //参考收益列表显示
    private void handlerShowEarnings(PsnXpadReferProfitDetailQueryResModel profitDetail) {

        //参考收益列表 判断显示方式，圆饼图或者列表
        if (profitDetail != null) {
            if (com.boc.bocsoft.mobile.common.utils.PublicUtils.isEmpty(profitDetail.getList())) {
//                fixedtermList.setVisibility(View.GONE);
//                cash_cercle.setVisibility(View.VISIBLE);
                earnbuild_havedate.setVisibility(View.GONE);
                earnbuild_noearn.setVisibility(View.VISIBLE);
            } else {
                fixedtermList.setVisibility(View.VISIBLE);
                cash_cercle.setVisibility(View.GONE);
                earnbuild_havedate.setVisibility(View.VISIBLE);
                earnbuild_noearn.setVisibility(View.GONE);
                for (int i = 0; i < profitDetail.getList().size(); i++) {
                    if (i == profitDetail.getList().size() - 1) {
                        addContentListView(profitDetail.getList().get(i), true);
                    } else {
                        addContentListView(profitDetail.getList().get(i), false);
                    }

                }
            }
        } else {
//            fixedtermList.setVisibility(View.GONE);
//            cash_cercle.setVisibility(View.VISIBLE);
            earnbuild_havedate.setVisibility(View.GONE);
            earnbuild_noearn.setVisibility(View.VISIBLE);
        }

//        }
    }

    //=================================接口回调处理段落================end==================

    //=================================自定义方法段落================start==================
    //参考收益单位描述
    private String getCashInfo() {
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

    /**
     * 为日积月累详情页设置数据
     *
     * @param banlanceDeta 客户持仓信息
     * @param productDeta  产品详情
     */
    public void setFixedEarnbuildDeta(String mConversationID, PsnXpadProductBalanceQueryResModel banlanceDeta, PsnXpadProductDetailQueryResModel productDeta, PsnXpadAccountQueryResModel.XPadAccountEntity xpadAccountEntity) {
        this.mConversationID = mConversationID;
        this.banlanceDeta = banlanceDeta;
        this.productDeta = productDeta;
        this.xpadAccountEntity = xpadAccountEntity;
    }

    //分红方式选择Dialog
    private void showYearlyDialog() {
        List<String> yearTypeList = new ArrayList<String>() {
        };
        yearTypeList.add(getStringResources(R.string.boc_trans_financial_fixedterm_dispense_cash));
        yearTypeList.add(getStringResources(R.string.boc_trans_financial_fixedterm_cashagin));
        if (null == yearlyTypeDialog) {
            yearlyTypeDialog = new SelectStringListDialog(mContext);
            yearlyTypeDialog.setListData(yearTypeList, true);
            yearlyTypeDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                @Override
                public void onSelect(int position, String model) {
                    if (0 == position) {
                        //现金分红传参为1
                        if (!"1".equalsIgnoreCase(mCurrentBonusMode)) {
                            showLoadingDialog();
                            getPresenter().getPsnXpadSetBonusMode(mConversationID, "1", banlanceDeta);
                        }
                    } else if (1 == position) {
                        //红再投为0
                        if (!"0".equalsIgnoreCase(mCurrentBonusMode)) {
                            showLoadingDialog();
                            getPresenter().getPsnXpadSetBonusMode(mConversationID, "0", banlanceDeta);
                        }
                    }
                    yearlyTypeDialog.dismiss();
                }
            });
        }
        yearlyTypeDialog.show();
    }

    //添加收益列表项
    private void addContentListView(PsnXpadReferProfitDetailQueryResModel.QueryModel viewModel, boolean isShowDividerLine) {
        FinanceListItemView itemView = new FinanceListItemView(getContext());
        itemView.isShowDividerLine(true);
        itemView.setTxtHeadLeft(getStringResources(R.string.boc_trans_financial_position_main_reference_nounit) + ":" + MoneyUtils.transMoneyFormat(viewModel.getPayprofit() + "", banlanceDeta.getCurCode()));
        //持有天数
        if (0 >= Integer.valueOf(viewModel.getBaldays())) {
            itemView.setTxtHeadRight(getString(R.string.boc_trans_financial_earnbuild_ended));
        } else {
            itemView.setAppendTextColor(mContext.getResources().getColor(R.color.boc_text_money_color_red), 1,
                    "已持有", viewModel.getBaldays() + "", "天");
        }

        itemView.setTxtCenterName(getStringResources(R.string.boc_trans_financial_earnbuild_startdate), getStringResources(R.string.boc_trans_financial_earnbuild_exyield), getStringResources(R.string.boc_position_redeem_shares_held));
        itemView.setTxtCenterValue(viewModel.getStartdate(), MoneyUtils.transMoneyFormat(viewModel.getExyield() + "", "001") + "%", setMoneyShowType(viewModel.getBalunit()));
        //判断提示是否显示
        if (7 >= Integer.valueOf(viewModel.getNextdays())) {
            itemView.setBottomAttribute(getColorResources(R.color.boc_text_color_red), "恭喜您！再持有" + viewModel.getNextdays() + "天就可获得更高收益率！");
        }
//        itemView.setItemViewMargins(0,10,0,0);
        TextView mNullView = new TextView(mContext);
        mNullView.setHeight(PublicUtils.dip2px(mContext, 15));
        mNullView.setBackgroundResource(R.color.boc_common_bg_color);
        fixedtermList.addView(mNullView);
        fixedtermList.addView(itemView);
    }

    /**
     * 得到颜色资源文件
     *
     * @param color
     * @return
     */
    private int getColorResources(int color) {
        return mContext.getResources().getColor(color);
    }

    /**
     * 得到string文件内容
     *
     * @param stringID
     * @return
     */
    private String getStringResources(int stringID) {
        return getResources().getString(stringID);
    }

    private String getreplaceString(String replacetext) {
        String info = getStringResources(R.string.boc_trans_financial_earnbuild_havedays);
        String info1 = "";
        info1 = info.replace("{0}", replacetext);
        return info1;
    }

    //参考收益描述
    private String getearBuildInfo(String endDate, String totalnummber, String currentEarnings, String totalEarning) {
        String earnbuildinfo = getStringResources(R.string.boc_trans_financial_earnbuild_earnbuildinfo);
        String info1 = "";
        String info2 = "";
        String info3 = "";
        String info4 = "";
        info1 = earnbuildinfo.replace("{0}", endDate + "");

        info2 = info1.replace("{1}", MoneyUtils.transMoneyFormat(totalnummber + "", "001") + "");
        info3 = info2.replace("{2}", MoneyUtils.transMoneyFormat(currentEarnings + "", banlanceDeta.getCurCode()) + "");
        info4 = info3.replace("{3}", MoneyUtils.transMoneyFormat(totalEarning + "", banlanceDeta.getCurCode()) + "");
        return info4;
    }

    /**
     * 获取分红方式
     */
    private String getCurrentBonusModeType(String modelType) {
        if ("0".equals(modelType)) {
            return getString(R.string.boc_trans_financial_fixedterm_cashagin);
        } else if ("1".equals(modelType)) {
            return getString(R.string.boc_trans_financial_fixedterm_dispense_cash);
        }
        return "";
    }

    /**
     * 份额显示规则{12300----》1.23万；----》亿}
     *
     * @param mHoldingQuantity
     * @return
     */
    private SpannableString setMoneyShowType(String mHoldingQuantity) {
        try {
            SpannableString mSpannableString;
            if (!StringUtils.isEmptyOrNull(mHoldingQuantity)) {
                String[] mMoney = MoneyUtils.convertNumberWanOrYiSplit(mHoldingQuantity);

                if (mMoney != null && mMoney.length > 0) {
                    if (mMoney.length == 1) {
                        LogUtils.d("yx-------mMoney[0]--->" + mMoney[0]);
                        mSpannableString = new SpannableString(MoneyUtils.transMoneyFormat(mHoldingQuantity, "001"));
                        LogUtils.d("yx-------mMoney111--->" + MoneyUtils.transMoneyFormat(mHoldingQuantity, "001"));
                        mSpannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.boc_text_color_dark_gray)), 0, MoneyUtils.transMoneyFormat(mMoney[0], "001").length(),
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        mSpannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, MoneyUtils.transMoneyFormat(mMoney[0], "001").length(),
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        return mSpannableString;
                    } else if (mMoney.length == 2) {
                        LogUtils.d("yx-------mMoney[0]--->" + mMoney[0]);
                        LogUtils.d("yx-------mMoney[1]--->" + mMoney[1]);
                        String mString = mMoney[0] + mMoney[1];//拼接字段   例如 1.23+万
                        mSpannableString = new SpannableString(mString);
                        mSpannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.boc_text_color_dark_gray)), 0, mString.length() - 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mSpannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, mString.length() - 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        mSpannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.boc_text_color_dark_gray)), mString.length() - 1, mString.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mSpannableString.setSpan(new AbsoluteSizeSpan(13, true), mString.length() - 1, mString.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        return mSpannableString;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SpannableString(mHoldingQuantity);
    }
    //=================================自定义方法段落================end==================

    /**
     * 参考收益详情查询 PsnXpadReferProfitDetailQuery
     */
    @Override
    public void obtainPsnXpadReferProfitDetailQuerySuccess(PsnXpadReferProfitDetailQueryResModel resModel) {
        closeProgressDialog();
        handlerShowEarnings(resModel);
    }

    /**
     * 参考收益详情查询 PsnXpadReferProfitDetailQuery
     */
    @Override
    public void obtainPsnXpadReferProfitDetailQueryFault() {
        closeProgressDialog();
        earnbuild_havedate.setVisibility(View.GONE);
        earnbuild_noearn.setVisibility(View.VISIBLE);

    }

    @Override
    public void obtainPsnXpadReferProfitQuerySuccess(PsnXpadReferProfitQueryResModel mViewModel) {
        if (mViewModel != null) {
            cashreferfit_info = mViewModel;
            //参考收益描述
            fixedterm_expProfit.setText(getearBuildInfo(cashreferfit_info.getIntedate(), banlanceDeta.getHoldingQuantity(), cashreferfit_info.getTotalprofit(), cashreferfit_info.getProfit()));
//        fixedtermcercle.setShowText(Float.valueOf(mViewModel.getProfit()),Float.valueOf(mViewModel.getUnpayprofit()));
            getReferProfitDetailQuery();
        } else {
            earnbuild_havedate.setVisibility(View.GONE);
            earnbuild_noearn.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 参考收益汇总查询 失败
     */
    @Override
    public void obtainPsnXpadReferProfitQueryFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        earnbuild_havedate.setVisibility(View.GONE);
        earnbuild_noearn.setVisibility(View.VISIBLE);
        if (!"XPAD.A500".equalsIgnoreCase(biiResultErrorException.getErrorCode() + "")) {
            showErrorDialog(biiResultErrorException.getErrorMessage() + "");
        }
    }

    /**
     * 4.14 014修改分红方式交易 PsnXpadSetBonusMode 成功回调
     *
     * @param mViewModel
     */
    @Override
    public void obtainPsnXpadSetBonusModeSuccess(psnXpadSetBonusModeResModel mViewModel) {
        closeProgressDialog();
        isReqFinancialHome = true;
        handlerPsnXpadSetBonusMode(mViewModel);

    }

    /**
     * 4.14 014修改分红方式交易 PsnXpadSetBonusMode 失败回调
     *
     * @param biiResultErrorException
     */
    @Override
    public void obtainPsnXpadSetBonusModeFail(BiiResultErrorException biiResultErrorException) {
        isReqFinancialHome = false;
        closeProgressDialog();
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    protected FinancialTypeEarBuildDetailPresenter initPresenter() {
        return new FinancialTypeEarBuildDetailPresenter(this);
    }

    /**
     * 请求收益累进列表数据
     */
    private void getReferProfitDetailQuery() {
        PsnXpadReferProfitDetailQueryParams profitParams = new PsnXpadReferProfitDetailQueryParams();
        profitParams.setConversationId(mConversationID);
        profitParams.setAccountKey(banlanceDeta.getBancAccountKey());
        profitParams.setProductCode(banlanceDeta.getProdCode());
        profitParams.setProgressionflag(banlanceDeta.getProgressionflag() + "");
        profitParams.setKind(banlanceDeta.getProductKind());
//        profitParams.setStartDate("");
//        profitParams.setEndDate("");
        profitParams.setCashRemit(banlanceDeta.getCashRemit());
        profitParams.setTranSeq(banlanceDeta.getTranSeq());
        profitParams.setPageSize("100");
        profitParams.setCurrentIndex("0");
        profitParams.set_refresh("true");
        getPresenter().getPsnXpadReferProfitDetailQuery(profitParams);
    }
//======================================接口返回处理段落======

    /**
     * 4.14 014修改分红方式交易 PsnXpadSetBonusMode 响应处理
     *
     * @param mViewModel
     */
    private void handlerPsnXpadSetBonusMode(psnXpadSetBonusModeResModel mViewModel) {
        if (mViewModel != null) {
            //0：红利再投资、1：现金分红
            if ("0".equalsIgnoreCase(mCurrentBonusMode)) {
                mCurrentBonusMode = "1";
                //红利再投资为0
                mDetailTabRowTextButton.addTextAndValue(getStringResources(R.string.boc_trans_financial_position_shareouttype),
                        getStringResources(R.string.boc_trans_financial_fixedterm_dispense_cash), getStringResources(R.string.boc_qrpay_change) + "");
            } else if ("1".equalsIgnoreCase(mCurrentBonusMode)) {
                mCurrentBonusMode = "0";
                mDetailTabRowTextButton.addTextAndValue(getStringResources(R.string.boc_trans_financial_position_shareouttype),
                        getStringResources(R.string.boc_trans_financial_fixedterm_cashagin), getStringResources(R.string.boc_qrpay_change) + "");
            }
        }
    }
}

