package com.chinamworld.bocmbci.server;

import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.SharedPreUtils;

/**
 * 本地数据保存 1:手机号->登陆信息 2:comm -> 最后登陆的手机号
 * 
 * @author luql
 *
 */
public class LocalDataService {

	public static LocalDataService mInstance;

	/**
	 * 省行联行号
	 */
	private final static String DEFAULT_IBKNUM_KEY = "40142"; //北京

	private LocalDataService() {
	}

	public static LocalDataService getInstance() {
		if (mInstance == null) {
			mInstance = new LocalDataService();
		}
		return mInstance;
	}

	/**
	 * 获取省行联行号
	 * 
	 * @param pack
	 *            模块名{see ConstantGloble.Forex}
	 * @return
	 */
	public String getIbkNum(String pack) {
		return SharedPreUtils.getInstance().getString(ConstantGloble.SHARED_PREF_KEY_IbkNum, pack, DEFAULT_IBKNUM_KEY);
	}

	/**
	 * 保存省行联行号到公共数据库
	 * 
	 * @param pack
	 *            模块
	 * @param ibkNum
	 *            省行联行号
	 */
	public void saveIbkNum(String pack, String ibkNum) {
		SharedPreUtils.getInstance().addOrModify(ConstantGloble.SHARED_PREF_KEY_IbkNum, pack, ibkNum);
	}

}
