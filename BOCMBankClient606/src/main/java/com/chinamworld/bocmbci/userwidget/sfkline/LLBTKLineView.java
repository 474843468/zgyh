package com.chinamworld.bocmbci.userwidget.sfkline;

import android.content.Context;
import android.util.AttributeSet;

import com.chinamworld.llbt.userwidget.tabview.TabButton;

/**
 * Created by Administrator on 2016/10/25.
 */
public class LLBTKLineView extends SFKLineView {
    public LLBTKLineView(Context context) {
        super(context);
        init();
    }

    public LLBTKLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LLBTKLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setQuShiData("yyyy\nMM-dd","yyyy-MM-dd HH:mm:ss","参考价格：","时间：","参考价格",2,"yyyy-MM-dd HH:mm:ss","暂无数据");
    }

    @Override
    public void onChanged(TabButton tb) {
        super.onChanged(tb);
        if(mCurIndex == 0 || mCurIndex == 1){
            setQuShiData("yyyy\nMM-dd\nHH:mm:ss","yyyy-MM-dd HH:mm:ss","参考价格：","时间：","参考价格",2,"yyyy-MM-dd HH:mm:ss","暂无数据");
        }else{
            init();
        }
    }

    /**  四方K线图数据 */
    private BaseSFKLineData[] mSFKLineDataArray = new BaseSFKLineData[50];

    /** 获得当前的K线图数据 */
    protected BaseSFKLineData getCurKLineData(){
        return mSFKLineDataArray[mCurIndex];
    }


    /** 设置K线图数据 */
    @Override
    public void setKLineData(BaseSFKLineData data){
        if(mSFKLineDataArray[mCurIndex] == null)
            mSFKLineDataArray[mCurIndex] = data;
        else {
            mSFKLineDataArray[mCurIndex].add(data);
        }
        super.setKLineData(mSFKLineDataArray[mCurIndex]);
        if(mKLineDataChanged != null) {
            mKLineDataChanged.onDataChange();
        }
    }

    /** 获得最新的更新时间区间  */
    public String getNewTimeZone(){
        String timeZone = "";
        if(mCurIndex != -1 && mCurIndex < mSFKLineDataArray.length){
            if(mSFKLineDataArray[mCurIndex] != null){
                timeZone = mSFKLineDataArray[mCurIndex].getNewTimeZone();
            }
        }
        return timeZone;
    }

    private IKLineDataChangedListener mKLineDataChanged;
    protected void setKLineDataChanged(IKLineDataChangedListener listener){
        mKLineDataChanged = listener;
    }

    interface IKLineDataChangedListener{
        void onDataChange();
    }

    @Override
    public void resetData() {
        for(int i = 0; i < mSFKLineDataArray.length;i++){
            mSFKLineDataArray[i] = null;
        }
        super.resetData();
    }

}
