package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.module;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by zcy7065 on 2016/11/8.
 */
public class MyScrollView extends ScrollView {

    /**
     * 当用户手指离开view时，view可能还在继续滑动，用来保存Y距离并做比较
     * */
    private int lastScrollY;
    /**
     * 持有一个OnScrollListener的实例
     * */

    private OnScrollListener onScrollListener;

    /**
     * MyScrollView的构造函数
     * */

    public MyScrollView(Context context){
        this(context,null);
    }

    public MyScrollView(Context context , AttributeSet attrs){
        this(context,attrs,0);
    }

    public MyScrollView(Context context , AttributeSet attrs , int defStyle){
        super(context , attrs , defStyle);
    }


    /**
     *设置滚动接口
     * */
    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }

    /**
     *当用户手指离开view时，获取view的滑动距离，回调给onScroll方法
     * */

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg){
            int scrollY = MyScrollView.this.getScrollY();

            /**
             * 相当于每隔5毫秒做一次比较，确定view已经停止滑动
             * */
            if(lastScrollY != scrollY){
                lastScrollY = scrollY;
                //obtainMessage（）
                handler.sendMessageDelayed(handler.obtainMessage(),5);
            }
            if(onScrollListener != null){
                onScrollListener.onScroll(scrollY);
            }
        }

    };

    /**
     *重写了onTouchEvent方法，当用户的手在view上时，将其滑动距离Y直接回调给
     * OnScrollListener接口的onScroll函数，当用户手离开时，延迟5毫秒给handler
     * 发送消息，在handler中处理view的滑动距离
     * */

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        if(onScrollListener != null){
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        switch(ev.getAction()){
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(),5);
                break;
        }
        return super.onTouchEvent(ev);

    }


    public interface OnScrollListener{

        public void onScroll(int scrollY);

    }

}
