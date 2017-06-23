package com.chinamworld.bocmbci.biz.acc.mybankaccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.adapter.ACCBankAccountAdapter;
import com.chinamworld.bocmbci.biz.acc.dialogActivity.FinanceIcDetailDialogActivity;
import com.chinamworld.bocmbci.biz.acc.dialogActivity.MyCardDetailDoalogActivity;
import com.chinamworld.bocmbci.biz.acc.financeicaccount.FinanceIcAccountTransferDetailActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCrcdDetailActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.MyVirtualBCListActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.MyVivtualQueryActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.CardWelcomGuideUtil;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.SwipeListView;
import com.chinamworld.bocmbci.widget.SwipeListView.DropViewListener;

/**
 * 账户管理主页面
 * 
 * @author wangmengmeng
 * 
 */
public class AccManageActivity extends AccBaseActivity {
	
	// TODO 成员变量/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/** 账户列表信息页 */
	private View view;
	/** 账户列表 */
	private SwipeListView lvBankAccountList;
	/** 添加关联账户视图 */
	private View addTransferView;
	/** 取消关联选择的项 */
	private int position = 0;
	/** 获取到的tokenId 保存 */
	private String tokenId;
	/** 所有账户信息adapter */
	private ACCBankAccountAdapter bankadapter;
	/** 取消关联点击 */
	private boolean click = true;
	/** 详情选择项 */
	private int detailPostion = 0;
	/** 是否有基金账户 */
	private boolean ishavebinding = false;
	public boolean isShowDialog = false;
	public static String currency;
	/** 电子现金账户详情信息 */
	private Map<String, String> callbackmap;
	private static String currency1 = "";
	private static String currency2 = "";

	private Map<String, Object> currencyMap1;
	private Map<String, Object> currencyMap2;
	private boolean ishavecurrencytwo = false;

	private Map<String, Object> resultDetail;
	/** 币种 */
	public final List<String> queryCurrencyList = new ArrayList<String>();
	public final List<String> queryCodeList = new ArrayList<String>();
	/** 标志 0：普通 1：明细 2：关联账户转账 */
	private int actionid = 0;
	public List<List<String>> queryCashRemitList = new ArrayList<List<String>>();
	public List<List<String>> queryCashRemitCodeList = new ArrayList<List<String>>();
	private String mobilNumStr;
	private String defaultAccIdStr;
	private int inposition;
	/** 账户状态 */
	private String status;
	/** 默认账户状态 */
	private String defaultstatus;
	/** 信用卡数据 */
	public List<Map<String, Object>> crcdList = new ArrayList<Map<String, Object>>();
	/** 电子现金账户数据 */
	public List<Map<String, Object>> financeIcList = new ArrayList<Map<String, Object>>();
	/** 普通账户数据 */
	public List<Map<String, Object>> cardList = new ArrayList<Map<String, Object>>();
	/** 虚拟信用卡数据 */
	public List<Map<String, Object>> xncrcdList = new ArrayList<Map<String, Object>>();

	/** 提示信息 */
	public TextView tv_notice_title;

	// TODO 成员方法/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 包含向右滑动的listview 目的是屏蔽左侧菜单的滑动事件
		setContainsSwipeListView(true);
		// 为界面标题赋值
		setTitle(this.getString(R.string.acc_main_title));
		// 添加布局
		view = addView(R.layout.acc_mybankaccount_list);
		gonerightBtn();
		// 初始化界面
		init();
		setLeftSelectedPosition("accountManager_1");
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		setLeftSelectedPosition("accountManager_1");
//	}

	/** 初始化界面 */
	private void init() {
		lvBankAccountList = (SwipeListView) view.findViewById(R.id.acc_accountlist);
		tv_notice_title = (TextView) view.findViewById(R.id.tv_add_notice_title);
		addTransferView = LayoutInflater.from(this).inflate(R.layout.acc_mybankaccount_list_add_transfer, null);
		// 设置列表底部视图
//		lvBankAccountList.addFooterView(addTransferView); 屏蔽自助关联
		LinearLayout ll_footer = (LinearLayout) addTransferView.findViewById(R.id.ll_footer);
		// 进行自助关联
		ll_footer.setOnClickListener(goRelevanceClickListener);
		// 请求所有账户列表信息
		requestAccBankAccountList(0);
	}


	// 过滤有效存单
	public boolean siftstatus(List<Map<String, Object>> accountDetaiList) {
		// Spinner 存折册号 根据账户列表来显示
		// 返回数据里面 存折号和存单是没有关联的 重新组装数据
		ArrayList<Map<String, Object>> volumesAndCdnumbers = new ArrayList<Map<String, Object>>();
		final List<String> volumes = new ArrayList<String>();
		// 找出列表中所有不相同的存折册号
		for (int i = 0; i < accountDetaiList.size(); i++) {
			Map<String, Object> detaimap = accountDetaiList.get(i);
			String volumeNumber = (String) detaimap.get(Dept.VOLUME_NUMBER);
			if (StringUtil.isNullOrEmpty(volumeNumber)) {
				continue;
			}
			if (volumes.size() > 0) {
				if (!volumes.contains(volumeNumber)) {
					volumes.add(volumeNumber);
				}
			} else {
				volumes.add(volumeNumber);
			}
		}

		if (StringUtil.isNullOrEmpty(volumes)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(BaseDroidApp.getInstanse().getCurrentAct().getResources().getString(R.string.no_dept_status));
			return false;
		}

		// 讲存折册号 和 存单号 对应起来
		for (int i = 0; i < volumes.size(); i++) {
			ArrayList<Map<String, Object>> list = null;
			list = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapContent = null;
			
			for (int j = 0; j < accountDetaiList.size(); j++) {
				// 单个存单详情
				Map<String, Object> detaimap = accountDetaiList.get(j);
				String volumeNumber = (String) detaimap.get(Dept.VOLUME_NUMBER);
				if (StringUtil.isNullOrEmpty(volumeNumber)) {
					continue;
				}
				String cdNumber = (String) detaimap.get(Dept.CD_NUMBER);
				if (volumeNumber.equals(volumes.get(i))) {
					mapContent = new HashMap<String, Object>();
					mapContent.put(ConstantGloble.VOL, cdNumber);
					mapContent.put(ConstantGloble.CONTENT, accountDetaiList.get(j));
					String deptstatus = (String) detaimap.get(Dept.STATUS);
					if (!StringUtil.isNull(deptstatus)) {
						if (deptstatus.equals(ConstantGloble.FOREX_ACCTYPE_NORMAL) || deptstatus.equals("V")) {
							list.add(mapContent);
						}
					}
				}
			}
			if (StringUtil.isNullOrEmpty(list)) {
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(Dept.VOLUME_NUMBER, volumes.get(i));
				map.put(Dept.CD_NUMBER, list);
				volumesAndCdnumbers.add(map);
			}
		}
		if (StringUtil.isNullOrEmpty(volumesAndCdnumbers)) {
			return false;
		} else {
			AccDataCenter.getInstance().setVolumesAndCdnumbers(volumesAndCdnumbers);
			return true;
		}
	}

