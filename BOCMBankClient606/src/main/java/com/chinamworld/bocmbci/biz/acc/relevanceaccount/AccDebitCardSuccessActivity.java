package com.chinamworld.bocmbci.biz.acc.relevanceaccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.adapter.RelevanceDebitFailAdapter;
import com.chinamworld.bocmbci.biz.acc.adapter.RelevanceDebitSuccessAdapter;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.Utils;

/**
 * 借记卡关联成功页
 * 
 * @author wangmengmeng
 * 
 */
public class AccDebitCardSuccessActivity extends AccBaseActivity {
	/** 借记卡关联成功页 */
	private View view;
	/** 确定 */
	protected Button btn_confirm;
	/** 借记卡列表 */
	private ListView lv_debit_list;
	/** 成功标题 */
	private TextView tv_success_title_2;
	/** 借记卡关联成功信息 */
	private Map<String, Object> debitSuccessMap;
	/** 成功列表 */
	private List<Map<String, Object>> successList = new ArrayList<Map<String, Object>>();
	/** 失败列表 */
	private List<Map<String, Object>> failList = new ArrayList<Map<String, Object>>();
	/** 一方成功失败标题 */
	private TextView tv_rel_title;
	/** 失败列表 */
	private ListView lv_debitfail_list;
	/** 失败标题 */
	private TextView tv_fail_title;
	private ScrollView scrollview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_myaccount_relevance_title));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		// 添加布局
		view = addView(R.layout.acc_relevanceaccount_debit_success);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		back.setVisibility(View.INVISIBLE);
		// 初始化界面
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		scrollview.smoothScrollTo(0, 0);
	}

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.acc_relevance_step1),
						this.getString(R.string.acc_relevance_step2),
						this.getString(R.string.acc_relevance_step3) });
		StepTitleUtils.getInstance().setTitleStep(3);
		debitSuccessMap = AccDataCenter.getInstance().getDebitlistSuccessMap();
		scrollview = (ScrollView) view.findViewById(R.id.scrollview);
		lv_debit_list = (ListView) view
				.findViewById(R.id.lv_acc_relevance_debit_list);
		tv_rel_title = (TextView) view.findViewById(R.id.tv_rel_title);
		lv_debitfail_list = (ListView) view
				.findViewById(R.id.lv_relevance_fail);
		tv_fail_title = (TextView) view.findViewById(R.id.tv_fail_title);
		tv_success_title_2 = (TextView) view
				.findViewById(R.id.tv_success_title_2);
		List<Map<String, Object>> succedList = (List<Map<String, Object>>) debitSuccessMap
				.get(Acc.RELEVANCEACCRES_RELEVANCESUCCEDACCOUNTLIST_RES);
		if (succedList == null || succedList.size() == 0) {

		} else {
			for (int j = 0; j < succedList.size(); j++) {
				successList.add(succedList.get(j));
			}

		}
		List<Map<String, Object>> ecashsuccedList = (List<Map<String, Object>>) debitSuccessMap
				.get(Acc.RELEVANCEACCRES_RELELECASHACCTSUCCEDLIST_RES);
		if (ecashsuccedList == null || ecashsuccedList.size() == 0) {

		} else {
			for (int j = 0; j < ecashsuccedList.size(); j++) {
				ecashsuccedList.get(j).put(
						Acc.RELEVANCEACCRES_CASHSUCCED_ACCOUNTTYPE_RES,
						accountTypeList.get(13));
				successList.add(ecashsuccedList.get(j));
			}

		}
		List<Map<String, Object>> medicalsuccedList = (List<Map<String, Object>>) debitSuccessMap
				.get(Acc.RELEVANCEACCRES_RELMEDICALACCTSUCCEDLIST_RES);
		if (medicalsuccedList == null || medicalsuccedList.size() == 0) {

		} else {
			for (int j = 0; j < medicalsuccedList.size(); j++) {
				medicalsuccedList.get(j).put(
						Acc.RELEVANCEACCRES_CASHSUCCED_ACCOUNTTYPE_RES,
						MEDICALACC);
				successList.add(medicalsuccedList.get(j));
			}

		}
		List<Map<String, Object>> failedList = (List<Map<String, Object>>) debitSuccessMap
				.get(Acc.RELEVANCEACCRES_RELEVANCEFAILEDACCOUNTLIST_RES);
		if (failedList == null || failedList.size() == 0) {

		} else {
			for (int j = 0; j < failedList.size(); j++) {
				failList.add(failedList.get(j));
			}

		}
		List<Map<String, Object>> ecashfailedList = (List<Map<String, Object>>) debitSuccessMap
				.get(Acc.RELEVANCEACCRES_RELELECASHACCTFAILEDLIST_RES);
		if (ecashfailedList == null || ecashfailedList.size() == 0) {

		} else {
			for (int j = 0; j < ecashfailedList.size(); j++) {
				ecashfailedList.get(j).put(
						Acc.RELEVANCEACCRES_CASHSUCCED_ACCOUNTTYPE_RES,
						accountTypeList.get(13));
				failList.add(ecashfailedList.get(j));
			}

		}
		List<Map<String, Object>> medicalfailedList = (List<Map<String, Object>>) debitSuccessMap
				.get(Acc.RELEVANCEACCRES_RELMEDICALACCTFAILEDLIST_RES);
		if (medicalfailedList == null || medicalfailedList.size() == 0) {

		} else {
			for (int j = 0; j < medicalfailedList.size(); j++) {
				medicalfailedList.get(j).put(
						Acc.RELEVANCEACCRES_CASHSUCCED_ACCOUNTTYPE_RES,
						MEDICALACC);
				failList.add(medicalfailedList.get(j));
			}

		}
		btn_confirm = (Button) view.findViewById(R.id.btnConfirm);
		btn_confirm.setOnClickListener(confirmClickListener);
		if (StringUtil.isNullOrEmpty(successList)
				&& !StringUtil.isNullOrEmpty(failList)) {
			// 无成功,都失败
			lv_debit_list.setVisibility(View.GONE);

			tv_success_title_2.setVisibility(View.GONE);
			tv_rel_title.setVisibility(View.VISIBLE);
			tv_rel_title.setText(BaseDroidApp.getInstanse().getCurrentAct()
					.getString(R.string.acc_relevance_title_fail));
			RelevanceDebitFailAdapter adapterfail = new RelevanceDebitFailAdapter(
					AccDebitCardSuccessActivity.this, failList);
			lv_debitfail_list.setAdapter(adapterfail);
			Utils.setListViewHeightBasedOnChildren(lv_debitfail_list);
		} else if (!StringUtil.isNullOrEmpty(successList)
				&& StringUtil.isNullOrEmpty(failList)) {
			// 无失败,都成功
			// 列表赋值
			tv_rel_title.setVisibility(View.VISIBLE);
			tv_rel_title.setText(BaseDroidApp.getInstanse().getCurrentAct()
					.getString(R.string.acc_relevance_title_success));
			lv_debitfail_list.setVisibility(View.GONE);
			tv_fail_title.setVisibility(View.GONE);
			RelevanceDebitSuccessAdapter adapter = new RelevanceDebitSuccessAdapter(
					AccDebitCardSuccessActivity.this, successList);
			lv_debit_list.setAdapter(adapter);
			Utils.setListViewHeightBasedOnChildren(lv_debit_list);

		} else if (!StringUtil.isNullOrEmpty(successList)
				&& !StringUtil.isNullOrEmpty(failList)) {
			// 有成功,有失败
			// 列表赋值
			tv_rel_title.setVisibility(View.GONE);
			RelevanceDebitSuccessAdapter adapter = new RelevanceDebitSuccessAdapter(
					AccDebitCardSuccessActivity.this, successList);
			lv_debit_list.setAdapter(adapter);
			Utils.setListViewHeightBasedOnChildren(lv_debit_list);
			RelevanceDebitFailAdapter adapterfail = new RelevanceDebitFailAdapter(
					AccDebitCardSuccessActivity.this, failList);
			lv_debitfail_list.setAdapter(adapterfail);
			Utils.setListViewHeightBasedOnChildren(lv_debitfail_list);
		}

	}

	/** 确定按钮点击事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	};
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 回主页面
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 屏蔽返回键
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}
