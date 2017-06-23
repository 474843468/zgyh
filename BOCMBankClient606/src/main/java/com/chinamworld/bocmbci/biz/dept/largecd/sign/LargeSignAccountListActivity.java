package com.chinamworld.bocmbci.biz.dept.largecd.sign;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
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

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.adapter.LargeSignAccountListAdapter;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 大额存单签约 选择账户列表页面
 *
 * @author luqp 2016年1月8日17:16:38
 */
public class LargeSignAccountListActivity extends DeptBaseActivity {
	private Context context = this;
	/** 加载布局 */
	private LayoutInflater inflater = null;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;

	/** 账户列表List */
	private List<Map<String, Object>> accList = null;
	/** 账户列表 ListView */
	private ListView accListView = null;
	/** 账户列表 Adapter */
	private LargeSignAccountListAdapter adapter = null;
	/** 下一步点击按钮 */
	private Button btnConfirm = null;
	/** 当前选中卡位置 */
	private int selectposition = -1;
	/** 更新后数据 */
	private List<Map<String, Object>> flushList = new ArrayList<Map<String, Object>>();
	/** 已签约已关联账户 */
	public List<Map<String ,Object>> agencyAssociatedAccountList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.large_cd_sign_title));

		inflater = LayoutInflater.from(this); //加载布局
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载布局
		view = inflater.inflate(R.layout.large_sign_select_account_list, null);
		tabcontent.addView(view);
		setLeftSelectedPosition("deptStorageCash_4");

		accList = DeptDataCenter.getInstance().getLargeSignAccountList(); // 得到账户列表
		agencyAssociatedAccountList = DeptDataCenter.getInstance().getLargeSelectAccountList(); // 已签约账户列表
		flushList = new ArrayList<Map<String, Object>>();
		List<String> accLists = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(agencyAssociatedAccountList)) {
			for (int i = 0; i < agencyAssociatedAccountList.size(); i++) {
				Map<String, Object> map = agencyAssociatedAccountList.get(i);
				String accNumber = (String) map.get(Dept.ACCOUNT_NUMBER);
				accLists.add(accNumber);
			}
		}
		for (int i = 0; i < accList.size(); i++) {  //将已签约的账户过滤
			Map<String, Object> accListMap = accList.get(i);
			String accListStr = (String) accListMap.get(Dept.ACCOUNT_NUMBER);
			if (!accLists.contains(accListStr)){
				flushList.add(accListMap);
			}
		}
		// 初始化控件
		init();
	}

	/** 初始化view和控件 */
	private void init() {
		accListView = (ListView) view.findViewById(R.id.acc_accountlist);
		btnConfirm = (Button) view.findViewById(R.id.btn_confirm);

		adapter = new LargeSignAccountListAdapter(context, flushList);
		accListView.setAdapter(adapter);
		accListView.setOnItemClickListener(listItemClickListener);
		// 确定按钮监听
		btnConfirm.setOnClickListener(confirmClickListener);
	}

	/** 账户详情监听 */
	private OnItemClickListener listItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 查询查询帐户详情PsnAccountQueryAccountDetail
			if (selectposition == position) {
				return;
			} else {
				selectposition = position;
				adapter.setSelectedPosition(position);
			}
		}
	};

	/** 确定按钮点击监听事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (selectposition == -1) {
				// 没有选择账户就点击了下一步,提示用户选择账户.
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						LargeSignAccountListActivity.this.getString(R.string.choose_account));
				return;
			}
			// 得到账户列表 将账户信息保存到集合
			DeptDataCenter.getInstance().setLargeSignSelectListMap(flushList.get(selectposition));
			Intent intent = new Intent(); //进入协议页面
			intent.setClass(context, LargeSignAgreementActivity.class);
			startActivity(intent);
		}
	};
}
