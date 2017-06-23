package com.boc.bocsoft.mobile.bocmobile.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * 微信分享的回调
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private final String TAG = "WXEntryActivity";
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG , "onCreate----------------------消息已发送");
		api = WXAPIFactory.createWXAPI(this, ApplicationConst.APP_ID, false);
		api.registerApp(ApplicationConst.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i(TAG , "onNewIntent----------------------");
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	/**
	 * 微信发送的请求回调
	 * @param req
	 */
	@Override
	public void onReq(BaseReq req) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onReq----------------------微信主动发送");
	}

	/**
	 * 第三方应用发送到微信的请求处理后的响应结果，将回调该方法
	 * @param resp
	 */
	@Override
	public void onResp(BaseResp resp) {
		Log.i(TAG, "onResp----------------------消息响应");
		String result = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = getResources().getString(R.string.errcode_success);
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = getResources().getString(R.string.errcode_cancel);
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = getResources().getString(R.string.errcode_deny);
			break;
		default:
			result = getResources().getString(R.string.errcode_unknown);
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
}
