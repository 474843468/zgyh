package com.chinamworld.bocmbci.biz.thridmanage.cecuritytrade;



/**
 * 填写关联账户信息页面
 * 
 * @author wangmengmeng
 * 
 */
public class AccInputRelevanceAccountActivity extends com.chinamworld.bocmbci.biz.acc.relevanceaccount.AccInputRelevanceAccountActivity {/*

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLeftSideList(this, LocalData.thirdManangerLeftListData);
		setLeftSelectedPosition(0);
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
	
	*//**
	 * 请求账户自助关联预交易回调
	 * 
	 * @param resultObj
	 *//*
	@Override
	public void requestPsnRelevanceAccountPreCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		// 通讯返回result
		Map<String, Object> relevancePremap = (Map<String, Object>) (biiResponseBody
				.getResult());
		if (StringUtil.isNullOrEmpty(relevancePremap)) {
			return;
		}
		// 储存返回信息
		AccDataCenter.getInstance().setRelevancePremap(relevancePremap);
		String accountType = (String) relevancePremap
				.get(Acc.RELEVANCEACCPRE_ACC_ACCOUNTTYPE_RES);

		if (accountType.equals(accountTypeList.get(3))) {
			// 借记卡
			etImageCode.setText(ConstantGloble.BOCINVT_NULL_STRING);
			Intent intent = new Intent(AccInputRelevanceAccountActivity.this,
					AccDebitCardChooseActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);

		} else if (accountType.equals(accountTypeList.get(13))) {
			// 电子现金账户
			etImageCode.setText(ConstantGloble.BOCINVT_NULL_STRING);
			Intent intent = new Intent(AccInputRelevanceAccountActivity.this,
					AccICCardConfirmActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		} else if (accountType.equals(accountTypeList.get(1))
				|| accountType.equals(accountTypeList.get(2))
				|| accountType.equals(accountTypeList.get(4))) {
			// 信用卡
			etImageCode.setText(ConstantGloble.BOCINVT_NULL_STRING);
			Intent intent = new Intent(AccInputRelevanceAccountActivity.this,
					AccCreditCardConfirmActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		}
	}
	
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
		

	
*/}
