package com.boc.keydriverinterface;

import android.content.Context;

import com.boc.device.key.BOCBank_wd;
import com.boc.device.key.BOCCallback;
import com.boc.device.key.BOCMAKeyPinSecurity;
import com.boc.device.key.BockeyAlgorithm;
import com.boc.device.key.BockeyType;
import com.boc.device.key.KeyCertInfo;
import com.boc.device.key.KeyCodeException;
import com.boc.device.key.KeyConst;
import com.boc.device.key.KeyDriver;
import com.boc.device.key.KeyInfo;
import com.boc.ftKeyDriverImpl.ftKeyDriver;
import com.excelsecu.boc.ESKeyDriver;

/**
 * 音频Key接口类
 * 
 * @author cland
 * 
 */
enum BOCKeyRoleAndAlgorithm {
	BOCKeyRoleAndAlgorithmUnknown, BOCKeyEnterpriseRSA1024, BOCKeyEnterpriseRSA2048, BOCKeyEnterpriseSM2, BOCKeyIndividualRSA1024, BOCKeyIndividualRSA2048, BOCKeyIndividualSM2,
};


public class MEBKeyDriverInterface {

	// keydriver对象
	private KeyDriver mKeyDriver = null;
	private MEBBocKeyMerchantType mMerchantId = MEBBocKeyMerchantType.BOCKeyMerchantUnknown;
	private String mSn = "";

	/**
	 * 首次登陆时调用
	 * 
	 * @param context
	 */
	public MEBKeyDriverInterface(Context context) {
		initKeyDriver(context, "", MEBBocKeyMerchantType.BOCKeyMerchantUnknown);
	}

	/**
	 * 登陆后传sn 调用
	 * 
	 * @param context
	 * @param sn
	 */
	public MEBKeyDriverInterface(Context context, String sn) {
		initKeyDriver(context, sn, MEBBocKeyMerchantType.BOCKeyMerchantUnknown);
	}

	/**
	 * 登陆后传merchantId 调用
	 * 
	 * @param context
	 * @param merchantId
	 */
	public MEBKeyDriverInterface(Context context,
			MEBBocKeyMerchantType merchantId) {
		initKeyDriver(context, "", merchantId);
	}

	/**
	 * 切换驱动 登陆使用
	 * 
	 * @param sn　输入可能不完整所以不能赋值
	 *            ，只是区分厂商使用
	 * @return
	 */
	public int changeDriverBySn(String sn) {
		if (sn == null || "".equals(sn) || sn.length() < 6)
			return -1;

		int id = Integer.parseInt(sn.substring(5, 6));
		if (id == 4) {// 握奇
			mMerchantId = MEBBocKeyMerchantType.BOCKeyMerchantWatchData;
			mKeyDriver = new BOCBank_wd();
		} else if (id == 5) {// 飞天
			mMerchantId = MEBBocKeyMerchantType.BOCKeyMerchantFeiTian;
			mKeyDriver = new ftKeyDriver();
		} else if (id == 6) {// 文鼎创
			mMerchantId = MEBBocKeyMerchantType.BOCKeyMerchantWenDingChuang;
			mKeyDriver = new ESKeyDriver();
		} else {
			disconnect();
			mKeyDriver = null;
		}
		mSn = "";

		return 0;
	}

	/**
	 * 切换驱动 登陆使用
	 * 
	 * @param id
	 * @return
	 */
	public int changeDirverByMerchantId(MEBBocKeyMerchantType id) {
		mSn = "";
		disconnect();
		mKeyDriver = null;
		if (MEBBocKeyMerchantType.BOCKeyMerchantUnknown == id)
			return -1;

		if (MEBBocKeyMerchantType.BOCKeyMerchantWatchData == id) {
			mMerchantId = id;
			mKeyDriver = new BOCBank_wd();
		} else if (MEBBocKeyMerchantType.BOCKeyMerchantFeiTian == id) {
			mMerchantId = id;
			mKeyDriver = new ftKeyDriver();
		} else if (MEBBocKeyMerchantType.BOCKeyMerchantWenDingChuang == id) {
			mMerchantId = id;
			mKeyDriver = new ESKeyDriver();
		}

		return 0;
	}

