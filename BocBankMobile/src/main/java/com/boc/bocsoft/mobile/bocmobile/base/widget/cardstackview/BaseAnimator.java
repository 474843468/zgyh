package com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;

/**
 * 动画基类
 * Created by liuweidong on 2016/11/22.
 */
public abstract class BaseAnimator {
    private static final String TAG = "BaseAnimator";

    public static final int ANIMATION_STATE_START = 0;// 动画开始
    public static final int ANIMATION_STATE_END = 1;// 动画结束
    public static final int ANIMATION_STATE_CANCEL = 2;// 动画取消
    static final int ANIMATION_DURATION = 300;// 动画默认显示时间

    protected CardStackView mCardStackView;
    protected AnimatorSet mAnimatorSet;

    public BaseAnimator(CardStackView cardStackView) {
        mCardStackView = cardStackView;
    }

    protected abstract void itemExpandAnimatorSet(CardStackView.ViewHolder viewHolder, int position);

    protected abstract void itemCollapseAnimatorSet(CardStackView.ViewHolder viewHolder);

    protected void initAnimatorSet() {
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorSet.setDuration(getDuration());
    }

    /**
     * itemview点击动画
     *
     * @param viewHolder
     * @param position
     */
    public void itemClick(final CardStackView.ViewHolder viewHolder, int position) {
        LogUtils.i(TAG + ":itemClick-------------------");
        if (mAnimatorSet != null && mAnimatorSet.isRunning())
            return;
        initAnimatorSet();
        if (mCardStackView.getSelectPosition() == position) {// 点击的为当前展开的itemview
            onItemCollapse(viewHolder);// 折叠
        } else {
            onItemExpand(viewHolder, position);// 展开
        }
        if (mCardStackView.getChildCount() == 1)
            mAnimatorSet.end();
    }

    /**
     * 展开
     *
     * @param viewHolder
     * @param position
     */
    private void onItemExpand(final CardStackView.ViewHolder viewHolder, int position) {
        LogUtils.i(TAG + ":onItemExpand-------------------");
        final CardStackView.ViewHolder clickViewHolder = mCardStackView.getViewHolder(mCardStackView.getSelectPosition());
        if (clickViewHolder != null) {
            clickViewHolder.onItemExpand(false);
        }

        mCardStackView.setSelectPosition(position);// 设置展开的position
        itemExpandAnimatorSet(viewHolder, position);// 设置展开的动画
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mCardStackView.setScrollEnable(false);// 折叠时不可滚
                if (clickViewHolder != null) {
                    clickViewHolder.onAnimationStateChange(ANIMATION_STATE_START, false);
                }
                viewHolder.onAnimationStateChange(ANIMATION_STATE_START, true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                viewHolder.onItemExpand(true);// 展开
                if (clickViewHolder != null) {
                    clickViewHolder.onAnimationStateChange(ANIMATION_STATE_END, false);
                }
                viewHolder.onAnimationStateChange(ANIMATION_STATE_END, true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                if (clickViewHolder != null) {
                    clickViewHolder.onAnimationStateChange(ANIMATION_STATE_CANCEL, false);
                }
                viewHolder.onAnimationStateChange(ANIMATION_STATE_CANCEL, true);
            }
        });
        mAnimatorSet.start();
    }

    /**
     * 折叠
     *
     * @param viewHolder
     */
    private void onItemCollapse(final CardStackView.ViewHolder viewHolder) {
        LogUtils.i(TAG + ":onItemCollapse-------------------");
        itemCollapseAnimatorSet(viewHolder);// 折叠动画效果
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                viewHolder.onItemExpand(false);// 折叠
                mCardStackView.setScrollEnable(true);// 展开时可滚
                viewHolder.onAnimationStateChange(ANIMATION_STATE_START, false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCardStackView.setSelectPosition(CardStackView.DEFAULT_SELECT_POSITION);// 设置展开的position
                viewHolder.onAnimationStateChange(ANIMATION_STATE_END, false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                viewHolder.onAnimationStateChange(ANIMATION_STATE_CANCEL, false);
            }
        });
        mAnimatorSet.start();
    }

    protected int getCollapseStartTop(int collapseShowItemCount) {
        int sum = mCardStackView.getChildCount() - mCardStackView.getSelectPosition() > 1 ? 1 : mCardStackView.getChildCount() - mCardStackView.getSelectPosition() - 1;
        return mCardStackView.getOverlapGapsCollapse() * (1 - collapseShowItemCount - (1 - sum));
    }

    /**
     * 动画显示时间
     *
     * @return
     */
    public int getDuration() {
        return mCardStackView.getDuration();
    }
}
