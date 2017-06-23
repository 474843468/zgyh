package com.chinamworld.bocmbci.biz.dept.myreg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.adapter.MyRegSaveAdapte;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 我的定期存款 存单列表页面 根据用户选择显示 有效 销户 冻结 全部
 * 
 * @author luqp 2015年9月28日9:24:41
 */
public class MyPositMneyListActivity extends DeptBaseActivity {
	private static final String TAG = "MyPositMneyListActivity";

	private Context context = this;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;

	// ///////////优惠费用试算start/////////////////////////
	/** 账户别名 */
	private String nickName;
	/** 交易类型Spinner */
	private Spinner typeSpinner;
	/** 账号详情 */
	private ListView accListView;
	/** 账号所有的信息 */
	private List<Map<String, Object>> accountDetaiList = null;
	/** 账号的状态 有效 销户 冻结 全部 */
	private MyRegSaveAdapte adapter = null;
	/** 修改后的账户别名 */
	private String strNickName;
	/** MyRegSaveActivity 选择账户页面 账户的位置 */
	private int selectedPosition = 0;

	/** 更新后数据 */
	private List<Map<String, Object>> flushList = new ArrayList<Map<String, Object>>();
	/** 到期日 */
	private String endDate;
	/** 当前用户id */
	private String accountId;
	// 通知编号list
	List<String> noticeIdList = null;

	// ///////////优惠费用试算end/////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		LayoutInflater inflater = LayoutInflater.from(this); // 加载布局
		view = inflater.inflate(R.layout.dept_my_save_detail_view, null);
		tabcontent.addView(view); // 加载布局
		setLeftSelectedPosition("deptStorageCash_2"); // 设置侧边栏

		// 得到数据中心的数据
		accountDetaiList = DeptDataCenter.getInstance().getWholesaveAccountList();
		if (StringUtil.isNullOrEmpty(accountDetaiList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}

		// 初始化控件
		init();
		initData();
	}

	/** 初始化所有控件 */
	private void init() {
		typeSpinner = (Spinner) view.findViewById(R.id.dept_bankbook_spinner);
		// 设置用户选择账户 (有效 ,销户 ,冻结 ,全部)
		setSpinnnerAdapter(typeSpinner, MyPositMneyAcc.getKeys());
		typeSpinner.setOnItemSelectedListener(listItemSpinnerClick); // 点击事件

		// 展示listView 数据
		accListView = (ListView) view.findViewById(R.id.lv_acc_list);
		adapter = new MyRegSaveAdapte(context, flushList);
		accListView.setAdapter(adapter);
		accListView.setSelection(0);
		accListView.setOnItemClickListener(listItemClickListener); // 点击事件
	}

