package com.chinamworld.llbt.userwidget.selectedview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chinamworld.llbt.userwidget.SlipMenu.SlipDrawerLayout;
import com.chinamworld.llbtwidget.R;


/**
 * 筛选布局控件
 */
public class FilterView extends FrameLayout implements View.OnClickListener {

    private SlipDrawerLayout drawerLayout;
    /** 内容填充区 */
    public ViewGroup contentPanel;
    private IFilterViewClickListener mListener;

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    /**
     * 初始化信息
     */
    private void initView(Context context) {
        View view = View.inflate(context, R.layout.llbt_filter_view, null);
        super.addView(view);
        contentPanel = (ViewGroup)view.findViewById(R.id.contentPanel);
        view.findViewById(R.id.btn_left).setOnClickListener(this);
        view.findViewById(R.id.btn_right).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                initDrawerLayout();
            }
        },100);
    }

    private void initDrawerLayout(){
        ViewGroup tmp = this;
        while(false == tmp instanceof SlipDrawerLayout){
            if(tmp == null)
                break;
            tmp = (ViewGroup) tmp.getParent();
        }
        drawerLayout = (SlipDrawerLayout)tmp;
    }



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
            if (mListener != null && mListener.onClick(v, ClickButtonType.ok) == true)
                return;
            colseDrawerLayout();
        } else if (i == R.id.btn_left) {// 重置
            if (mListener != null)
                mListener.onClick(v, ClickButtonType.reset);
        } else if (i == R.id.cancel) {// 取消
            if (mListener != null && mListener.onClick(v, ClickButtonType.cancel) == true)
                return;
            colseDrawerLayout();
        }
    }

    @Override
    protected void onFinishInflate() {
        if(getChildCount() <= 1)
            return;
        View tmp = null;
        for (int i = 1; i < getChildCount();) {
            tmp = getChildAt(i);
            super.removeView(tmp);
            addView(tmp);
        }
        super.onFinishInflate();
    }

    @Override
    public void addView(View child) {
        contentPanel.addView(child);
    }

    private void colseDrawerLayout(){
        if(drawerLayout != null)
            drawerLayout.toggle();
    }


    /**
     * 设置控件监听事件
     * @param onListener ： 监听事件
     */
    public void setOnFilterViewClickListener(IFilterViewClickListener onListener){
        mListener =  onListener;
    }

    public interface IFilterViewClickListener{
        /**
         * 点击事件
         * @param clickType ： 当前选中的按钮类型
         * @return： true:表示此点击事件已经处理完成，不需要控件继续处理。false：表示此事件未处理完成，后续工作由控件继续处理。默认返回false;
         */
        boolean onClick(View v, ClickButtonType clickType);
    }

    /** 点击的按钮类型 */
    public enum ClickButtonType{
        cancel,
        reset,
        ok,
    }

}
