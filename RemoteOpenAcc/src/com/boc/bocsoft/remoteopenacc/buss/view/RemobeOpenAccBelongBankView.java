package com.boc.bocsoft.remoteopenacc.buss.view;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.buss.activity.RemobeOpenAccSubStepFragment;

/**
 * 所属开户行View
 * 
 * @author gwluo
 * 
 */
public class RemobeOpenAccBelongBankView extends LinearLayout {
	private Context mContext;
	private EditText et_belong_bank;
	private ImageView iv_clear;
	private RemobeOpenAccSubStepFragment mFragment;
	private Button btn_cancle;

	public RemobeOpenAccBelongBankView(Context context,
			RemobeOpenAccSubStepFragment fragment) {
		super(context);
		mContext = context;
		mFragment = fragment;
		init();
	}

	private void init() {
		View.inflate(mContext, R.layout.bocroa_view_belong_bank, this);
		et_belong_bank = (EditText) findViewById(R.id.et_belong_bank);
		iv_clear = (ImageView) findViewById(R.id.iv_clear);
		iv_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_belong_bank.setText("");
			}
		});
		btn_cancle = (Button) findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hiddenSoftInput();
				mFragment.hidAlertSelectView();
				// mFragment.selectViewSelected();
			}
		});
		et_belong_bank.requestFocus();
		et_belong_bank.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// mFragment
					// .getBelongBank(et_belong_bank.getText().toString());
					hiddenSoftInput();
				}
				return false;
			}

		});
		et_belong_bank.postDelayed(new Runnable() {

			@Override
			public void run() {
				InputMethodManager inputMethodManager = (InputMethodManager) mContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.showSoftInput(et_belong_bank,
						InputMethodManager.SHOW_IMPLICIT);
			}
		}, 700);
		setListener();
	}

	private void hiddenSoftInput() {
		InputMethodManager manger = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		manger.hideSoftInputFromWindow(getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	protected void showToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	private void setListener() {

	}
}
