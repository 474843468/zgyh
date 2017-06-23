package com.chinamworld.bocmbci.biz.setting.hardwarebinding.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.setting.fragment.SettingBaseFragment;
import com.chinamworld.bocmbci.biz.setting.hardwarebinding.HardwareBindingActivity;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;

/**
 * 
 * 解绑结果页面
 * 
 * @author MeePwn
 * 
 */
public class UnbindMachineResultFragment extends SettingBaseFragment implements
		OnClickListener {

	private RelativeLayout rootLayout;
	private Button completeBtn;

	// 绑定设备服务码
	private String bindServiceCode = "PB107";

	private String fromWhere = "";
	private String otherMachineType = "other_machine";
	private String ownMachineType = "own_machine";

	public void setInfo(String fromWhere) {
		this.fromWhere = fromWhere;
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.hardware_unbind_result_frag,
				container, false);

		rootLayout = (RelativeLayout) rootView
				.findViewById(R.id.hardware_frag_root_layout);
		completeBtn = (Button) rootView.findViewById(R.id.hardware_unbind_complete);
		
		((HardwareBindingActivity)getActivity()).setTitle(R.string.hardware_unbind_machine);
		((HardwareBindingActivity)getActivity()).setLeftBtnGone();
		((HardwareBindingActivity)getActivity()).setRightBtnVisible();
		rootLayout.setOnClickListener(this);
		completeBtn.setOnClickListener(this);

		return rootView;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hardware_unbind_complete:
			if (ownMachineType.equals(fromWhere)) {
				// TODO 解绑完成，进入绑定流程
				Fragment fragment = new BindMachineFragment();
				Bundle bundle = new Bundle();

				fragment.setArguments(bundle);
				((HardwareBindingActivity) getActivity())
						.showFragment(fragment);
			} else {
				//本机解绑完成
				ModelBoc.onUnBindDeviceSuccess();//解绑完成后
				getActivity().finish();
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 处理获取安全因子组合获取
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.requestGetSecurityFactorCallBack(resultObj);

	}

}
