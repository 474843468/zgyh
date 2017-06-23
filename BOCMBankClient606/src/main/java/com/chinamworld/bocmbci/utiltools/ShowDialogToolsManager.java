package com.chinamworld.bocmbci.utiltools;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.chinamworld.bocmbci.abstracttools.ShowDialogTools;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.widget.CustomDialog;

public class ShowDialogToolsManager  extends  ShowDialogTools{

	public ShowDialogToolsManager()
	{
		Instance = this;
		
		
	}
	
	@Override
	public void showInfoMessageDialog(CharSequence message) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(message);
	}

	@Override
	public void showProgressDialog() {
		// TODO Auto-generated method stub
		BaseHttpEngine.showProgressDialog();
	}

	@Override
	public void dissMissProgressDialog() {
		// TODO Auto-generated method stub
		BaseHttpEngine.dissMissProgressDialog();
	}

	@Override
	public void showProgressDialogCanGoBack() {
		BaseHttpEngine.showProgressDialogCanGoBack();
		
	}

	@Override
	public void showForexMessageDialog(View v) {
		BaseDroidApp.getInstanse().showForexMessageDialog(v);
	}

	@Override
	public void dismissMessageDialogFore() {
		BaseDroidApp.getInstanse().dismissMessageDialogFore();
	}

	@Override
	public void showMessageDialog(String message,
			OnClickListener onClickListener) {
		BaseDroidApp.getInstanse().showMessageDialog(message, onClickListener);
		
	}

	@Override
	public void dissmissCloseOfProgressDialog() {
		BaseHttpEngine.dissmissCloseOfProgressDialog();
		
	}

	@Override
	public void showInfoMessageCheckedBoxDialog(String message,
			OnClickListener onclickListener,
			OnCheckedChangeListener checkedListener) {
		BaseDroidApp.getInstanse().showInfoMessageCheckedBoxDialog(message, onclickListener, checkedListener);
		
	}


	@Override
	public void showErrorDialog(String message, int btn1Text, int btn2Text,
			OnClickListener onclicklistener) {
		// TODO Auto-generated method stub
		BaseDroidApp.getInstanse().showErrorDialog(message, btn1Text, btn2Text, onclicklistener);	
	}

	@Override
	public void dismissErrorDialog() {
		// TODO Auto-generated method stub
		BaseDroidApp.getInstanse().dismissErrorDialog();
	}

	@Override
	public void createDialog(String errorCode, String message,
			OnClickListener onclick) {
		// TODO Auto-generated method stub
	BaseDroidApp.getInstanse().createDialog(errorCode, message, onclick);	
	}

	@Override
	public void createDialog(String errorCode, String message) {
		// TODO Auto-generated method stub
		BaseDroidApp.getInstanse().createDialog(errorCode, message);	
	}

	@Override
	public void dismissMessageDialog() {
		// TODO Auto-generated method stub
		BaseDroidApp.getInstanse().dismissMessageDialog();
	}

	@Override
	public void toastInCenter(Context context, String text) {
		// TODO Auto-generated method stub
		CustomDialog.toastInCenter(context, text);
	}

	@Override
	public void showAccountMessageDialog(View v) {
		// TODO Auto-generated method stub
		BaseDroidApp.getInstanse().showAccountMessageDialog(v);
	}

	@Override
	public void closeAllDialog() {
		// TODO Auto-generated method stub
		BaseDroidApp.getInstanse().closeAllDialog();
	}

	@Override
	public void showMessageDialogWithoutBtn(String message) {
		// TODO Auto-generated method stub
		BaseDroidApp.getInstanse().showMessageDialogWithoutBtn(message);
	}

	@Override
	public void showMessageAudiokey(OnClickListener onAudioClickListener) {
		// TODO Auto-generated method stub
		BaseDroidApp.getInstanse().showMessageAudiokey(onAudioClickListener);
	}

	@Override
	public void showSeurityChooseDialog(OnClickListener onclickListener) {
		// TODO Auto-generated method stub
		BaseDroidApp.getInstanse().showSeurityChooseDialog(onclickListener);
	}


}
