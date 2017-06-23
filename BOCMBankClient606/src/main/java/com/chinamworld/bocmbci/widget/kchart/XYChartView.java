package com.chinamworld.bocmbci.widget.kchart;

import android.content.Context;
import android.view.View;

public class XYChartView extends View {

//	private int mWidth; // X轴长度
//	private int mHeight; // Y轴长度
//
//	private int countX = 12; // X轴坐标数
//	private int countY = 5; // Y轴坐标数
//
//	private String unitX = "日"; // X轴单位
//	private String unitY = "kWh"; // Y轴单位
//
//	private String[] coordinateX; // X轴坐标
//
//	private float maxDataY = 0; // Y轴坐标最大值
//	private float minDataY = 0; // Y轴坐标最小值
//
//	private int spaceLeft = 100; // 与左边缘的距离
//	private int spaceBottom = 80; // 与下边缘的距离
//	private int spaceTop = 30; // 与上边缘的距离
//	private int spaceRight = 20; // 与右边缘的距离
//
//	private int paintColor = 0; // 画笔颜色
//	private int countLine = 0; // 计算线数量
//
//	private int tagX = 1;
//
//	private ArrayList<XYChartData> lineList; // 线列表
//
//	/*
//	 * 自定义控件一般写两个构造方法 CoordinatesView(Context context)用于java硬编码创建控件
//	 * 如果想要让自己的控件能够通过xml来产生就必须有第2个构造方法 CoordinatesView(Context context,
//	 * AttributeSet attrs) 因为框架会自动调用具有AttributeSet参数的这个构造方法来创建继承自View的控件
//	 */
	public XYChartView(Context context) {
		super(context, null);
//		lineList = new ArrayList<XYChartData>();
	}
//
//	public XYChartView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		lineList = new ArrayList<XYChartData>();
//	}
//
//	// 获取X轴的坐标数
//	public int getCountX() {
//		return countX;
//	}
//
//	// 设置X轴的坐标数
//	public void setCountX(int countX) {
//		this.countX = countX;
//	}
//
//	// 获取Y轴的坐标数
//	public int getCountY() {
//		return countY;
//	}
//
//	// 设置Y轴的坐标数
//	public void setCountY(int countY) {
//		this.countY = countY;
//	}
//
//	// 返回X坐标轴单位
//	public String getUnitX() {
//		return unitX;
//	}
//
//	// 设置X坐标轴单位
//	public void setUnitX(String unitX) {
//		this.unitX = unitX;
//	}
//
//	// 返回X坐标轴单位
//	public String getUnitY() {
//		return unitY;
//	}
//
//	// 设置X坐标轴单位
//	public void setUnitY(String unitY) {
//		this.unitY = unitY;
//	}
//
//	// 设置Y值最大值
//	public void findMaxDataY() {
//
//		for (int j = 0; j < lineList.size(); j++) {
//			maxDataY = lineList.get(j).getCoordinateY()[0];
//			minDataY = lineList.get(j).getCoordinateY()[0];
//			for (int i = 0; i < lineList.get(j).getCoordinateY().length; i++) {
//
//				if (maxDataY < lineList.get(j).getCoordinateY()[i])
//					maxDataY = lineList.get(j).getCoordinateY()[i];
//				if (minDataY > lineList.get(j).getCoordinateY()[i])
//					minDataY = lineList.get(j).getCoordinateY()[i];
//
//			}
//		}
//		// while (maxDataY % (countY * 10) != 0) {
//		// maxDataY++;
//		// }
//
//	}
//
//	public int getPaintColor() {
//		return paintColor;
//	}
//
//	public void setPaintColor(int paintColor) {
//		this.paintColor = paintColor;
//	}
//
//	/*
//	 * 控件创建完成之后，在显示之前都会调用这个方法，此时可以获取控件的大小 并得到原点坐标的点。
//	 */
//	@Override
//	public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
//		mWidth = width - spaceLeft - spaceRight; // 宽度
//		mHeight = height - spaceTop - spaceBottom; // 高度
//
//		super.onSizeChanged(width, height, oldWidth, oldHeight);
//	}
//
//	/*
//	 * 自定义控件一般都会重载onDraw(Canvas canvas)方法，来绘制自己想要的图形
//	 */
//	@Override
//	public void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		Paint paint = new Paint();
//		paint.setStrokeWidth(3);
//		paint.setColor(Color.BLACK);
//
//		// dataY = new int[]{20, 84, 56, 70, 130, 125, 10, 40, 60, 45, 77, 12,
//		// 145};
//
//		// coordinateX = new String[]{"1", "hello", "3", "4", "5", "6",
//		// "10/2012", "10/2012", "10/2012", "10/2012", "10/2012", "10/2012" };
//
//		// coordinateY = new int[]{0, 20, 40, 60, 80, 100, 120, 140};
//
//		// 画坐标轴
//		if (canvas != null) {
//			canvas.drawColor(Color.WHITE);
//			// 画直线
//			canvas.drawLine(spaceLeft, mHeight + spaceTop, mWidth + spaceLeft -mWidth / countX -5 ,
//					mHeight + spaceTop, paint);
//			canvas.drawLine(spaceLeft, mHeight + spaceTop, spaceLeft, spaceTop,
//					paint);
//
//			// 画X轴箭头
//			int x = mWidth + spaceLeft  -mWidth / countX -5, y = mHeight + spaceTop;
//			drawTriangle(canvas, new Point(x, y), new Point(x - 10, y - 5),
//					new Point(x - 10, y + 5));
//			canvas.drawText(unitX, x - 15, y + 18, paint);
//
//			// 画Y轴箭头
//			x = spaceLeft;
//			y = spaceTop;
//			drawTriangle(canvas, new Point(x, y), new Point(x - 5, y + 10),
//					new Point(x + 5, y + 10));
//
//			canvas.drawText(unitY, x + 12, y + 15, paint);
//
//			// 画坐标轴刻度线
//			int pieceX = mWidth / countX, pieceY = mHeight / countY;
//			drawBase(canvas, pieceX, pieceY);
//			for (int i = 0; i < countLine; i++) {
//				drawLine(canvas, pieceX, lineList.get(i));
//			}
//		}
//
//		// if (canvas != null) {
//		// /*
//		// * TODO 画数据 所有外部需要在坐标轴上画的数据，都在这里进行绘制
//		// */
//		// canvas.drawCircle(po.x + 2 * pa.x, po.y - 2 * pa.y, 2, paint);
//		// canvas.drawCircle(po.x + 2 * pb.x, po.y - 2 * pb.y, 2, paint);
//		// canvas.drawLine(po.x + 2 * pa.x, po.y - 2 * pa.y, po.x + 2 * pb.x,
//		// po.y - 2 * pb.y, paint);
//		// // canvas.drawPoint(pa.x+po.x, po.y-pa.y, paint);
//		// }
//
//	}
//
//	/**
//	 * 
//	 * @Title: drawLine
//	 * @Description: TODO(画线)
//	 * @param @param canvas
//	 * @param @param pieceX
//	 * @param @param line 设定文件
//	 * @return void 返回类型
//	 * @throws
//	 */
//	private void drawLine(Canvas canvas, int pieceX, XYChartData line) {
//		Paint paint = new Paint();
//		paint.setStrokeWidth(2);
//		paint.setFakeBoldText(true);
//		paint.setColor(line.getPaintColor());
//
//		Paint paint1 = new Paint();
//		paint1.setStrokeWidth(2);
//		paint1.setFakeBoldText(true);
//		paint1.setTextSize(14);
//		paint1.setColor(Color.RED);
//		// 下面的主题标识
////		canvas.drawLine(tagX, mHeight + spaceTop + 35, tagX + 10, mHeight
////				+ spaceTop + 35, paint);
////		paint1.setFakeBoldText(true);
////		canvas.drawText(line.getLineName(), tagX + 15, mHeight + spaceTop + 40,
////				paint1);
//		tagX += 70;
//
//		float[] y = line.getCoordinateY();
//
//		for (int i = 0; i < countX; i++) {
//			// Y轴坐标与最大值得比较
//			canvas.drawCircle(spaceLeft + pieceX * (i), mHeight + spaceTop
//					- mHeight * (y[i] - minDataY) / (maxDataY - minDataY), 4,
//					paint);
//			canvas.drawText(y[i] + "", spaceLeft + pieceX * (i), mHeight
//					+ spaceTop - mHeight * (y[i] - minDataY)
//					/ (maxDataY - minDataY) - 10, paint1);
//
//			if (i < countX - 1) {
//				canvas.drawLine(spaceLeft + pieceX * (i), mHeight + spaceTop
//						- mHeight * (y[i] - minDataY) / (maxDataY - minDataY),
//						spaceLeft + pieceX * (i + 1), mHeight + spaceTop
//								- mHeight * (y[i + 1] - minDataY)
//								/ (maxDataY - minDataY), paint);
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * @Title: drawBase
//	 * @Description: TODO(画基本坐标轴)
//	 * @param @param canvas
//	 * @param @param pieceX
//	 * @param @param pieceY
//	 * @return void 返回类型
//	 * @throws
//	 */
//	private void drawBase(Canvas canvas, int pieceX, int pieceY) {
//		Paint paint = new Paint();
//		paint.setFakeBoldText(true);
//		paint.setTextSize(14);
//		paint.setStrokeWidth(3);
//		paint.setColor(Color.BLACK);
//		// 画X轴刻度
//		for (int i = 0; i < countX; i++) {
//			paint.setStrokeWidth(3);
//			canvas.drawLine(spaceLeft + pieceX * i, mHeight + spaceTop - 7,
//					spaceLeft + pieceX * i, mHeight + spaceTop + 7, paint);
//			paint.setStrokeWidth(1);
//			canvas.drawLine(spaceLeft + pieceX * i, mHeight + spaceTop,
//					spaceLeft + pieceX * i, spaceTop, paint);
//
//			// for (int j = 0; j < 10; j++) {//小刻度 10个格
//			// canvas.drawLine(spaceLeft + pieceX * i + pieceX * j / 10,
//			// mHeight + spaceTop - 3, spaceLeft + pieceX * i + pieceX
//			// * j / 10, mHeight + spaceTop, paint);
//			// }
//			paint.setStrokeWidth(3);
//			paint.setTextAlign(Paint.Align.LEFT);
//
//			canvas.save();
//			canvas.rotate(45, spaceLeft + pieceX * (i), mHeight + spaceTop + 18);
//			canvas.drawText(coordinateX[i], spaceLeft + pieceX * (i), mHeight
//					+ spaceTop + 18, paint);
//			canvas.restore();
//		}
//
//		paint.setStrokeWidth(3);
//		// 画Y轴刻度rr
//		for (int j = 0; j < countY; j++) {
//			paint.setStrokeWidth(3);
//			canvas.drawLine(spaceLeft - 7, mHeight + spaceTop - pieceY * j,
//					spaceLeft + 7, mHeight + spaceTop - pieceY * j, paint);
//
//			paint.setStrokeWidth(1);
//			canvas.drawLine(spaceLeft, mHeight + spaceTop - pieceY * j, mWidth
//					+ spaceLeft - pieceX -5, mHeight + spaceTop - pieceY * j,
//					paint);
//			paint.setStrokeWidth(3);
//			// for (int i = 0; i < 10; i++) {//小刻度 10个格
//			// canvas.drawLine(spaceLeft, mHeight + spaceTop - pieceY * j
//			// -pieceY*i/10,
//			// spaceLeft + 5, mHeight + spaceTop - pieceY * j -pieceY*i/10,
//			// paint);
//			// }
//
//			paint.setTextAlign(Paint.Align.CENTER);
//			paint.setFakeBoldText(true);
//			// canvas.drawText(coordinateY[j]+"", spaceLeft - 18 , mHeight +
//			// spaceTop - pieceY * j + 5, paint);
//			paint.setFakeBoldText(true);
//			paint.setTextSize(14);
//			canvas.drawText(
//					splitStringwith2point(minDataY + (maxDataY - minDataY) * j
//							/ countY + "", 4), spaceLeft - 30, mHeight
//							+ spaceTop - pieceY * j + 5, paint);
//			// canvas.drawtext
//		}
//		canvas.drawText(
//				splitStringwith2point(minDataY + (maxDataY - minDataY) + "", 4),
//				spaceLeft - 30, mHeight + spaceTop - pieceY * countY + 5, paint);
//		paint.setStrokeWidth(1);
//		canvas.drawLine(spaceLeft, mHeight + spaceTop - pieceY * countY  , mWidth
//				+ spaceLeft - pieceX -5, mHeight + spaceTop - pieceY * countY,
//				paint);
//		paint.setStrokeWidth(3);
//	}
//
//	/**
//	 * 画三角形 用于画坐标轴的箭头
//	 */
//	private void drawTriangle(Canvas canvas, Point p1, Point p2, Point p3) {
//		Path path = new Path();
//		path.moveTo(p1.x, p1.y);
//		path.lineTo(p2.x, p2.y);
//		path.lineTo(p3.x, p3.y);
//		path.close();
//
//		Paint paint = new Paint();
//		paint.setColor(Color.BLACK);
//		paint.setStyle(Paint.Style.FILL);
//		// 绘制这个多边形
//		canvas.drawPath(path, paint);
//	}
//
//	/**
//	 * 
//	 * @Title: addLine
//	 * @Description: TODO(这里用一句话描述这个方法的作用)
//	 * @param @param coordinateX
//	 * @param @param coordinateY
//	 * @param @param dataY
//	 * @param @param color 设定文件
//	 * @return void 返回类型
//	 * @throws
//	 */
//	public void addLine(String lineName, String[] coordinateX, float[] dataY) {
//		int paintColor = 0;
//		countX = coordinateX.length;
//		switch (countLine % 6) {
//		case 0:
//			paintColor = Color.RED;
//			break;
//
//		case 1:
//			paintColor = Color.YELLOW;
//			break;
//
//		case 2:
//			paintColor = Color.BLUE;
//			break;
//
//		case 3:
//			paintColor = Color.GREEN;
//			break;
//
//		case 4:
//			paintColor = Color.MAGENTA;
//			break;
//
//		case 5:
//			paintColor = Color.CYAN;
//			break;
//
//		}
//
//		countLine++;
//		lineList.add(new XYChartData(lineName, coordinateX, dataY, paintColor));
//
//		findMaxDataY(); // 找出纵坐标的最大值
//		this.coordinateX = coordinateX;
//
//	}
//
//	/**
//	 * 截取小数点后面的位数
//	 * 
//	 * @param str
//	 *            需要转换的数据
//	 * @param number
//	 *            保留小数点后面的位数
//	 * @return
//	 */
//	public static String splitStringwith2point(String str, int number) {
//		if (str.contains(".")) {
//			StringBuffer sb = new StringBuffer();
//			String[] strings = str.split("\\.");
//			String before = strings[0];
//			String after = strings[1];
//			if (after.length() >= number) {
//				String strAfter = after.substring(0, number);
//				sb.append(before).append(".").append(strAfter);
//			} else {
//				sb.append(before).append(".").append(after);
//				int min = number - after.length();
//				for (int i = 0; i < min; i++) {
//					sb.append("0");
//				}
//			}
//			return sb.toString();
//		} else {
//			return str;
//		}
//	}

}
