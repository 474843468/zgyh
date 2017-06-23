package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

/**
 * 中银理财-组合购买，持有产品列表页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyComboBuyGuarantyProductListActivity extends BocInvtBaseActivity implements ICommonAdapter<Map<String, Object>>{

	/**持有产品列表*/
	private Map<String, Object> GuarantyProductList_map;
	/**用户选择的账户 信息*/
//	private Map<String, Object> chooseMap; 
//	/** 产品详情列表 */
//	private Map<String, Object> detailMap;
//	/** 用户选择的账户信息 */
//	private Map<String, Object> accound_map;
	private String mIsFlag="isFlag";
//	/**总组合份额*/
//	private Double combo_sum;
	private ArrayList<RegexpBean> list_rb=new ArrayList<RegexpBean>();
	/** 产品详情列表 */
//	private Map<String, Object> detailMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLeftButtonPopupGone();
		getBackgroundLayout().setTitleText("组合购买");
		getBackgroundLayout().setLeftButtonText("返回");
		getBackgroundLayout().setLeftButtonClickListener(backClickListener);
		setContentView(R.layout.product_query_and_buy_combo_buy_product_list);

//		chooseMap = BociDataCenter.getInstance().getChoosemap();
//		detailMap=BociDataCenter.getInstance().getDetailMap();
//		accound_map=BocInvestControl.accound_map;
		GuarantyProductList_map=BocInvestControl.GuarantyProductList_map;
		
		initUI();
	}
	/**
	 * 组件初始化及赋值
	 */
	@SuppressLint("ClickableViewAccessibility")
	@SuppressWarnings("unchecked")
	private void initUI(){
		//初始化
//		Button btn_last = (Button) findViewById(R.id.btn_last);
		Button btn_next = (Button) findViewById(R.id.btn_next);
		ListView listview = (ListView) findViewById(R.id.listview);
		//赋值
		btn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BocInvestControl.GuarantyProductList_edit_list.clear();//清空数据
				if (!addProductMessageMap()) {
					return; 
				}
				if (BocInvestControl.GuarantyProductList_edit_list.size()==0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog("请选择产品并输入被组合份额");
					return;
				}
//				if (combo_sum%Double.parseDouble(GuarantyProductList_map.get(BocInvestControl.GUARANTYBUYAMOUT).toString())!=0) {
//					BaseDroidApp.getInstanse().showInfoMessageDialog("被组合份额总量必须是组合买入金额的整数倍");
//					return;
//				}
				requestPsnInvtEvaluationInit();
				
			}
		});
	
		adapter = new CommonAdapter<Map<String, Object>>(ProductQueryAndBuyComboBuyGuarantyProductListActivity.this, 
				(List<Map<String, Object>>)GuarantyProductList_map.get(BocInvt.BOCI_LIST_RES),this);
		listview.setAdapter(adapter);
		listview.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ProductQueryAndBuyComboBuyGuarantyProductListActivity.this.closeInput();
				return false;
			}
			
		});
		
	}
	
	/**
	 * 请求风险评估
	 */
	private void requestPsnInvtEvaluationInit(){
		httpTools.requestHttp(BocInvt.PSNINVTEVALUATIONINIT_API,null, new IHttpResponseCallBack<Map<String, Object>>(){
			@Override
			public void httpResponseSuccess(Map<String, Object> result,String method) {
				BocInvestControl.danger_map=result;
				//进入组合购买确认页
				Intent intent = new Intent(ProductQueryAndBuyComboBuyGuarantyProductListActivity.this,ProductQueryAndBuyComboBuySureActivity.class);
				startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_COMBO_BUY);
				BaseHttpEngine.dissMissProgressDialog();
			}
		});
	}

	/**
	 * 展示 弹出框
	 */
