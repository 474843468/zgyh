/**
 * Copyright 2012 
 * 
 * Nicolas Desjardins  
 * https://github.com/mrKlar
 * 
 * Facilite solutions
 * http://www.facilitesolutions.com/
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chinamworld.bocmbci.widget.adapter;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.widget.draggridview.PagedDragDropGrid;
import com.chinamworld.bocmbci.widget.draggridview.PagedDragDropGridAdapter;
import com.chinamworld.bocmbci.widget.entity.Item;

public class MainPagedDragDropGridAdapter implements PagedDragDropGridAdapter {

	private Context context;
	private PagedDragDropGrid gridview;
	private LayoutInflater mInflater;
	private List<Page> pages;

	public MainPagedDragDropGridAdapter(Context context,
			PagedDragDropGrid gridview, List<Page> pages) {
		super();
		this.context = context;
		this.gridview = gridview;
		this.pages = pages;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int pageCount() {
		return pages.size();
	}

	private List<Item> itemsInPage(int page) {
		if (pages.size() > page) {
			return pages.get(page).getItems();
		}
		return Collections.emptyList();
	}

	@Override
	public View view(int page, int index) {
		View convertView = new View(context);
		convertView = mInflater.inflate(R.layout.grid_item_img_text, null);

		convertView.setTag(index);

		Item item = getItem(page, index);
		ImageView gridIcon = (ImageView) convertView
				.findViewById(R.id.grid_icon);
		gridIcon.setBackgroundResource(item.getDrawable());
		TextView gridText = (TextView) convertView.findViewById(R.id.grid_text);
		gridText.setText(item.getName());

		convertView.setClickable(true);
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return gridview.onLongClick(v);
			}
		});

		return convertView;
	}

	public Item getItem(int page, int index) {
		//防止数组下标越界
		try {
			List<Item> items = itemsInPage(page);
			return items.get(index);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int rowCount() {
		return ROWNUMBER;
	}

	@Override
	public int columnCount() {
		return COLNUMBER;
	}

	@Override
	public int itemCountInPage(int page) {
		return itemsInPage(page).size();
	}

	public void printLayout() {
		int i = 0;
//		for (Page page : pages) {
//			LogGloble.d("Page", Integer.toString(i++));
//
//			for (Item item : page.getItems()) {
//				LogGloble.d("Item", Long.toString(item.getId()));
//			}
//		}
	}

	private Page getPage(int pageIndex) {
		return pages.get(pageIndex);
	}

	@Override
	public void swapItems(int pageIndex, int itemIndexA, int itemIndexB) {
		getPage(pageIndex).swapItems(itemIndexA, itemIndexB);
	}

	@Override
	public void moveItemToPreviousPage(int pageIndex, int itemIndex) {
		int leftPageIndex = pageIndex - 1;
		if (leftPageIndex >= 0) {
			Page startpage = getPage(pageIndex);
			Page landingPage = getPage(leftPageIndex);

			Item item = startpage.removeItem(itemIndex);
			landingPage.addItem(item);
		}
	}

	@Override
	public void moveItemToNextPage(int pageIndex, int itemIndex) {
		int rightPageIndex = pageIndex + 1;
		if (rightPageIndex < pageCount()) {
			Page startpage = getPage(pageIndex);
			Page landingPage = getPage(rightPageIndex);

			Item item = startpage.removeItem(itemIndex);
			landingPage.addItem(item);
		}
	}

	@Override
	public void deleteItem(int pageIndex, int itemIndex) {
		getPage(pageIndex).deleteItem(itemIndex);
	}

	// modify by wjp
	public int getClicked() {
		return gridview.getClicked();
	}

}
