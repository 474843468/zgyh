package com.chinamworld.bocmbci.biz.preciousmetal.goldstoretransquery;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.SectionIndexer;

import com.chinamworld.bocmbci.BuildConfig;

/**
 * 自定义控件 带悬浮标题的 ListView
 * Created by linyl on 2016/9/13.
 */
public class PinnedSectionListView extends ListView{

    Context m_context;

    public PinnedSectionListView (Context context){
        super(context);
        m_context = context;
        initView();
    }

    public PinnedSectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_context = context;
        initView();
    }

    public PinnedSectionListView(Context context, AttributeSet attrs,
                                        int defStyle) {
        super(context, attrs, defStyle);
        m_context = context;
        initView();
    }

    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private IXListViewListener mListViewListener;

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

    // -- inner classes

    /**
     * List adapter to be implemented for being used with PinnedSectionListView
     * adapter.
     */
    public static interface PinnedSectionListAdapter extends ListAdapter {
        /**
         * This method shall return 'true' if views of given type has to be
         * pinned.
         */
        boolean isItemViewTypePinned(int viewType);
    }

    /** Wrapper class for pinned section view and its position in the list. */
    static class PinnedSection {
        public View view;
        public int position;
        public long id;
    }

    // -- class fields

    // fields used for handling touch events
    private final Rect mTouchRect = new Rect();
    private final PointF mTouchPoint = new PointF();
    private int mTouchSlop;
    private View mTouchTarget;
    private MotionEvent mDownEvent;

    // fields used for drawing shadow under a pinned section
    private GradientDrawable mShadowDrawable;
    private int mSectionsDistanceY;
    private int mShadowHeight;

    /** Shadow for being recycled, can be null. */
    PinnedSection mRecycleSection;

    /** shadow instance with a pinned view, can be null. */
    PinnedSection mPinnedSection;

    /**
     * Pinned view Y-translation. We use it to stick pinned view to the next
     * section.
     */
    int mTranslateY;

    /** Scroll listener which does the magic */
    private final OnScrollListener mOnScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

            ListAdapter adapter = getAdapter();
            if (adapter == null || visibleItemCount == 0)
                return; // nothing to do

            final boolean isFirstVisibleItemSection = isItemViewTypePinned(
                    adapter, adapter.getItemViewType(firstVisibleItem));

            if (isFirstVisibleItemSection) {
                View sectionView = getChildAt(0);
                if (sectionView.getTop() == getPaddingTop()) { // view sticks to
                    destroyPinnedShadow();
                } else {
                    ensureShadowForPosition(firstVisibleItem, firstVisibleItem,
                            visibleItemCount);
                }

            } else {
                int sectionPosition = findCurrentSectionPosition(firstVisibleItem);
                if (sectionPosition > -1) { // we have section position
                    ensureShadowForPosition(sectionPosition, firstVisibleItem,
                            visibleItemCount);
                } else {
                    destroyPinnedShadow();
                }
            }
        };

    };

    /** Default change observer. */
    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            recreatePinnedShadow();
        };

        @Override
        public void onInvalidated() {
            recreatePinnedShadow();
        }
    };

    private void initView() {
        setOnScrollListener(mOnScrollListener);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        initShadow(true);
        mScroller = new Scroller(m_context, new DecelerateInterpolator());
        this.setDividerHeight(0);
        this.setVerticalScrollBarEnabled(false);
        this.setFastScrollEnabled(false);
    }

    public void setShadowVisible(boolean visible) {
        initShadow(visible);
        if (mPinnedSection != null) {
            View v = mPinnedSection.view;
            invalidate(v.getLeft(), v.getTop(), v.getRight(), v.getBottom()
                    + mShadowHeight);
        }
    }

    public void initShadow(boolean visible) {
        if (visible) {
            if (mShadowDrawable == null) {
                mShadowDrawable = new GradientDrawable(Orientation.TOP_BOTTOM,
                        new int[] { Color.parseColor("#ffa0a0a0"),
                                Color.parseColor("#50a0a0a0"),
                                Color.parseColor("#00a0a0a0") });
                mShadowHeight = (int) (8 * getResources().getDisplayMetrics().density);
            }
        } else {
            if (mShadowDrawable != null) {
                mShadowDrawable = null;
                mShadowHeight = 0;
            }
        }
    }

    /** Create shadow wrapper with a pinned view for a view at given position */
    void createPinnedShadow(int position) {

        // try to recycle shadow
        PinnedSection pinnedShadow = mRecycleSection;
        mRecycleSection = null;

        if (pinnedShadow == null)
            pinnedShadow = new PinnedSection();
        View pinnedView = getAdapter().getView(position, pinnedShadow.view,
                PinnedSectionListView.this);

        LayoutParams layoutParams = (LayoutParams) pinnedView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            pinnedView.setLayoutParams(layoutParams);
        }

        int heightMode = MeasureSpec.getMode(layoutParams.height);
        int heightSize = MeasureSpec.getSize(layoutParams.height);

        if (heightMode == MeasureSpec.UNSPECIFIED)
            heightMode = MeasureSpec.EXACTLY;

        int maxHeight = getHeight() - getListPaddingTop()
                - getListPaddingBottom();
        if (heightSize > maxHeight)
            heightSize = maxHeight;

        int ws = MeasureSpec.makeMeasureSpec(getWidth() - getListPaddingLeft()
                - getListPaddingRight(), MeasureSpec.EXACTLY);
        int hs = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        pinnedView.measure(ws, hs);
        pinnedView.layout(0, 0, pinnedView.getMeasuredWidth(),
                pinnedView.getMeasuredHeight());
        mTranslateY = 0;

        pinnedShadow.view = pinnedView;
        pinnedShadow.position = position;
        pinnedShadow.id = getAdapter().getItemId(position);

        mPinnedSection = pinnedShadow;
    }

    /** Destroy shadow wrapper for currently pinned view */
    void destroyPinnedShadow() {
        if (mPinnedSection != null) {
            mRecycleSection = mPinnedSection;
            mPinnedSection = null;
        }
    }

    /** Makes sure we have an actual pinned shadow for given position. */
    void ensureShadowForPosition(int sectionPosition, int firstVisibleItem,
                                 int visibleItemCount) {
        if (visibleItemCount < 2) {
            destroyPinnedShadow();
            return;
        }

        if (mPinnedSection != null
                && mPinnedSection.position != sectionPosition) {
            destroyPinnedShadow();
        }

        if (mPinnedSection == null) {
            createPinnedShadow(sectionPosition);
        }

        int nextPosition = sectionPosition + 1;
        if (nextPosition < getCount()) {
            int nextSectionPosition = findFirstVisibleSectionPosition(
                    nextPosition, visibleItemCount
                            - (nextPosition - firstVisibleItem));
            if (nextSectionPosition > -1) {
                View nextSectionView = getChildAt(nextSectionPosition
                        - firstVisibleItem);
                final int bottom = mPinnedSection.view.getBottom()
                        + getPaddingTop();
                mSectionsDistanceY = nextSectionView.getTop() - bottom;
                if (mSectionsDistanceY < 0) {
                    mTranslateY = mSectionsDistanceY;
                } else {
                    mTranslateY = 0;
                }
            } else {
                mTranslateY = 0;
                mSectionsDistanceY = Integer.MAX_VALUE;
            }
        }

    }

    int findFirstVisibleSectionPosition(int firstVisibleItem,
                                        int visibleItemCount) {
        ListAdapter adapter = getAdapter();

        if (firstVisibleItem + visibleItemCount >= adapter.getCount())
            return -1;

        for (int childIndex = 0; childIndex < visibleItemCount; childIndex++) {
            int position = firstVisibleItem + childIndex;
            int viewType = adapter.getItemViewType(position);
            if (isItemViewTypePinned(adapter, viewType))
                return position;
        }
        return -1;
    }

    int findCurrentSectionPosition(int fromPosition) {
        ListAdapter adapter = getAdapter();

        if (fromPosition >= adapter.getCount())
            return -1;

        if (adapter instanceof SectionIndexer) {
            SectionIndexer indexer = (SectionIndexer) adapter;
            int sectionPosition = indexer.getSectionForPosition(fromPosition);
            int itemPosition = indexer.getPositionForSection(sectionPosition);
            int typeView = adapter.getItemViewType(itemPosition);
            if (isItemViewTypePinned(adapter, typeView)) {
                return itemPosition;
            }
        }

        for (int position = fromPosition; position >= 0; position--) {
            int viewType = adapter.getItemViewType(position);
            if (isItemViewTypePinned(adapter, viewType))
                return position;
        }
        return -1;
    }

    void recreatePinnedShadow() {
        destroyPinnedShadow();
        ListAdapter adapter = getAdapter();
        if (adapter != null && adapter.getCount() > 0) {
            int firstVisiblePosition = getFirstVisiblePosition();
            int sectionPosition = findCurrentSectionPosition(firstVisiblePosition);
            if (sectionPosition == -1)
                return;
            ensureShadowForPosition(sectionPosition, firstVisiblePosition,
                    getLastVisiblePosition() - firstVisiblePosition);
        }
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        if (listener == mOnScrollListener) {
            super.setOnScrollListener(listener);
        } else {

        }
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        post(new Runnable() {
            @Override
            public void run() {
                recreatePinnedShadow();
            }
        });
    }

    @Override
    public void setAdapter(ListAdapter adapter) {

        if (BuildConfig.DEBUG && adapter != null) {
            if (!(adapter instanceof PinnedSectionListAdapter))
                throw new IllegalArgumentException(
                        "Does your adapter implement PinnedSectionListAdapter?");
            if (adapter.getViewTypeCount() < 2)
                throw new IllegalArgumentException(
                        "Does your adapter handle at least two types"
                                + " of views in getViewTypeCount() method: items and sections?");
        }

        ListAdapter oldAdapter = getAdapter();
        if (oldAdapter != null)
            oldAdapter.unregisterDataSetObserver(mDataSetObserver);
        if (adapter != null)
            adapter.registerDataSetObserver(mDataSetObserver);
        if (oldAdapter != adapter)
            destroyPinnedShadow();
        super.setAdapter(adapter);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mPinnedSection != null) {
            int parentWidth = r - l - getPaddingLeft() - getPaddingRight();
            int shadowWidth = mPinnedSection.view.getWidth();
            if (parentWidth != shadowWidth) {
                recreatePinnedShadow();
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mPinnedSection != null) {
            int pLeft = getListPaddingLeft();
            int pTop = getListPaddingTop();
            View view = mPinnedSection.view;
            canvas.save();
            int clipHeight = view.getHeight()
                    + (mShadowDrawable == null ? 0 : Math.min(mShadowHeight,
                    mSectionsDistanceY));
            canvas.clipRect(pLeft, pTop, pLeft + view.getWidth(), pTop
                    + clipHeight);

            canvas.translate(pLeft, pTop + mTranslateY);
            drawChild(canvas, mPinnedSection.view, getDrawingTime());

            if (mShadowDrawable != null && mSectionsDistanceY > 0) {
                mShadowDrawable.setBounds(mPinnedSection.view.getLeft(),
                        mPinnedSection.view.getBottom(),
                        mPinnedSection.view.getRight(),
                        mPinnedSection.view.getBottom() + mShadowHeight);
                mShadowDrawable.draw(canvas);
            }

            canvas.restore();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        final float x = ev.getX();
        final float y = ev.getY();
        final int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN && mTouchTarget == null
                && mPinnedSection != null
                && isPinnedViewTouched(mPinnedSection.view, x, y)) {
            mTouchTarget = mPinnedSection.view;
            mTouchPoint.x = x;
            mTouchPoint.y = y;

            mDownEvent = MotionEvent.obtain(ev);
        }

        if (mTouchTarget != null) {
            if (isPinnedViewTouched(mTouchTarget, x, y)) {
                mTouchTarget.dispatchTouchEvent(ev);
            }

            if (action == MotionEvent.ACTION_UP) {
                super.dispatchTouchEvent(ev);
                performPinnedItemClick();
                clearTouchTarget();

            } else if (action == MotionEvent.ACTION_CANCEL) {
                clearTouchTarget();

            } else if (action == MotionEvent.ACTION_MOVE) {
                if (Math.abs(y - mTouchPoint.y) > mTouchSlop) {

                    MotionEvent event = MotionEvent.obtain(ev);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    mTouchTarget.dispatchTouchEvent(event);
                    event.recycle();

                    super.dispatchTouchEvent(mDownEvent);
                    super.dispatchTouchEvent(ev);
                    clearTouchTarget();

                }
            }
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isPinnedViewTouched(View view, float x, float y) {
        view.getHitRect(mTouchRect);
        mTouchRect.top += mTranslateY;

        mTouchRect.bottom += mTranslateY + getPaddingTop();
        mTouchRect.left += getPaddingLeft();
        mTouchRect.right -= getPaddingRight();
        return mTouchRect.contains((int) x, (int) y);
    }

    private void clearTouchTarget() {
        mTouchTarget = null;
        if (mDownEvent != null) {
            mDownEvent.recycle();
            mDownEvent = null;
        }
    }

    private boolean performPinnedItemClick() {
        if (mPinnedSection == null)
            return false;

        OnItemClickListener listener = getOnItemClickListener();
        if (listener != null && getAdapter().isEnabled(mPinnedSection.position)) {
            View view = mPinnedSection.view;
            playSoundEffect(SoundEffectConstants.CLICK);
            if (view != null) {
                view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
            }
            listener.onItemClick(this, view, mPinnedSection.position,
                    mPinnedSection.id);
            return true;
        }
        return false;
    }

    public static boolean isItemViewTypePinned(ListAdapter adapter, int viewType) {
        if (adapter instanceof HeaderViewListAdapter) {
            adapter = ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        }
        return ((PinnedSectionListAdapter) adapter)
                .isItemViewTypePinned(viewType);
    }


    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
//                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
//                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    public void setXListViewListener(IXListViewListener l) {
        mListViewListener = l;
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


}
