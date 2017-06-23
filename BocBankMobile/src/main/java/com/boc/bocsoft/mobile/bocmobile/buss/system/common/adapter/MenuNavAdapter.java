package com.boc.bocsoft.mobile.bocmobile.buss.system.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.gridview.DragGridBaseAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;
import java.lang.reflect.Field;

/**
 * 模块adapter
 * Created by lxw on 2016/7/6 0006.
 */
public class MenuNavAdapter extends DragGridBaseAdapter<Item> {

  public MenuNavAdapter(Context context) {
    super(context);
  }

  @Override public View getItemView(final int position, View convertView, ViewGroup parent) {
    ViewHold viewHold = null;
    if (convertView == null) {
      viewHold = new ViewHold();
      convertView = mInflater.inflate(R.layout.boc_dragrid_item, parent, false);
      ViewFinder finder = new ViewFinder(convertView);
      viewHold.llContent = finder.find(R.id.ll_content);
      viewHold.tvName = finder.find(R.id.tv_item);
      viewHold.ivIcon = finder.find(R.id.iv_item);
      convertView.setTag(viewHold);

      int w = dragGridView.getColumnWidth();
     /* int measuredWidth = dragGridView.getMeasuredWidth();
      int numColumns = dragGridView.getNumColumns();
      int horizontalSpacing = dragGridView.getHorizontalSpacing();
      int width = (measuredWidth - (numColumns-1)*horizontalSpacing)/numColumns;*/
      ViewGroup.LayoutParams layoutParams = viewHold.llContent.getLayoutParams();
      if (layoutParams == null) {
        layoutParams = new RelativeLayout.LayoutParams(w, w);
      } else {
        layoutParams.height = w;
      }
      viewHold.llContent.setLayoutParams(layoutParams);
    } else {
      viewHold = (ViewHold) convertView.getTag();
    }
    Item item = getItem(position);
    viewHold.ivIcon.setBackgroundResource(getResId(item.getIconId(), R.drawable.class));
    viewHold.tvName.setText(item.getTitle());
    item.setOrder(position);

    return convertView;
  }

  /**
   * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
   */
  private int getResId(String variableName, Class<?> c) {
    try {
      Field idField = c.getDeclaredField(variableName);
      return idField.getInt(idField);
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }

  class ViewHold {
    LinearLayout llContent;
    public ImageView ivIcon;
    public TextView tvName;
  }
}
