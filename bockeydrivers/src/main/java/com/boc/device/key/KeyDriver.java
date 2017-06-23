package com.boc.device.key;

import android.content.Context;

/**
 * 音频key驱动接口
 * 
 * @author lxw
 * 
 */
public interface KeyDriver {

	/**
	 * 连接音频key
	 * 
	 * @param context
	 *            activity上下文
	 * @param SN
	 *            序列号（当SN为nil或者正确的值时，允许建立连接；当SN为错误值时，不允许建立连接；SN为nil时用于登录前建立连接）
	 * @param keyType
	 *            key设备的类型（音频，蓝牙等）
	 * 
	 * @return 成功：0 失败：错误码
	 * @throws KeyCodeException
	 **/
	public int connectWithSN(Context context, String sn, BockeyType keyType)
			throws KeyCodeException;

	/**
	 * 断开key设备连接
	 * 
	 * @param keyInfo
	 *            [out]key相关信息数据集
	 * 
	 * @return 成功：0 失败：错误码
	 * @throws KeyCodeException
	 **/
	public int disconnect(BockeyType keyType) throws KeyCodeException;

	/**
	 * 查看音频key信息
	 * 
	 * @return KeyInfo 音频key信息
	 * @throws KeyCodeException
	 **/
	public KeyInfo keyInfo() throws KeyCodeException;

	/**
	 * 修改PIN码
	 * 
	 * @param oldPIN
	 *            原PIN码（密文）,需要用‘BOCMAKeyPinSecurity’类解密
	 * @param newPIN
	 *            新PIN码（密文）,需要用‘BOCMAKeyPinSecurity’类解密
	 * @param randomId
	 * 
	 * @param sessionId
	 *            会话ID（用于生成解密PIN码的密钥的种子）
	 * @param callback
	 *            回调接口
	 * @return 0表示成功，其它值表示失败
	 * @throws KeyCodeException
	 **/
	public int modifyPIN(String oldPin, String newPin, String randomId,
			String sessionId, BOCCallback callback) throws KeyCodeException;

	/**
	 * 读取证书
	 * 
	 * @param dn
	 *            证书DN号(后台能否查询到?)，如果DN值为nil，代表查询默认证书的信息
	 * @return 证书信息
	 * @throws KeyCodeException
	 **/
	public KeyCertInfo certInfo(String dn) throws KeyCodeException;

	/**
	 * 取得中间版本号
	 * 
	 * @return 驱动版本号（版本号格式为三位数字，例如：1.0.0）
	 * @throws KeyCodeException
	 **/
	public String driverVerion() throws KeyCodeException;

	/**
	 * 签名接口
	 * 
	 * @param data
	 *            签名的原文
	 * @param PIN
	 *            PIN码（密文),需要用‘BOCMAKeyPinSecurity’类解密
	 * @param DN
	 *            签名证书的DN号
	 * @param randomId
	 *            随机数（用于生成解密PIN码的密钥的种子）
	 * @param sessionId
	 *            会话ID（用于生成解密PIN码的密钥的种子）
	 * @param signAlg
	 *            签名的加密算法（RSA,SM2等）
	 * @param hashAlg
	 *            签名的hash算法（SHA-1,SHA-256等）
	 * @param callback
	 * @return 0表示成功，其它值表示失败
	 * @throws KeyCodeException
	 **/
	public int sign(String signData, String pin, String dn, String randomId,
			String sessionId, BockeyAlgorithm signAlg, BockeyAlgorithm hashAlg,
			BOCCallback callback) throws KeyCodeException;

	/**
	 * 读取证书
	 * 
	 * @return 剩余的重试次数
	 * @throws KeyCodeException
	 **/
	public int pinModifyRemainNumbers() throws KeyCodeException;

	/**
	 * 旋转key设备屏幕显示信息
	 * 
	 * @throws KeyCodeException
	 **/
	public void reverseKeyScreen() throws KeyCodeException;

}
