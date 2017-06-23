package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.ui;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductandredeem.PsnXpadHoldProductAndRedeemResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductredeemverify.PsnXpadHoldProductRedeemVerifyResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.presenter.RedeemConfirmInfoPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.presenter.RedeemContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.List;

/**
 * @author yx
 * @description 中银理财-我的持仓-赎回-确认信息界面
 * @date 2016-9-7 14:29:51
 */
public class RedeemConfirmInfoFragment extends MvpBussFragment<RedeemConfirmInfoPresenter> implements View.OnClickListener, RedeemContract.RedeemConfirmInfoView {
    // ====================view定义=================start=========
    /**
     * 页面根视图
     */
    private View mRootView;
    /**
     * 头部 -赎回份额
     */
    private DetailTableHead redeem_confirm_head_view;
    /**
     * 净值 显示内容（-产品名称，赎回手续费，业绩报酬(浮动管理费)，赎回日期）
     */
    private DetailContentView redeem_confirm_net_value_view;
    /**
     * 非净值显示内容(产品名称，份额面值，赎回日期)
     */
    private DetailContentView redeem_confirm_no_net_value_view;
    /**
     * 确认 按钮
     */
    private Button redeem_confirm_bt;
    // ====================view定义=================end===========

    // ===================接口code===============start=============
    /**
     * I00-3.8 008 PSNGetTokenId获取token
     */
    private final static int RESULT_CODE_PSNGETTOKENID = 0xff01;
    /**
     * I42-4.13 013持有产品赎回PsnXpadHoldProductAndRedeem
     */
    private final static int RESULT_CODE_PSNXPADHOLDPRODUCTANDREDEEM = 0xff02;

    // ===================接口code定义=================end===========

    // ===================变量义=================start===========

    /**
     * 赎回类型{净值、非净值}
     */
    private enum RedeemInterfaceType {
        REDEEMTYPE_NETVALUE, REDEEMTYPE_NOTNETVALUE
    }

    /**
     * 赎回调用类型-临时变量-默认非净值 NETVALUE
     */
    private RedeemInterfaceType mRedeemInterfaceType = RedeemInterfaceType.REDEEMTYPE_NOTNETVALUE;
    /**
     * I42-4.40 040产品详情查询PsnXpadProductDetailQuery
     */
    private PsnXpadProductDetailQueryResModel mPsnXpadProductDetailQueryResModel;
    /**
     * I42-4.33 033持有产品赎回预交易PsnXpadHoldProductRedeemVerify
     */
    private PsnXpadHoldProductRedeemVerifyResModel mPsnXpadHoldProductRedeemVerifyResModel;
    /**
     * 查询客户持仓信息PsnXpadProductBalanceQuery（Xpad-38）
     */
    private PsnXpadProductBalanceQueryResModel mPsnXpadProductBalanceQueryItemModel;
    /**
     * 会话id
     */
    private String mConversationId = "";
    /**
     * 是否立即赎回
     */
    private boolean isApplyNow = true;
    /**
     * 赎回份额
     */
    private String mRedeemQuantity = "";

