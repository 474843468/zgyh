package com.chinamworld.bocmbci.biz.prms.adapter;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.price.PrmsPricesActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;

/**
 * 贵金属行情列表适配器 新版本的
 * 
 * @author Administrator
 * 
 */
public class PrmsPricesListAdapter1 extends BaseAdapter {
	PrmsPricesActivity context;
	private LayoutInflater mInflater;
	private List<Map<String, String>> list;
	private OnItemClickListener buyOnClickListener;
	private OnItemClickListener saleOnClickListener;

	//wuhan
//	private OnItemClickListener saleonclick,buyonclick;
	
	public PrmsPricesListAdapter1(PrmsPricesActivity context,
			List<Map<String, String>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list ;
//		this.list = PrmsControl.getInstance().getDataList(list,context);
	}

	public void datachanged(List<Map<String, String>> list) {
		this.list = list ;
//		this.list = PrmsControl.getInstance().getDataList(list,context);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.prms_prices_listiterm1,
					null);
			holder = new ViewHolder();
			holder.buyFlag = (ImageView) convertView
					.findViewById(R.id.prms_price_listiterm1_buyflag);
			holder.saleFlag = (ImageView) convertView
					.findViewById(R.id.prms_price_listiterm1_saleflag);
			holder.buyPrice = (TextView) convertView
					.findViewById(R.id.prms_price_listiterm1_buyprice);
			holder.salePrice = (TextView) convertView
					.findViewById(R.id.prms_price_listiterm1_saleprice);
			holder.sourceCurrency = (TextView) convertView
					.findViewById(R.id.prms_price_listiterm1_sourceCurrency);
			holder.sourceCurrencyLayout = (LinearLayout) convertView
					.findViewById(R.id.prms_price_listiterm1_sourceCurrency_bg);
			holder.iv = (ImageView) convertView.findViewById(R.id.iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = list.get(position);
		initView(convertView, holder, map);
		
//		// wuhan
//				boolean isLogin = BaseDroidApp.getInstanse().isLogin();
//				if (isLogin) {
//					// holder.buyPrice.setEnabled(true);
//					// holder.buyFlag.setEnabled(true);
//					//
//					// holder.salePrice.setEnabled(true);
//					// holder.saleFlag.setEnabled(true);
//					holder.iv.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							Toast.makeText(context, "目前没有接口供 -- -- - - -- ", 0).show();
//
//						}
//					});
//					holder.buyPrice.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							saleOnClickListener
//									.onItemClick(null, v, position, position);
//						}
//					});
//					holder.buyFlag.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							saleOnClickListener
//									.onItemClick(null, v, position, position);
//						}
//					});
//					// 卖出
//					holder.salePrice.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							buyOnClickListener.onItemClick(null, v, position, position);
//						}
//					});
//					holder.saleFlag.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							buyOnClickListener.onItemClick(null, v, position, position);
//						}
//					});
//
//				} else {
//					// holder.buyPrice.setEnabled(false);
//					// holder.buyFlag.setEnabled(false);
//					// holder.salePrice.setEnabled(false);
//					// holder.saleFlag.setEnabled(false);
//					LogGloble.i("info", "islog===adapter ==" + isLogin);
////					holder.buyPrice.setOnClickListener(((PrmsPricesActivity)context).new MysaleOnClickListener(position));
////					holder.buyFlag.setOnClickListener(((PrmsPricesActivity)context).new MysaleOnClickListener(position)	);
////					// 卖出
////					holder.salePrice.setOnClickListener(((PrmsPricesActivity)context).new MysaleOnClickListener(position));
////							
////					holder.saleFlag.setOnClickListener(((PrmsPricesActivity)context).new MysaleOnClickListener(position));
//					 holder.buyPrice.setOnClickListener(new OnClickListener() {
//					
//					 @Override
//					 public void onClick(View v) {
//					 saleonclick.onItemClick(null, v, position, position);
//					 }
//					 });
//					 holder.buyFlag.setOnClickListener(new OnClickListener() {
//					
//					 @Override
//					 public void onClick(View v) {
//						 saleonclick.onItemClick(null, v, position, position);
//					 }
//					 });
//					 // 卖出
//					 holder.salePrice.setOnClickListener(new OnClickListener() {
//					
//					 @Override
//					 public void onClick(View v) {
//					 buyonclick.onItemClick(null, v, position, position);
//					 }
//					 });
//					 holder.saleFlag.setOnClickListener(new OnClickListener() {
//					
//					 @Override
//					 public void onClick(View v) {
//						 buyonclick.onItemClick(null, v, position, position);
//					 }
//					 });
//				}
		
		
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(context, "目前没有接口供 -- -- - - -- ", 0).show();

			}
		});
		holder.buyPrice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saleOnClickListener.onItemClick(null, v, position, position);
			}
		});
		holder.buyFlag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saleOnClickListener.onItemClick(null, v, position, position);
			}
		});
		// 卖出
		holder.salePrice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buyOnClickListener.onItemClick(null, v, position, position);
			}
		});
		holder.saleFlag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buyOnClickListener.onItemClick(null, v, position, position);
			}
		});
		return convertView;

	}

	/**
	 * 存放控件
	 * 
	 * @author Administrator
	 * 
	 */
	public class ViewHolder {
		public TextView sourceCurrency;
		private LinearLayout sourceCurrencyLayout;
		public TextView buyPrice;
		public TextView salePrice;
		public ImageView buyFlag;
		public ImageView saleFlag;
		/** 走势图 */
		private ImageView iv;
	}

	/**
	 * 布局
	 * 
	 * @param holder
	 * @param map
	 */
	private void initView(View convertView, ViewHolder holder,
			Map<String, String> map) {
		final String upDownFlag = map.get(Prms.QUERY_TRADERATE_UPDOWMFLAG);
		final String buyRate = String.valueOf(map
				.get(Prms.QUERY_TRADERATE_BUYRATE));
		final String sellRate = String.valueOf(map
				.get(Prms.QUERY_TRADERATE_SALERATE));
		final String sourceCurrencyCode = (String) map
				.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);

		if (upDownFlag.equals(ConstantGloble.PRMS_UPDOWNFLAG_UP)) {
			holder.buyPrice.setTextColor(context.getResources().getColor(R.color.red));
			holder.salePrice.setTextColor(context.getResources().getColor(R.color.red));
//			holder.buyFlag.setImageResource(R.drawable.shangsheng);// 上升
//			holder.saleFlag.setImageResource(R.drawable.shangsheng);
		}
		if (upDownFlag.equals(ConstantGloble.PRMS_UPDOWNFLAG_DOWN)) {
//			holder.buyFlag.setImageResource(R.drawable.xiajiang); // 下降
//			holder.saleFlag.setImageResource(R.drawable.xiajiang);
			holder.buyPrice.setTextColor(context.getResources().getColor(R.color.greens));
			holder.salePrice.setTextColor(context.getResources().getColor(R.color.greens));
		}
		if (upDownFlag.equals(ConstantGloble.PRMS_UPDOWNFLAG_NO)) {
//			holder.buyFlag.setImageResource(R.drawable.chiping); // 持平
//			holder.saleFlag.setImageResource(R.drawable.chiping);
			holder.buyPrice.setTextColor(context.getResources().getColor(R.color.gray_title));
			holder.salePrice.setTextColor(context.getResources().getColor(R.color.gray_title));
		}

		// 选择sourceCurrency 的背景图片
		if (sourceCurrencyCode.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
				|| sourceCurrencyCode
						.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)) {
			holder.sourceCurrencyLayout.setBackgroundResource(R.drawable.img_bg_card_di_yin);
		} else {
			holder.sourceCurrencyLayout.setBackgroundResource(R.drawable.img_bg_card_di_jin);
		}

		holder.buyPrice.setText(buyRate);
		holder.salePrice.setText(sellRate);
		String temp = "";
		final String sourceCurrency = (String) map
				.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
		final String targetCurrencyCode = (String) map
				.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
		if (sourceCurrency
				.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBG)
				&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币金
			temp = context.getResources().getString(R.string.prms_rembg);
