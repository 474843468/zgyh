package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailParams;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTabRowTextButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectCenterListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectCenterStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui.InvestTreatyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.adapter.FinancialTypeOutStandDetaileListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnXpadSetBonusMode.psnXpadSetBonusModeResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadreferprofitquery.PsnXpadReferProfitQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialPositionContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialTypeOutStandDetailePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCodeModeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCommonUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.ui.RedeemFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.ui.ShareConversionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

/**
 * 中银理财--持仓详情--业绩基准 （列表界面）
 * Created by zn on 2016/9/18.
 */
public class FinancialTypeOutStandDetaileFragment extends MvpBussFragment<FinancialTypeOutStandDetailePresenter>
        implements View.OnClickListener, FinancialPositionContract.FinancialTypeOutStandView {
    private static final String TAG = "FinancialTypeOutStandDetaileFragment";

    // =================================view定义=================start=================
    /**
     * 页面根视图
     */
    private View mRootView;
    /**
     * 业绩基准-持有份额
     */
    private DetailTableHead outstand_fragment_detailtabhead;

    /**
     * （有详情）显示内容（-产品名称，预期年化收益率，资金帐户，分红方式，参考收益）
     * （无详情）显示内容（-产品名称，业绩基准，资金帐户，）
     */
    private DetailContentView detailContent;
    /**
     * 详情列表 listview
     */
    private ListView outstand_fragment_list;
    //无份额明细提示
    private TextView fragment_outstand_noearn;
    /**
     * 点击按钮 --继续购买
     */
    private TextView outstand_goonbuy;
    /**
     * 点击按钮 --赎回
     */
    private TextView outstand_redeem;
    //分红方式选择Dialog
    private SelectCenterStringListDialog yearlyTypeDialog;
    // =================================view定义=================end=================
    // ===================接口code定义===============start=============
    /**
     * I42-4.36 036查询客户持仓信息 PsnXpadProductBalanceQuery
     */
    private final static int RESULT_CODE_PSNXPADPRODUCTBALANCEQUERY = 0xff03;
    // ===================接口code定义=================end===========
    // ===================变量定义=================start===========
    //产品详情
    private PsnXpadProductDetailQueryResModel productDeta;
    //客户持仓信息
    private PsnXpadProductBalanceQueryResModel banlanceDeta;
    //份额详细列表adapter
    private FinancialTypeOutStandDetaileListAdapter outStandAdapter;
    //收益汇总查询
    private PsnXpadReferProfitQueryResModel referProfitQueryDate;
    //份额明细列表成功结果
    private PsnXpadQuantityDetailResModel profitdetailList;
    //份额转换界面
    private ShareConversionFragment mShareConversionFragment = null;
    //赎回Fragment
    private RedeemFragment redeemFragment;
    //    //接口回调处理
//    private FinancialTypeOutStandDetailePresenter mOutStandPresenter;
    //份额详细列表
    private List<PsnXpadQuantityDetailResModel.ListEntity> modelList = new ArrayList<PsnXpadQuantityDetailResModel.ListEntity>();
    //收益汇总图表显示页面
    private FinancialTypeOutStandDetaileQueryFragment mFinancialTypeOutStandDetaileQueryFragment = null;
    //item详细
    private PsnXpadQuantityDetailResModel.ListEntity mListInfo;
    private LinearLayout outstand_bottonbtn;
    //会话ID
    private String mConversationID;
    //I42-4.37 037 查询客户理财账户信息 -单条记录
    private PsnXpadAccountQueryResModel.XPadAccountEntity mItemXPadAccountEntity = null;
    //分红方式 所在view
    private DetailTabRowTextButton mDetailTabRowTextButton = null;
    //业绩基准的预期年华收益率
    private FinancialPsnXpadExpectYieldQueryFragment yieldQueryFragment;
    //当前分红方式
    private String mCurrentBonusMode = "";
    //资金账户 需要从列表获取数据
//    private DetailTableRow mBancAccountDetailView = null;
    //是否需要列表界面刷新数据
    private boolean isReqFinancialHome = false;
    //37所有数据
    private PsnXpadAccountQueryResModel mPsnXpadAccountQueryResModel = null;

    // ====================变量定义=================end===========

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_financial_outstand_main, null);
        return mRootView;
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
        outstand_fragment_list = (ListView) mRootView.findViewById(R.id.outstand_fragment_list);
        View headerView = View.inflate(mContext, R.layout.boc_fragment_financial_outstand_main_addheader, null);
        outstand_fragment_detailtabhead = (DetailTableHead) headerView.findViewById(R.id.outstand_fragment_detailtabhead);
        detailContent = (DetailContentView) headerView.findViewById(R.id.outstand_fragment_detailcontent_view2);
        fragment_outstand_noearn = (TextView) headerView.findViewById(R.id.fragment_outstand_noearn);
        outstand_fragment_list.addHeaderView(headerView);

        //赎回
        outstand_redeem = (TextView) mRootView.findViewById(R.id.outstand_redeem);
        //继续购买
        outstand_goonbuy = (TextView) mRootView.findViewById(R.id.outstand_goonbuy);
        outstand_redeem.setOnClickListener(this);
        outstand_goonbuy.setOnClickListener(this);
        //底部按钮
        outstand_bottonbtn = (LinearLayout) mRootView.findViewById(R.id.outstand_bottonbtn);

        /**
         * 添加详情
         */
        //头部-持有份额  内容显示
        outstand_fragment_detailtabhead.updateData(getResourceString(R.string.boc_position_redeem_shares_held),
                MoneyUtils.transMoneyFormat(banlanceDeta.getHoldingQuantity(), "001"));
        //可用份额
        outstand_fragment_detailtabhead.setTableRow(getResourceString(R.string.boc_trans_shareconversion_result_useableshare),
                MoneyUtils.transMoneyFormat(banlanceDeta.getAvailableQuantity(), "001"));
        //份额面值
        outstand_fragment_detailtabhead.setTableRowTwo(getResourceString(R.string.boc_position_redeem_par_value_tranches),
                MoneyUtils.transMoneyFormat(banlanceDeta.getSellPrice(), banlanceDeta.getCurCode()));
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

        /**
         * 不带%号，如果不为0，与yearlyRR字段组成区间
         * yearlyRR  yearlyRRMax
         */
        //预计年化收益率
        if (!FinancialPositionCommonUtil.isShowMax(banlanceDeta.getYearlyRRMax())) {
            //预计年化收益率
            detailContent.addTextCntent(
                    getResourceString(R.string.boc_invest_treaty_rate_detail),
                    banlanceDeta.getYearlyRR() + "%",
                    getResourceString(R.string.boc_finance_account_transfer_detail_title),
                    new RateOnClickListener());
        } else {
            //预计年化收益率
            detailContent.addTextCntent(
                    getResourceString(R.string.boc_invest_treaty_rate_detail),
                    banlanceDeta.getYearlyRR() + "~" + banlanceDeta.getYearlyRRMax() + "%",
                    getResourceString(R.string.boc_finance_account_transfer_detail_title),
                    new RateOnClickListener());

        }
