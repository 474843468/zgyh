package com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.sfkline;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.trendview.BocTrendView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview.ECharsType;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview.ECharsView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview.IECharsData;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/21.
 */
public class SFKLineView  extends FrameLayout implements ITabViewSelectedChanged, OnTouchPositionListener, OntouchCancleListener {


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
    private ECharsView mECharsView;// ECharsView extends WebView
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

    private void initView(Context context){
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_sfkline,this,true);
        mTitleContentLayout = findViewById(R.id.titleContentLayout);
        mECharsView = (ECharsView)findViewById(R.id.echartsView);
        mBocTrendView = (BocTrendView)findViewById(R.id.echartsView1);


        this.chartsView = (ChartsView) findViewById(R.id.kLineView);
        chartsView.setChartGestureMove(false);

        this.maslipcandlestickchart = chartsView.getStickChart();
        maslipcandlestickchart.setChartGestureMove(false);

        mCharsView = (ImageView) findViewById(R.id.changedCharsView);
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

        ll_maValue = (LinearLayout) findViewById(R.id.ll_mavalue);
        ll_kline_info = (LinearLayout) findViewById(R.id.ll_kline_info);

        findViewById(R.id.changedCharsView).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mShowType == 1) {
//                    mCharsView.setImageDrawable(getResources().getDrawable(R.drawable.llbt_ima_zoushi));
                    mCharsView.setImageDrawable(getResources().getDrawable(R.drawable.llbt_ima_kline));
                    ll_maValue.setVisibility(View.GONE);
                    ll_kline_info.setVisibility(View.GONE);
                    setShowCharsView(2);
                }
                else if(mShowType == 2){
//                    mCharsView.setImageDrawable(getResources().getDrawable(R.drawable.llbt_ima_kline));
                    mCharsView.setImageDrawable(getResources().getDrawable(R.drawable.llbt_ima_zoushi));
                    ll_maValue.setVisibility(View.VISIBLE);
                    ll_kline_info.setVisibility(View.VISIBLE);
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

    ImageView mCharsView;

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
        mECharsView.setVisibility(mShowType == 2 ? View.VISIBLE : View.GONE);
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

    /** 设置趋势图等折线图数据 */
    public void setECharsViewData(IECharsData... data){
        mTimerRefreshTools.startTimer();
        mECharsView.setData(data);
//        BocTrendView.TrendVo item ;
//        List<BocTrendView.TrendVo> list = new ArrayList<BocTrendView.TrendVo>();
//        for(int i = 0; i < data[0].getXList().size();i++){
//            item = new BocTrendView.TrendVo();
//            item.setDate(data[0].getXList().get(i));
//            item.setPrice(data[0].getYList().get(i));
//            list.add(item);
//        }
//
//        mBocTrendView.update(list);
    }

  //重载
    public void setECharsViewData(ECharsType type, IECharsData... data){
        //mTimerRefreshTools.startTimer();
        //折线图类型
        mECharsView.setECharsType(type);
        setECharsViewData(data);
    }

    /** 当前选中项 */
    protected int mCurIndex;
    /** 设置当前选中项
     * 此方法必须要在注册数据刷新之后
     * */
    public void setCurSelectedIndex(int index){
        mCurIndex = index;
        mTabView.setCurSelectedIndex(mCurIndex);
    }
    /** 设置数据刷新的按钮文字，每个按钮文字之间用“;”隔开 */
    public void setContentText(String contentText){
        mTabView.setContentText(contentText);
        mTabView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChanged(TabButton tb) {
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
        entity = (OHLCEntity) maslipcandlestickchart.getStickData().get(mStickList.size() - 1);
//        getPosition(maslipcandlestickchart.getDisplayTo() - 1);
        setTVData(entity,position);
    }

    @Override
    public void getPosition(int position) {
        OHLCEntity entitBefore;
        if (mStickList.size() > 1) {
            entity = (OHLCEntity) maslipcandlestickchart.getStickData().get(position);
            if (position > 0) {
                entitBefore = (OHLCEntity) maslipcandlestickchart.getStickData().get(position - 1);
//                setTextViewColor(entity, entitBefore);
                setTextViewColor(getResources().getColor(R.color.white));
            }
            setTVData(entity,position);



        }
    }

    @Override
    public void onDoubleClick(View view) {
        Toast.makeText(mContext, "双击...", Toast.LENGTH_SHORT).show();

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
        closePrice.setText(isNullOrEmpty(entity) ? "" : String.valueOf(entity.getClose()));
        maxPrice.setText(isNullOrEmpty(entity) ? "" : String.valueOf(entity.getHigh()));
        minPrice.setText(isNullOrEmpty(entity) ? "" : String.valueOf(entity.getLow()));
        openPrice.setText(isNullOrEmpty(entity) ? "" : String.valueOf(entity.getOpen()));
        String year = isNullOrEmpty(entity) ? null : entity.getDate().trim();
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

    private boolean isNullOrEmpty(Object obj) {
        if(obj == null) {
            return true;
        } else if(obj instanceof String) {
            return ((String)obj).length() == 0;
        } else if(obj instanceof CharSequence) {
            return ((CharSequence)obj).length() == 0;
        } else if(obj instanceof Collection) {
            return ((Collection)obj).isEmpty();
        } else if(obj instanceof Map) {
            return ((Map)obj).isEmpty();
        } else if(!(obj instanceof Object[])) {
            return false;
        } else {
            Object[] object = (Object[])obj;
            boolean empty = true;

            for(int i = 0; i < object.length; ++i) {
                if(!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }

            return empty;
        }
    }

    private void setTextViewColor(OHLCEntity entity, OHLCEntity entitBefore) {
        setTextColor(openPrice,entitBefore.getClose() < entity.getOpen(), Color.RED,maslipcandlestickchart.getNegativeStickFillColor());
        setTextColor(closePrice,entity.getClose() < entity.getOpen(), Color.RED,maslipcandlestickchart.getNegativeStickFillColor());
        setTextColor(maxPrice,entity.getHigh() < entity.getOpen(), Color.RED,maslipcandlestickchart.getNegativeStickFillColor());
        setTextColor(minPrice,entity.getLow() < entity.getOpen(), Color.RED,maslipcandlestickchart.getNegativeStickFillColor());

//        if (maslipcandlestickchart.getTheme().equals(MASlipCandleStickChart.WHITETHEME)) {
//            openPrice.setTextColor(
//                    (entitBefore.getClose() < entity.getOpen()) ? Color.RED : Color.parseColor("#2c7008"));
//            closePrice.setTextColor((entity.getClose() >= entity.getOpen()) ? Color.RED : Color.parseColor("#2c7008"));
//            maxPrice.setTextColor((entity.getHigh() >= entity.getOpen()) ? Color.RED : Color.parseColor("#2c7008"));
//            minPrice.setTextColor((entity.getLow() >= entity.getOpen()) ? Color.RED : Color.parseColor("#2c7008"));
//        } else {
//            setTextColor(openPrice,entitBefore.getClose() < entity.getOpen(), Color.RED,maslipcandlestickchart.getNegativeStickFillColor());
//            setTextColor(closePrice,entity.getClose() < entity.getOpen(), Color.RED,maslipcandlestickchart.getNegativeStickFillColor());
//            setTextColor(maxPrice,entity.getHigh() < entity.getOpen(), Color.RED,maslipcandlestickchart.getNegativeStickFillColor());
//            setTextColor(minPrice,entity.getLow() < entity.getOpen(), Color.RED,maslipcandlestickchart.getNegativeStickFillColor());
//        }

    }

    private void setTextViewColor(int resid){
        openPrice.setTextColor(resid);
        closePrice.setTextColor(resid);
        maxPrice.setTextColor(resid);
        minPrice.setTextColor(resid);
    }

    private void setTextColor(TextView v, boolean flag, int color1, int color2){
        v.setTextColor(mContext.getResources().getColor(R.color.boc_common_bg_color));
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
        return (float) Math.round(average*10000)/10000;
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
    }

}
