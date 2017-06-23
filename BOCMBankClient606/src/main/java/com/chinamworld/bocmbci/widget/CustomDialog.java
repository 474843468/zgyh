/**
 * 文件名	：CustomDialog.java
 * 创建日期	：2012-10-15
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */

package com.chinamworld.bocmbci.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cfca.mobile.sip.SipBox;

import com.cfca.mobile.log.CodeException;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.foreign.adapter.ForeignDialogAdapter;
import com.chinamworld.bocmbci.biz.infoserve.InfoServeDataCenter;
import com.chinamworld.bocmbci.biz.prms.control.PrmsControl;
import com.chinamworld.bocmbci.biz.remittance.fragment.CYTextView;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.thread.BaseRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.BottomButtonUtils;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.DialogItemAdapter;
import com.chinamworld.bocmbci.widget.adapter.SecurityFactorAdapter;
import com.chinamworld.bocmbci.widget.adapter.ShotcutTabGridAdapter;
import com.chinamworld.bocmbci.widget.entity.ImageTextAndAct;
import com.chinamworld.llbt.utils.AnimUtils;
import android.view.animation.Animation;
public class CustomDialog extends Dialog {

	public final static int TAG_CLOSE = 0;
	public final static int TAG_PWD = 1;
	public final static int TAG_SUBMIT = 2;
	public final static int TAG_EXIT = 3;
	public final static int TAG_CONFIRM = 4;
	public final static int TAG_RETRY = 5;
	public final static int TAG_SURE = 6;
	public final static int TAG_CANCLE = 7;
	public final static int TAG_BACK = 8;

	public final static int TAG_RELA_ACC_TRAN = 9;
	public final static int TAG_COMMON_RECEIVER_TRAN = 10;
	public final static int TAG_TRAN_BOCMOBILE = 11;
	public final static int TAG_CONFIRM_BOC = 12;

	public final static int TAG_XIAOFEI_SETUP = 13;
	public final static int TAG_BILLSERVICE_SETUP = 14;
	public final static int TAG_MONEY_SETUP = 15;
	public final static int TAG_FUSHUSERVICE_SETUP = 16;

	public final static int TAG_MODIFY_PWD = 17;

	public final static int TAG_EDIT_ALIAS = 18;
	public final static int TAG_SET_DEFAULTNUM = 19;
	public final static int TAG_COSERN_CONNECTION = 20;

	public final static String PASSWORD_NEW = "pwd_new";
	public final static String PASSWORD_NEW_RC = "pwd_new_rc";
	public final static String PASSWORD_NEW_CONFIRM = "pwd_con";
	public final static String PASSWORD_NEW_CONFIRM_RC = "pwd_con_rc";

	public int currentPosition = 0;

	/** 当前账户内容 */
	private Map<String, Object> accountContent;

	/** 当前账户对应的详情内容 */
	private List<Map<String, Object>> accountDetaiList;

	private List<Map<String, Object>> volumesAndCdnumbers;
	/** 组装数据中 当前选中的list */
	List<Map<String, Object>> currentList;

	private ArrayAdapter<String> banksheetAdapter;
	// Layout账户别名
	private LinearLayout nickNameLayout;
	// Layout修改账户别名
	private FrameLayout modifyNickNameLayout;
	// TextView 账户别名
	private TextView accountNickName;
	// EditText 账户别名
	private EditText nickNameEt;

	private SipBox newPwd;
	private SipBox newPwdConfirm;