//        //资金账户
//         mBancAccountDetailView =detailContent.addTableRowDetail(getResourceString(R.string.boc_wealth_capital_account),
//                NumberUtils.formatStringNumber(banlanceDeta.getBancAccount()), false);
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
        //参考收益
        detailContent.addTextCntent(getResourceString(R.string.boc_trans_financial_position_main_reference_nounit),
                banlanceDeta.getExpProfit()+"");
        LogUtils.i("----产品名称------->getProdName+ getProdCode= " + banlanceDeta.getProdName() + "," +
                banlanceDeta.getProdCode());
        LogUtils.i("----预计年化收益率------->banlanceDeta.getYearlyRR() = " + banlanceDeta.getYearlyRR());
        LogUtils.i("----资金账户------->banlanceDeta.getBancAccount() = " + banlanceDeta.getBancAccount());
        LogUtils.i("------参考收益----->banlanceDeta.getExpProfit() = " + banlanceDeta.getExpProfit());
        LogUtils.i("------分红方式----->banlanceDeta.getCanChangeBonusMode() = " + banlanceDeta.getCanChangeBonusMode());

        //判断是否可赎回
        if ("0".equals(banlanceDeta.getCanRedeem())) {
            outstand_bottonbtn.setVisibility(View.VISIBLE);
            outstand_redeem.setVisibility(View.VISIBLE);
        }
        //判断是否可继续购买
        if ("0".equals(banlanceDeta.getCanAddBuy())) {
            outstand_bottonbtn.setVisibility(View.VISIBLE);
            outstand_goonbuy.setVisibility(View.VISIBLE);
        }
        if (!"0".equals(banlanceDeta.getCanRedeem()) && !"0".equals(banlanceDeta.getCanQuantityExchange())) {
            outstand_bottonbtn.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0, 0, 0, 0);
            outstand_fragment_list.setLayoutParams(lp);
        }
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        outStandAdapter = new FinancialTypeOutStandDetaileListAdapter(mContext);
        showLoadingDialog();
        //份额明细查询
        getPsnPsnXpadQuantityDetail();
    }

    /**
     * 初始化监听
     */
    @Override
    public void setListener() {
        //份额明细点击处理
        outstand_fragment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * PsnXpadReferProfitQuery  收益汇总查询
                 * @param accountKey 账号缓存标识(必选)
                 * @param productCode 产品代码(必选)
                 * @param kind 产品性质(必选)
                 * @param charCode 钞汇标识(必输01：钞;02：汇;00：人民币)
                 * @param tranSeq 份额流水号(业绩基准产品必输（PsnXpadProductBalanceQuery返回项standPro不为0的产品），
                 *                取自PsnXpadQuantityDetail接口返回项tranSeq)
                 */
                if (position - 1 >= 0) {
                    showLoadingDialog();
                    mListInfo = modelList.get(position - 1);
                    if (mPsnXpadAccountQueryResModel != null) {
                        mItemXPadAccountEntity = FinancialPositionCodeModeUtil.buildXPadAccountEntity(mListInfo, mPsnXpadAccountQueryResModel);
                    } else {
                        mItemXPadAccountEntity = null;
                    }
                    getPresenter().getPsnXpadReferProfitQuery(mListInfo.getBancAccountKey(),
                            banlanceDeta.getProdCode(), banlanceDeta.getProductKind(),
                            mListInfo.getCashRemit(), mListInfo.getTranSeq());
                }
            }
        });
    }

    /**
     * 初始化接口调用Presenter
     *
     * @return
     */
    @Override
    protected FinancialTypeOutStandDetailePresenter initPresenter() {
        return new FinancialTypeOutStandDetailePresenter(this);
    }

    /**
     * 监听view处理方法
     */
    @Override
    public void onClick(View v) {
        //赎回
        if (v.getId() == R.id.outstand_redeem) {
            if (null == redeemFragment) {
                redeemFragment = new RedeemFragment();
            }
            redeemFragment.setData(banlanceDeta, productDeta, mItemXPadAccountEntity);
            start(redeemFragment);
        }
        //继续购买
        if (v.getId() == R.id.outstand_goonbuy) {
            if (mItemXPadAccountEntity != null) {
                //继续购买操作
                PurchaseFragment fragment = PurchaseFragment.newInstance(FinancialPositionCodeModeUtil.buildPurchaseInputModeItemParams(productDeta, mItemXPadAccountEntity), null);
                start(fragment);
            } else {
                showErrorDialog("该账户非电子银行关联账户，请您更换账户或者先进行关联再购买。");
            }
        }
    }

    //（有明细）产品名称--详情点击事件
    private class ProdNameOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ToastUtils.show("点击查看详情");
        }
    }

    //（有明细）预计年化收益率--详情点击事件
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

    // =================================接口调用段落=================start=================

    /**
     * 4.68 068份额明细查询  PsnXpadQuantityDetail  成功回调
     *
     * @param resModel
     */
    @Override
    public void obtainPsnXpadQuantityDetailSuccess(PsnXpadQuantityDetailResModel resModel) {
        LogUtils.i(TAG, "-------->份额明细查询--成功！");
        handlePsnXpadQuantityDetailQuery(resModel);
        closeProgressDialog();
    }

    /**
     * 4.68 068份额明细查询  PsnXpadQuantityDetail  失败回调
     */
    @Override
    public void obtainPsnXpadQuantityDetailFail() {
        LogUtils.i(TAG, "-------->份额明细查询--失败！");
        fragment_outstand_noearn.setVisibility(View.VISIBLE);
        closeProgressDialog();
    }

    /**
     * PsnXpadReferProfitQuery  收益汇总查询  成功回调
     *
     * @param mViewModel
     */
    @Override
    public void obtainPsnXpadReferProfitQuerySuccess(PsnXpadReferProfitQueryResModel mViewModel) {
        LogUtils.i(TAG, "-------->收益汇总查询--成功！");
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
        } else {
//            if (mFinancialTypeOutStandDetaileQueryFragment == null) {
            mFinancialTypeOutStandDetaileQueryFragment = new FinancialTypeOutStandDetaileQueryFragment();
//            }
            banlanceDeta.setBancAccount(mListInfo.getBancAccount());
            banlanceDeta.setHoldingQuantity(mListInfo.getHoldingQuantity());
            banlanceDeta.setAvailableQuantity((mListInfo.getAvailableQuantity()));
            banlanceDeta.setCanPartlyRedeem(mListInfo.getCanPartlyRedeem());
            banlanceDeta.setLowestHoldQuantity(mListInfo.getLowestHoldQuantity());
            banlanceDeta.setRedeemStartingAmount(mListInfo.getRedeemStartingAmount());
            banlanceDeta.setBancAccountKey(mListInfo.getBancAccountKey());
            banlanceDeta.setTranSeq(mListInfo.getTranSeq());
            banlanceDeta.setCashRemit(mListInfo.getCashRemit());
            banlanceDeta.setProdEnd(mListInfo.getProdEnd());
            banlanceDeta.setCurCode(mListInfo.getCurCode());
            banlanceDeta.setXpadAccount(mListInfo.getXpadAccount());
            banlanceDeta.setCurrPeriod(mListInfo.getCurrPeriod());
            banlanceDeta.setTotalPeriod(mListInfo.getTotalPeriod());
            banlanceDeta.setProdBegin(mListInfo.getProdBegin());
            banlanceDeta.setCanQuantityExchange(mListInfo.getCanQuantityExchange());
            LogUtils.d("yx------列表---赎回--->" + banlanceDeta.getCanRedeem());
            LogUtils.d("yx------列表---份额转换--->" + banlanceDeta.getCanQuantityExchange());
            mFinancialTypeOutStandDetaileQueryFragment.setDetaileQueryDeta(
                    null, banlanceDeta, productDeta, mListInfo, mConversationID, mItemXPadAccountEntity);
            start(mFinancialTypeOutStandDetaileQueryFragment);
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

    /**
     * @param presenter
     */
    @Override
    public void setPresenter(BasePresenter presenter) {

    }
    // =================================接口调用段落=================end=================

    // =================================接口响应处理段落=================start=================

    /**
     * 份额明细查询  响应处理
     *
     * @param resModel
     */
    private void handlePsnXpadQuantityDetailQuery(PsnXpadQuantityDetailResModel resModel) {
        this.profitdetailList = resModel;
        if (profitdetailList != null) {
            LogUtils.i(profitdetailList.toString());
            LogUtils.i("size =" + profitdetailList.getList().size());
            if (profitdetailList.getList().size() >= 1) {
                //显示列表
                modelList = profitdetailList.getList();
//                mBancAccountDetailView.updateData(getResourceString(R.string.boc_wealth_capital_account),
//                        NumberUtils.formatStringNumber(modelList.get(0).getBancAccount()));
                outStandAdapter.setDatas(modelList);
                outstand_fragment_list.setAdapter(outStandAdapter);
                fragment_outstand_noearn.setVisibility(View.GONE);
            } else {
                fragment_outstand_noearn.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * PsnXpadReferProfitQuery  收益汇总查询   响应处理
     *
     * @param mViewModel
     */
    private void handlePsnXpadReferProfitQuery(PsnXpadReferProfitQueryResModel mViewModel) {
        this.referProfitQueryDate = mViewModel;
        if (referProfitQueryDate != null) {
//            if (mFinancialTypeOutStandDetaileQueryFragment == null) {
            mFinancialTypeOutStandDetaileQueryFragment = new FinancialTypeOutStandDetaileQueryFragment();
//            }
            banlanceDeta.setBancAccount(mListInfo.getBancAccount());
            banlanceDeta.setHoldingQuantity(mListInfo.getHoldingQuantity());
            banlanceDeta.setAvailableQuantity((mListInfo.getAvailableQuantity()));
            banlanceDeta.setCanPartlyRedeem(mListInfo.getCanPartlyRedeem());
            banlanceDeta.setLowestHoldQuantity(mListInfo.getLowestHoldQuantity());
            banlanceDeta.setRedeemStartingAmount(mListInfo.getRedeemStartingAmount());
            banlanceDeta.setBancAccountKey(mListInfo.getBancAccountKey());
            banlanceDeta.setTranSeq(mListInfo.getTranSeq());
            banlanceDeta.setCashRemit(mListInfo.getCashRemit());
            banlanceDeta.setProdEnd(mListInfo.getProdEnd());
            banlanceDeta.setCurCode(mListInfo.getCurCode());
            banlanceDeta.setXpadAccount(mListInfo.getXpadAccount());
            banlanceDeta.setCurrPeriod(mListInfo.getCurrPeriod());
            banlanceDeta.setTotalPeriod(mListInfo.getTotalPeriod());
            banlanceDeta.setProdBegin(mListInfo.getProdBegin());
            banlanceDeta.setCanQuantityExchange(mListInfo.getCanQuantityExchange());
            LogUtils.d("yx------列表---赎回--->" + banlanceDeta.getCanRedeem());
            LogUtils.d("yx------列表---份额转换--->" + banlanceDeta.getCanQuantityExchange());
            mFinancialTypeOutStandDetaileQueryFragment.setDetaileQueryDeta(
                    referProfitQueryDate, banlanceDeta, productDeta, mListInfo, mConversationID, mItemXPadAccountEntity);
            start(mFinancialTypeOutStandDetaileQueryFragment);
        }
    }

    // =================================接口响应处理段落=================end=================
    // =================================自定义dialog=================start=================
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
    // =================================自定义dialog=================start=================
    // =================================公共方法处理段=================start=================


    /**
     * 份额明细查询 请求
     */
    private void getPsnPsnXpadQuantityDetail() {
        PsnXpadQuantityDetailParams detailParams = new PsnXpadQuantityDetailParams();
        detailParams.setCharCode(String.valueOf(banlanceDeta.getCashRemit()));
        detailParams.setProductCode(String.valueOf(banlanceDeta.getProdCode()));
        getPresenter().getPsnXpadQuantityDetail(detailParams);
    }

    /**
     * 为业绩基准详情页设置数据
     *
     * @param banlanceDeta
     */
    public void setOutStandDetailDeta(PsnXpadProductBalanceQueryResModel banlanceDeta,
                                      PsnXpadProductDetailQueryResModel productDeta,
                                      String mConversationID,
                                      PsnXpadAccountQueryResModel.XPadAccountEntity mItemXPadAccountEntity,
                                      PsnXpadAccountQueryResModel mPsnXpadAccountQueryResModel) {
        this.banlanceDeta = banlanceDeta;
        this.productDeta = productDeta;
        this.mConversationID = mConversationID;
        this.mItemXPadAccountEntity = mItemXPadAccountEntity;
        this.mPsnXpadAccountQueryResModel = mPsnXpadAccountQueryResModel;
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
    // =================================公共方法处理段=================end=================
    //=================================自定义方法段落================start==================

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
    //=================================自定义方法段落================end==================
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
