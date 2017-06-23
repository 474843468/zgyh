package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.emptyview.CommonEmptyView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.measureview.MeasureListView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.DateUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.adapter.FinancialPsnXpadExpectYieldQueryListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.adapter.FinancialPsnXpadExpectYieldQueryOutlayListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadExpectYieldQuery.PsnXpadExpectYieldQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadExpectYieldQueryOutlay.PsnXpadExpectYieldQueryOutlayResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialPositionContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.presenter.FinancialPsnXpadExpectYieldQueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.util.DateUtils;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * 业绩基准 -->  预期年华收益率
 * PsnXpadExpectYieldQuery
 * Created by zn on 2016/11/10.
 */
public class FinancialPsnXpadExpectYieldQueryFragment
        extends MvpBussFragment<FinancialPsnXpadExpectYieldQueryPresenter>
        implements FinancialPositionContract.FinancialPsnXpadExpectYieldQueryView , View.OnClickListener{
    private View rootView;
    //客户持仓信息
//    private PsnXpadProductBalanceQueryResModel banlanceDeta;
    private String productCode;
    /**
     *4.72 072业绩基准产品预计年收益率查询PsnXpadExpectYieldQuery
     */
    private PsnXpadExpectYieldQueryResModel yieldQueryResModel;
    /**
     *4.73 073登录前业绩基准产品预计年收益率查询 PsnXpadExpectYieldQueryOutlay
     */
    private PsnXpadExpectYieldQueryOutlayResModel yieldQueryOutlayResModel;
    /**
     * 普通客户
     */
    private List<PsnXpadExpectYieldQueryResModel.ListEntity> yieldQueryResModelList1 =
            new ArrayList<PsnXpadExpectYieldQueryResModel.ListEntity>();
    /**
     * 中银理财
     */
    private List<PsnXpadExpectYieldQueryResModel.ListEntity> yieldQueryResModelList2 =
            new ArrayList<PsnXpadExpectYieldQueryResModel.ListEntity>();
    /**
     * 中银财富管理
     */
    private List<PsnXpadExpectYieldQueryResModel.ListEntity> yieldQueryResModelList3 =
            new ArrayList<PsnXpadExpectYieldQueryResModel.ListEntity>();
    /**
     * 中银私人银行
     */
    private List<PsnXpadExpectYieldQueryResModel.ListEntity> yieldQueryResModelList4 =
            new ArrayList<PsnXpadExpectYieldQueryResModel.ListEntity>();

    /**
     * 普通客户
     */
    private List<PsnXpadExpectYieldQueryOutlayResModel.ListEntity> yieldQueryResModelList5 =
            new ArrayList<PsnXpadExpectYieldQueryOutlayResModel.ListEntity>();
    /**
     * 中银理财
     */
    private List<PsnXpadExpectYieldQueryOutlayResModel.ListEntity> yieldQueryResModelList6 =
            new ArrayList<PsnXpadExpectYieldQueryOutlayResModel.ListEntity>();
    /**
     * 中银财富管理
     */
    private List<PsnXpadExpectYieldQueryOutlayResModel.ListEntity> yieldQueryResModelList7 =
            new ArrayList<PsnXpadExpectYieldQueryOutlayResModel.ListEntity>();
    /**
     * 中银私人银行
     */
    private List<PsnXpadExpectYieldQueryOutlayResModel.ListEntity> yieldQueryResModelList8 =
            new ArrayList<PsnXpadExpectYieldQueryOutlayResModel.ListEntity>();
    /**
     * 普通客户
     */
    private FinancialPsnXpadExpectYieldQueryListAdapter yieldQueryListAdapter1;
    /**
     * 中银理财
     */
    private FinancialPsnXpadExpectYieldQueryListAdapter yieldQueryListAdapter2;
    /**
     * 中银财富管理
     */
    private FinancialPsnXpadExpectYieldQueryListAdapter yieldQueryListAdapter3;
    /**
     * 中银私人银行
     */
    private FinancialPsnXpadExpectYieldQueryListAdapter yieldQueryListAdapter4;
    /**
     * 普通客户
     */
    private FinancialPsnXpadExpectYieldQueryOutlayListAdapter yieldQueryListAdapter5;
    /**
     * 中银理财
     */
    private FinancialPsnXpadExpectYieldQueryOutlayListAdapter yieldQueryListAdapter6;
    /**
     * 中银财富管理
     */
    private FinancialPsnXpadExpectYieldQueryOutlayListAdapter yieldQueryListAdapter7;
    /**
     * 中银私人银行
     */
    private FinancialPsnXpadExpectYieldQueryOutlayListAdapter yieldQueryListAdapter8;
    /**
     * 日期
     */
    private String queryDate;
    /**
     * 普通客户
     */
    private LinearLayout ll_content_view1;
    /**
     * 中银理财
     */
    private LinearLayout ll_content_view2;
    /**
     * 中银财富管理
     */
    private LinearLayout ll_content_view3;
    /**
     * 中银私人银行
     */
    private LinearLayout ll_content_view4;
    /**
     * 普通客户
     */
    private TextView tv_cash_management1;
    /**
     * 中银理财
     */
    private TextView tv_cash_management2;
    /**
     * 中银财富管理
     */
    private TextView tv_cash_management3;
    /**
     * 中银私人银行
     */
    private TextView tv_cash_management4;
    /**
     * 普通客户
     */
    private MeasureListView listview_cash_management1;
    /**
     * 中银理财
     */
    private MeasureListView listview_cash_management2;
    /**
     * 中银财富管理
     */
    private MeasureListView listview_cash_management3;
    /**
     * 中银私人银行
     */
    private MeasureListView listview_cash_management4;

    private RelativeLayout rl_content_view_nodata;

    private CommonEmptyView view_no_data;

//    private WealthManagemenAdviertisementView view_no_data_adviertisement;
    private ScrollView sl_content_view;
    private TextView yield_chose_date;
    private RelativeLayout rel_choose_date;
    private String mSystem;
    private String threeTime;
    //是否登陆 默认为 false
    private boolean isLogin = false;
    public static float max;

    private LocalDate mStartDate;
    private LocalDate mEndDate;
    private long minDate;
    private long maxDate;
    private LocalDate mLocalDate;//上一次选择日期
    /**
     * 份额转换页面设置数据
     * @param productCode  产品代码
     */
    public void setYieldQueryDeta(String productCode,boolean isLogin) {
        this.productCode = productCode;
        this.isLogin = isLogin;
    }
    /*
    * 加载View
     * @param mInflater
     * @return
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView=mInflater.inflate(R.layout.boc_fragment_expect_yield_query,null);
        return rootView;
    }
    /**
     * 得到头部标题
     * @return
     */
    @Override
    protected String getTitleValue() {
        return getContext().getResources().getString(R.string.boc_trans_financial_position_shouyilv);
    }
    /**
     * 头部类型设置
     * @return
     */
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }
    /**
     * 头部标题布局
     * @return
     */
    @Override
    protected View getTitleBarView() {
        TitleBarView titleBarView = (TitleBarView) super.getTitleBarView();
        titleBarView.setRightButton(null,null);
        return titleBarView;
    }
    /**
     * 初始化View
     */
    @Override
    public void initView() {
        rel_choose_date = (RelativeLayout)rootView.findViewById(R.id.rel_choose_date);
        sl_content_view = (ScrollView) rootView.findViewById(R.id.sl_content_view);
        yield_chose_date = (TextView)rootView.findViewById(R.id.yield_chose_date);
        yield_chose_date.setOnClickListener(this);
        ll_content_view1 = (LinearLayout)rootView.findViewById(R.id.ll_content_view1);
        ll_content_view2 = (LinearLayout)rootView.findViewById(R.id.ll_content_view2);
        ll_content_view3 = (LinearLayout)rootView.findViewById(R.id.ll_content_view3);
        ll_content_view4 = (LinearLayout)rootView.findViewById(R.id.ll_content_view4);
        tv_cash_management1 = (TextView)rootView.findViewById(R.id.tv_cash_management1);
        tv_cash_management2 = (TextView)rootView.findViewById(R.id.tv_cash_management2);
        tv_cash_management3 = (TextView)rootView.findViewById(R.id.tv_cash_management3);
        tv_cash_management4 = (TextView)rootView.findViewById(R.id.tv_cash_management4);
        listview_cash_management1 =(MeasureListView)rootView.findViewById(R.id.listview_cash_management1);
        listview_cash_management2 =(MeasureListView)rootView.findViewById(R.id.listview_cash_management2);
        listview_cash_management3 =(MeasureListView)rootView.findViewById(R.id.listview_cash_management3);
        listview_cash_management4 =(MeasureListView)rootView.findViewById(R.id.listview_cash_management4);
        rl_content_view_nodata = (RelativeLayout) rootView.findViewById(R.id.rl_content_view_nodata);
        view_no_data = (CommonEmptyView) rootView.findViewById(R.id.view_no_data);
        sl_content_view.setVisibility(View.GONE);
//        view_no_data_adviertisement = (WealthManagemenAdviertisementView) rootView.findViewById(R.id.view_no_data_adviertisement);
    }
    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        //实例化adapter
        view_no_data.setEmptyTips("暂无预期年化收益率",R.drawable.no_result);

        LocalDateTime mLocalDateTime = ApplicationContext.getInstance().getCurrentSystemDate();
        mSystem = mLocalDateTime.format(DateFormatters.dateFormatter1);
        yield_chose_date.setText(mSystem);
        threeTime = DateUtils.getRecenThreetMonth(mSystem);
        mStartDate = LocalDate.parse(threeTime, DateFormatters.dateFormatter1);
        mEndDate = LocalDate.parse(mSystem, DateFormatters.dateFormatter1);
        mLocalDate = mEndDate;
        minDate = DateUtil.parse(mStartDate.format(DateFormatters.dateFormatter1));
        maxDate = DateUtil.parse(mEndDate.format(DateFormatters.dateFormatter1));
        LogUtils.i("threeTime ="+threeTime);//2020/03/21
        LogUtils.i("mSystemTime ="+mSystem);//2020/06/20
        if(isLogin){
            //已登录
            showLoadingDialog(false);
            getPresenter().getPsnXpadExpectYieldQuery(productCode,yield_chose_date.getText().toString());
        }else{
            //未登录
            showLoadingDialog(false);
            getPresenter().getPsnXpadExpectYieldQueryOutlay(productCode,yield_chose_date.getText().toString());
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.yield_chose_date) {//选择日期

            DateTimePicker.showRangeDatePick(mContext, mLocalDate, minDate, maxDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
                @Override
                public void onChoiceDateSet(String strChoiceTime, LocalDate localDate) {
                    yield_chose_date.setText(strChoiceTime);
                    mLocalDate = localDate;
                    if(isLogin){
                        //已登录
                        showLoadingDialog(false);
                        getPresenter().getPsnXpadExpectYieldQuery(productCode,yield_chose_date.getText().toString());
                    }else{
                        //未登录
                        showLoadingDialog(false);
                        getPresenter().getPsnXpadExpectYieldQueryOutlay(productCode,yield_chose_date.getText().toString());
                    }

                }
            });
        }
    }
    /**
     * 预期年华收益率 失败
     */
    @Override
    public void obtainPsnXpadExpectYieldQueryFault() {
            closeProgressDialog();
        sl_content_view.setVisibility(View.GONE);
        rl_content_view_nodata.setVisibility(View.VISIBLE);
        rel_choose_date.setVisibility(View.GONE);
    }
    /**
     * 预期年华收益率 成功
     */
    @Override
    public void obtainPsnXpadExpectYieldQuerySuccess(PsnXpadExpectYieldQueryResModel resModel) {
        closeProgressDialog();
        handlePsnXpadExpectYieldQuery(resModel);
    }


    /**
     * 未登录预期年华收益率 失败
     */
    @Override
    public void obtainPsnXpadExpectYieldQueryOutlayFault() {
        closeProgressDialog();
        sl_content_view.setVisibility(View.GONE);
        rl_content_view_nodata.setVisibility(View.VISIBLE);
        rel_choose_date.setVisibility(View.GONE);
    }

    /**
     * 4.73 073登录前业绩基准产品预计年收益率查询 PsnXpadExpectYieldQueryOutlay
     * 未登录预期年华收益率 成功
     */
    @Override
    public void obtainPsnXpadExpectYieldQueryOutlaySuccess(PsnXpadExpectYieldQueryOutlayResModel resModel) {
        closeProgressDialog();
        handlePsnXpadExpectYieldQueryOutlay(resModel);
    }

    /**
     * 4.73 073登录前业绩基准产品预计年收益率查询 PsnXpadExpectYieldQueryOutlay
     * @param resModel
     */
    private void handlePsnXpadExpectYieldQueryOutlay(PsnXpadExpectYieldQueryOutlayResModel resModel) {
        this.yieldQueryOutlayResModel = resModel;
        if(yieldQueryOutlayResModel != null){
            if(PublicUtils.isEmpty(yieldQueryOutlayResModel.getList())){
                sl_content_view.setVisibility(View.GONE);
                rl_content_view_nodata.setVisibility(View.VISIBLE);
                rel_choose_date.setVisibility(View.GONE);
            }else{
                rel_choose_date.setVisibility(View.VISIBLE);
                sl_content_view.setVisibility(View.VISIBLE);
                rl_content_view_nodata.setVisibility(View.GONE);
                if(!PublicUtils.isEmpty(yieldQueryResModelList5)){//普通客户
                    yieldQueryResModelList5.clear();
                }
                if(!PublicUtils.isEmpty(yieldQueryResModelList6)){//中银理财
                    yieldQueryResModelList6.clear();
                }
                if(!PublicUtils.isEmpty(yieldQueryResModelList7)){//中银财富管理
                    yieldQueryResModelList7.clear();
                }
                if(!PublicUtils.isEmpty(yieldQueryResModelList8)){//中银私人银行
                    yieldQueryResModelList8.clear();
                }
                for(int i = 0;i<yieldQueryOutlayResModel.getList().size();i++){
                    if("0".equalsIgnoreCase(yieldQueryOutlayResModel.getList().get(i).getCustLevel())){//0：普通客户
                        yieldQueryResModelList5.add(yieldQueryOutlayResModel.getList().get(i));
                    }else if("1".equalsIgnoreCase(yieldQueryOutlayResModel.getList().get(i).getCustLevel())){//1：中银理财
                        yieldQueryResModelList6.add(yieldQueryOutlayResModel.getList().get(i));
                    }else if("2".equalsIgnoreCase(yieldQueryOutlayResModel.getList().get(i).getCustLevel())){//2：中银财富管理
                        yieldQueryResModelList7.add(yieldQueryOutlayResModel.getList().get(i));
                    }else if("3".equalsIgnoreCase(yieldQueryOutlayResModel.getList().get(i).getCustLevel())){//3：中银私人银行
                        yieldQueryResModelList8.add(yieldQueryOutlayResModel.getList().get(i));
                    }
                }
                if(!PublicUtils.isEmpty(yieldQueryResModelList5)){
                    yieldQueryListAdapter5 = new FinancialPsnXpadExpectYieldQueryOutlayListAdapter(yieldQueryResModelList5,mContext);
                    listview_cash_management1.setAdapter(yieldQueryListAdapter5);
                    ll_content_view1.setVisibility(View.VISIBLE);
                    getMaxYearRR2(yieldQueryResModelList5);
                }else {
                    ll_content_view1.setVisibility(View.GONE);
                }
                if(!PublicUtils.isEmpty(yieldQueryResModelList6)){
                    yieldQueryListAdapter6 = new FinancialPsnXpadExpectYieldQueryOutlayListAdapter(yieldQueryResModelList6,mContext);
                    listview_cash_management2.setAdapter(yieldQueryListAdapter6);
                    ll_content_view2.setVisibility(View.VISIBLE);
                    getMaxYearRR2(yieldQueryResModelList6);
                }else {
                    ll_content_view2.setVisibility(View.GONE);
                }
                if(!PublicUtils.isEmpty(yieldQueryResModelList7)){
                    yieldQueryListAdapter7 = new FinancialPsnXpadExpectYieldQueryOutlayListAdapter(yieldQueryResModelList7,mContext);
                    listview_cash_management3.setAdapter(yieldQueryListAdapter7);
                    ll_content_view3.setVisibility(View.VISIBLE);
                    getMaxYearRR2(yieldQueryResModelList7);
                }else {
                    ll_content_view3.setVisibility(View.GONE);
                }
                if(!PublicUtils.isEmpty(yieldQueryResModelList8)){
                    yieldQueryListAdapter8 = new FinancialPsnXpadExpectYieldQueryOutlayListAdapter(yieldQueryResModelList8,mContext);
                    listview_cash_management4.setAdapter(yieldQueryListAdapter8);
                    ll_content_view4.setVisibility(View.VISIBLE);
                    getMaxYearRR2(yieldQueryResModelList8);
                }else {
                    ll_content_view4.setVisibility(View.GONE);
                }
            }
        }
    }


    /**
     * 网络数据回调处理
     * @param resModel
     */
    private void handlePsnXpadExpectYieldQuery(PsnXpadExpectYieldQueryResModel resModel) {
            this.yieldQueryResModel = resModel;
            if(yieldQueryResModel != null){
                if(PublicUtils.isEmpty(yieldQueryResModel.getList())){
                    sl_content_view.setVisibility(View.GONE);
                    rl_content_view_nodata.setVisibility(View.VISIBLE);
                    rel_choose_date.setVisibility(View.GONE);
                }else{
                    rel_choose_date.setVisibility(View.VISIBLE);
                    sl_content_view.setVisibility(View.VISIBLE);
                    rl_content_view_nodata.setVisibility(View.GONE);
                    if(!PublicUtils.isEmpty(yieldQueryResModelList1)){//普通客户
                        yieldQueryResModelList1.clear();
                    }
                    if(!PublicUtils.isEmpty(yieldQueryResModelList2)){//中银理财
                        yieldQueryResModelList2.clear();
                    }
                    if(!PublicUtils.isEmpty(yieldQueryResModelList3)){//中银财富管理
                        yieldQueryResModelList3.clear();
                    }
                    if(!PublicUtils.isEmpty(yieldQueryResModelList4)){//中银私人银行
                        yieldQueryResModelList4.clear();
                    }
                    for(int i = 0;i<yieldQueryResModel.getList().size();i++){
                        if("0".equalsIgnoreCase(yieldQueryResModel.getList().get(i).getCustLevel())){//0：普通客户
                            yieldQueryResModelList1.add(yieldQueryResModel.getList().get(i));
                        }else if("1".equalsIgnoreCase(yieldQueryResModel.getList().get(i).getCustLevel())){//1：中银理财
                            yieldQueryResModelList2.add(yieldQueryResModel.getList().get(i));
                        }else if("2".equalsIgnoreCase(yieldQueryResModel.getList().get(i).getCustLevel())){//2：中银财富管理
                            yieldQueryResModelList3.add(yieldQueryResModel.getList().get(i));
                        }else if("3".equalsIgnoreCase(yieldQueryResModel.getList().get(i).getCustLevel())){//3：中银私人银行
                            yieldQueryResModelList4.add(yieldQueryResModel.getList().get(i));
                        }
                    }
                    if(!PublicUtils.isEmpty(yieldQueryResModelList1)){
                        yieldQueryListAdapter1 = new FinancialPsnXpadExpectYieldQueryListAdapter(yieldQueryResModelList1,mContext);
                        listview_cash_management1.setAdapter(yieldQueryListAdapter1);
                        ll_content_view1.setVisibility(View.VISIBLE);
                        getMaxYearRR(yieldQueryResModelList1);
                    }else {
                        ll_content_view1.setVisibility(View.GONE);
                    }
                    if(!PublicUtils.isEmpty(yieldQueryResModelList2)){
                        yieldQueryListAdapter2 = new FinancialPsnXpadExpectYieldQueryListAdapter(yieldQueryResModelList2,mContext);
                        listview_cash_management2.setAdapter(yieldQueryListAdapter2);
                        ll_content_view2.setVisibility(View.VISIBLE);
                        getMaxYearRR(yieldQueryResModelList2);
                    }else {
                        ll_content_view2.setVisibility(View.GONE);
                    }
                    if(!PublicUtils.isEmpty(yieldQueryResModelList3)){
                        yieldQueryListAdapter3 = new FinancialPsnXpadExpectYieldQueryListAdapter(yieldQueryResModelList3,mContext);
                        listview_cash_management3.setAdapter(yieldQueryListAdapter3);
                        ll_content_view3.setVisibility(View.VISIBLE);
                        getMaxYearRR(yieldQueryResModelList3);
                    }else {
                        ll_content_view3.setVisibility(View.GONE);
                    }
                    if(!PublicUtils.isEmpty(yieldQueryResModelList4)){
                        yieldQueryListAdapter4 = new FinancialPsnXpadExpectYieldQueryListAdapter(yieldQueryResModelList4,mContext);
                        listview_cash_management4.setAdapter(yieldQueryListAdapter4);
                        ll_content_view4.setVisibility(View.VISIBLE);
                        getMaxYearRR(yieldQueryResModelList4);
                    }else {
                        ll_content_view4.setVisibility(View.GONE);
                    }
                }
            }
        }

    /**
     * 筛选最大值
     * @param yieldQueryResModelList
     */
    private void getMaxYearRR(List<PsnXpadExpectYieldQueryResModel.ListEntity> yieldQueryResModelList) {

        for(int i = 0;i<yieldQueryResModelList.size();i++){
            max = Float.valueOf(yieldQueryResModelList.get(0).getRates());
            if(max <= Float.valueOf(yieldQueryResModelList.get(i).getRates())){
                max = Float.valueOf(yieldQueryResModelList.get(i).getRates());
            }
        }
    }


    private void getMaxYearRR2(List<PsnXpadExpectYieldQueryOutlayResModel.ListEntity> yieldQueryResModelList2) {
        for(int i = 0;i<yieldQueryResModelList2.size();i++){
            max = Float.valueOf(yieldQueryResModelList2.get(0).getRates());
            if(max <= Float.valueOf(yieldQueryResModelList2.get(i).getRates())){
                max = Float.valueOf(yieldQueryResModelList2.get(i).getRates());
            }
        }

    }
    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    protected FinancialPsnXpadExpectYieldQueryPresenter initPresenter() {
        return  new FinancialPsnXpadExpectYieldQueryPresenter(this);
    }


}