//			map.put("temp", temp);
			
		}else if (sourceCurrencyCode
				.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORG)
				&& targetCurrencyCode
						.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元金
			temp = context.getResources().getString(R.string.prms_dolorg);

		}else if (sourceCurrencyCode
				.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)
				&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币银
			temp = context.getResources().getString(R.string.prms_rembs);
		}else if (sourceCurrencyCode
				.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
				&& targetCurrencyCode
						.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元银
			temp = context.getResources().getString(R.string.prms_dolors);
		}else if (sourceCurrencyCode
				.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)
				&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币铂金
			// temp = "人民币铂金/克";
			temp = context.getResources().getString(R.string.prms_rmbboG);
		} else if (sourceCurrencyCode
				.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)
				&& targetCurrencyCode
						.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元bo金
			// temp = "美元钯金/盎司";
			temp = context.getResources().getString(R.string.prms_dolorboG);
		}else if (sourceCurrencyCode
				.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG)
				&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币巴金
			// temp = "人民币钯金/克";
			temp = context.getResources().getString(R.string.prms_rmbbaG);
		}else if (sourceCurrencyCode
				.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)
				&& targetCurrencyCode
						.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元钯金
			// temp = "美元钯金/盎司";
			temp = context.getResources().getString(R.string.prms_dolorbaG);
		} 
			
		
		holder.sourceCurrency.setText(temp);
		// buyOnClickListener = new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (context.doCheck()) {
		// Intent i = new Intent();
		// // 需要传入的参数 卖出价格/卖出标记/卖出贵金属种类
		// i.putExtra(Prms.PRMS_TRANSFLAG,
		// ConstantGloble.PRMS_TRADE_TRANSFLAG_SALE);
		// i.putExtra(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE,
		// sourceCurrencyCode);
		// i.setClass(context, PrmsTradeBuyActivity.class);
		// context.startActivity(i);
		// }
		// }
		// };
		// saleOnClickListener = new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (context.doCheck()) {// 已登录
		// Intent i = new Intent();
		// // 需要传入的数据有 买入价格/买入还是卖出/贵金属种类
		// i.putExtra(Prms.PRMS_TRANSFLAG,
		// ConstantGloble.PRMS_TRADE_TRANSFLAG_BUY);
		// i.putExtra(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE,
		// sourceCurrencyCode);
		// i.setClass(context, PrmsTradeBuyActivity.class);
		// context.startActivity(i);
		// }
		// }
		// };

	}
	
	
