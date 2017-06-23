package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.gridview.DragGridBaseAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.LifeTools;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;
import com.boc.bocsoft.mobile.framework.utils.ViewFinder;

/**
 * Created by dingeryue on 2016年09月01.
 */
public class LifeMenuAdapter extends DragGridBaseAdapter<LifeMenuModel> {
  public LifeMenuAdapter(Context context) {
    super(context);

    delIconPadding = (int) (context.getResources().getDisplayMetrics().density*8);
  }

  @Override public View getItemView(final int position, View convertView, ViewGroup parent) {
    ViewHold viewHold = null;
    if (convertView == null) {
      viewHold = new ViewHold();
      //convertView = mInflater.inflate(R.layout.boc_item_life_menu, parent, false);
      convertView = mInflater.inflate(R.layout.boc_life_menu_item, parent, false);
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
        layoutParams = new RelativeLayout.LayoutParams(w, -2);
        viewHold.llContent.setLayoutParams(layoutParams);
      } else {
        layoutParams.height = w;
      }
      viewHold.llContent.setMinimumHeight(w);

    } else {
      viewHold = (ViewHold) convertView.getTag();
    }
    LifeMenuModel item = getItem(position);
      viewHold.ivIcon.setBackgroundResource(LifeTools.getResIcon(item));

    //viewHold.tvName.setText(item.getCatName());
    viewHold.tvName.setText(LifeTools.getMenuName(item));
    //viewHold.tvName.setText("学费水费生活费\n电费公积金");
    return convertView;
  }

  @Override public void resetDelButtonParams(ViewGroup.MarginLayoutParams params) {
    params.topMargin = - delIconPadding;
  }

  class ViewHold {
    LinearLayout llContent;
    public ImageView ivIcon;
    public TextView tvName;
  }
}
