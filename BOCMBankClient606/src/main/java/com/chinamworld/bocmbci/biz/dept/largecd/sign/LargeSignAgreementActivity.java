package com.chinamworld.bocmbci.biz.dept.largecd.sign;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
/**
 * 大额存单签约  协议页面
 * @author luqp
 * 2016年1月8日17:16:38
 */
public class LargeSignAgreementActivity extends DeptBaseActivity{
	private Context context = this;
	/** 加载布局 */
	private LayoutInflater inflater = null;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	
	/** 甲方*/
	private TextView tv_jiafang = null;
	/** 选择的签约账户 */
	private Map<String, Object> selectedAccount;
	/** 甲方*/
	private String accountName = null;
	/** 接受按钮*/
	private Button btnYes = null;
	/** 不接受按钮*/
	private Button btnNo = null;
	/** 用户协议*/
	private TextView userProtocol = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.large_cd_sign_title));
		
		inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载布局
		view = inflater.inflate(R.layout.large_sign_agreement, null);
		tabcontent.addView(view);
		setLeftSelectedPosition("deptStorageCash_4");
		
		selectedAccount = DeptDataCenter.getInstance().getLargeSignSelectListMap();
		
		init();
	}

	/** 初始化view和控件*/
	private void init() {
		tv_jiafang = (TextView) view.findViewById(R.id.tv_jiafang);
		btnYes = (Button) view.findViewById(R.id.btn_description);
		btnNo = (Button) view.findViewById(R.id.btn_noaccept);
		
		accountName = (String) selectedAccount.get(Dept.LargeSign_accountName);
		tv_jiafang.setText(accountName);
		
		TextView protocol = (TextView)view.findViewById(R.id.agreeTextView);
		protocol.setText(Html.fromHtml( this.getResources().getString(R.string.large_sign_user_protocol_new)));
		// 不同意按钮 返回上个页面
		btnNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 用户确认进入确认页面
		btnYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, LargeSignAgreementConfirmActivity.class);
				startActivity(intent);
			}
		});
	}
}
