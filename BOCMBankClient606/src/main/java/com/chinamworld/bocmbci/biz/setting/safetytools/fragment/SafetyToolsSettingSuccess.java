package com.chinamworld.bocmbci.biz.setting.safetytools.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.setting.safetytools.SafetyToolsActivity;


/**
 * 
 * 安全工具设置成功页面
 * 
 * @author MeePwn
 *
 */
public class SafetyToolsSettingSuccess extends Fragment implements OnClickListener{
	
	private RelativeLayout rootLayout;
	private TextView dynamicPsswdTv;
	private Button completeBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.safety_tools_setting_success, container,false);
		initDatas();
		initViews(rootView);
		setViews();
		
		return rootView;
	}
	
	private void initDatas(){
		
	}
	
	private void initViews(View view){
		((SafetyToolsActivity)getActivity()).setLeftBtnGone();
		rootLayout = (RelativeLayout) view.findViewById(R.id.safety_tools_root_layout);
		dynamicPsswdTv = (TextView) view.findViewById(R.id.safety_tools_prompt_info_details_text);
		completeBtn = (Button) view.findViewById(R.id.safety_tools_setting_complete);
	}
	
	private void setViews(){
		((SafetyToolsActivity)getActivity()).setTitle(R.string.safety_tools_setting);
		((SafetyToolsActivity)getActivity()).setLeftBtnGone();
		((SafetyToolsActivity)getActivity()).setRightBtnVisible();
		dynamicPsswdTv.setText(getArguments().getString("safety_Name"));
		rootLayout.setOnClickListener(this);
		completeBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safety_tools_setting_complete:
			/**从安全工具设置进入 选择安全工具列表页面*/
//			Fragment fragment = new SafetyToolsFragment();
//			((SafetyToolsActivity)getActivity()).showFragment(fragment);
			/**603改造 回到9宫格*/
			getActivity().finish();
			ActivityTaskManager.getInstance().removeAllActivity();
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			break;

		default:
			break;
		}
	}
	
}
