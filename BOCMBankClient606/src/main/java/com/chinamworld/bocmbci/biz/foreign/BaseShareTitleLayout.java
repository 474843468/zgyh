package com.chinamworld.bocmbci.biz.foreign;
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
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.BaseRUtil;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.constant.LayoutValue;
/**
 * 外汇Title ForexRateInfoBackgroundLayout
 * @author by luqp 2016年9月22日
 */
public class BaseShareTitleLayout extends FrameLayout {
    // [start]
    private BaseActivity baseActivity = null;
    /** 背景布局*/
    private View rootView = null;
    /** 标题背景设置颜色*/
    private RelativeLayout backGround;
    /** 返回按钮*/
    private TextView back;
    /** 标题设置*/
    private TextView title;
    /** 右边分享*/
    private TextView share;
    /** 右上角更多*/
    private TextView more;
    private LinearLayout contentView;
    /*** 中间Title Layout*/
    private LinearLayout titleLayout;
    /*** 中间Title TextView*/
    private TextView titleText;
    /*** 中间Title Img*/
    private TextView titleImg;
    /**事件监听**/
    /** 返回按钮*/
    private OnClickListener setBackClickListener = null;
    /** 右边按钮*/
    private OnClickListener setMoreClickListener = null;
    /** 分享按钮*/
    private OnClickListener setShareClickListener = null;
    /** 货币对按钮*/
    private  OnClickListener setCurrencyClickListener = null;
    public BaseShareTitleLayout(Context context) {
        super(context);
        initView(context);
    }

    public BaseShareTitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
        TypedArray t = context.obtainStyledAttributes(attrs, BaseRUtil.Instance.getArrayID("R.styleable.BackgroundLayout"));

        for(int i = 0; i < t.getIndexCount(); ++i) {
            int id = t.getIndex(i);
            if(id == BaseRUtil.Instance.getID("R.styleable.BackgroundLayout_leftText")) {
                this.setBackText(t.getString(id));
            } else if(id == BaseRUtil.Instance.getID("R.styleable.BackgroundLayout_rightText")) {
                this.setMoreText(t.getString(id));
            } else if(id == BaseRUtil.Instance.getID("R.styleable.BackgroundLayout_titleText")) {
                this.setTitleText(t.getString(id));
            }
        }

        t.recycle();
    }

    @Override
    protected void onFinishInflate() {
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
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
        this.setLayoutParams(lp);
        rootView = LayoutInflater.from(context).inflate(R.layout.foreign_layout_tittle, this,true);
        backGround=(RelativeLayout) rootView.findViewById(R.id.main_layout);
        back=(TextView) rootView.findViewById(R.id.ib_back);
        title=(TextView) rootView.findViewById(R.id.tv_title);
        share = (TextView) rootView.findViewById(R.id.foreign_share);
        more=(TextView) rootView.findViewById(R.id.ib_top_right_btn);
        titleLayout =(LinearLayout) rootView.findViewById(R.id.ll_currency_right);
        titleText =(TextView) rootView.findViewById(R.id.currency_right);
        titleImg =(TextView) rootView.findViewById(R.id.currency_right_img);
        contentView = (LinearLayout) rootView.findViewById(R.id.sliding_body);
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.main_layout);
        if (relativeLayout != null) {
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
                    LayoutValue.SCREEN_WIDTH,LayoutValue.SCREEN_WIDTH / 7);
            relativeLayout.setLayoutParams(layoutParam);
        }
        onClickListeners(); // 标题按钮点击事件
    }
    /**
     * 用当前BaseActivity初始化控件
     * @param activity
     */
    public void init(BaseActivity activity) {
        this.baseActivity = activity;
    }



    // 返回图标按钮  文本&图片&颜色&是否显示设置------------------------------------------------------------
    /** 设置返回 图标方式drawableleft*/
    public void setBackImage(Drawable drawable){
        this.back.setCompoundDrawables(drawable,null,null,null);
    }

    /** 设置返回图标 文本信息*/
    public void setBackText(String str){
        this.back.setVisibility(View.VISIBLE);
        this.back.setText(str);
    }

    /**
     * 设置标题栏返回文本字体颜色
     * @param resid
     */
    public void setBackColor(int resid){
        this.back.setTextColor(resid);
    }

    /** 设置返回图标显示隐藏*/
    public void setBackVisibility(int visibility){
        this.back.setVisibility(visibility);
    }
    // 标题信息  文本&图片&颜色&是否显示设置------------------------------------------------------------------
    /** 设置Background Title标题信息*/
    public void setTitleText(String titleText) {
        if(titleText != null && titleText.length() > 0) {
            this.title.setText(titleText);
        }
    }

    public void setTitleText(int resid) {
        this.title.setText(resid);
    }
    /**
     * 设置标题头 背景颜色
     * @param resid  资源id
     */
    public void setTitleBackground(int resid){
        backGround.setBackgroundResource(resid);
    }
    /** 设置图标 Title显示隐藏*/
    public void setTitleVisibility(int visibility){
        this.title.setVisibility(visibility);
    }

    // 图片标题信息  文本&图片&颜色&是否显示设置---------------------------------------------------------------
    /** 设置中间图标  drawableRight*/
    public void setTitleImage(Drawable drawable){
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        this.titleImg.setCompoundDrawables(null,null,drawable,null);
    }

    /** 设置中间图标 文本信息*/
    public void setTitleTextView(String str){
        this.titleText.setVisibility(View.VISIBLE);
        this.titleText.setText(str);
        this.titleText.setCompoundDrawables(null,null,null,null);
    }
    /** 设置中间图标显示隐藏 currencyRightLayout*/
    public void setTitleLayoutVisibility(int visibility){
        this.titleLayout.setVisibility(visibility);
    }
    // 分享图标  文本&图片&颜色&是否显示设置---------------------------------------------------------------------
    /** 设置分享图标  drawableRight*/
    public void setShareImage(Drawable drawable){
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        this.share.setCompoundDrawables(null,null,drawable,null);

    }

    /** 设置分享图标 文本信息*/
    public void setShareText(String str){
        this.share.setVisibility(View.VISIBLE);
        this.share.setText(str);
        this.share.setCompoundDrawables(null,null,null,null);
    }
    /**
     * 设置标题栏分享本字体颜色
     * @param resid
     */
    public void setShareTextColor(int resid){
        this.share.setTextColor(resid);
    }
    /** 设置分享图标显示隐藏*/
    public void setShareVisibility(int visibility){
        this.share.setVisibility(visibility);
    }
