package com.chinamworld.bocmbci.biz.plps.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.SortData;
import com.chinamworld.bocmbci.biz.plps.adapter.CitySidbarSortAdapter;
import com.chinamworld.bocmbci.biz.plps.adapter.SidbarSortAdapter;
import com.chinamworld.bocmbci.biz.plps.order.CityAdressSideBar.OnTouchingLetterChangedListener;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

public class OrderAdressCityActivity extends PlpsBaseActivity{
	//加载布局
//	public LinearLayout tabcontent = null;
//	View view;
	//汉字转换成拼音
	private CharacterParser characterParser;
	private List<SortData> sourceDateList;
	//根据拼音来排列ListView里面的数据顺序
	private PinyinComparatorSid pinyinComparator;
	private CityAdressSideBar cityAdressSideBar;
	//listView的adpater 
	private SidbarSortAdapter sidbarSortAdapter;
	//cityListView 的adpater
	private CitySidbarSortAdapter citySidbarSortAdapter;
	//省listView
	private ListView provListView;
	//市listView
	private ListView cityListView;
	//省
	private LinearLayout provLinearLayout;
	private TextView provTextView;
	private TextView cursor;
	private String provName = null;
	//市
	private LinearLayout cityLinearLayout;
	private TextView cityTextView;
	private TextView cursort;
	private String cityName = null;
	//省listView显示数据
//	private List<Map<String, String>> allProvList = new ArrayList<Map<String,String>>();
	private List<String> allProvList = new ArrayList<String>();
	//市listView显示数据
	private List<Map<String, String>> allCityList = new ArrayList<Map<String,String>>();
	//判断省/或市标志
	private Boolean cityOrProv = false;
	//滑动sidbar显示字母
//	private TextView dialog;
	//上下箭头
	private ImageView provUp;
	private ImageView provDown;
	private ImageView cityUp;
	private ImageView cityDown;
	//搜索框
//	private ClearEditText mClearEditText;
	//城市多语言代码
	private String displayNo;
	
	// 判断是否选择地区（没选择显示默认北京，弹出提示框，提示选择）
	//private boolean hasChooseArea = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.plps_order_sidbar);
		initViews();
		setTitle("民生缴费");
		mRightButton.setVisibility(View.GONE);
		cityAdress.setVisibility(View.VISIBLE);
		cityAdress.setClickable(false);
		btnhide.setVisibility(View.GONE);
	}
	private void initViews(){
		Intent intent = getIntent();
		provName = intent.getStringExtra(Plps.PRVCDISPNAME);
		cityName = intent.getStringExtra(Plps.CITYDISPNAME);
		if (StringUtil.isNull(provName) || StringUtil.isNull(cityName)) {
			cityAdress.setText(Plps.DEFAULT_AREA);
			provName = "北京";
			cityName = "北京市";
		//	hasChooseArea = false;
		} else {
			String prvcDispNameReplace = provName.trim();
			if(PlpsDataCenter.municiplGovernment.contains(prvcDispNameReplace)){
				cityAdress.setText(provName);
			}else {
				cityAdress.setText(provName+cityName);
			}
		//	hasChooseArea = true;
		}
//		mClearEditText = (ClearEditText)findViewById(R.id.filter_edit);
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparatorSid();
		provLinearLayout = (LinearLayout)findViewById(R.id.ll_up);
		provTextView = (TextView)findViewById(R.id.prvadress);
		provTextView.setText(provName);
		provDown = (ImageView)findViewById(R.id.query_down);
		provUp = (ImageView)findViewById(R.id.query_up);
		provTextView.setTextColor(this.getResources().getColor(R.color.red));
		cursor = (TextView)findViewById(R.id.cursor);
		
		cityLinearLayout = (LinearLayout)findViewById(R.id.ll_up2);
		cityTextView = (TextView)findViewById(R.id.cityadress);
		cityTextView.setText(cityName);
		cityUp = (ImageView)findViewById(R.id.query_up2);
		cityDown = (ImageView)findViewById(R.id.query_down2);
		cursort = (TextView)findViewById(R.id.cursor2);
		// 省列表
		provListView = (ListView) findViewById(R.id.country_lvcountry);

		// 市列表
		cityListView = (ListView) findViewById(R.id.city_lvcountry);
//		dialog = (TextView)findViewById(R.id.dialog);
		
		cityAdressSideBar = (CityAdressSideBar)findViewById(R.id.sidbar);
//		cityAdressSideBar.setTextView(dialog);
		
		//设置右侧触摸监听
		cityAdressSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				// 字母首次出现的位置
				if(cityOrProv){
					int position = citySidbarSortAdapter.getPositionForSection(s.charAt(0));
					if(position!= -1){
						cityListView.setSelection(position);
					}
				}else {
					int position = sidbarSortAdapter.getPositionForSection(s.charAt(0));
					if(position != -1){
						provListView.setSelection(position);
					}
				}
			}
		});
		
		provListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// 根据adapter获取当前position所选的的对象
				String priName = ((SortData)sidbarSortAdapter.getItem(position)).getName();
				provTextView.setText(priName);
				provTextView.setTextColor(getResources().getColor(R.color.black));
				provUp.setVisibility(View.VISIBLE);
				provDown.setVisibility(View.GONE);
				provListView.setVisibility(View.GONE);
				cityListView.setVisibility(View.VISIBLE);
				cityTextView.setTextColor(getResources().getColor(R.color.red));
				cityOrProv = true;
				cityUp.setVisibility(View.GONE);
				cityDown.setVisibility(View.VISIBLE);
				cursor.setVisibility(View.GONE);
				cursort.setVisibility(View.VISIBLE);
