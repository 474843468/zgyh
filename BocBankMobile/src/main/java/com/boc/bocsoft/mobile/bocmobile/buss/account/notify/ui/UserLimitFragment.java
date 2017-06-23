package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractWebView;

/**开通短信通知协议界面
 * Created by wangtong on 2016/8/12.
 */
public class UserLimitFragment extends BussFragment {

    protected ContractWebView userLimit;
    protected TextView btnKnow;
    private View rootView = null;
    private boolean isContinueOpen = false;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_user_limit, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        userLimit = (ContractWebView) rootView.findViewById(R.id.user_limit);
        btnKnow = (TextView) rootView.findViewById(R.id.btn_know);
    }

    @Override
    public void initData() {
        super.initData();
        String name = getArguments().getString("userName");
        isContinueOpen = getArguments().getBoolean("isContinueOpen");
        ContractWebView.Contract contract = new ContractWebView.Contract();
        contract.setContractString(name);
        userLimit.setDefaultLoadUrl("file:///android_asset/webviewcontent/accountnotify/SmsNoticeContract.html");
        userLimit.setData(contract);
    }

    @Override
    public void setListener() {
        super.setListener();
        btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContinueOpen) {
                    startWithPop(findFragment(AccSmsNotifyHomeFragment.class).getOpenFragment());
                } else {
                    pop();
                }
            }
        });
    }

//    @Override
//    public boolean onBack() {
      /*  if (isContinueOpen) {
            startWithPop(findFragment(AccSmsNotifyHomeFragment.class).getOpenFragment());
        } else {
            pop();
        }
        return super.onBack();*/
        //在信息页面,屏蔽返回键 禁止返回
//        return  true;
//    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

}
