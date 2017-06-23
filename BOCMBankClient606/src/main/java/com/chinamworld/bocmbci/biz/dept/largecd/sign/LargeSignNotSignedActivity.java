package com.chinamworld.bocmbci.biz.dept.largecd.sign;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * 大额存单签约 签约提示页面
 * 
 * @author luqp 2016年1月8日17:16:38
 */
public class LargeSignNotSignedActivity extends DeptBaseActivity{
	private Context context = this;
	/** 加载布局 */
	private LayoutInflater inflater = null;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	/** 签约提示 */
	private TextView prompt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.large_cd_sign_title));
		
		inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载布局
		view = inflater.inflate(R.layout.large_sign_not_signed, null);
		tabcontent.addView(view);
		setLeftSelectedPosition("deptStorageCash_4");  // 设置侧边栏

		init();
	}

	/** 初始化view和控件 */
	private void init() {
		prompt = (TextView) view.findViewById(R.id.prompt);
		
		String head = "您尚未签约大额存单资金账户,请点击";
		String middle = "“这里”";
		String end = "进行签约。";
		SpannableString sp = new SpannableString(head + middle + end);
		TextViewStringSpan myStringSpan = new TextViewStringSpan();
		sp.setSpan(myStringSpan, head.length(), head.length() + middle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		prompt.setText(sp);
		prompt.setMovementMethod(LinkMovementMethod.getInstance());
	}

	/** 跳转Activity*/
	private class TextViewStringSpan extends ClickableSpan {

		@Override
		public void onClick(View widget) {
			// 请求账户列表
			BaseHttpEngine.showProgressDialogCanGoBack();
			requestQueryAllChinaBankAccount();
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(getResources().getColor(R.color.blue)); // 设置字体颜色
			ds.setUnderlineText(true); // 设置下划线 true显示下划线 false为不显示下划线.
		}
	}
	
	public void requestQueryAllChinaBankAccountCallBack(Object resultObj){
		super.requestQueryAllChinaBankAccountCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> accountList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		DeptDataCenter.getInstance().setLargeSignAccountList(accountList);
		
		Intent intent = new Intent();
		intent.setClass(context, LargeSignAccountListActivity.class);
		startActivity(intent);
	}
}
