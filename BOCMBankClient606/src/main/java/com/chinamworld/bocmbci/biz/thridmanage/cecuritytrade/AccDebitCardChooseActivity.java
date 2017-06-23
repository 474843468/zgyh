package com.chinamworld.bocmbci.biz.thridmanage.cecuritytrade;


/**
 * 借记卡选择关联账户
 * 
 * @author wangmengmeng
 * 
 */
public class AccDebitCardChooseActivity extends com.chinamworld.bocmbci.biz.acc.relevanceaccount.AccDebitCardChooseActivity {/*
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLeftSideList(this, LocalData.thirdManangerLeftListData);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		btn_confirm.setOnClickListener(confirmClickListener);
	}

	@Override
	protected void setSelectedMenu(int clickIndex) {
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		Intent it = new Intent();
		switch (clickIndex) {
		case 0:
			// 第三方存管
			it.setClass(this, CecurityTradeActivity.class);
			break;
		case 1:
			// 历史交易
			it.setClass(this, HistoryTradeActivity.class);
			break;
		case 2:
			// 台账
			it.setClass(this, PlatforAcctActivity.class);
			break;
		case 3:
			// 开户
			it.setClass(this, OpenAccActivity.class);
			break;
		}
		startActivity(it);
	}
	
	*//** 确定按钮点击事件 *//*
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
			*//** 用户选择的未关联借记卡信息 *//*
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
	
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// // 回主页面
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
*/}
