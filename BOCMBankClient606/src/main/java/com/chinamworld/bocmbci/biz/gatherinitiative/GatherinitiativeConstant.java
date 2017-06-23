package com.chinamworld.bocmbci.biz.gatherinitiative;

import java.util.LinkedHashMap;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.RUtil;

public class GatherinitiativeConstant {

	public static class GatherChannelType {

		/** 网上银行用户 */
		public static final String WEB_CHANNEL = "1";
		/** 手机银行用户 */
		public static final String MOBILE_CHANNEL = "2";

		private static LinkedHashMap<String, String> GATHER_CHANNEL_TYPE_MAP = new LinkedHashMap<String, String>();

		static {
			// 网上银行用户
			GATHER_CHANNEL_TYPE_MAP.put(WEB_CHANNEL, RUtil.getString(R.string.user_from_line_bank));
			// 手机银行用户
			GATHER_CHANNEL_TYPE_MAP.put(MOBILE_CHANNEL, RUtil.getString(R.string.user_from_phone_bank));

		}

		/**
		 * 返回手机渠道描述
		 * 
		 * @param transferWayTypeCode手机渠道代码
		 */
		public static String getTransferWayTypeStr(String transferWayTypeCode) {
			if (GATHER_CHANNEL_TYPE_MAP.containsKey(transferWayTypeCode)) {
				return GATHER_CHANNEL_TYPE_MAP.get(transferWayTypeCode);
			}
			return "";
		}

	}

}
