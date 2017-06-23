package com.chinamworld.bocmbci.biz.push;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiApi;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeDataCenter;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeDetialActivity;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeMainActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.database.entity.PushMessage;
import com.chinamworld.bocmbci.database.entity.PushMessage.MessageType;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * @ClassName: MessageService
 * @Description: 消息服务
 * @author luql
 * @date 2013-10-31 上午09:28:09
 */
public class MessageService {

	protected static final String TAG = MessageService.class.getSimpleName();
	/** 本地保存数据库最新消息最大个数 */
	public static final int MAX_LOCAL_NEW_MESSAGE = 100;

	private static MessageService instance;
	private static Context mContext;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == ConstantGloble.HTTP_STAGE_CONTENT) {
				try {
					// 消息轮询正常数据返回的处理
					Map<String, Object> resultMap = (Map<String, Object>) msg.obj;
					if (resultMap != null) {
						// 返回数据
						BiiResponse biiResponse = (BiiResponse) resultMap.get(ConstantGloble.HTTP_RESULT_DATA);
						if (biiResponse.isBiiexception()) {
							// 有异常
							PushCount.REQUEST_FAIED_COUNT++;
							FLogGloble.e(TAG, "返回数据有异常." + "PushCount:" + PushCount.getPushCountInfo());
						} else {
							// 回调对象
							PushCount.REQUEST_SUCCESS_COUNT++;
							List<BiiResponseBody> biiResponseBodyList = biiResponse.getResponse();
							if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
								BiiResponseBody biiResponseBody = biiResponseBodyList.get(0);
//								List<PushMessage> pushMessages = doRequestNewAndVipMessageCallback(biiResponseBody);
								List<PushMessage> pushMessages = new ArrayList<PushMessage>();
								List<PushMessage> newMessages = doRequestNewMessageCallback(biiResponseBody);
								List<PushMessage> vipMessages = doRequestVipMessageCallback(biiResponseBody);
								pushMessages.addAll(newMessages);
								pushMessages.addAll(vipMessages);
								if (!StringUtil.isNullOrEmpty(pushMessages)) {
									Intent intent = null;
									PushMessage pushMessage = pushMessages.get(0);
									// 更新通知栏消息条数
									InfoServeDataCenter dataCenter = InfoServeDataCenter.getInstance();
									int notifiCacheNum = dataCenter.getCacheNotificationNumber();
									String notifiCacheTitle = dataCenter.getCacheNotificationTitle();

									int notifiSumNum = notifiCacheNum + pushMessages.size();
									dataCenter.setNotificationNumber(notifiSumNum);
									if (notifiSumNum == 1  && pushMessage.getMessageType() == MessageType.New) {
										// 只有1条并且是特殊消息进入详情
										intent = new Intent(mContext, InfoServeDetialActivity.class);
										intent.putExtra(Push.INTENT_MESSAGE, pushMessage);
										intent.putExtra(Push.INTENT_SRC, Push.INTENT_NOTIFICATION);
										// 是否清空记录标志
										intent.putExtra(Push.INTENT_RESET, true);
										intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									} else {
										// 有多条进入列表
										intent = new Intent(mContext, InfoServeMainActivity.class);
										intent.putExtra(Push.INTENT_MESSAGE, pushMessage);
										intent.putExtra(Push.INTENT_SRC, Push.INTENT_NOTIFICATION);
										// 是否清空记录标志
										intent.putExtra(Push.INTENT_RESET, true);
										intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									}

									String title = String.format(mContext.getText(R.string.notification_title)
											.toString(), notifiSumNum);
									// 更新通知栏标题
									StringBuffer content = new StringBuffer();
									for (int i = 0, size = pushMessages.size(); i < size; i++) {
										PushMessage pmsg = pushMessages.get(i);
										String notification = pmsg.getNotification();
										content.append(notification);
										if (i < size - 1) {
											content.append(",");
										}
									}

									if (!StringUtil.isNullOrEmpty(notifiCacheTitle)) {
										content.append(",");
										content.append(notifiCacheTitle);
									}
									dataCenter.setNotificationTitle(content.toString());

									// TODO 根据类型进入不同页面
									TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
									stackBuilder.addParentStack(InfoServeMainActivity.class);
									stackBuilder.addNextIntent(intent);

									PendingIntent pdIntent = stackBuilder.getPendingIntent(0,
											PendingIntent.FLAG_UPDATE_CURRENT);

									NotificationCompat.Builder binder = DefaultNotificationBuilder.binder(mContext,
											title, title, content.toString(), R.drawable.icon, pdIntent);
									binder.setContentIntent(pdIntent);

									NotificationManager nm = (NotificationManager) mContext
											.getSystemService(Context.NOTIFICATION_SERVICE);
									nm.notify(PushConfig.NotificationId, binder.build());

									InfoServeDataCenter.getInstance().setUserShowNewMessageIndicator(
											!newMessages.isEmpty());
									InfoServeDataCenter.getInstance().setUserShowVipMessageIndicator(
											!vipMessages.isEmpty());
									BaseActivity currentAct = (BaseActivity)BaseDroidApp.getInstanse().getCurrentAct();
									if (currentAct != null) {
										currentAct.updateMessageView();
									}
								}
							}
						}
					} else {
						PushCount.REQUEST_FAIED_COUNT++;
						LogGloble.e(TAG, "resultMap is null");
					}
				} catch (Exception e) {
					PushCount.REQUEST_FAIED_COUNT++;
					FLogGloble.e(TAG, e.getMessage(), e);
				}
			}
		}
	};

	private List<PushMessage> doRequestNewMessageCallback(BiiResponseBody biiResponseBody) {
		List<PushMessage> pushMessages = new ArrayList<PushMessage>();
		Map<String, Object> map = (Map<String, Object>) (biiResponseBody.getResult());
		if (map == null) {
			return pushMessages;
		}
		int newMessageCount = 0;
		if (map.get(Push.PNS001_NewMessageNum_RESULT) != null) {
			newMessageCount = Integer.valueOf((String) map.get(Push.PNS001_NewMessageNum_RESULT));
		}
		// 最新消息
		if (newMessageCount > 0) {
			List<Map<String, String>> newLists = (List<Map<String, String>>) map.get(Push.PNS001_NewMessageList_RESULT);
			if (newLists != null && !newLists.isEmpty()) {
				for (Map<String, String> vip : newLists) {
					try {
						String contentId = vip.get(Push.PNS001_ContentID_RESULT);
						String notification = vip.get(Push.PNS001_Notification_RESULT);
						String sourceId = vip.get(Push.PNS001_SourceID_RESULT);
						String contentStyle = vip.get(Push.PNS001_ContentStyle_RESULT);
						String applicationid = vip.get(Push.PNS001_ApplicationID_RESULT);
						String dateTime = vip.get(Push.PNS001_DateTime_RESULT);
						PushMessage newMessage = new PushMessage();
						newMessage.setContentId(contentId);
						newMessage.setNotification(notification);
						newMessage.setSourceId(sourceId);
						newMessage.setContentStyle(contentStyle);
						newMessage.setApplicationId(applicationid);
						newMessage.setDateTime(InfoServeMainActivity.sdf.parse(dateTime).getTime());
						pushMessages.add(newMessage);
					} catch (Exception e) {
						LogGloble.e(TAG, e.getMessage(), e);
					}
				}
			}
		}
		return pushMessages;
	}

	private List<PushMessage> doRequestVipMessageCallback(BiiResponseBody biiResponseBody) {
		List<PushMessage> pushMessages = new ArrayList<PushMessage>();
		Map<String, Object> map = (Map<String, Object>) (biiResponseBody.getResult());
		if (map == null) {
			return pushMessages;
		}
		int vipMessageCount = 0;
		if (map.get(Push.PNS001_VipMessageNum_RESULT) != null) {
			vipMessageCount = Integer.valueOf((String) map.get(Push.PNS001_VipMessageNum_RESULT));
		}

		// 特殊消息
		if (vipMessageCount > 0) {
			List<Map<String, String>> vipLists = (List<Map<String, String>>) map.get(Push.PNS001_VipMessageList_RESULT);
			if (vipLists != null && !vipLists.isEmpty()) {
				for (Map<String, String> vip : vipLists) {
					try {
						String contentId = vip.get(Push.PNS001_ContentID_RESULT);
						String notification = vip.get(Push.PNS001_Notification_RESULT);
						String sourceId = vip.get(Push.PNS001_SourceID_RESULT);
						String contentStyle = vip.get(Push.PNS001_ContentStyle_RESULT);
						String applicationid = vip.get(Push.PNS001_ApplicationID_RESULT);
						String dateTime = vip.get(Push.PNS001_DateTime_RESULT);
						PushMessage vipMessage = new PushMessage();
						vipMessage.setContentId(contentId);
						vipMessage.setNotification(notification);
						vipMessage.setSourceId(sourceId);
						vipMessage.setContentStyle(contentStyle);
						vipMessage.setApplicationId(applicationid);
						vipMessage.setDateTime(InfoServeMainActivity.sdf.parse(dateTime).getTime());
						pushMessages.add(vipMessage);
					} catch (Exception e) {
						LogGloble.e(TAG, e.getMessage(), e);
					}
				}
			}
		}
		return pushMessages;
	}

	private MessageService() {
	}

	public static synchronized MessageService getInstance(Context context) {
		if (instance == null) {
			instance = new MessageService();
		}
		mContext = context;
		return instance;
	}

	// ------------------------------------------------------------------------------------
	// 设备绑定与认证

	/**
	 * 消息推送取票接口
	 * 
	 * @param contentId 内容Id
	 * @param successCallBackMethod
	 */
	public void getTicketForMessage(String contentId, HttpObserver callBack, String successCallBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Push.TicketForMessageMethod);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Push.TicketId, contentId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, callBack, successCallBackMethod);
	}

	// -----------------------------------------------------------------------------------------
	// 缓存

	// -----------------------------------------------------------------------------------------
	// 本地接口

	public void optimizeLocalPushMessage(String deviceId) {
		try {
			MessageDao messageDao = new MessageDao(BaseDroidApp.getInstanse().getDbHelper());
			messageDao.deleteOptimizeLocalCollection();
			//delete from msg where id = (select id from msg order by time desc limit 2,100000);
//			BaseDroidApp.getInstanse().getDbHelper().getWritableDatabase();
//			List<PushMessage> newMessages = messageDao.getNewMessages();
//			if (newMessages.size() > MAX_LOCAL_NEW_MESSAGE) {
//				List<PushMessage> ndMessage = newMessages.subList(MAX_LOCAL_NEW_MESSAGE, newMessages.size());
//				if (!ndMessage.isEmpty()) {
//					boolean deleteMessages = messageDao.deleteMessages(ndMessage);
//					if (deleteMessages) {
//						LogGloble.i(TAG, "消息推送优化删除最新消息" + ndMessage.size() + "个");
//					}
//				}
//			}
//
//			if (deviceId != null) {
//				List<PushMessage> vipMessages = messageDao.getVipMessages(deviceId);
//				if (vipMessages.size() > MAX_LOCAL_NEW_MESSAGE) {
//					List<PushMessage> vdMessage = vipMessages.subList(MAX_LOCAL_NEW_MESSAGE, vipMessages.size());
//					if (!vdMessage.isEmpty()) {
//						boolean deleteMessages = messageDao.deleteMessages(vdMessage);
//						if (deleteMessages) {
//							LogGloble.i(TAG, "消息推送优化删除特殊消息" + vdMessage.size() + "个");
//						}
//					}
//				}
//			}
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
	}

	/**
	 * 获取本地所有消息
	 * 
	 * @return 返回消息列表
	 */
	// public List<PushMessage> getLocalMessages(String deviceId) {
	// MessageDao messageDao = new
	// MessageDao(BaseDroidApp.getInstanse().getDbHelper());
	// return messageDao.getMessages(deviceId);
	// }

	/**
	 * 获取到本地特殊消息
	 * 
	 * @return 如果本地消息为空返回空集合
	 */
	public List<PushMessage> getLocalVipMessages(String deviceId) {
		if (deviceId == null) {
			return new ArrayList<PushMessage>();
		}
		MessageDao messageDao = new MessageDao(BaseDroidApp.getInstanse().getDbHelper());
		return messageDao.getVipMessages(deviceId);
	}

	/**
	 * 获取本地最新消息
	 * 
	 * @return 如果本地消息为空返回空集合
	 */
	public List<PushMessage> getLocalNewMessages() {
		MessageDao messageDao = new MessageDao(BaseDroidApp.getInstanse().getDbHelper());
		return messageDao.getNewMessages();
	}

	/**
	 * 删除一条消息
	 * 
	 * @param message 消息
	 * @return true删除成功,false删除失败
	 */
	public boolean deleteLocalMessage(PushMessage message) {
		return deleteLocalMessage(message.getContentId());
	}

	/**
	 * 删除一条消息
	 * 
	 * @param contentId 消息Id
	 * @return true删除成功,false删除失败
	 */
	public boolean deleteLocalMessage(String contentId) {
		MessageDao messageDao = new MessageDao(BaseDroidApp.getInstanse().getDbHelper());
		return messageDao.deleteMessage(contentId);
	}

	/**
	 * 根据类型删除消息
	 * 
	 * @param messageTypes
	 * @return
	 */
	public boolean deleteLocalMessagesByType(MessageType... messageTypes) {
		MessageDao messageDao = new MessageDao(BaseDroidApp.getInstanse().getDbHelper());
		return messageDao.deleteMessagesByType(messageTypes);
	}

	/**
	 * 批量删除消息
	 * @return true 清除成功，false清除失败
	 */
	public boolean removeLocalMessages(Collection<PushMessage> messages) {
		MessageDao messageDao = new MessageDao(BaseDroidApp.getInstanse().getDbHelper());
		return messageDao.deleteMessages(messages);
	}

	/**
	 * 更新本地消息
	 * 
	 * @param message 消息
	 * @return true 更新成功，false更新失败
	 */
	public boolean updateLocalMessage(PushMessage message) {
		MessageDao messageDao = new MessageDao(BaseDroidApp.getInstanse().getDbHelper());
		PushMessage m = messageDao.getMessage(message.getContentId());
		if (m != null) {
			return messageDao.updateMessage(message);
		} else {
			return messageDao.addMessage(message);
		}
	}

	// ----------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------
	// 消息接口

	/**
	 * 获取轮询消息
	 * 
	 * @param deviceType
	 * @param deviceId
	 */
	public void pushMessages(String deviceType, String deviceId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Push.PNS005);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Push.PNS005_DeviceType, deviceType);
		params.put(Push.PNS005_DeviceID, deviceId);
		biiRequestBody.setParams(params);
		HttpManager.requestPush(biiRequestBody, mHandler);
	}

	public void getMessages(String deviceType, String deviceId, HttpObserver callBack, String successCallBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Push.PNS001);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Push.PNS005_DeviceType, deviceType);
		params.put(Push.PNS005_DeviceID, deviceId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(BiiApi.BASE_PUSH_RUL, biiRequestBody, callBack, successCallBackMethod);
	}

	/**
	 * 获取一条特别消息
	 * 
	 * @param pushDevice 推送设备
	 * @param contentId 消息Id
	 * @param ticket 票
	 * @param callBack 回调
	 */
	public void getVipMessage(PushDevice pushDevice, String contentId, String ticket, HttpObserver callBack,
			String successCallBackMethod) {
		getVipMessage(pushDevice.getDeviceType(), pushDevice.getDeviceId(), contentId, ticket, callBack,
				successCallBackMethod);
	}

	/**
	 * 获取一条特别消息
	 * 
	 * @param deviceType 推送设备类型
	 * @param deviceId 推送设备Id
	 * @param contentId 消息Id
	 * @param ticket 票
	 * @param callBack 回调
	 */
	public void getVipMessage(String deviceType, String deviceId, String contentId, String ticket,
			HttpObserver callBack, String successCallBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Push.PNS002);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Push.PNS002_DeviceType, deviceType);
		params.put(Push.PNS002_DeviceID, deviceId);
		params.put(Push.PNS002_ContentID, contentId);
		params.put(Push.PNS002_Ticket, ticket);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(BiiApi.BASE_PUSH_RUL, biiRequestBody, callBack, successCallBackMethod);
	}

	/**
	 * 获取一条普通消息
	 * 
	 * @param PushDevice 推送设备
	 * @param contentId 消息Id
	 */
	public void getNewMessage(PushDevice pushDevice, String contentId, HttpObserver callBack,
			String successCallBackMethod) {
		getNewMessage(pushDevice.getDeviceType(), pushDevice.getDeviceId(), contentId, callBack, successCallBackMethod);
	}

	/**
	 * 获取一条普通消息
	 * 
	 * @param deviceType 推送设备类型
	 * @param deviceId 推送设备Id
	 * @param contentId 消息Id
	 * @param callBack 票
	 * @param successCallBackMethod 回调
	 */
	public void getNewMessage(String deviceType, String deviceId, String contentId, HttpObserver callBack,
			String successCallBackMethod) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Push.PNS003);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Push.PNS003_DeviceType, deviceType);
		params.put(Push.PNS003_DeviceID, deviceId);
		params.put(Push.PNS003_ContentID, contentId);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(BiiApi.BASE_PUSH_RUL, biiRequestBody, callBack, successCallBackMethod);
	}

	// -------------------------------------------------------------------------------------------
	// delete message

	/**
	 * 删除消息,注：该客户对于客户来说是隐式的操作，该接口无返回包
	 * 
	 * @param pushDevice 推送设备
	 * @param contentIds 消息Id
	 * @param callBack 回调对象
	 * @param noCallBack 回调方法
	 */
	public void deleteMessage(PushDevice pushDevice, String contentId, HttpObserver callBack, String noCallBack) {
		deleteMessage(pushDevice.getDeviceType(), pushDevice.getDeviceId(), Arrays.asList(contentId), callBack, null);
	}

	/**
	 * 删除消息,注：该客户对于客户来说是隐式的操作，该接口无返回包
	 * 
	 * @param deviceType 推送设备类型
	 * @param deviceId 推送设备Id
	 * @param contentIds 消息Id
	 * @param callBack 回调对象
	 * @param noCallBack 回调方法
	 */
	public void deleteMessage(String deviceType, String deviceId, String contentId, HttpObserver callBack,
			String noCallBack) {
		deleteMessage(deviceType, deviceId, Arrays.asList(contentId), callBack, null);
	}

	/**
	 * 删除消息,注：该客户对于客户来说是隐式的操作，该接口无返回包
	 * 
	 * @param pushDevice 推送设备
	 * @param contentIds 消息Id
	 * @param callBack
	 * @param noCallBack
	 */
	public void deleteMessage(PushDevice pushDevice, List<String> contentIds, HttpObserver callBack, String noCallBack) {
		deleteMessage(pushDevice.getDeviceType(), pushDevice.getDeviceId(), contentIds, callBack, null);
	}

	/**
	 * 删除消息,注：该客户对于客户来说是隐式的操作，该接口无返回包
	 * 
	 * @param deviceType 推送设备类型
	 * @param deviceId 推送设备Id
	 * @param contentIds 消息Id
	 * @param callBack
	 * @param noCallBack
	 */
	public void deleteMessage(String deviceType, String deviceId, List<String> contentIds, HttpObserver callBack,
			String noCallBack) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Push.PNS004);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Push.PNS004_DeviceType, deviceType);
		params.put(Push.PNS004_DeviceID, deviceId);
		params.put(Push.PNS004_RequestNum, contentIds.size());
		params.put(Push.PNS004_ContentIDList, contentIds);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(BiiApi.BASE_PUSH_RUL, biiRequestBody, callBack, null);
	}

	// -------------------------------------------------------------------------------
	// cache

}
