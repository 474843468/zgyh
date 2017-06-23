package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;


public abstract class BaseDialog extends Dialog {

	protected Context mContext;
	protected LayoutInflater inflater;
	private LinearLayout mContentLayout;

	public BaseDialog(Context context) {
		super(context, R.style.dialog_normal);
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		setContentView(R.layout.dialog_base);
		// 基类
		initBaseView();
		initView();
		// 子类
		initData();
		setListener();
	}

	public BaseDialog(Context context, int dialogNormal) {
		super(context, R.style.dialog_normal);
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		setContentView(R.layout.dialog_base);
		// 基类
		initBaseView();
		initView();
		// 子类
		initData();
		setListener();
	}

	protected View inflateView(int resid) {
		return inflater.inflate(resid, null);
	}

	private void initBaseView() {
		mContentLayout = (LinearLayout) findViewById(R.id.ll_dialog_base_content);
		mContentLayout.addView(onAddContentView());
	}


	protected abstract View onAddContentView();

	protected abstract void initView();

	protected abstract void initData();

	protected abstract void setListener();
}