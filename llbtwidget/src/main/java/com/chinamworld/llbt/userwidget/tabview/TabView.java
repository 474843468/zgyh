package com.chinamworld.llbt.userwidget.tabview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chinamworld.llbtwidget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签选项控件
 * Created by yuht on 2016/10/24.
 */
public class TabView extends LinearLayout implements View.OnClickListener{
    private Context mContext;

    private List<TabButton> mTabButtonList = new ArrayList<TabButton>();
    public TabView(Context context) {
        super(context);
        initView(context,null);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private ITabViewSelectedChanged mSelectedChanged;
    /** 设置TabView 选中改变事件 */
    public void setTabViewSelectedChanged(ITabViewSelectedChanged selectedChanged){
        mSelectedChanged = selectedChanged;
    }

    private int curIndex = -1;
    /** 设置当前选中项，从0开始计数 */
    public void setCurSelectedIndex(int nIndex){
        if(mTabButtonList.size() <= curIndex){
            return;
        }
        if(mSelectedChanged != null && curIndex != nIndex){
            mSelectedChanged.onChanged(mTabButtonList.get(nIndex));
        }
        mTabButtonList.get(nIndex).setSelectStatus(true);
        for(int i = 0; i < mTabButtonList.size();i ++){
            if(i == nIndex)
                continue;
            mTabButtonList.get(i).setSelectStatus(false);
        }

        curIndex = nIndex;
    }
    private void initView(Context context, AttributeSet attrSet){
        mContext = context;
        if(attrSet == null)
            return;
        TypedArray t = context.obtainStyledAttributes(attrSet, R.styleable.TabView);
        int id = 0;
        for(int i = 0; i < t.getIndexCount(); i++){
            id =t.getIndex(i);
            if(id == R.styleable.TabView_contentText){
                setContentText(t.getString(id));
            }
        }

    }


    /** 设置文字内容。以“;”隔开 */
    public void setContentText(String text){
        curIndex = -1;
        mTabButtonList.clear();
        if(text == null || text.length() <=0)
            return;
        String tmp[] = text.split(";");
        TabButton tmpButton;
        for(int i = 0; i < tmp.length;i++){
            if(tmp[i] == null || tmp[i].length() <= 0 )
                continue;
            tmpButton= createTabButton(tmp[i]);
            tmpButton.setTag(i);
            this.addView(tmpButton);
            mTabButtonList.add(tmpButton);
        }
    }

    private TabButton createTabButton(String text){
        TabButton tb = new TabButton(mContext);
        LayoutParams lp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
        tb.setLayoutParams(lp);
        tb.setText(text);
        tb.setOnClickListener(this);
        return tb;
    }

    @Override
    public void onClick(View v) {
        setCurSelectedIndex((Integer)v.getTag());
    }


}