	public int[] images = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher };

	private static final String TAG = "CustomDialog";
	
	
	// yuht 重新封装自定义弹出框方法
	/***
	 * v : 自定义的弹出框布局
	 * @param context
	 * @param v
	 */
	public CustomDialog(Context context,View v){
		this(context,R.style.Theme_Dialog);
		setContentView(v);
	}
	
	
	
	
	
	
	// end
	
	
	
	
	public CustomDialog(Context context) {
		this(context, R.style.Theme_Dialog);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		this.currentContent = context;
		setCancelable(true);
		setCanceledOnTouchOutside(false);
		// getWindow().setBackgroundDrawable(new BitmapDrawable()); // 去除黑色背景
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	private View contentView;
	private Context currentContent;

	/**
	 * 取得当前dialog的视图
	 * 
	 * @return
	 */
	public View getContentView() {
		return contentView;
	}

	// dialog.dismiss()之后会回调该方法
	@Override
	protected void onStop() {
		super.onStop();
		contentView = null;
		// noticeView = null; // 消息提示框需要复用 不能置空该视图
	}

	@Override
	public void dismiss() {
		LogGloble.d(TAG, "custom dialog dismiss!");
		try {
			super.dismiss();
		} catch (BadTokenException e) {
			LogGloble.w(TAG, "custom dialog dismiss error", e);
		}

	}

	public Context getCurrentContent() {
		return currentContent;
	}

	public void setCurrentContent(Context currentContent) {
		this.currentContent = currentContent;
	}

	@Override
	public void show() {
		try {
			super.show();
		} catch (Exception e) {
			LogGloble.w(TAG, "custom dialog show error", e);
		}

	}

	/**
	 * 初始化消息提示dialog 只有一个确定按钮 TAG_CONRIM
	 * 
	 * @param errorCode
	 *            错误码
	 * @param message
	 *            错误信息
	 * @param onclickListener
	 *            执行点击事件
	 * @return 视图
	 */
	// add by wjp 2012.10.31
	public View initMsgDialogView(String errorCode, String message, View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.message_dialog, null);
		Button confirmBtn = (Button) contentView.findViewById(R.id.confirm_btn);
		confirmBtn.setOnClickListener(onclickListener);

		TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.tv_metion_msg);
		// if (StringUtil.isNullOrEmpty(errorCode)) {
		// tvMentionMsg.setText(message);
		// }else{
		// tvMentionMsg.setText("("+errorCode+") "+message);
		// }
		tvMentionMsg.setText(message);

		confirmBtn.setTag(TAG_CONFIRM);
		return contentView;

	}

	/**
	 * 
	 * @param errorCode
	 *            错误码
	 * @param message
	 *            错误信息
	 * @param textResId1
	 *            第一个按钮文本
	 * @param textResId2
	 *            第二个按钮文本
	 * @param onclicklistener
	 *            执行点击事件
	 * @return 视图
	 */
	public View initMentionDialogView(String errorCode, String message, int textResId1, int textResId2,
			View.OnClickListener onclicklistener) {
		String btn1Text = currentContent.getResources().getString(textResId1);
		String btn2Text = currentContent.getResources().getString(textResId2);
		contentView = initMentionDialogView(errorCode, message, btn1Text, btn2Text, onclicklistener);
		return contentView;
	}

	/**
	 * 彈出通用提示對話框
	 * 
	 * @param errorCode
	 *            错误码
	 * @param message
	 *            错误信息
	 * @param btn1Text
	 *            第一个按钮文本
	 * @param btn2Text
	 *            第二个按钮文本
	 * @param onclickListener
	 *            执行监听事件
	 * @return 视图
	 */
	public View initMentionDialogView(String errorCode, String message, String btn1Text, String btn2Text,
			View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.comm_fail_message_dialog, null);
		Button retryBtn = (Button) contentView.findViewById(R.id.exit_btn);
		retryBtn.setOnClickListener(onclickListener);
		retryBtn.setText(btn1Text);
		retryBtn.setTag(TAG_CANCLE);
		TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.tv_metion_msg);
		tvMentionMsg.setText(Html.fromHtml(message));
		Button cancleBtn = (Button) contentView.findViewById(R.id.retry_btn);
		cancleBtn.setOnClickListener(onclickListener);
		cancleBtn.setText(btn2Text);
		cancleBtn.setTag(TAG_SURE);
		return contentView;
	}

	/**
	 * 2个红色按钮
	 * 
	 * @param errorCode
	 *            错误码
	 * @param message
	 *            错误信息
	 * @param textResId1
	 *            第一个按钮文本
	 * @param textResId2
	 *            第二个按钮文本
	 * @param onclicklistener
	 *            执行点击事件
	 * @return 视图
	 */
	public View init2RedMentionDialogView(String errorCode, String message, int textResId1, int textResId2,
			View.OnClickListener onclicklistener) {
		String btn1Text = currentContent.getResources().getString(textResId1);
		String btn2Text = currentContent.getResources().getString(textResId2);
		contentView = init2RedMentionDialogView(errorCode, message, btn1Text, btn2Text, onclicklistener);
		return contentView;
	}

	/**
	 * 2个红色按钮 彈出通用提示對話框
	 * 
	 * @param errorCode
	 *            错误码
	 * @param message
	 *            错误信息
	 * @param btn1Text
	 *            第一个按钮文本
	 * @param btn2Text
	 *            第二个按钮文本
	 * @param onclickListener
	 *            执行监听事件
	 * @return 视图
	 */
	public View init2RedMentionDialogView(String errorCode, String message, String btn1Text, String btn2Text,
			View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.comm_fail_2red_message_dialog, null);
		Button retryBtn = (Button) contentView.findViewById(R.id.exit_btn);
		retryBtn.setOnClickListener(onclickListener);
		retryBtn.setText(btn1Text);
		retryBtn.setTag(TAG_CANCLE);
		TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.tv_metion_msg);
		tvMentionMsg.setText(message);
		Button cancleBtn = (Button) contentView.findViewById(R.id.retry_btn);
		cancleBtn.setOnClickListener(onclickListener);
		cancleBtn.setText(btn2Text);
		cancleBtn.setTag(TAG_SURE);
		return contentView;
	}

	/**
	 * 自定义吐司(Toast)1s 提示
	 * 
	 * @param context
	 *            上下文对象
	 * @param text
	 *            要提示的信息
	 */
	public static void toastShow(Context context, String text) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.toast, null);
		((TextView) layout.findViewById(R.id.textview)).setText(text);
		Toast toast = new Toast(context);
		toast.setView(layout);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(1000);
		toast.show();
	}

	/**
	 * 中间显示吐司
	 * 
	 * @param context
	 *            上下文对象
	 * @param text
	 *            要显示的文本
	 */
	public static void toastInCenter(Context context, String text) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.toast_one, null);
		((TextView) layout.findViewById(R.id.textview)).setText(text);
		Toast toast = new Toast(context);
		toast.setView(layout);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(1000);
		toast.show();
	}

	/**
	 * 自定义吐司(Toast)提示 有两个 提示框的
	 * 
	 * @param context
	 *            上下文对象
	 * @param text
	 *            要提示的信息
	 */
	public static void toastShow(Context context, String text1, String text2) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.toast_with_two_message, null);
		((TextView) layout.findViewById(R.id.textview1)).setText(text1);
		((TextView) layout.findViewById(R.id.textview2)).setText(text2);
		Toast toast = new Toast(context);
		toast.setView(layout);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 贵金属账户信息
	 * 
	 * @Author xyl
	 * @param dataList
	 *            listview的数据
	 * @param onclickListener
	 *            重设按钮的点击事件
	 * @return
	 */
	public View initPrmsAccBalanceDialogView(String acctype, String nickName, View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.prms_accbalance_dialog, null);
		TextView accNumTextView = (TextView) contentView.findViewById(R.id.textView1);
		TextView nickNameTv = (TextView) contentView.findViewById(R.id.prms_accalias);
		TextView accTypeTv = (TextView) contentView.findViewById(R.id.prms_acctype);

		// 设置账户号码
		nickNameTv.setText(nickName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(BaseDroidApp.getInstanse().getCurrentAct(), nickNameTv);
		accTypeTv.setText(LocalData.AccountType.get(acctype));
		accNumTextView.setText(StringUtil.getForSixForString(PrmsControl.getInstance().accNum));
		Button reSetBtn = (Button) contentView.findViewById(R.id.reset);
		ImageView concernBtn = (ImageView) contentView.findViewById(R.id.img_exit_accdetail);
		reSetBtn.setOnClickListener(onclickListener);
		concernBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		concernBtn.setTag(TAG_CANCLE);
		return contentView;
	}

	/**
	 * 初始化提示框 没有提示标题 只有一个确定按钮 TAG_CONFIRM
	 * 
	 * @param errorCode
	 * @param message
	 * @param onclickListener
	 * @return
	 */
	public View initInfoDialogView(String errorCode, CharSequence message, View.OnClickListener onclickListener) {
		return initInfoDialogView(errorCode, message, Gravity.CENTER, onclickListener);
	}

	/**
	 * @Description: 初始化提示框 没有提示标题 只有一个确定按钮 TAG_CONFIRM
	 * @param errorCode
	 * @param message
	 *            消息内容
	 * @param gravity
	 *            textView gravity属性
	 * @param onclickListener
	 * @return
	 * @author: luql
	 * @date: 2015年2月11日 上午11:44:16
	 */
	public View initInfoDialogView(String errorCode, CharSequence message, int gravity, View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.comm_info_message_dialog, null);
		Button confirmBtn = (Button) contentView.findViewById(R.id.confirm_btn);
		confirmBtn.setOnClickListener(onclickListener);
		confirmBtn.setTag(TAG_CONFIRM);
		TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.tv_metion_msg);
		// TODO 是否显示错误码
		tvMentionMsg.setText(message);
		tvMentionMsg.setGravity(gravity);
		return contentView;
	}
	
	/**
	 * @Description: 初始化提示框 没有提示标题 只有一个确定按钮 TAG_CONFIRM
	 * @param errorCode
	 * @param message
	 *            消息内容
	 * @param gravity
	 *            textView gravity属性
	 * @param onclickListener
	 * @return
	 * @author: luql
	 * @date: 2015年2月11日 上午11:44:16
	 */
	public View initInfoDialogView2(String errorCode, CharSequence message1, CharSequence message2, int gravity, View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.comm_info_message_dialog2, null);
		Button confirmBtn = (Button) contentView.findViewById(R.id.confirm_btn);
		confirmBtn.setOnClickListener(onclickListener);
		confirmBtn.setTag(TAG_CONFIRM);
		TextView tvMentionMsgRed = (TextView) contentView.findViewById(R.id.tv_metion_msg_red);
		CYTextView tvMentionMsg = (CYTextView) contentView.findViewById(R.id.tv_metion_msg);
		// TODO 是否显示错误码
		tvMentionMsgRed.setText(message1);
		tvMentionMsg.SetText(message2.toString());
		tvMentionMsgRed.setGravity(gravity);
		tvMentionMsg.setGravity(gravity);
		return contentView;
	}

	public View initInfoDialogViewById(String errorCode, int id, CharSequence message, int gravity,
			View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.comm_info_message_dialog, null);
		Button confirmBtn = (Button) contentView.findViewById(R.id.confirm_btn);
		confirmBtn.setOnClickListener(onclickListener);
		confirmBtn.setTag(TAG_CONFIRM);
		TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.tv_metion_msg);
		// TODO 是否显示错误码
		tvMentionMsg.setText(message);
		tvMentionMsg.setGravity(gravity);
		return contentView;
	}

	/**
	 * 初始化提示框 有提示标题 只有一个确定按钮 TAG_CONFIRM
	 * 
	 * @param errorCode
	 * @param message
	 * @param onclickListener
	 * @return
	 */
	public View initInfoDialogView(String errorCode, String title, CharSequence message, View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.comm_info_message_have_title_dialog, null);
		Button confirmBtn = (Button) contentView.findViewById(R.id.confirm_btn);
		confirmBtn.setOnClickListener(onclickListener);
		confirmBtn.setTag(TAG_CONFIRM);
		TextView tvMentionTitile = (TextView) contentView.findViewById(R.id.tv_metion_title);
		TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.tv_metion_msg);
		// TODO 是否显示错误码
		tvMentionTitile.setText(title);
		tvMentionMsg.setText(message);

		return contentView;

	}

	/**
	 * 初始化提示框 有提示标题 只有一个确定按钮 TAG_CONFIRM
	 * 
	 * @param errorCode
	 * @param message
	 * @param onclickListener
	 * @return
	 */
	public View initInfoDialogViewOneBtn(String message, String btnText, View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.comm_info_message_have_title_dialog, null);
		Button confirmBtn = (Button) contentView.findViewById(R.id.confirm_btn);
		confirmBtn.setText(btnText);
		confirmBtn.setOnClickListener(onclickListener);
		confirmBtn.setTag(TAG_CONFIRM);
		TextView tvMentionTitile = (TextView) contentView.findViewById(R.id.tv_metion_title);
		TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.tv_metion_msg);
		tvMentionTitile.setText(null);
		tvMentionTitile.setVisibility(View.GONE);
		tvMentionMsg.setText(message);
		tvMentionMsg.setGravity(Gravity.LEFT);
		return contentView;

	}

	public View checkboxDialogView(String message,View.OnClickListener onclickListener,OnCheckedChangeListener checkedListener){
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.message_checkbox_dialog, null);
		Button confirm = (Button) contentView.findViewById(R.id.confirm_btn);
		confirm.setOnClickListener(onclickListener);
		TextView tv_message = (TextView) contentView.findViewById(R.id.tv_metion_msg);
		tv_message.setText(message);
		CheckBox checkbox = (CheckBox) contentView.findViewById(R.id.cbx_select);
		checkbox.setOnCheckedChangeListener(checkedListener);
		
		return contentView;
		
		
	}
	
	/** 音频key消息确认弹出框 */
	public View initInfoAudioDialogView(View.OnClickListener onaudioclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.isforex_confirm_dialog, null);
		// Button closeBtn = (Button)contentView.findViewById(R.id.btn);
		// closeBtn.setOnClickListener(onaudioclickListener);
		// closeBtn.setTag(TAG_CONFIRM);
		return contentView;
	}

	/**
	 * 选择转账类型 dialog
	 * 
	 * @param errorCode
	 *            错误码
	 * @param message
	 *            错误信息
	 * @param onclickListener
	 *            监听事件
	 * @return dialog视图
	 */
	/*
	 * public View initRelativeAccTransTypeDialogView( View.OnClickListener
	 * onclickListener) { contentView =
	 * LayoutInflater.from(currentContent).inflate(
	 * R.layout.prms_selectbuyorsale_dialog, null);
	 * 
	 * Button relaAccBtn = (Button) contentView .findViewById(R.id.prms_buy);
	 * relaAccBtn.setTag(TAG_RELA_ACC_TRAN);
	 * relaAccBtn.setOnClickListener(onclickListener);
	 * 
	 * Button commonTranBtn = (Button) contentView
	 * .findViewById(R.id.prms_sale);
	 * commonTranBtn.setTag(TAG_COMMON_RECEIVER_TRAN);
	 * commonTranBtn.setOnClickListener(onclickListener);
	 * 
	 * Button cancleBtn = (Button) contentView.findViewById(R.id.cancle_btn);
	 * cancleBtn.setTag(TAG_CANCLE);
	 * cancleBtn.setOnClickListener(onclickListener); return contentView; }
	 */
	public View initRelativeAccTransTypeDialogView(View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.trans_custom_dialog_3items, null);
		Button relaAccBtn = (Button) contentView.findViewById(R.id.rela_acc_tran_btn);
		relaAccBtn.setTag(TAG_RELA_ACC_TRAN);
		relaAccBtn.setOnClickListener(onclickListener);
		Button commonTranBtn = (Button) contentView.findViewById(R.id.common_receiver_tran_btn);
		commonTranBtn.setTag(TAG_COMMON_RECEIVER_TRAN);
		commonTranBtn.setOnClickListener(onclickListener);
		Button cancleBtn = (Button) contentView.findViewById(R.id.cancle_btn);
		cancleBtn.setTag(TAG_CANCLE);
		cancleBtn.setOnClickListener(onclickListener);
		return contentView;
	}

	/**
	 * 信用卡设定 dialog
	 * 
	 * @param errorCode
	 *            错误码
	 * @param message
	 *            错误信息
	 * @param onclickListener
	 *            监听事件
	 * @return dialog视图
	 */
	public View initCrcdSetupTypeDialogView(View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.crcd_custom_dialog_5items, null);
		Button xiaofei = (Button) contentView.findViewById(R.id.crcd_xiaofei_setup);
		xiaofei.setTag(TAG_XIAOFEI_SETUP);
		xiaofei.setOnClickListener(onclickListener);
		Button billservice = (Button) contentView.findViewById(R.id.crcd_billservice_setup);
		billservice.setTag(TAG_BILLSERVICE_SETUP);
		billservice.setOnClickListener(onclickListener);
		Button cutmoney = (Button) contentView.findViewById(R.id.crcd_cut_money_setup);
		cutmoney.setTag(TAG_MONEY_SETUP);
		cutmoney.setOnClickListener(onclickListener);
		Button fushuservice = (Button) contentView.findViewById(R.id.crcd_fushu_service_setup);
		fushuservice.setTag(TAG_FUSHUSERVICE_SETUP);
		fushuservice.setOnClickListener(onclickListener);
		Button cancleBtn = (Button) contentView.findViewById(R.id.cancle_btn);
		cancleBtn.setTag(TAG_CANCLE);
		cancleBtn.setOnClickListener(onclickListener);
		return contentView;
	}

	/**
	 * 转账汇款 修改收款人手机号和别名 选择dialog
	 * 
	 * @param errorCode
	 *            错误码
	 * @param message
	 *            错误信息
	 * @param onclickListener
	 *            监听事件
	 * @return dialog视图
	 */

	public View initPayeeEditDialogView(View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.trans_custom_dialog_manage_payee_edit_3items,
				null);
		Button relaAccBtn = (Button) contentView.findViewById(R.id.rela_acc_tran_btn);
		relaAccBtn.setTag(TAG_RELA_ACC_TRAN);
		relaAccBtn.setOnClickListener(onclickListener);
		Button commonTranBtn = (Button) contentView.findViewById(R.id.common_receiver_tran_btn);
		commonTranBtn.setTag(TAG_COMMON_RECEIVER_TRAN);
		commonTranBtn.setOnClickListener(onclickListener);
		Button cancleBtn = (Button) contentView.findViewById(R.id.cancle_btn);
		cancleBtn.setTag(TAG_CANCLE);
		cancleBtn.setOnClickListener(onclickListener);
		return contentView;
	}

	/**
	 * 修改收款人手机号和 dialog
	 * 
	 * @param onclickListener
	 * @return
	 */

	/**
	 * 
	 * @Author xyl
	 * @param btnMessage
	 *            btn 上显示的信息
	 * @param textviewMessage
	 *            提示信息
	 * @param onclickListener
	 *            btn 的onClick时间
	 * @return
	 */
	public View initFincSetAttentionFundDialogView(String btnMessage, String textviewMessage,
			View.OnClickListener onclickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.finc_setattentionfund_success_dialog, null);
		Button btn = (Button) contentView.findViewById(R.id.btn);
		btn.setTag(TAG_RELA_ACC_TRAN);
		btn.setOnClickListener(onclickListener);
		btn.setText(btnMessage);
		TextView messageTextView = (TextView) contentView.findViewById(R.id.message);
		messageTextView.setText(textviewMessage);
		return contentView;

	}

	/**
	 * 账户管家 修改功能菜单
	 * 
	 * @param onClickListenner
	 * @param isCanSetDefaultAcc
	 *            是否可以设定默认账户
	 * @param isCanConsernConnnection
	 *            是否可以取消关联
	 * @return
	 */
	public View initSettingAccManagerDialogView(View.OnClickListener onClickListenner, boolean isCanSetDefaultAcc,
			boolean isCanConsernConnnection) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.setting_accmanager_menu_dialog, null);
		Button editaliasBtn = (Button) contentView.findViewById(R.id.set_accmanager_editalias);// 修改账户别名
		editaliasBtn.setTag(TAG_EDIT_ALIAS);
		editaliasBtn.setOnClickListener(onClickListenner);
		// Button setdefaultaccBtn = (Button) contentView
		// .findViewById(R.id.set_accmanager_setdefaultacc);// 设置默认账户
		// LinearLayout setdefaultaccLl = (LinearLayout) contentView
		// .findViewById(R.id.set_accmanager_setdefaultacc_ll);// 设置默认账户
		// setdefaultaccBtn.setTag(TAG_SET_DEFAULTNUM);
		// setdefaultaccBtn.setOnClickListener(onClickListenner);
		Button conerconnectionBtn = (Button) contentView.findViewById(R.id.set_accmanager_consern);// 取消关联
		LinearLayout conerconnectionLl = (LinearLayout) contentView.findViewById(R.id.set_accmanager_consern_ll);// 取消关联
		conerconnectionBtn.setTag(TAG_COSERN_CONNECTION);
		conerconnectionBtn.setOnClickListener(onClickListenner);
		Button cancleBtn = (Button) contentView.findViewById(R.id.cancle_btn);
		cancleBtn.setTag(TAG_CANCLE);
		cancleBtn.setOnClickListener(onClickListenner);
		// if (isCanSetDefaultAcc) {
		// setdefaultaccLl.setVisibility(View.VISIBLE);
		// } else {
		// setdefaultaccLl.setVisibility(View.GONE);
		// }
		if (isCanConsernConnnection) {
			conerconnectionLl.setVisibility(View.VISIBLE);
		} else {
			conerconnectionLl.setVisibility(View.GONE);
		}
		return contentView;
	}

	/**
	 * 选择买入还是卖出
	 * 
	 * @Author xyl
	 * @param buyBtnListenner
	 *            买入事件
	 * @param saleBtnListenner
	 *            卖出事件
	 * @param concelBtnListenner
	 *            取消事件
	 * @return
	 */
	public View initPrmsSelectBuyOrSaleDialogView(String buy, String sale, View.OnClickListener onClickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.prms_selectbuyorsale_dialog, null);
		Button buyBtn = (Button) contentView.findViewById(R.id.prms_buy);// 买入
		buyBtn.setText(buy);
		buyBtn.setTag(TAG_RELA_ACC_TRAN);
		buyBtn.setOnClickListener(onClickListener);
		Button saleBtn = (Button) contentView.findViewById(R.id.prms_sale);// 卖出
		saleBtn.setText(sale);
		saleBtn.setTag(TAG_COMMON_RECEIVER_TRAN);
		saleBtn.setOnClickListener(onClickListener);
		Button cancleBtn = (Button) contentView.findViewById(R.id.cancle_btn);
		cancleBtn.setTag(TAG_CANCLE);

		cancleBtn.setOnClickListener(onClickListener);
		return contentView;
	}

	/**
	 * 
	 * @param accNumber
	 *            :外汇交易账户
	 * @param alaias
	 *            ：账户别名
	 * @param accType
	 *            ：账户类型
	 * @param onClickListener
	 *            ：重设外汇交易账户
	 * @return
	 */
	public View initForexCustomerAccResetDailgView(String accNumber, String alaias, String accType,
			android.view.View.OnClickListener onClickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.forex_customer_accinfo, null);
		ImageView img = (ImageView) contentView.findViewById(R.id.img_exit_accdetail);
		TextView accNumberText = (TextView) contentView.findViewById(R.id.customer_accID);
		TextView accAlaisText = (TextView) contentView.findViewById(R.id.customer_accAlias);
		TextView accTypeText = (TextView) contentView.findViewById(R.id.customer_accType);
		Button resetAccButton = (Button) contentView.findViewById(R.id.customer_resetAccButton);
		String acc = StringUtil.getForSixForString(accNumber);
		accNumberText.setText(acc);
		accAlaisText.setText(alaias);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(currentContent, accAlaisText);
		accTypeText.setText(accType);
		img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		resetAccButton.setOnClickListener(onClickListener);

		return contentView;
	}

	/**
	 * 功能描述：转账管理账户列表弹出框
	 * 
	 * @param list
	 *            数据源
	 * @param type
	 *            转入、转出类型
	 * @param listViewClick
	 *            listview的监听器
	 * @param click
	 *            返回按钮监听 新增收款人按钮监听 新增关联账户监听
	 * @return
	 */
	// public View initTransferAccountsDialogView(final List<Map<String,
	// Object>> list, final int type, AdapterView.OnItemClickListener
	// listViewClick, View.OnClickListener click) {
	//
	// contentView =
	// LayoutInflater.from(currentContent).inflate(R.layout.tran_acc_list_mytransfer_new,
	// null);
	// // 返回按钮
	// Button backBt = (Button)
	// contentView.findViewById(R.id.trans_acc_top_back);
	// backBt.setOnClickListener(click);
	//
	// // 标题
	// TextView topTitle = (TextView)
	// contentView.findViewById(R.id.trans_acc_top_title);
	// // 新建收款人或者新建关联账户
	// Button toprightBt = (Button)
	// contentView.findViewById(R.id.trans_acc_top_right_btn);
	// toprightBt.setOnClickListener(click);
	// // final ListView listView = (ListView) contentView
	// // .findViewById(R.id.tran_acc_listview);
	// final AccOutAdapter adapter = new AccOutAdapter(currentContent, list);
	// // 查询的父View
	// View searchContent =
	// contentView.findViewById(R.id.tran_acc_seach_linear);
	// // 查询输入框
	// EditText searchEt = (EditText)
	// contentView.findViewById(R.id.search_acc_in_et);
	// searchEt.addTextChangedListener(new TextWatcher() {
	//
	// List<Map<String, Object>> filterList = new ArrayList<Map<String,
	// Object>>();
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count, int
	// after) {
	// }
	//
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before, int
	// count) {
	// LogGloble.i(TAG, "onTextChanged.." + s);
	// filterDate(s);
	// /*
	// * Message msg = handler.obtainMessage(101); msg.obj =
	// * filterList; handler.sendMessage(msg);
	// */
	// // listView
	// if (adapter != null) {
	// adapter.setDate(filterList);
	// }
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// }
	//
	// private void filterDate(CharSequence s) {
	// filterList.clear();
	// if (s == null) {
	// filterList.addAll(list);
	// return;
	// }
	//
	// if (!StringUtil.isNullOrEmpty(list)) {
	// for (Map<String, Object> map : list) {
	// String name = (String) map.get(Tran.ACCOUNTNAME_RES);
	// LogGloble.i(TAG, "Name=" + name + "name.contains(s)=" +
	// name.contains(s));
	// if (name.contains(s) == true)
	// filterList.add(map);
	// }
	// }
	//
	// }
	//
	// });
	// // 二维码转账
	// Button twoDimension = (Button)
	// contentView.findViewById(R.id.btn_twoDimension_acc_in_mytransfer);
	// twoDimension.setOnClickListener(click);
	// // 手机号转账
	// Button phoneBt = (Button)
	// contentView.findViewById(R.id.btn_phoneNum_acc_in_mytransfer);
	// phoneBt.setOnClickListener(click);
	//
	// switch (type) {
	// // 转出账户
	// case Tran.TRANSFER_ACCOUNT_OUT_TYPE:
	// searchContent.setVisibility(View.GONE);
	// topTitle.setText(currentContent.getString(R.string.tran_acc_out_top_title));
	// toprightBt.setText(currentContent.getString(R.string.tran_acc_out_top_right));
	// break;
	// // 转入账户
	// case Tran.TRANSFER_ACCOUNT_IN_TYPE:
	// topTitle.setText(currentContent.getString(R.string.tran_acc_in_top_title));
	// toprightBt.setText(currentContent.getString(R.string.tran_acc_in_top_right));
	// break;
	// }
	//
	// // listView.setAdapter(adapter);
	// // listView.setOnItemClickListener(listViewClick);
	// return contentView;
	// }

	/**
	 * 转账里面 转入账户类表框
	 * 
	 * @param listView
	 * @param backListener
	 * @param addAccInNoClicklistener
	 * @param twoDimensionListener
	 * @param phoneListener
	 * @param textWatcher
	 * @return
	 */
	public View initTranAccInListView(boolean isshowtoprightBtn, ListView listView, View.OnClickListener backListener,
			View.OnClickListener addAccInNoClicklistener, TextWatcher textWatcher) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.tran_acc_list_mytransfer_new, null);
		// 返回按钮
		Button backBt = (Button) contentView.findViewById(R.id.trans_acc_top_back);
		backBt.setOnClickListener(backListener);
		// 新增收款人
		Button toprightBt = (Button) contentView.findViewById(R.id.trans_acc_top_right_btn);
		toprightBt.setOnClickListener(addAccInNoClicklistener);
		// list容器
		RelativeLayout listContainer = (RelativeLayout) contentView.findViewById(R.id.tran_list_container);
		listContainer.addView(listView);
		// 查询输入框
		EditText searchEt = (EditText) contentView.findViewById(R.id.search_acc_in_et);
		searchEt.addTextChangedListener(textWatcher);
		// // 二维码转账
		// Button twoDimension = (Button) contentView
		// .findViewById(R.id.btn_twoDimension_acc_in_mytransfer);
		// twoDimension.setOnClickListener(twoDimensionListener);
		// // 手机号转账
		// Button phoneBt = (Button) contentView
		// .findViewById(R.id.btn_phoneNum_acc_in_mytransfer);
		// phoneBt.setOnClickListener(phoneListener);
		// if (iscanmobile) {
		// phoneBt.setVisibility(View.VISIBLE);
		// } else {
		// phoneBt.setVisibility(View.INVISIBLE);
		// }
		if (isshowtoprightBtn) {
			toprightBt.setVisibility(View.VISIBLE);
		} else {
			toprightBt.setVisibility(View.GONE);
		}
		// if (iscantwodimen) {
		// twoDimension.setVisibility(View.VISIBLE);
		// } else {
		// twoDimension.setVisibility(View.INVISIBLE);
		// }
		return contentView;
	}

	/**
	 * 存款账户详情框（定期一本通）
	 * 
	 * @param saveType
	 *            存储类型
	 * @param onBankbookListener
	 *            存折下拉框监听
	 * @param onBanksheetListener
	 *            存单号下拉监听
	 * @param onCreateNoticeListener
	 *            建立通知下拉监听
	 * @param onCheckoutListener
	 *            支取按钮监听
	 * @return
	 */
	public View initMySaveDetailDialogView(int position, final View.OnClickListener onCreateNoticeListener,
			final View.OnClickListener onCheckoutListener, final View.OnClickListener onContinueSaveListener,
			final View.OnClickListener onModifyNickNameListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.dept_my_save_detail, null);

		// 当前账户
		accountContent = DeptDataCenter.getInstance().getMyRegAccountList().get(position);
		// 将当前选择的账户 数据储存
		DeptDataCenter.getInstance().setAccountContent(accountContent);

		if (accountContent == null) {
			return contentView;
		}
		// 账户详情列表
		accountDetaiList = (List<Map<String, Object>>) DeptDataCenter.getInstance().getAccountDetailCallBackMap()
				.get(ConstantGloble.ACC_DETAILIST);

		ImageButton closeIb = (ImageButton) contentView.findViewById(R.id.dept_close_ib);
		closeIb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
			}
		});

		// TextView 账户类型
		TextView accountTypeTv = (TextView) contentView.findViewById(R.id.dept_accounttype_tv);
		String strAccountType = (String) accountContent.get(Comm.ACCOUNT_TYPE);
		accountTypeTv.setText(LocalData.AccountType.get(strAccountType));
		// Layout账户别名
		nickNameLayout = (LinearLayout) contentView.findViewById(R.id.dept_nickname_layout);
		// Layout修改账户别名
		modifyNickNameLayout = (FrameLayout) contentView.findViewById(R.id.fl_nickname);
		// TextView 账户别名
		accountNickName = (TextView) contentView.findViewById(R.id.dept_nickname_tv);
		// EditText 账户别名
		nickNameEt = (EditText) contentView.findViewById(R.id.et_acc_nickname);
		EditTextUtils.setLengthMatcher((BaseActivity) currentContent, nickNameEt, 20);

		Button updateNickNameBtn = (Button) contentView.findViewById(R.id.btn_update_nickname);

		accountNickName.setText((String) accountContent.get(Comm.NICKNAME));
		ImageView modifyNickNameIv = (ImageView) contentView.findViewById(R.id.img_dept_update_nickname);
		modifyNickNameIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nickNameLayout.setVisibility(View.GONE);
				modifyNickNameLayout.setVisibility(View.VISIBLE);
				nickNameEt.requestFocus();
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(nickNameEt, 0);
				nickNameEt.setText(accountNickName.getText().toString());
				nickNameEt.setSelection(accountNickName.getText().toString().length());
			}
		});

		updateNickNameBtn.setOnClickListener(onModifyNickNameListener);

		// TextView 账号
		TextView accountNo = (TextView) contentView.findViewById(R.id.dept_accountno_tv);
		String strAccNo = (String) accountContent.get(Comm.ACCOUNTNUMBER);
		accountNo.setText(StringUtil.getForSixForString(strAccNo));

		// 存折号Layout
		LinearLayout bankbookLayout = (LinearLayout) contentView.findViewById(R.id.dept_bankbook_layout);
		// 存单Layout
		LinearLayout banksheetLayout = (LinearLayout) contentView.findViewById(R.id.dept_banksheet_layout);
		// 开户日期Layout
		LinearLayout opendateLayout = (LinearLayout) contentView.findViewById(R.id.dept_opendate_layout);
		// 开户日期TextView
		TextView opendateTv = (TextView) contentView.findViewById(R.id.dept_opendate_tv);

		String tmp_accType = (String) accountContent.get(Comm.ACCOUNT_TYPE);
		// 如果是教育续存 或者 零存整取 没有存折存单 返回的accountDetaiList里面只有一个数据
		if (DeptBaseActivity.EDUCATION_SAVE1.equals(tmp_accType) || DeptBaseActivity.ZERO_SAVE1.equals(tmp_accType)
				|| DeptBaseActivity.ZERO_SAVE2.equals(tmp_accType) || DeptBaseActivity.EDUCATION_SAVE2.equals(tmp_accType)) {
			bankbookLayout.setVisibility(View.GONE);
			banksheetLayout.setVisibility(View.GONE);
			opendateLayout.setVisibility(View.VISIBLE);
			Map<String, Object> content = accountDetaiList.get(0);
			opendateTv.setText((String) content.get(Dept.OPENDATE));
			DeptDataCenter.getInstance().setCurDetailContent(content);
			refreshMySaveContent(content, onCreateNoticeListener, onCheckoutListener, onContinueSaveListener);
		} else {
			// Spinner 存折册号 根据账户列表来显示
			// 返回数据里面 存折号和存单是没有关联的 重新组装数据
			volumesAndCdnumbers = new ArrayList<Map<String, Object>>();

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
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						currentContent.getResources().getString(R.string.no_cdnumber));
				return contentView;
			}

			// 将存折册号 和 存单号 对应起来
			for (int i = 0; i < volumes.size(); i++) {
				ArrayList<Map<String, Object>> list = null;
				list = new ArrayList<Map<String, Object>>();
				Map<String, Object> mapContent = null;
				for (int j = 0; j < accountDetaiList.size(); j++) {
					Map<String, Object> detaimap = accountDetaiList.get(j);

					if (!((String) detaimap.get(Dept.STATUS)).equals("00")
							&& !((String) detaimap.get(Dept.STATUS)).equals("V")) // 过滤无效存单
						// if(((String)detaimap.get(Dept.STATUS)).equals("V") ||
						// ((String)detaimap.get(Dept.STATUS)).equals("00"))
						// 00为有效
						// 01
						// 无效
						continue;

					String volumeNumber = (String) detaimap.get(Dept.VOLUME_NUMBER);
					if (StringUtil.isNullOrEmpty(volumeNumber)) {
						continue;
					}
					String cdNumber = (String) detaimap.get(Dept.CD_NUMBER);
					if (volumeNumber.equals(volumes.get(i))) {
						mapContent = new HashMap<String, Object>();
						mapContent.put(ConstantGloble.VOL, cdNumber);
						mapContent.put(ConstantGloble.CONTENT, accountDetaiList.get(j));
						list.add(mapContent);
					}
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(Dept.VOLUME_NUMBER, volumes.get(i));
				map.put(Dept.CD_NUMBER, list);
				volumesAndCdnumbers.add(map);
			}

			// 默认显示 第一个存折册号
			// Spinner 存折册号
			Spinner bankbookSpinner = (Spinner) contentView.findViewById(R.id.dept_bankbook_spinner);
			ArrayAdapter<String> bankbookAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(),
					R.layout.dept_spinner, volumes);
			bankbookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			bankbookSpinner.setAdapter(bankbookAdapter);

			// bankbookSpinner.setOnItemSelectedListener(onBankbookListener);
			// 默认显示第一个存单号
			List<Map<String, Object>> cds = (List<Map<String, Object>>) volumesAndCdnumbers.get(0).get(Dept.CD_NUMBER);
			List<String> cdnums = new ArrayList<String>();
			for (int i = 0; i < cds.size(); i++) {
				String vol = (String) cds.get(i).get(ConstantGloble.VOL);
				cdnums.add(vol);
			}

			// Spinner 存单号
			final Spinner banksheetSpinner = (Spinner) contentView.findViewById(R.id.dept_banksheet_spinner);
			banksheetAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(), R.layout.dept_spinner,
					cdnums);
			banksheetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			banksheetSpinner.setAdapter(banksheetAdapter);
			banksheetSpinner.setSelection(0);

			bankbookSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

					currentList = (List<Map<String, Object>>) volumesAndCdnumbers.get(position).get(Dept.CD_NUMBER);
					// 从新组装一个cdnumberlist
					List<String> cdlist = new ArrayList<String>();
					for (int i = 0; i < currentList.size(); i++) {
						String vol = (String) currentList.get(i).get(ConstantGloble.VOL);
						cdlist.add(vol);
					}
					banksheetAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(),
							R.layout.dept_spinner, cdlist);
					banksheetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					banksheetSpinner.setAdapter(banksheetAdapter);
					// banksheetSpinner.setSelection(0);
					// Map<String, Object> content = (Map<String,
					// Object>) currentList
					// .get(0).get(ConstantGloble.CONTENT);
					// DeptDataCenter.getInstance().setCurDetailContent(
					// content);
					// refreshMySaveContent(content);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});

			banksheetSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					Map<String, Object> content = (Map<String, Object>) currentList.get(position)
							.get(ConstantGloble.CONTENT);
					DeptDataCenter.getInstance().setCurDetailContent(content);
					refreshMySaveContent(content, onCreateNoticeListener, onCheckoutListener, onContinueSaveListener);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
		}

		return contentView;
	}

	// /////////////////////// 账户管理 我的定期存款 列表详情页点击事件 start//////////////////
	/**
	 * 存款账户详情框（定期一本通） 账户管理 存折册号和存单序号反显数据
	 * 
	 * @param saveType
	 *            存储类型
	 * @param onBankbookListener
	 *            存折下拉框监听
	 * @param onBanksheetListener
	 *            存单号下拉监听
	 * @param onCreateNoticeListener
	 *            建立通知下拉监听
	 * @param onCheckoutListener
	 *            支取按钮监听
	 * @return
	 */
	public View initMySaveView(int selectedPosition, Map<String,Object> positionMap, final View.OnClickListener onCreateNoticeListener,
			final View.OnClickListener onCheckoutListener, final View.OnClickListener onContinueSaveListener,
			final View.OnClickListener onModifyNickNameListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.dept_myreg_save_detail, null);

		// 当前账户
		accountContent = DeptDataCenter.getInstance().getMyRegAccountList().get(selectedPosition);
		// 将当前选择的账户 数据储存
		DeptDataCenter.getInstance().setAccountContent(accountContent);

		if (accountContent == null) {
			return contentView;
		}
		// 账户详情列表
