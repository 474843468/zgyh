package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.circlemenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.boc.bocsoft.mobile.bocmobile.R;


/**
 * 作者：xwg on 16/10/27 12:38
 * 跨境专区圆环菜单item
 */
public  class CircleIMenuItemView extends ImageView {



    private float angle = 0;
    private int position = 0;
    private String name;
    private String content;

    public CircleIMenuItemView(Context context) {
        this(context,null);
    }

    public CircleIMenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleIMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.BaseCircleMenuItemView);

            name = a.getString(R.styleable.BaseCircleMenuItemView_titileName);
            content = a.getString(R.styleable.BaseCircleMenuItemView_content);
            a.recycle();
        }
    }


    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}
