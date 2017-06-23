package com.boc.bocsoft.remoteopenacc.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.boc.bocsoft.remoteopenacc.R;

public class BocroaUtils {

	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		return false;
	}

	public static int dip2px(Context context, float dipValue) {
		return (int) (dipValue
				* context.getResources().getDisplayMetrics().density + 0.5f);
	}

	/**
	 * 从输入流读取数据
	 * 
	 * @param ins
	 * @return
	 */
	public static String readStringFromInputStream(InputStream ins) {

		if (ins == null) {
			return "";
		}

		StringBuilder sBuilder = new StringBuilder();
		BufferedReader mReader = null;

		try {
			mReader = new BufferedReader(new InputStreamReader(ins));

			for (;;) {
				String line = mReader.readLine();
				if (line == null) {
					break;
				}
				sBuilder.append(line);
			}

		} catch (Exception e) {

		} finally {
			if (mReader != null) {
				try {
					mReader.close();
					mReader = null;
				} catch (IOException e1) {
					mReader = null;
				}
			} else {
				try {
					ins.close();
				} catch (IOException e1) {
					ins = null;
				}
			}
		}

		return sBuilder.toString();

	}

	public static StringBuilder readFile(File file) {
		StringBuilder mBuilder = new StringBuilder();
		if (file == null || !file.exists() || !file.canRead()) {
			return mBuilder;
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			for (;;) {
				String readLine = br.readLine();
				if (readLine == null) {
					break;
				}
				mBuilder.append(readLine);
				mBuilder.append("\n");
			}

			br.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return mBuilder;
	}

	/**
	 * 判断字符是否是中文字符
	 * 
	 * @param ch
	 * @return
	 */
	public static boolean isChinese(char ch) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 返回字符串是否包含全角字符
	 * 
	 * @param str
	 * @return true 包含 false 不包含
	 */
	public static boolean isContainFullWidthChar(String str) {
		if (TextUtils.isEmpty(str)) {
			return false;
		}
		Matcher matcher = Pattern.compile(
				"[\uFF00-\uFF19\uFF21-\uFF3A\uFF41-\uFF5A]").matcher(str);
		while (matcher.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 设置edittext的光标颜色
	 * 
	 * @param editText
	 * 
	 */
	public static void setEditTextCourse(EditText editText) {
		if (editText == null) {
			return;
		}
		String fieldName = "mCursorDrawableRes";
		// mCursorDrawableRes
		Class<?> clzz = editText.getClass();
		try {
			for (;;) {

				if (clzz == null || clzz.equals(Object.class)) {
					break;
				}

				if (!clzz.equals(EditText.class)) {
					clzz = clzz.getSuperclass();
					continue;
				}

				clzz = clzz.getSuperclass();
				Field field = clzz.getDeclaredField(fieldName);

				field.setAccessible(true);
				field.set(editText, R.drawable.bocroa_cursor_bg);

				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 截取指定长度字符串，一个中文算两个字符，一个英文算一个字符。如果截取长度将一个中文截取一半，则将这个中文全部截取。例如“我在boc工作”
	 * 截取10个字符则是截取半个“作”，次方法截取9个结果为“我在boc工”
	 * 
	 * @param str
	 *            要截取的字符串
	 * @param len
	 *            要截取的长度
	 * @return 返回""表示不需要截取，否则返回截取后的字符串
	 */
	public static String getAppointLengthString(String str, int len) {
		if (getStringLength(str) <= len) {
			return "";
		}
		byte[] arr = null;// 定义字节数组用于存放字符串转化的字节
		try {
			arr = str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
		int count = 0;// 定义计数器用于记录从截取位置开始练习的负数个数
		for (int x = len - 1; x >= 0; x--) {
			if (arr[x] < 0)// 如果字节是负数则自增
				count++;
			else
				// 如果不是跳出循环
				break;
		}
		if (count % 2 == 0)
			try {
				return new String(arr, 0, len, "GBK");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
		else
			try {
				return new String(arr, 0, len - 1, "GBK");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
	}

	/**
	 * 获取字符串长度（一个汉字占2个字符）
	 * 
	 * @param s
	 * @return
	 */
	public static int getStringLength(String s) {
		char[] chs = s.toCharArray();
		int count = 0;
		for (int i = 0; i < chs.length; i++) {
			if ((chs[i] + "").getBytes().length >= 2) {
				count += 2;
			} else {
				count++;
			}
		}
		return count;
	}

	/**
	 * 对日期格式进行转换
	 * 
	 * @param fromDateStr
	 *            要转换的日期字符串
	 * @param fromFormat
	 *            转换前格式
	 * @param toFormat
	 *            转换后格式
	 * @return
	 */
	public static String transDateFormat(String fromDateStr, String fromFormat,
			String toFormat) {
		SimpleDateFormat formDateFormat = new SimpleDateFormat(fromFormat,
				Locale.getDefault());
		Date fromDate = null;
		try {
			fromDate = formDateFormat.parse(fromDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return fromDateStr;
		}
		SimpleDateFormat toDateFormat = new SimpleDateFormat(toFormat,
				Locale.getDefault());
		return toDateFormat.format(fromDate);
	}
}