//		accountDetaiList = (List<Map<String, Object>>) DeptDataCenter.getInstance().getAccountDetailCallBackMap()
//				.get(ConstantGloble.ACC_DETAILIST);

		ImageButton closeIb = (ImageButton) contentView.findViewById(R.id.dept_close_ib);
		closeIb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
			}
		});

		// TextView 账户类型
		TextView accountTypeTv = (TextView) contentView.findViewById(R.id.dept_accounttype_tv);
		String strAccountType = (String) accountContent.get(Comm.ACCOUNT_TYPE);
		accountTypeTv.setText(LocalData.AccountType.get(strAccountType));
		// Layout账户别名
		nickNameLayout = (LinearLayout) contentView.findViewById(R.id.dept_nickname_layout);
		// Layout修改账户别名
		modifyNickNameLayout = (FrameLayout) contentView.findViewById(R.id.fl_nickname);
		// TextView 账户别名
		accountNickName = (TextView) contentView.findViewById(R.id.dept_nickname_tv);
		// EditText 账户别名
		nickNameEt = (EditText) contentView.findViewById(R.id.et_acc_nickname);
		EditTextUtils.setLengthMatcher((BaseActivity) currentContent, nickNameEt, 20);

		Button updateNickNameBtn = (Button) contentView.findViewById(R.id.btn_update_nickname);

		accountNickName.setText((String) accountContent.get(Comm.NICKNAME));
		ImageView modifyNickNameIv = (ImageView) contentView.findViewById(R.id.img_dept_update_nickname);
		modifyNickNameIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nickNameLayout.setVisibility(View.GONE);
				modifyNickNameLayout.setVisibility(View.VISIBLE);
				nickNameEt.requestFocus();
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(nickNameEt, 0);
				nickNameEt.setText(accountNickName.getText().toString());
				nickNameEt.setSelection(accountNickName.getText().toString().length());
			}
		});

		updateNickNameBtn.setOnClickListener(onModifyNickNameListener);

		// TextView 账号
		TextView accountNo = (TextView) contentView.findViewById(R.id.dept_accountno_tv);
		String strAccNo = (String) accountContent.get(Comm.ACCOUNTNUMBER);
		accountNo.setText(StringUtil.getForSixForString(strAccNo));

		// 存折号Layout
		LinearLayout bankbookLayout = (LinearLayout) contentView.findViewById(R.id.dept_bankbook_layout);
		// 存单Layout
		LinearLayout banksheetLayout = (LinearLayout) contentView.findViewById(R.id.dept_banksheet_layout);
		// 开户日期Layout
		LinearLayout opendateLayout = (LinearLayout) contentView.findViewById(R.id.dept_opendate_layout);
		// 开户日期TextView
		TextView opendateTv = (TextView) contentView.findViewById(R.id.dept_opendate_tv);

		String tmp_accType = (String) accountContent.get(Comm.ACCOUNT_TYPE);
		// 如果是教育续存 或者 零存整取 没有存折存单 返回的accountDetaiList里面只有一个数据
		if (DeptBaseActivity.EDUCATION_SAVE1.equals(tmp_accType) || DeptBaseActivity.ZERO_SAVE1.equals(tmp_accType)
				|| DeptBaseActivity.ZERO_SAVE2.equals(tmp_accType) || DeptBaseActivity.EDUCATION_SAVE2.equals(tmp_accType)) {
			bankbookLayout.setVisibility(View.GONE);
			banksheetLayout.setVisibility(View.GONE);
			opendateLayout.setVisibility(View.VISIBLE);
//			Map<String, Object> content = accountDetaiList.get(position);
			opendateTv.setText((String) positionMap.get(Dept.OPENDATE));
			DeptDataCenter.getInstance().setCurDetailContent(positionMap);
			refreshMySaveContent(positionMap, onCreateNoticeListener, onCheckoutListener, onContinueSaveListener);
		} else {
			// Spinner 存折册号 根据账户列表来显示
			// 返回数据里面 存折号和存单是没有关联的 重新组装数据
//			volumesAndCdnumbers = new ArrayList<Map<String, Object>>();
//
//			final List<String> volumes = new ArrayList<String>();
//			// 找出列表中所有不相同的存折册号
//			for (int i = 0; i < positionMap.size(); i++) {
//				Map<String, Object> detaimap = (Map<String, Object>) positionMap.get(i);
//
//				String volumeNumber = (String) detaimap.get(Dept.VOLUME_NUMBER);
//				if (StringUtil.isNullOrEmpty(volumeNumber)) {
//					continue;
//				}
//				if (volumes.size() > 0) {
//					if (!volumes.contains(volumeNumber)) {
//						volumes.add(volumeNumber);
//					}
//				} else {
//					volumes.add(volumeNumber);
//				}
//			}
//			if (StringUtil.isNullOrEmpty(volumes)) {
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						currentContent.getResources().getString(R.string.no_cdnumber));
//				return contentView;
//			}