	/**
	 * 初始化音频Key驱动
	 * 
	 * @param context
	 *            activity上下文
	 * 
	 * @param sn
	 *            序列号（当SN为nil或者正确的值时，允许建立连接；当SN为错误值时，不允许建立连接；SN为nil时用于登录前建立连接）
	 * @param merchantId
	 *            (可以为空) 厂商Id
	 * @return
	 */
	public int initKeyDriver(Context context, String sn,
			MEBBocKeyMerchantType merchantId) {

		int ret = 0;
		if (sn != null && !"".equals(sn) && sn.length() >= 6) {
			mSn = sn;
			int id = Integer.parseInt(sn.substring(5, 6));
			if (id == 4) {// 握奇
				mMerchantId = MEBBocKeyMerchantType.BOCKeyMerchantWatchData;
				mKeyDriver = new BOCBank_wd();
			} else if (id == 5) {// 飞天
				mMerchantId = MEBBocKeyMerchantType.BOCKeyMerchantFeiTian;
				mKeyDriver = new ftKeyDriver();
			} else if (id == 6) {// 文鼎创
				mMerchantId = MEBBocKeyMerchantType.BOCKeyMerchantWenDingChuang;
				mKeyDriver = new ESKeyDriver();
			}

		} else if (merchantId != MEBBocKeyMerchantType.BOCKeyMerchantUnknown) {// 通过厂商名字
			if (MEBBocKeyMerchantType.BOCKeyMerchantWatchData == merchantId) {
				mMerchantId = merchantId;
				mKeyDriver = new BOCBank_wd();
			} else if (MEBBocKeyMerchantType.BOCKeyMerchantFeiTian == merchantId) {
				mMerchantId = merchantId;
				mKeyDriver = new ftKeyDriver();
			} else if (MEBBocKeyMerchantType.BOCKeyMerchantWenDingChuang == merchantId) {
				mMerchantId = merchantId;
				mKeyDriver = new ESKeyDriver();
			}
		}
		return ret;
	}

