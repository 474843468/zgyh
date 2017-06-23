package com.chinamworld.llbt.userwidget.refreshliseview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.llbtwidget.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 上拉刷新布局
 */
public class PullToRefreshLayout extends BaseFrameLayout {



    public static final String TAG = "PullToRefreshLayout";
    // 初始状态
    public static final int INIT = 0;
    // 释放加载
    public static final int RELEASE_TO_LOAD = 1;
    // 正在加载
    public static final int LOADING = 2;
    // 操作完毕
    public static final int DONE = 3;
    // 当前状态
    private int state = INIT;
    // 加载回调接口
    private IRefreshLayoutListener mListener;
    // 加载成功
    public static final int SUCCEED = 0;
    // 加载失败
    public static final int FAIL = 1;
    // 没有更多内容
    public static final int NO_MORE_DATA = 2;
    // 按下Y坐标，上一个事件点Y坐标
    private float downY, lastY;
    // 上拉的距离
    private float pullUpY = 0;
    // 释放加载的距离
    private float loadmoreDist = 200;
    private MyTimer timer;
    // 回滚速度
    public float MOVE_SPEED = 8;
    // 第一次执行布局
    private boolean isLayout = false;
    // 在加载过程中滑动操作
    private boolean isTouch = false;
    // 手指滑动距离与上拉头的滑动距离比，中间会随正切函数变化
    private float radio = 2;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;
    // 箭头翻转动画
    private RotateAnimation reverseAnimation;
    // 上拉头
    private View loadmoreView;
    // 上拉的箭头
    private View pullUpView;
    // 正在加载的图标
    private View loadingView;
    // 加载结果：成功或失败
    private TextView loadStateTextView;
    private ImageView loadingResultView;
    // 实现了Pullable接口的View
    private PullableListView pullableView;
    // 过滤多点触碰
    private int mEvents;
    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean canPullDown = true;
    private boolean canPullUp = true;
    private boolean isMove = true;


