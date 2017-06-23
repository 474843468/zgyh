package com.chinamworld.bocmbci.biz.dept.myreg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.adapter.AccAndDetailListAdapter;
import com.chinamworld.bocmbci.biz.investTask.BocInvestTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 我的定期存款  选择账户页面
 * 
 * @author 修改 luqp 2015年8月13日10:11:30
 */

public class MyRegSaveActivity extends DeptBaseActivity {

	private static final String TAG = "MyRegSaveActivity";

	private Context context = this;

	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;
	private ListView accListView = null;
	/** 当前选中卡位置 */
	private int currentPosition = 0;
	/** 账户id */
	private String accountId;
	/** 账户别名 */
	private String nickName;
	/** 到期日 */
	private String endDate;
	/** 修改后的账户别名 */
	private String strNickName;
	// 通知编号list
	List<String> noticeIdList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.my_reg_save);

		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载理财产品布局
		view = inflater.inflate(R.layout.dept_list_only_click, null);
		tabcontent.addView(view);
		newTranBtn = (Button) this.findViewById(R.id.ib_top_right_btn);
		newTranBtn.setVisibility(View.GONE);
		// add by xby
		((Button) findViewById(R.id.ib_top_right_btn)).setVisibility(View.GONE);
		setLeftSelectedPosition("deptStorageCash_2");
		
		// add lqp 2015年12月14日11:57:02 判断是否开通投资理财!
		BocInvestTask task = BocInvestTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {

			@Override
			public void SuccessCallBack(Object param) {
				// 请求账户列表
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestQueryMyRegAccountList();
			}
		},null);
		
		// 修改判断是否开通投资理财!
		// 请求账户列表
