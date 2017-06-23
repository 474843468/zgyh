package com.chinamworld.bocmbci.biz.epay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

public class EPayBaseActivity extends BaseActivity {
	
	public static String curLeftMenuIndex = "settingManager_6";

	public final static int ZY_QUERY = 213;
	public final static int RESET_DATA = 214;

	private View subView;
	private String subTitleName;
	private boolean showBackBtn;
	private int type;
	protected LinearLayout slidingBody;
	
	/** 登陆后的ConversationId */
	public String commConversationId = null;
	/** 1：银联无卡自助消费 2：银联代收 */
	public static int serviceType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		// 初始化公共页面布局
		slidingBody = initFrame(subView, this);
		// 初始化标题栏
		initTitleBar(showBackBtn, subTitleName);
		// 初始化左侧弹窗按钮
		super.initPulldownBtn();
		// 初始化页尾
		super.initFootMenu();
		// 初始化左侧弹出菜单
		initLeftSideList(this, LocalData.settingManagerlistData);
		//设置子页面 左侧菜单下标
		setLeftSelectedPosition(curLeftMenuIndex);
	}

	protected LinearLayout initFrame(View view, Activity context) {
		LinearLayout body = (LinearLayout) context.findViewById(R.id.sliding_body);
		body.removeAllViews();
		body.addView(view);
		return body;
	}

	protected void initTitleBar(boolean showBtn, String titleName) {
		// 设置标题
		this.setTitle(titleName);
		Button back = (Button) this.findViewById(R.id.ib_back);
		if (showBtn) {

			back.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		} else {
			back.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化标题右侧按钮
	 * 
	 * @param buttonName
	 * @param clickListener
	 * @return
	 */
	protected Button initTitleRightButton(String buttonName, View.OnClickListener clickListener) {
		Button bt_title_right = (Button) this.findViewById(R.id.ib_top_right_btn);
		if (clickListener == null) {
			return null;
		}

		bt_title_right.setVisibility(View.VISIBLE);
		bt_title_right.setText(buttonName);
		bt_title_right.setOnClickListener(clickListener);

		return bt_title_right;
	}

	/**
	 * 初始化标题右侧按钮_2
	 * 
	 * @param buttonName
	 * @param clickListener
	 * @return
	 */
	protected Button initTitleRightButton_B(String buttonName, View.OnClickListener clickListener) {
		Button bt_title_right_b = (Button) this.findViewById(R.id.ib_top_right_btn_b);
		if (clickListener == null) {
			return null;
		}

		bt_title_right_b.setVisibility(View.VISIBLE);
		bt_title_right_b.setText(buttonName);
		bt_title_right_b.setOnClickListener(clickListener);

		return bt_title_right_b;
	}

	/**
	 * 
	 */
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
//		super.setSelectedMenu(clickIndex);
		Class<?> clz = null;
//		setLeftSelectedPosition(clickIndex);
		ImageAndText iat = menuItem;
		try {
			clz = Class.forName(iat.getClassName());
			if(!this.getClass().getSimpleName().equals(clz.getSimpleName())) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent(this, clz);
//				if(clz == EditObligateMessageActivity.class){
//					//预留信息
//					intent.putExtra(Setting.I_WELCOMEINFO, EpayUtil.getLoginInfo(ConstantGloble.BIZ_LOGIN_DATA));
//				}
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				this.startActivity(intent);
			}
		} catch (ClassNotFoundException e) {
			LogGloble.exceptionPrint(e);
		}
		return true;
		
//		switch (clickIndex) {
//		case 0:
//			clz = EPayMainActivity.class;
//			break;
//		case 1:
//			clz = TransQueryActivity.class;
//			break;
//		default:
//			break;
//		}
//		
//		if (type != clickIndex) {
//			Intent intent = new Intent(this, clz);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			this.startActivity(intent);
//		} else
//			return;
	}

	/**
	 * 初始化步骤栏
	 * 
	 * @param context
	 * @param step
	 * @param part
	 */
	protected void initStepBar(int step, String[] stepNames) {
		StepTitleUtils stepTitleUtils = StepTitleUtils.getInstance();
		stepTitleUtils.initTitldStep(this, stepNames);
		stepTitleUtils.setTitleStep(step);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		case RESULT_CANCELED:
			break;
		}
	}

	/**
	 * 跳转到新增关联账户
	 */
	protected void goRelevanceAccount() {
//		Intent intent = new Intent();
//		intent.setClass(this, AccInputRelevanceAccountActivity.class);
//		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		
		BusinessModelControl.gotoAccRelevanceAccount(this,ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
	}

	/**
	 * 跳转到新增关联账户
	 */
	protected void hideRightButton() {
		Button bt_title_right = (Button) this.findViewById(R.id.ib_top_right_btn);
		bt_title_right.setVisibility(View.GONE);
	}

	public void setContentView(View subView) {
		this.subView = subView;
	}

	protected View getSubView() {
		return subView;
	}

	protected void setTitleName(String subTitleName) {
		this.subTitleName = subTitleName;
	}

	protected void setType(int type) {
		this.type = type;
		if(type == 0) 
			curLeftMenuIndex = "settingManager_6";//2级菜单
		else 
			curLeftMenuIndex = "settingManager_7";//支付查询
	}

	public void setShowBackBtn(boolean showBackBtn) {
		this.showBackBtn = showBackBtn;
	}

	public void hideFoot() {
		LinearLayout foot_layout = (LinearLayout) super.findViewById(R.id.foot_layout);
		foot_layout.setVisibility(View.GONE);
	}

	public View.OnClickListener rBtncloseListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
			overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
		}
	};

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}

}
