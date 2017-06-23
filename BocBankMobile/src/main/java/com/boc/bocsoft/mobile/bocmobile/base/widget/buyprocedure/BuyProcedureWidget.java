package com.boc.bocsoft.mobile.bocmobile.base.widget.buyprocedure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 购买理财产品的进度显示
 * Created by wangtong on 2016/9/17.
 */
public class BuyProcedureWidget extends View {

    private static final int RADIUS = 30;
    private static final int RACT_HEIGHT = 20;
    private static final int PADING = 40;
    private static final int SPACE_BETWEEN = 5;

    private Paint paint;
    private String[] text = new String[]{"提交申请", "扣款购买", "发起赎回"};// 名称
    private String[] date = new String[]{"09-10", "09-12", "2017-09-12"};// 时间

    private int viewWidth;
    private int viewHeight;
    private int leftTextWidth;//最左侧文本最宽值
    private int rightTextwidth;//最右侧文本最宽值
    private int textHeight;//第一层文本最高值
    private int dateHeight;//第二层文本最高值
    private CompleteStatus status = CompleteStatus.SUBMIT;

    private Rect leftRect;//左侧文本点击区域
    private Rect middRect;//中间文本点击区域
    private Rect rightRect;//右侧文本点击区域


    private String leftHint ="";//左侧提示语
    private String middHint="";//中间提示语
    private String rightHint="";//右侧提示语

    private ErrorDialog errorDialog;


    private boolean letfCanClick = false;//左侧是否弹出提示框
    private boolean middCanClick = false;//中间是否弹出提示框
    private boolean rightCanClick = false;//右侧是否弹出提示框


    public enum CompleteStatus {
        SUBMIT, PAY, REVERT
    }

    public BuyProcedureWidget(Context context) {
        super(context);
        initView();
    }