    /**
     * 份额面值
     */
    private String mParValueTranches = "";
    /**
     * 是否是净值类型
     */
    private boolean isNetValue = false;
    //37接口数据 账户详情
    private PsnXpadAccountQueryResModel.XPadAccountEntity xpadAccountEntity = null;
    //赎回结果model
    private PsnXpadHoldProductAndRedeemResModel mHoldProductAndRedeemResModel = null;
    // ====================变量定义=================end===========

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_position_redeem_confirm_info, null);
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
        return getStr(R.string.boc_position_redeem_confirm_title);
    }

    /**
     * 是否显示红头
     *
     * @return
     */
    @Override
    protected boolean getTitleBarRed() {
        return false;
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
        return false;
    }

    /**
     * 初始化view
     */
    @Override
    public void initView() {
        redeem_confirm_head_view = (DetailTableHead) mRootView.findViewById(R.id.redeem_confirm_head_view);
        redeem_confirm_net_value_view = (DetailContentView) mRootView.findViewById(R.id.redeem_confirm_net_value_view);
        redeem_confirm_no_net_value_view = (DetailContentView) mRootView.findViewById(R.id.redeem_confirm_no_net_value_view);
        redeem_confirm_bt = (Button) mRootView.findViewById(R.id.redeem_confirm_bt);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        //赎回份额
        redeem_confirm_head_view.updateData(getStr(R.string.boc_position_redeem_shares_redemption), MoneyUtils.transMoneyFormat(mPsnXpadHoldProductRedeemVerifyResModel.getRedeemQuantity(), mPsnXpadProductDetailQueryResModel.getCurCode()));
        redeem_confirm_head_view.setLayoutDetailHeadNoMargin();
        redeem_confirm_head_view.setDetailVisable(false);
//        if (mRedeemInterfaceType == RedeemInterfaceType.REDEEMTYPE_NETVALUE) {//净值
        if (isNetValue) {//净值
            String mContrfee = "业绩报酬\n(浮动管理费)";
            SpannableString spannableStringOne = new SpannableString(mContrfee);
            spannableStringOne.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.boc_text_color_gray)), 5, mContrfee.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            //产品名称
            redeem_confirm_net_value_view.addDetailRow(getStr(R.string.boc_position_redeem_product_name), mPsnXpadHoldProductRedeemVerifyResModel.getProdName() + "（" + mPsnXpadHoldProductRedeemVerifyResModel.getProdCode() + "）", true);
            //赎回手续费
            redeem_confirm_net_value_view.addDetailRow(getStr(R.string.boc_position_redeem_redeemfee), MoneyUtils.transMoneyFormat(mPsnXpadProductDetailQueryResModel.getRedeemFee(), mPsnXpadProductDetailQueryResModel.getCurCode()).replaceAll("\\|", "\n"), true);
            // 业绩报酬(浮动管理费)
//            redeem_confirm_net_value_view.addDetailRowNotLineBg(getStr(R.string.boc_position_redeem_contrfee) + "\n"
//                    + getStr(R.string.boc_position_redeem_float_management_fee), "实际年化收益率大于" + mPsnXpadProductDetailQueryResModel.getPfmcDrawStart() + "%时，超出部分收益按照" + mPsnXpadProductDetailQueryResModel.getPfmcDrawScale() + "%收取业绩报酬", R.color.boc_common_bg_color, 0);
            redeem_confirm_net_value_view.addDetailRowNotLineBgSpannable(spannableStringOne, "实际年化收益率大于" + mPsnXpadProductDetailQueryResModel.getPfmcDrawStart() + "%时，超出部分收益按照"
                    + mPsnXpadProductDetailQueryResModel.getPfmcDrawScale() + "%收取业绩报酬", R.color.boc_common_cell_color, 0);//?没赋值
            // 赎回日期
            if (isApplyNow) {//立即赎回
                redeem_confirm_net_value_view.addDetailRow("立即赎回日期",
                        mPsnXpadHoldProductRedeemVerifyResModel.getRedeemDate() + "（遇节假日顺延至下一工作日）", true);
            } else {//指定日期赎回
                redeem_confirm_net_value_view.addDetailRow("指定赎回日期",
                        mPsnXpadHoldProductRedeemVerifyResModel.getRedeemDate() + "（遇节假日顺延至下一工作日）", true);
            }
