package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import java.util.ArrayList;
import java.util.List;

public abstract class ChooseAdapter<T> extends BaseAdapter implements View.OnClickListener {

  private List<T> datas;
  private List<T> selectDatas;
  private List<T> currentSelectDatas = new ArrayList<>();

  private boolean isEdite = false;
  private ChooseCallBack chooseCallBack;

  public void update(List<T> datas) {
    this.datas = datas;
    notifyDataSetChanged();
  }

  public void update(List<T> datas,List<T> selectDatas){
    this.datas = datas;
    if(selectDatas == null){
      selectDatas = new ArrayList<>();
    }
    currentSelectDatas = new ArrayList<>(selectDatas);
    this.selectDatas = selectDatas;
  }


  @Override public int getCount() {
    return datas == null ? 0 : datas.size();
  }

  @Override public T getItem(int position) {
    return datas.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  public abstract void updateView(ItemView textView, T data, int pos);
  public void decorateView(ItemView textView) {

  }

  @Override public View getView(final int position, View convertView, final ViewGroup parent) {
    ItemView textView = (ItemView) convertView;
    if (textView == null) {
      textView = new ItemView(parent.getContext());
      decorateView(textView);
      textView.setOnClickListener(this);
    }
    textView.setTag(Integer.valueOf(position));
    updateView(textView, getItem(position), position);
    if(isEdit()){
      //
      textView.itemSelected(currentSelectDatas.indexOf(getItem(position))>=0?true:false);

      textView.setCompoundDrawables(textView.iconDrawable,null,textView.checkDrawable,null);
      //
    }else{
      textView.setCompoundDrawables(textView.iconDrawable,null,textView.rightDrawable,null);
    }
    textView.itemEdit(isEdit()).itemLast(position == getCount()-1);


    return textView;
  }

  public void setEdit(boolean isEdite){
    if(this.isEdite != isEdite){
      this.isEdite = isEdite;
      notifyDataSetChanged();
    }
  }
  public boolean isEdit(){
    return isEdite;
  }

  public int getCurrentSelectSize(){
    return currentSelectDatas.size();
  }

  @Override public int getViewTypeCount() {
    return 2;
  }

  @Override public int getItemViewType(int position) {
    return super.getItemViewType(position);
  }

  @Override public void onClick(View v) {
     Integer pos = (Integer) v.getTag();
    if(isEdit()){
      onCheckBoxClick((ItemView) v,pos);

    }else if(chooseCallBack != null){
      chooseCallBack.onItemClick(v,pos,datas.get(pos));
    }
  }

  private void onCheckBoxClick(ItemView itemView,int pos){
    if(chooseCallBack  == null || !chooseCallBack.onCheckChange(itemView.isSelected(),pos,datas.get(pos))){
      return;
    }

    itemView.setSelected(!itemView.isSelected());
    if(itemView.isSelected()){
      //被选中
      currentSelectDatas.add(datas.get(pos));
    }else{
      currentSelectDatas.remove(datas.get(pos));
    }
    if(chooseCallBack!=null){
      chooseCallBack.onAfterCheckChange();
    }
  }

  public void setChooseCallBack(ChooseCallBack<T> chooseCallBack) {
    this.chooseCallBack = chooseCallBack;
  }

  public List<T> getNewOrder() {
    if(selectDatas == null){
      return currentSelectDatas;
    }
    List<T> list = new ArrayList<>();
    final List<T> current = new ArrayList<>(currentSelectDatas);
    for(T t:selectDatas){
      if(current.indexOf(t)>=0){
        list.add(t);
        current.remove(t);
      }
    }
    list.addAll(current);

    currentSelectDatas = list;
    return list;
  }

  public interface  ChooseCallBack<T>{
    void onItemClick(View v,int pos,T data);

    /**
     *
     * @param oldCheck
     * @param pos
     * @param data
     * @return  返回true则允许改变
     */
    boolean onCheckChange(boolean oldCheck,int pos,T data);

    void onAfterCheckChange();
  }

  public static class ItemView extends TextView {

    final float checkboxTouchP = 0.7f;
    boolean isCan = false;//控制checkbox点击
    private boolean isEdit = false;
    private  boolean isLast = false;
    public int iconHeight = 0;
    public Drawable iconDrawable;
    public Drawable checkDrawable;
    public Drawable rightDrawable;
    private int height;

    private boolean isNotFullDivider = false;

    public ItemView(Context context) {
      super(context);
      initView();
    }

    private void initView() {

      TextView textView = this;
      Resources resources = this.getResources();

      int height = resources.getDimensionPixelSize(R.dimen.boc_space_between_88px);
      int paddingLeft = resources.getDimensionPixelSize(R.dimen.boc_space_between_32px);
      int paddingRight = resources.getDimensionPixelSize(R.dimen.boc_space_between_36px);
      int textSize = resources.getDimensionPixelSize(R.dimen.boc_text_size_common);

      textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
      itemHeight(height);
      textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
      textView.setTextColor(resources.getColor(R.color.boc_text_color_dark_gray));
      textView.setPadding(paddingLeft, 0, paddingRight, 0);
      textView.setCompoundDrawablePadding(paddingLeft);
      textView.setBackgroundColor(Color.WHITE);
      itemCheckDrawable(R.drawable.checkbox_bg);
    }

    public ItemView itemEdit(boolean isEdit){
      this.isEdit = isEdit;
      return this;
    }

    public ItemView itemLast(boolean islast){
      this.isLast = islast;
      return this;
    }
    public ItemView itemName(String name){
      setText(name);
      return  this;
    }

    public ItemView itemIcon(int res){

      Drawable drawable = getResources().getDrawable(res);
      drawable.setBounds(0,0,iconHeight,iconHeight);
      iconDrawable = drawable;
      return  this;
    }

    public ItemView itemCheckDrawable(int res){
      checkDrawable = getResources().getDrawable(res);
      checkDrawable.setBounds(0,0,checkDrawable.getIntrinsicWidth(),checkDrawable.getIntrinsicHeight());
      return this;
    }

    public ItemView itemRightDrawable(int res){
      rightDrawable = getResources().getDrawable(res);
      rightDrawable.setBounds(0,0,rightDrawable.getIntrinsicWidth(),rightDrawable.getIntrinsicHeight());
      return this;
    }

    public ItemView itemHeight(int h){
      height = h;
      setMinHeight(h);
      iconHeight = (int) (h*0.55f);
      return this;
    }

    public ItemView notFullDivider(boolean isNotFull){
      this.isNotFullDivider = isNotFull;
      return this;
    }

    public int itemHeight(){
      return height;
    }

    @Override public void setSelected(boolean selected) {
      super.setSelected(selected);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
      if (isEdit) {
        float x = event.getX();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          if (x > getMeasuredWidth() * checkboxTouchP) {
            isCan = true;
          }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
          if(isCan)performClick();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
          if (x < getMeasuredWidth() * checkboxTouchP) isCan = false;
        }
        return true;
      }
      return super.onTouchEvent(event);
    }

    Paint paint;

    Paint getLinePaint() {
      if (paint == null) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(0xffcbcad7);
        paint.setStrokeWidth(getResources().getDimension(R.dimen.boc_divider_1px));
      }
      return paint;
    }

    @Override public void draw(Canvas canvas) {
      super.draw(canvas);
      final Paint linePaint = getLinePaint();
      int left = getResources().getDimensionPixelSize(R.dimen.boc_space_between_28px);
      //canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint);

      if(!isNotFullDivider){

        canvas.drawLine(0, 0, getMeasuredWidth(), 0, linePaint);
        canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(),
            linePaint);

        return;
      }

     Integer pos = (Integer) this.getTag();

      if (pos!=null && pos == 0) {
        canvas.drawLine(0, 0, getMeasuredWidth(), 0, linePaint);
      } else {
        canvas.drawLine(left, 0, getMeasuredWidth(), 0, linePaint);
      }
      if (isLast) {
        canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint);
      } else {
        canvas.drawLine(left, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(),
            linePaint);
      }
    }

    public void itemSelected(final  boolean b) {
      post(new Runnable() {
        @Override public void run() {
          setSelected(b);
        }
      });
    }
  }
}