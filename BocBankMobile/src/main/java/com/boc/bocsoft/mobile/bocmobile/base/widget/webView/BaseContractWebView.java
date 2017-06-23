package com.boc.bocsoft.mobile.bocmobile.base.widget.webView;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import com.google.gson.Gson;

/**
 * 作者：XieDu
 * 创建时间：2016/9/13 15:47
 * 描述：
 */
public class BaseContractWebView extends BaseH5WebView<BaseContractWebView.Contract> {
    /**
     * 合同key
     */
    private static final String KEY = "contract";

    public BaseContractWebView(Context context) {
        super(context);
    }

    public BaseContractWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseContractWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initData() {
        super.initData();
        setKey(KEY);
    }

    /**
     * 合同传递对象
     */
    public static class Contract implements IWebInfoProxy {
        public String contractString;

        public Contract() {
        }

        /**
         * 只需要传入合同内容，无其他操作
         */
        public Contract(String contract) {
            contractString = contract;
        }

        /**
         * 只需要传入合同内容，无其他操作
         */
        public Contract(Object contract) {
            contractString = new Gson().toJson(contract);
        }

        public void setContractString(String value) {
            contractString = value;
        }

        @JavascriptInterface
        public String getContract() {
            return contractString;
        }

        @Override
        public String getInfoJson() {
            return contractString;
        }

        @Override
        public void close() {
        }
    }
}
