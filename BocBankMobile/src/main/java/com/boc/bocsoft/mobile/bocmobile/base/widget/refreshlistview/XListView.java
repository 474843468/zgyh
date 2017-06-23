package com.boc.bocsoft.mobile.bocmobile.base.widget.refreshlistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.boc.bocsoft.mobile.bocmobile.R;


public class XListView extends ListView implements OnScrollListener {

    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private IXListViewListener mListViewListener;
    private IXListViewListener2 mListViewListener2;

    // -- header view
    private XListViewHeader mHeaderView;
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    // private TextView mHeaderTimeView;
    private int mHeaderViewHeight; // header view's height
    private boolean mEnablePullRefresh = false;
    private boolean mPullRefreshing = false; // is refreashing.

    // -- footer view
    private XListViewFooter mFooterView;
    private boolean mEnablePullLoad;
    private boolean mPullLoading;
    private boolean mIsFooterReady = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400; // scroll back duration
    private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
    // feature.
    private static final int MINPAGESIZE = 5;

    private InnerAdapter miAdapter;
    private OnItemClickListener mListener;

    /**
     * @param context
     */
    public XListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new XListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView
                .findViewById(R.id.xlistview_header_content);
        mHeaderViewContent.setVisibility(View.INVISIBLE);
        // mHeaderTimeView = (TextView) mHeaderView
        // .findViewById(R.id.xlistview_header_time);
        addHeaderView(mHeaderView);

        // init footer view
        mFooterView = new XListViewFooter(context);

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        mHeaderViewHeight = mHeaderViewContent.getHeight();
                        getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                }
        );
        miAdapter = new InnerAdapter();

        super.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener == null) {
                    return;
                }
                ListAdapter adapter = getAdapter();
                if (adapter != null && adapter.getCount() > 2) {
                    if (position != 0 || position != adapter.getCount() - 1) {
                        mListener.onItemClick(parent, view, position - 1, id);
                    }
                }
            }
        });
        setVerticalFadingEdgeEnabled(false);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // make sure XListViewFooter is the last footer view, and only add once.
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(mFooterView);
            mFooterView.hide();
        }
        miAdapter.setAdapter(adapter);
        super.setAdapter(miAdapter);
        //super.setAdapter(adapter);
    }

    /**
     * enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = false;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.hide();
//			mFooterView.setOnClickListener(null);
        } else {
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            // mFooterView.setOnClickListener(new OnClickListener() {
            // @Override
            // public void onClick(View v) {
            // startLoadMore();
            // }
            // });
        }
    }

    /**
     * 刷新或加载状态还原
     */
    public void endRefresh(boolean isSuccess,boolean isLoadMore) {
        stopLoadMore(isSuccess,isLoadMore);
        stopRefresh(isSuccess);
    }