//				mClearEditText.setVisibility(View.VISIBLE);
				String provShort = PlpsDataCenter.mapCode_prov.get(priName);
				requestPlpsGetCityListByPrvcShortName(provShort);
			}
		});
		allProvList =  PlpsDataCenter.provListt;
		sourceDateList = filledDataProv(allProvList);
		//根据a-z排序
		Collections.sort(sourceDateList, pinyinComparator);
		sidbarSortAdapter = new SidbarSortAdapter(this, sourceDateList);
		provListView.setAdapter(sidbarSortAdapter);
		
		cityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// 判断是否选择地区
			//	hasChooseArea = true;
				// 根据adapter获取当前position所选的的对象
				String citName = ((SortData)citySidbarSortAdapter.getItem(position)).getName();
				displayNo = ((SortData)citySidbarSortAdapter.getItem(position)).getDisPlayNo();
//				for(int i=0; i<allCityList.size(); i++){
//					String citNameCompare = (String)allCityList.get(i).get(Plps.CITYNAME);
//					if(citName.equals(citNameCompare)){
//						displayNo = (String)allCityList.get(i).get(Plps.DISPLAYNO);
//						if (!StringUtil.isNullOrEmpty(displayNo)) {
//							PlpsDataCenter.getInstance().setDisplayNo(displayNo);
//						}
//					}
//				}
				if(!StringUtil.isNullOrEmpty(displayNo)){
					PlpsDataCenter.getInstance().setDisplayNo(displayNo);
				}
				cityTextView.setText(citName);
				String provceName = null;
				provceName =(String)provTextView.getText().toString();
				String prvcShortName = null;
				for(int i=0; i<allProvList.size(); i++){
//					String prvcName = (String)allProvList.get(i).get(Plps.PRVCDISPNAME);
					String prvcName = (String)allProvList.get(i);
					if(provceName.equals(prvcName)){
//						prvcShortName = (String)allProvList.get(i).get(Plps.PRVCSHORTNAME);
						prvcShortName = (String)PlpsDataCenter.mapCode_prov.get(prvcName);
						PlpsDataCenter.getInstance().setPrvcShortName(prvcShortName);
					}
				}
				PlpsDataCenter.getInstance().setPrvcDispName(provceName);
				PlpsDataCenter.getInstance().setCityDispName(cityTextView.getText().toString());
				requestCommConversationId();
			}
		});
		mLeftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (!hasChooseArea) {
//					BaseDroidApp.getInstanse().showMessageDialog(getResources().getString(R.string.plps_choose_area_error),
//							new OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									BaseDroidApp.getInstanse()
//											.dismissMessageDialog();
//								}
//							});
//				} else {
//					finish();
//				}
				finish();
			}
		});
		provLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				cityListView.setVisibility(View.GONE);
				provListView.setVisibility(View.VISIBLE);
				cityTextView.setTextColor(getResources().getColor(R.color.black));
				cityDown.setVisibility(View.GONE);
				cityUp.setVisibility(View.VISIBLE);
				cursort.setVisibility(View.GONE);
				provTextView.setTextColor(getResources().getColor(R.color.red));
				provUp.setVisibility(View.GONE);
				provDown.setVisibility(View.VISIBLE);
				cursor.setVisibility(View.VISIBLE);
				cityOrProv=false;
