package com.chinamworld.bocmbci.biz.dept.zntzck;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

/**签约--选择账户页面*/
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.adapter.DeptZntzckSignSelectAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 智能通知存款---选择签约账户页面 */
public class DeptZntzckSignSelectActivity extends DeptBaseActivity {
	private static final String TAG = "DeptYdzcQueryActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private View queryView = null;
	private ListView listView = null;
	private Button nextButton = null;
	private List<Map<String, String>> resultList = null;
	private DeptZntzckSignSelectAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.dept_zntzck_sign));
		ibRight.setVisibility(View.VISIBLE);
		ibBack.setVisibility(View.VISIBLE);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent(DeptZntzckSignSelectActivity.this, DeptZntzckThreeMenuActivity.class);
				startActivity(intent);
			}
		});
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		queryView = LayoutInflater.from(this).inflate(R.layout.dept_zntzck_sign_select, null);
		tabcontent.addView(queryView);
		listView = (ListView) findViewById(R.id.product_list);
		nextButton = (Button) findViewById(R.id.forex_nextButton);
		resultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.DEPT_RESULTLIST_LIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			return;
		}
		DeptZntzckSignSelectAdapter.selectedPosition = -1;
		adapter = new DeptZntzckSignSelectAdapter(DeptZntzckSignSelectActivity.this, resultList);
		listView.setAdapter(adapter);
		initOnClick();
	}

	private void initOnClick() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				DeptZntzckSignSelectAdapter.selectedPosition = position;
				// 更新数据显示
				adapter.notifyDataSetChanged();
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DeptZntzckSignSelectAdapter.selectedPosition < 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getResources().getString(R.string.dept_zntzck_sign_your_select));
					return;
				}
				Map<String, String> map = resultList.get(DeptZntzckSignSelectAdapter.selectedPosition);
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.DEPT_ACCINFO, map);
				Intent intnet = new Intent(DeptZntzckSignSelectActivity.this, DeptZntzckSignReadActivity.class);
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.DEPT_GOTO_TAG, "2");//查询页面
				startActivity(intnet);
			}
		});

	}
}
