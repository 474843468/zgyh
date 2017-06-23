package com.chinamworld.bocmbci.biz.preciousmetal;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.BaseRUtil;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage.GoldstoreMoreActivity;
import com.chinamworld.bocmbci.constant.LayoutValue;
/**
 * Created by wang-pc on 2016/8/25.
 */
public class PreviousmeatalBackgroundLayout extends FrameLayout {
    // [start]
    private BaseActivity baseActivity = null;
    /**
     * 背景布局
     */
    private View rootView = null;
    /**
     * 标题背景设置颜色
     */
    private RelativeLayout backGround;
    /**
     * 返回按钮
     */
    private TextView metalBack;

    /**
     * 标题设置
     */
    private TextView meatleTitle;
    /**
     * 右上角更多
     */
    private TextView metalRight;
    private LinearLayout contentView;
    /**事件监听**/
    private OnClickListener setmetalBackClickListener = null;
    private OnClickListener setmetalRightClickListener = null;



    public PreviousmeatalBackgroundLayout(Context context) {
        super(context);
        initView(context);
    }

    public PreviousmeatalBackgroundLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
        TypedArray t = context.obtainStyledAttributes(attrs, BaseRUtil.Instance.getArrayID("R.styleable.BackgroundLayout"));

        for(int i = 0; i < t.getIndexCount(); ++i) {
            int id = t.getIndex(i);
            if(id == BaseRUtil.Instance.getID("R.styleable.BackgroundLayout_leftText")) {
                this.setMetalBackText(t.getString(id));
            } else if(id == BaseRUtil.Instance.getID("R.styleable.BackgroundLayout_rightText")) {
                this.setMetalRightText(t.getString(id));
            } else if(id == BaseRUtil.Instance.getID("R.styleable.BackgroundLayout_titleText")) {
                this.setTitleText(t.getString(id));
            }
        }

        t.recycle();
    }

    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        if (this.getChildCount() <= 1)
            return;
        contentView = (LinearLayout) this.findViewById(R.id.sliding_body);
        contentView.setOrientation(LinearLayout.VERTICAL);
        View tmp = null;
        for (int i = 1; i < this.getChildCount(); ) {
            tmp = this.getChildAt(i);
            this.removeView(tmp);
            contentView.addView(tmp);
        }
        super.onFinishInflate();
    }

    @Override
    public void addView(View child) {
        contentView.addView(child);
    }

    private void initView(final Context context) {

        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.MATCH_PARENT);
        this.setLayoutParams(lp);
        rootView = LayoutInflater.from(context).inflate(R.layout.previousmetal_layout_tittle, this,
                true);
        backGround=(RelativeLayout) rootView.findViewById(R.id.main_layout);
        metalBack=(TextView) rootView.findViewById(R.id.ib_back);
        meatleTitle=(TextView) rootView.findViewById(R.id.tv_title);
        metalRight=(TextView) rootView.findViewById(R.id.ib_top_right_btn);
//        metalRight.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setClass(context, GoldstoreMoreActivity.class);
//                context.startActivity(intent);
//            }
//        });
//        metalBack.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                  baseActivity.finish();
//
//            }
//
//        });

        contentView = (LinearLayout) rootView.findViewById(R.id.sliding_body);
        RelativeLayout relativeLayout = (RelativeLayout) rootView
                .findViewById(R.id.main_layout);
        if (relativeLayout != null) {
            android.widget.RelativeLayout.LayoutParams layoutParam = new android.widget.RelativeLayout.LayoutParams(
                    LayoutValue.SCREEN_WIDTH,LayoutValue.SCREEN_WIDTH / 7);
            relativeLayout.setLayoutParams(layoutParam);
        }
        /**左TextView 点击事件**/
        this.metalBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(PreviousmeatalBackgroundLayout.this.setmetalBackClickListener == null) {
                    PreviousmeatalBackgroundLayout.this.baseActivity.finish();
                } else {
                    PreviousmeatalBackgroundLayout.this.setmetalBackClickListener.onClick(v);
                }

            }
        });

        /**右TextView 点击事件**/
        this.metalRight.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(PreviousmeatalBackgroundLayout.this.setmetalRightClickListener != null) {
                    PreviousmeatalBackgroundLayout.this.setmetalRightClickListener.onClick(v);
                } else {//默认 ...更多按钮事件
                    Intent intent= new Intent(PreviousmeatalBackgroundLayout.this.baseActivity, GoldstoreMoreActivity.class);
                    PreviousmeatalBackgroundLayout.this.baseActivity.startActivity(intent);
                }

            }
        });
    }
    /**
     * 用当前BaseActivity初始化控件
     *
     * @param activity
     */
    public void init(BaseActivity activity) {
        this.baseActivity = activity;
//        baseActivity.initFootMenu();
    }

    /**
     * 设置Background 标题信息
     */
    public void setTitleText(String titleText) {
        if(titleText != null && titleText.length() > 0) {
            this.meatleTitle.setText(titleText);
        }
    }

    public void setTitleText(int resid) {
        this.meatleTitle.setText(resid);
    }

    /**
     * 设置左边 图标方式drawableleft
     */
    public void setMetalBackImage(Drawable drawable){
        this.metalRight.setCompoundDrawables(drawable,null,null,null);
    }

    /**
     * 设置左图标 文本信息
     */
    public void setMetalBackText(String str){
        this.metalBack.setVisibility(View.VISIBLE);
        this.metalBack.setText(str);
    }

    /**
     * 设置右边图标  drawableRight
     */
    public void setMetalRightImage(Drawable drawable){
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        this.metalRight.setCompoundDrawables(null,null,drawable,null);

    }

    /**
     * 设置右图标 文本信息
     */
    public void setMetalRightText(String str){
        this.metalRight.setVisibility(View.VISIBLE);
        this.metalRight.setText(str);
        this.metalRight.setCompoundDrawables(null,null,null,null);
    }

    /**
     * 设置左图标 显示隐藏
     */
    public void setMetalBackVisibility(int visibility){
        this.metalBack.setVisibility(visibility);
    }

    /**
     * 设置右图标显示隐藏
     */
    public void setMetalRightVisibility(int visibility){
        this.metalRight.setVisibility(visibility);
    }

    public void setMetalBackonClickListener(OnClickListener clickListener) {
        this.setmetalBackClickListener = clickListener;
    }

    public void setMetalRightonClickListener(OnClickListener clickListener) {
        this.setmetalRightClickListener = clickListener;
    }

    public void setPaddingWithParent(int left, int top, int right, int bottom) {
        this.contentView.setPadding(left, top, right, bottom);
    }

    /**
     * 设置标题头 背景颜色
     * @param resid  资源id
     */
    public void setMeatleTitleBackground(int resid){
        backGround.setBackgroundResource(resid);
    }

    /**
     * 设置标题栏右文本字体颜色
     * @param resid
     */
    public void setMetalRightTextColor(int resid){
        this.metalRight.setTextColor(resid);
    }

}