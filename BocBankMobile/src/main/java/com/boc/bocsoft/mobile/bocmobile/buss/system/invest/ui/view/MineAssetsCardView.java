package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;

/**
 * Created by eyding on 16/7/9.
 *
 * 我的资产view
 */
public class MineAssetsCardView extends RelativeLayout implements View.OnClickListener {

  public MineAssetsCardView(Context context) {
    this(context, null);
  }

  public MineAssetsCardView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MineAssetsCardView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }

  private TextView tvInfo;
  private SecretTextView tvTotal;
  private ImageView ivInfo;
  private ImageView ivEye;


  private String money;
  private CharSequence moneyStyle;

  private void initView() {
    LayoutInflater.from(getContext()).inflate(R.layout.boc_view_invest_assetheader, this, true);

    ViewFinder viewFinder = new ViewFinder(this);
    tvInfo = viewFinder.find(R.id.tv_info);
    tvTotal = viewFinder.find(R.id.tv_money);
    ivInfo = viewFinder.find(R.id.iv_info);
    ivEye = viewFinder.find(R.id.iv_eye);

    ivInfo.setOnClickListener(this);
    ivEye.setOnClickListener(this);


    boolean spBoolean = SpUtils.getSpBoolean(getContext(), SpUtils.SPKeys.KEY_INVEST_SECRET, false);
    ivEye.setSelected(spBoolean);
    tvTotal.setSecret(spBoolean);
    setTotalAsset("20189.98");
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    ivEye.setBaseline(ivEye.getMeasuredHeight() - ivEye.getPaddingBottom());
  }

  @Override public void onClick(View v) {
    if(v == ivInfo){
      if(viewClickListener != null)viewClickListener.onTipsClick();
    }else if(v == ivEye){
      ivEye.setSelected(!ivEye.isSelected());
      SpUtils.saveBoolean(getContext(), SpUtils.SPKeys.KEY_INVEST_SECRET,ivEye.isSelected());
      updateMoneyUI();
    }
  }

  public void setTotalAsset(String total){
    money = total;
    //String s = MoneyUtils.transMoneyFormat(total, "001");

    moneyStyle = subSpan(money);

    updateMoneyUI();
  }

  private void updateMoneyUI(){
    tvTotal.setSecretText(moneyStyle);

    if(ivEye.isSelected()){
      tvTotal.setSecret(true);
    }else{
      tvTotal.setSecret(false);
    }
  }

  private CharSequence subSpan(String input){

    if(StringUtils.isEmptyOrNull(input)){
      input = "";
    }
    int index = input.indexOf(".");
    if(index<0)return input;

    SpannableString sp = new SpannableString(input);
    sp.setSpan(style,index,sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    return sp;
  }

  private CharacterStyle style = new CharacterStyle() {
    @Override public void updateDrawState(TextPaint tp) {
      tp.setTextSize(tp.getTextSize()*0.75f);
    }
  };


  private ViewClickListener viewClickListener;

  public void setViewClickListener(ViewClickListener viewClickListener) {
    this.viewClickListener = viewClickListener;
  }

  public interface  ViewClickListener{
    void onTipsClick();
  }
}