//            // 赎回日期
//            redeem_confirm_net_value_view.addDetailRow(getStr(R.string.boc_position_redeem_red_date),
//                    mPsnXpadHoldProductRedeemVerifyResModel.getRedeemDate());
//        } else if (mRedeemInterfaceType == RedeemInterfaceType.REDEEMTYPE_NOTNETVALUE) {//非净值
        } else {
            //产品名称
            redeem_confirm_net_value_view.addDetailRow(getStr(R.string.boc_position_redeem_product_name), mPsnXpadHoldProductRedeemVerifyResModel.getProdName() + "（" + mPsnXpadHoldProductRedeemVerifyResModel.getProdCode() + "）", true);
            //份额面值
            mParValueTranches = MoneyUtils.transMoneyFormat(mPsnXpadHoldProductRedeemVerifyResModel.getSellPrice(), mPsnXpadProductDetailQueryResModel.getCurCode()) + "";
            redeem_confirm_net_value_view.addDetailRow(getStr(R.string.boc_position_redeem_par_value_tranches), mParValueTranches, true);
//            //赎回日期
//            redeem_confirm_net_value_view.addDetailRow(getStr(R.string.boc_position_redeem_red_date),
//                    mPsnXpadHoldProductRedeemVerifyResModel.getRedeemDate());
            // 赎回日期
            if (isApplyNow) {//立即赎回
                redeem_confirm_net_value_view.addDetailRow("立即赎回日期",
                        mPsnXpadHoldProductRedeemVerifyResModel.getRedeemDate() + "（遇节假日顺延至下一工作日）", true);
            } else {//指定日期赎回
                redeem_confirm_net_value_view.addDetailRow("指定赎回日期",
                        mPsnXpadHoldProductRedeemVerifyResModel.getRedeemDate() + "（遇节假日顺延至下一工作日）", true);
            }
        }

    }

    /**
     * 初始化监听
     */
    @Override
    public void setListener() {
        redeem_confirm_bt.setOnClickListener(this);
    }

    /**
     * 监听view处理方法
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.redeem_confirm_bt) {//确认按钮
            showLoadingDialog(false);
            getPresenter().getPsnXpadHoldProductAndRedeem("", "", mConversationId);
//            getPresenter().getPsnXpadHoldProductAndRedeem("", mPsnXpadHoldProductRedeemVerifyResModel.getTranSeq(), mConversationId);
        }

    }

    @Override
    protected RedeemConfirmInfoPresenter initPresenter() {
        return new RedeemConfirmInfoPresenter(this);
    }

    /**
     * I42-4.13 013持有产品赎回 PsnXpadHoldProductAndRedeem 成功调用
     *
     * @param model
     */
    @Override
    public void obtainPsnXpadHoldProductAndRedeemSuccess(PsnXpadHoldProductAndRedeemResModel model) {
        handlePsnXpadHoldProductAndRedeem(model);
    }

    /**
     * I42-4.13 013持有产品赎回 PsnXpadHoldProductAndRedeem，失败调用
     */
    @Override
    public void obtainPsnXpadHoldProductAndRedeemFail() {
        closeProgressDialog();
    }

    /**
     * 猜你喜欢 成功回调
     *
     * @param wealthListBeen
     */
    @Override
    public void obtainQueryProductListSuccess(List<WealthListBean> wealthListBeen) {
        closeProgressDialog();
        RedeemResultFragment mRedeemResultFragment = new RedeemResultFragment();
        mRedeemResultFragment.setData(mRedeemQuantity, mPsnXpadProductDetailQueryResModel, mHoldProductAndRedeemResModel, isApplyNow, mParValueTranches, isNetValue, wealthListBeen);
//        start(mRedeemResultFragment);
        startWithPop(mRedeemResultFragment);

    }

    /***
     * 猜你喜欢 失败回调
     */
    @Override
    public void obtainQueryProductListFail() {
        closeProgressDialog();
        RedeemResultFragment mRedeemResultFragment = new RedeemResultFragment();
        mRedeemResultFragment.setData(mRedeemQuantity, mPsnXpadProductDetailQueryResModel, mHoldProductAndRedeemResModel, isApplyNow, mParValueTranches, isNetValue);
//        start(mRedeemResultFragment);
        startWithPop(mRedeemResultFragment);
    }
    //=================================接口回调处理段落================start==================

    /**
     * I42-4.13 013持有产品赎回 PsnXpadHoldProductAndRedeem 响应结果model
     *
     * @param mResModel 响应结果model
     */

    private void handlePsnXpadHoldProductAndRedeem(PsnXpadHoldProductAndRedeemResModel mResModel) {
        if (mResModel != null) {
            mHoldProductAndRedeemResModel = mResModel;

            if (xpadAccountEntity == null || StringUtils.isEmptyOrNull(xpadAccountEntity.getAccountId())) {
                closeProgressDialog();
                RedeemResultFragment mRedeemResultFragment = new RedeemResultFragment();
                mRedeemResultFragment.setData(mRedeemQuantity, mPsnXpadProductDetailQueryResModel, mHoldProductAndRedeemResModel, isApplyNow, mParValueTranches, isNetValue);
//                start(mRedeemResultFragment);
                startWithPop(mRedeemResultFragment);
            } else {
                getPresenter().updateRecentAccount(xpadAccountEntity);//更新账户状态
                PortfolioPurchaseModel mPortfolioPurchaseModel = new PortfolioPurchaseModel();
                mPortfolioPurchaseModel.setCurCode(mPsnXpadProductBalanceQueryItemModel.getCurCode());
                mPortfolioPurchaseModel.setProductKind(mPsnXpadProductBalanceQueryItemModel.getProductKind());
                mPortfolioPurchaseModel.setProductRisk(mPsnXpadProductDetailQueryResModel.getProdRisklvl());
                mPortfolioPurchaseModel.setProdCode(mPsnXpadProductBalanceQueryItemModel.getProdCode());
                mPortfolioPurchaseModel.setAccountId(xpadAccountEntity.getAccountId());
                getPresenter().getQueryProductList(mPortfolioPurchaseModel);
            }


        } else {
            closeProgressDialog();
        }
    }
    //=================================接口回调处理段落================end==================

    //=================================自定义方法段落================start==================

    /**
     * I42-4.40 040 产品详情查询 PsnXpadProductDetailQuery和I42-4.33 033持有产品赎回预交易PsnXpadHoldProductRedeemVerify model数据（上个界面传递过来）
     *
     * @param conversationId                          会话 id
     * @param mPsnXpadProductBalanceQueryItemModel    查询客户持仓信息model
     * @param mPsnXpadProductDetailQueryResModel      产品详情查询model
     * @param mPsnXpadHoldProductRedeemVerifyResModel 持有产品赎回预交易model
     * @param isApplyNow                              是否立即赎回
     * @param redeemQuantity                          赎回份额
     * @param isNetValue                              是否是净值类型
     * @param xpadAccountEntity                       37接口数据
     */
    public void setData(String conversationId, PsnXpadProductBalanceQueryResModel mPsnXpadProductBalanceQueryItemModel,
                        PsnXpadProductDetailQueryResModel mPsnXpadProductDetailQueryResModel,
                        PsnXpadHoldProductRedeemVerifyResModel mPsnXpadHoldProductRedeemVerifyResModel,
                        boolean isApplyNow, String redeemQuantity, boolean isNetValue, PsnXpadAccountQueryResModel.XPadAccountEntity xpadAccountEntity) {
        this.mConversationId = conversationId;
        this.mPsnXpadProductBalanceQueryItemModel = mPsnXpadProductBalanceQueryItemModel;
        this.mPsnXpadProductDetailQueryResModel = mPsnXpadProductDetailQueryResModel;
        this.mPsnXpadHoldProductRedeemVerifyResModel = mPsnXpadHoldProductRedeemVerifyResModel;
        this.isApplyNow = isApplyNow;
        this.mRedeemQuantity = redeemQuantity;
        this.isNetValue = isNetValue;
        this.xpadAccountEntity = xpadAccountEntity;
    }
    //=================================自定义方法段落================end==================


    //=================================自定义公共方法段落================start==================

    /**
     * String 转换成 int 类型
     *
     * @param number
     * @return
     */
    private int strToInt(String number) {
        if (!"".equals(number) && number != null) {
            return Integer.valueOf(number);
        }
        return 0;
    }

    /**
     * 通过资源id 查找 资源文件内容
     *
     * @param mResId 资源id
     * @return
     */
    private String getStr(int mResId) {
        return getContext().getResources().getString(mResId);
    }

    //=================================自定义公共方法段落================end==================

    @Override
    public void setPresenter(BasePresenter presenter) {

    }
}
