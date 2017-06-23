package com.boc.bocsoft.remoteopenacc.buss.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.common.activity.BaseFragment;

/**
 * 远程开户申请结果页面
 * 
 * @author lxw
 * 
 */
public class RemobeApplayOnlineAccResultFragment extends BaseFragment {

	private View mRoot;
	private Button mSubmitButton;
	private ImageView iv_result_icon;
	private TextView tv_result_title;
	private TextView tv_result_notice;
	private int mDrawable = -1;
	private String title;
	private String notice;
	private String btnName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle arguments = getArguments();
		if (arguments != null) {
			mDrawable = arguments.getInt("drawable");
			title = arguments.getString("title");
			notice = arguments.getString("notice");
			btnName = arguments.getString("btnName");
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater) {
		mRoot = inflater.inflate(
				R.layout.bocroa_fragment_remote_applay_online_acc_result, null,
				false);
		return mRoot;
	}

	@Override
	protected void initView() {
		mSubmitButton = (Button) mRoot.findViewById(R.id.btn_submit);
		iv_result_icon = (ImageView) mRoot.findViewById(R.id.iv_result_icon);
		tv_result_title = (TextView) mRoot.findViewById(R.id.tv_result_title);
		tv_result_notice = (TextView) mRoot.findViewById(R.id.tv_result_notice);
	}

	@Override
	protected void initData() {
		if (mDrawable != -1) {
			iv_result_icon.setImageResource(mDrawable);
		}
		if (!TextUtils.isEmpty(title)) {
			tv_result_title.setText(title);
		}
		if (!TextUtils.isEmpty(notice)) {
			tv_result_notice.setText(notice);
		}
		if (!TextUtils.isEmpty(btnName)) {
			mSubmitButton.setText(btnName);
		}
	}

	@Override
	public void onTaskSuccess(Message result) {

	}

	@Override
	public void onTaskFault(Message result) {
		super.onTaskFault(result);
	}

	@Override
	protected void setListener() {
		mSubmitButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (!onBtnClick()) {
					RemobeApplayOnlineAccResultFragment.this.jumpToHome();
				}
			}
		});

	}

	/**
	 * 查询开户进度结果处理
	 */
	/*
	 * private void handleOpenAccProgress() {
	 * QueryOpenAccountProgressResponseModel mParams = new
	 * QueryOpenAccountProgressResponseModel(); mParams.appliStat = "1";
	 * mParams.failReason = "wooo"; mParams.vcardNo = "636665555555";
	 * mProgressInqueryResultView.initData(mParams, mAccActivity);
	 * alertSelectView(mProgressInqueryResultView); }
	 */

	@Override
	public String getMainTitleText() {
		// return getResources().getString(R.string.bocroa_progress_query);
		return getResources().getString(R.string.bocroa_applay_result);
	}

	@Override
	public void onBackIconClick(View v) {
		RemobeApplayOnlineAccResultFragment.this.jumpToHome();
	}

	/**
	 * @return true事件被消耗掉子类处理事件，false父类处理事件
	 */
	public boolean onBtnClick() {
		return false;
	}

	/**
	 * @param drawable
	 *            图片
	 * @param title
	 *            图片右侧标题
	 * @param notice
	 *            提示语
	 * @param btnName
	 *            按钮
	 */
	// public void setView(int drawable, String title, String notice,
	// String btnName) {
	// mDrawable = drawable;
	// this.title = title;
	// this.notice = notice;
	// this.btnName = btnName;
	// }
}
