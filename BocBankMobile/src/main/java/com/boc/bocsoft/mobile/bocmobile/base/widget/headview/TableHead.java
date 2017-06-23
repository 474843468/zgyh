package com.boc.bocsoft.mobile.bocmobile.base.widget.headview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by huixiaobo on 2016/6/1.
 *
 */
public class TableHead extends LinearLayout implements View.OnClickListener {

    /**图片组件*/
    private ImageView imLeft, imMiddle, imRight;
    /**内容组件*/
    private TextView tvLeft, tvMiddle, tvRight;
    /**点击事件回调借口*/
    private OnHeadClickListener mHeadCilck;
    /**实例化布局*/
    private LayoutInflater infater;

    public TableHead(Context context) {
        super(context);
        infater = LayoutInflater.from(context);
        init();
    }

    public TableHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        infater = LayoutInflater.from(context);
        init();
    }

    public TableHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        infater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        View tableHead = infater.inflate(R.layout.boc_item_head, this);
        LinearLayout ll_left = (LinearLayout) findViewById(R.id.ll_left);
        LinearLayout ll_middle = (LinearLayout) findViewById(R.id.ll_middle);
        LinearLayout ll_right = (LinearLayout) findViewById(R.id.ll_right);
        imLeft = (ImageView) tableHead.findViewById(R.id.im_left);
        imMiddle = (ImageView) tableHead.findViewById(R.id.im_middle);
        imRight = (ImageView) tableHead.findViewById(R.id.im_right);
        tvLeft = (TextView) tableHead.findViewById(R.id.tv_left);
        tvMiddle = (TextView) tableHead.findViewById(R.id.tv_middle);
        tvRight = (TextView) tableHead.findViewById(R.id.tv_right);
        ll_left.setOnClickListener(this);
        ll_middle.setOnClickListener(this);
        ll_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mHeadCilck != null) {
            int i = v.getId();
            if (i == R.id.ll_left) {
                mHeadCilck.onLeftClick();

            } else if (i == R.id.ll_middle) {
                mHeadCilck.onMiddleClick();

            } else if (i == R.id.ll_right) {
                mHeadCilck.onRightClick();

            }
        }
    }

    /**
     * 设置头部图片
     * @param leftId 左边图片
     * @param middleId 中间图片
     * @param rightId 右边图片
     */
    public void setHeadImage(int leftId, int middleId, int rightId) {
        imLeft.setImageResource(leftId);
        imMiddle.setImageResource(middleId);
        imRight.setImageResource(rightId);
    }

    /**
     * 设置头部内容
     * @param leftText 左边内容
     * @param middleText 中间内容
     * @param rightText 右边内容
     */
    public void setHeadData(int leftText, int middleText, int rightText){
        tvLeft.setText(leftText);
        tvMiddle.setText(middleText);
        tvRight.setText(rightText);
    }

    /**
     * 设置回调点击事件
     * @param onHeadClickListener 接口
     */
    public void setHeadClickListener(OnHeadClickListener onHeadClickListener){
        mHeadCilck = onHeadClickListener;
    }

    public interface OnHeadClickListener{
        //左侧点击事件
        void onLeftClick();
        //中间点击事件
        void onMiddleClick();
        //右侧点击事件
        void onRightClick();
    }
}
