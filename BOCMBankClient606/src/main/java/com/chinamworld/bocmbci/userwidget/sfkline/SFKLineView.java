package com.chinamworld.bocmbci.userwidget.sfkline;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.base.widget.trendview.BocTrendView;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.userwidget.echarsview.ECharsType;
import com.chinamworld.bocmbci.userwidget.echarsview.ECharsView;
import com.chinamworld.bocmbci.userwidget.echarsview.IECharsData;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.llbt.userwidget.tabview.ITabViewSelectedChanged;
import com.chinamworld.llbt.userwidget.tabview.TabButton;
import com.chinamworld.llbt.userwidget.tabview.TabView;
import com.chinamworld.llbt.utils.TimerRefreshTools;
import com.forms.androidcharts.common.OnTouchPositionListener;
import com.forms.androidcharts.entity.DateValueEntity;
import com.forms.androidcharts.entity.IStickEntity;
import com.forms.androidcharts.entity.LineEntity;
import com.forms.androidcharts.entity.OHLCEntity;
import com.forms.androidcharts.view.MASlipCandleStickChart;
import com.forms.androidcharts.view.OntouchCancleListener;
import com.forms.view.ChartsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/21.
 */
public class SFKLineView  extends FrameLayout implements ITabViewSelectedChanged, OnTouchPositionListener, OntouchCancleListener, View.OnClickListener {


    public SFKLineView(Context context) {
        super(context);
        initView(context);
    }

    public SFKLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SFKLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private Context mContext;
    /** K线图  */
    private ChartsView chartsView;
    /** 折线图 */
    private ECharsView mECharsView;
    private MASlipCandleStickChart maslipcandlestickchart;
    // 柱状图集合
    private List<IStickEntity> mOHLCList = new ArrayList<IStickEntity>();;
    // 均线数据
    private List<IStickEntity> mStickList = new ArrayList<IStickEntity>();
    // 柱状图实体
    private OHLCEntity entity;
    // 柱状图显示数据
    private TextView closePrice;
    private TextView maxPrice;
    private TextView minPrice;
    private TextView openPrice;
    private TextView time;
    private TextView ccygrpNm;

    //MA 数据
    private TextView tv_MA5,tv_MA10,tv_MA20,tv_MA30;
    private LinearLayout ll_maValue,ll_kline_info;

    private TabView mTabView;

    private View mTitleContentLayout;

    BocTrendView mBocTrendView;

    private FrameLayout fl_title;
    //横屏图标
    private ImageView ima_Landscape;
    private ImageView ima_changeCharsView;
    private LinearLayout ll_kline_fengge;
    private LinearLayout ll_klinetitle_landscape;
    private ImageView ima_landscape_cancel;
    //横屏标题栏textviwe
    private TextView tv_currCode,tv_middleRate,tv_currDiff,tv_currDiffRate,tv_openPrice_landscape,tv_maxPrice_landscape,tv_minPrice_landscape,tv_closePrice_landscape;
    private List<Map<String,Object>> allRateList;
    private View fenggeLandscape;

    /** 从竖屏变为横屏状态时，由显示变为隐藏状态时的布局文件  */
    List<View> mViewList = new ArrayList<View>();

    private TextView sfkLine_empty;//数据为空时显示内容
    private View lineView;
    /**K线折线布局**/
//    private FrameLayout klinelayout;

    private void initView(Context context){
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.sfkline_view_layout,this,true);
        mTitleContentLayout = findViewById(R.id.titleContentLayout);
        mECharsView = (ECharsView)findViewById(R.id.echartsView);
        mBocTrendView = (BocTrendView)findViewById(R.id.echartsView1);


        this.chartsView = (ChartsView) findViewById(R.id.kLineView);


        this.maslipcandlestickchart = chartsView.getStickChart();

