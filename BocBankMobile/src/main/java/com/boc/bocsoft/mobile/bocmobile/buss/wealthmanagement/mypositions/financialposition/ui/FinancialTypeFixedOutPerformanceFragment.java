package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTabRowTextButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.pie.Cercle;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui.InvestTreatyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadSetBonusMode.psnXpadSetBonusModeResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadreferprofitdetailquery.PsnXpadReferProfitDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadreferprofitquery.PsnXpadReferProfitQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialPositionContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialTypeFixedTermDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCodeModeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCommonUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.ui.RedeemFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

/**
 * 持仓详情-固定期限类-非业绩基准
 * Created by cff on 2016/10/20.
 */
public class FinancialTypeFixedOutPerformanceFragment extends MvpBussFragment<FinancialTypeFixedTermDetailPresenter> implements View.OnClickListener, FinancialPositionContract.FinancialTypeFixedTermDetailView {

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
    //圆饼图
    private Cercle fixedtermcercle;
    //底部提示信息
    private TextView fixedterm_careinfoone;
    //底部按钮
    private LinearLayout fixedterm_bottonbtn;
    //无收益时信息提示
    private TextView fixedterm_noearn;

    private LinearLayout fixedterm_haveearn;

    private TextView fixedtermout_currentcode;
    //分红方式 所在view
    private DetailTabRowTextButton mDetailTabRowTextButton = null;
    // ====================view定义=================end===========
    // ===================接口code===============start=============


    // ===================接口code===============end=============

    // ===================变量义=================start===========
    //赎回Fragment
    private RedeemFragment redeemFragment;
    //产品详情
    private PsnXpadProductDetailQueryResModel productDeta;
    //客户持仓信息
    private PsnXpadProductBalanceQueryResModel banlanceDeta;
    //收益汇总对象列表
    private PsnXpadReferProfitQueryResModel fixedrefit_info = new PsnXpadReferProfitQueryResModel();
    //参考收益
    private TextView fixedterm_expProfit;
    //收益明细
    private TextView fixedterm_statment;
    //赎回按钮
    private TextView fixedterm_redeem;

    //继续购买按钮
    private TextView fixedterm_goonbuy;
    //分红方式选择Dialog
    private SelectStringListDialog yearlyTypeDialog;

