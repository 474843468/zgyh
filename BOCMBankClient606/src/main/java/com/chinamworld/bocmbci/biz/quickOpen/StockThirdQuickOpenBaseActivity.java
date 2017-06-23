package com.chinamworld.bocmbci.biz.quickOpen;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.biz.quickOpen.quickopen.StockThirdQuickOpenMustKnowActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 中银证券便捷开户模块基类
 * 
 * @author Zhi
 */
public class StockThirdQuickOpenBaseActivity extends BaseActivity {

	/** 左侧菜单——中银证券便捷开户 */
	private static final int QUICKOPEN = 0;
	/** 左侧菜单——开户进度查询 */
	private static final int OPENUQUERY = 1;
	/** 主页面 */
	private LinearLayout mBodyLayout;
	/** 右按钮 */
	private Button mRightButton;
	/** 左按钮 */
	private Button mLeftButton;
	/** 弹出二级菜单按钮*/
	public Button btn_show;
	
	/** intent传值的key */
	public static final String POSITION = "POSITION";
	public final static String WEB_URL = "WEBURL";
	public final static String WEB_POSTDATA = "POSTDATA";
	public final static String WEB_URL_NAME = "WEBPAGETITLE";
	public final static String ISQUERY = "ISQUERY";
	public final static String ISSUCCESS = "ISSUCCESS";
	public final static String RANDOM = "RANDOM";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);

		initLeftSideList(this, LocalData.quickOpenLeftListData);
		initPulldownBtn();
		initFootMenu();
		initBaseResours();
	}
	
	/** 初始化基类资源 */
	private void initBaseResours() {
		btn_show = (Button) findViewById(R.id.btn_show);
		mBodyLayout = (LinearLayout) findViewById(R.id.sliding_body);
		mRightButton = (Button) findViewById(R.id.ib_top_right_btn);
		mLeftButton = (Button) findViewById(R.id.ib_back);
		mRightButton.setOnClickListener(toMainOnClickListener);
		mLeftButton.setOnClickListener(backOnClickListener);
	}
	
	/** 添加主视图*/
	protected void addView(View v) {
		mBodyLayout.addView(v);
	}

	/**
	 * @param resource
	 *            引入布局id
	 */
	protected View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		mBodyLayout.removeAllViews();
		addView(view);
		return view;
	}
	
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		QuickOpenDataCenter.getInstance().clearAllData();
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent intent = new Intent();
		String menuId = menuItem.MenuID;
		if(menuId.equals("")){
			intent.setClass(this, StockThirdQuickOpenMustKnowActivity.class);
		}
		else if(menuId.equals("")){
			intent.setClass(this, StockThirdQuickOpenMustKnowActivity.class);
		}
		context.startActivity(intent);
		return true;
		
//		QuickOpenDataCenter.getInstance().clearAllData();
//		ActivityTaskManager.getInstance().removeAllActivity();
//		Intent intent = new Intent();
//		switch (clickIndex) {
//		case QUICKOPEN:
//			intent.setClass(this, StockThirdQuickOpenMustKnowActivity.class);
//			break;
//
//		case OPENUQUERY:
//			intent.setClass(this, StockThirdQuickOpenActivity.class)
//			.putExtra(ISQUERY, true);
//			break;
//		}
//		startActivity(intent);
	}
	
	/** 设置左上角按钮监听事件 */
	public void setTopLeftListener(OnClickListener listener) {
		mLeftButton.setOnClickListener(listener);
	}
	/** 设置左上角按钮文字 */
	public void setTopLeftText(int resId){
		mLeftButton.setText(resId);
	}
	/** 设置左上角按钮文字 */
	public void setTopLeftText(String str){
		mLeftButton.setText(str);
	}
	
	/** 设置右上角按钮监听事件 */
	public void setTopRightListener(OnClickListener listener) {
		mRightButton.setOnClickListener(listener);
	}
	/** 设置右上角按钮文字 */
	public void setTopRightText(int resId){
		mRightButton.setText(resId);
	}
	/** 设置右上角按钮文字 */
	public void setTopRightText(String str){
		mRightButton.setText(str);
	}
	/** 设置右上角按钮隐藏 */
	public void setTopRightGone(){
		mRightButton.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * 为请求网络提供统一的请求方法
	 * 
	 * @param requestMethod
	 *            要请求的接口
	 * @param responseMethod
	 *            请求成功后的回调方法
	 * @param params
	 *            参数列表，子类准备此参数
	 * @param needConversationId
	 *            是否需要ConversationId
	 */
	public void requestHttp(String requestMethod, String responseMethod, Map<String, Object> params, boolean needConversationId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(requestMethod);
		biiRequestBody.setParams(params);
		// 如果需要ConversationId
		if (needConversationId)
			biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, responseMethod);
	}

	/** 返回事件 */
	public OnClickListener backOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	/** 返回主菜单事件 */
	OnClickListener toMainOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			SafetyDataCenter.getInstance().clearAllData();
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	};

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