    protected BaseAdapter baseAdapter;
    /**
     * 执行自动回滚的handler
     */
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // 回弹速度随下拉距离moveDeltaY增大而增大
            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (Math.abs(pullUpY))));
            if (!isTouch && state == LOADING && -pullUpY <= loadmoreDist) {
                // 正在加载，且没有往上推的话则悬停，显示"正在刷新..."
                pullUpY = -loadmoreDist;
                timer.cancel();
            } else {
                //每次回弹MOVE_SPEED个单位
                if (pullUpY < 0) {
                    pullUpY += MOVE_SPEED;
                }

                // 已完成回弹
                if (pullUpY > 0) {
                    pullUpY = 0;
                    pullUpView.clearAnimation();
                    // 隐藏上拉头时有可能还在加载，只有当前状态不是正在加载时才改变状态
                    if (state != LOADING) {
                        changeState(INIT);
                    }
                    timer.cancel();
                }
            }
            // 刷新布局,会自动调用onLayout
            requestLayout();
        }
    };

    public void setOnRefreshListener(IRefreshLayoutListener listener) {
        mListener = listener;
    }

    public PullToRefreshLayout(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);

    }

    public void initListView(BaseAdapter adapter){
        baseAdapter = adapter;
        pullableView.setAdapter(adapter);
    }
    private void initView(Context context) {
        pullableView = new PullableListView(context);
        super.addView(pullableView);
        loadmoreView = LayoutInflater.from(context).inflate(R.layout.llbt_load_more,null);
        super.addView(loadmoreView);

        initView();
        timer = new MyTimer(updateHandler);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.boc_rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);

        reverseAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.boc_reverse_anim);
    }

    private void hidePullUpView() {
        timer.schedule(2);
    }

    private void changeState(int to) {
        state = to;
        switch (state) {
            case INIT:
                // 上拉布局初始状态
//                loadStateTextView.setText("上拉加载");
                loadStateTextView.setText("松开即可加载");
                pullUpView.clearAnimation();
//                pullUpView.setVisibility(View.VISIBLE);
                pullUpView.setVisibility(View.GONE);
                loadingResultView.setVisibility(View.GONE);
                break;
            case RELEASE_TO_LOAD:
                // 释放加载状态
                pullUpView.startAnimation(reverseAnimation);
//                pullUpView.setVisibility(View.GONE);
                loadStateTextView.setText("松开即可加载");
                break;
            case LOADING:
                // 正在加载状态
                pullUpView.clearAnimation();
//                pullUpView.setVisibility(View.INVISIBLE);
                pullUpView.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                loadingView.startAnimation(refreshingAnimation);
                loadStateTextView.setText("加载中…");
                // 加载操作
                if (mListener != null) {
                    mListener.onLoadMore(this);
                }
                break;
            case DONE:
                hidePullUpView();
                break;
        }
    }

    public int getLoadState() {
        return state;
    }

    /**
     * 加载完毕，显示加载结果。加载完成后一定要调用这个方法
     *
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void loadmoreCompleted(RefreshDataStatus refreshResult) {
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);
        pullUpView.clearAnimation();
//        pullUpView.setVisibility(View.INVISIBLE);
        pullUpView.setVisibility(View.GONE);
        if(refreshResult == RefreshDataStatus.Successed){
            // 加载成功
//            loadStateTextView.setText("加载成功");
//            loadingResultView.setBackgroundResource(R.drawable.boc_load_succeed);
        }
        else if(refreshResult == RefreshDataStatus.Failed){
            // 加载失败
            loadStateTextView.setText("网络不给力，请重试");
//            loadingResultView.setBackgroundResource(R.drawable.boc_load_failed);
        }
        else if(refreshResult == RefreshDataStatus.NoMoreData){
//            loadingResultView.setBackgroundResource(R.drawable.boc_load_failed);
            loadStateTextView.setText("没有更多内容");
        }
//        loadingResultView.setVisibility(View.VISIBLE);
        loadingResultView.setVisibility(View.GONE);
        // 刷新结果停留1秒
        postDelayed(new Runnable() {
            @Override
            public void run() {
                changeState(DONE);
            }
        }, 1000);
    }

    /**
     * 不限制上拉
     */
    private void releasePull() {
        canPullUp = true;
    }

    /*
     * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                lastY = downY;
                timer.cancel();
                mEvents = 0;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if(!isMove){
                    return false;
                }
                if (mEvents == 0) {
                    //liuzc  160721 修改listview超过一屏时向下推动失败
//                    if (((Pullable) pullableView).canPullUp() && canPullUp) {
//                        pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                    if ((((IPullable) pullableView).canPull() && canPullUp) ||
                            (ev.getY() > lastY && canPullUp)) {
                        if (ev.getY() > lastY) {
                            pullUpY = pullUpY + (ev.getY() - lastY); //向下移动速度同手指移动一致
                        } else {
                            // 可以上拉，正在下拉时不能上拉
                            pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                        }
                        if (pullUpY > 0) {
                            pullUpY = 0;
                            canPullDown = true;
                            canPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight()) {
                            pullUpY = -getMeasuredHeight();
                        }
                        if (state == LOADING) {
                            // 正在加载的时候触摸移动
                            isTouch = true;
                        }
                    } else {
                        releasePull();
                    }
                } else {
                    mEvents = 0;
                }
                lastY = ev.getY();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (Math.abs(pullUpY))));
                requestLayout();
                // 下面是判断上拉加载的，同上，注意pullUpY是负值
                if (-pullUpY <= loadmoreDist && state == RELEASE_TO_LOAD) {
                    changeState(INIT);
                }
                if (-pullUpY >= loadmoreDist && state == INIT) {
                    changeState(RELEASE_TO_LOAD);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (-pullUpY > loadmoreDist) {
                    // 正在加载时往上拉，释放后上拉头不隐藏
                    isTouch = false;
                }
                if (state == RELEASE_TO_LOAD) {
                    changeState(LOADING);
                }
                hidePullUpView();
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initView() {
        // 初始化上拉布局
        pullUpView = loadmoreView.findViewById(R.id.pullup_icon);
        loadStateTextView = (TextView) loadmoreView.findViewById(R.id.loadstate_tv);
        loadingView = loadmoreView.findViewById(R.id.loading_icon);
        loadingResultView = (ImageView) loadmoreView.findViewById(R.id.result_icon);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isLayout) {
            // 这里是第一次进来的时候做一些初始化
//            pullableView = getChildAt(0);
//            loadmoreView = getChildAt(1);
            isLayout = true;
//            initView();
            loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0).getMeasuredHeight();
        }
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
        pullableView.layout(0, (int) pullUpY, pullableView.getMeasuredWidth(), (int) pullUpY + pullableView.getMeasuredHeight());
        //liuzc 160721 修正loadmoreView的高度，保证能覆盖到屏幕底部
        loadmoreView.layout(0, (int) pullUpY + pullableView.getMeasuredHeight(), loadmoreView.getMeasuredWidth(),
                (int) this.getMeasuredHeight());

    }

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask();
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timer != null) {
            timer.cancel();
        }
    }

    /***/
    public void setMove(boolean move) {
        isMove = move;
    }

    @Override
    public void addView(View child) {
        super.addView(child,getChildCount() == 0 ? 0 : getChildCount() - 1);
    }

    /**
     * 设置隐藏控件的滚动条隐藏方法
     */
    public void setScrollBars(){
        if(pullableView == null) return;
        pullableView.setScrollBars();
    }


}
