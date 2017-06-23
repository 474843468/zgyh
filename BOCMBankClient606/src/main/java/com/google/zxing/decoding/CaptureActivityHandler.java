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

package com.google.zxing.decoding;

import java.util.Vector;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.chinamworld.bocmbci.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.CaptureActivity;
import com.google.zxing.Result;
import com.google.zxing.camera.CameraManager;
import com.google.zxing.view.ViewfinderResultPointCallback;


/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 * ���handler��ʵ������4��������Ľ��
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CaptureActivityHandler extends Handler 
{

    private static final String TAG = CaptureActivityHandler.class
            .getSimpleName();

    /**
     * �����ά���activity
     */
    private final CaptureActivity activity;
    /**
     * ���?��ͼƬ�Ľ����̣߳���һ��Looper�̣߳�
     */
    private final DecodeThread decodeThread;
    /**
     * ��ǰ�����״̬
     */
    private State state;

    /**
     * ״̬ö��
     */
    private enum State
    {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureActivityHandler(CaptureActivity activity,
            Vector<BarcodeFormat> decodeFormats, String characterSet)
    {
        this.activity = activity;
        /**
         * ���ý����߳�
         */
        decodeThread = new DecodeThread(activity, decodeFormats, characterSet,
                new ViewfinderResultPointCallback(activity.getViewfinderView()));
        /**
         * �����߳̿�ʼִ��
         */
        decodeThread.start();
        /**
         * ״̬����Ϊ�ɹ�
         */
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        /**
         * ���׼��
         */
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message)
    {
        switch (message.what)
        {
            case R.id.auto_focus:
                // LogGloble.d(TAG, "Got auto-focus message");
                // When one auto focus pass finishes, start another. This is the
                // closest thing to
                // continuous AF. It does seem to hunt a bit, but I'm not sure
                // what else to do.
                if (state == State.PREVIEW)
                {
                    /**
                     * �����Զ��۽�
                     */
                    CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
                }
                break;
//            case R.id.restart_preview:
//                LogGloble.d(TAG, "Got restart preview message");
//                restartPreviewAndDecode();
//                break;
            case R.id.decode_succeeded:
                state = State.SUCCESS;
                Bundle bundle = message.getData();
                Bitmap barcode = bundle == null ? null : (Bitmap) bundle
                        .getParcelable(DecodeThread.BARCODE_BITMAP);
                activity.handleDecode((Result) message.obj, barcode);
                break;
            case R.id.decode_failed:
                // We're decoding as fast as possible, so when one decode fails,
                // start another.
                state = State.PREVIEW;
                CameraManager.get().requestPreviewFrame(
                        decodeThread.getHandler(), R.id.decode);
                break;
                /**
            case R.id.return_scan_result:
                LogGloble.d(TAG, "Got return scan result message");
                activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
                activity.finish();
                break;
            case R.id.launch_product_query:
                LogGloble.d(TAG, "Got product query message");
                String url = (String) message.obj;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                activity.startActivity(intent);
                break;
                **/
        }
    }

    public void quitSynchronously()
    {
        state = State.DONE;
        /**
         * �ر����
         */
        CameraManager.get().stopPreview();
        /**
         * �ر�looper�����߳�
         */
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        
        try
        {
            /**
             * ����join����4�ȴ�����̵߳Ľ���
             */
            decodeThread.join();
        }
        catch (InterruptedException e)
        {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    public void restartPreviewAndDecode()
    {
        if (state == State.SUCCESS)
        {
            /**
             * ����״̬Ϊ����׼������ʵ�������ý���handler����ʾͼ���surface
             */
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
                    R.id.decode);
            /**
             * ��������Զ��۽�
             */
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            /**
             * ���view�еĽ��ͼƬ,������view�ٻ����Ǹ��м�Ŀ��ͺ���
             */
            activity.drawViewfinder();
        }
    }

}
