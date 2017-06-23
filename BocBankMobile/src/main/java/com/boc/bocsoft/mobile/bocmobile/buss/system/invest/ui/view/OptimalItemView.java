package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.PartialLoadView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.InvestTools;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.InvestItemVo;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.math.BigDecimal;

/**
 * Created by eyding on 16/7/9.
 */
public class OptimalItemView extends RelativeLayout{

  private TextView typeTv;
  private TextView nameTv;
  private TextView tipsTv;

  private TextView titleLeftTv;
  private TextView titleMiddleTv;
  private TextView titleRightTv;

  private TextView valueLeftTv;
  private TextView valuemiddleTv;
  private TextView valueRightTv;
  private TextView timeTv;

  private PartialLoadView loadingView;
  private RefrenshListener refrenshListener;

  private float valueLeftTvTextSize;

  public OptimalItemView(Context context) {
    super(context);
    initView();
  }

  public OptimalItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  public OptimalItemView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public OptimalItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initView();
  }

  private void initView(){
    setBackgroundColor(Color.WHITE);
    LayoutInflater.from(getContext()).inflate(R.layout.boc_item_invest_optimal,this);

    typeTv = (TextView) findViewById(R.id.tv_type);
    nameTv = (TextView) findViewById(R.id.tv_name);
    tipsTv = (TextView) findViewById(R.id.tv_tips);

    titleLeftTv = (TextView) findViewById(R.id.tv_title_left);
    titleMiddleTv = (TextView) findViewById(R.id.tv_title_middle);
    titleRightTv = (TextView) findViewById(R.id.tv_title_right);

    valueLeftTv = (TextView) findViewById(R.id.tv_value_left);
    valueLeftTvTextSize = valueLeftTv.getTextSize();
    resetLeftValueColor();

    valuemiddleTv = (TextView) findViewById(R.id.tv_value_middle);
    valueRightTv = (TextView) findViewById(R.id.tv_value_right);
    timeTv = (TextView) findViewById(R.id.tv_time);

    loadingView = (PartialLoadView) findViewById(R.id.view_loading);


    ////valueLeftTv.getPaint().setFakeBoldText(true);
    //valuemiddleTv.getPaint().setFakeBoldText(true);
    //valueRightTv.getPaint().setFakeBoldText(true);

    changeLoadIng();
  }


  private  InvestItemVo data;

  public void update(InvestItemVo data){
    this.data = data;

    changeLoadIng();
    valueLeftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,valueLeftTvTextSize);

    if(InvestTools.isFinancing(data)){
      updateFinancing();
    }else if(InvestTools.isFund(data)){
      updateFund();
    }

    valueLeftTv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
      @Override public boolean onPreDraw() {
        valueLeftTv.getViewTreeObserver().removeOnPreDrawListener(this);
        int measuredWidth = valueLeftTv.getMeasuredWidth();

        CharSequence text = valueLeftTv.getText();
        if(text==null)return true;
        float v = valueLeftTv.getPaint().measureText(text.toString());
        if(v>measuredWidth){
          for(;;){
            valueLeftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,valueLeftTv.getTextSize()*0.8f);
            v = valueLeftTv.getPaint().measureText(text.toString());
            if(v<measuredWidth)break;
          }
        }
        return false;
      }
    });
  }

  public InvestItemVo getData(){
    return  data;
  }


  private void resetLeftValueColor(){

    String text = valueLeftTv.getText().toString();

    if(StringUtils.isEmptyOrNull(text) || "--".equals(text)){
      valueLeftTv.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
    }else if(text.contains("-")){
      valueLeftTv.setTextColor(getResources().getColor(R.color.boc_text_color_green));
    }else{

      try {
        if(text != null && new BigDecimal(text.replace("%","")).compareTo(BigDecimal.ZERO) == 0){
          valueLeftTv.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        return;
        }
      }catch (Exception e){
      }

      valueLeftTv.setTextColor(getResources().getColor(R.color.boc_text_color_red));
    }
  }

  private void updateFinancing(){

    tipsTv.setVisibility(View.INVISIBLE);

    boolean isLogin = data.isLoadLoginApi(); //数据源判断
    //	0：结构性，1类基金

    typeTv.setText("[理财]");
    nameTv.setText(data.getProductName());
    //nameTv.setText(restContent(nameTv,data.getProductName(),"(990123)"));
    tipsTv.setText("");

    //登录前
    if("0".equals(data.getProductNature())){
      //结构型

      String isLockPeriod = data.getIsLockPeriod();
      if("1".equals(isLockPeriod) || "2".equals(isLockPeriod) || "3".equals(isLockPeriod)){
        //业绩基准
        titleLeftTv.setText("业绩基准");
      }else{
        titleLeftTv.setText("预期年化收益率");
      }

      valueLeftTv.setText(getValueDefault(data.getRateValue()));
      resetLeftValueColor();

      timeTv.setText("");
    }else if("1".equals(data.getProductNature())){
      //基金型
      titleLeftTv.setText("单位净值");
      valueLeftTv.setText(getValueDefault(data.getPrice()));
      resetLeftValueColor();

      timeTv.setText(getValueEmpty(data.getPriceDate()));
    }

    titleMiddleTv.setText("产品期限");

    //当为业绩基准产品时该字段有效；当为非业绩基准产品，且“产品期限特性”不是“无限开放式”时，该字段有效；其余情况该字段无效，请统一展示无固定期限
    //此处逻辑不处理在P层处理
    valuemiddleTv.setText(getValueDefault(data.getTimeLimit()));

    if(isLogin && (data.isLoadLoginApi() || !data.isLoadNoLoginApi())){
      titleRightTv.setText("剩余额度");
      valueRightTv.setText(getValueDefault(data.getAvailamt()));
    }else{
      titleRightTv.setText("起购金额");
      valueRightTv.setText(getValueDefault(data.getLowLimit()));
    }

  }

  /**
   * 重置内容 保证code值显示完全,name不完整显示。。。
   * @param tv
   * @param name
   * @param code
   * @return
   */
  private void restContent(final TextView tv, final String name, final String code){
    LogUtils.d("dding","重置内容:"+name+","+code);
    if(name == null || code == null){
      //名称 或者code 为空 没必要再进行处理了
      return;
    }

    tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
       @Override public void onGlobalLayout() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
           tv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
         }else{
           tv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
         }

         int viewWidth = tv.getMeasuredWidth();

        float nameWidth = tv.getPaint().measureText(name);
        float codeWidth = tv.getPaint().measureText(code);

        LogUtils.d("dding","---view treeObserver --- "+viewWidth+","+nameWidth+","+codeWidth);
        String nameValue = name;

        int lastIndex = nameValue.length()-1;
        if(nameWidth+codeWidth>viewWidth){
          float aviableWidth = viewWidth - codeWidth - tv.getPaint().measureText("...");
          for(;;){
            if(lastIndex<0)break;
            if(tv.getPaint().measureText(nameValue,0,lastIndex)<aviableWidth){
             break;
            }
            lastIndex-- ;
          }
          if(lastIndex>0){
            nameValue = nameValue.substring(0,lastIndex)+"...";
          }else{
            nameValue = "...";
          }
          //截取字符串
          tv.setText(nameValue+code);
        }else{
          tv.setText(name+code);
        }
      }
    });
  }


  private void updateFund(){

     /*
   * 基金产品类型
   * 01：理财型基金 02：QDII基金 03：ETF基金 04：保本型基金 05：指数型基金 06：货币型基金
   * 07：股票型基金 08：债券型基金 09：混合型基金 10：其他基金
   */
    if(loadingView!=null && loadingView.getVisibility() == View.VISIBLE){
      tipsTv.setVisibility(View.INVISIBLE);
    }else{
      tipsTv.setVisibility(View.VISIBLE);
    }


    if("06".equals(data.getFntype()) || "01".equals(data.getFntype())){
      //货币型
      updateFundCurrency();
    }else {
      updateFundNotCurrency();
    }
  }

  /**
   * 更新基金页面 - 货币型基金
   *
   * 基金名称、基金代码、7日年化收益率（时间）、万份收益、起购金额、基金类型
   */
  private void updateFundCurrency(){

    typeTv.setText("[基金]");
    //名称+代买
    nameTv.setText(data.getProductName()+"("+data.getProductCode()+")");

    restContent(nameTv,data.getProductName(),"("+data.getProductCode()+")");

    //基金类型
    tipsTv.setText(getFundTypeValue(data.getFntype()));

    titleLeftTv.setText("7日年化收益率");
    titleMiddleTv.setText("万份收益");
    titleRightTv.setText("起购金额");

    valueLeftTv.setText(getValueDefault(data.getSevenDayYield()));
    resetLeftValueColor();
    //万分收益
    valuemiddleTv.setText(getValueDefault(data.getFundIncomeUnit()));

    //起购金额 - 先判断是否可认购（最低认购）再判断是否可申购(最低申购) 逻辑P层处理,此处只负责显示
    valueRightTv.setText(getValueDefault(data.getLowLimit()));
    timeTv.setText(getValueEmpty(data.getPriceDate()));
  }

  /**
   * 更新基金界面 - 非货币基金
   *
   * 基金名称、基金代码、日增长率（时间）、最新净值、起购金额、基金类型、申购手续费/折扣（手续费和折扣如取不到不展示）
   */
  private void updateFundNotCurrency(){

    typeTv.setText("[基金]");
    //名称+代码
    nameTv.setText(data.getProductName()+"("+data.getProductCode()+")");
    restContent(nameTv,data.getProductName(),"("+data.getProductCode()+")");

    //基金类型
    tipsTv.setText(getFundTypeValue(data.getFntype()));

    titleLeftTv.setText("日涨幅");
    titleMiddleTv.setText("最新净值");
    //起购金额
    //titleRightTv.setText(getValueDefault(data.getLowLimit()));
    titleRightTv.setText("起购金额");

    valueLeftTv.setText(getValueDefault(data.getDayIncomeRatio()));
    resetLeftValueColor();
    valuemiddleTv.setText(getValueDefault(data.getPrice()));
    //valueRightTv.setText(getValueDefault(data.getChargeRate()));
    valueRightTv.setText(getValueDefault(data.getLowLimit()));

    timeTv.setText(getValueEmpty(data.getPriceDate()));

  }


  private void changeLoadIng(){
    if(data == null){
      return;
    }

    if(data.isRefrensh()){
      loadingView.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
    }else{
      loadingView.setLoadStatus(PartialLoadView.LoadStatus.REFRESH);
    }

    if(!data.isSuccess()){
      loadingView.setVisibility(View.VISIBLE);
      loadingView.setOnClickListener(clickListener);
    }else{
      loadingView.setVisibility(View.INVISIBLE);
      loadingView.setOnClickListener(null);
    }

  }

  private OnClickListener clickListener = new OnClickListener() {
    @Override public void onClick(View v) {

      loadingView.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
      //nofify
      if(refrenshListener!= null){
        refrenshListener.onRefrensh(OptimalItemView.this,data);
      }
    }
  };

  public void setRefrenshListener(RefrenshListener refrenshListener) {
    this.refrenshListener = refrenshListener;
  }

  public interface  RefrenshListener{
    void onRefrensh(View view,InvestItemVo investItemVo);
  }


  /*
  * 基金产品类型
      * 01：理财型基金 02：QDII基金 03：ETF基金 04：保本型基金 05：指数型基金 06：货币型基金
      * 07：股票型基金 08：债券型基金 09：混合型基金 10：其他基金
      */
  private String getFundTypeValue(String code){
    if(code == null)return "";
    switch (code){
      case "01":
        return "理财型";
      case "02":
        return "QDII";
      case "03":
        return "ETF";
      case "04":
        return "保本型";
      case "05":
        return "指数型";
      case "06":
        return "货币型";
      case "07":
        return "股票型";
      case "08":
        return "债券型";
      case "09":
        return "混合型";
      case "10":
        return "其他";
    }

    return "其他";
  }


  private String getValueDefault(String value){
    if(StringUtils.isEmptyOrNull(value)){
      return "--";
    }
    return value;
  }

  private String getValueEmpty(String value){
    if(StringUtils.isEmptyOrNull(value))return "";
    return value;
  }

  @Override public void setOnClickListener(OnClickListener l) {
    outClickListener = l;
    super.setOnClickListener(innerClickListener);
  }

  private View.OnClickListener innerClickListener = new View.OnClickListener(){

    @Override public void onClick(View v) {
      if(outClickListener == null){
        return;
      }
      if(loadingView==null || loadingView.getVisibility()==View.VISIBLE){
        return;
      }
      outClickListener.onClick(v);

    }
  };
  private OnClickListener outClickListener;
}
