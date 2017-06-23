package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;


/**
 * Created by yangle on 2016/12/13.
 * 描述:带开关和title的view
 */
public class ToggleRowView extends LinearLayout implements CompoundButton.OnCheckedChangeListener {

    private OnToggleListener mToggleListener;
    private CheckBox mToggle;
    private TextView mTvTitle;
    private View mUnderDividerLine;
    private boolean mToggleOn;
    private CharSequence mTitle;
    private View mRoot;

    public ToggleRowView(Context context) {
        this(context,null);
    }

    public ToggleRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ToggleRowView);
        mToggleOn = a.getBoolean(R.styleable.ToggleRowView_toggleOn, false);
        mTitle = a.getText(R.styleable.ToggleRowView_toggleTitle);
        a.recycle();

        mRoot = inflate(context, R.layout.boc_fragment_hce_widget_toggle_row, this);
        mToggle = (CheckBox) mRoot.findViewById(R.id.toggle);
        mTvTitle = (TextView) mRoot.findViewById(R.id.tv_toggle_title);
        mUnderDividerLine = mRoot.findViewById(R.id.divide_line);

        mToggle.setChecked(mToggleOn);
        mTvTitle.setText(mTitle);

        setListener();
    }

    private void setListener() {
        mToggle.setOnCheckedChangeListener(this);
    }

    public void setTitle(CharSequence text) {
        mTvTitle.setText(text);
    }

    public void hindUnderLine() {
        if (mUnderDividerLine.getVisibility() == View.VISIBLE) {
            mUnderDividerLine.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mToggleListener != null) {
            mToggleListener.onToggleChangedListener(buttonView,isChecked);
        }
    }

    public void setHeight(int height) {
        if (height > 0) {
            mRoot.getLayoutParams().height = height;
        }
    }

    public boolean isChecked() {
        return mToggle.isChecked();
    }

    public void setChecked(boolean isChecked) {
        mToggle.setChecked(isChecked);
    }

    public interface OnToggleListener {
        void onToggleChangedListener(CompoundButton buttonView, boolean isChecked);
    }

    public void setOnToggleListener(OnToggleListener listener){
        this.mToggleListener = listener;
    }

}