	/** 初始化数据 */
	private void initData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}

		// MyRegSaveActivity账户行情 用户选择的位置
		selectedPosition = intent.getIntExtra(DeptBaseActivity.DEPT_SELECTEDPOSITION, -1);
		accountId = (String) DeptDataCenter.getInstance().getMyRegAccountList().get(selectedPosition).get(Comm.ACCOUNT_ID);
		if (selectedPosition == -1) {
			return;
		}
	}

	/** Spinner 监听事件 */
	private OnItemSelectedListener listItemSpinnerClick = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			String key = MyPositMneyAcc.getKeys().get(position); // 得到用户选择的key
			String value = MyPositMneyAcc.getValueFromKey(key); // 得到用户选择的value
			// 根据用户选择 显示对应的数据
			selectFlushData(accountDetaiList, value);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	/** 账户监听事件 */
	private OnItemClickListener listItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// currentPosition = position;
			String status = (String) flushList.get(position).get(Dept.STATUS);
			Map<String, Object> positionMap = flushList.get(position);
			if (status.equals("00") || status.equals("V")) { // 有效数据 显示支取按钮
				BaseDroidApp.getInstanse().showMyRegSaveAccDialog(selectedPosition, positionMap, onCreateNoticeListener,
						onCheckoutListener, onContinueSaveListener, onModifyNickNameListener);
			} else { // 无效数据不显示支取按钮
				BaseDroidApp.getInstanse().showMyRegSaveAccDetailDialog(selectedPosition, positionMap,
						onCreateNoticeListener, onContinueSaveListener, onModifyNickNameListener);
			}
		}
	};

	/** 选择账户Spinner (有效 ,销户 ,冻结 ,全部) */
	private void setSpinnnerAdapter(Spinner spinner, List<String> data) {
		ArrayAdapter<String> remitadapter = new ArrayAdapter<String>(this, R.layout.spinner_item, data);
		remitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(remitadapter);
		spinner.setSelection(0);
	}

	/** 支取按钮 监听 */
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

	/** 查询系统时间返回 继续支取操作 */
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		Map<String, Object> content = DeptDataCenter.getInstance().getCurDetailContent();
		endDate = (String) content.get(Dept.INTEREST_ENDDATE);

		if (content.get(Dept.TYPE).equals(DeptBaseActivity.NOTIFY_SAVE)) {
			// 如果是通知存款 发送通讯 查询通知
			String strConvertType = (String) DeptDataCenter.getInstance().getCurDetailContent()
					.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
			if (strConvertType.equals(ConstantGloble.CONVERTTYPE_N)) {// 非约定转存
				String volumeNumber = (String) content.get(Dept.VOLUME_NUMBER);
				String cdNumber = (String) content.get(Dept.CD_NUMBER);
				requestQueryNotify(accountId, volumeNumber, cdNumber);
			} else {// 约定转存
				BiiHttpEngine.dissMissProgressDialog();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(context, MyPositDrawMoneyActivity.class);
				startActivity(intent);
			}

		} else {
			BiiHttpEngine.dissMissProgressDialog();
			if (!StringUtil.isNullOrEmpty(endDate)) {
				boolean noticeFlag = QueryDateUtils.compareDate(dateTime, endDate);
				if (!noticeFlag) {// 未到期提醒
					BaseDroidApp.getInstanse().showErrorDialog(
							this.getResources().getString(R.string.checkout_notify_message), R.string.cancle,
							R.string.confirm, onclicklistener);
				} else {
					Intent intent = new Intent();
					intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
					intent.setClass(context, MyPositDrawMoneyActivity.class);
					startActivity(intent);
				}
			} else { // 没有违约
				BiiHttpEngine.dissMissProgressDialog();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(context, MyPositDrawMoneyActivity.class);
				startActivity(intent);
			}
		}
	}

	/** 未到期提醒框 按钮监听 */
	private OnClickListener onclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch ((Integer) v.getTag()) {
			case CustomDialog.TAG_SURE:// 确定
				BaseDroidApp.getInstanse().dismissErrorDialog();
				// 如果是整存整取
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(context, MyPositDrawMoneyActivity.class);
				startActivity(intent);
				BaseDroidApp.getInstanse().dismissMessageDialog();
				break;
			case CustomDialog.TAG_CANCLE:// 取消
				BaseDroidApp.getInstanse().dismissErrorDialog();
				break;
			default:
				break;
			}
		}
	};

	/** 续存按钮 监听 教育储蓄和零存整取 */
	private OnClickListener onContinueSaveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra(ConstantGloble.CONTINUE_SAVE_FLAG, "Y");// 续存
			intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
			intent.setClass(context, MyPositDrawMoneyActivity.class);
			startActivity(intent);
			BaseDroidApp.getInstanse().dismissMessageDialog();// 关闭详情框
		}
	};

	/** 修改账户别名 监听 */
	private OnClickListener onModifyNickNameListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			strNickName = BaseDroidApp.getInstanse().getMessageDialog().getNickNameEt().getText().toString().trim();
			// 判断账户别名
			RegexpBean regNickName = new RegexpBean(MyPositMneyListActivity.this.getResources().getString(
					R.string.nickname_regex), strNickName, "nickname");
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

	/** 建立通知按钮 监听 */
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

	/** 通讯返回 */
	@Override
	public void communicationCallBack(int flag) {
		super.communicationCallBack(flag);
		switch (flag) {
		case QUERY_NOTIFY_ACCOUNT_DETAIL_CALLBACK:// 支取通知存款 查询通知详情 返回
			List<Map<String, Object>> noticeDetailList = DeptDataCenter.getInstance().getQueryNotifyCallBackList();
			// 通知编号list
			noticeIdList = new ArrayList<String>();
			for (int i = 0; i < noticeDetailList.size(); i++) {
				String noticeNo = (String) noticeDetailList.get(i).get(Dept.NOTIFY_ID);
				if (!StringUtil.isNullOrEmpty(noticeNo)) { // 判断通知编号是否为空
					noticeIdList.add(noticeNo);
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
					intent.setClass(context, MyPositDrawMoneyActivity.class);
					startActivity(intent);
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			} else {// 约定转存
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
				intent.setClass(context, MyPositDrawMoneyActivity.class);
				startActivity(intent);
				BaseDroidApp.getInstanse().dismissMessageDialog();
			}
			break;
		default:
		}
	}

	/**
	 * 根据用户选择 显示对应的数据
	 * 
	 * @param detailList
	 *            全部数据
	 * @param number
	 *            Spinner 用户选择数据 (有效,销户,冻结,全部) 根据用户选择项 显示对应的 adapter 数据
	 */
	private void selectFlushData(List<Map<String, Object>> detailList, String flag) {
		flushList.clear();
		for (int i = 0; i < detailList.size(); i++) {
			Map<String, Object> accListMap = detailList.get(i);
			String status = (String) accListMap.get(Dept.STATUS);
			if (status.equals(flag) && !StringUtil.isNull(status)) { // 根据用户选择显示对应的数据
				// 默认显示有效数据
				flushList.add(accListMap);
				adapter.refreshData(flushList); // 刷新adapter
			} else if (flag.equals(ConstantGloble.ALLACCS)) { // 用户点击全部数据
				flushList.add(accListMap);
				adapter.refreshData(flushList); // 刷新adapter
			} else if (!status.equals(flag)) { // 如果数据为空时 显示空白页面
				adapter.refreshData(flushList); // 刷新adapter
			}
		}
	}
}
