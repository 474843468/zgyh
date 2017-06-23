package com.chinamworld.bocmbci.biz.tran.remit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.remit.adapters.RemitSharedDelAccAdapter;

/**
 * 删除共享账户页面
 * 
 * @author wangmengmeng
 * 
 */
public class RemitDelSharedAccountActivity extends TranBaseActivity {
	/** 删除页面 */
	private View view;
	/** 删除按钮 */
	private Button delBtn;
	private List<Map<String, String>> shareAccList = new ArrayList<Map<String, String>>();
	private ListView lv_del;
	/** 选中序号列表 */
	private List<Integer> select = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.trans_remit_setmeal_del_title));
		back.setVisibility(View.GONE);
		mTopRightBtn.setText(this.getString(R.string.switch_off));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED);
				finish();
				overridePendingTransition(R.anim.no_animation,
						R.anim.slide_down_out);
			}
		});
		// 添加布局
		view = addView(R.layout.tran_remit_del_sharedacc);
		init();
	}

	public void init() {
		shareAccList = TranDataCenter.getInstance().getShareAccountList();
		goneLeftView();
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		lv_del = (ListView) view.findViewById(R.id.lv_sharedAcc);
		final RemitSharedDelAccAdapter adapter = new RemitSharedDelAccAdapter(
				this, shareAccList);
		/** 是否选中 true代表可选,false代表不可选 */
		final List<Boolean> booleanSelect = new ArrayList<Boolean>();
		for (int i = 0; i < shareAccList.size(); i++) {
			booleanSelect.add(i, true);
		}
		lv_del.setAdapter(adapter);
		adapter.setBooleanSelect(booleanSelect);
		lv_del.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (adapter.getBooleanSelect().get(position)) {
					// 代表可选
					select.add(position);
					booleanSelect.set(position, false);
					adapter.setBooleanSelect(booleanSelect);
				} else {
					// 已经选择过
					for (int j = 0; j < select.size(); j++) {
						if (select.get(j) == position) {
							select.remove(j);
							break;
						}
					}
					booleanSelect.set(position, true);
					adapter.setBooleanSelect(booleanSelect);
				}
			}
		});
		delBtn = (Button) view.findViewById(R.id.nextBtn);
		delBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				/** 用户选择的卡信息 */
				List<Map<String, String>> chooseDelList = new ArrayList<Map<String, String>>();
				if (select == null || select.size() == 0) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									RemitDelSharedAccountActivity.this
											.getString(R.string.tran_remit_no_choose_del));
					return;
				}
				for (int i = 0; i < select.size(); i++) {
					chooseDelList.add(shareAccList.get(select.get(i)));
				}
				shareAccList.removeAll(chooseDelList);
				TranDataCenter.getInstance().setShareAccountList(shareAccList);
				// TODO 返回输入信息页面
				setResult(RESULT_OK);
				finish();
				overridePendingTransition(R.anim.no_animation,
						R.anim.slide_down_out);
			}
		});
	}

	@Override
	public void onBackPressed() {
	}
}
