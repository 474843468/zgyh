package com.boc.bocsoft.mobile.bocmobile.base.widget.trendview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 折线图
 * Created by dingeryue on 2016年09月16.
 *
 * update()设置数据
 * setParamBuilder（） 设置view参数
 * getParamBuilder（）
 *
 * getParamBuilder().dateFormat("yyyy-MM-dd").bottomDateFormat("yyyy\nMM-dd").selectDateFormat("yyyy-MM-dd")
 * .emptyTips("木有数据")
 * .selectPriceTips("钱:")
 * .selectDateTips("日期：")
 * .touchIndicatorEnable(true)
 * .touchTips("手指放上显示净值额")
 * .leftPriceScale(3);
 */
public class BocTrendView extends View {

  // 背景色
  private int backgroundColorId = R.color.boc_titlebar_bg_white;

  public BocTrendView(Context context) {
    this(context,null);
  }

  public BocTrendView(Context context, AttributeSet attrs) {
    this(context, attrs,-1);
  }

  public BocTrendView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public BocTrendView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initView();
  }



  private Paint textPaint;//文字画笔
  private Paint linePaint;
  private Paint otherPaint;
  private float bottomTextSize;//底部和左侧文字大小
  private float infoTextSize;//选中信息的文字大小
  private int colorBottomText;//底部文字颜色
  private int colorInfoText;
  private int colorTrendline;//趋势线颜色
  private int colorIndicator; // 十字线颜色

  private int touchSlop;
  private BocTrendView.TrendViewParamBuilder builder;

  private void initView(){
    touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    // 设置背景
    setBackgroundResource(backgroundColorId);
    //setBackgroundColor(0x22000000);

    bottomTextSize = getResources().getDisplayMetrics().density*12;
    infoTextSize = bottomTextSize;
    colorBottomText = 0xff888888;
    colorInfoText = 0xffefefef;
    // 趋势线颜色
    colorTrendline = Color.rgb(78,132,241);
    colorIndicator = Color.rgb(49, 178, 122);

    textPaint = new Paint();
    textPaint.setColor(Color.BLUE);
    textPaint.setTextSize(bottomTextSize);
    textPaint.setAntiAlias(true);
    textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    textPaint.setStrokeWidth(1);

    linePaint = new Paint();
    linePaint.setAntiAlias(true);
    linePaint.setStrokeWidth(1);
    linePaint.setStyle(Paint.Style.STROKE);

    otherPaint = new Paint();
    otherPaint.setAntiAlias(true);
    otherPaint.setStyle(Paint.Style.STROKE);
    otherPaint.setStrokeWidth(1);

    setParamBuilder(new TrendViewParamBuilder());

/*   getParamBuilder().dateFormat("yyyy-MM-dd").bottomDateFormat("yyyy\nMM-dd").selectDateFormat("yyyy-MM-dd")
        .emptyTips("木有数据")
        .selectPriceTips("钱:")
        .selectDateTips("日111期：")
        .touchIndicatorEnable(true)
        .touchTips("手指放上显示净值额")
        .leftPriceScale(3);*/
   /* setDateFormat(new DataFormatInterface() {
      @Override public String fortmatDate(String ins) {
        return "09-08";
      }
    });
    setSelectTipsValue("jiage:","shijian");
    update(demo());*/
    //update(demo());
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int mode = MeasureSpec.getMode(heightMeasureSpec);
    int h = (int) (MeasureSpec.getSize(widthMeasureSpec) *0.75f);
    int height = MeasureSpec.getSize(heightMeasureSpec);
    switch (mode) {
      case MeasureSpec.AT_MOST:
        if(h>height){
          h = height;
        }
        break;
      case MeasureSpec.UNSPECIFIED:
        break;

      case MeasureSpec.EXACTLY:
        h = height;
        break;
    }
    setMeasuredDimension(widthMeasureSpec,MeasureSpec.makeMeasureSpec(h,MeasureSpec.EXACTLY));
  }



  int hNum = 6;//横线
  int vNum = 7;//竖线

  private int lineLeft;//线左侧
  private int lineRight;//线右侧
  private int lineTop;//上线
  private int lineBottom;//下线
  private int top;

  private float lineDy;
  private float lineDx;

  private float[] xPos;//底部x分隔坐标点
  private float[] yPos;//左侧y分隔坐标点

  private int selectPos;
  private boolean hasTouchDown = false;
  private boolean isCanSelectPos = false;


  private float downX;
  private float donwY;
  private float currentX;//当前触摸点x
  private float currentY;//当前触摸点y

  private final String MAX = "8888888888888888";
  private boolean isNeedInitDrawData = true;
  private void initDrawData(){
    if(!isNeedInitDrawData){
      return;
    }
    textPaint.setTextSize(bottomTextSize);
    float v = textPaint.measureText("8."+MAX.substring(0,Math.max(builder.leftPriceScale,0)));
    if(leftTexts!=null && leftTexts.length>0){
      for(int index=0;index<leftTexts.length;index++){

        if(leftTexts[index] == null)continue;

        String[] split = leftTexts[index].toPlainString().split("\n");
        if(split!= null && split.length>0){
          for(String sub:split){
            v = Math.max(v, textPaint.measureText(sub));
          }
        }
      }
    }

    int offset = (int) (v+0.5f);

    lineLeft = getPaddingLeft() +offset+10;//预留文字宽度
    lineRight = getMeasuredWidth() - getPaddingRight() - (int)(textPaint.measureText("88-88")/2)-20;
    Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
    float v1 =  fontMetrics.bottom - fontMetrics.top;

    int lineSize = 1;
    try {
      if (listvos != null && listvos.size() > 0) {
        TrendVo trendVo = listvos.get(0);
        lineSize = trendVo.formatDate.split("\n").length;
      }
    }catch (Exception e){}

    lineBottom = (int) (getMeasuredHeight() - getPaddingBottom() - v1*(lineSize) - 10);
    lineTop = getPaddingTop() + 40;
    top = getPaddingTop();

    lineDy = (lineBottom - lineTop )*1.f/(hNum-1);
    lineDx = (lineRight - lineLeft)*1.f/(vNum-1);

    xPos = new float[vNum];
    for(int index =0;index<vNum;index++){
      //水平
      xPos[index] = lineLeft +lineDx*index;
    }

    yPos = new float[hNum];
    for(int index =0;index<hNum;index++){
      //竖直
      yPos[index] = lineBottom - lineDy*index;
    }
    //计算绘制的点

    if(listvos == null || listvos.size() == 0)return;
    int index =0;
    final List<TrendVo> list = listvos;
    list.get(0).x = xPos[0];
    list.get(0).y =  0;

    List<TrendVo> tmp = new ArrayList<>();
    float x = 0;
    int startIndex = -1;//第几个空
    for(TrendVo item:list) {
      if (item.isBottomDivider) {

        makeSureSpaceXPos(tmp, x);//遇到分隔线绘制上一个空格内容
        tmp.clear();

        startIndex++;//遇到分隔线开始下一个空格
        x = xPos[startIndex];//下一个空格x起始位置
        item.x = x;
        //TODO 计算y
        item.y = getPosY(item.priceBigDecimal);
      } else {
        tmp.add(item);
      }
    }
    //LogUtils.d("dding","绘制时数据:"+listvos);
  }

  private void makeSureSpaceXPos(List<TrendVo> tmp,float start){
    if(tmp == null || tmp.size() == 0)return;
    float dx = lineDx*1.f/(tmp.size()+1);

    for(TrendVo TrendVo:tmp){
      TrendVo.x = start+dx;
      start=TrendVo.x;
      //TODO计算y
      TrendVo.y = getPosY(TrendVo.priceBigDecimal);
    }
  }

  private float getPosY(BigDecimal input){

    //计算y

    //差值
    BigDecimal dyBigDecimal = leftTexts[leftTexts.length-1].subtract(leftTexts[1]);

   for(int index =1;index<leftTexts.length;index++){
     if(input.compareTo(leftTexts[index]) == 0)return yPos[index];
   }

    //计算

    float totalDy = yPos[1] -  yPos[yPos.length-1];

    float v = input.subtract(leftTexts[1]).divide(dyBigDecimal,4).floatValue();
   return yPos[1] - v * totalDy;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if(builder == null)return;

    initDrawData();
    drawBg(canvas);

    if(listvos == null || listvos.size()==0){
      drawEmptyTips(canvas);
      return;
    }

    //文字
    drawText(canvas);

    //drawPoint(canvas);
    //连点呈线
    drawPointLine(canvas);

    TrendVo TrendVo = listvos.get(selectPos);

    //Log.d("dding","draw xx "+hasTouchDown+"  "+isCanSelectPos+"  "+selectPos);
    if(hasTouchDown && isCanSelectPos){
      //画十字线
      drawCross(canvas,TrendVo.x,TrendVo.y);
      drawSelectRectInfo(canvas,TrendVo);
    }
    drawSelectCircle(canvas,TrendVo.x,TrendVo.y);
    drawTouchTips(canvas);

    canvas.restore();
  }


  //画背景网格
  private void drawBg(Canvas canvas){
    linePaint.setColor(Color.LTGRAY);
    //纵6 横7
    Path linePath = new Path();
    //横
    for(int index=0;index<hNum;index++){
      linePath.moveTo(lineLeft,yPos[index]);
      linePath.lineTo(lineRight,yPos[index]);
    }

    //竖
    for(int index=0;index<vNum;index++){
      linePath.moveTo(xPos[index],yPos[0]);
      linePath.lineTo(xPos[index],top);
    }
    canvas.drawPath(linePath,linePaint);
  }

  private void drawText(Canvas canvas){

    textPaint.setTextAlign(Paint.Align.LEFT);
    textPaint.setTextSize(bottomTextSize);
    textPaint.setColor(colorBottomText);
    int left = getPaddingLeft();
    //竖方向文字
    for(int index=0;index<leftTexts.length;index++){
      canvas.drawText(leftTexts[index] == null?"":leftTexts[index].toPlainString(),left,lineBottom-lineDy*index+textPaint.getTextSize()/2,textPaint);
    }

    textPaint.setTextAlign(Paint.Align.CENTER);
    for(int index = 0;index<bottomTexts.length;index++){
      String text = bottomTexts[index];
      if(text == null)continue;
      String[] split = text.split("\n");
      if(split!=null && split.length>1){
        for(int curr =0;curr<split.length;curr++){
          canvas.drawText(split[curr],xPos[index],yPos[0]+textPaint.getTextSize()+3*curr + bottomTextSize*curr,textPaint);
        }
      }else{
        canvas.drawText(text,xPos[index],yPos[0]+textPaint.getTextSize()+3,textPaint);
      }
    }

    textPaint.setTextAlign(Paint.Align.LEFT);

  }

  private void drawPoint(Canvas canvas){
    otherPaint.setStyle(Paint.Style.FILL);
    for(TrendVo TrendVo:listvos){
      canvas.drawCircle(TrendVo.x,TrendVo.y,3,otherPaint);
    }
  }

  private void drawPointLine(Canvas canvas) {
    Path path = new Path();
    boolean isReset =false;
    for(TrendVo item:listvos){
      if(!isReset){
        path.reset();
        path.moveTo(item.x,item.y);
        isReset = true;
      }else{
        path.lineTo(item.x,item.y);
      }
    }

    linePaint.setColor(colorTrendline);
    canvas.drawPath(path, linePaint);

    // 设置填充色
    otherPaint.setColor(Color.argb( 200,195, 214, 242));
    otherPaint.setStyle(Paint.Style.FILL);
    Path ppp = new Path();
    ppp.addPath(path);
    ppp.moveTo(lineRight,listvos.get(listvos.size()-1).y);
    ppp.lineTo(lineRight,lineBottom);
    ppp.lineTo(lineLeft,lineBottom);
    ppp.lineTo(lineLeft,listvos.get(0).y);
    canvas.drawPath(ppp,otherPaint);
  }

  private void drawCross(Canvas canvas,float x,float y){
    //Log.d("dding","---十字线:"+x+"  "+y);
    otherPaint.setColor(colorIndicator);
    otherPaint.setStrokeWidth(1);
    canvas.drawLine(0,y,lineRight,y,otherPaint);
    canvas.drawLine(x,0,x,lineBottom,otherPaint);
  }

  private void drawSelectRectInfo(Canvas canvas,TrendVo TrendVo){
    if(TrendVo == null)return;
    String line1 = builder.selectPriceTips+TrendVo.price;
    String line2 =builder.selectDateTips+getDateValue(builder.dateFormatSelect,builder.dateFormatDate,TrendVo.date);
    textPaint.setTextSize(infoTextSize);
    float max = Math.max(textPaint.measureText(line1), textPaint.measureText(line2)) + 20;
    float left = TrendVo.x - max/2;
    float top = 0;

    float h = textPaint.getTextSize()*2 + 10 +10+10;
    otherPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    otherPaint.setColor(0x660000ff);
    textPaint.setColor(colorInfoText);
    RectF rectF = new RectF(left, top, left + max, top + h);
    if(rectF.left<lineLeft){
      rectF.offset(lineLeft-rectF.left,0);
    }

    if(rectF.right>lineRight){
      rectF.offset(lineRight-rectF.right,0);
    }
    canvas.drawRoundRect(rectF,10,10,otherPaint);
    canvas.drawText(line1,rectF.left+5,top+textPaint.getTextSize()+10,textPaint);
    canvas.drawText(line2,rectF.left+5,top+h-10,textPaint);
  }


  /**
   * 画选中的圆圈
   * @param cancas
   * @param x
   * @param y
   */
  private void drawSelectCircle(Canvas cancas,float x,float y){

    otherPaint.setStyle(Paint.Style.STROKE);
    otherPaint.setColor(Color.BLUE);
    otherPaint.setStrokeWidth(4);
    cancas.drawCircle(x,y,8,otherPaint);
    otherPaint.setStyle(Paint.Style.FILL);
    otherPaint.setColor(Color.WHITE);
    cancas.drawCircle(x,y,8,otherPaint);
  }


  private void drawEmptyTips(Canvas canvas){
    textPaint.setTextAlign(Paint.Align.CENTER);
    canvas.drawText(builder.emptyTips==null?"":builder.emptyTips.toString(),lineLeft+(lineRight-lineLeft)/2,getMeasuredHeight()/2,textPaint);
  }

  private void drawTouchTips(Canvas canvas){
    if(builder.touchIndicatorEnable && listvos!=null && listvos.size()>0 && !hasTouchDown){
      textPaint.setColor(Color.WHITE);
      textPaint.setTextAlign(Paint.Align.CENTER);
      canvas.drawText(builder.touchTips==null?"":builder.touchTips.toString(),lineLeft+(lineRight-lineLeft)/2,lineBottom-textPaint.getTextSize(),textPaint);
    }
  }

  private List<TrendVo> listvos;
  private BigDecimal[] leftTexts;
  private String[] bottomTexts;
  private int MULT_RATE = 100000;//放大速率 ， 将float值转换成int值进行处理

  /**
   * @param listVos
   * TrendVo 数据
   */
  public void update(List<TrendVo> listVos){
    this.listvos = listVos ;//sort 以日期进行排序
    this.isNeedInitDrawData = true;
    if(builder == null){
      return;
    }

     hNum = 6;//横线
     vNum = 7;//竖线

    selectPos = 0;

    LogUtils.d("dding","---处理前数据:"+listVos);

    if(listVos == null || listVos.size() == 0){
      invalidate();
      return;
    }

    if(listVos.size() == 1){
      TrendVo trendVo = new TrendVo();
      trendVo.setDate(listVos.get(0).date);
      trendVo.setPrice(listVos.get(0).price);
      listVos.add(trendVo);
    }
    //处理数据
    for(TrendVo item:listVos){

      item.intPrice = (int) (parseFloat(item.price)*MULT_RATE);
      item.priceBigDecimal = parseBigDecimal(item.price);

      //TODO 处理日期
      item.formatDate = getDateValue(builder.dateFormatBottom,builder.dateFormatDate,item.date);
   /*   if(builder.dateFormat != null){
        item.formatDate = builder.dataFormatInterface.fortmatDate(item.date);

      }else if(item.date!= null && item.date.length()>=5){
        item.formatDate = item.date.substring(5);
      }else{
        item.formatDate = item.date;
      }*/
    }

    Collections.sort(listVos, new Comparator<TrendVo>() {
      @Override public int compare(TrendVo lhs, TrendVo rhs) {
        return lhs.date.compareTo(rhs.date);
      }
    });


    LogUtils.d("dding","--- 处理后数据:"+listVos);

    //横坐标显示规则 起始 结束时间， 中间5档平分
    //宗坐标显示规则 0 最低 最高，中间三档平分

    //计算绘制的文字
    leftTexts = new BigDecimal[hNum];


    BigDecimal max = listVos.get(0).priceBigDecimal;
    BigDecimal min= listVos.get(0).priceBigDecimal;
    for(TrendVo TrendVo:listVos){
      if(TrendVo.priceBigDecimal.compareTo(max)>0){
        max = TrendVo.priceBigDecimal;
      }
      if(TrendVo.priceBigDecimal.compareTo(min)<0){
        min = TrendVo.priceBigDecimal;
      }
    }
    if(min.compareTo(max) == 0){
      min = min.subtract(BigDecimal.ONE);
      max = max.add(BigDecimal.ONE);
    }
    BigDecimal subtract = max.subtract(min);

    //差值均分  从 index 1 到尾部(index-1)
    BigDecimal divide = subtract.divide(new BigDecimal(hNum -1 -1));

    leftTexts[0] = null;//0 位置为空
    leftTexts[1] = min.setScale(builder.leftPriceScale,BigDecimal.ROUND_DOWN);//最小值
    leftTexts[leftTexts.length-1] = max.setScale(builder.leftPriceScale,BigDecimal.ROUND_UP);//最大

    LogUtils.d("dding",divide+"数值数据:"+leftTexts);
    //中间值
    for(int index=2;index<leftTexts.length-1;index++){
      leftTexts[index] = (leftTexts[index-1].add(divide).setScale(builder.leftPriceScale,BigDecimal.ROUND_HALF_UP));
    }


    if(listVos.size()<vNum){
      vNum = listVos.size();
    }
    //水平数据

    bottomTexts = new String[vNum];

      //中间分隔
      int avSize = listVos.size() - vNum;//每条线处一条数据 剩余数据平分到空格内
      if(avSize<=0){
        for(int index =0 ;index<listVos.size();index++){
          bottomTexts[index] = listVos.get(index).formatDate;
          listVos.get(index).isBottomDivider = true;
        }
      }else{

        int i = avSize / (vNum-1);//2
        int k = avSize%(vNum-1);//0
        int curr = 0;
        int lastIndex = 0;
        LogUtils.d("dding"," 每个空格："+i+"  于:"+k);
        for(int index =0;index<listVos.size();index++){

          if(index==0){
            listVos.get(0).isBottomDivider = true;
            bottomTexts[0]=listVos.get(0).formatDate;
            curr = 1;
            continue;
          }

          if(k>0 && index%(i+1+1) == 0){
            listVos.get(index).isBottomDivider = true;
            bottomTexts[curr]=listVos.get(index).formatDate;
            curr++;
            k--;
            lastIndex = index;
          }else if(k==0 && (index-lastIndex)%(i+1)==0){
            //
            listVos.get(index).isBottomDivider = true;
            bottomTexts[curr]=listVos.get(index).formatDate;
            curr++;
          }
        }

      }

    selectPos = listVos.size()-1;

    LogUtils.d("dding","--- 处理后数据222222:"+listVos);

    invalidate();
  }

  /**
   * 设置指示器是否可以用 ,
   * @param enable true 可用触摸时显示十字
   * @deprecated
   */
  public void setTouchIndicatorEnable(boolean enable){
    builder.touchIndicatorEnable(enable);
  }




  @Override public boolean onTouchEvent(MotionEvent event) {

    if(!builder.touchIndicatorEnable || listvos == null || listvos.size() == 0){
      return super.onTouchEvent(event);
    }

    int i = event.getAction() * MotionEvent.ACTION_MASK;
    final int actionMasked = MotionEventCompat.getActionMasked(event);
    switch (actionMasked){
      case MotionEvent.ACTION_DOWN:

        currentX = downX = event.getX();
        currentY= donwY = event.getY();
        if(downX<lineLeft || downX>lineRight){
          //非有效区域 不处理
          hasTouchDown = false;
          return  false;
        }
        hasTouchDown = true;
        postDownRunable();

        return true;
      case MotionEvent.ACTION_MOVE:
        currentX = event.getX();
        currentY = event.getY();

        if(!isCanSelectPos && hasTouchDown && Math.abs(currentY - donwY)> touchSlop){
          //滑动
          removeDownRunable();
          return false;
        }

        if(hasTouchDown){
          getSelectPos();
          return true;
        }
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        //Log.d("dding","----touch  up" + hasTouchDown+"  "+isCanSelectPos);
        if(hasTouchDown && isCanSelectPos){
          resetDefaultSelect();
          hasTouchDown = false;
          isCanSelectPos = false;
          invalidate();
          getParent().requestDisallowInterceptTouchEvent(false);
          return true;
        }else if(hasTouchDown){
          removeDownRunable();
        }
        return  false;
    }

    return super.onTouchEvent(event);
  }

  private void postDownRunable(){
    postDelayed(downRunable,400);
  }

  private void removeDownRunable(){
    hasTouchDown = false;
    isCanSelectPos = false;
    removeCallbacks(downRunable);
  }

  private void resetDefaultSelect(){
    selectPos = listvos == null?0:listvos.size()-1;
  }

  private Runnable downRunable = new Runnable() {
    @Override public void run() {
       //获取触摸点  计算选中位置
      if(!hasTouchDown)return;
      getParent().requestDisallowInterceptTouchEvent(true);
      isCanSelectPos = true;
      getSelectPos();
    }
  };

  private void getSelectPos(){
    if(!isCanSelectPos)return;

    final float x = currentX;

    if(x<lineLeft){
      selectPos = 0;
    }else if(x>lineRight){
      selectPos = listvos.size()-1;
    }else{
      //根据x判断
      TrendVo vo;
      int left = 0;
      int right = listvos.size()-1;
      for(int index=0;index<listvos.size();index++){
        vo = listvos.get(index);
        if(vo.x<x){
          left = index;
        }else if(vo.x>x){
          right = index;
          break;
        }
        if(x - listvos.get(left).x<=listvos.get(right).x - x){
          selectPos = left;
        }
      }
    }
    invalidate();
  }

  private int getBottomDividerPos(TrendVo trendVo){
    if(bottomTexts.length<listvos.size()){
      return -1;
    }
    int len = bottomTexts.length;
    int index = listvos.indexOf(trendVo);
    int count = listvos.size();
    if(count == 1){
      return len-1;
    }

    if(count == 2){
      return index==0?0:len-1;
    }

    if(index==0)return 0;
    if(index == listvos.size()-1)return len-1;


    return -1;

  }

  public static class TrendVo{
     String date;//日期  格式 2016-08-19
     String price;//净值

    private int intPrice;
    private BigDecimal priceBigDecimal;
    private boolean isBottomDivider = false;
    private String formatDate;
    private float x;
    private float y;

    public void setDate(String date) {
      this.date = date;
    }

    public void setPrice(String price) {
      this.price = price;
    }

    @Override public String toString() {
      return "TrendVo{" +
          "date='" + date + '\'' +
          ", price='" + price + '\'' +
          ", intPrice=" + intPrice +
          ", priceBigDecimal=" + priceBigDecimal +
          ", isBottomDivider=" + isBottomDivider +
          ", formatDate='" + formatDate + '\'' +
          ", x=" + x +
          ", y=" + y +
          '}';
    }
  }


  private List<TrendVo> demo(){
    List<TrendVo> TrendVos = new ArrayList<>();

    //TrendVos.add(newInstanceVO("2016-08-17","1.2000"));
    TrendVos.add(newInstanceVO("2016-08-18","1.1950"));

    TrendVos.add(newInstanceVO("2016-08-19","1.2010"));
    TrendVos.add(newInstanceVO("2016-08-22","1.1930"));

    TrendVos.add(newInstanceVO("2016-08-23","1.1890"));

    TrendVos.add(newInstanceVO("2016-08-24","1.1910"));
    TrendVos.add(newInstanceVO("2016-08-25","1.1930"));

    TrendVos.add(newInstanceVO("2016-08-26","1.2010"));

    TrendVos.add(newInstanceVO("2016-08-29","1.2020"));
    TrendVos.add(newInstanceVO("2016-08-30","1.1940"));

    TrendVos.add(newInstanceVO("2016-08-31","1.1930"));

    TrendVos.add(newInstanceVO("2016-09-01","1.2190"));
    TrendVos.add(newInstanceVO("2016-09-02","1.2140"));

    TrendVos.add(newInstanceVO("2016-09-05","1.1000"));

    TrendVos.add(newInstanceVO("2016-09-06","1.2900"));
    TrendVos.add(newInstanceVO("2016-09-07","1.2345"));

    TrendVos.add(newInstanceVO("2016\n09-08","1.1800"));

/*    TrendVos.add(newInstanceVO("2016-09-09","1.2345"));
    TrendVos.add(newInstanceVO("2016-09-12","1.2200"));

    TrendVos.add(newInstanceVO("2016-09-13","1.2345"));*/

    return TrendVos;
  }
  private TrendVo newInstanceVO(String date,String price){
    TrendVo TrendVo = new TrendVo();
    TrendVo.date = date;
    TrendVo.price = price;
    return TrendVo;
  }

  private float parseFloat(String input){
    try{
      return Float.parseFloat(input);
    }catch (Exception e){
      return 0f;
    }
  }

  private BigDecimal parseBigDecimal(String input){
    try {
      return new BigDecimal(input);
    }catch (Exception e){
      return BigDecimal.ZERO;
    }
  }

  public void setParamBuilder(TrendViewParamBuilder builder) {
    this.builder = builder;
    update(listvos);
  }

  public TrendViewParamBuilder getParamBuilder(){
    return builder;
  }

  public String getDateValue(SimpleDateFormat toFormat,SimpleDateFormat from,String date){
    try {
     return toFormat.format(from.parse(date));
    }catch (Exception e){
      return  date;
    }
  }


  public static class  TrendViewParamBuilder{

    /**
     * 触摸后 当前选中日期 （字段名）
     */
    private CharSequence selectDateTips = "时间:";
    /**
     * 触摸后显示净值 （字段名）
     */
    private CharSequence selectPriceTips = "单位净值(元):";
    /**
     * 数据为空时提示信息
     */
    private CharSequence emptyTips = "";
    /**
     * 触摸显示净值提示
     */
    private CharSequence touchTips = "手指移至曲线图上,可查看每日净值";

    /**
     * 触摸是否显示十字线
     */
    private boolean touchIndicatorEnable = true;

    /**
     * 左侧价格小数点后位数
     */
    private int leftPriceScale = 4;

    /**
     * 传入数据的 日期格式
     */
    private String dateFormat = "MM-dd";
    private SimpleDateFormat dateFormatDate = new SimpleDateFormat(dateFormat);
    /**
     * 底部日期格式
     */
    private String bottomDateFormat = "MM-dd";
    private SimpleDateFormat dateFormatBottom = new SimpleDateFormat(bottomDateFormat);
    /**
     * 选中提示中的日期格式
     */
    private String selectDateFormat = "MM-dd";
    private SimpleDateFormat dateFormatSelect = new SimpleDateFormat(selectDateFormat);

    //TODO 颜色 画笔，线条 网格 字体 等等设置


    public TrendViewParamBuilder selectDateTips(CharSequence selectDateTips){
      this.selectDateTips = selectDateTips;
      return this;
    }

    public TrendViewParamBuilder selectPriceTips(CharSequence selectPriceTips){
      this.selectPriceTips = selectPriceTips;
      return this;
    }

    public TrendViewParamBuilder emptyTips(CharSequence emptyTips){
      this.emptyTips = emptyTips;
      return this;
    }

    public TrendViewParamBuilder touchTips(CharSequence touchTips){
      this.touchTips = touchTips;
      return this;
    }

    public TrendViewParamBuilder touchIndicatorEnable(boolean touchIndicatorEnable){
      this.touchIndicatorEnable = touchIndicatorEnable;
      return this;
    }

    public TrendViewParamBuilder leftPriceScale(int leftPriceScale){
      this.leftPriceScale = leftPriceScale;
      return this;
    }


    public TrendViewParamBuilder dateFormat(String dateFormat){
      this.dateFormat = dateFormat;
      dateFormatDate = new SimpleDateFormat(dateFormat);
      return this;
    }

    public TrendViewParamBuilder bottomDateFormat(String bottomDateFormat){
      this.bottomDateFormat = bottomDateFormat;
      dateFormatBottom = new SimpleDateFormat(bottomDateFormat);
      return this;
    }

    public TrendViewParamBuilder selectDateFormat(String selectDateFormat){
      this.selectDateFormat = selectDateFormat;
      dateFormatSelect = new SimpleDateFormat(selectDateFormat);
      return this;
    }


  }

}
