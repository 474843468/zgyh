package com.boc.bocsoft.mobile.bocmobile.base.widget.gridview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;
import java.util.Collections;
import java.util.List;

/**
 * 定义要实现的方法
 * 
 * @author gwluo
 * 
 */
public abstract class DragGridBaseAdapter<T> extends BaseListAdapter<T> {

	private int mHidePosition = -1;
	private int showDelPos = -1;
	protected DragGridView dragGridView;
	protected int delIconWidth = -2;
	protected int delIconPadding = 0;

	public DragGridBaseAdapter(Context context) {
		super(context);
		delIconWidth = (int) (context.getResources().getDisplayMetrics().density*40);
		delIconPadding = (int) (context.getResources().getDisplayMetrics().density*8);
	}



	/**
	 * 获取显示视图
	 * @param position
	 * @param convertView
	 * @param parent
     * @return
     */
	public View getView(final int position, View convertView, ViewGroup parent) {

		RelativeLayout rl = (RelativeLayout) convertView;
		if(rl == null){
			rl = new RelativeLayout(parent.getContext());
			GridView.LayoutParams params = new AbsListView.LayoutParams(-1,-1);
			rl.setLayoutParams(params);
			ImageView imageView = new ImageView(parent.getContext());
			imageView.setImageResource(R.drawable.delete_item_btn);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView.setPadding(delIconPadding*2,delIconPadding,delIconPadding,delIconPadding*2);
			RelativeLayout.LayoutParams rlP = new RelativeLayout.LayoutParams(delIconWidth,delIconWidth);
			rlP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			resetDelButtonParams(rlP);
			rl.addView(imageView,rlP);
			ViewHolder holder = new ViewHolder();
			holder.ivDel = imageView;
			rl.setTag(holder);
		}

		ViewHolder holder = (ViewHolder) rl.getTag();

		holder.viewItem = getItemView(position,holder.viewItem,parent);
		if(holder.viewItem.getParent() == null)
		rl.addView(holder.viewItem,0,new RelativeLayout.LayoutParams(-1,-1));


		if(position == showDelPos){
			holder.ivDel.setVisibility(View.VISIBLE);
			holder.ivDel.setOnClickListener(new View.OnClickListener(){
				@Override public void onClick(View v) {
					endEidtForDel(getDatas().remove(position));
				}
			});

		}else{
			holder.ivDel.setVisibility(View.INVISIBLE);
			holder.ivDel.setOnClickListener(null);
		}

		if(position == mHidePosition){
			rl.setVisibility(View.INVISIBLE);
			mHidePosition = -1;
		}else{
			rl.setVisibility(View.VISIBLE);
		}
		return rl;
	}

	public abstract View getItemView(int position, View convertView, ViewGroup parent);

	/**
	 * 重新排列数据
	 * 
	 * @param from
	 * @param to
	 */
	public void reorderItems(int from, int to){
		List<T> data = getDatas();
		T temp = data.get(from);
		// 这里的处理需要注意下
		if (from < to) {
			for (int i = from; i < to; i++) {
				Collections.swap(data, i, i + 1);
			}
		} else if (from > to) {
			for (int i = from; i > to; i--) {
				Collections.swap(data, i, i - 1);
			}
		}

		data.set(to, temp);
		setDatas(data);
	}

	/**
	 * 设置某个item隐藏
	 *
	 * @param hidePosition
	 */
	public void setHideItem(int hidePosition){
		this.mHidePosition = hidePosition;
		notifyDataSetChanged();
	}

	public void  startEditMode(){
		notifyDataSetChanged();
	}


	public void showDelIcon(View view, final int pos){
		this.showDelPos = pos;
	}

	public  void endEidt(){
		showDelPos = -1;
		mHidePosition = -1;
		notifyDataSetChanged();
	}

	public void endDrag(){
		if(dragInterface!=null){
			dragInterface.onDragEnd();
		}
	}

	private void endEidtForDel(T data){
		endEidt();
		//通知gridview
		if(dragGridView != null){
			dragGridView.endEditMode(false);
		}

		if(dragInterface!=null){
			dragInterface.onItemDelete(data);
		}
	}

	private static class ViewHolder{
		private ImageView ivDel;
		private View viewItem;
	}

	private DragInterface<T> dragInterface;

	public void setDragInterface(DragInterface<T> dragInterface) {
		this.dragInterface = dragInterface;
	}

	public void bindDragView(DragGridView dragGridView){
		this.dragGridView = dragGridView;
	}

	public  interface DragInterface<T>{

		void onDragEnd();
		void onItemDelete(T t);
	}

	/**
	 * 设置删除按钮位置 (给子类一个机会自定义删除按钮位置)
	 * @param params
   */
	public void resetDelButtonParams(ViewGroup.MarginLayoutParams params){
	}
}
