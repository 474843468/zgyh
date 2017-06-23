package com.boc.bocsoft.remoteopenacc.buss.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.common.activity.BaseActivity;
import com.boc.bocsoft.remoteopenacc.common.activity.BaseFragment;
import com.boc.bocsoft.remoteopenacc.common.activity.BaseFragmentManager;
import com.boc.bocsoft.remoteopenacc.common.activity.FragmentNavInterface;

/**
 * 远程开户主Activity
 * 
 * @author lxw
 * 
 */
public class RemoteOpenAccActivity extends BaseActivity implements
		OnClickListener {

	private FragmentManager mFragmentManager;
	private Button btnToBack;
	private BaseFragment mCurrentFragment;
	// private LinkedList<BaseFragment> fragmentList = new
	// LinkedList<BaseFragment>();
	private RemobeOpenAccNavigationFragment navFragment = new RemobeOpenAccNavigationFragment();

	public final static int JUMP_IN = R.anim.bocroa_infromleft;
	public final static int JUMP_OUT = R.anim.bocroa_outtoright;
	public final static int GOBACK_IN = R.anim.bocroa_infromright;
	public final static int GOBACK_OUT = R.anim.bocroa_outtoleft;

	private long lastClickTime;

	private String nameStr;
	private String uuidStr;
	private String systemDateStr;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.bocroa_activity_remote_open_acc);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	protected void initView() {
		mFragmentManager = this.getSupportFragmentManager();
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.replace(R.id.remobe_root, navFragment, CURRENTFRAGMENT);
		ft.addToBackStack("");
		ft.commit();
		// jump2Fragment(navFragment);

	}

	private final String CURRENTFRAGMENT = "currentFragment";// 标识当前Fragment的tag

	/**
	 * 添加fragment跳转方法
	 * 
	 * @param fragment
	 */
	public void jump2Fragment(BaseFragment fragment, boolean addToBackStack) {

		// 防止重复点击
		if ((System.currentTimeMillis() - lastClickTime) < 300) {
			return;
		}
		lastClickTime = System.currentTimeMillis();
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.setCustomAnimations(GOBACK_IN, GOBACK_OUT, JUMP_IN, JUMP_OUT);
		ft.replace(R.id.remobe_root, fragment, CURRENTFRAGMENT);
		ft.show(fragment);
		if (addToBackStack) {
			ft.addToBackStack("");
		}
		BaseFragmentManager.pustFragment(fragment);
		ft.commitAllowingStateLoss();
	}

	public void jump2Fragment(BaseFragment fragment) {
		jump2Fragment(fragment, true);
	}

	/**
	 * 返回到主页面
	 */
	public void jumpToHome() {
		int count = mFragmentManager.getBackStackEntryCount();
		while (count > 1) {
			mFragmentManager.popBackStackImmediate();
			count = mFragmentManager.getBackStackEntryCount();
		}
	}

	/**
	 * 返回操作
	 */
	public void goBack() {
		mFragmentManager.popBackStackImmediate();
		int count = mFragmentManager.getBackStackEntryCount();
		if (count == 0) {
			this.finish();
		}
	}

	@Override
	public void onClick(View view) {

	}

	@Override
	public void onTaskSuccess(Message result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTaskFault(Message result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 处理回退键
			BaseFragment currentFragment = (BaseFragment) mFragmentManager
					.findFragmentByTag(CURRENTFRAGMENT);
			// if (currentFragment instanceof RemobeOpenAccSubStepFragment) {
			// RemobeOpenAccSubStepFragment subStepFragment =
			// (RemobeOpenAccSubStepFragment) currentFragment;
			// if (subStepFragment.isViewShow()) {
			// subStepFragment.hidAlertSelectView();
			// return true;
			// }
			// }
			if (!currentFragment.onGoBackFragment()) {
				return true;
			}
			if (mFragmentManager.getBackStackEntryCount() == 1) {
				this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean goBackFragment() {

		return true;
	}

	@Override
	protected FragmentNavInterface getFragmentNavInterface() {

		return new FragmentNavInterface() {

			@Override
			public void goBack() {
				RemoteOpenAccActivity.this.goBack();
			}

			@Override
			public void jump(BaseFragment fragment) {
				RemoteOpenAccActivity.this.jump2Fragment(fragment);
			}

			@Override
			public void jumpToHome() {
				RemoteOpenAccActivity.this.jumpToHome();
			}

			@Override
			public void jump(BaseFragment fragment, boolean addToBackStack) {
				RemoteOpenAccActivity.this.jump2Fragment(fragment,
						addToBackStack);
			}

		};
	}

	public String getNameStr() {
		return nameStr;
	}

	public void setNameStr(String nameStr) {
		this.nameStr = nameStr;
	}

	public String getUuidStr() {
		return uuidStr;
	}

	public void setUuidStr(String uuidStr) {
		this.uuidStr = uuidStr;
	}

	public String getSystemDateStr() {
		return systemDateStr;
	}

	public void setSystemDateStr(String systemDateStr) {
		this.systemDateStr = systemDateStr;
	}

}
