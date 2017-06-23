package com.chinamworld.bocmbci.biz.push;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.os.Message;

import com.chinamworld.bocmbci.biz.push.PushConstant.MessageTypeDictionary;
import com.chinamworld.bocmbci.database.DBHelper;
import com.chinamworld.bocmbci.database.entity.PushMessage;
import com.chinamworld.bocmbci.database.entity.PushMessage.MessageType;
import com.chinamworld.bocmbci.log.LogGloble;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

/**
 * @ClassName: MessageDao
 * @Description: 本地消息
 * @author luql
 * @date 2013-11-6 下午02:33:41
 * 
 */
public class MessageDao {

	private static final String TAG = Message.class.getSimpleName();

	private DBHelper dbHelper;

	public MessageDao(DBHelper dbHelper) {
		super();
		this.dbHelper = dbHelper;
	}

	// /**
	// * 获取所有消息,返回顺序为时间排序 select * from message order by dateTime desc | ASC
	// *
	// * @return 返回所有消息
	// */
	// public List<PushMessage> getMessages() {
	// try {
	// Dao<PushMessage, String> messageDao = dbHelper.getDao(PushMessage.class);
	// QueryBuilder<PushMessage, String> queryBuilder =
	// messageDao.queryBuilder();
	// return queryBuilder.orderBy(PushMessage.DATE_TIME, false).query();
	// } catch (SQLException e) {
	// LogGloble.e(TAG, e.getMessage(), e);
	// }
	// return new ArrayList<PushMessage>();
	// }

	/**
	 * 获取所有特殊消息
	 * 
	 * @return 如果消息为空返回空集合
	 */
	public List<PushMessage> getVipMessages(String deviceId) {
		try {
			Dao<PushMessage, String> messageDao = dbHelper.getDao(PushMessage.class);
			QueryBuilder<PushMessage, String> queryBuilder = messageDao.queryBuilder();
			Where<PushMessage, String> where = queryBuilder.where();
			where.eq(PushMessage.DEVICEID_COLUMN, deviceId);
			where.and().ne(PushMessage.CONTENTSTYLE_COLUMN, MessageTypeDictionary.NEW_MESSAGE_CODE);
			return queryBuilder.orderBy(PushMessage.DATE_TIME, false).query();
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return new ArrayList<PushMessage>();
	}

	/**
	 * 获取所有最新消息
	 * 
	 * @return 如果消息为空返回空集合
	 */
	public List<PushMessage> getNewMessages() {
		try {
			Dao<PushMessage, String> messageDao = dbHelper.getDao(PushMessage.class);
			QueryBuilder<PushMessage, String> queryBuilder = messageDao.queryBuilder();
			Where<PushMessage, String> where = queryBuilder.where();
			where.eq(PushMessage.CONTENTSTYLE_COLUMN, MessageTypeDictionary.NEW_MESSAGE_CODE);
			return queryBuilder.orderBy(PushMessage.DATE_TIME, false).query();
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return new ArrayList<PushMessage>();
	}

	/**
	 * 添加一条特殊消息
	 */
	public boolean addMessage(PushMessage message) {
		try {
			Dao<PushMessage, String> messageDao = dbHelper.getDao(PushMessage.class);
			int createId = messageDao.create(message);
			return createId > 0;
		} catch (SQLException e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 获取一条新消息
	 * 
	 * @param contentId 消息id
	 * @return 返回消息，不存在返回null
	 */
	public PushMessage getMessage(String contentId) {
		try {
			Dao<PushMessage, String> messageDao = dbHelper.getDao(PushMessage.class);
			return messageDao.queryForId(contentId);
		} catch (SQLException e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 更新消息
	 * 
	 * @param newMessage
	 * @return true更新成功，否则返回false
	 */
	public boolean updateMessage(PushMessage message) {
		try {
			Dao<PushMessage, String> messageDao = dbHelper.getDao(PushMessage.class);
			int updateId = messageDao.update(message);
			return updateId > 0;
		} catch (SQLException e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 删除一条新消息
	 * 
	 * @param contentId 消息id
	 * @return 删除成功返回true,否则返回false
	 */
	public boolean deleteMessage(String contentId) {
		try {
			Dao<PushMessage, String> messageDao = dbHelper.getDao(PushMessage.class);
			int deleteById = messageDao.deleteById(contentId);
			return deleteById > 0;
		} catch (SQLException e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 根据类型删除消息
	 * 
	 * @param messageTypes
	 * @return
	 */
	public boolean deleteMessagesByType(MessageType... messageTypes) {
		try {
			Dao<PushMessage, String> messageDao = dbHelper.getDao(PushMessage.class);
			List<PushMessage> queryForAll = messageDao.queryForAll();
			List<PushMessage> deleteMessages = new ArrayList<PushMessage>();
			for (PushMessage message : queryForAll) {
				for (MessageType type : messageTypes) {
					if (type == message.getMessageType()) {
						deleteMessages.add(message);
						continue;
					}
				}
			}
			int delete = messageDao.delete(deleteMessages);
			return delete > -1;
		} catch (SQLException e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 清空数据库
	 * 
	 */
	public boolean clearAll() {
		try {
			Dao<PushMessage, String> messageDao = dbHelper.getDao(PushMessage.class);
			int delete = messageDao.deleteBuilder().delete();
			return delete > -1;
		} catch (SQLException e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 返回消息总个数
	 * 
	 * @return 消息从个数，异常返回-1
	 */
	public long getMessagesCountOf() {
		try {
			Dao<PushMessage, String> messageDao = dbHelper.getDao(PushMessage.class);
			return messageDao.countOf();
		} catch (SQLException e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return -1;
	}

	/**
	 * 批量删除消息
	 * 
	 * @param messages 要删除的消息集合
	 * @return 删除成功返回true，否则返回false
	 */
	public boolean deleteMessages(Collection<PushMessage> messages) {
		try {
			Dao<PushMessage, String> messageDao = dbHelper.getDao(PushMessage.class);
			int delete = messageDao.delete(messages);
			return delete > -1;
		} catch (SQLException e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return false;
	}

	public boolean deleteOptimizeLocalCollection() {
		try {
			String newSql = "DELETE FROM " + PushMessage.TABLE_NAME + " WHERE " + PushMessage.CONTENT_ID + " IN "
					+ "(SELECT " + PushMessage.CONTENT_ID + " FROM " + PushMessage.TABLE_NAME + " WHERE "
					+ PushMessage.CONTENTSTYLE_COLUMN + " = '" + MessageTypeDictionary.NEW_MESSAGE_CODE + "' ORDER BY "
					+ PushMessage.DATE_TIME + " DESC LIMIT " + MessageService.MAX_LOCAL_NEW_MESSAGE + " , 10000000);";

			String vipSql = "DELETE FROM " + PushMessage.TABLE_NAME + " WHERE " + PushMessage.CONTENT_ID + " IN "
					+ "(SELECT " + PushMessage.CONTENT_ID + " FROM " + PushMessage.TABLE_NAME + " WHERE "
					+ PushMessage.CONTENTSTYLE_COLUMN + " != '" + MessageTypeDictionary.NEW_MESSAGE_CODE
					+ "' ORDER BY " + PushMessage.DATE_TIME + " DESC LIMIT " + MessageService.MAX_LOCAL_NEW_MESSAGE
					+ " , 10000000);";

			LogGloble.i(TAG, newSql);
			LogGloble.i(TAG, vipSql);
			SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
			writableDatabase.execSQL(newSql);
			writableDatabase.execSQL(vipSql);
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return false;
	}
}