        mCharsChangeImage = (ImageView) findViewById(R.id.changedCharsView);
        chartsView.setDisplayBottomVal(true);// 是否显示y轴下值
        this.chartsView.setOnTouchPositionListener(this);
        this.chartsView.setOntouchCancleListener(this);
        mTabView = (TabView)findViewById(R.id.tabView);
//        mTabView.setContentText("1小时;4小时;日;周;月");
        mTabView.setTabViewSelectedChanged(this);

        /** 设置触摸消失的时间 单位为秒 */
        maslipcandlestickchart.setDismissTime(3);
        /** 是否显示边框 */
        maslipcandlestickchart.setDisplayBorder(false);
        /** 是否显示经纬线数字和经纬线 */
        maslipcandlestickchart.setDisplayLongitudeTitle(true);
        maslipcandlestickchart.setDisplayLatitudeTitle(true);
        maslipcandlestickchart.setDisplayLatitude(true);
        maslipcandlestickchart.setDisplayLongitude(true);
        /** 设置经纬线颜色 */
        maslipcandlestickchart.setLatitudeColor(Color.GRAY);
        maslipcandlestickchart.setLongitudeColor(Color.GRAY);
        /** 设置纬线等分数 */
        maslipcandlestickchart.setLatitudeNum(3);
        /** 设置经线等分数 */
        maslipcandlestickchart.setLongitudeNum(1);
        /** 是否显示底部纬线值 */
        maslipcandlestickchart.setDisplayBottomVal(true);
        /** 设置整个图表的Padding */
        maslipcandlestickchart.setDataQuadrantPaddingTop(10);
        maslipcandlestickchart.setDataQuadrantPaddingBottom(0);
        maslipcandlestickchart.setDataQuadrantPaddingLeft(0);
        /** 设置图表可以随手势移动 */
        maslipcandlestickchart.setChartGestureMove(true);

        chartsView.setChartGestureMove(false);
//        ccygrpNm = (TextView) findViewById(R.id.name);
        maxPrice = (TextView) findViewById(R.id.maxPrice);
        closePrice = (TextView) findViewById(R.id.closePrice);
        minPrice = (TextView) findViewById(R.id.minPrice);
        openPrice = (TextView) findViewById(R.id.openPrice);
        time = (TextView) findViewById(R.id.time);

        tv_MA5 = (TextView) findViewById(R.id.tv_ma5);
        tv_MA10 = (TextView) findViewById(R.id.tv_ma10);
        tv_MA20 = (TextView) findViewById(R.id.tv_ma20);
        tv_MA30 = (TextView) findViewById(R.id.tv_ma30);

//        klinelayout = (FrameLayout) findViewById(R.id.rl);
        ll_maValue = (LinearLayout) findViewById(R.id.ll_mavalue);
        ll_kline_info = (LinearLayout) findViewById(R.id.ll_kline_info);
        ll_kline_fengge = (LinearLayout) findViewById(R.id.ll_fengge);
        lineView = findViewById(R.id.fenggeline);
        fl_title = (FrameLayout) findViewById(R.id.titleLayout);
        ima_Landscape = (ImageView) findViewById(R.id.ima_landscape);
        ima_changeCharsView = (ImageView) findViewById(R.id.changedCharsView);
        ll_klinetitle_landscape = (LinearLayout) findViewById(R.id.ll_klinetitle_landscape);
        ima_landscape_cancel = (ImageView) findViewById(R.id.ima_landscape_cancel);
        tv_currCode = (TextView) findViewById(R.id.tv_choose_currcode);
        tv_middleRate = (TextView) findViewById(R.id.middrate_landscape);
        tv_currDiff = (TextView) findViewById(R.id.currdiff_landscape);
        tv_currDiffRate = (TextView) findViewById(R.id.currdiffrate_landscape);
        tv_openPrice_landscape = (TextView) findViewById(R.id.openPrice_landscape);
        tv_maxPrice_landscape = (TextView) findViewById(R.id.maxPrice_landscape);
        tv_minPrice_landscape = (TextView) findViewById(R.id.minPrice_landscape);
        tv_closePrice_landscape = (TextView) findViewById(R.id.closePrice_landscape);
        fenggeLandscape = findViewById(R.id.view_fenge_landscape);
        ima_landscape_cancel.setOnClickListener(this);
        tv_currCode.setOnClickListener(this);
        ima_Landscape.setOnClickListener(this);

