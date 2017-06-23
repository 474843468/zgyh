package com.boc.bocsoft.remoteopenacc.buss.view;

import com.boc.bocsoft.remoteopenacc.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RemobeCommonEditTextView extends LinearLayout {

	private Context mContext;
	private View rootView;
	private TextView tv;
	private EditText et;

	public RemobeCommonEditTextView(Context context) {
		super(context);
		this.mContext = context;
		initView();
	}

	public RemobeCommonEditTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initView();
	}
	//delete by lgw 2015.10.28 由于要使用SDK 2.3.3(Android 10) 编译
	// public RemobeCommonEditTextView(Context context, AttributeSet attrs,
	// int defStyle) {
	// super(context, attrs, defStyle);
	// this.mContext = context;
	// initView();
	// }

	private void initView() {
		rootView = LayoutInflater.from(mContext).inflate(
				R.layout.bocroa_view_common_edittext, this);
		tv = (TextView) rootView.findViewById(R.id.remobe_common_tv);
		et = (EditText) rootView.findViewById(R.id.remobe_common_et);
	}

	/**
	 * 设置标题
	 */
	public void setTitle(String title) {
		tv.setText(title);
	}

	/**
	 * 获取输入信息
	 */
	public String getContent() {
		return et.getText().toString();
	}
}
