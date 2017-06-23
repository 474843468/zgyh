package com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.bii.constant.Tran;

/**
 * 选择所属银行页面
 * 
 * @author wangmengmeng
 * 
 */
public class ChooseBankActivity extends BaseActivity {
	private View inflaterView;
	/** 底部layout */
	private LinearLayout footLayout;
	/** 左侧菜单 */
	private Button showBtn;
	/** 银行名称列表 */
	private ListView bankList;
	public LayoutInflater mInflater;
	/** 加载布局 */
	public LinearLayout tabcontent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		initPulldownBtn(); // 加载上边下拉菜单
		initFootMenu(); // 加载底部菜单栏
//		initLeftSideList(this, LocalData.tranManagerLeftList); // 加载左边菜单栏
		setTitle(this.getString(R.string.tran_choose_address));
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		mInflater = LayoutInflater.from(this);
		inflaterView = mInflater.inflate(R.layout.tran_choosebank, null);
		tabcontent.removeAllViews();
		tabcontent.addView(inflaterView);
		Button mTopRightBtn = (Button) findViewById(R.id.ib_top_right_btn);
		Button back = (Button) findViewById(R.id.ib_back);
		back.setVisibility(View.INVISIBLE);
		mTopRightBtn.setText(this.getString(R.string.close));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		// 隐藏底部菜单
		footLayout = (LinearLayout) this.findViewById(R.id.foot_layout);
		footLayout.setVisibility(View.GONE);
		// 隐藏左侧菜单
		showBtn = (Button) this.findViewById(R.id.btn_show);
		showBtn.setVisibility(View.GONE);
		setLeftButtonPopupGone();
		bankList = (ListView) findViewById(R.id.banklist);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.choose_bankname, bankNameList);
		bankList.setAdapter(adapter);
		bankList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra(Tran.TRAN_DIR_QUERYLIST_BANKNAME_RES,
						bankNameList.get(arg2));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	public static List<String> bankNameList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("中国工商银行");
			add("中国建设银行");
			add("中国农业银行");
			add("招商银行");
			add("国家开发银行");
			add("中国进出口银行");
			add("中国农业发展银行");
			add("交通银行");
			add("中信银行");
			add("中国光大银行");
			add("华夏银行");
			add("中国民生银行");
			add("广发银行股份有限公司");
			add("平安银行");
			add("兴业银行");
			add("上海浦东发展银行");
			add("中国邮政储蓄银行");
			// add("中国人民银行");
			add("其它银行");
		}
	};

	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	};
}
