package com.chinamworld.bocmbci.biz.infoserve.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;

/**
 * @ClassName: IntentSpannable
 * @Description: 链接Spannable
 * @author xtdhwl
 * @date 2013-12-15 上午11:18:22
 * 
 */
public class IntentSpannable {

	// private static final String TAG = IntentSpannable.class.getSimpleName();
	/** 原始内容 */
	private String mContent;
	/** 正则表达式 */
	private String mRegex;
	/** 匹配过滤器 */
	private IntentMatchFilter mFilter;
	/** 匹配集合 */
	private List<SpanItem> mSpanItem;

	private String mContentResult;

	//
	public IntentSpannable setContent(String content, String regex, IntentMatchFilter filter) {
		this.mSpanItem = new ArrayList<SpanItem>();
		this.mContent = content;
		this.mContentResult = new String(content);
		this.mRegex = regex;
		this.mFilter = filter;
		return this;
	}

	//
	public Spannable process() {
		doProcess(mContent, mRegex);
		SpannableString spannable = null;
		if (mContentResult != null) {
			spannable = new SpannableString(mContentResult);
			for (SpanItem spanItem : mSpanItem) {
				spannable.setSpan(spanItem.result.style, spanItem.position.start, spanItem.position.end,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannable;
	}

	private void doProcess(String c, String regex) {
		String content = new String(c);
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(content);
		if (matcher.find()) {
			int start = matcher.start();
			// int end = matcher.end();
			String srcGroup = matcher.group();
			if (mFilter != null) {
				SpanItemResult reslut = mFilter.match(srcGroup);
				mSpanItem.add(new SpanItem(new Position(start, start + reslut.content.length()), reslut));
				mContentResult = mContentResult.replaceFirst(srcGroup, reslut.content);

				String replace = content.replaceFirst(srcGroup, getEmptyString(reslut.content.length()));
				doProcess(replace, regex);
			}
		}
	}

	public String getEmptyString(int leng) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < leng; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}

	public class SpanItem {
		SpanItemResult result;
		Position position;

		public SpanItem(Position position, SpanItemResult result) {
			this.result = result;
			this.position = position;
		}
	}

	class Position {
		int start, end;

		public Position(int x, int y) {
			this.start = x;
			this.end = y;
		}

		@Override
		public String toString() {
			return "Position [x=" + start + ", y=" + end + "]";
		}
	}

	/**
	 * @ClassName: IntentMatchFilter
	 * @Description: 匹配过滤器
	 * @author xtdhwl
	 * @date 2013-12-15 上午11:23:18
	 * 
	 */
	public interface IntentMatchFilter {
		SpanItemResult match(String srcContent);
	}

	/**
	* @ClassName: SpanItemResult
	* @Description: 
	* @author xtdhwl
	* @date 2013-12-15 上午11:23:52
	*
	 */
	public class SpanItemResult {
		String content;
		CharacterStyle style;

		public SpanItemResult(String content, CharacterStyle style) {
			this.content = content;
			this.style = style;
		}

	}
}
