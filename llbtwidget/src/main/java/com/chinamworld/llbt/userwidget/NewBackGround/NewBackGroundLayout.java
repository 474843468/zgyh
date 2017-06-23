package com.chinamworld.llbt.userwidget.NewBackGround;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.llbtwidget.R;

/**
 * 3.0风格title控件布局
 * Created by Administrator on 2016/10/12.
 */
public class NewBackGroundLayout extends FrameLayout implements View.OnClickListener{

    private Context mContext;
    /** 背景布局 */
    private View rootView = null;
    /** 标题布局 文本层 */
    private View backGround;
    /** 返回按钮 */
    private TextView leftBt;
    /** 分享按钮 */
    private TextView shareBt;
    /** 标题文本 */
    private TextView titleTV;
    /**  标题中间布局  */
    private ViewGroup titleContentPanel;
    /** 右上角更多 */
    private TextView rightBt;
    /** 中间布局文件 */
    private LinearLayout contentView,contentViewBottom;
    /**分隔线**/
    private View lineDivider;
    /**事件监听**/
    private OnClickListener onLeftButtonClickListener = null;
    private OnClickListener onRightButtonClickListener = null;
    private OnClickListener onSharButtonClickListener = null;
    private OnClickListener onTitleClickListener = null;
    /**标题的背景层**/
    private FrameLayout fl_title_layout;
    private FrameLayout mTitleLayout;
    /**
     * 标题样式
     */
    public enum TitleStyle{
        /**文本黑样式**/
        Black,
        /**文本白样式**/
        White
    }

    public NewBackGroundLayout(Context context) {
        super(context);
        initView(context);
    }

