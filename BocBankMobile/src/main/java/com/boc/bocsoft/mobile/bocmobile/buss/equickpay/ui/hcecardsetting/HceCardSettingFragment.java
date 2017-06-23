package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardsetting;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.widget.ToggleRowView;

/**
 * Created by yangle on 2016/12/13.
 * 描述:卡设置界面
 */
@Deprecated
public class HceCardSettingFragment extends BussFragment{

    private View mRootView;
    private TextView mTvTips;
    private DetailTableRowButton mTableRowButton;
    private ToggleRowView mToggleRowView;

    public static HceCardSettingFragment newInstance() {
        return new HceCardSettingFragment();
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
       return mRootView =  mInflater.inflate(R.layout.boc_fragment_hce_card_setting, null);
    }

    @Override
    public void initView() {
        super.initView();
        mToggleRowView = (ToggleRowView) mRootView.findViewById(R.id.toggle_view);
        mTableRowButton = (DetailTableRowButton) mRootView.findViewById(R.id.table_row);
        mTableRowButton.setBodyHeight(getResources().getDimensionPixelSize(R.dimen.boc_button_height_104px));
        mTableRowButton.addImgBtn("单笔交易限额","1,000.00元",R.drawable.boc_hce_edit);
        mTvTips = (TextView) mRootView.findViewById(R.id.tv_hint_tips);
    }

    @Override
    public void setListener() {
        super.setListener();
        mTableRowButton.setOnclick(new DetailTableRowButton.BtnCallback() {
            @Override
            public void onClickListener() {
                Toast.makeText(mActivity, "edit===", Toast.LENGTH_SHORT).show();
//                start(HceQuotaSettingFragment.newInstance());
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return "卡号261462646264616";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }


}