//		BaseHttpEngine.showProgressDialogCanGoBack();
//		requestQueryMyRegAccountList();
//		setLeftSelectedPosition(MY_REG_SAVE);
	}

	/** 账户详情监听*/
	private OnItemClickListener listItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 查询查询帐户详情PsnAccountQueryAccountDetail
			currentPosition = position;
			accountId = (String) DeptDataCenter.getInstance().getMyRegAccountList().get(position).get(Comm.ACCOUNT_ID);
			nickName = (String) DeptDataCenter.getInstance().getMyRegAccountList().get(position).get(Comm.NICKNAME);
			requestQueryAccountDetail(accountId);
			// Intent intent = new Intent();
			// intent.setClass(MyRegSaveActivity.this,
			// MyRegSaveConfirmActivity.class);
			// startActivity(intent);
			// 查询账户详情 返回有一个accountDetaiList 如果用户有多个存折号和存单号 list就有多项
			// 点击图标的时候 默认展现的是list里面的第一项
		}
	};

	/** 建立通知按钮 监听*/
	private OnClickListener onCreateNoticeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			String availableBalance = (String) DeptDataCenter.getInstance().getCurDetailContent()
					.get(Dept.AVAILABLE_BALANCE);

			double balanceTemp = Double.valueOf(availableBalance);
			// 判断是否有可用余额
			if (balanceTemp <= 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("当前存单没有可用的支取余额，请重新选择");
				return;
			}

			Intent intent = new Intent();
			intent.setClass(context, CreateNoticeActivity.class);
			startActivity(intent);
			BaseDroidApp.getInstanse().dismissMessageDialog();// 关闭详情框
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	};

	/** 支取按钮 监听*/
	private OnClickListener onCheckoutListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String availableBalance = (String) DeptDataCenter.getInstance().getCurDetailContent()
					.get(Dept.AVAILABLE_BALANCE);

			double balanceTemp = Double.valueOf(availableBalance);
			// 判断是否有可用余额
			if (balanceTemp <= 0) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("当前存单没有可用的支取余额，请重新选择");
				return;
			}
			// 查询系统时间
			BiiHttpEngine.showProgressDialog();
			requestSystemDateTime();

		}
	};

	/** 续存按钮 监听 教育储蓄和零存整取*/
	private OnClickListener onContinueSaveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra(ConstantGloble.CONTINUE_SAVE_FLAG, "Y");// 续存
			intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			intent.setClass(context, MyRegSaveChooseTranInAccActivity.class);
			startActivity(intent);
			BaseDroidApp.getInstanse().dismissMessageDialog();// 关闭详情框
		}
	};

	/** 修改账户别名 监听*/
	private OnClickListener onModifyNickNameListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			strNickName = BaseDroidApp.getInstanse().getMessageDialog().getNickNameEt().getText().toString().trim();
			// 判断账户别名
			RegexpBean regNickName = new RegexpBean(
					MyRegSaveActivity.this.getResources().getString(R.string.nickname_regex), strNickName, "nickname");
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			lists.add(regNickName);
			if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
				return;
			}

			if (nickName.equals(strNickName)) {
				// 隐藏键盘
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(BaseDroidApp.getInstanse().getMessageDialog().getNickNameEt().getWindowToken(),
						0);
				BaseDroidApp.getInstanse().getMessageDialog().refreshModifyNickName();
			} else {
				requestCommConversationId();
				BaseHttpEngine.showProgressDialog();
			}
		}
	};

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}

	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		String token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestModifyAccountAlias(accountId, strNickName, token);
	}

	/** 通讯返回*/
	@Override
	public void communicationCallBack(int flag) {
		super.communicationCallBack(flag);
		switch (flag) {
		case QUERY_ALL_ACCOUNT_CALLBACK:// 查询所有账户返回 刷新界面
			accListView = (ListView) view.findViewById(R.id.dept_list_view);
			List<Map<String, Object>> regList = DeptDataCenter.getInstance().getMyRegAccountList();
			if (StringUtil.isNullOrEmpty(regList)) {
				// BaseDroidApp.getInstanse().showErrorDialog(null,
				// "没有查询到符合储蓄类型的存款记录，是否立即关联新储蓄账户？", new View.OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// BaseDroidApp.getInstanse().dismissErrorDialog();
				// switch (EpayUtil.getInt(v.getTag(), 0)) {
				// case CustomDialog.TAG_SURE:
				// goRelevanceAccount();
				// break;
				// }
				// finish();
				// }
				// });

				BaseDroidApp.getInstanse().showMessageDialog(this.getResources().getString(R.string.query_no_result),
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
			AccAndDetailListAdapter adapter = new AccAndDetailListAdapter(context, regList);
			accListView.setAdapter(adapter);
			accListView.setOnItemClickListener(listItemClickListener);
			break;
		case QUERY_ACCOUNT_DETAIL_CALLBACK:// 查询定期存单详情返回 刷新界面
			// 根据存储类型显示不同的界面
			Map<String, Object> accountContent = DeptDataCenter.getInstance().getMyRegAccountList().get(currentPosition);
			Map<String, Object> map = DeptDataCenter.getInstance().getAccountDetailCallBackMap();
			List<Map<String, Object>> accountDetaiList = (List<Map<String, Object>>) DeptDataCenter.getInstance()
					.getAccountDetailCallBackMap().get(ConstantGloble.ACC_DETAILIST);

			// 如果用户选择的账户类型 教育储蓄 教育储蓄 定期一本通
			if (!accountContent.get(Comm.ACCOUNT_TYPE).equals(DeptBaseActivity.EDUCATION_SAVE1)
					&& !accountContent.get(Comm.ACCOUNT_TYPE).equals(DeptBaseActivity.ZERO_SAVE1)
					&& !accountContent.get(Comm.ACCOUNT_TYPE).equals(DeptBaseActivity.RANDOM_ONE_SAVE)) {
				// 有效存单的个数
				if (accountDetaiList != null && !accountDetaiList.isEmpty()) {

					int m = 0;
					for (int j = 0; j < accountDetaiList.size(); j++) {
						Map<String, Object> detaimap = accountDetaiList.get(j);


						if (((String) detaimap.get(Dept.STATUS)).equals("V")
								|| ((String) detaimap.get(Dept.STATUS)).equals("00")) {// 过滤无效存单

							m++;
						}
					}
					if (m == 0) {
						BaseDroidApp.getInstanse().showInfoMessageDialog(
								this.getResources().getString(R.string.no_dept_status));
						return;
					} else {
						BaseDroidApp.getInstanse()
								.showMyRegSaveAccDetailMessageDialog(currentPosition, onCreateNoticeListener,
										onCheckoutListener, onContinueSaveListener, onModifyNickNameListener);
					}
				} else {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							this.getResources().getString(R.string.no_dept_status_receipt));
					return;
				}

			} else {
				if (!accountContent.get(Comm.ACCOUNT_TYPE).equals(DeptBaseActivity.EDUCATION_SAVE1)
						&& !accountContent.get(Comm.ACCOUNT_TYPE).equals(DeptBaseActivity.ZERO_SAVE1)) {
					// 将数据放入数据中心
					DeptDataCenter.getInstance().setWholesaveAccountList(accountDetaiList);
					Intent intent = new Intent();
					intent.setClass(context, MyPositMneyListActivity.class);
					intent.putExtra(DeptBaseActivity.DEPT_SELECTEDPOSITION, currentPosition);
					startActivity(intent);
					BaseDroidApp.getInstanse().dismissMessageDialog();
				} else {
					BaseDroidApp.getInstanse().showMyRegSaveAccDetailMessageDialog(currentPosition, onCreateNoticeListener,
							onCheckoutListener, onContinueSaveListener, onModifyNickNameListener);
				}

			}

			break;
		case QUERY_NOTIFY_ACCOUNT_DETAIL_CALLBACK:// 支取通知存款 查询通知详情 返回
			List<Map<String, Object>> noticeDetailList = DeptDataCenter.getInstance().getQueryNotifyCallBackList();
			// 通知编号list
			noticeIdList = new ArrayList<String>();
			for (int i = 0; i < noticeDetailList.size(); i++) {
				String noticeNo = (String) noticeDetailList.get(i).get(Dept.NOTIFY_ID);
				if (!StringUtil.isNullOrEmpty(noticeNo)) { // 判断通知编号是否为空
					noticeIdList.add(noticeNo);
					// String temp_drawAmount = (String)
					// noticeDetailList.get(i).get(Dept.DRAW_AMOUNT); //支取金额
					// if(StringUtil.isNullOrEmpty(temp_drawAmount))
					// //判断支取金额是否为空
					// continue;
					// if(Double.valueOf(temp_drawAmount) > 0) //判断支取金额是否大于0
					// noticeIdList.add(noticeNo);
				}
			}
			DeptDataCenter.getInstance().setNoticeIdList(noticeIdList);
			// 非约定转存 首先判断是否有无通知编号 如果没有通知编号 违约支取
			String strConvertType = (String) DeptDataCenter.getInstance().getCurDetailContent()
					.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
			if (strConvertType.equals(ConstantGloble.CONVERTTYPE_N)) {// 如果是非约定
				// 判断有无通知
				if (StringUtil.isNullOrEmpty(noticeIdList)) {// 没有通知编号
					BaseDroidApp.getInstanse().showErrorDialog(this.getResources().getString(R.string.no_notify),
							R.string.cancle, R.string.confirm, onclicklistener);
					break;
				} else {// 有通知编号 判断支取日期
					Intent intent = new Intent();
					intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
					intent.setClass(context, MyRegSaveChooseTranInAccActivity.class);
					startActivity(intent);
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			} else {// 约定转存
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(context, MyRegSaveChooseTranInAccActivity.class);
				startActivity(intent);
				BaseDroidApp.getInstanse().dismissMessageDialog();
			}
			// 约定支取日期
			// String drawDate = (String) noticeDetail.get(Dept.DRAW_DATE);
			// boolean noticeFlag = QueryDateUtils.compareDate(dateTime,
			// drawDate);
			// if (!noticeFlag) {
			// // TODO 提醒信息带确认
			// BaseDroidApp.getInstanse().showErrorDialog(
			// this.getResources().getString(
			// R.string.checkout_notify_message),
			// R.string.cancle, R.string.confirm, onclicklistener);
			// }
			break;
		case MODIFY_NICKNAME_CALLBACK:// 修改账户别名
			// 用ui线程去刷新dialog
			MyRegSaveActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// 隐藏键盘
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(BaseDroidApp.getInstanse().getMessageDialog().getNickNameEt()
							.getWindowToken(), 0);
					BaseDroidApp.getInstanse().getMessageDialog().refreshModifyNickName();
				}
			});

			CustomDialog.toastShow(this, this.getString(R.string.acc_modifyAccountAlias));
			nickName = BaseDroidApp.getInstanse().getMessageDialog().getNickNameEt().getText().toString().trim();
			break;
		default:
			break;
		}
	}

	/** 查询系统时间返回 继续支取操作*/
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		Map<String, Object> content = DeptDataCenter.getInstance().getCurDetailContent();
		endDate = (String) content.get(Dept.INTEREST_ENDDATE);

