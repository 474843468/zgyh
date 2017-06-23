package com.boc.bocsoft.mobile.bocmobile.base.cordova;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConfig;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.NetworkUtils;
import java.util.List;
import java.util.Locale;
import org.apache.cordova.Config;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewEngine;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginEntry;
import org.apache.cordova.PluginManager;
import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewClient;
import org.apache.cordova.engine.SystemWebViewEngine;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者：XieDu
 * 创建时间：2016/11/28 9:27
 * 描述：
 */
public class BaseCordovaActivity extends BaseMobileActivity {

    public static final String URL = "url";
    public static final String TITLE = "title";

    private static final String URL_PREFIX_HTTP = "http";
    private static final String URL_PREFIX_HTTPS = "https";
    private static final String URL_PREFIX_FILE = "file";
    protected FrameLayout lytWebview;

    private String mUrl;

    protected Handler mHandler = new Handler();
    //protected WebView mCordovaWebView;
    protected LinearLayout llNetError;

    protected ProgressBar progressBar;

    public static String TAG = "BaseCordovaActivity";

    // The webview for our app
    protected CordovaWebView mCordovaWebView;
    protected SystemWebView mWebView;

    private static int ACTIVITY_STARTING = 0;
    private static int ACTIVITY_RUNNING = 1;
    private static int ACTIVITY_EXITING = 2;

    // Keep app running when pause is received. (default = true)
    // If true, then the JavaScript and native code continue to run in the background
    // when another application (activity) is started.
    protected boolean keepRunning = true;

    // Flag to keep immersive mode if set to fullscreen
    protected boolean immersiveMode;

