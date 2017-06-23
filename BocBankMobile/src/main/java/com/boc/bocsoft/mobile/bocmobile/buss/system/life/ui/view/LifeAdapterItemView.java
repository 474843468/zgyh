package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.adapter.ChooseAdapter;

/**
 * Created by dingeryue on 2016年11月09.
 */

public class LifeAdapterItemView {

  static  int itemHeight = ApplicationContext.getInstance().getResources().getDimensionPixelSize(R.dimen.boc_space_between_104px);

  static  int itemHeight86 = ApplicationContext.getInstance().getResources().getDimensionPixelSize(R.dimen.boc_space_between_80px);

  public static int SPACE = ApplicationContext.getInstance().getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px);
  /**
   * 缴费记录itemView
   */
  public static class RecordItemView extends ChooseAdapter.ItemView{
    public RecordItemView(Context context) {
      super(context);
      itemLast(false).itemName("缴费记录").itemRightDrawable(R.drawable.boc_arrow_right)
          .itemHeight(itemHeight86);
      getPaint().setFakeBoldText(true);
    }
  }

  /**
   * 正常ItemView
   */
  public static class NormalItemView extends ChooseAdapter.ItemView{

    public NormalItemView(Context context) {
      super(context);
      setSingleLine();
      setEllipsize(TextUtils.TruncateAt.END);
      itemLast(false).itemEdit(false).itemHeight(itemHeight);
      getPaint().setFakeBoldText(true);
    }
  }

  /**
   * 带删除的ItemView
   */
  public static class DelItemView extends ChooseAdapter.ItemView{

    public DelItemView(Context context) {
      super(context);
      setSingleLine();
      setEllipsize(TextUtils.TruncateAt.END);
      getPaint().setFakeBoldText(true);
      itemLast(false).itemEdit(true).itemHeight(itemHeight).itemRightDrawable(R.drawable.delete_item_btn);
    }
  }


  /**
   * 按钮view
   */
  public static class ItemButton extends TextView{
    public ItemButton(Context context) {
      super(context);
      setTextAppearance(getContext(),R.style.BocButton);
      setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.boc_text_size_common));
      setTextColor(getResources().getColor(R.color.boc_main_button_color));
      setGravity(Gravity.CENTER);
      setBackgroundColor(Color.WHITE);
      setMinHeight(itemHeight86);
      getPaint().setFakeBoldText(true);
    }
  }

  public static class WrapLayout<T extends View> extends LinearLayout{

    private T innerView;
    public WrapLayout(Context context) {
      super(context);
      setBackgroundColor(0xfff0eff5);
    }

    public void wrapView(T view,int left,int top,int right,int bottom){
      this.innerView = view;
      LinearLayout.LayoutParams params = new LayoutParams(-1,-2);
      params.leftMargin = left;
      params.rightMargin = right;
      params.topMargin = top;
      params.bottomMargin = bottom;
      addView(view,-1,-1);
      setPadding(left,top,right,bottom);
    }
    public T view(){
      return innerView;
    }
  }

}
