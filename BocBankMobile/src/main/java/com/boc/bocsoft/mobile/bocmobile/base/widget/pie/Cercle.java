package com.boc.bocsoft.mobile.bocmobile.base.widget.pie;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义控件  带进度转圈的已到账收益和未到账收益
 *
 * @author fanbin
 * @date 2016-9-10
 * method 注意各个方法的设置，一般用默认值就可以了，根据个别需要设置属性值    一般来说宽度match 高度尽量和宽度一样就可以了，
 * 使用：
 * 布局文件-----------start
 * android:id="@+id/cercle"
 * android:layout_width="300dp"
 * android:layout_height="300dp"
 * android:background="#fff"
 * android:gravity="center"
 * 布局文件-----------end
 * 在activity中find出来然后 使用setShowText 传入已到账收益和未到账收益就OK啦  剩下使用默认就和效果图一样了
 * 注意
 * 如果是日元用isRiyuan来设置,一定记得在使用setShowText之前使用
 */
public class Cercle extends TextView {
    private Paint mPaint;
    /**
     * 已到账收益的圆环的颜色
     */
    private int roundColor;
    /**
     * 未到账收益的圆环进度的颜色
     */
    private int roundProgressColor;
    /**
     * 圆环的宽度
     */
    private float roundWidth;
    /**
     * 百分比的字体颜色
     */
    private int textColor_progress;
    /**
     * 百分比的字体大小
     */
    private float textSize_progress;
    /**
     * 进度,在设置已到账收益和未到账收益金额的时候已计算出，无需开放方法,这个是用来中间显示数字的
     */
    private float progress_show;
    private String show_progress;
    /**
     * 进度,在设置已到账收益和未到账收益金额的时候已计算出，无需开放方法
     */
    private int progress_int;

    /**
     * 设置让蓝色进度逐渐增长的 无需开放方法
     */
    private int thread_progress = 0;
    /**
     * 设置让黄色进度逐渐增长的 无需开放方法
     */
    private int thread_progress2 = 0;
    /**
     * 圆的半径
     */
    private int radius = 70;
    Timer time = new Timer();
    /**
     * 已到账收益
     */
    private String yidaozhangshouyi;
    /**
     * 未到账收益
     */
    private String weidaozhangshouyi;
    /**
     * 已到账收益和未到账收益的字体大小  默认为14dp
     */
    private int yidaozhangshouyi_textSize;

    /**
     * 设置百分比下面“已到账”字体的大小
     */
    private int yidaozhang_Size;
    /**
     * 设置百分比下面“已到账”字体颜色
     */
    private int yidaozhang_color = Color.LTGRAY;

    private int banjin;

    public static final int HANDLER_CODE = 0x1111;

    private Context mContext;

    private boolean isRiyuan = false;


    public Cercle(Context context) {
        super(context);
    }

