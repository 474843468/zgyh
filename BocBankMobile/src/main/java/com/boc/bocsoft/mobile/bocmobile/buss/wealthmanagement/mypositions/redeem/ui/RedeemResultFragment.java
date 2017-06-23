package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.flowchart.BuyRedeemFlowView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.adapter.LikeGridAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPositionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadholdproductandredeem.PsnXpadHoldProductAndRedeemResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.util.RedeemCodeModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.TransInquireFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.ResultConvertUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.WealthPublicUtils;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.tencent.mm.sdk.openapi.SendMessageToWX;

import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yx
 * @description 中银理财-我的持仓-赎回-结果界面
 * @date 2016-9-7 14:30:07
 */
public class RedeemResultFragment extends MvpBussFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    // ====================view定义=================start=========
    /**
     * 页面根视图
     */
    private View mRootView;
    /**
     * 返回首页 按钮
     */
    private Button result_btn_back_home;
    /**
     * 公共结果界面
     */
    private BaseOperationResultView view_redeem_result;
    // ====================view定义=================end===========

    // ===================变量义=================start===========
    /**
     * I42-4.40 040产品详情查询PsnXpadProductDetailQuery
     */
    private PsnXpadProductDetailQueryResModel mPsnXpadProductDetailQueryResModel;
    /**
     * I42-4.13 013持有产品赎回 PsnXpadHoldProductAndRedeem
     */
    private PsnXpadHoldProductAndRedeemResModel mPsnXpadHoldProductAndRedeemResModel;
    /**
     * 是否是净值类型
     */
    private boolean isNetValue = false;
    /**
     * 是否立即赎回
     */
    private boolean isApplyNow = true;
    /**
     * 份额面值
     */
    private String mParValueTranches = "";
    /**
     * 理财推荐产品列表数据
     */
    private List<WealthListBean> wealthList = null;
    //赎回份额
    private String mRedeemQuantity = "";

    // ====================变量定义=================end===========

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_position_redeem_result, null);
        return mRootView;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    /**
     * 标题栏左侧图标点击事件
     */
    @Override
    protected void titleLeftIconClick() {
//        super.titleLeftIconClick();
        onBackKey();
//        popToAndReInit(WealthProductFragment.class);
    }

    /**
     * 标题栏右侧图标点击事件
     */
    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
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
     * 设置标题
     */
    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_loss_success_title);
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
        view_redeem_result = (BaseOperationResultView) mRootView.findViewById(R.id.view_redeem_result);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        view_redeem_result.setProcedureLayoutVisible(true, false);
        //view_redeem_result.setLikeLayoutVisible(true);
        view_redeem_result.updateHead(OperationResultHead.Status.SUCCESS, getStr(R.string.boc_position_redeem_redeem_submit));