    //会话ID
    private String mConversationID;
    //继续购买得到数据
    private PsnXpadAccountQueryResModel.XPadAccountEntity xpadAccountEntity;
    //当前分红方式
    private String mCurrentBonusMode = "";
    //是否需要列表界面刷新数据
    private boolean isReqFinancialHome = false;
    // ===================变量义=================end===========


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_fixedtermoutperformance, null);
        return mRootView;
    }

    /**
     * 设置标题
     *
     * @return
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

    /**
     * 头部右侧标题设置
     */
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

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        //内容显示
        sl_content_view = (ScrollView) mRootView.findViewById(R.id.sl_content_view);
        //头部标题显示
        fragment_fixedtermdetail_head_view = (DetailTableHead) mRootView.findViewById(R.id.fragment_fixedtermdetail_head_view);
        //底部提示语
        fixedterm_careinfoone = (TextView) mRootView.findViewById(R.id.fixedterm_careinfoone);
        //内容显示
        detailContent = (DetailContentView) mRootView.findViewById(R.id.fragment_fixedtermdetail_content_view);
        //参考收益描述
        fixedterm_expProfit = (TextView) mRootView.findViewById(R.id.fixedterm_expProfit);
        fixedterm_haveearn = (LinearLayout) mRootView.findViewById(R.id.fixedterm_haveearn);
        //圆饼图
        fixedtermcercle = (Cercle) mRootView.findViewById(R.id.fixedtermcercle);
        fixedtermcercle.setShowText((float) 0.00, (float) 0.00);
        //收益明细
        fixedterm_statment = (TextView) mRootView.findViewById(R.id.fixedterm_statment);
        fixedterm_statment.setOnClickListener(this);
        fixedterm_redeem = (TextView) mRootView.findViewById(R.id.fixedterm_redeem);
        fixedterm_goonbuy = (TextView) mRootView.findViewById(R.id.fixedterm_goonbuy);
        fixedterm_redeem.setOnClickListener(this);
        fixedterm_goonbuy.setOnClickListener(this);
        //底部按钮
        fixedterm_bottonbtn = (LinearLayout) mRootView.findViewById(R.id.fixedterm_bottonbtn);
        fixedterm_noearn = (TextView) mRootView.findViewById(R.id.fixedterm_noearn);

        fixedtermout_currentcode = (TextView) mRootView.findViewById(R.id.fixedtermout_currentcode);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        //头部信息
        fragment_fixedtermdetail_head_view.setTvSumMargin(getResources().getDimensionPixelOffset(R.dimen.boc_space_between_62px), 0, 0, 0);
        fragment_fixedtermdetail_head_view.updateData(getResourceString(R.string.boc_position_redeem_shares_held), MoneyUtils.transMoneyFormat(banlanceDeta.getHoldingQuantity(), "001"));
        fragment_fixedtermdetail_head_view.setTableRow(getResourceString(R.string.boc_trans_shareconversion_result_useableshare), MoneyUtils.transMoneyFormat(banlanceDeta.getAvailableQuantity(), "001"));
        fragment_fixedtermdetail_head_view.setTableRowTwo(getResourceString(R.string.boc_position_redeem_par_value_tranches), MoneyUtils.transMoneyFormat(banlanceDeta.getSellPrice(), banlanceDeta.getCurCode()));
        //中间内容显示
        detailContent.addTextAndButtonContent(getResourceString(R.string.boc_position_redeem_product_name), banlanceDeta.getProdName(), "（" + banlanceDeta.getProdCode() + ")");

        detailContent.setRightTvListener(new DetailContentView.DetailContentRightTvOnClickListener() {
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
        //预计年化收益率
        if (!FinancialPositionCommonUtil.isShowMax(banlanceDeta.getYearlyRRMax())) {
            detailContent.addTextCntent(getResourceString(R.string.boc_invest_treaty_rate_detail), banlanceDeta.getYearlyRR() + "%");
        } else {
            detailContent.addTextCntent(getResourceString(R.string.boc_invest_treaty_rate_detail), banlanceDeta.getYearlyRR() + "~" + banlanceDeta.getYearlyRRMax() + "%");
        } ;

//        //预计年化收益率
//        if (!FinancialPositionCommonUtil.isShowMax(banlanceDeta.getYearlyRRMax())) {
//            detailContent.addTextCntent(getResourceString(R.string.boc_invest_treaty_rate_detail), banlanceDeta.getYearlyRR() + "%", getResourceString(R.string.boc_finance_account_transfer_detail_title), new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    FinancialTypeProgressQueryFragment progressFragment = new FinancialTypeProgressQueryFragment();
//                    progressFragment.setReferDetail(banlanceDeta.getProdName(),
//                            banlanceDeta.getProdCode(), banlanceDeta.getBancAccountKey(), true);
//                    start(progressFragment);
//                }
//            });
//        } else {
//            detailContent.addTextCntent(getResourceString(R.string.boc_invest_treaty_rate_detail), banlanceDeta.getYearlyRR() + "~" + banlanceDeta.getYearlyRRMax() + "%", getResourceString(R.string.boc_finance_account_transfer_detail_title), new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    FinancialTypeProgressQueryFragment progressFragment = new FinancialTypeProgressQueryFragment();
//                    progressFragment.setReferDetail(banlanceDeta.getProdName(),
//                            banlanceDeta.getProdCode(), banlanceDeta.getBancAccountKey(), true);
//                    start(progressFragment);
//                }
//            });
//        }
        //预计年化收益率点击事件

        detailContent.addDetail(getResourceString(R.string.boc_wealth_capital_account), NumberUtils.formatStringNumber(banlanceDeta.getBancAccount()), false);
        //  当前分红方式:--> 0：红利再投资、 1：现金分红
        if ("0".equalsIgnoreCase(banlanceDeta.getCurrentBonusMode())) {
            mCurrentBonusMode = "0";
        } else if ("1".equalsIgnoreCase(banlanceDeta.getCurrentBonusMode())) {
            mCurrentBonusMode = "1";
        }
        /**canChangeBonusMode	是否可修改分红方式	String	0：是 1：否  */
        if ("0".equals(banlanceDeta.getCanChangeBonusMode())) {
            //更改分红方式 view
            mDetailTabRowTextButton = detailContent.addTextCntent(getResourceString(R.string.boc_trans_financial_position_shareouttype), getCurrentBonusModeType(banlanceDeta.getCurrentBonusMode()), getResourceString(R.string.boc_details_update), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //更改点击事件
                    showYearlyDialog();
                }
            });
        } else {
            detailContent.addTextCntent(getResourceString(R.string.boc_trans_financial_position_shareouttype), getCurrentBonusModeType(banlanceDeta.getCurrentBonusMode()));
        }
        //参考收益描述
        fixedtermout_currentcode.setText(getString(R.string.boc_trans_financial_position_main_reference) + getCashInfo());

        //判断是否可赎回
        if ("0".equals(banlanceDeta.getCanRedeem())) {
            fixedterm_bottonbtn.setVisibility(View.VISIBLE);
            fixedterm_redeem.setVisibility(View.VISIBLE);
        }
        //判断是否可继续购买
        if ("0".equals(banlanceDeta.getCanAddBuy())) {
            fixedterm_bottonbtn.setVisibility(View.VISIBLE);
            fixedterm_goonbuy.setVisibility(View.VISIBLE);
        }
        if (!"0".equals(banlanceDeta.getCanRedeem()) && !"0".equals(banlanceDeta.getCanAddBuy())) {
            fixedterm_bottonbtn.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp2.setMargins(0, PublicUtils.dip2px(mContext, 10), 0, 0);
            sl_content_view.setLayoutParams(lp2);
        }
        //暂无收益
        if (!FinancialPositionCommonUtil.isUnequalToZero(banlanceDeta.getExpProfit())) {
            fixedterm_haveearn.setVisibility(View.GONE);
            fixedterm_noearn.setVisibility(View.VISIBLE);
        } else {
            //请求收益汇总
            showLoadingDialog();
            getPresenter().getPsnXpadReferProfitQuery(banlanceDeta.getBancAccountKey(), banlanceDeta.getProdCode(), banlanceDeta.getProductKind(), banlanceDeta.getCashRemit(), banlanceDeta.getTranSeq());
        }

    }

    /**
     * 初始化监听
     */
    @Override
    public void setListener() {
        super.setListener();
    }

    @Override
    public void onClick(View v) {
        if (R.id.fixedterm_statment == v.getId()) {
            //收益明细点击事件
            FinancialTypeReferDetailFragment referdetail = new FinancialTypeReferDetailFragment();
            referdetail.setReferDetail(mConversationID, banlanceDeta,"");
            start(referdetail);


        } else if (R.id.fixedterm_redeem == v.getId()) {
            //赎回点击操作
//            if (null == redeemFragment) {
            redeemFragment = new RedeemFragment();
//            }
            redeemFragment.setData(banlanceDeta, productDeta, xpadAccountEntity);
            start(redeemFragment);
        } else if (R.id.fixedterm_goonbuy == v.getId()) {
            if (xpadAccountEntity != null) {
                //继续购买操作
                PurchaseFragment fragment = PurchaseFragment.newInstance(FinancialPositionCodeModeUtil.buildPurchaseInputModeItemParams(productDeta, xpadAccountEntity), null);
                start(fragment);
            } else {
                showErrorDialog("该账户非电子银行关联账户，请您更换账户或者先进行关联再购买。");
            }
        }
    }//=================================接口回调处理段落================start==================

    //=================================接口回调处理段落================end==================

    //=================================自定义方法段落================start==================

    /**
     * 为日积月累详情页设置数据
     *
     * @param banlanceModel
     * @param productDeta
     */
    public void setFixedTermDetailDeta(PsnXpadProductBalanceQueryResModel banlanceModel, PsnXpadProductDetailQueryResModel productDeta, String mConversationID, PsnXpadAccountQueryResModel.XPadAccountEntity xpadAccountEntity) {
        this.banlanceDeta = banlanceModel;
        this.productDeta = productDeta;
        this.mConversationID = mConversationID;
        this.xpadAccountEntity = xpadAccountEntity;
    }

    //分红方式选择Dialog
    private void showYearlyDialog() {
        List<String> yearTypeList = new ArrayList<String>();
        yearTypeList.add(getResourceString(R.string.boc_trans_financial_fixedterm_dispense_cash));
        yearTypeList.add(getResourceString(R.string.boc_trans_financial_fixedterm_cashagin));
        if (null == yearlyTypeDialog) {
            yearlyTypeDialog = new SelectStringListDialog(mContext);
            yearlyTypeDialog.setListData(yearTypeList, true);
            yearlyTypeDialog.isShowHeaderTitle(true);
            yearlyTypeDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                @Override
                public void onSelect(int position, String model) {
                    LogUtil.d("yx--------当前分红方式：-->" + mCurrentBonusMode);
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

    //得到收益详情信息描述
    private String getFixedTermInfo() {
        String info = getResourceString(R.string.boc_trans_financial_earnbuild_earnbuildinfo);
        String info1 = "";
        String info2 = "";
        String info3 = "";
        String info4 = "";
        info1 = info.replace("{0}", fixedrefit_info.getIntsdate() + "");
        info2 = info1.replace("{1}", MoneyUtils.transMoneyFormat(banlanceDeta.getHoldingQuantity() + "", "001"));
        info3 = info2.replace("{2}", MoneyUtils.transMoneyFormat(fixedrefit_info.getTotalprofit() + "", banlanceDeta.getCurCode()));
        info4 = info3.replace("{3}", MoneyUtils.transMoneyFormat(fixedrefit_info.getProfit() + "", banlanceDeta.getCurCode()));
        return info4;
    }

//=================================自定义方法段落================start==================

    /**
     * 获取分红方式
     */
    private String getCurrentBonusModeType(String modelType) {
        if ("0".equals(modelType)) {
            return getResourceString(R.string.boc_trans_financial_fixedterm_cashagin);
        } else if ("1".equals(modelType)) {
            return getResourceString(R.string.boc_trans_financial_fixedterm_dispense_cash);
        }
        return null;
    }

    /**
     * 获取资源String文件
     */
    private String getResourceString(int stringID) {
        return getResources().getString(stringID);
    }

    //=================================自定义方法段落================end==================
//    ===========================接口处理段落==========================

    /**
     * 参考收益汇总查询 成功
     *
     * @param mViewModel
     */
    @Override
    public void obtainPsnXpadReferProfitQuerySuccess(PsnXpadReferProfitQueryResModel mViewModel) {
        closeProgressDialog();
        if (mViewModel != null) {
            fixedrefit_info = mViewModel;
            //参考收益描述
            fixedterm_expProfit.setText(getFixedTermInfo());
            if ("027".equalsIgnoreCase(banlanceDeta.getCurCode())) {
                fixedtermcercle.setIsRiyuan(true);
            }
            fixedtermcercle.setShowText(Float.valueOf(mViewModel.getPayprofit()), Float.valueOf(mViewModel.getUnpayprofit()));

        }else{
            fixedterm_haveearn.setVisibility(View.GONE);
            fixedterm_noearn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 参考收益汇总查询 失败
     */
    @Override
    public void obtainPsnXpadReferProfitQueryFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        fixedterm_haveearn.setVisibility(View.GONE);
        fixedterm_noearn.setVisibility(View.VISIBLE);
        if (!"XPAD.A500".equalsIgnoreCase(biiResultErrorException.getErrorCode() + "")) {
            showErrorDialog(biiResultErrorException.getErrorMessage() + "");
        }
    }

    /**
     * 收益列表详情查询,后改为先跳转，后请求
     *
     * @param resModel
     */
    @Override
    public void obtainPsnXpadReferProfitDetailQuerySuccess(PsnXpadReferProfitDetailQueryResModel resModel) {
        //弹出收益详情列表
        closeProgressDialog();
    }

    /**
     * 收益列表详情查询失败
     */
    @Override
    public void obtainPsnXpadReferProfitDetailQueryFault() {

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
    protected FinancialTypeFixedTermDetailPresenter initPresenter() {
        return new FinancialTypeFixedTermDetailPresenter(this);
    }

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
//======================================接口返回处理段落======

    /**
     * 4.14 014修改分红方式交易 PsnXpadSetBonusMode 响应处理
     *
     * @param mViewModel
     */
    private void handlerPsnXpadSetBonusMode(psnXpadSetBonusModeResModel mViewModel) {
        if (mViewModel != null) {
            LogUtil.d("yx--------当前分红方式：-1->" + mCurrentBonusMode);
            //0：红利再投资、1：现金分红
            if ("0".equalsIgnoreCase(mCurrentBonusMode)) {
                mCurrentBonusMode = "1";
                //红利再投资为0
                mDetailTabRowTextButton.addTextAndValue(getResourceString(R.string.boc_trans_financial_position_shareouttype),
                        getResourceString(R.string.boc_trans_financial_fixedterm_dispense_cash), getResourceString(R.string.boc_qrpay_change) + "");
                LogUtil.d("yx--------当前分红方式：-2->" + mCurrentBonusMode);
            } else if ("1".equalsIgnoreCase(mCurrentBonusMode)) {
                mCurrentBonusMode = "0";
                mDetailTabRowTextButton.addTextAndValue(getResourceString(R.string.boc_trans_financial_position_shareouttype),
                        getResourceString(R.string.boc_trans_financial_fixedterm_cashagin), getResourceString(R.string.boc_qrpay_change) + "");
                LogUtil.d("yx--------当前分红方式：-3->" + mCurrentBonusMode);
            }
        }
    }
}
