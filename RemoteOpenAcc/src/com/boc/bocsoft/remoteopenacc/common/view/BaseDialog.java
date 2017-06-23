package com.boc.bocsoft.remoteopenacc.common.view;


import com.boc.bocsoft.remoteopenacc.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public abstract class BaseDialog extends Dialog{

	protected Context mContext;
	protected LayoutInflater inflater;
	private LinearLayout mContentLayout;
	
	public BaseDialog(Context context) {
		super(context, R.style.bocroa_style_dialog_normal);
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		//setContentView(R.layout.bocroa_dialog_base);
		
		
		this.setContentView(onAddConentView());
		initView();
		initData();
		setListener();
	}

	private void initBaseView() {
//		mContentLayout = (LinearLayout) findViewById(R.id.ll_dialog_base_content);
//		mContentLayout.addView(onAddConentView());
	}
	
	protected View inflateView(int resid) {
		return inflater.inflate(resid, null);
	}
	
	protected abstract View onAddConentView();
	protected abstract void initView();
	protected abstract void initData();
	protected abstract void setListener();
}
