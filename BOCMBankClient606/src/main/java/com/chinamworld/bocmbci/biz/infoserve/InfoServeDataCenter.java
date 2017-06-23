package com.chinamworld.bocmbci.biz.infoserve;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.biz.infoserve.bean.NeedReadMessage;
import com.chinamworld.bocmbci.database.entity.PushMessage;

/**
 * @ClassName: InfoServeDataCenter
 * @Description: 消息服务设置数据存储类
 * @author wanbing
 * @date 2013-11-19
 */
public class InfoServeDataCenter {
	private static final String TAG = InfoServeDataCenter.class.getSimpleName();

	private static InfoServeDataCenter dataCenter;
	/** 所有账户列表 */
	private List<Map<String, Object>> accountList;
	/** 大额到账详细 */
	private Map<String, Object> nonFixedProductDetail;
//	/** 理财产品提醒查询数据 */
//	private Map<String, Object> xpadDueDateRemindDetail;
	/** 必读消息集合 */
	private List<NeedReadMessage> mNeedReadMessageList=new ArrayList<NeedReadMessage>();

	/** 特殊消息集合(包含本地缓存) */
	private List<PushMessage> vipMessages;
	/** 最新消息集合(包含本地缓存) */
	private List<PushMessage> newMessages;
	/**vip支持器*/
	private boolean isShowMessageVipIndicator;
	/**消息支持器*/
	private boolean isShowMessageNewIndicator;
	/** 保存通知栏个数 */
	private int notificationNumber = 0;
	/** 保存通知栏标题 */
	private String notificationTitle = "";
	/** 是否刷新手动通知 */
	private boolean mIsRefresh;

	public List<NeedReadMessage> getmNeedReadMessageList() {
		return mNeedReadMessageList;
	}

//	public void setmNeedReadMessageList(List<NeedReadMessage> mNeedReadMessageList) {
//		this.mNeedReadMessageList = mNeedReadMessageList;
//	}

	/**
	 * 获取特殊消息列表(包含本地缓存)
	 * 
	 * @return 返回特殊消息列表
	 */
	public List<PushMessage> getVipMessages() {
		return vipMessages;
	}

	public void setVipMessages(List<PushMessage> vipMessages) {
		this.vipMessages = vipMessages;
	}

	/**
	 * 获取最新消息列表(包含本地缓存)
	 * 
	 * @return 返回最新消息列表
	 */
	public List<PushMessage> getNewMessages() {
		return newMessages;
	}

	public void setNewMessages(List<PushMessage> newMessages) {
		this.newMessages = newMessages;
	}


	private InfoServeDataCenter() {
		isShowMessageVipIndicator = false;
		isShowMessageNewIndicator = false;
	}

	public static InfoServeDataCenter getInstance() {
		if (dataCenter == null) {
			dataCenter = new InfoServeDataCenter();
		}
		return dataCenter;
	}
	
	public Map<String, Object> getNonFixedProductDetail() {
		return nonFixedProductDetail;
	}
	
	public void setNonFixedProductDetail(Map<String, Object> nonFixedProductDetail) {
		this.nonFixedProductDetail = nonFixedProductDetail;
	}

	public List<Map<String, Object>> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Map<String, Object>> accountList) {
		this.accountList = accountList;
	}

	/**
	 * 清楚所有消息缓存
	 */
	public void clearCacheAllMessages() {
		clearCacheVipMessages();
		clearCacheNewMessages();
		clearCacheNeedReadMessages();
	}

	/**
	 * 清楚特殊消息缓存
	 */
	public void clearCacheVipMessages() {
		if (vipMessages != null) {
			vipMessages.clear();
		}
	}
	
	/**
	 * 清除必读消息缓存
	 */
	public void clearCacheNeedReadMessages() {
		if (mNeedReadMessageList != null) {
			mNeedReadMessageList.clear();
		}
	}

	/**
	 * 清楚最新消息缓存
	 */
	public void clearCacheNewMessages() {
		if (newMessages != null) {
			newMessages.clear();
		}
	}

	/**
	 * 手动指定显示最新特殊推送指示器
	 * 
	 * @param isShow true显示，false为不显示
	 */
	public void setUserShowVipMessageIndicator(boolean isShow) {
		isShowMessageVipIndicator = isShow;
	}
	
	/**
	 * 手动指定显示最新消息推送指示器
	 * 
	 * @param isShow true显示，false为不显示
	 */
	public void setUserShowNewMessageIndicator(boolean isShow) {
		isShowMessageNewIndicator = isShow;
	}

	/**
	 * 消息推送指示器,如果指定显示或有未读消息返回true，否则返回false
	 * 
	 * @see #setUserShowMessageIndicator
	 * @return true现实指示器，false不显示指示器
	 */
	public boolean isShowMessageIndicator() {
		return isShowMessageVipIndicator || isShowMessageNewIndicator;
//		if (isShowMessageIndicator) {
//			return true;
//		}
//		if (vipMessages != null && !vipMessages.isEmpty()) {
//			for (PushMessage pm : vipMessages) {
//				if (!pm.isReaded()) {
//					return true;
//				}
//			}
//		}
//
//		if (newMessages != null && !newMessages.isEmpty()) {
//			for (PushMessage pm : newMessages) {
//				if (!pm.isReaded()) {
//					return true;
//				}
//			}
//		}
//		return false;
	}

//	public Map<String, Object> getXpadDueDateRemindDetail() {
//		return xpadDueDateRemindDetail;
//	}
//
//	public void setXpadDueDateRemindDetail(Map<String, Object> xpadDueDateRemindDetail) {
//		this.xpadDueDateRemindDetail = xpadDueDateRemindDetail;
//	}

	public void setNotificationNumber(int notificationNumber) {
		this.notificationNumber = notificationNumber;
	}

	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}

	public String getCacheNotificationTitle() {
		return notificationTitle;
	}

	public int getCacheNotificationNumber() {
		return notificationNumber;
	}

	/**
	 * 清楚通知栏缓存标示
	 */
	public void resetNotification() {
		notificationNumber = 0;
		notificationTitle = "";
	}

	public void clear() {
		clearCacheAllMessages();
		if (accountList != null) {
			accountList.clear();
		}
		if (nonFixedProductDetail != null) {
			nonFixedProductDetail.clear();
		}
//		if (xpadDueDateRemindDetail != null) {
//			xpadDueDateRemindDetail.clear();
//		}
	}

	public boolean isRefreshMessages() {
		return mIsRefresh;
	}

	/**
	 * 修改此标示消息推送列表刷新
	 * 
	 * @param isRefresh
	 */
	public void setIsRefreshMessages(boolean isRefresh) {
		this.mIsRefresh = isRefresh;
	}

}
