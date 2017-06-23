package com.chinamworld.bocmbci.biz.push;

public class PushCount {
	/**
	 * 请求总次数
	 */
	public static int REQUEST_COUNT = 0;
	/**
	 * 请求成功总次数
	 */
	public static int REQUEST_SUCCESS_COUNT = 0;
	/**
	 * 请求失败总个数
	 */
	public static int REQUEST_FAIED_COUNT = REQUEST_COUNT - REQUEST_SUCCESS_COUNT;
	/**
	 * 消息总个数
	 */
	public static int MESSAGE_COUNT = 0;
	/**
	 * 已读总个数
	 */
	public static int READED_COUNT = 0;
	/**
	 * 未读总个数
	 */
	public static int UNREAD_COUNT = MESSAGE_COUNT - READED_COUNT;

	public static void reset() {
		REQUEST_COUNT = 0;
		REQUEST_SUCCESS_COUNT = 0;
		REQUEST_FAIED_COUNT = 0;
		MESSAGE_COUNT = 0;
		READED_COUNT = 0;
		UNREAD_COUNT = 0;
	}

	public static String getPushCountInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("请求总次数:");
		sb.append(REQUEST_COUNT);
		sb.append(",请求成功总次数:");
		sb.append(REQUEST_SUCCESS_COUNT);
		sb.append(",请求失败总个数:");
		sb.append(REQUEST_FAIED_COUNT);
		sb.append(",消息总个数:");
		sb.append(MESSAGE_COUNT);
		sb.append(",已读总个数:");
		sb.append(READED_COUNT);
		sb.append(",未读总个数:");
		sb.append(UNREAD_COUNT);
		return sb.toString();
	}
}
