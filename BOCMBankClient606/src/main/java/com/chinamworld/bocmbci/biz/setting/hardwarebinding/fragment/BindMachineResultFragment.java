package com.chinamworld.bocmbci.biz.setting.hardwarebinding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.setting.fragment.SettingBaseFragment;
import com.chinamworld.bocmbci.biz.setting.hardwarebinding.HardwareBindingActivity;
import com.chinamworld.bocmbci.biz.setting.safetytools.SafetyToolsActivity;

/**
 * 
 * 绑定结果页面
 * 
 * @author MeePwn
 * 
 */
public class BindMachineResultFragment extends SettingBaseFragment implements
		OnClickListener {

	private RelativeLayout rootLayout;
	private Button completeBtn;

	// 安全工具设置页面传值，用于判断绑定完成后，返回页面（其他不可用）
	private String type = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.hardware_bind_result_frag,
				container, false);
		type = ((HardwareBindingActivity) getActivity()).type;
		rootLayout = (RelativeLayout) rootView
				.findViewById(R.id.hardware_frag_root_layout);
		completeBtn = (Button) rootView.findViewById(R.id.hardware_bind_complete);
	
		((HardwareBindingActivity)getActivity()).setTitle(R.string.hardware_bind_machine);
		((HardwareBindingActivity)getActivity()).setLeftBtnGone();
		((HardwareBindingActivity)getActivity()).setRightBtnVisible();
		rootLayout.setOnClickListener(this);
		completeBtn.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hardware_bind_complete:
			if ("bind_own_machine".equals(type)
					|| "unbind_own_machine".equals(type)
					|| "unbind_other_machine".equals(type)) {
				// TODO 后台激活，前台跳转到激活成功页面

				Intent intent = new Intent(getActivity(),
						SafetyToolsActivity.class);
				intent.putExtra("is_open_activated", type);
				startActivity(intent);
				getActivity().finish();
			} else if ("is_setting".equals(type)) {
				// TODO 安全工具设置页面，完成绑定
				getActivity().setResult(getActivity().RESULT_OK);
				getActivity().finish();
			} else {
				// TODO 服务设备绑定，回到首页
				getActivity().finish();
				
				// TODO 绑定完成，进入解绑流程
//				((HardwareBindingActivity)getActivity()).setLeftBtnVisible();
//				((HardwareBindingActivity)getActivity()).setRightBtnGone();
//				
//				Fragment fragment = new UnbindOwnMachineFragment();
//				Bundle bundle = new Bundle();
//
//				fragment.setArguments(bundle);
//				((HardwareBindingActivity) getActivity())
//						.showFragment(fragment);
			}
			break;
		}
	}

}
