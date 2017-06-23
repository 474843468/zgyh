package com.boc.bocsoft.mobile.bocmobile.buss.system.main.ui.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.tab.TabIndicatorView;


/**
 * 底部Tab
 *
 *
 *
 *  ondraw() 和dispatchdraw（）的区别

 绘制VIew本身的内容，通过调用View.onDraw(canvas)函数实现

 绘制自己的孩子通过dispatchDraw（canvas）实现



 View组件的绘制会调用draw(Canvas canvas)方法，draw过程中主要是先画Drawable背景，对 drawable调用setBounds()然后是draw(Canvas c)方法
 .有点注意的是背景drawable的实际大小会影响view组件的大小，drawable的实际大小通过getIntrinsicWidth()和getIntrinsicHeight()获取，当
 背景比较大时view组件大小等于背景drawable的大小

 画完背景后，draw过程会调用onDraw(Canvas canvas)方法，然后就是dispatchDraw(Canvas canvas)方法, dispatchDraw()主要是分发给子组件进行绘
 制，我们通常定制组件的时候重写的是onDraw()方法。值得注意的是ViewGroup容器组件的绘制，当它没有背景时直接调用的是dispatchDraw()方法, 而
 绕过了draw()方法，当它有背景的时候就调用draw()方法，而draw()方法里包含了dispatchDraw()方法的调用。因此要在ViewGroup上绘制东西的时候往
 往重写的是dispatchDraw()方法而不是onDraw()方法，或者自定制一个Drawable，重写它的draw(Canvas c)和 getIntrinsicWidth(),

 getIntrinsicHeight()方法，然后设为背景。
 */
public class BottomTabIndicator extends TabIndicatorView {

    protected View rootView;
    protected ImageView ivIndicatorIcon;
    protected TextView tvTitle;
    private String tagId;
    private Class<? extends Fragment> fragmentClass;

//    @Bind(R.id.tv_title)
//    TextView tvTitle;
//    @Bind(R.id.iv_indicator_icon)
//    ImageView ivIndcatorIcon;

    public BottomTabIndicator(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        ivIndicatorIcon = (ImageView) rootView.findViewById(R.id.iv_indicator_icon);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
    }

    @Override
    public View getContentView() {
        rootView = View.inflate(mContext, R.layout.boc_tab_indicator, null);
        return rootView;
    }

    public BottomTabIndicator setIcon(Drawable iocn) {
        ivIndicatorIcon.setImageDrawable(iocn);
        return this;
    }

    public BottomTabIndicator setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    public BottomTabIndicator setTagId(String tagId) {
        this.tagId = tagId;
        return this;
    }

    public BottomTabIndicator setFragmentClass(Class<? extends Fragment> fragmentClass) {
        this.fragmentClass = fragmentClass;
        return this;
    }


    @Override
    public String getTagId() {
        return this.tagId;
    }

    @Override
    public Class<? extends Fragment> getFragmentClass() {
        return this.fragmentClass;
    }


    private boolean isBadgeShow = false;
    private int badgeNumber;
    private Paint badgePaint;
    private int badgeR = getResources().getDimensionPixelOffset(R.dimen.boc_space_between_10px);
    public void setBadgeNumber(boolean isShow,int count){
        this.isBadgeShow = isShow;
        this.badgeNumber = count;
        invalidate();
    }

    @Override protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        LogUtils.d("dding","----dispatchDraw----->"+isBadgeShow);
        if(!isBadgeShow)return;
        if(badgePaint == null){
            badgePaint = new Paint();
            badgePaint.setAntiAlias(true);
            badgePaint.setColor(Color.RED);
            badgePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
        canvas.drawCircle(getMeasuredWidth()*0.75f,getMeasuredHeight()*0.2f,badgeR,badgePaint);
    }
}
