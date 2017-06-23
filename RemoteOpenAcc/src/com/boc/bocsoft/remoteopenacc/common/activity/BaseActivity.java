package com.boc.bocsoft.remoteopenacc.common.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.boc.bocma.exception.MAOPException;
import com.boc.bocsoft.remoteopenacc.common.service.OnTaskFinishListener;
import com.boc.bocsoft.remoteopenacc.common.view.LoadingDialog;
import com.boc.bocsoft.remoteopenacc.common.view.MessageDialog;

/**
 * 基础Activity类
 * 
 * @author lxw
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements
		OnTaskFinishListener {

	public final static String ACTION_SERVICE_TIMEOUT = "boc.intent.action.SERVICE_TIMEOUT";
	public final static String ACTION_SERVICE_FAULT = "boc.intent.action.SERVICE_FAULT";

	private LoadingDialog progressDialog;
	private MessageDialog errorDialog;
	private List<FregmentTouchListtener> fregmentTouchListteners;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		// IntentFilter filter = new IntentFilter(ACTION_SERVICE_TIMEOUT);
		// filter.addAction(ACTION_SERVICE_FAULT);
		// faultReceiver = new InterfaceFaultReceiver();
		// registerReceiver(faultReceiver, filter);

		// TODO 注册超时处理
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initView();
		initData();
		setListener();
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		initView();
		initData();
		setListener();
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		super.setContentView(view, params);
		initView();
		initData();
		setListener();
	}

	protected abstract void initView();

	protected abstract void initData();

	protected abstract void setListener();

	protected abstract FragmentNavInterface getFragmentNavInterface();

	/**
	 * 显示错误信息
	 * 
	 * @param errorMsg
	 */
	public void showErrorDialog(String errorMsg) {
		if (errorDialog == null) {
			errorDialog = new MessageDialog(this);
		}
		errorDialog.setErrorData(errorMsg);
		closeProgressDialog();
		if (!errorDialog.isShowing()) {
			errorDialog.show();
		}
	}

	/**
	 * 显示loading框
	 * 
	 * @param progressContent
	 */
	public final void showProgressDialog(String progressContent) {
		try {
			if (progressDialog == null) {
				progressDialog = new LoadingDialog(this);
				progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
			}
			// progressDialog.setLoadText(progressContent);
			// progressDialog.isOpenLoadImageView(true);
			if (!progressDialog.isShowing()) {
				progressDialog.setCloseDispaly(true);
				progressDialog.show();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 显示loading框
	 * 
	 * @param progressContent
	 */
	public final void showProgressDialog(String progressContent,
			boolean isDisplayClose) {
		try {
			if (progressDialog == null) {
				progressDialog = new LoadingDialog(this);
				progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
			}

			if (!progressDialog.isShowing()) {
				progressDialog.setCloseDispaly(isDisplayClose);
				progressDialog.show();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 关闭loading框
	 */
	public void closeProgressDialog() {
		try {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.cancel();
			}

			if (errorDialog != null && errorDialog.isShowing()) {
				errorDialog.cancel();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void onTaskFault(Message msg) {
		closeProgressDialog();
		MAOPException error = (MAOPException) msg.obj;
		if (error == null) {
			return;
		}
		// if (error.getType() == MAOPException.EXCEPTIONTYPE_NETWORK) {
		// showMessageDialog(mContext
		// .getString(R.string.error_network_exception));
		// } else if (error.getType() == MARemoteException.EXCEPTIONTYPE_RESULT)
		// {
		// if (!MEBPublicUtils.isEmpty(error.getMessage())) {
		// // 对不起，操作未成功!null
		// String errormsg = error.getMessage().replace("null", "");
		// showMessageDialog(errormsg);
		// }
		// }
		showErrorDialog(error.getMessage());
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (fregmentTouchListteners != null) {
			for (FregmentTouchListtener listtener : fregmentTouchListteners) {
				listtener.dispatchTouchEvent(ev);
			}
		}

		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 注册touch监听
	 * 
	 * @param mListtener
	 */
	public void registerFregmentTouchListtener(FregmentTouchListtener mListtener) {

		if (mListtener == null) {
			return;
		}
		if (fregmentTouchListteners == null) {
			fregmentTouchListteners = new ArrayList<FregmentTouchListtener>();
		}

		if (!fregmentTouchListteners.contains(mListtener)) {
			fregmentTouchListteners.add(mListtener);
		}
	}

	public static interface FregmentTouchListtener {
		boolean dispatchTouchEvent(MotionEvent ev);
	}
}
