package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;

import android.content.Context;
import android.text.TextUtils;

import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;

/***
 * 中行货币类
 * 
 * @author HVZHUNG
 *
 */
public final class BOCCurrency implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final String letterCode;
	public final String numberCode;
	public final String name;
	private static final Object clock = new Object();
	private static final HashMap<String, BOCCurrency> cacheKeyLetter = new HashMap<String, BOCCurrency>();
	private static final HashMap<String, BOCCurrency> cacheKeyNumber = new HashMap<String, BOCCurrency>();

	private BOCCurrency(String letterCode, String numberCode, String name) {
		this.letterCode = letterCode;
		this.numberCode = numberCode;
		this.name = name;
	}

	/**
	 * 通过字母代码获取货币实体
	 * 
	 * @param context
	 * @param letterCode
	 * @return
	 */
	public static BOCCurrency getInstanceByLetterCode(Context context,
			String letterCode) {
		synchronized (clock) {
			BOCCurrency instance = cacheKeyLetter.get(letterCode);
			if (instance == null) {
				// instance = getInstanceByAssets(context, CODE_TYPE_LETTER,
				// letterCode);
				instance = getInstanceByMap(letterCode);
				cacheInstance(instance);
			}
			return instance;
		}
	}

	/**
	 * 通过数字代码获取货币实体
	 * 
	 * @param context
	 * @param letterCode
	 * @return
	 */
	public static BOCCurrency getInstanceByNumberCode(Context context,
			String numberCode) {
		synchronized (clock) {
			BOCCurrency instance = cacheKeyNumber.get(numberCode);
			if (instance == null) {
				// instance = getInstanceByAssets(context, CODE_TYPE_NUMBER,
				// numberCode);
				instance = getInstanceByMap(numberCode);
				cacheInstance(instance);
			}
			return instance;
		}
	}

	private static BOCCurrency getInstanceByMap(String code) {
		BOCCurrency instance = new BOCCurrency(code, code,
				LocalData.Currency.get(code));

		return instance;
	}

	private static void cacheInstance(BOCCurrency instance) {
		cacheKeyLetter.put(instance.letterCode, instance);
		cacheKeyNumber.put(instance.numberCode, instance);
	}

	private static final int CODE_TYPE_LETTER = 0;
	private static final int CODE_TYPE_NUMBER = 1;

	/**
	 * 暂停使用
	 * 
	 * @param context
	 * @param codeType
	 * @param code
	 * @return
	 */
	@Deprecated
	private static BOCCurrency getInstanceByAssets(Context context,
			int codeType, String code) {
		BufferedReader reader = null;
		BOCCurrency instance = null;
		try {

			String[] list = context.getAssets().list("assets");

			for (int i = 0; i < list.length; i++) {
				LogGloble.e("TAG", "=" + i + " " + list[i]);
			}

			reader = new BufferedReader(new InputStreamReader(context
					.getAssets().open("boc_currency.data")));
			String line = "";
			String[] splits = null;
			LogGloble.e("TAG", "line1 = " + line);
			while ((line = reader.readLine()) != null) {
				splits = line.split(",");
				LogGloble.e("TAG", "line = " + line);
				if (splits != null && splits.length > 2) {
					String numberCode = splits[0];
					String letterCode = splits[1];
					String name = splits[2];
					LogGloble.e("TAG", "number = " + numberCode + " letter = "
							+ letterCode + " name = " + name);
					if (codeType == CODE_TYPE_LETTER
							&& !TextUtils.isEmpty(letterCode)
							&& letterCode.equals(code)) {
						instance = new BOCCurrency(letterCode, numberCode, name);
						break;
					} else if (codeType == CODE_TYPE_NUMBER
							&& !TextUtils.isEmpty(numberCode)
							&& numberCode.equals(code)) {
						instance = new BOCCurrency(letterCode, numberCode, name);
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		if (instance == null) {
			instance = new BOCCurrency(code, code, "货币不存在");
		}
		return instance;
	}

	@Override
	public String toString() {
		return "BOCCurrency [letterCode=" + letterCode + ", numberCode="
				+ numberCode + ", name=" + name + "]";
	}
}
