package com.boc.bocsoft.mobile.bocmobile.buss.system.mine.ui.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eyding on 16/7/9.
 */
public class SwitchCardView extends RecyclerView{
  public SwitchCardView(Context context) {
    super(context);
  }

  public SwitchCardView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }


  private  InnerAdapter innerAdapter;

  private void initView(){
    innerAdapter = new InnerAdapter();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

    setLayoutManager(linearLayoutManager);
    setAdapter(innerAdapter);
    setPadding(0,20,0,20);
  }


  public void updateData(){

  }

  private static class InnerAdapter extends RecyclerView.Adapter<InnerViewHolder>{

    private LayoutInflater layoutInflater;
    @Override public InnerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
      if(layoutInflater == null){
        layoutInflater = LayoutInflater.from(viewGroup.getContext());
      }
      View view = layoutInflater.inflate(R.layout.boc_item_mine_switchcard,null,false);
      int w = viewGroup.getContext().getResources().getDisplayMetrics().widthPixels;
      int space = 20;
      int itemW = (int) ((w - space*3)/2.5 + 0.5);
      view.setMinimumWidth(itemW);

      RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(itemW,-2);
      params.leftMargin = space;
      view.setLayoutParams(params);

      return new InnerViewHolder(view) ;
    }

    @Override public void onBindViewHolder(InnerViewHolder innerViewHolder, int i) {


    }

    @Override public int getItemCount() {
      return 12;
    }
  }

  private static class  InnerViewHolder extends ViewHolder{

    private TextView titleTv;
    private ImageView iconIv;
    public InnerViewHolder(View itemView) {
      super(itemView);

      titleTv = (TextView) itemView.findViewById(R.id.tv_name);
      iconIv = (ImageView) itemView.findViewById(R.id.iv_icon);
      titleTv.setText("adfasfsf");
      iconIv.setImageResource(R.drawable.boc_icon);
    }
  }

}
