package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadaccountquery.PsnXpadAccountQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCodeModeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery.PsnXpadProductDetailQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.ui.RedeemFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.ItemOnClickListener;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.adapter.RecommendAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.model.ProductModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.model.RecommendModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.presenter.RecommendPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignInitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui.ProtocolPeriodContinueFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui.PurchaseFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 理财——产品推荐界面
 * Created by Wan mengxin on 2016/9/21.
 */
public class RecommendFragment extends MvpBussFragment<RecommendPresenter> implements CommendContract.CommView {

    private View recommendView;
    public RecommendModel model;
    private LinearLayout noResult;
    private RecommendAdapter adapter;
    private RecommendModel.OcrmDetail detail;
    private String currentCode;
    private String currentDealCode;

    //详情的ListView
    PullableListView paymentRecordListView;
    //详情的上拉刷新
    PullToRefreshLayout paymentRecordRefresh;

    private boolean isRefresh = false;

    //当前加载页码
    private int pageCurrentIndex;
    //每页大小
    private static int pageSize;
    private String _refresh = "false";
    private String mark;//页面跳转识别标签
    private String transCount;//交易份额
    private String transSum;//交易金额
    public String maxPeriods;//最大购买期数
    private List<RecommendModel.OcrmDetail> adapterList;
    public PsnXpadProductBalanceQueryResModel balanceModel;
    private PsnXpadProductDetailQueryResModel resModel;
    private ArrayList<WealthAccountBean> accountlist;
    private boolean sign;

    public static final String DETAILRESMODEL = "PsnXpadProductDetailQueryResModel";
    public static final String SIGNINITBEAN = "PsnXpadSignInitBean";
    public static final String ACCOUNTLIST = "WealthAccountBean";
    public static final String DEALCODE = "DealCode";
    public static final String OCRMDETAIL = "OcrmDetail";

    protected View onCreateView(LayoutInflater mInflater) {
        recommendView = View.inflate(mContext, R.layout.boc_fragment_xpad_recommend, null);
        return recommendView;
    }

    @Override
    public void initView() {
        noResult = (LinearLayout) recommendView.findViewById(R.id.noResult);
        paymentRecordListView = (PullableListView) recommendView.findViewById(R.id.lv_transfer_query);
        paymentRecordRefresh = (PullToRefreshLayout) recommendView.findViewById(R.id.refresh_transfer_query);

        adapter = new RecommendAdapter(getActivity());
        paymentRecordListView.setAdapter(adapter);
    }

    @Override
    public void initData() {
        pageCurrentIndex = 0;
        pageSize = 10;
        model = new RecommendModel();
        adapterList = new ArrayList<>();
        resModel = new PsnXpadProductDetailQueryResModel();
        balanceModel = new PsnXpadProductBalanceQueryResModel();
        showLoadingDialog(true);
        getPresenter().psnOcrmProductQuery(buildCommendModel());
    }


    @Override
    public RecommendModel getModel() {
        return model;
    }

    /**
     * 查询商品推荐信息成功后回调
     */
    @Override
    public void psnOcrmProductQuerySuccess() {
        closeProgressDialog();
        List<RecommendModel.OcrmDetail> list = model.getResultList();
        if (!PublicUtils.isEmpty(list)) {
            adapterList.addAll(list);
            if (isRefresh) {
                paymentRecordRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }
            noResult.setVisibility(View.GONE);
            paymentRecordRefresh.setVisibility(View.VISIBLE);
            paymentRecordListView.setVisibility(View.VISIBLE);
            adapter.setDatas(adapterList);
            pageCurrentIndex += pageSize;//此处的索引是按记录的索引来走
            _refresh = "false";
        } else {
            noResult.setVisibility(View.VISIBLE);
            paymentRecordListView.setVisibility(View.GONE);
        }
    }

    /**
     * 查询商品推荐信息失败后回调
     */
    @Override
    public void psnOcrmProductQueryFailed() {
        closeProgressDialog();
    }

