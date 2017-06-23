package com.chinamworld.bocmbci.biz.setting.accmanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 修改账户别名别名页面
 * 
 * @author xyl
 * 
 */
public class EditAccAliasActivity extends AccountManagerBaseActivity {
	private static final String TAG = "EditAccAliasActivity";
	
	private TextView accTextView;
	private TextView accTypeTextView;
	private TextView oldAliasTv;
	
	private EditText accAliasEdit;
	
	private Button confirmBtn;
	
	private String accNumStr;
	private String accTypeStr;
	private String oldAccAliasStr;
	private String newAccAliasStr;
	private String accIdStr;
	private String accNameStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initData();
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}
		editAccAlias(accIdStr, accNameStr, accTypeStr, newAccAliasStr, tokenId);
	}

	@Override
	public void eidtAccAliasCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		biiResponseBody.getResult();
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent();
		intent.setClass(this, EditAccAliaSucceesActivity.class);
		intent.putExtra(Setting.I_ACCALIAS, newAccAliasStr);
		intent.putExtra(Setting.I_ACCTYPE, accTypeStr);
		intent.putExtra(Setting.I_ACCNUM, accNumStr);
		startActivity(intent);
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View childView = mainInflater.inflate(R.layout.setting_editalias, null);
		tabcontent.addView(childView);
		accTextView = (TextView) childView.findViewById(R.id.set_editalias_acc);
		accTypeTextView = (TextView) childView
				.findViewById(R.id.set_editalias_acctype);
		oldAliasTv = (TextView) childView
				.findViewById(R.id.set_oledaccalias_colon);
		accAliasEdit = (EditText) childView
				.findViewById(R.id.set_editalias_accalias);
		confirmBtn = (Button) childView.findViewById(R.id.set_confirm);
		confirmBtn.setOnClickListener(this);
		setTitle(getResources().getString(R.string.set_title_editnickname));
		// 最长20个英文字符最长10个中文字符
		EditTextUtils.setLengthMatcher(this, accAliasEdit, 20);
	}

	/**
	 * 初始化数据
	 * 
	 * @Author xyl
	 */
	private void initData() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		accTypeStr = extras.getString(Setting.I_ACCTYPE);
		accNumStr = extras.getString(Setting.I_ACCNUM);
		oldAccAliasStr = extras.getString(Setting.I_ACCALIAS);
		accIdStr = extras.getString(Setting.I_ACCID);
		accNameStr = extras.getString(Setting.I_ACCNAME);
		accTextView.setText(StringUtil.getForSixForString(accNumStr));
		accTypeTextView.setText(LocalData.AccountType.get(accTypeStr));
		oldAliasTv.setText(oldAccAliasStr);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.set_consern:
			finish();
			break;
		case R.id.set_confirm:
			newAccAliasStr = StringUtil.trim(accAliasEdit.getText().toString());
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			RegexpBean regexAlias = new RegexpBean(getResources().getString(
					R.string.set_newaccalias_no), newAccAliasStr, "nickname");
			lists.add(regexAlias);
			if (RegexpUtils.regexpDate(lists)) {
				if (newAccAliasStr.equals(accTypeTextView.getText().toString())) {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									getString(R.string.set_nickname_equle_with_acctype_error));
				} else {
					requestCommConversationId();
					BaseHttpEngine.showProgressDialog();
				}
			}
			break;

		case R.id.ib_top_right_btn:
			this.finish();
			break;
		default:
			break;
		}

	}

}
