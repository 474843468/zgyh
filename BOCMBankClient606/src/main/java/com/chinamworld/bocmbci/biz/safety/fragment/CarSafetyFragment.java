package com.chinamworld.bocmbci.biz.safety.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.carsafety.CarSafetyBaseActivity;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 车险车辆基本信息片段<br>
 * 职责 1.判断显示未上牌还是已上牌片段<br>
 * 2.通过片段暴露的接口与片段通信
 * 
 * @author Zhi
 */
public class CarSafetyFragment extends Fragment {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 此Fragment依附的Activity */
	private CarSafetyBaseActivity activity;
	/** 主显示视图 */
	private View mMainView;
	/** 车险开通地区 */
	private TextView tvTitle;
	/** 顶部车险开通地区红色字体文字 */
	private String tipInfo;
	/** 已上牌单选按钮 */
	private RadioButton rbYiShangPai;
	/** 未上牌单选按钮 */
	private RadioButton rbWeiShangPai;
	/** 查询按钮 */
	private Button btnQuery;
	/** 已上牌时查询按钮 */
	private Button btnQueryBig;
	/** 暂存按钮 */
	private Button btnSave;
	/** 按首字母排序后的简称列表 */
	private List<String> listLicenseNoZoneSimple;
	/** 按首字母排序后的行驶省份列表*/
	private List<String> listZone;
	/** 开通省份代码列表-code */
	private List<String> areaCodeList = new ArrayList<String>();
	/** 未上牌片段 */
	private WeiShangPaiFragment wspFragment;
	/** 已上牌片段 */
	private YiShangPaiFragment yspFragment;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public CarSafetyFragment(CarSafetyBaseActivity activity) {
//		this.activity = activity;
//	}
//

	@Override
	public void onAttach(Activity activity) {
		this.activity = (CarSafetyBaseActivity)activity;
		super.onAttach(activity);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.safety_carsafety_fragment, null);
		findView(mMainView);
		viewSet();
		pageSet();
		return mMainView;
	}
	
	/** 控件赋值 */
	private void findView(View mMainView) {
		tvTitle = (TextView) mMainView.findViewById(R.id.tv_title);
		rbYiShangPai = (RadioButton) mMainView.findViewById(R.id.rabtn1);
		rbWeiShangPai = (RadioButton) mMainView.findViewById(R.id.rabtn2);
		btnQuery = (Button) mMainView.findViewById(R.id.btnQuery);
		btnQueryBig = (Button) mMainView.findViewById(R.id.btnQuery_big);
		btnSave = (Button) mMainView.findViewById(R.id.btnSave);
		wspFragment = new WeiShangPaiFragment();
		yspFragment = new YiShangPaiFragment();
	}
	
	/** 控件设置 */
	private void viewSet() {
		btnQuery.setOnClickListener(queryListener);
		btnQueryBig.setOnClickListener(queryListener);
		btnSave.setOnClickListener(saveClickListener);
		
		rbWeiShangPai.setOnClickListener(rbListener);
		rbYiShangPai.setOnClickListener(rbListener);
		tvTitle.setText(tipInfo);
	}
	
	/** 页面显示设置 */
	private void pageSet() {
		FragmentManager fm = activity.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		// 根据续保、暂存保单继续投保和投保流程显示相应片段
		if (SafetyDataCenter.getInstance().isHoldToThere) {
			mMainView.findViewById(R.id.loan_advance_type).setVisibility(View.GONE);
			yspViewState();
			ft.replace(R.id.ll_carsafety, yspFragment);
		} else if (SafetyDataCenter.getInstance().isSaveToThere) {
			if (StringUtil.isNullOrEmpty(SafetyDataCenter.getInstance().getMapAutoInsurPolicyDetailQry().get(Safety.LICENSENO))) {
				wspViewState();
				ft.replace(R.id.ll_carsafety, wspFragment);
				wspFragment.tempState = true;
			} else {
				yspViewState();
				ft.replace(R.id.ll_carsafety, yspFragment);
				yspFragment.tempState = true;
			}
		} else {
			yspViewState();
			ft.replace(R.id.ll_carsafety, yspFragment);
		}
		
		ft.commit();
	}
	
	/** 已上牌页面状态 */
	private void yspViewState() {
		rbYiShangPai.setChecked(true);
		btnQuery.setVisibility(View.GONE);
		btnSave.setVisibility(View.GONE);
		btnQueryBig.setVisibility(View.VISIBLE);
		yspFragment.setSimZone(listLicenseNoZoneSimple);
	}
	
	/** 未上牌页面状态 */
	private void wspViewState() {
		rbWeiShangPai.setChecked(true);
		btnQuery.setVisibility(View.VISIBLE);
		btnSave.setVisibility(View.VISIBLE);
		btnQueryBig.setVisibility(View.GONE);
		wspFragment.setZone(listZone);
	}



	public void setActivity(Activity activity) {
		this.activity = (CarSafetyBaseActivity)activity;
	}

