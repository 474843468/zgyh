package com.chinamworld.llbt.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2016/10/24.
 */
public class TimerRefreshTools {

    public interface ITimerRefreshListener{
        void onRefresh();
    }

    public TimerRefreshTools(long time, ITimerRefreshListener listener){
        mTime = time;
        mRefreshListener = listener;
    }
    private long mTime;

    private ITimerRefreshListener mRefreshListener;


    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Message msg = new Message();
                msg.what = 1;
                timeHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

   private Handler timeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // 刷新接口
            if(isStop == true) {
                return;
            }
            stopTimer();
//            startTimer();
            if(mRefreshListener != null){
                mRefreshListener.onRefresh();
            }

        }
    };

    public void startTimer(){
        stopTimer();
        isStop = false;
        timeHandler.postDelayed(timeRunnable,mTime);
    }

    private boolean isStop = false;
    public  void stopTimer(){
        isStop = true;
        timeHandler.removeCallbacks(timeRunnable);
    }
}