    public BuyProcedureWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BuyProcedureWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.boc_text_size_small));//paint 设置textsize的单位是像素。
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        initViewSize();
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = viewHeight+getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLinesOut(canvas);
        drawCircleOut(canvas);
        drawLines(canvas);
        drawCircle(canvas);
    }

    /**
     * 画线
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        int left = leftTextWidth / 2 + PADING;
        int top = ((RADIUS+SPACE_BETWEEN) * 2 - RACT_HEIGHT) / 2;
        int right = (leftTextWidth / 2  + viewWidth - rightTextwidth / 2) / 2;
        int bottom = top + RACT_HEIGHT;
        int left2 = right;
        int right2 = viewWidth - rightTextwidth / 2 - PADING;
        //内沿
        Rect rect = new Rect(left, top, right, bottom);
        Rect rect2 = new Rect(left2, top, right2, bottom);
        if (status == CompleteStatus.SUBMIT) {
            paint.setColor(getResources().getColor(R.color.boc_round_progressbar_gray));
            canvas.drawRect(rect, paint);
            canvas.drawRect(rect2, paint);
        } else if (status == CompleteStatus.PAY) {
            paint.setColor(getResources().getColor(R.color.boc_round_progressbar_green));
            canvas.drawRect(rect, paint);
            paint.setColor(getResources().getColor(R.color.boc_round_progressbar_gray));
            canvas.drawRect(rect2, paint);
        } else if (status == CompleteStatus.REVERT) {
            paint.setColor(getResources().getColor(R.color.boc_round_progressbar_green));
            canvas.drawRect(rect, paint);
            canvas.drawRect(rect2, paint);
        }
    }

    /**
     * 画圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        int cx = leftTextWidth / 2 + PADING;
        int cx2 = viewWidth - rightTextwidth / 2 - PADING;
        int cx1 = cx + (cx2-cx) / 2;
        int cy = RADIUS+SPACE_BETWEEN;
        if (status == CompleteStatus.SUBMIT) {
            paint.setColor(getResources().getColor(R.color.boc_round_progressbar_green));
            canvas.drawCircle(cx, cy, RADIUS, paint);
            drawText(canvas, 0, false);
            paint.setColor(getResources().getColor(R.color.boc_round_progressbar_gray));
            canvas.drawCircle(cx1, cy, RADIUS, paint);
            canvas.drawCircle(cx2, cy, RADIUS, paint);
            drawText(canvas, 1, true);
            drawText(canvas, 2, true);
        } else if (status == CompleteStatus.PAY) {
            paint.setColor(getResources().getColor(R.color.boc_round_progressbar_green));
            canvas.drawCircle(cx, cy, RADIUS, paint);
            canvas.drawCircle(cx1, cy, RADIUS, paint);
            drawText(canvas, 0, false);
            drawText(canvas, 1, false);
            paint.setColor(getResources().getColor(R.color.boc_round_progressbar_gray));
            canvas.drawCircle(cx2, cy, RADIUS, paint);
            drawText(canvas, 2, true);
        } else if (status == CompleteStatus.REVERT) {
            paint.setColor(getResources().getColor(R.color.boc_round_progressbar_green));
            canvas.drawCircle(cx, cy, RADIUS, paint);
            canvas.drawCircle(cx1, cy, RADIUS, paint);
            canvas.drawCircle(cx2, cy, RADIUS, paint);
            drawText(canvas, 0, false);
            drawText(canvas, 1, false);
            drawText(canvas, 2, false);
        }
    }
    // onAttachedTowindow 和onmeasure的顺序不定，但是一定先于ondraw
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        initViewSize();

    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (errorDialog != null && errorDialog.isShowing()){
            errorDialog.dismiss();
        }
        errorDialog = null;
    }


    /**
     * 画文本
     *
     * @param canvas
     * @param index
     * @param flag
     */
    private void drawText(Canvas canvas, int index,boolean flag) {
        int left = leftTextWidth / 2 + PADING;
        int left2 = 0;
        int bottom = RADIUS * 2 + 2*SPACE_BETWEEN + textHeight + 16;
        int recttop = (RADIUS+SPACE_BETWEEN) * 2 + 16;
        int rectbottom = (RADIUS+SPACE_BETWEEN) * 2 + 26 +textHeight+dateHeight;

        Rect textRect = new Rect();
        if (flag == true) {
            paint.setColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        } else {
            paint.setColor(getResources().getColor(R.color.boc_round_progressbar_green));
        }
        if (index == 0) {
            paint.getTextBounds(text[index], 0, text[index].length(), textRect);
            left = leftTextWidth / 2 + PADING - textRect.width() / 2;
            paint.getTextBounds(date[index], 0, date[index].length(), textRect);
            left2 = leftTextWidth / 2 + PADING - textRect.width() / 2;
            leftRect = new Rect(PADING,recttop,leftTextWidth + PADING, rectbottom);
        } else if (index == 1) {
            paint.getTextBounds(text[index], 0, text[index].length(), textRect);
            int middTextWidth = textRect.width();
            left = (leftTextWidth / 2  + viewWidth - rightTextwidth / 2) / 2 - middTextWidth/2;
            paint.getTextBounds(date[index], 0, date[index].length(), textRect);
            int middDateWidth = textRect.width();
            left2 = (leftTextWidth / 2  + viewWidth - rightTextwidth / 2) / 2 - middDateWidth/2;
            middRect = new Rect(Math.min(left,left2),recttop,Math.min(left,left2) + Math.max(middTextWidth,middDateWidth),rectbottom);
        } else if (index == 2) {
            paint.getTextBounds(text[index], 0, text[index].length(), textRect);
            left = viewWidth - rightTextwidth / 2 - PADING - textRect.width() / 2;
            paint.getTextBounds(date[index], 0, date[index].length(), textRect);
//            left2 = viewWidth - textRect.width() - PADING;
            left2 = viewWidth - rightTextwidth / 2 - PADING  - textRect.width() / 2;
            rightRect = new Rect(viewWidth-rightTextwidth - PADING,recttop,viewWidth - PADING, rectbottom);
        }

        canvas.drawText(text[index], left, bottom, paint);
        paint.setFakeBoldText(true);
        canvas.drawText(date[index], left2, bottom + dateHeight + 10, paint);
        paint.setFakeBoldText(false);
    }

    public CompleteStatus getStatus() {
        return status;
    }

    /**
     * 设置当前的显示状态
     *
     * @param status
     */
    public void setStatus(CompleteStatus status) {
        this.status = status;
    }

    /**
     * 设置时间
     *
     * @param date
     */
    public void setDate(String[] date) {
        this.date = date;
    }

    /**
     * 设置文本
     *
     * @param text
     */
    public void setText(String[] text) {
        this.text = text;
    }


    private void initViewSize(){
        //左侧宽度是左侧上下文本宽度的最大值
        Rect textRect = new Rect();
        paint.setFakeBoldText(true);
        paint.getTextBounds(date[0], 0, date[0].length(),textRect);
        int leftWidthDate = textRect.width();
        paint.setFakeBoldText(false);
        paint.getTextBounds(text[0], 0, text[0].length(), textRect);
        int leftWidthText = textRect.width();
        leftTextWidth = Math.max(leftWidthDate,leftWidthText);

        //第一排高度：三个字符串高端最大值
        int leftTextHeight = textRect.height();
        paint.getTextBounds(text[1], 0, text[1].length(), textRect);
        int midTextHeight = textRect.height();
        paint.getTextBounds(text[2], 0, text[2].length(), textRect);
        int rightTextHeight = textRect.height();
        textHeight = Math.max(Math.max(leftTextHeight,midTextHeight),rightTextHeight);

        //右侧宽带为最右侧上下文本宽度的最大值
        paint.getTextBounds(text[2], 0, text[2].length(), textRect);
        int rightWidthText = textRect.width();
        paint.setFakeBoldText(true);
        paint.getTextBounds(date[2], 0, date[2].length(),textRect);
        int rightWidthDate = textRect.width();
        rightTextwidth = Math.max(rightWidthText,rightWidthDate);

        //第二排高度：三个字符串高端最大值
        int rightDateHeight = textRect.height();
        paint.getTextBounds(date[1], 0, date[1].length(),textRect);
        int midDateHeight = textRect.height();
        paint.getTextBounds(date[1], 0, date[1].length(),textRect);
        int leftDateHeight = textRect.height();
        dateHeight = Math.max(Math.max(leftDateHeight,midDateHeight),rightDateHeight);

        Paint.FontMetricsInt font = paint.getFontMetricsInt();
        int bottom = font.bottom;
        paint.setFakeBoldText(false);

        //10+16  两行文字间距10，第一行文字距离圆下沿16
        // 设置bottom的原因：drawText(@NonNull String text, float x, float y, @NonNull Paint paint)参数y是字符的baseline而非最低线,baseline到bottom的距离计算如上
        viewHeight = textHeight+ dateHeight + RADIUS * 2 +2*SPACE_BETWEEN + 26 + bottom;
    }

    private void drawCircleOut(Canvas canvas) {

        int cx = leftTextWidth / 2 + PADING;
        int cx2 = viewWidth - rightTextwidth / 2 - PADING;
        int cx1 = cx + (cx2-cx) / 2;
        int cy = RADIUS+SPACE_BETWEEN;
        paint.setColor(getResources().getColor(R.color.boc_round_progressbar_gray));
        canvas.drawCircle(cx, cy, RADIUS+SPACE_BETWEEN, paint);
        canvas.drawCircle(cx1, cy, RADIUS+SPACE_BETWEEN, paint);
        canvas.drawCircle(cx2, cy, RADIUS+SPACE_BETWEEN, paint);

    }

    private void drawLinesOut(Canvas canvas) {

        int left = leftTextWidth / 2 + PADING;
        int top = ((RADIUS+SPACE_BETWEEN) * 2 - RACT_HEIGHT) / 2;
        int right = (leftTextWidth / 2  + viewWidth - rightTextwidth / 2) / 2;
        int bottom = top + RACT_HEIGHT;
        int left2 = right;
        int right2 = viewWidth - rightTextwidth / 2 - PADING;
        //外沿
        Rect rect3 = new Rect(left,top-SPACE_BETWEEN,right,bottom+SPACE_BETWEEN);
        Rect rect4 = new Rect(left2,top-SPACE_BETWEEN,right2,bottom+SPACE_BETWEEN);
        paint.setColor(getResources().getColor(R.color.boc_round_progressbar_gray));
        canvas.drawRect(rect3, paint);
        canvas.drawRect(rect4, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){

            float x = event.getX();
            float y = event.getY();
            int xposition = Math.round(x);
            int yposition = Math.round(y);
            if (leftRect.contains(xposition,yposition) && letfCanClick && !StringUtils.isEmptyOrNull(leftHint)) {
                showHintDialog(leftHint);
                return true;

            }

            if (middRect.contains(xposition,yposition) && middCanClick && !StringUtils.isEmptyOrNull(middHint)) {
                showHintDialog(middHint);
                return true;
            }

            if (rightRect.contains(xposition,yposition) && rightCanClick && !StringUtils.isEmptyOrNull(rightHint)) {
                showHintDialog(rightHint);
                return true;
            }
        }



        return super.onTouchEvent(event);
    }


    /**
     * 报错弹出框
     */
    private void showHintDialog(String errorMessage){
        if (errorDialog == null)
            errorDialog = new ErrorDialog(this.getContext());
        errorDialog.setBtnText("确认");
        errorDialog.setCancelable(false);
        errorDialog.setErrorData(errorMessage);
        if (!errorDialog.isShowing()) {
            errorDialog.show();
        }

        errorDialog.setOnBottomViewClickListener(new ErrorDialog.OnBottomViewClickListener() {
            @Override
            public void onBottomViewClick() {
                errorDialog.dismiss();
            }
        });

    }


    public String getLeftHint() {
        return leftHint;
    }
    /*
    * 设置左侧提示语
    * **/
    public void setLeftHint(String leftHint) {
        letfCanClick = true;
        this.leftHint = leftHint;
    }

    public String getMiddHint() {
        return middHint;
    }
    /*
    * 设置中间提示语
    * **/
    public void setMiddHint(String middHint) {
        middCanClick = true;
        this.middHint = middHint;
    }

    public String getRightHint() {
        return rightHint;
    }
    /*
    * 设置右侧提示语
    * **/
    public void setRightHint(String rightHint) {
        rightCanClick = true;
        this.rightHint = rightHint;
    }

    public boolean isLetfCanClick() {
        return letfCanClick;
    }

    public void setLetfCanClick(boolean letfCanClick) {
        this.letfCanClick = letfCanClick;
    }

    public boolean isMiddCanClick() {
        return middCanClick;
    }

    public void setMiddCanClick(boolean middCanClick) {
        this.middCanClick = middCanClick;
    }

    public boolean isRightCanClick() {
        return rightCanClick;
    }

    public void setRightCanClick(boolean rightCanClick) {
        this.rightCanClick = rightCanClick;
    }

}