    public Cercle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Cercle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();
        //取自定义属性和默认值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.Cercle);
        roundColor = mTypedArray.getColor(R.styleable.Cercle_pieRoundColor, Color.rgb(53, 116, 193));//lan
        roundProgressColor = mTypedArray.getColor(R.styleable.Cercle_pieRoundProgressColor, Color.rgb(255, 209, 24));//huang
        roundWidth = mTypedArray.getDimension(R.styleable.Cercle_pieRoundWidth, dip2px(18));
        textSize_progress = mTypedArray.getDimension(R.styleable.Cercle_pieTextSize, dip2px(25));
        textColor_progress = mTypedArray.getColor(R.styleable.Cercle_pieRoundProgressColor, Color.BLACK);
        mTypedArray.recycle();
        yidaozhangshouyi_textSize = dip2px(14);
        yidaozhang_Size = dip2px(11);


    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLER_CODE) {
                invalidate();
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**画最外层的大圆*/
        int centre1 = getHeight() / 2;//获取圆心的x坐标
        int centre2 = getWidth() / 2;//获取圆心的x坐标
        //圆的半径
        banjin = dip2px(radius);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);//消除锯齿
        int hight6 = getTextHight("已到账收益：");
        /**画进度*/
        RectF oval = new RectF(centre2 - banjin, centre1 - banjin+2*hight6 , centre2 + banjin, centre1 + banjin+2*hight6);//- dip2px(30)
        mPaint.setStrokeWidth(roundWidth);
        mPaint.setColor(roundColor);
        canvas.drawArc(oval, -90, 360 * thread_progress / 100f, false, mPaint);//画的是已到账的蓝色
        mPaint.setStrokeWidth(roundWidth);
        mPaint.setColor(roundProgressColor);
        canvas.drawArc(oval, -90, 360 * thread_progress2 / 100f, false, mPaint);//画的是未到账的黄色

        //画百分比
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(textColor_progress);
        mPaint.setTextSize(textSize_progress);
        mPaint.setStrokeWidth(dip2px(2));

        int textWidth = getTextWidth(show_progress + "%");
        int textHight = getTextHight(show_progress + "%");
        canvas.drawText(show_progress + "%", centre2 - textWidth / 2, centre1 - textHight / 2 + dip2px(20) , mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(yidaozhang_color);
        mPaint.setTextSize(yidaozhang_Size);
        mPaint.setStrokeWidth(dip2px(1.2f));
        int textWidth1 = getTextWidth("已到账");
        int textHight1 = getTextHight("已到账");
        canvas.drawText("已到账", centre2 - textWidth1 / 2, centre1 + textHight1 + dip2px(20) , mPaint);
        //画上面已到账收益和未到账收益两个字段
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(yidaozhangshouyi_textSize);
        mPaint.setStrokeWidth(dip2px(2));

        int hight3 = getTextHight("已到账收益：" + yidaozhangshouyi);
        int width3 = getTextWidth("已到账收益：" + yidaozhangshouyi);

        int hight4 = getTextHight("未到账收益：" + weidaozhangshouyi);
        int width4 = getTextWidth("未到账收益：" + weidaozhangshouyi);

        if (width3 >= (getWidth() / 2) - dip2px(8) - 40) {
            canvas.drawText("已到账收益：", 10+dip2px(5), 20 + hight3, mPaint);
            canvas.drawText("未到账收益：", getWidth() /2+dip2px(5), 20 + getTextHight("未到账收益"), mPaint);
            canvas.drawText(yidaozhangshouyi + "", 10+dip2px(5), 30 + (2 * hight3), mPaint);
            canvas.drawText(weidaozhangshouyi + "", getWidth() /2+dip2px(5), 30 + (2 * getTextHight("未到账收益")), mPaint);
            mPaint.setColor(roundColor);
            canvas.drawCircle(10, 25 + hight3 / 2, dip2px(4), mPaint);
            mPaint.setColor(roundProgressColor);
            canvas.drawCircle(getWidth() /2, 25 + hight3 / 2, dip2px(4), mPaint);

        } else {
            canvas.drawText("已到账收益：" + yidaozhangshouyi, getWidth()/4-width3/2, 20 + hight3, mPaint);
            canvas.drawText("未到账收益：" + weidaozhangshouyi, getWidth() * 3 / 4 - width4 / 2, 20 + getTextHight("未到账收益"), mPaint);
            mPaint.setColor(roundColor);
            canvas.drawCircle(getWidth()/4-width3/2-dip2px(5), 25 + hight3 / 2, dip2px(4), mPaint);
            mPaint.setColor(roundProgressColor);
            canvas.drawCircle(getWidth()*3 /4 -width4/2-dip2px(5), 25 + hight3 / 2, dip2px(4), mPaint);
        }

        time.schedule(new TimerTask() {
            @Override
            public void run() {
                if (thread_progress < 100) {
                    thread_progress++;
                    mHandler.sendEmptyMessage(HANDLER_CODE);
                }
                if (thread_progress2 < (100 - progress_int)) {
                    thread_progress2++;
                    mHandler.sendEmptyMessage(HANDLER_CODE);
                }


            }
        }, 300, 50);
    }

    private int getTextWidth(String str) {
        Rect textBounds2 = new Rect();
        String numberStr2 = str;
        mPaint.getTextBounds(numberStr2, 0, numberStr2.length(), textBounds2);//get text bounds, that can get the text width and height
        return textBounds2.right - textBounds2.left;
    }

    private int getTextHight(String str) {
        Rect textBounds2 = new Rect();
        String numberStr2 = str;
        mPaint.getTextBounds(numberStr2, 0, numberStr2.length(), textBounds2);//get text bounds, that can get the text width and height
        return textBounds2.bottom - textBounds2.top;
    }

    /**
     * 设置上面的显示数字  以及进度
     *
     * @param yidaozhangshouyi  已到账收益金额
     * @param weidaozhangshouyi 未到账收益金额
     */
    public void setShowText(float yidaozhangshouyi, float weidaozhangshouyi) {

        if (weidaozhangshouyi != 0) {
            this.progress_show = (yidaozhangshouyi / (yidaozhangshouyi + weidaozhangshouyi)) * 100f;
        }
        if (progress_show < 0.01f) {
            progress_int = 0;
        }
        if (progress_show < 1f && progress_show >= 0.01f) {
            progress_int = 1;
        }
        if (progress_show >= 1f) {
            progress_int = (int) progress_show;
        }
        show_progress = progress_show + "0";
        if (show_progress.contains("E")) {
            show_progress = "0.00";
        } else if (show_progress.substring(show_progress.indexOf("."), show_progress.length()).length() > 2) {
            this.show_progress = show_progress.substring(0, show_progress.indexOf(".") + 3);
        }
        String yidaozhangshouyi_str = initMoney(yidaozhangshouyi+"");
        String weidaozhangshouyi_str = initMoney(weidaozhangshouyi+"");

        if (yidaozhangshouyi_str.length() > 3) {
            if (".00".equals(yidaozhangshouyi_str.substring(yidaozhangshouyi_str.length() - 3, yidaozhangshouyi_str.length()))) {
                if (isRiyuan) {
                    if (!yidaozhangshouyi_str.contains("-")) {
                        this.yidaozhangshouyi = yidaozhangshouyi_str.substring(1, yidaozhangshouyi_str.length() - 3).trim();
                    }else{
                        this.yidaozhangshouyi = "-"+yidaozhangshouyi_str.substring(2, yidaozhangshouyi_str.length() - 3).trim();
                    }
                } else {
                    if (!yidaozhangshouyi_str.contains("-")) {
                        this.yidaozhangshouyi = yidaozhangshouyi_str.substring(1, yidaozhangshouyi_str.length()).trim();
                    }else{
                        this.yidaozhangshouyi ="-"+ yidaozhangshouyi_str.substring(2, yidaozhangshouyi_str.length()).trim();
                    }
                }
            } else {
                if (!yidaozhangshouyi_str.contains("-")) {
                    this.yidaozhangshouyi = yidaozhangshouyi_str.substring(1, yidaozhangshouyi_str.length()).trim();
                }else{
                    this.yidaozhangshouyi ="-"+ yidaozhangshouyi_str.substring(2, yidaozhangshouyi_str.length()).trim();
                }
            }

        }
        if (weidaozhangshouyi_str.length() > 3) {

            if (".00".equals(weidaozhangshouyi_str.substring(weidaozhangshouyi_str.length() - 3, weidaozhangshouyi_str.length()))) {
                if (isRiyuan) {
                    if (!weidaozhangshouyi_str.contains("-")) {
                        this.weidaozhangshouyi = weidaozhangshouyi_str.substring(1, weidaozhangshouyi_str.length() - 3).trim();
                    }else{
                        this.weidaozhangshouyi ="-"+ weidaozhangshouyi_str.substring(2, weidaozhangshouyi_str.length()-3).trim();
                    }

                } else {
                    if (!weidaozhangshouyi_str.contains("-")) {
                        this.weidaozhangshouyi = weidaozhangshouyi_str.substring(1, weidaozhangshouyi_str.length()).trim();
                    }else{
                        this.weidaozhangshouyi ="-"+ weidaozhangshouyi_str.substring(2, weidaozhangshouyi_str.length()).trim();
                    }

                }
            } else {
                if (!weidaozhangshouyi_str.contains("-")) {
                    this.weidaozhangshouyi = weidaozhangshouyi_str.substring(1, weidaozhangshouyi_str.length()).trim();
                }else{
                    this.weidaozhangshouyi ="-"+ weidaozhangshouyi_str.substring(2, weidaozhangshouyi_str.length()).trim();
                }
            }
        } else {
            if (!yidaozhangshouyi_str.contains("-")) {
                this.yidaozhangshouyi = yidaozhangshouyi_str.substring(1, yidaozhangshouyi_str.length()).trim();
            }else{
                this.yidaozhangshouyi ="-"+ yidaozhangshouyi_str.substring(2, yidaozhangshouyi_str.length()).trim();
            }
            if (!weidaozhangshouyi_str.contains("-")) {
                this.weidaozhangshouyi = weidaozhangshouyi_str.substring(1, weidaozhangshouyi_str.length()).trim();
            }else{
                this.weidaozhangshouyi ="-"+ weidaozhangshouyi_str.substring(2, weidaozhangshouyi_str.length()).trim();
            }
        }



    }
