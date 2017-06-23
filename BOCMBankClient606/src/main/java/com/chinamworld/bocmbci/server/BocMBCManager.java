package com.chinamworld.bocmbci.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.alibaba.fastjson.MyJSON;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.BaseHttpObserver;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.MbcgHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.secure.encdec.DESCoder;
import com.chinamworld.bocmbci.secure.encdec.PBECoder;
import com.chinamworld.bocmbci.utils.DeviceUtils;
import com.chinamworld.bocmbci.utils.SharedPreUtils;

/**
 * 
 * @Description: Boc统计
 * @author: luql
 * @date: 2015年2月2日 下午4:14:15
 */
public class BocMBCManager extends BaseHttpObserver {

	private static final String TAG = "BocMBCManager";
	// TODO

//	private static final String Function_User_Url = "http://22.11.147.74:8080/BocMBCGate/functionUsed.action";
//	private static final String Function_User_Url = "http://22.188.131.21:9700/BocMBCGate/functionUsed.action";
//	private static final String Function_User_Url = "https://mbs.boc.cn/BocMBCGate/functionUsed.action";
	private static final String Function_User_Url = SystemConfig.Function_User_Url;
	/** 客户编号 */
	private static String CustNo = "custNo";
	/** 产品名称 */
	private static String ProductName = "productName";
	/** 版本号 */
	private static String VersionNo = "versionNo";
	/** 设备型号 */
	private static String Model = "model";
	/** 运行内存 */
	private static String RAM = "RAM";
	/** 存储空间 */
	private static String ROM = "ROM";
	/** 已使用存储空间 */
	private static String UsedROM = "usedROM";
	/** 分辨率 */
	private static String Resolution = "resolution";
	/** 系统版本 */
	private static String OsVersion = "osVersion";
	/** 终端标识 */
	private static String DeviceNo = "deviceNo";
	/** 功能集合 */
	private static String List = "list";
	private String _cifNumber;

	/**
	 * 上送功能使用情况
	 */
	public void sendFunctionUsedAction(Activity activity, String cifNumber) {
		if (cifNumber != null&&!"".equals(cifNumber)) {
			String ram = DeviceUtils.getTotalMemory(activity);
			String rom = DeviceUtils.getTotalInternalMemorySize();
			DisplayMetrics dm = activity.getResources().getDisplayMetrics();
			String resolution = dm.heightPixels + "x" + dm.widthPixels;
			String oprTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime());
			// 自定义头信息
			Map<String, Object> headerMap = new HashMap<String, Object>();
			_cifNumber = cifNumber;
			headerMap.put(CustNo, cifNumber);// 核心系统客户号
			headerMap.put(ProductName, ConstantGloble.APP_USER_AGENT_PRODUCT_NAME);
			headerMap.put(VersionNo, SystemConfig.APP_VERSION);
			headerMap.put(Model, android.os.Build.MODEL);
			headerMap.put(RAM, ram);
			headerMap.put(ROM, rom);
			headerMap.put(UsedROM, DeviceUtils.getAvailableInternalMemorySize());
			headerMap.put(Resolution, resolution);
			headerMap.put(OsVersion, android.os.Build.VERSION.RELEASE);
			headerMap.put("firstUse", isFirstUpdateFunctionUse());
			headerMap.put(DeviceNo, "2");

			// 登陆模块
			List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
			Map<String, Object> login = new HashMap<String, Object>();
			login.put("moduleNo", "P_LG_BU");
			login.put("functionNo", "P_LG_BU_CO001");
			login.put("oprTime", oprTime);
			params.add(login);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put(List, params);

			Map<String, Object> par = new HashMap<String, Object>();
			par.put("method", "GetEquipmentInfoAction");
			par.put("header", headerMap);
			par.put("params", map);

			HttpManager.requestString(Function_User_Url, ConstantGloble.GO_METHOD_POST, par, new MbcgHttpEngine(),
					this, "sendFunctionUsedActionCallback");
		}
	}

	/**
	 * 上送功能使用情况 回调
	 * 
	 * @param object
	 */
	public void sendFunctionUsedActionCallback(Object object) {
		try {
			Map<String, String> maps = null;
			String functionUsed = SharedPreUtils.getInstance().getString(ConstantGloble.SHARED_KEY_functionUsed, null);
			if (functionUsed != null) {
				maps = MyJSON.parseObject(functionUsed, Map.class);
			} else {
				maps = new HashMap<String, String>();
			}
			maps.put(encode(getCustNo()), createFunctionUseValue()); // 更新
			SharedPreUtils.getInstance().addOrModify(ConstantGloble.SHARED_KEY_functionUsed, MyJSON.toJSONString(maps));
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
			SharedPreUtils.getInstance().deleteItem(ConstantGloble.SHARED_KEY_functionUsed);
		}
	}

	/**
	 * 有版本号和用户确定是否为第一次,用户名
	 * 
	 * @return 1：是 0：否
	 */
	private String isFirstUpdateFunctionUse() {
		String reulst = "1";
		try {
			Map<String, String> maps = null;
			String functionUsed = SharedPreUtils.getInstance().getString(ConstantGloble.SHARED_KEY_functionUsed, null);
			if (functionUsed != null) {
				maps = MyJSON.parseObject(functionUsed, Map.class);
				String value = maps.get(encode(getCustNo()));
				String functionValue = createFunctionUseValue();
				if (functionValue.equals(value)) {
					reulst = "0";
				}
			}
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
			reulst = "0";
		}
		return reulst;
	}

	private String getCustNo() {
		return _cifNumber;
	}

	private String createFunctionUseValue() {
		return encode(getCustNo() + "|" + SystemConfig.APP_VERSION);
	}

	private String encode(String str) {
		String inputData = str;
		try {
			inputData = PBECoder.encrypt(str, DESCoder.DEFAULT_KEY, "salt");
			inputData = inputData.replaceAll("\\n", "");
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return inputData;
	}

	private String decode(String str) {
		String resultData = str;
		try {
			resultData = PBECoder.decrypt(str, DESCoder.DEFAULT_KEY, "salt");
		} catch (Exception e) {
			LogGloble.e(TAG, e.getMessage(), e);
		}
		return resultData;
	}

//
//	/** 记录数据 */
//	private static Map<BaseActivity, ModuleCount> map = new HashMap<BaseActivity, ModuleCount>();
//	/** 待发送数据 */
//	private static Map<String, ModuleCount> sendMap = new HashMap<String, ModuleCount>();

	/**
	 * @Description: 模块类标记
	 * @author: luql
	 * @date: 2015年3月26日 下午5:27:48
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface BocModule {
		String value();

		/* parent view id */
		int parentId() default 0;
	}

