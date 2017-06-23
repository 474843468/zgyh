package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AccountSpinnerAdapter<String> extends ArrayAdapter<String> {

	private LayoutInflater mInflater;
	private int mFieldId;
	private int mDropDownResource;
	private int mSpinnerResource;
	private String[] itemValues;

	@Override
	public String getItem(int position) {
		return itemValues[position];
	}

	@Override
	public int getPosition(Object item) {
		if (null != item)
			for (int i = 0; i < itemValues.length; i++) {
				if (item.equals(itemValues[i]))
					return i;
			}
		return -1;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mSpinnerResource, 1);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mDropDownResource, 2);
	}

	public AccountSpinnerAdapter(Context context, int textViewResourceId, String[] itemValues) {
		super(context, textViewResourceId, itemValues);
		this.itemValues = itemValues;
		mInflater = LayoutInflater.from(context);
		mSpinnerResource = textViewResourceId;
		mFieldId = 0;
	}

	private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource, int type) {
		View view;
		TextView text;

		if (convertView == null) {
			view = mInflater.inflate(resource, parent, false);
		} else {
			view = convertView;
		}

		try {
			if (mFieldId == 0) {
				// If no custom field is assigned, assume the whole resource is
				// a TextView
				text = (TextView) view;
			} else {
				// Otherwise, find the TextView field within the layout
				text = (TextView) view.findViewById(mFieldId);
			}
		} catch (ClassCastException e) {
			throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
		}

		String item = getItem(position);
		java.lang.String temp = "";
		switch (type) {
		case 1: // spinner页面显示内容
			temp = item.toString().split(" ")[0];
			break;
		case 2: // spinner选择内容
			temp = item.toString();
			break;
		}

		text.setText(temp);

		return view;
	}

	public void setmDropDownResource(int mDropDownResource) {
		this.mDropDownResource = mDropDownResource;
	}

	public int getmDropDownResource() {
		return mDropDownResource;
	}

}
