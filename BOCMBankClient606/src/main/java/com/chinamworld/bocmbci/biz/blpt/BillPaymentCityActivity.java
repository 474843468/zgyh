package com.chinamworld.bocmbci.biz.blpt;

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
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.biz.blpt.adapter.CityAdapter;

/**
 * 账单支付城市列表
 * 
 * @author panwe
 * 
 */
public class BillPaymentCityActivity extends BaseActivity {
	private LinearLayout mainLayout;
	private View viewContent;
	private ListView lvCity;
	private Button btnBack;
	private String shortName;
	private CityAdapter mAdapter;
	private int tag;
	private int selectposition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_province_list, null);
		setTitle(this.getString(R.string.blpt_city_title));
		initPulldownBtn();
		invisible();
		init();
	}

	private void init() {
		Intent intent = getIntent();
		shortName = intent.getStringExtra(Blpt.KEY_PROVICESHORTNAME);
		tag= intent.getIntExtra(Blpt.KEY_TAG, 0);
		mainLayout = (LinearLayout) this.findViewById(R.id.sliding_body);
		mainLayout.addView(viewContent);
		btnBack = (Button) findViewById(R.id.ib_back);
		btnBack.setOnClickListener(btnOnClick);
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.GONE);
		
		((TextView) viewContent.findViewById(R.id.tip)).setVisibility(View.VISIBLE);
		lvCity = (ListView) viewContent.findViewById(R.id.blpt_lv_province);
		lvCity.setOnItemClickListener(itemClick);
		mAdapter = new CityAdapter(this, BlptUtil.getInstance().getCityList());
		lvCity.setAdapter(mAdapter);
	}

	/**隐藏侧面、底部布局*/
	private void invisible() {
		Button btnhide = (Button) findViewById(R.id.btn_show);
		View view = findViewById(R.id.menu_popwindow);
		btnhide.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
	}
	
	/**list点击事件*/
	private OnItemClickListener itemClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (selectposition != position) {
				selectposition = position;
				mAdapter.setSelectedPosition(selectposition);
				toNext();
			}
		}
	};
	
	/**返回主页Listener*/
	private OnClickListener btnOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	private void toNext(){
		Intent it = new Intent(BillPaymentCityActivity.this, BillPaymentMainActivity.class);
		it.putExtra(Blpt.KEY_TAG, tag);
		it.putExtra(Blpt.KEY_CITY,(String)BlptUtil.getInstance().getCityList().get(selectposition).get(Blpt.CITY_NAME));
		it.putExtra(Blpt.CITY_DISNAME,(String)BlptUtil.getInstance().getCityList().get(selectposition).get(Blpt.CITY_DISNAME));
		it.putExtra(Blpt.KEY_PROVICESHORTNAME, shortName);
		startActivity(it);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.OneTask;
	}
}
