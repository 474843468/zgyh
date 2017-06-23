
package com.chinamworld.bocmbci.biz.preciousmetal;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import com.chinamworld.bocmbci.abstracttools.BaseRUtil;

public class NewTitleAndContent extends FrameLayout {
    private LinearLayout containerLayout = null;
    private LinearLayout buttonLayout = null;
    Button btn = null;
//    private TextView mTitleTv;

    public NewTitleAndContent(Context context) {
        super(context);
        this.initView(context, (AttributeSet)null);
    }

    public NewTitleAndContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.setLayoutParams(new LayoutParams(-1, -1));
        View rootView = LayoutInflater.from(context).inflate(BaseRUtil.Instance.getID("R.layout.new_title_and_content_layout"), this, true);
        this.containerLayout = (LinearLayout)rootView.findViewById(BaseRUtil.Instance.getID("R.id.containerLayout"));
        this.buttonLayout = (LinearLayout)rootView.findViewById(BaseRUtil.Instance.getID("R.id.buttonLayout"));
        this.btn = (Button)rootView.findViewById(BaseRUtil.Instance.getID("R.id.btn"));
//        this.mTitleTv = (TextView)rootView.findViewById(BaseRUtil.Instance.getID("R.id.titleText"));
        if(attrs != null) {
            TypedArray t = context.obtainStyledAttributes(attrs, BaseRUtil.Instance.getArrayID("R.styleable.TitleAndContentLayout"));

            for(int i = 0; i < t.getIndexCount(); ++i) {
                int id = t.getIndex(i);
//                if(id == BaseRUtil.Instance.getID("R.styleable.TitleAndContentLayout_titleVisibility")) {
//                    int visibility = t.getIndex(id);
//                    if(visibility == 0) {
//                        this.setTitleVisibility(0);
//                    } else if(visibility == 1) {
//                        this.setTitleVisibility(8);
//                    } else if(visibility == 2) {
//                        this.setTitleVisibility(4);
//                    }
//                }

//                if(id == BaseRUtil.Instance.getID("R.styleable.TitleAndContentLayout_android_text")) {
//                    this.setTitleText(t.getString(id));
//                }
            }

            t.recycle();
        }
    }

//    public void setTitleText(String textValue) {
//        if(textValue != null) {
//            this.mTitleTv.setText(textValue);
//        }
//
//    }

//    public void setTitleText(int resId) {
//        this.mTitleTv.setText(resId);
//    }

//    public void setTitleVisibility(int visibility) {
//        this.mTitleTv.setVisibility(visibility);
//    }

    public void setButtonVisibility(int visibility) {
        if(this.buttonLayout != null) {
            this.buttonLayout.setVisibility(visibility);
        }

    }

    public void setOnClickListener(OnClickListener clickListener) {
        if(this.btn != null) {
            this.btn.setOnClickListener(clickListener);
        }

    }

    protected void onFinishInflate() {
        if(this.getChildCount() > 1) {
            View tmp = null;
            byte i = 1;

            while(i < this.getChildCount()) {
                tmp = this.getChildAt(i);
                super.removeView(tmp);
                this.addView(tmp);
            }

            super.onFinishInflate();
        }
    }

    public int getContentChildrenCount() {
        return this.containerLayout.getChildCount();
    }

    public View getContentChildrenAt(int index) {
        return this.containerLayout == null?null:this.containerLayout.getChildAt(index);
    }

    public void addView(View child) {
        if(this.containerLayout != null) {
            this.containerLayout.addView(child);
        }

    }

    public void removeAllViews() {
        this.containerLayout.removeAllViews();
    }

    public void removeView(View view) {
        if(view != null) {
            this.containerLayout.removeView(view);
        }

    }
}
