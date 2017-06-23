package com.chinamworld.bocmbci.biz.plps.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 养老金业务ListView适配器
 * @author panwe
 *
 */
public class AnnuityAdapter extends BaseAdapter{
	private Context mContext;
	private String planType;
	private List<Map<String, Object>> mList;
	private final String[] type1 = new String[]{"序号","缴费类型","申请日期","缴费周期","到账日期","个人缴费金额","企业缴费金额","合计","企缴税前","企缴税后","本期税款","补缴税款"};
	private final String[] key1 = new String[]{Plps.SEQNO,Plps.PAYMENTTYPE,Plps.APPLYDATE,Plps.PAYMENTCYCLE,Plps.ARRIVEDATE,Plps.PERSONPAYMENTAMOUNT,Plps.ENTPAYMENTAMOUNT,Plps.AMOUNT,Plps.ENTPRETAX,Plps.ENTAFTERTAX,Plps.TAX,Plps.SUBTAX};
	
	private final String[] type2 = new String[]{"序号","投资组合名称","单位净值","净值日期","备注"};
	private final String[] key2 = new String[]{Plps.SEQNO,Plps.INVESTCOMPOUNDINGNAME,Plps.UNITVALUE,Plps.VALUEDATE,Plps.REMARK};
	
	private final String[] type3 = new String[]{"序号","实付日期","实付金额","银行户名","开户银行","银行账号","支付方式","支付原因","备注"};
	private final String[] key3 = new String[]{Plps.SEQNO,Plps.PAYDATE,Plps.PAYAMOUNT,Plps.BANKACCOUNTNAME,Plps.BANKNAME,Plps.BANKACCOUNTNUM,Plps.PAYTYPE,Plps.PAYSREZ,Plps.REMARK};
	
	private final String[] type4 = new String[]{"序号","转移日期","转移金额","转移方式","转移原因","转移证明文件"};
	private final String[] key4 = new String[]{Plps.SEQNO,Plps.TRANSFERDATE,Plps.TRANSFERAMOUNT,Plps.TRANSFERTYPE,Plps.TRANSFERREZ,Plps.FILE};
	
	private final String[] type5 = new String[]{"序号","转换日期","账号编号","账号名称","投资组合编号","投资组合名称","期望占比"};
	private final String[] key5 = new String[]{Plps.SEQNO,Plps.TRANSFERDATE,Plps.ACCOUNTNO,Plps.ACCOUNTNAME,Plps.INVESTCOMPOUNDINGNO,Plps.INVESTCOMPOUNDINGNAME,Plps.EXPECTRATE};
	
	private final String[] type6 = new String[]{"序号","申请日期","记账日期","账户名称","投资组合名称","划转类型","划转方向","划转数额","备注"};
	private final String[] key6 = new String[]{Plps.SEQNO,Plps.APPLYDATE,Plps.RECODEDATE,Plps.ACCOUNTNAME,Plps.INVESTCOMPOUNDINGNAME,Plps.TRANSFERTYPE,Plps.TRANSFERDIRECTION,Plps.TRANSFERAMOUNT,Plps.REMARK};
	
	private final String[] type7 = new String[]{"序号","申请日期","分配日期","分配金额","备注"};
	private final String[] key7 = new String[]{Plps.SEQNO,Plps.APPLYDATE,Plps.ASSIGNDATE,Plps.ASSIGNAMOUNT,Plps.REMARK};
	
	private final String[] type8 = new String[]{"序号","业务类型","申报日期","处理日期","处理结果","说明"};
	private final String[] key8 = new String[]{Plps.SEQNO,Plps.BUSSINESSTYPE,Plps.APPLYDATE,Plps.DISPOSEDATE,Plps.RESULT,Plps.EXPLAIN};
	
	public AnnuityAdapter(Context c,List<Map<String, Object>> list){
		this.mContext = c;
		this.mList = list;
	}

