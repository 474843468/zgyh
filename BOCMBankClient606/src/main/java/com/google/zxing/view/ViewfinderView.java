/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.google.zxing.ResultPoint;
import com.google.zxing.camera.CameraManager;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	private static final long ANIMATION_DELAY = 100L;
	private static final int OPAQUE = 0xFF;

	private final Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
//	private final int frameColor;
	private final int laserColor;
	private final int resultPointColor;
	private int scannerAlpha;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;
	
	private Bitmap lineBmp;
	private int startY;

	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
//		frameColor = resources.getColor(R.color.viewfinder_frame);
		laserColor = resources.getColor(R.color.viewfinder_laser);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		scannerAlpha = 0;
		possibleResultPoints = new HashSet<ResultPoint>(5);
		
		Drawable lineDrawable = resources.getDrawable(R.drawable.img_redline);
		BitmapDrawable bd = (BitmapDrawable) lineDrawable;
		lineBmp = bd.getBitmap();
	}

	@Override
	public void onDraw(Canvas canvas) {
		/**
		 * 锟斤拷锟斤拷锟轿撅拷锟斤拷锟叫硷拷锟斤拷示锟斤拷锟角革拷锟斤拷锟斤拷CameraManager锟叫讹拷锟斤拷
		 */
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		lineBmp = Bitmap.createScaledBitmap(lineBmp, frame.right - frame.left, 15, true);
		
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// Draw the exterior (i.e. outside the framing rect) darkened
		// 锟斤拷锟斤拷锟斤拷锟杰碉拷4锟斤拷锟斤拷锟�
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

//		if (resultBitmap != null) {
//			// Draw the opaque result bitmap over the scanning rectangle
//			paint.setAlpha(OPAQUE);
//			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
//		} else {
			//画边框 wjp
			int linewidht = 10;
			paint.setColor(laserColor);
			
			canvas.drawRect(-10 + frame.left, -10 + frame.top,
					-10 + (linewidht + frame.left), -10 + (40 + frame.top), paint);
			canvas.drawRect(-10 + frame.left, -10 + frame.top,
					-10 + (40 + frame.left), -10 + (linewidht + frame.top), paint);
			
			canvas.drawRect(10 + ((0 - linewidht) + frame.right),
					-10 + frame.top, 10 + (1 + frame.right),
					-10 + (40 + frame.top), paint);
			canvas.drawRect(10 + (-40 + frame.right), -10 + frame.top, 10
					+ frame.right, -10 + (linewidht + frame.top), paint);
			
			canvas.drawRect(-10 + frame.left, 10 + (-39 + frame.bottom),
					-10 + (linewidht + frame.left), 10 + (1 + frame.bottom),
					paint);
			canvas.drawRect(-10 + frame.left, 10
					+ ((0 - linewidht) + frame.bottom), -10 + (40 + frame.left),
					10 + (1 + frame.bottom), paint);
			
			canvas.drawRect(10 + ((0 - linewidht) + frame.right), 10
					+ (-39 + frame.bottom), 10 + (1 + frame.right), 10
					+ (1 + frame.bottom), paint);
			canvas.drawRect(10 + (-40 + frame.right), 10
					+ ((0 - linewidht) + frame.bottom), 10 + frame.right, 10
					+ (linewidht - (linewidht - 1) + frame.bottom), paint);
			
			paint.setColor(laserColor);
			paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
			if(frame.right - frame.left <= 320){
				startY += 6;//往下走的速度
			}else{
				startY += 8;
			}
			canvas.drawBitmap(lineBmp, frame.left,frame.top + startY, null);
			if (frame.top + startY > frame.height() + frame.top ) {
				startY = 0;
//				endY = 0;
			}

			// Draw a red "laser scanner" line through the middle to show
			// decoding is active
			// 锟斤拷锟斤拷一锟斤拷锟叫硷拷暮锟斤拷锟�
			paint.setColor(laserColor);
			paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//			int middle = frame.height() / 2 + frame.top;
//			canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1,
//					middle + 2, paint);

			/**
			 * 锟劫伙拷锟狡匡拷锟斤拷锟角斤拷锟侥碉拷
			 */
			Rect previewFrame = CameraManager.get().getFramingRectInPreview();
			float scaleX = frame.width() / (float) previewFrame.width();
			float scaleY = frame.height() / (float) previewFrame.height();
			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				/**
				 * 锟斤拷锟矫伙拷锌锟斤拷艿牡悖拷锟斤拷锟较次匡拷锟杰的碉拷
				 */
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				/**
				 * 锟斤拷锟斤拷锟斤拷瓶锟斤拷锟斤拷墙锟斤拷牡悖拷锟斤拷锟斤拷锟斤拷锟叫匡拷锟斤拷锟角讹拷维锟斤拷牡胤锟�
				 */
				for (ResultPoint point : currentPossible) {
					// canvas.drawCircle(frame.left + point.getX(), frame.top
					// + point.getY(), 6.0f, paint);
					canvas.drawCircle(frame.left
							+ (int) (point.getX() * scaleX), frame.top
							+ (int) (point.getY() * scaleY), 3.0f, paint);
				}
			}
			if (currentLast != null) {
				/**
				 * 锟斤拷锟斤拷锟角伙拷锟斤拷锟斤拷一锟轿匡拷锟杰筹拷锟街的点，
				 * 锟斤拷实锟角伙拷锟斤拷锟较次碉拷锟斤拷锟斤拷锟斤拷锟接ｏ拷锟斤拷锟角半径小一锟斤拷锟斤拷透锟斤拷锟叫∫伙拷锟�
				 */
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 3.0f, paint);
				}
			}

			// Request another update at the animation interval, but only
			// repaint the laser line,
			// not the entire viewfinder mask.
			/**
			 * 锟斤拷锟斤拷锟角伙拷媒锟斤拷锟绞憋拷锟斤拷锟斤拷锟街伙拷锟揭拷锟斤拷驴锟斤拷锟节诧拷锟斤拷锟斤拷锟斤拷
			 */
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
//		}
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷4锟斤拷锟酵计拷锟斤拷锟斤拷锟缴拷锟斤拷锟剿ｏ拷锟斤拷锟斤拷要锟斤拷锟斤拷扫锟借，
	 * 锟斤拷么锟斤拷要锟斤拷锟斤拷前锟斤拷图片锟斤拷锟斤拷然锟斤拷突锟斤拷锟斤拷锟角革拷锟斤拷秃锟斤拷锟� 锟斤拷activity锟叫碉拷锟斤拷
	 */
	public void drawViewfinder() {
//		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 *            锟斤拷resultBitmap锟斤拷为锟秸碉拷时锟斤拷view锟结将锟斤拷锟斤拷频锟斤拷锟斤拷锟斤拷希锟斤拷煤锟斤拷锟铰斤拷锟芥
	 *            ，锟斤拷锟斤拷锟斤拷锟角匡拷锟斤拷锟侥斤拷锟�
	 */
	public void drawResultBitmap(Bitmap barcode) {
//		resultBitmap = barcode;
		invalidate();
	}

	/**
	 * 锟矫凤拷锟斤拷锟斤拷ViewfinderResultPointCallback锟斤拷使锟矫碉拷锟斤拷锟斤拷4锟斤拷涌锟斤拷艿牡锟�
	 */
	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}
