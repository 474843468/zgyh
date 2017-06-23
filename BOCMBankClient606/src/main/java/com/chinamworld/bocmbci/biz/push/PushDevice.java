package com.chinamworld.bocmbci.biz.push;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.biz.infoserve.push.PushReceiver;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.secure.encdec.DESCoder;
import com.chinamworld.bocmbci.secure.encdec.PBECoder;
import com.chinamworld.bocmbci.utils.DeviceUtils;
import com.chinamworld.bocmbci.utils.SharedPreUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class PushDevice {

	private static final String TAG = PushDevice.class.getSimpleName();
	public static String DefaulDeviceID = "299918571|359540053653731";
	/**
	 * 设备类型(固定值)：设备类型IOS送“01”，WIN送“02”，Android取值为“03”
	 */
	public static final String ANDROID_DEVICE_TYPE = "03";

	/**
	 * 设备ID
	 */
	private String deviceId;

	private PushDevice(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return ANDROID_DEVICE_TYPE;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "PushDevice [deviceType=" + getDeviceType() + ",deviceId=" + deviceId + "]";
	}

	/**
	 * 保持设备到本地
	 * 
	 * @return
	 */
	public boolean save() {
		try {
			// 加密
			String inputData = PBECoder.encrypt(deviceId, DESCoder.DEFAULT_KEY, "salt");
			SharedPreUtils.getInstance().addOrModify(Push.COMM_DEVICE_ID, inputData);
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 加载本地配置
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static PushDevice load() {
		String deviceId = "";
		try {
			// 登陆信息中获取cfi
			HashMap<String, Object> bizDataMap = BaseDroidApp.getInstanse().getBizDataMap();
			if (!StringUtil.isNullOrEmpty(bizDataMap)) {
				Map<String, Object> loginMap = (Map<String, Object>) bizDataMap.get(ConstantGloble.BIZ_LOGIN_DATA);
				if (!StringUtil.isNullOrEmpty(loginMap)) {
					deviceId = (String) loginMap.get(Login.REGISTER_CIF_NUMBER);
				}
			}
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}

		if (TextUtils.isEmpty(deviceId)) {
			String outputData = SharedPreUtils.getInstance().getString(Push.COMM_DEVICE_ID, "");
			try {
				if (!TextUtils.isEmpty(outputData)) {
					//解密
					deviceId = PBECoder.decrypt(outputData, DESCoder.DEFAULT_KEY, "salt");
				}
			} catch (Exception e) {
				LogGloble.e(TAG, e.getMessage(), e);
			}
		} else {
			// 组装
			deviceId = cifIdToDeviceId(deviceId);
		}

		if (TextUtils.isEmpty(deviceId)) {
//			return null;
			deviceId = DefaulDeviceID;
		}
		PushDevice pushDevice = new PushDevice(deviceId);
		return pushDevice;
	}

	/**
	 * 客户Id 转为 设备Id
	 * 
	 * @param customerId 客户Id(登陆信息 )
	 * @return 设备Id
	 */
	public static String cifIdToDeviceId(String customerId) {
		if (TextUtils.isEmpty(customerId)) {
			return null;
		}
		String deviceId = customerId + "|" + DeviceUtils.getIMEI(BaseDroidApp.getContext());
		return deviceId;
	}

	/**
	 * 消息Id 转为 客户Id
	 */
	public static String deviceIdToCifId(String mesId) {
		if (mesId == null) {
			return null;
		}

		String[] split = mesId.split("\\|");
		if (split.length == 2) {
			return split[0];
		}
		return null;

	}

	public static void clear() {
		SharedPreUtils.getInstance().deleteItem(Push.COMM_DEVICE_ID);
	}
}
