package com.boc.bocsoft.mobile.bocmobile.base.widget.gridbtn;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：lq7090
 * 创建时间：2016/11/18.
 * 用途：表格按钮
 */
public class GridButton extends LinearLayout implements AdapterView.OnItemClickListener {

    private GridView gv_button;//button表格
    private int numColumns ;//每行按钮数
    private TextView selectedTv;//之前选中的Textview
    private OnClickListener onClickListener; //相应点击时的监听
    private int bg_normal ;//每个button 正常状态时的背景资源id
    private int bg_clicked ;//被点击button 背景资源id


    /**
     * 用于设定点击事件
     *
     * @param onGvItemClickListener
     */
    public void setOnGvItemClickListener(OnClickListener onGvItemClickListener) {
        this.onClickListener = onGvItemClickListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /**
         * 将之前选中的按钮恢复
         */

        recover();

        /**
         * 对新选择按钮做UI变更和相应操作
         */
        if(view instanceof LinearLayout)
        {
            LinearLayout l = (LinearLayout) view;
            View tv =l.getChildAt(0);//获取TextView
            if(tv instanceof TextView)
            {
                TextView t = (TextView) tv;
                selectedTv = t;//将当前按钮设为被选择按钮
                t.setBackgroundResource(bg_clicked);
                onClickListener.onClickGV(l, t, position);//在使用该控件的界面实现点击时的操作
            }
        }
    }

    /**
     * 将控件复原到未选择状态
     */

    public void recover()
    {
        /**
         * 将之前选中的按钮恢复
         */
        if(!selectedTv.equals(null))
        {
            selectedTv.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            selectedTv.setBackgroundResource(bg_normal);
        }

    }

    public GridButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 加载布局文件
         */
        LayoutInflater.from(context).inflate(R.layout.gridbutton, this, true);
        selectedTv = new TextView(context,null);
        gv_button = (GridView) findViewById(R.id.gv_button);
        numColumns = attrs.getAttributeIntValue(R.styleable.GridButton_GridButton_numColumns, 3);//每行按钮数，默认三
        gv_button.setNumColumns(numColumns);
        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String, String> map = null;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridButton, numColumns,0);
        CharSequence[] btnNames = a.getTextArray(R.styleable.GridButton_GridButton_btnNames);//获取btn名字数组
        int item_id = a.getInt(R.styleable.GridButton_GridButton_item,R.layout.gv_btn_item);
        bg_normal = a.getInt(R.styleable.GridButton_GridButton_normal_bg,R.drawable.boc_bg_gvbtn_normal);//按钮未点击时背景
        bg_clicked =a.getInt(R.styleable.GridButton_GridButton_selected_bg,R.drawable.boc_bg_gvbtn_cliked);//按钮点击时背景
        a.recycle();

        if (!btnNames.equals(null))
            for (int i = 0; i < btnNames.length; i++) {
                map = new HashMap<>();
                map.put("btnName", btnNames[i].toString());
                list.add(map);
            }
        String[] form = new String[]{"btnName"};
        int[] to = new int[]{R.id.tv_gv_btn};
        gv_button.setAdapter(new SimpleAdapter(context, list, item_id, form, to));
        gv_button.setOnItemClickListener(this);

    }

    /**
     * 用于实现点击时被点击按钮的操作
     */
    public interface OnClickListener {
        /**
         * 使用此控件时，通过l,v来做相应的操作
         * @param l item 布局LinearLayout
         * @param v item 中的TextView
         */
        public abstract void onClickGV(LinearLayout l, TextView v, int position);
    }

    /**
     * 重写OnMeasure方法，使GridView在父类中最大，用于解决在ScrolView中GridView显示不全问题
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec,expandSpec);
    }
}