	// 清除数据
	public void clearList() {
		if (!StringUtil.isNullOrEmpty(queryCurrencyList)) {
			queryCurrencyList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCodeList)) {
			queryCodeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCashRemitCodeList)) {
			queryCashRemitCodeList.clear();
		}
		if (!StringUtil.isNullOrEmpty(queryCashRemitList)) {
			queryCashRemitList.clear();
		}
	}

	// 402修改 分区显示
	/** 列表数据分区显示 */
	public List<Map<String, Object>> combinAccListData(List<Map<String, Object>> list) {
		List<Map<String, Object>> banklist = new ArrayList<Map<String, Object>>();
		/** 信用卡数据 */
		List<Map<String, Object>> crcdAccList = new ArrayList<Map<String, Object>>();
		/** 虚拟信用卡数据 */
		List<Map<String, Object>> xncrcdAccList = new ArrayList<Map<String, Object>>();
		/** 电子现金账户数据 */
		List<Map<String, Object>> financeIcAccList = new ArrayList<Map<String, Object>>();
		/** 普通账户数据 */
		List<Map<String, Object>> cardAccList = new ArrayList<Map<String, Object>>();
		/** 定一本数据 */
		List<Map<String, Object>> deptAccList = new ArrayList<Map<String, Object>>();
		
		for (Map<String, Object> map : list) {
			String acctype = (String) map.get(Acc.ACC_ACCOUNTTYPE_RES);
			if (acctype.equals(ZHONGYIN) || acctype.equals(GREATWALL) || acctype.equals(SINGLEWAIBI)) {
				// 信用卡
				crcdAccList.add(map);
			} else if (acctype.equals(ICCARD)) {
				// 单电子现金账户
				financeIcAccList.add(map);
			} else if (acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_REG)
					|| acctype.equals(ConstantGloble.ACC_TYPE_EDU)
					|| acctype.equals(ConstantGloble.ACC_TYPE_ZOR)) {
				// 定一本、零存整取、教育储蓄
				deptAccList.add(map);
			} else if (acctype
					.equalsIgnoreCase(ConstantGloble.ACC_TYPE_XNCRCD1)
					|| acctype
							.equalsIgnoreCase(ConstantGloble.ACC_TYPE_XNCRCD2)) {
				// 虚拟信用卡
				xncrcdAccList.add(map);
			} else {
				// 普通卡
				cardAccList.add(map);
			}
		}
		banklist.addAll(cardAccList);
		banklist.addAll(financeIcAccList);
		banklist.addAll(deptAccList);
		banklist.addAll(crcdAccList);
		banklist.addAll(xncrcdAccList);
		return banklist;
	}

	public void relTrans(int from, int to) {
		if (to == bankAccountList.size()) {
			return;
		} else {
			if (from == to) {
				return;
			}
		}
		Map<String, Object> transOut = bankAccountList.get(from);
		Map<String, Object> transIn = bankAccountList.get(to);
		String transOutType = (String) transOut.get(Acc.ACC_ACCOUNTTYPE_RES);
		if (LocalData.transOut.contains(transOutType)) {
			// 判断是否是转出账户类型
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.acc_not_transout));
			return;
		}
		String transInType = (String) transIn.get(Acc.ACC_ACCOUNTTYPE_RES);
		if (LocalData.transIn.contains(transInType)) {
			// 判断是否是转入账户类型
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.acc_not_transin));
			return;
		}
		if (transInType.equals(ConstantGloble.GREATWALL)
				|| transInType.equals(ConstantGloble.ZHONGYIN)
				|| transInType.equals(ConstantGloble.SINGLEWAIBI)) {
			// 判断如果转入为信用卡,转出只支持长城电子借记卡和长城信用卡

			if (transOutType.equals(GREATWALL)
					|| transOutType.equals(ConstantGloble.ZHONGYIN)
					|| transOutType.equals(ConstantGloble.SINGLEWAIBI)
					|| transOutType.equals(ConstantGloble.ACC_TYPE_BRO)) {
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.acc_not_transin_crcd));
				return;
			} 
