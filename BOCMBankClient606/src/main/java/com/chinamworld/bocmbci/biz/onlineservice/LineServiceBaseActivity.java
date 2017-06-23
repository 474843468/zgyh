package com.chinamworld.bocmbci.biz.onlineservice;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;

public class LineServiceBaseActivity extends BaseActivity implements OnClickListener{
	//返回按钮
	public Button mLeftButton;
	//主界面
	public Button mRightButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		initPulldownBtn();
		initFootMenu();
		initView();
	}
	
	/**初始化布局*/
	private void initView(){
		mLeftButton = (Button) findViewById(R.id.ib_back);
		mRightButton = (Button) findViewById(R.id.ib_top_right_btn);
		mRightButton.setVisibility(View.GONE);
		mLeftButton.setOnClickListener(this);
		mRightButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ib_back) {
			BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.online_service_info), R.string.cancle, R.string.confirm, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseDroidApp.getInstanse().dismissErrorDialog();
					
					switch (v.getId()) {
					case R.id.exit_btn:// 取消
						
						break;
					case R.id.retry_btn:// 确定
						finish();
						break;

					default:
						break;
					}
				}
			});
			
			return;
		}
		else if(v.getId() == R.id.ib_top_right_btn){
			PlpsDataCenter.getInstance().clearAllData();
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.FourTask;
	}

}
