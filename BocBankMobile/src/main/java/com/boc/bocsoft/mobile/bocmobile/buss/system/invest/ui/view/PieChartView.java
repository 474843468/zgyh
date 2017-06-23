package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import java.util.List;

/**
 * 资产 - 饼图view
 * Created by eyding on 16/8/11
 */
public class PieChartView extends View {
  public PieChartView(Context context) {
    super(context);
    initView();
  }

  public PieChartView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }
  private float maxRadius;//最底部
  private float innerRadius;//内径
  private float outRadius;//饼图外径

  private List<PieChartItem> itemList;
  private RectF rectF;

  private Paint cellPaint;
  private Paint borderPaint;
  private Paint otherPaint;

  private float boderWidth = 4; //
  private int borderColor = 0xfffefefe;


  private int saveFlag =  Canvas.MATRIX_SAVE_FLAG |
      Canvas.CLIP_SAVE_FLAG |
      Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
      Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
      Canvas.CLIP_TO_LAYER_SAVE_FLAG;
  private PorterDuffXfermode xfermode = new PorterDuffXfermode( PorterDuff.Mode.SRC_OUT);

  private void initView() {
    cellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    cellPaint.setStyle(Paint.Style.FILL);

    borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    borderPaint.setStyle(Paint.Style.STROKE);
    borderPaint.setStrokeWidth(boderWidth);
    borderPaint.setColor(borderColor);

    otherPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    otherPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    otherPaint.setStrokeWidth(2);

    xfermode =  new PorterDuffXfermode( PorterDuff.Mode.SRC_OUT);

  }


  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

     //canvas.drawColor(0xff992200);
    canvas.save();

    canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);

    otherPaint.setColor(Color.parseColor("#fff1f1f1"));

    //最底层圆
    canvas.drawCircle(0, 0, maxRadius, otherPaint);

    if(itemList == null || itemList.size() == 0){
      return;
    }

    if(itemList.size() == 1){
      drawCell(canvas,itemList.get(0));
      if(currentItem == itemList.get(0)){
        drawCellInfo(canvas,currentItem);
      }
      return;
    }

    //画cell
    int currentIndex = currentItem==null?-1:itemList.indexOf(currentItem);//当前选中的

    for (PieChartItem item : itemList) {
      if (item != currentItem){
        drawCell(canvas,item);
      }
    }
    for (PieChartItem item : itemList) {
      if (item != currentItem){
        drawCellBorder(canvas,item,currentIndex);
      }
    }

    //画选中的cell
     drawBorderCell(canvas,currentItem);



    //中间的圆圈
    otherPaint.setColor(Color.WHITE);
    canvas.drawCircle(0,0,innerRadius,otherPaint);

    //圆圈外
    otherPaint.setStyle(Paint.Style.STROKE);
    otherPaint.setColor(Color.parseColor("#ffefef"));
    canvas.drawCircle(0,0,innerRadius-1,otherPaint);

    //恢复画笔
    otherPaint.setStyle(Paint.Style.FILL);

     drawCellInfo(canvas,currentItem);

    int color = otherPaint.getColor();
    otherPaint.setColor(Color.WHITE);
    float startPos = itemList.get(0).startPos;
    canvas.drawArc(rectF,startPos+animationValue,360-animationValue,true,otherPaint);
    otherPaint.setColor(color);

    canvas.restore();

  }


  private void drawBorderCell(Canvas canvas,PieChartItem item){
    if(item == null)return;
    float offset = 12;
    float cellSpace = 12;
    PointF startPoint = getPoint(outRadius+offset+boderWidth, currentItem.startPos);
    PointF endPoint = getPoint(outRadius+offset+boderWidth, currentItem.startPos+currentItem.swipe);
    RectF cellRectF = outRectF(rectF,offset);

    canvas.save();
    RectF canvasRectf = new RectF(0,0,getMeasuredWidth(),getMeasuredHeight());
    canvasRectf.offset(-getMeasuredWidth()/2,-getMeasuredHeight()/2);

    int count = canvas.saveLayer(canvasRectf, cellPaint, saveFlag);
    //画border
    cellPaint.setShader(null);
    cellPaint.setColor(borderColor);
    canvas.drawArc(outRectF(cellRectF,boderWidth),currentItem.startPos,currentItem.swipe,true,cellPaint);

    cellPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    //切出中心
    canvas.drawCircle(0,0,innerRadius+offset-boderWidth,cellPaint);
    //切除边界
    cellPaint.setStrokeWidth(cellSpace);
    canvas.drawLine(0,0,startPoint.x,startPoint.y,cellPaint);
    canvas.drawLine(0,0,endPoint.x,endPoint.y,cellPaint);

    cellPaint.setXfermode(null);
    canvas.restoreToCount(count);

    int cellCount = canvas.saveLayer(canvasRectf, cellPaint, saveFlag);

    //画 ----- cell ------------
    buildShader(item);
    cellPaint.setShader(item.drawShader);
    cellPaint.setColor(item.shadeColors[0]);
    canvas.drawArc(cellRectF,item.startPos,item.swipe,true,cellPaint);

    cellPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    canvas.drawCircle(0,0,innerRadius+offset,cellPaint);
    cellPaint.setStrokeWidth(cellSpace+boderWidth*2);
    canvas.drawLine(0,0,startPoint.x,startPoint.y,cellPaint);
    canvas.drawLine(0,0,endPoint.x,endPoint.y,cellPaint);
    cellPaint.setXfermode(null);

    canvas.restoreToCount(cellCount);


    canvas.restore();


  }

  private void drawCellInfo(Canvas canvas,PieChartItem item){
    if(item == null)return;
    otherPaint.setColor(0xff222222);
    float angle = item.startPos + item.swipe/2;

    float outR = outRadius + 80;
    //得到线的终点坐标
    PointF point = getPoint(outR, angle);

    PointF fromPoint = getPoint(outR/2 + innerRadius/2-40,angle);
    canvas.drawLine(fromPoint.x,fromPoint.y,point.x,point.y,otherPaint);
    canvas.drawCircle(fromPoint.x,fromPoint.y,4,otherPaint);
    otherPaint.setTextSize(getResources().getDisplayMetrics().density*15);

    // - 135 到 -45 第一区域 直到下方
    // - 45 到 45 第二区域 直到左侧
    //45 到135 第三区域 直到上方
    // 135到225 第四区域 直到右侧
    PointF p1 = getPoint(outR,-135);
    PointF p2 = getPoint(outR,-45);
    PointF p3 = getPoint(outR,45);
    PointF p4 = getPoint(outR,135);

    float maxWidth = Math.max(otherPaint.measureText(item.name), otherPaint.measureText(item.info))+10;
    Paint.FontMetrics fontMetrics = otherPaint.getFontMetrics();
    float lineHeight = fontMetrics.descent-fontMetrics.ascent;

    final float harf = getMeasuredWidth()/2;
    otherPaint.setTextAlign(Paint.Align.CENTER);
    if(point.y<=p1.y){
      //第一区域
      LogUtils.d("dding","第一区域");
      canvas.drawText(item.name,point.x,point.y-fontMetrics.descent-lineHeight,otherPaint);
      canvas.drawText(item.info,point.x,point.y-fontMetrics.descent,otherPaint);
    }else if(point.x>p2.x){
      //2
      LogUtils.d("dding","第二区域");
      if(point.x+maxWidth>harf){
        //需要调整调整
        float x =  (point.x + maxWidth/2)<=harf?point.x:(point.x-(point.x+maxWidth/2-harf));
        canvas.drawText(item.name,x,point.y-lineHeight+fontMetrics.leading,otherPaint);
        canvas.drawText(item.info,x,point.y-fontMetrics.descent,otherPaint);

      }else{
        canvas.drawText(item.name,point.x+maxWidth/2,point.y,otherPaint);
        canvas.drawText(item.info,point.x+maxWidth/2,point.y+otherPaint.getTextSize(),otherPaint);
      }
    }else if(point.y>=p3.y){
      //3
      LogUtils.d("dding","第三区域");
      canvas.drawText(item.name,point.x,point.y-fontMetrics.ascent,otherPaint);
      canvas.drawText(item.info,point.x,point.y-fontMetrics.ascent+lineHeight,otherPaint);
    }else if(point.x<p4.x){
      LogUtils.d("dding","第四区域");
      //4
      if(point.x-maxWidth<-harf){

        float x =  (point.x - maxWidth/2)>=-harf?point.x:(point.x-(point.x-maxWidth/2+harf));
        canvas.drawText(item.name,x,point.y-lineHeight+fontMetrics.leading,otherPaint);
        canvas.drawText(item.info,x,point.y-fontMetrics.descent,otherPaint);

      }else{
        canvas.drawText(item.name,point.x-maxWidth/2,point.y,otherPaint);
        canvas.drawText(item.info,point.x-maxWidth/2,point.y+lineHeight,otherPaint);
      }
    }
  }


  private void drawSelectShade(Canvas canvas, float angle){
    //画阴影
    float dy = (float) (Math.sin((angle) * Math.PI / 180)*25);
    float dx = (float) (Math.cos((angle) * Math.PI / 180)*25);
    canvas.save();
    canvas.translate(dx,dy);
    int i = canvas.saveLayer(rectF, otherPaint, saveFlag);

    otherPaint.setColor(Color.DKGRAY);
    canvas.drawArc(rectF,currentItem.startPos,currentItem.swipe,true,otherPaint);

    otherPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    canvas.drawCircle(0,0,innerRadius,otherPaint);
    otherPaint.setXfermode(null);
    canvas.restoreToCount(i);
    canvas.restore();
  }

  private void buildShader(PieChartItem item){
    if(item.shadeColors != null && item.shadeColors !=null && item.shadeColors.length>0){

      int length = item.shadeColors.length;
      float[] floats = new float[length];
      float v = 1.f / length;
      if(length == 1){
        item.drawShader = new RadialGradient(0, 0, outRadius, new int[]{item.shadeColors[0],item.shadeColors[0]}, new float[]{0,1}, Shader.TileMode.CLAMP);
      }else{
        for(int index = 0;index<length;index++){
          floats[index] = index*v;
        }
        item.drawShader = new RadialGradient(0, 0, outRadius, item.shadeColors, floats, Shader.TileMode.CLAMP);
      }
    }
  }
  private void drawCell(Canvas canvas,PieChartItem item){
    buildShader(item);
    cellPaint.setShader(item.drawShader);
    cellPaint.setColor(item.shadeColors[0]);
    canvas.drawArc(rectF,item.startPos,item.swipe,true,cellPaint);
    cellPaint.setShader(null);
  }

  private PointF getPoint(float r,float angle){
    PointF pointF = new PointF();

    float dy = (float) (Math.sin((angle) * Math.PI / 180)*r);
    float dx = (float) (Math.cos((angle) * Math.PI / 180)*r);
    pointF.x =dx;
    pointF.y = dy;

    return pointF;
  }

  private void drawCellBorder(Canvas canvas,PieChartItem item,int selectIndex){
    int index = itemList.indexOf(item);

    boolean isStart = false;
    boolean isEnd = false;

    if(!isNextSelectItem(index,selectIndex,itemList.size())){
      isEnd = true;
    }

    if(!isPreSelectItem(index,selectIndex,itemList.size())){
      isStart = true;
    }

    if(isStart){
      PointF point = getPoint(outRadius, item.startPos);
      canvas.drawLine(0,0,point.x,point.y,borderPaint);
    }

    if(isEnd){
      PointF point = getPoint(outRadius, item.startPos+item.swipe);
      canvas.drawLine(0,0,point.x,point.y,borderPaint);
    }
  }

  private boolean isNextSelectItem(int index,int selectIndex,int count){
    return index+1 == selectIndex || (index+1) == count+selectIndex;
  }

  private boolean isPreSelectItem(int index,int selectIndex,int count){
    return index-1==selectIndex || index-1 == selectIndex-count;
  }

  private PieChartItem currentItem;


  @Override public boolean onTouchEvent(MotionEvent event) {

  /*  if(itemList == null || itemList.size() <=1){
      return super.onTouchEvent(event);
    }*/
    int action = event.getAction();

    if(action == MotionEvent.ACTION_DOWN){

      float x = event.getX() - getMeasuredWidth()/2;
      float y = event.getY() - getMeasuredHeight()/2;

      // 和圆心连线  到圆心距离大于内半径 小于外半径 ，计算弧度 判断落入的部分

      double touchR =  Math.sqrt(Math.pow(x,2)+Math.pow(y,2));

      if(touchR < innerRadius || touchR>outRadius ){
        if(currentItem != null){
          currentItem = null;
          invalidate();
        }
        return false;
      }

      double touchAngle = Math.atan2(y, x) * 180 / Math.PI;

      if(touchAngle<0){
        touchAngle+=360;
      }

      int index = 0;
      for(PieChartItem item:itemList){
        float start = item.startPos % 360;
        if(start<0){
          //start=start+360;
        }
        float end = start+(item.swipe);
        if(touchAngle>start && touchAngle<end || touchAngle+360>start && touchAngle+360<end){
          currentItem = item;
          invalidate();
          return true;
        }
        index++;
      }
      if(currentItem != null){
        currentItem = null;
        invalidate();
      }
    }
    return super.onTouchEvent(event);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int mode = MeasureSpec.getMode(heightMeasureSpec);
    if(mode != MeasureSpec.EXACTLY){
      setMeasuredDimension(MeasureSpec.makeMeasureSpec(getMeasuredWidth(),MeasureSpec.EXACTLY),
          MeasureSpec.makeMeasureSpec(getMeasuredWidth(),MeasureSpec.EXACTLY));
    }else{
      setMeasuredDimension(MeasureSpec.makeMeasureSpec(getMeasuredWidth(),MeasureSpec.EXACTLY),
          heightMeasureSpec);
    }

    initSize();
  }

  private void initSize(){

    maxRadius = Math.min(getMeasuredWidth(), getMeasuredHeight()) * 0.54f /2;
    innerRadius = maxRadius * 0.2f;
    outRadius = maxRadius -10;

    rectF = new RectF();
    rectF.left = -outRadius;
    rectF.top = -outRadius;
    rectF.right = outRadius;
    rectF.bottom = outRadius;
    //Log.d("dding", "---onMeasure:" + getMeasuredWidth() + "  " + getMeasuredHeight());
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    //Log.d("dding", "---onSize:" + w + "  " + h);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
  }

  private float animationValue = 0;
  public void setAnimationRangle(float rangle){
    //LogUtils.d("dding","------animator ---- >"+rangle);
    animationValue = rangle;
    invalidate();
  }

  public void setData(List<PieChartItem> pieChartItemList) {
    this.itemList = pieChartItemList;

    ObjectAnimator animator = ObjectAnimator.ofFloat(this, "animationRangle", 360);
    animator.setDuration(300);
    animator.setInterpolator(new LinearInterpolator());
    animator.setStartDelay(500);
    animator.start();

    Runnable runnable = new Runnable(){

      @Override public void run() {

      }
    };

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      postOnAnimation(runnable);
    }else{
      post(runnable);
    }
  }


  private RectF outRectF(RectF rectF,float out){
    return new RectF(rectF.left-out,rectF.top-out,rectF.right+out,rectF.bottom+out);
  }

  public void setSelect(int index){
    currentItem = null;
    if(itemList == null || itemList.size() == 0){
      return;
    }
    if(index<0 || index>itemList.size()){
      return;
    }

    currentItem = itemList.get(index);
    invalidate();
  }

  public static class PieChartItem {
    public String name;//名称
    public String info;//信息
    public int[] shadeColors;//颜色
    public float startPos;//开始绘制位置
    public float swipe;//绘制总长
    private Shader drawShader;//
  }

}