//	private  List<Map<String,String>>  getCurrencyList(){
//		
//	}

//	private List<Map<String, String>> getDataList(
//			List<Map<String, String>> sourcList) {
//		List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
//		for (Map<String, String> map : sourcList) {
//			final String sourceCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
//			final String targetCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
//			if (sourceCurrencyCode
//					.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBG)
//					&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币金
//				temp = context.getResources().getString(R.string.prms_rembg);
//				map.put("temp", temp);
//				tempList.add(map);
//			}
//		}
//
//		for (Map<String, String> map : sourcList) {
//			final String sourceCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
//			final String targetCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
//			if (sourceCurrencyCode
//					.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORG)
//					&& targetCurrencyCode
//							.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元金
//				temp = context.getResources().getString(R.string.prms_dolorg);
//				map.put("temp", temp);
//				tempList.add(map);
//
//			}
//		}
//		for (Map<String, String> map : sourcList) {
//			final String sourceCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
//			final String targetCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
//			if (sourceCurrencyCode
//					.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)
//					&& targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币银
//				temp = context.getResources().getString(R.string.prms_rembs);
//				map.put("temp", temp);
//				tempList.add(map);
//			}
//		}
//		for (Map<String, String> map : sourcList) {
//			final String sourceCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
//			final String targetCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
//			if (sourceCurrencyCode
//					.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
//					&& targetCurrencyCode
//							.equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元银
//				temp = context.getResources().getString(R.string.prms_dolors);
//				map.put("temp", temp);
//				tempList.add(map);
//			}
//		}
//
//	
//		
//		for (Map<String, String> map : sourcList) {
//			final String sourceCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
//			final String targetCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
//			if (sourceCurrencyCode
//					.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)
//					&& targetCurrencyCode
//					.equals(ConstantGloble.PRMS_CODE_RMB)) {//人民币铂金
////				temp = "人民币铂金/克";
//				temp = context.getResources().getString(R.string.prms_rmbboG);
//				map.put("temp", temp);
//				tempList.add(map);
//			}
//		}
//		for (Map<String, String> map : sourcList) {
//			final String sourceCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
//			final String targetCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
//			if (sourceCurrencyCode
//					.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)
//					&& targetCurrencyCode
//					.equals(ConstantGloble.PRMS_CODE_DOLOR)) {//美元bo金
////				temp = "美元钯金/盎司";
//				temp = context.getResources().getString(R.string.prms_dolorboG);
//				map.put("temp", temp);
//				tempList.add(map);
//			}
//		}
//		for (Map<String, String> map : sourcList) {
//			final String sourceCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
//			final String targetCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
//			if (sourceCurrencyCode
//					.equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG)
//					&& targetCurrencyCode
//							.equals(ConstantGloble.PRMS_CODE_RMB)) {//人民币巴金
////				temp = "人民币钯金/克";
//				temp = context.getResources().getString(R.string.prms_rmbbaG);
//				map.put("temp", temp);
//				tempList.add(map);
//			}
//		}
//		for (Map<String, String> map : sourcList) {
//			final String sourceCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
//			final String targetCurrencyCode = (String) map
//					.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
//			if (sourceCurrencyCode
//					.equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)
//					&& targetCurrencyCode
//					.equals(ConstantGloble.PRMS_CODE_DOLOR)) {//美元钯金
////				temp = "美元钯金/盎司";
//				temp = context.getResources().getString(R.string.prms_dolorbaG);
//				map.put("temp", temp);
//				tempList.add(map);
//			}
//		}
//		return tempList;
//
//	}

	public OnItemClickListener getBuyOnClickListener() {
		return buyOnClickListener;
	}

	public void setBuyOnClickListener(OnItemClickListener buyOnClickListener) {
		this.buyOnClickListener = buyOnClickListener;
	}

	public OnItemClickListener getSaleOnClickListener() {
		return saleOnClickListener;
	}

	public void setSaleOnClickListener(OnItemClickListener saleOnClickListener) {
		this.saleOnClickListener = saleOnClickListener;
	}

	public List<Map<String, String>> getList() {
		return list;
	}

	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}

	
	
}