//		List<Map<String, Object>> noticeDetailList = DeptDataCenter.getInstance().getQueryNotifyCallBackList();
//		// 通知编号list
//		noticeIdList = new ArrayList<String>();
//		for (int i = 0; i < noticeDetailList.size(); i++) {
//			String noticeNo = (String) noticeDetailList.get(i).get(Dept.NOTIFY_ID);
//			if (!StringUtil.isNullOrEmpty(noticeNo)) { // 判断通知编号是否为空
//				noticeIdList.add(noticeNo);
//			}
//		}
//		DeptDataCenter.getInstance().setNoticeIdList(noticeIdList);

		if (content.get(Dept.TYPE).equals(DeptBaseActivity.NOTIFY_SAVE)) {
			BiiHttpEngine.dissMissProgressDialog();
			// 如果是通知存款 发送通讯 查询通知

			String strConvertType = (String) DeptDataCenter.getInstance().getCurDetailContent()
					.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
			if (strConvertType.equals(ConstantGloble.CONVERTTYPE_N)) { // 非约定转存
				// String volumeNumber = (String)
				// content.get(Dept.VOLUME_NUMBER);
				// String cdNumber = (String) content.get(Dept.CD_NUMBER);
				//requestQueryNotify(accountId, volumeNumber, cdNumber);
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(context, MyRegSaveChooseTranInAccActivity.class);
				startActivity(intent);
				BaseDroidApp.getInstanse().dismissMessageDialog();
			} else { // 约定转存

				BiiHttpEngine.dissMissProgressDialog();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(context, MyRegSaveChooseTranInAccActivity.class);
				startActivity(intent);
			} 
		} else {
			BiiHttpEngine.dissMissProgressDialog();
			if (!StringUtil.isNullOrEmpty(endDate)) {
				boolean noticeFlag = QueryDateUtils.compareDate(dateTime, endDate);
				if (!noticeFlag) { // 未到期提醒
					BaseDroidApp.getInstanse().showErrorDialog(
							this.getResources().getString(R.string.checkout_notify_message), R.string.cancle,
							R.string.confirm, onclicklistener);

				} else {
					Intent intent = new Intent();
					intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
					intent.setClass(context, MyRegSaveChooseTranInAccActivity.class);
					startActivity(intent);
				}
			} else { // 没有违约
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(context, MyRegSaveChooseTranInAccActivity.class);
				startActivity(intent);
			}
		}
	}

	/** 未到期提醒框 按钮监听*/
	private OnClickListener onclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_SURE: // 确定
				BaseDroidApp.getInstanse().dismissErrorDialog();
				// 如果是整存整取
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(context, MyRegSaveChooseTranInAccActivity.class);
				startActivity(intent);
				BaseDroidApp.getInstanse().dismissMessageDialog();
				break;
			case CustomDialog.TAG_CANCLE: // 取消
				BaseDroidApp.getInstanse().dismissErrorDialog();
				break;
			default:
				break;
			}
		}
	};
}
