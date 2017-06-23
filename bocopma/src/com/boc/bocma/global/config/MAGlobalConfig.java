package com.boc.bocma.global.config;

import com.boc.bocma.global.MAGlobalConst;

public class MAGlobalConfig {

	/**
	 * 会话ID
	 */
	public static String conversationId = "";

	/**
	 * 是否离线测试状态
	 */
	public static boolean ISDEMO = false;

	/**
	 * 接口测试模式
	 */
	public static final boolean IN_INTERFACE_TEST_MODE = false;

	/**
	 * 用户ID
	 */
	public static String userId = null;

	/**
	 * 登陆后 TokenId
	 */
	public static String TokenId = null;

	/**
	 * 初始化 用户的平台环境
	 * 
	 * @param agent
	 *            终端应用标识，基于浏览器的RIA也是一种终端应用；APAD终端对应WEB渠道：APAD
	 * @param version
	 *            终端应用版本，建议格式：“x.y”，其中x为大版本，y为小版本
	 * @param device
	 *            设备信息，用来区别终端特性，建议格式：“厂商名,产品名,型号”;如果是基于浏览器的RIA应用，则填写浏览器信息，建议格式：“
	 *            厂商名,产品名,版本号”
	 * @param platform
	 *            操作系统信息，建议格式：“厂商名,产品名,版本号”
	 * @param plugins
	 *            客户端支持的安全控件或插件，逗号分隔的列表，列表项由网银定义
	 * @param page
	 *            页面标识，用来确定当前请求所在的页面及位置，格式由终端应用提供商定义
	 * @param local
	 *            国家及语言，标准格式
	 * @param ext
	 *            扩展信息，可由终端应用提供商自由定义
	 */
	public void init(String agent, String version, String device,
			String platform, String plugins, String page, String local,
			String ext) {
		if (agent != null)
			MAGlobalConst.AGENT = agent;
		if (version != null)
			MAGlobalConst.VERSION = version;
		if (device != null)
			MAGlobalConst.DEVICE = device;
		if (platform != null)
			MAGlobalConst.PLATFORM = platform;
		if (plugins != null)
			MAGlobalConst.PLUGINS = plugins;
		if (page != null)
			MAGlobalConst.PAGE = page;
		if (local != null)
			MAGlobalConst.LOCAL = local;
		if (ext != null)
			MAGlobalConst.EXT = ext;
	}
}
