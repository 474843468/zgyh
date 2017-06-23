package com.chinamworld.bocmbci.biz.setting.hardwarebinding;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.setting.SettingBaseActivity;
import com.chinamworld.bocmbci.biz.setting.hardwarebinding.fragment.BindMachineFragment;
import com.chinamworld.bocmbci.biz.setting.hardwarebinding.fragment.UnbindMachineFragment;
import com.chinamworld.bocmbci.biz.setting.hardwarebinding.fragment.UnbindOwnMachineFragment;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.DeviceInfoTools;
import com.chinamworld.bocmbci.utils.SharedPreUtils;

/**
 * 
 * 设备绑定宿主Activity
 * 
 * 解绑流程删除
 * 
 * @author MeePwn
 * 
 */
public class HardwareBindingActivity extends SettingBaseActivity implements
		OnClickListener {

	private Button leftBtn;
	private Button rightBtn;
	private TextView titleTv;

	private FrameLayout contentLayout;

	/**
	 * 安全工具设置页面传值，用于判断绑定完成后，返回页面 （其他不可用）
	 */
	public String type;
	
	/**
	 * 判断返回键的动作，是否回到主页面
	 */
	public String leftBtnClickType = "go_main";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hardware_binding_activity);
		titleTv = (TextView) findViewById(R.id.tv_title);
//		setTitle(R.string.hardware_bind_machine);
		
//		if (!RootTools.isRootAvailable()) {
			initDatas();
			leftBtn = (Button) findViewById(R.id.ib_back);
			rightBtn = (Button) findViewById(R.id.ib_main);
			contentLayout = (FrameLayout) findViewById(R.id.hardware_content_layout);

			LayoutValue.LEWFTMENUINDEX = "settingManager_12";
			initPulldownBtn();
			initLeftSideList(this, LocalData.settingManagerlistData);
			initFootMenu();
			
			leftBtn.setOnClickListener(this);
			rightBtn.setOnClickListener(this);
			contentLayout.setOnClickListener(this);
