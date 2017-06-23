package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
//import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
//import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BocProductForFixationHoldingInfo;
//import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.ReferenceProfitActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoUtil;
import com.chinamworld.bocmbci.utils.DateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
//import com.nineoldandroids.animation.Animator;
//import com.nineoldandroids.animation.Animator.AnimatorListener;
//import com.nineoldandroids.animation.ValueAnimator;
//import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
//import android.graphics.Paint;

/**
 * 理财产品列表的适配器
 * 
 * @author HVZHUNG
 *
 */
public class MyProductAdapter extends BaseAdapter {

	public static final String TAG = "MyProductAdapter";
	private ArrayList<BOCProductForHoldingInfo> infos = new ArrayList<BOCProductForHoldingInfo>();

	public void changeData(ArrayList<BOCProductForHoldingInfo> data) {
		infos.clear();
		if (data != null)
			infos.addAll(data);
		notifyDataSetChanged();
	}

	public void addDataAll(ArrayList<BOCProductForHoldingInfo> data) {
		if (data != null)
			infos.addAll(data);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (infos == null) {
			return 0;
		}
		return infos.size();
	}

	@Override
	public BOCProductForHoldingInfo getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		BOCProductForHoldingInfo item = getItem(position);
		if (convertView == null) {
			holder = ViewHolder.newInstance(BaseDroidApp.context, parent,
					item.issueType);
			convertView = holder.getView();
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.setData(item);
		return holder.getView();
	}

