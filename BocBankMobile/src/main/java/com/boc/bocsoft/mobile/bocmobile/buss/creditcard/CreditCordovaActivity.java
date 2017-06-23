package com.boc.bocsoft.mobile.bocmobile.buss.creditcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.boc.bocsoft.mobile.bocmobile.base.cordova.BaseCordovaActivity;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin.CreditPlugin;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin.PluginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin.model.AccountInfo;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin.model.CertifInfo3DBean;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import org.apache.cordova.PluginEntry;

import java.util.Collection;

/**
 * 信用卡 h5显示activity
 * Created by dingeryue on 2016年12月27.
 */

public class CreditCordovaActivity extends BaseCordovaActivity implements PluginCallback {

    /**
     * 参数 - 账户信息
     **/
    public final static String PARAM_ACCOUNT_INFO = "account";
    /**
     * 参数 - 全球交易人民币记账功能 开通标示
     **/
    public final static String PARAM_OPENFLAG = "openFlag";
    /**
     * 参数 - 外币账单自动购汇设置状态
     **/
    public final static String PARAM_FOREIGNPAYOFFSTATUS = "foreignPayOffStatus";
    /**
     * 参数 - 3D认证
     **/
    public final static String PARAM_CERTIFINFO = "CertifInfo";
    /**
     * 参数 - 加载URL路径
     **/
    public final static String PARAM_TARGETURL = BaseCordovaActivity.URL;

    private CreditPlugin creditPlugin;

    private AccountInfo accountInfo;// 账户信息
    private CertifInfo3DBean certifInfo3DBean;// 3D安全认证
    private boolean openFlag;// 全球交易人民币记账
    private String foreignPayOffStatus;// 外币账单自动购汇

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Collection<PluginEntry> pluginEntries = mCordovaWebView.getPluginManager().getPluginEntries();
        for (PluginEntry entry : pluginEntries) {
            if (CreditPlugin.class.getName().equals(entry.pluginClass)) {
                creditPlugin = (CreditPlugin) mCordovaWebView.getPluginManager().getPlugin(entry.service);
                break;
            }
        }

        if (creditPlugin == null) {

        } else {
            creditPlugin.setPluginCallBack(this);
        }

        parseIntent(getIntent());
        loadUrl(launchUrl);
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    private void parseIntent(Intent intent) {

        if (intent.hasExtra(PARAM_ACCOUNT_INFO)) {// 账户信息
            accountInfo = intent.getParcelableExtra(PARAM_ACCOUNT_INFO);
        }

        if (intent.hasExtra(PARAM_CERTIFINFO)) {// 3D安全认证
            certifInfo3DBean = intent.getParcelableExtra(PARAM_CERTIFINFO);
        }

        if (intent.hasExtra(PARAM_OPENFLAG)) {// 全球交易人民币记账
            openFlag = intent.getBooleanExtra(PARAM_OPENFLAG, false);
        }

        if (intent.hasExtra(PARAM_FOREIGNPAYOFFSTATUS)) {// 外币账单自动购汇
            foreignPayOffStatus = intent.getStringExtra(PARAM_FOREIGNPAYOFFSTATUS);
        }

    }

    @Override
    public AccountInfo getAccountInfo() {

        return accountInfo;
    }

    /**
     * 回显操作结果
     *
     * @param msg
     */
    @Override
    public void showResult(String msg) {
        ToastUtils.show("接收到：" + msg);
        //TODO 设置返回结构信息

        Intent intent = new Intent();
        intent.putExtra("msg", msg);
        setResult(Activity.RESULT_OK, intent);
        ActivityManager.getAppManager().finishActivity();

        //TODO  或者使用Bus传递数据
    }

    /**
     * 跳转指定原生页面
     *
     * @param page
     */
    @Override
    public void goToNative(String page) {
        //0：当前页，即先前打开H5页面时原生所处的页面；
        //1：卡详情页
        //2：手机银行首
        switch (page) {
            case "0":
                finish();
                return;
            case "1":
                finish();
                //TODO 跳转卡详情
                ToastUtils.show("跳转卡详情");
                return;
            case "2":
                //TODO 手机频道页
                ModuleActivityDispatcher.popToHomePage();
                return;
            default:
                return;
        }

    }

    @Override
    public void getInfoByCamera(String functionCode) {
    }

    /**
     * 全球交易人民币记账功能查询
     *
     * @param accountID
     * @return
     */
    @Override
    public boolean queryChargeOnRMBAccount(String accountID) {
        return openFlag;
    }

    /**
     * 外币账单自动购汇设置查询 “0”未设定 “1”已设定
     *
     * @param accountId
     * @return
     */
    @Override
    public String queryForeignPayOffStatus(String accountId) {
        return foreignPayOffStatus;
    }

    /**
     * 3D安全认证查询
     *
     * @param accountId
     * @return
     */
    @Override
    public CertifInfo3DBean query3DCertifInfo(String accountId) {
        return certifInfo3DBean;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }
}