    /**
     * 商品详情查询成功回调
     * 此处根据跳转标识 mark，进行跳转相关跳转
     * 购买需携带商品详情 联系人集合
     * 赎回需携带点击条目信息 以及商品详情
     */
    @Override
    public void psnXpadProductDetailQuerySuccess(PsnXpadProductDetailQueryResModel detailModel, PsnXpadAccountQueryResModel queryModel, ArrayList<WealthAccountBean> mList) {
        resModel = detailModel;
        accountlist = mList;
        if (!PublicUtils.isEmpty(queryModel.getList()) || !PublicUtils.isEmpty(accountlist)) {
            switch (mark) {
                //赎回页面
                case "001":
                    closeProgressDialog();
                    WealthAccountBean bean = accountlist.get(0);
                    PsnXpadAccountQueryResModel.XPadAccountEntity entity = new PsnXpadAccountQueryResModel.XPadAccountEntity();
                    BeanConvertor.toBean(bean, entity);
                    entity.setAccountId(bean.getAccountId());
                    RedeemFragment redeemFragment = new RedeemFragment();
                    redeemFragment.setData(balanceModel, resModel, transCount, entity);
                    start(redeemFragment);
                    break;

                //购买页面
                case "002":
                    closeProgressDialog();
                    resModel.setBuyAmount(transSum);
                    PurchaseFragment pruchasefragment = PurchaseFragment.newInstance(FinancialPositionCodeModeUtil.buildPurchaseInputModeParams(resModel, currentDealCode), accountlist);
                    start(pruchasefragment);
                    break;

                //协议购买
                case "003":
                    ProductModel proModel = new ProductModel();
                    proModel.setAccountId(accountlist.get(0).getAccountId());
                    proModel.setCurCode(resModel.getCurCode());
                    proModel.setProductCode(resModel.getProdCode());
                    proModel.setProductName(resModel.getProdName());
                    //TODO 此处ocrm接口未返回最大购买期数 所以临时填写“2”来请求下一步接口
                    if (!StringUtils.isEmpty(maxPeriods)) {
                        proModel.setRemainCycleCount(maxPeriods);
                    } else {
                        proModel.setRemainCycleCount("2");
                    }

                    getPresenter().psnXpadSignInit(proModel);
            }
        }
    }

    /**
     * 商品详情查询失败回调
     */
    @Override
    public void psnXpadProductDetailQueryFailed() {
        closeProgressDialog();
    }

    /**
     * 用户持仓查询成功回调
     * 持仓查询之后，遍历用户持仓详情获取单个持仓的数据，与用户点击的条目的产品代码的比对，
     * 如果相符则将其对象封装，继而查询其产品详情
     */
    @Override
    public void psnXpadProductBalanceQuerySuccess() {
        List<RecommendModel.balanceMsg> list = model.getBalanceList();
        if (!PublicUtils.isEmpty(list)) {
            for (RecommendModel.balanceMsg msg : list) {
                if (msg.getProdCode().equals(detail.getProductCode())) {
                    BeanConvertor.toBean(msg, balanceModel);
                    sign = true;
                    break;
                } else {
                    sign = false;
                }
            }
        } else {
            sign = false;

        }
        quertDetail(sign);
    }

    private void quertDetail(boolean m) {
        if (m) {
            getPresenter().psnXpadProductDetailQuery(currentCode);
        } else {
            closeProgressDialog();
            showErrorDialog("您当前没有该产品的持仓信息");
        }
    }

    /**
     * 用户持仓查询失败回调
     */
    @Override
    public void psnXpadProductBalanceQueryFailed() {
        closeProgressDialog();
    }

