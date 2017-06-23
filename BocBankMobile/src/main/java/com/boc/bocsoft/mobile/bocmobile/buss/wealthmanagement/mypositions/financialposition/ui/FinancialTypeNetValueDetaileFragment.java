package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTabRowTextButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectCenterListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectCenterStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui.InvestTreatyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadSetBonusMode.psnXpadSetBonusModeResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadreferprofitquery.PsnXpadReferProfitQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialPositionContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialTypeNetValueDetailePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCodeModeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCommonUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.ui.RedeemFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.ui.ShareConversionFragment;
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
 * 中银理财--持仓详情-净值
 * Created by zn on 2016/9/20.
 */
public class FinancialTypeNetValueDetaileFragment extends MvpBussFragment<FinancialTypeNetValueDetailePresenter>
        implements View.OnClickListener,
        FinancialPositionContract.FinancialTypeNetValueView {

    // =================================view定义=================start=================
    /**
     * 页面根视图
     */
    private View mRootView;
    //    内容显示区域
    private ScrollView sl_content_view;
    /**
     * 截止2016/09/06，您持有的1000份产品的总盈亏估算为33,000.00元。
     */
    private TextView fragment_netvalue_income;
    /**
     * 持仓盈亏
     */
    private TextView fragment_netvalue_position_loss_value;
    /**
     * 以实现盈亏
     */
    private TextView fragment_netvalue_position_profit_value;
    /**
     * 净值说明
     */
    private TextView fragment_netvalue_explain;
    /**
     * 持有份额
     */
    private DetailTableHead fragment_netvalue_detailtabhead;
    /**
     * 产品名称，最新净值，净值日期，资金帐户，分红方式
     */
    private DetailContentView detailContent;
    //分红方式选择Dialog
    private SelectCenterStringListDialog yearlyTypeDialog;
    //暂无收益提示
    private TextView fragment_netvalueterm_noearn;
    //收益详细
    private LinearLayout fragment_netvalue_info;
    private LinearLayout fragment_netvalue_bottonbtn;
    //赎回
    private TextView fragment_netvalue_redeem;
    //继续购买
    private TextView fragment_netvalue_goonbuy;
    // =================================view定义=================end=================
    // =================================变量义=================start=================
    //客户持仓信息列表
//    private PsnXpadProductBalanceQueryResModel banlanceList;
    //客户持仓信息
    private PsnXpadProductBalanceQueryResModel banlanceDeta;
    //产品详情
    private PsnXpadProductDetailQueryResModel productDeta;
    //收益汇总 结果
    private PsnXpadReferProfitQueryResModel referProfitQueryData;
    //份额转换界面
    private ShareConversionFragment mShareConversionFragment = null;
    //赎回Fragment
    private RedeemFragment redeemFragment;
    private TextView fragment_netvalue_info_shouyi;
    //I42-4.37 037 查询客户理财账户信息 -单条记录
    private PsnXpadAccountQueryResModel.XPadAccountEntity mItemXPadAccountEntity = null;
    //分红方式 所在view
    private DetailTabRowTextButton mDetailTabRowTextButton = null;
    //当前分红方式
    private String mCurrentBonusMode = "";
    //会话id
    private String mConversationID = "";
    //是否需要列表界面刷新数据
    private boolean isReqFinancialHome = false;
    // =================================变量定义=================end=================

    // =================================接口code=================start=================

    // =================================接口code定义=================end=================
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_financial_netvalue_main, null);
        return mRootView;
    }

    /**
     * 初始化接口调用Presenter
     *
     * @return
     */
    @Override
    protected FinancialTypeNetValueDetailePresenter initPresenter() {
        return new FinancialTypeNetValueDetailePresenter(this);
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

        //内容显示
        sl_content_view = (ScrollView) mRootView.findViewById(R.id.sl_content_view);
        fragment_netvalue_detailtabhead = (DetailTableHead) mRootView.findViewById(R.id.fragment_netvalue_detailtabhead);
        detailContent = (DetailContentView) mRootView.findViewById(R.id.fragment_netvalue_detailcontent_view2);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) detailContent.getLayoutParams();
        lp.setMargins(0, -20, 0, 0);
        detailContent.setLayoutParams(lp);
        fragment_netvalue_income = (TextView) mRootView.findViewById(R.id.fragment_netvalue_income);
        fragment_netvalue_position_loss_value = (TextView) mRootView.findViewById(R.id.fragment_netvalue_position_loss_value);
        fragment_netvalue_position_profit_value = (TextView) mRootView.findViewById(R.id.fragment_netvalue_position_profit_value);
        fragment_netvalue_explain = (TextView) mRootView.findViewById(R.id.fragment_netvalue_explain);
        fragment_netvalueterm_noearn = (TextView) mRootView.findViewById(R.id.fragment_netvalueterm_noearn);
        fragment_netvalue_info = (LinearLayout) mRootView.findViewById(R.id.fragment_netvalue_info);
        fragment_netvalue_info_shouyi = (TextView) mRootView.findViewById(R.id.fragment_netvalue_info_shouyi);

        //底部按钮
        fragment_netvalue_bottonbtn = (LinearLayout) mRootView.findViewById(R.id.fragment_netvalue_bottonbtn);
        fragment_netvalue_redeem = (TextView) mRootView.findViewById(R.id.fragment_netvalue_redeem);
        fragment_netvalue_goonbuy = (TextView) mRootView.findViewById(R.id.fragment_netvalue_goonbuy);

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        /**
         * 添加内容
         */
        //头部-持有份额  内容显示
        fragment_netvalue_detailtabhead.updateData(getResourceString(R.string.boc_position_redeem_shares_held),
                MoneyUtils.transMoneyFormat(banlanceDeta.getHoldingQuantity(), "001"));
        //可用份额
        fragment_netvalue_detailtabhead.setTableRow(getResourceString(R.string.boc_trans_shareconversion_result_useableshare),
                MoneyUtils.transMoneyFormat(banlanceDeta.getAvailableQuantity(), "001"));
        //产品名称
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

        //最新净值
        detailContent.addTextCntent(
                getResourceString(R.string.boc_trans_financial_netvalue_main_new_netvalue),
                MoneyUtils.financialNetValueTransMoneyFormat(banlanceDeta.getPrice(), banlanceDeta.getCurCode()));
        if (StringUtils.isEmptyOrNull(banlanceDeta.getPriceDate())) { //净值日期
            detailContent.addTextCntent(getResourceString(R.string.boc_trans_financial_netvalue_main_new_netvalue_date), "");
        } else {
            //净值日期
            detailContent.addTextCntent(getResourceString(R.string.boc_trans_financial_netvalue_main_new_netvalue_date),
                    banlanceDeta.getPriceDate());
        }

        //资金账户
        detailContent.addDetail(getResourceString(R.string.boc_wealth_capital_account),
                NumberUtils.formatStringNumber(banlanceDeta.getBancAccount()), false);
        //  当前分红方式:--> 0：红利再投资、 1：现金分红
        if ("0".equalsIgnoreCase(banlanceDeta.getCurrentBonusMode())) {
            mCurrentBonusMode = "0";
        } else if ("1".equalsIgnoreCase(banlanceDeta.getCurrentBonusMode())) {
            mCurrentBonusMode = "1";
        }
        //分红方式 currentBonusMode
        /**canChangeBonusMode	是否可修改分红方式	String	0：是 1：否  */
        if ("0".equals(banlanceDeta.getCanChangeBonusMode())) {
            //更改分红方式 view
            mDetailTabRowTextButton = detailContent.addTextCntent(
                    getResourceString(R.string.boc_trans_financial_position_shareouttype),
                    getCurrentBonusModeType(banlanceDeta.getCurrentBonusMode()),
                    getResourceString(R.string.boc_details_update), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //更改点击事件
                            showYearlyDialog();
                        }
                    });
        } else {
            detailContent.addTextCntent(getResourceString(R.string.boc_trans_financial_position_shareouttype), getCurrentBonusModeType(banlanceDeta.getCurrentBonusMode()));
        }
        //判断是否可赎回
        if ("0".equals(banlanceDeta.getCanRedeem())) {
            fragment_netvalue_bottonbtn.setVisibility(View.VISIBLE);
            fragment_netvalue_redeem.setVisibility(View.VISIBLE);
        }
        //判断是否可继续购买
        if ("0".equals(banlanceDeta.getCanAddBuy())) {
            fragment_netvalue_bottonbtn.setVisibility(View.VISIBLE);
            fragment_netvalue_goonbuy.setVisibility(View.VISIBLE);
        }

        if (!"0".equals(banlanceDeta.getCanRedeem()) && !"0".equals(banlanceDeta.getCanAddBuy())) {
            fragment_netvalue_bottonbtn.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp2.setMargins(0, PublicUtils.dip2px(mContext, 10), 0, 0);
            sl_content_view.setLayoutParams(lp2);
        }
        //暂无收益
        if (!FinancialPositionCommonUtil.isUnequalToZero(banlanceDeta.getExpProfit())) {
            fragment_netvalue_info.setVisibility(View.GONE);
            fragment_netvalueterm_noearn.setVisibility(View.VISIBLE);
        } else {
            //请求收益汇总
            showLoadingDialog();
            getPresenter().getPsnXpadReferProfitQuery(banlanceDeta.getBancAccountKey(),
                    banlanceDeta.getProdCode(), banlanceDeta.getProductKind(),
                    banlanceDeta.getCashRemit(), banlanceDeta.getTranSeq());
        }

        //参考收益描述
        fragment_netvalue_info_shouyi.setText(getString(R.string.boc_trans_financial_position_main_reference) + getNetInfo());
    }

    /**
     * 初始化监听
     */
    @Override
    public void setListener() {
        fragment_netvalue_redeem.setOnClickListener(this);
        fragment_netvalue_goonbuy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //赎回
        if (v.getId() == R.id.fragment_netvalue_redeem) {
//            if (null == redeemFragment) {
            redeemFragment = new RedeemFragment();
//            }
            redeemFragment.setData(banlanceDeta, productDeta, mItemXPadAccountEntity);
            start(redeemFragment);

//            if (mShareConversionFragment == null) {
//                mShareConversionFragment = new ShareConversionFragment();
//                mShareConversionFragment.setShareConversionDeta(banlanceDeta, productDeta);
//                start(mShareConversionFragment);
//            }else{
//                mShareConversionFragment.setShareConversionDeta(banlanceDeta, productDeta);
//                start(mShareConversionFragment);
//            }

        }
        //继续购买
        if (v.getId() == R.id.fragment_netvalue_goonbuy) {
            if (mItemXPadAccountEntity != null) {
                //继续购买操作
                PurchaseFragment fragment = PurchaseFragment.newInstance(FinancialPositionCodeModeUtil.buildPurchaseInputModeItemParams(productDeta, mItemXPadAccountEntity), null);
                start(fragment);
            } else {
                showErrorDialog("该账户非电子银行关联账户，请您更换账户或者先进行关联再购买。");
            }
        }
    }

    // =================================接口调用段落=================start=================

    /**
     * PsnXpadReferProfitQuery  收益汇总查询  成功回调
     *
     * @param mViewModel
     */
    @Override
    public void obtainPsnXpadReferProfitQuerySuccess(PsnXpadReferProfitQueryResModel mViewModel) {
        handlePsnXpadReferProfitQuery(mViewModel);
        closeProgressDialog();
    }

    /**
     * 参考收益汇总查询 失败
     */
    @Override
    public void obtainPsnXpadReferProfitQueryFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        if (!"XPAD.A500".equalsIgnoreCase(biiResultErrorException.getErrorCode() + "")) {
            showErrorDialog(biiResultErrorException.getErrorMessage() + "");
        }
        fragment_netvalue_info.setVisibility(View.GONE);
        fragment_netvalueterm_noearn.setVisibility(View.VISIBLE);
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

    /**
     * @param presenter
     */
    @Override
    public void setPresenter(BasePresenter presenter) {
    }
    // =================================接口调用段落=================end=================
    // =================================接口响应处理段落=================start=================

    /**
     * PsnXpadReferProfitQuery  收益汇总查询  响应处理
     *
     * @param mViewModel
     */
    private void handlePsnXpadReferProfitQuery(PsnXpadReferProfitQueryResModel mViewModel) {
        this.referProfitQueryData = mViewModel;
        if (referProfitQueryData != null) {
            fragment_netvalue_income.setText(getFixedTermInfo());
            //持仓盈亏
            fragment_netvalue_position_loss_value.setText(MoneyUtils.transMoneyFormat(referProfitQueryData.getBalamt() + "", banlanceDeta.getCurCode()));
            //已实现盈亏
            fragment_netvalue_position_profit_value.setText(MoneyUtils.transMoneyFormat(referProfitQueryData.getAmt(), banlanceDeta.getCurCode()));
            fragment_netvalue_info.setVisibility(View.VISIBLE);
            fragment_netvalueterm_noearn.setVisibility(View.GONE);
        } else {
            fragment_netvalue_info.setVisibility(View.GONE);
            fragment_netvalueterm_noearn.setVisibility(View.VISIBLE);
        }
    }

    // =================================接口响应处理段落=================end=================
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
        return "";
    }

    //=================================自定义方法段落================end==================