// 更多图标  文本&图片&颜色&是否显示设置------------------------------------------------------------------------
    /** 设置更多图标  drawableRight*/
    public void setMoreImage(Drawable drawable){
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        this.more.setCompoundDrawables(null,null,drawable,null);
    }

    /** 设置更多图标 文本信息*/
    public void setMoreText(String str){
        this.more.setVisibility(View.VISIBLE);
        this.more.setText(str);
        this.more.setCompoundDrawables(null,null,null,null);
    }
    /**
     * 设置标题栏更多文本字体颜色
     * @param resid
     */
    public void setMoreTextColor(int resid){
        this.more.setTextColor(resid);
    }

    /** 设置更多图标显示隐藏*/
    public void setMoreVisibility(int visibility){
        this.more.setVisibility(visibility);
    }
    //-----------------------------------------------------------------------------------------------------------

    public void setPaddingWithParent(int left, int top, int right, int bottom) {
        this.contentView.setPadding(left, top, right, bottom);
    }

    /** 返回按钮*/
    public void setBackClickListener(OnClickListener clickListener) {
        this.setBackClickListener = clickListener;
    }

    /** 中间货币对按钮*/
    public void setCurrencyClickListener(OnClickListener clickListener) {
        this.setCurrencyClickListener = clickListener;
    }

    /** 分享按钮*/
    public void setShareClickListener(OnClickListener clickListener) {
        this.setShareClickListener = clickListener;
    }

    /** 更多按钮*/
    public void setMoreClickListener(OnClickListener clickListener) {
        this.setMoreClickListener = clickListener;
    }

    /** 点击事件*/
    public void onClickListeners(){
        /** 返回按钮 点击事件**/
        this.back.setOnClickListener(new OnClickListener(){
            public void onClick(View view) {
                if(BaseShareTitleLayout.this.setBackClickListener == null) {
                    BaseShareTitleLayout.this.baseActivity.finish();
                } else {
                    BaseShareTitleLayout.this.setBackClickListener.onClick(view);
                }
            }
        });

        /** 货币对按钮 点击事件*/
        this.titleLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseShareTitleLayout.this.setCurrencyClickListener != null) {
                    BaseShareTitleLayout.this.setCurrencyClickListener.onClick(view);
                } else {//货币对按钮
                    Toast.makeText(getContext(), "分享正在开发中......", Toast.LENGTH_LONG).show();
                }
            }
        });

        /** 分享按钮 点击事件*/
        this.share.setOnClickListener(new  OnClickListener(){
            public void onClick(View view) {
                if(BaseShareTitleLayout.this.setShareClickListener != null) {
                    BaseShareTitleLayout.this.setShareClickListener.onClick(view);
                } else { //分享按钮 点击事件
                    Toast.makeText(getContext(), "分享正在开发中......", Toast.LENGTH_LONG).show();
                }
            }
        });

        /**右TextView 点击事件**/
        this.more.setOnClickListener(new OnClickListener(){
            public void onClick(View view) {
                if(BaseShareTitleLayout.this.setMoreClickListener != null) {
                    BaseShareTitleLayout.this.setMoreClickListener.onClick(view);
                } else {//默认 ...更多按钮事件
                    Intent intent= new Intent(BaseShareTitleLayout.this.baseActivity, ForeignMoreActivity.class);
                    BaseShareTitleLayout.this.baseActivity.startActivity(intent);
                }
            }
        });
    }
}