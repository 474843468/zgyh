package com.chinamworld.bocmbci.biz.acc.relevanceaccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.adapter.ChooseRelevanceDebitAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 借记卡选择关联账户
 * 
 * @author wangmengmeng
 * 
 */
public class AccDebitCardChooseActivity extends AccBaseActivity {
	private static final String TAG = AccDebitCardChooseActivity.class
			.getSimpleName();
	/** 借记卡选择关联账户页 */
	private View view;
	/** 账户自助关联返回的借记卡信息 */
	private List<Map<String, String>> debitList;
	/** 账户自助关联返回的未关联借记卡信息 */
	protected List<Map<String, String>> falseDebitList = new ArrayList<Map<String, String>>();
	/** 待关联账户列表 */
	private ListView lv_debit_list;
	/** 上一步 */
	private Button btn_last;
	/** 确定 */
	protected Button btn_confirm;
	/** 选中项序号 */
	protected int selectedPosition = -1;
	/** 选中序号列表 */
	protected List<Integer> select = new ArrayList<Integer>();
	/** 账户自助关联预交易返回信息 */
	private Map<String, Object> preResultMap;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_myaccount_relevance_title));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		// 添加布局
		view = addView(R.layout.acc_relevance_debit_list);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		setBackBtnClick(backBtnClick);
		// 初始化界面
		init();
	}

	/** 左侧返回按钮点击事件 */
	OnClickListener backBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
	};

	/** 初始化界面 */
	private void init() {
		StepTitleUtils.getInstance().initTitldStep(
				this,
				new String[] { this.getString(R.string.acc_relevance_step1),
						this.getString(R.string.acc_relevance_step2),
						this.getString(R.string.acc_relevance_step3) });
		StepTitleUtils.getInstance().setTitleStep(1);
		// 通过账户自助关联结果取到要用的借记卡信息
		preResultMap = AccDataCenter.getInstance().getRelevancePremap();
		debitList = (List<Map<String, String>>) (preResultMap
				.get(Acc.RELEVANCEACCPRE_ACC_RELEVANCEDEBITACCOUNTLIST_RES));

		for (int i = 0; i < debitList.size(); i++) {
			String acc_type = debitList.get(i).get(
					Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTTYPE_RES);
			if (acc_type.equals(YOUHUITONGZH)) {

			} else {
				if (Boolean.valueOf(debitList.get(i).get(
						Acc.RELEVANCEACCPRE_ACC_DEBITLINKEDFLAG_RES))) {
					// 已关联

				} else {
					// 未关联
					falseDebitList.add(debitList.get(i));
				}

				if (!StringUtil.isNull(debitList.get(i).get(
						Acc.RELEVANCEACCPRE_ACC_DEBITISHAVEELECASHACCT_RES))) {
					// 判断是否有电子现金账户
					if (debitList
							.get(i)
							.get(Acc.RELEVANCEACCPRE_ACC_DEBITISHAVEELECASHACCT_RES)
							.equals(isHaveECashAccountList.get(1))) {
						// 有电子现金账户
						if (debitList
								.get(i)
								.get(Acc.RELEVANCEACCPRE_ACC_DEBITLINKELECASHACCTFLAG_RES)
								.equals(isHaveECashAccountList.get(1))) {
							// 没有关联网银
							Map<String, String> listMap = new HashMap<String, String>();
							Map<String, String> list = debitList.get(i);
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_MAINACCOUNTNUMBER_RES,
									list.get(Acc.RELEVANCEACCPRE_ACC_MAINACCOUNTNUMBER_RES));
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTNUMBER_RES,
									list.get(Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTNUMBER_RES));
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTIBKNUM_RES,
									list.get(Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTIBKNUM_RES));
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_DEBITLINKEDFLAG_RES,
									list.get(Acc.RELEVANCEACCPRE_ACC_DEBITLINKEDFLAG_RES));
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_DEBITISHAVEELECASHACCT_RES,
									list.get(Acc.RELEVANCEACCPRE_ACC_DEBITISHAVEELECASHACCT_RES));
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_DEBITLINKELECASHACCTFLAG_RES,
									list.get(Acc.RELEVANCEACCPRE_ACC_DEBITLINKELECASHACCTFLAG_RES));
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTTYPE_RES,
									accountTypeList.get(13));
							falseDebitList.add(listMap);

						}
					}
				}
				if (!StringUtil.isNull(debitList.get(i).get(
						Acc.RELEVANCEACCPRE_ACC_DEBITISHAVEMEDICALACCT_RES))) {
					// 判断是否有医保账户
					if (debitList
							.get(i)
							.get(Acc.RELEVANCEACCPRE_ACC_DEBITISHAVEMEDICALACCT_RES)
							.equals(isHaveECashAccountList.get(1))) {
						// 有医保账户
						if (debitList
								.get(i)
								.get(Acc.RELEVANCEACCPRE_ACC_DEBITLINKMEDICALACCTFLAG_RES)
								.equals(isHaveECashAccountList.get(1))) {
							// 没有关联网银
							Map<String, String> listMap = new HashMap<String, String>();
							Map<String, String> list = debitList.get(i);
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_MAINACCOUNTNUMBER_RES,
									list.get(Acc.RELEVANCEACCPRE_ACC_MAINACCOUNTNUMBER_RES));
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTNUMBER_RES,
									list.get(Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTNUMBER_RES));
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTIBKNUM_RES,
									list.get(Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTIBKNUM_RES));
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_DEBITLINKEDFLAG_RES,
									list.get(Acc.RELEVANCEACCPRE_ACC_DEBITLINKEDFLAG_RES));
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_DEBITISHAVEMEDICALACCT_RES,
									list.get(Acc.RELEVANCEACCPRE_ACC_DEBITISHAVEMEDICALACCT_RES));
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_DEBITLINKMEDICALACCTFLAG_RES,
									list.get(Acc.RELEVANCEACCPRE_ACC_DEBITLINKMEDICALACCTFLAG_RES));
							listMap.put(
									Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTTYPE_RES,
									MEDICALACC);
							falseDebitList.add(listMap);
						}
					}

				}

			}

		}
		lv_debit_list = (ListView) view.findViewById(R.id.lv_relevance_debit);

		btn_last = (Button) view.findViewById(R.id.btnLast);
		btn_last.setOnClickListener(goLastClickListener);

		btn_confirm = (Button) view.findViewById(R.id.btnConfirm);
		btn_confirm.setOnClickListener(confirmClickListener);
		final ChooseRelevanceDebitAdapter adapter = new ChooseRelevanceDebitAdapter(
				AccDebitCardChooseActivity.this, falseDebitList);
		lv_debit_list.setAdapter(adapter);
		/** 是否选中 true代表可选,false代表不可选 */
		final List<Boolean> booleanSelect = new ArrayList<Boolean>();
		for (int i = 0; i < falseDebitList.size(); i++) {
			booleanSelect.add(i, true);
		}

		adapter.setBooleanSelect(booleanSelect);
		lv_debit_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (adapter.getBooleanSelect().get(position)) {
					// 代表可选
					select.add(position);
					selectedPosition = position;
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
					selectedPosition = position;
					booleanSelect.set(position, true);
					adapter.setBooleanSelect(booleanSelect);
				}
			}
		});
	}

	List<Map<String, String>> choosefalseDebitList = new ArrayList<Map<String, String>>();

	/** 上一步按钮点击事件 */
	OnClickListener goLastClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	/** 确定按钮点击事件 */
	OnClickListener confirmClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 请求借记卡关联结果
			if (select == null || select.size() == 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						AccDebitCardChooseActivity.this
								.getString(R.string.acc_choose_debit_rel));
				return;
			}
			/** 用户选择的未关联借记卡信息 */
			List<Map<String, String>> choosefalseDebitList = new ArrayList<Map<String, String>>();

			for (int i = 0; i < select.size(); i++) {
				String type = falseDebitList.get(select.get(i)).get(
						Acc.RELEVANCEACCPRE_ACC_DEBITACCOUNTTYPE_RES);
				if (type.equals(accountTypeList.get(13))) {
					// 关联电子现金账户
					falseDebitList.get(select.get(i)).put(
							Acc.RELEVANCEACCRES_LINKECASHORNOT_REQ,
							isHaveECashAccountList.get(1));
				} else if (type.equals(MEDICALACC)) {
					// 关联医保账户
					falseDebitList.get(select.get(i)).put(
							Acc.RELEVANCEACCRES_LINKMEDICALORNOT_REQ,
							isHaveECashAccountList.get(1));
				} else {
					// 关联借记卡
					falseDebitList.get(select.get(i)).put(
							Acc.RELEVANCEACCRES_LINKDEBITORNOT_REQ,
							isHaveECashAccountList.get(1));
				}
				choosefalseDebitList.add(falseDebitList.get(select.get(i)));
			}

			// 存储选择的借记卡账户信息
			AccDataCenter.getInstance().setChoosefalseDebitList(
					choosefalseDebitList);
			Intent intent = new Intent(AccDebitCardChooseActivity.this,
					AccDebitCardConfirmActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

		}
	};
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// // 回主页面
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}
}
