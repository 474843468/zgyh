package com.boc.bocsoft.mobile.bocmobile.base.widget.operation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;


/**
 * (请使用最新结果页组件 BaseResultView,使用参考 TestResultFragment)操作结果底部按钮
 * Created by niuguobin on 2016/6/2.
 */
@Deprecated
public class OperationResultBottom extends LinearLayout {
    private View layout_root, v_head;
    private Context mContext;
    private Button text_back;
    private HomeBtnCallback goHomeBtnCallback;

    @Deprecated
    public OperationResultBottom(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    @Deprecated
    public OperationResultBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    @Deprecated
    private void init() {
        layout_root = LayoutInflater.from(mContext).inflate(R.layout.boc_view_operation_result_bottom, this);
        text_back = (Button) layout_root.findViewById(R.id.btn_back_home);
        text_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goHomeBtnCallback != null)
                    goHomeBtnCallback.onHomeBack();
            }
        });

    }

    @Deprecated
    public void updateButton(String text) {
        text_back.setText(text);
    }

    /**
     * 设置背景颜色
     */
    @Deprecated
    public void updateButtonStyle() {
        text_back.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        text_back.setBackgroundColor(getResources().getColor(R.color.boc_main_btn_bg_color));
    }

    /**
     * 底部返回首页的按钮回调函数
     */
    @Deprecated
    public interface HomeBtnCallback {
        public void onHomeBack();
    }

    /**
     * 底部返回首页的按钮点击事件
     *
     * @param goHomeBtnCallback
     */
    @Deprecated
    public void setgoHomeOnclick(HomeBtnCallback goHomeBtnCallback) {
        this.goHomeBtnCallback = goHomeBtnCallback;
    }

}
