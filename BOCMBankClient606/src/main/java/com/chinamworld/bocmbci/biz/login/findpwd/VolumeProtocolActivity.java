package com.chinamworld.bocmbci.biz.login.findpwd;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.login.LoginActivity;
import com.chinamworld.bocmbci.biz.login.LoginBaseAcitivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.StringUtil;


/**
 * @ClassName: VolumeProtocolActivity
 * @Description: 批量注册合并协议
 * @author JiangWei
 * @date 2013-7-10 上午11:53:20
 */
public class VolumeProtocolActivity extends LoginBaseAcitivity {
	private View view = null;
	
	private String merginType;
	private String loginStatus;
	private String password_RC = "";//从上个页面传过来的
	private String password = "";	//从上个页面传过来的
	private String reptype = "";
	private String loginNames = "";
	/**合并后客户证件类型（主）*/
	private String identityTypeNew = "";
	/**合并后客户证件号码（主）*/
	private String identityNumNew = "";
	/**
	 * quickTrade：关闭
	 */
	private Button quickTrade = null;

	/**
	 * agreeButton：接受按钮
	 */
	private Button agreeButton = null;
	private Button agreeButton2 = null;
	/**
	 * noAgreeButton：不接受按钮
	 */
	private Button noAgreeButton = null;
	private Button noAgreeButton2 = null;
	
