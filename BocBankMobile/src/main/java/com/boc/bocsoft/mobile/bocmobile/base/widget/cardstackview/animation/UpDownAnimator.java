package com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.animation;

import android.animation.ObjectAnimator;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.BaseAnimator;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.CardStackView;

/**
 * 上下移动动画
 * Created by liuweidong on 2016/11/28.
 */
public class UpDownAnimator extends BaseAnimator {
    private static final String TAG = "UpDownAnimator";

    public UpDownAnimator(CardStackView cardStackView) {
        super(cardStackView);
    }

    /**
     * 展开动画设置
     *
     * @param viewHolder
     * @param position
     */
    protected void itemExpandAnimatorSet(final CardStackView.ViewHolder viewHolder, int position) {
        LogUtils.i(TAG + ":itemExpandAnimatorSet-------------------");
        View itemView = viewHolder.itemView;
        itemView.clearAnimation();// 取消view所有动画
        ObjectAnimator oa = ObjectAnimator.ofFloat(itemView, View.Y, itemView.getY(), mCardStackView.getScrollY() + mCardStackView.getPaddingTop());
        mAnimatorSet.play(oa);
        int collapseShowItemCount = 0;
        for (int i = 0; i < mCardStackView.getChildCount(); i++) {
            int childTop;
            if (i == mCardStackView.getSelectPosition())// 等于当前展开的itemview
                continue;

            final View child = mCardStackView.getChildAt(i);
            child.clearAnimation();
            if (i > mCardStackView.getSelectPosition() && collapseShowItemCount < 1) {// 大于当前展开的itemview且是下一个itemview
                childTop = mCardStackView.getShowHeight() - getCollapseStartTop(collapseShowItemCount) + mCardStackView.getScrollY();
                collapseShowItemCount++;
            } else if (i < mCardStackView.getSelectPosition()) {// 小于当前展开的itemview
                childTop = mCardStackView.getScrollY() - child.getHeight();
            } else {// 大于当前展开的itemview且是不相邻的下一个itemview
                childTop = mCardStackView.getShowHeight() + mCardStackView.getScrollY();
            }
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(child, View.Y, child.getY(), childTop);
            mAnimatorSet.play(objectAnimator);
        }
    }

    /**
     * 收起动画设置
     *
     * @param viewHolder
     */
    @Override
    protected void itemCollapseAnimatorSet(CardStackView.ViewHolder viewHolder) {
        LogUtils.i(TAG + ":itemCollapseAnimatorSet-------------------");
        int childTop = mCardStackView.getPaddingTop();
        for (int i = 0; i < mCardStackView.getChildCount(); i++) {
            View child = mCardStackView.getChildAt(i);
            child.clearAnimation();
            final CardStackView.LayoutParams lp = (CardStackView.LayoutParams) child.getLayoutParams();
            childTop += lp.topMargin;
            LogUtils.i(TAG + ":itemCollapseAnimatorSet-------------------i=" + i + ";" + childTop);
            if (i != 0) {
                childTop -= mCardStackView.getOverlapGaps() * 2;
            }
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(child, View.Y, child.getY(), childTop);
            mAnimatorSet.play(objectAnimator);
            childTop += lp.mHeaderHeight;
        }
    }

}
