package com.boc.bocsoft.mobile.bocmobile.base.widget.PullAndPushLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 可以进行下拉的布局，内部可以添加不可滚动的view
 * Created by gwluo on 2016/11/22.
 * setRefreshResult设置刷新结果
 * setRefreshListener设置刷新监听
 */

public class PullAndPushLayout extends LinearLayout {
  private int refreshDrawable;
  private int loadingDrawable;
  private String defaultNotice;
  private String refreshingNotice;
  private String pullDownNotice;
  private String pullUpNotice;
  private String successNotice;
  private String failNotice;
  private Context mContext;

  public PullAndPushLayout(Context context) {
    this(context, null, 0);
  }

  public PullAndPushLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PullAndPushLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mContext = context;
    initDefault();
    TypedArray array = context.getTheme()
        .obtainStyledAttributes(attrs, R.styleable.PullAndPushLayout, defStyleAttr, 0);
    int indexCount = array.getIndexCount();
    for (int i = 0; i < indexCount; i++) {
      int attr = array.getIndex(i);
      if (attr == R.attr.refreshViewDrawable) {
        refreshDrawable = array.getResourceId(i, 0);
      } else if (attr == R.attr.loadingDrawable) {
        loadingDrawable = array.getResourceId(i, 0);
      } else if (attr == R.attr.defaultNotice) {
        defaultNotice = array.getString(i);
      } else if (attr == R.attr.refreshingNotice) {
        refreshingNotice = array.getString(i);
      } else if (attr == R.attr.pullNotice) {
        pullDownNotice = array.getString(i);
      } else if (attr == R.attr.pushNotice) {
        pullUpNotice = array.getString(i);
      } else if (attr == R.attr.successNotice) {
        successNotice = array.getString(i);
      } else if (attr == R.attr.failNotice) {
        failNotice = array.getString(i);
      }
    }
  }

  private View header;
  private TextView tv_head_title;

  private void initDefault() {
    setOrientation(VERTICAL);
    header = View.inflate(mContext, R.layout.boc_refresh_heder, null);
    tv_head_title = (TextView) header.findViewById(R.id.tv_head_title);
    header.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
      @Override public boolean onPreDraw() {
        getViewTreeObserver().removeOnPreDrawListener(this);
        setPadding(0, -header.getHeight(), 0, 0);
        return false;
      }
    });
    addView(header);
    refreshDrawable = R.drawable.ic_launcher;
    loadingDrawable = R.drawable.ic_launcher;
    defaultNotice = "下拉刷新";
    refreshingNotice = "正在刷新...";
    pullDownNotice = "松开刷新";
    pullUpNotice = "松开刷新";
    successNotice = "刷新成功";
    failNotice = "刷新失败";
  }

  private int downY = 0;    // 按下时y轴的偏移量
  private DisplayMode currentState = DisplayMode.Pull_Down;        // 默认设为松开刷新的状态

  @Override public boolean onTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        downY = (int) ev.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        if (currentState == DisplayMode.Refreshing) return false;

        if (downY == 0) downY = (int) ev.getY();

        int moveY = (int) ev.getY();

        int diff = moveY - downY;

        if (diff > header.getHeight()) diff = header.getHeight();

        int paddingTop = -header.getHeight() + diff;
        if (paddingTop == 0 && currentState == DisplayMode.Pull_Down) {        // 完全显示
          currentState = DisplayMode.Release_Reafresh;        // 置为松开刷新状态
        } else if (paddingTop < 0 && currentState == DisplayMode.Release_Reafresh) {    // 完全隐藏
          currentState = DisplayMode.Pull_Down;        // 置为下拉刷新的状态
        }
        refreshHeaderViewState();
        setPadding(0, paddingTop, 0, 0);
        //                requestLayout();
        return true;
      case MotionEvent.ACTION_UP:
        if (currentState == DisplayMode.Pull_Down) {// 当前是下拉刷新的状态, 执行隐藏头布局的操作
          setPadding(0, -header.getHeight(), 0, 0);
        } else if (currentState == DisplayMode.Release_Reafresh) {    // 当前是释放刷新的状态, 切换到正在刷新中的界面
          currentState = DisplayMode.Refreshing;        // 把当前状态改为正在刷新中
          refreshHeaderViewState();
          setPadding(0, 0, 0, 0);        // 切换到完全显示的状态
          //                    if(mOnRefreshListener != null) {
          //                        mOnRefreshListener.onPullDownRefresh();		// 调用回调事件
          //                    }
        }
        break;
    }
    return true;
  }

  /**
   * 根据currentState的值刷新头布局的状态
   */
  private void refreshHeaderViewState() {
    if (currentState == DisplayMode.Pull_Down) {
      // 下拉刷新状态
      //            ivArrow.startAnimation(downAnimation);
      tv_head_title.setText(defaultNotice);
    } else if (currentState == DisplayMode.Release_Reafresh) {
      // 释放刷新
      //            ivArrow.startAnimation(upAnimation);
      tv_head_title.setText(pullDownNotice);
    } else if (currentState == DisplayMode.Refreshing) {
      // 正在刷新中
      //            ivArrow.clearAnimation();
      //            ivArrow.setVisibility(View.GONE);
      //            mProgressBar.setVisibility(View.VISIBLE);
      tv_head_title.setText(refreshingNotice);
      if (refreshListener != null) {
        refreshListener.onPullDown();
      }
      //            tv_head_title.postDelayed(new Runnable() {
      //                @Override
      //                public void run() {
      //                    setRefreshResult(false);
      //                }
      //            }, 800);
    }
  }

  /**
   * 设置刷新结果
   *
   * @param freshState true刷新成功，false 失败
   */
  public void setRefreshResult(boolean freshState) {
    if (freshState) {
      tv_head_title.setText(successNotice);
    } else {
      tv_head_title.setText(failNotice);
    }
    tv_head_title.postDelayed(new Runnable() {
      @Override public void run() {
        setPadding(0, -header.getHeight(), 0, 0);
        tv_head_title.setText(defaultNotice);
        currentState = DisplayMode.Pull_Down;
      }
    }, 300);
  }

  /**
   * 头布局显示的几种状态
   */
  public enum DisplayMode {
    Pull_Down,        // 松开刷新的状态
    Release_Reafresh,    // 释放刷新
    Refreshing        // 正在刷新中
  }

  private RefreshListener refreshListener;

  /**
   * 设置刷新监听
   */
  public void setRefreshListener(RefreshListener listener) {
    refreshListener = listener;
  }

  public interface RefreshListener {
    void onPullDown();
  }
}
