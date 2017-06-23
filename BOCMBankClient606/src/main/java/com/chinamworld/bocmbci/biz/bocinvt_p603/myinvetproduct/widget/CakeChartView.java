package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.chinamworld.bocmbci.R;

/**
 * 饼状图
 * 
 * @author HVZHUNG
 *
 */
public class CakeChartView extends View {

	private Paint paint;

	public int radius = -1;
	private RectF rectF;
//	private int[] datas;
	private double[] datas;
	private int[] colors;
	/** 开始绘图的角度 */
	private int startAngle = -90;
	/** 每个数据所占的角度 */
	private double[] sweepAngles;

	/** 图例显示的文字 */
	private CharSequence[] legengs;
	/** 图例文字的大小 */
	private int legengTextSize = 12;

	/** 图例与文字的间隔 */
	private int legengTextInterval = 20;
	/** 图例间的间隔 */
	private int legengTextsInterval = 20;

	public CakeChartView(Context context) {
		super(context);
	}

	public CakeChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeJoin(Join.ROUND);
		paint.setTextSize(legengTextSize);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.CakeChart);
		this.radius = (int) (ta.getDimensionPixelSize(
				R.styleable.CakeChart_CakeChart_radius, -1) + 0.5f);
		ta.recycle();

	}

	/**
	 * 设置饼状图的数据，注意：数据与颜色需要相同长度的数组
	 * 
	 * @param datas
	 *            数据
	 * @param colors
	 *            对应的颜色
	 * @param legengs
	 *            图例显示的文字
	 */
	public void setDataAndColor(double[] datas, int[] colors/*
														 * , CharSequence[]
														 * legengs
														 */) {
		if (datas == null || colors == null) {
			throw new NullPointerException("参数不能为空");
		}
		if (datas.length != colors.length) {
			throw new IllegalAccessError("数据个数与颜色个数必须相同");
		}
		this.datas = datas;
		this.colors = colors;
		this.legengs = legengs;
		// 计算每个数据所占的比例
		double total = 0;
		for (double data : datas) {
			total += data;
		}
		sweepAngles = new double[datas.length];
		for (int i = 0; i < datas.length; i++) {
			sweepAngles[i] = (double) (datas[i] / (double) total * 360);
		}
		invalidate();
	}

	/**
	 * 设置开始绘图的角度，默认开始角度为-90，即12点位置
	 * 
	 * @param startAngle
	 */
	public void setStartAngle(int startAngle) {
		this.startAngle = startAngle;
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// int measuredLegengsWidth = measuredLegengsWidth(widthMeasureSpec);
		// int measuredLegengsHeight = measuredLegengsHeight(heightMeasureSpec);
		int desiredWidth = radius * 2 + getPaddingLeft() + getPaddingRight();
		int desiredHeight = radius * 2 + getPaddingTop() + getPaddingBottom();

		int measuredWidth = resolveMeasured(widthMeasureSpec,
				radius == -1 ? getSuggestedMinimumWidth() : desiredWidth);
		int measuredHeight = resolveMeasured(heightMeasureSpec,
				radius == -1 ? getSuggestedMinimumHeight() : desiredHeight);

		if (radius <= 0)
			radius = (measuredWidth - getPaddingLeft() - getPaddingRight()) / 2;
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	/**
	 * 计算图例需要的高度
	 * 
	 * @return
	 */
	private int measuredLegengsHeight(int heightMeasureSpec) {
		int height = 0;
		if (legengs == null || legengs.length == 0) {
			return height;
		}
		return height;
	}

	/**
	 * 计算图例需要的宽度
	 * 
	 * @return
	 */
	private int measuredLegengsWidth(int widthMeasureSpec) {
		int width = 0;
		if (legengs == null || legengs.length == 0) {
			return width;
		}
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int size = MeasureSpec.getSize(widthMeasureSpec);

		return width;
	}

	private int resolveMeasured(int measureSpec, int desired) {
		int result = 0;
		int specSize = MeasureSpec.getSize(measureSpec);
		switch (MeasureSpec.getMode(measureSpec)) {
		case MeasureSpec.UNSPECIFIED:
			result = desired;
			break;
		case MeasureSpec.AT_MOST:
			result = Math.min(specSize, desired);
			break;
		case MeasureSpec.EXACTLY:
		default:
			result = specSize;
		}
		return result;
	}

	/***
	 * 设置饼状图的半径
	 * 
	 * @param radius
	 *            单位为px
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		requestLayout();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		int left = getPaddingLeft();
		int top = getPaddingTop();
		int right = w - getPaddingRight();
		int bottom = h - getPaddingBottom();
		rectF = new RectF(left, top, right, bottom);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(0,
			Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.setDrawFilter(filter);
		super.onDraw(canvas);
		if (datas == null || colors == null || datas.length != colors.length) {
			return;
		}

		int startAngle = this.startAngle;
		for (int i = 0; i < datas.length; i++) {
			paint.setColor(colors[i]);
			canvas.drawArc(rectF, startAngle, (float) sweepAngles[i], true, paint);
			startAngle += sweepAngles[i];
		}
	}

}
