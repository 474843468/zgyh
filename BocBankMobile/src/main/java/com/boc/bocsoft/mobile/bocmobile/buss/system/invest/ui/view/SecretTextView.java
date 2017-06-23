package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by dingeryue on 2016年10月16.
 */

public class SecretTextView extends TextView {

  public SecretTextView(Context context) {
    super(context);
  }

  public SecretTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SecretTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public SecretTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  private boolean isSecret;
  private CharSequence value;

   public void setSecretText(CharSequence text) {
     this.value = text;
     setSecret(isSecret);
  }

  public void setSecret(boolean isSecret){
    this.isSecret = isSecret;
    if(isSecret){
      setText("****");
    }else{
      setText(value);
    }
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
  /*  if(isSecret){
      canvas.save();
      Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
      //大概计算下
      int offset = (int) (-fontMetrics.ascent /2 - fontMetrics.descent);
      canvas.translate(0,offset);
      super.onDraw(canvas);
      canvas.restore();
    }else{
      super.onDraw(canvas);
    }*/

  /*  Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();

    int offset = (int) (getBaseline()+fontMetrics.ascent);
    canvas.drawLine(0,offset,400,offset,getPaint());
    offset = (int) (getBaseline()+fontMetrics.descent);
    canvas.drawLine(0,offset,400,offset,getPaint());
    canvas.drawLine(0,getBaseline(),300,getBaseline(),getPaint());*/
  }



}
