package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.presenter.QrcodeMePresenter;
import com.boc.bocsoft.mobile.framework.zxing.utils.BitmapUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.platformtools.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieDu on 2016/7/4.
 */
public class QrcodeMeFragment extends MvpBussFragment<QrcodeMePresenter>
        implements QrcodeMeContract.View, View.OnClickListener {

    protected SelectAccountButton btnSelectAccount;
    protected ImageView ivQrcode;
    protected Button btnSave;
    protected Button btnShare;
    protected LinearLayout ll_shared_pic;
    private View rootView;
    private SelectAccoutFragment mSelectAccountFragment;
    private AccountBean mAccountBean;
    private List<AccountBean> mAccountList;
    private ArrayList<String> mTypelist;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.fragment_qrcode_me, null);
        return rootView;
    }

    /**
     * 初始化数据
     */
    @Override
    public void initView() {
        btnSelectAccount = (SelectAccountButton) rootView.findViewById(R.id.btn_select_account);
        btnSelectAccount.setOnClickListener(QrcodeMeFragment.this);
        ivQrcode = (ImageView) rootView.findViewById(R.id.iv_qrcode);
        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(QrcodeMeFragment.this);
        btnShare = (Button) rootView.findViewById(R.id.btn_share);
        ll_shared_pic = (LinearLayout) rootView.findViewById(R.id.ll_shared_pic);
        btnShare.setOnClickListener(QrcodeMeFragment.this);
    }

    protected String getTitleValue() {
        return getString(R.string.boc_qrcode_myself);
    }

    @Override
    public void initData() {
        mTypelist = new ArrayList<>();
        mTypelist.add(ApplicationConst.ACC_TYPE_ORD);
        mTypelist.add(ApplicationConst.ACC_TYPE_BRO);
        mTypelist.add(ApplicationConst.ACC_TYPE_RAN);
        mTypelist.add(ApplicationConst.ACC_TYPE_GRE);
        mTypelist.add(ApplicationConst.ACC_TYPE_ZHONGYIN);
        mTypelist.add(ApplicationConst.ACC_TYPE_SINGLEWAIBI);
        mAccountList = ApplicationContext.getInstance().getChinaBankAccountList(mTypelist);
        if (mAccountList.size() > 0) {
            onSelectAccount(mAccountList.get(0));
        }
    }

    @Override
    public void setListener() {
        btnSelectAccount.setOnClickListener(QrcodeMeFragment.this);
        btnSave.setOnClickListener(QrcodeMeFragment.this);
        btnShare.setOnClickListener(QrcodeMeFragment.this);
    }

    /**
     * 是否显示标题栏
     */
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected QrcodeMePresenter initPresenter() {
        return new QrcodeMePresenter(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_select_account) {
            if (mSelectAccountFragment == null) {
                mSelectAccountFragment = SelectAccoutFragment.newInstance(mTypelist);
            }
            startForResult(mSelectAccountFragment,
                    SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
        } else if (view.getId() == R.id.btn_save) {
            boolean saveResult = BitmapUtils.saveBitmap(mContext,
                    BitmapUtils.createViewForBitmap(mContext, ll_shared_pic));
            String toast = saveResult ? "保存成功" : "保存失败，稍后再试";
            Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btn_share) {
            api = WXAPIFactory.createWXAPI(mContext, ApplicationConst.APP_ID, false);
            api.registerApp(ApplicationConst.APP_ID);//将应用的APPID注册到微信
            if (api.isWXAppInstalled()) {
                shareImg();
            } else {
                showErrorDialog(mContext.getString(R.string.wx_share_message));
            }
        }
    }

    private static final int THUMB_SIZE = 150;
    private IWXAPI api;//第三方app和微信通信的接口

    /**
     * 生成图片并分享
     */
    private void shareImg() {
        Bitmap bmp = Bitmap.createBitmap(ll_shared_pic.getWidth(), ll_shared_pic.getHeight(), Bitmap.Config.ARGB_8888);
        ll_shared_pic.draw(new Canvas(bmp));

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
        return (type == null) ? String.valueOf(System.currentTimeMillis()) :
                type + System.currentTimeMillis();
    }

    @Override
    public void onGenerateSuccess(Bitmap bitmap) {
        closeProgressDialog();
        if (bitmap == null) {
            showErrorDialog("二维码生成错误，请重试");
        } else {
            ivQrcode.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT == resultCode) {
            AccountBean accountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            onSelectAccount(accountBean);
        }
    }

    private void onSelectAccount(AccountBean accountBean) {
        if (accountBean == null) {
            return;
        }
        if (accountBean != mAccountBean) {
            mAccountBean = accountBean;
            btnSelectAccount.setData(accountBean);
            showLoadingDialog();
            getPresenter().generateQrcode(accountBean);
        }
    }
}
