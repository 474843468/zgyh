package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.apply;

import android.annotation.SuppressLint;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractWebView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseServiceBureauFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author wangyang
 *         16/8/30 17:14
 *         申请虚拟卡界面
 */
@SuppressLint("ValidFragment")
public class ApplyVirtualCardFragment extends BaseServiceBureauFragment {

    private AccountBean accountBean;

    public ApplyVirtualCardFragment(AccountBean accountBean, boolean isOnlyClosed) {
        super(isOnlyClosed);
        this.accountBean = accountBean;
    }

    @Override
    protected String getUrl() {
        return "file:///android_asset/webviewcontent/bureau/VirualCardSvrContract.html";
    }

    @Override
    protected ContractWebView.Contract getContract() {
        ContractWebView.Contract contract = new ContractWebView.Contract();
        contract.jsObj = new JSONObject();
        try {
            contract.jsObj.put("customerName", ApplicationContext.getInstance().getUser().getCustomerName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contract;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (accountBean != null)
            startWithPop(new VirtualCardApplyFragment(accountBean));
    }
}
