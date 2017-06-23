package com.boc.bocsoft.mobile.bocmobile.base.widget.expandableitemview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/11/30.
 */

public class ExpandableItemView extends LinearLayout {
    private Context mContext;
    private View rootView;
    private LinearLayout RlImage;//右侧按钮

    private TextView headTextView;
    private TableLayout tableLayout;
    private boolean isTextBold = true;


    private List<ItemView> items = new ArrayList<>();

    public ExpandableItemView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ExpandableItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ExpandableItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_expandable_item, this);
        headTextView = (TextView) rootView.findViewById(R.id.tv_head);
        tableLayout = (TableLayout) rootView.findViewById(R.id.tl_detail);
        RlImage = (LinearLayout) rootView.findViewById(R.id.ll_img);
    }


    /**
     * 获取左侧textView
     *
     * @return
     */
    public TextView getHeadTextView() {
        return headTextView;
    }

    public void setHeadTextView(TextView headTextView) {
        this.headTextView = headTextView;
    }

    public void setHeadTextContent(String content) {
        this.headTextView.setText(content);
    }

    public void isShowRightImg(boolean isShow) {
        if (isShow) {
            RlImage.setVisibility(VISIBLE);
        } else {
            RlImage.setVisibility(GONE);
        }
    }
    //可以使用tableRow，会自动每列对其；linerlayout每列不在自动对其
    private void ininTableLayout() {
        tableLayout.removeAllViews();
        for (ItemView itemView : items) {
            LinearLayout tableRow = new LinearLayout(tableLayout.getContext());
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.RIGHT);
            TextView leftText = new TextView(tableRow.getContext());
            leftText.setTextColor(tableLayout.getContext().getResources().getColor(itemView.leftTextColor));
            leftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableLayout.getContext().getResources().getDimension(itemView.leftTextSize));
            if (isTextBold){
                leftText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
            leftText.setText(itemView.leftContent);
            tableRow.addView(leftText);
            TextView rightText = new TextView(tableRow.getContext());
            rightText.setTextColor(tableLayout.getContext().getResources().getColor(itemView.rightTextColor));
            rightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, tableLayout.getContext().getResources().getDimension(itemView.rightTextSize));
            if (isTextBold){
                rightText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
            rightText.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_10px),0,0,0);
            rightText.setText(itemView.rightContent);
            tableRow.addView(rightText);
            tableLayout.addView(tableRow);
        }
    }

    public List<ItemView> getItems() {
        return items;
    }

    public void setItems(List<ItemView> items) {
        this.items = items;
        ininTableLayout();
    }

    public void setTextBold(boolean isSet){
        this.isTextBold = isSet;
    }


    public static class ItemView {

        private String leftContent;//左侧文本内容
        private String rightContent;//右侧文本内容
        private int leftTextColor = R.color.boc_text_color_dark_gray;//左侧文本颜色
        private int rightTextColor = R.color.boc_text_color_dark_gray;//右侧文本颜色
        private int leftTextSize = R.dimen.boc_space_between_26px; //左侧文本字体大小
        private int rightTextSize = R.dimen.boc_space_between_26px;//右侧文本字体大小

        public ItemView() {
        }

        public ItemView(String leftContent, String rightContent) {
            this.leftContent = leftContent;
            this.rightContent = rightContent;
        }

        public ItemView(String leftContent, String rightContent, int leftTextColor) {
            this.leftContent = leftContent;
            this.rightContent = rightContent;
            this.leftTextColor = leftTextColor;
        }

        public String getLeftContent() {
            return leftContent;
        }

        public void setLeftContent(String leftContent) {
            this.leftContent = leftContent;
        }

        public String getRightContent() {
            return rightContent;
        }

        public void setRightContent(String rightContent) {
            this.rightContent = rightContent;
        }


        public int getLeftTextColor() {
            return leftTextColor;
        }

        public void setLeftTextColor(int leftTextColor) {
            this.leftTextColor = leftTextColor;
        }

        public int getRightTextColor() {
            return rightTextColor;
        }

        public void setRightTextColor(int rightTextColor) {
            this.rightTextColor = rightTextColor;
        }



    }


}