//			P601 103  107 可以做转出

		}
		actionid = 2;
		clearList();
		// 请求详情

		if (transOutType.equals(GREATWALL)
				|| transOutType.equals(ConstantGloble.ZHONGYIN)
				|| transOutType.equals(ConstantGloble.SINGLEWAIBI)) {

			// 信用卡——关联账户转账流程
			detailPostion = from;
			inposition = to;
			chooseBankAccount = bankAccountList.get(from);
			// 查询信用卡详情
			requestPsnCrcdQueryAccountDetail(chooseBankAccount, null);
			BiiHttpEngine.showProgressDialog();
		} else {
			// 请求余额
			detailPostion = from;
			inposition = to;
			requestAccBankAccountDetail(String.valueOf(bankAccountList.get(from).get(Acc.ACC_ACCOUNTID_RES)));
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		lvBankAccountList.setSwipeListViewListener(swipeListViewListener);
		switch (resultCode) {
		case RESULT_OK:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				bankAccountList = AccDataCenter.getInstance().getBankAccountList();
				// 都已请求，进行列表赋值
				bankadapter = new ACCBankAccountAdapter(AccManageActivity.this, bankAccountList, 1);
				lvBankAccountList.setLastPositionClickable(false);
				lvBankAccountList.setAllPositionClickable(true);
				lvBankAccountList.setAdapter(bankadapter);
				bankadapter.setDefaultAccIdStr(defaultAccIdStr, defaultstatus);
				// 账户详情查询监听事件
				bankadapter.setOnbanklistItemDetailClickListener(onbanklistItemDetailClickListener);
				lvBankAccountList.setSwipeListViewListener(swipeListViewListener);
				if (bankAccountList.size() > 1) {
					lvBankAccountList.setCanDrag(true);
					lvBankAccountList.setDropViewListener(new DropViewListener() {

						@Override
						public void drop(int from, int to) {
							relTrans(from, to);
						}
					});
				} else {
					gonerightBtn();
				}
			}
			break;
		case 100:
			startOpenRightClick(detailPostion, 0, true);
			break;
		default:
			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
				bankAccountList = AccDataCenter.getInstance().getBankAccountList();
				// 都已请求，进行列表赋值
				bankadapter = new ACCBankAccountAdapter(AccManageActivity.this, bankAccountList, 1);
				lvBankAccountList.setLastPositionClickable(false);
				lvBankAccountList.setAllPositionClickable(true);
				lvBankAccountList.setAdapter(bankadapter);
				bankadapter.setDefaultAccIdStr(defaultAccIdStr, defaultstatus);
				// 账户详情查询监听事件
				bankadapter.setOnbanklistItemDetailClickListener(onbanklistItemDetailClickListener);
				lvBankAccountList.setSwipeListViewListener(swipeListViewListener);
				if (bankAccountList.size() > 1) {
					lvBankAccountList.setCanDrag(true);
					lvBankAccountList.setDropViewListener(new DropViewListener() {

						@Override
						public void drop(int from, int to) {
							relTrans(from, to);
						}
					});
				} else {
					gonerightBtn();
				}
			}
			break;
		}
	}

	public void startOpenRightClick(int position, int action, boolean right) {

		if (position >= bankAccountList.size()) {
			return;
		}
		// String type = (String) bankAccountList.get(position).get(
		// Acc.ACC_ACCOUNTTYPE_RES);
		// if (type.equalsIgnoreCase(ConstantGloble.ACC_ACTYPENOT)) {
		// BaseDroidApp.getInstanse().showInfoMessageDialog(
		// AccManageActivity.this
		// .getString(R.string.acc_cannot_query));
		// return;
		// }
		clearList();
		if (action == 0) {
			detailPostion = position;
			chooseBankAccount = bankAccountList.get(position);
			String acctype = (String) bankAccountList.get(position).get(Acc.ACC_ACCOUNTTYPE_RES);
			String accountId = (String) bankAccountList.get(position).get(Acc.ACC_ACCOUNTID_RES);
			if (acctype.equals(ZHONGYIN) || acctype.equals(GREATWALL) || acctype.equals(SINGLEWAIBI)) {
				actionid = 1;
				// 信用卡
				combinListData();
				int crcdposition = getposition(acctype, accountId);
				Intent intent = new Intent(AccManageActivity.this, MyCrcdDetailActivity.class);
				// 标记是从账户管理进来
				intent.putExtra("fromQuery", "fromAccQuery");
				intent.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, acctype);
				// 存储信用卡数据
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_QUERY_LIST, crcdList);
				// 账单分期成功后跳转标记
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.IS_EBANK_0, 4);
				intent.putExtra(ConstantGloble.ACC_POSITION, crcdposition);
				intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
				startActivityForResult(intent, 3);
			} else if (acctype.equals(ICCARD)) {
				actionid = 1;
				requestFinanceIcAccountList();
			} else if (acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_EDU) || acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_ZOR)) {
				actionid = 1;
				requestSystemDateTime();
				BiiHttpEngine.showProgressDialog();
			} else if (acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_REG)) {
				// 定一本
				actionid = 0;
				if (!StringUtil.isNullOrEmpty(AccDataCenter.getInstance().getVolumesAndCdnumbers())) {
					AccDataCenter.getInstance().getVolumesAndCdnumbers().clear();
				}
				requestAccBankAccountDetail(String.valueOf(bankAccountList.get(position).get(Acc.ACC_ACCOUNTID_RES)));
			} else if (acctype.equals(ConstantGloble.ACC_TYPE_XNCRCD1) || acctype.equals(ConstantGloble.ACC_TYPE_XNCRCD2)) {
				// 虚拟信用卡
				String accName = (String) bankAccountList.get(position).get(Acc.ACC_ACCOUNTNAME_RES);
				String accNumber = (String) bankAccountList.get(position).get(Acc.ACC_ACCOUNTNUMBER_RES);
				combinListData();
				int xnPosition = getposition(acctype, accountId);
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_VIRCRCDLIST, xncrcdList);
				Intent intent = new Intent(AccManageActivity.this, MyVivtualQueryActivity.class);
				intent.putExtra(Crcd.CRCD_VIRTUALCARDNO, accNumber);
				intent.putExtra(ConstantGloble.ACC_POSITION, xnPosition);
				intent.putExtra(Crcd.CRCD_ACCOUNTNAME_RES, accName);
				startActivityForResult(intent, 4);
			} else {
				actionid = 1;
				combinListData();
				AccDataCenter.getInstance().setCommCardList(cardList);
				// 请求余额
				requestAccBankAccountDetail(String.valueOf(bankAccountList.get(position).get(Acc.ACC_ACCOUNTID_RES)));
			}
		}
	}

	/** 筛选列表——进入详细功能模块 */
	public void click() {
		financeIcAccountList = chooseList(financeIcAccountList);
		if (financeIcAccountList == null || financeIcAccountList.size() == 0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(AccManageActivity.this.getString(R.string.acc_financeic_null));
			return;
		}
		AccDataCenter.getInstance().setFinanceIcAccountList(financeIcAccountList);
		Intent intent = new Intent(AccManageActivity.this, FinanceIcAccountTransferDetailActivity.class);
		String accountId = (String) bankAccountList.get(detailPostion).get(Acc.ACC_ACCOUNTID_RES);
		int financeicPosition = 0;
		for (int i = 0; i < financeIcAccountList.size(); i++) {
			Map<String, Object> bankmap = new HashMap<String, Object>();
			bankmap = financeIcAccountList.get(i);
			String id = (String) bankmap.get(Acc.ACC_ACCOUNTID_RES);
			if (id.equals(accountId)) {
				financeicPosition = i;
				break;
			}
		}
		intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
		intent.putExtra(ConstantGloble.ACC_POSITION, financeicPosition);
		startActivityForResult(intent, 5);
	}

	/** 筛选列表 */
	public List<Map<String, Object>> chooseList(List<Map<String, Object>> list) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			// 判断
			// isECashAccount 类型119、103、104这个值是1的话就是电子现金账户
			// 新建签约300的时候纯IC卡
			String acc_type = (String) list.get(i).get(Acc.ACC_ACCOUNTTYPE_RES);
			String isECashAccount = (String) list.get(i).get(Acc.ACC_ISECASHACCOUNT_RES);
			if (StringUtil.isNull(isECashAccount)) {
				continue;
			}
			if (acc_type.equalsIgnoreCase(AccBaseActivity.accountTypeList.get(3))
					&& isECashAccount.equals(ConstantGloble.ACC_FINANCEIC_ISECASH_ONE)) {
				// 119
				resultList.add(list.get(i));
			}
			if (acc_type.equals(AccBaseActivity.accountTypeList.get(1))
					&& isECashAccount.equals(ConstantGloble.ACC_FINANCEIC_ISECASH_ONE)) {
				// 103
				resultList.add(list.get(i));
			}
			if (acc_type.equalsIgnoreCase(AccBaseActivity.accountTypeList.get(2))
					&& isECashAccount.equals(ConstantGloble.ACC_FINANCEIC_ISECASH_ONE)) {
				// 104
				resultList.add(list.get(i));
			}
			if (acc_type.equals(AccBaseActivity.accountTypeList.get(13))) {
				// 300
				resultList.add(list.get(i));
			}
		}
		return resultList;
	}

	/** 重新组装列表数据 */
	public void combinListData() {
		/** 信用卡数据 */
		crcdList = new ArrayList<Map<String, Object>>();
		/** 虚拟信用卡数据 */
		xncrcdList = new ArrayList<Map<String, Object>>();
		/** 电子现金账户数据 */
		financeIcList = new ArrayList<Map<String, Object>>();
		/** 普通账户数据 */
		cardList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : bankAccountList) {
			String acctype = (String) map.get(Acc.ACC_ACCOUNTTYPE_RES);
			String accNumber = (String) map.get(Acc.ACC_ACCOUNTNUMBER_RES);
			if (acctype.equals(ZHONGYIN) || acctype.equals(GREATWALL) || acctype.equals(SINGLEWAIBI)) {
				// 信用卡
				crcdList.add(map);
			} else if (acctype.equals(ICCARD)) {
				// 单电子现金账户
				financeIcList.add(map);
			} else if (acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_REG)) {
				// 定一本
			} else if (acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_XNCRCD1) || acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_XNCRCD2)) {
				// 虚拟信用卡
				map.put(Crcd.CRCD_VIRTUALCARDNO, accNumber);
				xncrcdList.add(map);
			} else {
				// 普通卡
				cardList.add(map);
			}
		}
	}

	/** 得到卡的位置 */
	public int getposition(String type, String accountId) {
		int position = 0;
		List<Map<String, Object>> bankList = new ArrayList<Map<String, Object>>();
		if (type.equals(ZHONGYIN) || type.equals(GREATWALL) || type.equals(SINGLEWAIBI)) {
			// 信用卡
			bankList = crcdList;
		} else if (type.equals(ICCARD)) {
			// 单电子现金账户
			bankList = financeIcList;
		} else if (type.equalsIgnoreCase(ConstantGloble.ACC_TYPE_REG)) {

		} else if (type.equalsIgnoreCase(ConstantGloble.ACC_TYPE_XNCRCD1) || type.equalsIgnoreCase(ConstantGloble.ACC_TYPE_XNCRCD2)) {
			// 虚拟信用卡
			bankList = xncrcdList;
		} else {
			// 普通卡
			bankList = cardList;
		}
		for (int i = 0; i < bankList.size(); i++) {
			Map<String, Object> bankmap = new HashMap<String, Object>();
			bankmap = bankList.get(i);
			String id = (String) bankmap.get(Acc.ACC_ACCOUNTID_RES);
			if (id.equals(accountId)) {
				position = i;
				break;
			}
		}
		return position;
	}

	// TODO 控件事件/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 列表向右滑动事件 */
	BaseSwipeListViewListener swipeListViewListener = new BaseSwipeListViewListener() {
		@Override
		public void onOpened(int position, boolean toRight) {}

		@Override
		public void onClosed(int position, boolean fromRight) {}

		@Override
		public void onListChanged() {}

		@Override
		public void onMove(int position, float x) {}

		@Override
		public void onStartOpen(int position, int action, boolean right) {
			startOpenRightClick(position, action, right);
		}

		@Override
		public void onStartClose(int position, boolean right) {}

		@Override
		public void onClickFrontView(int position) {
			if (click) {
				if (position >= bankAccountList.size()) {
				} else {
					onbanklistItemDetailClickListener.onItemClick(null, lvBankAccountList, position, position);
				}
			} else {
				// 点击了取消关联——其它不可操作
			}
		}

		@Override
		public void onClickBackView(int position) {}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {}
	};

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (click) {
				click = false;
				if (bankAccountList == null || bankAccountList.size() == 0) {
					// 没有账户不可进行取消关联
					return;
				}
				setText(AccManageActivity.this.getString(R.string.finish));
				// 取消关联
				ACCBankAccountAdapter adapter = new ACCBankAccountAdapter(AccManageActivity.this, bankAccountList, 2);
				lvBankAccountList.setLastPositionClickable(false);
				lvBankAccountList.setAllPositionClickable(false);
				lvBankAccountList.setAdapter(adapter);
				adapter.setDefaultAccIdStr(defaultAccIdStr, defaultstatus);
				adapter.setOnbanklistCancelRelationClickListener(onbanklistCancelRelationClickListener);
				lvBankAccountList.setCanDrag(false);
			} else {
				click = true;
				// 右上角按钮赋值
				setText(AccManageActivity.this.getString(R.string.acc_main_right_btn));
				// 都已请求，进行列表赋值
				bankadapter = new ACCBankAccountAdapter(AccManageActivity.this, bankAccountList, 1);
				lvBankAccountList.setLastPositionClickable(false);
				lvBankAccountList.setAllPositionClickable(true);
				lvBankAccountList.setAdapter(bankadapter);
				bankadapter.setDefaultAccIdStr(defaultAccIdStr, defaultstatus);
				// 账户详情查询监听事件
				bankadapter.setOnbanklistItemDetailClickListener(onbanklistItemDetailClickListener);
				lvBankAccountList.setSwipeListViewListener(swipeListViewListener);
				if (bankAccountList.size() > 1) {
					lvBankAccountList.setCanDrag(true);
					lvBankAccountList.setDropViewListener(new DropViewListener() {

						@Override
						public void drop(int from, int to) {
							relTrans(from, to);
						}
					});
				} else {
					gonerightBtn();
				}

			}
		}
	};

	/** 进行自助关联监听事件 */
	OnClickListener goRelevanceClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (click) {
//				Intent intent = new Intent(AccManageActivity.this, AccInputRelevanceAccountActivity.class);
//				intent.putExtra(ConstantGloble.ACC_ISMY, true);
//				startActivityForResult(intent, 8);
				
				Map<String, Object>mapData=new HashMap<String, Object>();
				mapData.put(ConstantGloble.ACC_ISMY, true);
				if(BusinessModelControl.gotoAccRelevanceAccount(AccManageActivity.this, 8, mapData)){
					finish();	
				}
			} else {

			}
		}
	};

	/** 取消关联点击事件 */
	OnItemClickListener onbanklistCancelRelationClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (id == -1) {
				// 点击的是HeaderView或者是FooterView
				return;
			}
			if (bankAccountList.size() == 1) {
				// 只有一张卡不可以取消关联
				BaseDroidApp.getInstanse().showMessageDialog(AccManageActivity.this.getString(R.string.acc_cannotcancel), new OnClickListener() {

					@Override
					public void onClick(View v) {
						click = true;
						BaseDroidApp.getInstanse().dismissErrorDialog();
						// 右上角按钮赋值
						setText(AccManageActivity.this.getString(R.string.acc_main_right_btn));
						ACCBankAccountAdapter bankadapter = new ACCBankAccountAdapter(AccManageActivity.this, bankAccountList, 1);
						lvBankAccountList.setLastPositionClickable(false);
						lvBankAccountList.setAllPositionClickable(true);
						lvBankAccountList.setAdapter(bankadapter);
						bankadapter.setDefaultAccIdStr(defaultAccIdStr, defaultstatus);
						// 账户详情查询监听事件
						bankadapter.setOnbanklistItemDetailClickListener(onbanklistItemDetailClickListener);
						lvBankAccountList.setSwipeListViewListener(swipeListViewListener);
						lvBankAccountList.setCanDrag(false);
					}
				});
			} else {
				AccManageActivity.this.position = position;
				// 弹出对话框,是否取消
				BaseDroidApp.getInstanse().showErrorDialog(AccManageActivity.this.getString(R.string.acc_cancelrelation_msg), 
						R.string.cancle, R.string.confirm,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								switch (Integer.parseInt(v.getTag() + "")) {
								case CustomDialog.TAG_SURE:
									// 确定取消关联
									BaseDroidApp.getInstanse().dismissErrorDialog();
									BaseHttpEngine.showProgressDialog();
									requestCommConversationId();
									break;
								case CustomDialog.TAG_CANCLE:
									// 取消操作
									BaseDroidApp.getInstanse().dismissErrorDialog();
									break;
								}
							}
						});
			}

		}
	};
	
	/** 账户详情查询监听事件 */
	protected OnItemClickListener onbanklistItemDetailClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (id == -1) {
				// 点击的是HeaderView或者是FooterView
				return;
			}
			if (click) {

				clearList();
				actionid = 0;
				detailPostion = position;
				requestPsnIsPayrollAccount();
			} else {

			}
		}
	};