//=================================自定义公共方法段落================start==================
    //得到收益详情信息描述
    private String getFixedTermInfo() {
        String info = getResourceString(R.string.boc_trans_financial_netvalue_main_textinfo);
        String info1 = "";
        String info2 = "";
        String info3 = "";
        info1 = info.replace("{1}", referProfitQueryData.getProfitdate() + "");
        info2 = info1.replace("{2}", MoneyUtils.transMoneyFormat(banlanceDeta.getHoldingQuantity() + "", "001"));
        info3 = info2.replace("{3}", MoneyUtils.transMoneyFormat(referProfitQueryData.getTotalamt() + "", banlanceDeta.getCurCode()));
        return info3;
    }

    //参考收益单位描述
    private String getNetInfo() {
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
     * 为净值详情页设置数据
     *
     * @param banlanceDeta
     */
    public void setNetValueDetaileDeta(PsnXpadProductBalanceQueryResModel banlanceDeta,
                                       PsnXpadProductDetailQueryResModel productDeta,
                                       String mConversationID,
                                       PsnXpadAccountQueryResModel.XPadAccountEntity mItemXPadAccountEntity) {
        this.banlanceDeta = banlanceDeta;
        this.productDeta = productDeta;
        this.mConversationID = mConversationID;
        this.mItemXPadAccountEntity = mItemXPadAccountEntity;
    }

    /**
     * 得到颜色资源文件
     *
     * @param stringCode
     * @return
     */
    private String getResourceString(int stringCode) {
        return mContext.getResources().getString(stringCode);
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

    //分红方式选择Dialog
    private void showYearlyDialog() {
        List<String> yearTypeList = new ArrayList<String>();
        ;
        yearTypeList.add(getResourceString(R.string.boc_trans_financial_fixedterm_dispense_cash));
        yearTypeList.add(getResourceString(R.string.boc_trans_financial_fixedterm_cashagin));
        if (null == yearlyTypeDialog) {
            yearlyTypeDialog = new SelectCenterStringListDialog(mContext);
            yearlyTypeDialog.setListData(yearTypeList, true);
            yearlyTypeDialog.isShowHeaderTitle(true);
            yearlyTypeDialog.setOnSelectListener(new SelectCenterListDialog.OnSelectListener<String>() {
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
    // =================================自定义公共方法段落=================end=================
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
                mDetailTabRowTextButton.addTextAndValue(getResourceString(R.string.boc_trans_financial_position_shareouttype),
                        getResourceString(R.string.boc_trans_financial_fixedterm_dispense_cash), getResourceString(R.string.boc_qrpay_change) + "");
            } else if ("1".equalsIgnoreCase(mCurrentBonusMode)) {
                mCurrentBonusMode = "0";
                mDetailTabRowTextButton.addTextAndValue(getResourceString(R.string.boc_trans_financial_position_shareouttype),
                        getResourceString(R.string.boc_trans_financial_fixedterm_cashagin), getResourceString(R.string.boc_qrpay_change) + "");
            }
        }
    }
}
