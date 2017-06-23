package com.chinamworld.bocmbci.userwidget.sfkline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 注意：该控件支持横竖屏切换，需要再使用该控件的Activity
 *      在manifest 文件里注册时必须配置以下信息
 *      android:configChanges="orientation|keyboardHidden|screenSize"
 * Created by Administrator on 2016/10/27.
 */
public class InverstKLineView extends LLBTKLineView implements LLBTKLineView.IKLineDataChangedListener {



    Context mContext;
    TextView mDate,mTime,mOpenPrice,mMaxPrice,mMinPrice;

    public InverstKLineView(Context context){
        super(context);
        initView(context);
    }

    public InverstKLineView(Context context, AttributeSet attrs){
        super(context,attrs);
        initView(context);
    }

    public InverstKLineView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.invert_klineview_layout,this,false);
        getKLineTitle().addView(view);
        mDate = (TextView) view.findViewById(R.id.date);
        mTime = (TextView) view.findViewById(R.id.time_two);
        mOpenPrice = (TextView) view.findViewById(R.id.openPrice_two);
        mMaxPrice = (TextView) view.findViewById(R.id.maxPrice_two);
        mMinPrice = (TextView) view.findViewById(R.id.minPrice_two);

        setKLineDataChanged(this);
    }

    @Override
    public void onDataChange() {
        BaseSFKLineData data = getCurKLineData();
        if(data == null || data.getOHLCList() == null || data.getOHLCList().size() <= 0)
            return;
        OHLCItem item = data.getOHLCList().get(data.getOHLCList().size() - 1);
//        mOpenPrice.setText(String.valueOf(item.getOpen()));
//        mMaxPrice.setText(String.valueOf(item.getHigh()));
//        mMinPrice.setText(String.valueOf(item.getLow()));
//        String time = item.getTimeStamp();
//        setmDateTime(time);

    }

    /**
     * 代码设置年月日 时分秒
     * @param s  年月日 时分秒
     */
    public void setDateTimeText(String s){
        if(StringUtil.isNullOrEmpty(s))
            return;
        setmDateTime(s);

    }

    /**
     * 设置价格、日期时间等信息
     * @param item
     */
    public void setPriceAndTimeText(OHLCItem item){
        if(StringUtil.isNullOrEmpty(item)) return;
        mOpenPrice.setText(String.valueOf(item.getOpen()));
        mMaxPrice.setText(String.valueOf(item.getHigh()));
        mMinPrice.setText(String.valueOf(item.getLow()));
        String time = item.getTimeStamp();
        setmDateTime(time);
    }


    /**
     * 设置价格、日期时间等信息
     */
    public void setPriceAndTimeText(String timeStamp,String openPrice,String highPrice,String lowPrice){

        mOpenPrice.setText(openPrice);
        mMaxPrice.setText(highPrice);
        mMinPrice.setText(lowPrice);

        setmDateTime(timeStamp);
    }

    /**
     * 格式化时分秒 赋值
     * @param time 年月日 时分秒
     */
    private void setmDateTime(String time){
        if(time == null ||time.length()<=0)
            return;
        String year = time.trim(),hour = null;
        if(time != null && time.length() > 9){
            year = time.substring(0,8).trim();
            hour = time.substring(8).trim(); //add by 修改截取是丢失数据
        }
        mDate.setVisibility(View.GONE);
        mTime.setVisibility(View.GONE);
        if(year != null && year.length() > 0){
            year = time.substring(0,4) + "/" + time.substring(4,6) + "/" + time.substring(6,8);
            mDate.setText(year);
            mDate.setVisibility(View.VISIBLE);
        }
        if(hour != null && hour.length() > 0){
            // add by luqp 格式化时分秒
            hour = time.substring(8,10) + ":" + time.substring(10,12) + ":" +time.substring(12,14);
            mTime.setText(hour);
            mTime.setVisibility(View.VISIBLE);
        }
    }


}