//	/** 人民币监听事件 */
//	protected OnClickListener renmibiClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			currency = currency1;
//			isShowDialog = true;
//			requestPsnCrcdQueryAccountDetail(chooseBankAccount, currency);
//		}
//	};
//
//	/** 外币监听事件 */
//	protected OnClickListener dollerClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			currency = currency2;
//			isShowDialog = true;
//			requestPsnCrcdQueryAccountDetail(chooseBankAccount, currency);
//		}
//	};

	//TODO 网络请求与回调//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 请求所有账户列表信息 */
	public void requestAccBankAccountList(int i) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_LIST_API);
		if (i == 0) {
			// 通讯开始,展示通讯框
			BaseHttpEngine.showProgressDialogCanGoBack();
		}
		HttpManager.requestBii(biiRequestBody, this, "accBankAccountListCallBack");
	}

	/**
	 * 请求所有账户列表回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void accBankAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		bankAccountList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		BaseHttpEngine.dissMissProgressDialog();
		if (bankAccountList == null || bankAccountList.size() == 0) {
			// 通讯结束,关闭通讯框
			BaseDroidApp.getInstanse().showMessageDialog(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.acc_transferquery_null),
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							ActivityTaskManager.getInstance().removeAllActivity();
						}
					});
			return;
		} else {
			// 过滤借记虚拟卡、优汇通、虚拟信用卡（2种）
			List<Map<String, Object>> accountList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < bankAccountList.size(); i++) {
				String type = (String) bankAccountList.get(i).get(Acc.ACC_ACCOUNTTYPE_RES);
				if (XNCARDLIST.contains(type)) {
				} else {
					accountList.add(bankAccountList.get(i));
				}
			}
			bankAccountList.clear();
			bankAccountList = accountList;
			if (bankAccountList == null || bankAccountList.size() == 0) {
				// 通讯结束,关闭通讯框
				BaseDroidApp.getInstanse().showMessageDialog(BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.acc_transferquery_null),
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								ActivityTaskManager.getInstance().removeAllActivity();
							}
						});
				return;
			}
			// 账户分区显示
			bankAccountList = combinAccListData(bankAccountList);
			tv_notice_title.setVisibility(View.VISIBLE);
			// 存取全部账户信息
			AccDataCenter.getInstance().setBankAccountList(bankAccountList);
			// 都已请求，进行列表赋值
			bankadapter = new ACCBankAccountAdapter(this, bankAccountList, 1);
			lvBankAccountList.setLastPositionClickable(false);
			lvBankAccountList.setAllPositionClickable(true);
			lvBankAccountList.setAdapter(bankadapter);
			// 账户详情查询监听事件
			bankadapter.setOnbanklistItemDetailClickListener(onbanklistItemDetailClickListener);
			lvBankAccountList.setSwipeListViewListener(swipeListViewListener);
			if (bankAccountList.size() > 1) {
				// 右上角按钮赋值
				setText(this.getString(R.string.acc_main_right_btn));
				// 右上角按钮点击事件
				setRightBtnClick(rightBtnClick);
				lvBankAccountList.setCanDrag(true);
				lvBankAccountList.setDropViewListener(new DropViewListener() {

					@Override
					public void drop(int from, int to) {
						relTrans(from, to);
					}
				});
			}
			Map<String, Object> loginMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
			mobilNumStr = (String) loginMap.get(Comm.LOGINNAME);
			// 显示帮助指南
			CardWelcomGuideUtil.showCardWelcomGuid(BaseDroidApp.getInstanse().getCurrentAct());
			queryDefaultAcc(mobilNumStr);
		}

	}

	/**
	 * 查询默认账户
	 * 
	 * @param mobileNum
	 */
	public void queryDefaultAcc(String mobileNum) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.METHOD_QUERYDEFAULTACCT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Setting.MOBILE, mobileNum);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "queryDefaultAccCallback");
	}

	@SuppressWarnings("unchecked")
	public void queryDefaultAccCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			return;
		}
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		defaultstatus = (String) resultMap.get(Tran.ACCOUNTDETAIL_STATUS_RES);
		defaultAccIdStr = String.valueOf(resultMap.get(Setting.SET_QUERYDEFAULTACCOUNT_ACCOUNTNUMBER));
		bankadapter.setDefaultAccIdStr(defaultAccIdStr, defaultstatus);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		//查收默认账户错误不拦截
		if(Setting.METHOD_QUERYDEFAULTACCT.equals(biiResponseBody.getMethod())){
			BiiHttpEngine.dissMissProgressDialog();
			return true;
		}
		//工资卡查询接口异常处理
		if (Acc.ACC_PSNISPAYROLLACCOUNT.equals(biiResponseBody.getMethod())) {
			//数据返回异常
			if (biiResponse.isBiiexception()) {
				//因为不弹出错误提示框，只是不显示工资卡查询，所以错误的数据异常，让他是正常的
				biiResponse.setBiiexception(false);
				//没有错误数据区
				biiResponseBody.setError(null);
				//有正常的数据状态
				biiResponseBody.setStatus(ConstantGloble.STATUS_SUCCESS);
			}else{
				//AccDataCenter.getInstance().setIsPayrollAccount();
			}
		}
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			BiiError biiError = biiResponseBody.getError();
			// 判断是否存在error
			if (biiError != null) {
				if (biiError.getCode() != null) {
					if (biiResponseBody.getError().getCode().equals(ErrorCode.SETTING_NODEFAULTACC_ERROR)) {
						BiiHttpEngine.dissMissProgressDialog();
						return true;
					}
				}
			}
			if (Acc.PSNQUERYCUSTSIGNINVEFINASERVICE_API.equals(biiResponseBody.getMethod())) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					List<Map<String, String>> serviceList = new ArrayList<Map<String, String>>();
					String acctype = (String) chooseBankAccount.get(Acc.ACC_ACCOUNTTYPE_RES);
