package com.chinamworld.bocmbci.biz.quickOpen.openQuery;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.quickOpen.StockThirdQuickOpenBaseActivity;

/**
 * 证券开户查询结果页
 * 
 * @author Zhi
 */
public class QuickOpenResultActivity extends StockThirdQuickOpenBaseActivity {
	
	/** 成功或未明图片 */
	private ImageView ivResultImg;
	/** 成功或未明显示的文字 */
	private TextView tvResultText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.quickOpen_query_title);
		addView(R.layout.quick_open_query_result);
		initView();
	}
	
	private void initView() {
		ivResultImg = (ImageView) findViewById(R.id.iv_resultImage);
		tvResultText = (TextView) findViewById(R.id.tv_resultText);
		boolean isSuccess = getIntent().getBooleanExtra(ISSUCCESS, false);
		if (isSuccess) {
			ivResultImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.success));
			tvResultText.setText(getResources().getString(R.string.quickOpen_query_success));
		} else {
			ivResultImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.loseofopen));
			tvResultText.setText(getResources().getString(R.string.quickOpen_query_lose));
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		hineLeftSideMenu();
	}
}
