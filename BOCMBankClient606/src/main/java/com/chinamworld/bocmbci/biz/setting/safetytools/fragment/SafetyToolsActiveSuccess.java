package com.chinamworld.bocmbci.biz.setting.safetytools.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.setting.safetytools.SafetyToolsActivity;


/**
 * 
 * 安全工具激活成功页面
 * 
 * @author MeePwn
 *
 */
public class SafetyToolsActiveSuccess extends Fragment implements OnClickListener{
	
	private RelativeLayout rootLayout;
	private Button completeBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.safety_tools_active_success, container,false);
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
		completeBtn = (Button) view.findViewById(R.id.safety_tools_setting_complete);
	}
	
	private void setViews(){
		((SafetyToolsActivity)getActivity()).setTitle(R.string.safety_tools_setting);
		((SafetyToolsActivity)getActivity()).setLeftBtnGone();
		((SafetyToolsActivity)getActivity()).setRightBtnVisible();
		rootLayout.setOnClickListener(this);
		completeBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.safety_tools_setting_complete:
			getActivity().finish();
			ActivityTaskManager.getInstance().removeAllActivity();
			break;

		default:
			break;
		}
	}
	
}