//    /**
//     * 刷新或加载状态还原
//     */
//    public void endRefresh(boolean isSuccess) {
//    	stopLoadMore(isSuccess);
//    	stopRefresh(isSuccess);
//    }

    public void endRefresh() {
        endRefresh(true,true);
    }

    /**
     * 刷新完成   还原状态
     */
    public void stopRefresh() {
        stopRefresh(true);
    }

    /**
     * 刷新完成   还原状态
     *
     * @param flag true  刷新成功	false	刷新失败
     */
    public void stopRefresh(boolean flag) {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            mHeaderView.onStopRefresh(flag);
            resetHeaderHeight();
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        stopLoadMore(true,true);
    }

    /**
     * stop load more, reset footer view.
     *
     * @param flag true  加载成功	false	加载失败
     */
    public void stopLoadMore(boolean flag,boolean isLaodMore) {
        resetFooterHeight();
        //去掉外部隐藏
//        mFooterView.hide();
        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.onStopLoadMore(flag);
            //如果true，正常显示加载成功 false 没有更多数据
            if(isLaodMore){
                mFooterView.setState(XListViewFooter.STATE_NORMAL);
            }else{
            	mFooterView.setState(XListViewFooter.STATE_NO_MORE);
            }
        }else{
        	 mPullLoading = false;
             mFooterView.onStopLoadMore(flag);
             mFooterView.setState(XListViewFooter.STATE_NORMAL);
        }
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        // mHeaderTimeView.setText(time);
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta
                + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                mHeaderView.setState(XListViewHeader.STATE_READY);
            } else {
                mHeaderView.setState(XListViewHeader.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height,
                SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    @Override
    public void setEmptyView(View emptyView) {
        if (getEmptyView() == null && emptyView != null
                && emptyView.getParent() == null) {
            ViewGroup parent = (ViewGroup) getParent();
            int childCount = parent.getChildCount();
            parent.addView(emptyView, childCount);
        }
        super.setEmptyView(emptyView);
    }

    /**
     * 根据现有逻辑 有两种空页面，空页面支持下拉刷新
     * 1.无结果空页面，顶部显示
     * 2.报错空页面，居中显示
     * <p/>
     * 需求：空页面在一开始不显示，在请求结束或者出错时在再显示空白页面
     * 因此空白页面的显示和隐藏交由外部处理
     */
    private FrameLayout frameLayout;
    private View notResultView;
    private View errView;
    private boolean isShowNoResultView = false;//是否显示无结果页面
    private boolean isShowErrorView = false;//是否显示错误页面

    private void setEmptyView(View notResultView, View errView) {
        this.notResultView = notResultView;
        this.errView = errView;
        if (frameLayout == null) {
            frameLayout = new FrameLayout(getContext());
            frameLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
        if (mLayoutParams == null) {
            mLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }

        //添加
        if (notResultView != null) {
            ViewParent vp = notResultView.getParent();
            if (vp instanceof ViewGroup) {
                ((ViewGroup) vp).removeView(notResultView);
            }
            notResultView.setVisibility(View.GONE);
            frameLayout.addView(notResultView, mLayoutParams);
        }

        if (errView != null) {
            ViewParent vp = errView.getParent();
            if (vp instanceof ViewGroup) {
                ((ViewGroup) vp).removeView(errView);
            }
            frameLayout.addView(errView, mLayoutParams);
            errView.setVisibility(View.GONE);
        }

    }

    public void dismissEmptyView() {
        isShowErrorView = false;
        isShowNoResultView = false;
        miAdapter.notifyDataSetChanged();
    }

    /**
     * 显示无结果view
     * 前提是adapter无数据
     */
    private void setNoResultViewVisable(boolean isVis) {
        isShowNoResultView = isVis;
        miAdapter.notifyDataSetChanged();
    }

    /**
     * 显示错误信息view
     * 前提是adapter无数据
     */
    private void setErrorViewVisiable(boolean isVis) {
        isShowErrorView = isVis;
        miAdapter.notifyDataSetChanged();
    }

    /*	@Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if(frameLayout != null){
                android.view.ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
                layoutParams.height = b -t-20;
                frameLayout.setLayoutParams(layoutParams);
                frameLayout.requestLayout();
                //requestFocus();
            }
        }
        */
    FrameLayout.LayoutParams mLayoutParams;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mLayoutParams == null) {
            mLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        mLayoutParams.topMargin = h / 2;
        if (errView != null) {
            errView.setLayoutParams(mLayoutParams);
        }
        if (notResultView != null) {
            notResultView.setLayoutParams(mLayoutParams);
        }
        /*if(frameLayout != null){
			android.view.ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
			layoutParams.height =h-20;
			frameLayout.setLayoutParams(layoutParams);
			requestLayout();
		}*/
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;
        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke
                // loadmore.
                mFooterView.setState(XListViewFooter.STATE_READY);
            } else {
                mFooterView.setState(XListViewFooter.STATE_NORMAL);
            }
            startLoadMore();
        }
        mFooterView.setBottomMargin(height);

        // setSelection(mTotalItemCount - 1); // scroll to bottom
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
                    SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore() {
        mPullLoading = true;
        mFooterView.setState(XListViewFooter.STATE_LOADING);
        if (mListViewListener != null) {
            mListViewListener.onLoadMore();
        }
        if (mListViewListener2 != null) {
            mListViewListener2.onLoadMore(XListView.this);
        }
        dismissEmptyView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0
                        && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1
                        && (mFooterView.getBottomMargin() > 0 || deltaY < 0)
                        && getCount() > MINPAGESIZE) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    if (mEnablePullRefresh
                            && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
                        if (mListViewListener != null) {
                            mListViewListener.onRefresh();
                        }
                        if (mListViewListener2 != null) {
                            mListViewListener2.onRefresh(XListView.this);
                        }
                        dismissEmptyView();
                    }
                    resetHeaderHeight();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
                    // if (mEnablePullLoad
                    // && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                    // startLoadMore();
                    // }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

    public void setXListViewListener(IXListViewListener l) {
        mListViewListener = l;
    }

    public void setXListViewListener(IXListViewListener2 l2) {
        mListViewListener2 = l2;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IXListViewListener {
        public void onRefresh();

        public void onLoadMore();
    }

    public interface IXListViewListener2 {
        public void onRefresh(XListView xListView);

        public void onLoadMore(XListView xListView);
    }


    public void setFooterBackgroundRes(int resId) {
        if (mFooterView != null) {
            mFooterView.setBackgroundResource(resId);
        }
    }

    public void setFooterBackgroundColor(int resId) {
        if (mFooterView != null) {
            mFooterView.setBackgroundColor(resId);
            mFooterView.invalidate();
        }
    }

    /**
     * 是否处于加载更多的状态
     *
     * @return
     */
    public boolean isLoading() {
        return mPullLoading;
    }
    // public void showRefresh() {
    // mPullRefreshing = true;
    // mHeaderView.setVisiableHeight(mHeaderViewHeight);
    // mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
    // mListViewListener.onRefresh();
    // }

    private ListAdapter mOutAdapter;

    private class InnerAdapter extends BaseAdapter {
        private boolean isEmpty;
        private DataSetObserver mDataSetObserver;

        public void setAdapter(ListAdapter mAdapter) {
            if (mOutAdapter != null) {
                mOutAdapter.unregisterDataSetObserver(mDataSetObserver);
            }
            mOutAdapter = mAdapter;
            mDataSetObserver = new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    miAdapter.notifyDataSetChanged();
                }

                @Override
                public void onInvalidated() {
                    super.onInvalidated();

                    miAdapter.notifyDataSetInvalidated();
                }
            };
            mOutAdapter.registerDataSetObserver(mDataSetObserver);
        }

        @Override
        public int getCount() {
            int count = isShowEmpty() ? 1 : mOutAdapter == null ? 0 : mOutAdapter.getCount();
            return count;
        }

        @Override
        public Object getItem(int position) {
            return isShowEmpty() ? "" : mOutAdapter.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return isShowEmpty() ? 1 : mOutAdapter.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (isShowEmpty()) {
                if (isShowErrorView) {
                    changEmptyViewShow(ShowMode.ERROR_SHOW);
                } else if (isShowNoResultView) {
                    changEmptyViewShow(ShowMode.NORESULT_SHOW);
                } else {
                    changEmptyViewShow(ShowMode.NONE_SHOW);
                }
                return frameLayout;
            }
            return mOutAdapter.getView(position, convertView, parent);
        }

        @Override
        public int getItemViewType(int position) {
            //emptyView type类型为最后一个
            int type = 0;
            if (isShowEmpty()) {
                if (isShowNoResultView || isShowErrorView) {
                    type = getViewTypeCount() - 1;
                }
            } else {
                type = mOutAdapter == null ? -1 : mOutAdapter.getItemViewType(position);
            }
            return type;
        }

        @Override
        public int getViewTypeCount() {
            int count = mOutAdapter == null ? 1 : mOutAdapter.getViewTypeCount() + 1;
            return count;
        }

        @Override
        public void notifyDataSetInvalidated() {
            isEmpty = mOutAdapter.isEmpty();
            super.notifyDataSetInvalidated();
        }

        @Override
        public void notifyDataSetChanged() {
            isEmpty = mOutAdapter == null || mOutAdapter.isEmpty();
            super.notifyDataSetChanged();
        }

        private boolean isShowEmpty() {
            return isEmpty && (isShowErrorView || isShowNoResultView);
        }

    }

    private enum ShowMode {NORESULT_SHOW, ERROR_SHOW, NONE_SHOW}

    ;

    private void changEmptyViewShow(ShowMode mode) {
        switch (mode) {
            case NORESULT_SHOW:
                setVisiable(notResultView, true);
                setVisiable(errView, false);
                break;
            case ERROR_SHOW:
                setVisiable(notResultView, false);
                setVisiable(errView, true);
                break;
            case NONE_SHOW:
                setVisiable(notResultView, false);
                setVisiable(errView, false);
                break;
            default:
                break;
        }
    }

    private void setVisiable(View view, boolean isvis) {
        if (view == null) {
            return;
        }
        view.setVisibility(isvis ? View.VISIBLE : View.GONE);
    }
}
