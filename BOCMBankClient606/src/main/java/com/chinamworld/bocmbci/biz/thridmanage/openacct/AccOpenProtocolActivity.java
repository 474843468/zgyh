package com.chinamworld.bocmbci.biz.thridmanage.openacct;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;

/**
 * 用户协议
 * 
 * @author panwe
 * 
 */
public class AccOpenProtocolActivity extends BaseActivity implements OnClickListener {

	/** 主布局 */
	private LinearLayout mainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//setContentView(R.layout.biz_activity_layout);
		setContentView(R.layout.biz_activity_606_layout);
		initPulldownBtn(); // 加载上边下拉菜单
		initFootMenu(); // 加载底部菜单栏
		// 关闭
		TextView tv = (TextView) findViewById(R.id.ib_top_right_btn);
		tv.setText(R.string.close);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setTitle(R.string.third_openacc_open);
		// 隐藏返回
		findViewById(R.id.ib_back).setVisibility(View.INVISIBLE);

		mainView = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载理财产品布局
		View view = inflater.inflate(R.layout.third_accopen_protocol_info, null);
		mainView.addView(view);
		// 隐藏左侧菜单
		Button showBtn = (Button) this.findViewById(R.id.btn_show);
		showBtn.setVisibility(View.GONE);

		// 隐藏底部菜单
		LinearLayout footLayout = (LinearLayout) this.findViewById(R.id.foot_layout);
		footLayout.setVisibility(View.GONE);

		Button ibBack = (Button) this.findViewById(R.id.ib_back);
		Button agreeButton = (Button) findViewById(R.id.btnNo);
		Button noAgreeButton = (Button) findViewById(R.id.btnYes);
		ibBack.setOnClickListener(this);
		agreeButton.setOnClickListener(this);
		noAgreeButton.setOnClickListener(this);

		// 甲方
		TextView tv_first_part = (TextView) mainView.findViewById(R.id.tv_first_part);
		tv_first_part.setText(EpayUtil.getLoginInfo("customerName"));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.ib_back:
			this.finish();
			break;
		// 不同意
		case R.id.btnYes:
			setResult(100);
			this.finish();
			break;
		// 同意
		case R.id.btnNo:
			setResult(RESULT_OK);
			this.finish();
			break;

		}
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
