package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;

/**
 * 中银理财-持仓详情条目,优先右侧显示
 * Created by cff on 2016/10/11.
 */
public class DetailTabRowTextButton extends LinearLayout {

    protected View rootView;
    private Context mContext;

    protected LinearLayout layoutBody;
    protected TextView tvName;
    protected TextView tvValue;
    protected TextView tvButton;
    protected View vBody;

    public DetailTabRowTextButton(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public DetailTabRowTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    @SuppressLint("NewApi")
    public DetailTabRowTextButton(Context context, AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    /**
     * 初始化View
     */
    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(
                R.layout.boc_view_detail_textbutton, this);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        tvValue = (TextView) rootView.findViewById(R.id.tv_value);
        tvButton = (TextView) rootView.findViewById(R.id.tv_button);
        layoutBody = (LinearLayout) rootView.findViewById(R.id.layout_body);
        vBody = (View) rootView.findViewById(R.id.v_body);
    }

    /**
     * 为条目设置内容
     *
     * @param name
     * @param value
     */
    public void addTextAndValue(String name, String value) {
        tvName.setText(name);
        tvValue.setText(value);
    }
    /**
     * 设置数字下面的分割线的左右margging值
     * 设置边距时调用
     *
     * @param left
     * @param right
     */
    public void setLineMargging(int left, int right) {
        LayoutParams lp = new LayoutParams(vBody.getLayoutParams());
        lp.setMargins(left, 0, right, 0);
        vBody.setVisibility(GONE);
    }
    /**
     * 添加内容，右侧点击按钮
     *
     * @param name
     * @param value
     * @param button
     */
    public void addTextAndValue(String name, String value, String button) {
        tvName.setText(name);
        tvValue.setText(value);
        tvButton.setVisibility(View.VISIBLE);
        tvButton.setText(button);
        // getStringWidth(button);
        // 获取屏幕宽度
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        // 计算左侧标题屏幕宽度
        // int namewidth = getPXvalue(120);
        // 计算中间数值所占宽度
        TextPaint titlePaint = tvValue.getPaint();
        titlePaint.setTextSize(getPXvalue(13));
        titlePaint.setFakeBoldText(true);
        float namewidth = titlePaint.measureText(name) + getPXvalue(40);

        // 计算中间数值所占宽度
        TextPaint valuetPaint = tvValue.getPaint();
        valuetPaint.setTextSize(getPXvalue(13));
        valuetPaint.setFakeBoldText(true);
        float valuewidth = valuetPaint.measureText(button) + getPXvalue(15);

        // 计算右侧按钮所占屏幕宽度
        TextPaint right_button = tvButton.getPaint();
        right_button.setTextSize(getPXvalue(13));
        right_button.setFakeBoldText(true);
        float buttonwidth = right_button.measureText(button) + getPXvalue(15);
        // 计算标题按钮所占屏幕宽度
        int total_width = (int) (width - namewidth);
        if (valuewidth + buttonwidth > total_width) {

            if (buttonwidth > total_width) {
                tvValue.setVisibility(View.GONE);
            }
            // 设置数值值所占屏幕宽度
            tvValue.setLayoutParams(new LayoutParams(
                    (int) (total_width - buttonwidth),
                    LayoutParams.MATCH_PARENT));
            // 设置按钮所占宽度
            LayoutParams mLayoutButton = new LayoutParams((int) buttonwidth, LayoutParams.MATCH_PARENT);
            mLayoutButton.setMargins(getPXvalue(5), getPXvalue(0), getPXvalue(10), getPXvalue(0));
            tvButton.setLayoutParams(mLayoutButton);

        }
        // }

    }

    /**
     * 重置内容 保证code值显示完全,name不完整显示。。。
     *
     * @param tv
     * @param name
     * @param code
     * @return
     */
    private void restContent(final TextView tv, final String name, final String code) {
        LogUtils.d("dding", "重置内容:" + name + "," + code);

        tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                tv.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int viewWidth = tv.getMeasuredWidth();
                float nameWidth = tv.getPaint().measureText(name);
                float codeWidth = tv.getPaint().measureText(code);

                LogUtils.d("dding", "---view treeObserver --- " + viewWidth + "," + nameWidth + "," + codeWidth);
                String nameValue = name;

                int lastIndex = nameValue.length() - 1;
                if (nameWidth + codeWidth > viewWidth) {
                    float aviableWidth = viewWidth - codeWidth - tv.getPaint().measureText("...");
                    for (; ; ) {
                        if (lastIndex < 0) break;
                        if (tv.getPaint().measureText(nameValue, 0, lastIndex) < aviableWidth) {
                            break;
                        }
                        lastIndex--;
                    }
                    if (lastIndex > 0) {
                        nameValue = nameValue.substring(0, lastIndex) + "...";
                    } else {
                        nameValue = "...";
                    }
                    //截取字符串
                    tv.setText(nameValue + code);
                } else {
                    tv.setText(name + code);
                }
            }
        });
    }
    /**
     * 根据手机分辨率吧dp转换成px
     */
    private int getPXvalue(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale - 0.5f);
    }

    /**
     * 更改文字
     *
     * @param name
     * @param value
     * @param button
     */
    public void updateTextAndValue(String name, String value, String button) {
        tvName.setText(name);
        tvValue.setText(value);
        tvButton.setText(button);
    }

    /**
     * 设置显示分割线
     * @param isShow
     */
    public void setShowDevide(boolean isShow){
        if (isShow) {
            vBody.setVisibility(VISIBLE);
        }else{
            vBody.setVisibility(GONE);
        }
    }
}