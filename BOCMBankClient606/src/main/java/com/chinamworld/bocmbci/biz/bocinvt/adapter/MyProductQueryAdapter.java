/**
 *代码优化暂时屏蔽该适配器，采用通用的适配器接口来实现功能，请勿删除！谢谢 
 *联龙博通 林耀龙  2016-4-5
 **/
//package com.chinamworld.bocmbci.biz.bocinvt.adapter;
//
//import java.util.List;
//import java.util.Map;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.chinamworld.bocmbci.bii.constant.BocInvt;
//import com.chinamworld.bocmbci.utils.PopupWindowUtils;
//import com.chinamworld.bocmbci.utils.StringUtil;
//import com.chinamworld.bocmbci.R;
//
///**
// * 理财产品历史查询适配器
// * 
// * @author wangmengmeng
// * 
// */
//public class MyProductQueryAdapter extends BaseAdapter {
//	/** 查询结果列表信息 */
//	private List<Map<String, Object>> productQueryList;
//	private Context context;
//	private int traType;//交易类型的标示
//
//	/**
//	 * 适配器构造方法
//	 * @param context 
//	 * @param productQueryList 数据源
//	 */
//	public MyProductQueryAdapter(Context context,
//			List<Map<String, Object>> productQueryList) {
//		this.context = context;
//		this.productQueryList = productQueryList;
//	}
//	/**
//	 * 适配器构造方法
//	 * @param context
//	 * @param productQueryList 数据源
//	 * @param traType  交易类型标示
//	 */
//	public MyProductQueryAdapter(Context context,List<Map<String,Object>> productQueryList,int traType){
//		this.context = context;
//		this.productQueryList = productQueryList;
//		this.traType = traType;
//	}
//
//	@Override
//	public int getCount() {
//		return productQueryList.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return productQueryList.get(position);
//
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		if (convertView == null) {
//			convertView = LayoutInflater.from(context).inflate(
//					R.layout.bocinvt_hispro_list_item, null);
//		}
//		/** 交易日期 */
//		TextView boci_product_name = (TextView) convertView
//				.findViewById(R.id.boci_product_name);
//		/** 产品名称 */
//		TextView boci_yearlyRR = (TextView) convertView
//				.findViewById(R.id.boci_yearlyRR);
//		/** 交易金额 */
//		TextView boci_timeLimit = (TextView) convertView
//				.findViewById(R.id.boci_timeLimit);
//		boci_timeLimit.setTextColor(context.getResources()
//				.getColor(R.color.red));
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				boci_yearlyRR);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				boci_timeLimit);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(context,
//				boci_timeLimit);
//		/** 右三角 */
//		ImageView goDetail = (ImageView) convertView
//				.findViewById(R.id.boci_gotoDetail);
//		goDetail.setVisibility(View.VISIBLE);
//		// 赋值操作
//		switch (traType) {
//		case 1://常规交易
//			boci_product_name.setText(String.valueOf(productQueryList.get(position)
//					.get(BocInvt.BOCINCT_XPADTRAD_PAYMENTDATE_RES)));
//			boci_yearlyRR.setText(String.valueOf(productQueryList.get(position)
//					.get(BocInvt.BOCINCT_XPADTRAD_PRODNAME_RES)));
//			String timeLimit = (String) productQueryList.get(position).get(
//					BocInvt.BOCINCT_XPADTRAD_AMOUNT_RES);
//			String currency = (String) productQueryList.get(position).get(
//					BocInvt.BOCINCT_XPADTRAD_CURRENCYCODE_RES);
//			boci_timeLimit.setText(StringUtil.parseStringCodePattern(currency,
//					timeLimit, 2));
////			String trfType = String.valueOf(productQueryList.get(position)
////					.get(BocInvt.BOCINCT_XPADTRAD_TRFTYPE_RES));
////			String status = String.valueOf(productQueryList.get(position)
////					.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES));
////			String standardPro = String.valueOf(productQueryList.get(position)
////					.get(BocInvt.BOCINCT_XPADTRAD_STANDARDPRO_RES));
////			String productKind = String.valueOf(productQueryList.get(position)
////					.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTKIND_RES));
//			/** 列表页控制进入详情页面 **/
//			//判断item项是否可点击 进入详情页面  右三角显示隐藏状态设置  
////			if(status.equals("2")||status.equals("3")||status.equals("4")||status.equals("5")){//失败
////				goDetail.setVisibility(View.VISIBLE);
////			}else if(status.equals("1")){//成功
////				if(standardPro.equals("1") && productKind.equals("0")){//业绩
////					if(trfType.equals("02")){//常规业绩赎回成功 
////						goDetail.setVisibility(View.VISIBLE);
////					}else{//隐藏 不可点击进入详情
////						goDetail.setVisibility(View.INVISIBLE);
////						convertView.setTag(position);
////						//					convertView.setFocusable(false);
////						//					convertView.setFocusableInTouchMode(false);
////						//					convertView.setClickable(false);
////					}
////				}else if(productKind.equals("1") && standardPro.equals("0")){//净值
////					if(trfType.equals("00") || trfType.equals("01") || trfType.equals("02")){//认购/申购/赎回
////						goDetail.setVisibility(View.VISIBLE);
////					}else{
////						goDetail.setVisibility(View.INVISIBLE);
////						convertView.setTag(position);
////					}
////				}else{//非业绩、非净值
////					goDetail.setVisibility(View.INVISIBLE);
////					convertView.setTag(position);
////				}
////			}else if(status.equals("0")){//委托待处理
////				goDetail.setVisibility(View.INVISIBLE);
////				convertView.setTag(position);
////			}
////			if(standardPro.equals("1")){//业绩
////				if(!status.equals("1")){//交易失败
////					goDetail.setVisibility(View.VISIBLE);
////				}else{//成功交易
////					if(trfType.equals("02")){//常规业绩赎回成功 
////						goDetail.setVisibility(View.VISIBLE);
////					}else{//隐藏 不可点击进入详情
////						goDetail.setVisibility(View.GONE);
////						convertView.setTag(position);
////						//					convertView.setFocusable(false);
////						//					convertView.setFocusableInTouchMode(false);
////						//					convertView.setClickable(false);
////					}
////				}
////			}else if(productKind.equals("1")){//净值
////				goDetail.setVisibility(View.VISIBLE);
////			}else{
////				goDetail.setVisibility(View.GONE);
////				convertView.setTag(position);
////			}
//			break;
//		case 2://组合购买
//			boci_product_name.setText(String.valueOf(productQueryList.get(position)
//					.get(BocInvt.BOCINVT_XPADQUERY_RETURNDATE_RES)));
//			boci_yearlyRR.setText(String.valueOf(productQueryList.get(position)
//					.get(BocInvt.BOCINVT_XPADQUERY_PRODNAME_RES)));
//			String timeLimit_2 = (String) productQueryList.get(position).get(
//					BocInvt.BOCINVT_XPADQUERY_AMOUNT_RES);
//			String currency_2 = (String) productQueryList.get(position).get(
//					BocInvt.BOCINVT_XPADQUERY_CURRENCY_RES);
//			boci_timeLimit.setText(StringUtil.parseStringCodePattern(currency_2,
//					timeLimit_2, 2));
////			goDetail.setVisibility(View.VISIBLE);
//			break;
//		default:
//			break;
//		}
////		//交易状态status 为0 时 列表项不可点击状态
////		String status = String.valueOf(productQueryList.get(position)
////				.get(BocInvt.BOCINCT_XPADTRAD_STATUS_RES));
////		if(status.equals("0")){//委托代处理状态不可进详情
////			goDetail.setVisibility(View.GONE);
////			convertView.setTag(position);
////		}
//		return convertView;
//	}
//}
