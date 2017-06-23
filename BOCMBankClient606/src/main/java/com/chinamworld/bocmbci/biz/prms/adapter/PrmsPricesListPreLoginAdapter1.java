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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.price.PrmsPricesActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 贵金属行情列表适配器 新版本的
 * 
 * @author Administrator
 * 
 */
public class PrmsPricesListPreLoginAdapter1 extends BaseAdapter {
	PrmsPricesActivity context;
	private LayoutInflater mInflater;
	private  List<Map<String, Object>> preList;
	
	private OnItemClickListener buyOnClickListener;
	private OnItemClickListener saleOnClickListener;
	

	//wuhan
	private OnItemClickListener saleonclick,buyonclick;
	 List<Map<String, Object>> preLoginList;
	
	public PrmsPricesListPreLoginAdapter1(PrmsPricesActivity context,
			List<Map<String, Object>> list) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
//		this.preList = PrmsControl.getInstance().getPreLoginDataList(list,context);
		this.preList = list;
	}

	public void datachanged(List<Map<String, Object>> list) {
//		this.preList = PrmsControl.getInstance().getPreLoginDataList(list, context);
		this.preList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return preList.size();
	}

	@Override
	public Object getItem(int position) {
		return preList.get(position);
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
		
		Map<String, Object> map =  preList.get(position);
		initView(convertView, holder, map);
		// wuhan
				boolean isLogin = BaseDroidApp.getInstanse().isLogin();
				if (isLogin) {
					// holder.buyPrice.setEnabled(true);
					// holder.buyFlag.setEnabled(true);
					//
					// holder.salePrice.setEnabled(true);
					// holder.saleFlag.setEnabled(true);
					holder.iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
//							Toast.makeText(context, "目前没有接口供 -- -- - - -- ", 0).show();

						}
					});
					holder.buyPrice.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							saleOnClickListener
									.onItemClick(null, v, position, position);
						}
					});
					holder.buyFlag.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							saleOnClickListener
									.onItemClick(null, v, position, position);
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

				} else {
					// holder.buyPrice.setEnabled(false);
					// holder.buyFlag.setEnabled(false);
					// holder.salePrice.setEnabled(false);
					// holder.saleFlag.setEnabled(false);
					LogGloble.i("info", "islog===adapter ==" + isLogin);
//					holder.buyPrice.setOnClickListener(((PrmsPricesActivity)context).new MysaleOnClickListener(position));
//					holder.buyFlag.setOnClickListener(((PrmsPricesActivity)context).new MysaleOnClickListener(position)	);
//					// 卖出
//					holder.salePrice.setOnClickListener(((PrmsPricesActivity)context).new MysaleOnClickListener(position));
//							
//					holder.saleFlag.setOnClickListener(((PrmsPricesActivity)context).new MysaleOnClickListener(position));
					 holder.buyPrice.setOnClickListener(new OnClickListener() {
					
					 @Override
					 public void onClick(View v) {
					 saleonclick.onItemClick(null, v, position, position);
					 }
					 });
					 holder.buyFlag.setOnClickListener(new OnClickListener() {
					
					 @Override
					 public void onClick(View v) {
						 saleonclick.onItemClick(null, v, position, position);
					 }
					 });
					 // 卖出
					 holder.salePrice.setOnClickListener(new OnClickListener() {
					
					 @Override
					 public void onClick(View v) {
					 buyonclick.onItemClick(null, v, position, position);
					 }
					 });
					 holder.saleFlag.setOnClickListener(new OnClickListener() {
					
					 @Override
					 public void onClick(View v) {
						 buyonclick.onItemClick(null, v, position, position);
					 }
					 });
				}
		
		
//		holder.iv.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(context, "目前没有接口供 -- -- - - -- ", 0).show();
//
//			}
//		});
//		holder.buyPrice.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				saleOnClickListener.onItemClick(null, v, position, position);
//			}
//		});
//		holder.buyFlag.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				saleOnClickListener.onItemClick(null, v, position, position);
//			}
//		});
//		// 卖出
//		holder.salePrice.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				buyOnClickListener.onItemClick(null, v, position, position);
//			}
//		});
//		holder.saleFlag.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				buyOnClickListener.onItemClick(null, v, position, position);
//			}
//		});
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
			Map<String, Object> map) {
		final String upDownFlag = (String) map.get(Prms.PRELOGIN_QUERY_FLAG);
		final String buyRate = String.valueOf(map
				.get(Prms.PRELOGIN_QUERY_BUYRATE));
		final String sellRate = String.valueOf(map
				.get(Prms.PRELOGIN_QUERY_SELLRATE));
		final String sourceCurrencyCode = (String) map
				.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
		final String targetCurrencyCode =(String) map
		.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
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
		String temp ="";
//		temp = (String) map.get("temp");
		final String sourceCurrency = (String) map
				.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
	
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

	}
	
	

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

	public  List<Map<String, Object>> getList() {
		return preList;
	}

	public void setList( List<Map<String, Object>> list) {
		this.preList = list;
	}

	
	
	//wuhan
	public OnItemClickListener getsealOnClickListener() {
		return saleonclick;
	}

	public void setsaleOnClickListener(OnItemClickListener saleOnClickListener) {
		this.saleonclick = saleOnClickListener;
	}
	
	public OnItemClickListener getbuyonclick() {
		return buyonclick;
	}

	public void setbuyonclick(OnItemClickListener buyonclick) {
		this.buyonclick = buyonclick;
	}
}
