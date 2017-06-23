package com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.order;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.adapter.SortAdapter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.addpayee.order.SideBar.OnTouchingLetterChangedListener;

public class OrderMainActivity extends BaseActivity {
	private ListView sortListView;
	private SideBar sideBar;
	private SortAdapter adapter;
	
	  /** 
     * 汉字转换成拼音的类 
     */  
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	 /** 
     * 根据拼音来排列ListView里面的数据类 
     */  
	private PinyinComparator pinyinComparator;

	/** 加载布局 */
	public LinearLayout tabcontent = null;
	/** 用于listview显示数据 */
	private List<Map<String, String>> allBankList = new ArrayList<Map<String, String>>();
	
	private Button btnRight;

	View view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		initPulldownBtn(); // 加载上边下拉菜单
		initFootMenu(); // 加载底部菜单栏
		setLeftButtonPopupGone();//屏蔽左菜单栏
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		tabcontent.setPadding(14, 0, 14, 15);
		LayoutInflater mInflater = LayoutInflater.from(this);
		view = mInflater.inflate(
				R.layout.tran_mytransfer_activity_main, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		initViews(view);
		setTitle("选择账户所属银行");
	}

	private void initViews(View view) {
		  //实例化汉字转拼音类  
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		
		 //设置右侧触摸监听  
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				 //该字母首次出现的位置 
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象  
				
				String cnapsCode = ((SortModel)adapter.getItem(position)).getBankCode();
				String toOrgName = ((SortModel)adapter.getItem(position)).getName();
				
				Intent intent = new Intent();
				intent.putExtra(Tran.PAYEE_CNAPSCODE_REQ, cnapsCode);
				intent.putExtra(Tran.TRANS_BOCNATIONAL_TOORGNAME_REQ, toOrgName);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		allBankList = TranDataCenter.getInstance().getShishiBankList();
		
		SourceDateList = filledData(allBankList);
		   // 根据a-z进行排序源数据  
		
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		
		//返回键
		Button back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		//隐藏右边 主菜单按钮
		btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.GONE);
	}


	  /** 
     * 为ListView填充数据 
     * @param date 
     * @return 
     */  
	private List<SortModel> filledData(List<Map<String, String>> date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.size(); i++){
			Map<String, String> dataMap = date.get(i);
			SortModel sortModel = new SortModel();
			String nameStr = dataMap.get(Tran.PAYEE_BANKNAME_REQ);
			String bankCode = dataMap.get("bankCode");
			if("Y".equals(dataMap.get("flag"))){
				sortModel.setName(nameStr);
				sortModel.setSortLetters("常用");
				sortModel.setBankCode(bankCode);
			}else{
				sortModel.setName(nameStr);
				sortModel.setBankCode(bankCode);
				//汉字转换成拼音  
				String pinyin = characterParser.getSelling((String)date.get(i).get(Tran.PAYEE_BANKNAME_REQ));
				if("zhongqing".equals(pinyin.substring(0,9))){
					pinyin=pinyin.replace("z", "c");	
				}
				String sortString = pinyin.substring(0, 1).toUpperCase();
				
				 // 正则表达式，判断首字母是否是英文字母  
				if(sortString.matches("[A-Z]")){
					sortModel.setSortLetters(sortString.toUpperCase());
				}else {
					sortModel.setSortLetters("#");
				}
			}
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
	
}