//		} else {
//			initFootMenu();
//			BaseDroidApp.getInstanse().showMessageDialog("尊敬的客户，手机银行不支持ROOT后的Android手机和越狱后的iOS手机进行硬件绑定。",
//					new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							BaseDroidApp.getInstanse()
//									.dismissMessageDialog();
//							finish();
//						}
//					});
//		}
		
	}

	private void initDatas() {
		// 交易过程中，进行绑定或解绑
		type = getIntent().getStringExtra("is_open_activated");
		if (!"".equals(type) && !"is_setting".equals(type) && type != null) {
			Fragment fragment = null;
			if ("bind_own_machine".equals(type)) {
				// 绑定
				setTitle(R.string.hardware_bind_machine);
				fragment = new BindMachineFragment();
				showFragment(fragment);
			} else if ("unbind_own_machine".equals(type)) {
				// 解绑本机
				setTitle(R.string.hardware_unbind_machine);
				fragment = new UnbindOwnMachineFragment();
				showFragment(fragment);
			} else if ("unbind_other_machine".equals(type)) {
				// 解绑非本机
				setTitle(R.string.hardware_unbind_machine);
				fragment = new UnbindMachineFragment();
				showFragment(fragment);
			}
		} else {
			BaseHttpEngine.showProgressDialog();
			requestForLoginInfo();
		}
	}




	public void setTitle(int resId){
		String title = getResources().getString(resId);
		titleTv.setText(title);
	}
	
	public void setLeftBtnGone(){
		leftBtn.setVisibility(View.GONE);
	}
	
	public void setLeftBtnVisible(){
		leftBtn.setVisibility(View.VISIBLE);
	}
	
	public void setRightBtnVisible(){
		rightBtn.setVisibility(View.VISIBLE);
	}
	
	public void setRightBtnGone() {
		rightBtn.setVisibility(View.GONE);
	}

	public void showFragment(Fragment fragment) {
		if (fragment == null) {
			//TODO 提示用户无法绑定 
			BaseDroidApp.getInstanse().showMessageDialog(getResources().getString(R.string.hardware_unbind_can_not_unbind_info),
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse()
									.dismissMessageDialog();
							finish();
						}
					});
			return;
		}
		Bundle bundle = new Bundle();
		bundle.putString("is_open_activated", type);
		fragment.setArguments(bundle);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.hardware_content_layout, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			if ("go_main".equals(leftBtnClickType)) {
				finish();
			}else{				
				leftBtnClickType = "go_main";
				setRightBtnGone();
				getSupportFragmentManager().popBackStack();
			}
			break;
		case R.id.ib_main:
			finish();
			break;
		}
	}
	
	/**
	 * 请求登录信息
	 */
	private void requestForLoginInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.LOGIN_INFO_API);
		HttpManager.requestBii(biiRequestBody, this, "requestForLoginInfoCallBack");
	}
	
	/**
	 * 请求登录信息的回调
	 */
	public void requestForLoginInfoCallBack(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// TODO 登录返回数据
		// 当userStatus不为VERIFIED并且challengeQuestion不为空且长度大于0时，需弹出安全保护问题设置页面。
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
        // 获取用户的网银用户号
        BaseDroidApp.getInstanse().setOperatorId(resultMap.get("operatorId")+"");
        // 获取用户是否绑定设备
        BaseDroidApp.getInstanse().setHasBindingDevice(resultMap.get("hasBindingDevice")+"");
        
        switchFragment();
	}
	
	private void switchFragment(){
		Fragment fragment = null;

		// 是否绑定设备
		String hasBindingDevice = BaseDroidApp.getInstanse().getHasBindingDevice();
		// 服务器返回未设定
		String localBindInfo = SharedPreUtils.getInstance().getString(
				ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO, "");
		String cfcaString = DeviceInfoTools.getLocalCAOperatorId(this,BaseDroidApp.getInstanse().getOperatorId(),1);
		
		String localBindInfo_mac = SharedPreUtils.getInstance().getString(ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO_MAC, "");// MAC
    	String cfcamacString = DeviceInfoTools.getLocalCAOperatorId(this, BaseDroidApp.getInstanse().getOperatorId(),2);// mac
     	
		if ("1".equals(hasBindingDevice)) {
			if ("".equals(localBindInfo)) {
				// 解绑非本机设备
				setTitle(R.string.hardware_unbind_machine);
				fragment = new UnbindMachineFragment();
//			} else if (!"".equals(localBindInfo) && !cfcaString.equals(localBindInfo)) {
//				// 解绑非本机设备
////				fragment = new UnbindMachineFragment();
//				// TODO 不能解绑
//				BaseDroidApp.getInstanse().showErrorDialog("", R.string.cancle, R.string.confirm, new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						finish();
//					}
//				});
			}else if (!"".equals(localBindInfo)
				&& cfcaString.equals(localBindInfo)) {
				// 成功、失败都跳转到解绑本机页面
				setTitle(R.string.hardware_unbind_machine);
				fragment = new UnbindOwnMachineFragment();
			} else  if(cfcamacString.equals(localBindInfo_mac)) {
				setTitle(R.string.hardware_unbind_machine);
				fragment = new UnbindOwnMachineFragment();
			}else{
//				BaseDroidApp.getInstanse().showMessageDialog("尊敬的客户，系统检测到当前手机设备已被其他手机银行用户绑定。您可以手动卸载并重新安装手机银行客户端后再进行设备绑定！",
//						new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								BaseDroidApp.getInstanse()
//										.dismissMessageDialog();
//								finish();
//							}
//						});
			}
		}else if ("0".equals(hasBindingDevice)) {
			if ("".equals(localBindInfo) || cfcaString.equals(localBindInfo) ) {
				// TODO 如果没有绑定(CASE1)，则引导客户进行绑定，或者客户选择其他安全因子；
				// 删除本地信息
				SharedPreUtils.getInstance().deleteItem(ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO);
				SharedPreUtils.getInstance().deleteItem(ConstantGloble.SHARED_PREF_LOCAL_BIND_INFO_MAC);
				setTitle(R.string.hardware_bind_machine);
				// 绑定页面
				fragment = new BindMachineFragment();
				
//			} else if(!"".equals(localBindInfo)
//					&& !cfcaString.equals(localBindInfo)){
//				// 解绑非本机设备
////				fragment = new UnbindMachineFragment();
//				// TODO 不能解绑
//				BaseDroidApp.getInstanse().showErrorDialog("", R.string.cancle, R.string.confirm, new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						finish();
//					}
//				});

			} else {
//				BaseDroidApp.getInstanse().showMessageDialog("尊敬的客户，系统检测到当前手机设备已被其他手机银行用户绑定。您可以手动卸载并重新安装手机银行客户端后再进行设备绑定！",
//						new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								BaseDroidApp.getInstanse()
//										.dismissMessageDialog();
//								finish();
//							}
//						});
			}
		}

		showFragment(fragment);
	}
	
}
