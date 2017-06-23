package com.chinamworld.bocmbci.biz.sbremit;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.JustifyTextView;

/**
 * 结售汇特别提示页面
 *
 */
public class SBRemitSpecialTipsActivity extends SBRemitBaseActivity {
	
	private static final String TAG = "SBRemitSpecialTipsActivity";
	private View view;
	/** 提示 */
	private JustifyTextView tips_view;
	/** 选择框 */
	private CheckBox agree_cb;
	/** 警告 */
	private TextView tips_warn;
	/** 下一步 */
	private Button btnNext;
	private TextView agree_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		init();
		initViews();
	}

	private void initViews() {
		tips_view = (JustifyTextView) findViewById(R.id.tips);
		agree_cb = (CheckBox) findViewById(R.id.agree_cb);
		agree_tv = (TextView) findViewById(R.id.agree_tv);
		tips_warn = (TextView) findViewById(R.id.tips_warn);
		btnNext = (Button) findViewById(R.id.btnNext);
		
		tips_view.setText(getString(R.string.sbremit_is_open_tips));
		agree_tv.setOnClickListener(checkedListener);
		btnNext.setOnClickListener(onClick);
	}
	
	private OnClickListener checkedListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (agree_cb.isChecked()) {
				agree_cb.setChecked(false);
			} else {
				agree_cb.setChecked(true);
			}
		}
	};
	
	private OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (agree_cb.isChecked()) {
				BaseHttpEngine.showProgressDialog();
				requestExchangeSubmit();
			} else {
				tips_warn.setVisibility(View.VISIBLE);
			}
		}
	};
	
	/**
	 * 客户首次使用风险确认提交
	 */
	protected void requestExchangeSubmit() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(SBRemit.SBREMIT_EXCHANGE_SUBMIT);

		HttpManager.requestBii(biiRequestBody, this, "requestExchangeSubmitCallback");
	}
	
	public void requestExchangeSubmitCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		String resultStr = (String)resultMap.get(SBRemit.STATUS);
		if (StringUtil.isNull(resultStr)) {
			return;
		}
		boolean status = Boolean.parseBoolean(resultStr);
		if (status) {	// 成功
			setResult(RESULT_OK);
			finish();
		} else {	// 失败
			BaseDroidApp.getInstanse().showMessageDialog(getString(R.string.sign_failed), new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissMessageDialog();
				}
			});
		}
	}

	private void init() {
		setTitle(getString(R.string.sbremit_read_tips_titles));
		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		view = LayoutInflater.from(this).inflate(R.layout.sbremit_special_tips_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		tabcontent.setPadding(0, 0, 0, this.getResources()
				.getDimensionPixelSize(R.dimen.common_bottom_padding_new));
		
		((Button) this.findViewById(R.id.ib_top_right_btn)).setVisibility(View.GONE);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// 隐藏左侧二级菜单
		setLeftButtonPopupGone();
	}
}
