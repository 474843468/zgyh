package com.boc.bocsoft.remoteopenacc.common.activity;


import java.util.Stack;


/**
 * 管理fragment
 * @author lxw
 *
 */
public class BaseFragmentManager {
	
	private static Stack<BaseFragment> fragmentStack;
	
//	private static HashMap<Class<? extends BaseFragment>, BaseFragment> fragmentMap =
//			new HashMap<Class<? extends BaseFragment>, BaseFragment>();

//	private static Class<? extends BaseFragment> defaultFragmentClass;
	
//	public static synchronized BaseFragment getFragmet(
//			Class<? extends BaseFragment> clazz) {
//		BaseFragment baseFragment = fragmentMap.get(clazz);
//		if (null == baseFragment) {
//			try {
//				baseFragment = clazz.newInstance();
//				fragmentMap.put(clazz, baseFragment);
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		return baseFragment;
//	}
//	
	/**
	 * 添加fragment到堆栈
	 */
	public static void pustFragment(BaseFragment baseFragment){
		if(fragmentStack == null){
			fragmentStack = new Stack<BaseFragment>();
		}
		fragmentStack.push(baseFragment);
	}
	
	/**
	 * 获取当前fragment（堆栈中最后一个压入的）
	 */
	public static BaseFragment currentFragemnt(){
		BaseFragment fragment = fragmentStack.lastElement();
		return fragment;
	}
	
	/**
	 * 删除fragment
	 * 
	 * @param mFragment
	 */
	public static BaseFragment  popFragment() {
		return fragmentStack.pop();
			
	}
	
	public static boolean isEmpty(){
		if (fragmentStack != null) {
			return fragmentStack.isEmpty();
		}
		return true;
	}

//	public static Class<? extends BaseFragment> getDefaultFragmentClass() {
//		return defaultFragmentClass;
//	}
//
//	public static void setDefaultFragmentClass(
//			Class<? extends BaseFragment> defaultFragmentClass) {
//		BaseFragmentManager.defaultFragmentClass = defaultFragmentClass;
//	}
	
//	/**
//	 * 切换用户后，重置所有未被回收的fragment
//	 */
//	public static void resetAllFragment() {
//		Fragment mebBaseFragment = fragmentMap
//				.get(MEBMainPageFragment.class);
//		fragmentMap.clear();
//		fragmentMap.put(MEBMainPageFragment.class, mebBaseFragment);
//	}
//	
//	/**
//	 * 应用中是否已存在指定fragment的实例
//	 * @author hcp 2014-12-12
//	 * @param clazz
//	 * @return
//	 */
//	public static synchronized boolean containFragment(
//			Class<? extends MEBBaseFragment> clazz) {
//		MEBBaseFragment baseFragment = fragmentMap.get(clazz);
//		return null!=baseFragment;
//	}
}