//					if (acctype.equals(ZHONGYIN) || acctype.equals(GREATWALL) || acctype.equals(SINGLEWAIBI)) {
//						// 信用卡
//						lvBankAccountList.setSwipeListViewListener(null);
//						AccDataCenter.getInstance().setServiceList(serviceList);
//						AccDataCenter.getInstance().setBankAccountList(bankAccountList);
//						AccDataCenter.getInstance().setChooseBankAccount(chooseBankAccount);
//						Intent intent = new Intent(this, FinanceIcDetailDialogActivity.class);
//						intent.putExtra(ConstantGloble.ACC_ISMY, currency1);
//						intent.putExtra(ConstantGloble.CRCD_FLAG, ishavecurrencytwo);
//						intent.putExtra(ConstantGloble.CRCD_CURRENCY, currency2);
//						intent.putExtra(ConstantGloble.ACC_POSITION, detailPostion);
//						startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//					} else 
						if (acctype.equals(ICCARD)) {
						// 电子现金账户
						lvBankAccountList.setSwipeListViewListener(null);
						AccDataCenter.getInstance().setServiceList(serviceList);
						AccDataCenter.getInstance().setBankAccountList(bankAccountList);
						AccDataCenter.getInstance().setCallbackmap(callbackmap);
						AccDataCenter.getInstance().setChooseBankAccount(chooseBankAccount);
						Intent intent = new Intent(this, FinanceIcDetailDialogActivity.class);
						intent.putExtra(ConstantGloble.ACC_POSITION, detailPostion);
						startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
					} else {
						if (acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_REG)) {
							// 过滤有效存单
							// 账户详情列表
							List<Map<String, Object>> accountDetaiList = (List<Map<String, Object>>) chooseBankAccount.get(ConstantGloble.ACC_DETAILIST);
							if (siftstatus(accountDetaiList)) {
								// 有存单

							} else {
								BaseDroidApp.getInstanse().showInfoMessageDialog(
										BaseDroidApp.getInstanse().getCurrentAct().getResources().getString(R.string.no_dept_cdnumber));
								return true;
							}
						}
						// 把选择的账户详情进行存储
						lvBankAccountList.setSwipeListViewListener(null);
						AccDataCenter.getInstance().setServiceList(serviceList);
						AccDataCenter.getInstance().setAccountDetailList(bankAccountList);
						AccDataCenter.getInstance().setChooseBankAccount(chooseBankAccount);
						Intent intent = new Intent(this, FinanceIcDetailDialogActivity.class);
						intent.putExtra(ConstantGloble.CRCD_FLAG, ishavebinding);
						intent.putExtra(ConstantGloble.ACC_POSITION, detailPostion);
						intent.putExtra(Acc.ACC_ACCOUNTSTATUS_RES, status);
						startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
					}
					return true;
				}
				return false;// 没有异常
			} else {
				return super.httpRequestCallBackPre(resultObj);
			}
		}
		return super.httpRequestCallBackPre(resultObj);
	}

	/**
	 * 请求账户详情
	 */
	public void requestAccBankAccountDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.DETAIL_ACC_ACCOUNTID_REQ, accountId);
		BiiHttpEngine.showProgressDialog();
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "accBankAccountDetailCallback");
	}

	/**
	 * 请求账户详情回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	@SuppressWarnings("unchecked")
	public void accBankAccountDetailCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> callbackmap = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(callbackmap)) {
			// 把账户列表与详情列表拼接
			bankAccountList.get(detailPostion).put(ConstantGloble.ACC_DETAILIST, null);
		} else {
			status = (String) callbackmap.get(Acc.ACC_ACCOUNTSTATUS_RES);
			/** 账户详情列表信息 */
			List<Map<String, Object>> accountDetailList = (List<Map<String, Object>>) (callbackmap.get(ConstantGloble.ACC_DETAILIST));
			/** 把账户列表与详情列表拼接 */
			bankAccountList.get(detailPostion).put(ConstantGloble.ACC_DETAILIST, accountDetailList);
			bankAccountList.get(detailPostion).put(Acc.ACCOPENBANK, callbackmap.get(Acc.ACCOPENBANK));
			bankAccountList.get(detailPostion).put(Acc.ACC_OPENDATE_RES, callbackmap.get(Acc.ACC_OPENDATE_RES));
			bankAccountList.get(detailPostion).put(Acc.DETAIL_ACCOUNTDETAILIST_RES, callbackmap.get(Acc.DETAIL_ACCOUNTDETAILIST_RES));
			if (accountDetailList == null || accountDetailList.size() == 0) {
			} else {
				List<String> currencylist = new ArrayList<String>();
				List<String> codelist = new ArrayList<String>();

				for (int i = 0; i < accountDetailList.size(); i++) {
					String currencyname = (String) accountDetailList.get(i).get(Acc.DETAIL_CURRENCYCODE_RES);
					// 过滤
					if (StringUtil.isNull(LocalData.currencyboci.get(currencyname))) {
						currencylist.add(LocalData.Currency.get(currencyname));
						codelist.add(currencyname);
					}
				}
				for (int i = 0; i < accCurrencyList.size(); i++) {
					for (int j = 0; j < currencylist.size(); j++) {
						if (currencylist.get(j).equals(accCurrencyList.get(i))) {
							queryCurrencyList.add(currencylist.get(j));
							queryCodeList.add(codelist.get(j));
							List<String> cashRemitList = new ArrayList<String>();
							List<String> cashRemitCodeList = new ArrayList<String>();
							for (int k = 0; k < accountDetailList.size(); k++) {
								if (currencylist.get(j).equals(LocalData.Currency.get(accountDetailList.get(k).get(Acc.DETAIL_CURRENCYCODE_RES)))) {
									String cash = (String) accountDetailList.get(k).get(Acc.DETAIL_CASHREMIT_RES);
									cashRemitCodeList.add(cash);
									cashRemitList.add(LocalData.cashMapValue.get(cash));
								}
							}
							queryCashRemitList.add(cashRemitList);
							queryCashRemitCodeList.add(cashRemitCodeList);
							break;
						}
					}
				}
			}
		}
		if (actionid == 1) {
			// 请求系统时间
			requestSystemDateTime();
		} else if (actionid == 2) {
//			Map<String, Object> transOut = bankAccountList.get(detailPostion);
			Map<String, Object> transIn = bankAccountList.get(inposition);
			chooseBankAccount = bankAccountList.get(inposition);
//			String transOutType = (String) transOut.get(Acc.ACC_ACCOUNTTYPE_RES);
			String transInType = (String) transIn.get(Acc.ACC_ACCOUNTTYPE_RES);
			if (transInType.equals(ConstantGloble.GREATWALL)
					|| transInType.equals(ConstantGloble.ZHONGYIN)
					|| transInType.equals(ConstantGloble.SINGLEWAIBI)) {
				// 如果转入账户是信用卡，需要请求币种和信用卡购汇还款信息
				Map<String, Object> params = new HashMap<String, Object>();
				// 查询信用卡详情，默认选择全部币种
				params.put(Crcd.CRCD_ACCOUNTID_RES, bankAccountList.get(inposition).get(Crcd.CRCD_ACCOUNTID_RES));
				requestHttp(Crcd.CRCD_ACCOUNTDETAIL_API, "requestInCrcdDetailCallBack", params, false);
//				psnCrcdCurrencyQuery();
			} else {
				BiiHttpEngine.dissMissProgressDialog();
				AccDataCenter.getInstance().setQueryCashRemitCodeList(queryCashRemitCodeList);
				AccDataCenter.getInstance().setQueryCashRemitList(queryCashRemitList);
				AccDataCenter.getInstance().setQueryCodeList(queryCodeList);
				AccDataCenter.getInstance().setQueryCurrencyList(queryCurrencyList);
				AccDataCenter.getInstance().setBankAccountList(bankAccountList);
				Intent intent = new Intent(AccManageActivity.this, AccRelTransManagerActivity.class);
				intent.putExtra(ConstantGloble.ACC_POSITION, detailPostion);
				intent.putExtra(ConstantGloble.ACC_ISMY, inposition);
				startActivityForResult(intent, 2);
			}
		} else {
			chooseBankAccount = bankAccountList.get(detailPostion);
			String accountId = (String) chooseBankAccount.get(Acc.ACC_ACCOUNTID_RES);
			requestPsnQueryCustSignInveFinaService(accountId);
		}
	}

	/**
	 * 请求系统时间回调
	 */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNull(dateTime)) {
			return;
		}
		String acctype = (String) bankAccountList.get(detailPostion).get(Acc.ACC_ACCOUNTTYPE_RES);
		String accountId = (String) bankAccountList.get(detailPostion).get(Acc.ACC_ACCOUNTID_RES);
		if (acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_EDU) || acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_ZOR)) {
			combinListData();
			AccDataCenter.getInstance().setCommCardList(cardList);
			int eduposition = getposition(acctype, accountId);
			// 教育储蓄、零存整取
			queryCurrencyList.add(ConstantGloble.ACC_RMB);
			queryCodeList.add(LocalData.queryCurrencyMap.get(ConstantGloble.ACC_RMB));
			lvBankAccountList.setSwipeListViewListener(null);
			AccDataCenter.getInstance().setQueryCodeList(queryCodeList);
			AccDataCenter.getInstance().setQueryCurrencyList(queryCurrencyList);
			AccDataCenter.getInstance().setBankAccountList(bankAccountList);
			Intent intent = new Intent(AccManageActivity.this, AccountTransferDetailActivity.class);
			intent.putExtra(ConstantGloble.ACC_POSITION, eduposition);
			intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
		} else if (acctype.equals(ConstantGloble.ICCARD)) {
			click();
		} else {
			combinListData();
			AccDataCenter.getInstance().setCommCardList(cardList);
			int cardPosition = getposition(acctype, accountId);
			lvBankAccountList.setSwipeListViewListener(null);
			AccDataCenter.getInstance().setQueryCashRemitCodeList(queryCashRemitCodeList);
			AccDataCenter.getInstance().setQueryCashRemitList(queryCashRemitList);
			AccDataCenter.getInstance().setQueryCodeList(queryCodeList);
			AccDataCenter.getInstance().setQueryCurrencyList(queryCurrencyList);
			AccDataCenter.getInstance().setBankAccountList(bankAccountList);
			Intent intent = new Intent(AccManageActivity.this, AccountTransferDetailActivity.class);
			intent.putExtra(ConstantGloble.ACC_POSITION, cardPosition);
			intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
		}
	}

	/**
	 * 查询投资理财服务
	 */
	public void requestPsnQueryCustSignInveFinaService(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.PSNQUERYCUSTSIGNINVEFINASERVICE_API);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Acc.QUERY_ACCOUNTID_REQ, accountId);
		map.put(Acc.QUERY_QUERYTYPE_REQ, QUERYTYPE);
		biiRequestBody.setParams(map);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnQueryCustSignInveFinaServiceCallback");
	}

	@SuppressWarnings("unchecked")
	public void requestPsnQueryCustSignInveFinaServiceCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiHttpEngine.dissMissProgressDialog();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> serviceMap = (Map<String, Object>) biiResponseBody.getResult();
		List<Map<String, String>> serviceList = (List<Map<String, String>>) serviceMap.get(Acc.QUERY_OPENLIST_RES);
		String acctype = (String) chooseBankAccount.get(Acc.ACC_ACCOUNTTYPE_RES);