	@Override
	public int getItemViewType(int position) {

		String ItemProductKind = getItem(position).issueType;
		int type = 0;
		if (BOCProductForHoldingInfo.PROD_TYPE_CASH.equals(ItemProductKind)) {
			type = 0;
		} else if (BOCProductForHoldingInfo.PROD_TYPE_VALUE
				.equals(ItemProductKind)) {
			type = 1;
		} else if (BOCProductForHoldingInfo.PROD_TYPE_FIXATION
				.equals(ItemProductKind)) {
			type = 2;
		}
		return type;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	/**
	 * 现金管理类产品
	 * 
	 * @author HVZHUNG
	 *
	 */
	public static class ViewHolderCash extends ViewHolder {

		private View view;
		private View arrow;// 最右侧的箭头
		private TextView tv_earnings_type;// 收益类型
		private TextView tv_hold_share;// 持有份额
		private TextView tv_due_time;// 过期产品
		private TextView tv_earnings_percent;// 收益率/净值

		private BOCProductForHoldingInfo info;

		protected ViewHolderCash(final Context context, ViewGroup parent) {
			super(context, parent, BOCProductForHoldingInfo.PROD_TYPE_CASH);

			view = View.inflate(context,
					R.layout.bocinvt_product_item_cash_p603, null);
			arrow = view.findViewById(R.id.arrow);
			tv_earnings_type = (TextView) view
					.findViewById(R.id.tv_earnings_type);
			tv_hold_share = (TextView) view.findViewById(R.id.tv_hold_share);
			tv_due_time = (TextView) view.findViewById(R.id.tv_due_time);
			tv_earnings_percent = (TextView) view
					.findViewById(R.id.tv_earnings_percent);
//			tv_earnings_percent.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//			tv_earnings_percent.getPaint().setAntiAlias(true);// 抗锯齿

			// setToReferenceProfitView(info, tv_earnings_percent);

		}

		@Override
		public void setData(BOCProductForHoldingInfo info) {
			this.info = info;
			// tv_earnings_type.setText(HoldingBOCProductInfoUtil
			// .getFriendlyCashProType(info));
			tv_earnings_type.setText(info.prodName);
			tv_hold_share.setText(HoldingBOCProductInfoUtil
					.getFriendlyHoldingQuantity(info));
			tv_due_time.setText(DateUtils.DateFormatter(HoldingBOCProductInfoUtil
					.getFriendlyProdEnd(info)));
			tv_earnings_percent.setText(HoldingBOCProductInfoUtil
					.getFriendlyYearlyRRRange(info));
			setToReferenceProfitView(info, tv_earnings_percent);
		}

		@Override
		public View getView() {
			return view;
		}

	}

	/**
	 * 净值开放产品
	 * 
	 * @author HVZHUNG
	 *
	 */
	public static class ViewHolderValue extends ViewHolder {
		private View view;
		private View arrow;// 最右侧的箭头
		private TextView tv_earnings_type;// 收益类型
		private TextView tv_hold_share;// 持有份额
		private TextView tv_due_time;// 过期产品
		private TextView tv_earnings_percent;// 收益率/净值
		private BOCProductForHoldingInfo info;

		protected ViewHolderValue(Context context, ViewGroup parent) {
			super(context, parent, BOCProductForHoldingInfo.PROD_TYPE_VALUE);
			view = View.inflate(context,
					R.layout.bocinvt_product_item_cash_p603, null);
			arrow = view.findViewById(R.id.arrow);
			tv_earnings_type = (TextView) view
					.findViewById(R.id.tv_earnings_type);
			tv_hold_share = (TextView) view.findViewById(R.id.tv_hold_share);
			tv_due_time = (TextView) view.findViewById(R.id.tv_due_time);
			tv_earnings_percent = (TextView) view
					.findViewById(R.id.tv_earnings_percent);
//			tv_earnings_percent.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//			tv_earnings_percent.getPaint().setAntiAlias(true);// 抗锯齿
//			tv_earnings_type.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
			tv_earnings_percent.setTextColor(context.getResources().getColor(R.color.red));
			// setToReferenceProfitView(info, tv_earnings_percent);
		}

		@Override
		public void setData(BOCProductForHoldingInfo info) {
			tv_earnings_type.setText(info.prodName);
			tv_hold_share.setText(HoldingBOCProductInfoUtil
					.getFriendlyHoldingQuantity(info));
			tv_due_time.setText(DateUtils.DateFormatter(HoldingBOCProductInfoUtil
					.getFriendlyProdEnd(info)));
			tv_earnings_percent.setText(StringUtil.parseStringPattern(info.price,4));
//			tv_earnings_percent.setText(HoldingBOCProductInfoUtil
//					.getFriendlyYearlyRRRange(info));
			setToReferenceProfitView(info, tv_earnings_percent);
		}

		@Override
		public View getView() {
			return view;
		}

	}

	/**
	 * 固定期限产品
	 * 
	 * @author HVZHUNG
	 *
	 */
	public static class ViewHolderFixation extends ViewHolder {
		private View view;
		private TextView tv_hold_share;
		private TextView tv_due_time;
		private TextView tv_earnings_percent;
		private View arrow;
		private View fixation_layout;
		private View earnings_type_container;
		private BOCProductForHoldingInfo info;
		private TextView tv_earnings_type;// 名称
		/** 收起*/
		private View arrow2;
		/** 灰线*/
		private View grayLine;
//		/** 显示详情时候的动画 */
//		private ValueAnimator visibleAnimator;
//		/** 隐藏详情时候的动画 */
//		private ValueAnimator goneAnimator;
		private ListView listview;
		protected ViewHolderFixation(Context context, ViewGroup parent) {
			super(context, parent, BOCProductForHoldingInfo.PROD_TYPE_FIXATION);
			view = View.inflate(context,
					R.layout.bocinvt_product_item_fixation_p603, null);
			tv_earnings_type = (TextView) view
					.findViewById(R.id.tv_earnings_type);
			tv_hold_share = (TextView) view.findViewById(R.id.tv_hold_share);
			tv_due_time = (TextView) view.findViewById(R.id.tv_due_time);
			tv_earnings_percent = (TextView) view
					.findViewById(R.id.tv_earnings_percent);
			listview = (ListView) view.findViewById(R.id.boc_query_detail_result);
//			tv_earnings_percent.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//			tv_earnings_percent.getPaint().setAntiAlias(true);// 抗锯齿
			arrow = view.findViewById(R.id.arrow);
			arrow2 = view.findViewById(R.id.arrow2);
			grayLine = view.findViewById(R.id.img_line);
			fixation_layout = view.findViewById(R.id.fixation_layout);
			earnings_type_container = view.findViewById(R.id.earnings_type_container);
			
//
//			details_view.measure(MeasureSpec.makeMeasureSpec(0,
//					View.MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(
//					0, View.MeasureSpec.UNSPECIFIED));
//			initAnimators(details_view.getMeasuredHeight());
//			details_view.setVisibility(View.GONE);
//			arrow.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					if (info == null) {
//						return;
//					}
//
//					boolean isShowDetails = false;
//
//					if (details_view.getVisibility() == View.GONE
//							&& !visibleAnimator.isRunning()) {
//						isShowDetails = true;
//						visibleAnimator.start();
//					} else if (details_view.getVisibility() == View.VISIBLE
//							&& !goneAnimator.isRunning()) {
//						isShowDetails = false;
//						goneAnimator.start();
//					}
//					if (info instanceof BocProductForFixationHoldingInfo) {
//						((BocProductForFixationHoldingInfo) info).isShowDetails = isShowDetails;
//						v.setSelected(isShowDetails);
//					}
//
//				}
//			});
		}

//		/**
//		 * 初始化动画
//		 */
//		private void initAnimators(final int height) {
//			visibleAnimator = ValueAnimator.ofFloat(0f, 1f);
//			visibleAnimator.addListener(new AnimatorListener() {
//
//				@Override
//				public void onAnimationStart(Animator arg0) {
//					details_view.setVisibility(View.VISIBLE);
//
//				}
//
//				@Override
//				public void onAnimationRepeat(Animator arg0) {
//
//				}
//
//				@Override
//				public void onAnimationEnd(Animator arg0) {
//
//				}
//
//				@Override
//				public void onAnimationCancel(Animator arg0) {
//
//				}
//			});
//			visibleAnimator.addUpdateListener(new AnimatorUpdateListener() {
//
//				@Override
//				public void onAnimationUpdate(ValueAnimator arg0) {
//					float mHeight = arg0.getAnimatedFraction() * height;
//					LayoutParams layoutParams = details_view.getLayoutParams();
//					layoutParams.height = (int) mHeight;
//					details_view.setLayoutParams(layoutParams);
//
//				}
//			});
//			visibleAnimator.setDuration(300);
//			goneAnimator = ValueAnimator.ofFloat(1f, 0f);
//			goneAnimator.setDuration(300);
//			goneAnimator.addListener(new AnimatorListener() {
//
//				@Override
//				public void onAnimationStart(Animator arg0) {
//
//				}
//
//				@Override
//				public void onAnimationRepeat(Animator arg0) {
//
//				}
//
//				@Override
//				public void onAnimationEnd(Animator arg0) {
//					details_view.setVisibility(View.GONE);
//				}
//
//				@Override
//				public void onAnimationCancel(Animator arg0) {
//
//				}
//			});
//			goneAnimator.addUpdateListener(new AnimatorUpdateListener() {
//
//				@Override
//				public void onAnimationUpdate(ValueAnimator arg0) {
//					float mHeight = arg0.getAnimatedFraction() * height;
//					LayoutParams layoutParams = details_view.getLayoutParams();
//					layoutParams.height = (int) (height - mHeight);
//					details_view.setLayoutParams(layoutParams);
//				}
//			});
//
//		}

		@Override
		public void setData(BOCProductForHoldingInfo info) {
			this.info = info;
			// this.tv_hold_share.setText(HoldingBOCProductInfoUtil
			// .getStandardProName(info));
			this.tv_earnings_type.setText(info.prodName);
			this.tv_hold_share.setText(HoldingBOCProductInfoUtil
					.getFriendlyHoldingQuantity(info));
			this.tv_due_time.setText(DateUtils.DateFormatter(HoldingBOCProductInfoUtil
					.getFriendlyProdEnd(info)));
//			this.tv_earnings_percent.setText(StringUtil.splitStringwith2point(info.price,4));
			this.tv_earnings_percent.setText(HoldingBOCProductInfoUtil
					.getFriendlyYearlyRRRange(info));
			if(HoldingBOCProductInfoUtil.isStandardPro(info)){
				this.tv_earnings_percent.setText("-");
				arrow2.setOnClickListener(itemClickListener);
			}			
//			if (info instanceof BocProductForFixationHoldingInfo
//					&& ((BocProductForFixationHoldingInfo) info).isShowDetails) {
//				details_view.setVisibility(View.VISIBLE);
//				arrow.setSelected(true);
//
//			} else {
//				details_view.setVisibility(View.GONE);
//				arrow.setSelected(false);
//			}
			// setToReferenceProfitView(info, tv_earnings_percent);
		}

		@Override
		public View getView() {
			return view;
		}

		OnClickListener itemClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(listview.getVisibility() == View.VISIBLE) {  // 数据已展开，此时需要收起数据列表
					listview.setVisibility(View.GONE);
					arrow.setVisibility(View.VISIBLE);
					arrow2.setVisibility(View.GONE);
					grayLine.setVisibility(View.GONE);
				}
				else {// 展示数据列表
					listview.setVisibility(View.VISIBLE);	
					arrow.setVisibility(View.GONE);
					arrow2.setVisibility(View.VISIBLE);
					grayLine.setVisibility(View.VISIBLE);
				}
			}
		};
	}

	public static abstract class ViewHolder {

		private final String type;
		protected Context context;

		public static ViewHolder newInstance(Context context, ViewGroup parent,
				String type) {
			if (BOCProductForHoldingInfo.PROD_TYPE_CASH.equals(type)) {
				return new ViewHolderCash(context, parent);
			} else if (BOCProductForHoldingInfo.PROD_TYPE_VALUE.equals(type)) {
				return new ViewHolderValue(context, parent);
			} else if (BOCProductForHoldingInfo.PROD_TYPE_FIXATION.equals(type)) {
				return new ViewHolderFixation(context, parent);
			} else {
				// 其它类别，这里不应该执行
				return new ViewHolderCash(context, parent);
			}

		}

		protected ViewHolder(Context context, ViewGroup parent, String type) {
			this.type = type;
			this.context = context;
		}

		/**
		 * 设置显示数据
		 * 
		 * @param info
		 */
		public abstract void setData(BOCProductForHoldingInfo info);

		public abstract View getView();

		/**
		 * 设置跳转至参考收益页面的响应View
		 * 
		 * @return
		 */
		public void setToReferenceProfitView(
				final BOCProductForHoldingInfo info, View view) {
			if (view == null)
				return;
			// 关闭跳转，跳转转移到详情页面
			// view.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // 跳转到参考收益页面
			// if (info != null)
			// context.startActivity(ReferenceProfitActivity
			// .getIntent(context, info));
			//
			// }
			// });
		}

		public String getType() {
			return this.type;
		}
	}
	
}
