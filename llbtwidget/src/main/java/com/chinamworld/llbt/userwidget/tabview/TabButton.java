package com.chinamworld.llbt.userwidget.tabview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chinamworld.llbtwidget.R;

/**
 * Created by Administrator on 2016/10/24.
 */
public class TabButton extends FrameLayout {
    public TabButton(Context context) {
        super(context);
        initView(context);
    }

    public TabButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    TextView contentView;
    View mBottomView;
    private OnClickListener mOnClickListener;

    @Override
    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.llbt_tab_button_view,this,true);
        contentView = (TextView)findViewById(R.id.textView);
        mBottomView = findViewById(R.id.bottomView);
        findViewById(R.id.rootLayout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectStatus(true);
                if(mOnClickListener != null)
                    mOnClickListener.onClick(TabButton.this);
            }
        });
    }

    public void setText(String text){
        contentView.setText(text);
    }
    private boolean mIsSelected;
    public void setSelectStatus(boolean isSelected){
        mIsSelected = isSelected;
        if(mIsSelected == false) {
            contentView.setTextColor(this.getResources().getColor(R.color.boc_text_color_common_gray));
            mBottomView.setBackgroundColor(this.getResources().getColor(R.color.white));
        }
        else {
            contentView.setTextColor(this.getResources().getColor(R.color.boc_text_color_red));
            mBottomView.setBackgroundColor(this.getResources().getColor(R.color.boc_text_color_red));
        }
    }
}