//	private void showPopupWindow(final TextView tv,LayoutInflater inflater,final Map<String, Object> currentItem,ViewGroup viewGroup){
//		final PopupWindow popupWindow = new PopupWindow(this);
//		popupWindow.setAnimationStyle(R.style.popAnimation_up);
//		popupWindow.setOutsideTouchable(true);
//		popupWindow.setFocusable(true);
//		popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.color.transparent_00));
//		DisplayMetrics metrics = new DisplayMetrics();
//		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		int widthPixels = metrics.widthPixels;
//		int heightPixels = metrics.heightPixels;
//		int realWidth=widthPixels*4/5;
//		int realHeight=heightPixels*3/5;
//		popupWindow.setWidth(realWidth);
//		popupWindow.setHeight(realHeight);
//		View view = inflater.inflate(R.layout.product_query_and_buy_popupwindow_layout, viewGroup,false);
//		LabelTextView tv_1 = (LabelTextView) view.findViewById(R.id.tv_1);
//		LabelTextView tv_2 = (LabelTextView) view.findViewById(R.id.tv_2);
//		final EditText et_1 = (EditText) view.findViewById(R.id.et_1);
//		Button btn_sure = (Button) view.findViewById(R.id.btn_sure);
//		tv_1.setValueText(currentItem.get(BocInvestControl.CASHSHARE).toString());
//		tv_2.setValueText(currentItem.get(BocInvestControl.REMITSHARE).toString());
//		btn_sure.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//					//校验 输入值
//				if (et_1.getText().toString().trim().equals("")) {
//					return;
//				}
//					if (0<Double.parseDouble(et_1.getText().toString().trim())
//							&&
//							Double.parseDouble(et_1.getText().toString().trim())<=
//							Double.parseDouble(currentItem.get(BocInvestControl.CASHSHARE).toString())+
//							Double.parseDouble(currentItem.get(BocInvestControl.REMITSHARE).toString())) 
//					{
//						currentItem.put(BocInvestControl.FREEZEUNIT, et_1.getText().toString().trim());
//						tv.setText(et_1.getText().toString());
//						popupWindow.dismiss();
//					}else {
//						//输入金额校验
//					}
//			}
//		});
//		popupWindow.setContentView(view);
//		popupWindow.showAtLocation(tv, Gravity.CENTER, 0, -50);
//	}
	/**
	 * 校验并存储用户选择、输入的产品信息
	 * @return true/列表信息校验添加成功,false/失败
	 */
	@SuppressWarnings({ "unchecked" })
	private boolean addProductMessageMap(){
//		combo_sum=(double) 0;
		for (Map<String, Object> iMap : (List<Map<String, Object>>)GuarantyProductList_map.get(BocInvt.BOCI_LIST_RES)) {
			if (iMap.get(mIsFlag)!=null&&Boolean.parseBoolean(iMap.get(mIsFlag).toString())) {//选中复选框
				//校验输入值
				String tem_str = (String)iMap.get(BocInvestControl.FREEZEUNIT);
//				if (StringUtil.isNullOrEmpty(tem_str)/*||Double.parseDouble(tem_str)==((double)0)*/) {
//					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入被组合份额");
//					return false;
//				}
//				RegexpBean rb = new RegexpBean("被组合份额", tem_str, "tranAmount");
				//2016/5/1前业务确认份额允许输入小数
				RegexpBean rb = BocInvestControl.getRegexpBean(null/*detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString()*/, "被组合份额", tem_str, null);
				list_rb.clear();
				list_rb.add(rb);
				if (!RegexpUtils.regexpDate(list_rb)) {
					return false;
				}
				double d = Double.parseDouble(tem_str);
				if (0<d&&d<=Double.parseDouble(iMap.get(BocInvestControl.CASHSHARE).toString())+
						Double.parseDouble(iMap.get(BocInvestControl.REMITSHARE).toString())) {
					if (tem_str.contains(".")&&tem_str.substring(tem_str.indexOf(".")+1).length()>2) {
						BaseDroidApp.getInstanse().showInfoMessageDialog("请在选中产品输入0到持有份额范围内的数值(最多2位小数)");
						return false;
					}
					BocInvestControl.GuarantyProductList_edit_list.add(iMap);
//					combo_sum+=Double.parseDouble(iMap.get(BocInvestControl.FREEZEUNIT).toString());
				}else {
					BaseDroidApp.getInstanse().showInfoMessageDialog("被组合份额不能大于持有现钞份额和持有现汇份额");
					return false;
				}
			}
		}
		return true;
	}


	

	/**
	 * 监听事件，返回
	 */
	private OnClickListener backClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	private CommonAdapter<Map<String, Object>> adapter;
	@Override
	public View getView(int arg0, final Map<String, Object> currentItem,
			final LayoutInflater inflater, View convertView, final ViewGroup viewGroup) {
		convertView=inflater.inflate(R.layout.product_query_and_buy_combo_buy_product_list_item, viewGroup,false);
		TextView tv_1=(TextView) convertView.findViewById(R.id.tv_1);
		CheckBox check_box=(CheckBox) convertView.findViewById(R.id.check_box);
		TextView tv_2=(TextView) convertView.findViewById(R.id.tv_2);
		EditText et_3=(EditText) convertView.findViewById(R.id.et_3);
		tv_1.setText(currentItem.get(BocInvt.BOCINVT_SIGNINIT_PRODUCTNAME_REQ).toString());
		tv_2.setText(StringUtil.append2Decimals(Double.parseDouble(currentItem.get(BocInvestControl.CASHSHARE).toString())
		+Double.parseDouble(currentItem.get(BocInvestControl.REMITSHARE).toString())+"", 2));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tv_2);
		if (!StringUtil.isNullOrEmpty(currentItem.get(mIsFlag))&&Boolean.parseBoolean(currentItem.get(mIsFlag).toString())) {
			check_box.setChecked(true);
			et_3.setEnabled(true);
			et_3.setBackgroundResource(R.drawable.bg_for_edittext);
			if (!StringUtil.isNullOrEmpty(currentItem.get(BocInvestControl.FREEZEUNIT))) {
				et_3.setText(currentItem.get(BocInvestControl.FREEZEUNIT).toString());
			}
		}else {
			check_box.setChecked(false);
			et_3.setText("");
			et_3.setBackgroundResource(R.drawable.edit_bg_dark);
			et_3.setEnabled(false);
		}
		check_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					currentItem.put(mIsFlag, true);
				}else {
					currentItem.put(mIsFlag, false);
					currentItem.remove(BocInvestControl.FREEZEUNIT);
				}
				adapter.notifyDataSetChanged();
			}
		});
		et_3.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				currentItem.put(BocInvestControl.FREEZEUNIT, s.toString().trim());
			}
		});
	
		return convertView;
	}
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if (BocInvestControl.ACTIVITY_RESULT_CODE_COMBO_BUY==requestCode) {
				setResult(RESULT_OK);
				finish();
			}
			break;

		default:
			break;
		}
	}

}
