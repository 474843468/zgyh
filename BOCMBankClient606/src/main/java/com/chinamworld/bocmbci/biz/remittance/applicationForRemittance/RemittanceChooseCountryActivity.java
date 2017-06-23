package com.chinamworld.bocmbci.biz.remittance.applicationForRemittance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceBaseActivity;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.adapter.PinyinAdapter;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.pinyin.AssortView;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.pinyin.AssortView.OnTouchAssortListener;

/**
 * 选择国家页
 * 
 * @author Zhi
 */
public class RemittanceChooseCountryActivity extends RemittanceBaseActivity {
	/** 列表适配器 */
	private PinyinAdapter adapter;
	/** 列表控件 */
	private ExpandableListView elvCountry;
	/** 右侧字母索引栏 */
	private AssortView assortView;
	/** 国家信息 */
	private List<Map<String, String>> countryData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 隐藏右按钮 */
		setRightTopGone();
		setTitle(this.getString(R.string.remittance_country_area));
		addView(R.layout.remittance_info_input_choose_country);
		initView();
	}

	private void initView() {
		elvCountry = (ExpandableListView) findViewById(R.id.elist);
		assortView = (AssortView) findViewById(R.id.assort);
//		countryData = RemittanceUtils.getCountry(this);
		countryData = RemittanceDataCenter.getInstance().getListPsnQryInternationalTrans4CNYCountry();
		adapter = new PinyinAdapter(this, countryData);
		elvCountry.setAdapter(adapter);
		elvCountry.setOnChildClickListener(new OnChildClickListener() {
			
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				Intent intent = new Intent();
				intent.putExtra(Remittance.COUNTRYCODE, adapter.getAssort().getHashList().getValueIndex(groupPosition, childPosition).get(Remittance.COUNTRYCODE));
				setResult(RemittanceContent.CHOOSE_CHOUNTRY, intent);
				finish();
				overridePendingTransition(R.anim.no_animation, R.anim.push_up_out);
				return false;
			}
		});
		openAll();

		final EditText etSearch = (EditText) findViewById(R.id.edit_search);
		etSearch.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {
				if (s.toString() == null) {
					adapter.setData(countryData);
				}
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				String str = s.toString();
				for (int i = 0; i < countryData.size(); i++) {
					if (countryData.get(i).get(Remittance.NAME_CN).contains(str)) {
						list.add(countryData.get(i));
					}
				}
				adapter.setData(list);
				openAll();
			}
		});

		WindowManager wm = this.getWindowManager();
	    final int width = wm.getDefaultDisplay().getWidth();
		// 字母按键回调
		assortView.setOnTouchAssortListener(new OnTouchAssortListener() {
			View layoutView = LayoutInflater.from(RemittanceChooseCountryActivity.this).inflate(R.layout.remittance_choose_country_char_pop, null);
			TextView text = (TextView) layoutView.findViewById(R.id.textview);
			PopupWindow popupWindow;
			public void onTouchAssortListener(String str) {
				int index = adapter.getAssort().getHashList().indexOfKey(str);
				if (index != -1) {
					elvCountry.setSelectedGroup(index);
				}

				if (popupWindow != null) {
					text.setText(str);
				} else {
					popupWindow = new PopupWindow(layoutView, width / 4, width / 4, false);
					// 显示在Activity的根视图中心
					popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
				}
				text.setText(str);
			}

			public void onTouchAssortUP() {
				if (popupWindow != null)
					popupWindow.dismiss();
				popupWindow = null;
			}
		});
	}
	
	private void openAll() {
		// 展开所有
		for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
			elvCountry.expandGroup(i);
		}
	}
}