	/**
	 * 此片段对外接口，初始化与车险开通地区有关的数据和视图
	 * 
	 * @param areaCodeList
	 *            车险开通地区码列表
	 */
	public void initAreaInfo(List<String> areaCodeList) {
		// 为基本信息视图上边的TextView赋值
		tipInfo = getTipInfo(areaCodeList);
		setAreaData(areaCodeList);
		
		SafetyDataCenter.getInstance().setListAutoInsuranceQueryAllowArea(areaCodeList);
		if (SafetyDataCenter.getInstance().isHoldToThere || SafetyDataCenter.getInstance().isSaveToThere) {
			// 如果是持有保单续保，没有暂存功能，暂存保单继续投保不更新暂存单id，直接请求车险交易ID
			activity.getHttpTools().requestHttp(Safety.METHOD_PSNAUTOINSURANCEGETTRANSID, "requestPsnAutoInsuranceGetTransIdCallBack", null, true);
		} else {
			// 投保和暂存单继续投保都有暂存功能，要请求获取暂存单ID接口
			activity.getHttpTools().requestHttp(Safety.METHOD_PSNAUTOINSURGETPOLICYID, "requestPsnAutoInsurGetPolicyIdCallBack", null, true);
		}
	}
	
	/** 根据车险开通地区列表获取顶部红色字体提示文字 */
	private String getTipInfo(List<String> areaCodeList) {
		String tipInfo = "车险产品目前仅支持如下" + foematInteger(areaCodeList.size()) + "个地区投保：\n";
		int areaListSize = 0;
		List<String> listCN = SafetyDataCenter.listCN;
		for (int i = 0; i < listCN.size(); i++) {
			for (int j = 0; j < areaCodeList.size(); j++) {
				if (listCN.get(i).equals(areaCodeList.get(j))) {
					areaListSize ++;
					tipInfo += SafetyDataCenter.mapCode_CN.get(listCN.get(i));
					
					if (areaListSize != areaCodeList.size()) {
						tipInfo += "、";
					}
				}
			}
		}
		return tipInfo;
	}
	
	/** 根据车险开通地区列表设置简称列表和行驶省份列表 */
	private void setAreaData(List<String> areaCodeList) {
		listLicenseNoZoneSimple = new ArrayList<String>();
		listZone = new ArrayList<String>();
		List<String> listCNS = SafetyDataCenter.listCNS;
		for (int i = 0; i < listCNS.size(); i++) {
			for (int j = 0; j < areaCodeList.size(); j++) {
				if (areaCodeList.get(j).equals(listCNS.get(i))) {
					listLicenseNoZoneSimple.add(SafetyDataCenter.mapCityCode_CNS.get(areaCodeList.get(j)));
					listZone.add(SafetyDataCenter.mapCityCode_CN.get(areaCodeList.get(j)));
					this.areaCodeList.add(areaCodeList.get(j));
					break;
				}
			}
		}
	}

	/** 阿拉伯数字转汉字数字算法 */
	private static String foematInteger(int num) {
		String si = String.valueOf(num);
		String[] aa = { "", "十", "百", "千", "万", "十", "百", "千", "亿", "十" };
		String[] bb = { "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		char[] ch = si.toCharArray();
		StringBuffer sb = new StringBuffer();
		int maxindex = ch.length;
		// 字符的转换
		// 两位数的特殊转换
		if (maxindex == 2) {
			for (int i = maxindex - 1, j = 0; i >= 0; i--, j++) {
				if (ch[j] != 48) {
					if (j == 0 && ch[j] == 49) {
						sb.append(aa[i]);
					} else {
						sb.append(bb[ch[j] - 49] + aa[i]);
					}
				}
			}
			// 其他位数的特殊转换，使用的是int类型最大的位数为十亿
		} else {
			for (int i = maxindex - 1, j = 0; i >= 0; i--, j++) {
				if (ch[j] != 48) {
					sb.append(bb[ch[j] - 49] + aa[i]);
				}
			}
		}
		return sb.toString();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** 已上牌或未上牌单选按钮点击事件 */
	private OnClickListener rbListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (rbYiShangPai.isChecked()) {
				yspViewState();
				FragmentManager fm = activity.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.hide(wspFragment);
				ft.replace(R.id.ll_carsafety, yspFragment);
				ft.commit();
			} else {
				wspViewState();
				FragmentManager fm = activity.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.hide(yspFragment);
				ft.replace(R.id.ll_carsafety, wspFragment);
				ft.commit();
			}
		}
	};

	/** 车险界面查询按钮点击事件 */
	private OnClickListener queryListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (rbYiShangPai.isChecked()) {
				yspFragment.query();
			} else {
				wspFragment.query();
			}
		}
	};

	/** 暂存保单按钮点击事件 */
	private OnClickListener saveClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (wspFragment.submitRegexp(false)) {
				wspFragment.putSaveParams();
				activity.showSaveDialog();
			}
		}
	};
}
