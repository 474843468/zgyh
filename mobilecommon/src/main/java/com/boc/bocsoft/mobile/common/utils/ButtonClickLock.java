package com.boc.bocsoft.mobile.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *     	public void onItemClick(int position) {
 if(!MEBClickLock.isCanClick("ddingtest")){
 //do nothing
 return;
 }
 //do something
 }

 public void onClick(View v) {
 if(!MEBClickLock.isCanClick("ddingtest")){
 //do nothing
 return;
 }
 //do something
 }
 * 
 * 
 * 
 */

/**
 * click lock 实现暴力点击屏蔽 通过 {@link #isCanClick(String)}
 * 传入组件名字，同名的锁在一定时间内能不允许2次点击会返回false
 */
public class ButtonClickLock {
	/**
	 * 锁的缓存
	 */
	private static Map<String, LockCell> lockCells = new HashMap<String, LockCell>();

	/**
	 * 锁的持续时间
	 */
	private final static long DEFAULT_LOCK_TIME = 500;

	public static class LockCell {
		/**
		 * 锁名，唯一
		 */
		String lockName;
		/**
		 * 锁住，禁止点击时长
		 */
		public long lockDuration = DEFAULT_LOCK_TIME;

		long lastClickTime = -1;

		/**
		 * 是否可以点击
		 * 
		 * @return
		 */
		public boolean isCanClick() {
			long cur = System.currentTimeMillis();
			if (lastClickTime + lockDuration < cur) {
				lastClickTime = cur;
				return true;
			}
			return false;
		}
	}

	/**
	 * @param lockName
	 *            锁名，同一锁名在一定时间内{@link LockCell#lockDuration}禁止2此点击会返回false
	 * @return true:允许点击，调用可以处理点击事件 false:禁止点击，调用会屏蔽点击处理
	 */
	public static boolean isCanClick(String lockName) {
		if (StringUtils.isEmpty(lockName)) {
			return true;
		}
		return getLock(lockName).isCanClick();
	}

	/**
	 * 获取lock 不存在则创建
	 * 
	 * @param lockName
	 * @return
	 */
	public static synchronized LockCell getLock(String lockName) {
		if (StringUtils.isEmpty(lockName)) {
			return null;
		}
		LockCell lockCell = lockCells.get(lockName);
		if (lockCell == null) {
			lockCell = new LockCell();
			lockCell.lockName = lockName;
			lockCells.put(lockName, lockCell);
		}
		return lockCell;
	}
	/**
	 * 从缓存中移除lock
	 * 
	 * @param lockName
	 */
	public static synchronized void removeLock(String lockName) {
		lockCells.remove(lockName);
	}


	/**
	 * 移除锁
	 */
	public static synchronized void clearLock() {
		lockCells.clear();
	}
}