	/** 底部layout */
	private LinearLayout footLayout;
	/** 左侧菜单 */
	private Button showBtn;
	private TextView tvVolumeReg;
	/** 从客户登录提示协议 */
	private TextView textAssist;
	/** 甲方用户名*/
	private TextView tvUserName;
	private String userNameStr = "";
	/** 主客户登录提示协议 */
	private TextView tvHostPrompt;
	/** 甲方乙方布局*/
	private LinearLayout lytUserName;
	/** 协议名称*/
	private TextView tvTitle;
	/** 底部布局按钮布局*/
	private LinearLayout lytBottom1;//批量协议的按钮
	private LinearLayout lytBottom2;//主客户协议的按钮
	private LinearLayout lytBottom3;//从客户的协议按钮
	private Button btnSure;
	/** 主客户协议显示控件*/
	private TextView tvHostProtocol;
	
//	private ScrollView scrAllProtocol;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.read_pro);

		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.volume_register_protocol_info, null);
		tabcontent.addView(view);

		// 隐藏底部菜单
		footLayout = (LinearLayout) this.findViewById(R.id.foot_layout);
		footLayout.setVisibility(View.GONE);
		// 隐藏左侧菜单
		showBtn = (Button) this.findViewById(R.id.btn_show);
		showBtn.setVisibility(View.GONE);

		merginType = this.getIntent().getStringExtra(Login.COMBINE_STATUS);
		userNameStr = this.getIntent().getStringExtra(Login.NAME);
		loginStatus = this.getIntent().getStringExtra(Login.LOGIN_STATUS);
		reptype = this.getIntent().getStringExtra(Login.REG_TYPE);
		init();
	}

	/**
	 * 初始化所有的控件
	 */
	private void init() {
		ibBack.setVisibility(View.GONE);
		
		quickTrade = (Button) findViewById(R.id.ib_top_right_btn);
		quickTrade.setVisibility(View.VISIBLE);
		quickTrade.setText(this.getResources().getString(R.string.close));
		
		lytUserName = (LinearLayout)findViewById(R.id.lytUserName);
		tvTitle = (TextView)findViewById(R.id.contract_title_tv);
		tvVolumeReg = (TextView) findViewById(R.id.text_host);
		tvVolumeReg.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		textAssist = (TextView) findViewById(R.id.text_assist);
		textAssist.setMovementMethod(ScrollingMovementMethod.getInstance());
		tvHostProtocol = (TextView) findViewById(R.id.dept_protocol_content);
		tvHostProtocol.setMovementMethod(ScrollingMovementMethod.getInstance());
		lytBottom1 = (LinearLayout)findViewById(R.id.bottom_btn_layout);
		lytBottom2 = (LinearLayout)findViewById(R.id.bottom_btn_layout2);
		lytBottom3 = (LinearLayout)findViewById(R.id.bottom_btn_layout3);
//		scrAllProtocol = (ScrollView)findViewById(R.id.dept_create_layout);
		btnSure = (Button)findViewById(R.id.btnNo3);
		btnSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(101);
				finish();
			}
		});
		
		if(reptype.equals("3")){
			tvTitle.setVisibility(View.VISIBLE);
			lytUserName.setVisibility(View.VISIBLE);
			textAssist.setVisibility(View.GONE);
			tvVolumeReg.setVisibility(View.VISIBLE);
			showBottomLyt(lytBottom1);
//			if(merginType.equals(ConstantGloble.COMBINSTATUS_11) || merginType.equals("0")){//主客户进来
//				textAssist.setVisibility(View.GONE);
//			}else if(merginType.equals(ConstantGloble.COMBINSTATUS_10)){
//				requsetRegisterLoginName();
//				showBottomLyt(lytBottom3);
//				tvTitle.setVisibility(View.GONE);
//				lytUserName.setVisibility(View.GONE);
//				tvVolumeReg.setVisibility(View.GONE);
//				textAssist.setVisibility(View.VISIBLE);
//			}
		}else{
			if(merginType.equals(ConstantGloble.COMBINSTATUS_11) || merginType.equals("0")){//主客户进来
				tvHostProtocol.setVisibility(View.VISIBLE);
				tvTitle.setVisibility(View.GONE);
				lytUserName.setVisibility(View.GONE);
				textAssist.setVisibility(View.GONE);
				tvVolumeReg.setVisibility(View.GONE);
				showBottomLyt(lytBottom2);
			}else if(merginType.equals(ConstantGloble.COMBINSTATUS_10)){
				loginNames = VolumeProtocolActivity.this.getIntent().getStringExtra("loginNames");
				identityTypeNew = VolumeProtocolActivity.this.getIntent().getStringExtra("identityTypeNew");
				identityNumNew = VolumeProtocolActivity.this.getIntent().getStringExtra("identityNumNew");
				identityNumNew = StringUtil.getForEightForString(identityNumNew);
				String[] newLoginNames = loginNames.split("\\|");
				StringBuffer newLoginName = new StringBuffer();
				for (int i = 0; i < newLoginNames.length; i++) {
					String nawLoginNamet = newLoginNames[i].trim();
					newLoginName.append(StringUtil.getThreeFourThreeString(nawLoginNamet)+"、");
				}
				String loginName = newLoginName.toString().substring(0, newLoginName.length()-1);
				String hostProtocolStr =VolumeProtocolActivity.this.getResources().getString(R.string.volume_register_assist);
				String hostProtocolStrte = VolumeProtocolActivity.this.getResources().getString(R.string.volume_register_assiste);
				String hostProtocolStrt = VolumeProtocolActivity.this.getResources().getString(R.string.volume_register_assistt);
				String hostProtocolStrth = VolumeProtocolActivity.this.getResources().getString(R.string.volume_register_assisttw);
				String hostProtocolStrfor = VolumeProtocolActivity.this.getResources().getString(R.string.volume_register_assisttwth);
				String hostProtocolStrfi = VolumeProtocolActivity.this.getResources().getString(R.string.volume_register_assisttwfo);
		
				String hostTypeNumNew = LocalData.IDENTITYTYPE.get(identityTypeNew)+"（"+"证件号码："+identityNumNew+"）";
				String hostTypeNumNewt = "<font color=\"#ba001d\">" + hostTypeNumNew + "</font>";
				String newHostProtocolStr = hostProtocolStrfor.replace("XXX", loginName);
				
//				String newHostProStr = hostProtocolStr.replace("lognName", loginName);
				textAssist.setText(Html.fromHtml(hostProtocolStr+"<br>"	+hostProtocolStrte
						+hostTypeNumNewt
						+hostProtocolStrt+"<br>"+hostProtocolStrth+hostTypeNumNewt
						+newHostProtocolStr+"<br>"+hostProtocolStrfi));
//				textAssist.setText(newHostProtocolStr);
				showBottomLyt(lytBottom3);
				tvTitle.setVisibility(View.GONE);
				lytUserName.setVisibility(View.GONE);
				tvVolumeReg.setVisibility(View.GONE);
				textAssist.setVisibility(View.VISIBLE);
			}
		}
		
		agreeButton = (Button) findViewById(R.id.btnNo);
		agreeButton2 = (Button) findViewById(R.id.btnNo2);
		noAgreeButton = (Button) findViewById(R.id.btnYes);
		noAgreeButton2 = (Button) findViewById(R.id.btnYes2);
		noAgreeButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				noAgreeButton.performClick();
			}
		});
		agreeButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				agreeButtonAfter();
			}
		});
		// 关闭按钮事件
		quickTrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 不接受按钮事件
		noAgreeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(101);
				finish();

			}
		});
		// 接受按钮事件
		agreeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				setResult(100);
