package com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

/**
 * 微信分享相关方法
 * Created by liuweidong on 2016/11/1.
 */
public class ShareUtils {
    private static final int THUMB_SIZE = 150;

    /**
     * 分享图片
     */
    public static SendMessageToWX.Req shareImg(View view) {
        Bitmap bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bmp));

        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        return req;
    }

    /**
     * 分享链接
     *
     * @param flag 0：分享到好友 1：分享到朋友圈
     * @param url
     */
    public static SendMessageToWX.Req shareWebPage(int flag, String url, String title, String description) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
//        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.share_logo);
//        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");// transaction唯一标识一个请求
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        return req;
    }

    @Deprecated
    public static SendMessageToWX.Req shareWebPage(int flag, String url) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
//        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.share_logo);
//        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");// transaction唯一标识一个请求
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        return req;
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
