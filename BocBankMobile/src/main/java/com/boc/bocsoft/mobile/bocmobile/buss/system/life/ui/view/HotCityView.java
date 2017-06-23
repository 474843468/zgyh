package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingeryue on 2016年08月23.
 */
public class HotCityView<T> extends ViewGroup implements View.OnClickListener {
  public HotCityView(Context context) {
    this(context,null);
  }

  public HotCityView(Context context, AttributeSet attrs) {
    this(context, attrs,-1);
  }

  public HotCityView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }


  private TextView tvTitle;

  private List<TextView> textViews;

  private int spaceCell;
  private int contentSize;
  private int contentColor;
  private int cellHeight;

  private void initView(){
    //LayoutInflater.from(getContext()).inflate(R.layout.boc_view_hotcity,this,true);
    int paddingLeft = getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px);
    int paddingRight = paddingLeft;
    int paddingBottom = getResources().getDimensionPixelSize(R.dimen.boc_space_between_50px);

    int titleHeight = getResources().getDimensionPixelSize(R.dimen.boc_space_between_76px);
    cellHeight = getResources().getDimensionPixelSize(R.dimen.boc_space_between_62px);

    int titleSize = getResources().getDimensionPixelSize(R.dimen.boc_text_size_little_big);
     contentSize = getResources().getDimensionPixelSize(R.dimen.boc_text_size_common);

    int titleColor = getResources().getColor(R.color.boc_text_color_dark_gray);
     contentColor = getResources().getColor(R.color.boc_text_color_common_gray);
    int bgColor = 0xfff0eff5;

    spaceCell = getResources().getDimensionPixelOffset(R.dimen.boc_space_between_20px);

    setPadding(paddingLeft,0,paddingRight,paddingBottom);
    setBackgroundColor(bgColor);

    tvTitle = getCellView(titleColor,titleSize,titleHeight,Gravity.CENTER_VERTICAL);
    setClickable(false);
    setFocusable(false);
  }

  private List<T> datas;
  public  void setTitle(int res){
    tvTitle.setText(res);
  }
  public void setTitle(CharSequence title){
    tvTitle.setText(title);
  }
  public void updateData(List<T> data){
    this.datas = data;
    removeAllViews();
    addView(tvTitle);

    initCell();
    requestLayout();
  }

  private void initCell(){
    if(datas == null || datas.size() == 0){
      return;
    }
    if(textViews == null){
      textViews = new ArrayList<>();
    }
    for(int index=0;index<datas.size();index++){
      if(index>textViews.size()-1){
        TextView cell = getCellView(contentColor,contentSize,cellHeight,Gravity.CENTER);
        textViews.add(index,cell);
        cell.setClickable(true);
        cell.setFocusable(true);
        cell.setBackgroundResource(R.drawable.bg_hotcity_selector);
      }
      addView(textViews.get(index));
      updateView(textViews.get(index),datas.get(index),index);
    }

  }

  public void updateView(TextView textView,T t,int pos){

    textView.setTag(Integer.valueOf(pos));
    textView.setOnClickListener(this);
  }

  private TextView getCellView(int titleColor, int titleSize,int height,int gravity){
    TextView textView = new TextView(getContext());
    textView.setGravity(gravity);
    textView.setMinHeight(height);
    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleSize);
    textView.setTextColor(titleColor);
    return textView;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    //每行3个

    tvTitle.measure(widthMeasureSpec,heightMeasureSpec);
    int height = tvTitle.getMeasuredHeight()+getPaddingTop()+getPaddingBottom();

    int itemWidth = ( getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - 2 * spaceCell )/3;

    if(datas != null && datas.size()>0){
      for(TextView textView:textViews){
        textView.measure(MeasureSpec.makeMeasureSpec(itemWidth,MeasureSpec.EXACTLY),heightMeasureSpec);
      }
      int itemHeight = textViews.get(0).getMeasuredHeight();

      int lineNum =  datas.size()/3 + (datas.size()%3==0?0:1);
      height = height+lineNum*itemHeight + (lineNum-1)*spaceCell;
    }
    setMeasuredDimension(widthMeasureSpec,MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    //
    int left = getPaddingLeft();
    int top = getPaddingTop();

    tvTitle.layout(left,top,left+tvTitle.getMeasuredWidth(),top+tvTitle.getMeasuredHeight());

    top = top+tvTitle.getMeasuredHeight();
    int childCount = getChildCount();
    if(childCount == 0 || childCount == 1)return;

    for(int index =1;index<childCount;index++){
      View childAt = getChildAt(index);
      int pleft =left+ (index-1)%3 * (childAt.getMeasuredWidth() + spaceCell);
      int pTop = top+ (index-1)/3* (childAt.getMeasuredHeight()+spaceCell);

      childAt.layout(pleft,pTop,pleft+childAt.getMeasuredWidth(),pTop+childAt.getMeasuredHeight());
    }

  }

  @Override public void onClick(View v) {
    Integer pos = (Integer) v.getTag();
    if(itemClickListener != null){
      itemClickListener.onItemClick(pos,datas.get(pos));
    }
  }

  private HotCityClickListener<T> itemClickListener;
  public void setItemClickListener(HotCityClickListener<T> itemClickListener){
    this.itemClickListener = itemClickListener;
  }

  public interface HotCityClickListener<T>{
    void onItemClick(int pos,T t);
  }
}