	@Override
	public int getCount() {
		if (StringUtil.isNullOrEmpty(mList)) {
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (StringUtil.isNullOrEmpty(mList)) {
			return null;
		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler h;
		if (convertView == null) {
			h = new ViewHodler();
			convertView = View.inflate(mContext, R.layout.plps_annuity_list_item, null);
			h.mView = (LinearLayout) convertView.findViewById(R.id.layout);
			createChildView(h,h.mView);
			convertView.setTag(h);
		}else{
			h = (ViewHodler) convertView.getTag();
		}
		setText(h.mView,mList.get(position));
		return convertView;
	}
	
	/**
	 * 刷新数据
	 * @param list
	 * @param planType
	 */
	public void setData(List<Map<String, Object>> list,String planType) {
		this.mList = list;
		this.planType = planType;
		notifyDataSetChanged();
	}
	
	/**
	 * 创建控件
	 * @param h
	 * @param v
	 */
	private void createChildView(ViewHodler h,LinearLayout v){
		int count = 0;
		if (planType.equals("1")) {
			count = type1.length;
		}else if (planType.equals("2")) {
			count = type2.length;
		}else if(planType.equals("3")){
			count = type3.length;
		}else if(planType.equals("4")){
			count = type4.length;
		}else if(planType.equals("5")){
			count = type5.length;
		}else if(planType.equals("6")){
			count = type6.length;
		}else if(planType.equals("7")){
			count = type7.length;
		}else if(planType.equals("8")){
			count = type8.length;
		}
		for (int i = 0; i < count; i++) {
			LinearLayout mLayout = new LinearLayout(mContext);
			h.leftView = new TextView(mContext, null, R.style.textview_common);
			h.rightView = new TextView(mContext, null, R.style.textview_common);
			if (planType.equals("1")) {
				h.leftView.setText(type1[i]+"：");
			}else if(planType.equals("2")){
				h.leftView.setText(type2[i]+"：");
			}else if(planType.equals("3")){
				h.leftView.setText(type3[i]+"：");
			}else if(planType.equals("4")){
				h.leftView.setText(type4[i]+"：");
			}else if(planType.equals("5")){
				h.leftView.setText(type5[i]+"：");
			}else if(planType.equals("6")){
				h.leftView.setText(type6[i]+"：");
			}else if(planType.equals("7")){
				h.leftView.setText(type7[i]+"：");
			}else if(planType.equals("8")){
				h.leftView.setText(type8[i]+"：");
			}
			mLayout.addView(h.leftView);
			mLayout.addView(h.rightView);
			mLayout.setPadding(10, 2, 8, 2);
			v.addView(mLayout);
		}
	}
	
	/**
	 * 赋值
	 * @param v
	 * @param map
	 */
	private void setText(LinearLayout v,Map<String, Object> map){
		int count = v.getChildCount();
		for (int i = 0; i < count; i++) {
			LinearLayout childV = (LinearLayout) v.getChildAt(i);
				//服务类别1
			if (planType.equals("1")) {
				if (key1[i].equals(Plps.PAYMENTTYPE)) {
					if(StringUtil.isNullOrEmpty(map.get(key1[i]))){
						((TextView) childV.getChildAt(1)).setText("-");
					}else {
						((TextView) childV.getChildAt(1)).setText(PlpsDataCenter.paymentType.get(map.get(key1[i])));
					}
				}else{
					if(StringUtil.isNullOrEmpty(map.get(key1[i]))){
						((TextView) childV.getChildAt(1)).setText("-");
					}else {
						((TextView) childV.getChildAt(1)).setText((String)map.get(key1[i]));
					}
				}
				//服务类别2
			}else if(planType.equals("2")){
				if(StringUtil.isNullOrEmpty(map.get(key2[i]))){
					((TextView) childV.getChildAt(1)).setText("-");
				}else {
					((TextView) childV.getChildAt(1)).setText((String)map.get(key2[i]));
				}
				//服务类别3
			}else if(planType.equals("3")){
				if (key3[i].equals(Plps.PAYTYPE)) {
					if(StringUtil.isNullOrEmpty(map.get(key3[i]))){
						((TextView) childV.getChildAt(1)).setText("-");
					}else {
						((TextView) childV.getChildAt(1)).setText(PlpsDataCenter.payType.get(map.get(key3[i])));
					}
				}else if(key3[i].equals(Plps.PAYSREZ)){
					if(StringUtil.isNullOrEmpty(map.get(key3[i]))){
						((TextView) childV.getChildAt(1)).setText("-");
					}else {
						((TextView) childV.getChildAt(1)).setText(PlpsDataCenter.paysRez.get(map.get(key3[i])));
					}
				}else {
					if(StringUtil.isNullOrEmpty(map.get(key3[i]))){
						((TextView) childV.getChildAt(1)).setText("-");
					}else {
						((TextView) childV.getChildAt(1)).setText((String)map.get(key3[i]));
					}
				}
				//服务类别4
			}else if(planType.equals("4")){
				if (key4[i].equals(Plps.TRANSFERTYPE)) {
					if(StringUtil.isNullOrEmpty(map.get(key4[i]))){
						((TextView) childV.getChildAt(1)).setText("-");
					}else {
						((TextView) childV.getChildAt(1)).setText(PlpsDataCenter.transferType.get(map.get(key4[i])));
					}
				}else if(key4[i].equals(Plps.TRANSFERREZ)){
					if (StringUtil.isNullOrEmpty(map.get(key4[i]))) {
						((TextView) childV.getChildAt(1)).setText("-");
					}else {
						((TextView) childV.getChildAt(1)).setText(PlpsDataCenter.transferRez.get(map.get(key4[i])));
					}
				}else {
					if(StringUtil.isNullOrEmpty(map.get(key4[i]))){
						((TextView) childV.getChildAt(1)).setText("-");
					}else {
						((TextView) childV.getChildAt(1)).setText((String)map.get(key4[i]));
					}
				}
				//服务类别5
			}else if(planType.equals("5")){
				if(StringUtil.isNullOrEmpty(map.get(key5[i]))){
					((TextView) childV.getChildAt(1)).setText("-");
				}else {
					((TextView) childV.getChildAt(1)).setText((String)map.get(key5[i]));
				}
				//服务类型6
			}else if(planType.equals("6")){
				if (key6[i].equals(Plps.TRANSFERTYPE)) {
					if(StringUtil.isNullOrEmpty(map.get(key6[i]))){
						((TextView) childV.getChildAt(1)).setText("-");
					}else {
						((TextView) childV.getChildAt(1)).setText(PlpsDataCenter.transFerType.get(map.get(key6[i])));
					}
				}else if(key6[i].equals(Plps.TRANSFERDIRECTION)){
					if(StringUtil.isNullOrEmpty(map.get(key6[i]))){
						((TextView) childV.getChildAt(1)).setText("-");
					}else {
						((TextView) childV.getChildAt(1)).setText(PlpsDataCenter.transferDirection.get(map.get(key6[i])));
					}
				}else {
					if(StringUtil.isNullOrEmpty(map.get(key6[i]))){
						((TextView) childV.getChildAt(1)).setText("-");
					}else {
						((TextView) childV.getChildAt(1)).setText((String)map.get(key6[i]));
					}
				}
				//服务类型7
			}else if(planType.equals("7")){
				if(StringUtil.isNullOrEmpty(map.get(key7[i]))){
					((TextView) childV.getChildAt(1)).setText("-");
				}else {
					((TextView) childV.getChildAt(1)).setText((String)map.get(key7[i]));
				}
				//服务类型8
			}else if(planType.equals("8")){
				if(StringUtil.isNullOrEmpty(map.get(key8[i]))){
					((TextView) childV.getChildAt(1)).setText("-");
				}else {
					((TextView) childV.getChildAt(1)).setText((String)map.get(key8[i]));
				}
			}
		}
	}
	
	public class ViewHodler {
		public LinearLayout mView;
		public TextView leftView;
		public TextView rightView;
	}
}
