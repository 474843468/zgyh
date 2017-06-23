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

package com.boc.bocsoft.mobile.framework.zxing.scan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.boc.bocsoft.mobile.framework.zxing.camera.CameraManager;
import com.boc.bocsoft.mobile.framework.zxing.decode.DecodeThread;
import com.boc.bocsoft.mobile.framework.zxing.scan.itf.ICaptureHandlerHelper;
import com.boc.bocsoft.mobile.framework.zxing.utils.QrcodeConstants;
import com.google.zxing.Result;

public class CaptureHandler extends Handler {

	private final ICaptureHandlerHelper captureHandlerHelper;
	private final DecodeThread decodeThread;
	private final CameraManager cameraManager;
	private State state;

	private enum State {
		PREVIEW, SUCCESS, DONE
	}

	public CaptureHandler(ICaptureHandlerHelper captureHandlerHelper, CameraManager cameraManager) {
		this.captureHandlerHelper = captureHandlerHelper;
		decodeThread = new DecodeThread(captureHandlerHelper);
		decodeThread.start();
		state = State.SUCCESS;

		// Start ourselves capturing previews and decoding.
		this.cameraManager = cameraManager;
		cameraManager.startPreview();
		restartPreviewAndDecode();
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case QrcodeConstants.RESTART_PREVIEW:
			restartPreviewAndDecode();
			break;
		case QrcodeConstants.DECODE_SUCCEEDED:
			state = State.SUCCESS;
			Bundle bundle = message.getData();

			captureHandlerHelper.handleDecode((Result) message.obj, bundle);
			break;
		case QrcodeConstants.DECODE_FAILED:
			// We're decoding as fast as possible, so when one decode fails,
			// start another.
			state = State.PREVIEW;
			cameraManager.requestPreviewFrame(decodeThread.getHandler(), QrcodeConstants.DECODE);
			break;
		}
	}

	public void quitSynchronously() {
		state = State.DONE;
		cameraManager.stopPreview();
		Message quit = Message.obtain(decodeThread.getHandler(), QrcodeConstants.QUIT);
		quit.sendToTarget();
		try {
			// Wait at most half a second; should be enough time, and onPause()
			// will timeout quickly
			decodeThread.join(500L);
		} catch (InterruptedException e) {
			// continue
		}

		// Be absolutely sure we don't send any queued up messages
		removeMessages(QrcodeConstants.DECODE_SUCCEEDED);
		removeMessages(QrcodeConstants.DECODE_FAILED);
	}

	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			cameraManager.requestPreviewFrame(decodeThread.getHandler(), QrcodeConstants.DECODE);
		}
	}

}