//	/**
//	 * @Description: 记录模块类
//	 * @author: luql
//	 * @date: 2015年3月26日 下午5:26:09
//	 */
//	private static class ModuleCount {
//		@JSONField(serialize = false)
//		public String moduleClass;
//		/** 模块编号 String M 如果有多级模块，从上到下以管道符“|”分隔 */
//		public String moduleNo;
//		/** 功能编号 */
//		public String functionNo;
//		/** 操作标识 */
//		public String flag;
//		/** 使用时长 */
//		public long useTime;
//		/** 操作时间 */
//		public String oprTime;
//	}

//	/**
//	 * 
//	 * @Description: 记录activity时间
//	 * @param baseActivity
//	 */
//	private static void setActivityResume(BaseActivity activity) {
//		if (isInitModule()) {
//			// 初始化module
//			findModule();
//		}
//		if (!map.containsKey(activity)) {
//			String clazzName = activity.getClass().getName();
//			if (sendMap.containsKey(clazzName)) {
//				ModuleCount count = new ModuleCount();
//				count.moduleClass = clazzName;
//				count.useTime = SystemClock.currentThreadTimeMillis();
//				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//				count.oprTime = format.format(new Date().getTime());
//				map.put(activity, count);
//			}
//		}
//
//	}
//
//	/**
//	 * 
//	 * @Description: 记录activity时间
//	 * @param baseActivity
//	 */
//	private static void setActivityPause(BaseActivity activity) {
//		ModuleCount count = (ModuleCount) map.remove(activity);
//		if (count != null && sendMap.containsKey(count.moduleClass)) {
//			long useTime = SystemClock.currentThreadTimeMillis() - count.useTime;
//			ModuleCount module = (ModuleCount) sendMap.get(count.moduleClass);
//			module.useTime += useTime;
//		}
//	}
//
//	/**
//	 * 
//	 * @Description: 发送集合是否已经初始化
//	 * @return
//	 * @author: luql
//	 */
//	private static boolean isInitModule() {
//		return sendMap.isEmpty();
//	}
//
//	// 初始化发送集合
//	private static void findModule() {
//		// 手动添加
//		ImageAndText icon1 = new ImageAndText(R.drawable.prms_icon_price, "登陆");
//		icon1.setClassName(LoginActivity.class.getName());
//		createModule("登陆", Arrays.asList(new ImageAndText[] { icon1 }));
//		// 获取LocalData模块
//		Class<?> claszz = LocalData.class;
//		LocalData localData = new LocalData();
//		Field[] declaredFields = claszz.getDeclaredFields();
//		for (Field field : declaredFields) {
//			BocModule bocModule = field.getAnnotation(BocModule.class);
//			if (bocModule != null) {
//				String value = bocModule.value();
//				try {
//					ArrayList<ImageAndText> list = (ArrayList<ImageAndText>) field.get(localData);
//					createModule(value, list);
//				} catch (IllegalArgumentException e) {
//					LogGloble.i(TAG, e.getMessage(), e);
//				} catch (IllegalAccessException e) {
//					LogGloble.i(TAG, e.getMessage(), e);
//				}
//			}
//		}
//	}
//
//	// 初始化发送集合
//	private static void createModule(String name, List<ImageAndText> list) {
//		for (ImageAndText it : list) {
//			String className = it.getClassName();
//			String text = it.getText();
//			ModuleCount module = new ModuleCount();
//			module.moduleClass = className;
//			module.moduleNo = name + "|" + text;
//			module.functionNo = String.valueOf(sendMap.size());
//			// -1：其他，0：登陆1：查询，2：更新，3：支付
//			if (text.contains("查询")) {
//				module.flag = String.valueOf(1);
//			} else if (text.contains("登陆")) {
//				module.flag = String.valueOf(0);
//			} else if (text.contains("账") || text.contains("金")) {
//				// 账户 或资金
//				module.flag = String.valueOf(3);
//			} else {
//				// 其他
//				module.flag = String.valueOf(-1);
//			}
//			sendMap.put(className, module);
//		}
//	}

}
