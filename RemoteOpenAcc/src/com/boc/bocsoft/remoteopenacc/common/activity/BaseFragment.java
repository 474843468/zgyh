package com.boc.bocsoft.remoteopenacc.common.activity;

import com.boc.bocma.exception.MAOPException;
import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.common.service.OnTaskFinishListener;
import com.boc.bocsoft.remoteopenacc.common.util.ViewUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 基础fragment类
 * 
 * @author lxw
 * 
 */
public abstract class BaseFragment extends Fragment implements
		OnTaskFinishListener {

	protected BaseActivity mBaseActivity;
	protected Context mContext;
	private View mContentView;

	private View btn_titleBack;
	protected TextView tv_titleText;
	private ViewGroup main_title_center;
	private FragmentNavInterface navInterface;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mBaseActivity = (BaseActivity) getActivity();
		navInterface = mBaseActivity.getFragmentNavInterface();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mContentView = inflater.inflate(R.layout.bocroa_fragment_base, null);
		FrameLayout containerView = (FrameLayout) mContentView
				.findViewById(R.id.fragment_content);
		// 主页顶部视图
		// 返回按钮
		btn_titleBack = mContentView.findViewById(R.id.main_title_goback);

		main_title_center = (ViewGroup) mContentView
				.findViewById(R.id.main_title_center);
		tv_titleText = (TextView) mContentView
				.findViewById(R.id.main_title_text_tv);
		btn_titleBack.setOnClickListener(goBackOnClickListener);

		initTitle();
		containerView.removeAllViews();
		containerView.addView(onCreateView(inflater));
		initView();
		initData();
		setListener();
		return mContentView;
	}

	/**
	 * 设置标题
	 */
	private void initTitle() {
		String maintitle = getMainTitleText();
		if (maintitle != null) {
			tv_titleText.setText(getMainTitleText());
			tv_titleText.setVisibility(View.VISIBLE);
		}
	}

	protected abstract View onCreateView(LayoutInflater mInflater);

	protected abstract void initView();

	protected abstract void initData();

	protected abstract void setListener();

	/**
	 * 返回当前fragment对应的title文字信息，不显示文字信息返回null
	 * 
	 * @return
	 */
	public abstract String getMainTitleText();

	/**
	 * 显示错误信息
	 * 
	 * @param errorMsg
	 */
	public void showErrorDialog(String errorMsg) {
		mBaseActivity.showErrorDialog(errorMsg);
	}

	/**
	 * 显示loading框
	 * 
	 * @param progressContent
	 */
	public void showProgressDialog(String progressContent) {
		mBaseActivity.showProgressDialog(progressContent);
	}

	/**
	 * 显示loading框
	 */
	public void showProgressDialog() {
		mBaseActivity.showProgressDialog(null);
	}

	/**
	 * 显示关闭按钮
	 */
	public void showProgressDialog(boolean isDisplay) {
		mBaseActivity.showProgressDialog(null, isDisplay);
	}

	/**
	 * 关闭loading框
	 */
	public void closeProgressDialog() {
		mBaseActivity.closeProgressDialog();
	}

	@Override
	public void onTaskFault(Message msg) {
		closeProgressDialog();
		if (!isVisible()) {
			return;
		}
		MAOPException error = (MAOPException) msg.obj;
		if (error == null) {
			return;
		}
		// if (error.getType() == MARemoteException.EXCEPTIONTYPE_NETWORK) {
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
		mBaseActivity.showErrorDialog(error.getMessage());
	}

	private OnClickListener goBackOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			int id = v.getId();
			if (id == R.id.main_title_goback) {
				onBackIconClick(v);
			} else {
			}
		}

	};

	/**
	 * 返回按钮点击事件
	 */
	public void onBackIconClick(View v) {
		// 隐藏软键盘
		ViewUtils.hiddenKeyboard(getActivity(), v);
		if (navInterface != null && onGoBackFragment()) {
			navInterface.goBack();
		}
	}

	public void jumpToFragment(BaseFragment fragment) {
		navInterface.jump(fragment);
	}

	public void jumpToFragment(BaseFragment fragment, boolean addToBackStack) {
		navInterface.jump(fragment, addToBackStack);
	}

	public void jumpToHome() {
		navInterface.jumpToHome();
	}

	public boolean onGoBackFragment() {
		return true;
	}

	private BaseActivity.FregmentTouchListtener mFregmentTouchListtener = new BaseActivity.FregmentTouchListtener() {
		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			if (isHidden())
				return false;
			return BaseFragment.this.dispatchTouchEvent(ev);
		}
	};

	public boolean dispatchTouchEvent(MotionEvent ev) {

		return false;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			if (mBaseActivity != null) {
				mBaseActivity
						.registerFregmentTouchListtener(mFregmentTouchListtener);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mBaseActivity != null) {
			mBaseActivity
					.registerFregmentTouchListtener(mFregmentTouchListtener);
		}
	}
}
