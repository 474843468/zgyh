package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util;

import java.text.DecimalFormat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.utils.StringUtil;

public class Trans2ChineseNumber {

	private static final DecimalFormat format = new DecimalFormat("#.00");

	/**
	 * 关联份额输入控件与份额大写显示控件
	 * 
	 * @param et
	 *            份额输入控件
	 * @param tv
	 *            份额大写显示控件
	 */
	public static void relateNumInputAndChineseShower(EditText et,
			final TextView tv) {
		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)) {
					tv.setText("");
					tv.setVisibility(View.GONE);
				} else {
					tv.setText(transform2ChineseNumber(s.toString(), "份"));
					tv.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	/**
	 * 将数字转换为中文大写
	 * 
	 * @param text
	 * @param unit
	 *            单位
	 * @return
	 */
	public static String transform2ChineseNumber(String text, String unit) {

		String integers = "";
		String decimals = "";
		if(text.startsWith(".")){
			decimals = transformDecimals(text.substring(1));
		}
		if (text.contains(".")) {
			if(text.startsWith(".")){
				text = "0"+text;
//				text = format.format(new BigDecimal(text));
				text = StringUtil.append2Decimals(text, 2);
				// 转化小数位
				if(Double.parseDouble(text) == 0){
					return "零" + unit;
				}
				String[] split = text.split("\\.");
				integers = transformIntegers(split[0]);
				if (!"00".equals(split[1])) {
					decimals = transformDecimals(split[1]);
				}
				String result;
				// 转化小数位
				result = "零点" + decimals;
				return result + unit;
			}
				text = StringUtil.append2Decimals(text, 2);
				// 转化小数位
				String[] split = text.split("\\.");
				integers = transformIntegers(split[0]);
				if (!"00".equals(split[1])) {
					decimals = transformDecimals(split[1]);
				}
			

		} else {
			integers = transformIntegers(text);
		}
		String result;
		// 转化整数位
		if (TextUtils.isEmpty(decimals)) {
			result = integers;
		} else {
			result = integers + '点' + decimals;
		}
		return result + unit;

	}

	/**
	 * 将整数转化为中文大写
	 * 
	 * @param integers
	 * @return
	 */
	public static String transformIntegers(String integers) {
		if (integers == null) {
			return "";
		}
		// 如果传入的是空串则继续返回空串
		if("".equals(integers)) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		integers = String.valueOf(Long.valueOf(integers));
		if(integers.length() == 1 && integers.equals("0")){
			return "零";
			
		}
		char[] charArray = integers.toCharArray();
		for (int i = 0, lenght = charArray.length; i < lenght; i++) {
			char ch = charArray[i];
//			if (i == 0 && ch == '0') {// 第一个字符为0不转化
//				return "零";
//			}
			if (i == charArray.length - 1 && ch == '0') {// 最后一个字符为0不转化
				break;
			}
			String unit = getUnit(lenght - i);
			int nextIndext = i + 1;
			boolean hasNext = nextIndext < lenght;
			if (!hasNext) {// 最后一个字符只转化为中文不添加单位
				builder.append(transform2ChineseNumbr(ch));
				continue;
			}
			char nextChar = charArray[nextIndext];
			if (ch == '0') {
				if (nextChar != '0') {// 当前位置为0下一个位置不为0则只转化当前位置并不带单位，下一个位置也为0则不做任何操作
					builder.append(transform2ChineseNumbr(ch));
				}
			} else {
				if (nextChar == '0') {
					builder.append(transform2ChineseNumbr(ch));
					String nextUnit = getNextUnit(integers, nextIndext);
					System.out.println(nextUnit + " " + unit);
					if (nextUnit.length() > 0
							&& unit.length() > 0
							&& unit.charAt(unit.length() - 1) == nextUnit
									.charAt(nextUnit.length() - 1)) {
						// 如果下一个需要显示的单位与本位置单位最后一个字符相同，则本次不添加单位的最后一个字符
						builder.append(unit.length() > 1 ? unit.substring(0,
								unit.length() - 1) : unit);
					} else {// 添加完整的单位
						builder.append(unit);
					}
				} else {// 当前位置不为0，下一位置不为0,则需要将大于两个字符的单位截掉最后一个字符
					builder.append(transform2ChineseNumbr(ch));
					builder.append(unit.length() > 1 ? unit.substring(0,
							unit.length() - 1) : unit);
				}
			}

		}

		return builder.toString();
	}

	/**
	 * 获取下一个可显示的单位
	 * 
	 * @param text
	 * @param start
	 * @return
	 */
	private static String getNextUnit(String text, int start) {
		if (start > text.length()) {
			return "";
		}
		char[] charArray = text.toCharArray();
		for (int i = start, lenght = charArray.length; i < lenght; i++) {
			char ch = charArray[i];
			if (ch != '0') {
				return getUnit(lenght - i);
			}
		}
		return "";
	}

	/**
	 * 将小数转化为中文大写
	 * 
	 * @param decimals
	 * @return
	 */
	public static String transformDecimals(String decimals) {
		if (decimals == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		char[] charArray = decimals.toCharArray();
		for (char ch : charArray) {
			builder.append(transform2ChineseNumbr(ch));
		}
		return builder.toString();
	}

	public static String getUnit(int index) {
		String result = "";
		switch (index) {
		case 2:
			result = "拾";
			break;
		case 3:
			result = "佰";
			break;
		case 4:
			result = "仟";
			break;
		case 5:
			result = "万";
			break;
		case 6:
			result = "拾万";
			break;
		case 7:
			result = "佰万";
			break;
		case 8:
			result = "仟万";
			break;
		case 9:
			result = "亿";
			break;
		case 10:
			result = "拾亿";
			break;
		case 11:
			result = "佰亿";
			break;
		case 12:
			result = "仟亿";
			break;
		case 13:
			result = "万亿";
			break;
		case 14:
			result = "拾万亿";
			break;
		case 15:
			result = "佰万亿";
			break;
		case 16:
			result = "仟万亿";
			break;
		case 17:
			result = "兆";
			break;
		case 18:
			result = "拾兆";
			break;
		case 19:
			result = "佰兆";
			break;
		case 20:
			result = "仟兆";
			break;
		case 21:
			result = "万兆";
			break;
		case 22:
			result = "拾万兆";
			break;
		case 23:
			result = "佰万兆";
			break;
		case 24:
			result = "仟万兆";
			break;
		case 25:
			result = "亿兆";
			break;
		case 26:
			result = "拾亿兆";
			break;
		case 27:
			result = "佰亿兆";
			break;
		case 28:
			result = "仟亿兆";
			break;
		case 29:
			result = "万亿兆";
			break;
		default:
			break;
		}
		return result;
	}

	public static char transform2ChineseNumbr(char ch) {

		char result = ch;
		switch (ch) {
		case '0':
			result = '零';
			break;
		case '1':
			result = '壹';
			break;
		case '2':
			result = '贰';
			break;
		case '3':
			result = '叁';
			break;
		case '4':
			result = '肆';
			break;
		case '5':
			result = '伍';
			break;
		case '6':
			result = '陆';
			break;
		case '7':
			result = '柒';
			break;
		case '8':
			result = '捌';
			break;
		case '9':
			result = '玖';
			break;

		default:
			break;
		}
		return result;
	}
}