//		if (acctype.equals(ZHONGYIN) || acctype.equals(GREATWALL) || acctype.equals(SINGLEWAIBI)) {
//			// 信用卡
//			lvBankAccountList.setSwipeListViewListener(null);
//			AccDataCenter.getInstance().setServiceList(serviceList);
//			AccDataCenter.getInstance().setBankAccountList(bankAccountList);
//			AccDataCenter.getInstance().setChooseBankAccount(chooseBankAccount);
//			Intent intent = new Intent(this, FinanceIcDetailDialogActivity.class);
//			intent.putExtra(ConstantGloble.ACC_ISMY, currency1);
//			intent.putExtra(ConstantGloble.CRCD_FLAG, ishavecurrencytwo);
//			intent.putExtra(ConstantGloble.CRCD_CURRENCY, currency2);
//			intent.putExtra(ConstantGloble.ACC_POSITION, detailPostion);
//			startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//		} else 
			if (acctype.equals(ICCARD)) {
			// 电子现金账户
			lvBankAccountList.setSwipeListViewListener(null);
			AccDataCenter.getInstance().setServiceList(serviceList);
			AccDataCenter.getInstance().setBankAccountList(bankAccountList);
			AccDataCenter.getInstance().setCallbackmap(callbackmap);
			AccDataCenter.getInstance().setChooseBankAccount(chooseBankAccount);
			Intent intent = new Intent(this, FinanceIcDetailDialogActivity.class);
			intent.putExtra(ConstantGloble.ACC_POSITION, detailPostion);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
		} else {
			if (acctype.equalsIgnoreCase(ConstantGloble.ACC_TYPE_REG)) {
				// 过滤有效存单
				// 账户详情列表
				List<Map<String, Object>> accountDetaiList = (List<Map<String, Object>>) chooseBankAccount.get(ConstantGloble.ACC_DETAILIST);
				if (siftstatus(accountDetaiList)) {
					// 有存单
				} else {
					BaseDroidApp.getInstanse().showInfoMessageDialog(BaseDroidApp.getInstanse().getCurrentAct().getResources().getString(R.string.no_dept_cdnumber));
					return;
				}
			}
			// 把选择的账户详情进行存储
			lvBankAccountList.setSwipeListViewListener(null);
			AccDataCenter.getInstance().setServiceList(serviceList);
			AccDataCenter.getInstance().setAccountDetailList(bankAccountList);
			AccDataCenter.getInstance().setChooseBankAccount(chooseBankAccount);
			Intent intent = new Intent(this, FinanceIcDetailDialogActivity.class);
			intent.putExtra(ConstantGloble.CRCD_FLAG, ishavebinding);
			intent.putExtra(ConstantGloble.ACC_POSITION, detailPostion);
			intent.putExtra(Acc.ACC_ACCOUNTSTATUS_RES, status);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
		}
	}
	
	@Override
	public void requestFinanceIcAccountListCallBack(Object resultObj) {
		super.requestFinanceIcAccountListCallBack(resultObj);
		financeIcAccountList = AccDataCenter.getInstance().getFinanceIcAccountList();
		requestSystemDateTime();
	}
	
	/**
	 * 请求conversationid回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 获取TokenId
		pSNGetTokenId();
	}

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenId(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		// 请求取消关联
		requestCancleAccRelation();
	}

	/** 请求取消关联信息 */
	public void requestCancleAccRelation() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_CANCELACCRELATION_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.CANCELRELATION_ACC_ACCOUNTID, (String) (bankAccountList.get(position).get(Acc.ACC_ACCOUNTID_RES)));
		paramsmap.put(Acc.CANCELRELATION_ACC_ACCOUNTNUMBER, (String) (bankAccountList.get(position).get(Acc.ACC_ACCOUNTNUMBER_RES)));
		paramsmap.put(Acc.CANCELRELATION_ACC_TOKEN, tokenId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "cancleAccRelationCallBack");
	}

	/**
	 * 请求取消关联列表回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void cancleAccRelationCallBack(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		String result = (String) biiResponseBody.getResult();
		// // 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		CustomDialog.toastShow(this, this.getString(R.string.acc_cancelrelation_success));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_main_right_btn));
		click = true;
		bankAccountList.remove(position);
		bankadapter = new ACCBankAccountAdapter(AccManageActivity.this, bankAccountList, 1);
		lvBankAccountList.setLastPositionClickable(false);
		lvBankAccountList.setAllPositionClickable(true);
		lvBankAccountList.setAdapter(bankadapter);
		bankadapter.setDefaultAccIdStr(defaultAccIdStr, defaultstatus);
		// 账户详情查询监听事件
		bankadapter.setOnbanklistItemDetailClickListener(onbanklistItemDetailClickListener);
		lvBankAccountList.setSwipeListViewListener(swipeListViewListener);
		if (bankAccountList.size() > 1) {
			// 右上角按钮赋值
			setText(this.getString(R.string.acc_main_right_btn));
			// 右上角按钮点击事件
			setRightBtnClick(rightBtnClick);
			lvBankAccountList.setCanDrag(true);
			lvBankAccountList.setDropViewListener(new DropViewListener() {

				@Override
				public void drop(int from, int to) {
					relTrans(from, to);
				}
			});
		} else {
			gonerightBtn();
		}
	}

	/** 发送请求判断是否需要工资单查询 */
	public void requestPsnIsPayrollAccount() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_PSNISPAYROLLACCOUNT);
		// String accountId =
		// String.valueOf(chooseBankAccount.get(Acc.ACC_ACCOUNTID_RES));
		// Map<String, String> map = new HashMap<String, String>();
		// map.put(Acc.ACC_ACCOUNTID, accountId);
		// biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnIsPayrollAccountCallBack");
	}

	/** 判断是否需要工资单查询回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnIsPayrollAccountCallBack(Object resultObj) {

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> isPayrollDetail = (List<Map<String, Object>>) biiResponseBody.getResult();
//		LogGloble.e("====isPayrollDetail", isPayrollDetail.toString());
//		if (StringUtil.isNullOrEmpty(isPayrollDetail)) {
//			return;
//		}

		String acctype = (String) bankAccountList.get(detailPostion).get(Acc.ACC_ACCOUNTTYPE_RES);
		// 信用卡
		chooseBankAccount = bankAccountList.get(detailPostion);
		String accountId = (String) chooseBankAccount.get(Acc.ACC_ACCOUNTID_RES);
		LogGloble.i("accountId==xpj", accountId);
		AccDataCenter.getInstance().setIsPayrollAccount(false);
		if (!StringUtil.isNullOrEmpty(isPayrollDetail)) {
			LogGloble.e("====isPayrollDetail", isPayrollDetail.toString());
			for (int i = 0; i < isPayrollDetail.size(); i++) {
				String isPayrollAccountId = (String) isPayrollDetail.get(i).get(Acc.ACC_ACCOUNTID);
				LogGloble.i("isPayrollAccountId==xpj", isPayrollAccountId);
//				Boolean payrollAccountFlag = Boolean
//						.valueOf((String) isPayrollDetail.get(i).get(
//								Acc.ACC_PAYROLLACCOUNTFLAG));
//				LogGloble.i("payrollAccountFlag==xpj",
//						payrollAccountFlag.toString());
				String ispayrollAcctype = (String) isPayrollDetail.get(i).get(Acc.ACC_ACCOUNTTYPE);
				LogGloble.i("ispayrollAcctype==xpj", ispayrollAcctype);
				if (accountId.equals(isPayrollAccountId) && acctype.equals(ispayrollAcctype)) {
					AccDataCenter.getInstance().setIsPayrollAccount(true);
					LogGloble.i("payrollAccountFlag///if==xpj", String.valueOf(AccDataCenter.getInstance().getIsPayrollAccount()));
					break;
				}
			}
		}
		if (acctype.equals(ZHONGYIN) || acctype.equals(GREATWALL) || acctype.equals(SINGLEWAIBI)) {
			// 查询信用卡币种
//			psnCrcdCurrencyQuery();
			
			requestPsnCrcdQueryGeneralInfo(accountId);
		} else if (acctype.equals(ICCARD)) {
			// 电子现金账户
			requestDetail(accountId);
		} else if (acctype.equals(ConstantGloble.ACC_TYPE_XNCRCD1) || acctype.equals(ConstantGloble.ACC_TYPE_XNCRCD2)) {
			// 虚拟信用卡
			Intent intent = new Intent(AccManageActivity.this, MyVirtualBCListActivity.class);
			startActivityForResult(intent, 6);
		} else {
			// 请求余额
			if (!StringUtil.isNullOrEmpty(AccDataCenter.getInstance().getVolumesAndCdnumbers())) {
				AccDataCenter.getInstance().getVolumesAndCdnumbers().clear();
			}
			requestAccBankAccountDetail(String.valueOf(bankAccountList.get(detailPostion).get(Acc.ACC_ACCOUNTID_RES)));
		}
	}

	/** 请求电子现金账户余额以及详细信息 */
	public void requestDetail(String accountId) {
		BiiRequestBody biiRequestBody1 = new BiiRequestBody();
		biiRequestBody1.setMethod(Acc.ACC_ICACCOUNTDETAIL_API);
		Map<String, String> paramsmap1 = new HashMap<String, String>();
		paramsmap1.put(Acc.FINANCE_ACCOUNTID_REQ, accountId);
		biiRequestBody1.setParams(paramsmap1);
		// 通讯开始,开启通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody1, this, "requestDetailCallBack");
	}

	/**
	 * 请求电子现金账户余额以及详细信息回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		callbackmap = (Map<String, String>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(callbackmap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		String accountId = (String) chooseBankAccount.get(Acc.ACC_ACCOUNTID_RES);
		requestPsnQueryCustSignInveFinaService(accountId);
	}

	/** 请求信用卡币种 */
	public void psnCrcdCurrencyQuery() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDCURRENCYQUERY);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTNUMBER_RES, (String) (chooseBankAccount.get(Acc.ACC_ACCOUNTNUMBER_RES)));
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdCurrencyQueryCallBack");
	}

	/** 请求查询信用卡币种回调 */
	@SuppressWarnings("unchecked")
	public void psnCrcdCurrencyQueryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		currencyMap1 = (Map<String, Object>) resultMap.get(Crcd.CRCD_CURRENCY1);
		currencyMap2 = (Map<String, Object>) resultMap.get(Crcd.CRCD_CURRENCY2);

		if (!StringUtil.isNullOrEmpty(currencyMap1)) {
			currency1 = String.valueOf(currencyMap1.get(Crcd.CRCD_CODE));
			queryCurrencyList.add(LocalData.Currency.get(currency1));
			queryCodeList.add(currency1);
			queryCashRemitList.add(LocalData.cashremitList);
			queryCashRemitCodeList.add(cashNullList);
		}
		if (!StringUtil.isNullOrEmpty(currencyMap2)) {
			currency2 = String.valueOf(currencyMap2.get(Crcd.CRCD_CODE));
			queryCurrencyList.add(LocalData.Currency.get(currency2));
			queryCodeList.add(currency2);
			queryCashRemitList.add(LocalData.cashremitList);
			queryCashRemitCodeList.add(cashNullList);
		}
		if (!StringUtil.isNullOrEmpty(currencyMap2) && !StringUtil.isNullOrEmpty(currencyMap1)) {
			ishavecurrencytwo = true;
		} else {
			ishavecurrencytwo = false;
		}
		if (actionid == 1) {
			BiiHttpEngine.dissMissProgressDialog();
			lvBankAccountList.setSwipeListViewListener(null);
			AccDataCenter.getInstance().setQueryCashRemitCodeList(queryCashRemitCodeList);
			AccDataCenter.getInstance().setQueryCashRemitList(queryCashRemitList);
			AccDataCenter.getInstance().setQueryCodeList(queryCodeList);
			AccDataCenter.getInstance().setQueryCurrencyList(queryCurrencyList);
			AccDataCenter.getInstance().setBankAccountList(bankAccountList);
			Intent intent = new Intent(AccManageActivity.this, AccountTransferDetailActivity.class);
			intent.putExtra(ConstantGloble.ACC_POSITION, detailPostion);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
		} else if (actionid == 2) {
			BiiHttpEngine.dissMissProgressDialog();
			// 信用卡还款
			lvBankAccountList.setSwipeListViewListener(null);
			AccDataCenter.getInstance().setQueryCashRemitCodeList(queryCashRemitCodeList);
			AccDataCenter.getInstance().setQueryCashRemitList(queryCashRemitList);
			AccDataCenter.getInstance().setQueryCodeList(queryCodeList);
			AccDataCenter.getInstance().setQueryCurrencyList(queryCurrencyList);
			AccDataCenter.getInstance().setBankAccountList(bankAccountList);
			Intent intent = new Intent(AccManageActivity.this, AccCrcdTransManagerActivity.class);
			intent.putExtra(ConstantGloble.ACC_CRCD_CURRENCY1, currency1);
			intent.putExtra(ConstantGloble.CRCD_FLAG, ishavecurrencytwo);
			intent.putExtra(ConstantGloble.ACC_CRCD_CURRENCY2, currency2);
			intent.putExtra(ConstantGloble.ACC_POSITION, detailPostion);
			intent.putExtra(ConstantGloble.ACC_ISMY, inposition);
			startActivityForResult(intent, 7);
		} else {
			// 先查币种,再查详情
			if (!StringUtil.isNull(currency1)) {
				requestPsnCrcdQueryAccountDetail(chooseBankAccount, currency1);
			} else {
				requestPsnCrcdQueryAccountDetail(chooseBankAccount, currency2);
			}
		}
	}

	/** 请求查询信用卡详情 */
	public void requestPsnCrcdQueryAccountDetail(Map<String, Object> map, String value) {
		if (isShowDialog) {
			BaseHttpEngine.showProgressDialog();
		}
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_ACCOUNTDETAIL_API);
		String accountId = (String) map.get(Crcd.CRCD_ACCOUNTID_RES);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		paramsmap.put(Crcd.CRCD_CURRENCY, value);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdQueryAccountDetailCallBack");
	}

	/** 请求查询信用卡详情回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnCrcdQueryAccountDetailCallBack(Object resultObj) {
		isShowDialog = false;
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultDetail = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultDetail)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
//		Map<String, String> detailMap = new HashMap<String, String>();
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
		detailList = (List<Map<String, String>>) resultDetail.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		if (StringUtil.isNullOrEmpty(detailList)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
//		detailMap = detailList.get(0);
//		if (StringUtil.isNullOrEmpty(detailMap)) {
//			BiiHttpEngine.dissMissProgressDialog();
//			return;
//		}
		AccDataCenter.getInstance().setResultDetail(resultDetail);
		System.out.println("requestPsnCrcdQueryAccountDetailCallBack信用卡详情数据-->" + resultDetail);
		String accountId = (String) chooseBankAccount.get(Acc.ACC_ACCOUNTID_RES);
		if (actionid == 2) {
//			Map<String, Object> transOut = bankAccountList.get(detailPostion);
			Map<String, Object> transIn = bankAccountList.get(inposition);
			chooseBankAccount = bankAccountList.get(inposition);
//			String transOutType = (String) transOut.get(Acc.ACC_ACCOUNTTYPE_RES);
			String transInType = (String) transIn.get(Acc.ACC_ACCOUNTTYPE_RES);
			if (transInType.equals(ConstantGloble.GREATWALL)
					|| transInType.equals(ConstantGloble.ZHONGYIN)
					|| transInType.equals(ConstantGloble.SINGLEWAIBI)) {
				// 如果转入账户是信用卡，需要请求币种和信用卡购汇还款信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(Crcd.CRCD_ACCOUNTID_RES, bankAccountList.get(inposition).get(Crcd.CRCD_ACCOUNTID_RES));
				params.put(Crcd.CRCD_CURRENCY, null);
				requestHttp(Crcd.CRCD_ACCOUNTDETAIL_API, "requestInCrcdDetailCallBack", params, false);
//				psnCrcdCurrencyQuery();
			} else {
				
				requestAccBankAccountDetail((String) transIn.get(Acc.ACC_ACCOUNTID_RES));

				
//				BiiHttpEngine.dissMissProgressDialog();
//				AccDataCenter.getInstance().setBankAccountList(bankAccountList);
//				Intent intent = new Intent(AccManageActivity.this, AccRelTransManagerActivity.class);
//				intent.putExtra(ConstantGloble.ACC_POSITION, detailPostion);
//				intent.putExtra(ConstantGloble.ACC_ISMY, inposition);
//				startActivityForResult(intent, 2);
			}
		} else {
			requestPsnQueryCustSignInveFinaService(accountId);
		}

	}

	/** 转信用卡时请求转入信用卡详情回调 */
	@SuppressWarnings("unchecked")
	public void requestInCrcdDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultDetail = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultDetail)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
		detailList = (List<Map<String, String>>) resultDetail.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		if (StringUtil.isNullOrEmpty(detailList)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		bankAccountList.get(inposition).put(Crcd.CRCD_CRCDACCOUNTDETAILLIST, resultDetail);
		// 如果转入账户是信用卡，还需要请求币种和信用卡购汇还款信息
		psnCrcdCurrencyQuery();
	}
	
	/** 信用卡综合信息查询 */
	private void requestPsnCrcdQueryGeneralInfo(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYGENERALINFO);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdQueryGeneralInfoCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void requestPsnCrcdQueryGeneralInfoCallBack(Object resultObj) {
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(result)) {			
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		
		TranDataCenter.getInstance().setCrcdGeneralInfo(result);
		AccDataCenter.getInstance().setChooseBankAccount(chooseBankAccount);
		lvBankAccountList.setSwipeListViewListener(null);
		Intent intent = new Intent(this, MyCardDetailDoalogActivity.class);
		intent.putExtra(ConstantGloble.ACC_POSITION, detailPostion);
		startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
	}
}
