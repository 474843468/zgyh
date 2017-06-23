package com.boc.bocsoft.remoteopenacc.common.activity;

/**
 * fragment跳转接口
 * @author lxw
 *
 */
public interface FragmentNavInterface {

	public void goBack();
	public void jump(BaseFragment fragment);
	public void jump(BaseFragment fragment,boolean addToBackStack);
	public void jumpToHome();
}
