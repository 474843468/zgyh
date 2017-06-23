package com.chinamworld.bocmbci.widget.kchart;

import com.chinamworld.bocmbci.base.activity.BaseActivity;

public class MyChart_XYActivity extends BaseActivity {
	/** Called when the activity is first created. */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		containsSwipeListView = true;
//		setContentView(R.layout.kcart);
//		XYChartView xyChart = (XYChartView) findViewById(R.id.xyChart);
////		xyChart.setUnitX("日期");
////		xyChart.setUnitY("值");
//		xyChart.setUnitX("");
//		xyChart.setUnitY("");
//		xyChart.addLine("行情", KchartUtils.Data_X, KchartUtils.Data_Y);
//		((Button)findViewById(R.id.ib_back)).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//		});
//		((TextView)findViewById(R.id.tv_title)).setText("基金净值走势图");
//	}
//
	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.OneTask;
	}
}