//    /**
//     * 设置上面的显示数字  以及进度
//     *
//     * @param yidaozhangshouyi  已到账收益金额
//     * @param weidaozhangshouyi 未到账收益金额
//     */
//    public void setShowText(BigDecimal yidaozhangshouyi, BigDecimal weidaozhangshouyi) {
//        initMoney(yidaozhangshouyi+"");
//        if (weidaozhangshouyi != 0) {
//            this.progress_show = (yidaozhangshouyi / (yidaozhangshouyi + weidaozhangshouyi)) * 100f;
//        }
//        if (progress_show < 0.01f) {
//            progress_int = 0;
//        }
//        if (progress_show < 1f && progress_show >= 0.01f) {
//            progress_int = 1;
//        }
//        if (progress_show >= 1f) {
//            progress_int = (int) progress_show;
//        }
//        String show_progress = progress_show + "";
//        if (show_progress.contains("E")) {
//            this.progress_show = 0;
//        } else if (show_progress.substring(show_progress.indexOf("."), show_progress.length()).length() > 2) {
//            this.progress_show = Float.parseFloat(show_progress.substring(0, show_progress.indexOf(".") + 3));
//        }
//        String yidaozhangshouyi_str = yidaozhangshouyi + "";
//        String weidaozhangshouyi_str = weidaozhangshouyi + "";
//
//        if (yidaozhangshouyi_str.length() > 2) {
//            if (".0".equals(yidaozhangshouyi_str.substring(yidaozhangshouyi_str.length() - 2, yidaozhangshouyi_str.length()))) {
//                if (!isRiyuan) {
//                    this.yidaozhangshouyi = yidaozhangshouyi + "0";
//                }
//                if (isRiyuan) {
//                    this.yidaozhangshouyi = yidaozhangshouyi_str.substring(0,yidaozhangshouyi_str.length() - 2);
//                }
//
//            }else{
//                this.yidaozhangshouyi=yidaozhangshouyi+"";
//            }
//
//        }
//        if (weidaozhangshouyi_str.length() > 2) {
//            if (".0".equals(weidaozhangshouyi_str.substring(weidaozhangshouyi_str.length() - 2, weidaozhangshouyi_str.length()))) {
//                if (!isRiyuan){
//                    this.weidaozhangshouyi = weidaozhangshouyi + "0";
//                }
//                if (isRiyuan){
//                    this.weidaozhangshouyi =  weidaozhangshouyi_str.substring(0,weidaozhangshouyi_str.length() - 2);
//                }
//            }else{
//                this.weidaozhangshouyi=weidaozhangshouyi+"";
//            }
//        }else{
//            this.yidaozhangshouyi=yidaozhangshouyi+"";
//            this.weidaozhangshouyi=weidaozhangshouyi+"";
//        }
//
//
//
//    }

    /**
     * 设置是否是日元， 日元没有小数点
     *
     * @param isRiyuan
     */
    public void setIsRiyuan(boolean isRiyuan){
        this.isRiyuan=isRiyuan;
    }

    /**
     * 已到账收益和未到账收益的字体大小  默认为40
     *
     * @param yidaozhangshouyi_textSize 设置已到账收益和未到账收益的字体大小
     */
    public void setYidaozhangshouyi_textSize(int yidaozhangshouyi_textSize) {
        this.yidaozhangshouyi_textSize = yidaozhangshouyi_textSize;
    }

    /**
     * 在onDestory方法中调用
     */
    public void cancelTimer() {
        time.cancel();
        time = null;
    }

    /**
     * 设置已到账收益的颜色
     *
     * @param roundColor
     */
    public void setRoundColor(int roundColor) {
        this.roundColor = roundColor;
    }

    /**
     * 设置未到账收益的颜色
     *
     * @param roundProgressColor
     */
    public void setRoundProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    /**
     * 设置圆环的环宽
     *
     * @param roundWidth
     */
    public void setRoundWidth(float roundWidth) {
        this.roundWidth = dip2px(roundWidth);
    }

    /**
     * 设置圆环的半径
     *
     * @param radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * 设置百分比的字体颜色
     *
     * @param progressTextColor
     */
    public void setTextColor_progress(int progressTextColor) {
        this.textColor_progress = progressTextColor;
    }

    /**
     * 设置百分比的字体大小
     *
     * @param progressTextSize
     */
    public void setTextSize_progress(float progressTextSize) {
        this.textSize_progress = progressTextSize;
    }

    /**
     * 设置百分比下面“已到账”字体的大小
     *
     * @param yidaozhang_Size
     */
    public void setYidaozhang_Size(int yidaozhang_Size) {
        this.yidaozhang_Size = yidaozhang_Size;
    }

    /**
     * 设置百分比下面“已到账”字体的颜色
     *
     * @param yidaozhang_color
     */
    public void setYidaozhang_color(int yidaozhang_color) {
        this.yidaozhang_color = yidaozhang_color;
    }

    /**
     * 根据手机分辨率吧px转换成dp
     */
    private int px2didp(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机分辨率吧dp转换成px
     */
    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale - 0.5f);
    }

    private String initMoney(String money){
        if (!TextUtils.isEmpty(money)){
            BigDecimal bd=new BigDecimal(money);
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            return currency.format(bd);
        }else{
            return "";
        }
    }
}
