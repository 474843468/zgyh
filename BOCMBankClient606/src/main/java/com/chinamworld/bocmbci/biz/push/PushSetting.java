package com.chinamworld.bocmbci.biz.push;

import com.chinamworld.bocmbci.base.application.BaseApplication;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.SharedPreUtils;

/**
 * @ClassName: PushSetting
 * @Description: 消息推送
 * @author luql
 * @date 2013-10-31 上午09:14:20
 */
public class PushSetting {

	private static final String TAG = PushSetting.class.getSimpleName();

	// 版本
	private static final String PUSH_TIME = "push_time";
	private static final String PUSH_VERSION = "push_version";
	private static final String PUSH_STATE_KEY = "push_state";
	private static final String PUAH[] = new String[] { PUSH_VERSION, PUSH_STATE_KEY };

	/**
	 * @Description: 设置轮训时间
	 * @param time 毫秒
	 */
	public static void setPushTime(long time) {
		SharedPreUtils.getInstance().addOrModify(PUSH_TIME, String.valueOf(time));
	}

	/**
	 * @Description: 获取轮训时间
	 */
	public static long getPushTime() {
		try {
			String time = SharedPreUtils.getInstance().getString(PUSH_TIME, String.valueOf(PushConfig.PUSH_INTERVAL));
			return Long.valueOf(time);
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return PushConfig.PUSH_INTERVAL;
	}

	/**
	 * 设置是否开启消息轮询
	 * 
	 * @param isOpen true为开启，false为关闭
	 */
	public static void setPushState(boolean isOpen) {
		SharedPreUtils.getInstance().addOrModify(PUSH_STATE_KEY, String.valueOf(isOpen));
		SharedPreUtils.getInstance().addOrModifyInt(PUSH_VERSION, BaseApplication.APP_VERSION_CODE);
	}

	/**
	 * 获取消息轮询状态
	 * 
	 * @return true为开启轮询，false为关闭轮询
	 */
	public static boolean getPushState() {
		check();

		boolean result = true;
		try {
			String state = SharedPreUtils.getInstance().getString(PUSH_STATE_KEY, Boolean.TRUE.toString());
			result = Boolean.parseBoolean(state);
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
			clear();
		}
		return result;
	}

	private static void check() {
		if (!checkVersion(BaseApplication.APP_VERSION_CODE)) {
			clear();
		}
	}

	private static boolean checkVersion(int version) {
		int currentVersion = SharedPreUtils.getInstance().getInt(PUSH_VERSION, version);
		return currentVersion == version;
	}

	private static void clear() {
		for (String item : PUAH) {
			SharedPreUtils.getInstance().deleteItem(item);
		}
	}
}