	/**
	 * 连接设备
	 * 
	 * @param context
	 *            activity上下文
	 * 
	 * @param SN
	 *            序列号（当SN为nil或者正确的值时，允许建立连接；当SN为错误值时，不允许建立连接；SN为nil时用于登录前建立连接）
	 * @param merchantId
	 *            (可以为空) 厂商Id
	 * @return
	 */
	private MEBKeyDriverCommonModel connectKeyDriver(Context context,
			String sn, MEBBocKeyMerchantType merchantId) {
		MEBKeyDriverCommonModel model = new MEBKeyDriverCommonModel();
		if (mKeyDriver == null) {
		    if (sn != null && !"".equals(sn)) {
			// 判断sn号
			initKeyDriver(context, sn, MEBBocKeyMerchantType.BOCKeyMerchantUnknown);
			if (mKeyDriver == null) {
			    //  TODO
			    model.mError.setErrorId(31);
			    model.mError.setErrorMessage("您的中银e盾设备不支持音频模式，请前往中国银行网点进行更换！");
			    model.mError.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
			} else {
			    try {
				mKeyDriver.connectWithSN(context, sn, BockeyType.AudioKEY);
			    } catch (KeyCodeException exception) {
				model.mError.setErrorId(exception.getErrorCode());
				model.mError.setErrorMessage(getErrorMessage(exception.getErrorCode()));
				model.mError.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
				mKeyDriver = null;
			    }
			}
		    } else {
			model = cyclesConnectKeyDriver(context, sn, merchantId);
		    }
		} else {// mkeydriver不为空
			try {
				mKeyDriver.connectWithSN(context, sn, BockeyType.AudioKEY);
			} catch (KeyCodeException e3) {

				if (KeyConst.BOC_NO_DEVICE == e3.getErrorCode()) {
				    	// add by lxw 
				    	if (sn != null && !"".equals(sn)) {
						// keydriver至空
						model.mError.setErrorId(e3.getErrorCode());
						model.mError.setErrorMessage(getErrorMessage(e3
								.getErrorCode()));
						model.mError.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
						mKeyDriver = null;
				    	} else {
				    	    model = cyclesConnectKeyDriver(context, sn, merchantId);
				    	}
					
				} else {
					// keydriver至空
					model.mError.setErrorId(e3.getErrorCode());
					model.mError.setErrorMessage(getErrorMessage(e3
							.getErrorCode()));
					model.mError
							.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
					e3.printStackTrace();
					mKeyDriver = null;
				}

			}
		}

		if (model.mError.getErrorId() == KeyConst.BOC_SUCCESS) {
			model.mError.setErrorId(KeyConst.BOC_SUCCESS);
			model.mError.setErrorMessage(getErrorMessage(KeyConst.BOC_SUCCESS));
			model.mError.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);

			try {
				KeyInfo info = mKeyDriver.keyInfo();
				mSn = info.getKeySN();
				model.mInfo.setKeySN(sn);
				if (sn != null && !"".equals(sn) && sn.length() >= 6) {
					mSn = sn;
					int id = Integer.parseInt(sn.substring(5, 6));
					if (id == 4) {// 握奇
						mMerchantId = MEBBocKeyMerchantType.BOCKeyMerchantWatchData;
					} else if (id == 5) {// 飞天
						mMerchantId = MEBBocKeyMerchantType.BOCKeyMerchantFeiTian;
					} else if (id == 6) {// 文鼎创
						mMerchantId = MEBBocKeyMerchantType.BOCKeyMerchantWenDingChuang;
					}

				}
			} catch (KeyCodeException e) {
				e.printStackTrace();
			}
		}
		return model;
	}

	/**
	 * 循环连接音频Key
	 * 
	 * @param context
	 * @param sn
	 * @param merchantId
	 * @return
	 */
	private MEBKeyDriverCommonModel cyclesConnectKeyDriver(Context context,
			String sn, MEBBocKeyMerchantType merchantId) {
		MEBKeyDriverCommonModel model = new MEBKeyDriverCommonModel();
		KeyDriver tmpDriver = null;
		try {
			// woqi
//			tmpDriver = new ESKeyDriver();
			tmpDriver = new BOCBank_wd();
			tmpDriver.connectWithSN(context, sn, BockeyType.AudioKEY);
			mKeyDriver = tmpDriver;
		} catch (KeyCodeException e) {
			e.printStackTrace();
			int errorId = e.getErrorCode();
			if (KeyConst.BOC_NO_DEVICE == errorId) {
//				tmpDriver = new ftKeyDriver();
				tmpDriver = new ESKeyDriver();
//				tmpDriver = new BOCBank_wd();
				try {
					// 文鼎创
					tmpDriver.connectWithSN(context, sn, BockeyType.AudioKEY);
					mKeyDriver = tmpDriver;
				} catch (KeyCodeException e1) {
					e1.printStackTrace();
					int errorId1 = e1.getErrorCode();
					if (KeyConst.BOC_NO_DEVICE == errorId1) {
						tmpDriver = new ftKeyDriver();
						try {
							// 飞天
							tmpDriver.connectWithSN(context, sn,
									BockeyType.AudioKEY);
							mKeyDriver = tmpDriver;
						} catch (KeyCodeException e2) {
							e2.printStackTrace();
							int errorId2 = e2.getErrorCode();
							model.mError.setErrorId(errorId2);
							model.mError
									.setErrorMessage(getErrorMessage(errorId2));
							model.mError
									.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
						}
					} else {
						model.mError.setErrorId(errorId1);
						model.mError.setErrorMessage(getErrorMessage(errorId1));
						model.mError
								.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
					}
					
				}
			} else {
				model.mError.setErrorId(errorId);
				model.mError.setErrorMessage(getErrorMessage(errorId));
				model.mError
						.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
			}
			return model;
		}

		return model;
	}


	/**
	 * 断开key设备连接
	 * 
	 * @param keyInfo
	 *            [out]key相关信息数据集
	 * 
	 * @return 成功：0 失败：错误码
	 * @throws KeyCodeException
	 **/
	public int disconnect() {
		int ret = 0;
		try {
			if (mKeyDriver != null) {
				mKeyDriver.disconnect(BockeyType.AudioKEY);
			}
		} catch (KeyCodeException e) {
			ret = e.getErrorCode();
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 音频Key的PIN码解密
	 * 
	 * @param encrytoData
	 *            加密后的PIN码数据
	 * @param randomID
	 *            随机数（用于生成解密PIN码的密钥的种子）
	 * @param sessionID
	 *            会话ID（用于生成解密PIN码的密钥的种子）
	 * 
	 * @return PIN码明文
	 */
	public static String decryptKeyPinFromData(byte[] encrytoData,
			String randomID, String sessionID) {
		return BOCMAKeyPinSecurity.decryptKeyPinFromData(encrytoData, randomID,
				sessionID);

	}

	/**
	 * 音频Key的PIN码解密
	 * 
	 * @param encrytoData
	 *            加密后的PIN码数据
	 * @param randomID
	 *            随机数（用于生成解密PIN码的密钥的种子）
	 * @param sessionID
	 *            会话ID（用于生成解密PIN码的密钥的种子）
	 * 
	 * @return PIN码明文
	 */
	public static byte[] encryptWithKeyPin(String pin, String randomID,
			String sessionID) {
		return BOCMAKeyPinSecurity.encryptWithKeyPin(pin, randomID, sessionID);
	}

	/**
	 * 
	 * @param errorId
	 * @return
	 */
	public static String getErrorMessage(int errorId) {
		String str = "";
		switch (errorId) {
		// 操作成功
		case KeyConst.BOC_SUCCESS:
			str = "操作成功！";
			break;
		// 操作失败
		case KeyConst.BOC_OPERATION_FAILED:
			str = "操作失败，请重试！";
			break;
		// 设备未连接
		case KeyConst.BOC_NO_DEVICE:
			str = "请确认您的中银e盾设备是否连接或正确！";
			break;
		// 设备忙
		case KeyConst.BOC_DEVICE_BUSY:
			str = "操作失败，请重试！";// "设备忙！";
			break;
		// 参数错误
		case KeyConst.BOC_INVALID_PARAMETER:
			str = "操作失败，请重试！";// "参数错误！";
			break;
		// 密码错误
		case KeyConst.BOC_PASSWORD_INVALID:
			str = "您输入的中银e盾的密码错误！";// "密码错误！";
			break;
		// 用户取消操作
		case KeyConst.BOC_USER_CANCEL:
			str = "您已取消了本次操作！";// "用户取消操作！";
			break;
		// 操作超时
		case KeyConst.BOC_OPERATION_TIMEOUT:
			str = "您的本次操作已超时！";// "操作超时！";
			break;
		// 没有证书
		case KeyConst.BOC_NO_CERT:
			str = "请确认您的中银e盾设备是否正确！";// "没有证书！";
			break;
		// 证书格式不正确
		case KeyConst.BOC_CERT_INVALID:
			str = "请确认您的中银e盾设备是否正确！";// "证书格式不正确！";
			break;
		// 未知错误
		case KeyConst.BOC_UNKNOW_ERROR:
			str = "操作失败，请重试！";// "未知错误！";
			break;
		// PIN码锁定
		case KeyConst.BOC_PIN_LOCK:
			str = "您的中银e盾设备的密码被锁定！";// "PIN码锁定！";
			break;
		// 操作被打断（如来电等）
		case KeyConst.BOC_OPERATION_ERRUPT:
			str = "操作失败，请重试！";// "操作被打断（如来电等）！";
			break;
		// 通讯错误
		case KeyConst.BOC_COMM_FAILED:
			str = "操作失败，请重试！";// "通讯错误！";
			break;
		// 设备电量不足，不能进行通讯
		case KeyConst.BOC_ENERGY_LOW:
			str = "您的中银e盾设备电量不足，无法完成交易！";
			break;

		// 传入KeySN与设备SN不符
		case KeyConst.BOC_KEYSN_NOTMATCH:
			str = "请确认您的中银e盾设备是否正确！";// "传入KeySN与设备SN不符！";
			break;

		// 传入证书DN与设备的证书DN不符
		case KeyConst.BOC_KEYCERT_NOTMATCH:
			str = "请确认您的中银e盾设备是否正确！";// "传入证书DN与设备的证书DN不符！";
			break;

		// PIN码格式错误
		case KeyConst.BOC_KEYPIN_FORMATERROR:
			str = "您输入的中银e盾的密码格式错误！";
			break;

		// 用户不允许访问麦克风
		case KeyConst.BOC_MICROPHONE_DENY:
			str = "请您在系统设置功能隐私中允许中国银行（企业）访问您设备的麦克风！";
			break;
		// 设置的密码过于简单
		case KeyConst.BOC_KEYPIN_SIMPLE:
			str = "您设置的密码过于简单，请重新设置！";
			break;
		}

		return str;
	}

	// 登陆授权签名
	/**
	 * * @param context activity上下文
	 * 
	 * @param signData
	 *            签名的原文
	 * @param PIN
	 *            PIN码（密文),需要用‘BOCMAKeyPinSecurity’类解密
	 * @param randomId
	 *            随机数（用于生成解密PIN码的密钥的种子）
	 * @param sessionId
	 *            会话ID（用于生成解密PIN码的密钥的种子）
	 * @param callback
	 * 
	 * @return
	 */
	public MEBKeyDriverCommonModel loginSign(Context context, String signData,
			String pin, String randomId, String sessionId, BOCCallback callback) {
		MEBKeyDriverCommonModel model = new MEBKeyDriverCommonModel();
		int ret = 0;

		model = connectKeyDriver(context, mSn, mMerchantId);
		if (model.mError.getErrorId() != KeyConst.BOC_SUCCESS)
			return model;

		KeyInfo keyInfo;
		try {
			keyInfo = mKeyDriver.keyInfo();
		} catch (KeyCodeException e1) {
			ret = e1.getErrorCode();
			model.mError.setErrorId(ret);
			model.mError.setErrorMessage(getErrorMessage(ret));
			model.mError.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
			e1.printStackTrace();
			return model;
		}

		try {
			// String password =
			// MEBKeyDriverInterface.decryptKeyPinFromData(pin.getBytes(),
			// randomId, sessionId);
			// Log.i("解密后密码:", password);
			// 选择算法
			BOCKeyRoleAndAlgorithm algFlag = keyRoleAndAlgorithmByKeySN(keyInfo
					.getKeySN());
			if (BOCKeyRoleAndAlgorithm.BOCKeyEnterpriseRSA1024 == algFlag
					|| BOCKeyRoleAndAlgorithm.BOCKeyEnterpriseRSA2048 == algFlag
					|| BOCKeyRoleAndAlgorithm.BOCKeyIndividualRSA1024 == algFlag
					|| BOCKeyRoleAndAlgorithm.BOCKeyIndividualRSA2048 == algFlag)
				ret = mKeyDriver.sign(signData, pin, "", randomId, sessionId,
						BockeyAlgorithm.BOCRSA, BockeyAlgorithm.BOCSHAH1,
						callback);
			else if (BOCKeyRoleAndAlgorithm.BOCKeyEnterpriseSM2 == algFlag
					|| BOCKeyRoleAndAlgorithm.BOCKeyIndividualSM2 == algFlag)

				ret = mKeyDriver.sign(signData, pin, "", randomId, sessionId,
						BockeyAlgorithm.BOCSM2, BockeyAlgorithm.BOCSM3,
						callback);
		} catch (KeyCodeException e) {
			ret = e.getErrorCode();
			e.printStackTrace();
		}

		model.mError.setErrorId(ret);
		model.mError.setErrorMessage(getErrorMessage(ret));
		model.mError.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
		return model;
	}

	/**
	 * * @param context activity上下文
	 * 
	 * @param signData
	 *            签名的原文
	 * @param pin
	 *            PIN码（密文),需要用‘BOCMAKeyPinSecurity’类解密
	 * @param randomId
	 *            随机数（用于生成解密PIN码的密钥的种子）
	 * @param sessionId
	 *            会话ID（用于生成解密PIN码的密钥的种子）
	 * @param callback
	 * 
	 * @return
	 */
	public MEBKeyDriverCommonModel tradeSign(Context context, String signData,
			String pin, String randomId, String sessionId, BOCCallback callback) {
		MEBKeyDriverCommonModel model = new MEBKeyDriverCommonModel();
		int ret = 0;

		model = connectKeyDriver(context, mSn, mMerchantId);
		if (model.mError.getErrorId() != KeyConst.BOC_SUCCESS)
			return model;

		KeyInfo keyInfo;
		try {
			keyInfo = mKeyDriver.keyInfo();
		} catch (KeyCodeException e1) {
			ret = e1.getErrorCode();
			model.mError.setErrorId(ret);
			model.mError.setErrorMessage(getErrorMessage(ret));
			model.mError.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
			e1.printStackTrace();
			return model;
		}

		try {
			BOCKeyRoleAndAlgorithm algFlag = keyRoleAndAlgorithmByKeySN(keyInfo
					.getKeySN());
			if (BOCKeyRoleAndAlgorithm.BOCKeyEnterpriseRSA1024 == algFlag
					|| BOCKeyRoleAndAlgorithm.BOCKeyEnterpriseRSA2048 == algFlag
					|| BOCKeyRoleAndAlgorithm.BOCKeyIndividualRSA1024 == algFlag
					|| BOCKeyRoleAndAlgorithm.BOCKeyIndividualRSA2048 == algFlag)
				ret = mKeyDriver.sign(signData, pin, "", randomId, sessionId,
						BockeyAlgorithm.BOCRSA, BockeyAlgorithm.BOCSHA256,
						callback);
			else if (BOCKeyRoleAndAlgorithm.BOCKeyEnterpriseSM2 == algFlag
					|| BOCKeyRoleAndAlgorithm.BOCKeyIndividualSM2 == algFlag)
				ret = mKeyDriver.sign(signData, pin, "", randomId, sessionId,
						BockeyAlgorithm.BOCSM2, BockeyAlgorithm.BOCSM3,
						callback);
			// ret = mKeyDriver
			// .sign(signData, pin, "", randomId, sessionId,
			// BockeyAlgorithm.BOCSM2, BockeyAlgorithm.BOCSHA256,
			// callback);
		} catch (KeyCodeException e) {
			ret = e.getErrorCode();
			e.printStackTrace();
		}
		model.mError.setErrorId(ret);
		model.mError.setErrorMessage(getErrorMessage(ret));
		model.mError.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
		return model;
	}

	/**
	 * 修改PIN码
	 * 
	 * @param context
	 * @param sn
	 * @param oldPin
	 * @param newPin
	 * @param randomId
	 * @param sessionId
	 * @param callback
	 * @return
	 */

	public MEBKeyDriverCommonModel modifyPIN(Context context, String oldPin,
			String newPin, String randomId, String sessionId,
			BOCCallback callback) {
		MEBKeyDriverCommonModel model = new MEBKeyDriverCommonModel();
		int ret = 0;
		model = connectKeyDriver(context, mSn, mMerchantId);
		if (model.mError.getErrorId() != KeyConst.BOC_SUCCESS)
			return model;

		try {
			ret = mKeyDriver.modifyPIN(oldPin, newPin, randomId, sessionId,
					callback);
		} catch (KeyCodeException e) {
			ret = e.getErrorCode();
			e.printStackTrace();
		}
		model.mError.setErrorId(ret);
		model.mError.setErrorMessage(getErrorMessage(ret));
		model.mError.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
		return model;
	}

	/**
	 * 登陆前使用 token切换到ca使用
	 * 
	 * @param context
	 * @return
	 */
	public MEBKeyDriverCommonModel getKeyDriverInfoBeforeLogin(Context context) {
		MEBKeyDriverCommonModel model = new MEBKeyDriverCommonModel();
		int ret = 0;
		model = connectKeyDriver(context, mSn, mMerchantId);
		if (model.mError.getErrorId() != KeyConst.BOC_SUCCESS)
			return model;

		try {
			KeyInfo keyInfo = mKeyDriver.keyInfo();
			model.mInfo.setPinNeedModify(keyInfo.isPinNeedModify());
			model.mInfo.setPinStatus(keyInfo.getPinStatus());
			model.mInfo.setKeySN(keyInfo.getKeySN());
			model.mInfo.setBatteryStatus(keyInfo.getBatteryStatus());
			model.mInfo.setDriverVerion(mKeyDriver.driverVerion());
			model.mInfo.setRemainNumbers(mKeyDriver.pinModifyRemainNumbers());
		} catch (KeyCodeException e) {
			ret = e.getErrorCode();
			e.printStackTrace();
		}
		model.mError.setErrorId(ret);
		model.mError.setErrorMessage(getErrorMessage(ret));
		model.mError.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
		return model;
	}

	/**
	 * 查询key信息 包含证书
	 * 
	 * @param context
	 * @return
	 */
	public MEBKeyDriverCommonModel getKeyDriverInfo(Context context) {
		MEBKeyDriverCommonModel model = new MEBKeyDriverCommonModel();
		int ret = 0;
		model = connectKeyDriver(context, mSn, mMerchantId);
		if (model.mError.getErrorId() != KeyConst.BOC_SUCCESS)
			return model;

		try {
			KeyCertInfo keyCertInfo = mKeyDriver.certInfo("");
			model.mInfo.setExpiredDate(keyCertInfo.getExpiredDate());
			model.mInfo.setStartDate(keyCertInfo.getStartDate());
			model.mInfo.setOwner(keyCertInfo.getOwner());
			model.mInfo.setPublisher(keyCertInfo.getPublisher());

			KeyInfo keyInfo = mKeyDriver.keyInfo();
			model.mInfo.setPinNeedModify(keyInfo.isPinNeedModify());
			model.mInfo.setPinStatus(keyInfo.getPinStatus());
			model.mInfo.setKeySN(keyInfo.getKeySN());
			model.mInfo.setBatteryStatus(keyInfo.getBatteryStatus());
			model.mInfo.setDriverVerion(mKeyDriver.driverVerion());
			model.mInfo.setRemainNumbers(mKeyDriver.pinModifyRemainNumbers());
		} catch (KeyCodeException e) {
			ret = e.getErrorCode();
			e.printStackTrace();
		}
		model.mError.setErrorId(ret);
		model.mError.setErrorMessage(getErrorMessage(ret));
		model.mError.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
		return model;
	}

	/**
	 * 随机数
	 * 
	 * @return
	 */
	public static int random() {
		return new java.util.Random().nextInt();
	}

	/**
	 * 得到keysn值 必须登录后调用
	 * 
	 * @return
	 */
	public String getSn() {
		return mSn;
	}

	/**
	 * 得到厂商Id
	 * 
	 * @return
	 */
	public MEBBocKeyMerchantType getMerchantId() {
	    if (mSn == null || "".equals(mSn) || mSn.length() < 6)
		return MEBBocKeyMerchantType.BOCKeyMerchantUnknown;

        	int id = Integer.parseInt(mSn.substring(5, 6));
        	if (id == 4) {// 握奇
        		return MEBBocKeyMerchantType.BOCKeyMerchantWatchData;
        		
        	} else if (id == 5) {// 飞天
        		return MEBBocKeyMerchantType.BOCKeyMerchantFeiTian;
        	} else if (id == 6) {// 文鼎创
        		return  MEBBocKeyMerchantType.BOCKeyMerchantWenDingChuang;
        	} else {
        	    return MEBBocKeyMerchantType.BOCKeyMerchantUnknown;
        	}
        	
	}

	/**
	 * 反转
	 * 
	 * @return
	 */
	public MEBKeyDriverCommonModel reverseKeyScreen(Context context) {
		MEBKeyDriverCommonModel model = new MEBKeyDriverCommonModel();
		int ret = 0;
		model = connectKeyDriver(context, mSn, mMerchantId);
		if (model.mError.getErrorId() != KeyConst.BOC_SUCCESS)
			return model;

		try {
			mKeyDriver.reverseKeyScreen();
		} catch (KeyCodeException e) {
			ret = e.getErrorCode();
			e.printStackTrace();
		}
		model.mError.setErrorId(ret);
		model.mError.setErrorMessage(getErrorMessage(ret));
		model.mError.setType(MEBBocKeyRegisterType.BOCKeyNoramlRegister);
		return model;
	}

	/**
	 * 通过sn号得到使用算法
	 * 
	 * @param keySN
	 * @return
	 */
	private BOCKeyRoleAndAlgorithm keyRoleAndAlgorithmByKeySN(String keySN) {
		if (keySN == null || "".equals(keySN)) {
			return BOCKeyRoleAndAlgorithm.BOCKeyRoleAndAlgorithmUnknown;
		}
		String flag = keySN.substring(6, 7);
		if ("A".equals(flag) || "B".equals(flag)) {
			return BOCKeyRoleAndAlgorithm.BOCKeyIndividualRSA1024;
		} else if ("C".equals(flag) || "D".equals(flag)) {
			return BOCKeyRoleAndAlgorithm.BOCKeyIndividualRSA2048;
		} else if ("E".equals(flag) || "F".equals(flag)) {
			return BOCKeyRoleAndAlgorithm.BOCKeyIndividualSM2;
		} else if ("Z".equals(flag) || "Y".equals(flag)) {
			return BOCKeyRoleAndAlgorithm.BOCKeyEnterpriseRSA1024;
		} else if ("W".equals(flag) || "X".equals(flag)) {
			return BOCKeyRoleAndAlgorithm.BOCKeyEnterpriseRSA2048;
		} else if ("U".equals(flag) || "V".equals(flag)) {
			return BOCKeyRoleAndAlgorithm.BOCKeyEnterpriseSM2;
		} else {
			return BOCKeyRoleAndAlgorithm.BOCKeyRoleAndAlgorithmUnknown;
		}
	}
}
