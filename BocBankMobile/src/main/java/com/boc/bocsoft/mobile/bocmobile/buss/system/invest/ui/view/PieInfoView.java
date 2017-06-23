package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eyding on 16/8/11.
 */
public class PieInfoView extends LinearLayout implements View.OnClickListener {
  public PieInfoView(Context context) {
    super(context);
    initView();
  }

  public PieInfoView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  public PieInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }


  private void initView(){
    setOrientation(LinearLayout.VERTICAL);
    setBackgroundColor(getResources().getColor(R.color.boc_divider_line_color));
  }



  private List<InfoData> itemList;
  private List<ItemView> itemViews = new ArrayList<>();
  private PieInfoListener listener;
  public void setData(List<InfoData> itemList){
    this.itemList = itemList;
    if(getChildCount() !=0)removeAllViews();
    if(itemList == null || itemList.size() == 0)return;

    int count = itemList.size();
    int divider = getResources().getDimensionPixelOffset(R.dimen.boc_divider_1px);
    for(int index =0 ;index<count;index++){

      ItemView itemView = new ItemView(getContext());
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT);
      //分割线
      params.topMargin = divider;
      if(index == count-1)params.bottomMargin = divider;

      itemView.setBackgroundColor(Color.WHITE);
      itemViews.add(itemView);
      addView(itemView,params);
      updateItemView(itemView,itemList.get(index),index);
    }
  }

  final int size = ApplicationContext.getInstance().getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px);
  private void updateItemView(ItemView itemview, final InfoData infoData,int pos){

    itemview.ivIcon.setImageDrawable(getIconDrawable(infoData));

    itemview.tvName.setText(infoData.name);
    itemview.tv_money.setText(infoData.money+"元");

    itemview.ivArrow.setVisibility(StringUtils.isEmptyOrNull(infoData.target)?View.INVISIBLE:View.VISIBLE);
    itemview.setTag(Integer.valueOf(pos));
    itemview.setOnClickListener(this);
  }


  private ShapeDrawable getIconDrawable(final  InfoData infoData){
    ShapeDrawable shapeDrawable = new ShapeDrawable();
    shapeDrawable.setIntrinsicWidth(size);
    shapeDrawable.setIntrinsicHeight(size);
    final RadialGradient gradient;

    if(infoData.shaderColors != null && infoData.shaderColors.length>=2){
      gradient = new RadialGradient(size/2,size/2,size/2,infoData.shaderColors[0],infoData.shaderColors[1],
          Shader.TileMode.CLAMP);
    }else {
      gradient = null;
    }

    if(infoData.shaderColors == null || infoData.shaderColors.length==0){
      infoData.shaderColors = new int[]{Color.WHITE};
    }

    shapeDrawable.setShape(new Shape() {
      @Override public void draw(Canvas canvas, Paint paint) {
        paint.setColor(infoData.shaderColors[0]);
        paint.setShader(gradient);
        canvas.drawCircle(size/2,size/2,size/2,paint);
      }
    });

    return  shapeDrawable;
  }


  @Override public void onClick(View v) {
    Integer index = (Integer) v.getTag();
    if(index == null)return;
    actionItemClick(index,itemList.get(index));
  }

  /**
   * 点击
   * @param pos
   * @param data
   */
  private void actionItemClick(int pos,InfoData data){
    if(listener != null){
      listener.onItemClick(pos,data);
    }
  }

  public void setListener(PieInfoListener listener) {
    this.listener = listener;
  }

  public static class InfoData{
    private Shader shader;
    public String name;
    public String currency = "人民币元";
    public String money;
    public int shaderColors[];//渐变颜色
    public String target;//点击跳转目标
  }

  private class ItemView extends LinearLayout{

    public ItemView(Context context) {
      super(context);
      initView();
    }

    private ImageView ivIcon;
    private TextView tvName;
    private TextView tv_money;
    private ImageView ivArrow;
    private void initView(){
      setOrientation(LinearLayout.HORIZONTAL);
      LayoutInflater.from(getContext()).inflate(R.layout.boc_view_summarize_info_item,this,true);
      ViewFinder finder = new ViewFinder(this);
      ivIcon = finder.find(R.id.iv_icon);
      tvName = finder.find(R.id.tv_name);
      tv_money = finder.find(R.id.tv_money);
      ivArrow = finder.find(R.id.iv_arrow);
    }
  }

  public static interface PieInfoListener{
    void onItemClick(int pos,InfoData infoData);
  }
}
