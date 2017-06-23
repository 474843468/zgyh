package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQuery.PsnXpadProgressQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProgressQueryOutlay.PsnXpadProgressQueryOutlayParams;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SpringPressageView.SpringRateDetailContent;
import com.boc.bocsoft.mobile.bocmobile.base.widget.emptyview.CommonEmptyView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadProgressQueryOutlay.PsnXpadProgressQueryOutlayResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadprogressquery.PsnXpadProgressQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialPositionContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialTypeProgressQueryPresenter;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

/**
 * 收益累进产品 -- 累计产品收益查询 - 预计年化收益率
 * Created by cff on 2016/10/19.
 */
public class FinancialTypeProgressQueryFragment extends MvpBussFragment<FinancialTypeProgressQueryPresenter>
        implements FinancialPositionContract.FinancialTypeProgressQueryView {
    private static final String TAG = "FinancialTypeProgressQueryFragment";
    private View rootView;
    //产品头部介绍
    private TextView progress_product;
    //=============================变量======================================
    //回话ID
//    private String mConversationID;
    //接收传送数据
//    private PsnXpadProductBalanceQueryResModel banlanceDeta;
    //当前请求页数
    private int page = 0;
    //数据总共条目数
    private int queryNum_total;
    //展示数据
    private List<PsnXpadProgressQueryResModel.ListBean> progressList = new ArrayList<PsnXpadProgressQueryResModel.ListBean>();
    //展示数据
    private List<PsnXpadProgressQueryOutlayResModel.ListBean> progressOutlayList = new ArrayList<PsnXpadProgressQueryOutlayResModel.ListBean>();
    //请求参数
    private PsnXpadProgressQueryParams mParams = new PsnXpadProgressQueryParams();
    //请求参数
    private PsnXpadProgressQueryOutlayParams mParamsOutlay = new PsnXpadProgressQueryOutlayParams();
    private SpringRateDetailContent spring_date;
    private ScrollView scroll_no_emptyview;
    /**
     * 产品名称和code---默认为隐藏
     */
    private LinearLayout title_product_name;
    //空数据页面
    private RelativeLayout rl_content_view_nodata;
    //空数据页面
    private CommonEmptyView view_no_data;
    /**
     * 产品名称
     */
    private String ProdName;
    /**
     * 产品代码
     */
    private String ProdCod;
    /**
     * 资金账号缓存标识
     */
    private String BancAccountKey;
    //是否登录 默认为未登录
    private boolean isLogin = false;

    float max;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.view_financialtype_progress_view, null);
        return rootView;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_position_expected_annual_return_rate);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected View getTitleBarView() {
        TitleBarView titleBarView = (TitleBarView) super.getTitleBarView();
        titleBarView.setRightButton(null, null);
        return titleBarView;
    }

    @Override
    public void initView() {
        progress_product = (TextView) rootView.findViewById(R.id.progress_product);
        scroll_no_emptyview = (ScrollView) rootView.findViewById(R.id.scroll_no_emptyview);
        spring_date = (SpringRateDetailContent) rootView.findViewById(R.id.spring_date);
        title_product_name = (LinearLayout) rootView.findViewById(R.id.title_productnameandcode);
        rl_content_view_nodata = (RelativeLayout) rootView.findViewById(R.id.rl_content_view_nodata);
        view_no_data = (CommonEmptyView) rootView.findViewById(R.id.view_no_data);
        scroll_no_emptyview.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        progress_product.setText(ProdName + " (" + ProdCod + ")");
//        for(int i = 1;i<=10;i++){
//            spring_date.addTextContent("100",i*10+"",i+"-"+i*10+"天");
//        }
//
        if (isLogin) {
            //已登录 获取年华收益率详情
            showLoadingDialog();
            getPresenter().getPSNCreatConversation();
        } else {
            //未登录 获取年华收益率详情
            getPsnXpadProgressQueryOutlay(page);
//            getPresenter().getPsnXpadProgressQueryOutlay();
        }
    }

    @Override
    public void setListener() {
    }

    /**
     * 为预计年化收益率设置数据
     *
     * @param ProdName
     * @param ProdCod
     * @param BancAccountKey
     * @param isLogin
     */
    public void setReferDetail(String ProdName,
                               String ProdCod, String BancAccountKey, boolean isLogin) {
        this.ProdName = ProdName;
        this.ProdCod = ProdCod;
        this.BancAccountKey = BancAccountKey;
        this.isLogin = isLogin;
    }

    /**
     * 获取会话ID--成功
     *
     * @param conversationId
     */
    @Override
    public void obtainConversationSuccess(String conversationId) {
        LogUtils.i(TAG, "-------->获取会话ID--成功！");
        getPsnXpadProgressQuery(page, conversationId);
    }

    /**
     * 获取会话ID--失败
     */
    @Override
    public void obtainConversationFail() {
        LogUtils.i(TAG, "-------->获取会话ID--失败！");
        closeProgressDialog();
    }


    /**
     * 请求收益率查询成功
     *
     * @param resModel
     */
    @Override
    public void obtainPsnXpadProgressQuerySuccess(PsnXpadProgressQueryResModel resModel) {
        LogUtils.i(TAG, "----------请求收益率查询成功----------");
        closeProgressDialog();
        handlerPsnXpadProgressQuerySuccess(resModel);
    }

    /**
     * 请求收益率查询失败
     */
    @Override
    public void obtainPsnXpadProgressQueryFault() {
        LogUtils.i(TAG, "----------请求收益率查询失败----------");
        closeProgressDialog();
        rl_content_view_nodata.setVisibility(View.VISIBLE);
        view_no_data.setEmptyTips("暂无预期年化收益率", R.drawable.no_result);
        scroll_no_emptyview.setVisibility(View.GONE);
    }


    /**
     * 4.53 053 PsnXpadProgressQueryOutlay 登录前累进产品收益率查询
     * 成功
     *
     * @param resModel
     */
    @Override
    public void obtainPsnXpadProgressQueryOutlaySuccess(PsnXpadProgressQueryOutlayResModel resModel) {
        LogUtils.i(TAG, "----------登录前累进产品收益率查询成功----------");
        closeProgressDialog();
        handlerPsnXpadProgressQueryOutlaySuccess(resModel);
    }

    /**
     * 4.53 053 PsnXpadProgressQueryOutlay 登录前累进产品收益率查询
     * 失败
     */
    @Override
    public void obtainPsnXpadProgressQueryOutlayFault() {
        LogUtils.i(TAG, "----------登录前累进产品收益率查询失败----------");
        closeProgressDialog();
        rl_content_view_nodata.setVisibility(View.VISIBLE);
        view_no_data.setEmptyTips("暂无预期年化收益率", R.drawable.no_result);
        scroll_no_emptyview.setVisibility(View.GONE);

    }


    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    protected FinancialTypeProgressQueryPresenter initPresenter() {
        return new FinancialTypeProgressQueryPresenter(this);
    }
    //===========================自定义方法===========================

    /**
     * 网络数据回调处理
     *
     * @param resModel
     */
    private void handlerPsnXpadProgressQuerySuccess(PsnXpadProgressQueryResModel resModel) {
        queryNum_total = Integer.parseInt(resModel.getRecordNumber());
        if (0 == page) {
            progressList = resModel.getList();
        } else {
            progressList.addAll(resModel.getList());
        }
        if (PublicUtils.isEmpty(progressList)) {
            rl_content_view_nodata.setVisibility(View.VISIBLE);
            view_no_data.setEmptyTips("暂无预期年化收益率", R.drawable.no_result);
            scroll_no_emptyview.setVisibility(View.GONE);
        } else {
            scroll_no_emptyview.setVisibility(View.VISIBLE);
            rl_content_view_nodata.setVisibility(View.GONE);

            for (int i = 0; i < progressList.size(); i++) {
                max = Float.valueOf(progressList.get(0).getYearlyRR());
                if (max <= Float.valueOf(progressList.get(i).getYearlyRR())) {
                    max = Float.valueOf(progressList.get(i).getYearlyRR());
                }
            }
            for (int i = 0; i < progressList.size(); i++) {
                PsnXpadProgressQueryResModel.ListBean model = progressList.get(i);
                if (!StringUtils.isEmptyOrNull(model.getMaxDays())&&!"-1".equalsIgnoreCase(model.getMaxDays())) {
                    spring_date.addTextContent(String.valueOf(max),
                            model.getYearlyRR(), model.getMinDays() + "-" + model.getMaxDays() + "天");
                } else {
                    spring_date.addTextContent(String.valueOf(max),
                            model.getYearlyRR(), model.getMinDays() + "天及以上");
                }
            }


//            for(int i = 0; i < 5; i++){
//                spring_date.addTextContent( "15.0" ,String.valueOf(i+6.0),"1-7tian");
//
//            }


        }
    }

    /**
     * 4.53 053 PsnXpadProgressQueryOutlay 登录前累进产品收益率查询
     *
     * @param resModel
     */

    private void handlerPsnXpadProgressQueryOutlaySuccess(PsnXpadProgressQueryOutlayResModel resModel) {
        queryNum_total = Integer.parseInt(resModel.getRecordNumber());
        if (0 == page) {
            progressOutlayList = resModel.getList();
        } else {
            progressOutlayList.addAll(resModel.getList());
        }
        if (PublicUtils.isEmpty(progressOutlayList)) {
            rl_content_view_nodata.setVisibility(View.VISIBLE);
            view_no_data.setEmptyTips("暂无预期年化收益率", R.drawable.no_result);
            scroll_no_emptyview.setVisibility(View.GONE);
        } else {
            scroll_no_emptyview.setVisibility(View.VISIBLE);
            rl_content_view_nodata.setVisibility(View.GONE);
            for (int i = 0; i < progressOutlayList.size(); i++) {
                max = Float.valueOf(progressOutlayList.get(0).getYearlyRR());
                if (max <= Float.valueOf(progressOutlayList.get(i).getYearlyRR())) {
                    max = Float.valueOf(progressOutlayList.get(i).getYearlyRR());
                }
            }

            for (int i = 0; i < progressOutlayList.size(); i++) {
                PsnXpadProgressQueryOutlayResModel.ListBean model = progressOutlayList.get(i);
//                if (null != model.getMinDays()) {
//                    spring_date.addTextContent(progressOutlayList.get(progressOutlayList.size() - 1).getYearlyRR(),
//                            model.getYearlyRR(), model.getMinDays() + "-" + model.getMaxDays() + "天");
//                } else {
//                    spring_date.addTextContent(progressOutlayList.get(progressOutlayList.size() - 1).getYearlyRR(),
//                            model.getYearlyRR(), model.getMaxDays() + "天及以上");
//                }

                if (!StringUtils.isEmptyOrNull(model.getMaxDays())&&!"-1".equalsIgnoreCase(model.getMaxDays())) {
                    spring_date.addTextContent(String.valueOf(max),
                            model.getYearlyRR(), model.getMinDays() + "-" + model.getMaxDays() + "天");
                } else {
                    spring_date.addTextContent(String.valueOf(max),
                            model.getYearlyRR(), model.getMinDays() + "天及以上");
                }
            }
        }

    }

    /**
     * 登陆后
     *
     * @param page
     * @param conversationId
     */
    private void getPsnXpadProgressQuery(int page, String conversationId) {
        mParams.setConversationId(conversationId);
        mParams.setAccountKey(BancAccountKey);
        mParams.setPageSize("50");
        mParams.set_refresh("true");
        mParams.setCurrentIndex(page * 50 + "");
        mParams.setProductCode(ProdCod);
        getPresenter().getPsnXpadProgressQuery(mParams);
    }

    /**
     * 登陆前
     */
    private void getPsnXpadProgressQueryOutlay(int page) {
        mParamsOutlay.setProductCode(ProdCod);
        mParamsOutlay.setPageSize("50");
        mParamsOutlay.set_refresh("true");
        mParamsOutlay.setCurrentIndex(page * 50 + "");
        getPresenter().getPsnXpadProgressQueryOutlay(mParamsOutlay);
    }

    /**
     * 判断是否已加载所有数据
     *
     * @return true 已经全部加载；false 没有全部加载
     */
    private boolean isLoadAllRecord() {
        int loadnum = progressList == null ? 0 : progressList.size();
        int totalnum = queryNum_total;
        if (loadnum >= totalnum) {
            return true;
        } else {
            return false;
        }
    }

}
