package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by yangle on 2017/1/4.
 * 描述:
 */
public class HceScenceSelectGroupView extends LinearLayout implements HceSceneSelectView.OnClickListener {

    private HceSceneSelectView sceneView1;
    private HceSceneSelectView sceneView2;
    private int checkedId = -1;
    private int preCheckedId ;
    private int checkIndex;
    public HceScenceSelectGroupView(Context context) {
        this(context,null);
    }

    public HceScenceSelectGroupView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HceScenceSelectGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.boc_hce_scene_group_view, this);
        sceneView1 = (HceSceneSelectView) view.findViewById(R.id.hce_scene1);
        sceneView2 = (HceSceneSelectView) view.findViewById(R.id.hce_scene2);
        sceneView2.setBackground(R.drawable.boc_hce_unlock_background);
        sceneView2.setChecked(false);

        sceneView1.setOnClickListener(this);
        sceneView2.setOnClickListener( this);

    }

    @Override
    public void onClick(View v) {
        if (v == sceneView1.getBackgroundCbox()) {
            sceneView1.setChecked(true);
            sceneView2.setChecked(false);
            checkedId = sceneView1.getId();
            checkIndex = 0;
        }

        if (v == sceneView2.getBackgroundCbox()) {
            sceneView1.setChecked(false);
            sceneView2.setChecked(true);
            checkedId = sceneView2.getId();
            checkIndex = 1;
        }

        if (checkedId != preCheckedId) {
            if (checkedChangeListener != null) {
                checkedChangeListener.onCheckedChange(this,checkIndex,checkedId);
            }

        }
        preCheckedId = checkedId;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(HceScenceSelectGroupView view, int index,int checkedId);
    }

    private OnCheckedChangeListener checkedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.checkedChangeListener = listener;
    }

    public int getCheckedIndex() {
        return checkIndex;
    }
}
