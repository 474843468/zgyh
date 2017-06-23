package com.chinamworld.bocmbci.biz.dept.notmg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.adapter.NotMgAccListAdapter;
import com.chinamworld.bocmbci.biz.investTask.BocInvestTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.SwipeListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @ClassName: NotifyManageActivity
 * @Description: 通知管理
 * @author JiangWei
 * @date 2013-7-9 上午9:11:07
 */
public class NotifyManageActivity extends DeptBaseActivity {

	private LayoutInflater inflater = null;
	private Context context = this;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	/** 账户列表listview */
	private SwipeListView accListView = null;
	/** 当前选择list条目 */
	private int currentPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.notify_manage);
		// 包含向右滑动的listview 目的是屏蔽左侧菜单的滑动事件
		setContainsSwipeListView(true);

		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.dept_bankaccount_list, null);
		tabcontent.addView(view);
		newTranBtn = (Button) this.findViewById(R.id.ib_top_right_btn);
		newTranBtn.setVisibility(View.GONE);
		
		// add lqp 2015年12月14日11:58:43 判断是否开通投资理财!
		BocInvestTask task = BocInvestTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {

			@Override
			public void SuccessCallBack(Object param) {
				// 查询 通知管理 账户列表
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestQueryNotifyAccountList();
//				if(LocalData.deptStorageCashLeftList.size()==6){
					setLeftSelectedPosition("deptStorageCash_5");
//				}else{
//					setLeftSelectedPosition(NOTIFY_MANAGE+1);
//				}
			}
		},null);
		
		// 修改判断是否开通投资理财!
		// 查询 通知管理 账户列表
//		BaseHttpEngine.showProgressDialogCanGoBack();
//		requestQueryNotifyAccountList();
//		setLeftSelectedPosition(NOTIFY_MANAGE);
	}

	/**
	 * 通讯返回
	 */
	@Override
	public void communicationCallBack(int flag) {
		super.communicationCallBack(flag);
		switch (flag) {
		case QUERY_ALL_ACCOUNT_CALLBACK:// 查询定期存单详情返回 刷新界面
			// 根据账户类型 和 存单类型 显示不同的界面
			if (StringUtil.isNullOrEmpty(accountList)) {
				BaseDroidApp.getInstanse().showMessageDialog(this.getResources().getString(R.string.no_list_data),
						new OnClickListener() {

							@Override
							public void onClick(View v) {
//								ActivityTaskManager.getInstance().removeAllActivity();
//								Intent intent = new Intent();
//								intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
//								BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
								goToMainActivity();
							}
						});
				break;
			}
			accListView = (SwipeListView) view.findViewById(R.id.acc_accountlist);
			NotMgAccListAdapter adapter = new NotMgAccListAdapter(context, accountList);
			accListView.setLastPositionClickable(true);
			accListView.setAllPositionClickable(true);
			accListView.setAdapter(adapter);
			accListView.setSwipeListViewListener(swipeListViewListener);
			// accListView.setOnItemClickListener(new OnItemClickListener() {
			//
			// @Override
			// public void onItemClick(AdapterView<?> parent, View view,
			// int position, long id) {
			// // 保存当前点击数据
			// currentPosition = position;
			// accOutInfoMap = accountList.get(position);
			// //为了正常状态下 能跳转到支取
			// DeptDataCenter.getInstance().setAccountContent(accOutInfoMap);
			// DeptDataCenter.getInstance()
			// .setAccOutInfoMap(accOutInfoMap);
			// // 通讯 查询通知存款详情
			// String accountId = (String) accountList.get(position).get(
			// Comm.ACCOUNT_ID);
			// requestQueryNotifyAccountDetail(accountId);
			// }
			// });

			break;
		case QUERY_ACCOUNT_DETAIL_CALLBACK:// 查询通知存款详情返回
			if (checkCdNumber()) {
				Intent intent = new Intent();
				intent.putExtra(CURRENT_POSITION, currentPosition);
				intent.setClass(NotifyManageActivity.this, NotifyManageBeforeQueryActivity.class);
				startActivity(intent);
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						this.getResources().getString(R.string.slide_no_cdnumber));
				return;
			}
			break;
		default:
			break;
		}
	}

	/** 列表向右滑动事件 */
	BaseSwipeListViewListener swipeListViewListener = new BaseSwipeListViewListener() {
		@Override
		public void onOpened(int position, boolean toRight) {
		}

		@Override
		public void onClosed(int position, boolean fromRight) {
		}

		@Override
		public void onListChanged() {
		}

		@Override
		public void onMove(int position, float x) {
		}

		@Override
		public void onStartOpen(int position, int action, boolean right) {

			if (action == 0) {
				currentPosition = position;
				accOutInfoMap = accountList.get(position);
				// 为了正常状态下 能跳转到支取
				DeptDataCenter.getInstance().setAccountContent(accOutInfoMap);
				DeptDataCenter.getInstance().setAccOutInfoMap(accOutInfoMap);
				// 通讯 查询通知存款详情
				String accountId = (String) accountList.get(position).get(Comm.ACCOUNT_ID);
				requestQueryNotifyAccountDetail(accountId);
			}
		}

		@Override
		public void onStartClose(int position, boolean right) {
		}

		@Override
		public void onClickFrontView(int position) {
			// detailPosition=position;
			// bankmap = financeIcAccountList.get(position);
			// accountId = (String) bankmap.get(Acc.ACC_ACCOUNTID_RES);
			// // 取得accountid,来获得详情及余额信息
			// requestDetail(accountId);
		}

		@Override
		public void onClickBackView(int position) {
		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {

		}
	};

	/**
	 * 查看详情 返回数据 看有无非约定转存 并且有存单数据
	 */
	private boolean checkCdNumber() {
		// 如果通知存款返回详情里面 非约定存款数据 没有存单 不跳到下个页面 在本页面提示
		@SuppressWarnings("unchecked")
		List<Map<String, String>> accountDetaiList = (List<Map<String, String>>) DeptDataCenter.getInstance()
				.getCurDetailContent().get(ConstantGloble.ACC_DETAILIST);

		// 有非约定存款 并且存单序号不能空 就跳转到下个页面
		for (int i = 0; i < accountDetaiList.size(); i++) {
			Map<String, String> map = accountDetaiList.get(i);
			String convertType = map.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
			// 如果是非约定转存并且存单序号不为空
			if (!StringUtil.isNullOrEmpty(convertType) && convertType.equals(ConstantGloble.CONVERTTYPE_N)
					&& !StringUtil.isNullOrEmpty(map.get(Dept.CD_NUMBER))
					&& !StringUtil.isNullOrEmpty(map.get(Dept.TYPE))
					&& ConstantGloble.FOREX_ACCTYPE_TZCK.equals(map.get(Dept.TYPE))) {
				if (((String) map.get(Dept.STATUS)).equals("V") || ((String) map.get(Dept.STATUS)).equals("00"))
					return true;
			}

		}
		return false;
	}
}
