package com.boc.bocsoft.mobile.bocmobile.base.widget.PullToRefreshSwipeMenuListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;


/**
 * Created by gengjunying on 2016/12/12.
 */
public class PullToRefreshListHeader extends LinearLayout {

    private RelativeLayout mContainer;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;
    private int mState = STATE_NORMAL;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private final int ROTATE_ANIM_DURATION = 180;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;
    private ImageView img_jb1,img_qd;
    private TranslateAnimation mTranslateAnim;

    public PullToRefreshListHeader(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public PullToRefreshListHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 0);
        mContainer = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.swipe_menu_listview_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        img_jb1=(ImageView)findViewById(R.id.img_jb1);
        img_qd=(ImageView)findViewById(R.id.img_qd);

        //动画
        float fromXDelta = img_jb1.getRotationX();
        float toXDelta   = img_jb1.getRotationY();
        float fromYDelta = img_qd.getX();
        float toYDelta = img_qd.getY();

        mTranslateAnim = new TranslateAnimation(fromXDelta,toXDelta,fromYDelta,toYDelta);
        mTranslateAnim.setDuration(400);
        mTranslateAnim.setFillAfter(false);
        mTranslateAnim.setRepeatCount(-1);

        //header动画
        float pivotX = img_qd.getWidth() / 2f;
        float pivotY = img_qd.getHeight();
        float fromDegrees = -15f;
        float toDegrees = 15f;

        mRotateUpAnim = new RotateAnimation(fromDegrees, toDegrees, pivotX,pivotY);
        mRotateUpAnim.setDuration(400);
        mRotateUpAnim.setFillAfter(false);
//      mRotateUpAnim.setStartOffset(500);
        mRotateUpAnim.setRepeatCount(-1);
    }

    public void setState(int state) {
        if (state == mState)
            return;

        if (state == STATE_REFRESHING) {
            img_jb1.startAnimation(mTranslateAnim);
            img_qd.startAnimation(mRotateUpAnim);

        } else {
            img_jb1.clearAnimation();
            img_qd.clearAnimation();
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
                    img_jb1.startAnimation(mTranslateAnim);
                    img_qd.startAnimation(mRotateUpAnim);
                }
                if (mState == STATE_REFRESHING) {;
                    img_jb1.startAnimation(mTranslateAnim);
                    img_qd.startAnimation(mRotateUpAnim);
                }
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    img_jb1.clearAnimation();
                    img_qd.clearAnimation();
                }
                break;
            case STATE_REFRESHING:;
                img_jb1.startAnimation(mTranslateAnim);
                img_qd.startAnimation(mRotateUpAnim);
                break;
            default:
        }
        mState = state;
    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return mContainer.getHeight();
    }

}