//				mClearEditText.setVisibility(View.GONE);
			}
		});
		cityLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				provListView.setVisibility(View.GONE);
				cityListView.setVisibility(View.VISIBLE);
				provTextView.setTextColor(getResources().getColor(R.color.black));
				provUp.setVisibility(View.VISIBLE);
				provDown.setVisibility(View.GONE);
				cursor.setVisibility(View.GONE);
				cityTextView.setTextColor(getResources().getColor(R.color.red));
				cityDown.setVisibility(View.VISIBLE);
				cityUp.setVisibility(View.GONE);
				cursort.setVisibility(View.VISIBLE);
//				mClearEditText.setVisibility(View.VISIBLE);
				if(StringUtil.isNullOrEmpty(allCityList)){
					cityOrProv = true;
					requestPlpsGetCityListByPrvcShortName(PlpsDataCenter.mapCode_prov.get(provTextView.getText().toString()));
				}
				
			}
		});
		//根据输入框输入值的改变来过滤搜索
//		mClearEditText.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start,
//					int before, int count) {
//				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
//				filterData(s.toString());
//				
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence paramCharSequence,
//					int paramInt1, int paramInt2, int paramInt3) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable paramEditable) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		pSNGetTokenId();
	}
	/**
	 * 请求token*/
	private void pSNGetTokenId(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
	}
	public void aquirePSNGetTokenId(Object resultObj) {
		String tokenId = (String) this.getHttpTools().getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(tokenId)) {
			BaseHttpEngine.dissMissProgressDialog(); return;
		}
		requestPlpsSetDefaltArea(tokenId,PlpsDataCenter.getInstance().getPrvcShortName(), cityTextView.getText().toString(),displayNo);
	}
	@Override
	public void requestPlpsSetDefaltAreaCallBack(Object resultObj) {
		// TODO Auto-generated method stub
		super.requestPlpsSetDefaltAreaCallBack(resultObj);
		Intent intent = new Intent();
		intent.putExtra(Plps.PRVCDISPNAME, provTextView.getText().toString());
		intent.putExtra(Plps.CITYDISPNAME, cityTextView.getText().toString());
		setResult(RESULT_OK, intent);
		finish();
	}
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortData> filterDateList = new ArrayList<SortData>();
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = sourceDateList;
		}else {
			filterDateList.clear();
			for(SortData sortData : sourceDateList){
				String name = sortData.getName();
				if(name.indexOf(filterStr.toString()) != -1 ||characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortData);
				}
				
			}
		}
		//根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		sidbarSortAdapter.updateListView(filterDateList);
	}
	/**
	 * 查询某省已开通民生缴费项目的城市列表*/
	private void requestPlpsGetCityListByPrvcShortName(String prvcShortName){
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBodey = new BiiRequestBody();
		biiRequestBodey.setMethod(Plps.GETCITYLISTBYPRVC);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Plps.PRVCSHORTNAME, prvcShortName);
		biiRequestBodey.setParams(map);
		HttpManager.requestBii(biiRequestBodey, this, "requestPlpsGetCityListByPrvcShortNameCallBack");
	}

	@SuppressWarnings("unchecked")
	public void requestPlpsGetCityListByPrvcShortNameCallBack(Object resultObj) {
		
		Map<String, Object> cityResult = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(cityResult)){
			BaseHttpEngine.dissMissProgressDialog();
			return;}
		allCityList.clear();
		allCityList = (List<Map<String, String>>) cityResult
				.get(Plps.CITYLISTM);
		if(allCityList.size()>1){
			BaseHttpEngine.dissMissProgressDialog();
			sourceDateList = filledData(allCityList);
			//根据a-z排序
			Collections.sort(sourceDateList, pinyinComparator);
			citySidbarSortAdapter = new CitySidbarSortAdapter(this, sourceDateList);
			cityListView.setAdapter(citySidbarSortAdapter);
		}else {
			String citName = allCityList.get(0).get(Plps.CITYNAME);
			
			displayNo = (String) allCityList.get(0).get(Plps.DISPLAYNO);
			if (!StringUtil.isNullOrEmpty(displayNo)) {
				PlpsDataCenter.getInstance().setDisplayNo(displayNo);
			}	
			cityTextView.setText(citName);
			String provceName = null;
			provceName =(String)provTextView.getText().toString();
			String prvcShortName = null;
			for(int i=0; i<allProvList.size(); i++){
//				String prvcName = (String)allProvList.get(i).get(Plps.PRVCDISPNAME);
				String prvcName = (String)allProvList.get(i);
				if(provceName.equals(prvcName)){
//					prvcShortName = (String)allProvList.get(i).get(Plps.PRVCSHORTNAME);
					prvcShortName = (String)PlpsDataCenter.mapCode_prov.get(prvcName);
					PlpsDataCenter.getInstance().setPrvcShortName(prvcShortName);
				}
			}
			PlpsDataCenter.getInstance().setPrvcDispName(provceName);
			PlpsDataCenter.getInstance().setCityDispName(cityTextView.getText().toString());
			requestCommConversationId();
		}
		
	}
	 /** 
     * 为ListView填充数据 
     * @param date 
     * @return 
     */  
	private List<SortData> filledData(List<Map<String, String>> date) {
		List<SortData> mSortDataList = new ArrayList<SortData>();

		for (int i = 0; i < date.size(); i++) {
			Map<String, String> dataMap = date.get(i);
			SortData sortData = new SortData();
			String nameStr = null;
			String disPlayNo = null;
			if (!cityOrProv) {
				nameStr = dataMap.get(Plps.PRVCDISPNAME);
			} else {
				nameStr = dataMap.get(Plps.CITYNAME);
				disPlayNo = dataMap.get(Plps.DISPLAYNO);
			}
			if ("Y".equals(dataMap.get("flag"))) {
				sortData.setName(nameStr);
				// sortModel.setSortLetters("常用");
			} else {
				sortData.setName(nameStr);
				sortData.setDisPlayNo(disPlayNo);
				// 汉字转换成拼音
				String pinyin = null;
				// if(!cityOrProv){
				// pinyin =
				// characterParser.getSelling((String)date.get(i).get(Plps.PRVCDISPNAME));
				// }else {
				pinyin = characterParser.getSelling(nameStr);
				// }
				// String pinyin =
				// characterParser.getSelling((String)date.get(i).get(Tran.PAYEE_BANKNAME_REQ));
				if (nameStr.equals("重庆")) {
					pinyin = "chongqing";
				} else if (nameStr.equals("长沙")) {
					pinyin = "changsha";
				} else if (nameStr.equals("厦门")) {
					pinyin = "xiamen";
				}else if (nameStr.equals("漯河")) {
					pinyin = "luohe";
				}
				String sortString = pinyin.substring(0, 1).toUpperCase();

				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					sortData.setSortLetters(sortString.toUpperCase());
				} else {
					sortData.setSortLetters("#");
				}
			}
			mSortDataList.add(sortData);
		}
		return mSortDataList;
	}
	 /** 
     * 为省ListView填充数据 
     * @param date 
     * @return 
     */  
	private List<SortData> filledDataProv(List<String> date) {
		List<SortData> mSortDataList = new ArrayList<SortData>();
		for (int i = 0; i < date.size(); i++) {
			String dataMap = date.get(i);
			SortData sortData = new SortData();
			sortData.setName(dataMap);
			// 汉字转换成拼音
			String pinyin = null;
			pinyin = characterParser.getSelling((String) date.get(i));
			if(dataMap.equals("重庆")){
				pinyin = "chongqing";
			}else if (dataMap.equals("长沙")) {
				pinyin = "changsha";
			}else if (dataMap.equals("厦门")) {
				pinyin = "xiamen";
			}
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortData.setSortLetters(sortString.toUpperCase());
			} else {
				sortData.setSortLetters("#");
			}
			mSortDataList.add(sortData);
		}
		return mSortDataList;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (hasChooseArea) {
//			super.onKeyDown(keyCode, event);
//		}else {
//			BaseDroidApp.getInstanse().showMessageDialog(getResources().getString(R.string.plps_choose_area_error),
//					new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					BaseDroidApp.getInstanse()
//					.dismissMessageDialog();
//				}
//			});
//		}
		super.onKeyDown(keyCode, event);
		return true;
	}
}
