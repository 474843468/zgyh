package com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.framework.utils.AnimUtils;

/**
 * 局部刷新
 * Created by xdy on 2016/5/25.
 */
public class PartialLoadView extends ImageView {
    private Context mContext;
    /**
     * 加载和刷新的图片，0是加载，1是刷新
     */
    private int[] pics;
    Animation loadingAnim;
    private LoadStatus loadStatus;

    public PartialLoadView(Context context) {
        super(context);
        initView(context);
    }

    public PartialLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PartialLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        mContext = context;
        pics = new int[]{R.drawable.boc_partial_loading, R.drawable.boc_partial_refresh};
        loadingAnim = AnimUtils.getRotateCircleAnimation(mContext);
    }

    public void setPics(int[] pics) {
        this.pics = pics;
    }

    private LoadStatus mLoadStatus;

    /**
     * 显示加载动画，或者显示刷新按钮，或者加载成功隐藏按钮。
     * 刷新按钮时可点击
     *
     * @param loadStatus 加载状态
     */
    public void setLoadStatus(LoadStatus loadStatus) {
        mLoadStatus = loadStatus;
        switch (loadStatus) {
            case LOADING:
                setClickable(false);
                setImageResource(pics[0]);
                setVisibility(VISIBLE);
                startAnimation(loadingAnim);
                break;
            case REFRESH:
                clearAnimation();
                setImageResource(pics[1]);
                setVisibility(VISIBLE);
                setClickable(true);
                break;
            case SUCCESS:
                setClickable(false);
                clearAnimation();
                setVisibility(GONE);
                break;
        }
    }

    public enum LoadStatus {
        LOADING, REFRESH, SUCCESS
    }

    public LoadStatus getLoadStatus() {
        return mLoadStatus;
    }

    @Override protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility == View.VISIBLE && mLoadStatus == LoadStatus.LOADING){
            setLoadStatus(mLoadStatus);
        }
    }
}