    public NewBackGroundLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.BackgroundLayout);

        for(int i = 0; i < t.getIndexCount(); ++i) {
            int id = t.getIndex(i);
            if(id == R.styleable.BackgroundLayout_leftText) {
                this.setMetalBackText(t.getString(id));
            } else if(id == R.styleable.BackgroundLayout_rightText) {
                this.setMetalRightText(t.getString(id));
            } else if(id == R.styleable.BackgroundLayout_titleText) {
                this.setTitleText(t.getString(id));
            }
        }

        t.recycle();
    }

    @Override
    protected void onFinishInflate() {
        if (this.getChildCount() <= 1)
            return;
        contentView = (LinearLayout) this.findViewById(R.id.contentPanel);
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

    /**
     * 设置控件内容布局层次
     * @param contentLayoutFlag true--contentViewBottom;false--contentView
     */
    public void setContentLayout(boolean contentLayoutFlag){
        View tmp = null;
        if(contentLayoutFlag){
            for(int i = 0 ; i < contentView.getChildCount() ; ){
                tmp = contentView.getChildAt(i);
                contentView.removeView(tmp);
                contentViewBottom.addView(tmp);
            }
            contentViewBottom.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
        }else {
            for(int i = 0 ; i < contentViewBottom.getChildCount() ; ){
                tmp = contentViewBottom.getChildAt(i);
                contentViewBottom.removeView(tmp);
                contentView.addView(tmp);
            }
            contentViewBottom.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
        }
    }

    private void initView(final Context context) {
        mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.llbt_newbackground_layout, this, true);
        mTitleLayout = (FrameLayout) rootView.findViewById(R.id.fl_title);
        backGround=rootView.findViewById(R.id.title_layout);
        fl_title_layout = (FrameLayout) rootView.findViewById(R.id.titlebackgroung_layout);
        leftBt =(TextView) rootView.findViewById(R.id.ib_back);
        titleTV =(TextView) rootView.findViewById(R.id.tv_title);
        rightBt =(TextView) rootView.findViewById(R.id.ib_top_right_btn);
        contentView = (LinearLayout) rootView.findViewById(R.id.contentPanel);
        contentViewBottom = (LinearLayout) findViewById(R.id.contentPanelBottom);
        titleContentPanel = (ViewGroup)rootView.findViewById(R.id.titleContentPanel);
        lineDivider = rootView.findViewById(R.id.line_divider);
        /**左TextView 点击事件**/
        this.leftBt.setOnClickListener(this);
        /**右TextView 点击事件**/
        rightBt.setOnClickListener(this);
        shareBt = (TextView) rootView.findViewById(R.id.share_bt);
        shareBt.setOnClickListener(this);
        this.titleTV.setOnClickListener(this);
    }


    /**
     * 设置Background 标题信息
     */
    public void setTitleText(String titleText) {
        if(titleText != null) {
            this.titleTV.setText(titleText);
        }
    }

    /** 设置标题颜色*/
    public void setTitleTextColor(int color) {
        this.titleTV.setTextColor(getResources().getColor(color));
    }

    /**
     * 设置标题部分布局。
     * @param titleView
     */
    public void setTitleLayout(View titleView){
        titleContentPanel.removeAllViews();
        titleContentPanel.addView(titleView);
    }

    public void setTitleText(int resid) {
        titleTV.setText(resid);
    }

    /**
     * 设置左边 图标方式drawableleft
     */
    public void setOnLeftButtonImage(Drawable drawable){
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        leftBt.setCompoundDrawables(drawable,null,null,null);
    }

    /**
     * 设置左边图标及图标大小  drawableleft
     * @param drawable
     * @param with 设置显示图片宽度比例，例如1/2
     * @param height 设置显示图片高度比例，例如2/3
     */
    public void setOnLeftButtonImage(Drawable drawable,float with,float height){
        drawable.setBounds(0,0,(int)(drawable.getMinimumWidth()*with),(int)(drawable.getMinimumHeight()*height));
        leftBt.setCompoundDrawables(drawable,null,null,null);
    }

    /**
     * 设置左图标 文本信息
     */
    public void setMetalBackText(String str){
        this.leftBt.setVisibility(View.VISIBLE);
        this.leftBt.setText(str);
    }

    /**
     * 设置右边图标  drawableRight
     */
    public void setRightButtonImage(Drawable drawable){
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        rightBt.setCompoundDrawables(null,null,drawable,null);
    }

    /**
     * 设置右边图标及图标大小  drawableRight
     * @param drawable
     * @param with 设置显示图片宽度比例，例如1/2
     * @param height 设置显示图片高度比例，例如2/3
     */
    public void setRightButtonImage(Drawable drawable,float with,float height){
        drawable.setBounds(0,0,(int)(drawable.getMinimumWidth()*with),(int)(drawable.getMinimumHeight()*height));
        rightBt.setCompoundDrawables(null,null,drawable,null);
    }

    public void setShareButtonImage(Drawable drawable){
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        this.shareBt.setCompoundDrawables(null,null,drawable,null);

    }


    /**
     * 设置右图标 文本信息
     */
    public void setMetalRightText(String str){
        this.rightBt.setVisibility(View.VISIBLE);
        this.rightBt.setText(str);
        this.rightBt.setCompoundDrawables(null,null,null,null);
    }


    /**
     * 设置左图标 显示隐藏
     */
    public void setLeftButtonVisibility(int visibility){
        leftBt.setVisibility(visibility);
    }

    /**
     * 设置右图标显示隐藏
     */
    public void setRightButtonVisibility(int visibility){
        rightBt.setVisibility(visibility);
    }

    /**
     * 设置分享显示隐藏
     */
    public void setShareButtonVisibility(int visibility){
        shareBt.setVisibility(visibility);
    }

    public void setShareButtonImage(int id){
        shareBt.setBackgroundResource(id);
    }
    /**
     * 设置最左边按钮事件监听
     * @param clickListener
     */
    public void setOnLeftButtonClickListener(OnClickListener clickListener) {
        this.onLeftButtonClickListener = clickListener;
    }

    /**
     * 设置最右边按钮点击事件监听。
     * 默认为更多
     * @param clickListener
     */
    public void setOnRightButtonClickListener(OnClickListener clickListener) {
        this.onRightButtonClickListener = clickListener;
    }

    /**
     * 设置分享按钮点击事件监听
     * @param clickListener
     */
    public void setOnShareButtonClickListener(OnClickListener clickListener) {
        this.onSharButtonClickListener = clickListener;
    }

    public void setOnTitleClickListener(OnClickListener clickListener){
        this.onTitleClickListener = clickListener;
    }

    public void setPaddingWithParent(int left, int top, int right, int bottom) {
        this.contentView.setPadding(left, top, right, bottom);
    }

    /**
     * 设置标题头 背景颜色
     * @param resid  资源id
     */
    public void setTitleBackground(int resid){
        fl_title_layout.setBackgroundResource(resid);
    }


    /**
     * 设置标题栏右文本字体颜色
     * @param resid
     */
    public void setRightButtonTextColor(int resid){
        this.rightBt.setTextColor(resid);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.share_bt){
            if(onSharButtonClickListener != null) {
                onSharButtonClickListener.onClick(v);
            }
        }
        else if(v.getId() == R.id.ib_back){
            if(onLeftButtonClickListener != null) {
                onLeftButtonClickListener.onClick(v);
                return;
            }
            ((Activity)mContext).finish();
        }
        else if(v.getId() == R.id.ib_top_right_btn){
            if(onRightButtonClickListener != null) {
                onRightButtonClickListener.onClick(v);
                return;
            }
//            Intent intent= new Intent(mContext, MainMoreMenuActivity.class);
//            mContext.startActivity(intent);
        }else if(v.getId() == R.id.tv_title){
            if(onTitleClickListener !=null){
                onTitleClickListener.onClick(v);
                return;
            }
        }

    }

    /**
     * 标题栏分隔线
     * @param visibility
     */
    public void setLineDividerVisibility(int visibility){
        lineDivider.setVisibility(visibility);
    }

    /**
     * 设置标题的背景透明度
     * @param alpha
     */
    public void setTitleBackgroundAlpha(float alpha){
        fl_title_layout.setAlpha(alpha);
    }

    public int getTitleHeight(){
        return mTitleLayout.getHeight();
    }

    /**
     * 设置view的背景透明度
     * @param v
     * @return
     */
    public int getViewHeight(View v){
        if(v == null) return 0;
        return v.getHeight();
    }

    /**
     * 设置标题样式
     * @param style
     */
    public void setTitleStyle(TitleStyle style){
        leftBt.setTextColor(style == TitleStyle.Black ? getResources().getColor(R.color.boc_text_color_dark_gray) :
                    getResources().getColor(R.color.white));
        this.setOnLeftButtonImage(style == TitleStyle.Black ? getResources().getDrawable(R.drawable.llbt_new_back_icon) :
                    getResources().getDrawable(R.drawable.llbt_new_white_icon));
        titleTV.setTextColor(style == TitleStyle.Black ? getResources().getColor(R.color.boc_text_color_dark_gray) :
                getResources().getColor(R.color.white));
        shareBt.setTextColor(style == TitleStyle.Black ? getResources().getColor(R.color.boc_text_color_dark_gray) :
                getResources().getColor(R.color.white));
        this.setShareButtonImage(style == TitleStyle.Black ? getResources().getDrawable(R.drawable.llbt_btn_share_black) :
                getResources().getDrawable(R.drawable.llbt_share_white));
        rightBt.setTextColor(style == TitleStyle.Black ? getResources().getColor(R.color.boc_text_color_dark_gray) :
                getResources().getColor(R.color.white));
        this.setRightButtonImage(style == TitleStyle.Black ? getResources().getDrawable(R.drawable.llbt_point_more) :
                getResources().getDrawable(R.drawable.llbt_more_white));
    }

}