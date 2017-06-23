package com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.bean.ListHorizontalBean;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.platformtools.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信分享信息页面
 * Created by liuweidong on 2016/8/20.
 */
public class ShareInfoFragment extends BussFragment {
    private ShareInfoView shareInfoView;
    private IWXAPI api;//第三方app和微信通信的接口
    private boolean isOther = false;
    private static final int THUMB_SIZE = 150;
    private int softInputMode;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        shareInfoView = new ShareInfoView(mContext);
        api = WXAPIFactory.createWXAPI(mContext, ApplicationConst.APP_ID, false);
        api.registerApp(ApplicationConst.APP_ID);//将应用的APPID注册到微信

        // windowSoftInputMode属性的获取
        softInputMode = mActivity.getWindow().getAttributes().softInputMode;
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return shareInfoView;
    }

    @Override
    public void initView() {
        super.initView();
        if (isOther) {
            shareInfoView.isShowGone();
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        hideSoftInput();
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ljljlj", "ShareInfoFragment onDestroy()");
        mActivity.getWindow().setSoftInputMode(softInputMode);
    }

    @Override
    public void initData() {
        shareInfoView.setTxtShareInfo(getArguments().getString("ShareInfo"));
        shareInfoView.setMoneyInfo(getArguments().getStringArray("Money"));

        List<ListHorizontalBean> list = new ArrayList<>();
        String[] names = getArguments().getStringArray("Name");
        String[] values = getArguments().getStringArray("Value");
        for (int i = 0; i < values.length; i++) {
            if(StringUtils.isEmptyOrNull(values[i])){
            } else {
                ListHorizontalBean item = new ListHorizontalBean();
                item.setName(names[i]);
                item.setValue(values[i]);
                list.add(item);
            }
        }
        shareInfoView.setListViewData(list);
    }

    @Override
    public void setListener() {
        shareInfoView.setListener(new ShareInfoView.ClickListener() {
            @Override
            public void shareWechat() {
                if (api.isWXAppInstalled()) {
                    shareImg();
                } else {
                    showErrorDialog(mContext.getString(R.string.wx_share_message));
                }
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_common_share_info_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 设置是否为非转账功能
     *
     * @param other
     */
    public void setOther(boolean other) {
        isOther = other;
    }

    /**
     * 实例化当前页面对象，接收上个页面传递的数据
     *
     * @param shareInfo
     * @param name
     * @param value
     * @param moneyInfo
     * @return
     */
    public static ShareInfoFragment newInstance(String shareInfo, String[] name, String[] value, String[] moneyInfo) {
        Bundle bundle = new Bundle();
        bundle.putString("ShareInfo", shareInfo);
        bundle.putStringArray("Name", name);
        bundle.putStringArray("Value", value);
        bundle.putStringArray("Money", moneyInfo);
        ShareInfoFragment shareInfoFragment = new ShareInfoFragment();
        shareInfoFragment.setArguments(bundle);
        return shareInfoFragment;
    }

    /**
     * 分享图片
     */
    private void shareImg() {
        Log.i("shareImg", "----------------------分享图片开始");
        LinearLayout layout = shareInfoView.getShareView();//分享的页面
        Bitmap bmp = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.ARGB_8888);
        layout.draw(new Canvas(bmp));

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
        api.sendReq(req);//跳转到朋友圈或会话列表
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