//			Map<String, Object> content = accountDetaiList.get(position);
			// 存折册号
			TextView depyNumber = (TextView) contentView.findViewById(R.id.depy_number);
			String volumeNumber = (String) positionMap.get(Dept.VOLUME_NUMBER);
			depyNumber.setText(volumeNumber);

			// 存单号
			TextView deptFixNumber = (TextView) contentView.findViewById(R.id.dept_fix_number);
			String cdNumber = (String) positionMap.get(Dept.CD_NUMBER);
			deptFixNumber.setText(cdNumber);

			DeptDataCenter.getInstance().setCurDetailContent(positionMap);
			refreshMySaveContent(positionMap, onCreateNoticeListener, onCheckoutListener, onContinueSaveListener);
		}
		return contentView;
	}

	/**
	 * 存款账户详情框（定期一本通） 修改 无效账户 无支取按钮
	 * 
	 * @param selectedPosition
	 *            用户选择账户
	 * @param saveType
	 *            存储类型
	 * @param onBankbookListener
	 *            存折下拉框监听
	 * @param onBanksheetListener
	 *            存单号下拉监听
	 * @param onCreateNoticeListener
	 *            建立通知下拉监听
	 * @param onCheckoutListener
	 *            支取按钮监听
	 * @return
	 */
	public View initMySaveDialogView(int selectedPosition, Map<String,Object> positionMap, final View.OnClickListener onCreateNoticeListener,
			final View.OnClickListener onContinueSaveListener, final View.OnClickListener onModifyNickNameListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.dept_myreg_save_my_detail, null);

		// 当前账户
		accountContent = DeptDataCenter.getInstance().getMyRegAccountList().get(selectedPosition);
		// 将当前选择的账户 数据储存
		DeptDataCenter.getInstance().setAccountContent(accountContent);

		if (accountContent == null) {
			return contentView;
		}
		// 账户详情列表
//		accountDetaiList = (List<Map<String, Object>>) DeptDataCenter.getInstance().getAccountDetailCallBackMap()
//				.get(ConstantGloble.ACC_DETAILIST);

		ImageButton closeIb = (ImageButton) contentView.findViewById(R.id.dept_close_ib);
		closeIb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
			}
		});

		// TextView 账户类型
		TextView accountTypeTv = (TextView) contentView.findViewById(R.id.dept_accounttype_tv);
		String strAccountType = (String) accountContent.get(Comm.ACCOUNT_TYPE);
		accountTypeTv.setText(LocalData.AccountType.get(strAccountType));
		// Layout账户别名
		nickNameLayout = (LinearLayout) contentView.findViewById(R.id.dept_nickname_layout);
		// Layout修改账户别名
		modifyNickNameLayout = (FrameLayout) contentView.findViewById(R.id.fl_nickname);
		// TextView 账户别名
		accountNickName = (TextView) contentView.findViewById(R.id.dept_nickname_tv);
		// EditText 账户别名
		nickNameEt = (EditText) contentView.findViewById(R.id.et_acc_nickname);
		EditTextUtils.setLengthMatcher((BaseActivity) currentContent, nickNameEt, 20);

//		Button updateNickNameBtn = (Button) contentView.findViewById(R.id.btn_update_nickname);

		accountNickName.setText((String) accountContent.get(Comm.NICKNAME));
		ImageView modifyNickNameIv = (ImageView) contentView.findViewById(R.id.img_dept_update_nickname);
		modifyNickNameIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nickNameLayout.setVisibility(View.GONE);
				modifyNickNameLayout.setVisibility(View.VISIBLE);
				nickNameEt.requestFocus();
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(nickNameEt, 0);
				nickNameEt.setText(accountNickName.getText().toString());
				nickNameEt.setSelection(accountNickName.getText().toString().length());
			}
		});