    @Override
    public void setListener() {
        //上拉加载
        paymentRecordRefresh.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isRefresh = true;
                if (adapterList != null) {
                    if (adapterList.size() < Integer.parseInt(model.getRecordNumber())) {
                        queryPaymentRecordList();
                    } else {
                        paymentRecordRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    model.getResultList().clear();
                    pageCurrentIndex = 0;
                    _refresh = "true";
                    queryPaymentRecordList();
                }
            }
        });

        adapter.setOnViewClickListener(new ItemOnClickListener() {

            @Override
            public void onClickerCode(int position, RecommendModel.OcrmDetail item, View childView) {
                detail = adapterList.get(position);
                DetailsRequestBean detailsModel = new DetailsRequestBean();
                WealthDetailsFragment fragment = new WealthDetailsFragment();
                String code = RecommendFragment.this.detail.getProductCode();
                detailsModel.setProdCode(code);
                detailsModel.setProdKind("0");
                Bundle bundle = new Bundle();
                bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, detailsModel);
                bundle.putBoolean(WealthDetailsFragment.OTHER, true);
                fragment.setArguments(bundle);
                start(fragment);
            }

            @Override
            public void onClickerBtn(int position, RecommendModel.OcrmDetail item, View childView) {
                detail = adapter.getItem(position);
                transCount = detail.getTransCount();
                transSum = detail.getTransSum();
                maxPeriods = detail.getMaxPeriods();
                currentDealCode = detail.getDealCode();
                if ("05".equals(detail.getTransType()) || "07".equals(detail.getTransType())) {
                    if ("0".equals(detail.getIsPre())) {
                        mark = "003";
                        currentCode = detail.getProductCode();
                        showLoadingDialog(false);
                        getPresenter().psnXpadProductDetailQuery(currentCode);
                    }
                    //购买操作
                    else {
                        mark = "002";
                        currentCode = detail.getProductCode();
                        showLoadingDialog(false);
                        getPresenter().psnXpadProductDetailQuery(currentCode);
                    }
                }
                if ("06".equals(detail.getTransType()) || "08".equals(detail.getTransType())) {
                    //赎回操作
                    mark = "001";
                    currentCode = detail.getProductCode();
                    showLoadingDialog(false);
                    getPresenter().psnXpadProductBalanceQuery(currentCode);
                }
            }
        });
    }

    @Override
    public void psnXpadSignInitSuccess(PsnXpadSignInitBean bean) {
        closeProgressDialog();
        ProtocolPeriodContinueFragment continueFragment = new ProtocolPeriodContinueFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ProtocolPeriodContinueFragment.CONTINUE_FROM_KEY, ProtocolPeriodContinueFragment.CONTINUE_FROM_VALUE);
        bundle.putSerializable(DETAILRESMODEL, resModel);//商品详情
        bundle.putParcelableArrayList(ACCOUNTLIST, accountlist);//账户列表
        bundle.putParcelable(SIGNINITBEAN, bean);
        bundle.putString(DEALCODE, currentDealCode);
        bundle.putSerializable(OCRMDETAIL, detail);
        continueFragment.setArguments(bundle);
        start(continueFragment);
    }

    @Override
    public void psnXpadSignInitFailed() {
        closeProgressDialog();
    }

    /**
     * 调用接口，重新查询推荐信息
     */

    private void queryPaymentRecordList() {
        if (pageCurrentIndex == 0) {
            //开启加载对话框
            showLoadingDialog();
        }
        startPresenter();
        getPresenter().psnOcrmProductQuery(buildCommendModel());
    }

    /**
     * 封装上送参数
     */
    private RecommendModel buildCommendModel() {
        model.setProtpye("02");
        model.setTradeType("");
        model.setPageSize(pageSize + "");
        model.setCurrentIndex(pageCurrentIndex + "");
        model.set_refresh(_refresh);
        return model;
    }

    @Override
    protected String getTitleValue() {
        return mContext.getResources().getString(R.string.account_xpad_recommend);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void setPresenter(CommendContract.Presenter presenter) {

    }

    @Override
    protected RecommendPresenter initPresenter() {
        return new RecommendPresenter(this);
    }
}
