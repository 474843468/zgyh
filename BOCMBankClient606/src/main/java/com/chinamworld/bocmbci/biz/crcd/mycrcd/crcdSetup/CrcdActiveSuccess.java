package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 信用卡激活成功
 * 
 * @author huangyuchao
 * 
 */
public class CrcdActiveSuccess extends CrcdBaseActivity {

	/** 信用卡激活成功 */
	private View view;

	Button sureBtn;

	TextView tv_cardNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_creditcard_active_title));
		if (view == null) {
			view = addView(R.layout.crcd_active_info_success);
		}

		back.setVisibility(View.GONE);

		init();

	}

	public void init() {

		tv_cardNumber = (TextView) findViewById(R.id.tv_cardNumber);
		tv_cardNumber.setText(StringUtil.getForSixForString(CrcdActiveInfo.cardNumber));

		sureBtn = (Button) findViewById(R.id.sureButton);
		sureBtn.setOnClickListener(sureClick);
		String message=getResources().getString(R.string.mycrcd_setpassword);
		int setId = R.string.mycrcd_setpassword_yes;
		int cancleId = R.string.mycrcd_setpassword_no;
		BaseDroidApp.getInstanse().showErrorDialog(message,  cancleId,setId, new OnClickListener(){

			@Override
			public void onClick(View v) {

				switch ((Integer) v.getTag()) {
				case CustomDialog.TAG_SURE:// cancleId
					
					BaseDroidApp.getInstanse().dismissErrorDialog();
					Intent it = new Intent(CrcdActiveSuccess.this, CrcdSetConsumePWPreActivity.class);					
					it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, CrcdActiveInfo.cardNumber);
					it.putExtra("payorsearch", "pay");
//					startActivity(it);
					startActivityForResult(it, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
					break;
				case CustomDialog.TAG_CANCLE://setId
					BaseDroidApp.getInstanse().dismissErrorDialog();
					
					break;
				default:
					break;
				}
			}
		}
		);
	}

	OnClickListener sureClick = new View.OnClickListener() {

		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		};

	};

	@Override
	public void onBackPressed() {

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}
	
}
