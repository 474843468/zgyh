package com.boc.bocsoft.remoteopenacc.common.view;

import com.boc.bocsoft.remoteopenacc.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 提示信息对话框
 * @author lxw
 *
 */
public class MessageDialog extends BaseDialog{

	/****
	 * 错误提示对话框
	 */
	private View contentView;
	private Button enterBtn;
	private TextView contentTv;
	private OnOkClick onOkClick;
	
	public MessageDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	protected View onAddConentView() {
		contentView = inflateView(R.layout.bocroa_dialog_error);
		return contentView;
	}

	@Override
	protected void initView() {
		contentTv = (TextView) contentView
				.findViewById(R.id.tv_dialog_error_content);
		enterBtn = (Button) contentView
				.findViewById(R.id.btn_dialog_error_enter);
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setListener() {
		enterBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (onOkClick != null) {
					onOkClick.OnOKClick();
				}
				cancel();
			}
		});
	}
	
	protected void showErrorDialog(String errorMsg) {

		setErrorData(errorMsg);

		if(!isShowing()){
			show();
		}
	}
	
	public MessageDialog setErrorData(String msg) {
		// 内容
		contentTv.setText(msg);
		return this;
	}
	
	public void setOnOkClick(OnOkClick onOkClick) {
		this.onOkClick = onOkClick;
	}



	public interface OnOkClick{
		public boolean OnOKClick();
	}
}
