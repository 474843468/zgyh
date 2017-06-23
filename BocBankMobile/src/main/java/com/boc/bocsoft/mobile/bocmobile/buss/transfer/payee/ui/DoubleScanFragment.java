package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.TabPageIndicator.MyFragmentAdapter;

/**
 * 双扫描界面：扫描二维码或扫描银行卡
 * Created by zhx on 2016/9/12
 */
public class DoubleScanFragment extends BussFragment {
    private View rootView;
    protected ViewPager viewPager;
    String[] data = new String[]{"com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.ScanCardNumFragment",
            "com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui.QrcodeScanFragment"
    }; // MyFragmentAdapter需要的Fragment数组
    String[] title = new String[]{"拍银行卡", "扫二维码"}; // tab的标题
    private RadioButton btn_scan_bank_card;
    private RadioButton btn_scan_qr_code;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.fragment_double_scan, null);
        return rootView;
    }

    @Override
    public void initView() {
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(new MyFragmentAdapter((BussActivity) getActivity(), data, title));
        btn_scan_bank_card = (RadioButton) rootView.findViewById(R.id.btn_scan_bank_card);
        btn_scan_qr_code = (RadioButton) rootView.findViewById(R.id.btn_scan_qr_code);
    }

    @Override
    public void initData() {
        viewPager.setCurrentItem(0); // 定位到“拍银行卡”
    }

    @Override
    public void setListener() {
        btn_scan_bank_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0); // 定位到“拍银行卡”
            }
        });

        btn_scan_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1); // 定位到“扫二维码”
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: // 拍银行卡
                        btn_scan_bank_card.setChecked(true);
                        btn_scan_qr_code.setChecked(false);
                        break;
                    case 1: // 扫二维码
                        btn_scan_bank_card.setChecked(false);
                        btn_scan_qr_code.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }
}
