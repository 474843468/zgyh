package com.boc.bocsoft.remoteopenacc.common.view;

import com.boc.bocsoft.remoteopenacc.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 提示信息对话框,两个按钮
 * 
 * @author lgw
 * 
 */
public class MessageDialogTwoBtn extends BaseDialog {

	private View contentView;
	private Button enterBtn;
	private Button cancelBtn;
	private TextView contentTv;
	private OnBtnClick onOkClick;

	public MessageDialogTwoBtn(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	protected View onAddConentView() {
		contentView = inflateView(R.layout.bocroa_dialog_two_btn);
		return contentView;
	}

	@Override
	protected void initView() {
		contentTv = (TextView) contentView
				.findViewById(R.id.tv_dialog_error_content);
		enterBtn = (Button) contentView.findViewById(R.id.btn_dialog_enter);
		cancelBtn = (Button) contentView.findViewById(R.id.btn_dialog_cancel);

	}

	@Override
	protected void initData() {

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
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (onOkClick != null) {
					onOkClick.OnCancelClick();
				}
				cancel();
			}
		});
	}

	protected void showErrorDialog(String errorMsg) {

		setMsg(errorMsg);

		if (!isShowing()) {
			show();
		}
	}

	public MessageDialogTwoBtn setMsg(String msg) {
		// 内容
		contentTv.setText(msg);
		return this;
	}

	public void setOnBtnClickListener(OnBtnClick onOkClick) {
		this.onOkClick = onOkClick;
	}

	public abstract class OnBtnClick {
		public abstract boolean OnOKClick();

		public void OnCancelClick() {
		}
	}
}
