package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by yangle on 2016/12/31.
 * 描述:
 */
public class HceSceneSelectView extends RelativeLayout implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private CheckBox mBackgroundCbox;
    private ImageView mCheckedImg;

    public HceSceneSelectView(Context context) {
        this(context,null);
    }

    public HceSceneSelectView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public HceSceneSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.boc_hce_scene_select_view, this);
        mBackgroundCbox = (CheckBox) view.findViewById(R.id.cb_background);
        mCheckedImg = (ImageView) view.findViewById(R.id.iv_checked);
        mBackgroundCbox.setOnCheckedChangeListener(this);
        mBackgroundCbox.setOnClickListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCheckedImg.setVisibility(isChecked ? VISIBLE :GONE);
    }

    public boolean isChecked() {
        return mBackgroundCbox.isChecked();
    }

    public void setChecked(boolean isChecked) {
        mBackgroundCbox.setChecked(isChecked);
    }

    public void setBackground(int resId) {
        mBackgroundCbox.setBackgroundResource(resId);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public interface OnClickListener {
        void onClick(View v);
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
    }

    public CheckBox getBackgroundCbox() {
        return mBackgroundCbox;
    }


}