    // Read from config.xml:
    protected CordovaPreferences preferences;
    protected String launchUrl;
    protected List<PluginEntry> pluginEntries;
    protected BaseCordovaInterfaceImpl cordovaInterface;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        NetworkUtils.setWebViewCookie(mWebView, createCookieUrl());
    }

    private String createCookieUrl() {
        String biiUrl = ApplicationConfig.BII_URL;
        if (!biiUrl.endsWith("/")) {
            biiUrl += "/";
        }
        return biiUrl;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // need to activate preferences before super.onCreate to avoid "requestFeature() must be called before adding content" exception
        loadConfig();
        BIIClient.instance.getCookies();

        String logLevel = preferences.getString("loglevel", "ERROR");
        LOG.setLogLevel(logLevel);

        LOG.i(TAG, "Apache Cordova native platform version " + CordovaWebView.CORDOVA_VERSION
                + " is starting");
        LOG.d(TAG, "CordovaActivity.onCreate()");

        if (!preferences.getBoolean("ShowTitle", false)) {
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        if (preferences.getBoolean("SetFullscreen", false)) {
            LOG.d(TAG,
                    "The SetFullscreen configuration is deprecated in favor of Fullscreen, and will be removed in a future version.");
            preferences.set("Fullscreen", true);
        }
        if (preferences.getBoolean("Fullscreen", false)) {
            // NOTE: use the FullscreenNotImmersive configuration key to set the activity in a REAL full screen
            // (as was the case in previous cordova versions)
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) && !preferences.getBoolean(
                    "FullscreenNotImmersive", false)) {
                immersiveMode = true;
            } else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }

        super.onCreate(savedInstanceState);
        cordovaInterface = makeCordovaInterface();
        if (savedInstanceState != null) {
            cordovaInterface.restoreInstanceState(savedInstanceState);
        }
        initCordova();
    }

    @Override
    protected View getContentView() {
        View root = View.inflate(mContext, R.layout.boc_activity_webview, null);
        lytWebview = (FrameLayout) root.findViewById(R.id.lytWebview);
        llNetError = (LinearLayout) root.findViewById(R.id.llNetError);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        return root;
    }

    @Override
    public void initView() {
        mUrl = getIntent().getStringExtra(URL);
        String title = getIntent().getStringExtra(TITLE);
        launchUrl = StringUtils.isEmpty(mUrl) ? Config.getStartUrl() : mUrl;
        if (mTitleBarView != null) {
            mTitleBarView.setRightImgBtnVisible(false);
            this.mTitleBarView.setTitle(title);
        }
        super.initView();
    }

    @Override
    public void setListener() {
        super.setListener();

        llNetError.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadUrl();
            }
        });
    }

    protected void initCordova() {
        mCordovaWebView = makeWebView();
        createViews();
        if (!mCordovaWebView.isInitialized()) {
            mCordovaWebView.init(cordovaInterface, pluginEntries, preferences);
        }
        cordovaInterface.onCordovaInit(mCordovaWebView.getPluginManager());

        // Wire the hardware volume controls to control media if desired.
        String volumePref = preferences.getString("DefaultVolumeStream", "");
        if ("media".equals(volumePref.toLowerCase(Locale.ENGLISH))) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
    }

    /**
     * 设置webview
     */
    private void configWebView(WebView webView) {
        webView.setWebViewClient(
                new SystemWebViewClient((SystemWebViewEngine) mCordovaWebView.getEngine()) {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        super.shouldOverrideUrlLoading(view, url);
                        return false;
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        lytWebview.setVisibility(View.VISIBLE);
                        llNetError.setVisibility(View.GONE);
                        showLoadingDialog();
                    }

                    /**
                     * 页面加载完成
                     * @param view
                     * @param url
                     */
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        closeProgressDialog();
                    }

                    /**
                     * 加载页面发生错误
                     * @param view
                     * @param errorCode
                     * @param description
                     * @param failingUrl
                     */
                    public void onReceivedError(WebView view, int errorCode, String description,
                            String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                    }
                });

        webView.getSettings().setDomStorageEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setSaveFormData(false);
        webSettings.setDomStorageEnabled(true);

        webView.setWebChromeClient(
                new SystemWebChromeClient((SystemWebViewEngine) mCordovaWebView.getEngine()) {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        if (progressBar != null) {
                            if (newProgress == 100) {
                                progressBar.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setProgress(newProgress);
                            }
                        }
                    }
                });
        webView.setVerticalScrollBarEnabled(false);
    }

    @SuppressWarnings("deprecation")
    protected void loadConfig() {
        Config.init(this);
        preferences = Config.getPreferences();
        pluginEntries = Config.getPluginEntries();
    }

    //Suppressing warnings in AndroidStudio
    @SuppressWarnings({ "deprecation", "ResourceType" })
    protected void createViews() {
        //Why are we setting a constant as the ID? This should be investigated
        mCordovaWebView.getView().setId(100);
        mCordovaWebView.getView()
                       .setLayoutParams(
                               new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                       ViewGroup.LayoutParams.MATCH_PARENT));
        mWebView = (SystemWebView) mCordovaWebView.getView();
        configWebView(mWebView);
        lytWebview.addView(mCordovaWebView.getView());

        if (preferences.contains("BackgroundColor")) {
            try {
                int backgroundColor = preferences.getInteger("BackgroundColor", Color.BLACK);
                // Background of activity:
                mCordovaWebView.getView().setBackgroundColor(backgroundColor);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        mCordovaWebView.getView().requestFocusFromTouch();
    }

    /**
     * Construct the default web view object.
     * <p/>
     * Override this to customize the webview that is used.
     */
    protected CordovaWebView makeWebView() {
        return new CordovaWebViewImpl(makeWebViewEngine());
    }

    protected CordovaWebViewEngine makeWebViewEngine() {
        return CordovaWebViewImpl.createEngine(this, preferences);
    }

    protected BaseCordovaInterfaceImpl makeCordovaInterface() {
        return new BaseCordovaInterfaceImpl(this) {
            @Override
            public Object onMessage(String id, Object data) {
                // Plumb this to CordovaActivity.onMessage for backwards compatibility
                return BaseCordovaActivity.this.onMessage(id, data);
            }
        };
    }

    public void loadUrl() {
        loadUrl(launchUrl);
    }

    /**
     * Load the url into the webview.
     */
    public void loadUrl(String url) {
        launchUrl = url;
        if (mCordovaWebView == null) {
            initCordova();
        }

        // If keepRunning
        this.keepRunning = preferences.getBoolean("KeepRunning", true);

        mCordovaWebView.loadUrlIntoView(url, true);
    }

    /**
     * Called when the system is about to start resuming a previous activity.
     */
    @Override
    protected void onPause() {
        super.onPause();
        LOG.d(TAG, "Paused the activity.");

        if (this.mCordovaWebView != null) {
            // CB-9382 If there is an activity that started for result and main activity is waiting for callback
            // result, we shoudn't stop WebView Javascript timers, as activity for result might be using them
            boolean keepRunning =
                    this.keepRunning || this.cordovaInterface.getActivityResultCallback() != null;
            this.mCordovaWebView.handlePause(keepRunning);
        }
    }

    /**
     * Called when the activity receives a new intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Forward to plugins
        if (this.mCordovaWebView != null) {
            this.mCordovaWebView.onNewIntent(intent);
        }
    }

    /**
     * Called when the activity will start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LOG.d(TAG, "Resumed the activity.");

        if (this.mCordovaWebView == null) {
            return;
        }
        // Force window to have focus, so application always
        // receive user input. Workaround for some devices (Samsung Galaxy Note 3 at least)
        this.getWindow().getDecorView().requestFocus();

        this.mCordovaWebView.handleResume(this.keepRunning);
    }

    /**
     * Called when the activity is no longer visible to the user.
     */
    @Override
    protected void onStop() {
        super.onStop();
        LOG.d(TAG, "Stopped the activity.");

        if (this.mCordovaWebView == null) {
            return;
        }
        this.mCordovaWebView.handleStop();
    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
        super.onStart();
        LOG.d(TAG, "Started the activity.");

        if (this.mCordovaWebView == null) {
            return;
        }
        this.mCordovaWebView.handleStart();
    }

    /**
     * The final call you receive before your activity is destroyed.
     */
    @Override
    public void onDestroy() {
        LOG.d(TAG, "CordovaActivity.onDestroy()");
        super.onDestroy();

        if (this.mCordovaWebView != null) {
            mCordovaWebView.handleDestroy();
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
    }

    /**
     * Called when view focus is changed
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && immersiveMode) {
            final int uiOptions =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        // Capture requestCode here so that it is captured in the setActivityResultCallback() case.
        cordovaInterface.setActivityResultRequestCode(requestCode);
        super.startActivityForResult(intent, requestCode, options);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode The request code originally supplied to startActivityForResult(),
     * allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its
     * setResult().
     * @param intent An Intent, which can return result data to the caller (various data can be
     * attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        LOG.d(TAG, "Incoming Result. Request code = " + requestCode);
        super.onActivityResult(requestCode, resultCode, intent);
        cordovaInterface.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * Report an error to the host application. These errors are unrecoverable (i.e. the main
     * resource is unavailable).
     * The errorCode parameter corresponds to one of the ERROR_* constants.
     *
     * @param errorCode The error code corresponding to an ERROR_* value.
     * @param description A String describing the error.
     * @param failingUrl The url that failed to load.
     */
    public void onReceivedError(final int errorCode, final String description,
            final String failingUrl) {
        final BaseCordovaActivity me = this;

        // If errorUrl specified, then load it
        final String errorUrl = preferences.getString("errorUrl", null);
        if ((errorUrl != null) && (!failingUrl.equals(errorUrl)) && (mCordovaWebView != null)) {
            // Load URL on UI thread
            me.runOnUiThread(new Runnable() {
                public void run() {
                    me.mCordovaWebView.showWebPage(errorUrl, false, true, null);
                }
            });
        }
        // If not, then display error dialog
        else {
            final boolean exit = !(errorCode == WebViewClient.ERROR_HOST_LOOKUP);
            me.runOnUiThread(new Runnable() {
                public void run() {
                    lytWebview.setVisibility(View.GONE);
                    llNetError.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    /*
     * Hook in Cordova for menu plugins
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mCordovaWebView != null) {
            mCordovaWebView.getPluginManager().postMessage("onCreateOptionsMenu", menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mCordovaWebView != null) {
            mCordovaWebView.getPluginManager().postMessage("onPrepareOptionsMenu", menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mCordovaWebView != null) {
            mCordovaWebView.getPluginManager().postMessage("onOptionsItemSelected", item);
        }
        return true;
    }

    /**
     * Called when a message is sent to plugin.
     *
     * @param id The message id
     * @param data The message data
     * @return Object or null
     */
    public Object onMessage(String id, Object data) {
        if ("onReceivedError".equals(id)) {
            JSONObject d = (JSONObject) data;
            try {
                this.onReceivedError(d.getInt("errorCode"), d.getString("description"),
                        d.getString("url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if ("exit".equals(id)) {
            finish();
        }
        return null;
    }

    protected void onSaveInstanceState(Bundle outState) {
        cordovaInterface.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    /**
     * Called by the system when the device configuration changes while your activity is running.
     *
     * @param newConfig The new device configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.mCordovaWebView == null) {
            return;
        }
        PluginManager pm = this.mCordovaWebView.getPluginManager();
        if (pm != null) {
            pm.onConfigurationChanged(newConfig);
        }
    }

    /**
     * Called by the system when the user grants permissions
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[],
            int[] grantResults) {
        try {
            cordovaInterface.onRequestPermissionResult(requestCode, permissions, grantResults);
        } catch (JSONException e) {
            LOG.d(TAG, "JSONException: Parameters fed into the method are not valid");
            e.printStackTrace();
        }
    }
}