//        view_redeem_result.setTitleTwo("根据您的选择，系统将于"+mPsnXpadHoldProductAndRedeemResModel.getTransDate()+"日发起赎回\n\n");
        view_redeem_result.setDetailsTitleIsShow(false);
        //产品名称
        view_redeem_result.addDetailRow(getStr(R.string.boc_position_redeem_product_name), mPsnXpadHoldProductAndRedeemResModel.getProdName() + "（" + mPsnXpadHoldProductAndRedeemResModel.getProdCode() + "）");

        // 系统时间
        LocalDateTime mLocalDateTime = ApplicationContext.getInstance().getCurrentSystemDate();
        String mSystem = mLocalDateTime.format(DateFormatters.dateFormatter1);
        LogUtil.d("yx-------系统日期---->" + mSystem);
        if (isNetValue) {//净值
            String mValue1 = "";
            String mValue2 = "";
            String mStrTitel[] = new String[]{"今日赎回", "资金到账"};
            if (isApplyNow) {
                mValue1 = "系统将于" + mPsnXpadHoldProductAndRedeemResModel.getTransDate() + "发起赎回";
            } else {
                mValue1 = "指定" + mPsnXpadHoldProductAndRedeemResModel.getTransDate() + "发起赎回";
            }
            // 资金到账规则
            mValue2 = RedeemCodeModelUtil.convertRedPaymentMode(mPsnXpadProductDetailQueryResModel.getRedPaymentMode() + "", mPsnXpadProductDetailQueryResModel.getDateModeType() + "",
                    mPsnXpadProductDetailQueryResModel.getRedPaymentDate() + "", mPsnXpadProductDetailQueryResModel.getIsLockPeriod() + "", mPsnXpadProductDetailQueryResModel.getPaymentDate(),
                    mPsnXpadProductDetailQueryResModel.getDatesPaymentOffset() + "", mPsnXpadHoldProductAndRedeemResModel.getTransDate() + "");
//            if ("0".equalsIgnoreCase(mPsnXpadProductDetailQueryResModel.getRedPaymentMode() + "")) {//实时返还
//                mValue2 = "实时返还";//
//            } else if ("1".equalsIgnoreCase(mPsnXpadProductDetailQueryResModel.getRedPaymentMode() + "")) {//T+N返还
//                LogUtil.d("yx-------净值型资金到帐日---->" + mPsnXpadProductDetailQueryResModel.getRedPaymentDate());
//                mValue2 = "预计" + DateUtils.getDateAfterDayCount(mPsnXpadHoldProductAndRedeemResModel.getTransDate(), strToInt(mPsnXpadProductDetailQueryResModel.getRedPaymentDate())) + "左右到账";//profitDate	收益返还T+N(天数)????
//            } else if ("2".equalsIgnoreCase(mPsnXpadProductDetailQueryResModel.getRedPaymentMode() + "")) {//期末返还
//                //paymentDate//
//                mValue2 = "期末返还";//
//            }
            String mStrValue[] = new String[]{mValue1, mValue2};
            view_redeem_result.setViewBuyRedeemVisible(true, mStrTitel, mStrValue, BuyRedeemFlowView.CompleteRedeemStatusT.ONE);
            // 赎回份额
            view_redeem_result.addDetailRow(getStr(R.string.boc_position_redeem_shares_redemption), MoneyUtils.transMoneyFormat(mPsnXpadHoldProductAndRedeemResModel.getTrfAmount() + "", mPsnXpadHoldProductAndRedeemResModel.getCurrencyCode()) + "");
        } else {//非净值
            String mStrTitel[] = new String[]{"今日赎回", "本金到账", "收益到账"};
            String mValue1 = "";
            String mValue2 = "";
            String mValue3 = "";
            if (isApplyNow) {
                mValue1 = "系统将于" + mPsnXpadHoldProductAndRedeemResModel.getTransDate() + "发起赎回";
            } else {
                mValue1 = "指定" + mPsnXpadHoldProductAndRedeemResModel.getTransDate() + "发起赎回";
            }
            //  本金到账规则
            mValue2 = RedeemCodeModelUtil.convertRedPaymentMode(mPsnXpadProductDetailQueryResModel.getRedPaymentMode() + "", mPsnXpadProductDetailQueryResModel.getDateModeType() + "",
                    mPsnXpadProductDetailQueryResModel.getRedPaymentDate() + "", mPsnXpadProductDetailQueryResModel.getIsLockPeriod() + "", mPsnXpadProductDetailQueryResModel.getPaymentDate(),
                    mPsnXpadProductDetailQueryResModel.getDatesPaymentOffset() + "", mPsnXpadHoldProductAndRedeemResModel.getTransDate() + "");
//            if ("0".equalsIgnoreCase(mPsnXpadProductDetailQueryResModel.getRedPaymentMode() + "")) {//实时返还
//                mValue2 = "实时返还";//
//            } else if ("1".equalsIgnoreCase(mPsnXpadProductDetailQueryResModel.getRedPaymentMode() + "")) {//T+N返还
//                LogUtil.d("yx-------非净值型本金到帐日---->" + mPsnXpadProductDetailQueryResModel.getRedPaymentDate());
//                mValue2 = "预计" + DateUtils.getDateAfterDayCount(mPsnXpadHoldProductAndRedeemResModel.getTransDate(), strToInt(mPsnXpadProductDetailQueryResModel.getRedPaymentDate())) + "左右到账";//profitDate	收益返还T+N(天数)????
//            } else if ("2".equalsIgnoreCase(mPsnXpadProductDetailQueryResModel.getRedPaymentMode() + "")) {//期末返还
//                mValue2 = "期末返还";//
//            }

            //  收益到账规则
            mValue3 = RedeemCodeModelUtil.convertProfitMode(mPsnXpadProductDetailQueryResModel.getProfitMode() + "", mPsnXpadProductDetailQueryResModel.getDateModeType() + "",
                    mPsnXpadProductDetailQueryResModel.getProfitDate() + "", mPsnXpadProductDetailQueryResModel.getIsLockPeriod() + "", mPsnXpadProductDetailQueryResModel.getRedPayDate(),
                    mPsnXpadProductDetailQueryResModel.getDatesPaymentOffset() + "", mPsnXpadHoldProductAndRedeemResModel.getTransDate() + "");
//            if ("1".equalsIgnoreCase(mPsnXpadProductDetailQueryResModel.getProfitMode() + "")) {//T+N返还
//                LogUtil.d("yx-------非净值型收益到帐日---->" + mPsnXpadProductDetailQueryResModel.getProfitDate());
//                mValue3 = "预计" + DateUtils.getDateAfterDayCount(mPsnXpadHoldProductAndRedeemResModel.getTransDate(), strToInt(mPsnXpadProductDetailQueryResModel.getProfitDate())) + "左右到账";//profitDate	收益返还T+N(天数)????
//            } else if ("2".equalsIgnoreCase(mPsnXpadProductDetailQueryResModel.getProfitMode() + "")) {//期末返还
//                mValue3 = "期末返还";//
//            }

            String mStrValue[] = new String[]{mValue1, mValue2, mValue3};
            view_redeem_result.setViewBuyRedeemVisible(true, mStrTitel, mStrValue, BuyRedeemFlowView.CompleteRedeemStatusT.ONE);
//            //份额面值
//            view_redeem_result.addDetailRow(getStr(R.string.boc_position_redeem_par_value_tranches), mParValueTranches);

            // 赎回份额
            view_redeem_result.addDetailRow(getStr(R.string.boc_position_redeem_shares_redemption), MoneyUtils.transMoneyFormat(mPsnXpadHoldProductAndRedeemResModel.getTrfAmount() + "", mPsnXpadHoldProductAndRedeemResModel.getCurrencyCode()) + "");
        }
        view_redeem_result.addDetailRow("交易日期",
                mPsnXpadHoldProductAndRedeemResModel.getPaymentDate() + "");
        if (!PublicUtils.isEmpty(wealthList)) {
            view_redeem_result.setYouLikeAdapter(new LikeGridAdapter(mContext, wealthList), this);
        }
