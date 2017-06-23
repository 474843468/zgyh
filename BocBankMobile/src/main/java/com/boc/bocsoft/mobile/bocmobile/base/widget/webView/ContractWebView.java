package com.boc.bocsoft.mobile.bocmobile.base.widget.webView;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by huixiaobo on 2016/7/26.
 * 合同webView
 */
public class ContractWebView extends BaseWebView {

    /**合同key*/
    private static final String KEY = "contract";
    /**url*/
    public static final String ELOANCONTRACT_URL = "file:///android_asset/webviewcontent/eloanapply/index.html";
    /**url*/
    public static final String CHANGEACCCONTRACT_URL = "file:///android_asset/webviewcontent/changeaccount/index.html";

    /**合同传递对象*/
    private Contract mContract;

    public ContractWebView(Context context) {
        super(context);
    }

    public ContractWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ContractWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public void initView() {
        setDefaultLoadUrl(ELOANCONTRACT_URL);
        super.initView();
    }

    /**
     * 合同传递对象
     */
    public static class Contract {
        public JSONObject jsObj;
        public String contractString;

        public Contract() {

        }
        /**
         * 只需要传入合同内容，无其他操作
         * @param contract
         */
        public Contract(String contract) {
            jsObj = new JSONObject();
            try {
                jsObj.put("contract", contract);
                jsObj.put("cbiCustName", "");
                jsObj.put("cbiCerNo", "");
                jsObj.put("cbiCustAccount", "");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * 需要传入合同内容，和需要替换的字段
         * @param contract
         * @param cbiCustName
         * @param cbiCerNo
         * @param cbiCustAccount
         */
        public Contract(String contract, String cbiCustName, String cbiCerNo, String cbiCustAccount) {
            jsObj = new JSONObject();
            try {
                jsObj.put("contract", contract);
                jsObj.put("cbiCustName", cbiCustName);
                jsObj.put("cbiCerNo", cbiCerNo);
                jsObj.put("cbiCustAccount", cbiCustAccount);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * 贷款变更还款账户使用
         * @param loanType
         * @param accNo
         */
        public Contract(String loanType, String accNo) {
            jsObj = new JSONObject();
            try {
                jsObj.put("loanType", loanType);
                jsObj.put("accNo", accNo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public void setContractString(String value) {
            contractString = value;
        }

        @JavascriptInterface
        public JSONObject getContract() {
            return jsObj;
        }

        @JavascriptInterface
        public String getJsonContract() {
            return jsObj.toString();
        }

        @JavascriptInterface
        public String getContractString() {
            return contractString;
        }

    }

    /**
     * 传值对象
     */
    public void setData (Contract contract) {
        mContract = contract;
    }

    /**
     * 重写动态交互webView
     */
    @Override
    public void fankInteractive() {
        super.fankInteractive();
        if (mContract != null) {
            mWebView.addJavascriptInterface(mContract, KEY);

        }
    }

}
