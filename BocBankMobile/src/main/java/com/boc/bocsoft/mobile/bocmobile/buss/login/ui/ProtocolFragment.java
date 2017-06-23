package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;


/**
 * Created by feibin on 2016/6/30
 * 客户协议fragment  批量协议、 主从合并协议
 */
public class ProtocolFragment extends LoginBaseFragment {
    /**
     * view
     */
    protected View rootView;
    protected TextView userNameTv;
    protected TextView volumeProtocolTv;
    protected TextView hostProtocolTv;
    protected TextView assistProtocolTv;
    protected LinearLayout userNameLl;
    protected LinearLayout protocalTitleTv;
    protected Button volumeNoAgreeBtn;
    protected Button volumeAgreeBtn;
    protected LinearLayout volumelBottomLl;
    protected Button hostNoAgreeBtn;
    protected Button hostAgreeBtn;
    protected LinearLayout hostBottomLl;
    protected Button assistConfirmBtn;
    protected LinearLayout assistBottomLl;
    protected LinearLayout protocolBottomLl;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_protocol_login, null);
        return rootView;
    }

    @Override
    public void initView() {
        userNameTv = (TextView) rootView.findViewById(R.id.userNameTv);
        if(!StringUtils.isEmpty(mCustomerInfor.getName())){
            userNameTv.setText(mCustomerInfor.getName());
        }
        volumeProtocolTv = (TextView) rootView.findViewById(R.id.volumeProtocolTv);
        hostProtocolTv = (TextView) rootView.findViewById(R.id.hostProtocolTv);
        assistProtocolTv = (TextView) rootView.findViewById(R.id.assistProtocolTv);
        userNameLl = (LinearLayout) rootView.findViewById(R.id.userNameLl);
        protocalTitleTv = (LinearLayout) rootView.findViewById(R.id.protocalTitleTv);
        volumeNoAgreeBtn = (Button) rootView.findViewById(R.id.volumeNoAgreeBtn);
        volumeAgreeBtn = (Button) rootView.findViewById(R.id.volumeAgreeBtn);
        volumelBottomLl = (LinearLayout) rootView.findViewById(R.id.volumelBottomLl);
        hostNoAgreeBtn = (Button) rootView.findViewById(R.id.hostNoAgreeBtn);
        hostAgreeBtn = (Button) rootView.findViewById(R.id.hostAgreeBtn);
        hostBottomLl = (LinearLayout) rootView.findViewById(R.id.hostBottomLl);
        assistConfirmBtn = (Button) rootView.findViewById(R.id.assistConfirmBtn);
        assistBottomLl = (LinearLayout) rootView.findViewById(R.id.assistBottomLl);
        protocolBottomLl = (LinearLayout) rootView.findViewById(R.id.protocolBottomLl);
    }

    @Override
    public void initData() {
        super.initData();
        //批量注册客户，先显示批量协议
        if ("3".equals(regtype)) {
            showVolumeProtocolView();
        }
        //非批量注册客户，直接显示合并主从客户协议
        else {
            //主合并客户
            if ("11".equals(combinStatus)) {
                showHostProtocolView();
            }
            //从合并客户
            else if ("10".equals(combinStatus)) {
                showAssistProtocolView();
                setAssistProtocolTv();
            }
        }

    }

    @Override
    public void setListener() {

        //批量不接受按钮点击事件
        volumeNoAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginBasePresenter.queryLogout();
            }
        });
        //批量接受按钮点击事件
        volumeAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有主从合并客户关系
                if ("11".equals(combinStatus)) {
                    showHostProtocolView();
                } else if ("10".equals(combinStatus)) {
                    showAssistProtocolView();
                    setAssistProtocolTv();
                } else {
                    //判断是否需要修改密码
                    judgeModifyPassword();
                }

            }
        });

        //主客户不接受按钮点击事件
        hostNoAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginBasePresenter.queryLogout();
            }
        });
        //主客户接受按钮点击事件
        hostAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否需要修改密码
                judgeModifyPassword();
            }
        });
        //从客户确定按钮点击事件
        assistConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginBasePresenter.queryLogout();
            }
        });
    }

    /**
     * 批量注册view显示
     */
    private void showVolumeProtocolView() {
        protocalTitleTv.setVisibility(View.VISIBLE);
        userNameLl.setVisibility(View.VISIBLE);
        volumeProtocolTv.setVisibility(View.VISIBLE);
        hostProtocolTv.setVisibility(View.GONE);
        assistProtocolTv.setVisibility(View.GONE);
        volumelBottomLl.setVisibility(View.VISIBLE);
        assistBottomLl.setVisibility(View.GONE);
        hostBottomLl.setVisibility(View.GONE);
    }

    /**
     * 合并主客户view显示
     */
    private void showHostProtocolView() {
        protocalTitleTv.setVisibility(View.GONE);
        userNameLl.setVisibility(View.GONE);
        volumeProtocolTv.setVisibility(View.GONE);
        hostProtocolTv.setVisibility(View.VISIBLE);
        assistProtocolTv.setVisibility(View.GONE);
        hostBottomLl.setVisibility(View.VISIBLE);
        volumelBottomLl.setVisibility(View.GONE);
        assistBottomLl.setVisibility(View.GONE);
    }

    /**
     * 合并从客户view显示
     */
    private void showAssistProtocolView() {
        protocalTitleTv.setVisibility(View.GONE);
        userNameLl.setVisibility(View.GONE);
        volumeProtocolTv.setVisibility(View.GONE);
        hostProtocolTv.setVisibility(View.GONE);
        assistProtocolTv.setVisibility(View.VISIBLE);
        assistBottomLl.setVisibility(View.VISIBLE);
        hostBottomLl.setVisibility(View.GONE);
        volumelBottomLl.setVisibility(View.GONE);
    }

    /**
     * 设置从客户view显示数据
     */
    private void setAssistProtocolTv() {
        String loginNames = customerCombinInfor.getTerminalsIdNew();
        String identityTypeNew = customerCombinInfor.getIdentityTypeNew();
        String identityNumNew = customerCombinInfor.getIdentityNumNew();
        identityNumNew = NumberUtils.formatCardFor6Eight4(identityNumNew);
        String[] newLoginNames = loginNames.split("\\|");
        StringBuffer newLoginName = new StringBuffer();
        for (int i = 0; i < newLoginNames.length; i++) {
            String nawLoginNamet = newLoginNames[i].trim();
            newLoginName.append(NumberUtils.formatMobileNumberWithAsterrisk(nawLoginNamet) + "、");
        }
        String loginName = newLoginName.toString().substring(0, newLoginName.length() - 1);
        String assistProtocol0 = mContext.getResources().getString(R.string.boc_protocol_assist0);
        String assistProtocol1 = mContext.getResources().getString(R.string.boc_protocol_assist1);
        String assistProtocol2 = mContext.getResources().getString(R.string.boc_protocol_assist2);
        String assistProtocol3 = mContext.getResources().getString(R.string.boc_protocol_assist3);
        String assistProtocol4 = mContext.getResources().getString(R.string.boc_protocol_assist4);
        String assistProtocol5 = mContext.getResources().getString(R.string.boc_protocol_assist5);

        String identityStr = PublicCodeUtils.getIdentityType(mContext, identityTypeNew) + "（" + "证件号码：" + identityNumNew + "）";
        identityStr = "<font color=\"#ba001d\">" + identityStr + "</font>";
        assistProtocol4 = assistProtocol4.replace("XXX", loginName);

        assistProtocolTv.setText(Html.fromHtml(assistProtocol0 + "<br>" + assistProtocol1
                + identityStr
                + assistProtocol2 + "<br>" + assistProtocol3 + identityStr
                + assistProtocol4 + "<br>" + assistProtocol5));
    }

    @Override
    public boolean onBackPress() {
        return true;
    }
}
