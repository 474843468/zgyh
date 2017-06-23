package com.chinamworld.bocmbci.biz.sbremit.mysbremit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitBaseActivity;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitDataCenter;
import com.chinamworld.bocmbci.biz.sbremit.mysbremit.adapter.SBRemitAccRemainAdapter;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 我的结汇购汇页面 */
public class AccountRemainActivity extends SBRemitBaseActivity implements OnClickListener, OnItemClickListener {

	/** 账户标识 */
	private String acountId;
	/** 账户号码 */
	private String acountNumber;
	/** 账户类型 */
	private String acountType;
	/** 结汇/购汇类型 */
	private String sbType;
	/** 账号控件 */
	private TextView customer_accNumber;
	/** 账户余额列表控件 */
	private ListView remain_money_listView;
	/** 账户余额适配器 */
	private SBRemitAccRemainAdapter accRemainAdapter;
	/** 账户余额列表 */
	private List<Map<String, Object>> accRemainList;
	/** 账户余额筛选列表 */
	private List<Map<String, Object>> accRemainList2;
	/** 已用额度、剩余额度、随即值、表单 */
	private Map<String, Object> resultAmount;
	/** 账户的证件类型 */
	private String identityType;
	/** 账户的证件号码 */
	private String identityNumber;
	/** 账户的姓名 */
	private String name;
	/** 是否为重点关注对象 */
	private String important_focus;
	/** 重点关注对象签署状态 */
	private String signStatus;
	/** 交易类型主体代码，分为境内个人和境外个人 */
	private String custTypeCode;
	/** 钞汇类型 */
	private String cashRemit;
	/** 币种类型 */
	private String moneyType;
	/** 人民币余额 */
	private String availableBalanceRMB;
	/** 个人可结售汇金额折成美元 */
	private String annRmeAmtUSD;
	/** 境外人士提示信息 */
	private TextView outsideMessage;
	/** 格式化后人民币余额 */
	private String availableBalanceRMB_new;
	/** 币种类型 */
	private String moneytype_new;
	/** 可用余额*/
	private String availableBalance;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(this.getString(R.string.my_sbremit));// 为界面标题赋值
		addView(R.layout.sbremit_acc_remain);// 添加布局
		initViews();
		initParamsInfo();
	}

	private void initViews() {
		back.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		customer_accNumber = (TextView) findViewById(R.id.customer_accNumber);
		remain_money_listView = (ListView) findViewById(R.id.remain_money_listView);
		outsideMessage=(TextView) findViewById(R.id.outsideMessage);
		remain_money_listView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		case R.id.ib_top_right_btn:// 返回主页
			ActivityTaskManager.getInstance().removeAllActivityInFirstChange();
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化账户信息 账号、账户id
	 */
	private void initParamsInfo() {
		Intent intent = getIntent();
		Map<String, Object> accInfo = SBRemitDataCenter.getInstance().getAccInfo();
		acountId = (String) accInfo.get(Comm.ACCOUNT_ID);
		acountNumber = (String) accInfo.get(Comm.ACCOUNTNUMBER);
		acountType = (String) accInfo.get(Comm.ACCOUNT_TYPE);
		identityType = intent.getStringExtra("identityType");
		identityNumber = intent.getStringExtra("identityNumber");
		if (StringUtil.isNull(acountId) || StringUtil.isNull(acountNumber))
			return;
		((TextView) findViewById(R.id.acc_type)).setText(LocalData.AccountType.get(acountType) + "："
				+ StringUtil.getForSixForString(acountNumber));
		customer_accNumber.setText(StringUtil.getForSixForString(acountNumber));
		if (((Integer.parseInt(identityType) != 47) && (Integer.parseInt(identityType)) != 48
				&& (Integer.parseInt(identityType)) != 49 && (Integer.parseInt(identityType)) != 3
				&& (Integer.parseInt(identityType)) != 1 && (Integer.parseInt(identityType)) != 2)) {

			BaseDroidApp.getInstanse().showInfoMessageDialog("不可办理相关业务");
			finish();

		} else {
			communicationCallBack(GET_ACCOUNT_IN_CALLBACK);
			// requestForAccRemain();
		}

	}

	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case GET_ACCOUNT_IN_CALLBACK:
			accRemainList2 = new ArrayList<Map<String, Object>>();
			if (SBRemitDataCenter.getInstance().accRemainList == null)
				return;
			for (int i = 0; i < SBRemitDataCenter.getInstance().accRemainList.size(); i++) {
				moneyType = (String) SBRemitDataCenter.getInstance().accRemainList.get(i).get(SBRemit.CURRENCY);
				cashRemit = (String) SBRemitDataCenter.getInstance().accRemainList.get(i).get(SBRemit.CASH_REMIT);

				if (SBRemitDataCenter.getInstance().getMoneyType().contains(moneyType)
						|| SBRemitDataCenter.getInstance().getRmbCode().containsKey(moneyType)) {
					if ((SBRemit.MONERY_TYPE_056.equals(moneyType) || SBRemit.MONERY_TYPE_134.equals(moneyType)
							|| SBRemit.MONERY_TYPE_213.equals(moneyType) || SBRemit.MONERY_TYPE_085.equals(moneyType) || SBRemit.MONERY_TYPE_096
								.equals(moneyType)) && SBRemit.CASH_REMIT_02.equals(cashRemit)) {
						// 印尼卢比、巴西里亚尔、新台币、印度卢比、阿联酋迪拉姆：没有现汇
						continue;
					} else if ((SBRemit.MONERY_TYPE_032.equals(moneyType)||SBRemit.MONERY_TYPE_032_word.equals(moneyType)) && SBRemit.CASH_REMIT_01.equals(cashRemit)) {
						// 马来西亚林吉特：没有现钞
						continue;
					}
//					else if (SBRemit.MONERY_TYPE_101.equals(moneyType) || SBRemit.MONERY_TYPE_101_word.equals(moneyType)
//							|| SBRemit.MONERY_TYPE_166.equals(moneyType)|| SBRemit.MONERY_TYPE_166_word.equals(moneyType)
//							|| SBRemit.MONERY_TYPE_049.equals(moneyType)|| SBRemit.MONERY_TYPE_049_word.equals(moneyType)
//							|| SBRemit.MONERY_TYPE_179.equals(moneyType)|| SBRemit.MONERY_TYPE_179_word.equals(moneyType)
//							) {
//						// 哈萨克斯坦坚戈；柬埔寨瑞尔；蒙古图格里克；尼泊尔卢比，既没有现钞也没有现汇
//						continue;
//					} 
					else if (SBRemitDataCenter.getInstance().getRmbCode().containsKey(moneyType)) {
						accRemainList2.add(0, SBRemitDataCenter.getInstance().accRemainList.get(i));
					} else {
						accRemainList2.add(SBRemitDataCenter.getInstance().accRemainList.get(i));
					}
				}
				;
			}
			BaseHttpEngine.dissMissProgressDialog();
			availableBalanceRMB = (String) accRemainList2.get(0).get(SBRemit.AVAILABLE_BALANCE);
			if(identityType.equals("01")){
				setListView(accRemainList2);
				outsideMessage.setVisibility(View.GONE);
			}else {
				setListView(accRemainList2);
				int a=accRemainList2.size();
				if(a==1&&((accRemainList2.get(0).get("currency").equals("001"))||(accRemainList2.get(0).get("currency").equals("CNY")))){
					
					outsideMessage.setVisibility(View.VISIBLE);
				}else{
					outsideMessage.setVisibility(View.GONE);
				}
				
			}
			
			break;
		default:
			break;
		}
	}

	/**
	 * @Title: setListView
	 * @Description: 填充视图的数据
	 * @param
	 * @return void
	 */
	private void setListView(List<Map<String, Object>> remainList) {
		if (accRemainAdapter == null) {
			accRemainAdapter = new SBRemitAccRemainAdapter(this, this, remainList, acountId, acountNumber, identityType);

			remain_money_listView.setAdapter(accRemainAdapter);
		} else {
			accRemainAdapter.setData(remainList);

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == SREMIT_OPERATION || requestCode == BREMIT_OPERATION) {
				setResult(resultCode);
				finish();
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 所选币种信息 */
	Map<String, Object> map;
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		TextView money_type = (TextView) arg1.findViewById(R.id.acc_remain_listiterm_tv1);
		map = (Map<String, Object>) money_type.getTag();
		LogGloble.e("asd", "+++++---" + map);
		if (Integer.parseInt(identityType) == 47 || Integer.parseInt(identityType) == 48
				|| Integer.parseInt(identityType) == 49 || Integer.parseInt(identityType) == 3) {
			if (arg2 == 0) {
				return;
			}
		}
		availableBalance=(String) accRemainList2.get(arg2).get(SBRemit.AVAILABLE_BALANCE);
		if(Double.parseDouble(availableBalance)==0){
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					AccountRemainActivity.this.getResources()
							.getString(R.string.not_enough));
			return;
		}

		if (map == null)
			return;

		TextView right_tv = (TextView) arg1.findViewById(R.id.right_tv);
		TextView acc_remain_listiterm_tv1 = (TextView) arg1.findViewById(R.id.acc_remain_listiterm_tv1);
		sbType = sbRemitValueKey_New.get(right_tv.getText().toString());
		moneytype_new = (String) accRemainList2.get(arg2).get(SBRemit.CURRENCY);
		requestForCommonData(sbType, acountId);

		String accCurrency = (String) accRemainList2.get(arg2).get(SBRemit.CURRENCY);

		// 判断是不是美元结汇
		// if(!accCurrency.equals("014") && !accCurrency.equals("USD")){
		// requestbiggesttry();
		// }

	}

	

	/**
	 * @Title: requestForAccRemainCallBack
	 * @Description: 请求已用额度剩余额度余额回调
	 * @param @param resultObj
	 * @return void
	 */

	@SuppressWarnings("unchecked")
	public void requestForCommonDataCallBack(Object resultObj) {
	super.requestForCommonDataCallBack(resultObj);
		resultAmount = SBRemitDataCenter.getInstance().getResultAmount();
		name = (String) resultAmount.get("custName");
		important_focus = (String) resultAmount.get("typeStatus");
		signStatus = (String) resultAmount.get("signStatus");
		custTypeCode = (String) resultAmount.get("custTypeCode");
		annRmeAmtUSD = (String) resultAmount.get("annRmeAmtUSD");
		LogGloble.e("asd", "+++++" + resultAmount);
		//预关注对象页面跳转
		if ((Integer.valueOf(important_focus) == 02) && (Integer.valueOf(signStatus) == 0)) {
			Intent intent = new Intent();
			intent.putExtra(SBRemit.CURRENCY, isObjNull(map.get(SBRemit.CURRENCY)));
			intent.putExtra(SBRemit.CASH_REMIT, isObjNull(map.get(SBRemit.CASH_REMIT)));
			intent.putExtra(SBRemit.AVAILABLE_BALANCE, isObjNull(map.get(SBRemit.AVAILABLE_BALANCE)));
			intent.putExtra(SBRemit.CUSTNAME, name);
			intent.putExtra(SBRemit.IDENTITYNUMBER, identityNumber);
			intent.putExtra(SBRemit.IDENTITYTYPE, identityType);
			intent.putExtra(SBRemit.PUBDATE, isObjNull(resultAmount.get(SBRemit.PUBDATE)));
			intent.putExtra(SBRemit.END_DATE, isObjNull(resultAmount.get(SBRemit.END_DATE)));
			intent.putExtra(SBRemit.SB_TYPE, sbType);
			intent.putExtra(SBRemit.ACCOUNT_ID, acountId);
			LogGloble.e("asd", "sbType" + sbType);
			intent.putExtra(SBRemit.CASH_REMIT, cashRemit);
			intent.putExtra(SBRemit.AVAILABLE_BALANCERMB, (String)map.get(SBRemit.AVAILABLE_BALANCE));
			//修改
			intent.putExtra(SBRemit.ANNRMEAMTUSD, isObjNull(resultAmount.get(SBRemit.ANNRMEAMTUSD)));
			intent.setClass(this, ImpotantFousActivity.class);
			startActivity(intent);
			//关注名单界面跳转
		} else if((Integer.valueOf(important_focus) == 03) && (Integer.valueOf(signStatus) == 0)){
			Intent intent = new Intent();
			intent.putExtra(SBRemit.CURRENCY, isObjNull(map.get(SBRemit.CURRENCY)));
			intent.putExtra(SBRemit.CASH_REMIT, isObjNull(map.get(SBRemit.CASH_REMIT)));
			intent.putExtra(SBRemit.AVAILABLE_BALANCE, isObjNull(map.get(SBRemit.AVAILABLE_BALANCE)));
			intent.putExtra(SBRemit.CUSTNAME, name);
			intent.putExtra(SBRemit.IDENTITYNUMBER, identityNumber);
			intent.putExtra(SBRemit.IDENTITYTYPE, identityType);
			intent.putExtra(SBRemit.PUBDATE, isObjNull(resultAmount.get(SBRemit.PUBDATE)));
			intent.putExtra(SBRemit.END_DATE, isObjNull(resultAmount.get(SBRemit.END_DATE)));
			intent.putExtra(SBRemit.SB_TYPE, sbType);
			intent.putExtra(SBRemit.ACCOUNT_ID, acountId);
			LogGloble.e("asd", "sbType" + sbType);
			intent.putExtra(SBRemit.CASH_REMIT, cashRemit);
			intent.putExtra(SBRemit.AVAILABLE_BALANCERMB, (String)map.get(SBRemit.AVAILABLE_BALANCE));
			//修改
			intent.putExtra(SBRemit.ANNRMEAMTUSD, isObjNull(resultAmount.get(SBRemit.ANNRMEAMTUSD)));
			intent.setClass(this, FousListActivity.class);
			startActivity(intent);
		}
		else {
			if (sbType.equals("01")) {
				 availableBalanceRMB_new=StringUtil.append2Decimals((String)map.get(SBRemit.AVAILABLE_BALANCE), 2);


				startSRemit();
			} else {

				Map<String, Object> trymap = new HashMap<String, Object>(); // 直接保存美元的值
				trymap.put("availableBalanceCUR", availableBalanceRMB);
				trymap.put("annRmeAmtCUR", annRmeAmtUSD);
				SBRemitDataCenter.getInstance().setTryMap(trymap);
				startBRemit();
			}
			
//			requestbiggesttry(sbType, cashRemit, availableBalanceRMB, moneyType, annRmeAmtUSD);
		}

	}

	private void startActivity(){
		if (sbType.equals("01")) {

			startSRemit();
		} else {

			startBRemit();
		}
	}
	
	public void requestbiggesttryCallBack(Object resultObj) {
		super.requestbiggesttryCallBack(resultObj);
		startActivity();

	}

	/**
	 * 启动结汇 输入信息页
	 */
	private void startSRemit() {
		Intent intent = new Intent();
		intent.putExtra(SBRemit.CURRENCY, isObjNull(map.get(SBRemit.CURRENCY)));
		intent.putExtra(SBRemit.CASH_REMIT, isObjNull(map.get(SBRemit.CASH_REMIT)));
		intent.putExtra(SBRemit.USED_AMOUNT, isObjNull(resultAmount.get(SBRemit.USED_AMOUNT)));
		intent.putExtra(SBRemit.REMAIN_AMOUNT, isObjNull(resultAmount.get(SBRemit.REMAIN_AMOUNT)));
		// intent.putExtra(SBRemit.DATA_TABLE,
		// isObjNull(resultAmount.get(SBRemit.DATA_TABLE)));
		intent.putExtra(SBRemit.AVAILABLE_BALANCE, isObjNull(map.get(SBRemit.AVAILABLE_BALANCE)));
		// intent.putExtra(SBRemit.EXCHANGE_RATE,
		// isObjNull(map.get(SBRemit.EXCHANGE_RATE)));
		intent.putExtra(SBRemit.IMPORTANT_FOCUS, isObjNull(resultAmount.get(SBRemit.IMPORTANT_FOCUS)));
		intent.putExtra(SBRemit.SIGNSTATUS, isObjNull(resultAmount.get(SBRemit.SIGNSTATUS)));
		intent.putExtra(SBRemit.CUSTTYPRCODE, isObjNull(resultAmount.get(SBRemit.CUSTTYPRCODE)));
		intent.putExtra(SBRemit.CUSTNAME, name);
		intent.putExtra(SBRemit.IDENTITYTYPE, identityType);
		
		intent.putExtra(SBRemit.SB_TYPE, sbType);
		intent.putExtra(SBRemit.AVAILABLE_BALANCE_new, availableBalanceRMB_new);
		intent.putExtra(SBRemit.MONERY_TYPE_new, moneytype_new);
		intent.putExtra(SBRemit.ANNRMEAMTUSD, annRmeAmtUSD);
		
		if (resultAmount.containsKey(SBRemit.TRANS_RANDOM) && resultAmount.get(SBRemit.TRANS_RANDOM) != null)
			intent.putExtra(SBRemit.TRANS_RANDOM, resultAmount.get(SBRemit.TRANS_RANDOM).toString());
		intent.setClass(this, SRemitInputInfoActivity.class);
		startActivityForResult(intent, SREMIT_OPERATION);
	}

	/**
	 * 启动购汇 输入信息页
	 */
	private void startBRemit() {
		Intent intent = new Intent();
		intent.putExtra(SBRemit.CASH_REMIT, isObjNull(map.get(SBRemit.CASH_REMIT)));
		intent.putExtra(SBRemit.USED_AMOUNT, isObjNull(resultAmount.get(SBRemit.USED_AMOUNT)));
		intent.putExtra(SBRemit.REMAIN_AMOUNT, isObjNull(resultAmount.get(SBRemit.REMAIN_AMOUNT)));
		intent.putExtra(SBRemit.DATA_TABLE, isObjNull(resultAmount.get(SBRemit.DATA_TABLE)));
		intent.putExtra(SBRemit.AVAILABLE_BALANCE, isObjNull(map.get(SBRemit.AVAILABLE_BALANCE)));
		intent.putExtra(SBRemit.ANNRMEAMTUSD, isObjNull(resultAmount.get(SBRemit.ANNRMEAMTUSD)));
		intent.putExtra(SBRemit.IMPORTANT_FOCUS, isObjNull(resultAmount.get(SBRemit.IMPORTANT_FOCUS)));
		intent.putExtra(SBRemit.SIGNSTATUS, isObjNull(resultAmount.get(SBRemit.SIGNSTATUS)));
		if (resultAmount.containsKey(SBRemit.TRANS_RANDOM) && resultAmount.get(SBRemit.TRANS_RANDOM) != null)
			intent.putExtra(SBRemit.TRANS_RANDOM, resultAmount.get(SBRemit.TRANS_RANDOM).toString());
		intent.putExtra(SBRemit.IDENTITYTYPE, identityType);
		intent.setClass(this, BRemitInputInfoActivity.class);
		startActivityForResult(intent, BREMIT_OPERATION);
	}
	/**
	 * 启动购汇 输入信息页
	 */

}