        sfkLine_empty = (TextView) findViewById(R.id.sfkLine_empty);

        ima_changeCharsView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mShowType == 1) {
                    setShowCharsView(2);
                }
                else if(mShowType == 2){
                    setShowCharsView(1);
                }
            }
        });
        setShowCharsView(1);
    }



    /** 获得当前根布局 */
    protected ViewGroup getKLineTitle(){
        return (ViewGroup) findViewById(R.id.titleLayout);
    }

    ImageView mCharsChangeImage;

    /**
     * 显示/隐藏K线图上面内容
     * visibility  view visilibility 显示隐藏值
     */
    public void setTitleContentVisibility(int visibility){
        mTitleContentLayout.setVisibility(visibility);
    }


    /**
     * 1:K线图
     * 2：折线图
     */
    private int mShowType;

    /** 获得当前显示的图类型
     * 1:K线图
     * 2：折线图
     * */
    public int getShowType(){
        return mShowType;
    }

    /** 设置当前显示的图类型
     * 1:K线图
     * 2：折线图
     * */
    public void setShowType(int type){
        mShowType = type;
        setShowCharsView(mShowType);
    }

    private void setShowCharsView(int showType){
        mShowType = showType;
        chartsView.setVisibility(mShowType == 1 ? View.VISIBLE : View.GONE);
        if(mECharsView.getECharsType() == ECharsType.QuShi){
            mBocTrendView.setVisibility(mShowType == 2 ? View.VISIBLE : View.GONE);
        }else{
            mECharsView.setVisibility(mShowType == 2 ? View.VISIBLE : View.GONE);
        }
//        mECharsView.setVisibility(mShowType == 2 ? View.VISIBLE : View.GONE);
        ima_Landscape.setVisibility(mShowType == 2 ? View.GONE : View.VISIBLE);
        mCharsChangeImage.setImageDrawable(mShowType == 2 ? getResources().getDrawable(R.drawable.llbt_ima_kline):getResources().getDrawable(R.drawable.llbt_ima_zoushi));
        ll_maValue.setVisibility(mShowType == 2 ? View.GONE : View.VISIBLE );
        ll_kline_info.setVisibility(mShowType == 2 ? View.GONE : View.VISIBLE );
        lineView.setVisibility(mShowType == 2 ? View.VISIBLE : View.GONE);
//        mBocTrendView.setVisibility(mShowType == 2 ? View.VISIBLE : View.GONE);
        mTimerRefreshTools.stopTimer();
        if(mRefreshKLineDataListener != null){
            mRefreshKLineDataListener.onRefreshKLineDataCallBack(mCurIndex,getShowType(),false);
        }

    }

    private IRefreshKLineDataListener mRefreshKLineDataListener;

    /** 监听KLine数据刷新事件 */
    public void setRrefreshKLineDataListener(IRefreshKLineDataListener listener){
        mRefreshKLineDataListener = listener;
    }


    /** 设置K线图数据 */
    public void setKLineData(BaseSFKLineData data){
        mTimerRefreshTools.startTimer();
        this.mStickList = new ArrayList<IStickEntity>();
        for(StickItem item :data.getStickList()){
            mStickList.add(item.toStickEntity());
        }
        mOHLCList = new ArrayList<IStickEntity>();
        for(OHLCItem item :data.getOHLCList()){
            mOHLCList.add(item.toOHLCEntity());
        }
        if (mOHLCList == null || mOHLCList.size() <= 0) {
            return;
        }
        if (chartsView.candleStickData(mOHLCList)) {
            hidePositon(mOHLCList.size() - 1);
        }
    }

    private String bottomDate = "yyyy\nMM-dd",selectDate,selectPriceTips, selectDateTips,touchTips,dateFormat,emptyTips;
    private int leftPriceScale;
    /**
     * 趋势图数据
     * @param bottomDate X轴数据格式 如yyyy\nMM-dd
     * @param selectDate 选择数据的格式 yyyy-MM-dd
     * @param selectPriceTips Y轴提示信息
     * @param selectDateTips X轴提示信息
     * @param touchTips 触摸提示信息
     * @param leftPriceScale Y轴小数位数
     * @param dateFormat 日期格式
     * @param emptyTips 数据为空时提示信息
     */
    public void setQuShiData(String bottomDate,String selectDate,String selectPriceTips,
    String selectDateTips,String touchTips,int leftPriceScale,String dateFormat,String emptyTips){
        this.bottomDate = bottomDate;
        this.selectDate = selectDate;
        this.selectPriceTips = selectPriceTips;
        this.selectDateTips = selectDateTips;
        this.touchTips = touchTips;
        this.leftPriceScale = leftPriceScale;
        this.dateFormat = dateFormat;
        this.emptyTips = emptyTips;
    }

    /**
     * 趋势图数据
     */
    private void setQuShiData(){
        if(!StringUtil.isNullOrEmpty(dateFormat)){
            mBocTrendView.getParamBuilder().dateFormat(dateFormat);
        }
        if(!StringUtil.isNullOrEmpty(bottomDate)){
            mBocTrendView.getParamBuilder().bottomDateFormat(bottomDate);
        }
        if(!StringUtil.isNullOrEmpty(selectDate)){
            mBocTrendView.getParamBuilder().selectDateFormat(selectDate);
        }
        if(!StringUtil.isNullOrEmpty(selectPriceTips)){
            mBocTrendView.getParamBuilder().selectPriceTips(selectPriceTips);
        }
        if(!StringUtil.isNullOrEmpty(selectDateTips)){
            mBocTrendView.getParamBuilder() .selectDateTips(selectDateTips);
        }
        mBocTrendView.getParamBuilder().touchIndicatorEnable(true);
        if(!StringUtil.isNullOrEmpty(touchTips)){
            mBocTrendView.getParamBuilder().touchTips("手指移至曲线图上，可查看"+touchTips);
        }
        if(!StringUtil.isNullOrEmpty(leftPriceScale)){
            mBocTrendView.getParamBuilder().leftPriceScale(leftPriceScale);
        }
        if(!StringUtil.isNullOrEmpty(emptyTips)){
            mBocTrendView.getParamBuilder().emptyTips(emptyTips);
        }
    }

    /** 设置趋势图等折线图数据 */
    public void setECharsViewData(IECharsData... data){
        if(StringUtil.isNullOrEmpty(data)){
            sfkLine_empty.setVisibility(View.VISIBLE);
            mBocTrendView.setVisibility(View.GONE);
        }else{
            sfkLine_empty.setVisibility(View.GONE);
            mBocTrendView.setVisibility(View.VISIBLE);
            mTimerRefreshTools.startTimer();
//        mECharsView.setData(data);
            setQuShiData();
            BocTrendView.TrendVo item ;
            List<BocTrendView.TrendVo> list = new ArrayList<BocTrendView.TrendVo>();
            for(int i = 0; i < data[0].getXList().size();i++){
                item = new BocTrendView.TrendVo();
                item.setDate(data[0].getXList().get(i));
                item.setPrice(data[0].getYList().get(i));
                list.add(item);
            }
            mBocTrendView.update(list);
        }
    }

    /** 设置趋势图等折线图数据 */
    public void setECharsViewData(ECharsType type,IECharsData... data){
        if(StringUtil.isNullOrEmpty(data)){
            sfkLine_empty.setVisibility(View.VISIBLE);
            mECharsView.setVisibility(View.GONE);
            mBocTrendView.setVisibility(View.GONE);
        }else{
            if(data[0].getYList().size()==0){
                sfkLine_empty.setVisibility(View.VISIBLE);
                mECharsView.setVisibility(View.GONE);
                mBocTrendView.setVisibility(View.GONE);
            }else{
                sfkLine_empty.setVisibility(View.GONE);
                mECharsView.setECharsType(type);
                if(mECharsView.getECharsType() == ECharsType.QuShi){
                    mECharsView.setVisibility(View.GONE);
                    mBocTrendView.setVisibility(View.VISIBLE);
                    setECharsViewData(data);
                }else{
                    mECharsView.setVisibility(View.VISIBLE);
                    mBocTrendView.setVisibility(View.GONE);
                    mTimerRefreshTools.startTimer();
                    mECharsView.setData(data);
                }
            }
        }
    }

    /** 当前选中项 */
    protected int mCurIndex;
    /** 设置当前选中项
     * 此方法必须要在注册数据刷新之后
     * */
    public void setCurSelectedIndex(int index){
        mTabView.setCurSelectedIndex(index);
        mCurIndex = index;
    }
    /** 设置数据刷新的按钮文字，每个按钮文字之间用“;”隔开 */
    public void setContentText(String contentText){
        mTabView.setContentText(contentText);
        mTabView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChanged(TabButton tb) {
        resetData();
        mCurIndex = (Integer) tb.getTag();
        refreshData(false);
    }

    private void refreshData(boolean isBackgroundRefresh){
        mTimerRefreshTools.stopTimer();
        if(mRefreshKLineDataListener != null){
            mRefreshKLineDataListener.onRefreshKLineDataCallBack(mCurIndex,getShowType(),isBackgroundRefresh);
        }
    }

    /** 重置数据 */
    public void resetData(){
        chartsView.initStickChartView();
        mECharsView.setData();
        setTVData(null,-1);
        mTimerRefreshTools.stopTimer();
//        if(mRefreshKLineDataListener != null){
//            mRefreshKLineDataListener.onRefreshKLineDataCallBack(mCurIndex,getShowType(),false);
//        }
    }

    /**
     * 十字线消失相应方法 position 十字线消失对应的位置
     */
    @Override
    public void hidePositon(int position) {
        // 回到最新一个K线数据
        if(maslipcandlestickchart.getStickData() != null && mStickList.size() > 0 ){
            entity = (OHLCEntity) maslipcandlestickchart.getStickData().get(mStickList.size() - 1);
//          getPosition(maslipcandlestickchart.getDisplayTo() - 1);
            setTVData(entity,position);
        }
    }

    @Override
    public void getPosition(int position) {
//        OHLCEntity entitBefore;
        if (mStickList.size() > 1) {
            entity = (OHLCEntity) maslipcandlestickchart.getStickData().get(position);
            if (position > 0) {
//                entitBefore = (OHLCEntity) maslipcandlestickchart.getStickData().get(position - 1);
                setTextViewColor(getResources().getColor(R.color.white));
            }
            setTVData(entity,position);
        }
    }

    @Override
    public void onDoubleClick(View view) {
//        Toast.makeText(mContext, "双击...", Toast.LENGTH_SHORT).show();

    }


    /**
     * 设置柱状图文本显示
     *
     * @param entity
     * @param position
     *            柱状图实体
     */
    private void setTVData(OHLCEntity entity,int position) {
//        ccygrpNm.setText(resultName);
        setMAValueData(position);
        closePrice.setText(StringUtil.isNullOrEmpty(entity) ? "" : String.valueOf(entity.getClose()));
        maxPrice.setText(StringUtil.isNullOrEmpty(entity) ? "" : String.valueOf(entity.getHigh()));
        minPrice.setText(StringUtil.isNullOrEmpty(entity) ? "" : String.valueOf(entity.getLow()));
        openPrice.setText(StringUtil.isNullOrEmpty(entity) ? "" : String.valueOf(entity.getOpen()));
        //横屏时显示数据
        tv_closePrice_landscape.setText(StringUtil.isNullOrEmpty(entity) ? "" : String.valueOf(entity.getClose()));
        tv_maxPrice_landscape.setText(StringUtil.isNullOrEmpty(entity) ? "" : String.valueOf(entity.getHigh()));
        tv_minPrice_landscape.setText(StringUtil.isNullOrEmpty(entity) ? "" : String.valueOf(entity.getLow()));
        tv_openPrice_landscape.setText(StringUtil.isNullOrEmpty(entity) ? "" : String.valueOf(entity.getOpen()));
        String year = StringUtil.isNullOrEmpty(entity) ? null : entity.getDate().trim();
        if(year != null && year.length() >= 8){
            String year_str = year.substring(0,8).trim();
            if(year_str != null & year_str.length() > 0){
                String yearStr = year_str.substring(0,4) + "/" + year_str.substring(4,6) + "/" + year_str.substring(6,8);
                time.setText(yearStr);
            }else{
                time.setText("");
            }
        }else{
            time.setText("");
        }

    }


    private void setTextViewColor(int resid){
        openPrice.setTextColor(resid);
        closePrice.setTextColor(resid);
        maxPrice.setTextColor(resid);
        minPrice.setTextColor(resid);
    }

    TimerRefreshTools mTimerRefreshTools = new TimerRefreshTools(7000, new TimerRefreshTools.ITimerRefreshListener() {
        @Override
        public void onRefresh() {
            if(mRefreshKLineDataListener != null){
                mRefreshKLineDataListener.onRefreshKLineDataCallBack(mCurIndex,getShowType(),true);
            }
        }
    });

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTimerRefreshTools.stopTimer();
    }

    /**
     * 获取均线MA均值
     * @param xLineEntity  总数据list
     * @param position
     * @return
     */
    private float getAverage(LineEntity<DateValueEntity> xLineEntity, int position){
        float average = 0.00f;
        try{
            int day = xLineEntity.getDays();
            if(xLineEntity != null && position >= day){
                for(int i = 0;i<day;i++){
                    average += ((DateValueEntity)xLineEntity.getLineData().get(position-1)).getValue();
                }
            }
            average /= day;
        }catch (Exception e){

        }
        return (float) Math.round(average*100)/100;//控制小数位数为2位小数
    }

    /**
     * 均线均值赋值（MA5/MA10/ma20/ma30）
     * @param position
     */
    private void setMAValueData(int position){
        float ma5_value = getAverage(maslipcandlestickchart.getMA5(),position);
        float ma10_value = getAverage(maslipcandlestickchart.getMA10(),position);
        float ma20_value = getAverage(maslipcandlestickchart.getMA20(),position);
        float ma30_value = getAverage(maslipcandlestickchart.getMA30(),position);
        tv_MA5.setText("MA5:"+ (ma5_value > 0.00f ? ma5_value : "--"));
        tv_MA10.setText("MA10:"+ (ma10_value > 0.00f ? ma10_value : "--"));
        tv_MA20.setText("MA20:"+ (ma20_value > 0.00f ? ma20_value : "--"));
        tv_MA30.setText("MA30:"+ (ma30_value > 0.00f ? ma30_value : "--"));
        maslipcandlestickchart.getMA5().setLineColor(getResources().getColor(R.color.llbt_kline_ma5));
        maslipcandlestickchart.getMA10().setLineColor(getResources().getColor(R.color.llbt_kline_ma10));
        maslipcandlestickchart.getMA20().setLineColor(getResources().getColor(R.color.llbt_kline_ma20));
        maslipcandlestickchart.getMA30().setLineColor(getResources().getColor(R.color.llbt_kline_ma30));
    }

    /** 开启刷新计时器 */
    public void startRefreshData(){
        mTimerRefreshTools.startTimer();
    }

    /** 停止刷新数据 */
    public void stopRefreshData(){
        mTimerRefreshTools.stopTimer();
    }



    private ViewGroup mRootLayout;
    /**
     * 获取当前布局的根布局view 的 根布局方法
     */
    private ViewGroup getRootLayout(){
        if(mRootLayout == null)
            mRootLayout = getRootView(this);
        return mRootLayout;
    }

    /**
     * 迭代获取view 的 根布局方法。获得当前布局的根布局，请使用getRootLayout()方法
     * @param v view
     */
    private ViewGroup getRootView(View v){
        ViewGroup vg ;
        ViewParent parent = v.getParent();
        if(parent instanceof ViewGroup == false)
            return (ViewGroup) v;
        vg = (ViewGroup) parent;
        if(vg.getParent() == null) return vg;
        return getRootView(vg);
    }

    /**
     * 隐藏指定ViewGroup中所有元素(指定 v 除外)
     * @param vg
     */
    private void goneAllChildView(ViewGroup vg,View v){

        if(vg == null)
            return;
        if(vg.equals(v)) {
            mViewList.add(v);
            return;
        }
        for(int i = 0; i< vg.getChildCount();i++){
            if(vg.getChildAt(i).getVisibility() == View.VISIBLE) {
                mViewList.add(vg.getChildAt(i));
            }
            vg.getChildAt(i).setVisibility(View.GONE);
            if(vg.getChildAt(i) instanceof ViewGroup){
                goneAllChildView((ViewGroup) vg.getChildAt(i),v);
            }
        }
    }

    /**
     * 显示指定元素的所有父布局
     * @param v
     */
    private void visibilityAllParentByView(View v){
        if(v == null)
            return;
        v.setVisibility(View.VISIBLE);
        ViewParent parent = v.getParent();
        if(parent instanceof ViewGroup == false)
            return;
        visibilityAllParentByView((ViewGroup) parent);
    }

    /**
     * 显示指定viewGroup所有元素(vg中原本隐藏元素除外)
     * @param
     * @param
     */
    private void resetVisiableAllChildView(ViewGroup rootView){
        if(rootView == null || mViewList == null)
            return;
        for(int i = 0; i < rootView.getChildCount();i++){
            for(int j = 0;j< mViewList.size();j++){
                if(rootView.getChildAt(i).equals(mViewList.get(j))){
                    rootView.getChildAt(i).setVisibility(View.VISIBLE);
                }
            }
            if(rootView.getChildAt(i) instanceof ViewGroup){
                resetVisiableAllChildView((ViewGroup) rootView.getChildAt(i));
            }
        }
    }

    /**
     * 设置横屏显示内容（显示KLineView，其他隐藏）
     */
    private void setLandscapeContent(){
        ViewGroup vg = getRootLayout();
        mViewList.clear();
        goneAllChildView(vg,this);
//        final float scale = mContext.getResources().getDisplayMetrics().density;
//        int pxValue = (int) (150 * scale + 0.5f);
//        WindowManager wm = (WindowManager) getContext()
//                .getSystemService(Context.WINDOW_SERVICE);
////        int width = wm.getDefaultDisplay().getWidth();
//        int height = wm.getDefaultDisplay().getHeight();
//        klinelayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height-pxValue));
        visibilityAllParentByView(this);
        ll_kline_fengge.setVisibility(View.GONE);
        ll_kline_info.setVisibility(View.GONE);
        fl_title.setVisibility(View.GONE);
        ima_Landscape.setVisibility(View.GONE);
        fenggeLandscape.setVisibility(View.VISIBLE);
        ll_klinetitle_landscape.setVisibility(View.VISIBLE);
        ima_changeCharsView.setVisibility(View.INVISIBLE);
        maslipcandlestickchart.setChartGestureMove(true);
    }
    /**
     * 设置竖屏显示内容（显示KLineView，其他隐藏）
     */
    private void setPortraitContent(){
        ViewGroup vg = getRootLayout();
//        //dp转换px
//        final float scale = mContext.getResources().getDisplayMetrics().density;
//        int pxValue = (int) (250 * scale + 0.5f);
//        klinelayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,pxValue));
        resetVisiableAllChildView(vg);
        ll_kline_fengge.setVisibility(View.VISIBLE);
        ll_kline_info.setVisibility(View.VISIBLE);
        fl_title.setVisibility(View.VISIBLE);
        ima_changeCharsView.setVisibility(View.VISIBLE);
        ima_Landscape.setVisibility(View.VISIBLE);
        fenggeLandscape.setVisibility(View.GONE);
        ll_klinetitle_landscape.setVisibility(View.GONE);
        maslipcandlestickchart.setChartGestureMove(false);
    }

    /**
     * 横屏时 中间价、涨跌值、涨跌幅（需要再使用控件的Activity 7s刷新接口里调用进行赋值）
     * @param middrate
     * @param currdiff
     * @param currdiffrate
     */
    public void setRateTextRefresh(String middrate,String currdiff,String currdiffrate){
        tv_middleRate.setText(StringUtil.isNullOrEmpty(middrate) ? "--" : middrate);
        tv_currDiff.setText(StringUtil.isNullOrEmpty(currdiff) ? "--" : currdiff);
        tv_currDiffRate.setText(StringUtil.isNullOrEmpty(currdiffrate) ? "(--)" : "("+currdiffrate+")");
    }

    /**
     * 设置横屏时货币对文本
     * @param str
     */
    public void setLandscapeCurCode(String str){
        tv_currCode.setText(StringUtil.isNullOrEmpty(str) ? "--" : str+"  ");
    }

    /**
     * 设置横屏时的货币对数据源
     */
    public void setLandscapeAllRateList(List<Map<String,Object>> ratelist){
        if(StringUtil.isNullOrEmpty(ratelist)) return;
        allRateList = ratelist;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ima_landscape://横屏
                ((Activity)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case R.id.ima_landscape_cancel://横屏X按钮
                ((Activity)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case R.id.tv_choose_currcode://横屏货币对选择
                BaseDroidApp.getInstanse()
                        .showSelectCurrencyPairDialog(mContext , allRateList, onLandscapeCurCodeItemClickListener);
                break;
        }
    }

    private AdapterView.OnItemClickListener onLandscapeCurCodeItemClickListener =null;
    /**
     * 横屏时货币对item点击事件
     */
    public void setLandscapeCurCodeItemClickListener(AdapterView.OnItemClickListener listener){
        if(listener == null) return;
        onLandscapeCurCodeItemClickListener = listener;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setLandscapeContent();
        }else {
            setPortraitContent();
        }
    }

    /**
     * 设置横竖屏切换图标显示/隐藏
     * @param visibility
     */
    public void setImaLandscapePortraitVisibility(int visibility){
        ima_Landscape.setVisibility(visibility);
    }

    /**
     * 设置数据为空时的布局是否显示，默认为隐藏
     * @param visibility
     */
    public void setSfkLineEmptyVisibility(int visibility){
        sfkLine_empty.setVisibility(visibility);
    }

    /**
     * 设置横屏时 标题栏的背景颜色
     * @param resid
     */
    public void setLandscapeTitleBackground(int resid){
        ll_klinetitle_landscape.setBackgroundColor(resid);
    }

    /**
     * 设置数据为空时的布局是否显示，默认为隐藏
     * @param visibility
     */
    public void setmBocTrendViewVisibility(int visibility){
        mBocTrendView.setVisibility(visibility);
    }

}
