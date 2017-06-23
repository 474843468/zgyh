package com.chinamworld.bocmbci.biz.dept.largecd.sign;

import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 大额存单 未签约 不能自动关联页面
 * 			该账户尚未关联至电子银行，请到柜台办理网银账户关联。
 * @author luqp 2016年1月8日17:16:38
 */
public class LargeSignCannotRelationActivity extends DeptBaseActivity {
	private Context context = this;
	/** 加载布局 */
	private LayoutInflater inflater = null;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	
	/** 未签约不能自动关联页面 */
	private LinearLayout ll_prompt = null;
	/** 资金账户*/
	private TextView number = null;
	private Map<String, Object> accnumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.large_cd_sign_title));

		inflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载布局
		view = inflater.inflate(R.layout.large_sign_not_accord, null);
		tabcontent.addView(view);
		// setLeftSelectedPosition(LARGE_CD_MENU); // 设置侧边栏
		accnumber  = DeptDataCenter.getInstance().getSignedAcc();
		
		ll_prompt = (LinearLayout) view.findViewById(R.id.ll_prompt);
		ll_prompt.setVisibility(View.VISIBLE);
		
		number = (TextView) view.findViewById(R.id.tv_acc_number);
		String accNumber = (String) accnumber.get(Dept.ACCOUNT_NUMBER);
		number.setText(StringUtil.getForSixForString(accNumber));
	}
}
