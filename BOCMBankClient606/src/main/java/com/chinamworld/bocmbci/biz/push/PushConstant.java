package com.chinamworld.bocmbci.biz.push;

import java.util.HashMap;

import com.chinamworld.bocmbci.database.entity.PushMessage.MessageType;

/**
 * @ClassName: MessageConstant
 * @Description: 消息定义常量
 * @author luql
 * @date 2013-10-30 上午09:39:26
 * 
 */
public class PushConstant {

	public static class MessageError {
		/** 内容过期 */
		public static final String MESSAGE_UNVALIDATE_CODE = "M00001";

		private static HashMap<String, String> MESSAGE_ERROR_MAP = new HashMap<String, String>();
		static {
			MESSAGE_ERROR_MAP.put(MESSAGE_UNVALIDATE_CODE, "你的消息内容已过期！");
		}

		public static String getErrorDesc(String code) {
			if (MESSAGE_ERROR_MAP.containsKey(MESSAGE_UNVALIDATE_CODE)) {
				return MESSAGE_ERROR_MAP.get(MESSAGE_UNVALIDATE_CODE);
			}
			return "";
		}
	}

	public static class MessageTypeDictionary {
		/** 最新消息 */
		public static final String NEW_MESSAGE_CODE = "00";
		/** 汇入交易金额单笔超过签约值 */
		public static final String FOREX_MESSAGE_CODE = "01";
		/** 客户理财产品到期提醒 */
		public static final String INVESTMENT_MESSAGE_CODE = "02";
		/** 定期及通知存款到期 */
		public static final String TRANSFER_MESSAGE_CODE = "03";
		/** 信用卡还款提醒 */
		public static final String CRCD_MESSAGE_CODE = "04";

		public static HashMap<String, MessageType> MEESAGE_MAP_CODE = new HashMap<String, MessageType>();
		static {
			MEESAGE_MAP_CODE.put(NEW_MESSAGE_CODE, MessageType.New);
			MEESAGE_MAP_CODE.put(FOREX_MESSAGE_CODE, MessageType.Vip);
			MEESAGE_MAP_CODE.put(INVESTMENT_MESSAGE_CODE, MessageType.Vip);
			MEESAGE_MAP_CODE.put(TRANSFER_MESSAGE_CODE, MessageType.Vip);
			MEESAGE_MAP_CODE.put(CRCD_MESSAGE_CODE, MessageType.Vip);
		}

		public static MessageType getMessageCodeToType(String code) {
			if (MEESAGE_MAP_CODE.containsKey(code)) {
				return MEESAGE_MAP_CODE.get(code);
			}
			return null;
		}
	}

}