//		updateNickNameBtn.setOnClickListener(onModifyNickNameListener);

		// TextView 账号
		TextView accountNo = (TextView) contentView.findViewById(R.id.dept_accountno_tv);
		String strAccNo = (String) accountContent.get(Comm.ACCOUNTNUMBER);
		accountNo.setText(StringUtil.getForSixForString(strAccNo));

		// 存折号Layout
		LinearLayout bankbookLayout = (LinearLayout) contentView.findViewById(R.id.dept_bankbook_layout);
		// 存单Layout
		LinearLayout banksheetLayout = (LinearLayout) contentView.findViewById(R.id.dept_banksheet_layout);
		// 开户日期Layout
		LinearLayout opendateLayout = (LinearLayout) contentView.findViewById(R.id.dept_opendate_layout);
		// 开户日期TextView
		TextView opendateTv = (TextView) contentView.findViewById(R.id.dept_opendate_tv);

		String tmp_accType = (String) accountContent.get(Comm.ACCOUNT_TYPE);
		// 如果是教育续存 或者 零存整取 没有存折存单 返回的accountDetaiList里面只有一个数据
		if (DeptBaseActivity.EDUCATION_SAVE1.equals(tmp_accType) || DeptBaseActivity.ZERO_SAVE1.equals(tmp_accType)
				|| DeptBaseActivity.ZERO_SAVE2.equals(tmp_accType) || DeptBaseActivity.EDUCATION_SAVE2.equals(tmp_accType)) {
			bankbookLayout.setVisibility(View.GONE);
			banksheetLayout.setVisibility(View.GONE);
			opendateLayout.setVisibility(View.VISIBLE);
//			Map<String, Object> content = accountDetaiList.get(position);
			opendateTv.setText((String) positionMap.get(Dept.OPENDATE));
			DeptDataCenter.getInstance().setCurDetailContent(positionMap);
			refreshMyContent(positionMap, onCreateNoticeListener, onContinueSaveListener);
		} else {
//			Map<String, Object> content = accountDetaiList.get(position);
			// 存折册号
			TextView depyNumber = (TextView) contentView.findViewById(R.id.depy_number);
			String volumeNumber = (String) positionMap.get(Dept.VOLUME_NUMBER);
			depyNumber.setText(volumeNumber);

			// 存单号
			TextView deptFixNumber = (TextView) contentView.findViewById(R.id.dept_fix_number);
			String cdNumber = (String) positionMap.get(Dept.CD_NUMBER);
			deptFixNumber.setText(cdNumber);

			DeptDataCenter.getInstance().setCurDetailContent(positionMap);
			refreshMyContent(positionMap, onCreateNoticeListener, onContinueSaveListener);
		}
		return contentView;
	}

	// /////////////////////// 账户管理 我的定期存款 列表详情页点击事件 end//////////////////

	/**
	 * 刷新修改账户别名Layout
	 */
	public void refreshModifyNickName() {
		modifyNickNameLayout.setVisibility(View.GONE);
		nickNameLayout.setVisibility(View.VISIBLE);
		accountNickName.setText(nickNameEt.getText().toString().trim());
	}

	public View initMySaveWholesaveDetailDialogView(int position, final View.OnClickListener onCreateNoticeListener,
			final View.OnClickListener onCheckoutListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.dept_my_save_detail, null);

		// 当前账户
		accountContent = DeptDataCenter.getInstance().getWholesaveAccountList().get(position);
		// 将当前选择的账户 数据储存
		DeptDataCenter.getInstance().setAccountContent(accountContent);

		if (accountContent == null) {
			return contentView;
		}
		// 账户详情列表
		accountDetaiList = (List<Map<String, Object>>) DeptDataCenter.getInstance().getAccountDetailCallBackMap()
				.get(ConstantGloble.ACC_DETAILIST);

		// TextView 账户类型
		TextView accountTypeTv = (TextView) contentView.findViewById(R.id.dept_accounttype_tv);
		String strAccountType = (String) accountContent.get(Comm.ACCOUNT_TYPE);
		accountTypeTv.setText(LocalData.AccountType.get(strAccountType));
		// TextView 账户别名
		TextView accountNickName = (TextView) contentView.findViewById(R.id.dept_nickname_tv);
		accountNickName.setText((String) accountContent.get(Comm.NICKNAME));
		// TextView 账号
		TextView accountNo = (TextView) contentView.findViewById(R.id.dept_accountno_tv);
		String strAccNo = (String) accountContent.get(Comm.ACCOUNTNUMBER);
		accountNo.setText(StringUtil.getForSixForString(strAccNo));

		// Spinner 存折册号 根据账户列表来显示
		// 返回数据里面 存折号和存单是没有关联的 重新组装数据
		volumesAndCdnumbers = new ArrayList<Map<String, Object>>();

		final List<String> volumes = new ArrayList<String>();
		// 找出列表中所有不相同的存折册号
		for (int i = 0; i < accountDetaiList.size(); i++) {
			Map<String, Object> detaimap = accountDetaiList.get(i);
			String volumeNumber = (String) detaimap.get(Dept.VOLUME_NUMBER);
			if (volumes.size() > 0) {
				if (!volumes.contains(volumeNumber)) {
					volumes.add(volumeNumber);
				}
			} else {
				volumes.add(volumeNumber);
			}
		}

		// 讲存折册号 和 存单号 对应起来
		for (int i = 0; i < volumes.size(); i++) {
			ArrayList<Map<String, Object>> list = null;
			list = new ArrayList<Map<String, Object>>();
			Map<String, Object> mapContent = null;
			for (int j = 0; j < accountDetaiList.size(); j++) {
				Map<String, Object> detaimap = accountDetaiList.get(j);
				if (!((String) detaimap.get(Dept.STATUS)).equals("00") && !((String) detaimap.get(Dept.STATUS)).equals("V")) // 过滤无效存单
					// if(((String)detaimap.get(Dept.STATUS)).equals("V") ||
					// ((String)detaimap.get(Dept.STATUS)).equals("00"))
					// 00为有效
					// 01
					// 无效
					continue;
				String volumeNumber = (String) detaimap.get(Dept.VOLUME_NUMBER);
				String cdNumber = (String) detaimap.get(Dept.CD_NUMBER);
				if (volumeNumber.equals(volumes.get(i))) {
					mapContent = new HashMap<String, Object>();
					mapContent.put(ConstantGloble.VOL, cdNumber);
					mapContent.put(ConstantGloble.CONTENT, accountDetaiList.get(j));
					list.add(mapContent);
				}
			}

			if (list.isEmpty()) // 如果存单列表为空， 不显示当前存册
				continue;

			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Dept.VOLUME_NUMBER, volumes.get(i));
			map.put(Dept.CD_NUMBER, list);
			volumesAndCdnumbers.add(map);
		}

		// 默认显示 第一个存折册号
		// Spinner 存折册号
		Spinner bankbookSpinner = (Spinner) contentView.findViewById(R.id.dept_bankbook_spinner);
		ArrayAdapter<String> bankbookAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(),
				R.layout.dept_spinner, volumes);
		bankbookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bankbookSpinner.setAdapter(bankbookAdapter);

		// bankbookSpinner.setOnItemSelectedListener(onBankbookListener);
		// 默认显示第一个存单号
		List<Map<String, Object>> cds = (List<Map<String, Object>>) volumesAndCdnumbers.get(0).get(Dept.CD_NUMBER);
		List<String> cdnums = new ArrayList<String>();
		for (int i = 0; i < cds.size(); i++) {
			String vol = (String) cds.get(i).get(ConstantGloble.VOL);
			cdnums.add(vol);
		}

		// Spinner 存单号
		final Spinner banksheetSpinner = (Spinner) contentView.findViewById(R.id.dept_banksheet_spinner);
		banksheetAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(), R.layout.dept_spinner,
				cdnums);
		banksheetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		banksheetSpinner.setAdapter(banksheetAdapter);
		banksheetSpinner.setSelection(0);

		bankbookSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				currentList = (List<Map<String, Object>>) volumesAndCdnumbers.get(position).get(Dept.CD_NUMBER);
				// 从新组装一个cdnumberlist
				List<String> cdlist = new ArrayList<String>();
				for (int i = 0; i < currentList.size(); i++) {
					String vol = (String) currentList.get(i).get(ConstantGloble.VOL);
					cdlist.add(vol);
				}
				banksheetAdapter = new ArrayAdapter<String>(BaseDroidApp.getInstanse().getCurrentAct(),
						R.layout.dept_spinner, cdlist);
				banksheetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				banksheetSpinner.setAdapter(banksheetAdapter);
				// refreshMySaveContent(content);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		banksheetSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Map<String, Object> content = (Map<String, Object>) currentList.get(position).get(ConstantGloble.CONTENT);
				DeptDataCenter.getInstance().setCurDetailContent(content);
				refreshMySaveContent(content, onCreateNoticeListener, onCheckoutListener, null);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		ImageButton closeIb = (ImageButton) contentView.findViewById(R.id.dept_close_ib);
		closeIb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
			}
		});

		return contentView;
	}

	/**
	 * 刷新账号详情内容
	 * 
	 * @param position
	 */
	public void refreshMySaveContent(Map<String, Object> content, View.OnClickListener onCreateNoticeListener,
			View.OnClickListener onCheckoutListener, View.OnClickListener onContinueSaveListener) {
		RelativeLayout linearLayout = (RelativeLayout) contentView.findViewById(R.id.dept_my_detail_content);
		RelativeLayout contentLayout = (RelativeLayout) LayoutInflater.from(BaseDroidApp.getInstanse().getCurrentAct())
				.inflate(R.layout.dept_my_save_detail_content, null);

		// 转存方式LinearLayout
		final LinearLayout ll_convert_type = (LinearLayout) contentLayout.findViewById(R.id.ll_convert_type);
		// 账户状态LinearLayout
		LinearLayout ll_acc_state = (LinearLayout) contentLayout.findViewById(R.id.ll_acc_state);
		// 可支取金额layout
		LinearLayout availableLayout = (LinearLayout) contentLayout.findViewById(R.id.dept_available_balance_layout);
		// 月存金额layout
		LinearLayout monthSaveLayout = (LinearLayout) contentLayout.findViewById(R.id.dept_month_save_layout);
		// 当前余额layout
		LinearLayout currentMontyLayout = (LinearLayout) contentLayout.findViewById(R.id.dept_current_money_layout);
		// 起期layout
		LinearLayout startAccrualLayout = (LinearLayout) contentLayout.findViewById(R.id.layout_start_accrual_day);
		// 存款种类layout
		LinearLayout ll_deposit_type = (LinearLayout) contentLayout.findViewById(R.id.ll_deposit_type);
		// 到期日layout
		final LinearLayout endDayLayout = (LinearLayout) contentLayout.findViewById(R.id.layout_end_day);
		// 起期日layout
		final LinearLayout sumDayLayout = (LinearLayout) contentLayout.findViewById(R.id.layout_sum_day);
		// 月存金额textview
		TextView monthSaveTv = (TextView) contentLayout.findViewById(R.id.dept_month_save_tv);
		// 存单金额textview
		LinearLayout moneyAmountLayout = (LinearLayout) contentLayout.findViewById(R.id.layout_money_amount);
		String strMonthSave = (String) content.get(Dept.MONTH_BALANCE);
		if (StringUtil.isNullOrEmpty(strMonthSave)) {
			strMonthSave = "0.0";
		}
		// 当前余额textview
		TextView currentMoneyTv = (TextView) contentLayout.findViewById(R.id.dept_current_money_tv);

		TextView currencyTv = (TextView) contentLayout.findViewById(R.id.dept_currency_tv);
		TextView cashRemitTv = (TextView) contentLayout.findViewById(R.id.dept_cashremit_tv);
		TextView availableBalanceTv = (TextView) contentLayout.findViewById(R.id.dept_available_balance_tv);
		TextView labelConvert = (TextView) contentLayout.findViewById(R.id.dept_is_convert_tv);
		TextView convertTypeTv = (TextView) contentLayout.findViewById(R.id.dept_convert_type_tv);
		TextView cdPeriodTv = (TextView) contentLayout.findViewById(R.id.dept_cd_period_tv);
		TextView cdPeriodTvTop = (TextView) contentLayout.findViewById(R.id.dept_cd_period_tv_top);
		TextView moneyAmountTv = (TextView) contentLayout.findViewById(R.id.dept_cd_money_amount_tv);
		TextView cdInterestStartsDateTv = (TextView) contentLayout.findViewById(R.id.dept_cd_interest_startsdate_tv);
		TextView cdInterestEndDateTv = (TextView) contentLayout.findViewById(R.id.dept_cd_interest_enddate_tv);
		TextView cdCloseDateTv = (TextView) contentLayout.findViewById(R.id.dept_cd_close_date_tv);
		TextView saveTypeTv = (TextView) contentLayout.findViewById(R.id.dept_save_type_tv);
		TextView interestRateTv = (TextView) contentLayout.findViewById(R.id.dept_interest_rate_tv);
		TextView tv_acc_state = (TextView) contentLayout.findViewById(R.id.tv_acc_state);
		// 支取按钮
		Button checkoutBtn = (Button) contentView.findViewById(R.id.dept_checkout_btn);
		// 建立通知按钮
		Button createNoticeBtn = (Button) contentView.findViewById(R.id.dept_create_notice_btn);
		// 币种
		String currencyCode = (String) content.get(Comm.CURRENCYCODE);
		currencyTv.setText(LocalData.Currency.get(currencyCode));
		// 钞汇标志
		String strCashRemit = (String) content.get(Dept.CASHREMIT);
		cashRemitTv.setText(LocalData.CurrencyCashremit.get(strCashRemit));
		// 如果币种是人名币 钞汇标志不显示
		LinearLayout cashRemitLayout = (LinearLayout) contentLayout.findViewById(R.id.dept_cashremit_layout);
		if (currencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {
			cashRemitLayout.setVisibility(View.INVISIBLE);
		} else {
			cashRemitLayout.setVisibility(View.VISIBLE);
		}
		TextView curAndCashTv = (TextView) contentLayout.findViewById(R.id.dept_currency_cashremit_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(currentContent, curAndCashTv);

		// 可支取余额

		String strAvailableBalance = (String) content.get(Dept.AVAILABLE_BALANCE);
		availableBalanceTv.setText(StringUtil.parseStringCodePattern(currencyCode , strAvailableBalance, 2));

		// 是否自动转存
		TextView isConvertTv = (TextView) contentLayout.findViewById(R.id.dept_is_convert_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(currentContent, isConvertTv);
		// 是否自动转存
		String strConvertType = (String) content.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		// convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		// 存期
		String strCdPeriod = (String) content.get(Dept.CD_PERIOD);
		cdPeriodTv.setText(LocalData.CDPeriod.get(strCdPeriod));
		cdPeriodTvTop.setText(LocalData.CDPeriod.get(strCdPeriod));
		// 起期日
		cdInterestStartsDateTv.setText((String) content.get(Dept.INTEREST_STARTSDATE));
		// 到期日
		cdInterestEndDateTv.setText((String) content.get(Dept.INTEREST_ENDDATE));
		// 结期日
		cdCloseDateTv.setText((String) content.get(Dept.SETTLEMENT_DATE));
		// 存款种类
		String strType = (String) content.get(Dept.TYPE);
		saveTypeTv.setText(LocalData.fixAccTypeMap.get(strType));
		// 利率
		interestRateTv.setText((String) content.get(Dept.INTEREST_RATE));
		// 根据账户类型 和 存单类型 显示不同的界面
		String accountType = (String) accountContent.get(Comm.ACCOUNT_TYPE);

		String strContinueSaveBtn = this.getContext().getResources().getString(R.string.continue_save_btn);

		String accStatus = (String) accountContent.get(Comm.ACCOUNTSTATUS);
		tv_acc_state.setText(LocalData.SubAccountsStatus.get(accStatus));

		// 非约定转存时 “到期日”不显示
		if (strConvertType != null && strConvertType.equals(ConstantGloble.CONVERTTYPE_N)) {
			endDayLayout.setVisibility(View.GONE);
		}
		if (accountType.equals(ConstantGloble.ACC_TYPE_REG)) {// 定期一本通
			moneyAmountLayout.setVisibility(View.VISIBLE);

			String bookMoney = (String) content.get(Dept.BOOKBALANCE);

			// 如果币种是韩元和日元去除小数点显示整数位
			moneyAmountTv.setText(StringUtil.parseStringCodePattern(currencyCode , bookMoney, 2));
			moneyAmountTv.setTextColor(currentContent.getResources().getColor(R.color.red));

			sumDayLayout.setVisibility(View.GONE);
			// 整存整取，定活两便，通知存款才会显示“起息日”一项
			startAccrualLayout.setVisibility(View.VISIBLE);
			// 定期一本通下面存单又分为 定期一本通 定活两便 通知存款
			if (content.get(Dept.TYPE).equals(DeptBaseActivity.WHOE_SAVE)) {// 整存整取
				checkoutBtn.setOnClickListener(onCheckoutListener);
				createNoticeBtn.setVisibility(View.GONE);
				BottomButtonUtils.setSingleLineStyleGray(checkoutBtn);
				labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
				convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
			} else if (content.get(Dept.TYPE).equals(DeptBaseActivity.RANDOM_SAVE)) {// 定活两便
				ll_convert_type.setVisibility(View.GONE);
				endDayLayout.setVisibility(View.GONE);
				sumDayLayout.setVisibility(View.GONE);
				createNoticeBtn.setVisibility(View.GONE);
				BottomButtonUtils.setSingleLineStyleGray(checkoutBtn);
				// startAccrualLayout.setVisibility(View.GONE);
				checkoutBtn.setOnClickListener(onCheckoutListener);
				((LinearLayout) contentLayout.findViewById(R.id.layout_period)).setVisibility(View.GONE);
				((LinearLayout) contentLayout.findViewById(R.id.layout_period_top)).setVisibility(View.GONE);
				// labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
				// convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
			} else if (content.get(Dept.TYPE).equals(DeptBaseActivity.NOTIFY_SAVE)) {// 通知存款
				if (strConvertType != null && strConvertType.equals(ConstantGloble.CONVERTTYPE_N)) {
					createNoticeBtn.setOnClickListener(onCreateNoticeListener);
					createNoticeBtn.setVisibility(View.VISIBLE);
					BottomButtonUtils.setTwoButtonLineStyleGray(createNoticeBtn);
					BottomButtonUtils.setTwoButtonLineStyleGray(checkoutBtn);
				} else {
					createNoticeBtn.setVisibility(View.GONE);
					BottomButtonUtils.setSingleLineStyleGray(checkoutBtn);
				}
				((LinearLayout) contentLayout.findViewById(R.id.layout_period)).setVisibility(View.GONE);
				((LinearLayout) contentLayout.findViewById(R.id.layout_period_top)).setVisibility(View.VISIBLE);
				labelConvert.setText(currentContent.getResources().getString(R.string.promise_way));
				convertTypeTv.setText(LocalData.ConventionConvertType.get(strConvertType));
				cdPeriodTvTop.setText(LocalData.InfoDeposit.get(strCdPeriod));
				checkoutBtn.setOnClickListener(onCheckoutListener);
			} else {
				labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
				createNoticeBtn.setVisibility(View.GONE);
				BottomButtonUtils.setSingleLineStyleGray(checkoutBtn);
				convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
			}
		} else if (accountType.equals(ConstantGloble.ACC_TYPE_EDU)) {// 教育储蓄
			availableLayout.setVisibility(View.GONE);
			ll_convert_type.setVisibility(View.GONE);
			ll_deposit_type.setVisibility(View.GONE);
			monthSaveLayout.setVisibility(View.VISIBLE);// 月存金额
			startAccrualLayout.setVisibility(View.VISIBLE);
			endDayLayout.setVisibility(View.VISIBLE);
			currentMontyLayout.setVisibility(View.VISIBLE);//
			ll_acc_state.setVisibility(View.VISIBLE);// 显示账户状态
			curAndCashTv.setText(R.string.currency);

			monthSaveTv.setText(StringUtil.parseStringCodePattern(currencyCode , strMonthSave, 2));
			currentMoneyTv.setText(StringUtil.parseStringCodePattern(currencyCode , strAvailableBalance, 2));

			checkoutBtn.setText(strContinueSaveBtn);
			checkoutBtn.setOnClickListener(onContinueSaveListener);
			labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
			convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		} else if (accountType.equals(ConstantGloble.ACC_TYPE_ZOR)) {// 零存整取
			availableLayout.setVisibility(View.GONE);
			ll_convert_type.setVisibility(View.GONE);
			ll_deposit_type.setVisibility(View.GONE);
			monthSaveLayout.setVisibility(View.VISIBLE);
			ll_acc_state.setVisibility(View.VISIBLE);// 显示账户状态
			curAndCashTv.setText(R.string.currency);
			startAccrualLayout.setVisibility(View.VISIBLE);// 起期日
			endDayLayout.setVisibility(View.VISIBLE);// 到期日
			currentMontyLayout.setVisibility(View.VISIBLE);

			monthSaveTv.setText(StringUtil.parseStringCodePattern(currencyCode , strMonthSave, 2));
			currentMoneyTv.setText(StringUtil.parseStringCodePattern(currencyCode , strAvailableBalance, 2));

			checkoutBtn.setText(strContinueSaveBtn);
			checkoutBtn.setOnClickListener(onContinueSaveListener);
			labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
			convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		} else if (accountType.equals(ConstantGloble.ACC_TYPE_CBQX)) {// 存本取息
			availableLayout.setVisibility(View.GONE);
			monthSaveLayout.setVisibility(View.VISIBLE);
			currentMontyLayout.setVisibility(View.VISIBLE);

			monthSaveTv.setText(StringUtil.parseStringCodePattern(currencyCode , strMonthSave, 2));
			currentMoneyTv.setText(StringUtil.parseStringCodePattern(currencyCode , strAvailableBalance, 2));

			checkoutBtn.setVisibility(View.GONE);
			labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
			convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		} else {
			labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
			convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		}
		linearLayout.removeAllViews();
		linearLayout.addView(contentLayout);
	}

	/**
	 * 刷新账号详情内容 修改 无支取按钮
	 * 
	 * @param position
	 */
	public void refreshMyContent(Map<String, Object> content, View.OnClickListener onCreateNoticeListener,
			View.OnClickListener onContinueSaveListener) {
		RelativeLayout linearLayout = (RelativeLayout) contentView.findViewById(R.id.dept_my_detail_content);
		RelativeLayout contentLayout = (RelativeLayout) LayoutInflater.from(BaseDroidApp.getInstanse().getCurrentAct())
				.inflate(R.layout.dept_my_post_mney_content, null);

		// 转存方式LinearLayout
		final LinearLayout ll_convert_type = (LinearLayout) contentLayout.findViewById(R.id.ll_convert_type);
		// 账户状态LinearLayout
		LinearLayout ll_acc_state = (LinearLayout) contentLayout.findViewById(R.id.ll_acc_state);
		// 可支取金额layout
		LinearLayout availableLayout = (LinearLayout) contentLayout.findViewById(R.id.dept_available_balance_layout);
		// 月存金额layout
		LinearLayout monthSaveLayout = (LinearLayout) contentLayout.findViewById(R.id.dept_month_save_layout);
		// 当前余额layout
		LinearLayout currentMontyLayout = (LinearLayout) contentLayout.findViewById(R.id.dept_current_money_layout);
		// 起期layout
		LinearLayout startAccrualLayout = (LinearLayout) contentLayout.findViewById(R.id.layout_start_accrual_day);
		// 存款种类layout
		LinearLayout ll_deposit_type = (LinearLayout) contentLayout.findViewById(R.id.ll_deposit_type);
		// 到期日layout
		final LinearLayout endDayLayout = (LinearLayout) contentLayout.findViewById(R.id.layout_end_day);
		// 起期日layout
		final LinearLayout sumDayLayout = (LinearLayout) contentLayout.findViewById(R.id.layout_sum_day);
		// 月存金额textview
		TextView monthSaveTv = (TextView) contentLayout.findViewById(R.id.dept_month_save_tv);
		// 存单金额textview
		LinearLayout moneyAmountLayout = (LinearLayout) contentLayout.findViewById(R.id.layout_money_amount);
		String strMonthSave = (String) content.get(Dept.MONTH_BALANCE);
		if (StringUtil.isNullOrEmpty(strMonthSave)) {
			strMonthSave = "0.00";
		}
		// 当前余额textview
		TextView currentMoneyTv = (TextView) contentLayout.findViewById(R.id.dept_current_money_tv);

		TextView currencyTv = (TextView) contentLayout.findViewById(R.id.dept_currency_tv);
		TextView cashRemitTv = (TextView) contentLayout.findViewById(R.id.dept_cashremit_tv);
		TextView availableBalanceTv = (TextView) contentLayout.findViewById(R.id.dept_available_balance_tv);
		TextView labelConvert = (TextView) contentLayout.findViewById(R.id.dept_is_convert_tv);
		TextView convertTypeTv = (TextView) contentLayout.findViewById(R.id.dept_convert_type_tv);
		TextView cdPeriodTv = (TextView) contentLayout.findViewById(R.id.dept_cd_period_tv);
		TextView cdPeriodTvTop = (TextView) contentLayout.findViewById(R.id.dept_cd_period_tv_top);
		TextView moneyAmountTv = (TextView) contentLayout.findViewById(R.id.dept_cd_money_amount_tv);
		TextView cdInterestStartsDateTv = (TextView) contentLayout.findViewById(R.id.dept_cd_interest_startsdate_tv);
		TextView cdInterestEndDateTv = (TextView) contentLayout.findViewById(R.id.dept_cd_interest_enddate_tv);
		TextView cdCloseDateTv = (TextView) contentLayout.findViewById(R.id.dept_cd_close_date_tv);
		TextView saveTypeTv = (TextView) contentLayout.findViewById(R.id.dept_save_type_tv);
		TextView interestRateTv = (TextView) contentLayout.findViewById(R.id.dept_interest_rate_tv);
		TextView tv_acc_state = (TextView) contentLayout.findViewById(R.id.tv_acc_state);
		// 币种
		String currencyCode = (String) content.get(Comm.CURRENCYCODE);
		currencyTv.setText(LocalData.Currency.get(currencyCode));
		// 钞汇标志
		String strCashRemit = (String) content.get(Dept.CASHREMIT);
		cashRemitTv.setText(LocalData.CurrencyCashremit.get(strCashRemit));
		// 如果币种是人名币 钞汇标志不显示
		LinearLayout cashRemitLayout = (LinearLayout) contentLayout.findViewById(R.id.dept_cashremit_layout);
		if (currencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {
			cashRemitLayout.setVisibility(View.INVISIBLE);
		} else {
			cashRemitLayout.setVisibility(View.VISIBLE);
		}
		TextView curAndCashTv = (TextView) contentLayout.findViewById(R.id.dept_currency_cashremit_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(currentContent, curAndCashTv);

		// 可支取余额
		String strAvailableBalance = (String) content.get(Dept.AVAILABLE_BALANCE);
		if (strAvailableBalance.equals("0")) { // 如果可用余额为零显示 0.00
			availableBalanceTv.setText("0.00");
		}else{ // 正常显示可用余额
			availableBalanceTv.setText(StringUtil.parseStringCodePattern(currencyCode , strAvailableBalance, 2));	
		}
		// 是否自动转存
		TextView isConvertTv = (TextView) contentLayout.findViewById(R.id.dept_is_convert_tv);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(currentContent, isConvertTv);
		// 是否自动转存
		String strConvertType = (String) content.get(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE);
		// convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		// 存期
		String strCdPeriod = (String) content.get(Dept.CD_PERIOD);
		cdPeriodTv.setText(LocalData.CDPeriod.get(strCdPeriod));
		cdPeriodTvTop.setText(LocalData.CDPeriod.get(strCdPeriod));
		// 起期日
		cdInterestStartsDateTv.setText((String) content.get(Dept.INTEREST_STARTSDATE));
		// 到期日
		cdInterestEndDateTv.setText((String) content.get(Dept.INTEREST_ENDDATE));
		// 结期日
		cdCloseDateTv.setText((String) content.get(Dept.SETTLEMENT_DATE));
		// 存款种类
		String strType = (String) content.get(Dept.TYPE);
		saveTypeTv.setText(LocalData.fixAccTypeMap.get(strType));
		// 利率
		interestRateTv.setText((String) content.get(Dept.INTEREST_RATE));
		// 根据账户类型 和 存单类型 显示不同的界面
		String accountType = (String) accountContent.get(Comm.ACCOUNT_TYPE);

		String strContinueSaveBtn = this.getContext().getResources().getString(R.string.continue_save_btn);

		// 状态
		String accStatus = (String) accountContent.get(Comm.ACCOUNTSTATUS);
		tv_acc_state.setText(LocalData.SubAccountsStatus.get(accStatus));
		tv_acc_state.setVisibility(View.GONE);
		// 非约定转存时 “到期日”不显示
		if (strConvertType != null && strConvertType.equals(ConstantGloble.CONVERTTYPE_N)) {
			endDayLayout.setVisibility(View.GONE);
		}
		if (accountType.equals(ConstantGloble.ACC_TYPE_REG)) {// 定期一本通
			moneyAmountLayout.setVisibility(View.VISIBLE);
			String bookMoney = (String) content.get(Dept.BOOKBALANCE);
			// 如果币种是韩元和日元去除小数点显示整数位
			moneyAmountTv.setText(StringUtil.parseStringCodePattern(currencyCode , bookMoney, 2));
			moneyAmountTv.setTextColor(currentContent.getResources().getColor(R.color.red));
			sumDayLayout.setVisibility(View.GONE);
			// 整存整取，定活两便，通知存款才会显示“起息日”一项
			startAccrualLayout.setVisibility(View.VISIBLE);
			// 定期一本通下面存单又分为 定期一本通 定活两便 通知存款
			if (content.get(Dept.TYPE).equals(DeptBaseActivity.WHOE_SAVE)) {// 整存整取
				labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
				convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
			} else if (content.get(Dept.TYPE).equals(DeptBaseActivity.RANDOM_SAVE)) {// 定活两便
				ll_convert_type.setVisibility(View.GONE);
				endDayLayout.setVisibility(View.GONE);
				sumDayLayout.setVisibility(View.GONE);
				((LinearLayout) contentLayout.findViewById(R.id.layout_period)).setVisibility(View.GONE);
				((LinearLayout) contentLayout.findViewById(R.id.layout_period_top)).setVisibility(View.GONE);
			} else if (content.get(Dept.TYPE).equals(DeptBaseActivity.NOTIFY_SAVE)) {// 通知存款
				((LinearLayout) contentLayout.findViewById(R.id.layout_period)).setVisibility(View.GONE);
				((LinearLayout) contentLayout.findViewById(R.id.layout_period_top)).setVisibility(View.VISIBLE);
				labelConvert.setText(currentContent.getResources().getString(R.string.promise_way));
				convertTypeTv.setText(LocalData.ConventionConvertType.get(strConvertType));
				cdPeriodTvTop.setText(LocalData.InfoDeposit.get(strCdPeriod));
			} else {
				labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
				convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
			}
		} else if (accountType.equals(ConstantGloble.ACC_TYPE_EDU)) {// 教育储蓄
			availableLayout.setVisibility(View.GONE);
			ll_convert_type.setVisibility(View.GONE);
			ll_deposit_type.setVisibility(View.GONE);
			monthSaveLayout.setVisibility(View.VISIBLE);// 月存金额
			startAccrualLayout.setVisibility(View.VISIBLE);
			endDayLayout.setVisibility(View.VISIBLE);
			currentMontyLayout.setVisibility(View.VISIBLE);//
			ll_acc_state.setVisibility(View.VISIBLE);// 显示账户状态
			curAndCashTv.setText(R.string.currency);
			monthSaveTv.setText(StringUtil.parseStringCodePattern(currencyCode , strMonthSave, 2));
			currentMoneyTv.setText(StringUtil.parseStringCodePattern(currencyCode , strAvailableBalance, 2));
			labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
			convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		} else if (accountType.equals(ConstantGloble.ACC_TYPE_ZOR)) {// 零存整取
			availableLayout.setVisibility(View.GONE);
			ll_convert_type.setVisibility(View.GONE);
			ll_deposit_type.setVisibility(View.GONE);
			monthSaveLayout.setVisibility(View.VISIBLE);
			ll_acc_state.setVisibility(View.VISIBLE);// 显示账户状态
			curAndCashTv.setText(R.string.currency);
			startAccrualLayout.setVisibility(View.VISIBLE);// 起期日
			endDayLayout.setVisibility(View.VISIBLE);// 到期日
			currentMontyLayout.setVisibility(View.VISIBLE);
			monthSaveTv.setText(StringUtil.parseStringCodePattern(currencyCode , strMonthSave, 2));
			currentMoneyTv.setText(StringUtil.parseStringCodePattern(currencyCode , strAvailableBalance, 2));
			labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
			convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		} else if (accountType.equals(ConstantGloble.ACC_TYPE_CBQX)) {// 存本取息
			availableLayout.setVisibility(View.GONE);
			monthSaveLayout.setVisibility(View.VISIBLE);
			currentMontyLayout.setVisibility(View.VISIBLE);
			monthSaveTv.setText(StringUtil.parseStringCodePattern(currencyCode , strMonthSave, 2));
			currentMoneyTv.setText(StringUtil.parseStringCodePattern(currencyCode , strAvailableBalance, 2));
			labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
			convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		} else {
			labelConvert.setText(currentContent.getResources().getString(R.string.tran_mode));
			convertTypeTv.setText(LocalData.ConvertType.get(strConvertType));
		}
		linearLayout.removeAllViews();
		linearLayout.addView(contentLayout);
	}

	/**
	 * 初始化快捷菜单视图
	 * 
	 * @param icons
	 *            视图数据
	 * @param closeListener
	 *            关闭按钮监听
	 * @param onItemClickListener
	 *            子项点击事件
	 * @return
	 */
	public View initShotcutDialog(ArrayList<ImageTextAndAct> icons, View.OnClickListener closeListener,
			OnItemClickListener onItemClickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.shotcut_dialog_layout, null);
		TabGrid shotcutGrid = (TabGrid) contentView.findViewById(R.id.shotcut_grid);
		shotcutGrid.setColNum(3);
		shotcutGrid.setRowNum(3);
		shotcutGrid.setAdapter(new ShotcutTabGridAdapter(BaseDroidApp.getInstanse().getCurrentAct(), icons,
				R.layout.welcome_grid_item_img_text));
		shotcutGrid.setOnItemClickListener(onItemClickListener);

		ImageButton closeIb = (ImageButton) contentView.findViewById(R.id.img_close);
		closeIb.setTag(TAG_CLOSE);
		closeIb.setOnClickListener(closeListener);
		return contentView;
	}

	/**
	 * 通讯提示框
	 */
	public static CustomDialog createProgressDialog(final Context con) {
		return createProgressDialog(con, null);
	}

	/**
	 * 通讯提示框
	 */
	public static CustomDialog createProgressDialog(final Context con, View.OnClickListener closeListener) {
		CustomDialog dlg = new CustomDialog(con, R.style.Theme_Dialog);
		final View.OnClickListener _listener = closeListener;
		dlg.show();
		dlg.setCancelable(false);
		LayoutInflater factory = LayoutInflater.from(con);
		// 加载progress_dialog为对话框的布局xml
//		View view = factory.inflate(R.layout.progress_dialog, null);
		View view = factory.inflate(R.layout.loading_layout_new, null);
		Animation loadingAnim = AnimUtils.getRotateCircleAnimation(con);
		ImageView ivProgressbar = (ImageView) view.findViewById(R.id.iv_progressbar);
		View btn = view.findViewById(R.id.btn_close);
		btn.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				HttpManager.stopConnect();
				if (BaseRequestThread.isConnecting) {
					// BaseHttpEngine.sClient.getConnectionManager().shutdown();
					BaseRequestThread.currentThread.interrupt();
					BaseRequestThread.isConnecting = false;
				}
				if (BaseHttpEngine.canGoBack) {
					BaseDroidApp.getInstanse().getCurrentAct().finish();
					BaseHttpEngine.canGoBack = false;
				}
				if (_listener != null) {
					_listener.onClick(v);
				}
				BaseHttpEngine.dissMissProgressDialog();
			}
		});
		view.findViewById(R.id.iv_progressbar).startAnimation(loadingAnim);

		dlg.getWindow().setContentView(view);
		ivProgressbar.startAnimation(loadingAnim);
		WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 3 / 4;
		lp.height = LayoutValue.SCREEN_WIDTH / 3;
		dlg.getWindow().setAttributes(lp);
		return dlg;
	}

	/**
	 * 通讯提示框 可以传递监听事件
	 */
	public static CustomDialog createProgressDialog(final Context con, int messageId,
			final android.view.View.OnClickListener listener) {
		CustomDialog dlg = new CustomDialog(con, R.style.Theme_Dialog);
		dlg.show();
		dlg.setCancelable(false);
		LayoutInflater factory = LayoutInflater.from(con);
		// 加载progress_dialog为对话框的布局xml
		View view = factory.inflate(R.layout.progress_dialog, null);
		((TextView) view.findViewById(R.id.tvmessage)).setText(messageId);
		Button btn = (Button) view.findViewById(R.id.btnClose);
		btn.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClick(v);
				BaseHttpEngine.dissMissProgressDialog();
			}
		});

		dlg.getWindow().setContentView(view);

		WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 3 / 4;
		lp.height = LayoutValue.SCREEN_WIDTH / 3;
		dlg.getWindow().setAttributes(lp);
		return dlg;
	}

	/**
	 * 定位通讯框
	 * 
	 * @param errorCode
	 * @param message
	 * @param onclickListener
	 * @return
	 */
	public View createLocationProgressDialog(int tag, String btnText, String message, View.OnClickListener onclickListener) {
		if (tag == 1) {
			contentView = LayoutInflater.from(currentContent).inflate(R.layout.comm_info_message_dialog, null);
			Button confirmBtn = (Button) contentView.findViewById(R.id.confirm_btn);
			confirmBtn.setOnClickListener(onclickListener);
			confirmBtn.setText(btnText);
			confirmBtn.setTag(TAG_CONFIRM);
			TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.tv_metion_msg);
			// TODO 是否显示错误码
			tvMentionMsg.setText(message);
		} else {
			contentView = LayoutInflater.from(currentContent).inflate(R.layout.blpt_layout_dialog, null);
			Button confirmBtn = (Button) contentView.findViewById(R.id.confirm_btn);
			confirmBtn.setOnClickListener(onclickListener);
			TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.tv_metion_msg);
			tvMentionMsg.setText(message);
			confirmBtn.setTag(TAG_CONFIRM);
		}
		return contentView;

	}

	public EditText getNickNameEt() {
		return nickNameEt;
	}

	/**
	 * 得到修改密码里的数据（新密码，新密码确认）
	 * 
	 * @return 返回 旧密码 新密码 新密码确认
	 */
	public HashMap<String, String> getModifyPwd() {
		String newPwdStr = null;
		String newPwdStr_RC = null;
		String newPwdEnsureStr = null;
		String newPwdEnsureStr_RC = null;
		try {
			if (newPwd != null) {
				newPwdStr = newPwd.getValue().getEncryptPassword();
				newPwdStr_RC = newPwd.getValue().getEncryptRandomNum();

			}
			if (newPwdConfirm != null) {
				newPwdEnsureStr = newPwdConfirm.getValue().getEncryptPassword();
				newPwdEnsureStr_RC = newPwdConfirm.getValue().getEncryptRandomNum();
			}
		} catch (CodeException e) {
			LogGloble.exceptionPrint(e);
		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(PASSWORD_NEW, newPwdStr);
		map.put(PASSWORD_NEW_RC, newPwdStr_RC);
		map.put(PASSWORD_NEW_CONFIRM, newPwdEnsureStr);
		map.put(PASSWORD_NEW_CONFIRM_RC, newPwdEnsureStr_RC);
		return map;
	}

	/**
	 * 修改密码框
	 * 
	 * @param onclickListener
	 * @return
	 */
	public View initSecurityFactorDialogView(OnItemClickListener onItemClickListener, SecurityFactorAdapter sfAdapter) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.security_list_layout, null);
		ListView listView = (ListView) contentView.findViewById(R.id.listview);
		listView.setAdapter(sfAdapter);
		listView.setOnItemClickListener(onItemClickListener);
		return contentView;
	}

	/**
	 * 存款管理 转入转出dialog
	 * 
	 * @param onclickListener
	 * @return
	 */
	public View initDeptTranOutInView(int flag, ListView listView, View.OnClickListener backListener,
			View.OnClickListener rightTopListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.dept_tran_out_in_dialog, null);
		RelativeLayout rl = (RelativeLayout) contentView.findViewById(R.id.sliding_menu_view);
		Button backBtn = (Button) contentView.findViewById(R.id.ib_back);
		backBtn.setOnClickListener(backListener);
		Button topRightBtn = (Button) contentView.findViewById(R.id.ib_top_right_btn);
		if (rightTopListener == null) {
			topRightBtn.setVisibility(View.GONE);
		} else {
			topRightBtn.setOnClickListener(rightTopListener);
			topRightBtn.setVisibility(View.VISIBLE);
		}
		TextView titleTv = (TextView) contentView.findViewById(R.id.tv_title);
		titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				currentContent.getResources().getDimensionPixelSize(R.dimen.textsize_one_for));
		if (flag == ConstantGloble.TRANS_ACCOUNT_OPER_OUT) {// 选择的是转出账户
			topRightBtn.setText(currentContent.getResources().getString(R.string.add_new_tranout));
			titleTv.setText(currentContent.getResources().getString(R.string.tran_acc_out_top_title));
		} else if (flag == ConstantGloble.TRANS_ACCOUNT_OPER_IN) {// 选择的是转入账户
			topRightBtn.setText(currentContent.getResources().getString(R.string.add_new_tranin));
			titleTv.setText(currentContent.getResources().getString(R.string.tran_acc_in_top_title));
		}
		rl.removeAllViews();
		rl.addView(listView);
		return contentView;
	}

	/**
	 * 存款管理 转入转出dialog
	 * 
	 * @param onclickListener
	 * @return
	 */
	public View initMobileTranOutView(ListView listView, View.OnClickListener backListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.dept_tran_out_in_dialog, null);
		RelativeLayout rl = (RelativeLayout) contentView.findViewById(R.id.sliding_menu_view);
		Button backBtn = (Button) contentView.findViewById(R.id.ib_back);
		backBtn.setOnClickListener(backListener);
		Button topRightBtn = (Button) contentView.findViewById(R.id.ib_top_right_btn);
		TextView titleTv = (TextView) contentView.findViewById(R.id.tv_title);
		topRightBtn.setText(currentContent.getResources().getString(R.string.add_new_tranout));
		titleTv.setText(currentContent.getResources().getString(R.string.tran_acc_out_top_title));
		rl.removeAllViews();
		rl.addView(listView);
		return contentView;
	}

	/**
	 * 电子支付 操作菜单
	 * 
	 * @param closeServiceListener
	 * @param modifyQuotaListener
	 * @param cancelListener
	 * @return
	 */
	public View initEpayModifyDialog(View.OnClickListener closeServiceListener, View.OnClickListener modifyQuotaListener,
			View.OnClickListener cancelListener) {
		View view = LayoutInflater.from(currentContent).inflate(R.layout.epay_bom_modify_dialog, null);
		Button bt_close_service = (Button) view.findViewById(R.id.epay_modify_close_service);
		bt_close_service.setOnClickListener(closeServiceListener);
		Button bt_modify_quota = (Button) view.findViewById(R.id.epay_modify_md_quota);
		bt_modify_quota.setOnClickListener(modifyQuotaListener);
		Button bt_cancel = (Button) view.findViewById(R.id.epay_modify_cancel);
		bt_cancel.setOnClickListener(cancelListener);

		return view;
	}
	
	/**
	 * 跨境汇款 模板管理 其他操作弹出框
	 * 
	 * @param updateNameListener
	 *            修改模板名称监听
	 * @param queryDetailListener
	 *            查询模板详情监听
	 * @param deleteListener
	 *            删除模板监听
	 * @param cancelListener
	 *            取消按钮监听
	 * @return 弹出框View
	 */
	public View initRemitTemplateMenuDialog(View.OnClickListener updateNameListener, 
			View.OnClickListener queryDetailListener, 
			View.OnClickListener deleteListener, 
			View.OnClickListener cancelListener) {
		View view = LayoutInflater.from(currentContent).inflate(R.layout.remittance_template_list_other, null);
		Button btnMenuUpdateName = (Button) view.findViewById(R.id.btn_menu_updateName);
		btnMenuUpdateName.setOnClickListener(updateNameListener);
		Button btnMenuQueryDetail = (Button) view.findViewById(R.id.btn_menu_queryDetail);
		btnMenuQueryDetail.setOnClickListener(queryDetailListener);
		Button btnMenuDelate = (Button) view.findViewById(R.id.btn_menu_delate);
		btnMenuDelate.setOnClickListener(deleteListener);
		Button btnMenuCancle = (Button) view.findViewById(R.id.btn_menu_cancle);
		btnMenuCancle.setOnClickListener(cancelListener);
		return view;
	}

	/**
	 * 转账 转出dialog
	 * 
	 * @param onclickListener
	 * @return
	 */
	public View initTranOutView(ListView listView, View.OnClickListener backListener, View.OnClickListener rightTopListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.dept_tran_out_in_dialog, null);
		RelativeLayout rl = (RelativeLayout) contentView.findViewById(R.id.sliding_menu_view);
		Button backBtn = (Button) contentView.findViewById(R.id.ib_back);
		backBtn.setOnClickListener(backListener);
		Button topRightBtn = (Button) contentView.findViewById(R.id.ib_top_right_btn);
		topRightBtn.setOnClickListener(rightTopListener);
		TextView titleTv = (TextView) contentView.findViewById(R.id.tv_title);
		topRightBtn.setText(currentContent.getResources().getString(R.string.add_new_rel_acc));
		topRightBtn.setVisibility(View.VISIBLE);
		titleTv.setText(currentContent.getResources().getString(R.string.tran_acc_out_top_title));
		rl.removeAllViews();
		rl.addView(listView);
		return contentView;
	}

	/**
	 * 转账 转出dialog
	 * 
	 * @param onclickListener
	 * @return
	 */
	public View initTranOutView(ListView listView, String title, View.OnClickListener backListener,
			View.OnClickListener rightTopListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.dept_tran_out_in_dialog, null);
		RelativeLayout rl = (RelativeLayout) contentView.findViewById(R.id.sliding_menu_view);
		Button backBtn = (Button) contentView.findViewById(R.id.ib_back);
		backBtn.setOnClickListener(backListener);

		// 左按钮
		Button topRightBtn = (Button) contentView.findViewById(R.id.ib_top_right_btn);
		if (rightTopListener == null) {
			topRightBtn.setText("");
			topRightBtn.setVisibility(View.GONE);
		} else {
			topRightBtn.setOnClickListener(rightTopListener);
			topRightBtn.setText(currentContent.getResources().getString(R.string.add_new_rel_acc));
			topRightBtn.setVisibility(View.VISIBLE);
		}
		// 标题
		TextView titleTv = (TextView) contentView.findViewById(R.id.tv_title);
		titleTv.setText(title);
		rl.removeAllViews();
		rl.addView(listView);
		return contentView;
	}

	/**
	 * 大额到账提醒签约情况
	 * 
	 * @param position
	 *            下标
	 * @param onSignListener
	 *            签约点击
	 * @param onDeleteListener
	 *            解约点击
	 * @param onModifyListener
	 *            修改点击
	 * @return
	 */
	public View initNonFixedProductRemindAccDetailDialogView(int position, final View.OnClickListener onSignListener,
			final View.OnClickListener onDeleteListener, final View.OnClickListener onModifyListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.service_non_fixed_product_account_detail, null);

		// 账户信息
		Map<String, Object> accountInfo = InfoServeDataCenter.getInstance().getAccountList().get(position);

		if (accountInfo == null) {
			return contentView;
		}

		// 大额到账详细
		Map<String, Object> nonFixedProduceInfo = InfoServeDataCenter.getInstance().getNonFixedProductDetail();

		if (nonFixedProduceInfo == null) {
			return contentView;
		}

		ImageButton closeIb = (ImageButton) contentView.findViewById(R.id.dept_close_ib);
		closeIb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
			}
		});

		// TextView 账户类型
		TextView accountTypeTv = (TextView) contentView.findViewById(R.id.dept_accounttype_tv);
		String strAccountType = (String) accountInfo.get(Comm.ACCOUNT_TYPE);
		accountTypeTv.setText(LocalData.AccountType.get(strAccountType));
		// TextView 账户别名
		accountNickName = (TextView) contentView.findViewById(R.id.dept_nickname_tv);
		accountNickName.setText((String) accountInfo.get(Comm.NICKNAME));

		// TextView 账号
		TextView accountNo = (TextView) contentView.findViewById(R.id.dept_accountno_tv);
		String strAccNo = (String) accountInfo.get(Comm.ACCOUNTNUMBER);
		accountNo.setText(StringUtil.getForSixForString(strAccNo));

		// TextView 签约状态
		TextView signStateTv = (TextView) contentView.findViewById(R.id.sign_state_tv);
		Button signBtn = (Button) contentView.findViewById(R.id.sign_btn);
		Button deleteBtn = (Button) contentView.findViewById(R.id.delete_btn);
		Button modifyBtn = (Button) contentView.findViewById(R.id.modify_btn);
		signBtn.setOnClickListener(onSignListener);
		deleteBtn.setOnClickListener(onDeleteListener);
		modifyBtn.setOnClickListener(onModifyListener);
		if (nonFixedProduceInfo.get(Push.SIGN_FLAG) != null) {
			if (Push.NON_FIXED_SIGNED.equals((String) nonFixedProduceInfo.get(Push.SIGN_FLAG))) {
				// 已签约
				signStateTv.setText(R.string.infoserve_licaidaoqi_sign);
				signBtn.setVisibility(View.GONE);
				deleteBtn.setVisibility(View.VISIBLE);
				modifyBtn.setVisibility(View.VISIBLE);

			} else if (Push.NON_FIXED_UNSIGNED.equals((String) nonFixedProduceInfo.get(Push.SIGN_FLAG))) {
				// 未签约
				signStateTv.setText(R.string.infoserve_licaidaoqi_not_sign);
				signBtn.setVisibility(View.VISIBLE);
				deleteBtn.setVisibility(View.GONE);
				modifyBtn.setVisibility(View.GONE);
			}
		}
		// TextView 生效日期
		TextView fromdateTv = (TextView) contentView.findViewById(R.id.fromdate_tv);
		fromdateTv.setText((String) nonFixedProduceInfo.get(Push.NON_FIXED_FROMDATE));
		// TextView 生效时间
		TextView fromtimeTv = (TextView) contentView.findViewById(R.id.fromtime_tv);
		fromtimeTv.setText((String) nonFixedProduceInfo.get(Push.NON_FIXED_FROMTIME));
		// TextView 交易上限
		TextView beginamtTv = (TextView) contentView.findViewById(R.id.beginamt_tv);
		beginamtTv.setText((String) nonFixedProduceInfo.get(Push.NON_FIXED_BEGINAMT));
		// TextView 交易下限
		TextView endAmtTv = (TextView) contentView.findViewById(R.id.endAmt_tv);
		endAmtTv.setText((String) nonFixedProduceInfo.get(Push.NON_FIXED_ENDAMT));
		// TextView 货币类型
		TextView currencyTv = (TextView) contentView.findViewById(R.id.currency_tv);
		if (nonFixedProduceInfo.get(Push.NON_FIXED_CURRENCY) != null) {
			currencyTv.setText(LocalData.Currency.get((String) nonFixedProduceInfo.get(Push.NON_FIXED_CURRENCY)));
		}
		// TextView 夜间提醒
		TextView nightsignTv = (TextView) contentView.findViewById(R.id.nightsign_tv);
		if (nonFixedProduceInfo.get(Push.NON_FIXED_NIGHTSIGN) != null) {
			if (Push.NON_FIXED_NIGHT_YES.equals((String) nonFixedProduceInfo.get(Push.NON_FIXED_NIGHTSIGN))) {
				nightsignTv.setText(R.string.infoserve_daedaozhang_night_sign_yes);
			} else if (Push.NON_FIXED_NIGHT_NO.equals((String) nonFixedProduceInfo.get(Push.NON_FIXED_NIGHTSIGN))) {
				nightsignTv.setText(R.string.infoserve_daedaozhang_night_sign_no);
			}
		}

		return contentView;
	}

	/**
	 * 初始化民生服务dialog
	 * 
	 * @param listView
	 * @param title
	 * @return
	 */
	public View initAnnuityDialogView(ListView listView, String title) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.plps_annuity_dialog, null);
		LinearLayout mLayout = (LinearLayout) contentView.findViewById(R.id.layout);
		((TextView) contentView.findViewById(R.id.text)).setText(title);
		mLayout.removeAllViews();
		mLayout.addView(listView);
		return contentView;
	}

	/**
	 * 理财推荐列表为空时的dialog
	 */
	public View initMentionDialogView(SpannableString sp, View.OnClickListener onclickListener) {

		contentView = LayoutInflater.from(currentContent).inflate(R.layout.message_dialog, null);
		Button confirmBtn = (Button) contentView.findViewById(R.id.confirm_btn);
		confirmBtn.setOnClickListener(onclickListener);
		TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.tv_metion_msg);

		tvMentionMsg.setText(sp);

		tvMentionMsg.setMovementMethod(LinkMovementMethod.getInstance());

		return contentView;
	}
	
	/**
	 * 理财推荐列表为空时的dialog
	 */
	public View initLargeDialogView(SpannableString sp, View.OnClickListener onclickListener) {

		contentView = LayoutInflater.from(currentContent).inflate(R.layout.large_sign_prompt, null);
		// 右上角 叉按钮
		ImageButton closeIb = (ImageButton) contentView.findViewById(R.id.dept_close_ib);
		closeIb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
			}
		});
		
		TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.prompt);

		tvMentionMsg.setText(sp);
		tvMentionMsg.setMovementMethod(LinkMovementMethod.getInstance());
		return contentView;
	}
	
	/**
	 * 基金推荐列表为空时的dialog
	 */
	public View fundEmptyDialogView(SpannableString sp, View.OnClickListener onclickListener) {

		contentView = LayoutInflater.from(currentContent).inflate(R.layout.message_dialog, null);
		Button confirmBtn = (Button) contentView.findViewById(R.id.confirm_btn);
		confirmBtn.setOnClickListener(onclickListener);
		TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.tv_metion_msg);

		tvMentionMsg.setText(sp);

		tvMentionMsg.setMovementMethod(LinkMovementMethod.getInstance());

		return contentView;
	}

	/**
	 * dxd 转账处理中 通讯提示框
	 */
	public static CustomDialog createFincProgressDialog(final Context con, View.OnClickListener closeListener,
			final Handler myHandler) {
		int time = TranDataCenter.getInstance().getTimer();
		final CustomDialog dlg = new CustomDialog(con, R.style.Theme_Dialog);
		final View.OnClickListener _listener = closeListener;
		dlg.show();
		dlg.setCancelable(false);
		LayoutInflater factory = LayoutInflater.from(con);
		// 加载progress_dialog为对话框的布局xml
		View view = factory.inflate(R.layout.progress_new_dialog, null);
		Button btn = (Button) view.findViewById(R.id.btnClose);
		TextView textview = (TextView) view.findViewById(R.id.tvmessage0);
		textview.bringToFront();
		textview.setText("" + time);
		TranDataCenter.getInstance().setTextview(textview);
		ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

		final Timer timer = new Timer();

		btn.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				HttpManager.stopConnect();
				if (BaseRequestThread.isConnecting) {
					// BaseHttpEngine.sClient.getConnectionManager().shutdown();
					BaseRequestThread.currentThread.interrupt();
					BaseRequestThread.isConnecting = false;
				}
				if (BaseHttpEngine.canGoBack) {
					BaseDroidApp.getInstanse().getCurrentAct().finish();
					BaseHttpEngine.canGoBack = false;
				}
				if (_listener != null) {
					_listener.onClick(v);
				}
				timer.cancel();
				BaseHttpEngine.dissMissProgressDialog();

			}
		});

		dlg.getWindow().setContentView(view);
		WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 4 / 5;
		lp.height = LayoutValue.SCREEN_WIDTH * 3 / 5;
		dlg.getWindow().setAttributes(lp);

		timer.schedule(new TimerTask() {

			int timeChange = TranDataCenter.getInstance().getTimer();

			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;

				if (!dlg.isShowing()) {
					timer.cancel();
					return;
				}
				timeChange--;
				TranDataCenter.getInstance().setTimerChange(timeChange);
				myHandler.sendMessage(message);
				if (timeChange == 0) {
					timer.cancel();
					return;
				}

			}
		}, 500, 1000);

		return dlg;
	}

	/**
	 * dxd 转账失败 dialog
	 * */
	public static CustomDialog createFailProgressDialog(final Context con, SpannableString sp,
			View.OnClickListener closeListener, final Handler myHandler) {
		CustomDialog dlg = new CustomDialog(con, R.style.Theme_Dialog);
		final Timer timer = new Timer();
		final View.OnClickListener _listener = closeListener;
		dlg.show();
		dlg.setCancelable(false);
		LayoutInflater factory = LayoutInflater.from(con);
		// 加载progress_dialog为对话框的布局xml
		View view = factory.inflate(R.layout.progress_fail_dialog, null);
		Button btn = (Button) view.findViewById(R.id.btnClose);
		TextView tvmessage2 = (TextView) view.findViewById(R.id.tvmessage2);

		tvmessage2.setText(sp);

		tvmessage2.setMovementMethod(LinkMovementMethod.getInstance());

		ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

		btn.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				HttpManager.stopConnect();
				if (BaseRequestThread.isConnecting) {
					// BaseHttpEngine.sClient.getConnectionManager().shutdown();
					BaseRequestThread.currentThread.interrupt();
					BaseRequestThread.isConnecting = false;
				}
				if (BaseHttpEngine.canGoBack) {
					BaseDroidApp.getInstanse().getCurrentAct().finish();
					BaseHttpEngine.canGoBack = false;
				}
				if (_listener != null) {
					_listener.onClick(v);
				}
				timer.cancel();
				BaseHttpEngine.dissMissProgressDialog();
			}
		});

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 2;
				myHandler.sendMessage(message);
				timer.cancel();
			}
		}, 5000);
		dlg.getWindow().setContentView(view);
		WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 4 / 5;
		lp.height = LayoutValue.SCREEN_WIDTH * 3 / 5;
		dlg.getWindow().setAttributes(lp);
		return dlg;
	}

	/**
	 * dxd 转账结果未知 dialog（unknown）
	 * */
	public static CustomDialog createUnknownProgressDialog(final Context con, View.OnClickListener closeListener,
			View.OnClickListener skipListener) {
		CustomDialog dlg = new CustomDialog(con, R.style.Theme_Dialog);
		final View.OnClickListener _listener = closeListener;
		dlg.show();
		dlg.setCancelable(false);
		LayoutInflater factory = LayoutInflater.from(con);
		// 加载progress_dialog为对话框的布局xml
		View view = factory.inflate(R.layout.progress_skip_dialog, null);
		Button btn = (Button) view.findViewById(R.id.btnClose);
		TextView tvmessage2 = (TextView) view.findViewById(R.id.tvmessage2);
		tvmessage2.setOnClickListener(skipListener);
		tvmessage2.setText(Html.fromHtml("<u>" + "转账记录" + "</u>"));
		ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

		btn.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				HttpManager.stopConnect();
				if (BaseRequestThread.isConnecting) {
					// BaseHttpEngine.sClient.getConnectionManager().shutdown();
					BaseRequestThread.currentThread.interrupt();
					BaseRequestThread.isConnecting = false;
				}
				if (BaseHttpEngine.canGoBack) {
					BaseDroidApp.getInstanse().getCurrentAct().finish();
					BaseHttpEngine.canGoBack = false;
				}
				if (_listener != null) {
					_listener.onClick(v);
				}
				BaseHttpEngine.dissMissProgressDialog();
			}
		});

		dlg.getWindow().setContentView(view);
		WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
		lp.width = LayoutValue.SCREEN_WIDTH * 4 / 5;
		lp.height = LayoutValue.SCREEN_WIDTH * 7 / 10;
		dlg.getWindow().setAttributes(lp);
		return dlg;
	}
	
	
	/**
	 * 存款管理大额存单  提示信息  只有一个按钮的提示框   add luqp 2016年3月4日
	 * @param prompt 提示用户的信息
	 * @param onclickListener 右上角叉按钮点击事件
	 * @return 将提示框和提示信息返回
	 */
	public View largeDialogView(String prompt, View.OnClickListener onclickListener) {

		contentView = LayoutInflater.from(currentContent).inflate(R.layout.large_sign_prompt, null);
		// 右上角 叉按钮
		ImageButton closeIb = (ImageButton) contentView.findViewById(R.id.dept_close_ib);
		closeIb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
			}
		});
		TextView tvMentionMsg = (TextView) contentView.findViewById(R.id.prompt);
		tvMentionMsg.setText(prompt);
		tvMentionMsg.setMovementMethod(LinkMovementMethod.getInstance());
		return contentView;
	}


	/**
	 * 外汇详情 货币对 弹出框
	 * @param currency1Listener  货币对1
	 * @param currency2Listener  货币对1
	 * @param currency3Listener  货币对3
	 * @param cancel4  货币对4
	 * @return 弹出框View
	 * add by luqp 2016年9月29日
	 */
