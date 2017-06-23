package com.boc.bocsoft.remoteopenacc.common;

/**
 * 常量类
 * 
 * @author gwluo
 * 
 */
public interface RemoteOpenAccConstant {
	/**
	 * TODO 是否校验，需要删除
	 */
	public boolean isCheck = true;
	/**
	 * 身份证高宽比，实际计算为1.58但是预览界面不匹配
	 */
	public final double ID_RATIO = 1.5;
	/**
	 * 存放城市文件名称
	 */
	public final String CITY = "bocroa_provincecity";
	/**
	 * 身份证、银行卡识别key，不同签名文件对应key不同
	 */
//	public final String APP_KEY = "AF0047D2B0CFB034E3DFFED8F2AFFE09";// 本地测试版，不同电脑打版本需要通过GetSignature.apk重新计算key值
	public final String APP_KEY = "baf8969c10384d08f5da004551-OBP";// 正式版
//	public final String APP_KEY = "e48e149515fa811527b4004551-OBP";// 测试版
	public final String ID_PATH = "/idcardscan/";// 请勿改动
	// public final String ID_PATH = "/bocroaIDPic/";
	public final String SIA_CHANNEL_FLAG = "0000002";// 渠道标识
	public final String SIA_ORGIDT = "104100000004";// 网点机构号
	public final String ID_DEFAULT_DATE = "99991231";// 永久身份证默认日期
	// cfca相关常量
	public final int OUT_PUT_VALUE_TYPE_MSG = 2;// 输入验证码时值的类型
	public final int VERIFY_MIN_LENGTH = 6;// 输入验证码时的最短长度
	public final int cipherType = 2;// 0国密算法sm2,1国际算法rsa
	public final String CHANNEL_FLAG = "3";// 渠道标识 String(7) Y 1 WEB 2 APP 3
											// 手机银行
	public final int KEY_BOARD_TYPE = 1;// 0完全键盘，1数字键盘
	public final String ID_DATE_LONGTIME1 = "长期";// 身份证长期
	public final String ID_DATE_LONGTIME2 = "长期有效";// 身份证长期

}
