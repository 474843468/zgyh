package com.boc.device.key;

/**
 * 常量类
 * 
 * @author lxw
 * 
 */
public class KeyConst {

	// 操作成功
	public static final int BOC_SUCCESS = 0x00000000;
	// 操作失败
	public static final int BOC_OPERATION_FAILED = 0x00000001;
	// 设备未连接
	public static final int BOC_NO_DEVICE = 0x00000002;
	// 设备忙
	public static final int BOC_DEVICE_BUSY = 0x00000003;
	// 参数错误
	public static final int BOC_INVALID_PARAMETER = 0x00000004;
	// 密码错误
	public static final int BOC_PASSWORD_INVALID = 0x00000005;
	// 用户取消操作
	public static final int BOC_USER_CANCEL = 0x00000006;
	// 操作超时
	public static final int BOC_OPERATION_TIMEOUT = 0x00000007;
	// 没有证书
	public static final int BOC_NO_CERT = 0x00000008;
	// 证书格式不正确
	public static final int BOC_CERT_INVALID = 0x00000009;
	// 未知错误
	public static final int BOC_UNKNOW_ERROR = 0x0000000A;
	// PIN码锁定
	public static final int BOC_PIN_LOCK = 0x0000000B;
	// 操作被打断（如来电等）
	public static final int BOC_OPERATION_ERRUPT = 0x0000000C;
	// 通讯错误
	public static final int BOC_COMM_FAILED = 0x0000000D;
	// 设备电量不足，不能进行通讯
	public static final int BOC_ENERGY_LOW = 0x0000000E;

	// UTF-8编码格式
	public static final int BOC_ENCODE_UTF8 = 0x00;
	// GBK编码格式
	public static final int BOC_ENCODE_GBK = 0x01;

	// 简体中文
	public static final int BOC_Lan_CH_Simplified = 0x00;
	// 繁体中文
	public static final int BOC_Lan_CH_traditional = 0x01;
	// 英文
	public static final int BOC_LAN_English = 0x02;

	// add beta1.1
	// 传入KeySN与设备SN不符
	public static final int BOC_KEYSN_NOTMATCH = 0x0000000F;
	// 传入证书DN与设备的证书DN不符
	public static final int BOC_KEYCERT_NOTMATCH = 0x00000010;
	// PIN码格式错误
	public static final int BOC_KEYPIN_FORMATERROR = 0x00000011;
	// add beta1.2
	// 用户不允许访问麦克风
	public static final int BOC_MICROPHONE_DENY = 0x00000012;
	// 设置的密码过于简单
	public static final int BOC_KEYPIN_SIMPLE = 0x00000013;

}