//				finish();
				if(merginType.equals(ConstantGloble.COMBINSTATUS_11)){
//					scrAllProtocol.scrollTo(0, 0);
					tvHostProtocol.setVisibility(View.VISIBLE);
					tvTitle.setVisibility(View.GONE);
					lytUserName.setVisibility(View.GONE);
					textAssist.setVisibility(View.GONE);
					tvVolumeReg.setVisibility(View.GONE);
					showBottomLyt(lytBottom2);
				}else if(merginType.equals(ConstantGloble.COMBINSTATUS_10)){
					loginNames = VolumeProtocolActivity.this.getIntent().getStringExtra("loginNames");
					String[] newLoginNames = loginNames.split("\\|");
					StringBuffer newLoginName = new StringBuffer();
					for (int i = 0; i < newLoginNames.length; i++) {
						newLoginName.append(StringUtil.getThreeFourThreeString(newLoginNames[i])+"、");
					}
					String loginName = newLoginName.toString().substring(0, newLoginName.length()-2);
					String hostProtocolStr =VolumeProtocolActivity.this.getResources().getString(R.string.volume_register_assist);
					String newHostProStr = hostProtocolStr.replace("lognName", loginName);
					textAssist.setText(newHostProStr);

					showBottomLyt(lytBottom3);
					tvTitle.setVisibility(View.GONE);
					lytUserName.setVisibility(View.GONE);
					tvVolumeReg.setVisibility(View.GONE);
					textAssist.setVisibility(View.VISIBLE);
				}else{
					agreeButtonAfter();
				}
			}
		});
		tvUserName = (TextView)findViewById(R.id.userName);
		if(userNameStr != null && !userNameStr.equals("")){
			tvUserName.setText(userNameStr);
		}
	}
	private void showBottomLyt(View view){
		view.setVisibility(View.VISIBLE);
		switch (view.getId()) {
		case R.id.bottom_btn_layout:
			lytBottom2.setVisibility(View.GONE);
			lytBottom3.setVisibility(View.GONE);
			break;
		case R.id.bottom_btn_layout2:
			lytBottom1.setVisibility(View.GONE);
			lytBottom3.setVisibility(View.GONE);
			break;
		case R.id.bottom_btn_layout3:
			lytBottom1.setVisibility(View.GONE);
			lytBottom2.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	
	private void agreeButtonAfter(){
		if (loginStatus.equals("1") || loginStatus.equals("2")) {
			// TODO 弹出密码修改框
			// BaseDroidApp.getInstanse().showModifyPwdDialog(onclicklistener);
			Intent intent = new Intent(VolumeProtocolActivity.this, ForceModifyPwdActivity.class);
			intent.putExtra(Login.OLD_PASSWORD, password);
			intent.putExtra(Login.OLD_PASSWORD_RC, password_RC);
			startActivityForResult(intent, LoginActivity.FORCE_MODIFY_REQUEST);
//			VolumeProtocolActivity.this.finish();
			return;
		}else{
			setResult(100);
			finish();
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == LoginActivity.FORCE_MODIFY_REQUEST && resultCode == -1){
			finish();
			setResult(-1);
		}
	}
	
}
