package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.PartialLoadView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.InvestItemVo;
import java.util.List;

/**
 * Created by dingeryue on 2016年11月24.
 */
public class OptimalListView extends LinearLayout {

  public OptimalListView(Context context) {
    this(context,null);
  }

  public OptimalListView(Context context, AttributeSet attrs) {
    this(context, attrs,-1);
  }

  public OptimalListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public OptimalListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initView();
  }

  private SparseArray<OptimalItemView> allItemViews = new SparseArray<>();
  private List<InvestItemVo> datas;
  private PartialLoadView loadView;
  private View dividerView;

  private OptimalListViewListener listener;
  private int dividerHeight;//item间隔

  private void initView(){
    setOrientation(LinearLayout.VERTICAL);

    dividerHeight = getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px);

    dividerView = new View(getContext());
    dividerView.setBackgroundColor(getResources().getColor(R.color.boc_divider_line_color));
    //addView(dividerView,new LinearLayout.LayoutParams(-1,getResources().getDimensionPixelSize(R.dimen.boc_divider_1px)));

    loadView = new PartialLoadView(getContext());
    loadView.setScaleType(ImageView.ScaleType.CENTER);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,getResources().getDimensionPixelOffset(R.dimen.boc_space_between_220px));
    addView(loadView,params);

    loadView.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        v.setOnClickListener(clickListener);
      }
    });
  }

  private OnClickListener clickListener = new OnClickListener() {
    @Override public void onClick(View v) {
      if(listener == null)return;
      loadView.setOnClickListener(null);
      loadView.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
      listener.onRefrensh();
    }
  };

  private OnClickListener itemClickListener = new OnClickListener() {
    @Override public void onClick(View v) {
      OptimalItemView itemView = (OptimalItemView) v;
      InvestItemVo data = itemView.getData();
      Integer pos = (Integer) itemView.getTag();
      if(listener != null){
        listener.onItemClick(pos,data);
      }
    }
  };

  private OptimalItemView.RefrenshListener refrenshListener = new OptimalItemView.RefrenshListener() {
    @Override public void onRefrensh(View view,InvestItemVo investItemVo) {
      if(listener != null){

        listener.onItemRefrensh((Integer) view.getTag(),investItemVo);
      }

    }
  };

  public void updateDatas(List<InvestItemVo> investItemVos){
    this.datas = investItemVos;
    int n = datas == null?0:datas.size();

    if(n>0){
      setLoadingState(false,false);
    }

    OptimalItemView optimalItemView;
    int index = 0;
    for(;;index++){
      if(index>=n)break;

      if(index>=allItemViews.size()){
        allItemViews.append(index,new OptimalItemView(getContext()));
      }

      optimalItemView = allItemViews.get(index);

      if(optimalItemView.getParent() == null){
        LayoutParams params = new LayoutParams(-1, -2);
        params.topMargin = index==0?0:dividerHeight;
        addView(optimalItemView,params);
      }
      updateItemView(optimalItemView,index,datas.get(index));
    }

    if(allItemViews.size()>n){
      for(int i = n;index<allItemViews.size();i++){
       removeView(allItemViews.get(i));
      }
    }
  }

  public void updateItemData(InvestItemVo vo,int pos){
    allItemViews.get(pos).update(vo);
  }

  /*
   * 设置loading状态
   */
  public void setLoadingState(boolean isVisiable,boolean isLoading){

    loadView.setLoadStatus(isLoading? PartialLoadView.LoadStatus.LOADING: PartialLoadView.LoadStatus.REFRESH);
    loadView.setVisibility(isVisiable?View.VISIBLE:View.GONE);

    if(isVisiable){
      setBackgroundColor(Color.WHITE);
    }else{
      setBackgroundColor(Color.TRANSPARENT);
    }

    if(isVisiable && !isLoading){
      loadView.setOnClickListener(clickListener);
    }else{
      loadView.setOnClickListener(null);
    }
  }

  private void updateItemView(OptimalItemView itemView,int index,InvestItemVo data){
    itemView.setOnClickListener(itemClickListener);
    itemView.setRefrenshListener(refrenshListener);
    itemView.update(data);
    itemView.setTag(index);
  }

  public void setViewListener(OptimalListViewListener listener){
    this.listener = listener;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawDivider(canvas);
  }

  private  Paint dividerPaint;
  private void drawDivider(Canvas canvas){
    if(dividerPaint == null){
      dividerPaint = new Paint();
      dividerPaint.setColor(getResources().getColor(R.color.boc_divider_line_color));
      dividerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
      dividerPaint.setStrokeWidth(getResources().getDimension(R.dimen.boc_divider_1px)*2);
    }
    if(loadView!=null && loadView.getVisibility() == View.VISIBLE){
      canvas.drawLine(0,0,getMeasuredWidth(),0,dividerPaint);
    }
  }

  public interface OptimalListViewListener{

    /**
     * 全部刷新
     */
    void onRefrensh();

    /**
     * 单条刷新
     * @param pos 位置
     * @param itemVo 数据
     */
    void onItemRefrensh(int pos,InvestItemVo itemVo);

    /**
     * 点击item
     * @param pos 位置
     * @param itemVo 数据
     */
    void onItemClick(int pos,InvestItemVo itemVo);
  }
}