//	public View initForeignDetailsDialog(View.OnClickListener currency1Listener,
//											View.OnClickListener currency2Listener,
//											View.OnClickListener currency3Listener,
//											View.OnClickListener cancelListener) {
//		View view = LayoutInflater.from(currentContent).inflate(R.layout.foreign_details_dialog, null);
//		TextView btnMenuUpdateName = (TextView) view.findViewById(R.id.currency1);
//		btnMenuUpdateName.setOnClickListener(currency1Listener);
//		TextView btnMenuQueryDetail = (TextView) view.findViewById(R.id.currency2);
//		btnMenuQueryDetail.setOnClickListener(currency2Listener);
//		TextView btnMenuDelate = (TextView) view.findViewById(R.id.currency3);
//		btnMenuDelate.setOnClickListener(currency3Listener);
//		TextView btnMenuCancle = (TextView) view.findViewById(R.id.cancel_button);
//		btnMenuCancle.setOnClickListener(cancelListener);
//		return view;
//	}

	/**
	 * 底部显示Dialog (上面:Spinner 下面取消按钮)
	 * @param currencyPair
	 *            货币对数据
	 * @param onCurrencyPairsItemClickListener
	 *            litview item点击事件监听
	 * @return
	 */
	public View currencyPairDialogView(Context context, List<Map<String,Object>> currencyPair, final AdapterView.OnItemClickListener onCurrencyPairsItemClickListener) {
		contentView = LayoutInflater.from(currentContent).inflate(R.layout.foreign_currency_pair_dialog, null);
		ListView currencyPairLv = (ListView) contentView.findViewById(R.id.lv_currency_pair);
		Button cancelBt = (Button) contentView.findViewById(R.id.bt_cancel);
		DialogItemAdapter dialogItemAdapter = new DialogItemAdapter(context, currencyPair);
		currencyPairLv.setAdapter(dialogItemAdapter);
		dialogItemAdapter.setItemClickListener(onCurrencyPairsItemClickListener);

		/** 取消按钮*/
		cancelBt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
			}
		});
		return contentView;
	}
}
