package com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.scroll;

import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.CardStackView;


/**
 * Created by liuweidong on 2016/11/22.
 */
public class CardViewScrollImpl implements CardViewScroll {
    private static final String TAG = "CardViewScrollImpl";

    private CardStackView mCardStackView;
    private int mScrollY;
    private int mScrollX;

    public CardViewScrollImpl(CardStackView cardStackView) {
        mCardStackView = cardStackView;
    }

    @Override
    public void scrollViewTo(int x, int y) {
        LogUtils.i(TAG + ":scrollViewTo-------------------");
        x = clamp(x, mCardStackView.getWidth() - mCardStackView.getPaddingRight() - mCardStackView.getPaddingLeft(), mCardStackView.getWidth());
        y = clamp(y, mCardStackView.getShowHeight(), mCardStackView.getTotalHeight());
        mScrollY = y;
        mScrollX = x;
        updateChildPos();
    }

    @Override
    public void setViewScrollX(int x) {
        scrollViewTo(x, mScrollY);
    }

    @Override
    public int getViewScrollX() {
        return mScrollX;
    }

    @Override
    public void setViewScrollY(int y) {
        scrollViewTo(mScrollX, y);
    }

    @Override
    public int getViewScrollY() {
        return mScrollY;
    }

    private int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            return 0;
        }
        if ((my + n) > child) {
            return child - my;
        }
        return n;
    }

    private void updateChildPos() {
        for (int i = 0; i < mCardStackView.getChildCount(); i++) {
            View view = mCardStackView.getChildAt(i);
            if (view.getTop() - mScrollY < mCardStackView.getChildAt(0).getY()) {
                view.setTranslationY(mCardStackView.getChildAt(0).getY() - view.getTop());
            } else if (view.getTop() - mScrollY > view.getTop()) {
                view.setTranslationY(0);
            } else {
                view.setTranslationY(-mScrollY);
            }
        }
    }
}
