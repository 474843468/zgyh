package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.LifeTools;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.view.LifeAdapterItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.view.LifeAdapterItemView.ItemButton;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.view.LifeAdapterItemView.RecordItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.view.LifeAdapterItemView.WrapLayout;
import java.util.List;

/**
 * Created by dingeryue on 2016年11月09.
 * 生活更多adapter
 */

public class LifeMoreMenuAdapter {

  private static final int TYPE_RECORE = 0;
  private static final int TYPE_NORMAL = 1;

  private static final int TYPE_EDIT = 2;
  private static final int TYPE_BUTTON = 3;

  public static class LifeMoreNormalAdapter extends BaseAdapter{

    private List<LifeMenuModel> list;

    public void updateDatas(List<LifeMenuModel> list){
      this.list = list;
      notifyDataSetChanged();
    }

    @Override public int getCount() {
      return list == null?1:(1+list.size());
    }


    @Override public LifeMenuModel getItem(int position) {
      return position == 0 ?null: list.get(position-1);
    }

    @Override public long getItemId(int position) {
      return 0;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {


      if(getItemViewType(position) == TYPE_RECORE){
        WrapLayout<RecordItemView> wrapperView = (WrapLayout) convertView;
        if(wrapperView !=null)return wrapperView;
        wrapperView = new WrapLayout(parent.getContext());
        RecordItemView itemView = new RecordItemView(parent.getContext());
        wrapperView.wrapView(itemView,0,0,0,LifeAdapterItemView.SPACE);
        itemView.setCompoundDrawables(itemView.iconDrawable,null,itemView.rightDrawable,null);

        return wrapperView;
      }

      ChooseAdapter.ItemView itemView = (ChooseAdapter.ItemView) convertView;

      //正常View
      if(itemView == null){
        itemView = new LifeAdapterItemView.NormalItemView(parent.getContext());
      }

      LifeMenuModel item = getItem(position);
      itemView.itemName(LifeTools.getMenuName(item))
          .itemIcon(LifeTools.getResIcon(item));

      itemView.iconDrawable.setBounds(0,0,itemView.iconHeight,itemView.iconHeight);
      itemView.setCompoundDrawables(itemView.iconDrawable,null,itemView.rightDrawable,null);

      return itemView;
    }

    @Override public int getViewTypeCount() {
      return 4;
    }

    @Override public int getItemViewType(int position) {
      return position == 0?TYPE_RECORE:TYPE_NORMAL;
    }
  }

  /**
   * 生活更多菜单 编辑adapter
   */
  public static class LifeMoreEditAdapter extends BaseAdapter{

    private List<LifeMenuModel> list;
    public void updateDatas(List<LifeMenuModel> list){
      this.list = list;
      notifyDataSetChanged();
    }
    public List<LifeMenuModel> getDatas(){
      return list;
    }

    @Override public int getCount() {
      return list == null?1:(list.size()+1);
    }

    @Override public LifeMenuModel getItem(int position) {
      return getItemViewType(position) == TYPE_EDIT? list.get(position):null;
    }

    @Override public long getItemId(int position) {
      return 0;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {

      if(TYPE_BUTTON == getItemViewType(position)){
        WrapLayout<ItemButton> wrapLayout = (WrapLayout<ItemButton>) convertView;
        if(wrapLayout == null){
          wrapLayout = new WrapLayout<>(parent.getContext());
          ItemButton button =  new ItemButton(parent.getContext());
          button.setText("添加");

          wrapLayout.wrapView(button,0,LifeAdapterItemView.SPACE,0,LifeAdapterItemView.SPACE);
        }
        wrapLayout.view().setTag(Integer.valueOf(position));
        wrapLayout.view().setOnClickListener(onClickListener);
        return wrapLayout;
      }

      LifeAdapterItemView.DelItemView itemView = (LifeAdapterItemView.DelItemView) convertView;
      if(itemView == null){
        itemView = new LifeAdapterItemView.DelItemView(parent.getContext());
      }
      LifeMenuModel item = getItem(position);
      itemView.itemName(LifeTools.getMenuName(item)).itemIcon(LifeTools.getResIcon(item));

      int iconHeight = (int) (itemView.itemHeight() * .45f);
      itemView.rightDrawable.setBounds(0,0,iconHeight,iconHeight);
      itemView.setCompoundDrawables(itemView.iconDrawable,null,itemView.rightDrawable,null);

      itemView.setTag(Integer.valueOf(position));
      itemView.itemEdit(true);
      itemView.setOnClickListener(onClickListener);

      return itemView;
    }

    @Override public int getViewTypeCount() {
      return 4;
    }

    @Override public int getItemViewType(int position) {
      return position == (getCount()-1)?TYPE_BUTTON:TYPE_EDIT;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
      @Override public void onClick(View v) {

       Integer pos = (Integer) v.getTag();
        if(pos == null || editListener == null)return;
        LifeMenuModel item = getItem(pos);
        if(item != null){
          editListener.onItemDelClick(pos,item);
        }else{
          editListener.onAddClick();
        }
      }
    };

    private EditListener editListener;
    public void setEditListener(EditListener editListener) {
      this.editListener = editListener;
    }

    public  interface EditListener{
      /**
       * 点击item的删除按钮
       * @param pos
       * @param menuModel
       */
      void onItemDelClick(int pos,LifeMenuModel menuModel);

      /**
       * 点击添加按钮
       */
      void onAddClick();
    }

  }

}
