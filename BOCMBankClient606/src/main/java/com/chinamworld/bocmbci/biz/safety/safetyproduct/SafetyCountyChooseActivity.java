package com.chinamworld.bocmbci.biz.safety.safetyproduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyConstant;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.SafetyUtils;
import com.chinamworld.bocmbci.biz.safety.adapter.SafetyCountyAdapter;
import com.chinamworld.bocmbci.biz.safety.adapter.SafetyCountyAdapter.ViewHolder;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 国家、地区选择
 * @author panwe
 *
 */
public class SafetyCountyChooseActivity extends SafetyBaseActivity{
	private View mMainView;
	private ListView mListView;
	private EditText mEditText;
	private String lastSelected;
	private SafetyCountyAdapter mAdapter;
	private List<Map<String, Object>> countryData;
	private List<Map<String, Object>> srarchData = new ArrayList<Map<String,Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMainView = View.inflate(this, R.layout.safety_county_choose, null);
		setTitle(this.getString(R.string.safety_counrtychoose_title));
		setRightText(this.getString(R.string.close));
		setRightBtnClick(backOnClickListener);
		setLeftButtonPopupGone();
		setBottomTabGone();
		addView(mMainView);
		setLeftTopGone();
		initAllData();
		initViews();
	}
	
	private void initAllData(){
		countryData = SafetyUtils.getCountry(this);
		lastSelected = getIntent().getStringExtra(SafetyConstant.LASTSELECTED);
		if (!StringUtil.isNull(lastSelected)) {
			for (int i = 0; i < countryData.size(); i++) {
				if (lastSelected.contains((String)countryData.get(i).get(Safety.NAME))) {
					Map<String, Object> map = countryData.get(i);
					map.put(Safety.SELECT, true);
					countryData.remove(i);
					countryData.add(i, map);
				}
			}
		}
		srarchData.addAll(countryData);
	}
	
	private void initViews(){
		mEditText = (EditText) mMainView.findViewById(R.id.edit_search);
		mListView = (ListView) mMainView.findViewById(R.id.listview);
		mAdapter = new SafetyCountyAdapter(this, srarchData);
		mEditText.addTextChangedListener(editWatcher);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(listOnitemClick);
	}
	
	private OnItemClickListener listOnitemClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			ViewHolder holder = (ViewHolder) view.getTag();
			holder.checkbox.toggle();
			Map<String, Object> map = srarchData.get(position);
			map.put(Safety.SELECT, holder.checkbox.isChecked());
			int index = countryData.indexOf(map);
			countryData.remove(index);
			countryData.add(index, map);
		}
	};
	
	private TextWatcher editWatcher = new TextWatcher(){
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {}
		@Override
		public void afterTextChanged(Editable s) {
			if (StringUtil.isNull(s.toString())) {
				srarchData.addAll(countryData);
				mAdapter.setData(countryData);
			}
		}
	};
	
	/**
	 * 搜索操作
	 * @param v
	 */
	public void buttonSearchOnclick(View v){
		closeInput(mEditText);
		srarchData.clear();
		String strSh = mEditText.getText().toString();
		for (int i = 0; i < countryData.size(); i++) {
			if (((String)countryData.get(i).get(Safety.NAME)).contains(strSh)) {
				srarchData.add(countryData.get(i));
			}
		}
		mAdapter.setData(srarchData);
		if (StringUtil.isNullOrEmpty(srarchData)) {
			CustomDialog.toastInCenter(this,getString(R.string.bond_comm_error));
		}
	}
	
	/**
	 * 确定操作
	 * @param v
	 */
	public void buttonOkOnclick(View v){
		ArrayList<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < countryData.size(); i++) {
			if ((Boolean)countryData.get(i).get(Safety.SELECT)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(Safety.CODE, (String)countryData.get(i).get(Safety.CODE));
				map.put(Safety.NAME, (String)countryData.get(i).get(Safety.NAME));
				resultList.add(map);
			}
		}
		SafetyDataCenter.getInstance().setCountyList(resultList);
		setResult(1001);
		finish();
	}
}
