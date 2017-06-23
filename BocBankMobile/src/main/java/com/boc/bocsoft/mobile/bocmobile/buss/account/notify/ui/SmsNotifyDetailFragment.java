package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DeatilsBottomTableButtom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact.SmsNotifyContact;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.SmsNotifyEditModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.SmsNotifyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.presenter.SmsNotifyPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * 短信通知详情界面(修改，关闭)
 * Created by wangtong on 2016/6/15.
 */
public class SmsNotifyDetailFragment extends BussFragment implements SmsNotifyContact.View {

    protected View rootView;
    //用户姓名
    protected DetailTableRow customerName;
    //接收短信的手机号码
    protected DetailTableRow phoneNum;
    //短信通知金额区间
    protected DetailTableRow rangeMoney;
    //签约日期
    protected DetailTableRow signedDate;
    //签约渠道
    protected DetailTableRow signedType;
    //签约机构
    protected DetailTableRow signedAuthor;
    //修改按钮
    protected DeatilsBottomTableButtom modify;
    //关闭按钮
    protected DeatilsBottomTableButtom close;
    //数据模型
    private SmsNotifyModel model;
    //数据处理
    private SmsNotifyPresenter presenter;

    protected TextView fragmentTitle;

    protected ImageView btnBack;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_sms_notify, null);
        return rootView;
    }

    @Override
    public void initView() {
        customerName = (DetailTableRow) rootView.findViewById(R.id.customer_name);
        phoneNum = (DetailTableRow) rootView.findViewById(R.id.phone_num);
        signedDate = (DetailTableRow) rootView.findViewById(R.id.signed_date);
        rangeMoney = (DetailTableRow) rootView.findViewById(R.id.range_money);
        signedType = (DetailTableRow) rootView.findViewById(R.id.signed_type);
        signedAuthor = (DetailTableRow) rootView.findViewById(R.id.signed_author);
        modify = (DeatilsBottomTableButtom) rootView.findViewById(R.id.modify);
        close = (DeatilsBottomTableButtom) rootView.findViewById(R.id.close);
        fragmentTitle = (TextView) rootView.findViewById(R.id.fragment_title);
        btnBack = (ImageView) rootView.findViewById(R.id.btn_back);
    }

    @Override
    public void initData() {
        super.initData();
        fragmentTitle.setText("短信通知");
        model = getArguments().getParcelable(SmsNotifyEditFragment.KEY_MODEL);
        presenter = new SmsNotifyPresenter(this);

        customerName.updateValue(model.getUserName());
        phoneNum.updateValue(NumberUtils.formatMobileNumber(model.getPhoneNum()));
        rangeMoney.updateValue(model.getNotifyMoneyRange());
        signedDate.updateValue(model.getSignedDate());
        signedType.updateValue(model.getSignedType());
        if (TextUtils.isEmpty(model.getSignedAuthor())) {
            signedAuthor.setVisibility(View.GONE);
        } else {
            signedAuthor.updateValue(model.getSignedAuthor());
        }
        modify.updateColor(getResources().getColor(R.color.boc_main_button_color));
        close.updateColor(getResources().getColor(R.color.boc_text_color_red));
    }

    @Override
    public void setListener() {
        super.setListener();

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsNotifyEditFragment fragment = new SmsNotifyEditFragment();
                Bundle bundle = new Bundle();
                SmsNotifyEditModel model = new SmsNotifyEditModel(SmsNotifyDetailFragment.this.model);
                bundle.putParcelable(SmsNotifyEditFragment.KEY_MODEL, model);
                fragment.setArguments(bundle);
                start(fragment);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TitleAndBtnDialog cancelDialog = new TitleAndBtnDialog(getActivity());
                cancelDialog.setBtnName(new String[]{"取消", "确认"});
                cancelDialog.setNoticeContent("确认关闭短信通知？");
                cancelDialog.setGravity(Gravity.CENTER_VERTICAL);
                cancelDialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                        getResources().getColor(R.color.boc_common_cell_color),
                        getResources().getColor(R.color.boc_common_cell_color),
                        getResources().getColor(R.color.boc_text_color_red));
                cancelDialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                        getResources().getColor(R.color.boc_text_color_red),
                        getResources().getColor(R.color.boc_text_color_red),
                        getResources().getColor(R.color.boc_common_cell_color));
                cancelDialog.isShowTitle(false);
                cancelDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                    @Override
                    public void onLeftBtnClick(View view) {
                        cancelDialog.dismiss();
                    }

                    @Override
                    public void onRightBtnClick(View view) {
                        cancelDialog.dismiss();
                        presenter.psnSsmDelete();
                    }
                });
                cancelDialog.setGravity(Gravity.CENTER);
                cancelDialog.show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {
                                           pop();
                                       }
                                   }

        );
    }

    @Override
    public void ssmMessageDeleteReturned() {
        showToast("关闭成功");
        findFragment(AccSmsNotifyHomeFragment.class).refreshSmsList();
        popTo(AccSmsNotifyHomeFragment.class, false);
    }

    @Override
    public void psnSsmDeleteReturned() {
        showToast("关闭成功");
        pop();
    }

    @Override
    public SmsNotifyModel getUiModel() {
        return model;
    }

    @Override
    public void setPresenter(SmsNotifyContact.Presenter presenter) {

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }
}
