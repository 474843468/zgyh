package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadShareTransitionCommit.PsnXpadShareTransitionCommitResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadShareTransitionVerify.PsnXpadShareTransitionVerifyResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.presenter.ShareConversionConfirmInfoPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.presenter.ShareConversionContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author zn
 * @date 2016/9/8
 * @description 中银理财-份额转换-确认信息界面
 */
public class ShareConversionInfoFragment extends MvpBussFragment<ShareConversionConfirmInfoPresenter>
        implements View.OnClickListener, ShareConversionContract.ShareConversionConfirmInfoView {

    // ====================view定义=================start=========
    /**
     * 页面根视图
     */
    private View mRootView;
    /**
     * 头部 -转换份额
     */
    private DetailTableHead share_confirm_head_view;
    /**
     * 显示内容（-产品名称，预计年华收益率）
     */
    private DetailContentView share_confirm_value_view;
    /**
     * 确认 按钮
     */
    private Button share_confirm_bt;
    // ====================view定义=================end===========
    // =================================接口code=================start=================
    // =================================接口code定义=================end=================
    // =================================变量义=================start=================
    private static final String TAG = "ShareConversionInfoFragment";
    /**
     * 产品详情查询
     */
    private PsnXpadProductBalanceQueryResModel banlanceDeta;
    /**
     * 4.70 070份额转换预交易  PsnXpadShareTransitionVerify
     */
    private PsnXpadShareTransitionVerifyResModel PsnXpadShareTransitionVerifyResModel;

    /**
     * ContentMoney
     *
     * @param mInflater
     * @return
     */
    private String mContentMoney;
    protected ConfirmInfoView confirmInfoView;
    private LinkedHashMap<String, String> datas = new LinkedHashMap<>();
    //预交易返回结果
    private PsnXpadShareTransitionVerifyResModel TransitionVerifyResModel;
    private PsnXpadQuantityDetailResModel.ListEntity mListInfo;
    //产品详情
    private PsnXpadProductDetailQueryResModel productDeta;
    /**
     * 会话id
     */
    private String mConversationId = "";
    //37接口数据 账户信息数据
    private PsnXpadAccountQueryResModel.XPadAccountEntity mItemXPadAccountEntity = null;
    //提交结果数据
    private PsnXpadShareTransitionCommitResModel mShareTransitionCommitResModel = null;

    // =================================变量定义=================end=================
    @Override
    protected ShareConversionConfirmInfoPresenter initPresenter() {
        return new ShareConversionConfirmInfoPresenter(this);
    }


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
//        confirmInfoView = new ConfirmInfoView(mContext);
        Bundle bundle = getArguments();
        mContentMoney = bundle.getString("ContentMoney");
        mConversationId = bundle.getString("ConversationId");
        banlanceDeta = (PsnXpadProductBalanceQueryResModel) bundle.getSerializable("BalanceDate");
        mListInfo = (PsnXpadQuantityDetailResModel.ListEntity) bundle.getSerializable("mListInfo");
        productDeta = (PsnXpadProductDetailQueryResModel) bundle.getSerializable("productDate");
        mItemXPadAccountEntity = (PsnXpadAccountQueryResModel.XPadAccountEntity) bundle.getSerializable("mItemXPadAccountEntity");
        TransitionVerifyResModel = (PsnXpadShareTransitionVerifyResModel) bundle.getSerializable("TransitionVerify");
//        return confirmInfoView;
        mRootView = mInflater.inflate(R.layout.boc_fragment_position_shareconversion_info, null);
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
        return getString(R.string.boc_position_redeem_confirm_title);
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

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    /**
     * 初始化view
     */
    @Override
    public void initView() {
        share_confirm_head_view = (DetailTableHead) mRootView.findViewById(R.id.share_confirm_head_view);
        share_confirm_value_view = (DetailContentView) mRootView.findViewById(R.id.share_confirm_net_value_view);
        share_confirm_bt = (Button) mRootView.findViewById(R.id.share_confirm_bt);

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
//        confirmInfoView.setHeadValue(getResources().getString(R.string.boc_trans_shareconversion_main_share),
//                MoneyUtils.transMoneyFormat(mContentMoney, mListInfo.getCurCode()), true);
//        for (int i = 0; i < collectionName().length; i++) {
//            if (StringUtils.isEmptyOrNull(collectionValue()[i])) {
//                continue;
//            } else {
//                datas.put(collectionName()[i], collectionValue()[i]);
//            }
//        }
//        confirmInfoView.addData(datas, false);
        share_confirm_head_view.updateData(getStr(R.string.boc_trans_shareconversion_main_share),
                MoneyUtils.transMoneyFormat(TransitionVerifyResModel.getTranUnit(), TransitionVerifyResModel.getProCur()));
        share_confirm_head_view.setDetailVisable(false);
        share_confirm_head_view.setLayoutDetailHeadNoMargin();
        //产品名称
        share_confirm_value_view.addDetailRowNotAllLine(getStr(R.string.boc_position_redeem_product_name),
                banlanceDeta.getProdName() + "（" + banlanceDeta.getProdCode() + "）");
        //预计年收益
        share_confirm_value_view.addDetailRow(getStr(R.string.boc_trans_financial_earnbuild_exyield), YearlyDate());
    }

    /**
     * 名称
     *
     * @return
     */
    private String[] collectionName() {
        String[] name = new String[2];
        name[0] = getResources().getString(R.string.boc_position_redeem_product_name);
        name[1] = getResources().getString(R.string.boc_trans_financial_earnbuild_exyield);
        return name;
    }

    /**
     * 数据
     *
     * @return
     */
    private String[] collectionValue() {
        String[] value = new String[2];
        value[0] = banlanceDeta.getProdName() + " (" + banlanceDeta.getProdCode() + ")";
        value[1] = YearlyDate();
        return value;
    }

    /**
     * 初始化监听
     */
    @Override
    public void setListener() {
//        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener() {
//            @Override
//            public void onClickConfirm() {
//                showLoadingDialog(false);
//                /**
//                 * @param token          防重机制，通过PSNGetTokenId接口获取
//                 * @param accountKey     帐号缓存标识
//                 * @param conversationId 会话id
//                 */
//                String token = "", accountKey = mListInfo.getBancAccountKey(), conversationId = mConversationId;
////                getPresenter().getPsnXpadShareTransitionCommit(SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId());
//                getPresenter().getPsnXpadShareTransitionCommit(conversationId,accountKey);
//            }
//
//            @Override
//            public void onClickChange() {
//                //显示安全认证选择对话框
////                SecurityVerity.getInstance().selectSecurityType();
//            }
//        });
        share_confirm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog(false);
                /**
                 * @param token          防重机制，通过PSNGetTokenId接口获取
                 * @param accountKey     帐号缓存标识
                 * @param conversationId 会话id
                 */
                String token = "", accountKey = mListInfo.getBancAccountKey(), conversationId = mConversationId;
//                getPresenter().getPsnXpadShareTransitionCommit(SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId());
                getPresenter().getPsnXpadShareTransitionCommit(conversationId, accountKey);
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
     * 4.71 071 份额转换  确认提交  PsnXpadShareTransitionCommit  成功调用
     */
    @Override
    public void obtainPsnXpadShareTransitionCommitResModel(PsnXpadShareTransitionCommitResModel model) {
        LogUtils.i(TAG, "份额转换  确认提交--成功");
        handlePsnXpadShareTransitionCommit(model);
    }

    /**
     * 4.71 071 份额转换  确认提交  PsnXpadShareTransitionCommit  失败调用
     */
    @Override
    public void obtainPsnXpadShareTransitionCommitFail() {
        closeProgressDialog();
        LogUtils.i(TAG, "份额转换  确认提交--失败");
    }

    /**
     * 猜你喜欢 成功回调
     *
     * @param wealthListBeen
     */
    @Override
    public void obtainQueryProductListSuccess(List<WealthListBean> wealthListBeen) {
        closeProgressDialog();
        ShareConversionResultFragment mShareconversionResultFragment = new ShareConversionResultFragment();
        mShareconversionResultFragment.setData(mShareTransitionCommitResModel, banlanceDeta, mListInfo, mContentMoney, productDeta, wealthListBeen);
//        start(mShareconversionResultFragment);
        startWithPop(mShareconversionResultFragment);

    }

    /***
     * 猜你喜欢 失败回调
     */
    @Override
    public void obtainQueryProductListFail() {
        closeProgressDialog();
        ShareConversionResultFragment mShareconversionResultFragment = new ShareConversionResultFragment();
        mShareconversionResultFragment.setData(mShareTransitionCommitResModel, banlanceDeta, mListInfo, mContentMoney, productDeta);
//        start(mShareconversionResultFragment);
        startWithPop(mShareconversionResultFragment);
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
    }
    //=================================接口回调处理段落================start==================

    /**
     * 4.71 071 份额转换  确认提交  PsnXpadShareTransitionCommit  响应结果model
     *
     * @param mResModel 响应结果model
     */
    private void handlePsnXpadShareTransitionCommit(PsnXpadShareTransitionCommitResModel mResModel) {
        if (mResModel != null) {
            mShareTransitionCommitResModel = mResModel;
//            ShareConversionResultFragment mShareconversionResultFragment = new ShareConversionResultFragment();
//            mShareconversionResultFragment.setData(mResModel, banlanceDeta,mListInfo, mContentMoney,productDeta);
//            start(mShareconversionResultFragment);
            if (mItemXPadAccountEntity == null || StringUtils.isEmptyOrNull(mItemXPadAccountEntity.getAccountId())) {
                closeProgressDialog();
                ShareConversionResultFragment mShareconversionResultFragment = new ShareConversionResultFragment();
                mShareconversionResultFragment.setData(mShareTransitionCommitResModel, banlanceDeta, mListInfo, mContentMoney, productDeta);
//                start(mShareconversionResultFragment);
                startWithPop(mShareconversionResultFragment);
            } else {
                getPresenter().updateRecentAccount(mItemXPadAccountEntity);
//                PortfolioPurchaseModel mPortfolioPurchaseModel = new PortfolioPurchaseModel();
//                mPortfolioPurchaseModel.setCurCode(banlanceDeta.getCurCode());
//                mPortfolioPurchaseModel.setProductKind(banlanceDeta.getProductKind());
//                mPortfolioPurchaseModel.setProductRisk(productDeta.getProdRisklvl());
//                mPortfolioPurchaseModel.setProdCode(mListInfo.getProdCode());
//                mPortfolioPurchaseModel.setAccountId(mItemXPadAccountEntity.getAccountId());
//                getPresenter().getQueryProductList(mPortfolioPurchaseModel);

                PortfolioPurchaseModel mPortfolioPurchaseModel = new PortfolioPurchaseModel();
                mPortfolioPurchaseModel.setCurCode(banlanceDeta.getCurCode());
                mPortfolioPurchaseModel.setProductKind(banlanceDeta.getProductKind());
                mPortfolioPurchaseModel.setProductRisk(productDeta.getProdRisklvl());
                mPortfolioPurchaseModel.setProdCode(banlanceDeta.getProdCode());
                mPortfolioPurchaseModel.setAccountId(mItemXPadAccountEntity.getAccountId());
                getPresenter().getQueryProductList(mPortfolioPurchaseModel);
            }

        }
    }
    //=================================接口回调处理段落================end==================

    //=================================自定义方法段落================start==================
    private String YearlyDate() {
        /**
         * 不带%号，如果不为0，与yearlyRR字段组成区间
         * yearlyRR  yearlyRRMax
         */
        if (StringUtils.isEmptyOrNull(mListInfo.getYearlyRRMax())) {
            return mListInfo.getYearlyRR() + "%";
        } else {
            if (0 == Float.valueOf(mListInfo.getYearlyRRMax())) {
                //预计年化收益率
                return mListInfo.getYearlyRR() + "%";
            } else {
                //预计年化收益率
                return mListInfo.getYearlyRR() + "~" + mListInfo.getYearlyRRMax() + "%";
            }
        }

    }
    //=================================自定义方法段落================end==================
    //=================================自定义公共方法段落================start==================

    /**
     * 通过资源id 查找 资源文件内容
     *
     * @param mResId 资源id
     * @return
     */
    private String getStr(int mResId) {
        return getContext().getResources().getString(mResId);
    }

    /**
     * @param mContentMoney
     * @param mConversationId
     * @param banlanceDeta
     * @param model
     * @param mListInfo
     */
    public void setShareConversionFragmentDeta(String mContentMoney,
                                               String mConversationId,
                                               PsnXpadProductBalanceQueryResModel banlanceDeta,
                                               PsnXpadShareTransitionVerifyResModel model,
                                               PsnXpadQuantityDetailResModel.ListEntity mListInfo) {
        this.mContentMoney = mContentMoney;
        this.mConversationId = mConversationId;
        this.banlanceDeta = banlanceDeta;
        this.TransitionVerifyResModel = model;
        this.mListInfo = mListInfo;
    }
    //=================================自定义公共方法段落================end==================


}
