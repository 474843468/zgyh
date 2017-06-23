package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.adapter.LikeGridAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPositionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadShareTransitionCommit.PsnXpadShareTransitionCommitResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.TransInquireFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.ResultConvertUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.WealthPublicUtils;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.tencent.mm.sdk.openapi.SendMessageToWX;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zn
 * @description 中银理财-份额转换-结果页面
 * @date 2016/9/8
 */
public class ShareConversionResultFragment extends BussFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    // =================================上下文对象定义=================start=================

    // =================================上下文对象定义=================end=================

    // =================================view定义=================start=================
    /**
     * 页面根视图
     */
    private View mRootView;

    /**
     * 返回首页
     */
    private Button result_btn_back_home;
    /**
     * 交易记录
     */
    private EditChoiceWidget result_trade_record;
    /**
     * 分享产品
     */
    private EditChoiceWidget result_share_product;
    /**
     * 我的持仓
     */
    private EditChoiceWidget result_my_postion;

    /**
     * 头部风格
     */
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    /**
     * 操作结果头部组件
     */
    private OperationResultHead shareconversion_result_header;
    // =================================view定义=================end=================

    // =================================接口code=================start=================

    // =================================接口code定义=================end=================

    // =================================变量义=================start=================
    private static final String TAG = "ShareConversionResultFragment";
    /**
     * 4.71 071 份额转换  确认提交  PsnXpadShareTransitionCommit
     */
    private PsnXpadShareTransitionCommitResModel CommitResModel;
    /**
     * 产品详情查询
     */
    private PsnXpadProductBalanceQueryResModel banlanceDeta;
    //    68份额明细查询
    private PsnXpadQuantityDetailResModel.ListEntity mListInfo;
    //转换份额
    private String mContentMoney;

    private BaseOperationResultView view_share_result;
    //产品详情
    private PsnXpadProductDetailQueryResModel productDeta;
    /**
     * 理财推荐产品列表数据
     */
    private List<WealthListBean> wealthList = null;
    // =================================变量定义=================end=================


    /**
     * 初始化View视图
     *
     * @param mInflater
     * @return mRootView
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_position_shareconversion_result, null);
        return mRootView;
    }

//    /**
//     * 标题栏左侧图标点击事件
//     */
//    @Override
//    protected void titleLeftIconClick() {
//        super.titleLeftIconClick();
//    }

    /**
     * 标题栏左侧图标点击事件
     */
    @Override
    protected void titleLeftIconClick() {
//        super.titleLeftIconClick();
        popToAndReInit(WealthProductFragment.class);
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
        return getString(R.string.boc_sure_result);
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
        view_share_result = (BaseOperationResultView) mRootView.findViewById(R.id.view_share_result);
    }


    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        view_share_result.setProcedureLayoutVisible(true, false);
        //view_share_result.setLikeLayoutVisible(true);
        view_share_result.updateHead(OperationResultHead.Status.SUCCESS, getStr(R.string.boc_trans_shareconversion_main_share_submit));
        view_share_result.setDetailsTitleIsShow(false);
        //交易序列号
        view_share_result.addDetailRow(getStr(R.string.boc_trans_shareconversion_serial_number), CommitResModel.getTransactionId());
        //产品名称
        view_share_result.addDetailRow(getStr(R.string.boc_position_redeem_product_name),
                banlanceDeta.getProdName() + "（" + banlanceDeta.getProdCode() + "）");
        view_share_result.addDetailRow(getStr(R.string.boc_trans_shareconversion_main_share),
                MoneyUtils.transMoneyFormat(CommitResModel.getTranUnit(), CommitResModel.getProCur()));
        view_share_result.setBodyBtnVisibility(View.VISIBLE);
        if (!PublicUtils.isEmpty(wealthList)) {
            view_share_result.setYouLikeAdapter(new LikeGridAdapter(mContext, wealthList), this);
        }

        view_share_result.setTitleNeedName(getStr(R.string.boc_trans_shareconversion_result_need));
        view_share_result.isShowBottomInfo(true);
        view_share_result.addContentItem(getStr(R.string.boc_trans_shareconversion_result_share_product),
                new RedeemResultListener(RedeemResultListener.ID_SHARE));
        view_share_result.addContentItem(getStr(R.string.boc_trans_shareconversion_result_my_position),
                new RedeemResultListener(RedeemResultListener.ID_PRODUCT));
        view_share_result.addContentItem(getStr(R.string.boc_trans_shareconversion_result_trade_record),
                new RedeemResultListener(RedeemResultListener.ID_RECORD));
    }


    /**
     * 初始化监听
     */
    @Override
    public void setListener() {
        view_share_result.setgoHomeOnclick(new OperationResultBottom.HomeBtnCallback() {
            @Override
            public void onHomeBack() {//返回首页
                ModuleActivityDispatcher.popToHomePage();
            }
        });
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
    //=================================接口回调处理段落================start==================
    //=================================接口回调处理段落================end==================

    //=================================自定义方法段落================start==================

    /**
     * @param mCommitResModel
     * @param banlanceDeta
     * @param mListInfo
     * @param mContentMoney
     */
    public void setData(PsnXpadShareTransitionCommitResModel mCommitResModel,
                        PsnXpadProductBalanceQueryResModel banlanceDeta,
                        PsnXpadQuantityDetailResModel.ListEntity mListInfo, String mContentMoney,
                        PsnXpadProductDetailQueryResModel productDeta) {
        this.CommitResModel = mCommitResModel;
        this.mListInfo = mListInfo;
        this.mContentMoney = mContentMoney;
        this.banlanceDeta = banlanceDeta;
        this.productDeta = productDeta;
    }

    /**
     * @param mCommitResModel
     * @param banlanceDeta
     * @param mListInfo
     * @param mContentMoney
     * @param productDeta
     * @param wealthListBeen
     */
    public void setData(PsnXpadShareTransitionCommitResModel mCommitResModel,
                        PsnXpadProductBalanceQueryResModel banlanceDeta,
                        PsnXpadQuantityDetailResModel.ListEntity mListInfo, String mContentMoney,
                        PsnXpadProductDetailQueryResModel productDeta, List<WealthListBean> wealthListBeen) {
        this.CommitResModel = mCommitResModel;
        this.mListInfo = mListInfo;
        this.mContentMoney = mContentMoney;
        this.banlanceDeta = banlanceDeta;
        this.productDeta = productDeta;
        this.wealthList = wealthListBeen;
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
                if (productDeta == null) {
                    return;
                }
                String url = WealthConst.getShareProductUrl(productDeta.getProdCode(), productDeta.getProductKind());
                String title = productDeta.getProdName();
                String content = "";
                String date = "";
                date = ResultConvertUtils.convertDate(productDeta.getProductKind(), productDeta.getProdTimeLimit(),
                        productDeta.getIsLockPeriod(), productDeta.getProductTermType());
                if (WealthConst.PRODUCT_TYPE_2.equals(productDeta.getProductType())) {// 净值
                    String[] values = {MoneyUtils.getRoundNumber(productDeta.getPrice(), 4, BigDecimal.ROUND_HALF_UP),
                            date, productDeta.getSubAmount()};
                    content = WealthPublicUtils.buildShareStr("1", values, productDeta.getCurCode());
                } else {
                    String[] values = {ResultConvertUtils.convertRate(productDeta.getYearlyRR(), productDeta.getRateDetail()), date,
                            productDeta.getSubAmount()};
                    content = WealthPublicUtils.buildShareStr("0", values, productDeta.getCurCode());
                }
                SendMessageToWX.Req req = ShareUtils.shareWebPage(0, url, title, content);
                if (getApi() != null) {
                    getApi().sendReq(req);//跳转到朋友圈或会话列表
                }
            } else if (id == ID_PRODUCT) {//我的持仓
                popToAndReInit(FinancialPositionFragment.class);
            } else if (id == ID_RECORD) {//交易记录
//                ToastUtils.show("功能待开发。。。");
                TransInquireFragment.newinstance(mActivity, 1);
            }
        }
    }

    /**
     * 监听view处理方法
     */
    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onBack() {
        popToAndReInit(WealthProductFragment.class);
        return false;
    }
    //=================================自定义方法段落================end==================

    //=================================自定义公共方法段落================start==================
    //=================================自定义公共方法段落================end==================

}

