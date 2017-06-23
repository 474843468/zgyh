package com.chinamworld.bocmbci.biz.push;

import com.chinamworld.bocmbci.constant.SystemConfig;

/**
 * @ClassName: PushConfig
 * @Description: 轮询配置
 * @author luql
 * @date 2013-10-30 上午11:43:39
 */
public class PushConfig {

	/**
	 * 通知栏NotificationId
	 */
	public static final int NotificationId = 1000;

	/**
	 * 票有效期(特殊消息) 60分钟
	 */
	public static final int TICKET_TIME = 1000 * 60 * 60;
	// 轮询时间高级配置{Monday:{time:"00:00-24:00",interval:60000},Tuesday:{time:"00:00-24:00",interval:60000},
	// Wednesday:{time:"00:00-24:00",interval:60000},Thursday:{time:"00:00-24:00",interval:60000},
	// Friday:{time:"00:00-24:00",interval:60000},Saturday:{time:"00:00-24:00",interval:60000},
	// Sunday:{time:"00:00-24:00",interval:60000}}

	public static final int PUSH_INTERVAL = SystemConfig.PUSH_INTERVAL;
}