//        view_redeem_result.setBodyBtnVisibility(View.VISIBLE);
//        view_redeem_result.setTitleNeedName("您可能需要");
//        view_redeem_result. isShowBottomInfo(true);
        view_redeem_result.addContentItem("分享产品", new RedeemResultListener(RedeemResultListener.ID_SHARE));
        view_redeem_result.addContentItem("我的持仓", new RedeemResultListener(RedeemResultListener.ID_PRODUCT));
        view_redeem_result.addContentItem("交易记录", new RedeemResultListener(RedeemResultListener.ID_RECORD));
    }

    /**
     * 初始化监听
     */
    @Override
    public void setListener() {
        view_redeem_result.setgoHomeOnclick(new OperationResultBottom.HomeBtnCallback() {
            @Override
            public void onHomeBack() {//返回首页
                ModuleActivityDispatcher.popToHomePage();
            }
        });
    }

    /**
     * 监听view处理方法
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * 猜你喜欢 点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        start(WealthDetailsFragment.newInstance(wealthList.get(position)));
    }

    //赎回-操作结果界面-点击事件
    class RedeemResultListener implements View.OnClickListener {

        public static final int ID_SHARE = 101;
        public static final int ID_PRODUCT = 102;
        public static final int ID_RECORD = 103;

        int id;

        public RedeemResultListener(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            if (id == ID_SHARE) {//分享产品
                if (mPsnXpadProductDetailQueryResModel == null) {
                    return;
                }
                String url = WealthConst.getShareProductUrl(mPsnXpadProductDetailQueryResModel.getProdCode(), mPsnXpadProductDetailQueryResModel.getProductKind());
                String title = mPsnXpadProductDetailQueryResModel.getProdName();
                String content = "";
                String date = "";
                date = ResultConvertUtils.convertDate(mPsnXpadProductDetailQueryResModel.getProductKind(), mPsnXpadProductDetailQueryResModel.getProdTimeLimit(),
                        mPsnXpadProductDetailQueryResModel.getIsLockPeriod(), mPsnXpadProductDetailQueryResModel.getProductTermType());
                if (WealthConst.PRODUCT_TYPE_2.equals(mPsnXpadProductDetailQueryResModel.getProductType())) {// 净值
                    String[] values = {MoneyUtils.getRoundNumber(mPsnXpadProductDetailQueryResModel.getPrice(), 4, BigDecimal.ROUND_HALF_UP),
                            date, mPsnXpadProductDetailQueryResModel.getSubAmount()};
                    content = WealthPublicUtils.buildShareStr("1", values, mPsnXpadProductDetailQueryResModel.getCurCode());
                } else {//非净值
                    String[] values = {ResultConvertUtils.convertRate(mPsnXpadProductDetailQueryResModel.getYearlyRR(), mPsnXpadProductDetailQueryResModel.getRateDetail()), date,
                            mPsnXpadProductDetailQueryResModel.getSubAmount()};
                    content = WealthPublicUtils.buildShareStr("0", values, mPsnXpadProductDetailQueryResModel.getCurCode());
                }
                SendMessageToWX.Req req = ShareUtils.shareWebPage(0, url, title, content);
                if (getApi() != null) {
                    getApi().sendReq(req);//跳转到朋友圈或会话列表
                }
            } else if (id == ID_PRODUCT) {//我的持仓
                popToAndReInit(FinancialPositionFragment.class);
//                startWithPop(FinancialPositionFragment.class);
            } else if (id == ID_RECORD) {//交易记录
                TransInquireFragment.newinstance(mActivity, 1);
            }
        }
    }
    //=================================接口回调处理段落================start==================
    //=================================接口回调处理段落================end==================

    //=================================自定义方法段落================start==================

    /**
     * I42-4.40 040 产品详情查询 PsnXpadProductDetailQuery和I42-4.13 013持有产品赎回 PsnXpadHoldProductAndRedeem model数据（上个界面传递过来）
     *
     * @param mPsnXpadProductDetailQueryResModel 产品详情查询model
     * @param mResModel                          持有产品赎回model
     * @param isApplyNow                         是否立即赎回
     * @param mParValueTranches                  份额面值
     */
    public void setData(String redeemQuantity, PsnXpadProductDetailQueryResModel mPsnXpadProductDetailQueryResModel, PsnXpadHoldProductAndRedeemResModel mResModel,
                        boolean isApplyNow, String mParValueTranches, boolean isNetValue) {
        this.mRedeemQuantity = redeemQuantity;
        this.mPsnXpadProductDetailQueryResModel = mPsnXpadProductDetailQueryResModel;
        this.mPsnXpadHoldProductAndRedeemResModel = mResModel;
        this.isApplyNow = isApplyNow;
        this.mParValueTranches = mParValueTranches;
        this.isNetValue = isNetValue;

    }

    /**
     * I42-4.40 040 产品详情查询 PsnXpadProductDetailQuery和I42-4.13 013持有产品赎回 PsnXpadHoldProductAndRedeem model数据（上个界面传递过来）
     *
     * @param mPsnXpadProductDetailQueryResModel 产品详情查询model
     * @param mResModel                          持有产品赎回model
     * @param isApplyNow                         是否立即赎回
     * @param mParValueTranches                  份额面值
     * @param wealthListBeen                     猜你喜欢数据
     */
    public void setData(String redeemQuantity, PsnXpadProductDetailQueryResModel mPsnXpadProductDetailQueryResModel, PsnXpadHoldProductAndRedeemResModel mResModel,
                        boolean isApplyNow, String mParValueTranches, boolean isNetValue, List<WealthListBean> wealthListBeen) {
        this.mRedeemQuantity = redeemQuantity;
        this.mPsnXpadProductDetailQueryResModel = mPsnXpadProductDetailQueryResModel;
        this.mPsnXpadHoldProductAndRedeemResModel = mResModel;
        this.isApplyNow = isApplyNow;
        this.mParValueTranches = mParValueTranches;
        this.isNetValue = isNetValue;
        this.wealthList = wealthListBeen;

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
        return getString(mResId);
    }

    @Override
    public boolean onBack() {
        onBackKey();
        return false;
    }

    /**
     * 返回处理
     */
    private void onBackKey() {
        WealthProductFragment mWealthProductFragment = findFragment(WealthProductFragment.class);
        if (mWealthProductFragment != null) {
            popToAndReInit(WealthProductFragment.class);
        } else {
            mWealthProductFragment = new WealthProductFragment();
            startWithPop(mWealthProductFragment);
            mActivity.finish();
        }
    }
    //=================================自定义公共方法段落================end==================

}
