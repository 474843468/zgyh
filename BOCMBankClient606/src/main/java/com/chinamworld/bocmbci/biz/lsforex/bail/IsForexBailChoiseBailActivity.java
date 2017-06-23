package com.chinamworld.bocmbci.biz.lsforex.bail;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.adapter.IsForexBailChoiseBailAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;

/** 首页主题页面*/
public class IsForexBailChoiseBailActivity extends BaseActivity {
	private static final String TAG = "IsForexBailChoiseBailActivity";
	private View rateInfoView = null;
	private ListView listView = null;
	private LinearLayout tabcontent;
	private IsForexBailChoiseBailAdapter adapter = null;
	private List<Map<String, Object>> list = null;
	private Button backButton = null;

	private int selectPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setContentView(R.layout.biz_activity_layout);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		View footView = findViewById(R.id.rl_menu);
		footView.setVisibility(View.GONE);
		Button leftButton = (Button) findViewById(R.id.btn_show);
		leftButton.setVisibility(View.GONE);
		// 初始化弹窗按钮
		initPulldownBtn();
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 20);
		}
		setTitle(getResources().getString(R.string.isForex_bail_choise_title));
		rateInfoView = LayoutInflater.from(this).inflate(R.layout.isforex_bail_choise, null);
		backButton = (Button) findViewById(R.id.ib_back);
		tabcontent.addView(rateInfoView);
		listView = (ListView) findViewById(R.id.product_list);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		list = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_RESULT_KEY);
		if (list == null || list.size() <= 0) {
			return;
		}
		adapter = new IsForexBailChoiseBailAdapter(this, list);
		listView.setAdapter(adapter);
		adapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				selectPosition = position;
				BaseDroidApp.getInstanse().getBizDataMap().put(IsForex.ISFOREX_CASEREMIT_RES, selectPosition);
				setResult(RESULT_OK);
				finish();
			}
		});
		initOnClick();
	}

	private void initOnClick() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				IsForexBailChoiseBailAdapter.selectedPosition = position;
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.no_animation, R.anim.slide_down_out);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
}
