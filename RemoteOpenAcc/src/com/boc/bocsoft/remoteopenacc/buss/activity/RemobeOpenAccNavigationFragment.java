package com.boc.bocsoft.remoteopenacc.buss.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.buss.model.querysysdate.QuerySysDateParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.querysysdate.QuerySysDateResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.service.RemoteOpenAccService;
import com.boc.bocsoft.remoteopenacc.common.activity.BaseFragment;

/**
 * 远程开户导航页，包含进度查询、我要开户
 * 
 * @author fb
 * 
 */
public class RemobeOpenAccNavigationFragment extends BaseFragment {

	private View mRoot;
	// 获取系统日期code
	private final static int CODE_SYS_DATE = 0x11;
	private Button btn_open_acc;
	private Button btn_progress_inquery;
	private RemobeOpenAuthenticationFragment mAuthenticationFragment;
	private RemobeOpenProgressInqueryFragment mProgressInqueryFragment;
	private RemoteOpenAccActivity mAccActivity;
	private RemoteOpenAccService mRemoteOpenAccService;
	//设备时间
    private Calendar deviceTimeCal; 
    private String deviceTimeStr; 
    
    
	@Override
	public View onCreateView(LayoutInflater inflater) {
		mRoot = inflater.inflate(
				R.layout.bocroa_fragment_remote_open_acc_navigation, null,
				false);
		return mRoot;
	}

	@Override
	protected void initView() {
		btn_open_acc = (Button) mRoot.findViewById(R.id.btn_open_acc);
		btn_progress_inquery = (Button) mRoot
				.findViewById(R.id.btn_progress_inquery);
	}

	@Override
	protected void initData() {
		mRemoteOpenAccService = new RemoteOpenAccService(mContext, this);
		mAccActivity = (RemoteOpenAccActivity) getActivity();
		deviceTimeCal = (Calendar) Calendar.getInstance();
		deviceTimeStr = format24.format(deviceTimeCal.getTime());
	}

	@Override
	protected void setListener() {
		btn_open_acc.setOnClickListener(queryClickListener);
		btn_progress_inquery.setOnClickListener(queryClickListener);
	}

	/**
	 * 点击事件监听
	 */
	private OnClickListener queryClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.btn_open_acc) {
				querySystemDate();
			} else if (id == R.id.btn_progress_inquery) {
				// 跳转到进度查询界面
				mProgressInqueryFragment = new RemobeOpenProgressInqueryFragment();
				((RemoteOpenAccActivity) getActivity())
						.jump2Fragment(mProgressInqueryFragment);
			} else {
			}
		}
	};

	@Override
	public void onTaskSuccess(Message result) {
		switch (result.what) {
		// 获取系统日期 0x11
		case CODE_SYS_DATE:
			closeProgressDialog();
			handlequerySystemDate((QuerySysDateResponseModel) result.obj);
			break;
		default:
			break;
		}

	}

	@Override
	public void onTaskFault(Message result) {
		// TODO 失败处理异常 用本地时间
		closeProgressDialog();
		QuerySysDateResponseModel resultModel = new QuerySysDateResponseModel();
		resultModel.sysdate = deviceTimeStr;
		handlequerySystemDate(resultModel);
	}

	/**
	 * 查询系统日期
	 */
	private void querySystemDate() {
		showProgressDialog();
		QuerySysDateParamsModel mParams = new QuerySysDateParamsModel();
		mRemoteOpenAccService.querysysdate(mParams, CODE_SYS_DATE);
	}

	/**
	 * 处理获取系统日期
	 * 
	 * @param obj
	 */
	private void handlequerySystemDate(QuerySysDateResponseModel resultModel) {
		String sysdate = resultModel.sysdate;
		((RemoteOpenAccActivity) getActivity()).setSystemDateStr(sysdate);
		String[] dateStrList = sysdate.split(" ");
		String startdate = null;
		String enddate = null;
		if (dateStrList.length > 0) {
			startdate = dateStrList[0] + " " + "08:00:00";
			enddate = dateStrList[0] + " " + "20:00:00";
		}
		if (isDateInRange(startdate, enddate, sysdate)) {
			// 跳转到开户验证界面
			mAuthenticationFragment = new RemobeOpenAuthenticationFragment();
			mAccActivity.jump2Fragment(mAuthenticationFragment);
		} else {
			showErrorDialog(getResources().getString(R.string.bocroa_message_service_time_regex));
		}

	}

	@Override
	public String getMainTitleText() {
		return getResources().getString(R.string.bocroa_remote_open_account);
	}

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat format24 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 比较date是否在date1,date2之间（日期相等也返回true）
	 * 
	 * @param date1
	 *            开始时间 yyyy-MM-dd HH:mm:ss 格式
	 * @param date2
	 *            结束时间 yyyy-MM-dd HH:mm:ss 格式
	 * @param date
	 *            当前日期时间 yyyy-MM-dd HH:mm:ss 格式
	 * @return true if date1<=date<=date1
	 */
	private Boolean isDateInRange(String date1, String date2, String date) {
		if (date1 == null || date2 == null) {
			return false;
		} else {
			Date date11 = null;
			Date date22 = null;
			Date dateNow = null;
			try {
				date11 = format24.parse(date1);
				date22 = format24.parse(date2);
				dateNow = format24.parse(date);
			} catch (ParseException e) {
				return false;
			}
			return isDateBefore(dateNow, date22)
					&& isDateBefore(date11, dateNow);
		}
	}

	/**
	 * 比较date2是否在date1之后（日期相等也返回true）
	 * 
	 * @param date1
	 * @param date2
	 * @return if date2>=date1
	 */
	private Boolean isDateBefore(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		} else {
			return !date1.after(date2);
		}
	}
}
