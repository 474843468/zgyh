package com.chinamworld.bocmbci.biz.crcd.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;

public class SpinnerAdapter extends ArrayAdapter<String> {

	Context context;

	List<String> items = new ArrayList<String>();

	public SpinnerAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);

		this.items = objects;
		this.context = context;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {

			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(
					android.R.layout.simple_spinner_dropdown_item, parent,
					false);

		}

		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(items.get(position));
		// tv.setTextColor(Color.BLUE);

		// int textSize =
		// context.getResources().getDimensionPixelSize(R.dimen.textsize_default);
		int textSize = Integer.valueOf(context.getResources().getString(
				R.string.spinnertextsize));
		tv.setTextSize(textSize);

		return convertView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(
					R.layout.custom_spinner_item, parent, false);
		}

		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(items.get(position));

		// tv.setTextColor(Color.BLUE);
		// int textSize = context.getResources().getDimensionPixelSize(
		// R.dimen.textsize_default);

		int textSize = Integer.valueOf(context.getResources().getString(
				R.string.spinnertextsize));
		tv.setTextSize(textSize);

		return convertView;
	}
}
