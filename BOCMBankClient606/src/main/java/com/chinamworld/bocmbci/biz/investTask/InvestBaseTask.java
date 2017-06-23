package com.chinamworld.bocmbci.biz.investTask;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.IHttpRequestHandle;
import com.chinamworld.bocmbci.mode.IActivityEvent;
import com.chinamworld.bocmbci.mode.IFunc;

/**
 * 理财模块 做任务基类
 * 
 * @author yuht
 * 
 */
public abstract class InvestBaseTask implements IActivityEvent,
		IHttpRequestHandle {

	// [start] 抽象方法定申明

	/**
	 * 获得当前需要做任务的弹出框视图
	 * 
	 * @return
	 */
	protected abstract View getTaskView();

	/**
	 * 检查当前所需要做的任务的状态 ture:任务已全部做成功，false: 任务未做完
	 * 
	 * @return
	 */
	protected abstract boolean getDoTaskStatus();

	/**
	 * 联网获得当前已经做过的任务状态
	 */
	protected abstract void getTaskStatusOnLine(IAction actionCallBack);

	/**
	 * 当任务做完一个任务后，回到当前Activity后，数据处理。比如设置当前任务状态等
	 * @param requestCode ： 跳转请求码
	 * @param resultCode ： 返回结果码
	 * @param data ： 返回数据
	 * @return
	 */
	protected abstract boolean onActivityForResult(int requestCode, int resultCode, Intent data);
	// [end]

	// [start] 局部变量定义
	/** Standard activity result: operation canceled. */
	public static final int RESULT_CANCELED = 0;
	/** Standard activity result: operation succeeded. */
	public static final int RESULT_OK = -1;
	/** Start of user-defined activity results. */
	public static final int RESULT_FIRST_USER = 1;

	/**
	 * getTaskStatusOnLine方法重写后，当发送通讯检查任务状态的任务完成后，必须调用此方法。
	 */
	protected IAction OnLineRequestCallBack;

	/**
	 * 通讯工具类对象
	 */
	protected HttpTools httpTools;
	/**
	 * 当前任务框所在的Activity对象
	 */
	protected Activity context;

	/**
	 * 任务全部做完后回调
	 */
	private IAction taskSuccess;
	// [end]

	// [start] 基本基本实现方法

	protected InvestBaseTask(BaseActivity context) {
		this.context = context;
		httpTools = new HttpTools(context, this);
	}
	/**
	 * 做任务时，可能需要使用的参数
	 */
	protected Object taskParam;
	
	/**
	 * 返回false,则表示中断默认操作
	 * 返回true,则表示继续默认操作
	 */
	protected IFunc<Boolean> exitActionCallBack;
	/**
	 * 关闭任务框时的事件处理。
	 * 默认关闭当前的activity
	 * @param exitAction
	 */
	public void setExitListener(IFunc<Boolean> exitAction){
		exitActionCallBack = exitAction;
	}
	
	/**
	 * 开始做任务
	 * 
	 * @param taskSuccess
	 *            ：任务做完成后，回调接口
	 */
	public void doTask(final IAction taskSuccess,Object param) {
		taskParam = param;
		this.taskSuccess = taskSuccess;
		getTaskStatusOnLine(new IAction() {
			@Override
			public void SuccessCallBack(Object param) {
				showTask();
			}
		});
	}

	/**
	 * 根据任务状态，判断是否显示任务弹出框
	 * 如果做完任务，则回调任务完成接口
	 * 如果为做完，则弹出任务框
	 */
	public void showTask() {
		// 联网通信完成
		if (getDoTaskStatus()) {
			// 任务已经做完
			BaseDroidApp.getInstanse().dismissMessageDialog();
			if (taskSuccess != null)
				taskSuccess.SuccessCallBack(null);
		} else {
			View v = getTaskView();
			if (v != null)
				BaseDroidApp.getInstanse().showAccountMessageDialog(getTaskView());
		}
	}

	/**
	 * 实现OnActivityResult接口方法。
	 * 在派生类中重写此方法时，必须要回调此
	 */
	@Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		if(onActivityForResult(requestCode,resultCode,data) == false)
			return true;
		showTask();
		return true;
	}

	// [end]

	// [start] 通讯类需要处理数据的回调方法
	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse resultObj) {
		// TODO Auto-generated method stub
		return ((BaseActivity)context).doBiihttpRequestCallBackPre(resultObj);
	}

	@Override
	public boolean doBiihttpRequestCallBackAfter(BiiResponse resultObj) {
		// TODO Auto-generated method stub
		return ((BaseActivity)context).doBiihttpRequestCallBackAfter(resultObj);
	}

	// [end]

}
