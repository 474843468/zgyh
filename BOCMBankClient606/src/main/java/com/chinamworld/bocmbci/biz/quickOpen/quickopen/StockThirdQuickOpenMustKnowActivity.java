package com.chinamworld.bocmbci.biz.quickOpen.quickopen;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.quickOpen.QuickOpenDataCenter;
import com.chinamworld.bocmbci.biz.quickOpen.StockThirdQuickOpenBaseActivity;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银国际证券开户协议页
 * 
 * @author Zhi
 */
public class StockThirdQuickOpenMustKnowActivity extends StockThirdQuickOpenBaseActivity implements OnClickListener{

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 协议主体 */
	private TextView tvMustKnowText;
	/** 同意 */
	private CheckBox cbAgree;
	/** 下一步按钮 */
	private Button btnAgree;
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (StringUtil.isNullOrEmpty(QuickOpenDataCenter.getInstance().getListCommonQueryAllChinaBankAccount())) {
			BaseHttpEngine.showProgressDialogCanGoBack();
			Map<String, Object> params = new HashMap<String, Object>();
			List<String> accountTypeList = new ArrayList<String>();
			accountTypeList.add("119");
			params.put(Comm.ACCOUNT_TYPE, accountTypeList);
			requestHttp(Comm.QRY_ALL_BANK_ACCOUNT, "requestPsnCommonQueryAllChinaBankAccountCallBack", params, false);
		} else {
			addView(R.layout.quick_open_mustknow);
			setTitle(R.string.quickOpen_title_open);
			setTopRightGone();
			initView();
		}
	}
	
	private void initView() {
		setTopLeftListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				QuickOpenDataCenter.getInstance().clearAllData();
				finish();
			}
		});
		tvMustKnowText = (TextView) findViewById(R.id.tv_mustKnowText);
		cbAgree = (CheckBox) findViewById(R.id.cb_isCheck);
		btnAgree = (Button) findViewById(R.id.btnAgree);
		
		tvMustKnowText.setText(Html.fromHtml(getFromAssets("quickopen.txt")));
		
		cbAgree.setOnClickListener(this);
		findViewById(R.id.tv_isCheck).setOnClickListener(this);
		btnAgree.setOnClickListener(this);
	}
	
	public String getFromAssets(String fileName){
		String result = "";
        try { 
        	InputStream in = getResources().getAssets().open(fileName);
        	//获取文件的字节数
        	int lenght = in.available();
        	//创建byte数组
        	byte[]  buffer = new byte[lenght];
        	//将文件中的数据读到byte数组中
        	in.read(buffer);
        	result = EncodingUtils.getString(buffer, "GB2312");
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
		return result;
} 
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 4) {
			finish();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cb_isCheck:
			break;
		case R.id.tv_isCheck:
			if (cbAgree.isChecked()) {
				cbAgree.setChecked(false);
			} else {
				cbAgree.setChecked(true);
			}
			break;
		case R.id.btnAgree:
			if (!cbAgree.isChecked()) {
				BaseDroidApp.getInstanse().showInfoMessageDialog("您还未勾选《个人信息披露授权书》，请勾选同意后再点击下一步按钮进行操作。");
				return;
			}
			Intent intent = new Intent(this, StockThirdQuickOpenActivity.class);
			startActivityForResult(intent, 4);
			break;
		}
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO ---------------------------------------网络请求与回调方法--------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 查询中行账户列表回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(Object resultObj) {
		List<Map<String, Object>> resultList =  HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showMessageDialog("您没有适合开通第三方存管的借记卡。请首先申请借记卡并关联进网银后再来尝试。", new OnClickListener() {
				@Override
				public void onClick(View v) {
					QuickOpenDataCenter.getInstance().clearAllData();
					finish();
				}
			});
			return;
		}
		QuickOpenDataCenter.getInstance().setListCommonQueryAllChinaBankAccount(resultList);
		BaseHttpEngine.dissMissProgressDialog();
		addView(R.layout.quick_open_mustknow);
		setTitle(R.string.quickOpen_title_open);
		setTopRightGone();
		initView();
	}
}
