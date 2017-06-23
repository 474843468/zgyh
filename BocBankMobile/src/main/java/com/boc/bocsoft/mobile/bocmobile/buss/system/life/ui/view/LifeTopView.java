package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by eyding on 16/7/16.
 * main - 生活 导航view
 */
public class LifeTopView extends RelativeLayout{

  private TextView titleTv;
  private TextView locationTv;
  public LifeTopView(Context context) {
    this(context,null);
  }

  public LifeTopView(Context context, AttributeSet attrs) {
    this(context, attrs,-1);
  }

  public LifeTopView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }

  private void initView(){
    LayoutInflater.from(getContext()).inflate(R.layout.boc_view_main_life_nav,this,true);

    titleTv = (TextView) findViewById(R.id.tv_title);
    locationTv = (TextView) findViewById(R.id.tv_loaction);
  }

  public void setTitle(CharSequence title){
      titleTv.setText(title);
  }

  public void setLeftLoading(){
    locationTv.setText("loading...");
  }

  public void endLeftLoading(CharSequence left){
    locationTv.setText(left);
  }

  public void leftClick(OnClickListener clickListener){
    locationTv.setOnClickListener(clickListener);
  }
}
